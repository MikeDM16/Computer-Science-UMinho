#version 330

uniform	mat4 m_pvm;
uniform mat3 m_normal;
uniform	mat4 m_viewModel;

in vec4 position;	// local space
in vec2 texCoord0;	// local space
in vec3 normal;

// the data to be sent to the fragment shader
out Data {
	vec3 normal;
	vec4 position;
	
	vec2 texCoord;
} DataOut;


void main () {
	DataOut.normal = normalize(m_normal * normal);
	DataOut.position = -position;

	// Pass-through the texture coordinates
	DataOut.texCoord = texCoord0;
	// transform the vertex coordinates
	gl_Position = m_pvm * position;	
}