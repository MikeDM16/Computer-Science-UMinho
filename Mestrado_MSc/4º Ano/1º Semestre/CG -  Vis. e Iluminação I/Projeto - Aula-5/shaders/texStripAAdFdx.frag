#version 330

uniform vec4 diffuse;

uniform vec4 otherColor;
uniform float div;
uniform float width;
uniform float factor;

uniform	float shininess;
uniform mat4 m_view; 
uniform mat4 m_view_model;
uniform	vec4 l_dir;

in Data {
	vec2 texCoord;
	
	vec4 eye;
	vec3 normal;
} DataIn;

out vec4 outputF;

void main() {
	// set the specular term to black
	float spec = (0.0);
	
	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 e = normalize(vec3(DataIn.eye));
	vec3 l = normalize(vec3(m_view * -l_dir));
	
	float intensity = max(dot(n, l), 0.0);

	if (intensity > 0.0) {
		// compute the half vector
		vec3 h = normalize(l + e);	
		// compute the specular intensity
		float intSpec = max(dot(h,n), 0.0);
		// compute the specular term into spec
		spec = (pow(intSpec,shininess));
	}

	vec4 colorLuz = max(intensity *  diffuse + spec, diffuse * 0.25) ;

	vec2 tc = DataIn.texCoord * div;

	float fr = fract(tc.s);
	
	vec2 deriv = vec2(dFdx(tc.s), dFdy(tc.s));
	float len = length(deriv);
	
	float actualGap = len * factor;
	
	float f = smoothstep(width - actualGap, width, fr) - smoothstep(1.0 - actualGap, 1.0, fr);	
	outputF = mix(diffuse, otherColor, f) + colorLuz;
	//outputF = vec4(len*10);
}