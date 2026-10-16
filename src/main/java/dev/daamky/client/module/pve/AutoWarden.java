package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "AutoWarden",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Автофарм сундуков Вардена на анархии"
)
public class AutoWarden extends Module {
   public static final AutoWarden autoWarden = new AutoWarden();
   public final StringSetting wh = new StringSetting("Warehouse Home").value("wh");
   public final StringSetting warden = new StringSetting("Warden Home").value("warden");
   public final BooleanSetting autoLoot = new BooleanSetting("Auto Loot").value(true);
   public final BooleanSetting autoDeposit = new BooleanSetting("Auto Deposit").value(true);
   public final BooleanSetting autoSupplies = new BooleanSetting("Auto Supplies").value(true);
   public final BooleanSetting timedOnly = new BooleanSetting("Timed Only").value(true);
   public final StringSetting invis = new StringSetting("Supply Sign").value("[invis]");
   public final FloatSetting retryDelayMs = new FloatSetting("Retry Delay (ms)").value(150.0F).range(50.0F, 1500.0F).step(10.0F);
   public static final Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
   public static final Pattern pattern2 = Pattern.compile("(\\d+)\\s*(?:с|s|сек|sec)");
   public final Set<BlockPos> set = new HashSet<>();
   public AutoWarden.FarmState farmState = AutoWarden.FarmState.IDLE;
   public BlockPos mifucyu = null;
   public long meqcadv = 0L;
   public int intVal = 0;

   public AutoWarden() {
      this.addSettings(new Setting[]{this.wh, this.warden, this.autoLoot, this.autoDeposit, this.autoSupplies, this.timedOnly, this.invis, this.retryDelayMs});
   }

   public static AutoWarden getInstance() {
      return autoWarden;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.farmState = this.autoSupplies.getValue() ? AutoWarden.FarmState.CHECK_SUPPLIES : AutoWarden.FarmState.TELEPORT_HOME;
      this.set.clear();
      this.mifucyu = null;
      this.meqcadv = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      this.getMc();
      super.onDisable();
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0)),
            PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1))
         }
      );
   }

   public void run() {
      long var1 = System.currentTimeMillis();
      this.run2();
      switch (this.farmState) {
         case IDLE:
            this.farmState = this.autoSupplies.getValue() ? AutoWarden.FarmState.CHECK_SUPPLIES : AutoWarden.FarmState.TELEPORT_HOME;
            this.meqcadv = var1;
            break;
         case CHECK_SUPPLIES:
            if (this.getMc()) {
               this.farmState = AutoWarden.FarmState.TELEPORT_HOME;
            } else {
               this.farmState = AutoWarden.FarmState.NAVIGATE_TO_SUPPLIES;
            }

            this.meqcadv = var1;
            break;
         case NAVIGATE_TO_SUPPLIES:
            BlockPos var12 = this.getCase();
            if (var12 == null) {
               this.farmState = AutoWarden.FarmState.TELEPORT_HOME;
               return;
            }

            double var16 = mc.player.getBlockPos().getSquaredDistance(var12);
            if (var16 <= 9.0) {
               this.getMc();
               this.farmState = AutoWarden.FarmState.RESTOCK;
               this.meqcadv = var1;
            } else {
               this.handleClass_2338(var12);
            }
            break;
         case RESTOCK:
            BlockPos var11 = this.getCase();
            if (var11 != null && var1 - this.meqcadv > 600L) {
               Vec3d var15 = Vec3d.ofCenter(var11);
               BlockHitResult var17 = new BlockHitResult(var15, Direction.UP, var11, false);
               if (mc.interactionManager != null) {
                  mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, var17);
                  mc.player.swingHand(Hand.MAIN_HAND);
               }

               this.meqcadv = var1;
               this.farmState = AutoWarden.FarmState.TELEPORT_HOME;
            }
            break;
         case TELEPORT_HOME:
            if (var1 - this.meqcadv > 800L) {
               if (mc.player.networkHandler != null) {
                  mc.player.networkHandler.sendChatCommand("home " + this.warden.getValue());
               }

               this.meqcadv = var1;
               this.farmState = AutoWarden.FarmState.SCAN_CHESTS;
            }
            break;
         case SCAN_CHESTS:
            this.mifucyu = this.getNext();
            if (this.mifucyu != null) {
               this.farmState = AutoWarden.FarmState.NAVIGATE_TO_CHEST;
               this.handleClass_2338(this.mifucyu);
               this.meqcadv = var1;
            }
            break;
         case NAVIGATE_TO_CHEST:
            if (this.mifucyu == null) {
               this.farmState = AutoWarden.FarmState.SCAN_CHESTS;
               return;
            }

            double var10 = mc.player.getBlockPos().getSquaredDistance(this.mifucyu);
            if (var10 <= 9.0) {
               this.getMc();
               this.farmState = AutoWarden.FarmState.WAIT_UNLOCK;
               this.meqcadv = var1;
            }
            break;
         case WAIT_UNLOCK:
            if (this.mifucyu == null) {
               this.farmState = AutoWarden.FarmState.SCAN_CHESTS;
               return;
            }

            long var9 = this.parseGroup(this.mifucyu);
            if (var9 <= 0L || !this.timedOnly.getValue()) {
               this.handleClass_23382(this.mifucyu);
               this.farmState = AutoWarden.FarmState.OPEN_CHEST;
               this.meqcadv = var1;
            }
            break;
         case OPEN_CHEST:
            if (mc.currentScreen != null) {
               this.farmState = AutoWarden.FarmState.LOOT_CHEST;
               this.meqcadv = var1;
               this.intVal = 0;
               return;
            }

            if (var1 - this.meqcadv > this.retryDelayMs.getValue().longValue() && this.mifucyu != null) {
               this.meqcadv = var1;
               Vec3d var8 = Vec3d.ofCenter(this.mifucyu);
               BlockHitResult var14 = new BlockHitResult(var8, Direction.UP, this.mifucyu, false);
               if (mc.interactionManager != null) {
                  mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, var14);
                  mc.player.swingHand(Hand.MAIN_HAND);
               }
            }
            break;
         case LOOT_CHEST:
            if (!(mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var3)) {
               if (var1 - this.meqcadv > 800L) {
                  if (this.mifucyu != null) {
                     this.set.add(this.mifucyu);
                  }

                  this.farmState = AutoWarden.FarmState.RETREAT_SAFETY;
                  this.meqcadv = var1;
               }

               return;
            }

            int var13 = var3.getInventory().size();
            boolean var5 = false;

            for (int var6 = 0; var6 < var13; var6++) {
               ItemStack var7 = var3.getSlot(var6).getStack();
               if (!var7.isEmpty() && this.computeContains(var7) && mc.interactionManager != null) {
                  mc.interactionManager.clickSlot(var3.syncId, var6, 0, SlotActionType.QUICK_MOVE, mc.player);
                  this.meqcadv = var1;
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               this.intVal++;
               if (this.intVal > 2) {
                  mc.player.closeHandledScreen();
                  if (this.mifucyu != null) {
                     this.set.add(this.mifucyu);
                  }

                  this.farmState = AutoWarden.FarmState.RETREAT_SAFETY;
                  this.meqcadv = var1;
               }
            }
            break;
         case RETREAT_SAFETY:
            if (var1 - this.meqcadv > 2000L) {
               this.farmState = this.autoDeposit.getValue() ? AutoWarden.FarmState.TELEPORT_WAREHOUSE : AutoWarden.FarmState.TELEPORT_HOME;
               this.meqcadv = var1;
            }
            break;
         case TELEPORT_WAREHOUSE:
            if (var1 - this.meqcadv > 800L) {
               if (mc.player.networkHandler != null) {
                  mc.player.networkHandler.sendChatCommand("home " + this.wh.getValue());
               }

               this.meqcadv = var1;
               this.farmState = AutoWarden.FarmState.DEPOSIT_LOOT;
            }
            break;
         case DEPOSIT_LOOT:
            if (var1 - this.meqcadv > 3000L) {
               this.farmState = AutoWarden.FarmState.CHECK_SUPPLIES;
               this.meqcadv = var1;
            }
      }
   }

   public void handleClass_2338(BlockPos pos) {
      if (pos != null && mc.player != null) {
         if (mc.player.networkHandler != null) {
            mc.player.networkHandler.sendChatMessage("#goto " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
         }
      }
   }

   public void getMc() {
      if (mc.player != null && mc.player.networkHandler != null) {
         mc.player.networkHandler.sendChatMessage("#stop");
      }
   }

   public BlockPos getNext() {
      if (mc.player != null && mc.world != null) {
         BlockPos var1 = mc.player.getBlockPos();
         BlockPos var2 = null;
         double var3 = Double.MAX_VALUE;

         for (byte var5 = -48; var5 <= 48; var5 += 16) {
            for (byte var6 = -48; var6 <= 48; var6 += 16) {
               int var7 = var1.getX() + var5 >> 4;
               int var8 = var1.getZ() + var6 >> 4;
               if (mc.world.getChunkManager().isChunkLoaded(var7, var8)) {
                  for (BlockEntity var10 : mc.world.getChunk(var7, var8).getBlockEntities().values()) {
                     if (var10 instanceof ChestBlockEntity) {
                        BlockPos var11 = var10.getPos();
                        if (!this.set.contains(var11)) {
                           double var12 = var1.getSquaredDistance(var11);
                           if (var12 < var3) {
                              var3 = var12;
                              var2 = var11;
                           }
                        }
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public BlockPos getCase() {
      if (mc.player != null && mc.world != null) {
         BlockPos var1 = mc.player.getBlockPos();
         String var2 = this.invis.getValue().toLowerCase();

         for (byte var3 = -32; var3 <= 32; var3 += 16) {
            for (byte var4 = -32; var4 <= 32; var4 += 16) {
               int var5 = var1.getX() + var3 >> 4;
               int var6 = var1.getZ() + var4 >> 4;
               if (mc.world.getChunkManager().isChunkLoaded(var5, var6)) {
                  for (BlockEntity var8 : mc.world.getChunk(var5, var6).getBlockEntities().values()) {
                     if (var8 instanceof SignBlockEntity) {
                        SignBlockEntity var9 = (SignBlockEntity)var8;
                        SignText var10 = var9.getFrontText();

                        for (int var11 = 0; var11 < 4; var11++) {
                           if (var10.getMessage(var11, false).getString().toLowerCase().contains(var2)) {
                              return var9.getPos();
                           }
                        }
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public long parseGroup(BlockPos chestPos) {
      if (mc.world != null && chestPos != null) {
         Box var2 = new Box(chestPos).expand(3.0, 4.0, 3.0);

         for (Entity var5 : mc.world.getOtherEntities(mc.player, var2)) {
            String var6;
            label33: {
               var6 = null;
               if (var5 instanceof TextDisplayEntity var7 && var7.getCustomName() != null) {
                  var6 = var7.getCustomName().getString();
                  break label33;
               }

               if (var5 instanceof ArmorStandEntity var8 && var8.hasCustomName()) {
                  var6 = var8.getCustomName().getString();
               }
            }

            if (var6 != null) {
               String var11 = var6.replaceAll("§[0-9a-fk-orA-FK-OR]", "").trim();
               Matcher var12 = pattern.matcher(var11);
               if (var12.find()) {
                  int var13 = Integer.parseInt(var12.group(1));
                  int var10 = Integer.parseInt(var12.group(2));
                  return(longVal)var13 * 60L + (long)var10;
               }

               Matcher var9 = pattern2.matcher(var11);
               if (var9.find()) {
                  return Long.parseLong(var9.group(1));
               }
            }
         }

         return -1L;
      } else {
         return -1L;
      }
   }

   public void handleClass_23382(BlockPos chestPos) {
      if (mc.world != null && chestPos != null) {
         BlockPos var2 = chestPos.up();
         if (mc.world.getBlockState(var2).getBlock() instanceof CandleBlock && mc.interactionManager != null) {
            mc.interactionManager.updateBlockBreakingProgress(var2, Direction.UP);
            mc.player.swingHand(Hand.MAIN_HAND);
         }
      }
   }

   public boolean computeNext(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         if (stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION)) {
            PotionContentsComponent var2 = (PotionContentsComponent)stack.get(DataComponentTypes.POTION_CONTENTS);
            if (var2 != null) {
               for (StatusEffectInstance var4 : var2.getEffects()) {
                  if (var4.getEffectType().equals(StatusEffects.INVISIBILITY)) {
                     return true;
                  }
               }

               if (var2.potion().isPresent()) {
                  for (StatusEffectInstance var6 : ((Potion)((RegistryEntry)var2.potion().get()).value()).getEffects()) {
                     if (var6.getEffectType().equals(StatusEffects.INVISIBILITY)) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void run2() {
      if (mc.player != null && mc.interactionManager != null) {
         if (!mc.player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            for (int var1 = 0; var1 < 9; var1++) {
               ItemStack var2 = mc.player.getInventory().getStack(var1);
               if (var2.isOf(Items.SPLASH_POTION) && this.computeNext(var2)) {
                  mc.player.getInventory().selectedSlot = var1;
                  mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                  return;
               }
            }

            for (int var3 = 9; var3 < 36; var3++) {
               ItemStack var4 = mc.player.getInventory().getStack(var3);
               if (var4.isOf(Items.SPLASH_POTION) && this.computeNext(var4)) {
                  mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var3, 0, SlotActionType.QUICK_MOVE, mc.player);
                  return;
               }
            }
         }
      }
   }

   public boolean computeContains(ItemStack stack) {
      return stack != null && !stack.isEmpty()
         ? stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)
            || stack.isOf(Items.GOLDEN_APPLE)
            || stack.isOf(Items.TOTEM_OF_UNDYING)
            || stack.isOf(Items.NETHERITE_INGOT)
            || stack.isOf(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
            || stack.isOf(Items.DIAMOND_BLOCK)
            || stack.isOf(Items.END_CRYSTAL)
            || stack.isOf(Items.EXPERIENCE_BOTTLE)
            || stack.isOf(Items.SHULKER_BOX)
            || stack.isOf(Items.ENCHANTED_BOOK)
            || stack.getItem().toString().contains("netherite_")
            || stack.getItem().toString().contains("diamond_")
         : false;
   }

   public void lambda$onEvent$1(PacketEvent.PacketEventData event) {
      if (event.isReceive() && event.packet() instanceof GameMessageS2CPacket var2) {
         String var4 = var2.content().getString().toLowerCase();
         if (var4.contains("телепортация") || var4.contains("teleport")) {
            this.meqcadv = System.currentTimeMillis();
         }
      }
   }

   public void lambda$onEvent$0(UpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         this.run();
      }
   }

   public static enum FarmState {
      IDLE,
      CHECK_SUPPLIES,
      NAVIGATE_TO_SUPPLIES,
      RESTOCK,
      TELEPORT_HOME,
      SCAN_CHESTS,
      NAVIGATE_TO_CHEST,
      WAIT_UNLOCK,
      OPEN_CHEST,
      LOOT_CHEST,
      RETREAT_SAFETY,
      TELEPORT_WAREHOUSE,
      DEPOSIT_LOOT;

      public static AutoWarden.FarmState[] $values() {
         return new AutoWarden.FarmState[]{
            IDLE,
            CHECK_SUPPLIES,
            NAVIGATE_TO_SUPPLIES,
            RESTOCK,
            TELEPORT_HOME,
            SCAN_CHESTS,
            NAVIGATE_TO_CHEST,
            WAIT_UNLOCK,
            OPEN_CHEST,
            LOOT_CHEST,
            RETREAT_SAFETY,
            TELEPORT_WAREHOUSE,
            DEPOSIT_LOOT
         };
      }
   }
}
