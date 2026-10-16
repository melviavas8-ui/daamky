package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.Class_1792Holder;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(
   name = "MineHelper",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Подсвечивает руды рядом с игроком (алмазы, золото, железо и т.д.)"
)
public class MineHelper extends Module {
   public static MineHelper mineHelper;
   public final Class_1792Holder mseyb = new Class_1792Holder("Diamond Ore", Items.DIAMOND_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder = new Class_1792Holder("Gold Ore", Items.GOLD_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder2 = new Class_1792Holder("Iron Ore", Items.IRON_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder3 = new Class_1792Holder("Coal Ore", Items.COAL_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder4 = new Class_1792Holder("Lapis Ore", Items.LAPIS_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder5 = new Class_1792Holder("Redstone Ore", Items.REDSTONE_ORE).computeClass_1792Holder(true);
   public final Class_1792Holder class_1792Holder6 = new Class_1792Holder("Nether Quartz Ore", Items.NETHER_QUARTZ_ORE).computeClass_1792Holder(true);
   public final MultiBooleanSetting multiBooleanSetting = new MultiBooleanSetting("Ores")
      .value(
         this.mseyb,
         this.class_1792Holder,
         this.class_1792Holder2,
         this.class_1792Holder3,
         this.class_1792Holder4,
         this.class_1792Holder5,
         this.class_1792Holder6
      );
   public final FloatSetting range = new FloatSetting("Range").value(24.0F).range(8.0F, 64.0F).step(1.0F);
   public final FloatSetting scanDelayMs = new FloatSetting("Scan Delay (ms)").value(500.0F).range(100.0F, 2000.0F).step(50.0F);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Outline").values("Fill", "Outline", "Both");
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(0, 255, 255, 200))
      .computeVisible(this::lambda$new$0);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(0, 255, 255, 50)).computeVisible(this::lambda$new$1);
   public final List<BlockPos> list = new CopyOnWriteArrayList<>();
   public long longVal = 0L;
   public Thread mvuihof;

   public static MineHelper getInstance() {
      if (mineHelper == null) {
         mineHelper = new MineHelper();
      }

      return mineHelper;
   }

   public List<String> initAdd() {
      ArrayList var1 = new ArrayList();
      if (this.mseyb.getValue()) {
         var1.add("diamond_ore deepslate_diamond_ore");
      }

      if (this.class_1792Holder.getValue()) {
         var1.add("gold_ore deepslate_gold_ore nether_gold_ore");
      }

      if (this.class_1792Holder2.getValue()) {
         var1.add("iron_ore deepslate_iron_ore");
      }

      if (this.class_1792Holder3.getValue()) {
         var1.add("coal_ore deepslate_coal_ore");
      }

      if (this.class_1792Holder4.getValue()) {
         var1.add("lapis_ore deepslate_lapis_ore");
      }

      if (this.class_1792Holder5.getValue()) {
         var1.add("redstone_ore deepslate_redstone_ore");
      }

      if (this.class_1792Holder6.getValue()) {
         var1.add("nether_quartz_ore");
      }

      return var1;
   }

   public MineHelper() {
      this.addSettings(new Setting[]{this.multiBooleanSetting, this.range, this.scanDelayMs, this.both, this.outlineColor, this.fillColor});
      mineHelper = this;
      Runnable var1 = this::lambda$new$2;
      this.mseyb.onAction(var1);
      this.class_1792Holder.onAction(var1);
      this.class_1792Holder2.onAction(var1);
      this.class_1792Holder3.onAction(var1);
      this.class_1792Holder4.onAction(var1);
      this.class_1792Holder5.onAction(var1);
      this.class_1792Holder6.onAction(var1);
      this.range.onAction(var1);
      this.scanDelayMs.onAction(var1);
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3)),
            Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4))
         }
      );
   }

   @Override
   public void onEnable() {
      this.list.clear();
      this.longVal = 0L;
   }

   @Override
   public void onDisable() {
      this.list.clear();
      super.onDisable();
   }

   public boolean computeFlag(Block block) {
      if (!this.mseyb.getValue() || block != Blocks.DIAMOND_ORE && block != Blocks.DEEPSLATE_DIAMOND_ORE) {
         if (!this.class_1792Holder.getValue() || block != Blocks.GOLD_ORE && block != Blocks.DEEPSLATE_GOLD_ORE && block != Blocks.NETHER_GOLD_ORE) {
            if (!this.class_1792Holder2.getValue() || block != Blocks.IRON_ORE && block != Blocks.DEEPSLATE_IRON_ORE) {
               if (!this.class_1792Holder3.getValue() || block != Blocks.COAL_ORE && block != Blocks.DEEPSLATE_COAL_ORE) {
                  if (!this.class_1792Holder4.getValue() || block != Blocks.LAPIS_ORE && block != Blocks.DEEPSLATE_LAPIS_ORE) {
                     return !this.class_1792Holder5.getValue() || block != Blocks.REDSTONE_ORE && block != Blocks.DEEPSLATE_REDSTONE_ORE
                        ? this.class_1792Holder6.getValue() && block == Blocks.NETHER_QUARTZ_ORE
                        : true;
                  } else {
                     return true;
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public void handleBoth(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && mc.player != null && mc.world != null) {
         boolean var2 = this.both.is("Outline") || this.both.is("Both");
         boolean var3 = this.both.is("Fill") || this.both.is("Both");
         ClientWorld var4 = mc.world;

         for (BlockPos var6 : this.list) {
            if (this.computeFlag(var4.getBlockState(var6).getBlock())) {
               float var7 = (float)var6.getX();
               float var8 = (float)var6.getY();
               float var9 = (float)var6.getZ();
               float var10 = var7 + 1.0F;
               float var11 = var8 + 1.0F;
               float var12 = var9 + 1.0F;
               if (var3) {
                  Render.boxRenderer.handleFloat(var7, var8, var9, var10, var11, var12, 0.0F, this.fillColor.getValue(), BoxRenderer.Render.FILL, 0.0F);
               }

               if (var2) {
                  Render.boxRenderer.handleFloat(var7, var8, var9, var10, var11, var12, 1.5F, this.outlineColor.getValue(), BoxRenderer.Render.OUTLINE, 0.0F);
               }
            }
         }
      }
   }

   public void lambda$onEvent$4(Render3DEvent.Render3DEventData event) {
      this.handleBoth(event);
   }

   public void lambda$onEvent$3(PostMotionEvent.EventPostMotionData event) {
      this.initAdd();
   }

   public void lambda$new$2() {
      this.list.clear();
      this.longVal = 0L;
   }

   public Boolean lambda$new$1() {
      return this.both.is("Fill") || this.both.is("Both");
   }

   public Boolean lambda$new$0() {
      return this.both.is("Outline") || this.both.is("Both");
   }
}
