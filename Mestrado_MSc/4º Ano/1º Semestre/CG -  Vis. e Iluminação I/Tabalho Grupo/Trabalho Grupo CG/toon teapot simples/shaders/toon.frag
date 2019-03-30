#version 330

in Data {
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {

	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);

	vec4 color;

	float intensity = dot(n,DataIn.l_dir);

	if (intensity > 0.95)
		color = vec4(1.0,0.5,0.5,1.0);
	else if (intensity > 0.75)
		color = vec4(0.6,0.3,0.3,1.0);
	else if (intensity > 0.25)
		color = vec4(0.4,0.2,0.2,1.0);
	else
		color = vec4(0.2,0.1,0.1,1.0);

	colorOut = color;
}