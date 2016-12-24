#version 150

in vec3 in_Position;
in vec2 in_TexCoord;

smooth out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
    vec4 v = projectionMatrix * viewMatrix * vec4(in_Position, 1.0);
    gl_Position = v;
    texCoord = in_TexCoord;
}