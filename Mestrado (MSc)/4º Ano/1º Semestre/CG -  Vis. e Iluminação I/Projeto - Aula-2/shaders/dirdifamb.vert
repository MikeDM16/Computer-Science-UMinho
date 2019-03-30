#version 330

// the matrices
uniform mat4 m_pvm;
uniform	mat4 m_view;
uniform	mat3 m_normal;

uniform	vec4 diffuse;
uniform	vec4 ambient;

uniform	vec4 l_dir;	   // global space

in vec4 position;	// local space
in vec3 normal;		// local space

// the data to be sent to the fragment shader
out Data {
	vec4 color;
} DataOut;

void main () {
	// transform normal to camera space and normalize it
	vec3 n = normalize(m_normal * normal);
	// transform light to camera space and normalize it
	vec3 ld = normalize(vec3(m_view * -l_dir));

	// compute the intensity as the dot product
	// the max prevents negative intensity values
	float intensity = max(dot(n, ld), 0.0);

	if(intensity > 0.9)
		// compute the color as the maximum between the two components
		//nao usar a ambiente pq essa é sempre preta!
		DataOut.color = 0.9 * diffuse;
		//DataOut.color = vec4(1,1,1,1);
	else if (intensity > 0.5)
		DataOut.color = 0.6 * diffuse;
		//DataOut.color =	vec4(0.9,0.9,0.9,1);
	else if (intensity > 0.3)
		DataOut.color = 0.3 * diffuse;
		//DataOut.color =	vec4(0.6,0.6,0.6,1);
	else 
		DataOut.color = 0.1 * diffuse;
		//DataOut.color =	vec4(0.2,0.2,0.2,1);
	
	// transform the vertex coordinates
	gl_Position = m_pvm * position;	
}