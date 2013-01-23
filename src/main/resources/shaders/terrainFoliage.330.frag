#version 330

in vec4 normal;
in vec4 color;
in vec4 biomecolor;
in vec2 uvcoord;

uniform sampler2D Diffuse;

layout(location=0) out vec4 outputColor;
layout(location=1) out vec4 normalColor;

void main() {
	vec4 texel = texture(Diffuse, uvcoord);
	if (texel.a <= 0.0f) discard;
	outputColor = texel * color * biomecolor;
	normalColor = (normal + vec4(1, 1, 1, 1)) / 2;
}
