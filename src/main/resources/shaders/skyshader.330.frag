#version 330

in vec2 uvcoord;
in vec4 color;

uniform vec4 ambient;
uniform vec4 skyColor;
uniform vec4 sunColor;
uniform vec4 moonColor;

uniform vec4 sunDir;

layout(location=0) out vec4 outputColor;

void main() {
	outputColor = skyColor;
}
