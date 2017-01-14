#version 150

in vec3 position;
in vec3 color;

flat out vec3 f_color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
    vec4 v = projectionMatrix * viewMatrix * vec4(position, 1.0);
    gl_Position = v;
    f_color = color;
}