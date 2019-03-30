#version 330

uniform sampler2D texture;
uniform float pixel_height, pixel_width;
uniform vec2 screenSize;

in Data {
  vec4 eye;
  vec2 texCoord;
  vec3 normal;
  vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() { 
	float shininess = 128;
	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e = normalize(vec3(DataIn.eye));

	float dx = pixel_width*(1./screenSize[0]);
	float dy = pixel_height*(1./screenSize[1]);

	vec2 coord = vec2(dx*floor(DataIn.texCoord.x/dx),
	                  dy*floor(DataIn.texCoord.y/dy));

  	// get texture2D color
	vec4 eColor = texture2D(texture, coord);

	float intensity = dot(n,DataIn.l_dir);

	vec4 spec = vec4(0.0);
	if (intensity > 0.0){
		// compute the half vector
		vec3 h = normalize(DataIn.l_dir + e);
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = vec4(1) * pow(intSpec,shininess);
	}

	colorOut = intensity * eColor + spec + 0.2*eColor;
}