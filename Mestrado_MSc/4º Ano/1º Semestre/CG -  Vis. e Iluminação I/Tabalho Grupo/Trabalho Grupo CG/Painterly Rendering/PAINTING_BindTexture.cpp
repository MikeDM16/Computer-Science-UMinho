//////////////////////////////////////////////////////////////////////////////////////////
//	PAINTING_BindTexture.cpp
//	Bind the texture for a given style
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
#include "Image/IMAGE.h"
#include "PAINTING_STYLE.h"
#include "PAINTING.h"

void PAINTING::BindSourceTexture()
{
	glBindTexture(GL_TEXTURE_RECTANGLE_ARB, sourceTexture);
}

void PAINTING::BindPaintingTexture(PAINTING_STYLE style)
{
	glBindTexture(GL_TEXTURE_RECTANGLE_ARB, textureObjects[style]);
}
