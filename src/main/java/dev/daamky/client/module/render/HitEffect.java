package dev.daamky.client.module.render;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

@ModuleInfo(
   name = "Hit Effect",
   category = ModuleCategory.VISUALS,
   description = "Создает визуальные волны по блокам вокруг цели при ударе"
)
public class HitEffect extends Module {
   public static final HitEffect hitEffect = new HitEffect();
   public final FloatSetting radius = new FloatSetting("Radius").value(6.0F).range(3.0F, 12.0F).step(1.0F);
   public final FloatSetting lifetime = new FloatSetting("Lifetime").value(1200.0F).range(400.0F, 3000.0F).step(50.0F);
   public final FloatSetting maxWaves = new FloatSetting("Max Waves").value(2.0F).range(1.0F, 5.0F).step(1.0F);
   public final BooleanSetting customColor = new BooleanSetting("Custom Color").value(false);
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(255, 60, 60, 220));
   public final CopyOnWriteArrayList<HitEffect.HitWave> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
   public final ExecutorService executorService = Executors.newSingleThreadExecutor(HitEffect::lambda$new$0);
   public final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

   public HitEffect() {
      this.addSettings(new Setting[]{this.radius, this.lifetime, this.maxWaves, this.customColor, this.color});
   }

   public static HitEffect getInstance() {
      return hitEffect;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.copyOnWriteArrayList.clear();
      this.atomicBoolean.set(false);
   }

   @Override
   public void onEvent() {
      Subscription var1 = AttackEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public List<HitEffect.RenderBlock> initInit(BlockPos origin, int rad) {
      ArrayList var3 = new ArrayList();
      if (mc.world == null) {
         return var3;
      } else {
         int var4 = rad * rad;
         byte var5 = 100;

         for (int var6 = -rad; var6 <= rad && var3.size() < var5; var6++) {
            for (int var7 = -rad; var7 <= rad && var3.size() < var5; var7++) {
               int var8 = var6 * var6 + var7 * var7;
               if (var8 <= var4) {
                  BlockPos var9 = this.computeClass_2338(origin.add(var6, 0, var7));
                  if (var9 != null) {
                     BlockState var10 = mc.world.getBlockState(var9);
                     if (!var10.isAir() && !(var10.getBlock() instanceof PlantBlock)) {
                        VoxelShape var11 = var10.getOutlineShape(mc.world, var9);
                        if (!var11.isEmpty()) {
                           List var12 = var11.getBoundingBoxes();
                           if (!var12.isEmpty()) {
                              var3.add(new HitEffect.RenderBlock(var9, var12, (float)Math.sqrt((double)var8)));
                           }
                        }
                     }
                  }
               }
            }
         }

         return var3;
      }
   }

   public BlockPos computeClass_2338(BlockPos xz) {
      if (mc.world == null) {
         return null;
      } else {
         for (int var2 = 2; var2 >= -4; var2--) {
            BlockPos var3 = xz.up(var2);
            if (!mc.world.getBlockState(var3).isAir() && mc.world.getBlockState(var3.up()).isAir()) {
               return var3;
            }
         }

         return null;
      }
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData e) {
      if (this.isEnabled() && !this.copyOnWriteArrayList.isEmpty() && mc.world != null && mc.player != null) {
         long var2 = System.currentTimeMillis();
         long var4 = (long)this.lifetime.getValue().floatValue();
         int var6 = (int)this.radius.getValue().floatValue();
         Color var7;
         if (this.customColor.getValue()) {
            var7 = this.color.getValue();
         } else {
            var7 = ThemeColor.getColor4();
         }

         Iterator var8 = this.copyOnWriteArrayList.iterator();
         boolean var9 = false;

         while (var8.hasNext()) {
            HitEffect.HitWave var10 = (HitEffect.HitWave)var8.next();
            long var11 = var2 - var10.startTime;
            if (var11 > var4) {
               this.copyOnWriteArrayList.remove(var10);
            } else {
               float var13 = (float)var11 / (float)var4;
               float var14 = var13 * (float)var6;
               float var15 = (float)Math.pow((double)(1.0F - var13), 0.6);
               float var16 = var14 - 2.0F;
               float var17 = var14 + 0.5F;

               for (HitEffect.RenderBlock var19 : var10.blocks) {
                  if (!(var19.dist < var16) && !(var19.dist > var17)) {
                     float var20 = 1.0F - Math.abs(var19.dist - var14) / 2.0F;
                     var20 = Math.max(0.0F, Math.min(1.0F, var20)) * var15;
                     if (!(var20 <= 0.06F)) {
                        int var21 = (int)(var20 * (float)(var7.getAlpha() > 0 ? var7.getAlpha() : 220));
                        Color var22 = new Color(var7.getRed(), var7.getGreen(), var7.getBlue(), Math.max(0, Math.min(255, var21)));
                        float var23 = 1.0F + var20 * 1.5F;

                        for (Box var25 : var19.boxes) {
                           Box var26 = var25.offset(var19.pos);
                           Render.boxRenderer
                              .handleFloat(
                                 (float)var26.minX,
                                 (float)var26.minY,
                                 (float)var26.minZ,
                                 (float)var26.maxX,
                                 (float)var26.maxY,
                                 (float)var26.maxZ,
                                 var23,
                                 var22,
                                 BoxRenderer.Render.OUTLINE,
                                 0.0F
                              );
                           var9 = true;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void lambda$onEvent$1(AttackEvent.AttackEventData e) {
      if (this.isEnabled() && e != null && e.entity() != null && mc.world != null) {
         if (!this.atomicBoolean.get()) {
            int var2 = (int)this.maxWaves.getValue().floatValue();
            if (this.copyOnWriteArrayList.size() < var2) {
               Vec3d var3 = e.entity().getPos();
               BlockPos var4 = BlockPos.ofFloored(var3.x, var3.y - 0.1, var3.z);
               int var5 = (int)this.radius.getValue().floatValue();

               try {
                  List var6 = this.initInit(var4, var5);
                  if (!var6.isEmpty()) {
                     this.copyOnWriteArrayList.add(new HitEffect.HitWave(var6, System.currentTimeMillis()));
                  }
               } catch (Throwable var7) {
               }
            }
         }
      }
   }

   public static Thread lambda$new$0(Runnable r) {
      Thread var1 = new Thread(r, "HitEffect-Worker");
      var1.setDaemon(true);
      return var1;
   }

   private static final class HitWave {
      public final List<HitEffect.RenderBlock> blocks;
      public final long startTime;

      public HitWave(List<HitEffect.RenderBlock> blocks, long startTime) {
         this.blocks = blocksx;
         this.startTime = startTimex;
      }
   }

   private static final class RenderBlock {
      public final BlockPos pos;
      public final List<Box> boxes;
      public final float dist;

      public RenderBlock(BlockPos pos, List<Box> boxes, float dist) {
         this.pos = posx;
         this.boxes = boxesx;
         this.dist = distx;
      }
   }
}
