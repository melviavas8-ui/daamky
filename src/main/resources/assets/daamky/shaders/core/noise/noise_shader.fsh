#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform vec4 TintColor;
uniform float Time;
uniform float Scale;
uniform int Mode;

in vec2 TexCoord;
in vec4 FragColor;
out vec4 OutColor;

float rawDepthDiff(vec2 uv) {
    float now = texture(Sampler1, uv).r;
    float was = texture(Sampler2, uv).r;
    return was - now;
}

float noiseMask(vec2 uv) {
    float diff = rawDepthDiff(uv);
    return smoothstep(0.00003, 0.00035, diff);
}

float smoothHandMask(vec2 uv, vec2 texel) {
    float center = noiseMask(uv);
    if (center <= 0.0001) return 0.0;
    if (center >= 0.999) return 1.0;

    float n = noiseMask(uv + vec2(0.0, texel.y));
    float s = noiseMask(uv - vec2(0.0, texel.y));
    float e = noiseMask(uv + vec2(texel.x, 0.0));
    float w = noiseMask(uv - vec2(texel.x, 0.0));
    
    return (center * 2.0 + n + s + e + w) / 6.0;
}

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 345.45));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), f.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), f.x), f.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    mat2 rot = mat2(1.6, 1.2, -1.2, 1.6);
    for (int i = 0; i < 4; i++) {
        v += noise(p) * a;
        p = rot * p + vec2(1.7, 9.2);
        a *= 0.5;
    }
    return v;
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec2 uv = TexCoord;
    float centerSample = noiseMask(uv);
    if (centerSample <= 0.0001) discard;

    vec2 texel = 1.0 / vec2(textureSize(Sampler1, 0));
    float mask = (centerSample >= 0.999) ? 1.0 : smoothHandMask(uv, texel);
    if (mask < 0.005) discard;

    vec2 res = vec2(textureSize(Sampler1, 0));
    float aspect = (res.y > 0.0) ? (res.x / res.y) : 1.777778;

    float t = Time;
    float s = max(0.2, Scale);
    vec2 p = vec2(uv.x * aspect, uv.y) * (2.0 * s);
    vec3 col = TintColor.rgb;
    float fill = 1.0;
    float outA = TintColor.a;

    if (Mode == 0) {
        // Mode 0: Seamless Cosmic Nebula & Flowing Energy Waves (No dark voids, no gaps)
        vec2 drift = vec2(t * 0.22, -t * 0.16);
        
        vec2 warp1 = vec2(
            fbm(p * 0.85 + drift * 0.6 + vec2(1.2, 3.4)),
            fbm(p * 0.85 - drift * 0.5 + vec2(5.6, 7.8))
        );
        vec2 q = p + (warp1 - 0.5) * 1.4;
        
        vec2 warp2 = vec2(
            fbm(q * 1.30 + drift * 0.8 + vec2(2.3, 8.9)),
            fbm(q * 1.30 - drift * 0.6 + vec2(4.1, 6.7))
        );
        vec2 rCoord = q + (warp2 - 0.5) * 0.8;

        float n1 = fbm(rCoord * 1.1 + drift * 0.3);
        float n2 = fbm(rCoord * 2.2 - drift * 0.4);
        float energy = n1 * 0.55 + n2 * 0.45;

        float contour = pow(clamp(1.0 - abs(n1 - n2) * 2.5, 0.0, 1.0), 3.0);
        float pulseWave = pow(clamp(1.0 - abs(sin(rCoord.x * 1.4 + rCoord.y * 0.8 - t * 1.2 + n1 * 3.0)), 0.0, 1.0), 4.0);

        float core = smoothstep(0.15, 0.85, energy + contour * 0.4 + pulseWave * 0.3);
        
        vec3 baseTint = TintColor.rgb * (0.65 + energy * 0.35);
        vec3 litTint  = mix(TintColor.rgb, vec3(1.0), 0.55);
        vec3 hotCore  = vec3(1.0, 1.0, 1.0);
        
        col = mix(baseTint, litTint, clamp(contour * 0.8 + pulseWave * 0.5, 0.0, 1.0));
        col = mix(col, hotCore, pow(core, 2.5) * 0.70);
        
        fill = mask * (0.60 + core * 0.40);
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    } else if (Mode == 1) {
        // Mode 1: Plasma Fluid
        float v1 = sin(p.x * 3.0 + t * 1.5);
        float v2 = sin(p.y * 3.0 + t * 1.8);
        float v3 = sin((p.x + p.y) * 2.5 + t * 2.0);
        float plasma = (v1 + v2 + v3) / 3.0;
        float intensity = smoothstep(-0.6, 0.9, plasma);
        col = mix(TintColor.rgb, vec3(1.0), intensity * 0.6);
        fill = mask * (0.55 + intensity * 0.45);
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    } else if (Mode == 2) {
        // Mode 2: Continuous Electric Lightning flowing along the sword blade downward (no fract seams, no tears)
        float tElec = t * 3.2;

        // Blade coordinate space: aligned with held sword diagonal (45 degrees)
        vec2 bladeP = vec2(p.x * 0.7071 - p.y * 0.7071, p.x * 0.7071 + p.y * 0.7071);

        // Downward travel time along the blade
        float downFlow = bladeP.y - tElec * 2.6;

        // Bolt 1: Central main surging lightning bolt
        float disp1 = (fbm(vec2(downFlow * 1.5, tElec * 0.9)) - 0.5) * 0.38;
        disp1 += (noise(vec2(downFlow * 4.5, tElec * 2.2)) - 0.5) * 0.12;
        float dist1 = abs(bladeP.x - disp1);

        // Bolt 2: Secondary dancing fork / branch offset along the blade
        float downFlow2 = bladeP.y - tElec * 3.1 + 3.7;
        float disp2 = (fbm(vec2(downFlow2 * 1.8 + 5.2, -tElec * 0.8)) - 0.5) * 0.32 + 0.18;
        disp2 += (noise(vec2(downFlow2 * 5.0, tElec * 2.5 + 1.4)) - 0.5) * 0.09;
        float dist2 = abs(bladeP.x - disp2);

        // Bolt 3: High-frequency rapid arc on the opposite flank
        float downFlow3 = bladeP.y - tElec * 2.2 + 8.1;
        float disp3 = (fbm(vec2(downFlow3 * 2.2 - 2.8, tElec * 1.1)) - 0.5) * 0.30 - 0.16;
        float dist3 = abs(bladeP.x - disp3);

        // Exponential laser-hot core filaments
        float core1 = exp(-dist1 * 48.0) * 1.8;
        float core2 = exp(-dist2 * 52.0) * 1.4;
        float core3 = exp(-dist3 * 56.0) * 1.1;
        float totalCore = core1 + core2 + core3;

        // Soft ionizing corona glow
        float glow1 = exp(-dist1 * 12.0) * 0.85 + exp(-dist1 * 3.5) * 0.35;
        float glow2 = exp(-dist2 * 14.0) * 0.65 + exp(-dist2 * 4.0) * 0.28;
        float glow3 = exp(-dist3 * 16.0) * 0.50 + exp(-dist3 * 4.5) * 0.22;
        float totalGlow = glow1 + glow2 + glow3;

        // High-voltage crackling sparks and micro-arcs
        float sparkNoise = noise(vec2(bladeP.x * 6.0, downFlow * 6.0) + vec2(tElec * 2.5, -tElec * 3.0));
        float sparks = pow(clamp(sparkNoise, 0.0, 1.0), 5.0) * 1.2;

        // Ambient plasma field surging along the blade
        float plasmaPulse = 0.50 + 0.18 * sin(downFlow * 2.5 + tElec * 1.8);
        float elecIntensity = totalCore + totalGlow + sparks;

        vec3 elecColor = TintColor.rgb * (plasmaPulse + totalGlow * 0.65);
        elecColor += mix(TintColor.rgb, vec3(0.55, 0.88, 1.0), 0.55) * totalGlow;
        elecColor += vec3(1.0, 1.0, 1.0) * (totalCore * 1.1 + sparks * 0.9);

        col = elecColor;
        fill = mask * clamp(plasmaPulse + elecIntensity * 0.70, 0.55, 1.0);
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    } else if (Mode == 3) {
        // Mode 3: Rainbow Spectrum
        float hue = fract(p.x * 0.25 + p.y * 0.15 + t * 0.2);
        vec3 rainbow = hsv2rgb(vec3(hue, 0.85, 1.0));
        float pN = fbm(p * 1.2 + vec2(t * 0.4, -t * 0.3));
        col = mix(rainbow, vec3(1.0), pN * 0.35);
        fill = mask * (0.55 + pN * 0.45);
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    } else if (Mode == 4) {
        // Mode 4: Glow Outline
        vec2 px = vec2(0.0018, 0.0018) * (1.0 + s * 0.8);
        float m0 = noiseMask(uv + vec2(-px.x, 0.0));
        float m1 = noiseMask(uv + vec2(px.x, 0.0));
        float m2 = noiseMask(uv + vec2(0.0, -px.y));
        float m3 = noiseMask(uv + vec2(0.0, px.y));
        float edge = abs(m0 - m1) + abs(m2 - m3);
        float edgeGlow = smoothstep(0.02, 0.45, edge);
        float pulse = 0.85 + 0.15 * sin(t * 3.5);
        col = mix(TintColor.rgb, vec3(1.0, 1.0, 1.0), edgeGlow * 0.7);
        fill = edgeGlow * 2.2 * pulse + mask * 0.30;
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    } else if (Mode == 5) {
        // Mode 5: CustomGlint Crystalline Glass
        float diagonal = p.x * 0.707 + p.y * 0.707;
        float crossDiag = -p.x * 0.707 + p.y * 0.707;

        float sweep1 = sin(diagonal * 3.0 - t * 2.2);
        float sweep2 = cos(diagonal * 5.5 + crossDiag * 2.0 + t * 1.4);
        float sweep3 = sin(crossDiag * 4.0 - t * 1.8);

        float glassSheen = pow(max(0.0, sweep1 * 0.5 + 0.5), 8.0) * 1.8;
        float sharpGleam = pow(max(0.0, sin(diagonal * 8.0 - t * 3.0)), 16.0) * 2.5;
        float caustics = pow(max(0.0, sweep2 * sweep3 * 0.5 + 0.5), 6.0) * 1.3;

        float dispR = sin(diagonal * 3.5 - t * 2.0 + 0.25) * 0.5 + 0.5;
        float dispG = sin(diagonal * 3.5 - t * 2.0) * 0.5 + 0.5;
        float dispB = sin(diagonal * 3.5 - t * 2.0 - 0.25) * 0.5 + 0.5;
        vec3 dispersion = vec3(dispR, dispG, dispB);

        float tintMix = sin(diagonal * 1.8 + t * 0.6) * 0.5 + 0.5;
        vec3 baseGlass = TintColor.rgb * (0.40 + tintMix * 0.35);

        vec3 glassColor = baseGlass + dispersion * 0.35 + vec3(glassSheen + sharpGleam + caustics);

        col = glassColor;
        fill = mask * (0.45 + clamp(glassSheen + sharpGleam + caustics, 0.0, 1.0) * 0.55);
        outA = clamp(TintColor.a * fill * mask, 0.0, 1.0);
    }

    if (outA <= 0.001) discard;
    OutColor = vec4(col, outA);
}
