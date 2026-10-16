package org.framesync.client;

/** Stub for optional framesync dependency */
public class RenderManager {
    public static RenderManager getInstance() {
        return Holder.INSTANCE;
    }
    public void init() {}
    public void shutdown() {}
    public boolean isEnabled() { return false; }
    private static class Holder {
        static final RenderManager INSTANCE = new RenderManager();
    }
}
