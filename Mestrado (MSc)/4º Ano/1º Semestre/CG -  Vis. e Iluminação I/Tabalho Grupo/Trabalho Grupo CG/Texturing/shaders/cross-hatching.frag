#version 330

uniform sampler2D texEarth, texSpec, texPoke, texWorm;
uniform float shininess=128;
uniform float far = 8;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	/*
	// set the specular term to black
	vec4 spec = vec4(0.0);
	


  	vec3 tc = vec3(1.0, 0.0, 0.0);
  
	float lum = length(texture2D(texEarth, DataIn.texCoord).rgb);
	vec3 l = texture(texEarth, DataIn.texCoord).rgb;
	tc = vec3(1.0, 1.0, 1.0);

	float clm = 4;

	if (lum < 1) 
	{
	  if (mod(gl_FragCoord.x + gl_FragCoord.y, clm) == 0.0) 
	    tc = l;
	}  

	if (lum < 0.7) 
	{
	  if (mod(gl_FragCoord.x - gl_FragCoord.y, clm) == 0.0) 
	    tc = l;
	}  

	if (lum < 0.5) 
	{
	  if (mod(gl_FragCoord.x + gl_FragCoord.y-30, clm) == 0.0) 
	    tc = l;
	}  

	if (lum < 0.3) 
	{
	  if (mod(gl_FragCoord.x - gl_FragCoord.y-30, clm) == 0.0) 
	    tc = l;
	}	  
  
  colorOut = vec4(tc, 1.0);
  */


  // get texture2D color
	vec4 eColor = texture2D(texEarth, DataIn.texCoord);
	float eColorValue = length(texture2D(texEarth, DataIn.texCoord).rgb);

	// only water is specular
	vec4 eSpec = texture2D(texSpec, DataIn.texCoord);
	float eSpecValue = length(texture2D(texSpec, DataIn.texCoord).rgb);


	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e = normalize(vec3(DataIn.eye));

	vec4 spec = vec4(0.0);

	float intensity = dot(n,DataIn.l_dir);

	if (intensity > 0.0){
		// compute the half vector
		vec3 h = normalize(DataIn.l_dir + e);
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = vec4(1) * pow(intSpec,shininess);
	}

	vec4 day = (intensity) * eColor + spec * eSpec + 0.2*eColor;

	float lum = length(day.r);

	if (lum < 1) 
	{
	  if (mod(gl_FragCoord.x + gl_FragCoord.y, far) == 0.0) 
	    colorOut = day;
	}  

	if (lum < 0.7) 
	{
	  if (mod(gl_FragCoord.x - gl_FragCoord.y, far) == 0.0) 
	    colorOut = day;
	}  

	if (lum < 0.5) 
	{
	  if (mod(gl_FragCoord.x + gl_FragCoord.y-10, far) == 0.0) 
	    colorOut = day;
	}  

	if (lum < 0.3) 
	{
	  if (mod(gl_FragCoord.x - gl_FragCoord.y-10, far) == 0.0) 
	   colorOut = day;
	}	

			
}
