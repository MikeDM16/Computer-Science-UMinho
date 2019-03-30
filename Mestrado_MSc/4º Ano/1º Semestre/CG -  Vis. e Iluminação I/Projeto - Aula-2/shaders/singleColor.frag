#version 330

uniform vec4 diffuse;

out vec4 colorOut;

void main() {

	colorOut = diffuse * 0.75;
}