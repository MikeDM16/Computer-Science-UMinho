uniform vec4 lightPos;
varying vec4 pos;
varying vec4 scrpos;

void main()
{
gl_Position = ftransform();
pos = lightPos-gl_TextureMatrix[1]*gl_Vertex;
scrpos = ftransform();
}