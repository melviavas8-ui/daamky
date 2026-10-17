package dev.daamky.client.notification;

import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.AnimatedValue;
import dev.daamky.client.internal.ClientFont;
import dev.daamky.client.internal.HudElement;
import dev.daamky.client.internal.InterfaceMetrics;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.theme.ThemeManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class NotificationManager extends HudElement {
   public static final List<NotificationManager.Notification> list = new CopyOnWriteArrayList<>();
   public static final Pattern pattern = Pattern.compile(
      "(?i)(зачарованное золотое яблоко|зачарованное яблоко|enchanted golden apple|золотое яблоко|golden apple|зелье(?::|\\s+[а-яА-ЯёЁa-zA-Z0-9IVX]+)?|зелья|potion|тотем бессмертия|тотем|totem|хорус|chorus fruit|хоруса|жемчуг|pearl|стрела|стрелы|arrow|цель|ты ешь|ты пьёшь|твой)"
   );
   public static boolean flag = false;
   public static boolean mnaev = false;
   public static final boolean[] flags = new boolean[4];
   public static final boolean[] flags2 = new boolean[4];
   public boolean flag2 = false;
   public static long mceceh = 0L;

   public NotificationManager() {
      super(0.0F, 0.0F);
      this.setEnabled(true);
   }

   @Override
   public String getName() {
      return "Notifications";
   }

   public static void initAdd(String message, NotificationManager.Notification.Type type, long duration) {
      list.add(new NotificationManager.Notification(message, type, duration));
   }

   public static void handleInfo(String message) {
      initAdd(message, NotificationManager.Notification.Type.INFO, 3000L);
   }

   public static void handleInfo(String message, ItemStack itemStack) {
      initAdd(message, NotificationManager.Notification.Type.INFO, itemStack, 3000L);
   }

   public static void handleSuccess(String message) {
      initAdd(message, NotificationManager.Notification.Type.SUCCESS, 3000L);
   }

   public static void handleWarning(String message) {
      initAdd(message, NotificationManager.Notification.Type.WARNING, 3000L);
   }

   public static void handleWarning(String message, ItemStack itemStack) {
      initAdd(message, NotificationManager.Notification.Type.WARNING, itemStack, 3000L);
   }

   public static void handleError(String message) {
      initAdd(message, NotificationManager.Notification.Type.ERROR, 3000L);
   }

   public static void initNext(String module, boolean on) {
      synchronized (list) {
         for (NotificationManager.Notification var4 : list) {
            if (var4.isToggle() && module.equals(var4.getMessage())) {
               var4.setToggledOn(on);
               var4.setTimestamp(System.currentTimeMillis());
               var4.setDuration(on ? 3000L : 1200L);
               return;
            }
         }

         NotificationManager.Notification var7 = new NotificationManager.Notification(
            module, on ? NotificationManager.Notification.Type.TOGGLE_ON : NotificationManager.Notification.Type.TOGGLE_OFF, on ? 3000L : 1200L
         );
         var7.setToggle(true);
         var7.setToggledOn(on);
         var7.getAnimatedValue2().handleStart(on ? 0.0 : 1.0);
         list.add(var7);
      }
   }

   public static void initNext(String tag, String message, NotificationManager.Notification.Type type) {
      if (tag != null) {
         synchronized (list) {
            for (NotificationManager.Notification var5 : list) {
               if (tag.equals(var5.getTag())) {
                  var5.setMessage(message);
                  var5.setType(type);
                  var5.setTimestamp(System.currentTimeMillis());
                  var5.setDuration(999999L);
                  return;
               }
            }

            NotificationManager.Notification var8 = new NotificationManager.Notification(message, type, 999999L);
            var8.setTag(tag);
            list.add(var8);
         }
      }
   }

   public static void initNext(String tag, String endMessage, NotificationManager.Notification.Type endType, long duration) {
      if (tag != null) {
         synchronized (list) {
            for (NotificationManager.Notification var7 : list) {
               if (tag.equals(var7.getTag())) {
                  var7.setTag(null);
                  if (endMessage != null) {
                     var7.setMessage(endMessage);
                  }

                  if (endType != null) {
                     var7.setType(endType);
                  }

                  var7.setTimestamp(System.currentTimeMillis());
                  var7.setDuration(duration);
                  return;
               }
            }

            if (endMessage != null) {
               list.add(new NotificationManager.Notification(endMessage, endType != null ? endType : NotificationManager.Notification.Type.SUCCESS, duration));
            }
         }
      }
   }

   public static void handleNext(String tag) {
      if (tag != null) {
         synchronized (list) {
            for (NotificationManager.Notification var3 : list) {
               if (tag.equals(var3.getTag())) {
                  var3.setDuration(0L);
                  var3.setTag(null);
               }
            }
         }
      }
   }

   public static void runFill() {
      long var0 = System.currentTimeMillis();
      if (var0 - mceceh >= 500L) {
         mceceh = var0;
         MinecraftClient var2 = MinecraftClient.getInstance();
         if (var2.player == null) {
            flag = false;
            mnaev = false;
            Arrays.fill(flags, false);
            Arrays.fill(flags2, false);
         } else {
            float var3 = var2.player.getMaxHealth();
            if (var3 > 0.0F) {
               float var4 = var2.player.getHealth();
               float var5 = var4 / var3;
               if (var5 < 0.2F) {
                  if (!mnaev) {
                     initAdd("Здоровье критически низкое: " + (int)(var5 * 100.0F) + "%!", NotificationManager.Notification.Type.ERROR, 4000L);
                     mnaev = true;
                     flag = true;
                  }
               } else if (var5 < 0.5F && !flag) {
                  initAdd("Здоровье на исходе: " + (int)(var5 * 100.0F) + "%!", NotificationManager.Notification.Type.WARNING, 4000L);
                  flag = true;
               }

               if (var5 >= 0.55F) {
                  flag = false;
                  mnaev = false;
               } else if (var5 >= 0.25F) {
                  mnaev = false;
               }
            }

            for (int var7 = 0; var7 < 4; var7++) {
               ItemStack var8 = (ItemStack)var2.player.getInventory().armor.get(var7);
               if (!var8.isEmpty() && var8.isDamageable()) {
                  float var6 = (float)(var8.getMaxDamage() - var8.getDamage()) / (float)var8.getMaxDamage();
                  if (var6 < 0.2F) {
                     if (!flags2[var7]) {
                        initAdd(
                           var8.getName().getString() + " прочность критически низкая: " + (int)(var6 * 100.0F) + "%!",
                           NotificationManager.Notification.Type.ERROR,
                           4000L
                        );
                        flags2[var7] = true;
                        flags[var7] = true;
                     }
                  } else if (var6 < 0.5F && !flags[var7]) {
                     initAdd(
                        var8.getName().getString() + " прочность низкая: " + (int)(var6 * 100.0F) + "%!",
                        NotificationManager.Notification.Type.WARNING,
                        4000L
                     );
                     flags[var7] = true;
                  }

                  if (var6 >= 0.55F) {
                     flags[var7] = false;
                     flags2[var7] = false;
                  } else if (var6 >= 0.25F) {
                     flags2[var7] = false;
                  }
               } else {
                  flags[var7] = false;
                  flags2[var7] = false;
               }
            }
         }
      }
   }

   @Override
   public void render(MatrixStack matrixStack) {
      runFill();
      float var2 = (float)mc.getWindow().getScaledWidth();
      float var3 = (float)mc.getWindow().getScaledHeight();
      float var4 = this.getAutoHelper().getScale2();
      if (var4 == 0.0F) {
         var4 = this.computeFloat(120.0F);
      }

      this.getAutoHelper().setX(var2 / 2.0F - var4 / 2.0F);
      if (!this.flag2 && var2 > 0.0F && var3 > 0.0F) {
         boolean var5 = this.getAutoHelper().getY() != 0.0F;
         if (!var5) {
            this.getAutoHelper().setY(var3 - this.computeFloat(65.0F));
         }

         this.flag2 = true;
      }

      boolean var60 = mc.currentScreen instanceof ChatScreen || mc.currentScreen != null && mc.currentScreen.getClass().getSimpleName().contains("ClickGUI");
      if (var60) {
         synchronized (list) {
            boolean var7 = false;

            for (NotificationManager.Notification var9 : list) {
               if ("Пример уведомления клиента".equals(var9.getMessage())) {
                  var7 = true;
                  var9.setTimestamp(System.currentTimeMillis());
                  break;
               }
            }

            if (!var7) {
               list.add(new NotificationManager.Notification("Пример уведомления клиента", NotificationManager.Notification.Type.INFO, 999999L));
            }
         }
      } else {
         synchronized (list) {
            for (NotificationManager.Notification var64 : list) {
               if ("Пример уведомления клиента".equals(var64.getMessage()) && var64.getDuration() == 999999L) {
                  var64.setDuration(0L);
               }
            }
         }
      }

      long var62 = System.currentTimeMillis();
      synchronized (list) {
         list.removeIf((p0) -> this.lambda$render$0(var62, p0));
      }

      if (list.isEmpty()) {
         this.getAutoHelper().setWidth(0.0F);
         this.getAutoHelper().setHeight(0.0F);
      } else {
         float var66 = this.computeFloat(6.0F);
         float var67 = this.computeFloat(7.0F);
         float var10 = this.computeFloat(12.0F);
         float var11 = this.computeFloat(6.5F);
         float var12 = this.computeFloat(6.0F);
         boolean var13 = this.getAutoHelper().getMergeCards();
         float var14 = var13 ? 0.0F : this.computeFloat(3.0F);
         float var15 = this.computeFloat(3.0F);
         float var16 = this.computeFloat(0.5F);
         float var17 = this.computeFloat(4.5F);
         float var18 = var66 + this.computeFloat(7.0F);
         float var19 = this.computeFloat(3.0F);
         float var20 = 0.0F;
         float var21 = 0.0F;

         for (NotificationManager.Notification var23 : list) {
            float var24 = var23.computeNext(this.getMkadw(), var66);
            float var25 = var23.isToggle() ? var10 : var67;
            float var26;
            if (var13) {
               var26 = var12 + var25 + var15 + var16 + var17 + var24 + var12;
            } else {
               float var27 = var12 + var25 + var12;
               float var28 = var12 + var24 + var12;
               var26 = var27 + var14 + var28;
            }

            if (var26 > var20) {
               var20 = var26;
            }

            var21 += var18 + var19;
         }

         if (var21 > 0.0F) {
            var21 -= var19;
         }

         this.getAutoHelper().setWidth(var20);
         this.getAutoHelper().setHeight(var21);
         float var68 = this.getAutoHelper().getX();
         float var69 = this.getAutoHelper().getY();
         float var70 = var69 + var21 - var18;

         for (NotificationManager.Notification var72 : list) {
            float var73 = var72.computeNext(this.getMkadw(), var66);
            float var74 = var72.isToggle() ? var10 : var67;
            float var29 = var12 + var74 + var12;
            float var30 = var12 + var73 + var12;
            float var31 = var13 ? var12 + var74 + var15 + var16 + var17 + var73 + var12 : var29 + var14 + var30;
            float var32 = var68 + (var20 - var31) / 2.0F;
            var72.getAnimatedValue().update();
            float var34 = (float)var72.getAnimatedValue().getValue();
            matrixStack.push();
            float var35 = var32 + var31 / 2.0F;
            float var36 = var70 + var18 / 2.0F;
            matrixStack.translate(var35, var36, 0.0F);
            matrixStack.scale(var34, var34, 1.0F);
            matrixStack.translate(-var35, -var36, 0.0F);
            Color var37 = this.initInit();
            int var38 = (int)((float)var37.getAlpha() * var34) << 24 | var37.getRGB() & 16777215;
            float var39 = this.getHeight();
            float var41 = var32 + var29 + var14;
            if (var13) {
               if (this.getAutoHelper().isBlur()) {
                  Render.shaderProgram.handleVal(matrixStack, var32, var70, var31, var18, var39, var38);
               } else {
                  Render.rectRenderer.handleVal(matrixStack, var32, var70, var31, var18, var39, var38);
               }
            } else if (this.getAutoHelper().isBlur()) {
               Render.shaderProgram.handleVal(matrixStack, var32, var70, var29, var18, var39, var38);
               Render.shaderProgram.handleVal(matrixStack, var41, var70, var30, var18, var39, var38);
            } else {
               Render.rectRenderer.handleVal(matrixStack, var32, var70, var29, var18, var39, var38);
               Render.rectRenderer.handleVal(matrixStack, var41, var70, var30, var18, var39, var38);
            }

            this.runContent();
            if (Interface.getInstance().isEnabled() && Interface.check2()) {
               if (var13) {
                  Interface.handleVal(matrixStack, var32, var70, var31, var18, var39, var34, Interface.check4());
               } else {
                  Interface.handleVal(matrixStack, var32, var70, var29, var14, var30, var18, var39, var34, Interface.check4());
               }
            }

            float var42 = var70 + var18 / 2.0F;
            float var43 = var42 - var66 / 2.0F - this.computeFloat(0.8F);
            float var44 = var42 - var67 / 2.0F - this.computeFloat(0.5F);
            float var45 = var13 ? var32 + var12 : var32 + var12;
            if (var72.isToggle()) {
               var72.getAnimatedValue2().update();
               var72.getAnimatedValue2().animateTo(var72.isToggledOn() ? 1.0 : 0.0, var72.isToggledOn() ? 220L : 150L, this.getEasing());
               float var76 = (float)var72.getAnimatedValue2().getValue();
               float var80 = var42 - var11 / 2.0F;
               int var49 = (int)(46.0F + 209.0F * (1.0F - var76));
               int var50 = (int)(204.0F + -124.0F * (1.0F - var76));
               int var51 = (int)(113.0F + -33.0F * (1.0F - var76));
               int var52 = (int)(255.0F * var34) << 24 | var49 << 16 | var50 << 8 | var51;
               Render.rectRenderer.handleVal(matrixStack, var45, var80, var10, var11, var11 / 2.0F, var52);
               float var53 = var11 - this.computeFloat(1.6F);
               float var54 = var45 + this.computeFloat(0.8F) + var76 * (var10 - var53 - this.computeFloat(1.6F));
               float var55 = var80 + this.computeFloat(0.8F);
               int var56 = (int)(255.0F * var34) << 24 | 16777215;
               Render.ringRenderer.handleVal(matrixStack, var54 + var53 / 2.0F, var55 + var53 / 2.0F, var53 / 2.0F, var56);
            } else if (var72.getItemStack() != null && !var72.getItemStack().isEmpty()) {
               matrixStack.push();
               matrixStack.translate(var45, var44, 0.0F);
               float var75 = var67 / 16.0F;
               matrixStack.scale(var75, var75, 1.0F);
               if (this.currentEvent != null && this.currentEvent.getContext() != null) {
                  this.currentEvent.getContext().drawItem(var72.getItemStack(), 0, 0);
               }

               matrixStack.pop();
            } else {
               boolean var46 = ThemeManager.getInstance().getIsDayMode() || MainMenuScreen.isDayMode;
               Color var47 = var72.getType() == NotificationManager.Notification.Type.INFO
                  ? (var46 ? new Color(15, 23, 42) : Color.WHITE)
                  : var72.getType().getColor();
               int var48 = (int)(255.0F * var34) << 24 | var47.getRGB() & 16777215;
               this.getValue2().handleVal(matrixStack, var72.getType().getIcon(), var45, var44, var67, var48);
            }

            float var77;
            if (var13) {
               float var78 = var72.isToggle() ? var10 : var67;
               Color var81 = InterfaceMetrics.initInit(255);
               int var83 = (int)((float)var81.getAlpha() * var34) << 24 | var81.getRGB() & 16777215;
               Render.rectRenderer.handleVal(matrixStack, var45 + var78 + var15, var42 - this.computeFloat(3.5F), var16, this.computeFloat(7.0F), 0.0F, var83);
               var77 = var45 + var78 + var15 + var16 + var17;
            } else {
               var77 = var41 + var12;
            }

            Color var79 = this.getColor();
            float var82 = var77;

            for (NotificationManager.TextSpan var86 : var72.initInit()) {
               Color var87 = var86.color != null ? var86.color : var79;
               Color var88 = new Color(var87.getRed(), var87.getGreen(), var87.getBlue(), (int)((float)var87.getAlpha() * var34));
               this.getMkadw().handleColor(matrixStack, var86.text, var82, var43, var66, var88);
               var82 += this.getMkadw().computeFloat(var86.text, var66);
            }

            matrixStack.pop();
            var70 -= var18 + var19;
         }
      }
   }

   @Override
   public float getHeight() {
      return this.computeFloat(3.5F * MainMenuScreen.getGsRadius());
   }

   public boolean lambda$render$0(long now, NotificationManager.Notification n) {
      n.getAnimatedValue().update();
      boolean var4 = now - n.getTimestamp() > n.getDuration();
      n.getAnimatedValue().animateTo(var4 ? 0.0 : 1.0, 150L, this.getEasing());
      return var4 && n.getAnimatedValue().getValue() <= 0.01;
   }

   public static class Notification {
      public String message;
      public NotificationManager.Notification.Type type;
      public final ItemStack itemStack;
      public String tag = null;
      public long timestamp;
      public long duration;
      public final AnimatedValue animatedValue = new AnimatedValue();
      public boolean toggle = false;
      public boolean toggledOn = false;
      public final AnimatedValue animatedValue2 = new AnimatedValue();
      public List<NotificationManager.TextSpan> list = null;

      public Notification(String message, NotificationManager.Notification.Type type, long duration) {
         this(message, type, duration, null);
      }

      public Notification(String message, NotificationManager.Notification.Type type, long duration, ItemStack itemStack) {
         this.message = message;
         this.type = type;
         this.duration = duration;
         this.timestamp = System.currentTimeMillis();
         this.itemStack = itemStack;
      }

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      public String getMessage() {
         return this.message;
      }

      public String getString() {
         return this.toggle ? "Функция \"" + this.message + (this.toggledOn ? "\" включена" : "\" выключена") : this.message;
      }

      public List<NotificationManager.TextSpan> initInit() {
         if (this.list != null) {
            return this.list;
         } else {
            ArrayList var1 = new ArrayList();
            if (this.toggle) {
               var1.add(new NotificationManager.TextSpan("Функция \"", null));
               var1.add(new NotificationManager.TextSpan(this.message, new Color(255, 150, 40)));
               if (this.toggledOn) {
                  var1.add(new NotificationManager.TextSpan("\" включена", new Color(46, 204, 113)));
               } else {
                  var1.add(new NotificationManager.TextSpan("\" выключена", new Color(255, 80, 80)));
               }

               this.list = var1;
               return var1;
            } else {
               List var2 = initContains(this.message);
               this.list = var2;
               return var2;
            }
         }
      }

      public float computeNext(ClientFont font, float fontSize) {
         List var3 = this.initInit();
         float var4 = 0.0F;

         for (NotificationManager.TextSpan var6 : (Iterable<NotificationManager.TextSpan>)(Iterable)var3) {
            var4 += font.computeFloat(var6.text, fontSize);
         }

         return var4;
      }

      public static List<NotificationManager.TextSpan> initContains(String raw) {
         ArrayList var1 = new ArrayList();
         if (raw != null && !raw.isEmpty()) {
            if (raw.contains("§")) {
               int var7 = 0;
               Color var8 = null;

               for (int var9 = 0; var9 < raw.length(); var9++) {
                  if (raw.charAt(var9) == 167 && var9 + 1 < raw.length()) {
                     if (var9 > var7) {
                        var1.add(new NotificationManager.TextSpan(raw.substring(var7, var9), var8));
                     }

                     char var10 = Character.toLowerCase(raw.charAt(var9 + 1));
                     var8 = computeColor(var10);
                     var7 = ++var9 + 1;
                  }
               }

               if (var7 < raw.length()) {
                  var1.add(new NotificationManager.TextSpan(raw.substring(var7), var8));
               }

               return var1;
            } else {
               Matcher var2 = NotificationManager.pattern.matcher(raw);

               int var3;
               for (var3 = 0; var2.find(); var3 = var2.end()) {
                  if (var2.start() > var3) {
                     var1.add(new NotificationManager.TextSpan(raw.substring(var3, var2.start()), null));
                  }

                  String var4 = var2.group();
                  String var5 = var4.toLowerCase();
                  Color var6 = null;
                  if (var5.contains("зачарованное") || var5.contains("enchanted")) {
                     var6 = new Color(255, 85, 255);
                  } else if (var5.contains("золот") || var5.contains("golden")) {
                     var6 = new Color(255, 185, 0);
                  } else if (var5.contains("зель") || var5.contains("potion")) {
                     var6 = new Color(255, 75, 100);
                  } else if (var5.contains("тотем") || var5.contains("totem")) {
                     var6 = new Color(245, 160, 20);
                  } else if (var5.contains("хорус") || var5.contains("chorus")) {
                     var6 = new Color(200, 155, 225);
                  } else if (var5.contains("жемчуг") || var5.contains("pearl") || var5.contains("стрел") || var5.contains("arrow")) {
                     var6 = new Color(90, 185, 240);
                  } else if (var5.equals("цель")) {
                     var6 = new Color(235, 80, 70);
                  } else if (var5.equals("ты ешь") || var5.equals("ты пьёшь") || var5.equals("твой")) {
                     var6 = new Color(52, 175, 245);
                  }

                  var1.add(new NotificationManager.TextSpan(var4, var6));
               }

               if (var3 < raw.length()) {
                  var1.add(new NotificationManager.TextSpan(raw.substring(var3), null));
               }

               return var1;
            }
         } else {
            return var1;
         }
      }

      public static Color computeColor(char c) {
         return switch (c) {
            case '0' -> Color.BLACK;
            case '1' -> new Color(0, 0, 170);
            case '2' -> new Color(0, 170, 0);
            case '3' -> new Color(0, 170, 170);
            case '4' -> new Color(170, 0, 0);
            case '5' -> new Color(170, 0, 170);
            case '6' -> new Color(255, 180, 0);
            case '7' -> new Color(170, 170, 170);
            case '8' -> new Color(85, 85, 85);
            case '9' -> new Color(85, 85, 255);
            default -> null;
            case 'a' -> new Color(85, 255, 85);
            case 'b' -> new Color(85, 255, 255);
            case 'c' -> new Color(255, 85, 85);
            case 'd' -> new Color(255, 85, 255);
            case 'e' -> new Color(255, 255, 85);
         };
      }

      public NotificationManager.Notification.Type getType() {
         return this.type;
      }

      public long getTimestamp() {
         return this.timestamp;
      }

      public long getDuration() {
         return this.duration;
      }

      public AnimatedValue getAnimatedValue() {
         return this.animatedValue;
      }

      public boolean isToggle() {
         return this.toggle;
      }

      public void setToggle(boolean toggle) {
         this.toggle = toggle;
         this.list = null;
      }

      public boolean isToggledOn() {
         return this.toggledOn;
      }

      public void setToggledOn(boolean toggledOn) {
         this.toggledOn = toggledOn;
         this.list = null;
      }

      public AnimatedValue getAnimatedValue2() {
         return this.animatedValue2;
      }

      public void setTimestamp(long timestamp) {
         this.timestamp = timestamp;
      }

      public void setDuration(long duration) {
         this.duration = duration;
      }

      public void setMessage(String message) {
         this.message = message;
         this.list = null;
      }

      public void setType(NotificationManager.Notification.Type type) {
         this.type = type;
      }

      public String getTag() {
         return this.tag;
      }

      public void setTag(String tag) {
         this.tag = tag;
      }

      public static enum Type {
         INFO("Y", new Color(100, 150, 255)),
         SUCCESS("F", new Color(80, 255, 80)),
         WARNING("m", new Color(255, 200, 50)),
         ERROR("n", new Color(255, 80, 80)),
         TOGGLE_ON("Z", new Color(46, 204, 113)),
         TOGGLE_OFF("Z", new Color(255, 80, 80));

         public final String icon;
         public final Color color;

         private Type(String icon, Color color) {
            this.icon = icon;
            this.color = color;
         }

         public String getIcon() {
            return this.icon;
         }

         public Color getColor() {
            return this.color;
         }

         public static NotificationManager.Notification.Type[] $values() {
            return new NotificationManager.Notification.Type[]{INFO, SUCCESS, WARNING, ERROR, TOGGLE_ON, TOGGLE_OFF};
         }
      }
   }

   public static class TextSpan {
      public final String text;
      public final Color color;

      public TextSpan(String text, Color color) {
         this.text = text;
         this.color = color;
      }
   }
}
