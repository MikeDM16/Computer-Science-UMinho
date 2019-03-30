#version 330

uniform sampler2D texture;
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

	float dot = dot(e, n);

	if (dot < 0.2 && dot > -0.2)
		colorOut = vec4(0.0,0.0,0.0,1.0);
	else
		colorOut = vec4(1.0,1.0,1.0,1.0);
}
