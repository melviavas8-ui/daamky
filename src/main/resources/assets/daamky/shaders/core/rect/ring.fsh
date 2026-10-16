#version 150
#moj_import <gonedlc:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;

uniform vec2 uSize;
uniform float uInnerRadius;
uniform float uSmoothness;
uniform float uStartAngle; // in radians
uniform float uEndAngle;   // in radians, must be >= uStartAngle
uniform vec2 uInnerOffset;

out vec4 fragColor;

void main() {
    float pad = uSmoothness + 1.0;
    vec2 pos = (FragCoord - vec2(0.5)) * (uSize + vec2(pad * 2.0));

    float outerRadius = min(uSize.x, uSize.y) * 0.5;
    float dist = length(pos);

    // Ring / Crescent SDF: smooth outer and inner edges
    float outerAlpha = 1.0 - smoothstep(outerRadius - uSmoothness, outerRadius, dist);
    float distInner = length(pos - uInnerOffset);
    float innerAlpha = smoothstep(uInnerRadius, uInnerRadius + uSmoothness, distInner);
    float ringAlpha = outerAlpha * innerAlpha;

    // Arc clipping: clip to angle range [uStartAngle, uEndAngle]
    float angle = atan(pos.y, pos.x); // -PI to +PI

    // Normalize angle to be within [uStartAngle - 2PI, uStartAngle + 4PI] range to handle wraparound
    float a = angle;
    // Ensure a is in range [uStartAngle - PI, uStartAngle + PI]
    a = mod(a - uStartAngle + 3.14159265, 6.28318530) + uStartAngle;

    float arcSpan = uEndAngle - uStartAngle;
    float edgeSmooth = uSmoothness / max(dist, 0.001);
    float arcAlpha;
    if (arcSpan >= 6.28) {
        arcAlpha = 1.0;
    } else {
        float inArc = smoothstep(-edgeSmooth, edgeSmooth, a - uStartAngle)
                    * (1.0 - smoothstep(arcSpan - edgeSmooth, arcSpan + edgeSmooth, a - uStartAngle));
        arcAlpha = inArc;
    }

    float alpha = ringAlpha * arcAlpha;
    if (alpha < 0.005) discard;

    fragColor = vec4(FragColor.rgb, FragColor.a * alpha);
}
