package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.StopHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "AutoEnd",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автоматический фарм Энд-городов, сундуков и лута в Краю"
)
public class AutoEnd extends Module {
   public static final AutoEnd autoEnd = new AutoEnd();
   public final BooleanSetting autoLoot = new BooleanSetting("Auto Loot").value(true);
   public final BooleanSetting autoEat = new BooleanSetting("Auto Eat").value(true);
   public final FloatSetting searchRadius = new FloatSetting("Search Radius").value(64.0F).range(16.0F, 128.0F).step(4.0F);
   public final FloatSetting lootDelayMs = new FloatSetting("Loot Delay (ms)").value(100.0F).range(50.0F, 500.0F).step(10.0F);
   public final StringSetting stringSetting = new StringSetting("Warehouse Home").value("end_loot");
   public final StopHelper stopHelper = new StopHelper();
   public final Set<BlockPos> set = new HashSet<>();
   public BlockPos class_2338 = null;
   public long timeStamp = 0L;
   public AutoEnd.State state = AutoEnd.State.SCANNING;

   public AutoEnd() {
      this.addSettings(new Setting[]{this.autoLoot, this.autoEat, this.searchRadius, this.lootDelayMs, this.stringSetting});
   }

   public static AutoEnd getInstance() {
      return autoEnd;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.state = AutoEnd.State.SCANNING;
      this.set.clear();
      this.class_2338 = null;
      this.longVal = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      this.stopHelper.runStop();
      super.onDisable();
   }

   @Override
   public void onEvent() {
      this.addEventListeners(new Subscription[]{UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0))});
   }

   public void runAuto() {
      if (this.autoEat.getValue() && mc.player != null && mc.interactionManager != null) {
         if (mc.player.getHungerManager().getFoodLevel() <= 16) {
            for (int var1 = 0; var1 < 9; var1++) {
               ItemStack var2 = mc.player.getInventory().getStack(var1);
               if (!var2.isEmpty() && var2.contains(DataComponentTypes.FOOD)) {
                  mc.player.getInventory().selectedSlot = var1;
                  mc.options.useKey.setPressed(true);
                  return;
               }
            }
         } else if (!mc.player.isUsingItem()) {
            mc.options.useKey.setPressed(false);
         }
      }
   }

   public void run() {
      long var1 = System.currentTimeMillis();
      switch (this.state) {
         case SCANNING:
            if (this.getMc()) {
               this.state = AutoEnd.State.DEPOSITING;
               this.longVal = var1;
               return;
            }

            this.class_2338 = this.getNext();
            if (this.class_2338 != null) {
               this.state = AutoEnd.State.NAVIGATING;
               this.stopHelper.handleGoal(this.class_2338, 2);
            }
            break;
         case NAVIGATING:
            if (this.class_2338 == null) {
               this.state = AutoEnd.State.SCANNING;
               return;
            }

            double var10 = mc.player.getBlockPos().getSquaredDistance(this.class_2338);
            if (var10 <= 9.0 || this.stopHelper.check2()) {
               this.stopHelper.runStop();
               this.state = AutoEnd.State.OPENING;
               this.longVal = var1;
            } else if (this.stopHelper.check3()) {
               this.set.add(this.class_2338);
               this.state = AutoEnd.State.SCANNING;
            }
            break;
         case OPENING:
            if (mc.currentScreen != null) {
               this.state = AutoEnd.State.LOOTING;
               this.longVal = var1;
               return;
            }

            if (var1 - this.longVal > 500L && this.class_2338 != null) {
               this.longVal = var1;
               Vec3d var9 = Vec3d.ofCenter(this.class_2338);
               BlockHitResult var12 = new BlockHitResult(var9, Direction.UP, this.class_2338, false);
               if (mc.interactionManager != null) {
                  mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, var12);
                  mc.player.swingHand(Hand.MAIN_HAND);
               }
            }
            break;
         case LOOTING:
            if (!(mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var8)) {
               if (var1 - this.longVal > 800L) {
                  if (this.class_2338 != null) {
                     this.set.add(this.class_2338);
                  }

                  this.state = AutoEnd.State.SCANNING;
               }

               return;
            }

            if (var1 - this.longVal < this.lootDelayMs.getValue().longValue()) {
               return;
            }

            int var11 = var8.getInventory().size();
            boolean var5 = false;

            for (int var6 = 0; var6 < var11; var6++) {
               ItemStack var7 = var8.getSlot(var6).getStack();
               if (!var7.isEmpty() && this.computeContains(var7) && mc.interactionManager != null) {
                  mc.interactionManager.clickSlot(var8.syncId, var6, 0, SlotActionType.QUICK_MOVE, mc.player);
                  this.longVal = var1;
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               mc.player.closeHandledScreen();
               if (this.class_2338 != null) {
                  this.set.add(this.class_2338);
               }

               this.state = AutoEnd.State.SCANNING;
            }
            break;
         case DEPOSITING:
            if (var1 - this.longVal > 1000L) {
               String var3 = this.stringSetting.getValue();
               if (var3 != null && !var3.isEmpty() && mc.player.networkHandler != null) {
                  mc.player.networkHandler.sendChatCommand("home " + var3);
               }

               this.longVal = var1;
               this.state = AutoEnd.State.SCANNING;
            }
      }
   }

   public boolean getMc() {
      if (mc.player == null) {
         return false;
      } else {
         for (int var1 = 0; var1 < 36; var1++) {
            if (mc.player.getInventory().getStack(var1).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }

   public BlockPos getNext() {
      if (mc.player != null && mc.world != null) {
         BlockPos var1 = mc.player.getBlockPos();
         int var2 = this.searchRadius.getValue().intValue();
         BlockPos var3 = null;
         double var4 = Double.MAX_VALUE;

         for (int var6 = -var2; var6 <= var2; var6 += 16) {
            for (int var7 = -var2; var7 <= var2; var7 += 16) {
               int var8 = var1.getX() + var6 >> 4;
               int var9 = var1.getZ() + var7 >> 4;
               if (mc.world.getChunkManager().isChunkLoaded(var8, var9)) {
                  for (BlockEntity var11 : mc.world.getChunk(var8, var9).getBlockEntities().values()) {
                     if (var11 instanceof ChestBlockEntity) {
                        BlockPos var12 = var11.getPos();
                        if (!this.set.contains(var12)) {
                           double var13 = var1.getSquaredDistance(var12);
                           if (var13 < var4 && var13 <= (double)(var2 * var2)) {
                              var4 = var13;
                              var3 = var12;
                           }
                        }
                     }
                  }
               }
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public boolean computeContains(ItemStack stack) {
      return stack != null && !stack.isEmpty()
         ? stack.isOf(Items.ELYTRA)
            || stack.isOf(Items.SHULKER_SHELL)
            || stack.isOf(Items.SHULKER_BOX)
            || stack.isOf(Items.DRAGON_EGG)
            || stack.isOf(Items.DIAMOND)
            || stack.isOf(Items.DIAMOND_BLOCK)
            || stack.isOf(Items.NETHERITE_INGOT)
            || stack.isOf(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
            || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)
            || stack.isOf(Items.GOLDEN_APPLE)
            || stack.isOf(Items.TOTEM_OF_UNDYING)
            || stack.isOf(Items.ENCHANTED_BOOK)
            || stack.isOf(Items.END_CRYSTAL)
            || stack.isOf(Items.EXPERIENCE_BOTTLE)
            || stack.getItem().toString().contains("diamond_")
            || stack.getItem().toString().contains("netherite_")
         : false;
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         this.runAuto();
         this.stopHelper.runGoal();
         this.run();
      }
   }

   public static enum State {
      SCANNING,
      NAVIGATING,
      OPENING,
      LOOTING,
      DEPOSITING;

      public static AutoEnd.State[] $values() {
         return new AutoEnd.State[]{SCANNING, NAVIGATING, OPENING, LOOTING, DEPOSITING};
      }
   }
}
