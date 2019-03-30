//////////////////////////////////////////////////////////////////////////////////////////
//	CONTROL_POINT.h
//	Class to hold the data on a control point for the B spline to represent a brush stroke
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	

#ifndef CONTROL_POINT_H
#define CONTROL_POINT_H

class CONTROL_POINT
{
public:
	int x, y;

	CONTROL_POINT operator+(const CONTROL_POINT & rhs) const
	{	return CONTROL_POINT(x + rhs.x, y + rhs.y);	}

	CONTROL_POINT()
	{}

	CONTROL_POINT(int newX, int newY)	:	x(newX), y(newY)
	{}

	CONTROL_POINT(float newX, float newY)	:	x(int(newX)), y(int(newY))
	{}

	~CONTROL_POINT()
	{}
};

#endif