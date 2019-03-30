#version 330

uniform sampler2D texDay, textSpec, texNight, texClouds;
uniform float shininess = 100;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {

	// get texture color
	vec4 dColor = texture(texDay, DataIn.texCoord);
	vec4 gloss = texture(textSpec, DataIn.texCoord);
	vec4 nColor = texture(texNight, DataIn.texCoord);
	vec4 nuvens = texture(texClouds, DataIn.texCoord);

	// set the specular term to black
	vec4 corDia, corNoite, spec = vec4(0.0);

	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 l = normalize(DataIn.l_dir);
	vec3 e = normalize(vec3(DataIn.eye));

	float intensity = max(dot(n, l), 0.0);

	// if the vertex is lit compute the specular color
	if (intensity > 0.0) {
		// compute the half vector
		vec3 h = normalize(DataIn.l_dir + e);	
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = vec4(1.0) * pow(intSpec,shininess);
	}
	
	corDia = nuvens * intensity + (1-nuvens) * intensity* dColor + gloss * spec;
	corNoite = (1-nuvens) * nColor;

	if (intensity > 0.1)
		colorOut = corDia;
	else if(intensity > 0.0)
			colorOut = mix(nColor, 0.1f * nuvens + (1-nuvens) * dColor + gloss * spec, intensity*10.0f);
		else
			colorOut = corNoite;

}