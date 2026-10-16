package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.MovementInputEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.internal.KeyBindingUtil;
import dev.daamky.client.internal.PacketHelper;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.RotationHelper;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "ElytraStrafe",
   category = ModuleCategory.MOTION,
   description = "Позволяет свободно стрейфиться при полете на элитрах"
)
public class ElytraStrafe extends Module {
   public static final ElytraStrafe elytraStrafe = new ElytraStrafe();
   public final FloatSetting fireworkSlot = new FloatSetting("Firework Slot").value(7.0F).range(1.0F, 9.0F).step(1.0F);
   public final FloatSetting fireworkDelay = new FloatSetting("Firework Delay").value(1.0F).range(0.1F, 2.0F).step(0.1F);
   public final BooleanSetting autoTakeoff = new BooleanSetting("Auto Takeoff").value(true);
   public long timeStamp = 0L;

   public ElytraStrafe() {
      this.addSettings(new Setting[]{this.fireworkSlot, this.fireworkDelay, this.autoTakeoff});
   }

   public static ElytraStrafe getInstance() {
      return elytraStrafe;
   }

   @Override
   public void onEvent() {
      Subscription var1 = MovementInputEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void initCurrent() {
      if (mc.player != null) {
         if (this.autoTakeoff.getValue()) {
            boolean var1 = InventoryUtil.getMc();
            if (!mc.player.isGliding() && var1 && !mc.player.isOnGround() && !mc.player.isTouchingWater() && !mc.player.isInLava()) {
               mc.player.startGliding();
               PacketHelper.initInit(new ClientCommandC2SPacket(mc.player, Mode.START_FALL_FLYING));
            } else if (mc.player.isOnGround() && var1 && !mc.player.isTouchingWater() && !mc.player.isInLava() && !mc.player.isGliding()) {
               mc.player.jump();
            }
         }

         long var3 = (long)(this.fireworkDelay.getValue() * 1000.0F);
         if (mc.player.isGliding() && System.currentTimeMillis() - this.longVal > var3 && !mc.player.isUsingItem()) {
            this.runCurrent();
         }
      }
   }

   public void runCurrent() {
      int var1 = InventoryUtil.computeInt(Items.FIREWORK_ROCKET, true);
      if (var1 != -1) {
         int var2 = mc.player.getInventory().selectedSlot;
         InventoryUtil.handleInt(var1);
         InventoryUtil.useItem(Hand.MAIN_HAND);
         InventoryUtil.handleInt(var2);
         this.longVal = System.currentTimeMillis();
      } else {
         int var4 = InventoryUtil.computeInt(Items.FIREWORK_ROCKET, false);
         if (var4 != -1) {
            int var3 = (int)(this.fireworkSlot.getValue() - 1.0F);
            InventoryUtil.initInit(var4, var3);
            this.longVal = System.currentTimeMillis();
         }
      }
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      this.initCurrent();
   }

   public void lambda$onEvent$0(MovementInputEvent.MovementInputEventData event) {
      if (mc.player != null && mc.player.isGliding()) {
         float var2 = event.getDirectionalInput().isForwards() ? 1.0F : (event.getDirectionalInput().isBackwards() ? -1.0F : 0.0F);
         float var3 = event.getDirectionalInput().isLeft() ? 1.0F : (event.getDirectionalInput().isRight() ? -1.0F : 0.0F);
         float var4 = (float)Math.toDegrees(KeyBindingUtil.computeRadians(mc.player.getYaw(), var2, var3));
         float var5 = !mc.options.sneakKey.isPressed() && !mc.options.jumpKey.isPressed()
            ? 0.0F
            : (Math.abs(var3) + Math.abs(var2) > 0.1F ? -45.0F : -90.0F);
         if (mc.options.sneakKey.isPressed()) {
            var5 *= -1.0F;
         }

         VecRotationHelper.getInstance().handleModule(new Rotation(var4, var5), RotationHelper.rotationHelper3, Priority.LOWEST, this);
      }
   }
}
