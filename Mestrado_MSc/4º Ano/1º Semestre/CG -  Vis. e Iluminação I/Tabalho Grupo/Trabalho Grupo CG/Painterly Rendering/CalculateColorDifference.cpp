//////////////////////////////////////////////////////////////////////////////////////////
//	CalculateColorDifference.cpp
//	Calculate the difference between 2 colors, described as 3-component GLubyte vectors
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
#include "CalculateColorDifference.h"

GLushort CalculateColorDifference(const GLubyte * c1, const GLubyte * c2)
{
	//Accumulate the squares of the differences of the RGB components,
	//then square root
	GLushort diff=0;

	for(int i=0; i<3; ++i)
		diff+=(c1[i]-c2[i])*(c1[i]-c2[i]);

	return GLushort(sqrt((float)diff));
}
