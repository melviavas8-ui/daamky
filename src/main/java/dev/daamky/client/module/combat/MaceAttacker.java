package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;

@ModuleInfo(
   name = "MaceAttacker",
   description = "Автоматизирует действия при использовании булавы (Delta Mace Helper)",
   category = ModuleCategory.BATTLE,
   subCategory = "Combat"
)
public class MaceAttacker extends Module {
   public static final MaceAttacker maceAttacker = new MaceAttacker();
   public final BooleanSetting booleanSetting = new BooleanSetting("Усиление урона").value(true);
   public final BooleanSetting booleanSetting2 = new BooleanSetting("Авто-переключение булавы").value(true);
   public final FloatSetting floatSetting = new FloatSetting("Мин. высота падения").value(1.5F).range(0.5F, 5.0F).step(0.1F);
   public boolean flag = false;
   public int intVal = -1;

   public MaceAttacker() {
      this.addSettings(new Setting[]{this.booleanSetting, this.booleanSetting2, this.floatSetting});
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.run();
   }

   public void runEnabled() {
      if (mc.player != null && mc.world != null) {
         if (mc.player.isOnGround() || mc.player.isTouchingWater() || mc.player.isClimbing() || mc.player.getAbilities().flying) {
            this.run();
         } else if (this.booleanSetting2.getValue()) {
            int var1 = this.getMc();
            if (var1 == -1) {
               this.run();
            } else if (mc.player.getItemCooldownManager().isCoolingDown(Items.MACE.getDefaultStack())) {
               this.run();
            } else {
               LivingEntity var2 = null;
               AttackAura var3 = AttackAura.getInstance();
               if (var3 != null && var3.isEnabled() && var3.class_1309 != null && var3.class_1309.isAlive()) {
                  var2 = var3.class_1309;
               } else if (mc.crosshairTarget instanceof EntityHitResult var4 && var4.getEntity() instanceof LivingEntity var5 && var5.isAlive()) {
                  var2 = var5;
               }

               if (var2 != null) {
                  double var8 = Math.hypot(var2.getX() - mc.player.getX(), var2.getZ() - mc.player.getZ());
                  if (!(var8 > 6.0)) {
                     float var10 = this.floatSetting.getValue();
                     boolean var7 = mc.player.fallDistance >= var10
                        || mc.player.getVelocity().y < -0.2 && mc.player.getY() > var2.getY() + 1.2;
                     if (var7 && !this.flag && mc.player.getInventory().selectedSlot != var1) {
                        this.intVal = mc.player.getInventory().selectedSlot;
                        mc.player.getInventory().selectedSlot = var1;
                        this.flag = true;
                     }
                  }
               }
            }
         }
      }
   }

   public void run() {
      if (this.flag && this.intVal != -1 && mc.player != null) {
         if (this.intVal >= 0 && this.intVal < 9) {
            mc.player.getInventory().selectedSlot = this.intVal;
         }

         this.flag = false;
         this.intVal = -1;
      }
   }

   public int getMc() {
      if (mc.player == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 < 9; var1++) {
            ItemStack var2 = mc.player.getInventory().getStack(var1);
            if (!var2.isEmpty() && var2.isOf(Items.MACE)) {
               return var1;
            }
         }

         return -1;
      }
   }

   public static MaceAttacker getInstance() {
      return maceAttacker;
   }

   public void lambda$onEvent$0(UpdateEvent e) {
      this.runEnabled();
   }
}
