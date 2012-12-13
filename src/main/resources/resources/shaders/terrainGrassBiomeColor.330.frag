#version 330

in vec4 normal;
in vec4 color;
in vec4 biomecolor;
in vec2 uvcoord;

uniform sampler2D Diffuse;

layout(location=0) out vec4 outputColor;

void main()
{
	vec4 texel = texture(Diffuse, uvcoord);
	if (normal.y < 0) {
		// Top face
		outputColor = texel * color;
	} else if (normal.y == 0) {
		// Side face
		vec4 colorableTexel = texture(Diffuse, vec2(uvcoord.x + 0.1875f, uvcoord.y + 0.125f));
		if (colorableTexel.a > 0.0f) {
			outputColor = colorableTexel * color * biomecolor;
		} else {
			outputColor = texel * color;
		}
	} else {
		// Bottom face
		outputColor = texel * color * biomecolor;
	}
}