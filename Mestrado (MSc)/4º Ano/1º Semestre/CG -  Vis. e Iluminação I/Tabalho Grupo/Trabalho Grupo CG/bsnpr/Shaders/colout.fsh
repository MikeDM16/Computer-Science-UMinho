varying vec4 pos;
varying vec4 color;

varying vec4 projCoords;
uniform sampler2D shadowMap;

void main()
{
	vec4 col = texture2DProj(shadowMap, projCoords);
	
	float shadow = 10.0*length(pos.xyz) > col.w+0.75 ? 0.0 : 1.0;

	if(projCoords.x > 1.0 || projCoords.x < 0.0 ) shadow = 1.0;
	if(projCoords.y > 1.0 || projCoords.y < 0.0 ) shadow = 1.0;

	gl_FragColor = vec4(color.xyz, shadow);
}