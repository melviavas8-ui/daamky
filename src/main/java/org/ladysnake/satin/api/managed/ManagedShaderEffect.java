package org.ladysnake.satin.api.managed;

import java.util.function.Consumer;

/** Stub for optional Satin shader API */
public interface ManagedShaderEffect {
    void render(float tickDelta);
    void setUniformValue(String name, float value);
    void setUniformValue(String name, float v0, float v1);
    void setUniformValue(String name, float v0, float v1, float v2);
    void setUniformValue(String name, float v0, float v1, float v2, float v3);
}
