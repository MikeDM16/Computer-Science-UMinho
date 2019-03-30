#version 330

uniform sampler2D texEarth, texSpec, texPoke, texWorm;

in Data {
  vec4 eye;
  vec2 texCoord;
  vec3 normal;
  vec3 l_dir;
} DataIn;

out vec4 colorOut;

uniform mat3 G[2] = mat3[](
	mat3( 1.0, 2.0, 1.0, 0.0, 0.0, 0.0, -1.0, -2.0, -1.0 ),
	mat3( 1.0, 0.0, -1.0, 2.0, 0.0, -2.0, 1.0, 0.0, -1.0 )
);


void main(void) {
    // pontos vizinhança 8x8
    vec2 texOffset = vec2(0.001,0.001); 
    vec2 tc0 = DataIn.texCoord.st + vec2(-texOffset.s, -texOffset.t);
    vec2 tc1 = DataIn.texCoord.st + vec2(         0.0, -texOffset.t);
    vec2 tc2 = DataIn.texCoord.st + vec2(+texOffset.s, -texOffset.t);
    vec2 tc3 = DataIn.texCoord.st + vec2(-texOffset.s,          0.0);
    vec2 tc4 = DataIn.texCoord.st + vec2(         0.0,          0.0);
    vec2 tc5 = DataIn.texCoord.st + vec2(+texOffset.s,          0.0);
    vec2 tc6 = DataIn.texCoord.st + vec2(-texOffset.s, +texOffset.t);
    vec2 tc7 = DataIn.texCoord.st + vec2(         0.0, +texOffset.t);
    vec2 tc8 = DataIn.texCoord.st + vec2(+texOffset.s, +texOffset.t);
    
    vec4 col0 = texture2D(texEarth, tc0);
    vec4 col1 = texture2D(texEarth, tc1);
    vec4 col2 = texture2D(texEarth, tc2);
    vec4 col3 = texture2D(texEarth, tc3);
    vec4 col4 = texture2D(texEarth, tc4);
    vec4 col5 = texture2D(texEarth, tc5);
    vec4 col6 = texture2D(texEarth, tc6);
    vec4 col7 = texture2D(texEarth, tc7);
    vec4 col8 = texture2D(texEarth, tc8);
    vec4 sum = 8.0 * col4 - (col0 + col1 + col2 + col3 + col5 + col6 + col7 + col8); 
    float r = sum.r + sum.g + sum.b;

    colorOut = texture(texEarth, DataIn.texCoord);

    // Mexer no parametro aumenta o alcance do rebordo
    if (r > 0.08)
        colorOut = vec4(0,0,0,1);

}
