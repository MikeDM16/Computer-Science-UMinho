#version 330

uniform	vec4 diffuse;
uniform	vec4 specular;
uniform	float shininess;
uniform float l_coff;
uniform vec4 l_spot_dir;
uniform mat4 m_view; 

in Data {
	vec4 eye;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {

	// set the specular term to black
	vec4 spec = vec4(0.0);

	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e = normalize(vec3(DataIn.eye));
	vec3 l = normalize(DataIn.l_dir);
	
	vec3 l_spot_dirN = normalize(vec3(m_view * (-l_spot_dir)) );
	
	float teta = dot(l_spot_dirN, l);
	float intensity = 0.0;

	if( teta > l_coff ){
		intensity = max(dot(n, l), 0.0);

		// if the vertex is lit compute the specular color
		if (intensity > 0.0) {
			// compute the half vector
			vec3 h = normalize(l + e);	
			// compute the specular intensity
			float intSpec = max(dot(h,n), 0.0);
			// compute the specular term into spec
			spec = specular * pow(intSpec,shininess);
		}
	}
	colorOut = max(intensity *  diffuse + spec, diffuse * 0.25);
}
