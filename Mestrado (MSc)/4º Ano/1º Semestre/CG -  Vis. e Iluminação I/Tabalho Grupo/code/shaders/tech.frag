#version 330

uniform	mat4 m_view;
uniform mat4 m_model_view;

uniform float tech_Color;

uniform	vec4 diffuse;
uniform	vec4 l_dir;	   // global space

in Data {
	vec4 eye;
	vec2 texCoord;
	vec3 normal;
	vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() {
	// transform light to camera space and normalize it
	vec3 ld = normalize( vec3(m_view * -l_dir) );
	vec3 normal = normalize( DataIn.normal );
	float intensity = max(dot(normal, ld), 0.0);

	//vec3 E = normalize(vec3(m_model_view * DataIn.pos));
	vec3 E  = normalize(vec3(DataIn.eye));
	vec3 H = normalize(vec3(ld + E));
	vec4 spec = vec4(pow(dot( H, normal), 28 ));

	vec4 K_coolColor = vec4(0.03,0.134,1,1); // some preaty blue i found
	vec4 K_warmColor = vec4(1,0.14,0,1); // some preaty orange i found	
	vec4 aux = vec4(1,1,1,1);

	if(tech_Color == 1){
		K_coolColor = vec4(0.501, 0.607, 0.639, 1); // some preaty gray i found
		K_warmColor = vec4(0.968, 0.878, 0.094, 1); // some preaty yellowish color i found
		aux = vec4(1, 1, 0,1);
	}
	float alpha = 0.25;  
	float beta = 0.6;

	vec4 K_cool = K_coolColor + aux*alpha;
	vec4 K_warm = K_warmColor + aux*beta; 
	
	float limite = dot(E, normal);

	if (limite < 0.2 && limite > -0.2)
		colorOut = vec4(0.0,0.0,0.0,1.0);
	else
		colorOut = mix(K_cool, K_warm, (intensity)) ;
}