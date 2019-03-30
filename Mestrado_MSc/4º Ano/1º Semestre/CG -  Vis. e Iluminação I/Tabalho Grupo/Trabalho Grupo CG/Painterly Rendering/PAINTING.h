//////////////////////////////////////////////////////////////////////////////////////////
//	PAINTING.h
//	Class for the paintings of all styles for a single source image
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	

#ifndef PAINTING_H
#define PAINTING_H

class PAINTING
{
public:
	//Inititate the painting
	bool Init(char * sourceFilename);

	//Bind the source texture
	void BindSourceTexture();

	//Bind the texture for the painting of a given style
	void BindPaintingTexture(PAINTING_STYLE style);

	//Size
	GLuint width, height;

protected:
	//Source image texture object
	GLuint sourceTexture;

	//Texture objects for displaying paintings
	GLuint textureObjects[NUM_PAINTING_STYLES+1];

	//Draw a single brush stroke, given a starting point
	bool DrawBrushStroke(	int startX, int startY, int radius,
							unsigned int minStrokeLength, unsigned int maxStrokeLength,
							bool enhanceColors, const GLubyte * referenceData,
							const GLubyte * currentData, const float * gradientData);
};

#endif	//PAINTING_H