#version 330

layout(location=0) in vec4 vPosition;
layout(location=1) in float vlight;
layout(location=2) in vec4 vNormal;
layout(location=3) in vec2 vTexCoord;
layout(location=4) in float vskylight;

out vec4 normal;
out vec4 color;
out vec2 uvcoord;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;

void main()
{
	gl_Position = Projection * View * Model * vPosition;

	normal = vNormal;
	color = vec4(vlight + vskylight,vlight + vskylight,vlight + vskylight, 1);
	uvcoord = vTexCoord;

} 
