
uniform sampler2D colormap;
uniform sampler2D normalmap;
uniform sampler2D hatchmap0;
uniform sampler2D hatchmap1;

void main()
{

	
	//perlin noise for edge & color distortion
	vec4 hatch0 = texture2D(hatchmap0, gl_TexCoord[0].xy)*2.0-vec4(1.0);
	hatch0 = normalize(hatch0)*0.0089;


	float edgeSize=0.0019; // = 1/textureSize

	//sample colors for edge detection
	vec4 s0 = texture2D(colormap, gl_TexCoord[0].xy+hatch0.xy);
	vec4 s1 = texture2D(colormap, gl_TexCoord[0].xy+vec2(0.0, edgeSize)+hatch0.xy);
	vec4 s2 = texture2D(colormap, gl_TexCoord[0].xy+vec2(0.0, -edgeSize)+hatch0.xy);
	vec4 s3 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize,0.0)+hatch0.xy);
	vec4 s4 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize,0.0)+hatch0.xy);

	vec4 s5 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize)+hatch0.xy);
	vec4 s6 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize)+hatch0.xy);
	vec4 s7 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize,edgeSize)+hatch0.xy);
	vec4 s8 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize,-edgeSize)+hatch0.xy);

	//extra sample for overlapping of colors
	vec4 s02 = texture2D(colormap, gl_TexCoord[0].xy-hatch0.xy*1.5); 

	//laplace edge detection
	vec3 edge=s1.xyz+s2.xyz+s3.xyz+s4.xyz+s5.xyz+s6.xyz+s7.xyz+s8.xyz+s0.xyz*vec3(-8.0);

	//makes edges white
	float wedge = min(1.0,(edge.x+edge.y+edge.z)*1000.0);
	wedge=max(0.0,wedge);

	//sample cel shading
	vec4 inedge = texture2D(normalmap, gl_TexCoord[0].xy+hatch0.xy);

	//combine
	vec4 finalCol =inedge*vec4(1.0-wedge)*(s0+s02)*0.5;

	//add shadows (stored in colormap alpha channel)
	gl_FragColor =(1.0-s0.w)*vec4(0.4)*finalCol+finalCol*s0.w;

}