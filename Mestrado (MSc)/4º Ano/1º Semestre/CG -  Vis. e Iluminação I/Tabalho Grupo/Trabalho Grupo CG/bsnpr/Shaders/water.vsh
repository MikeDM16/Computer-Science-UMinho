
uniform vec4 lightPos;
uniform vec4 camPos;

uniform float waterMove;

varying vec4 lightVec;
varying vec4 camVec;

void main()
{
	gl_Position = ftransform();

	//texcoords for scrolling normal textures
	gl_TexCoord[0] = gl_MultiTexCoord0+vec4(waterMove, 0.0,0.0,0.0);
	gl_TexCoord[1] = gl_MultiTexCoord0+vec4(-waterMove*4.0, 0.0,0.0,0.0);

	lightVec = lightPos-gl_Vertex;
	camVec = camPos-gl_Vertex;
}