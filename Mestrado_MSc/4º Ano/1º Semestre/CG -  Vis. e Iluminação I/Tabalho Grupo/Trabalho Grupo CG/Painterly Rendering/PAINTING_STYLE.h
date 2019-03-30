//////////////////////////////////////////////////////////////////////////////////////////
//	PAINTING_STYLE.h
//	Enumerated values for each painting style
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	

#ifndef PAINTING_STYLE_H
#define PAINTING_STYLE_H

enum PAINTING_STYLE
{
	PAINTING_STYLE_IMPRESSIONIST,
	PAINTING_STYLE_EXPRESSIONIST,
	PAINTING_STYLE_POINTILLIST,
	NUM_PAINTING_STYLES
};

class PAINTING_STYLE_DATA
{
public:
	int numBrushes;
	int * brushRadii;
	
	int approxThreshold;
	double blurFactor;
	double gridFactor;

	int minStrokeLength;
	int maxStrokeLength;

	bool enhanceColors;
};	

#endif