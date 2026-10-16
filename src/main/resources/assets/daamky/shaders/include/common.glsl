float rdist(vec2 pos, vec2 size, vec4 radius) {
    radius.xy = (pos.x > 0.0) ? radius.xy : radius.wz;
    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;
    float r = min(radius.x, min(size.x, size.y) * 0.5);

    vec2 q = abs(pos) - (size * 0.5 - vec2(r));
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;
}

float ralpha(vec2 size, vec2 coord, vec4 radius, float smoothness) {
    vec2 pad = vec2(smoothness * 1.5);
    vec2 pos = (coord - vec2(0.5)) * (size + pad * 2.0);
    float dist = rdist(pos, size, radius);
    return 1.0 - smoothstep(-smoothness * 0.5, smoothness * 0.5, dist);
}

const vec2[4] RECT_VERTICES_COORDS = vec2[] (
    vec2(0.0, 0.0),
    vec2(0.0, 1.0),
    vec2(1.0, 1.0),
    vec2(1.0, 0.0)
);

vec2 rvertexcoord(int id) {
    return RECT_VERTICES_COORDS[id % 4];
}