package dev.daamky.client.internal;

import dev.daamky.client.util.MinecraftAccess;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class CritHelper implements MinecraftAccess {
   public static final CritHelper critHelper = new CritHelper();
   public final AtomicInteger atomicInteger = new AtomicInteger(0);

   public static CritHelper getInstance() {
      return critHelper;
   }

   public void run() {
   }

   public void closeEquals(Entity target) {
      ClientPlayerEntity var2 = mc.player;
      if (var2 != null && target != null) {
         int var3 = this.atomicInteger.incrementAndGet();
         long var4 = System.currentTimeMillis();
         int var6 = var2.age;
         Vec3d var7 = var2.getPos();
         Vec3d var8 = var2.getVelocity();
         double var9 = Math.sqrt(var8.x * var8.x + var8.z * var8.z);
         boolean var11 = var2.isOnGround();
         boolean var12 = var2.isSprinting();
         boolean var13 = var2.isSneaking();
         boolean var14 = mc.options != null && mc.options.jumpKey != null && mc.options.jumpKey.isPressed() || var8.y > 0.005;
         float var15 = var2.fallDistance;
         float var16 = var2.getAttackCooldownProgress(0.5F);
         float var17 = var2.getAttackCooldownProgress(0.0F);
         float var18 = var2.getAttackCooldownProgress(1.0F);
         float var19 = var2.getYaw();
         float var20 = var2.getPitch();
         String var21 = target.getName().getString();
         String var22 = target.getClass().getSimpleName();
         Vec3d var23 = target.getPos();
         double var24 = target.getVelocity().y;
         boolean var26 = target.isOnGround();
         double var27 = var2.getEyePos().distanceTo(target.getEyePos());
         double var29 = this.initMax(target, var2.getEyePos());
         boolean var31 = var2.isTouchingWater();
         boolean var32 = var2.isInLava();
         boolean var33 = var2.isSubmergedInWater();
         boolean var34 = var2.isClimbing();
         boolean var35 = var2.isGliding();
         boolean var36 = var2.hasVehicle();
         boolean var37 = var2.hasStatusEffect(StatusEffects.BLINDNESS);
         boolean var38 = var2.hasStatusEffect(StatusEffects.SLOW_FALLING);
         boolean var39 = var2.hasStatusEffect(StatusEffects.LEVITATION);
         ItemStack var40 = var2.getMainHandStack();
         String var41 = var40.isEmpty() ? "Air/Fist" : var40.getItem().getName().getString();
         boolean var42 = !var40.isEmpty()
            && (
               var40.getItem() instanceof SwordItem
                  || var40.getItem() instanceof AxeItem
                  || var40.getItem() instanceof MaceItem
                  || var40.getItem() instanceof TridentItem
            );
         boolean var43 = !var31 && !var33 && !var32 && !var34 && !var35 && !var36 && !var37 && !var38;
         boolean var44 = var8.y < -0.01 || var15 > 0.0F;
         boolean var45 = var16 > 0.9F && var44 && !var11 && !var12 && var43 && target instanceof LivingEntity;
         String var46;
         if (var45) {
            var46 = "CRIT";
         } else if (var12) {
            var46 = "SMASH";
         } else if (var11) {
            var46 = "GROUND";
         } else if (var8.y >= -0.01) {
            var46 = "AIR_RISING";
         } else {
            var46 = "AIR_NONCRIT";
         }

         String var47;
         if ("CRIT".equals(var46)) {
            var47 = "§a§l[CRIT ✓]";
         } else if ("SMASH".equals(var46)) {
            var47 = "§c§l[SMASH ✗]";
         } else if ("GROUND".equals(var46)) {
            var47 = "§e§l[GROUND]";
         } else {
            var47 = "§6§l[" + var46 + "]";
         }

         String var48 = var12 ? "§cON" : "§aOFF";
         String var49 = var11 ? "§eGND" : "§bAIR";
         String var50 = String.format(
            Locale.US,
            "%s §f#%d §7| CD:§f%.2f §7| Fall:§f%.2f §7| Sp:%s §7| VelY:§f%.3f §7| %s §7| D:§f%.2fm",
            var47,
            var3,
            var16,
            var15,
            var48,
            var8.y,
            var49,
            var29
         );
         var2.sendMessage(Text.literal(var50), true);

         try {
            File var51 = new File(mc.runDirectory, "daamky-combat-debug.csv");
            boolean var52 = !var51.exists() || var51.length() == 0L;

            try (FileWriter var53 = new FileWriter(var51, true)) {
               if (var52) {
                  var53.write(
                     "hitIndex,timestamp,tick,posX,posY,posZ,velX,velY,velZ,horizSpeed,onGround,isSprinting,isSneaking,isJumping,fallDist,cd05,cd00,cd10,playerYaw,playerPitch,targetName,targetType,targetX,targetY,targetZ,targetVelY,targetOnGround,distEye,distBox,inWater,inLava,isClimbing,isGliding,hasVehicle,hasBlindness,hasSlowFalling,hasLevitation,heldItem,isWeapon,vanillaCrit,hitClassification\n"
                  );
               }

               var53.write(
                  String.format(
                     Locale.US,
                     "%d,%d,%d,%.3f,%.4f,%.3f,%.4f,%.6f,%.4f,%.4f,%b,%b,%b,%b,%.4f,%.4f,%.4f,%.4f,%.2f,%.2f,\"%s\",\"%s\",%.3f,%.4f,%.3f,%.4f,%b,%.3f,%.3f,%b,%b,%b,%b,%b,%b,%b,%b,\"%s\",%b,%b,\"%s\"\n",
                     var3,
                     var4,
                     var6,
                     var7.x,
                     var7.y,
                     var7.z,
                     var8.x,
                     var8.y,
                     var8.z,
                     var9,
                     var11,
                     var12,
                     var13,
                     var14,
                     var15,
                     var16,
                     var17,
                     var18,
                     var19,
                     var20,
                     var21,
                     var22,
                     var23.x,
                     var23.y,
                     var23.z,
                     var24,
                     var26,
                     var27,
                     var29,
                     var31,
                     var32,
                     var34,
                     var35,
                     var36,
                     var37,
                     var38,
                     var39,
                     var41,
                     var42,
                     var45,
                     var46
                  )
               );
            }
         } catch (Throwable var58) {
         }
      }
   }

   public double initMax(Entity entity, Vec3d point) {
      Box var3 = entity.getBoundingBox();
      double var4 = Math.max(var3.minX, Math.min(point.x, var3.maxX));
      double var6 = Math.max(var3.minY, Math.min(point.y, var3.maxY));
      double var8 = Math.max(var3.minZ, Math.min(point.z, var3.maxZ));
      return point.distanceTo(new Vec3d(var4, var6, var8));
   }
}
