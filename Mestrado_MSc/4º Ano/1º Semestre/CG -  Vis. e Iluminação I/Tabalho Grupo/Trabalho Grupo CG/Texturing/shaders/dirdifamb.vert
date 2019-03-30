#version 330

// the matrices
uniform mat4 m_pvm;
uniform	mat3 m_normal;

in vec4 position;	// local space
in vec3 normal;		// local space

// the data to be sent to the fragment shader
out Data {
	vec3 n;
	vec4 pos;
} DataOut;

void main () {
	// transform normal to camera space and normalize it
	DataOut.n = normalize( vec3(m_normal * normal) );
	DataOut.pos = -position; 

	// transform the vertex coordinates
	gl_Position = m_pvm * position;	
}