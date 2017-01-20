#version 150

precision highp float;

smooth in vec2 f_texcoord;
smooth in vec3 t;
smooth in vec3 b;
smooth in vec3 n;
smooth in vec3 tbn_viewdir;
smooth in vec3 tbn_lightdir;

uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D heightMap;

uniform float parallax_multiplier;

void main(void) {
    const float h_mult = 1.0 / 32.0;

    vec3 v = normalize(tbn_viewdir);

    float t = 1.0;
    float h = h_mult * (-1.0 + texture2D(heightMap, f_texcoord).r);

    float k = 0.5;

    for (int i = 0; i != 16; ++i) {
        t = t + k * (h - v.z * t);
        h = h_mult * (-1.0 + texture2D(heightMap, f_texcoord + v.xy * t).r);
    }

    t = t * parallax_multiplier;
    // t = t * (1.0 - pow(1.0 - v.z, 4));

    vec2 tc = f_texcoord + v.xy * t;
    tc.s = max(0.0, min(1.0, tc.s));
    tc.t = max(0.0, min(1.0, tc.t));

    vec4 texel = texture2D(diffuseMap, tc);
    if (texel.a <= 0.0) discard;

    vec3 l = normalize(tbn_lightdir);

    vec3 texel_n = (texture2D(normalMap, tc).rgb - 0.5) * 2.0;

    float d = max(0.0, dot(l, texel_n));

    // vec3 v = normalize(tbn_viewdir);

    gl_FragColor = texel * d;
}