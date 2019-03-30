// ======================================================================== //
// Copyright 2009-2018 Intel Corporation                                    //
//                                                                          //
// Licensed under the Apache License, Version 2.0 (the "License");          //
// you may not use this file except in compliance with the License.         //
// You may obtain a copy of the License at                                  //
//                                                                          //
//     http://www.apache.org/licenses/LICENSE-2.0                           //
//                                                                          //
// Unless required by applicable law or agreed to in writing, software      //
// distributed under the License is distributed on an "AS IS" BASIS,        //
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. //
// See the License for the specific language governing permissions and      //
// limitations under the License.                                           //
// ======================================================================== //

#include "../common/lights/light.h"
#include "../common/math/sampling.h"
#include "../common/math/linearspace.h"
#include "hdrloader.h"

namespace embree {

struct InfiniteLight
{
  Light super;            //!< inherited light fields

  HDRLoaderResult hdr_map;	// lightMap
  float spherePdf;          //!< pdf of sampling the sphere
};


// Implementation
//////////////////////////////////////////////////////////////////////////////

static Vec2f dir2map_coord(Vec3fa dir) {
	Vec2f res;
	float d, r;

	d = sqrtf(dir.x*dir.x + dir.y*dir.y);

	if (fabsf(d) < 1.e-4f) { r = 0.f; }
	else { r = 0.159154943f * acosf(dir.z); }

	res.x = .5f + dir.x * r;
	res.y = .5f + dir.y * r;

	return res;
}

#define R 0
#define G 1
#define B 2

static Vec3fa map_lookup(const InfiniteLight* self, Vec2f map_coord) {
	int x, y, ndx;
	float *pix_addr;

	x = (int)(map_coord.x * self->hdr_map.width);
	y = self->hdr_map.height - (int)(map_coord.y * self->hdr_map.height);  // Radiance maps are -Y
	ndx = (y * self->hdr_map.width + x ) * 3;
	pix_addr = (float *)&self->hdr_map.cols[ndx];
	return Vec3fa(*(pix_addr+R), *(pix_addr + G), *(pix_addr + B));
}


inline static float RGB2Y(const float *RGB) {
	return 0.299f * RGB[R] + 0.587f * RGB[G] + 0.114f * RGB[B];
}

static bool NormalizeToT(const InfiniteLight* self, const float setY) {
	float maxY, Y, *auxPtr;
	int i=0;
	const int numPixels = self->hdr_map.height * self->hdr_map.width;

	
	// do first pixel
	Y = maxY = RGB2Y(self->hdr_map.cols);

	for (i = 1, auxPtr = self->hdr_map.cols + 3; i < numPixels; i++, auxPtr += 3) {
		Y = RGB2Y(auxPtr);
		maxY = (Y > maxY ? Y : maxY);
	}

	const float normC = setY / maxY;

	//printf("NormalizeToT - maxY = %f , normC = %f\n", maxY, normC);

	for (i = 0, auxPtr = self->hdr_map.cols; i < 3*numPixels; i++, auxPtr++) {
		(*auxPtr) *= normC;
	}

	return true;
}

Light_SampleRes InfiniteLight_sample(const Light* super,
                                 const DifferentialGeometry& dg,
                                 const Vec2f& s)
{
  const InfiniteLight* self = (InfiniteLight*)super;
  Light_SampleRes res;
  Vec2f map_coord;
  Vec3fa radiance;

  // verify if the HDR map has been loaded
  if ((self->hdr_map.cols == NULL) || (self->hdr_map.width == 0) || (self->hdr_map.height == 0)) {
	  res.dir = Vec3fa(0.f);
	  res.dist = 0.f;
	  res.pdf = 0.f;
	  res.weight = Vec3fa(0.f);
  }
  else {
	  // uniform sample the sphere to get the direction
	  const float phi = float(two_pi) * s.x;
	  const float cosTheta = 1.f - 2.f * s.y;
	  const float sinTheta = sqrt(1.0f - cosTheta*cosTheta);
	  res.dir = cartesian(phi, sinTheta, cosTheta);
	  map_coord = dir2map_coord(res.dir);
	  radiance = map_lookup(self, map_coord);

	  res.dist = inf;
	  res.pdf = self->spherePdf;
	  res.weight = radiance / res.pdf;
  }

  return res;
}

Light_EvalRes InfiniteLight_eval(const Light* super,
                             const DifferentialGeometry& dg,
                             const Vec3fa& dir)
{
  const InfiniteLight* self = (InfiniteLight*)super;
  Light_EvalRes res;
  Vec2f map_coord;
  res.value = Vec3fa(0.f);
  res.dist = inf;
  res.pdf = 0.f;

  if ((self->hdr_map.cols!=NULL) && (self->hdr_map.width!=0) && (self->hdr_map.height!=0)) {
	  map_coord = dir2map_coord(dir);
	  res.value = map_lookup(self, map_coord);
	  res.pdf = self->spherePdf;
  }

  return res;
}


// Exports (called from C++)
//////////////////////////////////////////////////////////////////////////////

//! Set the parameters of an ispc-side InfiniteLight object
extern "C" void InfiniteLight_set(void* super, const char *HDRfilename)
{

	InfiniteLight* self = (InfiniteLight*)super;
	self->hdr_map.cols = NULL;
	self->hdr_map.height = self->hdr_map.width = 0;
	self->spherePdf = .25f / (float)pi;   // this is 1 / (4* PI) steradians 

	if (HDRfilename) {  // read HDR file
		if (!HDRLoader::load(HDRfilename, self->hdr_map)) {
			self->hdr_map.cols = NULL;
			self->hdr_map.height = self->hdr_map.width = 0;
		}
		//else {
		//	NormalizeToT(self, 8.f); 
		//}
	}
}

//! Create an ispc-side InfiniteLight object
extern "C" void* InfiniteLight_create()
{
	InfiniteLight* self = (InfiniteLight*) alignedMalloc(sizeof(InfiniteLight));

  Light_Constructor(&self->super);
  self->super.sample = InfiniteLight_sample;
  self->super.eval = InfiniteLight_eval;

  InfiniteLight_set(self, NULL);

  return self;
}

} // namespace embree
