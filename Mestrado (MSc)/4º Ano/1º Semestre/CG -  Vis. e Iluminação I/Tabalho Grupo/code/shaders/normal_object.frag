#version 330

uniform float hatching;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	float shininess = 128;
		// normalize both input vectors
	vec3 n  = normalize(DataIn.normal);
	vec3 e  = normalize(vec3(DataIn.eye));
	vec3 ld = normalize(vec3(DataIn.l_dir));

  	float intensity = dot(n,DataIn.l_dir);

	vec4 spec = vec4(0.0);
	if (intensity > 0.0){
		// compute the half vector
		vec3 h = normalize(DataIn.l_dir + e);
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = vec4(1) * pow(intSpec,shininess);
	}

	vec4 diffuse = vec4(0.3,0.15,0.5,1);
	vec4 light = intensity*diffuse + 0.5*spec;

	colorOut = light;
}
