#version 330

uniform sampler2D texEarth, texSpec, texPoke, texWorm;
uniform float shininess=128;
uniform float pixel_height=10, pixel_width=10;
uniform float screenSize_height=1024, screenSize_width=1024;

in Data {
  vec4 eye;
  vec2 texCoord;
  vec3 normal;
  vec3 l_dir;
} DataIn;

out vec4 colorOut;

void main() { 
  float dx = pixel_width*(1./screenSize_height);
  float dy = pixel_height*(1./screenSize_width);

  vec2 coord = vec2(dx*floor(DataIn.texCoord.x/dx),
                    dy*floor(DataIn.texCoord.y/dy));

  
	colorOut = vec4(texture2D(texEarth, coord).rgb, 1.0);
}