package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityFilterHelper implements MinecraftAccess {
   public LivingEntity target;
   public final List<LivingEntity> list = new ArrayList<>();
   public long timeStamp = 0L;

   public void setTarget(LivingEntity target) {
      if (this.target == null) {
         this.target = target;
      }
   }

   public void runTarget() {
      this.target = null;
   }

   public void handleTarget(Predicate<LivingEntity> predicate) {
      this.handleNext(predicate);
      if (this.target != null && !predicate.test(this.target)) {
         this.runTarget();
      }
   }

   public void searchTargets(Iterable<Entity> entities, float maxDistance) {
      if (this.computeTarget(maxDistance)) {
         this.runTarget();
      }

      this.list.clear();

      for (Entity var4 : entities) {
         if (var4 instanceof LivingEntity) {
            LivingEntity var5 = (LivingEntity)var4;
            if (!(mc.player.getEyePos().distanceTo(RotationMath.initInit(var5)) > (double)maxDistance)) {
               this.list.add(var5);
            }
         }
      }

      if (System.currentTimeMillis() - this.timeStamp > 150L) {
         this.list.sort(EntityFilterHelper::lambda$searchTargets$0);
         this.timeStamp = System.currentTimeMillis();
      }
   }

   public boolean computeTarget(float maxDistance) {
      return this.target != null && RotationMath.initInit(this.target).distanceTo(mc.player.getEyePos()) > (double)maxDistance;
   }

   public void handleNext(Predicate<LivingEntity> predicate) {
      if (!this.list.isEmpty()) {
         for (LivingEntity var3 : this.list) {
            if (predicate.test(var3)) {
               this.setTarget(var3);
               return;
            }
         }
      }
   }

   public LivingEntity getTarget() {
      return this.target;
   }

   public static int lambda$searchTargets$0(LivingEntity a, LivingEntity b) {
      double var2 = mc.player.getEyePos().distanceTo(RotationMath.initInit(a));
      double var4 = mc.player.getEyePos().distanceTo(RotationMath.initInit(b));
      return Double.compare(var2, var4);
   }

   public static class EntityFilter implements MinecraftAccess {
      public List<String> targetSettings;
      public boolean mukey;

      public EntityFilter(List<String> targetSettings) {
         this.targetSettings = targetSettings;
      }

      public boolean computeFlag(LivingEntity entity) {
         if (entity == mc.player) {
            return false;
         } else if (!entity.isAlive() || entity.getHealth() <= 0.0F) {
            return false;
         } else {
            return entity == mc.player.getControllingVehicle() ? false : this.computeContains(entity);
         }
      }

      public boolean computeContains(LivingEntity entity) {
         if (entity instanceof PlayerEntity var2) {
            return FriendsStore.getInstance().computeContains(var2.getName().getString()) && !this.mukey ? false : this.targetSettings.contains("Players");
         } else if (entity instanceof AnimalEntity) {
            return this.targetSettings.contains("Animals");
         } else {
            return entity instanceof MobEntity ? this.targetSettings.contains("Mobs") : !(entity instanceof ArmorStandEntity);
         }
      }
   }
}
