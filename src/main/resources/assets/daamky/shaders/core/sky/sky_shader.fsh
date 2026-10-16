#version 150

in vec2 v_FragCoord;

uniform vec2 uResolution;
uniform float uTime;

out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(234.34, 435.345));
    p += dot(p, p + 19.19);
    return fract(p.x * p.y);
}

void main() {
    vec2 uv = v_FragCoord.xy / uResolution.y;
    float t = uTime * 0.02;

    vec3 col = vec3(0.0);

    // Stars
    vec2 st = v_FragCoord.xy / uResolution.xy;
    vec2 gv = fract(st * 50.0 + t * 0.01) - 0.5;
    vec2 id = floor(st * 50.0);

    float star = 1.0 - smoothstep(0.0, 0.1, length(gv));
    float twinkle = sin(uTime * hash21(id) * 10.0 + id.x) * 0.5 + 0.5;
    star *= twinkle;
    star *= 0.3 + 0.7 * hash21(id);

    col += star * 0.8;

    // Nebula glow
    vec2 nuv = uv * 1.5;
    float neb = 0.0;
    for (int i = 1; i < 5; i++) {
        vec2 p = nuv * (1.0 + i * 0.3) + vec2(t * (0.1 + i * 0.03), t * 0.05);
        neb += 0.5 / float(i) * sin(p.x * 3.0 + p.y * 2.0 + t * 0.5);
    }
    neb = abs(neb);

    vec3 nebCol = vec3(0.6, 0.2, 0.8) * neb * 0.3;
    nebCol += vec3(0.1, 0.3, 0.7) * neb * 0.2;

    col += nebCol;

    // Gradient horizon
    float horizon = smoothstep(0.5, -0.2, uv.y - 0.3);
    col = mix(col, vec3(0.02, 0.01, 0.05), horizon);

    fragColor = vec4(col, 1.0);
}
