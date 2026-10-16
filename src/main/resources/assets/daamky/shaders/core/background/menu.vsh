#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

// Передаем координаты в фрагментный шейдер
out vec2 v_FragCoord;

void main() {
    v_FragCoord = Position.xy;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}