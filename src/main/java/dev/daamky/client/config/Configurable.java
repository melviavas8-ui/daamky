package dev.daamky.client.config;

import dev.daamky.client.event.Subscription;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.util.MinecraftAccess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configurable implements MinecraftAccess {
   public final List<Setting<?>> settings = new ArrayList<>();
   public final List<Subscription> eventListeners = new ArrayList<>();

   public void addSettings(Setting<?>... settings) {
      if (settings != null) {
         for (Setting var5 : settings) {
            if (var5 != null) {
               this.settings.add(var5);
            }
         }
      }
   }

   public void addSettings(List<Setting<?>> settings) {
      if (settings != null) {
         for (Setting var3 : settings) {
            if (var3 != null) {
               this.settings.add(var3);
            }
         }
      }
   }

   public void addEventListeners(Subscription... eventListeners) {
      this.eventListeners.addAll(Arrays.asList(eventListeners));
   }

   public void addEventListeners(List<Subscription> eventListeners) {
      this.eventListeners.addAll(eventListeners);
   }

   public void clearEventListeners() {
      this.eventListeners.forEach(Subscription::unsubscribe);
      this.eventListeners.clear();
   }

   public void removeEventListeners(Subscription... eventListeners) {
      this.eventListeners.removeAll(Arrays.asList(eventListeners));
   }

   public void removeEventListeners(List<Subscription> eventListeners) {
      this.eventListeners.removeAll(eventListeners);
   }

   public void onEvent() {
   }

   public List<Setting<?>> getSettings() {
      return this.settings;
   }

   public Setting<?> getSetting(String name) {
      if (name == null) {
         return null;
      } else {
         for (Setting var3 : this.settings) {
            if (var3 != null && name.equalsIgnoreCase(var3.getName())) {
               return var3;
            }
         }

         return null;
      }
   }

   public List<Subscription> getEventListeners() {
      return this.eventListeners;
   }
}
