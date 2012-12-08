#version 120

varying vec4 color;
varying vec2 uvcoord;

uniform sampler2D Diffuse;

void main()
{
    vec4 texel = texture2D(Diffuse, uvcoord);
    if(texel.a <= 0.0f) discard;
    gl_FragColor = texel * color;
} 