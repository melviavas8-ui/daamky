#version 150

in vec2 TexCoord;

uniform sampler2D Sampler0;
uniform int uGlintMode;
uniform int uSolidGlint;
uniform vec2 uScreenSize;
uniform float uTime;
uniform vec3 uColor1;
uniform vec3 uColor2;
uniform float uFlowSpeed;
uniform float uPulse;
uniform float uIntensity;

out vec4 fragColor;

// --- SHARED UTILS ---
float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    
    float dt = dot(i, vec2(12.9898, 78.233));
    vec4 v = vec4(dt, dt + 12.9898, dt + 78.233, dt + 91.2228);
    vec4 h = fract(sin(v) * 43758.5453);
    
    return mix(mix(h.x, h.y, f.x), mix(h.z, h.w, f.x), f.y);
}

// --- NEBULA MODE ---
float nebulaFbm(vec2 p) {
    p *= 3.0;
    mat2 m = mat2(1.6, 1.2, -1.2, 1.6);
    float f = 0.5000 * noise(p); p = m * p;
    f += 0.2500 * noise(p); p = m * p;
    f += 0.1250 * noise(p); p = m * p;
    return 0.5 + 0.5 * f;
}

vec3 getNebulaColor(vec2 uv) {
    float t = uTime * uFlowSpeed * 0.4;
    vec2 warp = vec2(nebulaFbm(uv + t), nebulaFbm(uv - t * 0.7));
    float pattern = nebulaFbm(uv * 1.5 + warp * 0.9 + t * 0.5);
    return mix(uColor1, uColor2, pattern);
}

// --- LIQUID MODE ---
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

float liquidFbm(vec2 uv, int octaves) {               
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

float liquidFbmLow(vec2 uv) {
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

vec3 getLiquidColor(vec2 uv) {
    float t = uTime * uFlowSpeed * 0.6;
    vec2 rv = uv / (length(uv * 2.5) * (uv * 30.0));
    uv *= rot2d(0.3 * t);
    
    float val = 0.5 * liquidFbm(uv * 2.0 * liquidFbmLow(length(uv) + rv - t), 4);
    uv *= rot2d(-0.6 * t);
    
    float colVal = 1.6 * (0.5 * liquidFbm(uv * val * 8.0, 3) + 0.02 * r(uv.x, uv.y));
    colVal *= 3.8;
    colVal = colVal / (1.0 + colVal);
    colVal = smoothstep(0.18, 0.88, colVal);
    
    return mix(uColor1, uColor2, colVal);
}

// --- GLASS (CRYSTAL REFRACTION & SPECULAR) MODE ---
vec3 getGlassColor(vec2 uv) {
    float t = uTime * uFlowSpeed * 0.85;
    
    // Diagonal glass coordinate projection
    vec2 p = uv * 1.8;
    float diagonal = p.x * 0.707 + p.y * 0.707;
    float crossDiag = -p.x * 0.707 + p.y * 0.707;
    
    // 1. Broad smooth glass light reflection sweeps
    float sweep1 = sin(diagonal * 3.0 - t * 2.2);
    float sweep2 = cos(diagonal * 5.5 + crossDiag * 2.0 + t * 1.4);
    float sweep3 = sin(crossDiag * 4.0 - t * 1.8);
    
    // 2. High-index crystalline specular flare (pure bright reflections)
    float glassSheen = pow(max(0.0, sweep1 * 0.5 + 0.5), 8.0) * 1.8;
    float sharpGleam = pow(max(0.0, sin(diagonal * 8.0 - t * 3.0)), 16.0) * 2.5;
    float caustics = pow(max(0.0, sweep2 * sweep3 * 0.5 + 0.5), 6.0) * 1.3;
    
    // 3. Prismatic chromatic dispersion (glass refraction color splitting)
    float dispR = sin(diagonal * 3.5 - t * 2.0 + 0.25) * 0.5 + 0.5;
    float dispG = sin(diagonal * 3.5 - t * 2.0) * 0.5 + 0.5;
    float dispB = sin(diagonal * 3.5 - t * 2.0 - 0.25) * 0.5 + 0.5;
    vec3 dispersion = vec3(dispR, dispG, dispB);
    
    // 4. Base tinted crystal glass color
    float tintMix = sin(diagonal * 1.8 + t * 0.6) * 0.5 + 0.5;
    vec3 baseGlass = mix(uColor1, uColor2, tintMix) * 0.45;
    
    // Combine base glass tint + chromatic dispersion + brilliant pure white specular highlights
    vec3 glassColor = baseGlass + dispersion * 0.35 + vec3(glassSheen + sharpGleam + caustics);
    return glassColor;
}

// --- RAINBOW MODE ---
vec3 getRainbowColor(vec2 uv) {
    float scroll = uv.x + uv.y + uTime * uFlowSpeed * 0.3;
    float r = abs(sin(scroll * 6.28318 + 0.0)) * 0.5 + 0.5;
    float g = abs(sin(scroll * 6.28318 + 2.09439)) * 0.5 + 0.5;
    float b = abs(sin(scroll * 6.28318 + 4.18879)) * 0.5 + 0.5;
    return vec3(r, g, b);
}

void main() {
    vec2 screenUv = gl_FragCoord.xy / uScreenSize.xy;
    vec2 uv = screenUv * 2.0 - 1.0;
    uv.x *= uScreenSize.x / uScreenSize.y;

    vec3 shaderColor = vec3(0.0);
    if (uGlintMode == 1) {
        shaderColor = getLiquidColor(uv);
    } else if (uGlintMode == 2) {
        shaderColor = getGlassColor(uv);
    } else if (uGlintMode == 3) {
        shaderColor = getRainbowColor(uv);
    } else {
        shaderColor = getNebulaColor(uv);
    }

    // Dynamic pulsating wave (smooth rhythm modulation scaled by uPulse)
    float wave = sin(uTime * 3.2) * 0.5 + 0.5;
    float pulseFactor = mix(1.0, 0.15 + 1.25 * (wave * wave), clamp(uPulse, 0.0, 1.0));

    // Scale final RGB directly for additive blending (GL_SRC_COLOR, GL_ONE) & Alpha
    vec3 finalRgb = shaderColor * pulseFactor * uIntensity;

    float alpha = clamp(uIntensity * pulseFactor, 0.0, 1.0);
    if (uSolidGlint == 0) {
        alpha *= texture(Sampler0, TexCoord).r;
    }

    fragColor = vec4(finalRgb, alpha);
}
