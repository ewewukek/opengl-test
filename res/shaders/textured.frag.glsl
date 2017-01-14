#version 150

precision highp float;

smooth in vec2 f_texcoord;

uniform sampler2D diffuseMap;

void main(void) {
    vec4 texel = texture2D(diffuseMap, f_texcoord);
    if (texel.a <= 0.0) discard;
    gl_FragColor = vec4(texel);
}