package dev.daamky.client.gui;

import dev.daamky.client.config.ConfigManager;
import dev.daamky.client.font.FontRegistry;
import dev.daamky.client.font.IconFontRegistry;
import dev.daamky.client.gui.component.CategoryComponent;
import dev.daamky.client.gui.component.CheckComponent;
import dev.daamky.client.gui.component.ColorPickerComponent;
import dev.daamky.client.gui.component.ComboComponent;
import dev.daamky.client.gui.component.NumberComponent;
import dev.daamky.client.gui.component.SliderComponent;
import dev.daamky.client.gui.component.SwitchComponent;
import dev.daamky.client.gui.component.TextFieldComponent;
import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.ClientPaths;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.EasingTypeHelper;
import dev.daamky.client.internal.EngHelper;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.internal.GlyphMap;
import dev.daamky.client.internal.LegacyModule2;
import dev.daamky.client.internal.MacroEngine;
import dev.daamky.client.internal.MacroStore;
import dev.daamky.client.internal.MainMenuScreenHelper;
import dev.daamky.client.internal.MaxMinHelper;
import dev.daamky.client.internal.MenuAnimation;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.RenderAccess;
import dev.daamky.client.internal.RenderKit;
import dev.daamky.client.internal.RenderQueue;
import dev.daamky.client.internal.TintColorHelper;
import dev.daamky.client.internal.TotalHelper;
import dev.daamky.client.internal.UHalfTexelSizeHelper;
import dev.daamky.client.internal.ValueHelper;
import dev.daamky.client.internal.ValueToHelper;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.legacy.module.ModuleCategory;
import dev.daamky.client.model.enums.AnimationDirection;
import dev.daamky.client.model.enums.KeyCode;
import dev.daamky.client.model.enums.ThemeVariant;
import dev.daamky.client.module.render.ClickGUI;
import dev.daamky.client.notification.NotificationManager;
import dev.daamky.client.rotation.RotationProfileManager;
import dev.daamky.client.util.KeyNames;
import java.awt.Color;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.json.JSONObject;

public class MainMenuScreen extends Screen implements RenderAccess {
   public static float S = 1.0F;
   public static final MatrixStack SHARED_MS = TintColorHelper.class_4587;
   public boolean exit = false;
   public long openStartTime = 0L;
   public long closeStartTime = 0L;
   public static final float OPEN_DURATION = 380.0F;
   public static final float CLOSE_DURATION = 260.0F;
   public boolean toggleKeyReleased = false;
   public ValueToHelper glomalAnim = new ValueToHelper();
   public float scaleFix = 1.0F;
   public double lastMouseX;
   public double lastMouseY;
   public static ThemeVariant selectedTheme = ThemeVariant.NIGHT;
   public static ThemeVariant preSelectedTheme = ThemeVariant.NIGHT;
   public static ThemeVariant[] themes = ThemeVariant.values();
   public static Direction2 animation14 = new Direction2(300, 1.0);
   public static boolean isDayMode = false;
   public static int selectedAccentIndex = 0;
   public static final Identifier SEND_ICON = Identifier.of("daamky", "textures/icons/send.png");
   public static final Identifier SEARCH_ICON = Identifier.of("daamky", "textures/icons/search.png");
   public static final Identifier PICKAXE_ICON = Identifier.of("daamky", "textures/icons/pickaxe.png");
   public static final Identifier ICON_TRASH = Identifier.of("daamky", "textures/icons/trash.png");
   public static final Identifier ICON_SAVE = Identifier.of("daamky", "textures/icons/save.png");
   public static final Identifier ICON_LOAD = Identifier.of("daamky", "textures/icons/download.png");
   public static final Identifier ICON_USER = Identifier.of("daamky", "textures/icons/user.png");
   public static final Identifier ICON_USERS = Identifier.of("daamky", "textures/icons/users.png");
   public static final Identifier SUN_ICON = Identifier.of("daamky", "textures/icons/sun.png");
   public static final Identifier MOON_ICON = Identifier.of("daamky", "textures/icons/moon.png");
   public static final int[][] ACCENT_PRESETS = new int[][]{
      {-53909, -2097094}, {-13315175, -16411031}, {-1, -15788246}, {-1541639, -4184365}, {-16730881, -16748586}
   };
   public static final int[] ACCENT_DOT_COLORS = new int[]{-53909, -13315175, -920071, -5796870, -16730881};
   public final Animation[] accentAnims = new Animation[]{
      new Direction2(250, 1.0), new Direction2(250, 1.0), new Direction2(250, 1.0), new Direction2(250, 1.0), new Direction2(250, 1.0)
   };
   public static MainMenuScreen.ViewMode currentView = MainMenuScreen.ViewMode.MODULES;
   public final Direction2 tabSwitchAnim = new Direction2(220, 1.0, AnimationDirection.FORWARDS);
   public final Animation animSearchDropdown = new Direction2(220, 1.0, AnimationDirection.BACKWARDS);
   public final Direction animGuiSettings = new Direction(260, 1.0, AnimationDirection.BACKWARDS);
   public final Direction animRotationsTab = new Direction(250, 1.0, AnimationDirection.BACKWARDS);
   public final Direction animCreateConfigModal = new Direction(250, 1.0, AnimationDirection.BACKWARDS);
   public final Direction animCreateMacroModal = new Direction(250, 1.0, AnimationDirection.BACKWARDS);
   public boolean typingMacroModal = false;
   public int macroModalFocusField = 0;
   public String newMacroNameBuffer = "";
   public String newMacroParamBuffer = "";
   public int newMacroActionType = 0;
   public int newMacroKey = -1;
   public boolean newMacroListeningKey = false;
   public MacroEngine.Macro listeningMacro = null;
   public float macroScrollAnim = 0.0F;
   public float macroScrollTarget = 0.0F;
   public float macroMaxScroll = 0.0F;
   public Module highlightedModule = null;
   public long highlightStartTime = 0L;
   public static int cApp;
   public static int cSide;
   public static int cCard;
   public static int cCardDisabled;
   public static int cBrd;
   public static int cBrdL;
   public static int cInp;
   public static int cEl1;
   public static int cEl2;
   public static int C_DIM = -8747884;
   public static int C_WHITE = -920071;
   public static final int C_DANGER = -1750963;
   public static final int C_SUCCESS = -14498466;
   public static int L_TXT = -920071;
   public static int L_SUB = -8747884;
   public static int L_EL1 = -15394782;
   public static int L_EL2 = -15724006;
   public static int L_BRD = -14802386;
   public ModuleCategory active = ModuleCategory.COMBAT;
   public float scrollTarget = 0.0F;
   public float scrollAnim = 0.0F;
   public float maxScroll = 0.0F;
   public SliderComponent draggingSlider = null;
   public NumberComponent activeBind = null;
   public Module bindingModule = null;
   public TextFieldComponent activeString = null;
   public String stringBuffer = "";
   public ColorPickerComponent draggingColor = null;
   public int draggingColorBar = 0;
   public static boolean searchActive = false;
   public static String searchQuery = "";
   public long searchTypeTime = System.currentTimeMillis();
   public final Animation animSearchFocus = new Direction2(260, 1.0, AnimationDirection.BACKWARDS);
   public final Animation animSearchText = new Direction2(260, 1.0, AnimationDirection.BACKWARDS);
   public final Animation animLang = new Direction2(200, 1.0);
   public final Animation animDayNight = new Direction2(260, 1.0, AnimationDirection.BACKWARDS);
   public boolean typingConfigName = false;
   public String newConfigBuffer = "";
   public float configScrollTarget = 0.0F;
   public float configScrollAnim = 0.0F;
   public float configMaxScroll = 0.0F;
   public static int draggingCurvePointIndex = -1;
   public static int draggingAimNodeIndex = -1;
   public static String draggingRotationSlider = null;
   public static int selectedEasingOffset = 0;
   public static boolean typingNodeLabel = false;
   public static String nodeLabelBuffer = "";
   public float rotNoiseScroll = 0.0F;
   public float rotNoiseScrollTarget = 0.0F;
   public float rotNoiseMaxScroll = 0.0F;
   public static boolean langRu = true;
   public static boolean showGuiSettings = false;
   public static float gsScale = 0.7F;
   public static float gsRadius = 1.0F;
   public static float gsPad = 1.0F;
   public static float gsBlur = 1.0F;
   public static float gsFont = 1.0F;
   public static float gsBg = 1.0F;
   public static boolean gs3DAnim = false;
   public static boolean is3DCapturing = false;
   public static float gsPosX = -1.0F;
   public static float gsPosY = -1.0F;
   public boolean gsDraggingHeader = false;
   public float gsDragOffX = 0.0F;
   public float gsDragOffY = 0.0F;
   public String dragGs = null;
   public float frozenU = 1.0F;
   public static final long SESSION_START_TIME = System.currentTimeMillis();
   public static final int SEARCH_LIMIT = 24;
   public final HashMap<String, Animation> chipAnims = new HashMap<>();
   public final HashMap<String, Float> descHeightCache = new HashMap<>();
   public final HashMap<String, float[]> smoothVals = new HashMap<>();
   public final IdentityHashMap<Component<?>, Animation> visAnims = new IdentityHashMap<>();
   public static final Set<String> ESP_SUB_MODULES = Set.of();
   public float U = 1.0F;
   public static final String[][] GS_SLIDERS = new String[][]{
      {"GUI Scale", "0.35", "1.3", "scale"}, {"Text Size", "0.8", "1.3", "font"}, {"Radius", "0", "2", "radius"}
   };
   public static boolean gsBlurOn = true;

   public MainMenuScreen() {
      super(Text.literal("MenuNight"));
   }

   public static float getGsRadius() {
      return gsRadius;
   }

   public String getSESSION_START_TIME() {
      long var1 = Math.max(0L, (System.currentTimeMillis() - SESSION_START_TIME) / 1000L);
      long var3 = var1 / 3600L;
      long var5 = var1 % 3600L / 60L;
      long var7 = var1 % 60L;
      boolean var9 = EngHelper.getInstance().check();
      String var10 = var9 ? "Сессия: " : "Session: ";
      if (var3 > 0L) {
         return var10 + var3 + (var9 ? "ч " : "h ") + var5 + (var9 ? "м" : "m");
      } else {
         return var5 > 0L ? var10 + var5 + (var9 ? "м " : "m ") + var7 + (var9 ? "с" : "s") : var10 + var7 + (var9 ? "с" : "s");
      }
   }

   public Animation chipAnim(String key) {
      return this.chipAnims.computeIfAbsent(key, MainMenuScreen::lambda$chipAnim$0);
   }

   public static int getAccent() {
      int var0 = MathHelper.clamp(selectedAccentIndex, 0, 4);
      int[] var1 = ACCENT_PRESETS[var0];
      return isDayMode ? var1[1] : var1[0];
   }

   public static int getAccent2() {
      int var0 = MathHelper.clamp(selectedAccentIndex, 0, 4);
      int[] var1 = ACCENT_PRESETS[var0];
      return isDayMode ? var1[0] : var1[1];
   }

   public static boolean computeFlag(int color) {
      int var1 = color >> 16 & 0xFF;
      int var2 = color >> 8 & 0xFF;
      int var3 = color & 0xFF;
      return 0.299 * (double)var1 + 0.587 * (double)var2 + 0.114 * (double)var3 > 175.0;
   }

   public float getOpenStartTime() {
      float var1 = MathHelper.clamp((float)(System.currentTimeMillis() - this.openStartTime) / 380.0F, 0.0F, 1.0F);
      float var2 = 1.75F;
      float var3 = var2 + 1.0F;
      float var4 = var1 - 1.0F;
      return 1.0F + var3 * var4 * var4 * var4 + var2 * var4 * var4;
   }

   public float getTime() {
      if (!this.exit) {
         float var2 = MathHelper.clamp((float)(System.currentTimeMillis() - this.openStartTime) / 380.0F, 0.0F, 1.0F);
         return MathHelper.clamp(var2 * 1.6F, 0.0F, 1.0F);
      } else {
         float var1 = MathHelper.clamp((float)(System.currentTimeMillis() - this.closeStartTime) / 260.0F, 0.0F, 1.0F);
         return MathHelper.clamp(1.0F - var1 * 1.2F, 0.0F, 1.0F);
      }
   }

   public float getTime2() {
      if (!this.exit) {
         float var4 = this.getOpenStartTime();
         return 0.65F + 0.35F * var4;
      } else {
         float var1 = MathHelper.clamp((float)(System.currentTimeMillis() - this.closeStartTime) / 260.0F, 0.0F, 1.0F);
         float var2 = (float)Math.sin((double)var1 * Math.PI) * (1.0F - var1) * 0.16F;
         float var3 = var1 * var1 * 0.45F;
         return 1.0F + var2 - var3;
      }
   }

   public float getTime3() {
      if (!this.exit) {
         float var2 = this.getOpenStartTime();
         return (1.0F - Math.min(1.0F, var2)) * 18.0F * this.U;
      } else {
         float var1 = MathHelper.clamp((float)(System.currentTimeMillis() - this.closeStartTime) / 260.0F, 0.0F, 1.0F);
         return (float)Math.pow((double)var1, 2.0) * 12.0F * this.U;
      }
   }

   public void handleMax(float sx, float sy, float sw, float sh) {
      int var5 = (int)((float)mc.getWindow().getScaledWidth() / this.scaleFix);
      int var6 = (int)((float)mc.getWindow().getScaledHeight() / this.scaleFix);
      float var7 = (float)var5 / 2.0F;
      float var8 = (float)var6 / 2.0F;
      float var9 = this.getTime2();
      float var10 = this.getTime3();
      float var11 = (sx - var7) * var9 + var7;
      float var12 = (sy - var8) * var9 + var8 + var10;
      float var13 = Math.max(0.0F, sw * var9);
      float var14 = Math.max(0.0F, sh * var9);
      RenderQueue.handleMax(var11, var12, var13, var14, 2.0F);
   }

   public void handleDegrees(MatrixStack ms, float cx, float cy, float size, int color) {
      float var6 = size / 2.0F;
      float var7 = Math.max(1.0F, 1.1F * this.U);
      ms.push();
      ms.translate(cx, cy, 0.0F);
      ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0F));
      RenderKit.Render2D.handleFloat(-var6, -var7 / 2.0F, size, var7, color, var7 / 2.0F);
      RenderKit.Render2D.handleFloat(-var7 / 2.0F, -var6, var7, size, color, var7 / 2.0F);
      ms.pop();
   }

   public void handleDegrees(MatrixStack ms, float cx, float cy, float size, boolean pointingDown, int color) {
      float var7 = size / 2.0F;
      float var8 = Math.max(1.0F, 1.1F * this.U);
      float var9 = size * 0.62F;
      float var10 = pointingDown ? -0.8F * this.U : 0.8F * this.U;
      ms.push();
      ms.translate(cx, cy + var10, 0.0F);
      ms.push();
      ms.translate(-var7 * 0.38F, 0.0F, 0.0F);
      ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pointingDown ? 40.0F : -40.0F));
      RenderKit.Render2D.handleFloat(-var8 / 2.0F, -var9 / 2.0F, var8, var9, color, var8 / 2.0F);
      ms.pop();
      ms.push();
      ms.translate(var7 * 0.38F, 0.0F, 0.0F);
      ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pointingDown ? -40.0F : 40.0F));
      RenderKit.Render2D.handleFloat(-var8 / 2.0F, -var9 / 2.0F, var8, var9, color, var8 / 2.0F);
      ms.pop();
      ms.pop();
   }

   public void handleDegrees2(MatrixStack ms, float cx, float cy, float size, int color) {
      if (isDayMode) {
         float var6 = size * 0.25F;
         float var7 = size * 0.38F;
         float var8 = size * 0.52F;
         float var9 = var8 - var7;
         float var10 = Math.max(1.1F * this.U, size * 0.12F);
         ms.push();
         ms.translate(cx, cy, 0.0F);
         Render.ringRenderer.handleVal(ms, 0.0F, 0.0F, var6, color);

         for (int var11 = 0; var11 < 8; var11++) {
            ms.push();
            ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var11 * 45.0F));
            Render.rectRenderer.handleVal(ms, -var10 / 2.0F, -var8, var10, var9, var10 / 2.0F, color);
            ms.pop();
         }

         ms.pop();
      } else {
         float var12 = size * 0.44F;
         float var13 = size * 0.38F;
         float var14 = size * 0.2F;
         float var15 = -size * 0.12F;
         Render.ringRenderer.handleVal(ms, cx, cy, var12, var13, var14, var15, color);
      }
   }

   public void handleIcon(MatrixStack ms, float cx, float cy, float size, int color) {
      float var6 = size * 0.85F;
      Render.myazem.handleClass_2960(ms, cx - var6 / 2.0F, cy - var6 / 2.0F, var6, var6, 0.0F, color, 0.0F, 0.0F, 1.0F, 1.0F, SEND_ICON);
   }

   public void handleCos(float s, float w, float h, float r, float[] out) {
      float var6 = w - 2.0F * r;
      float var7 = (float) (Math.PI / 2) * r;
      float var8 = h - 2.0F * r;
      float var9 = w - 2.0F * r;
      float var10 = h - 2.0F * r;
      if (s < var6) {
         out[0] = r + s;
         out[1] = 0.0F;
      } else if ((s = s - var6) < var7) {
         float var11 = (float) (-Math.PI / 2) + s / var7 * 0.5F * (float) Math.PI;
         out[0] = w - r + r * (float)Math.cos((double)var11);
         out[1] = r + r * (float)Math.sin((double)var11);
      } else if ((s = s - var7) < var8) {
         out[0] = w;
         out[1] = r + s;
      } else if ((s = s - var8) < var7) {
         float var19 = 0.0F + s / var7 * 0.5F * (float) Math.PI;
         out[0] = w - r + r * (float)Math.cos((double)var19);
         out[1] = h - r + r * (float)Math.sin((double)var19);
      } else if ((s = s - var7) < var9) {
         out[0] = w - r - s;
         out[1] = h;
      } else if ((s = s - var9) < var7) {
         float var20 = (float) (Math.PI / 2) + s / var7 * 0.5F * (float) Math.PI;
         out[0] = r + r * (float)Math.cos((double)var20);
         out[1] = h - r + r * (float)Math.sin((double)var20);
      } else if ((s = s - var7) < var10) {
         out[0] = 0.0F;
         out[1] = h - r - s;
      } else {
         s -= var10;
         float var21 = (float) Math.PI + s / var7 * 0.5F * (float) Math.PI;
         out[0] = r + r * (float)Math.cos((double)var21);
         out[1] = r + r * (float)Math.sin((double)var21);
      }
   }

   public void handleMin(float x, float y, float w, float h, float r, int color, float thick) {
      float var8 = 2.0F * (w - 2.0F * r + h - 2.0F * r) + (float) (Math.PI * 2) * r;
      float var9 = 14.0F * this.U;
      float var10 = 10.0F * this.U;
      float var11 = var9 + var10;
      float var12 = (float)(System.currentTimeMillis() % 2800L) / 2800.0F * var11;
      float[] var13 = new float[2];
      float[] var14 = new float[2];

      for (float var15 = 0.0F; var15 < var8; var15 += var11) {
         float var16 = (var15 + var12) % var8;
         float var17 = 1.6F * this.U;

         for (float var18 = 0.0F; var18 < var9; var18 += var17) {
            float var19 = (var16 + var18) % var8;
            float var20 = (var16 + Math.min(var18 + var17, var9)) % var8;
            this.handleCos(var19, w, h, r, var13);
            this.handleCos(var20, w, h, r, var14);
            float var21 = var14[0] - var13[0];
            float var22 = var14[1] - var13[1];
            float var23 = Math.min(var13[0], var14[0]) - thick / 2.0F;
            float var24 = Math.min(var13[1], var14[1]) - thick / 2.0F;
            float var25 = Math.max(thick, Math.abs(var21) + thick);
            float var26 = Math.max(thick, Math.abs(var22) + thick);
            RenderKit.Render2D.handleFloat(x + var23, y + var24, var25, var26, color, thick / 2.0F);
         }
      }
   }

   public static void initGs() {
      try {
         File var0 = new File(ClientPaths.string7);
         if (!var0.exists()) {
            var0.mkdirs();
         }

         File var1 = new File(var0, "menu_settings.json");
         JSONObject var2 = new JSONObject();
         var2.put("dayMode", isDayMode);
         var2.put("accentIndex", selectedAccentIndex);
         var2.put("gsScale", gsScale);
         var2.put("gsRadius", gsRadius);
         var2.put("gsPad", gsPad);
         var2.put("gsBlur", gsBlur);
         var2.put("gsBlurOn", gsBlurOn);
         var2.put("gsFont", gsFont);
         var2.put("gsBg", gsBg);
         var2.put("gs3DAnim", gs3DAnim);
         Files.writeString(var1.toPath(), var2.toString(2), StandardCharsets.UTF_8);
      } catch (Throwable var3) {
      }
   }

   public static void initGs2() {
      try {
         File var0 = new File(ClientPaths.string7, "menu_settings.json");
         if (var0.exists()) {
            String var1 = Files.readString(var0.toPath(), StandardCharsets.UTF_8);
            JSONObject var2 = new JSONObject(var1);
            if (var2.has("dayMode")) {
               isDayMode = var2.getBoolean("dayMode");
            }

            if (var2.has("accentIndex")) {
               selectedAccentIndex = var2.getInt("accentIndex");
            }

            if (var2.has("gsScale")) {
               gsScale = (float)var2.getDouble("gsScale");
            }

            if (var2.has("gsRadius")) {
               gsRadius = (float)var2.getDouble("gsRadius");
            }

            if (var2.has("gsPad")) {
               gsPad = (float)var2.getDouble("gsPad");
            }

            if (var2.has("gsBlur")) {
               gsBlur = (float)var2.getDouble("gsBlur");
            }

            if (var2.has("gsBlurOn")) {
               gsBlurOn = var2.getBoolean("gsBlurOn");
            }

            if (var2.has("gsFont")) {
               gsFont = (float)var2.getDouble("gsFont");
            }

            if (var2.has("gs3DAnim")) {
               gs3DAnim = var2.getBoolean("gs3DAnim");
            }

            gsBg = 1.0F;
         }
      } catch (Throwable var3) {
      }
   }

   public float computeDesc(FontRegistry font, String desc, float width) {
      if (desc != null && !desc.isEmpty()) {
         String var4 = desc + ":" + (int)width + ":" + (int)(this.computeFont(7.2F) * 10.0F);
         Float var5 = this.descHeightCache.get(var4);
         if (var5 != null) {
            return var5;
         } else {
            float var6 = font.computeChar(desc, width, this.computeFont(7.2F));
            this.descHeightCache.put(var4, var6);
            return var6;
         }
      } else {
         return 0.0F;
      }
   }

   public float smooth(String key, float target) {
      float[] var3 = this.smoothVals.computeIfAbsent(key, (p0) -> MainMenuScreen.lambda$smooth$1(target, p0));
      var3[0] += (target - var3[0]) * 0.3F;
      return var3[0];
   }

   public float visAnim(Module f, Component<?> setting) {
      Animation var3 = this.visAnims.computeIfAbsent(setting, MainMenuScreen::lambda$visAnim$2);
      var3.setDirection(setting.getVisible().get() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      return var3.getEnd();
   }

   public float computeNext(List<String> values, float width) {
      if (values != null && !values.isEmpty()) {
         float var3 = 0.0F;
         float var4 = 0.0F;

         for (String var6 : values) {
            String var7 = EngHelper.computeString(var6);
            float var8 = IconFontRegistry.fontRegistry3.computeDelegate(var7, this.computeFont(8.5F)) + 12.0F * this.U;
            if (var3 + var8 > width && var3 > 0.0F) {
               var3 = 0.0F;
               var4 += 18.0F * this.U;
            }

            var3 += var8 + 4.0F * this.U;
         }

         return var4 + 18.0F * this.U;
      } else {
         return 18.0F * this.U;
      }
   }

   public float computeNext(CategoryComponent s, float width) {
      Collection var3 = s.getValues();
      if (var3 != null && !var3.isEmpty()) {
         float var4 = 0.0F;
         float var5 = 0.0F;

         for (CheckComponent var7 : (Iterable<CheckComponent>)(Iterable)var3) {
            String var8 = EngHelper.computeString(var7.getName());
            float var9 = IconFontRegistry.fontRegistry3.computeDelegate(var8, this.computeFont(8.5F)) + 12.0F * this.U;
            if (var4 + var9 > width && var4 > 0.0F) {
               var4 = 0.0F;
               var5 += 18.0F * this.U;
            }

            var4 += var9 + 4.0F * this.U;
         }

         return var5 + 18.0F * this.U;
      } else {
         return 18.0F * this.U;
      }
   }

   public static boolean computeFlag(Module f) {
      return false;
   }

   public boolean getCurrentView() {
      return currentView == MainMenuScreen.ViewMode.MODULES && !this.getSearchQuery().isEmpty();
   }

   public boolean computeCase(Module f) {
      if (!this.getCurrentView()) {
         return true;
      } else {
         String var2 = this.getSearchQuery();
         if (f.getName().toLowerCase().contains(var2)) {
            return true;
         } else if (f.getBigName() != null && f.getBigName().toLowerCase().contains(var2)) {
            return true;
         } else {
            String var3 = EngHelper.computeString(f.getBigName());
            if (var3 != null && var3.toLowerCase().contains(var2)) {
               return true;
            } else if (f.getDesc() != null && f.getDesc().toLowerCase().contains(var2)) {
               return true;
            } else {
               for (Component var5 : f.getSettings()) {
                  if (var5.getName().toLowerCase().contains(var2)) {
                     return true;
                  }

                  String var6 = EngHelper.computeString(var5.getName());
                  if (var6 != null && var6.toLowerCase().contains(var2)) {
                     return true;
                  }
               }

               return false;
            }
         }
      }
   }

   public boolean computeCategory(Module f) {
      if (computeFlag(f)) {
         return false;
      } else {
         return this.getCurrentView() ? this.computeCase(f) : f.getCategory() == this.active;
      }
   }

   public void runTime() {
      searchQuery = "";
      this.searchTypeTime = System.currentTimeMillis();
      this.runAnim();
   }

   public void runAnim() {
      this.scrollTarget = 0.0F;
      this.scrollAnim = 0.0F;
   }

   public float computeOpen(Component<?> s, float width) {
      if (s instanceof SliderComponent) {
         return 22.0F * this.U;
      } else if (s instanceof ComboComponent var5) {
         return 15.0F * this.U + this.computeNext(var5.list, width);
      } else if (s instanceof CategoryComponent var4) {
         return 15.0F * this.U + this.computeNext(var4, width);
      } else if (s instanceof CheckComponent) {
         return 15.0F * this.U;
      } else if (s instanceof SwitchComponent) {
         return 20.0F * this.U;
      } else if (s instanceof NumberComponent) {
         return 15.0F * this.U;
      } else if (s instanceof TextFieldComponent) {
         return 15.0F * this.U;
      } else if (s instanceof ColorPickerComponent var3) {
         return var3.pickerOpen ? 58.0F * this.U : 15.0F * this.U;
      } else {
         return 14.0F * this.U;
      }
   }

   public float computeDesc(Module f, float colWidth) {
      float var3 = colWidth - 26.0F * this.U;
      float var4 = 11.5F * this.U;
      float var5 = var4 + this.computeFloat(8.5F) / 2.0F;
      float var6 = var5 + this.computeFloat(8.5F) / 2.0F;
      if (!f.getDesc().isEmpty()) {
         var6 += 3.5F * this.U + this.computeDesc(IconFontRegistry.fontRegistry3, EngHelper.computeString(f.getDesc()), var3);
      }

      float var7 = 0.0F;
      int var8 = 0;

      for (Component var10 : f.getSettings()) {
         float var11 = this.visAnim(f, var10);
         if (var11 > 0.001F) {
            var7 += (this.computeOpen(var10, var3) + 5.0F * this.U) * var11;
            var8++;
         }
      }

      return var8 > 0 ? var6 + 7.0F * this.U + this.max2() + 7.0F * this.U + var7 + 4.0F * this.U : var6 + 11.5F * this.U;
   }

   public float computeFont(float base) {
      return base * this.U * gsFont;
   }

   public float computeFloat(float base) {
      return IconFontRegistry.fontRegistry3.computeDelegate(this.computeFont(base));
   }

   public float getU() {
      return 590.0F * this.U;
   }

   public float getFloat() {
      return 360.0F * this.U;
   }

   public float getFloat2() {
      return 130.0F * this.U;
   }

   public float getFloat3() {
      return 40.0F * this.U;
   }

   public float getGs() {
      return 13.0F * this.U * gsPad;
   }

   public float getGs2() {
      return 9.0F * this.U * gsPad;
   }

   public float max() {
      return Math.max(2.0F, 8.0F * this.U * gsRadius);
   }

   public float max2() {
      return Math.max(0.75F * this.U, 0.5F * S);
   }

   public void handleMin(int screenWidth, int screenHeight) {
      float var3 = S * gsScale;
      float var4 = (float)screenWidth * 0.95F;
      float var5 = (float)screenHeight * 0.95F;
      float var6 = 590.0F;
      float var7 = 360.0F;
      float var8 = Math.min(var4 / var6, var5 / var7);
      this.U = Math.min(var3, var8);
   }

   public static float computeFloat(String key) {
      return switch (key) {
         case "scale" -> gsScale;
         case "font" -> gsFont;
         case "radius" -> gsRadius;
         case "pad" -> gsPad;
         case "blur" -> gsBlur;
         case "bg" -> gsBg;
         default -> 1.0F;
      };
   }

   public static void handleVal(String key, float v) {
      switch (key) {
         case "scale":
            gsScale = MathHelper.clamp(v, 0.35F, 1.3F);
            break;
         case "font":
            gsFont = MathHelper.clamp(v, 0.8F, 1.3F);
            break;
         case "radius":
            gsRadius = MathHelper.clamp(v, 0.0F, 2.0F);
            break;
         case "pad":
            gsPad = MathHelper.clamp(v, 0.7F, 1.3F);
            break;
         case "blur":
            gsBlur = MathHelper.clamp(v, 0.0F, 1.0F);
            break;
         case "bg":
            gsBg = 1.0F;
      }
   }

   public void runDay() {
      this.animDayNight.setDirection(isDayMode ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var1 = isDayMode
         ? this.animDayNight.getEnd()
         : (this.animDayNight.checkDuration() && this.animDayNight.getDirection() == AnimationDirection.BACKWARDS ? 0.0F : this.animDayNight.getEnd());
      int var2 = -16316663;
      int var3 = -920071;
      byte var4 = -1;
      int var5 = GlyphMap.computeInt2(var3, var4, gsBlur);
      cApp = GlyphMap.computeInt2(var2, var5, var1);
      int var6 = -16448249;
      int var7 = -460036;
      byte var8 = -1;
      int var9 = GlyphMap.computeInt2(var7, var8, gsBlur);
      cSide = GlyphMap.computeInt2(var6, var9, var1);
      int var10 = -15856110;
      int var11 = -1906448;
      byte var12 = -1;
      int var13 = GlyphMap.computeInt2(var11, var12, gsBlur);
      cCard = GlyphMap.computeInt2(var10, var13, var1);
      int var14 = -16119283;
      int var15 = -920071;
      int var16 = -460036;
      int var17 = GlyphMap.computeInt2(var15, var16, gsBlur);
      cCardDisabled = GlyphMap.computeInt2(var14, var17, var1);
      cBrd = GlyphMap.computeInt2(-15198177, isDayMode && gsBlur < 0.05F ? -3418655 : -3418655, var1);
      cBrdL = GlyphMap.computeInt2(-14540245, isDayMode && gsBlur < 0.05F ? -7035976 : -7035976, var1);
      int var18 = -15856110;
      byte var19 = -1;
      cInp = GlyphMap.computeInt2(var18, var19, var1);
      cEl1 = GlyphMap.computeInt2(-15856110, -1906448, var1);
      cEl2 = GlyphMap.computeInt2(-15461350, -3418655, var1);
      C_DIM = GlyphMap.computeInt2(-7829358, -12102295, var1);
      C_WHITE = GlyphMap.computeInt2(-789515, -15788246, var1);
      L_TXT = GlyphMap.computeInt2(-789515, -15788246, var1);
      L_SUB = GlyphMap.computeInt2(-7829358, -12102295, var1);
      L_EL1 = GlyphMap.computeInt2(-15856110, -1906448, var1);
      L_EL2 = GlyphMap.computeInt2(-15461350, -3418655, var1);
      L_BRD = GlyphMap.computeInt2(-15198177, -3418655, var1);
   }

   public float getGs3() {
      return 28.0F * this.U + (float)(GS_SLIDERS.length * 26) * this.U + 24.0F * this.U + 24.0F * this.U + 12.0F * this.U;
   }

   public List<Module> getSearchQuery() {
      ArrayList var1 = new ArrayList();
      if (searchQuery.trim().isEmpty()) {
         return var1;
      } else {
         String var2 = searchQuery.trim().toLowerCase();

         for (Module var4 : FactoryHelper.getInstance().getClear().values()) {
            if (!var4.getName().equalsIgnoreCase("ClickGui")
               && !(var4 instanceof LegacyModule2)
               && !computeFlag(var4)
               && (
                  var4.getName().toLowerCase().contains(var2)
                     || var4.getBigName() != null && var4.getBigName().toLowerCase().contains(var2)
                     || var4.getDesc() != null && var4.getDesc().toLowerCase().contains(var2)
               )) {
               var1.add(var4);
               if (var1.size() >= 6) {
                  break;
               }
            }
         }

         return var1;
      }
   }

   public void initDay(DrawContext context, RenderTickCounter tickCounter) {
      float var3 = 2.0F;
      float var4 = (float)mc.getWindow().getScaleFactor();
      this.scaleFix = var3 / var4;
      int var5 = (int)((float)mc.getWindow().getScaledWidth() / this.scaleFix);
      int var6 = (int)((float)mc.getWindow().getScaledHeight() / this.scaleFix);
      float var7 = is3DCapturing ? 1.0F : this.getTime();
      float var8 = is3DCapturing ? 1.0F : this.getTime2();
      float var9 = is3DCapturing ? 0.0F : this.getTime3();
      float var10 = (float)var5 / 2.0F;
      float var11 = (float)var6 / 2.0F;
      if (context != null) {
         context.getMatrices().push();
         context.getMatrices().translate(var10, var11 + var9, 0.0F);
         context.getMatrices().scale(var8, var8, 1.0F);
         context.getMatrices().translate(-var10, -var11, 0.0F);
      }

      SHARED_MS.push();
      SHARED_MS.translate(var10, var11 + var9, 0.0F);
      SHARED_MS.scale(var8, var8, 1.0F);
      SHARED_MS.translate(-var10, -var11, 0.0F);
      S = FactoryHelper.getInstance().getClear().get(LegacyModule2.class).mvodq.getCachedValue();
      this.handleMin(var5, var6);
      float var12 = this.getU();
      float var13 = this.getFloat();
      float var14 = (float)var5 / 2.0F - var12 / 2.0F;
      float var15 = (float)var6 / 2.0F - var13 / 2.0F;
      FontRegistry var16 = IconFontRegistry.fontRegistry3;
      FontRegistry var17 = IconFontRegistry.fontRegistry2;
      FontRegistry var18 = IconFontRegistry.fontRegistry;
      FontRegistry var19 = IconFontRegistry.fontRegistry7;
      FontRegistry var20 = IconFontRegistry.fontRegistry6;
      FontRegistry var21 = IconFontRegistry.fontRegistry8;
      int var22 = getAccent();
      boolean var23 = computeFlag(var22);
      if (!is3DCapturing && gsBlur > 0.01F) {
         int var148 = isDayMode ? -1 : -16316663;
         TintColorHelper.handleFloat(var14, var15, var12, var13, var7, this.max(), var148);
      } else {
         int var24 = isDayMode ? -1 : cApp;
         RenderKit.Render2D.handleFloat(var14, var15, var12, var13, GlyphMap.computeRound2(cBrdL, var7 * 0.85F), this.max());
         RenderKit.Render2D.handleFloat(
            var14 + this.max2(),
            var15 + this.max2(),
            var12 - this.max2() * 2.0F,
            var13 - this.max2() * 2.0F,
            GlyphMap.computeRound2(var24, var7),
            Math.max(0.5F, this.max() - this.max2())
         );
      }

      float var149 = var14;
      float var26 = this.getFloat2();
      float var27 = (isDayMode ? (gsBlur > 0.01F ? 0.22F : 0.65F) : (gsBlur > 0.01F ? 0.35F : 0.0F)) * var7;
      if (var27 > 0.01F) {
         float var28 = Math.max(0.5F, this.max() - this.max2());
         int var29 = GlyphMap.computeRound2(cSide, var27);
         RenderKit.Render2D.handleFloat(
            var14 + this.max2(),
            var15 + this.max2(),
            var26 - this.max2(),
            var13 - 2.0F * this.max2(),
            new int[]{var29, var29, var29, var29},
            var28,
            0.0F,
            0.0F,
            var28
         );
      }

      RenderKit.Render2D.handleFloat(
         var14 + var26,
         var15 + this.max2(),
         this.max2(),
         var13 - 2.0F * this.max2(),
         GlyphMap.computeRound2(cBrd, var7 * (gsBlur > 0.01F ? 0.6F : 0.85F)),
         0.0F
      );
      long var150 = System.currentTimeMillis();
      String var30 = "GONE";
      float var31 = this.computeFont(13.0F);
      float var32 = 2.2F * this.U;
      float var33 = 0.0F;

      for (int var34 = 0; var34 < var30.length(); var34++) {
         var33 += var18.computeDelegate(String.valueOf(var30.charAt(var34)), var31) + (var34 == var30.length() - 1 ? 0.0F : var32);
      }

      float var151 = var14 + (var26 - var33) / 2.0F;
      float var35 = var15 + (this.getFloat3() - this.computeFloat(13.0F) - this.computeFloat(7.2F) - 2.5F * this.U) / 2.0F;

      for (int var36 = 0; var36 < var30.length(); var36++) {
         String var37 = String.valueOf(var30.charAt(var36));
         float var38 = (float)Math.sin((double)var150 / 400.0 - (double)var36 * 0.7) * 0.5F + 0.5F;
         int var39 = isDayMode ? -15788246 : -16118508;
         int var40 = GlyphMap.computeInt2(GlyphMap.computeRound2(var39, var7), GlyphMap.computeRound2(var22, var7), 0.2F + 0.8F * var38);
         var18.handleDelegate(var37, var151, var35, var31, var40);
         var151 += var18.computeDelegate(var37, var31) + var32;
      }

      float var152 = this.computeFont(7.2F);
      float var153 = var16.computeDelegate("1.21.4", var152);
      float var154 = var14 + (var26 - var153) / 2.0F;
      float var155 = var35 + this.computeFloat(13.0F) + 2.5F * this.U;
      var16.handleDelegate("1.21.4", var154, var155, var152, GlyphMap.computeRound2(isDayMode ? -10193781 : -8485223, var7 * 0.85F));
      float var156 = var15 + this.getFloat3();
      RenderKit.Render2D.handleFloat(var14 + 10.0F * this.U, var156, var26 - 20.0F * this.U, this.max2(), GlyphMap.computeRound2(cBrd, 0.75F * var7), 0.0F);
      ModuleCategory[] var41 = new ModuleCategory[]{
         ModuleCategory.COMBAT, ModuleCategory.MOVEMENT, ModuleCategory.RENDER, ModuleCategory.PLAYER, ModuleCategory.PVE, ModuleCategory.OTHER
      };
      String[] var42 = new String[]{"CONFIGS", "MACROS", "ROTATIONS", "GUISET"};
      float var43 = var15 + this.getFloat3() + 10.0F * this.U;
      float var44 = 13.0F * this.U;
      float var45 = 21.0F * this.U;

      for (ModuleCategory var49 : var41) {
         boolean var50 = currentView == MainMenuScreen.ViewMode.MODULES && this.active == var49 && !this.getCurrentView() && !showGuiSettings;
         Animation var51 = this.chipAnim("nav:" + var49);
         var51.setDirection(var50 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var52 = var51.getEnd();
         boolean var53 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var149, var43, var26, var45);
         Animation var54 = this.chipAnim("navh:" + var49);
         var54.setDirection(var53 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var55 = var54.getEnd();
         if (var52 > 0.01F) {
            int var56 = GlyphMap.computeRound2(var22, (isDayMode ? 0.16F : 0.14F) * var7 * var52);
            RenderKit.Render2D.handleFloat(var149 + 7.0F * this.U, var43 + 1.5F * this.U, var26 - 14.0F * this.U, var45 - 3.0F * this.U, var56, 5.0F * this.U);
         } else if (var55 > 0.01F) {
            RenderKit.Render2D.handleFloat(
               var149 + 7.0F * this.U,
               var43 + 1.5F * this.U,
               var26 - 14.0F * this.U,
               var45 - 3.0F * this.U,
               GlyphMap.computeRound(isDayMode ? 0 : 255, (isDayMode ? 0.05F : 0.04F) * var7 * var55),
               5.0F * this.U
            );
         }

         int var179 = isDayMode ? -10193781 : C_DIM;
         int var57 = isDayMode ? -15788246 : C_WHITE;
         int var58 = GlyphMap.computeRound3(
            GlyphMap.computeInt2(GlyphMap.computeRound2(var179, 0.95F), GlyphMap.computeRound2(var57, 0.95F), Math.max(var52, var55)), var7
         );
         int var59 = GlyphMap.computeRound3(
            GlyphMap.computeInt2(GlyphMap.computeRound2(var179, 0.95F), GlyphMap.computeRound2(var57, 0.95F), Math.max(var52, var55 * 0.7F)), var7
         );
         float var60 = this.computeFont(10.5F);
         var19.handleDelegate(var49.getIcon(), var149 + var44, var43 + 5.0F * this.U, var60, var58);
         var17.handleDelegate(EngHelper.computeString(var49.getName()), var149 + var44 + 18.0F * this.U, var43 + 6.0F * this.U, this.computeFont(8.2F), var59);
         var43 += var45 + 2.0F * this.U;
      }

      var43 += 4.0F * this.U;
      RenderKit.Render2D.handleFloat(var149 + 10.0F * this.U, var43, var26 - 20.0F * this.U, this.max2(), GlyphMap.computeRound2(cBrd, 0.75F * var7), 0.0F);
      var43 += 6.0F * this.U;

      for (String var165 : var42) {
         boolean var167 = var165.equals("GUISET");
         boolean var169 = var165.equals("CONFIGS");
         boolean var171 = var165.equals("MACROS");
         boolean var173 = var165.equals("ROTATIONS");
         boolean var175 = false;
         if (var167) {
            var175 = showGuiSettings;
         } else if (var169) {
            var175 = currentView == MainMenuScreen.ViewMode.CONFIGS && !showGuiSettings;
         } else if (var171) {
            var175 = currentView == MainMenuScreen.ViewMode.MACROS && !showGuiSettings;
         } else if (var173) {
            var175 = currentView == MainMenuScreen.ViewMode.ROTATIONS && !showGuiSettings;
         }

         Animation var177 = this.chipAnim("nav:" + var165);
         var177.setDirection(var175 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var180 = var177.getEnd();
         boolean var182 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var149, var43, var26, var45);
         Animation var185 = this.chipAnim("navh:" + var165);
         var185.setDirection(var182 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var187 = var185.getEnd();
         if (var180 > 0.01F) {
            int var189 = GlyphMap.computeRound2(var22, (isDayMode ? 0.16F : 0.14F) * var7 * var180);
            RenderKit.Render2D.handleFloat(var149 + 7.0F * this.U, var43 + 1.5F * this.U, var26 - 14.0F * this.U, var45 - 3.0F * this.U, var189, 5.0F * this.U);
         } else if (var187 > 0.01F) {
            RenderKit.Render2D.handleFloat(
               var149 + 7.0F * this.U,
               var43 + 1.5F * this.U,
               var26 - 14.0F * this.U,
               var45 - 3.0F * this.U,
               GlyphMap.computeRound(isDayMode ? 0 : 255, (isDayMode ? 0.05F : 0.04F) * var7 * var187),
               5.0F * this.U
            );
         }

         int var190 = isDayMode ? -10193781 : C_DIM;
         int var61 = isDayMode ? -15788246 : C_WHITE;
         int var62 = GlyphMap.computeRound3(
            GlyphMap.computeInt2(GlyphMap.computeRound2(var190, 0.95F), GlyphMap.computeRound2(var61, 0.95F), Math.max(var180, var187)), var7
         );
         int var63 = GlyphMap.computeRound3(
            GlyphMap.computeInt2(GlyphMap.computeRound2(var190, 0.95F), GlyphMap.computeRound2(var61, 0.95F), Math.max(var180, var187 * 0.7F)), var7
         );
         float var66 = this.computeFont(10.5F);
         String var64;
         String var65;
         if (var167) {
            var64 = "GUI Settings";
            var65 = "h";
         } else if (var169) {
            var64 = "Configs";
            var65 = "O";
         } else if (var171) {
            var64 = "Macros";
            var65 = "I";
         } else {
            var64 = "Rotations";
            var65 = "i";
         }

         float var67 = var149 + var44 + 18.0F * this.U;
         float var68 = var43 + 6.0F * this.U;
         var19.handleDelegate(var65, var149 + var44, var43 + 5.0F * this.U, var66, var62);
         var17.handleDelegate(EngHelper.computeString(var64), var67, var68, this.computeFont(8.2F), var63);
         var43 += var45 + 2.0F * this.U;
      }

      float var160 = 28.0F * this.U;
      float var162 = 9.0F * this.U;
      float var164 = var15 + var13 - var160 - var162;
      float var166 = var149 + var162;
      float var168 = var26 - var162 * 2.0F;
      float var170 = 5.0F * this.U;
      if (!is3DCapturing && gsBlur > 0.01F) {
         TintColorHelper.handleFloat(var166, var164, var168, var160, var7 * gsBlur, var170, isDayMode ? -1 : -16250610);
      }

      float var172 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 0.92F) * var7;
      int var174 = isDayMode ? -1 : -15460574;
      RenderKit.Render2D.handleFloat(var166, var164, var168, var160, GlyphMap.computeRound2(cBrd, var7 * (gsBlur > 0.01F ? 0.35F : 0.85F)), var170);
      RenderKit.Render2D.handleFloat(
         var166 + this.max2(),
         var164 + this.max2(),
         var168 - this.max2() * 2.0F,
         var160 - this.max2() * 2.0F,
         GlyphMap.computeRound2(var174, var172),
         Math.max(0.5F, var170 - this.max2())
      );
      float var176 = 18.0F * this.U;
      float var178 = var166 + 5.0F * this.U;
      float var181 = var164 + (var160 - var176) / 2.0F;
      if (mc.player != null) {
         Identifier var183 = mc.player.getSkinTextures().texture();
         Render.myazem
            .handleRgb(
               SHARED_MS,
               var183,
               var178,
               var181,
               var176,
               var176,
               0.4F * this.U,
               3.5F * this.U,
               new Color(GlyphMap.computeColor(255, (int)(var7 * 255.0F)), true)
            );
      } else {
         RenderKit.Render2D.handleFloat(var178, var181, var176, var176, GlyphMap.computeRound2(isDayMode ? -3418655 : -12303292, var7), 3.5F * this.U);
         RenderKit.Render2D.handleFloat(
            var178 + this.max2(),
            var181 + this.max2(),
            var176 - this.max2() * 2.0F,
            var176 - this.max2() * 2.0F,
            GlyphMap.computeRound(isDayMode ? 230 : 0, var7),
            Math.max(0.5F, 3.5F * this.U - this.max2())
         );
         var18.handleDelegate2(
            "G",
            var178 + var176 / 2.0F,
            var181 + (var176 - this.computeFloat(7.5F)) / 2.0F,
            this.computeFont(7.5F),
            GlyphMap.computeRound(isDayMode ? 20 : 255, var7)
         );
      }

      String var184 = mc.getSession() != null && mc.getSession().getUsername() != null
         ? mc.getSession().getUsername()
         : (mc.player != null ? mc.player.getName().getString() : "Player");
      String var186 = this.getSESSION_START_TIME();
      float var188 = var178 + var176 + 5.5F * this.U;
      float var191 = var164 + 5.5F * this.U;
      var18.handleDelegate(var184, var188, var191, this.computeFont(7.8F), GlyphMap.computeRound(isDayMode ? 15 : 255, var7));
      float var192 = var164 + 15.0F * this.U;
      var16.handleVal(
         var186,
         var188,
         var192,
         var168 - (var188 - var166) - 4.0F * this.U,
         GlyphMap.computeRound2(isDayMode ? -10193781 : -8485223, var7 * 0.85F),
         this.computeFont(6.4F)
      );
      float var193 = var14 + var26;
      float var194 = var12 - var26;
      RenderKit.Render2D.handleFloat(var193, var15 + this.getFloat3(), var194 - this.max(), this.max2(), GlyphMap.computeRound2(cBrd, var7), 0.0F);
      String var195;
      if (currentView == MainMenuScreen.ViewMode.CONFIGS) {
         var195 = EngHelper.computeString("Features") + " / " + EngHelper.computeString("Configs");
      } else if (currentView == MainMenuScreen.ViewMode.MACROS) {
         var195 = EngHelper.computeString("Features") + " / " + EngHelper.computeString("Macros");
      } else if (currentView == MainMenuScreen.ViewMode.ROTATIONS) {
         var195 = EngHelper.computeString("Features") + " / " + EngHelper.computeString("Rotation Manager");
      } else {
         var195 = this.getCurrentView()
            ? EngHelper.computeString("Search")
            : EngHelper.computeString("Features") + " / " + EngHelper.computeString(this.active.getName());
      }

      var18.handleDelegate(
         var195,
         var193 + this.getGs(),
         var15 + (this.getFloat3() - this.computeFloat(9.0F)) / 2.0F,
         this.computeFont(9.0F),
         GlyphMap.computeRound(isDayMode ? 20 : 255, var7)
      );
      float var196 = 5.0F;
      float var197 = 8.0F * this.U;
      float var198 = 6.0F * this.U;
      float var199 = var196 * var197 + (var196 - 1.0F) * var198;
      float var69 = var14 + var12 - this.getGs();
      float var70 = var15 + (this.getFloat3() - var197) / 2.0F;
      float var71 = var69 - var199;

      for (int var72 = 0; var72 < 5; var72++) {
         boolean var73 = selectedAccentIndex == var72;
         this.accentAnims[var72].setDirection(var73 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var74 = this.accentAnims[var72].getEnd();
         int var75 = ACCENT_DOT_COLORS[var72];
         float var76 = var197 + 2.0F * this.U * var74;
         float var77 = var70 - 1.0F * this.U * var74;
         Render.ringRenderer
            .handleRgb(SHARED_MS, var71 + var197 / 2.0F, var77 + var76 / 2.0F, var76 / 2.0F, new Color(GlyphMap.computeRound2(var75, var7), true));
         if (var74 > 0.01F) {
            Render.ringRenderer
               .handleRgb(
                  SHARED_MS,
                  var71 + var197 / 2.0F,
                  var77 + var76 / 2.0F,
                  var76 / 2.0F + 1.8F * this.U,
                  1.0F * this.U,
                  new Color(GlyphMap.computeRound2(var75, var7 * var74 * 0.8F), true)
               );
         }

         var71 += var197 + var198;
      }

      float var200 = 18.0F * this.U;
      float var201 = 16.0F * this.U;
      float var202 = var69 - var199 - 8.0F * this.U - var200;
      float var203 = var15 + (this.getFloat3() - var201) / 2.0F;
      boolean var204 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var202, var203, var200, var201);
      Animation var205 = this.chipAnim("sys:modehov");
      var205.setDirection(var204 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var78 = var205.getEnd();
      float var79 = 4.5F * this.U;
      if (!is3DCapturing && gsBlur > 0.01F) {
         TintColorHelper.handleFloat(var202, var203, var200, var201, var7 * gsBlur, var79, isDayMode ? -1 : -16250610);
      }

      float var80 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 1.0F) * var7;
      int var81 = GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, var80), GlyphMap.computeRound2(var22, var7 * 0.25F), var78);
      int var82 = GlyphMap.computeInt2(GlyphMap.computeRound2(cBrd, var7 * (gsBlur > 0.01F ? 0.35F : 1.0F)), GlyphMap.computeRound2(var22, var7), var78);
      RenderKit.Render2D.handleFloat(var202, var203, var200, var201, var82, var79);
      RenderKit.Render2D.handleFloat(
         var202 + this.max2(), var203 + this.max2(), var200 - this.max2() * 2.0F, var201 - this.max2() * 2.0F, var81, Math.max(0.5F, var79 - this.max2())
      );
      this.handleDegrees2(
         SHARED_MS, var202 + var200 / 2.0F, var203 + var201 / 2.0F, 8.5F * this.U, GlyphMap.computeColor(isDayMode ? 20 : 255, (int)(var7 * 255.0F))
      );
      float var83 = var202 - 8.0F * this.U;
      RenderKit.Render2D.handleFloat(
         var83,
         var15 + (this.getFloat3() - 14.0F * this.U) / 2.0F,
         this.max2(),
         14.0F * this.U,
         GlyphMap.computeRound2(cBrd, var7 * (gsBlur > 0.01F ? 0.35F : 1.0F)),
         0.0F
      );
      float var84 = 46.0F * this.U;
      float var85 = 16.0F * this.U;
      float var86 = var83 - 8.0F * this.U - var84;
      float var87 = var15 + (this.getFloat3() - var85) / 2.0F;
      boolean var88 = EngHelper.getInstance().check();
      this.animLang.setDirection(var88 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var89 = this.animLang.getEnd();
      if (!is3DCapturing && gsBlur > 0.01F) {
         TintColorHelper.handleFloat(var86, var87, var84, var85, var7 * gsBlur, var85 / 2.0F, isDayMode ? -1 : -16250610);
      }

      float var90 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 1.0F) * var7;
      RenderKit.Render2D.handleFloat(var86, var87, var84, var85, GlyphMap.computeRound2(cBrd, var7 * (gsBlur > 0.01F ? 0.35F : 1.0F)), var85 / 2.0F);
      RenderKit.Render2D.handleFloat(
         var86 + this.max2(),
         var87 + this.max2(),
         var84 - this.max2() * 2.0F,
         var85 - this.max2() * 2.0F,
         GlyphMap.computeRound2(cInp, var90),
         (var85 - this.max2() * 2.0F) / 2.0F
      );
      float var91 = (var84 - 2.0F * this.max2()) / 2.0F;
      float var92 = var86 + this.max2() + (1.0F - var89) * var91;
      RenderKit.Render2D.handleFloat(
         var92, var87 + this.max2(), var91, var85 - 2.0F * this.max2(), GlyphMap.computeRound2(var22, var7 * 0.88F), (var85 - 2.0F * this.max2()) / 2.0F
      );
      int var93 = GlyphMap.computeInt2(
         GlyphMap.computeRound2(C_DIM, 0.95F), GlyphMap.computeColor(isDayMode && var89 < 0.5F ? 20 : 255, (int)(var7 * 255.0F)), var89
      );
      int var94 = GlyphMap.computeInt2(
         GlyphMap.computeRound2(C_DIM, 0.95F), GlyphMap.computeColor(isDayMode && var89 >= 0.5F ? 20 : 255, (int)(var7 * 255.0F)), 1.0F - var89
      );
      var18.handleDelegate2("RU", var86 + this.max2() + var91 / 2.0F, var87 + (var85 - this.computeFloat(7.0F)) / 2.0F, this.computeFont(7.0F), var93);
      var18.handleDelegate2("ENG", var86 + this.max2() + var91 + var91 / 2.0F, var87 + (var85 - this.computeFloat(7.0F)) / 2.0F, this.computeFont(7.0F), var94);
      this.animSearchFocus.setDirection(searchActive ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      this.animSearchText.setDirection(!searchQuery.isEmpty() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var95 = this.animSearchFocus.getEnd();
      float var96 = this.animSearchText.getEnd();
      float var97 = Math.max(var95, var96);
      float var98 = 100.0F * this.U;
      float var99 = 175.0F * this.U;
      float var100 = var98 + (var99 - var98) * var97;
      float var101 = 18.5F * this.U;
      float var102 = var86 - 8.0F * this.U - var100;
      float var103 = var15 + (this.getFloat3() - var101) / 2.0F;
      boolean var104 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var102, var103, var100, var101);
      Animation var105 = this.chipAnim("sys:searchhov");
      var105.setDirection(var104 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      if (!is3DCapturing && gsBlur > 0.01F) {
         TintColorHelper.handleFloat(var102, var103, var100, var101, var7 * gsBlur, 5.0F * this.U, isDayMode ? -1 : -16250610);
      }

      float var106 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 1.0F) * var7;
      int var107 = GlyphMap.computeRound2(isDayMode ? -7035976 : -13288889, var7 * (gsBlur > 0.01F ? (var95 > 0.5F ? 0.6F : 0.35F) : 1.0F));
      RenderKit.Render2D.handleFloat(var102, var103, var100, var101, var107, 5.0F * this.U);
      RenderKit.Render2D.handleFloat(
         var102 + this.max2(),
         var103 + this.max2(),
         var100 - this.max2() * 2.0F,
         var101 - this.max2() * 2.0F,
         GlyphMap.computeRound2(cInp, var106),
         Math.max(0.5F, 5.0F * this.U - this.max2())
      );
      int var108 = isDayMode ? -15788246 : -1;
      int var109 = GlyphMap.computeRound3(var108, var7 * (0.8F + 0.2F * var95));
      String var110 = "\ue002";
      float var111 = this.computeFont(9.0F);
      float var112 = var102 + 7.5F * this.U;
      float var113 = var103 + (var101 - this.computeFloat(9.0F)) / 2.0F - 0.5F * this.U;
      var21.handleDelegate(var110, var112, var113, var111, var109);
      float var114 = this.computeFont(9.5F);
      float var115 = var112 + var21.computeDelegate(var110, var111) + 5.0F * this.U;
      float var116 = var100 - (var115 - var102) - 6.0F * this.U;
      float var117 = var103 + (var101 - this.computeFloat(9.5F)) / 2.0F - 0.5F * this.U;
      float var118 = Math.max(var96, var95);
      if (var118 < 0.99F) {
         var16.handleDelegate(
            EngHelper.computeString("Search"), var115, var117 + 2.0F * this.U * var118, var114, GlyphMap.computeRound2(C_DIM, var7 * (1.0F - var118))
         );
      }

      if (var96 > 0.01F) {
         var16.handleVal2(
            searchQuery, var115, var117 - 2.0F * this.U * (1.0F - var96), var116, GlyphMap.computeRound(isDayMode ? 20 : 255, var7 * var96), var114
         );
      }

      boolean var119 = System.currentTimeMillis() - this.searchTypeTime < 500L;
      float var120 = var95 * (!var119 && System.currentTimeMillis() / 500L % 2L != 0L ? 0.0F : 1.0F);
      float var121 = this.smooth("search:caret", var120);
      float var122 = this.smooth("search:caretx", var115 + Math.min(var16.computeDelegate(searchQuery, var114), var116) + 1.5F * this.U);
      RenderKit.Render2D.handleFloat(
         var122, var103 + 3.5F * this.U, 0.8F * this.U, var101 - 7.0F * this.U, GlyphMap.computeRound2(C_WHITE, var7 * var121), 0.4F * this.U
      );
      float var123 = var193 + this.getGs();
      float var124 = var15 + this.getFloat3() + this.getGs();
      float var125 = var194 - this.getGs() * 2.0F;
      float var126 = var13 - this.getFloat3() - this.getGs() * 2.0F;
      this.animRotationsTab.setDirection(currentView == MainMenuScreen.ViewMode.ROTATIONS ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var127 = this.animRotationsTab.getEnd();
      if (currentView == MainMenuScreen.ViewMode.CONFIGS) {
         this.initConfig(var14, var15, var12, var13, var123, var124, var125, var126, var7, var22, var16, var17, var18);
      } else if (currentView == MainMenuScreen.ViewMode.MACROS) {
         this.handleMax(var14, var15, var12, var13, var123, var124, var125, var126, var7, var22, var16, var17, var18);
      } else if (currentView == MainMenuScreen.ViewMode.ROTATIONS) {
         this.handleFormat(var14, var15, var12, var13, var123, var124, var125, var126, var7, var22, var16, var17, var18, var19);
      } else {
         this.initName(var193, var15, var194, var13, var123, var124, var125, var126, var7, var22, var16, var17, var18);
      }

      List var128 = this.getSearchQuery();
      boolean var129 = searchActive && !var128.isEmpty();
      this.animSearchDropdown.setDirection(var129 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var130 = this.animSearchDropdown.getEnd();
      if (var130 > 0.01F) {
         float var131 = var100;
         float var132 = 23.0F * this.U;
         float var133 = 6.0F * this.U + (float)var128.size() * var132;
         float var134 = var102;
         float var135 = var103 + var101 + 3.0F * this.U;
         if (!is3DCapturing && gsBlur > 0.01F) {
            TintColorHelper.handleFloat(var102, var135, var100, var133, var130 * var7 * gsBlur, 6.0F * this.U, isDayMode ? -1 : -16250610);
         }

         int var136 = GlyphMap.computeRound2(cBrdL, var130 * var7);
         RenderKit.Render2D.handleFloat(var102, var135, var100, var133, this.max2(), var136, 6.0F * this.U);
         if (is3DCapturing || gsBlur < 0.98F) {
            int var137 = GlyphMap.computeRound2(isDayMode ? -1 : -15921388, is3DCapturing ? var130 * var7 : (0.88F + 0.12F * (1.0F - gsBlur)) * var130 * var7);
            RenderKit.Render2D.handleFloat(
               var102 + this.max2(),
               var135 + this.max2(),
               var100 - this.max2() * 2.0F,
               var133 - this.max2() * 2.0F,
               var137,
               Math.max(0.5F, 6.0F * this.U - this.max2())
            );
         }

         float var207 = var135 + 3.0F * this.U;

         for (Module var139 : (Iterable<Module>)(Iterable)var128) {
            boolean var140 = MaxMinHelper.computeFlag(
               (float)this.lastMouseX, (float)this.lastMouseY, var134 + 4.0F * this.U, var207, var131 - 8.0F * this.U, var132
            );
            Animation var141 = this.chipAnim("pop:" + var139.getName());
            var141.setDirection(var140 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
            float var142 = var141.getEnd();
            if (var142 > 0.01F) {
               RenderKit.Render2D.handleFloat(
                  var134 + 4.0F * this.U,
                  var207 + 1.0F * this.U,
                  var131 - 8.0F * this.U,
                  var132 - 2.0F * this.U,
                  GlyphMap.computeRound2(var22, 0.15F * var142 * var130 * var7),
                  4.0F * this.U
               );
            }

            int var143 = GlyphMap.computeRound3(
               GlyphMap.computeInt2(GlyphMap.computeRound2(C_DIM, 0.9F), GlyphMap.computeRound2(var22, 0.95F), var142), var130 * var7
            );
            int var144 = GlyphMap.computeRound3(
               GlyphMap.computeInt2(GlyphMap.computeRound2(isDayMode ? -15788246 : C_WHITE, 0.9F), GlyphMap.computeRound2(var22, 1.0F), var142), var130 * var7
            );
            var19.handleDelegate(var139.getCategory().getIcon(), var134 + 8.0F * this.U, var207 + 6.0F * this.U, this.computeFont(9.5F), var143);
            var18.handleDelegate(EngHelper.computeString(var139.getBigName()), var134 + 24.0F * this.U, var207 + 6.5F * this.U, this.computeFont(8.0F), var144);
            String var145 = EngHelper.computeString(var139.getCategory().getName());
            float var146 = var16.computeDelegate(var145, this.computeFont(6.5F)) + 8.0F * this.U;
            float var147 = var134 + var131 - 8.0F * this.U - var146;
            RenderKit.Render2D.handleFloat(var147, var207 + 5.0F * this.U, var146, 13.0F * this.U, GlyphMap.computeRound2(cInp, var130 * var7), 3.0F * this.U);
            var16.handleDelegate(var145, var147 + 4.0F * this.U, var207 + 8.0F * this.U, this.computeFont(6.5F), GlyphMap.computeRound2(C_DIM, var130 * var7));
            var207 += var132;
         }
      }

      this.animGuiSettings.setDirection(showGuiSettings ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var206 = this.animGuiSettings.getEnd();
      if (var206 > 0.01F) {
         this.parseGs(var14, var15, var12, var13, var194, var7 * var206, var206, var22, var16, var17, var18, var20);
      }

      MainMenuScreenHelper.run4();
      SHARED_MS.pop();
      if (context != null) {
         context.getMatrices().pop();
      }
   }

   public void initName(
      float mx,
      float y,
      float mw,
      float h,
      float cx,
      float cy,
      float cw,
      float chh,
      float globalAnim,
      int acc,
      FontRegistry body,
      FontRegistry med,
      FontRegistry bold
   ) {
      this.handleMax(mx, y + this.getFloat3(), mw, h - this.getFloat3());
      this.scrollAnim = this.scrollAnim + (this.scrollTarget - this.scrollAnim) * 0.25F;
      float var14 = (cw - this.getGs2()) / 2.0F;
      float[] var15 = new float[]{cy - this.scrollAnim, cy - this.scrollAnim};
      float var16 = this.tabSwitchAnim.getEnd();
      float var17 = (1.0F - var16) * 10.0F * this.U;
      float var18 = var16 * globalAnim;

      for (Module var20 : FactoryHelper.getInstance().getClear().values()) {
         if (!var20.getName().equalsIgnoreCase("ClickGui")
            && !var20.getName().equalsIgnoreCase("Click GUI")
            && !var20.getName().equalsIgnoreCase("Click_GUI")
            && !var20.getName().equalsIgnoreCase("Configs")
            && !var20.getName().equalsIgnoreCase("ConfigsFeature")
            && !(var20 instanceof LegacyModule2)
            && this.computeCategory(var20)) {
            float var21 = this.computeDesc(var20, var14);
            int var22 = var15[0] <= var15[1] ? 0 : 1;
            float var23 = cx + (float)var22 * (var14 + this.getGs2());
            float var24 = var15[var22];
            var15[var22] += var21 + this.getGs2();
            float var25 = var23;
            float var26 = var24 + var17;
            if (!(var26 + var21 < y + this.getFloat3() - 15.0F * this.U) && !(var26 > y + h + 15.0F * this.U)) {
               var20.getAnimation16().setDirection(var20.isEnabled() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var27 = var20.getAnimation16().getEnd();
               float var28 = var18;
               boolean var29 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var23, var26, var14, var21);
               Animation var30 = this.chipAnim(var20.getName() + ":cardhov");
               var30.setDirection(var29 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var31 = var30.getEnd();
               boolean var32 = this.highlightedModule == var20;
               float var33 = 0.0F;
               if (var32) {
                  long var34 = System.currentTimeMillis() - this.highlightStartTime;
                  if (var34 < 1800L) {
                     float var36 = 1.0F - (float)var34 / 1800.0F;
                     float var37 = (float)((double)var34 / 600.0 * Math.PI * 2.0);
                     float var38 = (float)(Math.sin((double)var37 - (Math.PI / 2)) * 0.5 + 0.5);
                     var33 = var38 * var36;
                  } else {
                     this.highlightedModule = null;
                  }
               }

               float var127 = (isDayMode ? (gsBlur > 0.01F ? 0.26F : 0.85F) : (gsBlur > 0.01F ? 0.36F : 1.0F)) * var18;
               int var35 = GlyphMap.computeInt2(GlyphMap.computeRound2(cCardDisabled, var127), GlyphMap.computeRound2(cCard, var127), var27);
               int var128 = GlyphMap.computeInt2(var35, GlyphMap.computeRound2(cInp, (gsBlur > 0.01F ? 0.4F : 0.88F) * var18), var31 * 0.25F);
               if (var33 > 0.01F) {
                  float var129 = 2.0F * this.U * var33;
                  RenderKit.Render2D.handleFloat(
                     var23 - var129,
                     var26 - var129,
                     var14 + var129 * 2.0F,
                     var21 + var129 * 2.0F,
                     GlyphMap.computeRound2(acc, var33 * 0.85F * var18),
                     this.max() + var129
                  );
               }

               int var130 = acc & 16777215;
               float var131 = var27 * 0.02F + 0.08F;
               float var39 = 0.5F * this.U;
               float var40 = 1.6F * this.U;
               float[] var41 = new float[]{0.24F, 0.16F, 0.1F, 0.05F, 0.02F};
               float[] var42 = new float[]{0.4F, 0.9F, 1.6F, 2.6F, 3.8F};
               float[] var43 = new float[]{0.55F, 0.7F, 0.85F, 1.0F, 1.1F};

               for (int var44 = 0; var44 < 5; var44++) {
                  float var45 = var42[var44] * this.U;
                  float var46 = var41[var44] * var131 * var28 * (isDayMode ? 0.45F : 1.0F);
                  int var47 = var130 | (int)(var46 * 255.0F) << 24;
                  RenderKit.Render2D.handleFloat(
                     var25 - var45 + var39, var26 + var40 * var43[var44], var14 + var45 * 2.0F, var21 - var40 * 0.35F + var45, var47, this.max() + var45
                  );
               }

               if (!is3DCapturing && gsBlur > 0.01F) {
                  TintColorHelper.handleFloat(var25, var26, var14, var21, var28 * gsBlur, this.max(), isDayMode ? -1 : -16316663);
                  int var132 = GlyphMap.computeRound2(isDayMode ? -3418655 : -1, var28 * (isDayMode ? 0.18F : 0.1F));
                  RenderKit.Render2D.handleFloat(var25, var26, var14, var21, var132, this.max());
                  RenderKit.Render2D.handleFloat(
                     var25 + this.max2(),
                     var26 + this.max2(),
                     var14 - this.max2() * 2.0F,
                     var21 - this.max2() * 2.0F,
                     var128,
                     Math.max(0.5F, this.max() - this.max2())
                  );
               } else {
                  RenderKit.Render2D.handleFloat(var25, var26, var14, var21, var128, this.max());
               }

               float var133 = var25 + 13.0F * this.U;
               float var134 = var14 - 26.0F * this.U;
               float var135 = 11.5F * this.U;
               float var136 = var26 + var135 + this.computeFloat(8.5F) / 2.0F;
               String var48 = this.bindingModule == var20 ? "..." : (var20.getKey() == -1 ? "n/a" : KeyCode.keyName(var20.getKey()).replace("NONE", "n/a"));
               float var49 = 12.0F * this.U;
               float var50 = Math.max(19.0F * this.U, Math.min(body.computeDelegate(var48, this.computeFont(7.2F)) + 8.0F * this.U, var14 * 0.38F));
               float var51 = var25 + var14 - 13.0F * this.U - var50;
               float var52 = var136 - var49 / 2.0F;
               boolean var53 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var51, var52, var50, var49);
               Animation var54 = this.chipAnim(var20.getName() + ":modbindhov");
               var54.setDirection(var53 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var55 = var54.getEnd();
               float var56 = this.bindingModule == var20
                  ? 0.55F + 0.45F * (float)Math.sin((double)((float)(System.currentTimeMillis() % 1000L) / 1000.0F) * Math.PI * 2.0)
                  : 0.0F;
               if (!is3DCapturing && gsBlur > 0.01F) {
                  TintColorHelper.handleFloat(var51, var52, var50, var49, var28 * gsBlur, 3.5F * this.U, isDayMode ? -1 : -16250610);
               }

               float var57 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 0.85F) * var28;
               int var58 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(L_EL1, var57 * (1.0F + 0.3F * var55)), GlyphMap.computeRound2(acc, var28 * 0.2F), Math.max(var55 * 0.5F, var56)
               );
               int var59 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(L_BRD, var28 * (gsBlur > 0.01F ? 0.35F : 0.85F)),
                  GlyphMap.computeRound2(acc, var28 * Math.max(var55 * 0.7F, var56)),
                  Math.max(var55, var56)
               );
               RenderKit.Render2D.handleFloat(var51, var52, var50, var49, var59, 3.5F * this.U);
               RenderKit.Render2D.handleFloat(
                  var51 + this.max2(),
                  var52 + this.max2(),
                  var50 - this.max2() * 2.0F,
                  var49 - this.max2() * 2.0F,
                  var58,
                  Math.max(0.5F, 3.5F * this.U - this.max2())
               );
               body.handleDelegate2(
                  var48,
                  var51 + var50 / 2.0F,
                  var52 + (var49 - this.computeFloat(7.2F)) / 2.0F - 0.4F * this.U,
                  this.computeFont(7.2F),
                  GlyphMap.computeRound2(L_TXT, var28 * (0.75F + 0.25F * Math.max(var55, var56)))
               );
               float var60 = 18.0F * this.U;
               float var61 = 10.5F * this.U;
               float var62 = var51 - 7.0F * this.U - var60;
               float var63 = var136 - var61 / 2.0F;
               boolean var64 = computeFlag(acc);
               int var65 = GlyphMap.getColor(isDayMode ? 210 : 22, isDayMode ? 215 : 24, isDayMode ? 225 : 30, (int)(var28 * 255.0F));
               int var66 = GlyphMap.computeRound2(var64 && isDayMode ? -15788246 : acc, var28);
               int var67 = GlyphMap.computeInt2(var65, var66, var27);
               RenderKit.Render2D.handleFloat(var62, var63, var60, var61, var67, var61 / 2.0F);
               float var68 = var61 - 2.5F * this.U;
               float var69 = var62 + 1.25F * this.U + (var60 - var68 - 2.5F * this.U) * var27;
               float var70 = var63 + 1.25F * this.U;
               int var71 = GlyphMap.computeColor(isDayMode ? 245 : 135, (int)(var28 * 255.0F));
               int var72 = var64 && isDayMode ? GlyphMap.getColor(255, 255, 255, (int)(var28 * 255.0F)) : GlyphMap.getColor(14, 16, 22, (int)(var28 * 255.0F));
               int var73 = GlyphMap.computeInt2(var71, var72, var27);
               RenderKit.Render2D.handleFloat(var69, var70, var68, var68, var73, var68 / 2.0F);
               int var74 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(C_DIM, var28 * 0.9F), GlyphMap.computeRound2(L_TXT, var28), Math.max(var27, var31 * 0.5F)
               );
               float var75 = var136 - this.computeFloat(8.5F) / 2.0F - 0.5F * this.U;
               bold.handleDelegate(var20.getBigName(), var133, var75, this.computeFont(8.5F), var74);
               float var76 = var75 + this.computeFloat(8.5F) + 3.5F * this.U;
               float var77 = var136 + this.computeFloat(8.5F) / 2.0F;
               if (!var20.getDesc().isEmpty()) {
                  String var78 = EngHelper.computeString(var20.getDesc());
                  float var79 = this.computeDesc(body, var78, var134);
                  body.handleChar(var78, var133, var76, var134, GlyphMap.computeRound2(L_SUB, var28 * (0.8F + 0.2F * var27)), this.computeFont(7.2F));
                  var77 = var76 + var79;
               }

               boolean var137 = false;

               for (Component var80 : var20.getSettings()) {
                  if (this.visAnim(var20, var80) > 0.001F) {
                     var137 = true;
                     break;
                  }
               }

               float var139 = var77 + 11.5F * this.U;
               if (var137) {
                  float var140 = var77 + 7.0F * this.U;
                  int var81 = GlyphMap.computeRound2(isDayMode ? -15788246 : -1, var28 * (isDayMode ? 0.08F : 0.06F));
                  int var82 = GlyphMap.computeInt2(var81, GlyphMap.computeRound2(acc, var28 * 0.35F), 0.75F);
                  int var83 = GlyphMap.computeInt2(var81, var82, var27);
                  RenderKit.Render2D.handleFloat(var133, var140, var134, this.max2(), var83, this.max2() / 2.0F);
                  var139 = var140 + this.max2() + 7.0F * this.U;
               }

               int var141 = GlyphMap.computeInt2(GlyphMap.computeRound2(isDayMode ? -7035976 : -12102295, var28), acc, 0.38F + 0.62F * var27);

               for (Component var143 : var20.getSettings()) {
                  float var144 = this.visAnim(var20, var143);
                  if (!(var144 <= 0.01F)) {
                     float var84 = var28 * var144;
                     if (var143 instanceof SliderComponent var85) {
                        if (this.draggingSlider == var85) {
                           float var87 = MaxMinHelper.computeFloat4(((float)this.lastMouseX - var133) / var134, 0.0F, 1.0F);
                           float var88 = MaxMinHelper.computeFloat4(
                              MaxMinHelper.computeRound2(var85.min + (var85.max - var85.min) * var87, var85.increment), var85.min, var85.max
                           );
                           if (var85.getCachedValue() != var88) {
                              var85.withCachedValue(var88);
                           }
                        }

                        float var158 = MathHelper.clamp((var85.getCachedValue() - var85.min) / (var85.max - var85.min), 0.0F, 1.0F);
                        if (this.draggingSlider == var85) {
                           var85.getAnimation().handleAction((double)var158);
                        } else {
                           var85.getAnimation().getFinishAction();
                           var85.getAnimation().computeValueToHelper((double)var158, 0.22F, ValueHelper.applyFunction2);
                        }

                        boolean var166 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, 22.0F * this.U);
                        Animation var89 = this.chipAnim(var20.getName() + ":" + var85.getName() + ":hov");
                        var89.setDirection(var166 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var90 = var89.getEnd();
                        med.handleDelegate(
                           EngHelper.computeString(var85.getName()),
                           var133,
                           var139 + 1.5F * this.U,
                           this.computeFont(7.8F),
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.85F + 0.15F * var90) * (0.75F + 0.25F * var27))
                        );
                        String var91 = String.valueOf((double)Math.round((double)var85.getCachedValue().floatValue() * 100.0) / 100.0);
                        med.handleDelegate(
                           var91,
                           var133 + var134 - med.computeDelegate(var91, this.computeFont(7.8F)),
                           var139 + 1.5F * this.U,
                           this.computeFont(7.8F),
                           GlyphMap.computeRound2(var141, var84)
                        );
                        float var92 = var139 + 13.0F * this.U;
                        float var93 = 3.0F * this.U;
                        int var94 = GlyphMap.getColor(isDayMode ? 210 : 24, isDayMode ? 215 : 24, isDayMode ? 225 : 32, (int)(var84 * 255.0F));
                        RenderKit.Render2D.handleFloat(var133, var92, var134, var93, var94, var93 / 2.0F);
                        float var95 = var134 * (this.draggingSlider == var85 ? var158 : var85.getAnimation().getValue());
                        if (var95 > 0.0F) {
                           RenderKit.Render2D.handleFloat(var133, var92, var95, var93, GlyphMap.computeRound2(var141, var84), var93 / 2.0F);
                        }

                        float var96 = var133 + var95;
                        float var97 = var92 + var93 / 2.0F;
                        float var98 = (6.0F + 1.5F * var90) * this.U;
                        int var99 = GlyphMap.computeColor(isDayMode ? 255 : 255, (int)(var84 * (0.7F + 0.3F * var27) * 255.0F));
                        RenderKit.Render2D.handleFloat(var96 - var98 / 2.0F, var97 - var98 / 2.0F, var98, var98, var99, var98 / 2.0F);
                        var139 += this.computeOpen(var85, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof ComboComponent var145) {
                        String var86 = var145.getValue();
                        med.handleDelegate(
                           EngHelper.computeString(var145.getName()),
                           var133,
                           var139 + 2.0F * this.U,
                           this.computeFont(8.0F),
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.75F + 0.25F * var27))
                        );
                        float var159 = 0.0F;
                        float var167 = 0.0F;

                        for (String var183 : var145.list) {
                           String var190 = EngHelper.computeString(var183);
                           float var197 = body.computeDelegate(var190, this.computeFont(8.5F)) + 12.0F * this.U;
                           if (var159 + var197 > var134 && var159 > 0.0F) {
                              var159 = 0.0F;
                              var167 += 18.0F * this.U;
                           }

                           float var204 = var133 + var159;
                           float var210 = var139 + 15.0F * this.U + var167;
                           Animation var216 = this.chipAnim(var20.getName() + ":" + var145.getName() + ":" + var183);
                           var216.setDirection(var183.equals(var86) ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                           float var222 = var216.getEnd();
                           this.handleDay(body, var190, var204, var210, var197, 14.0F * this.U, var222, var84, globalAnim, var27);
                           var159 += var197 + 4.0F * this.U;
                        }

                        var139 += this.computeOpen(var145, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof CategoryComponent var146) {
                        int var152 = var146.getValues().size();
                        int var160 = 0;

                        for (CheckComponent var176 : var146.getValues()) {
                           if (var176.getCachedValue()) {
                              var160++;
                           }
                        }

                        String var169 = var160 + "/" + var152;
                        med.handleDelegate(
                           EngHelper.computeString(var146.getName()),
                           var133,
                           var139 + 2.0F * this.U,
                           this.computeFont(8.0F),
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.75F + 0.25F * var27))
                        );
                        body.handleDelegate(
                           var169,
                           var133 + var134 - body.computeDelegate(var169, this.computeFont(7.0F)),
                           var139 + 3.0F * this.U,
                           this.computeFont(7.0F),
                           GlyphMap.computeRound2(C_DIM, var84)
                        );
                        float var177 = 0.0F;
                        float var184 = 0.0F;

                        for (CheckComponent var198 : var146.getValues()) {
                           String var205 = EngHelper.computeString(var198.getName());
                           float var211 = body.computeDelegate(var205, this.computeFont(8.5F)) + 12.0F * this.U;
                           if (var177 + var211 > var134 && var177 > 0.0F) {
                              var177 = 0.0F;
                              var184 += 18.0F * this.U;
                           }

                           float var217 = var133 + var177;
                           float var223 = var139 + 15.0F * this.U + var184;
                           Animation var227 = this.chipAnim(var20.getName() + ":" + var146.getName() + ":" + var198.getName());
                           var227.setDirection(var198.getCachedValue() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                           float var230 = var227.getEnd();
                           this.handleDay(body, var205, var217, var223, var211, 14.0F * this.U, var230, var84, globalAnim, var27);
                           var177 += var211 + 4.0F * this.U;
                        }

                        var139 += this.computeOpen(var146, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof CheckComponent var147) {
                        boolean var153 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, 16.0F * this.U);
                        Animation var161 = this.chipAnim(var20.getName() + ":" + var147.getName() + ":hov");
                        var161.setDirection(var153 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var170 = var161.getEnd();
                        Animation var178 = this.chipAnim(var20.getName() + ":" + var147.getName() + ":tog");
                        var178.setDirection(var147.getCachedValue() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var185 = var178.getEnd();
                        float var192 = 20.0F * this.U;
                        float var199 = 11.0F * this.U;
                        float var206 = var133 + var134 - var192;
                        float var212 = var139 + (15.0F * this.U - var199) / 2.0F;
                        float var218 = var139 + (15.0F * this.U - this.computeFloat(8.0F)) / 2.0F - 0.4F * this.U;
                        med.handleDelegate(
                           EngHelper.computeString(var147.getName()),
                           var133,
                           var218,
                           this.computeFont(8.0F),
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.85F + 0.15F * var170) * (0.75F + 0.25F * var27))
                        );
                        int var224 = GlyphMap.getColor(
                           isDayMode ? (gsBlur < 0.05F ? 226 : 210) : 24,
                           isDayMode ? (gsBlur < 0.05F ? 232 : 215) : 24,
                           isDayMode ? (gsBlur < 0.05F ? 240 : 225) : 32,
                           (int)(var84 * 255.0F)
                        );
                        int var228 = isDayMode && gsBlur < 0.05F
                           ? GlyphMap.computeRound2(-14800581, var84)
                           : GlyphMap.computeRound2(var64 && isDayMode ? -15788246 : var141, var84);
                        int var231 = GlyphMap.computeInt2(var224, var228, var185);
                        RenderKit.Render2D.handleFloat(var206, var212, var192, var199, var231, var199 / 2.0F);
                        float var233 = var199 - 2.5F * this.U;
                        float var100 = var206 + 1.25F * this.U + (var192 - var233 - 2.5F * this.U) * var185;
                        float var101 = var212 + 1.25F * this.U;
                        int var102 = GlyphMap.computeColor(isDayMode ? 245 : 140, (int)(var84 * 255.0F));
                        int var103 = var64 && !isDayMode
                           ? GlyphMap.getColor(12, 14, 20, (int)(var84 * 255.0F))
                           : GlyphMap.getColor(255, 255, 255, (int)(var84 * (0.7F + 0.3F * var27) * 255.0F));
                        int var104 = GlyphMap.computeInt2(var102, var103, var185);
                        RenderKit.Render2D.handleFloat(var100, var101, var233, var233, var104, var233 / 2.0F);
                        if (var64 && !isDayMode && var185 > 0.01F) {
                           RenderKit.Render2D.handleFloat(
                              var100, var101, var233, var233, GlyphMap.getColor(12, 14, 20, (int)(var185 * var84 * 255.0F)), var233 / 2.0F
                           );
                        }

                        var139 += this.computeOpen(var147, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof SwitchComponent var148) {
                        boolean var154 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, 20.0F * this.U);
                        Animation var162 = this.chipAnim(var20.getName() + ":" + var148.getName() + ":btn");
                        var162.setDirection(var154 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var171 = var162.getEnd();
                        var148.valueToHelper.getFinishAction();
                        float var179 = var148.valueToHelper.getValue();
                        float var186 = Math.max(var171, var179);
                        int var193 = GlyphMap.computeInt2(GlyphMap.computeRound2(cBrdL, var84), GlyphMap.computeRound2(var141, var84), var186);
                        int var200 = GlyphMap.computeRound2(cEl1, var84);
                        RenderKit.Render2D.handleFloat(var133, var139, var134, 20.0F * this.U, var193, 4.0F * this.U);
                        RenderKit.Render2D.handleFloat(
                           var133 + this.max2(),
                           var139 + this.max2(),
                           var134 - this.max2() * 2.0F,
                           20.0F * this.U - this.max2() * 2.0F,
                           var200,
                           Math.max(0.5F, 4.0F * this.U - this.max2())
                        );
                        med.handleDelegate2(
                           EngHelper.computeString(var148.getName()),
                           var133 + var134 / 2.0F,
                           var139 + (20.0F * this.U - this.computeFloat(8.5F)) / 2.0F - 0.75F * this.U,
                           this.computeFont(8.5F),
                           GlyphMap.computeRound3(
                              GlyphMap.computeInt2(GlyphMap.computeRound2(C_WHITE, 0.9F), GlyphMap.computeRound2(var141, 0.95F), var186), var84
                           )
                        );
                        var139 += this.computeOpen(var148, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof NumberComponent var149) {
                        boolean var155 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, 16.0F * this.U);
                        Animation var163 = this.chipAnim(var20.getName() + ":" + var149.getName() + ":hov");
                        var163.setDirection(var155 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var172 = var163.getEnd();
                        boolean var180 = this.activeBind == var149;
                        Animation var187 = this.chipAnim(var20.getName() + ":" + var149.getName() + ":act");
                        var187.setDirection(var180 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var194 = var187.getEnd();
                        String var201 = var180
                           ? "..."
                           : (var149.getCachedValue() != -1 && var149.getCachedValue() != -999 ? KeyCode.keyName(var149.getCachedValue()) : "n/a");
                        float var207 = body.computeDelegate(var201, this.computeFont(7.0F)) + 11.0F * this.U;
                        med.handleVal(
                           EngHelper.computeString(var149.getName()),
                           var133,
                           var139 + 3.0F * this.U,
                           var134 - var207 - 12.0F * this.U,
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.85F + 0.15F * var172) * (0.75F + 0.25F * var27)),
                           this.computeFont(8.0F)
                        );
                        float var213 = var133 + var134 - var207;
                        float var219 = var180
                           ? 0.55F + 0.45F * (float)Math.sin((double)((float)(System.currentTimeMillis() % 1000L) / 1000.0F) * Math.PI * 2.0)
                           : 0.0F;
                        if (!is3DCapturing && gsBlur > 0.01F) {
                           TintColorHelper.handleFloat(var213, var139, var207, 15.0F * this.U, var84 * gsBlur, 2.5F * this.U, isDayMode ? -1 : -16250610);
                        }

                        int var225 = GlyphMap.computeInt2(
                           GlyphMap.computeRound2(cBrdL, var84 * (gsBlur > 0.01F ? 0.35F : 1.0F)),
                           GlyphMap.computeRound2(var141, var84 * Math.max(var194 * 0.6F, var219)),
                           Math.max(var194, var172 * 0.5F)
                        );
                        RenderKit.Render2D.handleFloat(var213, var139, var207, 15.0F * this.U, var225, 2.5F * this.U);
                        RenderKit.Render2D.handleFloat(
                           var213 + this.max2(),
                           var139 + this.max2(),
                           var207 - this.max2() * 2.0F,
                           15.0F * this.U - this.max2() * 2.0F,
                           GlyphMap.computeRound2(cEl1, var84 * (gsBlur > 0.01F ? 0.12F : 0.85F)),
                           Math.max(0.5F, 2.5F * this.U - this.max2())
                        );
                        body.handleDelegate2(
                           var201,
                           var213 + var207 / 2.0F,
                           var139 + (15.0F * this.U - this.computeFloat(7.0F)) / 2.0F - 0.4F * this.U,
                           this.computeFont(7.0F),
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.65F + 0.35F * Math.max(var194, var219)))
                        );
                        var139 += this.computeOpen(var149, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof TextFieldComponent var150) {
                        boolean var156 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, 16.0F * this.U);
                        Animation var164 = this.chipAnim(var20.getName() + ":" + var150.getName() + ":hov");
                        var164.setDirection(var156 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var173 = var164.getEnd();
                        boolean var181 = this.activeString == var150;
                        Animation var188 = this.chipAnim(var20.getName() + ":" + var150.getName() + ":str");
                        var188.setDirection(var181 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var195 = var188.getEnd();
                        String var202 = var181 ? this.stringBuffer : (var150.getValue().isEmpty() ? "..." : var150.getValue());
                        float var208 = Math.min(body.computeDelegate(var202, this.computeFont(7.0F)) + 11.0F * this.U, var134 * 0.55F);
                        med.handleVal(
                           EngHelper.computeString(var150.getName()),
                           var133,
                           var139 + 3.0F * this.U,
                           var134 - var208 - 12.0F * this.U,
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.85F + 0.15F * var173) * (0.75F + 0.25F * var27)),
                           this.computeFont(8.0F)
                        );
                        float var214 = var133 + var134 - var208;
                        if (!is3DCapturing && gsBlur > 0.01F) {
                           TintColorHelper.handleFloat(var214, var139, var208, 15.0F * this.U, var84 * gsBlur, 2.5F * this.U, isDayMode ? -1 : -16250610);
                        }

                        int var220 = GlyphMap.computeInt2(
                           GlyphMap.computeRound2(cBrdL, var84 * (gsBlur > 0.01F ? 0.35F : 1.0F)), GlyphMap.computeRound2(var141, var84), var195
                        );
                        RenderKit.Render2D.handleFloat(var214, var139, var208, 15.0F * this.U, var220, 2.5F * this.U);
                        RenderKit.Render2D.handleFloat(
                           var214 + this.max2(),
                           var139 + this.max2(),
                           var208 - this.max2() * 2.0F,
                           15.0F * this.U - this.max2() * 2.0F,
                           GlyphMap.computeRound2(cEl1, var84 * (gsBlur > 0.01F ? 0.12F : 0.85F)),
                           Math.max(0.5F, 2.5F * this.U - this.max2())
                        );
                        body.handleVal(
                           var202,
                           var214 + 5.0F * this.U,
                           var139 + (15.0F * this.U - this.computeFloat(7.0F)) / 2.0F - 0.4F * this.U,
                           var208 - 10.0F * this.U,
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.6F + 0.4F * var195)),
                           this.computeFont(7.0F)
                        );
                        var139 += this.computeOpen(var150, var134) * var144 + 5.0F * this.U * var144;
                     }

                     if (var143 instanceof ColorPickerComponent) {
                        ColorPickerComponent var151 = (ColorPickerComponent)var143;
                        Animation var157 = this.chipAnim(var20.getName() + ":" + var151.getName() + ":pop");
                        var157.setDirection(var151.pickerOpen ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var165 = var157.getEnd();
                        float var174 = this.computeOpen(var151, var134);
                        boolean var182 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var133, var139, var134, var174);
                        Animation var189 = this.chipAnim(var20.getName() + ":" + var151.getName() + ":hov");
                        var189.setDirection(var182 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
                        float var196 = var189.getEnd();
                        int var203 = var151.getCachedValue();
                        String var209 = var20.getName() + ":" + var151.getName();
                        float var215 = 5.0F * this.U;
                        float var221 = var133 + var134 - var215 * 2.0F - 1.0F * this.U;
                        float var226 = var139 + (15.0F * this.U - var215 * 2.0F) / 2.0F;
                        float var229 = var139 + (15.0F * this.U - this.computeFloat(8.0F)) / 2.0F - 0.4F * this.U;
                        med.handleVal(
                           EngHelper.computeString(var151.getName()),
                           var133,
                           var229,
                           var134 - 24.0F * this.U,
                           GlyphMap.computeRound2(C_WHITE, var84 * (0.85F + 0.15F * var196) * (0.75F + 0.25F * var27)),
                           this.computeFont(8.0F)
                        );
                        int var232 = (int)this.smooth(var209 + ":pr", (float)(var203 >> 16 & 0xFF));
                        int var234 = (int)this.smooth(var209 + ":pg", (float)(var203 >> 8 & 0xFF));
                        int var235 = (int)this.smooth(var209 + ":pb", (float)(var203 & 0xFF));
                        float var236 = var221 + var215;
                        float var237 = var226 + var215;
                        int var238 = GlyphMap.computeColor(var232, var234, var235, var84);
                        Render.ringRenderer.handleRgb(SHARED_MS, var236, var237, var215, new Color(var238, true));
                        if (var196 > 0.01F || var165 > 0.01F) {
                           Render.ringRenderer.handleRgb(SHARED_MS, var236, var237, var215 + 1.8F * this.U, 1.0F * this.U, new Color(var238, true));
                        }

                        if (var165 > 0.01F) {
                           float[] var239 = Color.RGBtoHSB(var203 >> 16 & 0xFF, var203 >> 8 & 0xFF, var203 & 0xFF, null);
                           float var105 = var133 + 7.0F * this.U;
                           float var106 = var139 + 16.0F * this.U;
                           float var107 = var134 - 14.0F * this.U;
                           float var108 = 38.0F * this.U;
                           float var109 = 4.5F * this.U;
                           byte var110 = 48;
                           float var111 = var107 / (float)var110;
                           RenderKit.Render2D.handleFloat(var105, var106, var107, var108, GlyphMap.computeRound2(cCard, var84 * var165), var109);

                           for (int var112 = 0; var112 < var110; var112++) {
                              float var113 = (float)var112 / (float)var110;
                              float var114 = (float)(var112 + 1) / (float)var110;
                              int var115 = Color.HSBtoRGB(var113, 1.0F, 1.0F);
                              int var116 = Color.HSBtoRGB(var114, 1.0F, 1.0F);
                              int var117 = Color.HSBtoRGB(var113, 1.0F, 0.08F);
                              int var118 = Color.HSBtoRGB(var114, 1.0F, 0.08F);
                              int var119 = GlyphMap.computeRound2(var115, var84 * var165);
                              int var120 = GlyphMap.computeRound2(var116, var84 * var165);
                              int var121 = GlyphMap.computeRound2(var118, var84 * var165);
                              int var122 = GlyphMap.computeRound2(var117, var84 * var165);
                              float var123 = var112 == 0 ? var109 : 0.0F;
                              float var124 = var112 == 0 ? var109 : 0.0F;
                              float var125 = var112 == var110 - 1 ? var109 : 0.0F;
                              float var126 = var112 == var110 - 1 ? var109 : 0.0F;
                              RenderKit.Render2D.handleFloat(
                                 var105 + (float)var112 * var111,
                                 var106,
                                 var111 + 0.6F * this.U,
                                 var108,
                                 new int[]{var119, var120, var121, var122},
                                 var123,
                                 var125,
                                 var126,
                                 var124
                              );
                           }

                           RenderKit.Render2D.handleFloat(
                              var105, var106, var107, var108, this.max2(), GlyphMap.computeRound2(cBrdL, var84 * var165 * 0.65F), var109
                           );
                           float var247 = var105 + var107 * var239[0];
                           float var248 = var106 + var108 * (1.0F - var239[2]);
                           float var249 = this.smooth(var209 + ":k", var247);
                           float var250 = this.smooth(var209 + ":ky", var248);
                           float var251 = 4.2F * this.U;
                           Render.ringRenderer.handleRgb(SHARED_MS, var249, var250, var251 + 1.6F * this.U, new Color(0, 0, 0, (int)(var84 * var165 * 140.0F)));
                           Render.ringRenderer
                              .handleRgb(
                                 SHARED_MS,
                                 var249,
                                 var250,
                                 var251 + 1.0F * this.U,
                                 new Color(GlyphMap.getColor(255, 255, 255, (int)(var84 * var165 * 255.0F)), true)
                              );
                           Render.ringRenderer.handleRgb(SHARED_MS, var249, var250, var251, new Color(GlyphMap.computeRound2(var203, var84 * var165), true));
                        }

                        if (var165 > 0.01F && this.draggingColor == var151) {
                           float var240 = var133 + 7.0F * this.U;
                           float var241 = var139 + 16.0F * this.U;
                           float var242 = var134 - 14.0F * this.U;
                           float var243 = 38.0F * this.U;
                           float var244 = MaxMinHelper.computeFloat4(((float)this.lastMouseX - var240) / var242, 0.0F, 1.0F);
                           float var245 = Math.max(0.04F, MaxMinHelper.computeFloat4(1.0F - ((float)this.lastMouseY - var241) / var243, 0.0F, 1.0F));
                           int var246 = Color.HSBtoRGB(var244, 1.0F, var245);
                           if (var246 != var203) {
                              var151.withCachedValue(var246);
                           }
                        }

                        var139 += this.computeOpen(var151, var134) * var144 + 5.0F * this.U * var144;
                     }
                  }
               }
            }
         }
      }

      this.maxScroll = Math.max(0.0F, Math.max(var15[0], var15[1]) - (cy - this.scrollAnim) - chh);
      RenderQueue.runGl();
   }

   public void handleMax(
      float wx,
      float wy,
      float ww,
      float wh,
      float cx,
      float cy,
      float cw,
      float chh,
      float ga,
      int acc,
      FontRegistry body,
      FontRegistry med,
      FontRegistry bold
   ) {
      MacroStore.getInstance().isInitialized();
      this.macroScrollAnim = this.macroScrollAnim + (this.macroScrollTarget - this.macroScrollAnim) * 0.25F;
      List var14 = MacroStore.getInstance().getList();
      boolean var15 = EngHelper.getInstance().check();
      if (var14.isEmpty()) {
         float var16 = cy + (chh - 40.0F * this.U) / 2.0F;
         String var17 = var15 ? "У вас еще нет макросов" : "No macros created yet";
         bold.handleDelegate2(var17, cx + cw / 2.0F, var16, this.computeFont(9.5F), GlyphMap.computeRound(isDayMode ? 20 : 255, ga));
         String var18 = var15 ? "Нажмите кнопку ниже, чтобы создать свой первый макрос" : "Click the button below to create your first macro";
         body.handleDelegate2(var18, cx + cw / 2.0F, var16 + 16.0F * this.U, this.computeFont(7.0F), GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, ga));
         float var19 = 140.0F * this.U;
         float var20 = 23.0F * this.U;
         float var21 = cx + (cw - var19) / 2.0F;
         float var22 = var16 + 36.0F * this.U;
         boolean var23 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var21, var22, var19, var20);
         Animation var24 = this.chipAnim("macro:emptybtn");
         var24.setDirection(var23 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var25 = var24.getEnd();
         int var26 = GlyphMap.computeInt2(GlyphMap.computeRound2(acc, ga * 0.88F), GlyphMap.computeRound2(acc, ga), var25);
         RenderKit.Render2D.handleFloat(var21, var22, var19, var20, var26, 5.0F * this.U);
         int var27 = computeFlag(acc) ? -15788246 : -1;
         String var28 = var15 ? "+ Создать макрос" : "+ Create Macro";
         bold.handleDelegate2(
            var28,
            var21 + var19 / 2.0F,
            var22 + (var20 - this.computeFloat(8.2F)) / 2.0F - 0.5F * this.U,
            this.computeFont(8.2F),
            GlyphMap.computeRound2(var27, ga)
         );
      } else {
         float var68 = 130.0F * this.U;
         float var70 = 20.0F * this.U;
         float var72 = cx + (cw - var68) / 2.0F;
         float var74 = cy + chh - var70 - 4.0F * this.U;
         float var76 = cy + 4.0F * this.U;
         float var78 = var74 - 6.0F * this.U;
         float var80 = var78 - var76;
         this.macroScrollTarget = MaxMinHelper.computeFloat4(this.macroScrollTarget, 0.0F, this.macroMaxScroll);
         this.macroScrollAnim = this.macroScrollAnim + (this.macroScrollTarget - this.macroScrollAnim) * 0.25F;
         RenderQueue.handleFloat(cx - 4.0F * this.U, var76 - 2.0F * this.U, cw + 8.0F * this.U, var80 + 4.0F * this.U);
         byte var82 = 2;
         float var84 = 8.0F * this.U;
         float var86 = 6.0F * this.U;
         float var88 = (cw - var84) / (float)var82;
         float var90 = 36.0F * this.U;

         for (int var92 = 0; var92 < var14.size(); var92++) {
            MacroEngine.Macro var29 = (MacroEngine.Macro)var14.get(var92);
            int var30 = var92 % var82;
            int var31 = var92 / var82;
            float var32 = cx + (float)var30 * (var88 + var84);
            float var33 = var76 - this.macroScrollAnim + (float)var31 * (var90 + var86);
            if (!(var33 + var90 < var76 - 10.0F * this.U) && !(var33 > var78 + 10.0F * this.U)) {
               boolean var34 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var32, var33, var88, var90);
               Animation var35 = this.chipAnim("mac_card:" + var29.getName());
               var35.setDirection(var34 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var36 = var35.getEnd();
               int var37 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(cCard, (0.84F + 0.16F * (1.0F - gsBlur)) * ga), GlyphMap.computeRound2(cInp, ga), var36 * 0.4F
               );
               RenderKit.Render2D.handleFloat(
                  var32,
                  var33,
                  var88,
                  var90,
                  GlyphMap.computeInt2(GlyphMap.computeRound2(cBrd, ga), GlyphMap.computeRound2(acc, ga * 0.5F), var36),
                  5.5F * this.U
               );
               RenderKit.Render2D.handleFloat(
                  var32 + this.max2(),
                  var33 + this.max2(),
                  var88 - this.max2() * 2.0F,
                  var90 - this.max2() * 2.0F,
                  var37,
                  Math.max(0.5F, 5.5F * this.U - this.max2())
               );
               bold.handleDelegate(
                  var29.getName(), var32 + 10.0F * this.U, var33 + 7.5F * this.U, this.computeFont(8.2F), GlyphMap.computeRound(isDayMode ? 20 : 255, ga)
               );
               String var38 = "";
               if (!var29.getSteps().isEmpty()) {
                  MacroEngine.MacroStep var39 = var29.getSteps().get(0);
                  if (var39.getType() == MacroEngine.ActionType.USE_COMMAND) {
                     var38 = "cmd: /" + var39.computeParams("command");
                  } else if (var39.getType() == MacroEngine.ActionType.SEND_MESSAGE) {
                     var38 = "msg: " + var39.computeParams("message");
                  } else if (var39.getType() == MacroEngine.ActionType.SWAP_SLOT_TO_ITEM) {
                     var38 = "swap: " + var39.computeParams("item");
                  } else {
                     var38 = var29.getSteps().size() + (var15 ? " действий" : " actions");
                  }
               } else {
                  var38 = var15 ? "0 действий" : "0 actions";
               }

               body.handleDelegate(
                  var38, var32 + 10.0F * this.U, var33 + 19.5F * this.U, this.computeFont(6.5F), GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, ga)
               );
               float var112 = 17.0F * this.U;
               float var40 = var33 + (var90 - var112) / 2.0F;
               float var41 = 18.0F * this.U;
               float var42 = var32 + var88 - 7.0F * this.U - var41;
               boolean var43 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var42, var40, var41, var112);
               Animation var44 = this.chipAnim("mac_del:" + var29.getName());
               var44.setDirection(var43 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var45 = var44.getEnd();
               int var46 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, ga), GlyphMap.computeRound2(cInp, ga), var45);
               RenderKit.Render2D.handleFloat(var42, var40, var41, var112, var46, 3.5F * this.U);
               int var47 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, ga), GlyphMap.computeRound(isDayMode ? 20 : 255, ga), var45
               );
               float var48 = 1.0F + 0.28F * var45;
               float var49 = var42 + var41 / 2.0F;
               float var50 = var40 + var112 / 2.0F - 0.5F * this.U;
               SHARED_MS.push();
               SHARED_MS.translate(var49, var50, 0.0F);
               SHARED_MS.scale(var48, var48, 1.0F);
               SHARED_MS.translate(-var49, -var50, 0.0F);
               IconFontRegistry.fontRegistry7.handleDelegate2("o", var49, var50 - this.computeFloat(7.5F) / 2.0F, this.computeFont(7.5F), var47);
               SHARED_MS.pop();
               String var51 = this.listeningMacro == var29
                  ? "..."
                  : (var29.getKey() != -1 && var29.getKey() != -999 ? KeyCode.keyName(var29.getKey()).replace("NONE", "n/a") : "n/a");
               float var52 = Math.max(22.0F * this.U, bold.computeDelegate(var51, this.computeFont(6.5F)) + 10.0F * this.U);
               float var53 = var42 - 5.0F * this.U - var52;
               boolean var54 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var53, var40, var52, var112);
               float var55 = 3.5F * this.U;
               if (!is3DCapturing && gsBlur > 0.01F) {
                  TintColorHelper.handleFloat(var53, var40, var52, var112, ga * gsBlur, var55, isDayMode ? -1 : -16250610);
               }

               float var56 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 0.85F) * ga;
               int var57 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(cEl1, var56), GlyphMap.computeRound2(acc, ga * 0.3F), this.listeningMacro != var29 && !var54 ? 0.0F : 1.0F
               );
               if (this.listeningMacro == var29) {
                  RenderKit.Render2D.handleFloat(var53, var40, var52, var112, GlyphMap.computeRound2(acc, ga * 0.4F), var55, 4.0F * this.U, 1.0F);
                  RenderKit.Render2D.handleFloat(var53, var40, var52, var112, GlyphMap.computeRound2(acc, ga * 0.9F), var55);
                  RenderKit.Render2D.handleFloat(
                     var53 + this.max2(),
                     var40 + this.max2(),
                     var52 - this.max2() * 2.0F,
                     var112 - this.max2() * 2.0F,
                     var57,
                     Math.max(0.5F, var55 - this.max2())
                  );
               } else {
                  int var58 = GlyphMap.computeInt2(
                     GlyphMap.computeRound2(cBrd, ga * (gsBlur > 0.01F ? 0.35F : 0.85F)), GlyphMap.computeRound2(acc, ga * 0.7F), var54 ? 1.0F : 0.0F
                  );
                  RenderKit.Render2D.handleFloat(var53, var40, var52, var112, var58, var55);
                  RenderKit.Render2D.handleFloat(
                     var53 + this.max2(),
                     var40 + this.max2(),
                     var52 - this.max2() * 2.0F,
                     var112 - this.max2() * 2.0F,
                     var57,
                     Math.max(0.5F, var55 - this.max2())
                  );
               }

               bold.handleDelegate2(
                  var51,
                  var53 + var52 / 2.0F,
                  var40 + (var112 - this.computeFloat(6.5F)) / 2.0F - 0.4F * this.U,
                  this.computeFont(6.5F),
                  this.listeningMacro == var29 ? acc : GlyphMap.computeRound2(C_DIM, ga)
               );
            }
         }

         int var93 = (var14.size() + var82 - 1) / var82;
         float var95 = (float)var93 * (var90 + var86);
         this.macroMaxScroll = Math.max(0.0F, var95 - var80);
         RenderQueue.runGl();
         boolean var97 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var72, var74, var68, var70);
         Animation var99 = this.chipAnim("macro:createbtn");
         var99.setDirection(var97 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
         float var101 = var99.getEnd();
         int var103 = GlyphMap.computeInt2(GlyphMap.computeRound2(acc, ga * 0.88F), GlyphMap.computeRound2(acc, ga), var101);
         RenderKit.Render2D.handleFloat(var72, var74, var68, var70, var103, 4.5F * this.U);
         int var104 = computeFlag(acc) ? -15788246 : -1;
         String var106 = var15 ? "+ Создать макрос" : "+ Create Macro";
         bold.handleDelegate2(
            var106,
            var72 + var68 / 2.0F,
            var74 + (var70 - this.computeFloat(7.8F)) / 2.0F - 0.5F * this.U,
            this.computeFont(7.8F),
            GlyphMap.computeRound2(var104, ga)
         );
      }

      this.animCreateMacroModal.setDirection(this.typingMacroModal ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var69 = this.animCreateMacroModal.getEnd();
      if (var69 > 0.01F) {
         float var71 = 276.0F * this.U;
         float var73 = 154.0F * this.U;
         float var75 = wx + (ww - var71) / 2.0F;
         float var77 = wy + (wh - var73) / 2.0F - (1.0F - var69) * 15.0F * this.U;
         RenderKit.Render2D.handleFloat(wx, wy, ww, wh, GlyphMap.computeRound2(0, var69 * 0.65F), 0.0F);
         int var79 = GlyphMap.computeRound2(isDayMode ? -1 : cCard, var69);
         RenderKit.Render2D.handleFloat(var75, var77, var71, var73, GlyphMap.computeRound2(cBrd, var69), this.max());
         RenderKit.Render2D.handleFloat(
            var75 + this.max2(), var77 + this.max2(), var71 - this.max2() * 2.0F, var73 - this.max2() * 2.0F, var79, Math.max(0.5F, this.max() - this.max2())
         );
         String var81 = var15 ? "Создать новый макрос" : "Create New Macro";
         bold.handleDelegate(var81, var75 + 16.0F * this.U, var77 + 12.0F * this.U, this.computeFont(9.2F), GlyphMap.computeRound(isDayMode ? 20 : 255, var69));
         float var83 = var71 - 32.0F * this.U;
         float var85 = 20.0F * this.U;
         float var87 = var75 + 16.0F * this.U;
         float var89 = var77 + 30.0F * this.U;
         boolean var91 = this.macroModalFocusField == 0;
         int var94 = GlyphMap.computeRound2(cInp, var69);
         RenderKit.Render2D.handleFloat(var87, var89, var83, var85, GlyphMap.computeRound2(var91 ? acc : cBrd, var69 * 0.8F), 4.0F * this.U);
         RenderKit.Render2D.handleFloat(
            var87 + this.max2(),
            var89 + this.max2(),
            var83 - this.max2() * 2.0F,
            var85 - this.max2() * 2.0F,
            var94,
            Math.max(0.5F, 4.0F * this.U - this.max2())
         );
         String var96 = this.newMacroNameBuffer.isEmpty() ? (var15 ? "Название макроса..." : "Macro name...") : this.newMacroNameBuffer;
         int var98 = this.newMacroNameBuffer.isEmpty() ? GlyphMap.computeRound2(C_DIM, var69) : GlyphMap.computeRound(isDayMode ? 20 : 255, var69);
         String var100 = var91 && System.currentTimeMillis() / 450L % 2L == 0L ? "▌" : "";
         med.handleDelegate(
            var96 + (this.newMacroNameBuffer.isEmpty() ? "" : var100),
            var87 + 7.0F * this.U,
            var89 + (var85 - this.computeFloat(7.2F)) / 2.0F,
            this.computeFont(7.2F),
            var98
         );
         float var102 = var89 + var85 + 8.0F * this.U;
         float var105 = 20.0F * this.U;
         float var107 = 2.0F * this.U;
         float var108 = (var83 - var107 * 2.0F) / 3.0F;
         float var109 = var105 - var107 * 2.0F;
         RenderKit.Render2D.handleFloat(var87, var102, var83, var105, GlyphMap.computeRound2(cInp, var69), 4.5F * this.U);
         float var111 = this.smooth("macro:type_slide", (float)this.newMacroActionType);
         float var113 = var87 + var107 + var111 * var108;
         float var114 = var102 + var107;
         RenderKit.Render2D.handleFloat(var113, var114, var108, var109, GlyphMap.computeRound2(acc, var69 * 0.9F), 3.5F * this.U);
         String[] var115 = var15 ? new String[]{"Команда", "Сообщение", "Свап"} : new String[]{"Command", "Message", "Swap"};

         for (int var116 = 0; var116 < 3; var116++) {
            float var118 = var87 + var107 + (float)var116 * var108 + var108 / 2.0F;
            float var120 = var102 + (var105 - this.computeFloat(6.8F)) / 2.0F - 0.5F * this.U;
            float var122 = Math.max(0.0F, 1.0F - Math.abs(var111 - (float)var116));
            int var124 = computeFlag(acc) ? -15788246 : -1;
            int var126 = GlyphMap.computeRound(isDayMode ? 20 : 255, var69 * 0.75F);
            int var128 = GlyphMap.computeInt2(var126, var124, var122);
            bold.handleDelegate2(var115[var116], var118, var120, this.computeFont(6.8F), var128);
         }

         float var117 = var102 + var105 + 8.0F * this.U;
         String var119;
         if (this.newMacroActionType == 0) {
            var119 = var15 ? "Команда (/spawn, /home)..." : "Command (/spawn, /home)...";
         } else if (this.newMacroActionType == 1) {
            var119 = var15 ? "Текст сообщения в чат..." : "Chat message text...";
         } else {
            var119 = var15 ? "Предмет (totem_of_undying, ender_pearl)..." : "Item (totem_of_undying, ender_pearl)...";
         }

         boolean var121 = this.macroModalFocusField == 1;
         RenderKit.Render2D.handleFloat(var87, var117, var83, var85, GlyphMap.computeRound2(var121 ? acc : cBrd, var69 * 0.8F), 4.0F * this.U);
         RenderKit.Render2D.handleFloat(
            var87 + this.max2(),
            var117 + this.max2(),
            var83 - this.max2() * 2.0F,
            var85 - this.max2() * 2.0F,
            var94,
            Math.max(0.5F, 4.0F * this.U - this.max2())
         );
         String var123 = this.newMacroParamBuffer.isEmpty() ? var119 : this.newMacroParamBuffer;
         int var125 = this.newMacroParamBuffer.isEmpty() ? GlyphMap.computeRound2(C_DIM, var69) : GlyphMap.computeRound(isDayMode ? 20 : 255, var69);
         String var127 = var121 && System.currentTimeMillis() / 450L % 2L == 0L ? "▌" : "";
         med.handleDelegate(
            var123 + (this.newMacroParamBuffer.isEmpty() ? "" : var127),
            var87 + 7.0F * this.U,
            var117 + (var85 - this.computeFloat(7.2F)) / 2.0F,
            this.computeFont(7.2F),
            var125
         );
         float var129 = var117 + var85 + 12.0F * this.U;
         float var130 = 22.0F * this.U;
         float var131 = 5.0F * this.U;
         String var132 = this.newMacroListeningKey
            ? "..."
            : (this.newMacroKey != -1 && this.newMacroKey != -999 ? KeyCode.keyName(this.newMacroKey).replace("NONE", "n/a") : "n/a");
         String var133 = (var15 ? "Бинд: " : "Key: ") + var132;
         float var134 = Math.max(54.0F * this.U, bold.computeDelegate(var133, this.computeFont(7.2F)) + 16.0F * this.U);
         boolean var135 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var87, var129, var134, var130);
         float var136 = (gsBlur > 0.01F ? (isDayMode ? 0.16F : 0.1F) : 0.85F) * var69;
         int var137 = GlyphMap.computeInt2(
            GlyphMap.computeRound2(cEl1, var136), GlyphMap.computeRound2(acc, var69 * 0.22F), !this.newMacroListeningKey && !var135 ? 0.0F : 1.0F
         );
         int var138 = GlyphMap.computeRound2(
            this.newMacroListeningKey ? acc : (var135 ? GlyphMap.computeInt2(cBrd, acc, 0.6F) : cBrd), var69 * (gsBlur > 0.01F ? 0.35F : 0.9F)
         );
         if (this.newMacroListeningKey) {
            RenderKit.Render2D.handleFloat(var87, var129, var134, var130, GlyphMap.computeRound2(acc, var69 * 0.45F), var131, 6.0F * this.U, 1.0F);
         }

         if (!is3DCapturing && gsBlur > 0.01F) {
            TintColorHelper.handleFloat(var87, var129, var134, var130, var69 * gsBlur, var131, isDayMode ? -1 : -16250610);
         }

         RenderKit.Render2D.handleFloat(var87, var129, var134, var130, var138, var131);
         RenderKit.Render2D.handleFloat(
            var87 + this.max2(), var129 + this.max2(), var134 - this.max2() * 2.0F, var130 - this.max2() * 2.0F, var137, Math.max(0.5F, var131 - this.max2())
         );
         bold.handleDelegate2(
            var133,
            var87 + var134 / 2.0F,
            var129 + (var130 - this.computeFloat(7.2F)) / 2.0F - 0.5F * this.U,
            this.computeFont(7.2F),
            this.newMacroListeningKey ? acc : GlyphMap.computeRound(isDayMode ? 20 : 255, var69)
         );
         float var139 = var83 - var134 - 8.0F * this.U;
         float var59 = (var139 - 6.0F * this.U) / 2.0F;
         float var60 = var87 + var134 + 8.0F * this.U;
         float var61 = var60 + var59 + 6.0F * this.U;
         boolean var62 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var60, var129, var59, var130);
         int var63 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, var69), GlyphMap.computeRound2(cInp, var69), var62 ? 1.0F : 0.0F);
         RenderKit.Render2D.handleFloat(var60, var129, var59, var130, var63, 4.5F * this.U);
         String var64 = var15 ? "Отмена" : "Cancel";
         bold.handleDelegate2(
            var64,
            var60 + var59 / 2.0F,
            var129 + (var130 - this.computeFloat(7.2F)) / 2.0F - 0.6F * this.U,
            this.computeFont(7.2F),
            GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, var69)
         );
         boolean var65 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var61, var129, var59, var130);
         int var66 = GlyphMap.computeInt2(GlyphMap.computeRound2(acc, var69 * 0.88F), GlyphMap.computeRound2(acc, var69), var65 ? 1.0F : 0.0F);
         RenderKit.Render2D.handleFloat(var61, var129, var59, var130, var66, 4.5F * this.U);
         String var67 = var15 ? "Создать" : "Create";
         bold.handleDelegate2(
            var67,
            var61 + var59 / 2.0F,
            var129 + (var130 - this.computeFloat(7.2F)) / 2.0F - 0.6F * this.U,
            this.computeFont(7.2F),
            GlyphMap.computeRound2(computeFlag(acc) ? -15788246 : -1, var69)
         );
      }
   }

   public void initConfig(
      float wx,
      float wy,
      float ww,
      float wh,
      float cx,
      float cy,
      float cw,
      float chh,
      float ga,
      int acc,
      FontRegistry body,
      FontRegistry med,
      FontRegistry bold
   ) {
      float var14 = cy;
      float var16 = 160.0F * this.U;
      boolean var20 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, cx, cy, var16, chh) && !this.typingConfigName;
      Animation var21 = this.chipAnim("cfg:createhov");
      var21.setDirection(var20 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var22 = var21.getEnd();
      int var23 = GlyphMap.computeRound2(isDayMode ? -1 : cCard, (0.84F + 0.16F * (1.0F - gsBlur)) * ga);
      int var24 = GlyphMap.computeInt2(var23, GlyphMap.computeRound2(cInp, ga), var22 * 0.5F);
      RenderKit.Render2D.handleFloat(cx, cy, var16, chh, var24, this.max());
      int var25 = isDayMode ? -7035976 : -12102295;
      int var26 = GlyphMap.computeRound3(GlyphMap.computeInt2(var25, acc, Math.max(var22, this.typingConfigName ? 1.0F : 0.0F)), ga);
      this.handleMin(cx, cy, var16, chh, this.max(), var26, 1.4F * this.U);
      float var27 = 16.0F * this.U;
      float var28 = cx + var16 / 2.0F;
      float var29 = cy + chh / 2.0F - 16.0F * this.U;
      int var30 = GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, ga), GlyphMap.computeRound2(acc, ga * 0.35F), var22);
      Render.ringRenderer.handleRgb(SHARED_MS, var28, var29, var27, new Color(var30, true));
      bold.handleDelegate2(
         "+",
         var28,
         var29 - this.computeFloat(12.0F) / 2.0F - 1.0F * this.U,
         this.computeFont(12.0F),
         GlyphMap.computeRound3(GlyphMap.computeInt2(GlyphMap.computeColor(isDayMode ? 20 : 255, 230), GlyphMap.computeRound2(acc, 1.0F), var22), ga)
      );
      int var31 = GlyphMap.computeInt2(GlyphMap.computeRound(isDayMode ? 20 : 255, ga), GlyphMap.computeRound2(acc, ga), var22 * 0.6F);
      bold.handleDelegate2(EngHelper.computeString("Create Config"), var28, var29 + var27 + 13.0F * this.U, this.computeFont(9.0F), var31);
      body.handleDelegate2(
         EngHelper.computeString("Save current setup"),
         var28,
         var29 + var27 + 26.0F * this.U,
         this.computeFont(6.8F),
         GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, ga)
      );
      body.handleDelegate2(
         EngHelper.computeString("into a local .gone file"),
         var28,
         var29 + var27 + 36.0F * this.U,
         this.computeFont(6.8F),
         GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, ga)
      );
      float var32 = cx + var16 + 12.0F * this.U;
      float var33 = cw - var16 - 12.0F * this.U;
      float var34 = chh;
      this.configScrollTarget = MaxMinHelper.computeFloat4(this.configScrollTarget, 0.0F, this.configMaxScroll);
      this.configScrollAnim = this.configScrollAnim + (this.configScrollTarget - this.configScrollAnim) * 0.25F;
      RenderQueue.handleFloat(var32 - 4.0F * this.U, cy - 2.0F * this.U, var33 + 8.0F * this.U, chh + 4.0F * this.U);
      float var35 = cy - this.configScrollAnim;
      List var36 = ConfigManager.getInstance().getConfigsNames();
      if (var36.isEmpty()) {
         body.handleDelegate2(
            EngHelper.computeString("No local configs found. Create one on the left!"),
            var32 + var33 / 2.0F,
            cy + chh / 2.0F - 10.0F * this.U,
            this.computeFont(8.0F),
            GlyphMap.computeRound2(C_DIM, ga)
         );
      } else {
         for (String var38 : (Iterable<String>)(Iterable)var36) {
            float var39 = 36.0F * this.U;
            float var40 = var35;
            var35 += var39 + 6.0F * this.U;
            if (!(var40 + var39 < var14 - 10.0F * this.U) && !(var40 > var14 + var34 + 10.0F * this.U)) {
               Animation var41 = this.chipAnim("cfg_entry:" + var38);
               float var42 = var41.getEnd();
               float var43 = (1.0F - var42) * -16.0F * this.U;
               float var44 = ga * var42;
               boolean var45 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var32, var40, var33, var39);
               Animation var46 = this.chipAnim("cfg:" + var38);
               var46.setDirection(var45 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var47 = var46.getEnd();
               SHARED_MS.push();
               SHARED_MS.translate(var43, 0.0F, 0.0F);
               int var48 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(cCard, (0.84F + 0.16F * (1.0F - gsBlur)) * var44), GlyphMap.computeRound2(cInp, var44), var47 * 0.4F
               );
               RenderKit.Render2D.handleFloat(
                  var32,
                  var40,
                  var33,
                  var39,
                  GlyphMap.computeInt2(GlyphMap.computeRound2(cBrd, var44), GlyphMap.computeRound2(acc, var44 * 0.5F), var47),
                  5.5F * this.U
               );
               RenderKit.Render2D.handleFloat(
                  var32 + this.max2(),
                  var40 + this.max2(),
                  var33 - this.max2() * 2.0F,
                  var39 - this.max2() * 2.0F,
                  var48,
                  Math.max(0.5F, 5.5F * this.U - this.max2())
               );
               bold.handleDelegate(
                  var38 + ".gone", var32 + 12.0F * this.U, var40 + 7.5F * this.U, this.computeFont(8.2F), GlyphMap.computeRound(isDayMode ? 20 : 255, var44)
               );
               body.handleDelegate(
                  "Local Storage",
                  var32 + 12.0F * this.U,
                  var40 + 19.5F * this.U,
                  this.computeFont(6.5F),
                  GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, var44)
               );
               float var49 = 17.0F * this.U;
               float var50 = var40 + (var39 - var49) / 2.0F;
               float var51 = 18.0F * this.U;
               float var52 = var32 + var33 - 8.0F * this.U - var51;
               boolean var53 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var52, var50, var51, var49);
               Animation var54 = this.chipAnim("cfg_del:" + var38);
               var54.setDirection(var53 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
               float var55 = var54.getEnd();
               int var56 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, var44), GlyphMap.computeRound2(cInp, var44), var55);
               RenderKit.Render2D.handleFloat(var52, var50, var51, var49, var56, 3.5F * this.U);
               int var57 = GlyphMap.computeInt2(
                  GlyphMap.computeRound2(isDayMode ? -10193781 : C_DIM, var44), GlyphMap.computeRound(isDayMode ? 20 : 255, var44), var55
               );
               float var58 = 1.0F + 0.28F * var55;
               float var59 = var52 + var51 / 2.0F;
               float var60 = var50 + var49 / 2.0F - 0.5F * this.U;
               SHARED_MS.push();
               SHARED_MS.translate(var59, var60, 0.0F);
               SHARED_MS.scale(var58, var58, 1.0F);
               SHARED_MS.translate(-var59, -var60, 0.0F);
               IconFontRegistry.fontRegistry7.handleDelegate2("o", var59, var60 - this.computeFloat(7.5F) / 2.0F, this.computeFont(7.5F), var57);
               SHARED_MS.pop();
               String var61 = EngHelper.computeString("Save");
               float var62 = bold.computeDelegate(var61, this.computeFont(6.8F));
               float var63 = Math.max(36.0F * this.U, var62 + 12.0F * this.U);
               float var64 = var52 - 5.0F * this.U - var63;
               boolean var65 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var64, var50, var63, var49);
               int var66 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, var44), GlyphMap.computeRound2(cInp, var44), var65 ? 1.0F : 0.0F);
               RenderKit.Render2D.handleFloat(var64, var50, var63, var49, var66, 3.5F * this.U);
               bold.handleDelegate2(
                  var61,
                  var64 + var63 / 2.0F,
                  var50 + (var49 - this.computeFloat(6.8F)) / 2.0F - 0.6F * this.U,
                  this.computeFont(6.8F),
                  GlyphMap.computeRound2(C_DIM, var44)
               );
               String var67 = EngHelper.computeString("Load");
               float var68 = bold.computeDelegate(var67, this.computeFont(6.8F));
               float var69 = Math.max(38.0F * this.U, var68 + 12.0F * this.U);
               float var70 = var64 - 5.0F * this.U - var69;
               boolean var71 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var70, var50, var69, var49);
               int var72 = GlyphMap.computeInt2(GlyphMap.computeRound2(acc, var44 * 0.85F), GlyphMap.computeRound2(acc, var44 * 1.0F), var71 ? 1.0F : 0.0F);
               RenderKit.Render2D.handleFloat(var70, var50, var69, var49, var72, 3.5F * this.U);
               int var73 = computeFlag(acc) ? -15788246 : -1;
               bold.handleDelegate2(
                  var67,
                  var70 + var69 / 2.0F,
                  var50 + (var49 - this.computeFloat(6.8F)) / 2.0F - 0.6F * this.U,
                  this.computeFont(6.8F),
                  GlyphMap.computeRound2(var73, var44)
               );
               SHARED_MS.pop();
            }
         }
      }

      this.configMaxScroll = Math.max(0.0F, var35 + this.configScrollAnim - var14 - var34);
      RenderQueue.runGl();
      this.animCreateConfigModal.setDirection(this.typingConfigName ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var74 = this.animCreateConfigModal.getEnd();
      if (var74 > 0.01F) {
         float var75 = 195.0F * this.U;
         float var76 = 92.0F * this.U;
         float var77 = wx + (ww - var75) / 2.0F;
         float var78 = wy + (wh - var76) / 2.0F;
         float var79 = ga * var74;
         RenderKit.Render2D.handleFloat(wx, wy, ww, wh, GlyphMap.computeRound2(-16777216, var79 * 0.7F), this.max());
         float var80 = Math.max(0.01F, 0.88F + 0.12F * var74);
         float var81 = (1.0F - var74) * 12.0F * this.U;
         SHARED_MS.push();
         SHARED_MS.translate(var77 + var75 / 2.0F, var78 + var76 / 2.0F + var81, 0.0F);
         SHARED_MS.scale(var80, var80, 1.0F);
         SHARED_MS.translate(-(var77 + var75 / 2.0F), -(var78 + var76 / 2.0F), 0.0F);
         RenderKit.Render2D.handleFloat(var77, var78, var75, var76, GlyphMap.computeRound2(cBrd, var79), this.max());
         RenderKit.Render2D.handleFloat(
            var77 + this.max2(),
            var78 + this.max2(),
            var75 - this.max2() * 2.0F,
            var76 - this.max2() * 2.0F,
            GlyphMap.computeRound2(cCard, var79 * 0.98F),
            Math.max(0.5F, this.max() - this.max2())
         );
         bold.handleDelegate2(
            EngHelper.computeString("Create New Config"),
            var77 + var75 / 2.0F,
            var78 + 12.0F * this.U,
            this.computeFont(9.5F),
            GlyphMap.computeRound(isDayMode ? 20 : 255, var79)
         );
         float var82 = var75 - 24.0F * this.U;
         float var83 = 21.0F * this.U;
         float var84 = var77 + 12.0F * this.U;
         float var85 = var78 + 30.0F * this.U;
         RenderKit.Render2D.handleFloat(var84, var85, var82, var83, GlyphMap.computeRound2(acc, var79), 4.5F * this.U);
         RenderKit.Render2D.handleFloat(
            var84 + this.max2(),
            var85 + this.max2(),
            var82 - this.max2() * 2.0F,
            var83 - this.max2() * 2.0F,
            GlyphMap.computeRound2(cInp, var79),
            4.0F * this.U
         );
         String var86 = this.newConfigBuffer.isEmpty() ? EngHelper.computeString("Configuration name...") : this.newConfigBuffer;
         int var87 = this.newConfigBuffer.isEmpty() ? GlyphMap.computeRound2(C_DIM, var79) : GlyphMap.computeRound(isDayMode ? 20 : 255, var79);
         med.handleVal(var86, var84 + 8.0F * this.U, var85 + (var83 - this.computeFloat(8.0F)) / 2.0F, var82 - 16.0F * this.U, var87, this.computeFont(8.0F));
         if (System.currentTimeMillis() / 500L % 2L == 0L) {
            float var88 = var84 + 8.0F * this.U + med.computeDelegate(this.newConfigBuffer, this.computeFont(8.0F)) + 1.5F * this.U;
            RenderKit.Render2D.handleFloat(
               var88, var85 + 4.0F * this.U, 0.9F * this.U, var83 - 8.0F * this.U, GlyphMap.computeRound2(C_WHITE, var79), 0.45F * this.U
            );
         }

         float var89 = (var82 - 8.0F * this.U) / 2.0F;
         float var90 = 18.0F * this.U;
         float var91 = var78 + 60.0F * this.U;
         boolean var92 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var84, var91, var89, var90);
         RenderKit.Render2D.handleFloat(
            var84,
            var91,
            var89,
            var90,
            GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, var79), GlyphMap.computeRound2(cEl2, var79), var92 ? 1.0F : 0.0F),
            4.0F * this.U
         );
         bold.handleDelegate2(
            EngHelper.computeString("Cancel"),
            var84 + var89 / 2.0F,
            var91 + (var90 - this.computeFloat(7.5F)) / 2.0F - 0.5F * this.U,
            this.computeFont(7.5F),
            GlyphMap.computeRound2(C_DIM, var79)
         );
         boolean var93 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var84 + var89 + 8.0F * this.U, var91, var89, var90);
         RenderKit.Render2D.handleFloat(
            var84 + var89 + 8.0F * this.U,
            var91,
            var89,
            var90,
            GlyphMap.computeInt2(GlyphMap.computeRound2(acc, var79 * 0.9F), GlyphMap.computeRound2(acc, var79), var93 ? 1.0F : 0.0F),
            4.0F * this.U
         );
         int var94 = computeFlag(acc) ? -15788246 : -1;
         bold.handleDelegate2(
            EngHelper.computeString("Create"),
            var84 + var89 + 8.0F * this.U + var89 / 2.0F,
            var91 + (var90 - this.computeFloat(7.5F)) / 2.0F - 0.5F * this.U,
            this.computeFont(7.5F),
            GlyphMap.computeRound2(var94, var79)
         );
         SHARED_MS.pop();
      }
   }

   public void handleMs(float x1, float y1, float x2, float y2, float thickness, int color) {
      float var7 = x2 - x1;
      float var8 = y2 - y1;
      float var9 = (float)Math.hypot((double)var7, (double)var8);
      if (!(var9 < 0.2F)) {
         float var10 = (float)Math.toDegrees(Math.atan2((double)var8, (double)var7));
         SHARED_MS.push();
         SHARED_MS.translate(x1, y1, 0.0F);
         SHARED_MS.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var10));
         RenderKit.Render2D.handleFloat(0.0F, -thickness / 2.0F, var9, thickness, color, thickness / 2.0F);
         SHARED_MS.pop();
      }
   }

   public void handleMax(FontRegistry font, String text, float x, float y, float maxW, float size, int color) {
      if (text != null && !text.isEmpty()) {
         float var8 = font.computeDelegate(text, size);
         if (var8 <= maxW) {
            font.handleDelegate(text, x, y, size, color);
         } else {
            float var9 = Math.max(16.0F, 24.0F * this.U);
            float var10 = var8 + var9;
            float var11 = Math.max(15.0F, 24.0F * this.U);
            float var12 = 1400.0F;
            float var13 = var10 / var11 * 1000.0F;
            float var14 = var12 + var13;
            long var15 = System.currentTimeMillis();
            float var17 = (float)(var15 % (long)var14);
            float var18;
            if (var17 < var12) {
               var18 = 0.0F;
            } else {
               var18 = (var17 - var12) / var13 * var10;
            }

            RenderQueue.handleFloat(x, y - 2.0F * this.U, maxW, font.computeDelegate(size) + 4.0F * this.U);

            for (float var19 = x - var18; var19 < x + maxW; var19 += var10) {
               if (var19 + var8 > x) {
                  font.handleDelegate(text, var19, y, size, color);
               }
            }

            RenderQueue.runGl();
         }
      }
   }

   public void handleFormat(
      float x,
      float y,
      float w,
      float h,
      float cx,
      float cy,
      float cw,
      float chh,
      float ga,
      int acc,
      FontRegistry body,
      FontRegistry med,
      FontRegistry bold,
      FontRegistry icons
   ) {
      RotationProfileManager var15 = RotationProfileManager.getInstance();
      boolean var16 = EngHelper.getInstance().check();
      float var17 = cw * 0.52F;
      float var18 = cw - var17 - this.getGs2();
      float var19 = (chh - this.getGs2()) * 0.52F;
      float var20 = chh - var19 - this.getGs2();
      RenderKit.Render2D.handleFloat(cx, cy, var17, var19, GlyphMap.computeRound2(cBrd, ga), 6.0F * this.U);
      RenderKit.Render2D.handleFloat(
         cx + this.max2(), cy + this.max2(), var17 - this.max2() * 2.0F, var19 - this.max2() * 2.0F, GlyphMap.computeRound2(cCard, ga), 5.5F * this.U
      );
      float var25 = 38.0F * this.U;
      float var26 = 44.0F * this.U;
      float var27 = 14.0F * this.U;
      float var28 = cx + var17 - 10.0F * this.U - var26;
      float var29 = var28 - 5.0F * this.U - var25;
      float var30 = cy + 8.0F * this.U;
      float var31 = var29 - 10.0F * this.U - (cx + 12.0F * this.U);
      this.handleMax(
         bold,
         var16 ? "Кривая скорости наводки (Spline)" : "Aim Velocity Spline",
         cx + 12.0F * this.U,
         cy + 10.0F * this.U,
         var31,
         this.computeFont(8.5F),
         GlyphMap.computeRound2(C_WHITE, ga)
      );
      this.handleMax(
         body,
         var16 ? "Фазы ускорения и торможения" : "Acceleration & Deceleration Curves",
         cx + 12.0F * this.U,
         cy + 21.0F * this.U,
         var31,
         this.computeFont(6.8F),
         GlyphMap.computeRound2(C_DIM, ga)
      );
      boolean var32 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var28, var30, var26, var27);
      RenderKit.Render2D.handleFloat(
         var28,
         var30,
         var26,
         var27,
         GlyphMap.computeInt2(GlyphMap.computeRound2(acc, ga * 0.75F), GlyphMap.computeRound2(acc, ga), var32 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      int var33 = computeFlag(acc) ? -15788246 : -1;
      bold.handleDelegate2(
         var16 ? "+ Точка" : "+ Point",
         var28 + var26 / 2.0F,
         var30 + (var27 - this.computeFloat(6.8F)) / 2.0F - 0.6F * this.U,
         this.computeFont(6.8F),
         GlyphMap.computeRound2(var33, ga)
      );
      boolean var34 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var29, var30, var25, var27);
      RenderKit.Render2D.handleFloat(
         var29,
         var30,
         var25,
         var27,
         GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, ga), GlyphMap.computeRound2(cEl1, ga), var34 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      med.handleDelegate2(
         var16 ? "Сброс" : "Reset",
         var29 + var25 / 2.0F,
         var30 + (var27 - this.computeFloat(6.8F)) / 2.0F - 0.6F * this.U,
         this.computeFont(6.8F),
         GlyphMap.computeRound2(C_DIM, ga)
      );
      float var35 = cx + 12.0F * this.U;
      float var36 = cy + 34.0F * this.U;
      float var37 = var17 - 24.0F * this.U;
      float var38 = var19 - 44.0F * this.U;
      RenderKit.Render2D.handleFloat(var35, var36, var37, var38, GlyphMap.computeRound2(cInp, ga * 0.6F), 4.0F * this.U);

      for (int var39 = 1; var39 <= 3; var39++) {
         float var40 = var36 + var38 * (float)var39 / 4.0F;
         RenderKit.Render2D.handleFloat(var35 + 2.0F * this.U, var40, var37 - 4.0F * this.U, 0.8F * this.U, GlyphMap.computeRound2(cBrd, ga * 0.4F), 0.0F);
         float var41 = 2.5F * (1.0F - (float)var39 / 4.0F);
         body.handleDelegate(
            String.format("%.1fx", var41), var35 + 4.0F * this.U, var40 - 6.0F * this.U, this.computeFont(5.8F), GlyphMap.computeRound2(C_DIM, ga * 0.6F)
         );
      }

      byte var122 = 48;
      float var123 = var35;
      float var124 = var36 + var38 - MathHelper.clamp(var15.computeCurve(0.0F) / 2.5F * var38, 0.0F, var38);

      for (int var42 = 1; var42 <= var122; var42++) {
         float var43 = (float)var42 / (float)var122;
         float var44 = var35 + var43 * var37;
         float var45 = var36 + var38 - MathHelper.clamp(var15.computeCurve(var43) / 2.5F * var38, 0.0F, var38);
         this.handleMs(var123, var124, var44, var45, 2.2F * this.U, GlyphMap.computeRound2(acc, ga * 0.95F));
         var123 = var44;
         var124 = var45;
      }

      float var125 = (float)(System.currentTimeMillis() % 1200L) / 1200.0F;
      float var126 = var35 + var125 * var37;
      RenderKit.Render2D.handleFloat(var126, var36, 1.2F * this.U, var38, GlyphMap.computeRound2(acc, ga * 0.45F), 0.5F * this.U);
      List var127 = var15.getCurvePoints();

      for (int var128 = 0; var128 < var127.size(); var128++) {
         RotationProfileManager.CurvePoint var46 = (RotationProfileManager.CurvePoint)var127.get(var128);
         float var47 = var35 + var46.time * var37;
         float var48 = var36 + var38 - MathHelper.clamp(var46.speed / 2.5F * var38, 0.0F, var38);
         boolean var49 = MaxMinHelper.computeFlag(
            (float)this.lastMouseX, (float)this.lastMouseY, var47 - 5.0F * this.U, var48 - 5.0F * this.U, 10.0F * this.U, 10.0F * this.U
         );
         boolean var50 = draggingCurvePointIndex == var128;
         float var51 = !var50 && !var49 ? 3.2F * this.U : 4.5F * this.U;
         int var52 = var50 ? C_WHITE : acc;
         RenderKit.Render2D.handleFloat(var47 - var51, var48 - var51, var51 * 2.0F, var51 * 2.0F, GlyphMap.computeRound2(var52, ga), var51);
         if (var49 || var50) {
            String var53 = String.format("%.0f%% | %.2fx", var46.time * 100.0F, var46.speed);
            float var54 = bold.computeDelegate(var53, this.computeFont(6.2F)) + 6.0F * this.U;
            RenderKit.Render2D.handleFloat(
               var47 - var54 / 2.0F, var48 - 14.0F * this.U, var54, 10.0F * this.U, GlyphMap.computeRound2(cEl1, ga * 0.95F), 2.5F * this.U
            );
            bold.handleDelegate2(var53, var47, var48 - 12.5F * this.U, this.computeFont(6.2F), GlyphMap.computeRound2(C_WHITE, ga));
         }
      }

      float var129 = cy + var19 + this.getGs2();
      RenderKit.Render2D.handleFloat(cx, var129, var17, var20, GlyphMap.computeRound2(cBrd, ga), 6.0F * this.U);
      RenderKit.Render2D.handleFloat(
         cx + this.max2(), var129 + this.max2(), var17 - this.max2() * 2.0F, var20 - this.max2() * 2.0F, GlyphMap.computeRound2(cCard, ga), 5.5F * this.U
      );
      float var130 = 82.0F * this.U;
      float var131 = 14.0F * this.U;
      float var132 = 72.0F * this.U;
      float var133 = 14.0F * this.U;
      float var134 = cx + var17 - 10.0F * this.U - var130;
      float var135 = var134 - 5.0F * this.U - var132;
      float var55 = var129 + 8.0F * this.U;
      float var56 = var135 - 10.0F * this.U - (cx + 12.0F * this.U);
      this.handleMax(
         bold,
         var16 ? "Динамика и настройки шума" : "Dynamics & Noise Profile",
         cx + 12.0F * this.U,
         var129 + 10.0F * this.U,
         var56,
         this.computeFont(8.5F),
         GlyphMap.computeRound2(C_WHITE, ga)
      );
      boolean var57 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var135, var55, var132, var133);
      RenderKit.Render2D.handleFloat(
         var135,
         var55,
         var132,
         var133,
         GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, ga), GlyphMap.computeRound2(cEl1, ga), var57 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      bold.handleDelegate2(
         "< " + var15.getEasingType().name().replace("EASE_", "").replace("OUT_", "").replace("IN_", "") + " >",
         var135 + var132 / 2.0F,
         var55 + (var133 - this.computeFloat(5.8F)) / 2.0F,
         this.computeFont(5.8F),
         GlyphMap.computeRound2(C_DIM, ga)
      );
      boolean var58 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var134, var55, var130, var131);
      RenderKit.Render2D.handleFloat(
         var134,
         var55,
         var130,
         var131,
         GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, ga), GlyphMap.computeRound2(cEl1, ga), var58 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      bold.handleDelegate2(
         "< " + var15.getCurrentPresetName() + " >",
         var134 + var130 / 2.0F,
         var55 + (var131 - this.computeFloat(6.0F)) / 2.0F,
         this.computeFont(6.0F),
         GlyphMap.computeRound2(acc, ga)
      );
      float var59 = cx + 12.0F * this.U;
      float var60 = var129 + 28.0F * this.U;
      float var61 = var17 - 24.0F * this.U;
      float var62 = var20 - 34.0F * this.U;
      float var63 = 18.0F * this.U;
      this.rotNoiseScroll = this.rotNoiseScroll + (this.rotNoiseScrollTarget - this.rotNoiseScroll) * 0.25F;
      float var64 = 13.0F * var63;
      this.rotNoiseMaxScroll = Math.max(0.0F, var64 - var62);
      RenderQueue.handleFloat(var59 - 2.0F * this.U, var60, var61 + 8.0F * this.U, var62);
      float var65 = var60 - this.rotNoiseScroll;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Общая скорость (Rotation Speed)" : "Rotation Speed Multiplier",
         String.format("%.2fx", var15.getRotationSpeed()),
         (var15.getRotationSpeed() - 0.1F) / 2.9F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Скорость Yaw (Yaw Speed)" : "Yaw Speed Multiplier",
         String.format("%.2fx", var15.getYawSpeed()),
         (var15.getYawSpeed() - 0.1F) / 2.9F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Скорость Pitch (Pitch Speed)" : "Pitch Speed Multiplier",
         String.format("%.2fx", var15.getPitchSpeed()),
         (var15.getPitchSpeed() - 0.1F) / 2.9F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Ускорение (Acceleration Factor)" : "Acceleration Smoothing",
         String.format("%.0f%%", var15.getAccelFactor() * 100.0F),
         (var15.getAccelFactor() - 0.05F) / 0.95F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Частота шума (Frequency)" : "Noise Frequency",
         String.format("%.1f Hz", var15.getNoiseFrequency()),
         (var15.getNoiseFrequency() - 0.5F) / 19.5F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Шум Yaw (Yaw Jitter)" : "Yaw Jitter Amplitude",
         String.format("%.1f°", var15.getYawNoise()),
         var15.getYawNoise() / 5.0F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Шум Pitch (Pitch Jitter)" : "Pitch Jitter Amplitude",
         String.format("%.1f°", var15.getPitchNoise()),
         var15.getPitchNoise() / 5.0F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Рандомизация (Humanize Jitter)" : "Humanize Jitter Variance",
         String.format("%.0f%%", var15.getHumanizeJitter() * 100.0F),
         var15.getHumanizeJitter(),
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Шанс овершута (Overshoot Chance)" : "Overshoot Chance",
         String.format("%.0f%%", var15.getOvershootChance() * 100.0F),
         var15.getOvershootChance(),
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Дистанция овершута (Distance)" : "Overshoot Distance",
         String.format("%.1f°", var15.getOvershootDistance()),
         (var15.getOvershootDistance() - 0.5F) / 11.5F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Время овершута (Duration)" : "Overshoot Duration",
         String.format("%.0f ms", var15.getOvershootDuration()),
         (var15.getOvershootDuration() - 50.0F) / 550.0F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Макс. шаг Yaw (Max Yaw Step)" : "Max Yaw Delta / Tick",
         String.format("%.0f°", var15.getMaxYawStep()),
         (var15.getMaxYawStep() - 5.0F) / 175.0F,
         ga,
         acc,
         body,
         bold
      );
      var65 += var63;
      this.handleWhite(
         var59,
         var65,
         var61,
         var16 ? "Макс. шаг Pitch (Max Pitch Step)" : "Max Pitch Delta / Tick",
         String.format("%.0f°", var15.getMaxPitchStep()),
         (var15.getMaxPitchStep() - 3.0F) / 87.0F,
         ga,
         acc,
         body,
         bold
      );
      RenderQueue.runGl();
      if (this.rotNoiseMaxScroll > 1.0F) {
         float var66 = cx + var17 - 6.0F * this.U;
         float var68 = 2.0F * this.U;
         RenderKit.Render2D.handleFloat(var66, var60, var68, var62, GlyphMap.computeRound2(cInp, ga * 0.5F), 1.0F * this.U);
         float var70 = Math.max(12.0F * this.U, var62 / (var62 + this.rotNoiseMaxScroll) * var62);
         float var71 = var60 + this.rotNoiseScroll / this.rotNoiseMaxScroll * (var62 - var70);
         RenderKit.Render2D.handleFloat(var66, var71, var68, var70, GlyphMap.computeRound2(acc, ga * 0.75F), 1.0F * this.U);
      }

      float var148 = cx + var17 + this.getGs2();
      RenderKit.Render2D.handleFloat(var148, cy, var18, chh, GlyphMap.computeRound2(cBrd, ga), 6.0F * this.U);
      RenderKit.Render2D.handleFloat(
         var148 + this.max2(), cy + this.max2(), var18 - this.max2() * 2.0F, chh - this.max2() * 2.0F, GlyphMap.computeRound2(cCard, ga), 5.5F * this.U
      );
      float var149 = 46.0F * this.U;
      float var150 = 14.0F * this.U;
      float var72 = 48.0F * this.U;
      float var73 = 14.0F * this.U;
      float var74 = var148 + var18 - 10.0F * this.U - var72;
      float var75 = var74 - 6.0F * this.U - var149;
      float var76 = cy + 7.0F * this.U;
      float var77 = var75 - 8.0F * this.U - (var148 + 12.0F * this.U);
      this.handleMax(
         bold,
         var16 ? "Хитбокс и траектория" : "Hitbox & Sequence",
         var148 + 12.0F * this.U,
         cy + 9.0F * this.U,
         var77,
         this.computeFont(8.5F),
         GlyphMap.computeRound2(C_WHITE, ga)
      );
      boolean var78 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var75, var76, var149, var150);
      int var79 = var15.isSmoothPath() ? acc : cInp;
      RenderKit.Render2D.handleFloat(
         var75,
         var76,
         var149,
         var150,
         GlyphMap.computeInt2(GlyphMap.computeRound2(var79, ga * 0.75F), GlyphMap.computeRound2(var79, ga), var78 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      int var80 = var15.isSmoothPath() ? (computeFlag(acc) ? -15788246 : -1) : C_DIM;
      bold.handleDelegate2(
         var15.isSmoothPath() ? "SMOOTH" : "STEP",
         var75 + var149 / 2.0F,
         var76 + (var150 - this.computeFloat(6.0F)) / 2.0F,
         this.computeFont(6.0F),
         GlyphMap.computeRound2(var80, ga)
      );
      boolean var81 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var74, var76, var72, var73);
      RenderKit.Render2D.handleFloat(
         var74,
         var76,
         var72,
         var73,
         GlyphMap.computeInt2(GlyphMap.computeRound2(cInp, ga), GlyphMap.computeRound2(cEl1, ga), var81 ? 1.0F : 0.0F),
         3.5F * this.U
      );
      bold.handleDelegate2(
         var15.getTraversalMode().name(),
         var74 + var72 / 2.0F,
         var76 + (var73 - this.computeFloat(6.0F)) / 2.0F,
         this.computeFont(6.0F),
         GlyphMap.computeRound2(acc, ga)
      );
      float var82 = 104.0F * this.U;
      float var83 = 148.0F * this.U;
      float var84 = var148 + (var18 - var82) / 2.0F;
      float var85 = cy + 27.0F * this.U;
      RenderKit.Render2D.handleFloat(
         var84 - 8.0F * this.U, var85 - 3.0F * this.U, var82 + 16.0F * this.U, var83 + 6.0F * this.U, GlyphMap.computeRound2(cInp, ga * 0.35F), 5.0F * this.U
      );
      RenderKit.Render2D.handleFloat(
         var84 - 8.0F * this.U, var85 - 3.0F * this.U, var82 + 16.0F * this.U, var83 + 6.0F * this.U, GlyphMap.computeRound2(cBrd, ga * 0.3F), 5.0F * this.U
      );
      Identifier var86 = mc.player != null && mc.player.getSkinTextures() != null
         ? mc.player.getSkinTextures().texture()
         : DefaultSkinHelper.getSteve().texture();
      float var87 = 32.0F * this.U;
      float var88 = 32.0F * this.U;
      float var89 = var84 + (var82 - var87) / 2.0F;
      float var90 = var85 + 3.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var89, var90, var87, var88, 1.5F * this.U, GlyphMap.computeRound2(-1, ga), 0.125F, 0.125F, 0.125F, 0.125F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var89 - 0.75F * this.U,
            var90 - 0.75F * this.U,
            var87 + 1.5F * this.U,
            var88 + 1.5F * this.U,
            1.5F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.625F,
            0.125F,
            0.125F,
            0.125F,
            var86
         );
      float var91 = 36.0F * this.U;
      float var92 = 48.0F * this.U;
      float var93 = var84 + (var82 - var91) / 2.0F;
      float var94 = var90 + var88 + 2.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var93, var94, var91, var92, 1.0F * this.U, GlyphMap.computeRound2(-1, ga), 0.3125F, 0.3125F, 0.125F, 0.1875F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var93 - 0.5F * this.U,
            var94 - 0.5F * this.U,
            var91 + 1.0F * this.U,
            var92 + 1.0F * this.U,
            1.0F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.3125F,
            0.5625F,
            0.125F,
            0.1875F,
            var86
         );
      float var95 = 12.0F * this.U;
      float var96 = 48.0F * this.U;
      float var97 = var93 - var95 - 2.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var97, var94, var95, var96, 1.0F * this.U, GlyphMap.computeRound2(-1, ga), 0.6875F, 0.3125F, 0.0625F, 0.1875F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var97 - 0.5F * this.U,
            var94 - 0.5F * this.U,
            var95 + 1.0F * this.U,
            var96 + 1.0F * this.U,
            1.0F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.6875F,
            0.5625F,
            0.0625F,
            0.1875F,
            var86
         );
      float var99 = var93 + var91 + 2.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var99, var94, var95, var96, 1.0F * this.U, GlyphMap.computeRound2(-1, ga), 0.5625F, 0.8125F, 0.0625F, 0.1875F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var99 - 0.5F * this.U,
            var94 - 0.5F * this.U,
            var95 + 1.0F * this.U,
            var96 + 1.0F * this.U,
            1.0F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.8125F,
            0.8125F,
            0.0625F,
            0.1875F,
            var86
         );
      float var100 = 16.0F * this.U;
      float var101 = 52.0F * this.U;
      float var102 = var94 + var92 + 2.0F * this.U;
      float var103 = var93 + 1.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var103, var102, var100, var101, 1.0F * this.U, GlyphMap.computeRound2(-1, ga), 0.0625F, 0.3125F, 0.0625F, 0.1875F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var103 - 0.5F * this.U,
            var102 - 0.5F * this.U,
            var100 + 1.0F * this.U,
            var101 + 1.0F * this.U,
            1.0F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.0625F,
            0.5625F,
            0.0625F,
            0.1875F,
            var86
         );
      float var104 = var93 + var91 - var100 - 1.0F * this.U;
      Render.myazem
         .handleClass_2960(SHARED_MS, var104, var102, var100, var101, 1.0F * this.U, GlyphMap.computeRound2(-1, ga), 0.3125F, 0.8125F, 0.0625F, 0.1875F, var86);
      Render.myazem
         .handleClass_2960(
            SHARED_MS,
            var104 - 0.5F * this.U,
            var102 - 0.5F * this.U,
            var100 + 1.0F * this.U,
            var101 + 1.0F * this.U,
            1.0F * this.U,
            GlyphMap.computeRound2(-1, ga * 0.95F),
            0.3125F,
            0.8125F,
            0.0625F,
            0.1875F,
            var86
         );
      float var105 = var85 + var83 - 0.97222227F * var83;
      float var106 = var85 + var83 - 0.7777778F * var83;
      float var107 = var85 + var83 - 0.5555556F * var83;
      RenderKit.Render2D.handleFloat(var84 - 4.0F * this.U, var105, var82 + 8.0F * this.U, 0.6F * this.U, GlyphMap.computeRound2(cBrd, ga * 0.3F), 0.0F);
      RenderKit.Render2D.handleFloat(var84 - 4.0F * this.U, var106, var82 + 8.0F * this.U, 0.6F * this.U, GlyphMap.computeRound2(cBrd, ga * 0.3F), 0.0F);
      RenderKit.Render2D.handleFloat(var84 - 4.0F * this.U, var107, var82 + 8.0F * this.U, 0.6F * this.U, GlyphMap.computeRound2(cBrd, ga * 0.3F), 0.0F);
      List var108 = var15.getAimNodes();

      for (int var109 = 0; var109 < var108.size() - 1; var109++) {
         RotationProfileManager.AimNode var110 = (RotationProfileManager.AimNode)var108.get(var109);
         RotationProfileManager.AimNode var111 = (RotationProfileManager.AimNode)var108.get(var109 + 1);
         float var112 = var84 + var82 / 2.0F + var110.relX * var82;
         float var113 = var85 + var83 - var110.relY / 1.8F * var83;
         float var114 = var84 + var82 / 2.0F + var111.relX * var82;
         float var115 = var85 + var83 - var111.relY / 1.8F * var83;
         this.handleMs(var112, var113, var114, var115, 2.0F * this.U, GlyphMap.computeRound2(acc, ga * 0.85F));
      }

      if (var15.getTraversalMode() == RotationProfileManager.TraversalMode.CYCLE && var108.size() > 2) {
         RotationProfileManager.AimNode var151 = (RotationProfileManager.AimNode)var108.get(var108.size() - 1);
         RotationProfileManager.AimNode var154 = (RotationProfileManager.AimNode)var108.get(0);
         float var157 = var84 + var82 / 2.0F + var151.relX * var82;
         float var160 = var85 + var83 - var151.relY / 1.8F * var83;
         float var163 = var84 + var82 / 2.0F + var154.relX * var82;
         float var166 = var85 + var83 - var154.relY / 1.8F * var83;
         this.handleMs(var157, var160, var163, var166, 1.2F * this.U, GlyphMap.computeRound2(acc, ga * 0.4F));
      }

      for (int var152 = 0; var152 < var108.size(); var152++) {
         RotationProfileManager.AimNode var155 = (RotationProfileManager.AimNode)var108.get(var152);
         float var158 = var84 + var82 / 2.0F + var155.relX * var82;
         float var161 = var85 + var83 - var155.relY / 1.8F * var83;
         boolean var164 = MaxMinHelper.computeFlag(
            (float)this.lastMouseX, (float)this.lastMouseY, var158 - 6.0F * this.U, var161 - 6.0F * this.U, 12.0F * this.U, 12.0F * this.U
         );
         boolean var167 = draggingAimNodeIndex == var152;
         float var169 = !var167 && !var164 ? 4.5F * this.U : 5.5F * this.U;
         int var116 = var167 ? C_WHITE : acc;
         int var117 = var167 ? -15788246 : (computeFlag(acc) ? -15788246 : -1);
         RenderKit.Render2D.handleFloat(var158 - var169, var161 - var169, var169 * 2.0F, var169 * 2.0F, GlyphMap.computeRound2(var116, ga), var169);
         bold.handleDelegate2(
            String.valueOf(var152 + 1), var158, var161 - this.computeFloat(5.8F) / 2.0F, this.computeFont(5.8F), GlyphMap.computeRound2(var117, ga)
         );
      }

      float var153 = var85 + var83 + 6.0F * this.U;
      float var156 = chh - (var153 - cy) - 8.0F * this.U;
      float var159 = var18 - 20.0F * this.U;
      float var162 = var148 + 10.0F * this.U;
      RenderKit.Render2D.handleFloat(var162, var153, var159, var156, GlyphMap.computeRound2(cInp, ga * 0.4F), 4.0F * this.U);
      float var165 = 13.0F * this.U;
      float var168 = var153 + 3.0F * this.U;

      for (int var170 = 0; var170 < Math.min(4, var108.size()); var170++) {
         RotationProfileManager.AimNode var171 = (RotationProfileManager.AimNode)var108.get(var170);
         String var172 = String.format("#%d %s (Y: %.2fm)", var170 + 1, var171.label, var171.relY);
         med.handleDelegate(
            var172, var162 + 6.0F * this.U, var168 + (var165 - this.computeFloat(6.2F)) / 2.0F, this.computeFont(6.2F), GlyphMap.computeRound2(C_WHITE, ga)
         );
         float var118 = 10.0F * this.U;
         float var119 = var162 + var159 - 6.0F * this.U - var118;
         float var120 = var168 + (var165 - var118) / 2.0F;
         boolean var121 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var119, var120, var118, var118);
         RenderKit.Render2D.handleFloat(
            var119,
            var120,
            var118,
            var118,
            GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, ga), GlyphMap.computeRound2(-1750963, ga), var121 ? 1.0F : 0.0F),
            2.0F * this.U
         );
         bold.handleDelegate2(
            "x",
            var119 + var118 / 2.0F,
            var120 + (var118 - this.computeFloat(6.0F)) / 2.0F,
            this.computeFont(6.0F),
            GlyphMap.computeRound2(var121 ? C_WHITE : C_DIM, ga)
         );
         var168 += var165 + 2.0F * this.U;
      }
   }

   public void handleWhite(float x, float y, float w, String label, String valStr, float progress, float ga, int acc, FontRegistry body, FontRegistry bold) {
      body.handleDelegate(label, x, y, this.computeFont(6.2F), GlyphMap.computeRound2(C_DIM, ga));
      bold.handleDelegate(valStr, x + w - bold.computeDelegate(valStr, this.computeFont(6.2F)), y, this.computeFont(6.2F), GlyphMap.computeRound2(C_WHITE, ga));
      float var11 = y + 8.5F * this.U;
      float var12 = 3.2F * this.U;
      RenderKit.Render2D.handleFloat(x, var11, w, var12, GlyphMap.computeRound2(cInp, ga), var12 / 2.0F);
      float var13 = MathHelper.clamp(progress, 0.0F, 1.0F) * w;
      if (var13 > 1.0F) {
         RenderKit.Render2D.handleFloat(x, var11, var13, var12, GlyphMap.computeRound2(acc, ga * 0.9F), var12 / 2.0F);
      }

      float var14 = 2.8F * this.U;
      RenderKit.Render2D.handleFloat(x + var13 - var14, var11 + var12 / 2.0F - var14, var14 * 2.0F, var14 * 2.0F, GlyphMap.computeRound2(C_WHITE, ga), var14);
   }

   public boolean handleRotationManagerClick(double mouseX, double mouseY, int button, float cx, float cy, float cw, float chh) {
      RotationProfileManager var10 = RotationProfileManager.getInstance();
      float var11 = cw * 0.52F;
      float var12 = cw - var11 - this.getGs2();
      float var13 = (chh - this.getGs2()) * 0.52F;
      float var18 = 38.0F * this.U;
      float var19 = 44.0F * this.U;
      float var20 = 14.0F * this.U;
      float var21 = cx + var11 - 10.0F * this.U - var19;
      float var22 = var21 - 5.0F * this.U - var18;
      float var23 = cy + 8.0F * this.U;
      if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var21, var23, var19, var20)) {
         List var68 = var10.getCurvePoints();
         if (var68.size() < 12) {
            var68.add(new RotationProfileManager.CurvePoint(0.5F, 1.0F));
            var68.sort(MainMenuScreen::lambda$handleRotationManagerClick$3);
            var10.closeFile();
            TotalHelper.handleInt2(1, 1);
         }

         return true;
      } else if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var22, var23, var18, var20)) {
         var10.initAdd();
         var10.closeFile();
         TotalHelper.run15();
         return true;
      } else {
         float var24 = cx + 12.0F * this.U;
         float var25 = cy + 34.0F * this.U;
         float var26 = var11 - 24.0F * this.U;
         float var27 = var13 - 44.0F * this.U;
         List var28 = var10.getCurvePoints();

         for (int var29 = 0; var29 < var28.size(); var29++) {
            RotationProfileManager.CurvePoint var30 = (RotationProfileManager.CurvePoint)var28.get(var29);
            float var31 = var24 + var30.time * var26;
            float var32 = var25 + var27 - MathHelper.clamp(var30.speed / 2.5F * var27, 0.0F, var27);
            if (MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var31 - 6.0F * this.U, var32 - 6.0F * this.U, 12.0F * this.U, 12.0F * this.U)) {
               if (button == 1 && var28.size() > 2) {
                  var28.remove(var29);
                  var10.closeFile();
                  TotalHelper.run15();
                  return true;
               }

               if (button == 0) {
                  draggingCurvePointIndex = var29;
                  return true;
               }
            }
         }

         float var69 = cy + var13 + this.getGs2();
         float var70 = 82.0F * this.U;
         float var33 = 14.0F * this.U;
         float var34 = 72.0F * this.U;
         float var35 = 14.0F * this.U;
         float var36 = cx + var11 - 10.0F * this.U - var70;
         float var37 = var36 - 5.0F * this.U - var34;
         float var38 = var69 + 8.0F * this.U;
         if ((button == 0 || button == 1) && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var36, var38, var70, var33)) {
            List var72 = RotationProfileManager.getPresetList();
            if (var72.isEmpty()) {
               var72 = List.of("Default");
            }

            int var74 = var72.indexOf(var10.getCurrentPresetName());
            if (var74 == -1) {
               var74 = 0;
            }

            int var76 = button == 1 ? (var74 - 1 + var72.size()) % var72.size() : (var74 + 1) % var72.size();
            var10.setCurrentPresetName((String)var72.get(var76));
            TotalHelper.handleInt2(1, 1);
            return true;
         } else if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var37, var38, var34, var35)) {
            EasingTypeHelper.EasingType[] var71 = EasingTypeHelper.EasingType.values();
            int var73 = var10.getEasingType().ordinal();
            int var75 = (var73 + 1) % var71.length;
            var10.setEasingType(var71[var75]);
            var10.setCurrentPresetName2("Custom");
            var10.closeFile();
            TotalHelper.handleInt2(1, 1);
            return true;
         } else {
            float var39 = cx + 12.0F * this.U;
            float var40 = var69 + 28.0F * this.U;
            float var41 = var11 - 24.0F * this.U;
            float var42 = chh - var13 - this.getGs2() - 34.0F * this.U;
            float var43 = 18.0F * this.U;
            if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var39, var40, var41, var42)) {
               float var44 = (float)mouseY - (var40 - this.rotNoiseScroll);
               int var45 = (int)(var44 / var43);
               String[] var46 = new String[]{
                  "rot_speed",
                  "yaw_speed",
                  "pitch_speed",
                  "accel",
                  "freq",
                  "yaw_noise",
                  "pitch_noise",
                  "humanize",
                  "over_chance",
                  "over_dist",
                  "over_dur",
                  "max_yaw_step",
                  "max_pitch_step"
               };
               if (var45 >= 0 && var45 < var46.length) {
                  draggingRotationSlider = var46[var45];
                  this.handleDouble(mouseX, mouseY, cx, cy, cw, chh);
                  return true;
               }
            }

            float var77 = cx + var11 + this.getGs2();
            float var47 = 46.0F * this.U;
            float var48 = 14.0F * this.U;
            float var49 = 48.0F * this.U;
            float var50 = 14.0F * this.U;
            float var51 = var77 + var12 - 10.0F * this.U - var49;
            float var52 = var51 - 6.0F * this.U - var47;
            float var53 = cy + 7.0F * this.U;
            if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var52, var53, var47, var48)) {
               var10.setSmoothPath(!var10.isSmoothPath());
               var10.closeFile();
               TotalHelper.handleInt2(1, 1);
               return true;
            } else if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var51, var53, var49, var50)) {
               RotationProfileManager.TraversalMode[] var78 = RotationProfileManager.TraversalMode.values();
               int var79 = (var10.getTraversalMode().ordinal() + 1) % var78.length;
               var10.setTraversalMode(var78[var79]);
               var10.closeFile();
               TotalHelper.handleInt2(1, 1);
               return true;
            } else {
               float var54 = 104.0F * this.U;
               float var55 = 148.0F * this.U;
               float var56 = var77 + (var12 - var54) / 2.0F;
               float var57 = cy + 27.0F * this.U;
               List var58 = var10.getAimNodes();

               for (int var59 = 0; var59 < var58.size(); var59++) {
                  RotationProfileManager.AimNode var60 = (RotationProfileManager.AimNode)var58.get(var59);
                  float var61 = var56 + var54 / 2.0F + var60.relX * var54;
                  float var62 = var57 + var55 - var60.relY / 1.8F * var55;
                  if (MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var61 - 7.0F * this.U, var62 - 7.0F * this.U, 14.0F * this.U, 14.0F * this.U)) {
                     if (button == 1 && var58.size() > 1) {
                        var58.remove(var59);
                        var10.getAimNodes();
                        var10.closeFile();
                        TotalHelper.run15();
                        return true;
                     }

                     if (button == 0) {
                        draggingAimNodeIndex = var59;
                        return true;
                     }
                  }
               }

               if (button == 0
                  && MaxMinHelper.computeFlag(
                     (float)mouseX, (float)mouseY, var56 - 8.0F * this.U, var57 - 3.0F * this.U, var54 + 16.0F * this.U, var55 + 6.0F * this.U
                  )) {
                  float var81 = MathHelper.clamp((float)((mouseX - (double)(var56 + var54 / 2.0F)) / (double)var54), -0.45F, 0.45F);
                  float var83 = MathHelper.clamp((float)(((double)(var57 + var55) - mouseY) / (double)var55 * 1.8F), 0.1F, 1.8F);
                  String var85 = var83 > 1.6F ? "Head" : (var83 > 1.2F ? "Chest" : (var83 > 0.8F ? "Pelvis" : "Legs"));
                  int var87 = var58.size() + 1;
                  var58.add(new RotationProfileManager.AimNode(var87, var85, var81, var83, 0.0F, 1.0F));
                  var10.getAimNodes();
                  var10.closeFile();
                  TotalHelper.handleInt2(1, 1);
                  return true;
               } else {
                  float var80 = var57 + var55 + 6.0F * this.U;
                  float var82 = var12 - 20.0F * this.U;
                  float var84 = var77 + 10.0F * this.U;
                  float var86 = var80 + 3.0F * this.U;
                  float var63 = 13.0F * this.U;

                  for (int var64 = 0; var64 < Math.min(4, var58.size()); var64++) {
                     float var65 = 10.0F * this.U;
                     float var66 = var84 + var82 - 6.0F * this.U - var65;
                     float var67 = var86 + (var63 - var65) / 2.0F;
                     if (button == 0 && MaxMinHelper.computeFlag((float)mouseX, (float)mouseY, var66, var67, var65, var65) && var58.size() > 1) {
                        var58.remove(var64);
                        var10.getAimNodes();
                        var10.closeFile();
                        TotalHelper.run15();
                        return true;
                     }

                     var86 += var63 + 2.0F * this.U;
                  }

                  return false;
               }
            }
         }
      }
   }

   public void handleDouble(double mouseX, double mouseY, float cx, float cy, float cw, float chh) {
      RotationProfileManager var9 = RotationProfileManager.getInstance();
      float var10 = cw * 0.52F;
      float var11 = cw - var10 - this.getGs2();
      float var12 = (chh - this.getGs2()) * 0.52F;
      if (draggingCurvePointIndex >= 0 && draggingCurvePointIndex < var9.getCurvePoints().size()) {
         float var24 = cx + 12.0F * this.U;
         float var26 = cy + 34.0F * this.U;
         float var28 = var10 - 24.0F * this.U;
         float var29 = var12 - 44.0F * this.U;
         RotationProfileManager.CurvePoint var21 = var9.getCurvePoints().get(draggingCurvePointIndex);
         var21.time = MathHelper.clamp((float)((mouseX - (double)var24) / (double)var28), 0.0F, 1.0F);
         var21.speed = MathHelper.clamp((float)(((double)(var26 + var29) - mouseY) / (double)var29 * 2.5), 0.0F, 2.5F);
      } else if (draggingAimNodeIndex >= 0 && draggingAimNodeIndex < var9.getAimNodes().size()) {
         float var13 = cx + var10 + this.getGs2();
         float var22 = 104.0F * this.U;
         float var23 = 148.0F * this.U;
         float var25 = var13 + (var11 - var22) / 2.0F;
         float var27 = cy + 27.0F * this.U;
         RotationProfileManager.AimNode var20 = var9.getAimNodes().get(draggingAimNodeIndex);
         var20.relX = MathHelper.clamp((float)((mouseX - (double)(var25 + var22 / 2.0F)) / (double)var22), -0.45F, 0.45F);
         var20.relY = MathHelper.clamp((float)(((double)(var27 + var23) - mouseY) / (double)var23 * 1.8F), 0.1F, 1.8F);
      } else {
         if (draggingRotationSlider != null) {
            float var15 = var10 - 24.0F * this.U;
            float var16 = cx + 12.0F * this.U;
            float var17 = MathHelper.clamp((float)((mouseX - (double)var16) / (double)var15), 0.0F, 1.0F);
            var9.setCurrentPresetName2("Custom");
            String var18 = draggingRotationSlider;
            switch (var18) {
               case "rot_speed":
                  var9.setRotationSpeed(0.1F + var17 * 2.9F);
                  break;
               case "yaw_speed":
                  var9.setYawSpeed(0.1F + var17 * 2.9F);
                  break;
               case "pitch_speed":
                  var9.setPitchSpeed(0.1F + var17 * 2.9F);
                  break;
               case "accel":
                  var9.setAccelFactor(0.05F + var17 * 0.95F);
                  break;
               case "freq":
                  var9.setNoiseFrequency(0.5F + var17 * 19.5F);
                  break;
               case "yaw_noise":
                  var9.setYawNoise(var17 * 5.0F);
                  break;
               case "pitch_noise":
                  var9.setPitchNoise(var17 * 5.0F);
                  break;
               case "humanize":
                  var9.setHumanizeJitter(var17);
                  break;
               case "over_chance":
                  var9.setOvershootChance(var17);
                  break;
               case "over_dist":
                  var9.setOvershootDistance(0.5F + var17 * 11.5F);
                  break;
               case "over_dur":
                  var9.setOvershootDuration(50.0F + var17 * 550.0F);
                  break;
               case "max_yaw_step":
                  var9.setMaxYawStep(5.0F + var17 * 175.0F);
                  break;
               case "max_pitch_step":
                  var9.setMaxPitchStep(3.0F + var17 * 87.0F);
            }
         }
      }
   }

   public void runDragging() {
      if (draggingCurvePointIndex >= 0 || draggingAimNodeIndex >= 0 || draggingRotationSlider != null) {
         RotationProfileManager.getInstance().closeFile();
      }

      draggingCurvePointIndex = -1;
      draggingAimNodeIndex = -1;
      draggingRotationSlider = null;
   }

   public void parseGs(
      float wx,
      float wy,
      float ww,
      float wh,
      float mw,
      float ga,
      float gsA,
      int acc,
      FontRegistry body,
      FontRegistry med,
      FontRegistry bold,
      FontRegistry guiicon
   ) {
      float var13 = 190.0F * this.U;
      float var14 = this.getGs3();
      if (gsPosX < 0.0F || gsPosY < 0.0F) {
         gsPosX = wx + ww + 10.0F * this.U;
         gsPosY = wy;
      }

      if (this.gsDraggingHeader) {
         gsPosX = (float)this.lastMouseX - this.gsDragOffX;
         gsPosY = (float)this.lastMouseY - this.gsDragOffY;
      }

      float var15 = gsPosX;
      float var16 = gsPosY;
      if (SHARED_MS != null) {
         float var17 = var15 + var13 / 2.0F;
         float var18 = var16 + var14 / 2.0F;
         float var19 = Math.max(0.01F, 0.86F + 0.14F * gsA);
         SHARED_MS.push();
         SHARED_MS.translate(var17, var18, 0.0F);
         SHARED_MS.scale(var19, var19, 1.0F);
         SHARED_MS.translate(-var17, -var18, 0.0F);
      }

      if (!is3DCapturing && gsBlur > 0.01F) {
         TintColorHelper.handleFloat(var15, var16, var13, var14, ga, this.max(), isDayMode ? -1 : -16118508);
      } else {
         int var50 = GlyphMap.computeRound2(isDayMode ? -1 : cApp, ga);
         RenderKit.Render2D.handleFloat(var15, var16, var13, var14, GlyphMap.computeRound2(cBrdL, ga * 0.7F), this.max());
         RenderKit.Render2D.handleFloat(
            var15 + this.max2(), var16 + this.max2(), var13 - this.max2() * 2.0F, var14 - this.max2() * 2.0F, var50, Math.max(0.5F, this.max() - this.max2())
         );
      }

      int var51 = isDayMode ? (gsBlur < 0.05F ? -1 : -460036) : cSide;
      float var52 = Math.max(0.5F, this.max() - this.max2());
      float var53 = (isDayMode ? 0.65F : (gsBlur > 0.01F ? 0.15F : 0.0F)) * ga;
      if (var53 > 0.01F) {
         RenderKit.Render2D.handleFloat(
            var15 + this.max2(),
            var16 + this.max2(),
            var13 - this.max2() * 2.0F,
            24.0F * this.U,
            GlyphMap.computeRound2(var51, var53),
            var52,
            var52,
            0.0F,
            0.0F
         );
      }

      RenderKit.Render2D.handleFloat(
         var15 + this.max2(), var16 + 24.0F * this.U, var13 - this.max2() * 2.0F, this.max2(), GlyphMap.computeRound2(cBrd, ga * 0.85F), 0.0F
      );
      bold.handleDelegate(
         EngHelper.computeString("GUI Settings"),
         var15 + 12.0F * this.U,
         var16 + 7.5F * this.U,
         this.computeFont(8.5F),
         GlyphMap.computeRound(isDayMode ? 20 : 255, ga)
      );
      float var20 = 13.0F * this.U;
      float var21 = var15 + var13 - var20 - 7.0F * this.U;
      float var22 = var16 + 5.5F * this.U;
      boolean var23 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var21, var22, var20, var20);
      Animation var24 = this.chipAnim("gs:close");
      var24.setDirection(var23 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var25 = var24.getEnd();
      int var26 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, ga), GlyphMap.computeRound2(-1750963, ga * 0.85F), var25);
      RenderKit.Render2D.handleFloat(var21, var22, var20, var20, var26, 3.5F * this.U);
      this.handleDegrees(SHARED_MS, var21 + var20 / 2.0F, var22 + var20 / 2.0F, 5.0F * this.U, GlyphMap.computeColor(isDayMode ? 40 : 255, (int)(ga * 255.0F)));
      float var27 = var16 + 30.0F * this.U;
      float var28 = var13 - 24.0F * this.U;

      for (String[] var32 : GS_SLIDERS) {
         String var33 = var32[3];
         float var34 = Float.parseFloat(var32[1]);
         float var35 = Float.parseFloat(var32[2]);
         if (this.dragGs != null && this.dragGs.equals(var33)) {
            float var36 = MaxMinHelper.computeFloat4(((float)this.lastMouseX - (var15 + 12.0F * this.U)) / var28, 0.0F, 1.0F);
            float var37 = var34 + (var35 - var34) * var36;
            if (var37 != computeFloat(var33)) {
               handleVal(var33, var37);
            }
         }

         float var69 = computeFloat(var33);
         boolean var72 = MaxMinHelper.computeFlag((float)this.lastMouseX, (float)this.lastMouseY, var15 + 12.0F * this.U, var27, var28, 24.0F * this.U);
         Animation var38 = this.chipAnim("gs:" + var33);
         var38.setDirection(!var72 && (this.dragGs == null || !this.dragGs.equals(var33)) ? AnimationDirection.BACKWARDS : AnimationDirection.FORWARDS);
         float var39 = var38.getEnd();
         med.handleDelegate(
            EngHelper.computeString(var32[0]),
            var15 + 12.0F * this.U,
            var27 + 1.0F * this.U,
            this.computeFont(7.8F),
            GlyphMap.computeRound2(C_WHITE, ga * (0.85F + 0.15F * var39))
         );
         String var40 = Math.round(var69 * 100.0F) + "%";
         int var41 = isDayMode && gsBlur < 0.05F ? GlyphMap.computeRound2(-15788246, ga) : GlyphMap.computeRound2(acc, ga);
         med.handleDelegate(
            var40, var15 + 12.0F * this.U + var28 - med.computeDelegate(var40, this.computeFont(7.8F)), var27 + 1.0F * this.U, this.computeFont(7.8F), var41
         );
         float var42 = var27 + 13.0F * this.U;
         float var43 = MathHelper.clamp((var69 - var34) / (var35 - var34), 0.0F, 1.0F);
         float var44 = 2.8F * this.U;
         int var45 = GlyphMap.getColor(
            isDayMode ? (gsBlur < 0.05F ? 220 : 210) : 24,
            isDayMode ? (gsBlur < 0.05F ? 228 : 215) : 24,
            isDayMode ? (gsBlur < 0.05F ? 238 : 225) : 32,
            (int)(ga * 255.0F)
         );
         int var46 = isDayMode && gsBlur < 0.05F ? GlyphMap.computeRound2(-14800581, ga) : GlyphMap.computeRound2(acc, ga);
         RenderKit.Render2D.handleFloat(var15 + 12.0F * this.U, var42, var28, var44, var45, var44 / 2.0F);
         if (var43 > 0.0F) {
            RenderKit.Render2D.handleFloat(var15 + 12.0F * this.U, var42, var28 * var43, var44, var46, var44 / 2.0F);
         }

         float var47 = var15 + 12.0F * this.U + var28 * var43;
         float var48 = var42 + var44 / 2.0F;
         float var49 = (5.8F + 1.2F * var39) * this.U;
         RenderKit.Render2D.handleFloat(var47 - var49 / 2.0F, var48 - var49 / 2.0F, var49, var49, GlyphMap.computeColor(255, (int)(ga * 255.0F)), var49 / 2.0F);
         var27 += 26.0F * this.U;
      }

      boolean var55 = MaxMinHelper.computeFlag(
         (float)this.lastMouseX, (float)this.lastMouseY, var15 + 12.0F * this.U, var27, var28 + 12.0F * this.U, 22.0F * this.U
      );
      Animation var57 = this.chipAnim("gs:blurtoggle:hov");
      var57.setDirection(var55 ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      Animation var59 = this.chipAnim("gs:blurtoggle:on");
      var59.setDirection(gsBlurOn ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var61 = var59.getEnd();
      med.handleDelegate(
         EngHelper.computeString("Blur"),
         var15 + 12.0F * this.U,
         var27 + 3.0F * this.U,
         this.computeFont(7.8F),
         GlyphMap.computeRound2(C_WHITE, ga * (0.85F + 0.15F * var57.getEnd()))
      );
      float var63 = 22.0F * this.U;
      float var65 = 12.5F * this.U;
      float var67 = var15 + var13 - 12.0F * this.U - var63;
      float var70 = var27 + (22.0F * this.U - var65) / 2.0F;
      boolean var73 = computeFlag(acc);
      int var75 = GlyphMap.computeRound2(GlyphMap.getColor(isDayMode ? 210 : 22, isDayMode ? 215 : 22, isDayMode ? 225 : 30, (int)(ga * 255.0F)), ga);
      int var77 = GlyphMap.computeRound2(var73 && isDayMode ? -15788246 : acc, ga);
      int var79 = GlyphMap.computeInt2(var75, var77, var61);
      RenderKit.Render2D.handleFloat(var67, var70, var63, var65, var79, var65 / 2.0F);
      float var81 = var65 - 3.0F * this.U;
      float var83 = var67 + 1.5F * this.U + (var63 - var81 - 3.0F * this.U) * var61;
      float var85 = var70 + 1.5F * this.U;
      int var87 = var73 && !isDayMode && gsBlurOn ? GlyphMap.getColor(12, 14, 20, (int)(ga * 255.0F)) : GlyphMap.getColor(255, 255, 255, (int)(ga * 255.0F));
      RenderKit.Render2D.handleFloat(var83, var85, var81, var81, var87, var81 / 2.0F);
      var27 += 22.0F * this.U;
      var55 = computeFlag(acc);
      med.handleDelegate(
         EngHelper.computeString("Animation"),
         var15 + 12.0F * this.U,
         var27 + 3.0F * this.U,
         this.computeFont(7.8F),
         GlyphMap.computeRound2(C_WHITE, ga * 0.85F)
      );
      float var58 = 34.0F * this.U;
      float var60 = 14.0F * this.U;
      var61 = 4.0F * this.U;
      var63 = var15 + var13 - 12.0F * this.U - var58;
      var65 = var63 - var61 - var58;
      var67 = var27 + (22.0F * this.U - var60) / 2.0F;
      Animation var71 = this.chipAnim("gs:anim:def");
      var71.setDirection(!gs3DAnim ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var74 = var71.getEnd();
      Animation var76 = this.chipAnim("gs:anim:3d");
      var76.setDirection(gs3DAnim ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      float var78 = var76.getEnd();
      var79 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, ga), GlyphMap.computeRound2(acc, ga * 0.9F), var74);
      int var82 = GlyphMap.computeInt2(GlyphMap.computeRound2(cBrd, ga), GlyphMap.computeRound2(acc, ga), var74);
      int var84 = GlyphMap.computeInt2(GlyphMap.computeRound2(C_DIM, ga), GlyphMap.computeRound2(var55 && !isDayMode ? -16184818 : -1, ga), var74);
      RenderKit.Render2D.handleFloat(var65, var67, var58, var60, var82, 3.5F * this.U);
      RenderKit.Render2D.handleFloat(
         var65 + this.max2(), var67 + this.max2(), var58 - this.max2() * 2.0F, var60 - this.max2() * 2.0F, var79, Math.max(0.5F, 3.5F * this.U - this.max2())
      );
      bold.handleDelegate2("2D", var65 + var58 / 2.0F, var67 + (var60 - this.computeFloat(6.5F)) / 2.0F, this.computeFont(6.5F), var84);
      int var86 = GlyphMap.computeInt2(GlyphMap.computeRound2(cEl1, ga), GlyphMap.computeRound2(acc, ga * 0.9F), var78);
      var87 = GlyphMap.computeInt2(GlyphMap.computeRound2(cBrd, ga), GlyphMap.computeRound2(acc, ga), var78);
      int var89 = GlyphMap.computeInt2(GlyphMap.computeRound2(C_DIM, ga), GlyphMap.computeRound2(var55 && !isDayMode ? -16184818 : -1, ga), var78);
      RenderKit.Render2D.handleFloat(var63, var67, var58, var60, var87, 3.5F * this.U);
      RenderKit.Render2D.handleFloat(
         var63 + this.max2(), var67 + this.max2(), var58 - this.max2() * 2.0F, var60 - this.max2() * 2.0F, var86, Math.max(0.5F, 3.5F * this.U - this.max2())
      );
      bold.handleDelegate2("3D", var63 + var58 / 2.0F, var67 + (var60 - this.computeFloat(6.5F)) / 2.0F, this.computeFont(6.5F), var89);
      if (SHARED_MS != null) {
         SHARED_MS.pop();
      }
   }

   public void handleDay(FontRegistry font, String text, float cx, float cy, float tw, float th, float sel, float sa, float ga, float en) {
      int var11 = getAccent();
      boolean var12 = computeFlag(var11);
      int var13 = GlyphMap.computeRound2(isDayMode ? -1906448 : cEl1, ga * sa);
      int var14 = isDayMode ? GlyphMap.computeRound2(-15788246, ga * sa) : GlyphMap.computeRound2(var11, (0.78F + 0.22F * en) * ga * sa);
      int var15 = GlyphMap.computeInt2(var13, var14, sel);
      int var16 = GlyphMap.computeRound2(cBrd, ga * sa);
      int var17 = GlyphMap.computeRound2(isDayMode ? -15788246 : var11, ga * sa);
      int var18 = GlyphMap.computeInt2(var16, var17, sel);
      RenderKit.Render2D.handleFloat(cx, cy, tw, th, var18, 4.0F * this.U);
      RenderKit.Render2D.handleFloat(
         cx + this.max2(), cy + this.max2(), tw - this.max2() * 2.0F, th - this.max2() * 2.0F, var15, Math.max(0.5F, 4.0F * this.U - this.max2())
      );
      int var19 = isDayMode ? -10193781 : C_DIM;
      int var20 = var12 && !isDayMode ? -16184818 : -1;
      int var21 = GlyphMap.computeInt2(GlyphMap.computeRound2(var19, ga * sa), GlyphMap.computeRound2(var20, ga * sa * (0.75F + 0.25F * en)), sel);
      font.handleDelegate2(text, cx + tw / 2.0F, cy + (th - this.computeFloat(8.0F)) / 2.0F - 0.8F * this.U, this.computeFont(8.0F), var21);
   }

   public void run() {
      this.run3();
      TotalHelper.run3();
   }

   public void handleModule(Module target) {
      this.run3();
      TotalHelper.run3();
   }

   public void run2() {
      this.run3();
      TotalHelper.run3();
   }

   public void run3() {
   }

   public void render(DrawContext context, int mouseXRaw, int mouseYRaw, float delta) {
      if (this.exit && (float)(System.currentTimeMillis() - this.closeStartTime) >= 260.0F) {
         this.exit = false;
         mc.setScreen(null);
      } else {
         if (gsBlur > 0.01F) {
            UHalfTexelSizeHelper.handleClass_4587(context != null ? context.getMatrices() : SHARED_MS);
         }

         float var5 = (float)mc.getWindow().getScaledWidth() / this.scaleFix / 2.0F;
         float var6 = (float)mc.getWindow().getScaledHeight() / this.scaleFix / 2.0F;
         float var7 = this.getTime2();
         float var8 = this.getTime3();
         this.lastMouseX = (double)(((float)mouseXRaw / this.scaleFix - var5) / var7 + var5);
         this.lastMouseY = (double)(((float)mouseYRaw / this.scaleFix - var6 - var8) / var7 + var6);
         this.runDay();
         this.initDay(context, null);
      }
   }

   public void init() {
      if (MenuAnimation.check()) {
         MenuAnimation.run();
      }

      this.exit = false;
      this.openStartTime = System.currentTimeMillis();
      this.closeStartTime = 0L;
      LegacyModule2 var1 = FactoryHelper.getInstance().getClear().get(LegacyModule2.class);
      ClickGUI var2 = ClickGUI.getInstance();
      int var3 = var1 != null ? var1.getKey() : -1;
      int var4 = var2 != null ? var2.getBind() : -1;
      boolean var5 = var3 > 0 && KeyNames.computeGlfw(var3);
      boolean var6 = var4 > 0 && KeyNames.computeGlfw(var4);
      this.toggleKeyReleased = !var5 && !var6;
      searchActive = false;
      this.searchTypeTime = System.currentTimeMillis();
      this.bindingModule = null;
      this.tabSwitchAnim.getTimerUtil();
      this.animGuiSettings.setDirection(AnimationDirection.BACKWARDS);
      langRu = EngHelper.getInstance().check();
      this.animDayNight.setDirection(isDayMode ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
      if (!isDayMode) {
         this.animDayNight.timerUtil.setLastMS(System.currentTimeMillis() - 500L);
      }

      this.runDay();
   }

   public boolean mouseClicked(double mouseXRaw, double mouseYRaw, int button) {
      if (this.exit) {
         return true;
      } else {
         float var6 = this.getTime2();
         float var7 = this.getTime3();
         int var8 = (int)((float)mc.getWindow().getScaledWidth() / this.scaleFix);
         int var9 = (int)((float)mc.getWindow().getScaledHeight() / this.scaleFix);
         float var10 = (float)var8 / 2.0F;
         float var11 = (float)var9 / 2.0F;
         float var12 = (float)((mouseXRaw / (double)this.scaleFix - (double)var10) / (double)var6 + (double)var10);
         float var13 = (float)((mouseYRaw / (double)this.scaleFix - (double)var11 - (double)var7) / (double)var6 + (double)var11);
         this.handleMin(var8, var9);
         float var14 = this.getU();
         float var15 = this.getFloat();
         float var16 = (float)var8 / 2.0F - var14 / 2.0F;
         float var17 = (float)var9 / 2.0F - var15 / 2.0F;
         FontRegistry var18 = IconFontRegistry.fontRegistry3;
         float var19 = this.getFloat2();
         if (this.activeBind != null) {
            if (button == 1) {
               this.activeBind.withCachedValue(-999);
            } else {
               this.activeBind.withCachedValue(-100 + button);
            }

            this.activeBind = null;
            return true;
         } else if (this.bindingModule != null) {
            if (button == 1) {
               this.bindingModule.setKey(-1);
            } else {
               this.bindingModule.setKey(-100 + button);
            }

            this.bindingModule = null;
            return true;
         } else {
            float var20 = 5.0F;
            float var21 = 8.0F * this.U;
            float var22 = 6.0F * this.U;
            float var23 = var20 * var21 + (var20 - 1.0F) * var22;
            float var24 = var16 + var14 - this.getGs();
            float var25 = var17 + (this.getFloat3() - var21) / 2.0F;
            float var26 = var24 - var23;

            for (int var27 = 0; var27 < 5; var27++) {
               if (MaxMinHelper.computeFlag(var12, var13, var26 - 2.0F * this.U, var25 - 2.0F * this.U, var21 + 4.0F * this.U, var21 + 4.0F * this.U)
                  && button == 0) {
                  selectedAccentIndex = var27;
                  initGs();
                  TotalHelper.handleInt2(var27, 5);
                  return true;
               }

               var26 += var21 + var22;
            }

            float var89 = 18.0F * this.U;
            float var28 = 16.0F * this.U;
            float var29 = var24 - var23 - 8.0F * this.U - var89;
            float var30 = var17 + (this.getFloat3() - var28) / 2.0F;
            if (MaxMinHelper.computeFlag(var12, var13, var29, var30, var89, var28) && button == 0) {
               isDayMode = !isDayMode;
               initGs();
               TotalHelper.handleInt2(isDayMode ? 1 : 0, 2);
               return true;
            } else {
               float var31 = var29 - 8.0F * this.U;
               float var32 = 46.0F * this.U;
               float var33 = 16.0F * this.U;
               float var34 = var31 - 8.0F * this.U - var32;
               float var35 = var17 + (this.getFloat3() - var33) / 2.0F;
               if (MaxMinHelper.computeFlag(var12, var13, var34, var35, var32, var33) && button == 0) {
                  EngHelper.getInstance().run();
                  langRu = EngHelper.getInstance().check();
                  return true;
               } else {
                  float var36 = this.animSearchFocus.getEnd();
                  float var37 = this.animSearchText.getEnd();
                  float var38 = Math.max(var36, var37);
                  float var39 = 100.0F * this.U;
                  float var40 = 175.0F * this.U;
                  float var41 = var39 + (var40 - var39) * var38;
                  float var42 = 18.5F * this.U;
                  float var43 = var34 - 8.0F * this.U - var41;
                  float var44 = var17 + (this.getFloat3() - var42) / 2.0F;
                  List var45 = this.getSearchQuery();
                  if (this.animSearchDropdown.getEnd() > 0.01F && !var45.isEmpty()) {
                     float var46 = var41;
                     float var47 = 23.0F * this.U;
                     float var48 = 6.0F * this.U + (float)var45.size() * var47;
                     float var49 = var43;
                     float var50 = var44 + var42 + 3.0F * this.U;
                     if (MaxMinHelper.computeFlag(var12, var13, var43, var50, var41, var48)) {
                        float var106 = var50 + 3.0F * this.U;

                        for (Module var115 : (Iterable<Module>)(Iterable)var45) {
                           if (MaxMinHelper.computeFlag(var12, var13, var49 + 4.0F * this.U, var106, var46 - 8.0F * this.U, var47) && button == 0) {
                              currentView = MainMenuScreen.ViewMode.MODULES;
                              this.active = var115.getCategory();
                              this.tabSwitchAnim.getTimerUtil();
                              this.runTime();
                              searchActive = false;
                              float var118 = (this.getU() - this.getFloat2() - this.getGs() * 2.0F - this.getGs2()) / 2.0F;
                              float[] var122 = new float[]{0.0F, 0.0F};
                              float var126 = 0.0F;

                              for (Module var134 : FactoryHelper.getInstance().getClear().values()) {
                                 if (!var134.getName().equalsIgnoreCase("ClickGui")
                                    && !var134.getName().equalsIgnoreCase("Click GUI")
                                    && !var134.getName().equalsIgnoreCase("Click_GUI")
                                    && !var134.getName().equalsIgnoreCase("Configs")
                                    && !var134.getName().equalsIgnoreCase("ConfigsFeature")
                                    && !(var134 instanceof LegacyModule2)
                                    && !computeFlag(var134)
                                    && var134.getCategory() == this.active) {
                                    float var138 = this.computeDesc(var134, var118);
                                    int var141 = var122[0] <= var122[1] ? 0 : 1;
                                    float var143 = var122[var141];
                                    if (var134 == var115) {
                                       var126 = var143;
                                    }

                                    var122[var141] += var138 + this.getGs2();
                                 }
                              }

                              float var131 = this.getFloat() - this.getFloat3() - this.getGs() * 2.0F;
                              float var135 = Math.max(0.0F, Math.max(var122[0], var122[1]) - var131);
                              this.scrollTarget = MathHelper.clamp(var126 - 6.0F * this.U, 0.0F, var135);
                              this.highlightedModule = var115;
                              this.highlightStartTime = System.currentTimeMillis();
                              TotalHelper.handleInt2(1, 1);
                              return true;
                           }

                           var106 += var47;
                        }

                        return true;
                     }
                  }

                  if (MaxMinHelper.computeFlag(var12, var13, var43, var44, var41, var42)) {
                     if (button == 1) {
                        this.runTime();
                        searchActive = false;
                        TotalHelper.run15();
                     } else if (button == 0) {
                        if (!searchActive) {
                           TotalHelper.run10();
                        }

                        searchActive = true;
                        this.searchTypeTime = System.currentTimeMillis();
                     }

                     return true;
                  } else {
                     searchActive = false;
                     if (showGuiSettings && this.animGuiSettings.getEnd() > 0.05F) {
                        float var90 = 190.0F * this.U;
                        float var92 = this.getGs3();
                        float var94 = gsPosX < 0.0F ? var16 + var14 + 10.0F * this.U : gsPosX;
                        float var97 = gsPosY < 0.0F ? var17 : gsPosY;
                        if (MaxMinHelper.computeFlag(var12, var13, var94, var97, var90, var92)) {
                           float var102 = 13.0F * this.U;
                           float var105 = var94 + var90 - var102 - 7.0F * this.U;
                           float var109 = var97 + 5.5F * this.U;
                           if (MaxMinHelper.computeFlag(var12, var13, var105, var109, var102, var102) && button == 0) {
                              showGuiSettings = false;
                              return true;
                           }

                           if (MaxMinHelper.computeFlag(var12, var13, var94, var97, var90, 24.0F * this.U) && button == 0) {
                              this.gsDraggingHeader = true;
                              this.gsDragOffX = var12 - var94;
                              this.gsDragOffY = var13 - var97;
                              return true;
                           }

                           float var113 = var97 + 30.0F * this.U;
                           float var117 = var90 - 24.0F * this.U;

                           for (String[] var132 : GS_SLIDERS) {
                              float var136 = var113 + 13.0F * this.U;
                              if (button == 0
                                 && MaxMinHelper.computeFlag(
                                    var12, var13, var94 + 8.0F * this.U, var136 - 6.0F * this.U, var117 + 8.0F * this.U, 16.0F * this.U
                                 )) {
                                 this.frozenU = this.U;
                                 this.dragGs = var132[3];
                                 float var139 = Float.parseFloat(var132[1]);
                                 float var142 = Float.parseFloat(var132[2]);
                                 float var144 = MaxMinHelper.computeFloat4((var12 - (var94 + 12.0F * this.U)) / var117, 0.0F, 1.0F);
                                 handleVal(this.dragGs, var139 + (var142 - var139) * var144);
                                 return true;
                              }

                              var113 += 26.0F * this.U;
                           }

                           if (button == 0 && MaxMinHelper.computeFlag(var12, var13, var94 + 8.0F * this.U, var113, var117 + 12.0F * this.U, 22.0F * this.U)) {
                              gsBlurOn = !gsBlurOn;
                              gsBlur = gsBlurOn ? 1.0F : 0.0F;
                              initGs();
                              return true;
                           }

                           var113 += 22.0F * this.U;
                           float var121 = 34.0F * this.U;
                           float var125 = 14.0F * this.U;
                           float var129 = 4.0F * this.U;
                           float var133 = var94 + var90 - 12.0F * this.U - var121;
                           float var137 = var133 - var129 - var121;
                           float var140 = var113 + (22.0F * this.U - var125) / 2.0F;
                           if (button == 0 && MaxMinHelper.computeFlag(var12, var13, var137, var140, var121, var125)) {
                              if (gs3DAnim) {
                                 gs3DAnim = false;
                                 initGs();
                              }

                              return true;
                           }

                           if (button == 0 && MaxMinHelper.computeFlag(var12, var13, var133, var140, var121, var125)) {
                              if (!gs3DAnim) {
                                 gs3DAnim = true;
                                 initGs();
                              }

                              return true;
                           }

                           return true;
                        }
                     }

                     ModuleCategory[] var91 = new ModuleCategory[]{
                        ModuleCategory.COMBAT, ModuleCategory.MOVEMENT, ModuleCategory.RENDER, ModuleCategory.PLAYER, ModuleCategory.PVE, ModuleCategory.OTHER
                     };
                     String[] var93 = new String[]{"CONFIGS", "MACROS", "ROTATIONS", "GUISET"};
                     float var95 = var17 + this.getFloat3() + 10.0F * this.U;
                     float var98 = 21.0F * this.U;

                     for (ModuleCategory var53 : var91) {
                        if (MaxMinHelper.computeFlag(var12, var13, var16, var95, var19, var98) && button == 0) {
                           currentView = MainMenuScreen.ViewMode.MODULES;
                           showGuiSettings = false;
                           if (this.active != var53 || this.getCurrentView()) {
                              this.runTime();
                              if (this.active != var53) {
                                 this.active = var53;
                                 this.tabSwitchAnim.getTimerUtil();
                              }

                              this.scrollTarget = 0.0F;
                           }

                           return true;
                        }

                        var95 += var98 + 2.0F * this.U;
                     }

                     var95 += 10.0F * this.U;

                     for (String var111 : var93) {
                        boolean var54 = var111.equals("GUISET");
                        boolean var55 = var111.equals("CONFIGS");
                        boolean var56 = var111.equals("MACROS");
                        boolean var57 = var111.equals("ROTATIONS");
                        if (MaxMinHelper.computeFlag(var12, var13, var16, var95, var19, var98) && button == 0) {
                           if (var54) {
                              showGuiSettings = !showGuiSettings;
                              if (showGuiSettings) {
                                 gsPosX = var16 + var14 + 10.0F * this.U;
                                 gsPosY = var17;
                              }

                              initGs();
                              return true;
                           }

                           if (var55) {
                              currentView = MainMenuScreen.ViewMode.CONFIGS;
                              showGuiSettings = false;
                              this.tabSwitchAnim.getTimerUtil();
                              initGs();
                              return true;
                           }

                           if (var56) {
                              currentView = MainMenuScreen.ViewMode.MACROS;
                              showGuiSettings = false;
                              this.tabSwitchAnim.getTimerUtil();
                              initGs();
                              return true;
                           }

                           if (var57) {
                              currentView = MainMenuScreen.ViewMode.ROTATIONS;
                              showGuiSettings = false;
                              this.tabSwitchAnim.getTimerUtil();
                              initGs();
                              return true;
                           }
                        }

                        var95 += var98 + 2.0F * this.U;
                     }

                     float var101 = var16 + var19 + this.getGs();
                     float var104 = var17 + this.getFloat3() + this.getGs();
                     float var108 = var14 - var19 - this.getGs() * 2.0F;
                     float var112 = var15 - this.getFloat3() - this.getGs() * 2.0F;
                     if (currentView == MainMenuScreen.ViewMode.CONFIGS) {
                        this.handleConfig(var16, var17, var14, var15, var101, var104, var108, var112, var12, var13, button);
                        return true;
                     } else if (currentView == MainMenuScreen.ViewMode.MACROS) {
                        this.handleFloat(var16, var17, var14, var15, var101, var104, var108, var112, var12, var13, button);
                        return true;
                     } else if (currentView == MainMenuScreen.ViewMode.ROTATIONS) {
                        return this.handleRotationManagerClick((double)var12, (double)var13, button, var101, var104, var108, var112) ? true : true;
                     } else {
                        if (MaxMinHelper.computeFlag(var12, var13, var16 + var19, var17 + this.getFloat3(), var14 - var19, var15 - this.getFloat3())) {
                           float var116 = (var108 - this.getGs2()) / 2.0F;
                           float[] var119 = new float[]{var104 - this.scrollAnim, var104 - this.scrollAnim};

                           for (Module var127 : FactoryHelper.getInstance().getClear().values()) {
                              if (!var127.getName().equalsIgnoreCase("ClickGui")
                                 && !var127.getName().equalsIgnoreCase("Click GUI")
                                 && !var127.getName().equalsIgnoreCase("Click_GUI")
                                 && !var127.getName().equalsIgnoreCase("Configs")
                                 && !var127.getName().equalsIgnoreCase("ConfigsFeature")
                                 && !(var127 instanceof LegacyModule2)
                                 && this.computeCategory(var127)) {
                                 float var58 = this.computeDesc(var127, var116);
                                 int var59 = var119[0] <= var119[1] ? 0 : 1;
                                 float var60 = var101 + (float)var59 * (var116 + this.getGs2());
                                 float var61 = var119[var59];
                                 var119[var59] += var58 + this.getGs2();
                                 if (MaxMinHelper.computeFlag(var12, var13, var60, var61, var116, var58)) {
                                    float var62 = var60 + 13.0F * this.U;
                                    float var63 = var116 - 26.0F * this.U;
                                    float var64 = 11.5F * this.U;
                                    float var65 = var61 + var64 + this.computeFloat(8.5F) / 2.0F;
                                    String var66 = this.bindingModule == var127
                                       ? "..."
                                       : (var127.getKey() == -1 ? "n/a" : KeyCode.keyName(var127.getKey()).replace("NONE", "n/a"));
                                    float var67 = 12.0F * this.U;
                                    float var68 = Math.max(
                                       19.0F * this.U, Math.min(var18.computeDelegate(var66, this.computeFont(7.2F)) + 8.0F * this.U, var116 * 0.38F)
                                    );
                                    float var69 = var60 + var116 - 13.0F * this.U - var68;
                                    float var70 = var65 - var67 / 2.0F;
                                    if (MaxMinHelper.computeFlag(var12, var13, var69, var70, var68, var67) && button == 0) {
                                       this.bindingModule = var127;
                                       return true;
                                    }

                                    float var71 = var65 + this.computeFloat(8.5F) / 2.0F + 3.5F * this.U;
                                    float var72 = var65 + this.computeFloat(8.5F) / 2.0F;
                                    if (!var127.getDesc().isEmpty()) {
                                       float var73 = this.computeDesc(var18, EngHelper.computeString(var127.getDesc()), var63);
                                       var72 = var71 + var73;
                                    }

                                    float var145 = var72 - var61 + 7.0F * this.U;
                                    if (button == 2 && var13 < var61 + var145) {
                                       this.bindingModule = var127;
                                       return true;
                                    }

                                    boolean var74 = false;

                                    for (Component var76 : var127.getSettings()) {
                                       if (this.visAnim(var127, var76) > 0.001F) {
                                          var74 = true;
                                          break;
                                       }
                                    }

                                    float var146 = var72 + 11.5F * this.U;
                                    if (var74) {
                                       var146 = var72 + 7.0F * this.U + this.max2() + 7.0F * this.U;
                                    }

                                    boolean var147 = var13 < var61 + var145;
                                    boolean var77 = false;

                                    for (Component var79 : var127.getSettings()) {
                                       float var80 = this.visAnim(var127, var79);
                                       if (!(var80 <= 0.01F)) {
                                          float var81 = this.computeOpen(var79, var63);
                                          if (var79 instanceof SliderComponent var82
                                             && var82.getVisible().get()
                                             && button == 0
                                             && MaxMinHelper.computeFlag(
                                                var12, var13, var62 - 4.0F * this.U, var146 + 8.0F * this.U, var63 + 8.0F * this.U, 16.0F * this.U
                                             )) {
                                             this.draggingSlider = var82;
                                             float var83 = MaxMinHelper.computeFloat4((var12 - var62) / var63, 0.0F, 1.0F);
                                             float var84 = MaxMinHelper.computeFloat4(
                                                MaxMinHelper.computeRound2(var82.min + (var82.max - var82.min) * var83, var82.increment), var82.min, var82.max
                                             );
                                             var82.withCachedValue(var84);
                                             var77 = true;
                                          }

                                          if (var79 instanceof ComboComponent var148 && var148.getVisible().get() && button == 0) {
                                             float var155 = 0.0F;
                                             float var160 = 0.0F;

                                             for (String var86 : var148.list) {
                                                String var87 = EngHelper.computeString(var86);
                                                float var88 = var18.computeDelegate(var87, this.computeFont(8.5F)) + 12.0F * this.U;
                                                if (var155 + var88 > var63 && var155 > 0.0F) {
                                                   var155 = 0.0F;
                                                   var160 += 18.0F * this.U;
                                                }

                                                if (MaxMinHelper.computeFlag(
                                                   var12, var13, var62 + var155, var146 + 15.0F * this.U + var160, var88, 14.0F * this.U
                                                )) {
                                                   var148.withValue(var86);
                                                   var77 = true;
                                                }

                                                var155 += var88 + 4.0F * this.U;
                                             }
                                          }

                                          if (var79 instanceof CategoryComponent var149 && var149.getVisible().get() && button == 0) {
                                             float var156 = 0.0F;
                                             float var161 = 0.0F;

                                             for (CheckComponent var167 : var149.getValues()) {
                                                String var169 = EngHelper.computeString(var167.getName());
                                                float var170 = var18.computeDelegate(var169, this.computeFont(8.5F)) + 12.0F * this.U;
                                                if (var156 + var170 > var63 && var156 > 0.0F) {
                                                   var156 = 0.0F;
                                                   var161 += 18.0F * this.U;
                                                }

                                                if (MaxMinHelper.computeFlag(
                                                   var12, var13, var62 + var156, var146 + 15.0F * this.U + var161, var170, 14.0F * this.U
                                                )) {
                                                   var167.withCachedValue(!var167.getCachedValue());
                                                   var77 = true;
                                                }

                                                var156 += var170 + 4.0F * this.U;
                                             }
                                          }

                                          if (var79 instanceof CheckComponent var150
                                             && var150.getVisible().get()
                                             && button == 0
                                             && MaxMinHelper.computeFlag(
                                                var12, var13, var62 - 4.0F * this.U, var146 - 2.0F * this.U, var63 + 8.0F * this.U, 18.0F * this.U
                                             )) {
                                             var150.withCachedValue(!var150.getCachedValue());
                                             var77 = true;
                                          }

                                          if (var79 instanceof SwitchComponent var151
                                             && var151.getVisible().get()
                                             && button == 0
                                             && MaxMinHelper.computeFlag(var12, var13, var62, var146, var63, 20.0F * this.U)) {
                                             var151.runAction();
                                             var77 = true;
                                          }

                                          if (var79 instanceof NumberComponent var152 && var152.getVisible().get()) {
                                             String var157 = this.activeBind == var152
                                                ? "..."
                                                : (
                                                   var152.getCachedValue() != -1 && var152.getCachedValue() != -999
                                                      ? KeyCode.keyName(var152.getCachedValue())
                                                      : "n/a"
                                                );
                                             float var162 = var18.computeDelegate(var157, this.computeFont(7.0F)) + 11.0F * this.U;
                                             if (MaxMinHelper.computeFlag(
                                                var12,
                                                var13,
                                                var62 + var63 - var162 - 2.0F * this.U,
                                                var146 - 2.0F * this.U,
                                                var162 + 4.0F * this.U,
                                                19.0F * this.U
                                             )) {
                                                if (button == 0) {
                                                   this.activeBind = this.activeBind == var152 ? null : var152;
                                                   var77 = true;
                                                } else if (button == 1) {
                                                   var152.withCachedValue(-999);
                                                   this.activeBind = null;
                                                   var77 = true;
                                                }
                                             }
                                          }

                                          if (var79 instanceof TextFieldComponent var153 && var153.getVisible().get() && button == 0) {
                                             String var158 = var153.getValue().isEmpty() ? "..." : var153.getValue();
                                             float var163 = Math.min(var18.computeDelegate(var158, this.computeFont(7.0F)) + 11.0F * this.U, var63 * 0.55F);
                                             if (MaxMinHelper.computeFlag(
                                                var12,
                                                var13,
                                                var62 + var63 - var163 - 2.0F * this.U,
                                                var146 - 2.0F * this.U,
                                                var163 + 4.0F * this.U,
                                                19.0F * this.U
                                             )) {
                                                this.activeString = var153;
                                                this.stringBuffer = var153.getValue();
                                                var77 = true;
                                             }
                                          }

                                          if (var79 instanceof ColorPickerComponent var154 && var154.getVisible().get() && button == 0) {
                                             if (MaxMinHelper.computeFlag(var12, var13, var62, var146, var63, 16.0F * this.U)) {
                                                var154.pickerOpen = !var154.pickerOpen;
                                                var77 = true;
                                             } else if (var154.pickerOpen) {
                                                float var159 = var62 + 7.0F * this.U;
                                                float var164 = var146 + 16.0F * this.U;
                                                float var166 = var63 - 14.0F * this.U;
                                                float var168 = 38.0F * this.U;
                                                if (MaxMinHelper.computeFlag(
                                                   var12, var13, var159 - 2.0F * this.U, var164 - 2.0F * this.U, var166 + 4.0F * this.U, var168 + 4.0F * this.U
                                                )) {
                                                   this.draggingColor = var154;
                                                   var77 = true;
                                                }
                                             }
                                          }

                                          var146 += var81 * var80 + 5.0F * this.U * var80;
                                       }
                                    }

                                    if (var77) {
                                       return true;
                                    }

                                    if (var147) {
                                       if (var127.getName().equalsIgnoreCase("ESP") && button == 0 && mc != null) {
                                          mc.setScreen(new ESPConfiguratorScreen(this));
                                          return true;
                                       }

                                       if (button == 0 || button == 1) {
                                          var127.handleFlag(!var127.isEnabled());
                                       }

                                       return true;
                                    }

                                    return true;
                                 }
                              }
                           }
                        }

                        return super.mouseClicked(mouseXRaw, mouseYRaw, button);
                     }
                  }
               }
            }
         }
      }
   }

   public void handleFloat(float wx, float wy, float ww, float wh, float cx, float cy, float cw, float chh, float mouseX, float mouseY, int button) {
      List var12 = MacroStore.getInstance().getList();
      if (this.typingMacroModal) {
         float var44 = 276.0F * this.U;
         float var46 = 154.0F * this.U;
         float var48 = wx + (ww - var44) / 2.0F;
         float var50 = wy + (wh - var46) / 2.0F;
         float var52 = var44 - 32.0F * this.U;
         float var53 = 20.0F * this.U;
         float var54 = var48 + 16.0F * this.U;
         float var55 = var50 + 30.0F * this.U;
         if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var54, var55, var52, var53)) {
            this.macroModalFocusField = 0;
            this.newMacroListeningKey = false;
         } else {
            float var56 = var55 + var53 + 8.0F * this.U;
            float var57 = 20.0F * this.U;
            float var58 = 2.0F * this.U;
            float var59 = (var52 - var58 * 2.0F) / 3.0F;
            float var60 = var57 - var58 * 2.0F;

            for (int var61 = 0; var61 < 3; var61++) {
               float var63 = var54 + var58 + (float)var61 * var59;
               if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var63, var56 + var58, var59, var60)) {
                  this.newMacroActionType = var61;
                  TotalHelper.handleInt2(0, 1);
                  return;
               }
            }

            float var62 = var56 + var57 + 8.0F * this.U;
            if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var54, var62, var52, var53)) {
               this.macroModalFocusField = 1;
               this.newMacroListeningKey = false;
            } else {
               float var64 = var62 + var53 + 12.0F * this.U;
               float var65 = 22.0F * this.U;
               boolean var66 = EngHelper.getInstance().check();
               String var67 = this.newMacroListeningKey
                  ? "..."
                  : (this.newMacroKey != -1 && this.newMacroKey != -999 ? KeyCode.keyName(this.newMacroKey).replace("NONE", "n/a") : "n/a");
               String var68 = (var66 ? "Бинд: " : "Key: ") + var67;
               float var69 = Math.max(54.0F * this.U, IconFontRegistry.fontRegistry.computeDelegate(var68, this.computeFont(7.2F)) + 16.0F * this.U);
               if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var54, var64, var69, var65)) {
                  this.newMacroListeningKey = !this.newMacroListeningKey;
               } else {
                  float var70 = var52 - var69 - 8.0F * this.U;
                  float var71 = (var70 - 6.0F * this.U) / 2.0F;
                  float var72 = var54 + var69 + 8.0F * this.U;
                  float var73 = var72 + var71 + 6.0F * this.U;
                  if (button == 0) {
                     if (MaxMinHelper.computeFlag(mouseX, mouseY, var72, var64, var71, var65)) {
                        this.typingMacroModal = false;
                        this.newMacroListeningKey = false;
                        this.newMacroNameBuffer = "";
                        this.newMacroParamBuffer = "";
                        return;
                     }

                     if (MaxMinHelper.computeFlag(mouseX, mouseY, var73, var64, var71, var65)) {
                        String var38 = this.newMacroNameBuffer.trim().isEmpty() ? "Macro #" + (var12.size() + 1) : this.newMacroNameBuffer.trim();
                        MacroEngine.Macro var39 = new MacroEngine.Macro(var38, this.newMacroKey);

                        MacroEngine.ActionType var40 = switch (this.newMacroActionType) {
                           case 0 -> MacroEngine.ActionType.USE_COMMAND;
                           case 1 -> MacroEngine.ActionType.SEND_MESSAGE;
                           default -> MacroEngine.ActionType.SWAP_SLOT_TO_ITEM;
                        };
                        MacroEngine.MacroStep var41 = new MacroEngine.MacroStep(var40);
                        String var42 = this.newMacroParamBuffer.trim();
                        if (var40 == MacroEngine.ActionType.USE_COMMAND) {
                           var41.getParams().addProperty("command", var42.isEmpty() ? "spawn" : (var42.startsWith("/") ? var42.substring(1) : var42));
                        } else if (var40 == MacroEngine.ActionType.SEND_MESSAGE) {
                           var41.getParams().addProperty("message", var42.isEmpty() ? "Hello" : var42);
                        } else {
                           var41.getParams().addProperty("item", var42.isEmpty() ? "totem_of_undying" : var42);
                        }

                        var39.getSteps().add(var41);
                        MacroStore.getInstance().handleAdd(var39);
                        this.typingMacroModal = false;
                        this.newMacroListeningKey = false;
                        this.newMacroNameBuffer = "";
                        this.newMacroParamBuffer = "";
                        this.newMacroKey = -1;
                        TotalHelper.handleInt2(1, 1);
                        return;
                     }
                  }
               }
            }
         }
      } else if (var12.isEmpty()) {
         float var43 = cy + (chh - 40.0F * this.U) / 2.0F;
         float var45 = 140.0F * this.U;
         float var47 = 23.0F * this.U;
         float var49 = cx + (cw - var45) / 2.0F;
         float var51 = var43 + 36.0F * this.U;
         if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var49, var51, var45, var47)) {
            this.typingMacroModal = true;
            this.macroModalFocusField = 0;
            this.newMacroNameBuffer = "";
            this.newMacroParamBuffer = "";
            this.newMacroKey = -1;
            this.newMacroListeningKey = false;
            this.newMacroActionType = 0;
         }
      } else {
         float var13 = 130.0F * this.U;
         float var14 = 20.0F * this.U;
         float var15 = cx + (cw - var13) / 2.0F;
         float var16 = cy + chh - var14 - 4.0F * this.U;
         if (button == 0 && MaxMinHelper.computeFlag(mouseX, mouseY, var15, var16, var13, var14)) {
            this.typingMacroModal = true;
            this.macroModalFocusField = 0;
            this.newMacroNameBuffer = "";
            this.newMacroParamBuffer = "";
            this.newMacroKey = -1;
            this.newMacroListeningKey = false;
            this.newMacroActionType = 0;
         } else {
            float var17 = cy + 4.0F * this.U;
            float var18 = var16 - 6.0F * this.U;
            float var19 = var18 - var17;
            if (MaxMinHelper.computeFlag(mouseX, mouseY, cx, var17, cw, var19)) {
               byte var20 = 2;
               float var21 = 8.0F * this.U;
               float var22 = 6.0F * this.U;
               float var23 = (cw - var21) / (float)var20;
               float var24 = 36.0F * this.U;

               for (int var25 = 0; var25 < var12.size(); var25++) {
                  MacroEngine.Macro var26 = (MacroEngine.Macro)var12.get(var25);
                  int var27 = var25 % var20;
                  int var28 = var25 / var20;
                  float var29 = cx + (float)var27 * (var23 + var21);
                  float var30 = var17 - this.macroScrollAnim + (float)var28 * (var24 + var22);
                  if (!(var30 + var24 < var17 - 10.0F * this.U) && !(var30 > var18 + 10.0F * this.U)) {
                     float var31 = 17.0F * this.U;
                     float var32 = var30 + (var24 - var31) / 2.0F;
                     float var33 = 18.0F * this.U;
                     float var34 = var29 + var23 - 7.0F * this.U - var33;
                     if (MaxMinHelper.computeFlag(mouseX, mouseY, var34, var32, var33, var31) && button == 0) {
                        MacroStore.getInstance().removeMacro(var26.getName());
                        TotalHelper.handleInt2(1, 1);
                        return;
                     }

                     String var35 = this.listeningMacro == var26
                        ? "..."
                        : (var26.getKey() != -1 && var26.getKey() != -999 ? KeyCode.keyName(var26.getKey()).replace("NONE", "n/a") : "n/a");
                     float var36 = Math.max(22.0F * this.U, IconFontRegistry.fontRegistry.computeDelegate(var35, this.computeFont(6.5F)) + 10.0F * this.U);
                     float var37 = var34 - 5.0F * this.U - var36;
                     if (MaxMinHelper.computeFlag(mouseX, mouseY, var37, var32, var36, var31) && button == 0) {
                        this.listeningMacro = this.listeningMacro == var26 ? null : var26;
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   public void handleConfig(float wx, float wy, float ww, float wh, float cx, float cy, float cw, float chh, float mouseX, float mouseY, int button) {
      float var12 = cy;
      float var13 = chh;
      float var14 = 160.0F * this.U;
      if (this.typingConfigName) {
         float var36 = 195.0F * this.U;
         float var37 = 92.0F * this.U;
         float var38 = wx + (ww - var36) / 2.0F;
         float var39 = wy + (wh - var37) / 2.0F;
         float var40 = var36 - 24.0F * this.U;
         float var41 = var38 + 12.0F * this.U;
         float var42 = (var40 - 8.0F * this.U) / 2.0F;
         float var43 = 18.0F * this.U;
         float var44 = var39 + 60.0F * this.U;
         if (button == 0) {
            if (!MaxMinHelper.computeFlag(mouseX, mouseY, var38, var39, var36, var37)) {
               this.typingConfigName = false;
               this.newConfigBuffer = "";
               return;
            }

            if (MaxMinHelper.computeFlag(mouseX, mouseY, var41, var44, var42, var43)) {
               this.typingConfigName = false;
               this.newConfigBuffer = "";
               return;
            }

            if (MaxMinHelper.computeFlag(mouseX, mouseY, var41 + var42 + 8.0F * this.U, var44, var42, var43)) {
               if (!this.newConfigBuffer.trim().isEmpty()) {
                  String var45 = this.newConfigBuffer.trim();
                  ConfigManager.getInstance().setCurrentConfig(var45);
                  this.typingConfigName = false;
                  this.newConfigBuffer = "";
                  Animation var46 = this.chipAnim("cfg_entry:" + var45);
                  var46.getTimerUtil();
                  var46.setDirection(AnimationDirection.FORWARDS);
                  TotalHelper.handleInt2(1, 1);
                  NotificationManager.handleInfo("Saved config: " + var45);
               }

               return;
            }
         }
      } else if (MaxMinHelper.computeFlag(mouseX, mouseY, cx, cy, var14, chh) && button == 0) {
         this.typingConfigName = true;
         this.newConfigBuffer = "";
         TotalHelper.handleInt2(1, 1);
      } else {
         float var18 = cx + var14 + 12.0F * this.U;
         float var19 = cw - var14 - 12.0F * this.U;
         float var20 = cy - this.configScrollAnim;

         for (String var23 : ConfigManager.getInstance().getConfigsNames()) {
            float var24 = 36.0F * this.U;
            float var25 = var20;
            var20 += var24 + 6.0F * this.U;
            if (!(var25 + var24 < var12 - 10.0F * this.U) && !(var25 > var12 + var13 + 10.0F * this.U)) {
               float var26 = 17.0F * this.U;
               float var27 = var25 + (var24 - var26) / 2.0F;
               float var28 = 18.0F * this.U;
               float var29 = var18 + var19 - 8.0F * this.U - var28;
               if (button == 0
                  && MaxMinHelper.computeFlag(mouseX, mouseY, var29 - 3.0F * this.U, var27 - 3.0F * this.U, var28 + 6.0F * this.U, var26 + 6.0F * this.U)) {
                  ConfigManager.getInstance().handleConfig(var23);
                  TotalHelper.run15();
                  NotificationManager.handleInfo("Deleted config: " + var23);
                  return;
               }

               String var30 = EngHelper.computeString("Save");
               float var31 = Math.max(36.0F * this.U, IconFontRegistry.fontRegistry.computeDelegate(var30, this.computeFont(6.8F)) + 12.0F * this.U);
               float var32 = var29 - 5.0F * this.U - var31;
               if (button == 0
                  && MaxMinHelper.computeFlag(mouseX, mouseY, var32 - 3.0F * this.U, var27 - 3.0F * this.U, var31 + 6.0F * this.U, var26 + 6.0F * this.U)) {
                  ConfigManager.getInstance().setCurrentConfig(var23);
                  TotalHelper.handleInt2(1, 1);
                  NotificationManager.handleInfo("Saved config: " + var23);
                  return;
               }

               String var33 = EngHelper.computeString("Load");
               float var34 = Math.max(38.0F * this.U, IconFontRegistry.fontRegistry.computeDelegate(var33, this.computeFont(6.8F)) + 12.0F * this.U);
               float var35 = var32 - 5.0F * this.U - var34;
               if (button == 0
                  && MaxMinHelper.computeFlag(mouseX, mouseY, var35 - 3.0F * this.U, var27 - 3.0F * this.U, var34 + 6.0F * this.U, var26 + 6.0F * this.U)) {
                  ConfigManager.getInstance().loadJson(var23);
                  TotalHelper.handleInt2(0, 1);
                  NotificationManager.handleInfo("Loaded config: " + var23);
                  return;
               }
            }
         }
      }
   }

   public boolean mouseDragged(double mouseXRaw, double mouseYRaw, int button, double deltaX, double deltaY) {
      float var10 = this.getTime2();
      float var11 = this.getTime3();
      int var12 = (int)((float)mc.getWindow().getScaledWidth() / this.scaleFix);
      int var13 = (int)((float)mc.getWindow().getScaledHeight() / this.scaleFix);
      float var14 = (float)var12 / 2.0F;
      float var15 = (float)var13 / 2.0F;
      this.lastMouseX = (mouseXRaw / (double)this.scaleFix - (double)var14) / (double)var10 + (double)var14;
      this.lastMouseY = (mouseYRaw / (double)this.scaleFix - (double)var15 - (double)var11) / (double)var10 + (double)var15;
      this.handleMin(var12, var13);
      float var16 = this.getU();
      float var17 = this.getFloat();
      float var18 = (float)var12 / 2.0F - var16 / 2.0F;
      float var19 = (float)var13 / 2.0F - var17 / 2.0F;
      if (currentView == MainMenuScreen.ViewMode.ROTATIONS) {
         float var20 = Math.max(130.0F * this.U, Math.min(160.0F * this.U, var16 * 0.23F));
         float var21 = var18 + var20 + this.getGs();
         float var22 = var19 + this.getFloat3() + this.getGs();
         float var23 = var16 - var20 - this.getGs() * 2.0F;
         float var24 = var17 - this.getFloat3() - this.getGs() * 2.0F;
         this.handleDouble(this.lastMouseX, this.lastMouseY, var21, var22, var23, var24);
      }

      return super.mouseDragged(mouseXRaw, mouseYRaw, button, deltaX, deltaY);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (this.dragGs != null || this.gsDraggingHeader) {
         initGs();
      }

      this.draggingSlider = null;
      this.draggingColor = null;
      this.dragGs = null;
      this.gsDraggingHeader = false;
      this.runDragging();
      int var6 = -100 + button;
      ClickGUI var7 = ClickGUI.getInstance();
      if (var7 != null && var6 == var7.getBind()) {
         this.toggleKeyReleased = true;
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.exit) {
         return true;
      } else if (this.bindingModule != null) {
         boolean var15 = keyCode == 256 || keyCode == 261;
         this.bindingModule.setKey(var15 ? -1 : keyCode);
         this.bindingModule = null;
         return true;
      } else if (this.activeBind != null) {
         boolean var14 = keyCode == 256 || keyCode == 261;
         this.activeBind.withCachedValue(var14 ? -999 : keyCode);
         this.activeBind = null;
         return true;
      } else if (this.listeningMacro != null) {
         boolean var13 = keyCode == 256 || keyCode == 261;
         this.listeningMacro.setKey(var13 ? -1 : keyCode);
         MacroStore.getInstance().closeFile();
         this.listeningMacro = null;
         return true;
      } else if (this.newMacroListeningKey) {
         boolean var12 = keyCode == 256 || keyCode == 261;
         this.newMacroKey = var12 ? -1 : keyCode;
         this.newMacroListeningKey = false;
         return true;
      } else if (this.typingMacroModal) {
         if (keyCode == 256) {
            this.typingMacroModal = false;
            this.newMacroListeningKey = false;
            return true;
         } else if (keyCode == 258) {
            this.macroModalFocusField = this.macroModalFocusField == 0 ? 1 : 0;
            return true;
         } else if (keyCode != 257 && keyCode != 335) {
            if (keyCode != 259) {
               return true;
            } else {
               if (this.macroModalFocusField == 0 && !this.newMacroNameBuffer.isEmpty()) {
                  this.newMacroNameBuffer = this.newMacroNameBuffer.substring(0, this.newMacroNameBuffer.length() - 1);
               } else if (this.macroModalFocusField == 1 && !this.newMacroParamBuffer.isEmpty()) {
                  this.newMacroParamBuffer = this.newMacroParamBuffer.substring(0, this.newMacroParamBuffer.length() - 1);
               }

               return true;
            }
         } else {
            if (this.macroModalFocusField == 0) {
               this.macroModalFocusField = 1;
            } else {
               String var11 = this.newMacroNameBuffer.trim().isEmpty() ? "Macro" : this.newMacroNameBuffer.trim();
               MacroEngine.Macro var17 = new MacroEngine.Macro(var11, this.newMacroKey);

               MacroEngine.ActionType var18 = switch (this.newMacroActionType) {
                  case 0 -> MacroEngine.ActionType.USE_COMMAND;
                  case 1 -> MacroEngine.ActionType.SEND_MESSAGE;
                  default -> MacroEngine.ActionType.SWAP_SLOT_TO_ITEM;
               };
               MacroEngine.MacroStep var8 = new MacroEngine.MacroStep(var18);
               String var9 = this.newMacroParamBuffer.trim();
               if (var18 == MacroEngine.ActionType.USE_COMMAND) {
                  var8.getParams().addProperty("command", var9.isEmpty() ? "spawn" : (var9.startsWith("/") ? var9.substring(1) : var9));
               } else if (var18 == MacroEngine.ActionType.SEND_MESSAGE) {
                  var8.getParams().addProperty("message", var9.isEmpty() ? "Hello" : var9);
               } else {
                  var8.getParams().addProperty("item", var9.isEmpty() ? "totem_of_undying" : var9);
               }

               var17.getSteps().add(var8);
               MacroStore.getInstance().handleAdd(var17);
               this.typingMacroModal = false;
               this.newMacroListeningKey = false;
               this.newMacroNameBuffer = "";
               this.newMacroParamBuffer = "";
               this.newMacroKey = -1;
               TotalHelper.handleInt2(1, 1);
            }

            return true;
         }
      } else if (this.typingConfigName) {
         if (keyCode != 257 && keyCode != 335) {
            if (keyCode == 256) {
               this.typingConfigName = false;
               this.newConfigBuffer = "";
               return true;
            } else if (keyCode == 259 && !this.newConfigBuffer.isEmpty()) {
               this.newConfigBuffer = this.newConfigBuffer.substring(0, this.newConfigBuffer.length() - 1);
               return true;
            } else {
               return true;
            }
         } else {
            if (!this.newConfigBuffer.trim().isEmpty()) {
               String var10 = this.newConfigBuffer.trim();
               ConfigManager.getInstance().setCurrentConfig(var10);
               this.typingConfigName = false;
               this.newConfigBuffer = "";
               Animation var16 = this.chipAnim("cfg_entry:" + var10);
               var16.getTimerUtil();
               var16.setDirection(AnimationDirection.FORWARDS);
               TotalHelper.handleInt2(1, 1);
            }

            return true;
         }
      } else if (searchActive) {
         if (keyCode == 256) {
            if (!searchQuery.isEmpty()) {
               this.runTime();
            } else {
               searchActive = false;
            }
         } else if (keyCode == 257 || keyCode == 335) {
            searchActive = false;
         } else if (keyCode == 259 && !searchQuery.isEmpty()) {
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            this.runAnim();
         }

         return true;
      } else if (this.activeString != null) {
         if (keyCode == 257 || keyCode == 335) {
            this.activeString.withCachedValue(this.stringBuffer);
            this.activeString = null;
         } else if (keyCode == 256) {
            this.activeString = null;
         } else if (keyCode == 259 && !this.stringBuffer.isEmpty()) {
            this.stringBuffer = this.stringBuffer.substring(0, this.stringBuffer.length() - 1);
         }

         return true;
      } else {
         LegacyModule2 var5 = FactoryHelper.getInstance().getClear().get(LegacyModule2.class);
         ClickGUI var6 = ClickGUI.getInstance();
         boolean var7 = var5 != null && keyCode == var5.getKey() || var6 != null && keyCode == var6.getBind();
         if (var7) {
            if (this.toggleKeyReleased && System.currentTimeMillis() - this.openStartTime > 250L) {
               this.close();
            }

            return true;
         } else if (keyCode == 256) {
            this.close();
            return true;
         } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
         }
      }
   }

   public boolean charTyped(char chr, int modifiers) {
      if (this.exit) {
         return true;
      } else if (this.typingMacroModal) {
         if (this.macroModalFocusField == 0) {
            if (this.newMacroNameBuffer.length() < 24 && chr >= ' ' && chr != 167) {
               this.newMacroNameBuffer = this.newMacroNameBuffer + chr;
            }
         } else if (this.macroModalFocusField == 1 && this.newMacroParamBuffer.length() < 80 && chr >= ' ' && chr != 167) {
            this.newMacroParamBuffer = this.newMacroParamBuffer + chr;
         }

         return true;
      } else if (this.typingConfigName) {
         if (this.newConfigBuffer.length() < 30 && (Character.isLetterOrDigit(chr) || chr == '_' || chr == '-')) {
            this.newConfigBuffer = this.newConfigBuffer + chr;
         }

         return true;
      } else if (searchActive) {
         if (searchQuery.length() < 24) {
            searchQuery = searchQuery + chr;
            this.runAnim();
         }

         return true;
      } else if (this.activeString == null) {
         return super.charTyped(chr, modifiers);
      } else {
         String var3 = String.valueOf(chr);
         if (!this.activeString.isOnlyNumber() || var3.matches("[0-9.,-]+")) {
            this.stringBuffer = this.stringBuffer + var3;
         }

         return true;
      }
   }

   public boolean mouseScrolled(double mouseXRaw, double mouseYRaw, double horizontalAmount, double verticalAmount) {
      if (this.exit) {
         return true;
      } else {
         float var9 = this.getTime2();
         float var10 = this.getTime3();
         int var11 = (int)((float)mc.getWindow().getScaledWidth() / this.scaleFix);
         int var12 = (int)((float)mc.getWindow().getScaledHeight() / this.scaleFix);
         float var13 = (float)var11 / 2.0F;
         float var14 = (float)var12 / 2.0F;
         float var15 = (float)((mouseXRaw / (double)this.scaleFix - (double)var13) / (double)var9 + (double)var13);
         float var16 = (float)((mouseYRaw / (double)this.scaleFix - (double)var14 - (double)var10) / (double)var9 + (double)var14);
         this.handleMin(var11, var12);
         float var17 = this.getU();
         float var18 = this.getFloat();
         float var19 = (float)var11 / 2.0F - var17 / 2.0F;
         float var20 = (float)var12 / 2.0F - var18 / 2.0F;
         float var21 = var19 + this.getFloat2();
         float var22 = var20 + this.getFloat3();
         float var23 = var17 - this.getFloat2();
         float var24 = var18 - this.getFloat3();
         if (currentView == MainMenuScreen.ViewMode.ROTATIONS && MaxMinHelper.computeFlag(var15, var16, var21, var22, var23, var24)) {
            this.rotNoiseScrollTarget = MaxMinHelper.computeFloat4(
               (float)((double)this.rotNoiseScrollTarget - verticalAmount * 25.0 * (double)this.U), 0.0F, this.rotNoiseMaxScroll
            );
            return true;
         } else if (currentView == MainMenuScreen.ViewMode.MACROS && MaxMinHelper.computeFlag(var15, var16, var21, var22, var23, var24)) {
            this.macroScrollTarget = MaxMinHelper.computeFloat4(
               (float)((double)this.macroScrollTarget - verticalAmount * 25.0 * (double)this.U), 0.0F, this.macroMaxScroll
            );
            return true;
         } else if (currentView == MainMenuScreen.ViewMode.CONFIGS && MaxMinHelper.computeFlag(var15, var16, var21, var22, var23, var24)) {
            this.configScrollTarget = MaxMinHelper.computeFloat4(
               (float)((double)this.configScrollTarget - verticalAmount * 25.0 * (double)this.U), 0.0F, this.configMaxScroll
            );
            return true;
         } else if (MaxMinHelper.computeFlag(var15, var16, var21, var22, var23, var24)) {
            this.scrollTarget = MaxMinHelper.computeFloat4((float)((double)this.scrollTarget - verticalAmount * 30.0 * (double)this.U), 0.0F, this.maxScroll);
            return true;
         } else {
            return super.mouseScrolled(mouseXRaw, mouseYRaw, horizontalAmount, verticalAmount);
         }
      }
   }

   public void close() {
      if (!this.exit) {
         initGs();
         RotationProfileManager.getInstance().closeFile();
         if (gs3DAnim && mc.world != null && mc.player != null) {
            MenuAnimation.initCos(this);
            this.exit = false;
            mc.setScreen(null);
         } else {
            this.exit = true;
            this.closeStartTime = System.currentTimeMillis();
         }
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      LegacyModule2 var4 = FactoryHelper.getInstance().getClear().get(LegacyModule2.class);
      ClickGUI var5 = ClickGUI.getInstance();
      if (var4 != null && keyCode == var4.getKey() || var5 != null && keyCode == var5.getBind()) {
         this.toggleKeyReleased = true;
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   public static int lambda$handleRotationManagerClick$3(RotationProfileManager.CurvePoint p1, RotationProfileManager.CurvePoint p2) {
      return Float.compare(p1.time, p2.time);
   }

   public static Animation lambda$visAnim$2(Component k) {
      return new Direction2(300, 1.0);
   }

   public static float[] lambda$smooth$1(float target, String k) {
      return new float[]{target};
   }

   public static Animation lambda$chipAnim$0(String k) {
      return new Direction2(300, 1.0);
   }

   static {
      initGs2();
      MenuAnimation.initInit();
   }

   public static enum ViewMode {
      MODULES,
      CONFIGS,
      MACROS,
      ROTATIONS;

      public static MainMenuScreen.ViewMode[] $values() {
         return new MainMenuScreen.ViewMode[]{MODULES, CONFIGS, MACROS, ROTATIONS};
      }
   }
}
