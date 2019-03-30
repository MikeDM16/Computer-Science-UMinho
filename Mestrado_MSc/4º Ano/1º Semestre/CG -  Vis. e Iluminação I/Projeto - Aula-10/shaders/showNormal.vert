#version 420

in vec4 position;
in vec3 normal;
in vec3 tangente;
uniform mat3 m_normal;
uniform mat4 m_pvm;

out Data {
	vec3 tangente;
	vec3 normal;
} DataOut;

void main()
{
	DataOut.normal =  normalize(m_normal * normal);
	DataOut.tangente = normalize(m_normal * tangente);

	gl_Position = m_pvm * position ;
} 
