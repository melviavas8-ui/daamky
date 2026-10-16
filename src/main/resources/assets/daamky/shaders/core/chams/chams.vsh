#version 150

in vec3 Position;
in vec4 Color;

uniform mat4 ProjMat;
uniform vec2 uScreenSize;

out vec4 v_Color;

void main() {
    // Position is already in view-space (baked by BufferBuilder from the model's
    // matrix stack). Only ProjMat is needed — ModelViewMat would double-transform.
    gl_Position = ProjMat * vec4(Position, 1.0);
    v_Color = Color;
}
