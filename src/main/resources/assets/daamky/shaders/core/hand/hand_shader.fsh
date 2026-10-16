#version 150

in vec2 TexCoord;
out vec4 fragColor;

uniform sampler2D Sampler0;
uniform vec2 uResolution;
uniform float uTime;

uniform int uMode;
uniform int uShaderType;

uniform vec4 uOutlineColor;
uniform vec3 uColor1;
uniform vec3 uColor2;

uniform float uOutlineWidth;
uniform float uOpacity;
uniform float uFlowSpeed;
uniform float uIntensity;

void main() {
    vec4 handTex = texture(Sampler0, TexCoord);
    vec2 texelSize = 1.0 / uResolution;

    float handAlpha = max(handTex.a, max(handTex.r, max(handTex.g, handTex.b)) > 0.01 ? 1.0 : 0.0);

    float outlineAlpha = 0.0;
    if (uMode == 0 || uMode == 1) {
        float radius = max(1.0, uOutlineWidth);
        float totalWeight = 0.0;
        float accumulated = 0.0;
        for (float x = -radius; x <= radius; x += 1.0) {
            for (float y = -radius; y <= radius; y += 1.0) {
                float distSq = x * x + y * y;
                if (distSq <= radius * radius) {
                    float dist = sqrt(distSq);
                    float weight = clamp(1.0 - (dist / (radius + 0.5)), 0.0, 1.0);
                    vec2 offset = vec2(x, y) * texelSize;
                    vec4 s = texture(Sampler0, TexCoord + offset);
                    float sAlpha = max(s.a, max(s.r, max(s.g, s.b)) > 0.01 ? 1.0 : 0.0);
                    accumulated += sAlpha * weight;
                    totalWeight += weight;
                }
            }
        }
        if (totalWeight > 0.0) {
            float density = accumulated / totalWeight;
            outlineAlpha = smoothstep(0.06, 0.60, density);
        }
    }

    vec3 fillRGB = uColor1;
    float time = uTime * uFlowSpeed;

    if (uShaderType == 0) { // Nebula
        vec2 uv = (TexCoord * uResolution - uResolution * 0.5) / min(uResolution.x, uResolution.y);
        for (float i = 1.0; i < 5.0; i += 1.0) {
            uv.x += 0.4 / i * cos(i * 3.0 * uv.y + time);
            uv.y += 0.4 / i * cos(i * 2.0 * uv.x + time);
        }
        float wave = abs(sin(time - uv.y - uv.x));
        fillRGB = mix(uColor1, uColor2, wave) * uIntensity;
    } else if (uShaderType == 1) { // Glow
        float glowWave = 0.5 + 0.5 * sin(time * 3.0);
        fillRGB = mix(uColor1, uColor2, glowWave) * (1.2 * uIntensity);
    } else if (uShaderType == 2) { // Gradient
        float gFactor = clamp(TexCoord.y + sin(TexCoord.x * 4.0 + time) * 0.2, 0.0, 1.0);
        fillRGB = mix(uColor1, uColor2, gFactor);
    } else if (uShaderType == 3) { // Solid
        fillRGB = uColor1;
    }

    vec4 finalColor = vec4(0.0);

    if (handAlpha > 0.05) {
        if (uMode == 0 || uMode == 2) {
            finalColor = vec4(fillRGB, handAlpha * uOpacity);
        } else {
            finalColor = vec4(fillRGB * 0.1, handAlpha * uOpacity * 0.2);
        }
    } else if (outlineAlpha > 0.05 && (uMode == 0 || uMode == 1)) {
        finalColor = vec4(uOutlineColor.rgb, outlineAlpha * uOutlineColor.a * uOpacity);
    }

    fragColor = finalColor;
}
