package dev.daamky.client.internal;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.render.ClickGUI;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.util.MinecraftAccess;
import java.util.HashSet;
import java.util.Set;

public class EnabledSet implements MinecraftAccess {
   public static final EnabledSet enabledSet = new EnabledSet();
   public boolean flag = false;
   public final Set<String> set = new HashSet<>();

   public static EnabledSet getInstance() {
      return enabledSet;
   }

   public boolean check() {
      return this.flag;
   }

   public void runEnabled() {
      if (!this.flag) {
         this.flag = true;
         this.set.clear();

         for (Module var2 : ModuleRegistry.getInstance().getList()) {
            if (var2.isEnabled() && !(var2 instanceof ClickGUI)) {
               this.set.add(var2.getName());
               var2.setEnabledInternal(false, false);
            }
         }

         if (mc != null && mc.currentScreen != null) {
            mc.setScreen(null);
         }
      }
   }

   public void runNext() {
      if (this.flag) {
         this.flag = false;

         for (Module var2 : ModuleRegistry.getInstance().getList()) {
            if (this.set.contains(var2.getName())) {
               var2.setEnabledInternal(true, false);
            }
         }

         this.set.clear();
         NotificationManager.handleSuccess("Клиент восстановлен");
      }
   }

   public void run() {
      if (this.flag) {
         this.runNext();
      } else {
         this.runEnabled();
      }
   }
}
