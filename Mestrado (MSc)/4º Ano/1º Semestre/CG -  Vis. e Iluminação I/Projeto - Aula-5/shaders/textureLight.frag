#version 330

uniform vec4 diffuse;
uniform	float shininess;
uniform mat4 m_viewModel;
uniform	vec4 l_dir;	   // global space

uniform vec4 otherColor;
uniform float div;
uniform float width;
uniform float factor;

in Data {
	vec3 normal;
	vec4 position;

	vec2 texCoord;
} DataIn;

out vec4 outputF;

void main() {
	// set the specular term to black
	vec4 spec = vec4(0.0);

	// normalize both input vectors
	vec3 n = normalize(DataIn.normal);
	vec3 l = normalize( vec3(m_viewModel * l_dir) );
	float intensity = max( dot(n, l), 0.0);

	vec3 E = normalize(vec3(m_viewModel * DataIn.position));
	vec3 H = normalize(vec3(l + E));
	spec = vec4(pow(dot( H, n), shininess ));

	vec2 tc = DataIn.texCoord * div;

	float fr = fract(tc.s);
	
	vec2 deriv = vec2(dFdx(tc.s), dFdy(tc.s));
	float len = length(deriv);
	
	float actualGap = len * factor;
	
	float f = smoothstep(width - actualGap, width, fr) - smoothstep(1.0 - actualGap, 1.0, fr);	

	//outputF = mix(diffuse, otherColor, f) +  max(intensity *  diffuse + spec, diffuse * 0.25);
	vec4 corText = vec4(mix(diffuse, otherColor, f)); 
	vec4 corLuz = max(intensity *  diffuse + spec, diffuse * 0.25);
	outputF = corText + corLuz;
	//outputF = vec4(len*10);
}