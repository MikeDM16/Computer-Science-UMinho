//////////////////////////////////////////////////////////////////////////////////////////
//	PAINTING_BindTexture.cpp
//	Draw a single brush stroke, given a starting point
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <vector>
#include <stdlib.h>
#include "GL files/glee.h"	//header for opengl versions >1.1 and extensions
#include <GL/glu.h>
#include "Maths/Maths.h"
#include "Log/LOG.h"
#include "Image/IMAGE.h"
#include "CalculateColorDifference.h"
#include "CONTROL_POINT.h"
#include "PAINTING_STYLE.h"
#include "PAINTING.h"

extern GLuint circleTexture;

bool PAINTING::DrawBrushStroke(	int startX, int startY, int radius,
								unsigned int minStrokeLength, unsigned int maxStrokeLength,
								bool enhanceColors, const GLubyte * referenceData,
								const GLubyte * currentData, const float * gradientData)
{
	//Get the color of the stroke
	GLubyte strokeColor[3];
	strokeColor[0]=referenceData[(startY*width+startX)*3  ];
	strokeColor[1]=referenceData[(startY*width+startX)*3+1];
	strokeColor[2]=referenceData[(startY*width+startX)*3+2];

	//Enhance the color if necessary
	if(enhanceColors)
	{
		for(int c=0; c<3; ++c)
		{
			float temp=float(strokeColor[c])/255;
			temp=2*temp*temp;

			if(temp>1.0f)
				temp=1.0f;

			strokeColor[c]=GLubyte(255*temp);
		}
	}

	//Set this color
	glColor3ubv(strokeColor);

	//Draw at a random depth in [0, 1]
	float depth=float(rand())/RAND_MAX;



	//A vector to hold the control points of the B-spline
	std::vector <CONTROL_POINT> controlPoints;

	//Add the first control point to the vector
	controlPoints.push_back(CONTROL_POINT(startX, startY));

	//Get the image gradient at this point
	float gradient=gradientData[startY*width+startX];

	//Calculate the 2 possible positions of the next point, ensuring they are within the image
	int nextPoint1X=int(startX+radius*cosf(gradient+(float)M_PI/2));

	if(nextPoint1X<0)
		nextPoint1X=0;

	if(nextPoint1X>(int)(width-1))
		nextPoint1X=width-1;

	int nextPoint1Y=int(startY+radius*sinf(gradient+(float)M_PI/2));
	
	if(nextPoint1Y<0)
		nextPoint1Y=0;

	if(nextPoint1Y>(int)(height-1))
		nextPoint1Y=height-1;
	
	int nextPoint2X=int(startX+radius*cosf(gradient-(float)M_PI/2));

	if(nextPoint2X<0)
		nextPoint2X=0;

	if(nextPoint2X>(int)(width-1))
		nextPoint2X=width-1;

	int nextPoint2Y=int(startY+radius*sinf(gradient-(float)M_PI/2));
	
	if(nextPoint2Y<0)
		nextPoint2Y=0;

	if(nextPoint2Y>(int)(height-1))
		nextPoint2Y=height-1;

	CONTROL_POINT nextPoint1(nextPoint1X, nextPoint1Y);
	CONTROL_POINT nextPoint2(nextPoint2X, nextPoint2Y);

	//Look up the color of the reference image under each point and see which
	//is closest to the stroke color. Add this to the vector
	if(	CalculateColorDifference(	strokeColor,
									&referenceData[nextPoint1.y*width+nextPoint1.x])
		<
		CalculateColorDifference(	strokeColor,
									&referenceData[nextPoint2.y*width+nextPoint2.x]))
	{
		controlPoints.push_back(nextPoint1);
	}
	else
	{
		controlPoints.push_back(nextPoint2);
	}


	//Keep adding control points until one of the following conditions is met:
	// i)  the maximum length is reached
	// ii) the stroke leaves the canvas
	// iii)the difference between the reference image and the stroke color is greater
	//		than the difference between the reference image and the final painting, given
	//		the minimum length has been reached
	for(;;)
	{
		//Save the position of the last control point
		CONTROL_POINT lastControlPoint=controlPoints[controlPoints.size()-1];

		//Check for any terminating condition
		if(controlPoints.size()>=maxStrokeLength)
			break;

		if(	lastControlPoint.x<0				||
			lastControlPoint.x>=(int)(width-1)	||
			lastControlPoint.y<0				||
			lastControlPoint.y>=(int)(height-1))
		{
			break;
		}

		if(	controlPoints.size()>=minStrokeLength &&
			(CalculateColorDifference(
				&referenceData[(lastControlPoint.y*width+lastControlPoint.x)*3],
				strokeColor)) >
			 CalculateColorDifference(
				&referenceData[(lastControlPoint.y*width+lastControlPoint.x)*3],
				&currentData[(lastControlPoint.y*width+lastControlPoint.x)*3]))
		{
			break;
		}
										
			

		//Get the image gradient at the last control point
		float gradient=gradientData[lastControlPoint.y*width + lastControlPoint.x];

		//Calculate the 2 possible positions of the next point
		CONTROL_POINT nextPoint1=lastControlPoint +
									CONTROL_POINT(	radius*cosf(gradient+(float)M_PI/2),
													radius*sinf(gradient+(float)M_PI/2));

		CONTROL_POINT nextPoint2=lastControlPoint +
									CONTROL_POINT(	radius*cosf(gradient-(float)M_PI/2),
													radius*sinf(gradient-(float)M_PI/2));

		//Choose the point which will cause the stroke to curve least, and add this to the vector
		VECTOR2D lastDir(	(float)(lastControlPoint.x-controlPoints[controlPoints.size()-2].x),
							(float)(lastControlPoint.y-controlPoints[controlPoints.size()-2].y));

		lastDir.Normalize();

		VECTOR2D d1(cosf(gradient+(float)M_PI/2), sinf(gradient+(float)M_PI/2));
		VECTOR2D d2(cosf(gradient-(float)M_PI/2), sinf(gradient-(float)M_PI/2));

		if(lastDir.DotProduct(d1)>lastDir.DotProduct(d2))
			controlPoints.push_back(nextPoint1);
		else
			controlPoints.push_back(nextPoint2);
	}



	//Draw the stroke as a cubic B spline if there are at least 4 control points
	int numControlPoints=controlPoints.size();

	//Order of spline
	int order;
	
	if(numControlPoints>=4)
		order=4;
	else
		order=numControlPoints;
	
	//Calculate how many vertices to use when drawing the curve
	int numVertices=(numControlPoints-1)*radius/2;

	//Create an open, uniform knot vector
	std::vector <float> knotVector;
	int knotValue=0;

	for(std::size_t i=0; i<(std::size_t)(numControlPoints+order); ++i)
	{
		if(i <= controlPoints.size() && i >= (std::size_t)order)
			++knotValue;

		knotVector.push_back((float)knotValue/(numControlPoints-order+1));
	}

	//Calculate the value of the basis functions at each vertex
	float * basisFunctions=new float[numVertices*numControlPoints*order];
	if(!basisFunctions)
	{
		LOG::Instance()->OutputError("Unable to create space for basis functions");
		return false;
	}

	//Loop through the vertices
	for(int i=0; i<numVertices; ++i)
	{
		//Calculate the t value at this vertex
		float t=(float)i/(numVertices);

		//Set the first order basis functions for each control point
		for(int j=0; j<numControlPoints; ++j)
		{
			if(t>=knotVector[j] && t<knotVector[j+1])
				basisFunctions[(i*numControlPoints+j)*order]=1.0f;
			else
				basisFunctions[(i*numControlPoints+j)*order]=0.0f;
		}

		//Recursively calculate the higher order functions
		for(int k=1; k<order; ++k)
		{
			for(int j=0; j<numControlPoints; ++j)
			{
				//Retreive the values to use in calculating the functions
				float Njkm1=basisFunctions[(i*numControlPoints+j)*order+k-1];
				float Njp1km1=basisFunctions[(i*numControlPoints+j+1)*order+k-1];

				float xj=knotVector[j];
				float xjpkm1=knotVector[j+k-1+1];
				float xjpk=knotVector[j+k+1];
				float xjp1=knotVector[j+1];

				//calculate the function, watching for divide by zero
				float term1;
				if(fabs(xjpkm1-xj)<EPSILON)
					term1=0.0f;
				else
					term1=((t-xj)*Njkm1)/(xjpkm1-xj);

				float term2;
				if(fabs(xjpk-xjp1)<EPSILON)
					term2=0.0f;
				else
					term2=((xjpk-t)*Njp1km1)/(xjpk-xjp1);

				//Save this value into our array
				basisFunctions[(i*numControlPoints+j)*order+k]=term1+term2;
			}
		}
	}

	//Draw the curve by drawing a circle at each vertex

	//Bind and enable circle texture
	glBindTexture(GL_TEXTURE_2D, circleTexture);
	glEnable(GL_TEXTURE_2D);

	//Use the alpha test to discard fragments outside the circle
	glAlphaFunc(GL_GREATER, 0.5f);
	glEnable(GL_ALPHA_TEST);

	glBegin(GL_QUADS);
	{
		for(int i=0; i<numVertices; ++i)
		{
			//Vertex position
			int x=0, y=0;

			for(int j=0; j<numControlPoints; ++j)
			{
				x+=(int)(controlPoints[j].x*basisFunctions[(i*numControlPoints+j)*order+order-1]);
				y+=(int)(controlPoints[j].y*basisFunctions[(i*numControlPoints+j)*order+order-1]);
			}

			glTexCoord2i(0, 0);
			glVertex3f(float(x-radius), float(y-radius), depth);
			
			glTexCoord2i(1, 0);
			glVertex3f(float(x+radius), float(y-radius), depth);
			
			glTexCoord2i(1, 1);
			glVertex3f(float(x+radius), float(y+radius), depth);
			
			glTexCoord2i(0, 1);
			glVertex3f(float(x-radius), float(y+radius), depth);
		}
	}
	glEnd();

	glDisable(GL_TEXTURE_2D);
	glDisable(GL_ALPHA_TEST);

	if(basisFunctions)
		delete [] basisFunctions;
	basisFunctions=NULL;

	return true;
}
