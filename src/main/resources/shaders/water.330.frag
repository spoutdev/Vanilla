#version 330

in vec4 color;
in vec4 biomeColor;
in vec4 normal;
in vec2 uvcoord;

uniform sampler2D Diffuse;

layout(location=0) out vec4 outputColor;
layout(location=1) out vec4 normalColor;
void main() {
	vec4 fontsample = texture(Diffuse, uvcoord);
	outputColor = fontsample * color * biomeColor;
	normalColor = (normal + vec4(1, 1, 1, 1)) / 2;
}
