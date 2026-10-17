package dev.daamky.client.gui;

import dev.daamky.client.font.FontBootstrap;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.Geometry;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.model.enums.Easing;
import dev.daamky.client.render.shader.ShaderSource;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Vector4f;

public class CustomTitleScreen extends Screen {
   public static float floatVal = 0.55F;
   public static float float2 = 0.35F;
   public static float float3 = 0.95F;
   public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
   public static boolean mzojoky = false;
   public final List<CustomTitleScreen.MenuButton> list = new ArrayList<>();
   public final AnimatedValue animatedValue = new AnimatedValue();
   public boolean flag = false;
   public float float4 = 0.0F;
   public final AnimatedValue animatedValue2 = new AnimatedValue();
   public final AnimatedValue mxayh = new AnimatedValue();
   public float float5;
   public float mejesv;
   public final float mmuwa = 118.0F;
   public final float float6 = 20.0F;
   public boolean flag2 = false;
   public final AnimatedValue animatedValue3 = new AnimatedValue();
   public String string = "Goodbye, ";
   public float float7;
   public float float8;
   public float float9;
   public float float10;
   public boolean murogi = false;
   public float float11 = 0.0F;
   public final AnimatedValue mhadcu = new AnimatedValue();
   public final AnimatedValue animatedValue4 = new AnimatedValue();
   public long timeStamp = 0L;
   public static final long long2 = System.currentTimeMillis();
   public static final long mpeazk = 4600L;
   public long long3 = 0L;
   public float float12 = 0.0F;
   public boolean maiab = false;
   public final AnimatedValue animatedValue5 = new AnimatedValue();
   public final AnimatedValue animatedValue6 = new AnimatedValue();

   public static void handleVal(MatrixStack ms, int width, int height) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null && var3.mouse != null && var3.getWindow() != null && var3.getWindow().getScaleFactor() > 0.0) {
         double var4 = var3.getWindow().getScaleFactor();
         float var6 = (float)(var3.mouse.getX() / var4);
         float var7 = (float)(var3.mouse.getY() / var4);
         ShaderSource.handleGl(1.0F, var6, var7);
      } else {
         ShaderSource.handleFloat(1.0F);
      }
   }

   public CustomTitleScreen() {
      super(Text.of(""));
   }

   public String initPattern() {
      LocalDate var1 = LocalDate.now();
      boolean var2 = EngHelper.getInstance().check();
      DateTimeFormatter var3 = var2
         ? DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("ru"))
         : DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH);
      String var4 = var1.format(var3);
      return !var4.isEmpty() ? Character.toUpperCase(var4.charAt(0)) + var4.substring(1) : var4;
   }

   public String getGood() {
      int var1 = LocalTime.now().getHour();
      boolean var2 = EngHelper.getInstance().check();
      if (var1 >= 5 && var1 < 12) {
         return var2 ? "Доброе утро, " : "Good morning, ";
      } else if (var1 >= 12 && var1 < 18) {
         return var2 ? "Добрый день, " : "Good afternoon, ";
      } else if (var1 >= 18 && var1 < 23) {
         return var2 ? "Добрый вечер, " : "Good evening, ";
      } else {
         return var2 ? "Доброй ночи, " : "Good night, ";
      }
   }

   public void init() {
      super.init();
      this.list.clear();
      if (mzojoky) {
         this.animatedValue.handleStart(1.0);
      } else {
         this.animatedValue.handleStart(0.0);
      }

      this.animatedValue2.handleStart(0.0);
      this.mxayh.handleStart(0.0);
      this.float4 = 0.0F;
      this.flag = false;
      if (!this.flag2) {
         this.animatedValue3.handleStart(0.0);
         this.mhadcu.handleStart(0.0);
         this.animatedValue4.handleStart(0.0);
         this.float11 = 0.0F;
         this.longVal = 0L;
         this.long3 = 0L;
         this.maiab = false;
      }

      float var1 = 132.0F;
      float var2 = 20.5F;
      float var3 = 4.5F;
      float var4 = (var2 + var3) * 4.0F + 18.5F + 2.0F;
      float var5 = (float)Math.round((float)this.width / 2.0F - var1 / 2.0F);
      float var6 = (float)Math.round((float)this.height / 2.0F - var4 / 2.0F + 20.0F);
      this.list.add(new CustomTitleScreen.MenuButton("l", "Singleplayer", var5, var6, var1, var2, this::lambda$init$0));
      float var7 = var6 + var2 + var3;
      this.list.add(new CustomTitleScreen.MenuButton("U", "Multiplayer", var5, var7, var1, var2, this::lambda$init$1));
      var7 += var2 + var3;
      this.list.add(new CustomTitleScreen.MenuButton("k", "Account Manager", var5, var7, var1, var2, this::lambda$init$2));
      var7 += var2 + var3;
      this.list.add(new CustomTitleScreen.MenuButton("T", "Settings", var5, var7, var1, var2, this::lambda$init$3));
      var7 += var2 + var3;
      this.float9 = var1;
      this.float10 = 18.5F;
      this.float7 = var5;
      this.float8 = var7 + 2.0F;
      String[] var8 = new String[]{"See you later,", "Goodbye,", "Have a nice day,", "Closing down,"};
      this.string = var8[(int)(Math.random() * (double)var8.length)];
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      MatrixStack var5 = context.getMatrices();
      ShaderSource.handleGl(1.0F, (float)mouseX, (float)mouseY);
      UHalfTexelSizeHelper.handleClass_4587(var5);
      MinecraftClient var6 = MinecraftClient.getInstance();
      String var7 = var6 != null && var6.getSession() != null && var6.getSession().getUsername() != null ? var6.getSession().getUsername() : "Player";
      this.animatedValue.update();
      if (mzojoky) {
         this.animatedValue.animateTo(1.0, 520L, Easing.QUINT_OUT, true);
      }

      float var8 = (float)this.animatedValue.getValue();
      if (var8 < 0.999F) {
         this.initNon(var5, mouseX, mouseY, var7, 1.0F - var8);
      }

      if (var8 > 0.001F) {
         this.initMin(var5, mouseX, mouseY, var7, var8);
      }

      if (this.flag2) {
         MinecraftClient var9 = MinecraftClient.getInstance();
         if (var9 != null) {
            var9.scheduleStop();
         } else {
            System.exit(0);
         }
      }
   }

   public void initNon(MatrixStack ms, int mouseX, int mouseY, String username, float alphaProg) {
      float var6 = Math.max(0.0F, Math.min(1.0F, alphaProg));
      int var7 = (int)(var6 * 255.0F);
      if (var7 > 0) {
         ms.push();
         float var8 = (float)this.width / 2.0F;
         float var9 = (float)this.height / 2.0F;
         float var10 = 1.0F - var6;
         float var11 = -var10 * 42.0F;
         float var12 = 0.93F + 0.07F * var6;
         ms.translate(var8, var9 + var11, 0.0F);
         ms.scale(var12, var12, 1.0F);
         ms.translate(-var8, -var9, 0.0F);
         String var13 = this.getGood();
         float var14 = 12.5F;
         float var15 = FontBootstrap.clientFont2.computeFloat(var13, var14);
         float var16 = FontBootstrap.clientFont2.computeFloat(username, var14);
         float var17 = var15 + var16;
         float var18 = var8 - var17 / 2.0F;
         float var19 = var9 - 22.0F;
         FontBootstrap.clientFont2.handleColor(ms, var13, var18, var19, var14, new Color(255, 255, 255, var7));
         Color var20 = ThemeColor.getColor4();
         Color var21 = new Color(var20.getRed(), var20.getGreen(), var20.getBlue(), var7);
         FontBootstrap.clientFont2.handleColor(ms, username, var18 + var15, var19, var14, var21);
         this.float5 = var8 - 118.0F / 2.0F;
         this.mejesv = var9 + 12.0F;
         float var22 = 2.0F;
         float var23 = 20.0F - var22 * 2.0F;
         float var24 = 118.0F - var23 - var22 * 2.0F;
         if (this.flag) {
            float var25 = (float)mouseX - (this.float5 + var22) - var23 / 2.0F;
            this.float4 = Math.max(0.0F, Math.min(1.0F, var25 / var24));
         } else if (!mzojoky) {
            this.animatedValue2.update();
            this.animatedValue2.animateTo(0.0, 240L, Easing.CUBIC_OUT, true);
            this.float4 = (float)this.animatedValue2.getValue();
         }

         float var45 = 20.0F / 2.0F;
         Color var26 = new Color(255, 255, 255, (int)(18.0F * var6));
         Color var27 = new Color(11, 12, 17, (int)(215.0F * var6));
         float var28 = 0.38F;
         float var29 = 2.0F;
         float[] var30 = new float[]{0.26F, 0.14F, 0.07F, 0.03F};
         float[] var31 = new float[]{0.5F, 1.4F, 2.8F, 4.8F};

         for (int var32 = 0; var32 < 4; var32++) {
            float var33 = var31[var32];
            int var34 = (int)(var30[var32] * var28 * var6 * 255.0F);
            if (var34 > 0) {
               int var35 = var34 << 24;
               Render.rectRenderer
                  .handleRgb(
                     ms, this.float5 - var33, this.mejesv + var29 + var33 * 0.35F, 118.0F + var33 * 2.0F, 20.0F + var33, var45 + var33, new Color(var35, true)
                  );
            }
         }

         Render.shaderProgram.handleVal(ms, this.float5, this.mejesv, 118.0F, 20.0F, var45, var27.getRGB());
         Render.rectRenderer.handleRgb(ms, this.float5 - 0.5F, this.mejesv - 0.5F, 118.0F + 1.0F, 20.0F + 1.0F, var45 + 0.5F, var26);
         Render.rectRenderer.handleRgb(ms, this.float5, this.mejesv, 118.0F, 20.0F, var45, var27);
         float var46 = this.float5 + var22 + var24 * this.float4;
         float var47 = this.mejesv + var22;
         if (this.float4 > 0.005F) {
            float var48 = var46 - this.float5 + var23 / 2.0F;
            float var50 = var48 + this.float4 * (118.0F - var48);
            float var36 = this.float4 * var45;
            Vector4f var37 = new Vector4f(var36, var36, var45, var45);
            int var38 = Math.max(0, Math.min(255, (int)(this.float4 * 140.0F * var6)));
            Color var39 = new Color(var20.getRed(), var20.getGreen(), var20.getBlue(), var38);
            Render.rectRenderer.handleRgb(ms, this.float5, this.mejesv, var50, 20.0F, var37, var39);
         }

         boolean var49 = Geometry.computeFlag((float)mouseX, (float)mouseY, var46, var47, var23, var23);
         this.mxayh.update();
         this.mxayh.animateTo(!var49 && !this.flag ? 0.0 : 1.0, 160L, Easing.QUINT_OUT, true);
         float var51 = (float)this.mxayh.getValue();
         float var52 = var23 / 2.0F;
         Color var53 = new Color(var20.getRed(), var20.getGreen(), var20.getBlue(), var7);
         if (var51 > 0.01F) {
            Color var54 = new Color(var20.getRed(), var20.getGreen(), var20.getBlue(), (int)((30.0F + 40.0F * var51) * var6));
            Render.rectRenderer.handleRgb(ms, var46 - 1.0F, var47 - 1.0F, var23 + 2.0F, var23 + 2.0F, var52 + 1.0F, var54);
         }

         Render.rectRenderer.handleRgb(ms, var46, var47, var23, var23, var52, var53);
         String var55 = "D";
         float var56 = 4.8F;
         float var40 = FontBootstrap.clientFont15.computeFloat(var55, var56);
         float var41 = var46 + (var23 - var40) / 2.0F + 0.6F;
         float var42 = var47 + (var23 - var56) / 2.0F - 0.5F;
         boolean var43 = (double)var20.getRed() * 0.299 + (double)var20.getGreen() * 0.587 + (double)var20.getBlue() * 0.114 > 180.0
            || ThemeManager.getInstance().getIsDayMode()
            || MainMenuScreen.isDayMode;
         Color var44 = var43 ? new Color(14, 16, 22, var7) : new Color(255, 255, 255, var7);
         FontBootstrap.clientFont15.handleColor(ms, var55, var41, var42, var56, var44);
         ms.pop();
      }
   }

   public void initMin(MatrixStack ms, int mouseX, int mouseY, String username, float introProg) {
      float var6 = Math.max(0.0F, Math.min(1.0F, introProg));
      if (!(var6 <= 0.001F)) {
         float var7 = 132.0F;
         float var8 = 20.5F;
         float var9 = 4.5F;
         float var10 = (var8 + var9) * 4.0F + 18.5F + 2.0F;
         float var11 = (float)Math.round((float)this.width / 2.0F - var7 / 2.0F);
         float var12 = (float)Math.round((float)this.height / 2.0F - var10 / 2.0F + 20.0F);
         float var13 = Math.min(1.0F, introProg * 1.35F);
         float var14 = Easing.QUINT_OUT.ease(var13);
         float var15 = (1.0F - var14) * -24.0F;
         float var16 = 0.93F + 0.07F * var14;
         float var17 = Math.min(1.0F, var13 * 1.25F);
         int var18 = (int)(var17 * 255.0F);
         String var19 = this.initPattern();
         String var20 = LocalTime.now().format(dateTimeFormatter);
         float var21 = 25.0F;
         float var22 = 5.8F;
         float var23 = var12 - var21 - 8.0F;
         float var24 = var23 - var22 - 3.5F;
         float var25 = (float)this.width / 2.0F;
         float var26 = (var24 + var23 + var21) / 2.0F;
         ms.push();
         ms.translate(var25, var26 + var15, 0.0F);
         ms.scale(var16, var16, 1.0F);
         ms.translate(-var25, -var26, 0.0F);
         FontBootstrap.mkadw.handleColor2(ms, var19, var25, var24, var22, new Color(180, 192, 215, (int)(210.0F * var17)));
         FontBootstrap.clientFont6.handleColor2(ms, var20, var25, var23, var21, new Color(255, 255, 255, var18));
         ms.pop();
         float var27 = var12;

         for (int var28 = 0; var28 < this.list.size(); var28++) {
            CustomTitleScreen.MenuButton var29 = this.list.get(var28);
            var29.x = var11;
            var29.y = var27;
            var29.w = var7;
            var29.h = var8;
            float var30 = (float)var28 * 0.065F;
            float var31 = Math.max(0.0F, Math.min(1.0F, (introProg - var30) / (1.0F - var30)));
            float var32 = Easing.QUINT_OUT.ease(var31);
            float var33 = (1.0F - var32) * (26.0F + (float)var28 * 4.0F);
            float var34 = Math.max(0.0F, Math.min(1.0F, var31 * 1.3F));
            var29.floatVal = var33;
            var29.initInit(ms, mouseX, mouseY, var34 * 255.0F);
            var27 += var8 + var9;
         }

         float var35 = (float)this.list.size() * 0.065F;
         float var36 = Math.max(0.0F, Math.min(1.0F, (introProg - var35) / (1.0F - var35)));
         float var37 = Easing.QUINT_OUT.ease(var36);
         float var38 = (1.0F - var37) * 42.0F;
         float var39 = Math.max(0.0F, Math.min(1.0F, var36 * 1.3F));
         this.float9 = var7;
         this.float10 = 18.5F;
         this.float7 = var11;
         this.float8 = var27 + 2.0F + var38;
         this.initInit(ms, mouseX, mouseY, var39 * 255.0F);
      }
   }

   public void initInit(MatrixStack ms, int mouseX, int mouseY, float alpha) {
      float var5 = 5.5F;
      float var6 = alpha / 255.0F;
      float var7 = 1.75F;
      float var8 = this.float10 - var7 * 2.0F;
      float var10 = this.float9 - var8 - var7 * 2.0F;
      if (this.flag2) {
         this.float11 = 1.0F;
         this.murogi = false;
      } else if (this.murogi) {
         float var11 = (float)mouseX - (this.float7 + var7) - var8 / 2.0F;
         this.float11 = Math.max(0.0F, Math.min(1.0F, var11 / var10));
         if (this.float11 >= 1.0F) {
            this.murogi = false;
            MinecraftClient var12 = MinecraftClient.getInstance();
            if (var12 != null) {
               var12.scheduleStop();
            } else {
               System.exit(0);
            }

            return;
         }
      } else {
         this.mhadcu.update();
         this.mhadcu.animateTo(0.0, 220L, Easing.CUBIC_OUT, true);
         this.float11 = (float)this.mhadcu.getValue();
      }

      float var38 = this.flag2 ? 1.0F : this.float11;
      float var39 = 0.38F;
      float var13 = 2.0F;
      float[] var14 = new float[]{0.26F, 0.14F, 0.07F, 0.03F};
      float[] var15 = new float[]{0.5F, 1.4F, 2.8F, 4.8F};

      for (int var16 = 0; var16 < 4; var16++) {
         float var17 = var15[var16];
         int var18 = (int)(var14[var16] * var39 * var6 * 255.0F);
         if (var18 > 0) {
            int var19 = var18 << 24;
            Render.rectRenderer
               .handleRgb(
                  ms,
                  this.float7 - var17,
                  this.float8 + var13 + var17 * 0.35F,
                  this.float9 + var17 * 2.0F,
                  this.float10 + var17,
                  var5 + var17,
                  new Color(var19, true)
               );
         }
      }

      Color var40 = new Color(255, 255, 255, (int)(38.0F * var6));
      Color var41 = new Color(13, 15, 22, (int)(225.0F * var6));
      Render.shaderProgram.handleVal(ms, this.float7, this.float8, this.float9, this.float10, var5, var41.getRGB());
      Render.rectRenderer.handleRgb(ms, this.float7 - 0.5F, this.float8 - 0.5F, this.float9 + 1.0F, this.float10 + 1.0F, var5 + 0.5F, var40);
      Render.rectRenderer.handleRgb(ms, this.float7, this.float8, this.float9, this.float10, var5, var41);
      Color var42 = ThemeColor.getColor4();
      float var43 = this.float7 + var7 + var10 * var38;
      float var20 = this.float8 + var7;
      if (var38 > 0.005F) {
         float var21 = var43 - this.float7 + var8 / 2.0F;
         float var22 = var21 + var38 * (this.float9 - var21);
         float var23 = var38 * var5;
         Vector4f var24 = new Vector4f(var23, var23, var5, var5);
         int var25 = Math.max(0, Math.min(255, (int)(var38 * 140.0F * var6)));
         Color var26 = new Color(var42.getRed(), var42.getGreen(), var42.getBlue(), var25);
         Render.rectRenderer.handleRgb(ms, this.float7, this.float8, var22, this.float10, var24, var26);
      }

      boolean var44 = EngHelper.getInstance().check();
      String var45 = var44 ? "Потяните для выхода" : "Slide to exit";
      float var46 = 5.0F;
      float var47 = FontBootstrap.mkadw.computeFloat(var45, var46);
      float var48 = Math.max(0.0F, 1.0F - var38 * 1.6F);
      int var49 = (int)(var48 * 185.0F * var6);
      if (var49 > 0) {
         float var27 = this.float7 + (this.float9 - var47) / 2.0F + 6.0F;
         float var28 = this.float8 + (this.float10 - var46) / 2.0F - 0.5F;
         FontBootstrap.mkadw.handleColor(ms, var45, var27, var28, var46, new Color(215, 222, 238, var49));
      }

      boolean var50 = !this.flag2 && Geometry.computeFlag((float)mouseX, (float)mouseY, var43, var20, var8, var8);
      this.animatedValue4.update();
      this.animatedValue4.animateTo(!var50 && !this.murogi ? 0.0 : 1.0, 160L, Easing.QUINT_OUT, true);
      float var51 = (float)this.animatedValue4.getValue();
      float var29 = 4.0F;
      Color var30 = new Color(var42.getRed(), var42.getGreen(), var42.getBlue(), Math.max(0, Math.min(255, (int)(255.0F * var6))));
      Color var31 = new Color(255, 255, 255, Math.max(0, Math.min(255, (int)((24.0F + 26.0F * var51) * var6))));
      if (var51 > 0.01F || var38 > 0.01F) {
         Color var32 = new Color(var42.getRed(), var42.getGreen(), var42.getBlue(), (int)((25.0F * var38 + 35.0F * var51) * var6));
         Render.rectRenderer.handleRgb(ms, var43 - 1.0F, var20 - 1.0F, var8 + 2.0F, var8 + 2.0F, var29 + 1.0F, var32);
      }

      Render.rectRenderer.handleRgb(ms, var43 - 0.4F, var20 - 0.4F, var8 + 0.8F, var8 + 0.8F, var29 + 0.4F, var31);
      Render.rectRenderer.handleRgb(ms, var43, var20, var8, var8, var29, var30);
      String var52 = "D";
      float var33 = 4.8F;
      float var34 = FontBootstrap.clientFont15.computeFloat(var52, var33);
      float var35 = var43 + (var8 - var34) / 2.0F + 0.6F;
      float var36 = var20 + (var8 - var33) / 2.0F - 0.5F;
      Color var37 = new Color(0, 0, 0, Math.max(0, Math.min(255, (int)(245.0F * var6))));
      FontBootstrap.clientFont15.handleColor(ms, var52, var35, var36, var33, var37);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!mzojoky) {
         if (button == 0 && Geometry.computeFlag((float)mouseX, (float)mouseY, this.float5, this.mejesv, 118.0F, 20.0F)) {
            this.flag = true;
            return true;
         } else {
            return super.mouseClicked(mouseX, mouseY, button);
         }
      } else if (button == 0 && Geometry.computeFlag((float)mouseX, (float)mouseY, this.float7, this.float8, this.float9, this.float10)) {
         this.murogi = true;
         return true;
      } else {
         for (CustomTitleScreen.MenuButton var7 : this.list) {
            if (var7.computeAction(mouseX, mouseY, button)) {
               return true;
            }
         }

         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (button == 0) {
         if (this.flag) {
            this.flag = false;
            if (!mzojoky) {
               if (this.float4 >= 0.85F) {
                  this.float4 = 1.0F;
                  mzojoky = true;
                  this.animatedValue.handleStart(0.0);
                  this.animatedValue.animateTo(1.0, 520L, Easing.QUINT_OUT, true);
               } else {
                  this.animatedValue2.handleStart((double)this.float4);
               }
            }
         }

         if (this.murogi) {
            this.murogi = false;
            if (this.float11 >= 0.85F) {
               this.float11 = 1.0F;
               MinecraftClient var6 = MinecraftClient.getInstance();
               if (var6 != null) {
                  var6.scheduleStop();
               } else {
                  System.exit(0);
               }

               return true;
            }

            this.mhadcu.handleStart((double)this.float11);
         }
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public void lambda$init$3() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null) {
         var1.setScreen(new OptionsScreen(this, var1.options));
      }
   }

   public void lambda$init$2() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null) {
         var1.setScreen(new AccountManagerScreen(this));
      }
   }

   public void lambda$init$1() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null) {
         var1.setScreen(new MultiplayerScreen(this));
      }
   }

   public void lambda$init$0() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null) {
         var1.setScreen(new SelectWorldScreen(this));
      }
   }

   public static class MenuButton {
      public String icon;
      public String title;
      public float x;
      public float y;
      public float w;
      public float h;
      public float floatVal = 0.0F;
      public Runnable action;
      public final AnimatedValue animatedValue = new AnimatedValue();

      public MenuButton(String icon, String title, float x, float y, float w, float h, Runnable action) {
         this.icon = icon;
         this.title = title;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         this.action = action;
      }

      public boolean computeAction(double mouseX, double mouseY, int button) {
         float var6 = this.y + this.floatVal;
         if (button == 0
            && Geometry.computeFlag(mouseX, mouseY, (double)(this.x - 2.0F), (double)(var6 - 2.0F), (double)(this.w + 4.0F), (double)(this.h + 4.0F))) {
            if (this.action != null) {
               this.action.run();
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
