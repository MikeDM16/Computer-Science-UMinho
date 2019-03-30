
uniform sampler2D normalMap;

varying vec4 lightVec;
varying vec4 camVec;
void main()
{
	vec4 normal = texture2D(normalMap, gl_TexCoord[1].xy*0.25);
	normal = normal*2.0-vec4(1.0);

	vec4 normal2 = texture2D(normalMap, gl_TexCoord[0].xy);
	normal2 = normal2*2.0-vec4(1.0);
	
	//average normals
	vec4 normal3 = (normal+normal2)*0.5;
	vec3 nnormal = normalize(normal3.xzy);

	vec3 ncamVec = normalize(camVec.xyz);
	vec3 nLightVec = normalize(lightVec.xyz);

	//headlight cel shading
	float edge=dot(ncamVec,nnormal) <0.5 ? 0.75:1.0;
	edge=dot(ncamVec,nnormal) <0.25 ? 0.5: edge;
	edge=dot(ncamVec,nnormal) <0.05 ? 0.25: edge;

	//sun cel shading
	float edgeL=dot(nLightVec,nnormal) <0.5 ? 0.75:1.0;
	edgeL=dot(nLightVec,nnormal) <0.25 ? 0.5: edge;
	edgeL=dot(nLightVec,nnormal) <0.05 ? 0.25: edge;

	vec4 finalEdge = vec4(edge)*0.5+vec4(edgeL)*0.5;

	gl_FragColor = vec4(finalEdge.xyz, 0.0);

}