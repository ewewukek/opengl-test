#version 150

in vec3 position;
in vec2 texcoord;
in vec3 normal;
in vec3 tangent;
in vec3 bitangent;

smooth out float diff;
smooth out vec2 f_texcoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat3 normalMatrix;

void main(void) {
    vec3 f_lightDir = vec3(0.0, 0.0, 1.0);
    vec4 v_cam = viewMatrix * vec4(position, 1.0);
    vec4 v = projectionMatrix * v_cam;
    gl_Position = v;
    viewdir = -v_cam.xyz;
    f_texcoord = texcoord;
    n = normalMatrix * normal;
    t = normalMatrix * tangent;
    b = cross(n, t);
}