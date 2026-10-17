package dev.daamky.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.ContextHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Matrix4f;
import org.joml.Vector2f;

@ModuleInfo(
   name = "Trajectories",
   category = ModuleCategory.VISUALS,
   description = "Показывает траекторию полета стрел и зелий"
)
public class Trajectories extends Module {
   public static final Trajectories mpiav = new Trajectories();
   public final FloatSetting lineWidth = new FloatSetting("Line Width").value(2.5F).range(1.0F, 6.0F).step(0.5F);
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(180, 120, 255, 255));
   public final FloatSetting boxSize = new FloatSetting("Box Size").value(0.2F).range(0.05F, 0.5F).step(0.05F);
   public final BooleanSetting trackFlyingItems = new BooleanSetting("Track Flying Items").value(true);
   public long longVal = System.currentTimeMillis();
   public Vec3d class_243 = null;
   public boolean flag = false;
   public final List<Trajectories.ProjectilePrediction> miyje = new ArrayList<>();
   public final List<Trajectories.PathInfo> list = new ArrayList<>();
   public final Map<Entity, Box> map = new HashMap<>();
   public double doubleVal;
   public double mosut;
   public double double2;
   public float floatVal;
   public float maipi;
   public ItemStack mlouyy = ItemStack.EMPTY;
   public long long2 = 0L;
   public final List<Entity> list2 = new ArrayList<>();

   public Trajectories() {
      this.addSettings(new Setting[]{this.lineWidth, this.color, this.boxSize, this.trackFlyingItems});
   }

   @Override
   public void onEnable() {
      this.longVal = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.map.clear();
      this.list2.clear();
      this.list.clear();
      this.miyje.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = Render2DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void initInit(BufferBuilder buffer, Matrix4f matrix, List<Vec3d> points, Camera camera, int colorRgb, float width) {
      int var7 = points.size();
      if (var7 >= 2) {
         int var8 = colorRgb >> 24 & 0xFF;
         if (var8 <= 0) {
            var8 = 255;
         }

         int var9 = colorRgb >> 16 & 0xFF;
         int var10 = colorRgb >> 8 & 0xFF;
         int var11 = colorRgb & 0xFF;
         int var12 = var9 << 16 | var10 << 8 | var11 | var8 << 24;
         Vec3d var13 = camera.getPos();
         float var14 = width * 0.004F;
         Vec3d[] var15 = new Vec3d[var7];

         for (int var16 = 0; var16 < var7; var16++) {
            Vec3d var17 = (Vec3d)points.get(var16);
            Vec3d var18;
            if (var16 == 0) {
               var18 = ((Vec3d)points.get(1)).subtract(var17).normalize();
            } else if (var16 == var7 - 1) {
               var18 = var17.subtract((Vec3d)points.get(var7 - 2)).normalize();
            } else {
               var18 = ((Vec3d)points.get(var16 + 1)).subtract((Vec3d)points.get(var16 - 1)).normalize();
            }

            if (var18.lengthSquared() < 1.0E-5) {
               var18 = new Vec3d(0.0, 1.0, 0.0);
            }

            Vec3d var19 = var13.subtract(var17);
            Vec3d var20 = var18.crossProduct(var19);
            double var21 = var20.length();
            if (var21 > 1.0E-5) {
               var20 = var20.multiply(1.0 / var21);
            } else {
               var20 = new Vec3d(1.0, 0.0, 0.0);
            }

            var15[var16] = var20.multiply((double)var14);
         }

         for (int var23 = 0; var23 < var7 - 1; var23++) {
            Vec3d var24 = (Vec3d)points.get(var23);
            Vec3d var25 = (Vec3d)points.get(var23 + 1);
            Vec3d var26 = var15[var23];
            Vec3d var28 = var15[var23 + 1];
            buffer.vertex(
                  matrix,
                  (float)(var24.x - var26.x),
                  (float)(var24.y - var26.y),
                  (float)(var24.z - var26.z)
               )
               .color(var12);
            buffer.vertex(
                  matrix,
                  (float)(var24.x + var26.x),
                  (float)(var24.y + var26.y),
                  (float)(var24.z + var26.z)
               )
               .color(var12);
            buffer.vertex(
                  matrix,
                  (float)(var25.x + var28.x),
                  (float)(var25.y + var28.y),
                  (float)(var25.z + var28.z)
               )
               .color(var12);
            buffer.vertex(
                  matrix,
                  (float)(var25.x - var28.x),
                  (float)(var25.y - var28.y),
                  (float)(var25.z - var28.z)
               )
               .color(var12);
         }
      }
   }

   public void handleMin(BufferBuilder buffer, Matrix4f matrix, Vec3d pos, Color color, boolean isEntity) {
      float var6 = isEntity ? 0.45F : this.boxSize.getValue();
      float var7 = (float)(pos.x - (double)var6);
      float var8 = (float)(pos.y - (double)var6);
      float var9 = (float)(pos.z - (double)var6);
      float var10 = (float)(pos.x + (double)var6);
      float var11 = (float)(pos.y + (double)var6);
      float var12 = (float)(pos.z + (double)var6);
      int var13 = color.getAlpha();
      int var14 = color.getRGB() & 16777215 | Math.min(255, (int)((float)var13 * 0.25F)) << 24;
      buffer.vertex(matrix, var7, var8, var9).color(var14);
      buffer.vertex(matrix, var10, var8, var9).color(var14);
      buffer.vertex(matrix, var10, var8, var12).color(var14);
      buffer.vertex(matrix, var7, var8, var12).color(var14);
      buffer.vertex(matrix, var7, var11, var12).color(var14);
      buffer.vertex(matrix, var10, var11, var12).color(var14);
      buffer.vertex(matrix, var10, var11, var9).color(var14);
      buffer.vertex(matrix, var7, var11, var9).color(var14);
      buffer.vertex(matrix, var7, var8, var9).color(var14);
      buffer.vertex(matrix, var7, var11, var9).color(var14);
      buffer.vertex(matrix, var10, var11, var9).color(var14);
      buffer.vertex(matrix, var10, var8, var9).color(var14);
      buffer.vertex(matrix, var10, var8, var12).color(var14);
      buffer.vertex(matrix, var10, var11, var12).color(var14);
      buffer.vertex(matrix, var7, var11, var12).color(var14);
      buffer.vertex(matrix, var7, var8, var12).color(var14);
      buffer.vertex(matrix, var7, var8, var12).color(var14);
      buffer.vertex(matrix, var7, var11, var12).color(var14);
      buffer.vertex(matrix, var7, var11, var9).color(var14);
      buffer.vertex(matrix, var7, var8, var9).color(var14);
      buffer.vertex(matrix, var10, var8, var9).color(var14);
      buffer.vertex(matrix, var10, var11, var9).color(var14);
      buffer.vertex(matrix, var10, var11, var12).color(var14);
      buffer.vertex(matrix, var10, var8, var12).color(var14);
   }

   public String computeArrow(Item item) {
      if (item instanceof BowItem || item instanceof CrossbowItem) {
         return "Arrow";
      } else if (item instanceof TridentItem) {
         return "Trident";
      } else if (item instanceof EnderPearlItem) {
         return "Ender Pearl";
      } else if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) {
         return "Potion";
      } else if (item instanceof SnowballItem) {
         return "Snowball";
      } else {
         return item instanceof EggItem ? "Egg" : "Projectile";
      }
   }

   public String computeArrow(Entity entity) {
      if (entity instanceof EnderPearlEntity) {
         return "Ender Pearl";
      } else if (entity instanceof PotionEntity) {
         return "Potion";
      } else if (entity instanceof SnowballEntity) {
         return "Snowball";
      } else if (entity instanceof EggEntity) {
         return "Egg";
      } else if (entity instanceof TridentEntity) {
         return "Trident";
      } else {
         return entity instanceof PersistentProjectileEntity ? "Arrow" : "Projectile";
      }
   }

   public float computeFloat(Item item) {
      if (item instanceof BowItem) {
         int var2 = mc.player.getItemUseTimeLeft();
         if (var2 == 0) {
            return 3.0F;
         } else {
            float var3 = (float)(72000 - var2) / 20.0F;
            var3 = (var3 * var3 + var3 * 2.0F) / 3.0F;
            if ((float)var2 > 1.0F) {
               var3 = 1.0F;
            }

            return var3 * 3.0F;
         }
      } else if (item instanceof CrossbowItem) {
         return 3.15F;
      } else if (item instanceof TridentItem) {
         return 2.5F;
      } else {
         return !(item instanceof SplashPotionItem) && !(item instanceof LingeringPotionItem) ? 1.5F : 0.5F;
      }
   }

   public float computeFloat2(Item item) {
      if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
         return 0.05F;
      } else {
         return !(item instanceof SplashPotionItem) && !(item instanceof LingeringPotionItem) ? 0.03F : 0.05F;
      }
   }

   public float computeFloat(Entity entity) {
      if (entity instanceof PotionEntity) {
         return 0.05F;
      } else {
         return entity instanceof PersistentProjectileEntity ? 0.05F : 0.03F;
      }
   }

   public float computeFloat2(Entity entity) {
      return entity.isTouchingWater() ? 0.8F : 0.99F;
   }

   public ItemStack initInit(Entity entity) {
      if (entity instanceof EnderPearlEntity) {
         return new ItemStack(Items.ENDER_PEARL);
      } else if (!(entity instanceof PotionEntity var2)) {
         if (entity instanceof SnowballEntity) {
            return new ItemStack(Items.SNOWBALL);
         } else if (entity instanceof EggEntity) {
            return new ItemStack(Items.EGG);
         } else if (entity instanceof TridentEntity) {
            return new ItemStack(Items.TRIDENT);
         } else {
            return entity instanceof PersistentProjectileEntity ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
         }
      } else {
         ItemStack var3 = var2.getStack();
         return var3 != null && !var3.isEmpty() ? var3 : new ItemStack(Items.SPLASH_POTION);
      }
   }

   public static Trajectories getInstance() {
      return mpiav;
   }

   public void lambda$onEvent$2(Render2DEvent.Render2DEventData event) {
      MatrixStack var2 = event.getMatrixStack();
      DrawContext var3 = event.getContext();
      ContextHelper.Context var4 = new ContextHelper.Context();
      if (!this.list.isEmpty() && mc.options.getPerspective().isFirstPerson()) {
         for (Trajectories.PathInfo var6 : this.list) {
            if (var6.class_243 != null) {
               Vector2f var7 = var4.initInit(
                  (double)((float)var6.class_243.x),
                  (double)((float)var6.class_243.y + (var6.mlowk ? 0.75F : 0.35F)),
                  (double)((float)var6.class_243.z)
               );
               if (var7.x != Float.MAX_VALUE && var7.y != Float.MAX_VALUE) {
                  int var8 = var6.intVal;
                  double var9 = (double)var8 * 0.05;
                  String var11 = String.format(Locale.ROOT, "%.1f сек", var9).replace('.', ',');
                  float var12 = 6.8F;
                  float var13 = FontBootstrap.clientFont6.computeFloat(var11, var12);
                  float var14 = 9.5F;
                  float var15 = 4.5F;
                  float var16 = 3.5F;
                  boolean var17 = var6.class_1799 != null && !var6.class_1799.isEmpty();
                  float var18 = (var17 ? var14 + var16 : 0.0F) + var13;
                  float var19 = var15 * 2.0F + var18;
                  float var20 = 14.0F;
                  float var21 = var7.x - var19 / 2.0F;
                  float var22 = var7.y - var20 / 2.0F;
                  Render.rectRenderer.handleRgb(var2, var21 - 0.5F, var22 - 0.5F, var19 + 1.0F, var20 + 1.0F, 4.5F, new Color(255, 255, 255, 28));
                  Render.rectRenderer.handleRgb(var2, var21, var22, var19, var20, 4.0F, new Color(10, 12, 17, 240));
                  float var23 = var21 + var15;
                  if (var17 && var3 != null) {
                     var2.push();
                     var2.translate(var23, var22 + (var20 - var14) / 2.0F, 0.0F);
                     var2.scale(var14 / 16.0F, var14 / 16.0F, 1.0F);
                     var3.drawItem(var6.class_1799, 0, 0);
                     var2.pop();
                     var23 += var14 + var16;
                  }

                  float var24 = var22 + (var20 - var12) / 2.0F - 0.4F;
                  FontBootstrap.clientFont6.handleColor(var2, var11, var23, var24, var12, new Color(255, 255, 255, 255));
               }
            }
         }
      }
   }

   public void lambda$onEvent$1(Render3DEvent.Render3DEventData event) {
      this.class_243 = null;
      this.miyje.clear();
      MatrixStack var2 = event.getMatrixStack();
      Color var3 = this.color.getValue();
      Vec3d var4 = mc.getEntityRenderDispatcher().camera.getPos();
      var2.push();
      var2.translate(-var4.x, -var4.y, -var4.z);
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      Matrix4f var5 = var2.peek().getPositionMatrix();
      ItemStack var6 = mc.player.getMainHandStack();
      boolean var7 = false;
      Item var8 = null;
      if (var6 != null && !var6.isEmpty()) {
         var8 = var6.getItem();
         var7 = var8 instanceof BowItem
            || var8 instanceof CrossbowItem
            || var8 instanceof TridentItem
            || var8 instanceof EnderPearlItem
            || var8 instanceof SplashPotionItem
            || var8 instanceof LingeringPotionItem
            || var8 instanceof SnowballItem
            || var8 instanceof EggItem;
      }

      long var9 = System.currentTimeMillis();
      boolean var11 = var9 - this.long2 > 50L
         || mc.player.getX() != this.doubleVal
         || mc.player.getY() != this.mosut
         || mc.player.getZ() != this.double2
         || mc.player.getYaw() != this.floatVal
         || mc.player.getPitch() != this.maipi
         || !ItemStack.areEqual(var6, this.mlouyy);
      if (var11) {
         this.long2 = var9;
         this.doubleVal = mc.player.getX();
         this.mosut = mc.player.getY();
         this.double2 = mc.player.getZ();
         this.floatVal = mc.player.getYaw();
         this.maipi = mc.player.getPitch();
         this.mlouyy = var6.copy();
         this.list.clear();
         this.map.clear();

         for (Entity var13 : this.list2) {
            if (var13.canHit()) {
               this.map.put(var13, var13.getBoundingBox().expand(0.3));
            }
         }

         if (var7) {
            boolean var65 = var8 instanceof BowItem;
            boolean var68 = var8 instanceof CrossbowItem;
            boolean var14 = var8 instanceof TridentItem;
            boolean var15 = var8 instanceof EnderPearlItem;
            boolean var16 = var8 instanceof SplashPotionItem || var8 instanceof LingeringPotionItem;
            float var17 = mc.player.getPitch();
            float var18 = mc.player.getYaw();
            boolean var19 = false;
            if (var68) {
               ItemEnchantmentsComponent var20 = EnchantmentHelper.getEnchantments(var6);

               for (Entry var22 : var20.getEnchantmentEntries()) {
                  if ("minecraft:multishot".equals(((RegistryEntry)var22.getKey()).getIdAsString())) {
                     var19 = true;
                     break;
                  }
               }
            }

            int var79 = var19 ? 3 : 1;

            for (int var81 = 0; var81 < var79; var81++) {
               float var82 = 0.0F;
               if (var19) {
                  if (var81 == 0) {
                     var82 = -10.0F;
                  }

                  if (var81 == 2) {
                     var82 = 10.0F;
                  }
               }

               float var23 = var18 + var82;
               double var24 = mc.player.getX() - (double)(MathHelper.cos(var23 / 180.0F * (float) Math.PI) * 0.16F);
               double var26 = mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()) - 0.1F;
               double var28 = mc.player.getZ() - (double)(MathHelper.sin(var23 / 180.0F * (float) Math.PI) * 0.16F);
               double var30 = (double)(-MathHelper.sin(var23 / 180.0F * (float) Math.PI) * MathHelper.cos(var17 / 180.0F * (float) Math.PI));
               double var32 = (double)(MathHelper.cos(var23 / 180.0F * (float) Math.PI) * MathHelper.cos(var17 / 180.0F * (float) Math.PI));
               double var34 = (double)(-MathHelper.sin((var17 - (float)(var16 ? 20 : 0)) / 180.0F * (float) Math.PI));
               float var36 = this.computeFloat(var8);
               double var37 = Math.sqrt(var30 * var30 + var34 * var34 + var32 * var32);
               var30 = var30 / var37 * (double)var36;
               var34 = var34 / var37 * (double)var36;
               var32 = var32 / var37 * (double)var36;
               double var39 = var24;
               double var41 = var26;
               double var43 = var28;
               float var45 = this.computeFloat2(var8);
               boolean var46 = false;
               Object var47 = null;
               Trajectories.PathInfo var48 = new Trajectories.PathInfo();
               var48.string = this.computeArrow(var8);
               var48.class_1799 = var6 != null ? var6.copy() : ItemStack.EMPTY;
               var48.list.add(new Vec3d(var24, var26, var28));

               for (int var49 = 0; var49 < 300; var49++) {
                  double var90 = var34 * 0.99;
                  Vec3d var56 = new Vec3d(var39, var41, var43);
                  Vec3d var57 = new Vec3d(var39 += var30 *= 0.99, var41 += var34 = var90 - (double)var45, var43 += var32 *= 0.99);
                  BlockHitResult var58 = mc.world.raycast(new RaycastContext(var56, var57, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
                  if (var58.getType() != Type.MISS) {
                     var57 = var58.getPos();
                     var47 = var58;
                  }

                  for (java.util.Map.Entry var60 : this.map.entrySet()) {
                     Entity var62 = (Entity)var60.getKey();
                     Box var63 = (Box)var60.getValue();
                     Optional var61;
                     if ((
                           var63.intersects(var56.x, var56.y, var56.z, var57.x, var57.y, var57.z)
                              || var63.contains(var57)
                        )
                        && (var61 = var63.raycast(var56, var57)).isPresent()) {
                        var57 = (Vec3d)var61.get();
                        var47 = new EntityHitResult(var62, var57);
                        var46 = true;
                        break;
                     }
                  }

                  var48.list.add(var57);
                  if (var47 != null) {
                     var48.intVal = var49 + 1;
                     break;
                  }
               }

               if (var47 != null) {
                  Vec3d var96;
                  var48.class_243 = var96 = var48.list.get(var48.list.size() - 1);
                  var48.mlowk = var46;
                  if (!var19 || var81 == 1) {
                     this.class_243 = var96;
                     this.flag = var46;
                  }
               }

               this.list.add(var48);
            }
         }

         if (this.trackFlyingItems.getValue()) {
            for (Entity var69 : this.list2) {
               boolean var71 = var69 instanceof ProjectileEntity;
               if (var71) {
                  Trajectories.PathInfo var73 = new Trajectories.PathInfo();
                  var73.string = this.computeArrow(var69);
                  var73.muhoj = true;
                  var73.class_1799 = this.initInit(var69);
                  double var76 = var69.getX();
                  double var78 = var69.getY() + (double)var69.getHeight() / 2.0;
                  double var80 = var69.getZ();
                  Vec3d var83 = var69.getVelocity();
                  double var84 = var83.x;
                  double var25 = var83.y;
                  double var27 = var83.z;
                  float var29 = this.computeFloat(var69);
                  float var86 = this.computeFloat2(var69);
                  boolean var31 = false;
                  Object var88 = null;
                  var73.list.add(new Vec3d(var76, var78, var80));

                  for (int var33 = 0; var33 < 300; var33++) {
                     var84 *= (double)var86;
                     var25 = (var25 - (double)var29) * (double)var86;
                     var27 *= (double)var86;
                     Vec3d var40 = new Vec3d(var76, var78, var80);
                     Vec3d var91 = new Vec3d(var76 += var84, var78 += var25, var80 += var27);
                     BlockHitResult var42 = mc.world.raycast(new RaycastContext(var40, var91, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
                     if (var42.getType() != Type.MISS) {
                        var91 = var42.getPos();
                        var88 = var42;
                     }

                     for (java.util.Map.Entry var44 : this.map.entrySet()) {
                        Entity var93 = (Entity)var44.getKey();
                        if (var93 != var69) {
                           Box var94 = (Box)var44.getValue();
                           if (var94.intersects(var40.x, var40.y, var40.z, var91.x, var91.y, var91.z)
                              || var94.contains(var91)) {
                              Optional var95 = var94.raycast(var40, var91);
                              if (var95.isPresent()) {
                                 var91 = (Vec3d)var95.get();
                                 var88 = new EntityHitResult(var93, var91);
                                 var31 = true;
                                 break;
                              }
                           }
                        }
                     }

                     var73.list.add(var91);
                     if (var88 != null) {
                        var73.class_243 = var91;
                        var73.mlowk = var31;
                        var73.intVal = var33 + 1;
                        break;
                     }
                  }

                  if (var73.class_243 != null) {
                     this.list.add(var73);
                  }
               }
            }
         }
      }

      Render.renderEffect.handleBlend(var2);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      float var67 = this.lineWidth.getValue();
      Camera var70 = mc.gameRenderer.getCamera();
      BufferBuilder var72 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

      for (Trajectories.PathInfo var77 : this.list) {
         if (var77.list.size() >= 2) {
            this.initInit(var72, var5, var77.list, var70, var3.getRGB(), var67);
         }

         if (var77.class_243 != null) {
            this.handleMin(var72, var5, var77.class_243, var3, var77.mlowk);
         }
      }

      try {
         BuiltBuffer var75 = var72.end();
         if (var75 != null) {
            BufferRenderer.drawWithGlobalProgram(var75);
         }
      } catch (Exception var64) {
      }

      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      var2.pop();
      var2.pop();
   }

   public void lambda$onEvent$0(TickEvent event) {
      if (mc.world != null && mc.player != null) {
         this.list2.clear();

         for (Entity var3 : mc.world.getOtherEntities(mc.player, new Box(mc.player.getBlockPos()).expand(128.0))) {
            if (var3.canHit() || var3 instanceof ProjectileEntity) {
               this.list2.add(var3);
            }
         }
      }
   }

   private static class PathInfo {
      public final List<Vec3d> list = new ArrayList<>();
      public Vec3d class_243 = null;
      public boolean mlowk = false;
      public int intVal = 0;
      public String string = "Landing";
      public boolean muhoj = false;
      public ItemStack class_1799 = ItemStack.EMPTY;

      public PathInfo() {
      }
   }

   private static class ProjectilePrediction {
      public Vec3d pos;
      public boolean hitEntity;
      public int ticks;

      public ProjectilePrediction(Vec3d pos, boolean hitEntity, int ticks) {
         this.pos = pos;
         this.hitEntity = hitEntity;
         this.ticks = ticks;
      }
   }
}
