varying vec4 pos;
varying vec4 scrpos;

void main()
{
float fragZ=length(pos.xyz)*10.0;
gl_FragColor = vec4(scrpos.xyz/scrpos.w,fragZ);
}