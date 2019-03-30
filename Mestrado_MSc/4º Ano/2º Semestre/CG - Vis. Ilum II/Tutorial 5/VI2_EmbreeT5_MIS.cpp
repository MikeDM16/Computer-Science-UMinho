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

#include "../common/math/random_sampler.h"
#include "../common/core/differential_geometry.h"
#include "../common/tutorial/tutorial_device.h"
#include "../common/tutorial/scene_device.h"  

namespace embree { 

	extern "C" ISPCScene* g_ispc_scene;
	extern "C" bool g_changed;
	extern "C" int g_instancing_mode;

	int frameID = 0;

	/* scene data */
	RTCDevice g_device = nullptr;
	RTCScene g_scene = nullptr;
	bool g_subdiv_mode = false;

	/* Buffer for progressive rendering */
	Vec3fa *ProgBuffer;
	int ProgBuffer_count;
	RandomSampler *samplers = NULL;

	// ID of the geometry associated with the code added Quad Lights
	int QuadLightID[4] = { -1, -1, -1, -1 };
	Vec3fa QuadLighRad[4];

	// control the sample method
	typedef enum { LIGHT, BRDF, MIS } Sample_Method;

	Sample_Method method = LIGHT;
	// Sample_Method method = BRDF;
	// Sample_Method method = MIS;

#define FIXED_EDGE_TESSELLATION_VALUE 3

#define MAX_EDGE_LEVEL 64.0f 
#define MIN_EDGE_LEVEL  4.0f
#define LEVEL_FACTOR   64.0f

	inline float updateEdgeLevel(ISPCSubdivMesh* mesh, const Vec3fa& cam_pos, const size_t e0, const size_t e1)
	{
		const Vec3fa v0 = mesh->positions[0][mesh->position_indices[e0]];
		const Vec3fa v1 = mesh->positions[0][mesh->position_indices[e1]];
		const Vec3fa edge = v1 - v0;
		const Vec3fa P = 0.5f*(v1 + v0);
		const Vec3fa dist = cam_pos - P;
		return max(min(LEVEL_FACTOR*(0.5f*length(edge) / length(dist)), MAX_EDGE_LEVEL), MIN_EDGE_LEVEL);
	}


	void updateEdgeLevelBuffer(ISPCSubdivMesh* mesh, const Vec3fa& cam_pos, size_t startID, size_t endID)
	{
		for (size_t f = startID; f < endID;f++) {
			unsigned int e = mesh->face_offsets[f];
			unsigned int N = mesh->verticesPerFace[f];
			if (N == 4) /* fast path for quads */
				for (size_t i = 0; i < 4; i++)
					mesh->subdivlevel[e + i] = updateEdgeLevel(mesh, cam_pos, e + (i + 0), e + (i + 1) % 4);
			else if (N == 3) /* fast path for triangles */
				for (size_t i = 0; i < 3; i++)
					mesh->subdivlevel[e + i] = updateEdgeLevel(mesh, cam_pos, e + (i + 0), e + (i + 1) % 3);
			else /* fast path for general polygons */
				for (size_t i = 0; i < N; i++)
					mesh->subdivlevel[e + i] = updateEdgeLevel(mesh, cam_pos, e + (i + 0), e + (i + 1) % N);
		}
	}

#if defined(ISPC)
	void updateSubMeshEdgeLevelBufferTask(int taskIndex, int threadIndex, ISPCSubdivMesh* mesh, const Vec3fa& cam_pos)
	{
		const size_t size = mesh->numFaces;
		const size_t startID = ((taskIndex + 0)*size) / taskCount;
		const size_t endID = ((taskIndex + 1)*size) / taskCount;
		updateEdgeLevelBuffer(mesh, cam_pos, startID, endID);
	}
	void updateMeshEdgeLevelBufferTask(int taskIndex, int threadIndex, ISPCScene* scene_in, const Vec3fa& cam_pos)
	{
		ISPCGeometry* geometry = g_ispc_scene->geometries[taskIndex];
		if (geometry->type != SUBDIV_MESH) return;
		ISPCSubdivMesh* mesh = (ISPCSubdivMesh*)geometry;
		unsigned int geomID = mesh->geom.geomID;
		if (mesh->numFaces < 10000) {
			updateEdgeLevelBuffer(mesh, cam_pos, 0, mesh->numFaces);
			rtcUpdateBuffer(g_scene, mesh->geom.geomID, RTC_LEVEL_BUFFER);
		}
	}
#endif

	void updateEdgeLevels(ISPCScene* scene_in, const Vec3fa& cam_pos)
	{
		/* first update small meshes */
#if defined(ISPC)
		parallel_for(size_t(0), size_t(scene_in->numGeometries), [&](const range<size_t>& range) {
			const int threadIndex = (int)TaskScheduler::threadIndex();
			for (size_t i = range.begin(); i < range.end(); i++)
				updateMeshEdgeLevelBufferTask((int)i, threadIndex, scene_in, cam_pos);
		});
#endif

		/* now update large meshes */
		for (size_t g = 0; g < scene_in->numGeometries; g++)
		{
			ISPCGeometry* geometry = g_ispc_scene->geometries[g];
			if (geometry->type != SUBDIV_MESH) continue;
			ISPCSubdivMesh* mesh = (ISPCSubdivMesh*)geometry;
#if defined(ISPC)
			if (mesh->numFaces < 10000) continue;
			parallel_for(size_t(0), size_t((mesh->numFaces + 4095) / 4096), [&](const range<size_t>& range) {
				const int threadIndex = (int)TaskScheduler::threadIndex();
				for (size_t i = range.begin(); i < range.end(); i++)
					updateSubMeshEdgeLevelBufferTask((int)i, threadIndex, mesh, cam_pos);
			});
#else
			updateEdgeLevelBuffer(mesh, cam_pos, 0, mesh->numFaces);
#endif
			rtcUpdateBuffer(g_scene, mesh->geom.geomID, RTC_LEVEL_BUFFER);
		}
	}

	bool g_use_smooth_normals = false;
	void device_key_pressed_handler(int key)
	{
		if (key == 110 /*n*/) g_use_smooth_normals = !g_use_smooth_normals;
		else if (key == 'l') {  // change sampling method
			switch (method) {
			case LIGHT:
				method = BRDF;
				printf("BRDF sampling\n");
				break;
			case BRDF:
				method = MIS;
				printf("MIS sampling\n");
				break;
			case MIS:
				method = LIGHT;
				printf("LIGHT sampling\n");
				break;
			}
			g_changed = true;  // forces the reset of the progressive rendering buffers
		}
		else device_key_pressed_default(key);
	}

	RTCScene convertScene(ISPCScene* scene_in)
	{
		for (size_t i = 0; i < scene_in->numGeometries; i++)
		{
			ISPCGeometry* geometry = scene_in->geometries[i];
			if (geometry->type == SUBDIV_MESH) {
				g_subdiv_mode = true; break;
			}
		}

		int scene_flags = RTC_SCENE_STATIC | RTC_SCENE_INCOHERENT;
		int scene_aflags = RTC_INTERSECT1 | RTC_INTERPOLATE;
		if (g_subdiv_mode)
			scene_flags = RTC_SCENE_DYNAMIC | RTC_SCENE_INCOHERENT | RTC_SCENE_ROBUST;

		RTCScene scene_out = ConvertScene(g_device, g_ispc_scene, (RTCSceneFlags)scene_flags, (RTCAlgorithmFlags)scene_aflags, RTC_GEOMETRY_STATIC);

		/* commit individual objects in case of instancing */
		if (g_instancing_mode == ISPC_INSTANCING_SCENE_GEOMETRY || g_instancing_mode == ISPC_INSTANCING_SCENE_GROUP)
		{
			for (unsigned int i = 0; i < scene_in->numGeometries; i++) {
				if (scene_in->geomID_to_scene[i]) rtcCommit(scene_in->geomID_to_scene[i]);
			}
		}

		/* commit changes to scene */
		return scene_out;
	}


	void postIntersectGeometry(const RTCRay& ray, DifferentialGeometry& dg, ISPCGeometry* geometry, int& materialID)
	{
		if (geometry->type == TRIANGLE_MESH)
		{
			ISPCTriangleMesh* mesh = (ISPCTriangleMesh*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == QUAD_MESH)
		{
			ISPCQuadMesh* mesh = (ISPCQuadMesh*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == SUBDIV_MESH)
		{
			ISPCSubdivMesh* mesh = (ISPCSubdivMesh*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == LINE_SEGMENTS)
		{
			ISPCLineSegments* mesh = (ISPCLineSegments*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == HAIR_SET)
		{
			ISPCHairSet* mesh = (ISPCHairSet*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == CURVES)
		{
			ISPCHairSet* mesh = (ISPCHairSet*)geometry;
			materialID = mesh->geom.materialID;
		}
		else if (geometry->type == GROUP) {
			unsigned int geomID = ray.geomID; {
				postIntersectGeometry(ray, dg, ((ISPCGroup*)geometry)->geometries[geomID], materialID);
			}
		}
		else
			assert(false);
	}

	AffineSpace3fa calculate_interpolated_space(ISPCInstance* instance, float gtime)
	{
		if (instance->numTimeSteps == 1)
			return AffineSpace3fa(instance->spaces[0]);

		/* calculate time segment itime and fractional time ftime */
		const int time_segments = instance->numTimeSteps - 1;
		const float time = gtime*(float)(time_segments);
		const int itime = clamp((int)(floor(time)), (int)0, time_segments - 1);
		const float ftime = time - (float)(itime);
		return (1.0f - ftime)*AffineSpace3fa(instance->spaces[itime + 0]) + ftime*AffineSpace3fa(instance->spaces[itime + 1]);
	}

	inline int postIntersect(const RTCRay& ray, DifferentialGeometry& dg)
	{
		int materialID = 0;
		unsigned int instID = ray.instID; {
			unsigned int geomID = ray.geomID; {
				ISPCGeometry* geometry = nullptr;
				if (g_instancing_mode == ISPC_INSTANCING_SCENE_GEOMETRY || g_instancing_mode == ISPC_INSTANCING_SCENE_GROUP) {
					ISPCInstance* instance = g_ispc_scene->geomID_to_inst[instID];
					geometry = g_ispc_scene->geometries[instance->geom.geomID];
				}
				else {
					geometry = g_ispc_scene->geometries[geomID];
				}
				postIntersectGeometry(ray, dg, geometry, materialID);
			}
		}

		if (g_instancing_mode != ISPC_INSTANCING_NONE)
		{
			unsigned int instID = ray.instID;
			{
				/* get instance and geometry pointers */
				ISPCInstance* instance = g_ispc_scene->geomID_to_inst[instID];

				/* convert normals */
				//AffineSpace3fa space = (1.0f-ray.time)*AffineSpace3fa(instance->space0) + ray.time*AffineSpace3fa(instance->space1);
				AffineSpace3fa space = calculate_interpolated_space(instance, ray.time);
				dg.Ng = xfmVector(space, dg.Ng);
				dg.Ns = xfmVector(space, dg.Ns);
			}
		}

		return materialID;
	}

	inline Vec3fa face_forward(const Vec3fa& dir, const Vec3fa& _Ng) {
		const Vec3fa Ng = _Ng;
		return dot(dir, Ng) < 0.0f ? Ng : neg(Ng);
	}

	/*
	 * Protoypes for shading functions: */
	static Vec3fa direct(RTCRay& ray, RayStats& stats, DifferentialGeometry& dg, ISPCOBJMaterial* material, RandomSampler& sampler, Vec3fa& Rdir, Light_SampleRes& ls);
	static Vec3fa secondaryRay(RTCRay& ray, RayStats& stats, RandomSampler& sampler);

	/// uniformly distributed sampling of hemisphere oriented along the +z-axis
	inline Vec3fa uniformSampleHemisphere(const Vec2f s)
	{
		const float phi = float(two_pi) * s.x;
		const float cosTheta = s.y;
		const float sinTheta = sqrt(1.0f - cosTheta*cosTheta);
		return cartesian(phi, sinTheta, cosTheta);
	}

	/*! Uniformly distributed hemisphere sampling. Up direction is provided as argument. */
	inline Sample3f uniformSampleHemisphere(const float  u, const float  v, const Vec3fa& N)
	{
		// get a cosine weigthed direction for an hemisphere whose central axis is the Z axis  
		Vec3fa localDir = uniformSampleHemisphere(Vec2f(u, v));
		Sample3f s;
		// rotate the cosine weighted direction to one whose central axis is the normal N
		s.v = frame(N) * localDir;
		// compute the probability with which this direction was selected (constant)
		s.pdf = 1.f / (2.f * float(pi));
		// return the sampled direction and respective probability
		return s;
	}


	/*! Cosine weighted hemisphere sampling. Up direction is provided as argument. */
	inline Sample3f cosineSampleHemisphere(const float  u, const float  v, const Vec3fa& N)
	{
		// get a cosine weigthed direction for an hemisphere whose central axis is the Z axis  
		Vec3fa localDir = cosineSampleHemisphere(Vec2f(u, v));
		Sample3f s;
		// rotate the cosine weighted direction to one whose central axis is the normal N
		s.v = frame(N) * localDir;
		// compute the probability with which this direction was selected
		s.pdf = cosineSampleHemispherePDF(localDir);
		// return the sampled direction and respective probability
		return s;
	}

	/*! importance sample the Phong lobe around R. R is provided as argument. */
	inline Sample3f powerCosineSampleHemisphere(const float  u, const float  v, const Vec3fa& R, const float Ns)
	{
		// get a direction for a lobe whose central axis is the Z axis  
		Vec3fa localDir = powerCosineSampleHemisphere(Ns, Vec2f(u, v));
		Sample3f s;
		// rotate the direction to one whose central axis is R
		s.v = frame(R) * localDir;
		// compute the probability with which this direction was selected
		s.pdf = powerCosineSampleHemispherePDF(localDir, Ns);
		// return the sampled direction and respective probability
		return s;
	}


	/* task that renders a single pixel */
	Vec3fa renderPixelStandard(float x, float y, const ISPCCamera& camera, RayStats& stats, RandomSampler& sampler)
	{

		/* initialize ray */
		RTCRay ray;
		ray.org = Vec3fa(camera.xfm.p);
		ray.dir = Vec3fa(normalize(x*camera.xfm.l.vx + y*camera.xfm.l.vy + camera.xfm.l.vz));
		ray.tnear = 0.0f;
		ray.tfar = inf;
		ray.geomID = RTC_INVALID_GEOMETRY_ID;
		ray.primID = RTC_INVALID_GEOMETRY_ID;
		ray.mask = -1;
		//ray.time = RandomSampler_get1D(sampler);
		ray.time = 0.f;

		/* intersect ray with scene */
		rtcIntersect(g_scene, ray);

		RayStats_addRay(stats);

		/*
		 * shading is performed by the shade() method according to the following prototype:
		 *    color = shade(ray, depth);
		 *
		 * different shade() methods are provided below for the purpose of this tutorial
		 */

		Vec3fa color = Vec3fa(0.f);

		// pathTrace
		color = secondaryRay(ray, stats, sampler);

		return color;
	}

	/* renders a single screen tile */
	void renderTileStandard(int taskIndex,
		int threadIndex,
		int* pixels,
		const unsigned int width,
		const unsigned int height,
		const float time,
		const ISPCCamera& camera,
		const int numTilesX,
		const int numTilesY)
	{
		const int t = taskIndex;
		const unsigned int tileY = t / numTilesX;
		const unsigned int tileX = t - tileY * numTilesX;
		const unsigned int x0 = tileX * TILE_SIZE_X;
		const unsigned int x1 = min(x0 + TILE_SIZE_X, width);
		const unsigned int y0 = tileY * TILE_SIZE_Y;
		const unsigned int y1 = min(y0 + TILE_SIZE_Y, height);

		const float ProgBuffer_countf = (float)ProgBuffer_count;

		for (unsigned int y = y0; y < y1; y++) for (unsigned int x = x0; x < x1; x++)
		{
			Vec3fa color(0.f);

			Vec2f jitter = RandomSampler_get2D(samplers[taskIndex]) - Vec2f(0.5f);
			ProgBuffer[y*width + x] += renderPixelStandard((float)x+jitter.x, (float)y + jitter.y, camera, g_stats[threadIndex], samplers[taskIndex]);
			color = ProgBuffer[y*width + x] / ProgBuffer_countf;

			/* write color to framebuffer */
			unsigned int r = (unsigned int)(255.0f * clamp(color.x, 0.0f, 1.0f));
			unsigned int g = (unsigned int)(255.0f * clamp(color.y, 0.0f, 1.0f));
			unsigned int b = (unsigned int)(255.0f * clamp(color.z, 0.0f, 1.0f));
			pixels[y*width + x] = (b << 16) + (g << 8) + r;
		}
	}

	/* task that renders a single screen tile */
	void renderTileTask(int taskIndex, int threadIndex, int* pixels,
		const unsigned int width,
		const unsigned int height,
		const float time,
		const ISPCCamera& camera,
		const int numTilesX,
		const int numTilesY)
	{
		renderTile(taskIndex, threadIndex, pixels, width, height, time, camera, numTilesX, numTilesY);
	}

	/**************************************/
	/* shading functions */
	/**************************************/

	// direct illumination
	Vec3fa direct(RTCRay& ray, RayStats& stats, DifferentialGeometry& dg, ISPCOBJMaterial* material, RandomSampler& sampler, Vec3fa& Rdir, Light_SampleRes& ls) {
		int i;

		// do direct lighting with shadows: iterate over lights 
		Vec3fa color = Vec3fa(0.f);

		do { // randomly select one light source among the numLights
			i = (int)(RandomSampler_getFloat(sampler) * g_ispc_scene->numLights);
		} while ((i < 0) || (i >= g_ispc_scene->numLights));
		const Light* l = g_ispc_scene->lights[i];
		ls = l->sample(l, dg, RandomSampler_get2D(sampler));

		const float cos_L_N = dot(ls.dir, dg.Ns);
		// light on the right side of reflecting surface ??
		// remember that the normal (Ns) has been flipped to point to the side of te incident ray
		if (cos_L_N > 0.f) {

			RTCRay shadowRay = RTCRay(dg.P, ls.dir, 1.e-2f, ls.dist - 1.e-2f, ray.time);
			rtcOccluded(g_scene, shadowRay);
			RayStats_addShadowRay(stats);

			if (shadowRay.geomID) {  // geomID is 0 if geometry found along shadow ray
				// note that ls.weight already has the pdf factored in
				color = ls.weight * (material->Kd * cos_L_N + material->Ks * powf(dot(ls.dir, Rdir), material->Ns));
				// multiply by the number of ligyt since we are only sampling 1 out of numLights
				// we avoid 2 divisions (computing the pdf and then dividing by it)
				color *= (float)g_ispc_scene->numLights;
			}
		}
		return color;
	}

	// probability of a ray direction given the existing light sources
	float directPDF(Vec3fa& dir, DifferentialGeometry& dg, RandomSampler& sampler) {
		int i;
		float lightPDF=1.f;

		do { // randomly select one light source among the numLights
			i = (int)(RandomSampler_getFloat(sampler)*g_ispc_scene->numLights);
		} while ((i < 0) || (i >= g_ispc_scene->numLights));
		const Light* l = g_ispc_scene->lights[i];

		// factor in the number of light sources
		lightPDF = 1.f / ((float)g_ispc_scene->numLights);

		Light_EvalRes leval = l->eval(l, dg, dir);
		lightPDF *= leval.pdf;

		return lightPDF;
	}

	// secondary ray
	Vec3fa secondaryRay(RTCRay& ray, RayStats& stats, RandomSampler& sampler) {
		RTCRay sec_ray;
		float lightPDF, brdfPDF;
		int l;
		Vec3fa L;

		// shade background black 
		if (ray.geomID == RTC_INVALID_GEOMETRY_ID) {
			return Vec3fa(0.0f);
		}
		// intersected the QuadLights added with add_4QuadLights
		for (l = 0; l < 4; l++) {
			if ((QuadLightID[l] != -1) && (ray.geomID == QuadLightID[l])) {
				return (QuadLighRad[l]);
			}
		}

		// compute differential geometry 
		DifferentialGeometry dg;
		dg.geomID = ray.geomID;
		dg.primID = ray.primID;
		dg.u = ray.u;
		dg.v = ray.v;
		dg.P = ray.org + ray.tfar*ray.dir;
		dg.Ng = ray.Ng;
		dg.Ns = ray.Ng;

		if (g_use_smooth_normals)
			if (ray.geomID != RTC_INVALID_GEOMETRY_ID) // FIXME: workaround for ISPC bug, location reached with empty execution mask
			{
				Vec3fa dPdu, dPdv;
				unsigned int geomID = ray.geomID; {
					rtcInterpolate(g_scene, geomID, ray.primID, ray.u, ray.v, RTC_VERTEX_BUFFER0, nullptr, &dPdu.x, &dPdv.x, 3);
				}
				dg.Ns = cross(dPdv, dPdu);
			}

		int materialID = postIntersect(ray, dg);
		// both shading and geometric normals are flipped (if required)
		// such that they are normalized and point to the same side as the incident ray
		dg.Ng = face_forward(ray.dir, normalize(dg.Ng));
		dg.Ns = face_forward(ray.dir, normalize(dg.Ns));

		// defaullt handle non-OBJ materials
		if (g_ispc_scene->materials[materialID]->type != MATERIAL_OBJ) {
			// in order to give some shade to non-OBJ materials
			return (Vec3fa(0.5f) * dot(neg(ray.dir), dg.Ns));
		}

		Vec3fa color = Vec3fa(0.f);
		ISPCOBJMaterial* material = (ISPCOBJMaterial*)g_ispc_scene->materials[materialID];

		// specular reflection direction
		const float cosi = dot(neg(ray.dir), dg.Ns);
		Vec3fa Rdir = 2.0f*cosi*dg.Ns - neg(ray.dir);

		if (method == LIGHT) {
			Light_SampleRes ls;
			// do not divide color by lightPDF because it has already been factored in within direct()
			L = direct(ray, stats, dg, material, sampler, Rdir, ls);
			color += L;
		}

		else if (method == BRDF) {
			// for this scene materials are either diffuse or glossy; no further options

			if (reduce_max(material->Kd) > 0.f) {
				// get diffuse reflection sample: includes direction + probability
				const Vec2f sample = RandomSampler_get2D(sampler);
				const Sample3f KdSample = cosineSampleHemisphere(sample.x, sample.y, dg.Ns);

				/* initialize ray */
				sec_ray.org = Vec3fa(dg.P);
				sec_ray.dir = KdSample.v;
				sec_ray.tnear = 1e-2f;
				sec_ray.tfar = inf;
				sec_ray.geomID = RTC_INVALID_GEOMETRY_ID;
				sec_ray.primID = RTC_INVALID_GEOMETRY_ID;
				sec_ray.mask = -1;
				sec_ray.time = 0.f;

				/* intersect ray with scene */
				rtcIntersect(g_scene, sec_ray);

				RayStats_addRay(stats);

				L = Vec3fa(0.0f);
				// intersected the QuadLights added with add_4QuadLights
				for (l = 0; l < 4; l++) {
					if ((QuadLightID[l] != -1) && (sec_ray.geomID == QuadLightID[l])) {
						L = QuadLighRad[l];
						break;
					}
				}

				// multiplication by the cosine (sec_ray.dir , dg.Ns) is NOT DONE
				// because the sampling direction is distributed according to this cosine:
				// (p(sec_ray.dir) = cosine (sec_ray.dir , dg.Ns) / pi
				// there is a pi term in the denominator of the pdf, therefore multiply L by it!
				L *= float(pi) * material->Kd;
				//L *= material->Kd * dot(dg.Ns, sec_ray.dir) / KdSample.pdf;
				color += L;
			}
			// specular reflection
			// only do specular reflection if Ks > 0  
			else if (reduce_max(material->Ks) > 0.f) {
				Sample3f KsSample;

				if (material->Ns >= 1.e4f) { // specular ideal
					KsSample.v = Rdir;
					KsSample.pdf = 1.f;
				}
				else {  // glossy
					const Vec2f sample = RandomSampler_get2D(sampler);
					KsSample = powerCosineSampleHemisphere(sample.x, sample.y, Rdir, material->Ns);
				}

				/* initialize ray */
				sec_ray.org = Vec3fa(dg.P);
				sec_ray.dir = KsSample.v;
				sec_ray.tnear = 1e-2f;
				sec_ray.tfar = inf;
				sec_ray.geomID = RTC_INVALID_GEOMETRY_ID;
				sec_ray.primID = RTC_INVALID_GEOMETRY_ID;
				sec_ray.mask = -1;
				sec_ray.time = 0.f;

				/* intersect ray with scene */
				rtcIntersect(g_scene, sec_ray);

				RayStats_addRay(stats);

				L = Vec3fa(0.0f);
				// intersected the QuadLights added with add_4QuadLights
				for (l = 0; l < 4; l++) {
					if ((QuadLightID[l] != -1) && (sec_ray.geomID == QuadLightID[l])) {
						L = QuadLighRad[l];
						break;
					}
				}
				// multiplication by the cos(sec_ray.dir , Rdir)^material->Ns is NOT DONE
				// because the sampling direction is distributed according to this term:
				// (p(sec_ray.dir) = cos(sec_ray.dir , Rdir)^material->Ns * (material->Ns+1) *.5f / pi
				// there is a normalization term in the pdf, therefore divide L by it!
				L *= material->Ks;
	
				if (material->Ns < 1.e4f) { // glossy lobe

					L *= powf(dot(sec_ray.dir, Rdir), material->Ns);
					L /= KsSample.pdf;
					L *= logf(material->Ns);
					// PDF normalization term
					//L *= 2.f * (float)pi / (material->Ns + 1.f);
				}
				color += L;
			} // end specular reflection

		} // end BRDF sampling
		else if (method == MIS) {
			float weight;
			Vec3fa L;
			float which_dist = RandomSampler_get1D(sampler);

			if (which_dist < 0.5f) {  // sample using the light sources
				Light_SampleRes ls;
				// do not divide color by lightPDF because it has already been factored in within direct()
				L = direct(ray, stats, dg, material, sampler, Rdir, ls);

				// compute the probability wit which this direction was found
				lightPDF = ls.pdf;

				// get the probability of this direction if it had been sampled from the BRDF
				if (reduce_max(material->Kd) > 0.f) { // if it is diffuse
					brdfPDF = cosineSampleHemispherePDF(ls.dir);
				}
				else {  // if it is specular
					if (material->Ns >= 1.e4f) { // specular ideal
						brdfPDF = 0.f;
					}
					else {  // glossy
						brdfPDF = powerCosineSampleHemispherePDF(ls.dir, material->Ns);
					}					
				}
				// compute the MIS weight
				// weight = ...
			}
			else { // sample the BRDF
				if (reduce_max(material->Kd) > 0.f) {
					// get diffuse reflection sample: includes direction + probability
					const Vec2f sample = RandomSampler_get2D(sampler);
					const Sample3f KdSample = cosineSampleHemisphere(sample.x, sample.y, dg.Ns);
					brdfPDF = KdSample.pdf;

					/* initialize ray */
					sec_ray.org = Vec3fa(dg.P);
					sec_ray.dir = KdSample.v;
					sec_ray.tnear = 1e-2f;
					sec_ray.tfar = inf;
					sec_ray.geomID = RTC_INVALID_GEOMETRY_ID;
					sec_ray.primID = RTC_INVALID_GEOMETRY_ID;
					sec_ray.mask = -1;
					sec_ray.time = 0.f;

					/* intersect ray with scene */
					rtcIntersect(g_scene, sec_ray);

					RayStats_addRay(stats);
					L = Vec3fa(0.0f);
					// intersected the QuadLights added with add_4QuadLights
					for (l = 0; l < 4; l++) {
						if ((QuadLightID[l] != -1) && (sec_ray.geomID == QuadLightID[l])) {
							L = QuadLighRad[l];
							break;
						}
					}
					// multiplication by the cosine (sec_ray.dir , dg.Ns) is NOT DONE
					// because the sampling direction is distributed according to this cosine:
					// (p(sec_ray.dir) = cosine (sec_ray.dir , dg.Ns) / pi
					// there is a pi term in the denominator of the pdf, therefore multiply L by it!
					L *= float(pi) * material->Kd;
					//L *= material->Kd * dot(dg.Ns, sec_ray.dir) / KdSample.pdf;

				}
				// specular reflection
				// only do specular reflection if Ks > 0  
				else if (reduce_max(material->Ks) > 0.f) {
					Sample3f KsSample;

					if (material->Ns >= 1.e4f) { // specular ideal
						KsSample.v = Rdir;
						KsSample.pdf = 1.f;
					}
					else {  // glossy
						const Vec2f sample = RandomSampler_get2D(sampler);
						KsSample = powerCosineSampleHemisphere(sample.x, sample.y, Rdir, material->Ns);
					}
					brdfPDF = KsSample.pdf ;

					/* initialize ray */
					sec_ray.org = Vec3fa(dg.P);
					sec_ray.dir = KsSample.v;
					sec_ray.tnear = 1e-2f;
					sec_ray.tfar = inf;
					sec_ray.geomID = RTC_INVALID_GEOMETRY_ID;
					sec_ray.primID = RTC_INVALID_GEOMETRY_ID;
					sec_ray.mask = -1;
					sec_ray.time = 0.f;

					/* intersect ray with scene */
					rtcIntersect(g_scene, sec_ray);

					RayStats_addRay(stats);
					L = Vec3fa(0.0f);
					// intersected the QuadLights added with add_4QuadLights
					for (l = 0; l < 4; l++) {
						if ((QuadLightID[l] != -1) && (sec_ray.geomID == QuadLightID[l])) {
							L = QuadLighRad[l];
							break;
						}
					}
					L *= material->Ks;

					if (material->Ns < 1.e4f) { // glossy lobe

						L *= powf(dot(sec_ray.dir, Rdir), material->Ns);
						L /= KsSample.pdf;
						//L *= logf(material->Ns);
						// PDF normalization term
						//L *= 2.f * (float)pi / (material->Ns + 1.f);
					}
				} // end specular reflection

				// what would be the probability density of sampling this same direction according to 
				// the light sources?
				lightPDF = directPDF(sec_ray.dir, dg, sampler);
				// compute the MIS weight
				// weight = ...
			}

			// weight radiance L by the sum of all sampling probabilities
			L *= weight;

			// divide the radiance by the probability with which the particular sampling PDF was selected
			//L /= ...;
			color += L;
		}
		return color;
	}

	// --------------------------------------------------------------------

	typedef struct { int v0, v1, v2, v3; } Quad;

	// add quadLights
	static void add_4QuadLights(void) {
		const int totalNewLights = 4;
		const int totalPreviousLights = g_ispc_scene->numLights;
		Vec3fa edge1, edge2;
		Vertex* vertices;
		Quad* quads;


		// add quadLight
		Light** newLights = NULL;
		void* myQuadLight;
		newLights = new Light*[totalPreviousLights + totalNewLights];
		// copy previously defined lights
		for (size_t i = 0; i < totalPreviousLights; i++) newLights[i] = g_ispc_scene->lights[i];
		delete[] g_ispc_scene->lights;
		g_ispc_scene->lights = newLights;

		// add new lights

		// add quad 0 to the scene
		QuadLightID[0] = rtcNewQuadMesh(g_scene, RTC_GEOMETRY_STATIC, 1, 4);
		// map the vertices buffer
		vertices = (Vertex*)rtcMapBuffer(g_scene, QuadLightID[0], RTC_VERTEX_BUFFER);
		// map the indices buffer
		quads = (Quad*)rtcMapBuffer(g_scene, QuadLightID[0], RTC_INDEX_BUFFER);
		QuadLighRad[0] = Vec3fa(13.f);

		// larger light source
		myQuadLight = QuadLight_create();
		edge1 = Vec3fa(1.5f, 0.f, 0.f);
		edge2 = Vec3fa(0.f, -1.5f, 0.f);
		QuadLight_set(myQuadLight, Vec3fa(3.f, 0.75f, 0.f),
			edge1, edge2, QuadLighRad[0]);
		g_ispc_scene->lights[totalPreviousLights + 0] = (Light *)myQuadLight;
		// set vertices
		vertices[0].x = 3.f;
		vertices[0].y = .75f;
		vertices[0].z = 0.f;
		vertices[1].x = vertices[0].x + edge1.x;
		vertices[1].y = vertices[0].y + edge1.y;
		vertices[1].z = vertices[0].z + edge1.z;
		vertices[2].x = vertices[0].x + edge1.x + edge2.x;
		vertices[2].y = vertices[0].y + edge1.y + edge2.y;
		vertices[2].z = vertices[0].z + edge1.z + edge2.z;
		vertices[3].x = vertices[0].x + edge2.x;
		vertices[3].y = vertices[0].y + edge2.y;
		vertices[3].z = vertices[0].z + edge2.z;
		// set indexes
		quads[0].v0 = 0; quads[0].v1 = 1;quads[0].v2 = 2;quads[0].v3 = 3;
		// end larger light source

		// unmap the vertices buffer
		rtcUnmapBuffer(g_scene, QuadLightID[0], RTC_VERTEX_BUFFER);
		// unmap the indexes buffer
		rtcUnmapBuffer(g_scene, QuadLightID[0], RTC_INDEX_BUFFER);

		// add quad 1 to the scene
		QuadLightID[1] = rtcNewQuadMesh(g_scene, RTC_GEOMETRY_STATIC, 1, 4);
		// map the vertices buffer
		vertices = (Vertex*)rtcMapBuffer(g_scene, QuadLightID[1], RTC_VERTEX_BUFFER);
		// map the indices buffer
		quads = (Quad*)rtcMapBuffer(g_scene, QuadLightID[1], RTC_INDEX_BUFFER);
		QuadLighRad[1] = Vec3fa(60.f);
		// 2nd larger light source
		myQuadLight = QuadLight_create();
		edge1 = Vec3fa(.666f, 0.f, 0.f);
		edge2 = Vec3fa(0.f, -.666f, 0.f);
		QuadLight_set(myQuadLight, Vec3fa(.917f, 0.333f, 0.f),
			edge1, edge2, QuadLighRad[1]);
		g_ispc_scene->lights[totalPreviousLights + 1] = (Light *)myQuadLight;
		// set vertices
		vertices[0].x = .917f;
		vertices[0].y = .333f;
		vertices[0].z = 0.f;
		vertices[1].x = vertices[0].x + edge1.x;
		vertices[1].y = vertices[0].y + edge1.y;
		vertices[1].z = vertices[0].z + edge1.z;
		vertices[2].x = vertices[0].x + edge1.x + edge2.x;
		vertices[2].y = vertices[0].y + edge1.y + edge2.y;
		vertices[2].z = vertices[0].z + edge1.z + edge2.z;
		vertices[3].x = vertices[0].x + edge2.x;
		vertices[3].y = vertices[0].y + edge2.y;
		vertices[3].z = vertices[0].z + edge2.z;
		// set indexes
		quads[0].v0 = 0; quads[0].v1 = 1;quads[0].v2 = 2;quads[0].v3 = 3;
		// end 2nd larger light source

		// unmap the vertices buffer
		rtcUnmapBuffer(g_scene, QuadLightID[1], RTC_VERTEX_BUFFER);
		// unmap the indexes buffer
		rtcUnmapBuffer(g_scene, QuadLightID[1], RTC_INDEX_BUFFER);

		// add quad 2 to the scene
		QuadLightID[2] = rtcNewQuadMesh(g_scene, RTC_GEOMETRY_STATIC, 1, 4);
		// map the vertices buffer
		vertices = (Vertex*)rtcMapBuffer(g_scene, QuadLightID[2], RTC_VERTEX_BUFFER);
		// map the indices buffer
		quads = (Quad*)rtcMapBuffer(g_scene, QuadLightID[2], RTC_INDEX_BUFFER);
		QuadLighRad[2] = Vec3fa(450.f);		
		// 3rd larger light source
		myQuadLight = QuadLight_create();
		edge1 = Vec3fa(.2f, 0.f, 0.f);
		edge2 = Vec3fa(0.f, -.2f, 0.f);
		QuadLight_set(myQuadLight, Vec3fa(-1.35f, 0.1f, 0.f),
			edge1, edge2, QuadLighRad[2]);
		g_ispc_scene->lights[totalPreviousLights + 2] = (Light *)myQuadLight;
		// set vertices
		vertices[0].x = -1.35f;
		vertices[0].y = .1f;
		vertices[0].z = 0.f;
		vertices[1].x = vertices[0].x + edge1.x;
		vertices[1].y = vertices[0].y + edge1.y;
		vertices[1].z = vertices[0].z + edge1.z;
		vertices[2].x = vertices[0].x + edge1.x + edge2.x;
		vertices[2].y = vertices[0].y + edge1.y + edge2.y;
		vertices[2].z = vertices[0].z + edge1.z + edge2.z;
		vertices[3].x = vertices[0].x + edge2.x;
		vertices[3].y = vertices[0].y + edge2.y;
		vertices[3].z = vertices[0].z + edge2.z;
		// set indexes
		quads[0].v0 = 0; quads[0].v1 = 1;quads[0].v2 = 2;quads[0].v3 = 3;
		// end 3rd larger light source
		// unmap the vertices buffer
		rtcUnmapBuffer(g_scene, QuadLightID[2], RTC_VERTEX_BUFFER);
		// unmap the indexes buffer
		rtcUnmapBuffer(g_scene, QuadLightID[2], RTC_INDEX_BUFFER);

		// add quad 3 to the scene
		QuadLightID[3] = rtcNewQuadMesh(g_scene, RTC_GEOMETRY_STATIC, 1, 4);
		// map the vertices buffer
		vertices = (Vertex*)rtcMapBuffer(g_scene, QuadLightID[3], RTC_VERTEX_BUFFER);
		// map the indices buffer
		quads = (Quad*)rtcMapBuffer(g_scene, QuadLightID[3], RTC_INDEX_BUFFER);
		QuadLighRad[3] = Vec3fa(4000.f);	
		// smaller light source
		myQuadLight = QuadLight_create();
		edge1 = Vec3fa(.0667f, 0.f, 0.f);
		edge2 = Vec3fa(0.f, -.0667f, 0.f);
		QuadLight_set(myQuadLight, Vec3fa(-3.783f, 0.0333f, 0.f),
			edge1, edge2, QuadLighRad[3]);
		g_ispc_scene->lights[totalPreviousLights + 3] = (Light *)myQuadLight;
		// set vertices
		vertices[0].x = -3.783f;
		vertices[0].y = .0333f;
		vertices[0].z = 0.f;
		vertices[1].x = vertices[0].x + edge1.x;
		vertices[1].y = vertices[0].y + edge1.y;
		vertices[1].z = vertices[0].z + edge1.z;
		vertices[2].x = vertices[0].x + edge1.x + edge2.x;
		vertices[2].y = vertices[0].y + edge1.y + edge2.y;
		vertices[2].z = vertices[0].z + edge1.z + edge2.z;
		vertices[3].x = vertices[0].x + edge2.x;
		vertices[3].y = vertices[0].y + edge2.y;
		vertices[3].z = vertices[0].z + edge2.z;
		// set indexes
		quads[0].v0 = 0; quads[0].v1 = 1;quads[0].v2 = 2;quads[0].v3 = 3;
		// smaller light source

		// unmap the vertices buffer
		rtcUnmapBuffer(g_scene, QuadLightID[3], RTC_VERTEX_BUFFER);
		// unmap the indexes buffer
		rtcUnmapBuffer(g_scene, QuadLightID[3], RTC_INDEX_BUFFER);

		g_ispc_scene->numLights += totalNewLights;

	}


	Vec3fa old_p, old_vx, old_vy, old_vz;
	int old_viewport_W, old_viewport_H;

	/* called by the C++ code for initialization */
	extern "C" void device_init(char* cfg)
	{
		/* create new Embree device */
		g_device = rtcNewDevice(cfg);
		error_handler(nullptr, rtcDeviceGetError(g_device));

		/* set error handler */
		rtcDeviceSetErrorFunction2(g_device, error_handler, nullptr);

		/* set start render mode */
		renderTile = renderTileStandard;
		key_pressed_handler = device_key_pressed_handler;
		old_vx = old_vy = old_vz = old_p = Vec3fa(0.f);
		old_viewport_W = 0;
		old_viewport_H = 0;

		// create scene
		// imported from OBJ
		g_scene = convertScene(g_ispc_scene);

		// add area lights
		add_4QuadLights();

		rtcCommit(g_scene);

		switch (method) {
		case LIGHT:
			printf("LIGHT sampling!\n");
			break;
		case BRDF:
			printf("BRDF sampling!\n");
			break;
		case MIS:
			printf("MIS sampling!\n");
			break;
		}
	}

	/* called by the C++ code to render */
	extern "C" void device_render(int* pixels,
		const unsigned int width,
		const unsigned int height,
		const float time,
		const ISPCCamera& camera)
	{
		bool camera_changed = g_changed; g_changed = false;
		camera_changed |= ne(old_vx, camera.xfm.l.vx); old_vx = camera.xfm.l.vx;
		camera_changed |= ne(old_vy, camera.xfm.l.vy); old_vy = camera.xfm.l.vy;
		camera_changed |= ne(old_vz, camera.xfm.l.vz); old_vz = camera.xfm.l.vz;
		camera_changed |= ne(old_p, camera.xfm.p);    old_p = camera.xfm.p;
		bool viewport_changed = false;
		viewport_changed |= (old_viewport_W != width); old_viewport_W = width;
		viewport_changed |= (old_viewport_H != height); old_viewport_H = height;

		const int numTilesX = (width + TILE_SIZE_X - 1) / TILE_SIZE_X;
		const int numTilesY = (height + TILE_SIZE_Y - 1) / TILE_SIZE_Y;
		const int numTiles = numTilesX*numTilesY;

		/* 1st frame */
		if (frameID == 0) {

			if (g_subdiv_mode) {
				updateEdgeLevels(g_ispc_scene, camera.xfm.p);
				rtcCommit(g_scene);
			}

			// Create Progressive Buffer
			ProgBuffer = new Vec3fa[width*height];
			for (size_t i = 0; i < width*height; i++) ProgBuffer[i] = Vec3fa(0.f);
			ProgBuffer_count = 0;

			// create and initialize samplers
			samplers = new RandomSampler[numTiles];
			for (int i = 0; i < numTiles; i++) RandomSampler_init(samplers[i], 1323*i);

		}
		else if (viewport_changed) {

			if (g_subdiv_mode) {
				updateEdgeLevels(g_ispc_scene, camera.xfm.p);
				rtcCommit(g_scene);
			}

			// Create Progressive Buffer
			delete[] ProgBuffer;
			ProgBuffer = new Vec3fa[width*height];
			for (size_t i = 0; i < width*height; i++) ProgBuffer[i] = Vec3fa(0.f);
			ProgBuffer_count = 0;

			delete[] samplers;
			samplers = new RandomSampler[numTiles];
			for (int i = 0; i < numTiles; i++) RandomSampler_init(samplers[i], 1323*i);

		}
		else if (camera_changed) {
			if (g_subdiv_mode) {
				updateEdgeLevels(g_ispc_scene, camera.xfm.p);
				rtcCommit(g_scene);
			}

			for (size_t i = 0; i < width*height; i++) ProgBuffer[i] = Vec3fa(0.f);
			ProgBuffer_count = 0;

		}

		// increment frame counter 
		frameID++;
		// increase count for progressive rendering
		ProgBuffer_count++;

		/* render image */
		parallel_for(size_t(0), size_t(numTilesX*numTilesY), [&](const range<size_t>& range) {
			const int threadIndex = (int)TaskScheduler::threadIndex();
			for (size_t i = range.begin(); i < range.end(); i++)
				renderTileTask((int)i, threadIndex, pixels, width, height, time, camera, numTilesX, numTilesY);
		});
	}

	/* called by the C++ code for cleanup */
	extern "C" void device_cleanup()
	{
		rtcDeleteScene(g_scene); g_scene = nullptr;
		rtcDeleteDevice(g_device); g_device = nullptr;
		delete ProgBuffer;
		delete[] samplers;
	}

} // namespace embree
