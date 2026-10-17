package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.EntryHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.internal.Rotation;
import dev.daamky.client.internal.RotationHelper;
import dev.daamky.client.internal.VecRotationHelper;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Auto Buff",
   category = ModuleCategory.BATTLE,
   description = "Автоматически бросает зелья для баффов"
)
public class AutoBuff extends Module {
   public static final AutoBuff autoBuff = new AutoBuff();
   public final MultiBooleanSetting fireResistance = new MultiBooleanSetting("Potions")
      .value(new BooleanSetting("Strength").value(true), new BooleanSetting("Speed").value(true), new BooleanSetting("Fire resistance").value(false));
   public final ModeSetting packet = new ModeSetting("Mode").value("Legit").values("Legit", "Packet");
   public final FloatSetting tickAfterSpawn = new FloatSetting("Tick after spawn").value(10.0F).range(0.0F, 200.0F).step(5.0F);
   public final BooleanSetting autoDisable = new BooleanSetting("Auto disable").value(false);
   public final BooleanSetting onlyOnGround = new BooleanSetting("Only on ground").value(true);
   public final DelayHelper delayHelper = new DelayHelper();
   public final DelayHelper delayHelper2 = new DelayHelper();
   public final List<String> list = new ArrayList<>();
   public final List<String> list2 = new ArrayList<>();
   public float floatVal;
   public int intVal;
   public int inv = -1;
   public int int2 = -1;
   public boolean flag;
   public boolean flag2;
   public boolean flag3;
   public boolean flag4;

   public AutoBuff() {
      this.addSettings(new Setting[]{this.fireResistance, this.packet, this.tickAfterSpawn, this.autoDisable, this.onlyOnGround});
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(0, this::lambda$onEvent$0));
      this.addEventListeners(new Subscription[]{var1});
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (this.flag4 && this.inv != -1 && this.int2 != -1) {
         this.swapBack();
      }

      if (this.flag3 && mc.player != null) {
         VecRotationHelper.getInstance()
            .handleModule(new Rotation(mc.player.getYaw(), this.floatVal), RotationHelper.rotationHelper3, Priority.HIGH, this);
      }

      this.flag4 = false;
      this.flag3 = false;
      this.flag2 = false;
      this.flag = false;
      this.list.clear();
      this.list2.clear();
   }

   public void runBuff() {
      if (this.shouldBuff() && this.delayHelper.computeCurrent(250L)) {
         if (this.flag3 && this.delayHelper2.computeCurrent(5L)) {
            this.throwAll();
            this.delayHelper.runCurrent();
            this.flag = true;
            this.flag3 = false;
         } else if (this.flag) {
            this.initBack(true);
         } else {
            this.run();
         }
      }
   }

   public void runBuff2() {
      long var1 = 10L;
      if (!this.flag2) {
         if (this.shouldBuff()) {
            this.getMc();
         }
      } else {
         if (this.flag && this.delayHelper.computeCurrent(var1)) {
            this.runBack();
         } else if (this.flag3 && this.delayHelper2.computeCurrent(var1 / 2L)) {
            this.runItem();
         } else if (this.list.isEmpty() && this.list2.isEmpty()) {
            this.initBack(false);
         } else {
            this.runRemove();
         }
      }
   }

   public void getMc() {
      this.floatVal = mc.player.getPitch();
      this.intVal = mc.player.getInventory().selectedSlot;
      this.initInit();
      this.runClear();
      this.flag2 = true;
   }

   public void initInit() {
      VecRotationHelper.getInstance().handleModule(new Rotation(mc.player.getYaw(), 90.0F), RotationHelper.rotationHelper3, Priority.CRITICAL, this);
   }

   public void initBack(boolean packet) {
      if (this.flag4 && !packet) {
         this.swapBack();
      }

      if (!packet) {
         InventoryUtil.handleInt(this.intVal);
      }

      VecRotationHelper.getInstance().handleModule(new Rotation(mc.player.getYaw(), this.floatVal), RotationHelper.rotationHelper3, Priority.HIGH, this);
      if (!packet) {
         this.flag2 = false;
      } else {
         this.flag = false;
      }

      if (this.autoDisable.getValue() && (packet || this.allBuffed())) {
         this.toggle();
      }
   }

   public void runBack() {
      this.flag3 = false;
      this.flag = false;
      if (this.flag4 && this.inv != -1 && this.int2 != -1) {
         this.swapBack();
      }
   }

   public void swapBack() {
      Runnable var1 = this::lambda$swapBack$1;
      if (EntryHelper.check()) {
         EntryHelper.handleLong(10L, var1);
      } else {
         var1.run();
      }

      this.flag4 = false;
      this.int2 = -1;
      this.inv = -1;
   }

   public boolean shouldBuff() {
      return this.fireResistance.getAllNames().stream().anyMatch(this::lambda$shouldBuff$2);
   }

   public boolean allBuffed() {
      return this.fireResistance.getAllNames().stream().allMatch(this::lambda$allBuffed$3);
   }

   public void runClear() {
      this.list.clear();
      this.list2.clear();

      for (String var2 : this.fireResistance.getAllNames()) {
         RegistryEntry var3 = this.computeClass_6880(var2);
         if (var3 != null && !mc.player.hasStatusEffect(var3) && this.computeFlag(var3)) {
            this.list.add(var2);
         }
      }
   }

   public RegistryEntry<StatusEffect> computeClass_6880(String p) {
      return switch (p) {
         case "Strength" -> StatusEffects.STRENGTH;
         case "Speed" -> StatusEffects.SPEED;
         case "Fire resistance" -> StatusEffects.FIRE_RESISTANCE;
         default -> null;
      };
   }

   public boolean computeFlag(RegistryEntry<StatusEffect> e) {
      return this.computeNext(e, 0, 9) != -1 || this.computeNext(e, 9, 36) != -1;
   }

   public int computeNext(RegistryEntry<StatusEffect> e, int from, int to) {
      if (mc.player == null) {
         return -1;
      } else {
         for (int var4 = from; var4 < to; var4++) {
            ItemStack var5 = mc.player.getInventory().getStack(var4);
            if (var5.getItem() instanceof SplashPotionItem) {
               PotionContentsComponent var6 = (PotionContentsComponent)var5.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);

               for (StatusEffectInstance var8 : var6.getEffects()) {
                  if (var8.getEffectType().equals(e)) {
                     return var4;
                  }
               }

               if (var6.potion().isPresent()) {
                  for (StatusEffectInstance var10 : ((Potion)((RegistryEntry)var6.potion().get()).value()).getEffects()) {
                     if (var10.getEffectType().equals(e)) {
                        return var4;
                     }
                  }
               }
            }
         }

         return -1;
      }
   }

   public void runRemove() {
      String var1 = !this.list.isEmpty() ? this.list.removeFirst() : (!this.list2.isEmpty() ? this.list2.removeFirst() : null);
      if (var1 != null) {
         RegistryEntry var2 = this.computeClass_6880(var1);
         if (var2 != null) {
            if (!mc.player.hasStatusEffect(var2)) {
               int var3 = this.computeNext(var2, 0, 9);
               int var4 = this.computeNext(var2, 9, 36);
               if (var3 != -1) {
                  InventoryUtil.handleInt(var3);
                  this.run();
               } else if (var4 != -1) {
                  this.swapFromInventory(var4, var2);
               }
            }
         }
      }
   }

   public void swapFromInventory(int inv, RegistryEntry<StatusEffect> e) {
      this.flag4 = true;
      this.inv = inv;
      int var3 = InventoryUtil.getMc();
      this.int2 = var3 != -1 && var3 < 9 ? var3 : InventoryUtil.getInt();
      int var4 = this.int2;
      if (this.int2 != -1) {
         Runnable var5 = this::lambda$swapFromInventory$4;
         if (EntryHelper.check()) {
            EntryHelper.handleLong(10L, var5);
         } else {
            var5.run();
         }
      }
   }

   public void throwAll() {
      int var1 = mc.player.getInventory().selectedSlot;

      for (String var3 : this.fireResistance.getAllNames()) {
         RegistryEntry var4 = this.computeClass_6880(var3);
         if (var4 != null && !mc.player.hasStatusEffect(var4)) {
            int var5 = this.computeNext(var4, 0, 9);
            int var6 = this.computeNext(var4, 9, 36);
            Runnable var7 = AutoBuff::lambda$throwAll$5;
            if (var5 != -1) {
               InventoryUtil.handleInt(var5);
               var7.run();
            } else if (var6 != -1) {
               int var8 = InventoryUtil.getInt();
               Runnable var9 = () -> AutoBuff.lambda$throwAll$6(var6, var8, var7);
               if (EntryHelper.check()) {
                  EntryHelper.handleLong(10L, var9);
               } else {
                  var9.run();
               }
            }
         }
      }

      InventoryUtil.handleInt(var1);
   }

   public void run() {
      this.initInit();
      this.delayHelper2.runCurrent();
      this.flag3 = true;
   }

   public void runItem() {
      InventoryUtil.useItem(Hand.MAIN_HAND);
      this.delayHelper.runCurrent();
      this.flag = true;
      this.flag3 = false;
   }

   public static AutoBuff getInstance() {
      return autoBuff;
   }

   public static void lambda$throwAll$6(int inv, int best, Runnable use) {
      InventoryUtil.initInit(inv, best);
      InventoryUtil.handleInt(best);
      use.run();
      InventoryUtil.initInit(inv, best);
   }

   public static void lambda$throwAll$5() {
      InventoryUtil.useItem(Hand.MAIN_HAND);
   }

   public void lambda$swapFromInventory$4() {
      InventoryUtil.initInit(this.inv, this.int2);
      InventoryUtil.handleInt(this.int2);
      this.run();
   }

   public boolean lambda$allBuffed$3(String p) {
      RegistryEntry var2 = this.computeClass_6880(p);
      return var2 == null || mc.player.hasStatusEffect(var2) || !this.computeFlag(var2);
   }

   public boolean lambda$shouldBuff$2(String p) {
      RegistryEntry var2 = this.computeClass_6880(p);
      return var2 != null && !mc.player.hasStatusEffect(var2) && this.computeFlag(var2);
   }

   public void lambda$swapBack$1() {
      InventoryUtil.initInit(this.inv, this.int2);
   }

   public void lambda$onEvent$0(TickEvent e) {
      if (mc.player != null && mc.world != null) {
         if (!this.onlyOnGround.getValue() || mc.player.isOnGround()) {
            if (!((float)mc.player.age < this.tickAfterSpawn.getValue())) {
               if (this.packet.is("Packet")) {
                  this.runBuff();
               } else {
                  this.runBuff2();
               }
            }
         }
      }
   }
}
