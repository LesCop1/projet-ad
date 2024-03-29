#version 410 core

in vec2 pass_UV;
in vec4 pass_Color;

out vec4 FragColor;

uniform sampler2D textureSampler;

void main(void) {
    float R = 0.5;

    float dist = length(pass_UV - vec2(R));
    if (dist >= R) {
        discard;
    }

    vec4 tex = texture(textureSampler, pass_UV);
    float alpha = tex.a * smoothstep(R, R - 0.01, dist);
    FragColor = pass_Color * vec4(tex.rgb, alpha);
}