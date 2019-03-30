#version 330

uniform	mat4 m_pvm;

in vec4 position;	// local space
in vec2 texCoord0;

// the data to be sent to the fragment shader
out Data {
	vec2 texCoord;
} DataOut;

void main () {
	DataOut.texCoord = texCoord0;
	gl_Position = m_pvm * position;	
}