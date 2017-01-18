#version 150

precision highp float;

smooth in vec2 f_texcoord;
smooth in vec3 t;
smooth in vec3 b;
smooth in vec3 n;
smooth in vec3 viewdir;

uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D specularMap;
uniform vec3 lightdir;

void main(void) {
    vec4 texel = texture2D(diffuseMap, f_texcoord);
    if (texel.a <= 0.0) discard;

    vec3 l = normalize(lightdir);

    vec3 texel_n = (texture2D(normalMap, f_texcoord).rgb - 0.5) * 2.0;
    vec3 normaldir = normalize( (t * texel_n.x) + (b * texel_n.y) + (n * texel_n.z) );

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