//////////////////////////////////////////////////////////////////////////////////////////
//	PAINTING_Init.cpp
//	Initialise a painting
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include "GL files/glee.h"	//header for opengl versions >1.1 and extensions
#include <GL/glu.h>
#include "Maths/Maths.h"
#include "Log/LOG.h"
#include "Window/WINDOW.h"
#include "Image/IMAGE.h"
#include "GaussianBlur.h"
#include "CalculateGradients.h"
#include "CalculateColorDifference.h"
#include "PAINTING_STYLE.h"
#include "PAINTING.h"

extern PAINTING_STYLE_DATA * paintingStyleData;

bool PAINTING::Init(char * sourceFilename)
{
	//Load the source image
	IMAGE sourceImage;

	if(!sourceImage.Load(sourceFilename))
	{
		LOG::Instance()->OutputError("Unable to load %s", sourceFilename);
		return false;
	}

	if(sourceImage.paletted)
		sourceImage.ExpandPalette();

	//Save the width and height
	width=sourceImage.width;
	height=sourceImage.height;

	glGenTextures(1, &sourceTexture);
	glBindTexture(GL_TEXTURE_RECTANGLE_ARB, sourceTexture);
		
	glTexImage2D(	GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA8, width, height, 0,
					sourceImage.format, GL_UNSIGNED_BYTE, sourceImage.data);
		
	glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_WRAP_S, GL_CLAMP);
	glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_WRAP_T, GL_CLAMP);

	//Check the image will fit on the screen
	if(width>WINDOW::Instance()->width || height>WINDOW::Instance()->height)
	{
		LOG::Instance()->OutputError("Source image is larger than window");
		return false;
	}

	//Create space for temporary sets of data about the image
	
	//Photograph data
	GLubyte * sourceData=new GLubyte[width*height*3];

	//Data we are trying to approximate with a given brush
	GLubyte * referenceData=new GLubyte[width*height*3];

	//The difference between the current painting and the reference
	GLushort * differenceData=new GLushort[width*height];

	//Luminance of each pixel in the reference image
	GLubyte * luminanceData=new GLubyte[width*height];

	//Image gradient at each point (radians)
	float * gradientData=new float[width*height];

	//Final painting
	GLubyte * finalData=new GLubyte[width*height*3];

	//Check all memory allocation was successful
	if(	!sourceData		|| !referenceData	|| !differenceData		||
		!luminanceData	|| !gradientData	|| !finalData)
	{
		LOG::Instance()->OutputError("Unable to allocate space for painting creation");
		return false;
	}

	//Copy the data from the source image to the sourceData array
	for(GLuint y=0; y<height; ++y)
	{
		for(GLuint x=0; x<width; ++x)
		{
			sourceData[(y*width+x)*3  ]=
								sourceImage.data[y*sourceImage.stride+x*sourceImage.bpp/8  ];
			
			sourceData[(y*width+x)*3+1]=
								sourceImage.data[y*sourceImage.stride+x*sourceImage.bpp/8+1];
			
			sourceData[(y*width+x)*3+2]=
								sourceImage.data[y*sourceImage.stride+x*sourceImage.bpp/8+2];
		}
	}

	//Ensure the source data does not contain any (255, 255, 255) colors
	//as this will be used as the initial color of the canvas
	for(GLuint y=0; y<height; ++y)
	{
		for(GLuint x=0; x<width; ++x)
		{
			if(	sourceData[(y*width+x)*3  ]==255 && 
				sourceData[(y*width+x)*3+1]==255 && 
				sourceData[(y*width+x)*3+2]==255)
			{
				sourceData[(y*width+x)*3+2]=254;
			}
		}
	}


	//Loop through the painting styles
	for(int i=0; i<NUM_PAINTING_STYLES; ++i)
	{
		int numBrushes=paintingStyleData[i].numBrushes;
		int * brushRadii=paintingStyleData[i].brushRadii;
		double blurFactor=paintingStyleData[i].blurFactor;
		double gridFactor=paintingStyleData[i].gridFactor;
		int approxThreshold=paintingStyleData[i].approxThreshold;

		//Create the texture object for this painting style
		glGenTextures(1, &textureObjects[i]);
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, textureObjects[i]);
		
		glTexImage2D(	GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA8, width, height, 0,
						GL_RGB, GL_UNSIGNED_BYTE, NULL);
		
		glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_WRAP_T, GL_CLAMP);

		//Paint the canvas bright white
		for(GLuint j=0; j<width*height*3; ++j)
			finalData[j]=255;

		//Update the texture object from the canvas
		glTexSubImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, 0, 0, width, height,
						GL_RGB, GL_UNSIGNED_BYTE, finalData);					

		//Loop through the brushes
		for(int brush=0; brush<numBrushes; ++brush)
		{
			int radius=brushRadii[brush];

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			glColor4fv(white);

			//Draw the painting so far to the screen
			glEnable(GL_TEXTURE_RECTANGLE_ARB);
			glDisable(GL_DEPTH_TEST);

			glBegin(GL_TRIANGLE_STRIP);
			{
				glTexCoord2i(0, 0);
				glVertex2i(0, 0);

				glTexCoord2i(width, 0);
				glVertex2i(width, 0);

				glTexCoord2i(0, height);
				glVertex2i(0, height);

				glTexCoord2i(width, height);
				glVertex2i(width, height);
			}
			glEnd();

			glDisable(GL_TEXTURE_RECTANGLE_ARB);
			glEnable(GL_DEPTH_TEST);

			//Generate the reference image for this brush
			if(!GaussianBlur(sourceData, referenceData, width, height, blurFactor*radius))
				return false;

			//Create the difference image, where each pixel contains the difference between the
			//current painting and the reference image
			for(GLuint j=0; j<width*height; ++j)
			{
				differenceData[j]=CalculateColorDifference(&finalData[j*3], &referenceData[j*3]);

				//If the current image contains (255, 255, 255), let the difference be 65535
				if(	finalData[j*3  ]==255 &&
					finalData[j*3+1]==255 &&
					finalData[j*3+2]==255)
				{
					differenceData[j]=65535;
				}
			}
		
			//Fill the luminanceData with the luminance of the reference image
			for(GLuint j=0; j<width*height; ++j)
			{
				luminanceData[j]=GLubyte(	referenceData[j*3  ]*0.30 +
											referenceData[j*3+1]*0.59 +
											referenceData[j*3+2]*0.11);
			}

			//Calculate the image gradients from the luminance data
			CalculateGradients(luminanceData, gradientData, width, height);

			//Loop through the grid squares to paint in
			int gridSize=int(gridFactor*radius);

			for(GLuint y=0; y<height; y+=gridSize)
			{
				for(GLuint x=0; x<width; x+=gridSize)
				{
					//Find the total error within this grid square
					int areaError=0;

					for(int py=0; py<gridSize; ++py)
						for(int px=0; px<gridSize; ++px)
							areaError+=differenceData[(y+py)*width+(x+px)];

					areaError/=(gridSize*gridSize);

					//If the error in this square is greater than the threshold, paint within
					//this grid square
					if(areaError>approxThreshold)
					{
						int errorX=0, errorY=0;
						int errorDist=65535;
						int largestError=-1;

						//Find the point of largest error within the grid square
						//Favour points near the centre of the square
						for(int py=0; py<gridSize; ++py)
						{
							for(int px=0; px<gridSize; ++px)
							{
								//Find the distance of this pixel from the centre of
								//the grid square
								int distanceFromCentre=	(py-(gridSize-1)/2)*(py-(gridSize-1)/2)+
														(px-(gridSize-1)/2)*(px-(gridSize-1)/2);

								if(	 differenceData[(y+py)*width+(x+px)]>largestError		||
									(differenceData[(y+py)*width+(x+px)]==largestError &&
										distanceFromCentre<errorDist))
								{
									errorX=px;
									errorY=py;
									errorDist=distanceFromCentre;
									largestError=differenceData[(y+py)*width+(x+px)];
								}
							}
						}

						//Draw a brush stroke, starting at this point
						if(!DrawBrushStroke(x+errorX, y+errorY, radius,
											paintingStyleData[i].minStrokeLength,
											paintingStyleData[i].maxStrokeLength,
											paintingStyleData[i].enhanceColors,
											referenceData, finalData, gradientData))
						{
							return false;
						}
					}
				}
			}

			//Update finalData and the texture object
			glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, finalData);
			glCopyTexSubImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, 0, 0, 0, 0, width, height);
		}
	}

	//Free the temporary memory allocated
	if(sourceData)
		delete [] sourceData;
	sourceData=NULL;
	
	if(referenceData)
		delete [] referenceData;
	referenceData=NULL;
	
	if(differenceData)
		delete [] differenceData;
	differenceData=NULL;

	if(luminanceData)
		delete [] luminanceData;
	luminanceData=NULL;
	
	if(gradientData)
		delete [] gradientData;
	gradientData=NULL;
	
	if(finalData)
		delete [] finalData;
	finalData=NULL;

	return true;
}