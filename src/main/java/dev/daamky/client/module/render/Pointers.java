package dev.daamky.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.EntityFilterHelper;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Pointers",
   category = ModuleCategory.VISUALS,
   description = "Показывает стрелочками, где находятся враги"
)
public class Pointers extends Module {
   public static final Pointers pointers = new Pointers();
   public final MultiBooleanSetting items = new MultiBooleanSetting("Targets")
      .value(
         new BooleanSetting("Players").value(true),
         new BooleanSetting("Animals").value(false),
         new BooleanSetting("Mobs").value(false),
         new BooleanSetting("Items").value(false)
      );
   public final ModeSetting modeSetting = new ModeSetting("Players mode").value("Client").values("Client", "Custom").visibleWhen(this::lambda$new$0);
   public final ModeSetting modeSetting2 = new ModeSetting("Animals mode").value("Client").values("Client", "Custom").visibleWhen(this::lambda$new$1);
   public final ModeSetting modeSetting3 = new ModeSetting("Mobs mode").value("Client").values("Client", "Custom").visibleWhen(this::lambda$new$2);
   public final ModeSetting modeSetting4 = new ModeSetting("Friends mode").value("Client").values("Client", "Custom").visibleWhen(this::lambda$new$3);
   public final ModeSetting mdagidu = new ModeSetting("Items mode").value("Client").values("Client", "Custom").visibleWhen(this::lambda$new$4);
   public final ColorSetting playerColor = new ColorSetting("Player color").computeColorSetting(new Color(-1)).computeVisible(this::lambda$new$5);
   public final ColorSetting animalsColor = new ColorSetting("Animals color").computeColorSetting(new Color(-1)).computeVisible(this::lambda$new$6);
   public final ColorSetting mobsColor = new ColorSetting("Mobs color").computeColorSetting(new Color(-1)).computeVisible(this::lambda$new$7);
   public final ColorSetting friendsColor = new ColorSetting("Friends color").computeColorSetting(new Color(94, 255, 69)).computeVisible(this::lambda$new$8);
   public final ColorSetting itemsColor = new ColorSetting("Items color").computeColorSetting(new Color(255, 72, 69)).computeVisible(this::lambda$new$9);
   public final ModeSetting arrow6 = new ModeSetting("Style")
      .value("Triangle")
      .values("Triangle", "Arrow 1", "Arrow 2", "Arrow 3", "Arrow 4", "Arrow 5", "Arrow 6");
   public final ModeSetting arrow6Setting = new ModeSetting("GPS style")
      .value("Arrow 5")
      .values("Triangle", "Arrow 1", "Arrow 2", "Arrow 3", "Arrow 4", "Arrow 5", "Arrow 6");
   public final FloatSetting gpsSize = new FloatSetting("GPS size").value(1.0F).range(0.2F, 1.5F).step(0.1F);
   public final FloatSetting size = new FloatSetting("Size").value(1.0F).range(0.2F, 1.5F).step(0.1F);
   public final FloatSetting radius = new FloatSetting("Radius").value(40.0F).range(20.0F, 100.0F).step(1.0F);
   public final ModeSetting miqou = new ModeSetting("Animation").value("In").values("Out", "In", "None");
   public final FloatSetting duration = new FloatSetting("Duration").value(4.0F).range(1.0F, 20.0F).step(1.0F);
   public final EntityFilterHelper.EntityFilter munwoce = new EntityFilterHelper.EntityFilter(this.items.getAllNames());
   public final Set<Entity> set = ConcurrentHashMap.newKeySet();
   public final List<Entity> mkoqal = new ArrayList<>();
   public final HashMap<Entity, AnimatedValue> hashMap = new HashMap<>();
   public final AnimatedValue animatedValue = new AnimatedValue();
   public final AnimatedValue animatedValue2 = new AnimatedValue();

   public Pointers() {
      this.addSettings(
         new Setting[]{
            this.items,
            this.modeSetting,
            this.modeSetting2,
            this.modeSetting3,
            this.modeSetting4,
            this.mdagidu,
            this.playerColor,
            this.animalsColor,
            this.mobsColor,
            this.friendsColor,
            this.itemsColor,
            this.arrow6,
            this.arrow6Setting,
            this.gpsSize,
            this.size,
            this.radius,
            this.miqou,
            this.duration
         }
      );
   }

   @Override
   public void onDisable() {
      this.set.clear();
      this.mkoqal.clear();
      this.hashMap.clear();
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$10));
      Subscription var2 = Render2DEvent.getInstance().subscribe(new EventListener<>(2, this::lambda$onEvent$12));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void handleRecoveredClass245(Render2DEvent.Render2DEventData event, Entity entity, AnimatedValue spawn) {
   }

   public void handleBlue(DrawContext context, float x, float y, float size, Color color) {
      float var6 = (float)color.getRed() / 255.0F;
      float var7 = (float)color.getGreen() / 255.0F;
      float var8 = (float)color.getBlue() / 255.0F;
      float var9 = (float)color.getAlpha() / 255.0F;
      this.handleString(context, x, y, size, var6, var7, var8, var9, this.arrow6.getValue());
   }

   public void handleBlue2(DrawContext context, float x, float y, float size, Color color) {
      float var6 = (float)color.getRed() / 255.0F;
      float var7 = (float)color.getGreen() / 255.0F;
      float var8 = (float)color.getBlue() / 255.0F;
      float var9 = (float)color.getAlpha() / 255.0F;
      this.handleString(context, x, y, size, var6, var7, var8, var9, this.arrow6Setting.getValue());
   }

   public float getGps() {
      return this.gpsSize.getValue();
   }

   public void handleString(DrawContext context, float x, float y, float size, float r, float g, float b, float a, String style) {
      String var10 = switch (style) {
         case "Arrow 1" -> "arrows/a1";
         case "Arrow 2" -> "arrows/a2";
         case "Arrow 3" -> "arrows/a3";
         case "Arrow 4" -> "arrows/a4";
         case "Arrow 5" -> "arrows/a5";
         case "Arrow 6" -> "arrows/a6";
         default -> "pointers/triangle";
      };
      RenderSystem.setShaderTexture(0, InputHelper.getImage(var10));
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder var11 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      float var16 = size + 8.0F;
      int var13 = (int)(a * 255.0F) << 24 | (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
      Matrix4f var14 = context.getMatrices().peek().getPositionMatrix();
      var11.vertex(var14, x - var16 / 2.0F, y + var16, 0.0F).texture(0.0F, 1.0F).color(var13);
      var11.vertex(var14, x + var16 / 2.0F, y + var16, 0.0F).texture(1.0F, 1.0F).color(var13);
      var11.vertex(var14, x + var16 / 2.0F, y, 0.0F).texture(1.0F, 0.0F).color(var13);
      var11.vertex(var14, x - var16 / 2.0F, y, 0.0F).texture(0.0F, 0.0F).color(var13);
      BuiltBuffer var15 = var11.end();
      if (var15 != null) {
         BufferRenderer.drawWithGlobalProgram(var15);
      }

      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   public float getMc() {
      return (float)mc.getWindow().getScaledWidth() / 2.0F;
   }

   public float getFloat() {
      return (float)mc.getWindow().getScaledHeight() / 2.0F;
   }

   public float getMax() {
      return mc.currentScreen instanceof HandledScreen var2
         ? (float)Math.max(var2.width, var2.height) * 0.05F + (this.radius.getMax() - this.radius.getValue())
         : 0.0F;
   }

   public float computeAtan(Entity entity, float tickDelta) {
      if (mc.player != null && mc.gameRenderer != null && mc.gameRenderer.getCamera() != null) {
         Vec3d var3 = mc.gameRenderer.getCamera().getPos();
         double var4 = MathHelper.lerp((double)tickDelta, entity.prevX, entity.getX());
         double var6 = MathHelper.lerp((double)tickDelta, entity.prevZ, entity.getZ());
         double var8 = var4 - var3.x;
         double var10 = var6 - var3.z;
         float var12 = (float)(Math.toDegrees(Math.atan2(var10, var8)) - 90.0);
         float var13 = mc.gameRenderer.getCamera().getYaw();
         return MathHelper.wrapDegrees(var12 - var13);
      } else {
         return 0.0F;
      }
   }

   public int computeRgb(Entity entity) {
      int var2 = entity.getId() * 13;
      Color var3 = ThemeColor.computeColor(var2);
      int var4 = var3.getRGB();
      if (entity instanceof ItemEntity) {
         return this.mdagidu.is("Custom") ? this.itemsColor.getValue().getRGB() : var4;
      } else if (entity instanceof PlayerEntity var5) {
         if (FriendsStore.getInstance().computeContains(var5.getName().getString())) {
            return this.modeSetting4.is("Custom") ? this.friendsColor.getValue().getRGB() : var4;
         } else {
            return this.modeSetting.is("Custom") ? this.playerColor.getValue().getRGB() : var4;
         }
      } else if (entity instanceof AnimalEntity) {
         return this.modeSetting2.is("Custom") ? this.animalsColor.getValue().getRGB() : var4;
      } else if (entity instanceof MobEntity) {
         return this.modeSetting3.is("Custom") ? this.mobsColor.getValue().getRGB() : var4;
      } else {
         return -1;
      }
   }

   public static Pointers getInstance() {
      return pointers;
   }

   public void lambda$onEvent$12(Render2DEvent.Render2DEventData event) {
      this.animatedValue.update();
      this.animatedValue2.update();
      this.animatedValue.animateTo(mc.player.getYaw(), 200L, Easing.EXPO_OUT);
      this.animatedValue2.animateTo(this.getMax(), 300L, Easing.EXPO_OUT);
      this.mkoqal.clear();
      this.mkoqal.addAll(this.set);

      for (Entity var5 : this.mkoqal) {
         this.hashMap.computeIfAbsent(var5, Pointers::lambda$onEvent$11);
      }

      String var32 = this.arrow6.getValue();

      String var31 = switch (var32) {
         case "Arrow 1" -> "arrows/a1";
         case "Arrow 2" -> "arrows/a2";
         case "Arrow 3" -> "arrows/a3";
         case "Arrow 4" -> "arrows/a4";
         case "Arrow 5" -> "arrows/a5";
         case "Arrow 6" -> "arrows/a6";
         default -> "pointers/triangle";
      };
      DrawContext var33 = event.getContext();
      MatrixStack var34 = var33.getMatrices();
      RenderSystem.setShaderTexture(0, InputHelper.getImage(var31));
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder var7 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      boolean var8 = false;
      float var9 = this.getMc();
      float var10 = this.getFloat();
      float var11 = event.getPartialTicks();
      Iterator var12 = this.hashMap.entrySet().iterator();

      while (var12.hasNext()) {
         Entry var13 = (Entry)var12.next();
         Entity var14 = (Entity)var13.getKey();
         AnimatedValue var15 = (AnimatedValue)var13.getValue();
         boolean var16 = this.set.contains(var14);
         var15.update();
         var15.animateTo(var16 ? 1.0 : 0.0, this.duration.getValue().longValue() * 50L, Easing.SINE_OUT);
         float var17 = (float)var15.getValue();
         if (var17 > 0.0F) {
            float var18 = 1.0F;
            if (this.miqou.is("In")) {
               var18 = 2.0F - var17;
            } else if (this.miqou.is("Out")) {
               var18 = 0.3F + 0.7F * var17;
            }

            float var19 = this.radius.getValue() * var18;
            float var20 = this.computeAtan(var14, var11);
            int var21 = this.computeRgb(var14);
            int var22 = var21 >> 16 & 0xFF;
            int var23 = var21 >> 8 & 0xFF;
            int var24 = var21 & 0xFF;
            int var25 = (int)((float)(var21 >> 24 & 0xFF) * var17);
            int var26 = var25 << 24 | var22 << 16 | var23 << 8 | var24;
            var34.push();
            var34.translate(var9, var10, 0.0F);
            var34.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT ? -var20 : var20));
            var34.translate(-var9, -var10, 0.0F);
            Matrix4f var27 = var34.peek().getPositionMatrix();
            float var28 = this.size.getValue() * 20.0F;
            float var29 = var28 + 8.0F;
            float var30 = (float)((double)(var10 - var19) - this.animatedValue2.getValue());
            var7.vertex(var27, var9 - var29 / 2.0F, var30 + var29, 0.0F).texture(0.0F, 1.0F).color(var26);
            var7.vertex(var27, var9 + var29 / 2.0F, var30 + var29, 0.0F).texture(1.0F, 1.0F).color(var26);
            var7.vertex(var27, var9 + var29 / 2.0F, var30, 0.0F).texture(1.0F, 0.0F).color(var26);
            var7.vertex(var27, var9 - var29 / 2.0F, var30, 0.0F).texture(0.0F, 0.0F).color(var26);
            var8 = true;
            var34.pop();
         }

         if (!var16 && var15.getValue() <= 0.0) {
            var12.remove();
         }
      }

      BuiltBuffer var2;
      if (var8 && (var2 = var7.end()) != null) {
         BufferRenderer.drawWithGlobalProgram(var2);
      }

      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   public static AnimatedValue lambda$onEvent$11(Entity k) {
      return new AnimatedValue();
   }

   public void lambda$onEvent$10(TickEvent event) {
      if (mc.world != null && mc.player != null) {
         this.set.clear();
         this.munwoce.targetSettings = this.items.getAllNames();
         this.munwoce.mukey = true;
         boolean var2 = this.items.isEnabled("Items");
         double var3 = mc.player.getX();
         double var5 = mc.player.getY();
         double var7 = mc.player.getZ();
         double var9 = 4096.0;

         for (Entity var12 : mc.world
            .getOtherEntities(mc.player, new Box(var3 - 64.0, var5 - 64.0, var7 - 64.0, var3 + 64.0, var5 + 64.0, var7 + 64.0))) {
            double var18 = var12.getX() - var3;
            LivingEntity var13;
            double var14;
            double var16;
            if (!(var18 * var18 + (var16 = var12.getY() - var5) * var16 + (var14 = var12.getZ() - var7) * var14 > var9)
               && (var12 instanceof ItemEntity && var2 || var12 instanceof LivingEntity && this.munwoce.computeFlag(var13 = (LivingEntity)var12))) {
               this.set.add(var12);
            }
         }
      }
   }

   public Boolean lambda$new$9() {
      return this.mdagidu.is("Custom") && this.items.isEnabled("Items");
   }

   public Boolean lambda$new$8() {
      return this.modeSetting4.is("Custom") && this.items.isEnabled("Players");
   }

   public Boolean lambda$new$7() {
      return this.modeSetting3.is("Custom") && this.items.isEnabled("Mobs");
   }

   public Boolean lambda$new$6() {
      return this.modeSetting2.is("Custom") && this.items.isEnabled("Animals");
   }

   public Boolean lambda$new$5() {
      return this.modeSetting.is("Custom") && this.items.isEnabled("Players");
   }

   public Boolean lambda$new$4() {
      return this.items.isEnabled("Items");
   }

   public Boolean lambda$new$3() {
      return this.items.isEnabled("Players");
   }

   public Boolean lambda$new$2() {
      return this.items.isEnabled("Mobs");
   }

   public Boolean lambda$new$1() {
      return this.items.isEnabled("Animals");
   }

   public Boolean lambda$new$0() {
      return this.items.isEnabled("Players");
   }
}
