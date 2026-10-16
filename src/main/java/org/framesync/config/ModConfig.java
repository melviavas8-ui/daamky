package org.framesync.config;

/** Stub for optional framesync dependency */
public class ModConfig {
    public static ModConfig getInstance() {
        return Holder.INSTANCE;
    }
    public boolean isEnabled() { return false; }
    public void load() {}
    public void save() {}
    private static class Holder {
        static final ModConfig INSTANCE = new ModConfig();
    }
}
