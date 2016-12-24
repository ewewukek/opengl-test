#version 150

precision highp float;

smooth in vec2 texCoord;

uniform sampler2D diffuseMap;

void main(void) {
    vec4 texel = texture2D(diffuseMap, texCoord);
    gl_FragColor = vec4(texel);
    if (texel.a <= 0.0) discard;
}