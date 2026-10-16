package dev.daamky.client.module.misc;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.CollisionUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.MutableText;

@ModuleInfo(
   name = "Health Resolver",
   category = ModuleCategory.MICS,
   description = "Исправляет рассинхрон здоровья на сервере"
)
public class HealthResolver extends Module {
   public static final HealthResolver healthResolver = new HealthResolver();
   public final ModeSetting funTime = new ModeSetting("Mode").value("Fun Time").values("Really World", "Fun Time");

   public HealthResolver() {
      this.addSettings(new Setting[]{this.funTime});
   }

   public boolean checkEnabled() {
      return this.funTime.is("Really World") && this.isEnabled();
   }

   public boolean checkFun() {
      return this.funTime.is("Fun Time") && this.isEnabled();
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(HealthResolver::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static HealthResolver getInstance() {
      return healthResolver;
   }

   public static void lambda$onEvent$0(TickEvent event) {
      if (mc.player != null && mc.world != null) {
         if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getServerInfo() != null) {
            for (PlayerEntity var2 : mc.world.getPlayers()) {
               if (var2 != mc.player && CollisionUtil.computeFlag(var2.getName().getString())) {
                  Object var3 = null;
                  String var4 = "";
                  if (var2.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME) != null && (var3 = var2.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME)) != null
                     )
                   {
                     ReadableScoreboardScore var5 = var2.getScoreboard().getScore(var2, (ScoreboardObjective)var3);
                     MutableText var6 = ReadableScoreboardScore.getFormattedScore(var5, var3.method_55380(StyledNumberFormat.EMPTY));
                     var4 = var6.getString();
                  }

                  float var10 = 0.0F;
                  if (!var4.isEmpty()) {
                     String var12 = var4.replaceAll("[^0-9.]", "");

                     try {
                        if (!var12.isEmpty() && (var10 = Float.parseFloat(var12)) > 0.0F) {
                           var2.setHealth(var10);
                        }
                     } catch (NumberFormatException var8) {
                     }
                  }
               }
            }
         }
      }
   }
}
