package dev.daamky.client.module.misc;

import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleInfo(
   name = "Anonymous",
   category = ModuleCategory.MICS,
   description = "Скрывает ваш никнейм и сервер для стримов"
)
public class Anonymous extends Module {
   public static final Anonymous anonymous = new Anonymous();
   public MultiBooleanSetting noFunTime = new MultiBooleanSetting("Hides")
      .value(
         new BooleanSetting("Name").value(true),
         new BooleanSetting("Hide friends").value(true).visibleWhen(this::lambda$new$0),
         new BooleanSetting("No Fun Time").value(false)
      );
   public final ConcurrentHashMap<String, Integer> mvijy = new ConcurrentHashMap<>();
   public final AtomicInteger atomicInteger = new AtomicInteger(1);

   public Anonymous() {
      this.addSettings(new Setting[]{this.noFunTime});
   }

   public String getMc() {
      return this.isEnabled() ? "Protected" : mc.getSession().getUsername();
   }

   public String computeEnabled(String name) {
      return this.isEnabled()
            && this.noFunTime.isEnabled("Name")
            && this.noFunTime.isEnabled("Hide friends")
            && FriendsStore.getInstance().computeContains(name)
         ? this.generateProtectedFriendName(name)
         : name;
   }

   public String generateProtectedFriendName(String originalName) {
      int var2 = this.mvijy.computeIfAbsent(originalName.toLowerCase(), this::lambda$generateProtectedFriendName$1);
      return "Подружка " + var2;
   }

   @Override
   public void onEvent() {
   }

   public static Anonymous getInstance() {
      return anonymous;
   }

   public MultiBooleanSetting getNoFunTime() {
      return this.noFunTime;
   }

   public Integer lambda$generateProtectedFriendName$1(String key) {
      return this.atomicInteger.getAndIncrement();
   }

   public Boolean lambda$new$0() {
      return this.getNoFunTime().isEnabled("Name");
   }
}
