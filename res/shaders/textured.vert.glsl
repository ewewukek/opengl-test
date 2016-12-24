#version 150

in vec3 in_Position;
in vec2 in_TexCoord;

smooth out vec2 s_TexCoord;

void main(void) {
    gl_Position = vec4(in_Position, 1.0);
    s_TexCoord = in_TexCoord;
}