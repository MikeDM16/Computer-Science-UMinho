//////////////////////////////////////////////////////////////////////////////////////////
//	CalculateGradients.h
//	Calculate image gradients from luminance data
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	

#ifndef CALCULATE_GRADIENTS_H
#define CALCULATE_GRADIENTS_H

void CalculateGradients(const GLubyte * luminanceData, float * gradientData,
						GLuint width, GLuint height);

#endif