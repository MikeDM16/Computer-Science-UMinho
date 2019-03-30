//////////////////////////////////////////////////////////////////////////////////////////
//	GaussianBlur.h
//	Perform a Gaussian blur on image data
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	

#ifndef GAUSSIAN_BLUR_H
#define GAUSSIAN_BLUR_H

//Perform a Gaussian blur with standard deviation sigma
//on sourceImageData of size width x height, putting the result in destImageData
//Assumes row data is tightly packed
bool GaussianBlur(	GLubyte * sourceImageData, GLubyte * destImageData,
					int width, int height, double sigma);

#endif