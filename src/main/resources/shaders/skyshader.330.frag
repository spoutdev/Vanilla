#version 330

in vec2 uvcoord;
in vec4 color;
in vec4 normal;


uniform sampler2D Stars;

uniform float suny;
uniform float sunSize;
uniform vec4 dawnColor;
uniform vec4 dayColor;
uniform vec4 nightColor;

layout(location=0) out vec4 outputColor;
layout(location=1) out vec4 normalColor;

void main() {


	float sunWeight;
	vec4 skyColor;
	
	vec4 skyTex = texture(Stars, uvcoord);

	float yAbs = abs(suny);
	if (yAbs < sunSize) {
		sunWeight = (suny + sunSize) / sunSize / 2.0f;
		vec4 weightedSun;
		if (suny < 0) {
			weightedSun = dawnColor;
		} else {
			float dawnWeight = suny / sunSize;
			weightedSun = mix(dawnColor, dayColor, dawnWeight);
		}
		skyColor = mix(skyTex, weightedSun, sunWeight);
	} else {
		if (suny < 0) {
			sunWeight = 0;
			skyColor = skyTex;
		} else {
			sunWeight = 1;
			skyColor = dayColor;
		}
	}
	
	
	
	outputColor = skyColor;
	
	normalColor = (normal + vec4(1, 1, 1, 1)) / 2;
}
