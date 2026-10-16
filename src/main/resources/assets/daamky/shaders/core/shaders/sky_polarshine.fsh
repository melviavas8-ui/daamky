#version 150

in vec3 Direction;
in vec4 vColor;

uniform float Time;
uniform float Mode;
uniform vec4 ColorModulator;

out vec4 OutColor;

float hash12(vec2 p) {
    vec3 p3  = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float valueNoise(vec2 uv) {
    vec2 id = floor(uv);
    vec2 gv = fract(uv);
    
    float tl = hash12(id);
    float tr = hash12(id + vec2(1.0, 0.0));
    float bl = hash12(id + vec2(0.0, 1.0));
    float br = hash12(id + vec2(1.0, 1.0));

    vec2 sgv = gv * gv * (3.0 - 2.0 * gv);

    float m1 = mix(tl, tr, sgv.x);
    float m2 = mix(bl, br, sgv.x);
    return mix(m1, m2, sgv.y);
}

float fbm1(vec2 uv) {
    return 0.5 * valueNoise(0.4 * uv);
}

float f0(vec2 uv) {
    return fbm1(uv + 10.0 * fbm1(uv + Time + 5.0 * fbm1(uv)));
}

float f1(vec2 uv) {
    return fbm1(uv + fbm1(uv + fbm1(uv)));
}

vec3 polar(vec2 uv, float offset, vec3 col1, vec3 col2) {
    float h = f1(uv);
    float amp = f0(uv);
    float y = 1.5 * amp * sin(uv.x * h + offset + Time);
    
    float d = y - uv.y;
    float bottom = smoothstep(-2.0, -1.0, d);
    float top = smoothstep(4.0 + sin(Time + 10.0 * h + uv.x), -3.0, d);

    float m = bottom * top;
    return mix(col1, col2, smoothstep(0.0, 1.0, m)) * m;
}

void main() {
    vec3 d = normalize(Direction);
    
    // FPS Optimization: skip calculating for ground pixels (massive speedup)
    if (d.y < -0.15) {
        OutColor = vec4(0.0, 0.0, 0.0, 1.0) * ColorModulator * vColor;
        return;
    }
    
    float yaw = atan(d.z, d.x);
    float pitch = asin(d.y);
    vec2 uv = vec2(yaw, pitch) * 2.5;
    uv.y *= -1.0;
    
    vec3 GREEN, BLUE, MAGENTA, VIOLET;
    if (Mode < 0.5) { // Green
        GREEN = vec3(137.0,255.0,119.0) / 255.0;
        BLUE = vec3(55.0,234.0,249.0) / 255.0;
        MAGENTA = vec3(119.0,81.0,169.0) / 255.0;
        VIOLET = vec3(32.0,18.0,79.0) / 255.0;
    } else if (Mode < 1.5) { // Red
        GREEN = vec3(255.0,119.0,119.0) / 255.0;
        BLUE = vec3(249.0,150.0,55.0) / 255.0;
        MAGENTA = vec3(169.0,81.0,81.0) / 255.0;
        VIOLET = vec3(79.0,18.0,18.0) / 255.0;
    } else if (Mode < 2.5) { // Blue
        GREEN = vec3(119.0,200.0,255.0) / 255.0;
        BLUE = vec3(55.0,100.0,249.0) / 255.0;
        MAGENTA = vec3(81.0,119.0,169.0) / 255.0;
        VIOLET = vec3(18.0,32.0,79.0) / 255.0;
    } else { // Pink
        GREEN = vec3(255.0,119.0,220.0) / 255.0;
        BLUE = vec3(249.0,55.0,150.0) / 255.0;
        MAGENTA = vec3(169.0,81.0,150.0) / 255.0;
        VIOLET = vec3(79.0,18.0,60.0) / 255.0;
    }

    vec3 col = (vec3(0.0, 7.0, 100.0) / 255.0) * (smoothstep(0.0, 1.0, d.y) + 0.1);
    
    col += polar(uv * 9.0, 5.0, VIOLET, MAGENTA);
    col += polar(uv * 6.0, 30.0, VIOLET, BLUE);
    col += polar(uv * 8.0, 1.0, BLUE, GREEN);

    col += polar((uv + 0.7) * 9.0, 9.0, VIOLET, MAGENTA);
    col += polar((uv - 0.7) * 6.0, 21.0, VIOLET, BLUE);
    col += polar((uv - 0.7) * 8.0, 28.0, BLUE, GREEN);

    float mask = smoothstep(-0.15, 0.3, d.y);

    OutColor = vec4(col * 0.5 * mask, 1.0) * ColorModulator * vColor;
}
