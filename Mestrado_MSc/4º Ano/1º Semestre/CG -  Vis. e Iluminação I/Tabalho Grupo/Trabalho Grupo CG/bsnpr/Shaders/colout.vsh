uniform vec4 lightPos;
varying vec4 pos;
varying vec4 color;

varying vec4 projCoords;

void main()
{
	gl_Position = ftransform();
	color = gl_Color;
	projCoords = gl_TextureMatrix[0] *gl_TextureMatrix[1]*gl_Vertex;
	pos = lightPos - gl_TextureMatrix[1]*gl_Vertex;

}