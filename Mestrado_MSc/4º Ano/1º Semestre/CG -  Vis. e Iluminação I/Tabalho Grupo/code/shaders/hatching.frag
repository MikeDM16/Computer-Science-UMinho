#version 330

uniform sampler2D texEarth, texSpec;
uniform float hatching;

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
	vec3 n  = normalize(DataIn.normal);
	vec3 e  = normalize(vec3(DataIn.eye));
	vec3 ld = normalize(vec3(DataIn.l_dir));

  	// get texture2D color
	vec4 eColor = texture2D(texEarth, DataIn.texCoord);
	float eColorValue = length(eColor.rgb);

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

	vec4 day = (intensity) * eColor + spec + 0.2*eColor;
	float lum = length(day.rgb);
	colorOut = day;
	if (lum < 1.1) 
	{
	  // hatch from left top corner to right bottom
	  if (mod(gl_FragCoord.x + gl_FragCoord.y, hatching) == 0.0) 
	    colorOut = vec4(0,0,0,1);
	}  

	if (lum < 0.8) 
	{
	    // hatch from right top corner to left boottom
	   if (mod(gl_FragCoord.x - gl_FragCoord.y, hatching) == 0.0) 
	    colorOut = vec4(0,0,0,1);
	}  

	if (lum < 0.5) 
	{
	  // hatch from left top to right bottom
	  if (mod(gl_FragCoord.x - gl_FragCoord.y - 10, hatching) == 0.0) 
	   colorOut = vec4(0,0,0,1);
	}


	if (lum < 0.2) 
	{
	  // hatch from right top to right bottom
	  if (mod(gl_FragCoord.x - gl_FragCoord.y - 10, hatching) == 0.0) 
	   colorOut = vec4(0,0,0,1);
	}
}
