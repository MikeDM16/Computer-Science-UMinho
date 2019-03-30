# version 300

uniform mat4 m_pvm;
in vec4 position;

uniform vec4 difusa;
uniform vec4 ambiente;

in vec3 normal; //espaco modelo / local
uniform vec4 ldir; // espaco global / mundo 

uniform mat4 m_view;
uniform mat3 m_n; //modelView ^T ^(-1)

void main(){
    vec3 n = vec3(m_n * normal); //espaço camara
    vec3 ld = vec3(m_view * ldir); // espaço camara

    /*Para evitar intensidades negativas, que nao fariam sentido,
    utiliza-se a função maximo */
	float i = max(0, dot(normalize(ld), normalize(n));
    
    cor = i * difusa + ambiente;

    gl_Position = m_pvm * position;
}