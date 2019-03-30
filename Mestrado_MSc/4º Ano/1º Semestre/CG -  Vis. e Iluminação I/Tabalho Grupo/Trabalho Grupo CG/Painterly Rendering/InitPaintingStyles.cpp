//////////////////////////////////////////////////////////////////////////////////////////
//	InitPaintingStyles.cpp
//	Initialise the painting styles
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#include "Log/LOG.h"
#include "PAINTING_STYLE.h"
#include "InitPaintingStyles.h"

extern PAINTING_STYLE_DATA * paintingStyleData;

bool InitPaintingStyles()
{
	paintingStyleData=new PAINTING_STYLE_DATA[NUM_PAINTING_STYLES];
	if(!paintingStyleData)
	{
		LOG::Instance()->OutputError("Unable to create space for painting style data");
		return false;
	}

	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].approxThreshold=25;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].blurFactor=0.5;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].gridFactor=1.0;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].minStrokeLength=4;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].maxStrokeLength=16;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].numBrushes=3;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].enhanceColors=false;

	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].brushRadii=new int[3];
	if(!paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].brushRadii)
	{
		LOG::Instance()->OutputError("Unable to create space for brush radii");
		return false;
	}

	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].brushRadii[0]=8;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].brushRadii[1]=4;
	paintingStyleData[PAINTING_STYLE_IMPRESSIONIST].brushRadii[2]=2;


	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].approxThreshold=25;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].blurFactor=0.5;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].gridFactor=1.0;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].minStrokeLength=10;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].maxStrokeLength=16;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].numBrushes=3;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].enhanceColors=true;

	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].brushRadii=new int[3];
	if(!paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].brushRadii)
	{
		LOG::Instance()->OutputError("Unable to create space for brush radii");
		return false;
	}

	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].brushRadii[0]=8;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].brushRadii[1]=4;
	paintingStyleData[PAINTING_STYLE_EXPRESSIONIST].brushRadii[2]=2;


	paintingStyleData[PAINTING_STYLE_POINTILLIST].approxThreshold=25;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].blurFactor=0.5;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].gridFactor=0.5;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].minStrokeLength=0;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].maxStrokeLength=0;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].numBrushes=2;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].enhanceColors=false;

	paintingStyleData[PAINTING_STYLE_POINTILLIST].brushRadii=new int[2];
	if(!paintingStyleData[PAINTING_STYLE_POINTILLIST].brushRadii)
	{
		LOG::Instance()->OutputError("Unable to create space for brush radii");
		return false;
	}

	paintingStyleData[PAINTING_STYLE_POINTILLIST].brushRadii[0]=4;
	paintingStyleData[PAINTING_STYLE_POINTILLIST].brushRadii[1]=2;
	
	return true;
}




