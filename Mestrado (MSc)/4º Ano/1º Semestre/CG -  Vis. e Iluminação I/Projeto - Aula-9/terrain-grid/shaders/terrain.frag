#version 330

in Data {
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 color;

void main() {
	
	vec3 n = normalize(DataIn.normal);
	
	float intensity = max(0.0, dot(n, DataIn.l_dir));
	color = intensity * vec4(0.8, 0.6, 0.2, 1.0);
}
	