package dev.daamky.client.module.combat;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.KeyEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.gui.SwapRadialMenuScreen;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.EntryHelper;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

@ModuleInfo(
   name = "Auto Swap",
   category = ModuleCategory.BATTLE,
   description = "Мгновенно перекладывает выбранные предметы во вторую руку по нажатию клавиши"
)
public class AutoSwap extends Module {
   public static final AutoSwap mkizz = new AutoSwap();
   public final IntegerSetting swapKey = new IntegerSetting("Swap key").value(86);
   public final ModeSetting dual = new ModeSetting("Mode").values("Dual", "Triple").value("Dual");
   public final ModeSetting instant = new ModeSetting("Bypass").values("Instant", "Grim", "Matrix", "Polar").value("Instant");
   public final ModeSetting sphereHead = new ModeSetting("First item").values("Sphere / Head", "Totem", "Golden Apple", "Shield").value("Sphere / Head");
   public final ModeSetting totem = new ModeSetting("Second item").values("Sphere / Head", "Totem", "Golden Apple", "Shield").value("Totem");
   public final ModeSetting goldenApple = new ModeSetting("Third item").values("Sphere / Head", "Totem", "Golden Apple", "Shield").value("Golden Apple");
   public final BooleanSetting notify = new BooleanSetting("Notify").value(true);
   public boolean flag = false;
   public final DelayHelper delayHelper = new DelayHelper();

   public AutoSwap() {
      this.goldenApple.visibleWhen(this::lambda$new$0);
      this.addSettings(new Setting[]{this.swapKey, this.dual, this.instant, this.sphereHead, this.totem, this.goldenApple, this.notify});
   }

   public static AutoSwap getInstance() {
      return mkizz;
   }

   public Item getHead() {
      return this.computeContains(this.sphereHead.getValue());
   }

   public Item getTotem() {
      return this.computeContains(this.totem.getValue());
   }

   public Item getGolden() {
      return this.computeContains(this.goldenApple.getValue());
   }

   public void handleGolden(int slotIndex, Item item) {
      if (item != null && item != Items.AIR) {
         String var3 = Registries.ITEM.getId(item).toString();
         if (slotIndex == 0) {
            this.sphereHead.value(var3);
         } else if (slotIndex == 1) {
            this.totem.value(var3);
         } else if (slotIndex == 2) {
            this.goldenApple.value(var3);
         }
      } else {
         if (slotIndex == 0) {
            this.sphereHead.value("Empty");
         } else if (slotIndex == 1) {
            this.totem.value("Empty");
         } else if (slotIndex == 2) {
            this.goldenApple.value("Empty");
         }
      }
   }

   public void handleGolden(int slotIndex, String itemName) {
      if (slotIndex == 0) {
         this.sphereHead.value(itemName);
      } else if (slotIndex == 1) {
         this.totem.value(itemName);
      } else if (slotIndex == 2) {
         this.goldenApple.value(itemName);
      }
   }

   @Override
   public void onEvent() {
      Subscription var1 = KeyEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var2 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      Subscription var3 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void handleNotify(Item targetItem) {
      if (targetItem != null && targetItem != Items.AIR && mc.player != null) {
         int var2 = this.computeComp(targetItem);
         if (var2 == -1) {
            if (this.notify.getValue()) {
               this.handleString("Предмет \"" + targetItem.getName().getString() + "\" не найден в инвентаре");
            }
         } else {
            this.initCase(var2, targetItem);
         }
      }
   }

   public void runNotify() {
      if (mc.world != null && mc.player != null && mc.interactionManager != null && this.flag) {
         Item var1 = this.getHead2();
         if (var1 != null && var1 != Items.AIR) {
            int var2 = this.computeComp(var1);
            if (var2 == -1) {
               if (this.notify.getValue()) {
                  this.handleString("Предмет \"" + var1.getName().getString() + "\" не найден в инвентаре");
               }

               this.flag = false;
            } else {
               this.initCase(var2, var1);
            }
         } else {
            this.flag = false;
         }
      }
   }

   public Item getHead2() {
      if (mc.player == null) {
         return null;
      } else {
         Item var1 = mc.player.getOffHandStack().getItem();
         Item var2 = this.computeContains(this.sphereHead.getValue());
         Item var3 = this.computeContains(this.totem.getValue());
         return var1 == var2 ? var3 : var2;
      }
   }

   public void initCase(int slot, Item item) {
      if (mc.interactionManager != null && mc.player != null) {
         String var3 = this.instant.getValue();
         if ("Grim".equalsIgnoreCase(var3) || "Polar".equalsIgnoreCase(var3) || "Matrix".equalsIgnoreCase(var3)) {
            long var4 = "Polar".equalsIgnoreCase(var3) ? 350L : ("Matrix".equalsIgnoreCase(var3) ? 200L : 150L);
            if (!this.delayHelper.computeCurrent(var4)) {
               this.flag = false;
               return;
            }

            this.delayHelper.runCurrent();
         }

         AttackAura.int3 = 3;
         InventoryUtil.handleFlag(false);
         int var7 = mc.player.playerScreenHandler.syncId;
         int var5 = slot < 9 ? slot + 36 : slot;
         ItemStack var6 = mc.player.playerScreenHandler.getSlot(var5).getStack();
         if (var6.contains(DataComponentTypes.BUNDLE_CONTENTS)) {
            mc.interactionManager.clickSlot(var7, var5, 1, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(var7, 45, 0, SlotActionType.PICKUP, mc.player);
            if (!mc.player.playerScreenHandler.getCursorStack().isEmpty()) {
               mc.interactionManager.clickSlot(var7, var5, 0, SlotActionType.PICKUP, mc.player);
            }
         } else {
            mc.interactionManager.clickSlot(var7, var5, 40, SlotActionType.SWAP, mc.player);
         }

         if (mc.player.networkHandler != null) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var7));
         }

         mc.player.closeHandledScreen();
         InventoryUtil.handleFlag(true);
         if (this.notify.getValue()) {
            this.handleString("Свапнул на \"" + item.getName().getString() + "\"");
         }

         this.flag = false;
      }
   }

   public int computeComp(Item item) {
      if (mc.player != null && item != null && item != Items.AIR) {
         int var2 = -1;

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = mc.player.getInventory().getStack(var3);
            if (!var4.isEmpty() && var4.getItem() == item) {
               if (var4.contains(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
                  AttributeModifiersComponent var5 = (AttributeModifiersComponent)var4.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                  if (var5 != null && !var5.modifiers().isEmpty()) {
                     return var3 < 9 ? var3 + 36 : var3;
                  }
               }

               if (var2 == -1) {
                  var2 = var3 < 9 ? var3 + 36 : var3;
               }
            }
         }

         return var2;
      } else {
         return -1;
      }
   }

   public Item computeContains(String modeName) {
      if (modeName != null && !modeName.equalsIgnoreCase("Empty") && !modeName.equalsIgnoreCase("None")) {
         Identifier var2 = Identifier.tryParse(modeName);
         if (var2 != null && Registries.ITEM.containsId(var2)) {
            return (Item)Registries.ITEM.get(var2);
         } else {
            String var3 = modeName.toLowerCase();
            if (var3.contains("sphere") || var3.contains("head") || var3.contains("сфера")) {
               return Items.PLAYER_HEAD;
            } else if (var3.contains("totem") || var3.contains("тотем")) {
               return Items.TOTEM_OF_UNDYING;
            } else if (var3.contains("apple") || var3.contains("gapple") || var3.contains("яблоко")) {
               return Items.GOLDEN_APPLE;
            } else {
               return !var3.contains("shield") && !var3.contains("щит") ? Items.AIR : Items.SHIELD;
            }
         }
      } else {
         return Items.AIR;
      }
   }

   public void lambda$onEvent$3(UpdateEvent event) {
      if (!EntryHelper.check()) {
         this.runNotify();
      }
   }

   public void lambda$onEvent$2(TickEvent event) {
      if (EntryHelper.check()) {
         this.runNotify();
      }
   }

   public void lambda$onEvent$1(KeyEvent.KeyEventData event) {
      int var2 = this.swapKey.getValue();
      if (event.key() == var2) {
         if (event.action() == 1) {
            if ("Triple".equalsIgnoreCase(this.dual.getValue())) {
               if (mc.currentScreen == null) {
                  mc.setScreen(new SwapRadialMenuScreen());
               }
            } else if (mc.currentScreen == null) {
               this.flag = true;
            }
         } else if (event.action() == 0 && "Triple".equalsIgnoreCase(this.dual.getValue()) && mc.currentScreen instanceof SwapRadialMenuScreen var3) {
            var3.runStack();
         }
      }
   }

   public Boolean lambda$new$0() {
      return "Triple".equalsIgnoreCase(this.dual.getValue());
   }
}
