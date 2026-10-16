package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.Arrays;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleInfo(
   name = "Auto Totem",
   category = ModuleCategory.BATTLE,
   description = "Берет тотем бессмертия в руку при критическом падении здоровья или уроне"
)
public class AutoTotem extends Module {
   public static final AutoTotem mravt = new AutoTotem();
   public final FloatSetting health = new FloatSetting("Health").value(6.0F).range(1.0F, 20.0F).step(0.5F);
   public final BooleanSetting swapBack = new BooleanSetting("Swap Back").value(true);
   public final BooleanSetting preferPlainTotems = new BooleanSetting("Prefer Plain Totems").value(true);
   public final BooleanSetting notWhileEating = new BooleanSetting("Not While Eating").value(false);
   public final BooleanSetting smartPredict = new BooleanSetting("Smart Predict").value(true);
   public final BooleanSetting notify = new BooleanSetting("Notify").value(true);
   public final DelayHelper delayHelper = new DelayHelper();
   public final float[] mvopqas = new float[20];
   public int intVal = -1;
   public boolean flag = false;

   public AutoTotem() {
      this.addSettings(new Setting[]{this.health, this.swapBack, this.preferPlainTotems, this.notWhileEating, this.smartPredict, this.notify});
   }

   public static AutoTotem getInstance() {
      return mravt;
   }

   @Override
   public void onDisable() {
      this.intVal = -1;
      this.flag = false;
   }

   @Override
   public void onEnable() {
      this.intVal = -1;
      this.flag = false;
      if (mc.player != null) {
         float var1 = mc.player.getHealth() + (mc.player.hasStatusEffect(StatusEffects.ABSORPTION) ? mc.player.getAbsorptionAmount() : 0.0F);
         Arrays.fill(this.mvopqas, var1);
      }
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void runArraycopy() {
      if (mc.player != null && mc.world != null && mc.currentScreen == null) {
         System.arraycopy(this.mvopqas, 0, this.mvopqas, 1, 19);
         float var1 = mc.player.getHealth() + (mc.player.hasStatusEffect(StatusEffects.ABSORPTION) ? mc.player.getAbsorptionAmount() : 0.0F);
         this.mvopqas[0] = var1;
         if (!this.notWhileEating.getValue() || !this.getMc()) {
            Boolean var2 = this.getPredict();
            if (var2 != null) {
               this.flag = var2;
               if (var2) {
                  if (this.delayHelper.computeCurrent(400L) && this.checkFormat()) {
                     this.delayHelper.runCurrent();
                  }
               } else if (this.checkBack()) {
                  this.delayHelper.runCurrent();
               }
            }
         }
      }
   }

   public boolean checkFormat() {
      if (mc.player == null) {
         return false;
      } else {
         ItemStack var1 = mc.player.getOffHandStack();
         boolean var2 = this.preferPlainTotems.getValue();
         if (!this.computeFlag(var1) || var2 && var1.hasGlint()) {
            int var3 = -1;
            if (var2) {
               var3 = this.getMc();
            }

            if (var3 == -1 && !this.computeFlag(var1)) {
               var3 = InventoryUtil.computeInt(Items.TOTEM_OF_UNDYING);
            }

            if (var3 == -1) {
               return false;
            } else {
               if (this.swapBack.getValue() && this.intVal == -1 && !var1.isEmpty()) {
                  this.intVal = var3;
               }

               if (this.notify.getValue()) {
                  this.handleString("Взят тотем в руку (Здоровье: " + String.format("%.1f", this.mvopqas[0]) + ")");
               }

               InventoryUtil.initInit2(var3);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean checkBack() {
      if (mc.player == null) {
         return false;
      } else {
         ItemStack var1 = mc.player.getOffHandStack();
         if (!this.swapBack.getValue() || this.intVal == -1 || !var1.isEmpty() && !this.computeFlag(var1)) {
            this.intVal = -1;
            return false;
         } else {
            InventoryUtil.initInit2(this.intVal);
            this.intVal = -1;
            return true;
         }
      }
   }

   public Boolean getPredict() {
      if (mc.player == null) {
         return false;
      } else if (mc.player.getItemCooldownManager().isCoolingDown(Items.TOTEM_OF_UNDYING.getDefaultStack())) {
         return false;
      } else {
         ItemStack var1 = mc.player.getActiveItem();
         if (this.smartPredict.getValue() && this.getMc() && var1.getItem().getMaxUseTime(var1, mc.player) > 5) {
            int var2 = mc.player.getItemUseTimeLeft();
            if (var2 < 10) {
               return null;
            }

            float var3 = (this.mvopqas[19] - this.mvopqas[0]) / 20.0F;
            if (var2 < 30 && var3 * (float)var2 < this.mvopqas[0]) {
               return null;
            }
         }

         float var4 = this.health.getValue();
         if (this.mvopqas[0] <= var4) {
            return true;
         } else {
            return this.mvopqas[0] >= var4 + 0.5F ? false : null;
         }
      }
   }

   public boolean getMc() {
      if (mc.player == null) {
         return false;
      } else {
         ItemStack var1 = mc.player.getActiveItem();
         return mc.player.isUsingItem() && !var1.isEmpty() && var1.get(DataComponentTypes.FOOD) != null;
      }
   }

   public boolean computeFlag(ItemStack stack) {
      return stack != null && !stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING;
   }

   public int getMcInt() {
      if (mc.player == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 < 36; var1++) {
            ItemStack var2 = mc.player.getInventory().getStack(var1);
            if (!var2.isEmpty() && var2.getItem() == Items.TOTEM_OF_UNDYING && !var2.hasGlint()) {
               return var1 < 9 ? var1 + 36 : var1;
            }
         }

         return -1;
      }
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      this.runArraycopy();
   }
}
