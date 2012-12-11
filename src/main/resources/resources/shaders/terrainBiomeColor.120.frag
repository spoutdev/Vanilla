#version 120

varying vec4 color;
varying vec4 biomecolor;
varying vec2 uvcoord;

uniform sampler2D Diffuse;

void main()
{
    vec4 fontsample = texture2D(Diffuse, uvcoord);
    gl_FragColor = fontsample * color * biomecolor;
} 