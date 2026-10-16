#version 150

in vec3 Direction;
in vec4 vColor;

uniform float Time;
uniform float Mode;
uniform vec4 ColorModulator;

out vec4 OutColor;

float n(vec2 p){
   return sin(p.x * 3.0 + sin(p.y * 2.7)) * cos(p.y * 1.1 + cos(p.x * 2.3));
}
   
float f(vec2 p){
    float v = 0.0, a = 1.0;
    for (int i = 0; i < 9; i++) {
        v += n(p) * a;
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

void main() {
    vec3 d = normalize(Direction);
    
    // FPS Optimization: skip rendering below horizon
    if (d.y < -0.1) {
        OutColor = vec4(0.0, 0.0, 0.0, 1.0) * ColorModulator * vColor;
        return;
    }
    
    // Stereographic projection centered at zenith
    vec2 p = d.xz / (d.y + 1.0);
    p *= 3.0; // Adjust scale so it covers the sky beautifully

    float t = Time * 0.8; // slightly slower for better look
    
    vec3 c1, c2, c3;
    if (Mode < 0.5) { // Green
        c1 = vec3(0.1, 0.6, 0.2);
        c2 = vec3(0.5, 0.7, 0.2);
        c3 = vec3(1.0, 1.3, 1.0);
    } else if (Mode < 1.5) { // Red
        c1 = vec3(0.6, 0.1, 0.2);
        c2 = vec3(0.7, 0.3, 0.1);
        c3 = vec3(1.3, 1.0, 1.0);
    } else if (Mode < 2.5) { // Blue (Original)
        c1 = vec3(0.1, 0.2, 0.6);
        c2 = vec3(0.7, 0.5, 0.2);
        c3 = vec3(1.3, 1.3, 1.3);
    } else { // Pink
        c1 = vec3(0.6, 0.1, 0.5);
        c2 = vec3(0.7, 0.2, 0.6);
        c3 = vec3(1.3, 1.0, 1.3);
    }
    
    float f1 = f(p + t * 0.3 + 1.0);
    float f2 = f(p + t * 0.3 + 2.0);
    vec3 c = c1 / (length(p + vec2(sin(t * 2.2), sin(t)) + f1) * 2.1)
           + c2 / (length(p + vec2(cos(t * 2.0), sin(t)) + f1) * 2.1)
           + c3 / (length(p + vec2(sin(t * 1.2), cos(t * 1.3)) + f2) * 2.1);
           
    vec4 fragColor = vec4(c * 0.8 / (3.5 + c), 1.0);
    
    // Fade out near the horizon
    float mask = smoothstep(-0.1, 0.2, d.y);
    fragColor.a *= mask;
    fragColor.rgb *= mask;
    
    OutColor = fragColor * ColorModulator * vColor;
}
