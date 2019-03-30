#version 420
 
layout(triangles) in;
layout (line_strip, max_vertices=6) out;

uniform mat4 m_pvm;

in Data {
	vec3 normal;
	vec3 tangente;
} DataIn[3];

void main()
{
	for(int i = 0; i<3; i++){
		vec3 n = normalize( DataIn[i].normal );
		vec3 t = normalize( DataIn[i].tangente );

		vec3 bitangente = cross(n, t);
		
		// Vertice central v0 
		gl_Position = gl_in[i].gl_Position;
		EmitVertex();

		// Ponto pos + normal
		gl_Position = gl_in[i].gl_Position + vec4(n, 1);
		EmitVertex();
	}
	EndPrimitive();
}

