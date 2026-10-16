package dev.daamky.client.mixin;

import dev.daamky.client.module.misc.Optimization;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleManager.class})
public class ParticleManagerMixin {
   @Inject(
      method = {"addParticle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddParticle(
      ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir
   ) {
      Optimization var16 = Optimization.getInstance();
      if (var16.checkEnabled2()) {
         cir.setReturnValue(null);
      } else {
         if (var16.checkEnabled8() && parameters != null) {
            Identifier var17 = Registries.PARTICLE_TYPE.getId(parameters.getType());
            if (var17 == null) {
               return;
            }

            String var18 = var17.getPath();
            if (var18.contains("explosion") || var18.contains("smoke") || var18.contains("flash") || var18.contains("firework")) {
               cir.setReturnValue(null);
            }
         }
      }
   }

   @Inject(
      method = {"addParticle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddParticleDirect(Particle particle, CallbackInfo ci) {
      if (Optimization.getInstance().checkEnabled2()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"addEmitter"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddEmitter(Entity entity, ParticleEffect parameters, CallbackInfo ci) {
      Optimization var5 = Optimization.getInstance();
      if (var5.checkEnabled2()) {
         ci.cancel();
      } else {
         if (var5.checkEnabled8() && parameters != null) {
            Identifier var6 = Registries.PARTICLE_TYPE.getId(parameters.getType());
            if (var6 == null) {
               return;
            }

            String var7 = var6.getPath();
            if (var7.contains("explosion") || var7.contains("smoke") || var7.contains("flash") || var7.contains("firework")) {
               ci.cancel();
            }
         }
      }
   }

   @Inject(
      method = {"addEmitter"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddEmitterAge(Entity entity, ParticleEffect parameters, int maxAge, CallbackInfo ci) {
      Optimization var6 = Optimization.getInstance();
      if (var6.checkEnabled2()) {
         ci.cancel();
      } else {
         if (var6.checkEnabled8() && parameters != null) {
            Identifier var7 = Registries.PARTICLE_TYPE.getId(parameters.getType());
            if (var7 == null) {
               return;
            }

            String var8 = var7.getPath();
            if (var8.contains("explosion") || var8.contains("smoke") || var8.contains("flash") || var8.contains("firework")) {
               ci.cancel();
            }
         }
      }
   }
}
