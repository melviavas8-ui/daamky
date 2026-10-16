package dev.daamky.client.internal;

import dev.daamky.client.legacy.setting.MotionConfig6;
import dev.daamky.client.model.enums.Priority;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.movement.InventoryMove;
import dev.daamky.client.util.KeyNames;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public final class InventoryUtil implements MinecraftAccess {
   public static void handleFlag(boolean state) {
      if (mc.options != null && mc.getWindow() != null) {
         KeyBinding[] var1 = new KeyBinding[]{
            mc.options.forwardKey,
            mc.options.backKey,
            mc.options.leftKey,
            mc.options.rightKey,
            mc.options.jumpKey,
            mc.options.sprintKey
         };
         long var2 = mc.getWindow().getHandle();

         for (KeyBinding var7 : var1) {
            boolean var8 = state && InputUtil.isKeyPressed(var2, var7.getDefaultKey().getCode());
            var7.setPressed(var8);
         }
      }
   }

   public static void initInit(int slot) {
      if (mc.player != null && mc.interactionManager != null) {
         int var1 = mc.player.playerScreenHandler.syncId;
         int var2 = slot < 9 ? slot + 36 : slot;
         mc.interactionManager.clickSlot(var1, var2, 1, SlotActionType.THROW, mc.player);
         if (mc.player.networkHandler != null) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
         }

         mc.player.closeHandledScreen();
      }
   }

   public static void initInit2(int slot) {
      if (slot != -1 && mc.player != null && mc.interactionManager != null) {
         int var1 = mc.player.playerScreenHandler.syncId;
         int var2 = slot < 9 ? slot + 36 : slot;
         if (var2 != 45) {
            AttackAura.int3 = 3;
            handleFlag(false);
            ItemStack var3 = mc.player.playerScreenHandler.getSlot(var2).getStack();
            if (var3.contains(DataComponentTypes.BUNDLE_CONTENTS)) {
               mc.interactionManager.clickSlot(var1, var2, 1, SlotActionType.PICKUP, mc.player);
               mc.interactionManager.clickSlot(var1, 45, 0, SlotActionType.PICKUP, mc.player);
               if (!mc.player.playerScreenHandler.getCursorStack().isEmpty()) {
                  mc.interactionManager.clickSlot(var1, var2, 0, SlotActionType.PICKUP, mc.player);
               }
            } else {
               mc.interactionManager.clickSlot(var1, var2, 40, SlotActionType.SWAP, mc.player);
            }

            if (mc.player.networkHandler != null) {
               mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
            }

            mc.player.closeHandledScreen();
            handleFlag(true);
         }
      }
   }

   public static void initInit3(int slot) {
      if (slot != -1 && mc.player != null && mc.interactionManager != null) {
         int var1 = mc.player.playerScreenHandler.syncId;
         int var2 = slot < 9 ? slot + 36 : slot;
         if (var2 != 6) {
            AttackAura.int3 = 3;
            handleFlag(false);
            if (var2 >= 36 && var2 <= 44) {
               mc.interactionManager.clickSlot(var1, 6, var2 - 36, SlotActionType.SWAP, mc.player);
            } else {
               int var3 = mc.player.getInventory().selectedSlot;
               mc.interactionManager.clickSlot(var1, var2, var3, SlotActionType.SWAP, mc.player);
               mc.interactionManager.clickSlot(var1, 6, var3, SlotActionType.SWAP, mc.player);
               mc.interactionManager.clickSlot(var1, var2, var3, SlotActionType.SWAP, mc.player);
            }

            if (mc.player.networkHandler != null) {
               mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1));
            }

            mc.player.closeHandledScreen();
            handleFlag(true);
         }
      }
   }

   public static void initInit(int from, int to) {
      if (from != -1 && to != -1 && mc.player != null && mc.interactionManager != null) {
         int var2 = mc.player.playerScreenHandler.syncId;
         int var3 = from < 9 ? from + 36 : from;
         int var4 = to < 9 ? to + 36 : to;
         if (var3 != var4) {
            if (var4 >= 36 && var4 <= 44) {
               mc.interactionManager.clickSlot(var2, var3, var4 - 36, SlotActionType.SWAP, mc.player);
            } else if (var3 >= 36 && var3 <= 44) {
               mc.interactionManager.clickSlot(var2, var4, var3 - 36, SlotActionType.SWAP, mc.player);
            } else {
               int var5 = mc.player.getInventory().selectedSlot;
               mc.interactionManager.clickSlot(var2, var3, var5, SlotActionType.SWAP, mc.player);
               mc.interactionManager.clickSlot(var2, var4, var5, SlotActionType.SWAP, mc.player);
               mc.interactionManager.clickSlot(var2, var3, var5, SlotActionType.SWAP, mc.player);
            }

            if (!InventoryMove.getInstance().is()) {
               mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var2));
            }
         }
      }
   }

   public static void handleInt(int from, int to) {
      initInit(from, to);
   }

   public static void handleClass_1792(Item item) {
      if (mc.player != null && mc.interactionManager != null) {
         int var1 = computeInt(item);
         if (var1 != -1) {
            initInit4(var1);
         }
      }
   }

   public static void initInit4(int slot) {
      if (slot != -1 && mc.player != null && mc.interactionManager != null) {
         int var1 = mc.player.getInventory().selectedSlot;
         int var2 = mc.player.playerScreenHandler.syncId;
         int var3 = slot < 9 ? slot + 36 : slot;
         AttackAura.int3 = 3;
         handleFlag(false);
         if (var3 >= 36 && var3 <= 44) {
            int var4 = var3 - 36;
            if (var4 != var1) {
               mc.player.getInventory().selectedSlot = var4;
               mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(var4));
            }

            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            if (var4 != var1) {
               mc.player.getInventory().selectedSlot = var1;
               mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(var1));
            }
         } else {
            mc.interactionManager.clickSlot(var2, var3, var1, SlotActionType.SWAP, mc.player);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.interactionManager.clickSlot(var2, var3, var1, SlotActionType.SWAP, mc.player);
         }

         handleFlag(true);
      }
   }

   public static void useItem(Hand hand) {
      if (mc.player != null) {
         VecRotationHelper var1 = VecRotationHelper.getInstance();
         float var2 = var1.getLast() != null ? var1.getCurrent().getYaw() : mc.player.getYaw();
         float var3 = var1.getLast() != null ? var1.getCurrent().getPitch() : mc.player.getPitch();
         PacketHelper.closeClose(InventoryUtil::lambda$useItem$0);
         mc.player.swingHand(hand);
      }
   }

   public static void handleInt(int slot) {
      initInit(slot, true);
   }

   public static void initInit(int slot, boolean client) {
      if (mc.player != null) {
         if (mc.player.getInventory().selectedSlot != slot) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            if (client) {
               mc.player.getInventory().selectedSlot = slot;
            }
         }
      }
   }

   public static int computeInt(Item item, boolean inHotbar) {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var2 = mc.player.getInventory().main;
         int var3 = inHotbar ? 0 : 9;
         int var4 = inHotbar ? 9 : 36;
         int var5 = -1;

         for (int var6 = var3; var6 < var4; var6++) {
            if (((ItemStack)var2.get(var6)).getItem() == item) {
               var5 = var6;
            }
         }

         return var5;
      }
   }

   public static int computeInt(Item input) {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var1 = mc.player.getInventory().main;
         int var2 = -1;

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = (ItemStack)var1.get(var3);
            if (var4.getItem() == input) {
               var2 = var3;
               break;
            }
         }

         if (var2 < 9 && var2 != -1) {
            var2 += 36;
         }

         return var2;
      }
   }

   public static boolean getMc() {
      return mc.player != null && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
   }

   public static int computeInt(boolean inHotbar) {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var1 = mc.player.getInventory().main;
         int var2 = inHotbar ? 0 : 9;
         int var3 = inHotbar ? 9 : 36;

         for (int var4 = var2; var4 < var3; var4++) {
            if (((ItemStack)var1.get(var4)).getItem() instanceof AxeItem) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int getInt() {
      int var0 = getMc();
      return var0 != -1 ? var0 : getInt2();
   }

   public static int getMcInt() {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var0 = mc.player.getInventory().main;
         int var1 = mc.player.getInventory().selectedSlot;

         for (int var2 = 0; var2 < 9; var2++) {
            if (((ItemStack)var0.get(var2)).isEmpty() && var1 != var2) {
               return var2;
            }
         }

         return -1;
      }
   }

   public static int getInt2() {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var0 = mc.player.getInventory().main;
         int var1 = mc.player.getInventory().selectedSlot;

         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = (ItemStack)var0.get(var2);
            if (!(var3.getItem() instanceof SwordItem) && var1 != var2) {
               return var2;
            }
         }

         return -1;
      }
   }

   public static int getInt3() {
      if (mc.player == null) {
         return -1;
      } else {
         int var0 = 0;
         DefaultedList var1 = mc.player.getInventory().main;

         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = (ItemStack)var1.get(var2);
            if (var3.isOf(Items.TOTEM_OF_UNDYING) && !var3.hasEnchantments()) {
               var0++;
            }
         }

         return var0;
      }
   }

   public static int computeInt2(boolean saveEnchanted) {
      if (mc.player == null) {
         return -1;
      } else {
         DefaultedList var1 = mc.player.getInventory().main;

         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = (ItemStack)var1.get(var2);
            if (var3.isOf(Items.TOTEM_OF_UNDYING) && (!saveEnchanted || !var3.hasEnchantments())) {
               return var2 < 9 ? var2 + 36 : var2;
            }
         }

         return -1;
      }
   }

   public InventoryUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Packet lambda$useItem$0(Hand hand, float currentYaw, float currentPitch, int s) {
      return new PlayerInteractItemC2SPacket(hand, s, currentYaw, currentPitch);
   }

   public static class ItemUsage {
      public final Item item;
      public final Module provider;
      public boolean flag = false;
      public boolean useRotation = true;
      public Rotation rotation = new Rotation(Float.MIN_VALUE, Float.MIN_VALUE);
      public int intVal = -1;
      public int int2 = -1;
      public int mxaeo = -1;
      public int int3 = -1;
      public int int4 = 0;
      public Runnable runnable = null;
      public InventoryUtil.ItemUsage.UsageState usageState = InventoryUtil.ItemUsage.UsageState.IDLE;

      public void runIdle() {
         this.usageState = InventoryUtil.ItemUsage.UsageState.IDLE;
         this.intValVal4 = 0;
         this.mxaeo = -1;
         this.intValVal3 = -1;
      }

      public void setRotation(Rotation rotation) {
         this.rotation = rotationx;
      }

      public void initRotation() {
         if (this.useRotation) {
            float var1 = this.rotation.getYaw() != Float.MIN_VALUE ? this.rotation.getYaw() : MinecraftAccess.mc.player.getYaw();
            float var2 = this.rotation.getPitch() != Float.MIN_VALUE ? this.rotation.getPitch() : MinecraftAccess.mc.player.getPitch();
            RotationHelper var3 = new RotationHelper(new MotionConfig6(), false);
            VecRotationHelper.getInstance().handleModule(new Rotation(var1, var2), var3, Priority.REQUIRED, this.provider);
         }
      }

      public void handleUse(boolean isLegit) {
         if (MinecraftAccess.mc.currentScreen == null && this.runnable != null) {
            this.runnable.run();
         }

         if (!MinecraftAccess.mc.player.getItemCooldownManager().isCoolingDown(this.item.getDefaultStack())) {
            if (isLegit) {
               this.runnable = this::lambda$handleUse$0;
            } else {
               this.runItem();
            }

            this.flag = false;
         }
      }

      public void handleUse(int bind, boolean isLegit) {
         if (!isLegit) {
            this.runnable = null;
         }

         if (MinecraftAccess.mc.currentScreen == null && this.runnable != null) {
            this.runnable.run();
         }

         if (!KeyNames.computeGlfw(bind) && MinecraftAccess.mc.currentScreen == null) {
            this.flag = false;
         } else if (!this.flag && MinecraftAccess.mc.currentScreen == null && !MinecraftAccess.mc.player.getItemCooldownManager().isCoolingDown(this.item.getDefaultStack())) {
            if (isLegit) {
               this.runnable = this::lambda$handleUse$1;
            } else {
               this.runItem();
            }
         }
      }

      public void runItem() {
         if (MinecraftAccess.mc.player.getOffHandStack().isOf(this.item)) {
            this.initRotation();
            InventoryUtil.useItem(Hand.OFF_HAND);
            this.flag = true;
         } else if (MinecraftAccess.mc.player.getMainHandStack().isOf(this.item)) {
            this.initRotation();
            InventoryUtil.useItem(Hand.MAIN_HAND);
            this.flag = true;
         } else {
            int var1 = InventoryUtil.computeInt(this.item);
            if (var1 == -1) {
               TextShaper.handleString(
                  Formatting.RED
                     + "Предмет "
                     + Formatting.WHITE
                     + this.item.getName().getString()
                     + Formatting.RED
                     + " не найден в инвентаре или хотбаре!"
               );
               this.flag = true;
            } else {
               this.initRotation();
               InventoryUtil.handleClass_1792(this.item);
               this.flag = true;
            }
         }
      }

      public void executeLegitMode(int bind) {
         switch (this.usageState) {
            case IDLE:
               if (bind != -1 && !KeyNames.computeGlfw(bind)) {
                  return;
               }

               this.intVal = InventoryUtil.computeInt(this.item, true);
               this.intValVal2 = MinecraftAccess.mc.player.getInventory().selectedSlot;
               this.mxaeo = -1;
               this.intValVal3 = -1;
               if (this.intVal == -1) {
                  this.mxaeo = InventoryUtil.computeInt(this.item, false);
                  if (this.mxaeo == -1) {
                     TextShaper.handleString(
                        Formatting.RED
                           + "Предмет "
                           + Formatting.WHITE
                           + this.item.getName().getString()
                           + Formatting.RED
                           + " не найден в инвентаре или хотбаре!"
                     );
                     this.flag = true;
                     this.usageState = InventoryUtil.ItemUsage.UsageState.IDLE;
                     return;
                  }

                  this.usageState = InventoryUtil.ItemUsage.UsageState.SWAP_ITEM;
                  this.intValVal4 = 1;
               } else {
                  MinecraftAccess.mc.player.getInventory().selectedSlot = this.intVal;
                  this.usageState = InventoryUtil.ItemUsage.UsageState.USE_ITEM;
                  this.intValVal4 = 1;
               }
               break;
            case SWAP_ITEM:
               if (this.intValVal4-- > 0) {
                  return;
               }

               int var2 = InventoryUtil.getInt();
               if (var2 != -1 && this.mxaeo != -1) {
                  this.intValVal3 = var2;
                  Runnable var3 = this::lambda$executeLegitMode$2;
                  if (EntryHelper.check()) {
                     EntryHelper.handleLong(10L, var3);
                  } else {
                     var3.run();
                  }
               } else {
                  this.usageState = InventoryUtil.ItemUsage.UsageState.IDLE;
               }
               break;
            case USE_ITEM:
               if (this.intValVal4-- > 0) {
                  return;
               }

               this.initRotation();
               InventoryUtil.useItem(Hand.MAIN_HAND);
               this.usageState = InventoryUtil.ItemUsage.UsageState.RESTORE_SLOT;
               this.intValVal4 = 1;
               break;
            case RESTORE_SLOT:
               if (this.intValVal4-- > 0) {
                  return;
               }

               if (this.mxaeo != -1 && this.intValVal3 != -1) {
                  InventoryUtil.initInit(this.mxaeo, this.intValVal3);
                  this.mxaeo = -1;
                  this.intValVal3 = -1;
               }

               MinecraftAccess.mc.player.getInventory().selectedSlot = this.intValVal2;
               this.usageState = InventoryUtil.ItemUsage.UsageState.IDLE;
               this.runnable = null;
               this.flag = true;
         }
      }

      public Item getItem() {
         return this.item;
      }

      public Module getProvider() {
         return this.provider;
      }

      public boolean check() {
         return this.flag;
      }

      public boolean isUseRotation() {
         return this.useRotation;
      }

      public Rotation getRotation() {
         return this.rotation;
      }

      public int getInt4() {
         return this.intValVal4;
      }

      public Runnable getRunnable() {
         return this.runnable;
      }

      public InventoryUtil.ItemUsage.UsageState getUsageState() {
         return this.usageState;
      }

      public ItemUsage(Item item, Module provider) {
         this.item = itemx;
         this.provider = providerx;
      }

      public void setUseRotation(boolean useRotation) {
         this.useRotation = useRotationx;
      }

      public void lambda$executeLegitMode$2(int hotbarSlot) {
         InventoryUtil.initInit(this.mxaeo, hotbarSlot);
         MinecraftAccess.mc.player.getInventory().selectedSlot = this.intVal = hotbarSlot;
         InventoryUtil.handleInt(this.intVal);
         this.usageState = InventoryUtil.ItemUsage.UsageState.USE_ITEM;
         this.intValVal4 = 1;
      }

      public void lambda$handleUse$1(int bind) {
         this.executeLegitMode(bind);
      }

      public void lambda$handleUse$0() {
         this.executeLegitMode(-1);
      }

      public static enum UsageState {
         IDLE,
         SWAP_ITEM,
         USE_ITEM,
         RESTORE_SLOT;

         public static InventoryUtil.ItemUsage.UsageState[] $values() {
            return new InventoryUtil.ItemUsage.UsageState[]{IDLE, SWAP_ITEM, USE_ITEM, RESTORE_SLOT};
         }
      }
   }
}
