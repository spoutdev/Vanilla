#version 120

varying vec4 normal;
varying vec4 color;
varying vec4 biomecolor;
varying vec2 uvcoord;

uniform sampler2D Diffuse;

void main()
{
	if (normal.y < 0) {
		// Bottom face
		gl_FragColor = texture2D(Diffuse, uvcoord) * color;
	} else if (normal.y == 0) {
		// Side face
		vec4 colorableTexel = texture2D(Diffuse, vec2(uvcoord.x + 0.1875f, uvcoord.y + 0.125f));
		if (colorableTexel.a > 0.0f) {
			gl_FragColor = colorableTexel * color * biomecolor;
		} else {
			gl_FragColor = texture2D(Diffuse, uvcoord) * color;
		}
	} else {
		// Top face
		gl_FragColor = texture2D(Diffuse, uvcoord) * color * biomecolor;
	}
}