#version 150

in vec4 vColor;
in vec3 vPos;
in vec2 vTexCoord;

uniform sampler2D Sampler0;
uniform vec2 uScreenSize;
uniform float uTime;
uniform vec3 uColor1;
uniform vec3 uColor2;
uniform float uFlowSpeed;
uniform float uPulse;
uniform float uIntensity;

out vec4 fragColor;

float hash(vec3 p) {
    p = fract(p * 0.3183099 + .1);
    p *= 17.0;
    return fract(p.x * p.y * p.z * (p.x + p.y + p.z));
}

float noise(in vec3 x) {
    vec3 i = floor(x);
    vec3 f = fract(x);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(mix(hash(i + vec3(0,0,0)), hash(i + vec3(1,0,0)), f.x),
            mix(hash(i + vec3(0,1,0)), hash(i + vec3(1,1,0)), f.x), f.y),
            mix(mix(hash(i + vec3(0,0,1)), hash(i + vec3(1,0,1)), f.x),
                    mix(hash(i + vec3(0,1,1)), hash(i + vec3(1,1,1)), f.x), f.y), f.z);
}

float fbm(vec3 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    for(int i = 0; i < 3; i++) {
        value += amplitude * noise(p * frequency);
        frequency *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    vec4 tex = texture(Sampler0, vTexCoord);
    if (tex.a < 0.1) {
        discard;
    }

    float t = uTime * uFlowSpeed * 0.15;
    vec3 p = vPos * 3.0;

    vec3 warp1 = vec3(
            fbm(p + vec3(0.0, 0.0, 0.0) + t * 0.5),
            fbm(p + vec3(5.2, 1.3, 2.8) + t * 0.3),
            sin(p.z * 2.0 + t) * 0.5
    );

    float n = fbm(p + warp1 * 1.5);
    vec3 nebulaColor = mix(uColor1, uColor2, n);
    nebulaColor = mix(nebulaColor, vec3(1.0), pow(n, 4.0) * 0.5);

    float alpha = smoothstep(0.25, 0.7, n) * 0.85;
    float breath = sin(uTime * uPulse * 2.0) * 0.15 + 0.85;
    nebulaColor *= breath;
    nebulaColor *= uIntensity;

    alpha *= vColor.a * tex.a;

    fragColor = vec4(nebulaColor, alpha);
}
