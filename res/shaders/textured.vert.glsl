#version 150

in vec3 position;
in vec2 texcoord;

smooth out vec2 f_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
    vec4 v = projectionMatrix * viewMatrix * vec4(position, 1.0);
    gl_Position = v;
    f_texcoord = texcoord;
}