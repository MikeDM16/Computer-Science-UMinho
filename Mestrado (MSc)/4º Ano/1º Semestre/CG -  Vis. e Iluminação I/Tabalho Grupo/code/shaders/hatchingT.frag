#version 330

uniform sampler2D texEarth, hatch1, hatch2, hatch3, hatch4, hatch5, hatch6;

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

    // get texture2D color
	vec4 eColor = texture2D(texEarth, DataIn.texCoord);
	float eColorValue = length(eColor.rgb);

	float intensity = max(0.0, dot(n,DataIn.l_dir));

	vec4 spec = vec4(0.0);
	if (intensity > 0.0){
		// compute the half vector
		vec3 h = normalize(DataIn.l_dir + e);
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = vec4(1) * pow(intSpec,shininess);
	}

	vec4 color = intensity * eColor + 0.2*eColor;

	
	vec3 adjust = vec3(0.3, 0.59, 0.11);
	float lum = dot(color.rgb, adjust);

	float interval = 1.0/6.0;
	vec4 color1, color2, color3;
	float mixFactor;
	
	if (lum < interval) {
		color1 = intensity * texture(hatch6, DataIn.texCoord);
		color2 = intensity * texture(hatch5, DataIn.texCoord);
		mixFactor = smoothstep(-0.9, 0.2, intensity * 6);
	}
	else if (lum < interval * 2) {
		color1 = intensity * texture(hatch5, DataIn.texCoord);
		color2 = intensity * texture(hatch4, DataIn.texCoord);
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*2) * 6);
	}
	else if (lum < interval * 3) {
		color1 = intensity *texture(hatch4, DataIn.texCoord);
		color2 = intensity *texture(hatch3, DataIn.texCoord);
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*3) * 6);
	}
	else if (lum < interval * 4) {
		color1 = intensity *texture(hatch3, DataIn.texCoord);
		color2 = intensity *texture(hatch2, DataIn.texCoord);
		mixFactor = smoothstep(-0.9, 0.2, (intensity -interval*4) * 6);
	}
	else if (lum < interval * 5) {
		color1 = intensity *texture(hatch2, DataIn.texCoord);
		color2 = intensity *texture(hatch1, DataIn.texCoord);
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*5) * 6);
	}
	else {
		color1 = intensity * texture(hatch1, DataIn.texCoord);
		color2 = intensity * vec4(1);
		mixFactor = smoothstep(-0.9, 0.2, (intensity - interval*6) * 6);
	}
	
	colorOut = mix(color1, color2, mixFactor);   
}
