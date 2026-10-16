#version 150

in vec4 v_Color;

uniform vec2 uScreenSize;
uniform float uTime;
uniform vec3 uColor1;
uniform vec3 uColor2;
uniform float uFlowSpeed;
uniform float uPulse;
uniform float uIntensity;

out vec4 fragColor;

void main() {
    // Normalized screen coordinates
    vec2 uv = gl_FragCoord.xy / uScreenSize;

    // Flow going UP the body — vertical scan lines
    float scan = fract(uv.y * 5.0 - uTime * uFlowSpeed * 0.4) * 2.0 - 1.0;

    // Diagonal waves
    float diag = sin((uv.x * 5.5 + uv.y * 4.0 + uTime * uFlowSpeed * 0.6) * 6.28318) * 0.5 + 0.5;

    // Fine shimmer
    float fine = sin((uv.x * 17.0 + uv.y * 13.0 + uTime * 4.0) * 6.28318) * 0.06;

    // Combine
    float pattern = abs(scan) * 0.4 + diag * 0.35 + fine;

    // Global pulse — never goes below 0.35
    float pulse = 1.0 - sin(uTime * 2.2) * 0.35 * uPulse;

    // Alpha: visible everywhere, brighter at wave peaks
    float alpha = (0.3 + pattern * 0.7) * pulse * uIntensity;

    // Color blends between c1 and c2 based on pattern
    vec3 color = mix(uColor1, uColor2, pattern);

    fragColor = vec4(color, alpha);
}
