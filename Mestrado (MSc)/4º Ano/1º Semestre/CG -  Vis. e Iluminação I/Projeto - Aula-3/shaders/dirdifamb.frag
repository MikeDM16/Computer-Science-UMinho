#version 330

uniform	mat4 m_view;
uniform mat4 m_model_view;

uniform	vec4 diffuse;
uniform vec4 ambient;
uniform	vec4 l_dir;	   // global space

in Data {
	vec3 n;
	vec4 pos;
} DataIn;

out vec4 colorOut;

void main() {
	// transform light to camera space and normalize it
	vec3 ld = normalize( vec3(m_view * -l_dir) );
	vec3 normal = normalize( DataIn.n );
	float intensity = max(dot(normal, ld), 0.0);

	vec3 E = normalize(vec3(m_model_view * DataIn.pos));
	vec3 H = normalize(vec3(ld + E));
	vec4 spec = vec4(pow(dot( H, normal), 28 ));

	vec4 cor = intensity * diffuse + diffuse*ambient; 

	colorOut = cor;
}