#version 150

precision highp float;

smooth in vec2 s_TexCoord;

void main(void) {
    gl_FragColor = vec4(s_TexCoord.x, s_TexCoord.y, 1.0, 1.0);
}