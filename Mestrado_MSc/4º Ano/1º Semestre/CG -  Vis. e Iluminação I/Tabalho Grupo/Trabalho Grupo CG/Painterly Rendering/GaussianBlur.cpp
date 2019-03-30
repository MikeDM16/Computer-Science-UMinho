//////////////////////////////////////////////////////////////////////////////////////////
//	GaussianBlur.cpp
//	Perform a Gaussian blur on image data
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
#include "GaussianBlur.h"

//Perform a Gaussian blur with standard deviation sigma
//on sourceImageData of size width x height, putting the result in destImageData
//Assumes row data is tightly packed
bool GaussianBlur(	GLubyte * sourceImageData, GLubyte * destImageData,
					int width, int height, double sigma)
{
	//Create space to hold a temporary image
	GLubyte * tempImageData=new GLubyte[width*height*3];
	if(!tempImageData)
	{
		LOG::Instance()->OutputError("Unable to create space for %d x %d temporary image",
										width, height);
		return false;
	}

	//Create space to hold the (1D) kernel values
	//Ignore values outside 3 standard deviations
	int kernelRadius=int(3*sigma);
	int kernelSize=kernelRadius*2+1;

	double * kernel=new double[kernelSize];
	if(!kernel)
	{
		LOG::Instance()->OutputError("Unable to create space for Gaussian kernel");
		return false;
	}

	//Fill the kernel values
	for(int x=-kernelRadius; x<=kernelRadius; ++x)
		kernel[x+kernelRadius]=exp(-(x*x)/(2*sigma*sigma))/(sqrt(2*M_PI)*sigma);

	//Normalize the values
	double kernelTotal=0.0f;

	for(int i=0; i<kernelSize; ++i)
		kernelTotal+=kernel[i];

	for(int i=0; i<kernelSize; ++i)
		kernel[i]/=kernelTotal;

	//Apply the kernel in the x direction, saving the data in tempImageData
	//Loop through pixels
	for(int y=0; y<height; ++y)
	{
		for(int x=0; x<width; ++x)
		{
			//Loop through components
			for(int c=0; c<3; ++c)
			{
				//Keep a running total of the pixel's new value
				double newValue=0.0;

				//Loop through values in kernel
				for(int kx=-kernelRadius; kx<=kernelRadius; ++kx)
				{
					//Calculate the pixel column to read
					int pixelColumn=x+kx;

					//If the pixel is outside the image, reflect about the edge
					if(pixelColumn<0)
						pixelColumn=-pixelColumn-1;

					if(pixelColumn>=width)
						pixelColumn=width-(pixelColumn-width)-1;
					
					//Add kernel value*pixel value to the new value
					newValue+=kernel[kx+kernelRadius]*sourceImageData[(y*width+pixelColumn)*3+c];
				}

				//Save the value into the temporary image data
				tempImageData[(y*width+x)*3+c]=GLubyte(newValue);
			}
		}
	}

	//Now apply the kernel in the y direction to tempImageData
	//Loop through pixels
	for(int y=0; y<height; ++y)
	{
		for(int x=0; x<width; ++x)
		{
			//Loop through components
			for(int c=0; c<3; ++c)
			{
				//Keep a running total of the pixel's new value
				double newValue=0.0;

				//Loop through values in kernel
				for(int kx=-kernelRadius; kx<=kernelRadius; ++kx)
				{
					//Calculate the pixel row to read
					int pixelRow=y+kx;

					//If the pixel is outside the image, reflect about the edge
					if(pixelRow<0)
						pixelRow=-pixelRow-1;

					if(pixelRow>=height)
						pixelRow=height-(pixelRow-height)-1;
					
					//Add kernel value*pixel value to the new value
					newValue+=kernel[kx+kernelRadius]*tempImageData[(pixelRow*width+x)*3+c];
				}

				//Save the value into the destination image data
				destImageData[(y*width+x)*3+c]=GLubyte(newValue);
			}
		}
	}

	//Delete temporary memory
	if(tempImageData)
		delete [] tempImageData;
	tempImageData=NULL;
	
	if(kernel)
		delete [] kernel;
	kernel=NULL;

	return true;
}


