package dev.daamky.client.module.movement;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.CollisionUtil;
import dev.daamky.client.internal.EntityHelper;
import dev.daamky.client.internal.KeyBindingUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.util.MinecraftAccess;

@ModuleInfo(
   name = "NoWeb",
   category = ModuleCategory.MOTION,
   description = "Позволяет быстро ходить в паутине"
)
public class NoWeb extends Module implements MinecraftAccess {
   public static final NoWeb noWeb = new NoWeb();

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(NoWeb::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static NoWeb getInstance() {
      return noWeb;
   }

   public static void lambda$onEvent$0(UpdateEvent e) {
      if (mc.player != null && mc.world != null) {
         if (CollisionUtil.isInWeb()) {
            double var1 = (double)EntityHelper.computeRandom(0.62F, 0.64F);
            double[] var3 = KeyBindingUtil.computeRadians(var1);
            double var4 = 0.0;
            if (mc.options.jumpKey.isPressed()) {
               var4 = 1.2;
            } else if (mc.options.sneakKey.isPressed()) {
               var4 = -2.0;
            }

            mc.player.setVelocity(var3[0], var4, var3[1]);
         }
      }
   }
}
