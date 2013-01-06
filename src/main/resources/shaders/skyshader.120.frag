#version 120

varying vec2 uvcoord;
varying vec4 color;


uniform sampler2D Stars;

uniform float suny;
uniform float sunSize;
uniform vec4 dawnColor;
uniform vec4 dayColor;
uniform vec4 nightColor;



void main() {


	float sunWeight;
	vec4 skyColor;
	
	vec4 skyTex = texture2D(Stars, uvcoord);

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
	
	
	
	gl_FragColor = skyColor;
}
