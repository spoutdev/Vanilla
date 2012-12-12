#version 120

attribute vec4 vPosition;
attribute float vlight;
attribute vec4 vNormal;
attribute vec2 vTexCoord;
attribute float vskylight;

varying vec4 color;
varying vec4 normal;
varying vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;
uniform vec4 skyColor;
uniform vec2 animation;

void main()
{
	gl_Position = Projection * View  * Model * vPosition;

	color = vec4(vlight, vlight, vlight, 1) + vec4(vskylight * skyColor.x, vskylight * skyColor.y, vskylight * skyColor.z, skyColor.w);
	normal = vNormal;
	uvcoord = vTexCoord + animation;

} 
