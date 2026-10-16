package dev.daamky.client.internal;

import net.minecraft.client.MinecraftClient;

public interface RenderAccess {
   MinecraftClient mc = MinecraftClient.getInstance();
}
