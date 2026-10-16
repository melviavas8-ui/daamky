#version 150

in vec2 v_FragCoord;

uniform vec2 uResolution;
uniform float uTime;

out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    mat2 rot = mat2(0.866, -0.5, 0.5, 0.866);
    for (int i = 0; i < 4; i++) {
        v += a * noise(p);
        p = rot * p * 2.0 + vec2(10.0);
        a *= 0.5;
    }
    return v;
}

void main() {
    vec2 res = (uResolution.x > 1.0 && uResolution.y > 1.0) ? uResolution : vec2(1920.0, 1080.0);
    vec2 uv = (gl_FragCoord.xy != vec2(0.0)) ? (gl_FragCoord.xy / res.xy) : (v_FragCoord / res.xy);
    
    float t = uTime * 0.10;

    // Organic fluid wave warping
    vec2 q = vec2(0.0);
    q.x = fbm(uv * 1.8 + vec2(0.0, t * 0.4));
    q.y = fbm(uv * 1.8 + vec2(1.0, t * 0.3));

    vec2 r = vec2(0.0);
    r.x = fbm(uv * 2.2 + 1.0 * q + vec2(1.7, 9.2) + 0.12 * t);
    r.y = fbm(uv * 2.2 + 1.0 * q + vec2(8.3, 2.8) + 0.10 * t);

    float f = fbm(uv * 1.6 + r * 1.4);

    // High-Contrast Pure Black & White Grayscale Palette
    float bgDark = 0.06;
    float bgMid = 0.22;
    float bgLight = 0.48;

    float baseVal = mix(bgDark, bgMid, clamp(f * f * 2.5, 0.0, 1.0));
    baseVal = mix(baseVal, bgLight, clamp(length(q), 0.0, 1.0) * 0.45);

    // Topographic crisp white sheen lines
    float bands = 14.0;
    float lineVal = fract(f * bands);
    float lineThickness = 0.025;
    float sheenLine = smoothstep(lineThickness + 0.02, lineThickness, abs(lineVal - 0.5));
    float glowLine = smoothstep(0.20, 0.0, abs(lineVal - 0.5));

    // Combine monochrome elements
    float intensity = baseVal + sheenLine * 0.55 + glowLine * 0.18;

    // Subtle grain texture for depth
    float grain = (hash(uv * 500.0 + uTime) - 0.5) * 0.025;
    intensity += grain;

    // Soft vignette
    vec2 p = uv - 0.5;
    intensity *= (1.0 - 0.30 * dot(p, p));

    intensity = clamp(intensity, 0.0, 1.0);

    fragColor = vec4(vec3(intensity), 1.0);
}