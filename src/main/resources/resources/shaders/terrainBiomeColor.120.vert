#version 120
	
attribute vec4 vPosition;
attribute vec4 vColor;
attribute vec4 vNormal;
attribute vec2 vTexCoord;
attribute vec4 vBiomeColor;

varying vec4 color;
varying vec4 biomecolor;
varying vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;
		
void main()
{
	gl_Position = Projection * View * Model * vPosition;
	
	uvcoord = vTexCoord;
	color = vColor;
	biomecolor = vBiomeColor;
} 
