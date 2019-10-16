#version 330 core

in vec2 pass_textCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void) {
    out_Color = texture(textureSampler, pass_textCoords);
}