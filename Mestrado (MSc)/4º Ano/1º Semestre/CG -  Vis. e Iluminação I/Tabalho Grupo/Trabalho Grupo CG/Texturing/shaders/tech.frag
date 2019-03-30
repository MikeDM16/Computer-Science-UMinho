#version 330

uniform	mat4 m_view;
uniform mat4 m_model_view;

uniform	vec4 diffuse;
uniform vec4 ambient;
uniform	vec4 l_dir;	   // global space

in Data {
	vec3 n;
	vec4 pos;
} DataIn;

out vec4 colorOut;

void main() {
	// transform light to camera space and normalize it
	vec3 ld = normalize( vec3(m_view * -l_dir) );
	vec3 normal = normalize( DataIn.n );
	float intensity = max(dot(normal, ld), 0.0);

	vec3 E = normalize(vec3(m_model_view * DataIn.pos));
	vec3 H = normalize(vec3(ld + E));
	vec4 spec = vec4(pow(dot( H, normal), 28 ));

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
}