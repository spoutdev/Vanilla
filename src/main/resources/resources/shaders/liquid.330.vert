#version 330

layout(location=0) in vec4 vPosition;
layout(location=1) in float vlight;
layout(location=2) in vec4 vNormal;
layout(location=3) in vec2 vTexCoord;
layout(location=4) in float vskylight;

out vec4 color;
out vec4 normal;
out vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;
uniform vec4 skyColor;
uniform vec2 animation;
		
void main()
{
	gl_Position = Projection * View * Model * vPosition;
	
	color = vec4(vlight, vlight, vlight, 1) + vec4(vskylight * skyColor.x, vskylight * skyColor.y, vskylight * skyColor.z, skyColor.w);
	normal = vNormal;
	uvcoord = vTexCoord + animation;

} 
