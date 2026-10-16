package dev.daamky.client.internal;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.MovementInputEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Box;

public class PushBoost extends NamedChoiceBase {
   public final FloatSetting pushBoost = new FloatSetting("Push Boost").value(0.07F).range(0.02F, 0.08F).step(0.005F);
   public final BooleanSetting autoHop = new BooleanSetting("Auto Hop").value(true);
   public Subscription subscription;
   public Subscription subscription2;

   public PushBoost() {
      this.addSettings(new Setting[]{this.pushBoost, this.autoHop});
   }

   @Override
   public String getName() {
      return "GrimEntity";
   }

   @Override
   public void onEnable() {
      this.subscription = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEnable$1));
      this.subscription2 = MovementInputEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEnable$3));
   }

   @Override
   public void runUnsubscribe() {
      if (this.subscription != null) {
         this.subscription.unsubscribe();
         this.subscription = null;
      }

      if (this.subscription2 != null) {
         this.subscription2.unsubscribe();
         this.subscription2 = null;
      }
   }

   public void lambda$onEnable$3(MovementInputEvent.MovementInputEventData event) {
      if (mc.player != null && mc.world != null && this.autoHop.getValue() && KeyBindingUtil.check4()) {
         Box var2 = mc.player.getBoundingBox().expand(0.2, 0.1, 0.2);
         boolean var3 = !mc.world.getOtherEntities(mc.player, var2, PushBoost::lambda$onEnable$2).isEmpty();
         if (var3 && mc.player.isOnGround()) {
            event.setJump(true);
         }
      }
   }

   public static boolean lambda$onEnable$2(Entity e) {
      return e instanceof LivingEntity && !(e instanceof ArmorStandEntity) && e.isAlive() || e instanceof BoatEntity || e.isPushable();
   }

   public void lambda$onEnable$1(UpdateEvent event) {
      if (mc.player != null && mc.world != null && KeyBindingUtil.check4()) {
         Box var2 = mc.player.getBoundingBox().expand(0.2, 0.1, 0.2);
         List var3 = mc.world.getOtherEntities(mc.player, var2, PushBoost::lambda$onEnable$0);
         int var4 = var3.size();
         if (var4 > 0) {
            float var5 = this.pushBoost.getValue();
            double var6 = Math.min((double)var5 * (double)var4, 0.22);
            float var8 = KeyBindingUtil.getMc();
            if (var8 != -1.0F) {
               double var9 = Math.toRadians((double)var8);
               double var11 = -Math.sin(var9) * var6;
               double var13 = Math.cos(var9) * var6;
               mc.player.addVelocity(var11, 0.0, var13);
            }

            if (this.autoHop.getValue() && mc.player.isOnGround()) {
               mc.player.jump();
            }
         }
      }
   }

   public static boolean lambda$onEnable$0(Entity e) {
      return e instanceof LivingEntity && !(e instanceof ArmorStandEntity) && e.isAlive() || e instanceof BoatEntity || e.isPushable();
   }
}
