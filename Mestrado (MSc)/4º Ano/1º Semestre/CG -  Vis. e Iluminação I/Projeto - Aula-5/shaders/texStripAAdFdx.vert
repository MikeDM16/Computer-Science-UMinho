#version 330

uniform	mat4 m_pvm;
uniform mat3 m_normal;
uniform	mat4 m_viewModel;
uniform	mat4 m_view;

uniform vec4 l_pos;

in vec4 position;	// local space
in vec3 normal;
in vec2 texCoord0;	// local space

// the data to be sent to the fragment shader
out Data {
	vec2 texCoord;

	vec4 eye;
	vec3 normal;
} DataOut;


void main () {
	DataOut.normal = normalize(m_normal * normal);
	DataOut.eye = -(m_viewModel * position);

	// Pass-through the texture coordinates
	DataOut.texCoord = texCoord0;
	// transform the vertex coordinates
	gl_Position = m_pvm * position;	
}