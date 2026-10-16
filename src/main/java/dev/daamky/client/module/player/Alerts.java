package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Alerts",
   description = "Уведомления о действиях цели и окружении (неотключаемый)",
   category = ModuleCategory.ACTOR,
   subCategory = "Alerts"
)
public class Alerts extends Module {
   public static final Alerts alerts = new Alerts();
   public final BooleanSetting booleanSetting = new BooleanSetting("Твоё поедание").value(true);
   public final BooleanSetting myewq = new BooleanSetting("Цель ест золотое яблоко").value(true);
   public final BooleanSetting booleanSetting2 = new BooleanSetting("Цель ест зачарованное яблоко").value(true);
   public final BooleanSetting booleanSetting3 = new BooleanSetting("Цель ест хорус").value(true);
   public final BooleanSetting booleanSetting4 = new BooleanSetting("Цель пьёт зелье").value(true);
   public final BooleanSetting booleanSetting5 = new BooleanSetting("Показывать эффект зелья").value(true);
   public final BooleanSetting booleanSetting6 = new BooleanSetting("Поп тотема цели").value(true);
   public final BooleanSetting booleanSetting7 = new BooleanSetting("Твой тотем лопнул").value(true);
   public final BooleanSetting mpoozub = new BooleanSetting("Входящий снаряд").value(true);
   public final BooleanSetting booleanSetting8 = new BooleanSetting("Игрок вошёл в радиус").value(false);
   public final FloatSetting floatSetting = new FloatSetting("Радиус игрока (блоки)")
      .value(24.0F)
      .range(8.0F, 64.0F)
      .step(1.0F)
      .visibleWhen(this.booleanValSetting8::getValue);
   public final BooleanSetting booleanSetting9 = new BooleanSetting("Высокий пинг").value(false);
   public final FloatSetting floatSetting2 = new FloatSetting("Порог пинга (мс)")
      .value(200.0F)
      .range(100.0F, 500.0F)
      .step(10.0F)
      .visibleWhen(this.booleanValSetting9::getValue);
   public int intVal = -1;
   public boolean flag = false;
   public boolean flag2 = false;
   public boolean flag3 = false;
   public boolean flag4 = false;
   public int int2 = 0;
   public final Set<Integer> set = new HashSet<>();
   public final Set<UUID> set2 = new HashSet<>();
   public boolean flag5 = false;
   public long timeStamp = 0L;
   public long mzivpe = 0L;

   public Alerts() {
      this.addSettings(
         new Setting[]{
            this.booleanValSetting,
            this.myewq,
            this.booleanValSetting2,
            this.booleanValSetting3,
            this.booleanValSetting4,
            this.booleanValSetting5,
            this.booleanValSetting6,
            this.booleanValSetting7,
            this.mpoozub,
            this.booleanValSetting8,
            this.floatValSetting,
            this.booleanValSetting9,
            this.floatValSetting2
         }
      );
   }

   @Override
   public void setEnabledInternal(boolean newState, boolean config) {
      if (newState) {
         super.setEnabledInternal(true, config);
      }
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = PacketEvent.getInstance().subscribe(new EventListener<>(this::setE));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void runClear() {
      if (mc.player != null && mc.world != null) {
         this.flag3 = this.computeFlag(mc.player);
         if (this.booleanValSetting.getValue()) {
            boolean var1 = mc.player.isUsingItem();
            if (var1 && !this.flag2) {
               this.initInit2(mc.player.getActiveItem());
            }

            this.flag2 = var1;
         }

         LivingEntity var3 = AttackAura.getInstance().class_1309;
         if (var3 != null && var3.isAlive()) {
            if (var3.getId() != this.intVal) {
               this.intVal = var3.getId();
               this.flag = false;
               this.intVal2 = 0;
            }

            this.flag4 = this.computeFlag(var3);
            boolean var2 = var3.isUsingItem();
            if (var2 && !this.flag) {
               this.initInit(var3.getActiveItem());
            }

            this.flag = var2;
         } else {
            this.intVal = -1;
            this.flag = false;
            this.flag4 = false;
         }

         if (this.mpoozub.getValue()) {
            this.scanProjectiles();
         }

         if (this.booleanValSetting8.getValue()) {
            this.initAdd();
         } else {
            this.set2.clear();
            this.flag5 = false;
         }

         if (this.booleanValSetting9.getValue()) {
            this.runCurrent();
         }
      } else {
         this.runClear2();
      }
   }

   public void runClear2() {
      this.intVal = -1;
      this.flag = false;
      this.flag4 = false;
      this.intVal2 = 0;
      this.set.clear();
      this.set2.clear();
      this.flag5 = false;
   }

   public void setE(PacketEvent.PacketEventData e) {
      if (e.isReceive() && mc.player != null && mc.world != null) {
         Packet var3 = e.packet();
         EntityStatusS2CPacket var2;
         if (var3 instanceof EntityStatusS2CPacket && (var2 = (EntityStatusS2CPacket)var3).getStatus() == 35) {
            Entity var4 = var2.getEntity(mc.world);
            if (var4 == mc.player) {
               if (this.booleanValSetting7.getValue()) {
                  NotificationManager.handleWarning(this.flag3 ? "Твой тотем лопнул (чар.)" : "Твой тотем лопнул", new ItemStack(Items.TOTEM_OF_UNDYING));
               }
            } else {
               if (this.booleanValSetting6.getValue() && var4 != null && var4 == AttackAura.getInstance().class_1309) {
                  this.intVal2++;
                  String var5 = this.flag4 ? "чар." : "обычный";
                  NotificationManager.handleWarning("Цель лопнула тотем (" + var5 + ") ×" + this.intVal2, new ItemStack(Items.TOTEM_OF_UNDYING));
               }
            }
         }
      }
   }

   public void initInit(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            if (this.booleanValSetting2.getValue()) {
               NotificationManager.handleInfo("Цель ест зачарованное яблоко", new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
            }
         } else if (stack.isOf(Items.GOLDEN_APPLE)) {
            if (this.myewq.getValue()) {
               NotificationManager.handleInfo("Цель ест золотое яблоко", new ItemStack(Items.GOLDEN_APPLE));
            }
         } else if (stack.isOf(Items.CHORUS_FRUIT)) {
            if (this.booleanValSetting3.getValue()) {
               NotificationManager.handleInfo("Цель ест хорус", new ItemStack(Items.CHORUS_FRUIT));
            }
         } else if (stack.getItem() instanceof PotionItem) {
            if (!this.booleanValSetting4.getValue()) {
               return;
            }

            String var2 = this.booleanValSetting5.getValue() ? this.computeNext(stack) : null;
            NotificationManager.handleInfo(var2 != null ? "Цель пьёт зелье: " + var2 : "Цель пьёт зелье", stack.copy());
         }
      }
   }

   public void initInit2(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            NotificationManager.handleInfo("Ты ешь зачарованное яблоко", new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
         } else if (stack.isOf(Items.GOLDEN_APPLE)) {
            NotificationManager.handleInfo("Ты ешь золотое яблоко", new ItemStack(Items.GOLDEN_APPLE));
         } else if (stack.isOf(Items.CHORUS_FRUIT)) {
            NotificationManager.handleInfo("Ты ешь хорус", new ItemStack(Items.CHORUS_FRUIT));
         } else if (stack.getItem() instanceof PotionItem) {
            String var2 = this.booleanValSetting5.getValue() ? this.computeNext(stack) : null;
            NotificationManager.handleInfo(var2 != null ? "Ты пьёшь зелье: " + var2 : "Ты пьёшь зелье", stack.copy());
         }
      }
   }

   public void scanProjectiles() {
      Box var1 = mc.player.getBoundingBox().expand(28.0);
      Vec3d var2 = mc.player.getEyePos();

      for (Entity var4 : mc.world.getOtherEntities(mc.player, var1)) {
         Vec3d var5;
         Vec3d var6;
         ProjectileEntity var7;
         int var8;
         if ((var4 instanceof PersistentProjectileEntity || var4 instanceof EnderPearlEntity)
            && !this.set.contains(var8 = var4.getId())
            && (!(var4 instanceof ProjectileEntity) || (var7 = (ProjectileEntity)var4).getOwner() != mc.player)
            && !((var6 = var4.getVelocity()).lengthSquared() < 0.02)
            && !((var5 = var2.subtract(var4.getPos())).length() > 28.0)
            && !(var6.normalize().dotProduct(var5.normalize()) < 0.88)) {
            this.set.add(var8);
            NotificationManager.handleWarning("Входящий снаряд: " + (var4 instanceof EnderPearlEntity ? "жемчуг" : "стрела"));
         }
      }

      this.set.removeIf(Alerts::lambda$scanProjectiles$1);
   }

   public void initAdd() {
      double var1 = (double)this.floatValSetting.getValue().floatValue();
      HashSet var3 = new HashSet();

      for (PlayerEntity var5 : mc.world.getPlayers()) {
         if (var5 != mc.player && (double)var5.distanceTo(mc.player) <= var1) {
            var3.add(var5.getUuid());
            if (this.flag5 && !this.set2.contains(var5.getUuid())) {
               NotificationManager.handleInfo(var5.getName().getString() + " рядом (" + (int)var5.distanceTo(mc.player) + "м)");
            }
         }
      }

      this.set2.clear();
      this.set2.addAll(var3);
      this.flag5 = true;
   }

   public void runCurrent() {
      long var2 = System.currentTimeMillis();
      if (var2 - this.longVal >= 2000L) {
         this.longVal = var2;
         if (mc.getNetworkHandler() != null) {
            PlayerListEntry var4 = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (var4 != null) {
               int var5 = var4.getLatency();
               int var1;
               if (var5 > (var1 = this.floatValSetting2.getValue().intValue())) {
                  if (var2 - this.mzivpe > 6000L) {
                     NotificationManager.handleWarning("Высокий пинг: " + var5 + " мс");
                     this.mzivpe = var2;
                  }
               } else if ((double)var5 < (double)var1 * 0.8) {
                  this.mzivpe = 0L;
               }
            }
         }
      }
   }

   public boolean computeFlag(LivingEntity entity) {
      return this.computeContains(entity.getMainHandStack()) || this.computeContains(entity.getOffHandStack());
   }

   public boolean computeContains(ItemStack stack) {
      if (stack == null || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
         return false;
      } else if (!stack.hasEnchantments() && !stack.hasGlint()) {
         String var2 = stack.getName().getString().toLowerCase();
         return var2.contains("чар") || var2.contains("элит") || var2.contains("лучш") || var2.contains("super");
      } else {
         return true;
      }
   }

   public String computeNext(ItemStack stack) {
      PotionContentsComponent var2 = (PotionContentsComponent)stack.get(DataComponentTypes.POTION_CONTENTS);
      if (var2 == null) {
         return null;
      } else {
         Iterator var3 = var2.getEffects().iterator();
         if (var3.hasNext()) {
            StatusEffectInstance var4 = (StatusEffectInstance)var3.next();
            String var5 = ((StatusEffect)var4.getEffectType().value()).getName().getString();
            int var6 = var4.getAmplifier();
            return var6 > 0 ? var5 + " " + (var6 + 1) : var5;
         } else {
            return null;
         }
      }
   }

   public static Alerts getInstance() {
      return alerts;
   }

   public static boolean lambda$scanProjectiles$1(Integer id) {
      return mc.world.getEntityById(id) == null;
   }

   public void lambda$onEvent$0(UpdateEvent e) {
      this.runClear();
   }
}
