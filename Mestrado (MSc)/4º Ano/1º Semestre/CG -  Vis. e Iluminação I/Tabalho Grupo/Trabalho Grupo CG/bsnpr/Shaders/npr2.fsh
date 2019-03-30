
uniform sampler2D colormap;
uniform sampler2D hatchmap;
uniform sampler2D sheetmap;
uniform sampler2D shadowhatch;
uniform vec4	hatchOffset;
uniform float   hatchScale;
uniform vec4    pencilColor;
uniform float   edgeSize;

void main()
{
	//perlin noise for edge & color distortion
	vec4 hatch0 = texture2D(hatchmap, gl_TexCoord[0].xy+hatchOffset.xy)*2.0-vec4(1.0);
	hatch0 =hatch0 *hatchScale;

	//sample colors for edge detection
	vec4 s0 = texture2D(colormap, gl_TexCoord[0].xy+hatch0.xy);
	vec4 s02 = texture2D(colormap, gl_TexCoord[0].xy-hatch0.xy*1.5);
	vec4 s1 = texture2D(colormap, gl_TexCoord[0].xy+vec2(0.0, edgeSize)+hatch0.xy);
	vec4 s2 = texture2D(colormap, gl_TexCoord[0].xy+vec2(0.0, -edgeSize)+hatch0.xy);
	vec4 s3 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize,0.0)+hatch0.xy);
	vec4 s4 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize,0.0)+hatch0.xy);

	vec4 s5 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize)+hatch0.xy);
	vec4 s6 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize)+hatch0.xy);
	vec4 s7 = texture2D(colormap, gl_TexCoord[0].xy+vec2(-edgeSize,edgeSize)+hatch0.xy);
	vec4 s8 = texture2D(colormap, gl_TexCoord[0].xy+vec2(edgeSize,-edgeSize)+hatch0.xy);

	//laplace edge detection
	vec3 edge=s1.xyz+s2.xyz+s3.xyz+s4.xyz+s5.xyz+s6.xyz+s7.xyz+s8.xyz+s0.xyz*vec3(-8.0);

	//makes edges white
	float wedge = min(1.0,(edge.x+edge.y+edge.z)*1000.0);
	wedge=max(0.0,wedge);
	
	//sample sheet texture
	vec4 sheet = texture2D(sheetmap, gl_TexCoord[0].xy*4.0);

	//modulate pencil color with edges
	vec4 invpc = vec4(1.0)-pencilColor;
	vec4 pencil = vec4(1.0)-(vec4(wedge)*invpc);

	//sample shadow texture
	vec4 shadow = (vec4(1.0)-texture2D(shadowhatch, (gl_TexCoord[0].xy*4.0)+hatch0.xy));
	shadow *= (1.0-s0.w);

	//combine
	gl_FragColor =pencil*sheet-shadow*(vec4(1.0)-pencilColor);

}