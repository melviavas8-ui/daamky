#version 150

in vec2 TexCoord;
in vec4 FragColor;

uniform sampler2D Sampler0;
uniform float uRange;
uniform float uThickness;
uniform float uSmoothness;
uniform int uOutline;
uniform float uOutlineThickness;
uniform vec4 uOutlineColor;

out vec4 fragColor;

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

void main() {
    float dist = median(texture(Sampler0, TexCoord).rgb) - 0.5 + uThickness;
    vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float unitRange = max(1.0, uRange);
    float len = length(h);
    float pixels = len > 0.00001 ? unitRange / len : unitRange * 4.0;
    float alpha = clamp(dist * pixels + 0.5, 0.0, 1.0);
    if (uSmoothness > 0.001) {
        alpha = smoothstep(-uSmoothness, uSmoothness, dist * pixels);
    }
    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (uOutline != 0) {
        float outlineAlpha = clamp((dist + uOutlineThickness) * pixels + 0.5, 0.0, 1.0);
        color = mix(uOutlineColor, FragColor, alpha);
        color.a *= outlineAlpha;
    }

    if (color.a <= 0.001) {
        discard;
    }

    fragColor = color;
}