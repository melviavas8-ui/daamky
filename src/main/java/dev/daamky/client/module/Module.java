package dev.daamky.client.module;

import dev.daamky.client.config.Configurable;
import dev.daamky.client.module.misc.ToggleSounds;
import dev.daamky.client.module.render.ClickGUI;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.util.MinecraftAccess;

public abstract class Module extends Configurable implements MinecraftAccess {
   public final String name;
   public final String description;
   public final ModuleCategory tag;
   public final String subCategory;
   public int bind;
   public boolean bindHold;
   public boolean enabled;

   public Module() {
      ModuleInfo var1 = this.getClass().getAnnotation(ModuleInfo.class);
      if (var1 == null) {
         try {
            throw new Exception("No data for " + this.getClass().getName());
         } catch (Exception var3) {
            throw new RuntimeException(var3);
         }
      } else {
         this.name = var1.name();
         this.description = var1.description().isBlank() ? this.getCategoryDescription(var1.category()) : var1.description();
         this.tag = var1.category();
         this.subCategory = var1.subCategory();
         this.bind = var1.bind();
      }
   }

   public Module(String name, String description, ModuleCategory tag) {
      this.name = name;
      this.description = description;
      this.tag = tag;
      this.subCategory = "";
      this.bind = -999;
   }

   public boolean hasBind() {
      return this.bind != -999;
   }

   public String getCategoryDescription(ModuleCategory tag) {
      return switch (tag) {
         case BATTLE -> "Боевой модуль: помогает в PvP и атаке.";
         case MOTION -> "Движение: меняет скорость, перемещение или управление.";
         case VISUALS -> "Визуальный модуль: меняет отображение и интерфейс.";
         case ACTOR -> "Действие: автоматизирует игровые действия.";
         case PVE -> "PvE модуль: автоматизирует добычу ресурсов и фарм.";
         case MICS -> "Разное: вспомогательная функция клиента.";
         case HUD -> "Интерфейс: элементы отображения на экране.";
         default -> "Модуль клиента.";
      };
   }

   public void toggle() {
      this.setEnabledInternal(!this.enabled, false);
   }

   public void setEnabled(boolean newState) {
      this.setEnabledInternal(newState, false);
   }

   public void setEnabledInternal(boolean newState, boolean config) {
      if (this.enabled != newState) {
         this.enabled = newState;
         if (this.enabled) {
            this.onEnable();
            this.onEvent();
            if (!config && !(this instanceof ClickGUI)) {
               try {
                  NotificationManager.initNext(this.name, true);
               } catch (Throwable var5) {
               }
            }
         } else {
            this.onDisable();
            this.clearEventListeners();
            if (!config && !(this instanceof ClickGUI)) {
               try {
                  NotificationManager.initNext(this.name, false);
               } catch (Throwable var4) {
               }
            }
         }

         if (!config && !(this instanceof ClickGUI)) {
            ToggleSounds.handleFlag(newState);
         }
      }
   }

   @Override
   public abstract void onEvent();

   public void onEnable() {
   }

   public void onDisable() {
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public ModuleCategory getCategory() {
      return this.tag;
   }

   public String getSubCategory() {
      return this.subCategory;
   }

   public int getBind() {
      return this.bind;
   }

   public boolean isBindHold() {
      return this.bindHold;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setBind(int bind) {
      this.bind = bind;
   }

   public void setBindHold(boolean bindHold) {
      this.bindHold = bindHold;
   }

   public String getDisplayName() {
      for (Setting var2 : this.getSettings()) {
         if (var2 instanceof ModeSetting
            && ("Mode".equalsIgnoreCase(var2.getName()) || "Type".equalsIgnoreCase(var2.getName()) || "Target".equalsIgnoreCase(var2.getName()))) {
            Object var3 = var2.getValue();
            if (var3 != null && !var3.toString().isEmpty()) {
               return var3.toString();
            }
         }
      }

      for (Setting var5 : this.getSettings()) {
         if (var5 instanceof ModeSetting) {
            Object var6 = var5.getValue();
            if (var6 != null && !var6.toString().isEmpty()) {
               return var6.toString();
            }
         }
      }

      return null;
   }
}
