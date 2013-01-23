#version 330

in vec4 normal;
in vec4 color;
in vec4 biomecolor;
in vec2 uvcoord;

uniform sampler2D Diffuse;

layout(location=0) out vec4 outputColor;
layout(location=1) out vec4 normalColor;

void main() {
	if (normal.y < 0) {
		// Bottom face
		outputColor = texture(Diffuse, uvcoord) * color;
	} else if (normal.y == 0) {
		// Side face
		vec4 colorableTexel = texture(Diffuse, vec2(uvcoord.x + 0.1875f, uvcoord.y + 0.125f));
		if (colorableTexel.a > 0.0f) {
			outputColor = colorableTexel * color * biomecolor;
		} else {
			outputColor = texture(Diffuse, uvcoord) * color;
		}
	} else {
		// Top face
		outputColor = texture(Diffuse, uvcoord) * color * biomecolor;
	}
	normalColor = (normal + vec4(1, 1, 1, 1)) / 2;
}
