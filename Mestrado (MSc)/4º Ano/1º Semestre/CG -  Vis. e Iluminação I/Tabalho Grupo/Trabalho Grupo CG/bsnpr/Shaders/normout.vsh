uniform vec4 camPos;
uniform vec4 lightPos;

varying vec3 normal;
varying vec3 vert;
varying vec3 camVec;
varying vec3 lightVec;

void main()
{
gl_Position = ftransform();
mat4 ori = gl_TextureMatrix[1];
mat3 normat = mat3(ori[0][0],ori[0][1],ori[0][2],
		   ori[1][0],ori[1][1],ori[1][2],
		   ori[2][0],ori[2][1],ori[2][2]);

lightVec=lightPos.xyz;
vec4 cam =(camPos);
vec4 vrt =ori*(gl_Vertex);
vec4 nrm =ori*(gl_Vertex+vec4(gl_Normal,0.0));
camVec = cam.xyz;
normal = nrm.xyz;
vert   = vrt.xyz;

}