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
		
void main()
{
	gl_Position = Projection * View  * Model * vPosition;
	
	normal = vNormal;
	color = vec4(vlight + vskylight,vlight + vskylight,vlight + vskylight, 1);
	uvcoord = vTexCoord;
} 
