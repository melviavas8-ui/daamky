#version 150

in vec3 Direction;
in vec4 vColor;

uniform float Time;
uniform vec3 Accent;
uniform vec4 ColorModulator;

out vec4 OutColor;

float hash13(vec3 p) {
    p = fract(p * vec3(17.17, 31.71, 51.13));
    p += dot(p, p.yzx + 19.19);
    return fract((p.x + p.y) * p.z);
}

float noise(vec3 x) {
    vec3 p = floor(x);
    vec3 f = fract(x);
    f = f * f * (3.0 - 2.0 * f);

    float n = p.x + p.y * 57.0 + 113.0 * p.z;
    float res = mix(mix(mix(hash13(vec3(n)), hash13(vec3(n + 1.0)), f.x),
                        mix(hash13(vec3(n + 57.0)), hash13(vec3(n + 58.0)), f.x), f.y),
                    mix(mix(hash13(vec3(n + 113.0)), hash13(vec3(n + 114.0)), f.x),
                        mix(hash13(vec3(n + 170.0)), hash13(vec3(n + 171.0)), f.x), f.y), f.z);
    return res;
}

float fbm2(vec3 p) {
    float v = 0.0;
    v += noise(p) * 0.5; p = p * 2.02;
    v += noise(p) * 0.25;
    return v;
}

void main() {
    vec3 d = normalize(Direction);
    vec3 accent = max(Accent, vec3(0.05));
    
    // Fast sky gradient
    vec3 deep = accent * 0.16 + vec3(0.01, 0.01, 0.02);
    vec3 horizon = mix(deep, accent * 0.5, 0.6);
    vec3 color = mix(horizon, deep, smoothstep(0.0, 0.8, abs(d.y)));

    // Fast nebula
    float t = Time * 0.02;
    vec3 q = d * 2.5 + vec3(t, t, -t);
    float clouds = fbm2(q);
    
    float nebula = smoothstep(0.4, 0.9, clouds) * 0.8;
    vec3 nebColor = mix(accent, vec3(1.0, 0.6, 0.2), clouds);
    color += nebColor * nebula;

    // Fast stars
    vec3 p = d * 150.0;
    vec3 cell = floor(p);
    float h = hash13(cell);
    if (h > 0.99) {
        float f = length(fract(p) - 0.5);
        color += mix(vec3(1.0), accent, 0.3) * (1.0 - smoothstep(0.0, 0.05, f)) * (0.5 + 0.5 * sin(Time * 2.0 + h * 50.0));
    }

    OutColor = vec4(pow(color, vec3(0.85)), 1.0) * ColorModulator * vColor;
}
