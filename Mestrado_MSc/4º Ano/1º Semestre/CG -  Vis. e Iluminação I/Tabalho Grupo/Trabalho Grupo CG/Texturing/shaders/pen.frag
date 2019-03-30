#version 330

uniform	mat4 m_view;
uniform mat4 m_model_view;

uniform	vec4 diffuse;
uniform vec4 ambient;
uniform	vec4 l_dir;	   // global space

uniform sampler2D texEarth, hatch1, hatch2, colormap, normalmap;

in Data {
  vec4 eye;
  vec2 texCoord;
  vec3 normal;
  vec4 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	// transform light to camera space and normalize it
	vec3 ld = normalize( vec3(m_view * -DataIn.l_dir) );
	vec3 normal = normalize( DataIn.normal );
	float intensity = max(dot(normal, ld), 0.0);

	//vec4 cor = intensity * diffuse + diffuse*ambient; 
	float alpha = 0.2;  
	float beta = 0.9;
	//vec4 K_blue = vec4(0,0.15,0.23,1);
	//vec4 K_yellow = vec4(0,25.06,0,1);
	float K_blue = 0.3;
	float K_yellow = 0.8;
	vec4 K_cool = K_blue + alpha*diffuse;
	vec4 K_warm = K_yellow + beta*diffuse; 

	vec4 cor = ( (1 + intensity)/2 )*K_cool + (1 - ( (1 + intensity)/2) )*K_warm; 
	colorOut = cor;



	//perlin noise for edge & color distortion
	vec4 hatch0 = texture2D(hatch1, DataIn.texCoord.xy)*2.0-vec4(1.0);
	hatch0 = normalize(hatch0)*0.0089;

	
	float edgeSize=0.0019; // = 1/textureSize

	//sample colors for edge detection
	vec4 s0 = texture2D(colormap, DataIn.texCoord.xy+hatch0.xy);
	vec4 s1 = texture2D(colormap, DataIn.texCoord.xy+vec2(0.0, edgeSize)+hatch0.xy);
	vec4 s2 = texture2D(colormap, DataIn.texCoord.xy+vec2(0.0, -edgeSize)+hatch0.xy);
	vec4 s3 = texture2D(colormap, DataIn.texCoord.xy+vec2(-edgeSize,0.0)+hatch0.xy);
	vec4 s4 = texture2D(colormap, DataIn.texCoord.xy+vec2(edgeSize,0.0)+hatch0.xy);

	vec4 s5 = texture2D(colormap, DataIn.texCoord.xy+vec2(edgeSize)+hatch0.xy);
	vec4 s6 = texture2D(colormap, DataIn.texCoord.xy+vec2(-edgeSize)+hatch0.xy);
	vec4 s7 = texture2D(colormap, DataIn.texCoord.xy+vec2(-edgeSize,edgeSize)+hatch0.xy);
	vec4 s8 = texture2D(colormap, DataIn.texCoord.xy+vec2(edgeSize,-edgeSize)+hatch0.xy);

	//extra sample for overlapping of colors
	vec4 s02 = texture2D(colormap, DataIn.texCoord.xy-hatch0.xy*1.5); 

	//laplace edge detection
	vec3 edge=s1.xyz+s2.xyz+s3.xyz+s4.xyz+s5.xyz+s6.xyz+s7.xyz+s8.xyz+s0.xyz*vec3(-8.0);

	//makes edges white
	float wedge = min(1.0,(edge.x+edge.y+edge.z)*1000.0);
	wedge=max(0.0,wedge);

	//sample cel shading
	vec4 inedge = texture2D(normalmap, DataIn.texCoord.xy+hatch0.xy);

	//combine
	vec4 finalCol =inedge*vec4(1.0-wedge)*(s0+s02)*0.5;

	//add shadows (stored in colormap alpha channel)
	colorOut =(1.0-s0.w)*vec4(0.4)*finalCol+finalCol*s0.w;
}