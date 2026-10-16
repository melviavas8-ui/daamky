#version 150

in vec4 vColor;

uniform vec2 uScreenSize;
uniform float uTime;
uniform vec3 uColor1;
uniform vec3 uColor2;
uniform float uFlowSpeed;
uniform float uPulse;
uniform float uIntensity;

out vec4 fragColor;

mat2 rot2d(float angle) {
    return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
}

float r(float a, float b) {
    return fract(sin(dot(vec2(a, b), vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec3 x) {
    vec3 p  = floor(x);
    vec3 f  = fract(x);
    f       = f * f * (3.0 - 2.0 * f);
    float n = p.x + p.y * 57.0 + 113.0 * p.z;
    
    // Vectorised 3D value noise (4x performance gain)
    vec4 h1 = fract(sin(vec4(n + 0.0, n + 1.0, n + 57.0, n + 58.0) * 12.9898) * 43758.5453);
    vec4 h2 = fract(sin(vec4(n + 113.0, n + 114.0, n + 170.0, n + 171.0) * 12.9898) * 43758.5453);
    
    return mix(mix(mix(h1.x, h1.y, f.x),
                   mix(h1.z, h1.w, f.x), f.y),
               mix(mix(h2.x, h2.y, f.x),
                   mix(h2.z, h2.w, f.x), f.y), f.z);
}

vec3 dnoise2f(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    
    // Vectorised 2D noise hash (4x performance gain)
    vec4 h = fract(sin(vec4(
        i.x + 0.0 + (i.y + 0.0) * 57.0,
        i.x + 1.0 + (i.y + 0.0) * 57.0,
        i.x + 0.0 + (i.y + 1.0) * 57.0,
        i.x + 1.0 + (i.y + 1.0) * 57.0
    )) * 43758.5453);
    
    float a = h.x;
    float b = h.y;
    float c = h.z;
    float d = h.w;
    
    float u = f.x, v = f.y;
    float du = 30.0 * u * u * (u * (u - 2.0) + 1.0);
    float dv = 30.0 * v * v * (v * (v - 2.0) + 1.0);
    u = u * u * u * (u * (u * 6.0 - 15.0) + 10.0);
    v = v * v * v * (v * (v * 6.0 - 15.0) + 10.0);
    
    float k0 = a;
    float k1 = b - a;
    float k2 = c - a;
    float k3 = a - b - c + d;
    return vec3(k0 + k1 * u + k2 * v + k3 * u * v,
                du * (k1 + k3 * v),
                dv * (k2 + k3 * u));
}

float fbm(vec2 uv, int octaves) {               
    vec2 p = uv;
    float f, dx, dz, w = 0.5;
    f = dx = dz = 0.0;
    
    float angle = 2.0 * noise(vec3(p * 0.1, 0.15 * uTime * uFlowSpeed));
    mat2 rotMat = rot2d(angle);
    
    for(int i = 0; i < 8; ++i) {        
        if (i >= octaves) break;
        vec3 n = dnoise2f(uv);
        dx += n.y;
        dz += n.z;
        f += w * n.x / (1.0 + dx * dx + dz * dz);
        w *= 0.86;
        uv *= vec2(1.16);
        uv = rotMat * uv;
    }
    return f;
}

float fbmLow(vec2 uv) {
    float f, dx, dz, w = 0.5;
    f = dx = dz = 0.0;
    for(int i = 0; i < 4; ++i) {        
        vec3 n = dnoise2f(uv);
        dx += n.y;
        dz += n.z;
        f += w * n.x / (1.0 + dx * dx + dz * dz);
        w *= 0.75;
        uv *= vec2(1.5);
    }
    return f;
}

void main() {
    vec2 screenUv = gl_FragCoord.xy / uScreenSize.xy;
    vec2 uv = 1.0 - 2.0 * screenUv;
    uv.y /= uScreenSize.x / uScreenSize.y;
    
    uv *= 8.0;
    
    float t = uTime * uFlowSpeed * 0.6;
    
    float len = max(0.0001, length(uv * 2.5));
    vec2 rv = vec2(1.0 / (len * 30.0));
    uv *= rot2d(0.3 * t);
    float val = 0.5 * fbm(uv * 2.0 * fbmLow(length(uv) + rv - t), 4);
    uv *= rot2d(-0.6 * t);
    
    float colVal = 1.6 * (0.5 * fbm(uv * val * 8.0, 3) + 0.02 * r(uv.x, uv.y));
    colVal *= 3.8;
    colVal = colVal / (1.0 + colVal);
    colVal = smoothstep(0.18, 0.88, colVal);
    
    vec3 finalRgb = mix(uColor1, uColor2, colVal);
    
    float pulse = 1.0 - sin(uTime * 2.2) * 0.35 * uPulse;
    fragColor = vec4(finalRgb * pulse * uIntensity, vColor.a);
}
