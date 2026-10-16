package dev.daamky.client.internal;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.util.MinecraftAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationDataCsvHelper implements MinecraftAccess {
   public static final RotationDataCsvHelper mkialf = new RotationDataCsvHelper();
   public int intVal = 0;
   public final List<AimSnapshot> list = new ObjectArrayList(20);
   public LivingEntity muyta;
   public final DelayHelper delayHelper = new DelayHelper();
   public final DelayHelper delayHelper2 = new DelayHelper();

   public void runClear() {
      this.list.clear();
      this.intVal = 0;
      this.handleString("Начата сборка датасета");
   }

   public void runClear2() {
      this.intVal = 0;
      this.initPlayer();
      this.list.clear();
      this.handleString("Прекращена сборка датасета");
   }

   public void setEvent(AttackEvent.AttackEventData event) {
      Entity var2 = event.entity();
      if (var2 instanceof LivingEntity) {
         LivingEntity var3;
         this.muyta = var3 = (LivingEntity)var2;
         this.delayHelper.runCurrent();
         this.delayHelper2.runCurrent();
      }
   }

   public void handlePacketEventData(PacketEvent.PacketEventData event) {
   }

   public void getMc() {
      this.initYaw(mc.player);
   }

   public void initPlayer() {
      if (!this.list.isEmpty()) {
         File var1 = new File("rotation_data.csv");

         try (BufferedWriter var2 = new BufferedWriter(new FileWriter(var1))) {
            var2.write(
               "yaw_delta,pitch_delta,target_yaw,target_pitch,since_attack,distance,on_ground,mini_hitbox,player_yaw,player_pitch,player_pos_x,player_pos_y,player_pos_z,player_vel_x,player_vel_y,player_vel_z,target_pos_x,target_pos_y,target_pos_z,target_vel_x,target_vel_y,target_vel_z,delta_x,delta_y,delta_z\n"
            );

            for (AimSnapshot var4 : this.list) {
               var2.write(
                  String.format(
                     Locale.US,
                     "%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n",
                     var4.getYawDelta(),
                     var4.getPitchDelta(),
                     var4.getTargetYaw(),
                     var4.getTargetPitch(),
                     var4.getSinceAttack(),
                     var4.getDistance(),
                     var4.getOnGround(),
                     var4.getMiniHitbox(),
                     var4.getPlayerYaw(),
                     var4.getPlayerPitch(),
                     var4.getPlayerPosX(),
                     var4.getPlayerPosY(),
                     var4.getPlayerPosZ(),
                     var4.getPlayerVelX(),
                     var4.getPlayerVelY(),
                     var4.getPlayerVelZ(),
                     var4.getTargetPosX(),
                     var4.getTargetPosY(),
                     var4.getTargetPosZ(),
                     var4.getTargetVelX(),
                     var4.getTargetVelY(),
                     var4.getTargetVelZ(),
                     var4.getDeltaX(),
                     var4.getDeltaY(),
                     var4.getDeltaZ()
                  )
               );
            }

            this.intVal = this.intVal + this.list.size();
            this.handleString("Сохранено " + this.list.size() + " сэмплов");
            this.list.clear();
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }

   public void initYaw(LivingEntity player) {
      if (!this.delayHelper.computeCurrent(2000L) && !player.isDead() && mc.world.getPlayers().contains(player)) {
         if (this.muyta != null && !this.muyta.isDead()) {
            Rotation var2 = RotationMath.initAtan(this.muyta.getPos());
            float var3 = player.getYaw();
            float var4 = player.getPitch();
            float var5 = player.prevYaw;
            float var6 = player.prevPitch;
            VecRotationHelper var7 = VecRotationHelper.getInstance();
            if (var7.getLast() != null) {
               Rotation var8 = var7.getCurrent();
               Rotation var9 = var7.getPrevious();
               var3 = var8.getYaw();
               var4 = var8.getPitch();
               var5 = var9.getYaw();
               var6 = var9.getPitch();
            }

            float var17 = MathHelper.wrapDegrees(var2.getYaw());
            float var18 = MathHelper.wrapDegrees(var2.getPitch());
            Vec3d var10 = player.getVelocity();
            Vec3d var11 = this.muyta.getVelocity();
            Vec3d var12 = player.getPos();
            Vec3d var13 = this.muyta.getPos();
            float var14 = (float)(this.muyta.getX() - this.muyta.prevX);
            float var15 = (float)(this.muyta.getY() - this.muyta.prevY);
            float var16 = (float)(this.muyta.getZ() - this.muyta.prevZ);
            this.list
               .add(
                  new AimSnapshot(
                     Math.abs(var3 - var5),
                     Math.abs(var4 - var6),
                     var17,
                     var18,
                     (float)this.delayHelper.getCurrent(),
                     (float)var12.distanceTo(var13),
                     player.isOnGround() ? 1.0F : 0.0F,
                     !player.isGliding() && !player.isSwimming() ? 0.0F : 1.0F,
                     var3,
                     var4,
                     (float)var12.x,
                     (float)var12.y,
                     (float)var12.z,
                     (float)var10.x,
                     (float)var10.y,
                     (float)var10.z,
                     (float)var13.x,
                     (float)var13.y,
                     (float)var13.z,
                     (float)var11.x,
                     (float)var11.y,
                     (float)var11.z,
                     var14,
                     var15,
                     var16
                  )
               );
         }
      }
   }

   public float computeFloat(float angle) {
      angle %= 360.0F;
      if (angle < 0.0F) {
         angle += 360.0F;
      }

      return angle;
   }

   public float computeFloat2(float angle) {
      angle = this.computeFloat(angle);
      if (angle > 180.0F) {
         angle -= 360.0F;
      }

      return angle;
   }

   public float computeFloat(float angle1, float angle2) {
      float var3 = this.computeFloat(angle1);
      float var4 = this.computeFloat(angle2);
      float var5 = var3 - var4;
      return this.computeFloat2(var5);
   }

   public static RotationDataCsvHelper getInstance() {
      return mkialf;
   }

   public int getInt() {
      return this.intVal;
   }

   public List<AimSnapshot> getList() {
      return this.list;
   }
}
