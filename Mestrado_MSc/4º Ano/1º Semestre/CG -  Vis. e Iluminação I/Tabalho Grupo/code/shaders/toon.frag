#version 330

uniform float toonlevels;

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	vec4 color;
	
	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e  = normalize(vec3(DataIn.eye));
	float intensity = dot(n,DataIn.l_dir);

	if(toonlevels == 1){
		color = vec4(0.0, 1.0, 0.0, 1.0)*intensity;
	}

	if(toonlevels == 2){
		if (intensity > 0.55)
			color = vec4(0.0, 170f/255, 0.0, 1.0);
		else
			color = vec4(0.0, 100f/255, 0.0, 1.0);
	}

	if(toonlevels == 3){
		if (intensity > 0.75)
			color = vec4(193f/255, 1.0, 193f/255, 1.0);
		else if (intensity > 0.30)
			color = vec4(0.0, 150f/255, 0.0, 1.0);
		else
			color = vec4(0.0, 100f/255, 0.0, 1.0);
	}

	if(toonlevels == 4){
		if (intensity > 0.90)
			color = vec4(193f/255, 1.0, 193f/255, 1.0);
		else if (intensity > 0.75)
			color = vec4(50f/255, 200f/255, 50f/255, 1.0);
		else if (intensity > 0.25)
			color = vec4(0.0, 150f/255, 0.0, 1.0);
		else
			color = vec4(0.0, 100f/255, 0.0, 1.0);
	}

	float dot = dot(e, n);

	if (dot < 0.2 && dot > -0.2)
		colorOut = vec4(0.0,0.0,0.0,1.0);
	else
		colorOut = color;
}