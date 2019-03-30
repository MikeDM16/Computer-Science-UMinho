//////////////////////////////////////////////////////////////////////////////////////////
//	CalculateGradients.cpp
//	Calculate image gradients from luminance data
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include "Maths/Maths.h"
#include "Log/LOG.h"
#include "GL files/glee.h"
#include "CalculateGradients.h"

void CalculateGradients(const GLubyte * luminanceData, float * gradientData,
						GLuint width, GLuint height)
{
	//Loop through pixels in the image
	for(GLuint y=0; y<height; ++y)
	{
		for(GLuint x=0; x<width; ++x)
		{
			//Save the luminance of the surrounding pixels
			GLubyte xm1ym1=luminanceData[(y==0 ? 1 : y-1)*width+(x==0 ? 1 : x-1)];
			GLubyte xym1=luminanceData[(y==0 ? 1 : y-1)*width+x];
			GLubyte xp1ym1=luminanceData[(y==0 ? 1 : y-1)*width+(x==width-1 ? width-2 : x+1)];

			GLubyte xm1y=luminanceData[y*width+(x==0 ? 1 : x-1)];
			GLubyte xp1y=luminanceData[y*width+(x==width-1 ? width-2 : x+1)];

			GLubyte xm1yp1=luminanceData[(y==height-1 ? height-2 : y+1)*width+(x==0 ? 1 : x-1)];
			GLubyte xyp1=luminanceData[(y==height-1 ? height-2 : y+1)*width+x];
			GLubyte xp1yp1=luminanceData[(y==height-1 ? height-2 : y+1)*width+(x==width-1 ? width-2 : x+1)];

			//Calculate the gradient component in each orientation
			int gx= -1*xm1ym1 -2*xm1y -1*xm1yp1 +1*xp1ym1 +2*xp1y +1*xp1yp1;
			int gy= -1*xm1ym1 -2*xym1 -1*xp1ym1 +1*xm1yp1 +2*xyp1 +1*xp1yp1;

			if(gx==0)
				gx=1;

			//Save the gradient into the array
			gradientData[y*width+x]=atanf(float(gy)/gx);
		}
	}
}


