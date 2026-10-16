#version 150

uniform sampler2D Sampler0;
uniform float Saturation;
uniform float Brightness;

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec4 col = texture(Sampler0, TexCoord);
    float luma = dot(col.rgb, vec3(0.2126, 0.7152, 0.0722));
    vec3 sat = mix(vec3(luma), col.rgb, Saturation);
    vec3 result = sat * Brightness;
    fragColor = vec4(clamp(result, 0.0, 1.0), col.a);
}
