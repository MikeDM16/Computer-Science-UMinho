varying vec3 normal;
varying vec3 vert;
varying vec3 camVec;
varying vec3 lightVec;

void main()
{
	vec3 ncamVec = normalize(camVec-vert);
	vec3 nLightVec = normalize(lightVec-vert);
	vec3 nnormal =normalize(normal-vert);

	float shadingV= dot(ncamVec, nnormal);
	float shadingL= dot(nLightVec, nnormal);
	
	//headlight cel shading
	float edge=shadingV <0.5 ? 0.75:1.0;
	edge=shadingV <0.25 ? 0.5: edge;
	edge=shadingV <0.05 ? 0.25: edge;

	//sun cel shading
	float edgeL=shadingL <0.5 ? 0.75:1.0;
	edgeL=shadingL <0.05 ? 0.25: edgeL;

	vec4 finalEdge = vec4(edge)*0.5+vec4(edgeL)*0.5;

	gl_FragColor = vec4(finalEdge.xyz, 0.0);
}