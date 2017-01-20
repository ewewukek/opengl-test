#version 150

precision highp float;

smooth in float diff;
smooth in vec2 f_texcoord;

uniform sampler2D normalMap;

void main(void) {
    vec3 texel_n = (texture2D(normalMap, f_texcoord).rgb - 0.5) * 2.0;

    float d = max(0.0, dot(l, normaldir));

    vec3 v = normalize(viewdir);

    float s = 0.0;
    vec4 spec = texture2D(specularMap, f_texcoord);

    if (d > 0.0) {
        vec3 h = normalize(l + v);
        s = max(0.0, dot(h, normaldir));
        s = pow(s, 200) * 0.7;
    }

    gl_FragColor = texel * d + spec * s;
}