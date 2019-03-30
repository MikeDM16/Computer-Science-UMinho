#version 330

uniform sampler2D tex;

in Data {
	vec2 texCoord;
} DataIn;

out vec4 colorOut;

void main() {

	// get texture color
	vec4 texColor = texture(tex, DataIn.texCoord);

	colorOut = texColor;
}