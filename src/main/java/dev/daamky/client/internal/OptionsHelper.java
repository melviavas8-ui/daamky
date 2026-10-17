package dev.daamky.client.internal;

import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.util.MinecraftAccess;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class OptionsHelper {
   public final SprintState sprintState = new SprintState();
   public final MultiBooleanSetting noAttackIfEat = new MultiBooleanSetting("Options")
      .value(
         new BooleanSetting("Only crits").value(true),
         new BooleanSetting("Smart crits").value(true).visibleWhen(this::lambda$new$0),
         new BooleanSetting("Raytrace").value(true),
         new BooleanSetting("Shield break").value(true),
         new BooleanSetting("Always shield").value(false),
         new BooleanSetting("Ignore walls").value(false),
         new BooleanSetting("No attack if eat").value(false)
      );
   public long timeStamp = -1L;
   public long long2 = -1L;
   public float floatVal = 20.0F;

   public void runEnabled() {
      if (MinecraftAccess.mc.player != null) {
         if (!this.noAttackIfEat.isEnabled("Smart crits") || this.checkRandom()) {
            this.sprintState.run();
         }
      }
   }

   public void runCurrent() {
      if (MinecraftAccess.mc.world != null) {
         long var1 = MinecraftAccess.mc.world.getTime();
         long var3 = System.currentTimeMillis();
         if (this.timeStamp >= 0L && var1 != this.timeStamp) {
            long var5 = var3 - this.long2;
            float var7 = 1000.0F / (float)Math.max(var5, 1L);
            this.floatVal = MathHelper.clamp(var7, 1.0F, 20.0F);
         }

         if (var1 != this.timeStamp) {
            this.timeStamp = var1;
            this.long2 = var3;
         }
      }
   }

   public boolean checkRandom() {
      this.runCurrent();
      float var1 = this.floatVal / 20.0F;
      float var2 = MathHelper.clamp(0.92F * var1, 0.83F, 0.95F);
      boolean var3 = MinecraftAccess.mc.player.getAttackCooldownProgress(0.5F) >= var2;
      if (!var3) {
         return false;
      } else {
         boolean var5 = MinecraftAccess.mc.player.isTouchingWater() || MinecraftAccess.mc.player.isInLava();
         boolean var6 = !MinecraftAccess.mc.player.isOnGround() && !var5 && MinecraftAccess.mc.player.getVelocity().y < 0.0;
         float var7 = 0.065F + (float)Math.random() * 0.115F;
         boolean var8 = MinecraftAccess.mc.player.fallDistance > var7;
         if (var6 && var8) {
            return true;
         } else {
            return MinecraftAccess.mc.player.isOnGround() ? Math.random() < 0.85F : false;
         }
      }
   }

   public SprintState getSprintState() {
      return this.sprintState;
   }

   public MultiBooleanSetting getNoAttackIfEat() {
      return this.noAttackIfEat;
   }

   public long getLong() {
      return this.timeStamp;
   }

   public long getLong2() {
      return this.long2;
   }

   public float getFloat() {
      return this.floatVal;
   }

   public Boolean lambda$new$0() {
      return this.getNoAttackIfEat().isEnabled("Only crits");
   }

   public static class CombatConfigurable {
      public final LivingEntity target;
      public final Rotation rotation;
      public final float distance;
      public final boolean onlyCrits;
      public final boolean smartCrits;
      public final boolean flag;
      public final boolean shieldBreak;
      public final boolean alwaysShield;
      public final boolean ignoreWalls;
      public final boolean noAttackIfEat;

      public CombatConfigurable(
         LivingEntity target,
         Rotation rotation,
         float distance,
         boolean onlyCrits,
         boolean smartCrits,
         boolean flag,
         boolean shieldBreak,
         boolean alwaysShield,
         boolean ignoreWalls,
         boolean noAttackIfEat
      ) {
         this.target = target;
         this.rotation = rotation;
         this.distance = distance;
         this.onlyCrits = onlyCrits;
         this.smartCrits = smartCrits;
         this.flag = flag;
         this.shieldBreak = shieldBreak;
         this.alwaysShield = alwaysShield;
         this.ignoreWalls = ignoreWalls;
         this.noAttackIfEat = noAttackIfEat;
      }

      public CombatConfigurable(LivingEntity target, Rotation rotation, float distance, List<String> options) {
         this(
            target,
            rotation,
            distance,
            options.contains("Only crits"),
            options.contains("Smart crits"),
            options.contains("Raytrace"),
            options.contains("Shield break"),
            options.contains("Always shield"),
            options.contains("Ignore walls"),
            options.contains("No attack if eat")
         );
      }
   }
}
