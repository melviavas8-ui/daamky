package org.ladysnake.satin.api.managed;

import java.util.function.Consumer;
import net.minecraft.util.Identifier;

/** Stub for optional Satin shader API */
public class ShaderEffectManager {
    private static final ShaderEffectManager INSTANCE = new ShaderEffectManager();
    public static ShaderEffectManager getInstance() { return INSTANCE; }
    public ManagedShaderEffect manage(Identifier id, Consumer<ManagedShaderEffect> init) {
        return new ManagedShaderEffect() {
            public void render(float tickDelta) {}
            public void setUniformValue(String name, float value) {}
            public void setUniformValue(String name, float v0, float v1) {}
            public void setUniformValue(String name, float v0, float v1, float v2) {}
            public void setUniformValue(String name, float v0, float v1, float v2, float v3) {}
        };
    }
    public ManagedShaderEffect manage(Identifier id) {
        return manage(id, s -> {});
    }
}
