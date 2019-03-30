#version 330

uniform sampler2D hatch1, hatch2, hatch3, hatch4, hatch5, hatch6;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	vec4 color1, color2;
	float mixFactor;
	float shininess = 128;
	// gv normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e = normalize(vec3(DataIn.eye));
	// intensity goes from -1 to 1
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
	
	float interval = 1.0/6.0;
	if (intensity < interval) {
		color1 = texture(hatch6, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = texture(hatch5, DataIn.texCoord);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity-interval) * 6);			
	}
	else if (intensity < interval*2) {
		color1 = texture(hatch5, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = texture(hatch4, DataIn.texCoord);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*2) * 6);			
	}
	else if (intensity < interval*3) {
		color1 = texture(hatch4, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = texture(hatch3, DataIn.texCoord);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*3) * 6);	
	}
	else if (intensity < interval*4) {
		color1 = texture(hatch3, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = texture(hatch2, DataIn.texCoord);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*4) * 6);		
	}
	else if (intensity < interval*5) {
		color1 = texture(hatch2, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = texture(hatch1, DataIn.texCoord);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*5) * 6);	
	}
	else {
		color1 = texture(hatch1, DataIn.texCoord);
		color1 = intensity *color1 + spec + 0.25 * color1;
		color2 = vec4(1);
		color2 = intensity *color2 + spec + 0.25 * color2;
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*6) * 6);		
	}
	
	colorOut = mix(color1, color2, mixFactor);
}