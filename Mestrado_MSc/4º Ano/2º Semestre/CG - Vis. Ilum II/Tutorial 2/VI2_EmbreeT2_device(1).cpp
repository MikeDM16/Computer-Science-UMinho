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

	/* scene data */
	RTCDevice g_device = nullptr;
	RTCScene g_scene = nullptr;
	bool g_subdiv_mode = false;

#define SPP 1
#define SPL 1

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

	/* Buffer for progressive rendering */
	Vec3fa *ProgBuffer;
	int ProgBuffer_count;
	RandomSampler *samplers=NULL;

	/*
	 * Protoypes for shading functions: */
	static Vec3fa direct(RTCRay& ray, RayStats& stats, DifferentialGeometry& dg, ISPCOBJMaterial* material, RandomSampler& sampler);
	static Vec3fa Whitted_shade(RTCRay& ray, int depth, RayStats& stats, RandomSampler& sampler);

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

		// Whitted shading
		color = Whitted_shade(ray, 1, stats, sampler);

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

		for (unsigned int y = y0; y < y1; y++) for (unsigned int x = x0; x < x1; x++)
		{
			int sample;
			Vec3fa color(0.f);
			color += renderPixelStandard((float)x, (float)y, camera, g_stats[threadIndex], samplers[taskIndex]);

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
	Vec3fa direct (RTCRay& ray, RayStats& stats, DifferentialGeometry& dg, ISPCOBJMaterial* material, RandomSampler& sampler) {
		const float lightPDF = 1.f / (float)g_ispc_scene->numLights;
		// do direct lighting with shadows: iterate over lights 
		Vec3fa color = Vec3fa(0.f);

		// only do direct if Kd > 0
		if (reduce_max(material->Kd) > 0.f) {
			for (size_t i = 0; i < g_ispc_scene->numLights; i++)
			{
				const Light* l = g_ispc_scene->lights[i];
				Vec3fa l_color = Vec3fa(0.f);
				Light_SampleRes ls = l->sample(l, dg, RandomSampler_get2D(sampler));
				float cos_L_N = dot(ls.dir, dg.Ns);
				if (cos_L_N > 0.f) {
					RTCRay shadowRay = RTCRay(dg.P, ls.dir, 1.e-2f, ls.dist - 1.e-2f, ray.time);
					rtcOccluded(g_scene, shadowRay);
					RayStats_addShadowRay(stats);
					if (shadowRay.geomID) {  // geomID is 0 if geometry found along shadow ray
						l_color += ls.weight * material->Kd * dot(ls.dir, dg.Ns);
					}
				}
				color += l_color;
			}
		}
		return color;
	}

	// Whitted shading
#define MAX_DEPTH 3
	Vec3fa Whitted_shade(RTCRay& ray, int depth, RayStats& stats, RandomSampler& sampler) {
		RTCRay spec_ray;

		// shade background black 
		if (ray.geomID == RTC_INVALID_GEOMETRY_ID) {
			return Vec3fa(0.0f);
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
		dg.Ng = face_forward(ray.dir, normalize(dg.Ng));
		dg.Ns = face_forward(ray.dir, normalize(dg.Ns));

		// defaullt handle non-OBJ materials
		if (g_ispc_scene->materials[materialID]->type != MATERIAL_OBJ) {
			// in order to give some shade to non-OBJ materials
			return (Vec3fa(0.5f) * dot(neg(ray.dir), dg.Ns));
		}


		// do direct lighting no shadows: iterate over lights 
		Vec3fa color = Vec3fa(0.f);
		ISPCOBJMaterial* material = (ISPCOBJMaterial*)g_ispc_scene->materials[materialID];

		color += direct (ray, stats, dg, material, sampler);


		// specular interactions 
		// only do specular reflection if cos >0 (right side of the surface)
		const float cosi = dot(neg(ray.dir), dg.Ns);
		if ((depth < MAX_DEPTH) && (cosi >0.f)) {
			// specular reflection
			// only do specular reflection if Ks > 0  
			if (reduce_max(material->Ks) > 0.f) {

				// specular reflection direction
				const Vec3fa Rdir = 2.0f*cosi*dg.Ns - neg(ray.dir);

				/* initialize ray */
				spec_ray.org = Vec3fa(dg.P);
				spec_ray.dir = Rdir;
				spec_ray.tnear = 1e-2f;
				spec_ray.tfar = inf;
				spec_ray.geomID = RTC_INVALID_GEOMETRY_ID;
				spec_ray.primID = RTC_INVALID_GEOMETRY_ID;
				spec_ray.mask = -1;
				spec_ray.time = 0.f;

				/* intersect ray with scene */
				rtcIntersect(g_scene, spec_ray);

				RayStats_addRay(stats);
				Vec3fa L = Whitted_shade(spec_ray, depth + 1, stats, sampler);

				color += L * material->Ks;
			}
		}
		return color;
	}
	// --------------------------------------------------------------------

	// add quadLights
	static void add_QuadLights(int _nx, int _nz) {
		const float centerX = 280.f, centerZ = 280.f;
		const int nx = (_nx < 1 ? 1 : (_nx > 16 ? 16 : _nx));
		const int nz = (_nz < 1 ? 1 : (_nz > 16 ? 16 : _nz));
		const int totalNewLights = nx * nz;
		const int totalPreviousLights = g_ispc_scene->numLights;
		const Vec3fa lightRad = Vec3fa(70.f);
		const float lightXl = 80.f / (float)nx;
		const float lightZl = 80.f / (float)nz;
		const float lightSpacing = 10.f;
		const int spacesX = nx - 1;
		const int spacesZ = nz - 1;
		float leftX, leftZ;

		// odd nbr lights in X
		if (nx % 2)
		{
			leftX = centerX - ((spacesX >> 1)*lightSpacing + lightXl * ((nx >> 1)+.5f));
		}
		else { // even nbr lights in X
			leftX = centerX - (((spacesX >> 1) + .5f) *lightSpacing + lightXl * (nx >> 1));
		}
		// odd nbr lights in Z
		if (nz % 2)
		{
			leftZ = centerZ - ((spacesZ >> 1)*lightSpacing + lightZl * ((nz >> 1) + .5f));
		}
		else { // even nbr lights in Z
			leftZ = centerZ - (((spacesZ >> 1) + .5f) *lightSpacing + lightZl * (nz >> 1));
		}


		// add quadLight
		Light** newLights = NULL;
		void* myQuadLight;
		newLights = new Light*[totalPreviousLights+totalNewLights];
		// copy previously defined lights
		for (size_t i = 0; i < totalPreviousLights; i++) newLights[i] = g_ispc_scene->lights[i];
		delete[] g_ispc_scene->lights;
		g_ispc_scene->lights = newLights;

		// add new lights
		for (size_t ix = 0; ix < nx; ix++) {
			for (size_t iz = 0; iz < nz; iz++) {
				myQuadLight = QuadLight_create();
				QuadLight_set(myQuadLight, Vec3fa(leftX + ix *(lightXl + lightSpacing), 548.f, leftZ + iz *(lightZl + lightSpacing)), Vec3fa(0.f, 0.f, lightZl),
					Vec3fa(lightXl, 0.f, 0.f), lightRad);
				g_ispc_scene->lights[totalPreviousLights + ix*nz + iz] = (Light *)myQuadLight;
			}
		}
		g_ispc_scene->numLights += totalNewLights;
	}

	Vec3fa old_p;
	unsigned int set_width = 0, set_height = 0;

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
		old_p = Vec3fa(1E10);


	}

	/* called by the C++ code to render */
	extern "C" void device_render(int* pixels,
		const unsigned int width,
		const unsigned int height,
		const float time,
		const ISPCCamera& camera)
	{
		bool camera_changed = g_changed; g_changed = false;

		/* create scene */
		if (g_scene == nullptr) {
			g_scene = convertScene(g_ispc_scene);
			if (g_subdiv_mode) updateEdgeLevels(g_ispc_scene, camera.xfm.p);

			//add_QuadLights(1, 1);

			rtcCommit(g_scene);
			old_p = camera.xfm.p;

			// Create Progressive Buffer
			ProgBuffer = new Vec3fa[width*height];
			camera_changed = true;

			set_width = width;
			set_height = height;
		}

		else
		{
			/* check if camera changed */
			if (ne(camera.xfm.p, old_p)) {
				camera_changed = true;
				old_p = camera.xfm.p;
			}

			if ((set_width != width) || (set_height != height)) {
				set_width = width;
				set_height = height;
				delete[] ProgBuffer;
				ProgBuffer = new Vec3fa[width*height];
				camera_changed = true;
			}

			/* update edge levels if camera changed */
			if (camera_changed && g_subdiv_mode) {
				updateEdgeLevels(g_ispc_scene, camera.xfm.p);
				rtcCommit(g_scene);
			}
		}

		const int numTilesX = (width + TILE_SIZE_X - 1) / TILE_SIZE_X;
		const int numTilesY = (height + TILE_SIZE_Y - 1) / TILE_SIZE_Y;

		if (camera_changed) {
			for (size_t i = 0; i < width*height; i++) ProgBuffer[i] = Vec3fa(0.f);
			ProgBuffer_count = 0;
			if (samplers != NULL) delete[] samplers;
			samplers = new RandomSampler[numTilesX*numTilesY];
			for (int i = 0; i < numTilesX*numTilesY; i++) RandomSampler_init(samplers[i], i);

		}

		// increase count for progressive rendering
		ProgBuffer_count++;

		/* render image */
		parallel_for(size_t(0), size_t(numTilesX*numTilesY), [&](const range<size_t>& range) {
			const int threadIndex = (int)TaskScheduler::threadIndex();
			for (size_t i = range.begin(); i < range.end(); i++)
				renderTileTask((int)i, threadIndex, pixels, width, height, time, camera, numTilesX, numTilesY);
		});
		//rtcDebug();
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
