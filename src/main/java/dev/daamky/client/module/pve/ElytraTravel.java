package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "ElytraTravel",
   description = "Автополёт на элитре к введённым координатам",
   category = ModuleCategory.PVE,
   subCategory = "PvE"
)
public class ElytraTravel extends Module {
   public static final ElytraTravel elytraTravel = new ElytraTravel();
   public final StringSetting stringSetting = new StringSetting("Координаты (X Z или X Y Z)").placeholder("напр. 1200 64 -840");
   public final StringSetting mkedlu = new StringSetting("Высота Y").placeholder("напр. 120");
   public final FloatSetting floatSetting = new FloatSetting("Фейерверк (сек)").value(2.0F).range(1.0F, 5.0F).step(0.5F);
   public final BooleanSetting booleanSetting = new BooleanSetting("Выключать по прибытии").value(true);
   public static final Pattern pattern = Pattern.compile("-?\\d+(?:\\.\\d+)?");
   public long longVal;
   public long long2;
   public boolean flag;
   public int intVal = -1;
   public int majeaq = -1;
   public int mliisv = 0;
   public final DelayHelper delayHelper = new DelayHelper();

   public ElytraTravel() {
      this.addSettings(new Setting[]{this.stringSetting, this.mkedlu, this.floatSetting, this.booleanSetting});
   }

   @Override
   public void onEvent() {
      this.longVal = 0L;
      this.long2 = 0L;
      this.flag = false;
      this.run();
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.getMc();
      this.run();
   }

   public void run() {
      if (this.mliisv != 0) {
         InventoryUtil.handleFlag(true);
      }

      this.mliisv = 0;
      this.intVal = -1;
      this.majeaq = -1;
   }

   public void tick() {
      if (mc.player != null && mc.world != null && mc.interactionManager != null && mc.options != null) {
         this.run3();
         if (mc.currentScreen != null) {
            this.getMc();
         } else {
            double[] var1 = this.getSize();
            if (var1 == null) {
               this.setMessage("ElytraTravel: укажите координаты (X Z или X Y Z)");
               this.getMc();
            } else if (!InventoryUtil.getMc()) {
               this.setMessage("Наденьте элитры!");
               this.getMc();
            } else {
               double var2 = var1[0] - mc.player.getX();
               double var4 = var1[1] - mc.player.getY();
               double var6 = var1[2] - mc.player.getZ();
               double var8 = Math.sqrt(var2 * var2 + var6 * var6);
               if (var8 <= 4.0 && Math.abs(var4) <= 4.0) {
                  this.getMc();
                  if (this.booleanSetting.getValue()) {
                     NotificationManager.handleSuccess("ElytraTravel: прибыли на место");
                     mc.execute(this::lambda$tick$1);
                  }
               } else if (!mc.player.isGliding()) {
                  this.run2();
               } else {
                  this.getMc();
                  this.handleAtan(var2, var4, var6);
                  this.runCurrent();
               }
            }
         }
      }
   }

   public void run2() {
      if (mc.player.isOnGround()) {
         mc.options.jumpKey.setPressed(true);
         this.flag = true;
      } else {
         this.flag = !this.flag;
         mc.options.jumpKey.setPressed(this.flag);
      }
   }

   public void getMc() {
      if (mc.options != null) {
         mc.options.jumpKey.setPressed(false);
      }

      this.flag = false;
   }

   public void handleAtan(double dx, double dy, double dz) {
      float var7 = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
      float var8 = dy > 3.0 ? -35.0F : (dy < -6.0 ? 30.0F : 0.0F);
      float var9 = mc.player.getYaw();
      float var10 = mc.player.getPitch();
      float var11 = var9 + MathHelper.clamp(MathHelper.wrapDegrees(var7 - var9), -9.0F, 9.0F);
      float var12 = MathHelper.clamp(var10 + MathHelper.clamp(var8 - var10, -7.0F, 7.0F), -90.0F, 90.0F);
      mc.player.setYaw(var11);
      mc.player.setPitch(var12);
      mc.player.headYaw = var11;
      mc.player.bodyYaw = var11;
   }

   public void runCurrent() {
      long var1 = (long)(this.floatSetting.getValue() * 1000.0F);
      long var3 = System.currentTimeMillis();
      if (var3 - this.longVal >= var1) {
         if (this.mliisv == 0) {
            if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
               mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
               this.longVal = var3;
            } else {
               int var5 = this.getMc();
               if (var5 == -1) {
                  this.setMessage("ElytraTravel: нет фейерверков!");
               } else {
                  this.intVal = var5;
                  this.majeaq = mc.player.getInventory().selectedSlot;
                  this.delayHelper.runCurrent();
                  this.mliisv = 1;
                  this.longVal = var3;
               }
            }
         }
      }
   }

   public void run3() {
      if (mc.player != null && mc.interactionManager != null) {
         if (this.mliisv != 0) {
            AttackAura.int3 = 3;
            if (this.delayHelper.computeCurrent(50L)) {
               this.delayHelper.runCurrent();
               int var1 = mc.player.playerScreenHandler.syncId;
               switch (this.mliisv) {
                  case 1:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.intVal;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.intVal));
                     } else {
                        mc.interactionManager.clickSlot(var1, this.intVal, this.majeaq, SlotActionType.SWAP, mc.player);
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
                        mc.player.closeHandledScreen();
                     }

                     this.mliisv = 2;
                     break;
                  case 2:
                     mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                     this.mliisv = 3;
                     break;
                  case 3:
                     if (this.intVal < 9) {
                        mc.player.getInventory().selectedSlot = this.majeaq;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.majeaq));
                     } else {
                        mc.interactionManager.clickSlot(var1, this.intVal, this.majeaq, SlotActionType.SWAP, mc.player);
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
                        mc.player.closeHandledScreen();
                     }

                     this.mliisv = 4;
                     break;
                  case 4:
                     this.mliisv = 0;
                     this.intVal = -1;
                     this.majeaq = -1;
               }
            }
         }
      }
   }

   public double[] getSize() {
      List var7 = this.initAdd(this.stringSetting.getValue());
      double var1;
      double var3;
      double var5;
      if (var7.size() >= 3) {
         var5 = (Double)var7.get(0);
         var3 = (Double)var7.get(1);
         var1 = (Double)var7.get(2);
      } else {
         if (var7.size() != 2) {
            return null;
         }

         var5 = (Double)var7.get(0);
         var1 = (Double)var7.get(1);
         List var8 = this.initAdd(this.mkedlu.getValue());
         var3 = var8.isEmpty() ? mc.player.getY() : (Double)var8.get(0);
      }

      return new double[]{var5, var3, var1};
   }

   public List<Double> initAdd(String s) {
      ArrayList var2 = new ArrayList();
      if (s != null && !s.isBlank()) {
         Matcher var3 = pattern.matcher(s);

         while (var3.find()) {
            try {
               var2.add(Double.parseDouble(var3.group()));
            } catch (NumberFormatException var5) {
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public void setMessage(String message) {
      long var2 = System.currentTimeMillis();
      if (var2 - this.long2 > 2500L) {
         NotificationManager.handleWarning(message);
         this.long2 = var2;
      }
   }

   public static ElytraTravel getInstance() {
      return elytraTravel;
   }

   public void lambda$tick$1() {
      this.setEnabled(false);
   }

   public void lambda$onEvent$0(UpdateEvent e) {
      this.tick();
   }
}
