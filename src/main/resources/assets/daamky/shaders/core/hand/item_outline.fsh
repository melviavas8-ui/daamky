#version 150

in vec4 vColor;
in vec2 vTexCoord;

uniform sampler2D Sampler0;
uniform vec4 uOutlineColor;
uniform float uOutlineWidth;
uniform vec4 uSpriteBounds;

out vec4 fragColor;

void main() {
    vec4 current = texture(Sampler0, vTexCoord);
    if (current.a > 0.1) {
        discard;
    }

    float minU = uSpriteBounds.x;
    float minV = uSpriteBounds.y;
    float maxU = uSpriteBounds.z;
    float maxV = uSpriteBounds.w;

    if (maxU <= minU) {
        minU = 0.0;
        maxU = 1.0;
    }
    if (maxV <= minV) {
        minV = 0.0;
        maxV = 1.0;
    }

    vec2 texelStep = vec2((maxU - minU) / 16.0, (maxV - minV) / 16.0);
    float maxAlpha = 0.0;
    int r = int(ceil(uOutlineWidth));

    for (int x = -r; x <= r; x++) {
        for (int y = -r; y <= r; y++) {
            if (x == 0 && y == 0) continue;
            if (float(x * x + y * y) > uOutlineWidth * uOutlineWidth + 0.5) continue;

            vec2 sampleUV = clamp(vTexCoord + vec2(float(x), float(y)) * texelStep, vec2(minU, minV), vec2(maxU, maxV));
            float sa = texture(Sampler0, sampleUV).a;
            if (sa > maxAlpha) {
                maxAlpha = sa;
            }
        }
    }

    if (maxAlpha > 0.1) {
        fragColor = vec4(uOutlineColor.rgb, uOutlineColor.a * maxAlpha * vColor.a);
    } else {
        discard;
    }
}
