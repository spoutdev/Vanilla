#version 120

attribute vec4 vPosition;
attribute float vlight;
attribute vec4 vNormal;
attribute vec2 vTexCoord;
attribute float vskylight;
attribute vec4 vBiomeColor;

varying vec4 color;
varying vec4 biomeColor;
varying vec4 normal;
varying vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;
uniform vec4 skyColor;
uniform vec4 sunDir;
uniform vec2 animation;
uniform float sunAmbient;

void main()
{
	gl_Position = Projection * View  * Model * vPosition;

	vec3 L = normalize(sunDir.xyz - vPosition.xyz);
	vec3 E = normalize(-vPosition.xyz); // we are in Eye Coordinates, so EyePos is (0,0,0)  
	vec3 R = normalize(-reflect(L,vNormal.xyz));

	//calculate Ambient Term:
	vec4 Iamb = skyColor * sunAmbient * vskylight;

	//calculate Diffuse Term:
	vec4 Idiff = skyColor * (1 - sunAmbient) * max(dot(vNormal.xyz,L), 0.0);
	Idiff = clamp(Idiff * vskylight, 0.0, 1.0);

	// calculate Specular Term:
	// float shininess = 0.1;
	// vec4 Ispec = skyColor * 0.3 * pow(max(dot(R,E),0.0),0.3 * shininess);
	// Ispec = clamp(Ispec * vskylight, 0.0, 1.0); 

	color = vec4(vlight, vlight, vlight, 1) + Iamb + Idiff;
	biomeColor = vBiomeColor;
	normal = vNormal;
	uvcoord = vTexCoord + animation;

} 
