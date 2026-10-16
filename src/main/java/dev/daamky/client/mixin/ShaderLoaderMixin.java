package dev.daamky.client.mixin;

import dev.daamky.client.module.render.Ambience;
import dev.daamky.client.render.shader.ManagedShader;
import net.minecraft.client.gl.ShaderLoader;
import net.minecraft.client.gl.ShaderLoader.Definitions;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ShaderLoader.class})
public class ShaderLoaderMixin {
   @Inject(
      method = {"apply"},
      at = {@At("RETURN")}
   )
   private void onApply(Definitions definitions, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
      System.out.println("[GONE CLIENT] Initializing custom shaders...");

      try {
         Class.forName("dev.daamky.client.render.shader.NoiseShader");
         Class.forName("dev.daamky.client.render.shader.WorldShaderUniforms");
         Ambience.getInstance();
         ManagedShader.run();
         System.out.println("[GONE CLIENT] Custom shaders initialized successfully!");
      } catch (Exception var6) {
         System.err.println("[GONE CLIENT ERROR] Failed to initialize custom shaders!");
         var6.printStackTrace();
      }
   }
}
