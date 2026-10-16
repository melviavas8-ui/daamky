package dev.daamky.client.internal;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

public class Profile extends OtherClientPlayerEntity {
   public Profile(ClientWorld world, GameProfile profile) {
      super(world, profile);
   }

   public void run() {
      this.unsetRemoved();
      this.clientWorld.addEntity(this);
   }

   public void run2() {
      this.clientWorld.removeEntity(this.getId(), RemovalReason.DISCARDED);
      this.onRemoved();
   }

   public void takeKnockback(double strength, double x, double z) {
   }

   public boolean isPushable() {
      return false;
   }

   public void pushAwayFrom(Entity entity) {
   }

   public boolean collidesWith(Entity other) {
      return false;
   }

   public boolean isCollidable() {
      return false;
   }

   public boolean isAttackable() {
      return true;
   }

   public boolean canHit() {
      return true;
   }

   public void onDamaged(DamageSource damageSource) {
      super.onDamaged(damageSource);
   }
}
