#version 120
	
attribute vec4 vPosition;
attribute float vlight;
attribute vec4 vNormal;
attribute vec2 vTexCoord;
attribute float vskylight;

varying vec4 normal;
varying vec4 color;
varying vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;
uniform vec4 skyColor;
		
void main()
{
	gl_Position = Projection * View  * Model * vPosition;
	
	normal = vNormal;
	color = vec4(vlight, vlight, vlight, 1) + vec4(vskylight * skyColor.x, vskylight * skyColor.y, vskylight * skyColor.z, skyColor.w);
	uvcoord = vTexCoord;
} 
