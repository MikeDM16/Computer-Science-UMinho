#version 330

uniform sampler2D heightMap;
uniform float scale;

uniform mat4 m_pvm;
uniform mat3 m_normal;
uniform mat4 m_view;
uniform vec4 l_dir;
uniform float timer;

uniform float A = 1, Q = 0.7, w = 0.4, phi = 1;
uniform vec2 D = vec2(0,1);

in vec4 position;
in vec2 texCoord0;

out Data {
	vec3 normal;
	vec3 l_dir;
} DataOut;

void main() {
	vec3 p1, p2, v1, v2;
	float h;

	h = texture(heightMap, texCoord0 - vec2(1.0/1024,0) ).r * scale;
	p1 = vec3(position.x - 1, h, position.z);
	h = texture(heightMap, texCoord0 + vec2(1.0/1024,0) ).r * scale;
	p2 = vec3(position.x + 1, h, position.z);
	v1 = p2 - p1;

	h = texture(heightMap, texCoord0 - vec2(0, 1.0/1024) ).r * scale;
	p1 = vec3(position.x, h, position.z -1);
	h = texture(heightMap, texCoord0 + vec2(0, 1.0/1024) ).r * scale;
	p2 = vec3(position.x, h, position.z + 1);
	v2 = p2 - p1; 

	vec3 n = normalize(cross(v2, v1));
	DataOut.normal = normalize(m_normal * n);
	//DataOut.normal = normalize(m_normal * vec3(0,1,0));	
	DataOut.l_dir = vec3(normalize(- (m_view * l_dir)));

	h = texture(heightMap, texCoord0).r * scale;
	vec4 new_pos = vec4(position.x, h, position.z, 1);

	gl_Position = m_pvm * new_pos;
}