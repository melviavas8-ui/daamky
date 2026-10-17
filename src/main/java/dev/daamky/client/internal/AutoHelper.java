package dev.daamky.client.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.FramebufferResizeEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.util.KeyNames;
import dev.daamky.client.util.MinecraftAccess;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class AutoHelper implements MinecraftAccess {
   @Expose
   @SerializedName("x")
   public float x;
   @Expose
   @SerializedName("y")
   public float y;
   @Expose
   @SerializedName("blur")
   public boolean blur = true;
   @Expose
   @SerializedName("scale")
   public float scale = 1.0F;
   @Expose
   @SerializedName("attachInfoHUD")
   public Boolean attachInfoHUD = true;
   @Expose
   @SerializedName("mergeCards")
   public Boolean mergeCards = false;
   public float initialXVal;
   public float initialYVal;
   public float visualYOffset = 0.0F;
   public float startX;
   public float startY;
   public boolean dragging;
   public float width = 0.0F;
   public float height = 0.0F;
   @Expose
   @SerializedName("name")
   public final String name;
   public final Module feature;
   public Runnable onReleaseCallback;
   public BiPredicate<Float, Float> dragFilter;
   public BiConsumer<Float, Float> clickCallback;
   @Expose
   @SerializedName("cardMode")
   public Boolean cardMode = false;
   @Expose
   @SerializedName("graphMode")
   public Boolean graphMode = true;
   @Expose
   @SerializedName("showCombat")
   public Boolean showCombat = true;
   @Expose
   @SerializedName("showMovement")
   public Boolean showMovement = true;
   @Expose
   @SerializedName("showRender")
   public Boolean showRender = true;
   @Expose
   @SerializedName("showPlayer")
   public Boolean showPlayer = true;
   @Expose
   @SerializedName("showOther")
   public Boolean showOther = true;
   @Expose
   @SerializedName("showSuffix")
   public Boolean showSuffix = true;
   @Expose
   @SerializedName("alignSide")
   public String alignSide = "Auto";
   @Expose
   @SerializedName("screenWidth")
   public float lastScreenWidth = -1.0F;
   @Expose
   @SerializedName("screenHeight")
   public float lastScreenHeight = -1.0F;

   public boolean getShowCombat() {
      return this.showCombat == null || this.showCombat;
   }

   public void setShowCombat(boolean showCombat) {
      this.showCombat = showCombat;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public boolean getShowMovement() {
      return this.showMovement == null || this.showMovement;
   }

   public void setShowMovement(boolean showMovement) {
      this.showMovement = showMovement;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public boolean getShowRender() {
      return this.showRender == null || this.showRender;
   }

   public void setShowRender(boolean showRender) {
      this.showRender = showRender;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public boolean getShowPlayer() {
      return this.showPlayer == null || this.showPlayer;
   }

   public void setShowPlayer(boolean showPlayer) {
      this.showPlayer = showPlayer;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public boolean getShowOther() {
      return this.showOther == null || this.showOther;
   }

   public void setShowOther(boolean showOther) {
      this.showOther = showOther;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public boolean getShowSuffix() {
      return this.showSuffix == null || this.showSuffix;
   }

   public void setShowSuffix(boolean showSuffix) {
      this.showSuffix = showSuffix;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public String getAlignSide() {
      return this.alignSide == null ? "Auto" : this.alignSide;
   }

   public void setAlignSide(String alignSide) {
      this.alignSide = alignSide;
      DraggableManagerSaveHelper.getInstance().save();
   }

   public void runAlign() {
      if ("Auto".equalsIgnoreCase(this.getAlignSide())) {
         this.setAlignSide("Right");
      } else if ("Right".equalsIgnoreCase(this.getAlignSide())) {
         this.setAlignSide("Left");
      } else {
         this.setAlignSide("Auto");
      }
   }

   public boolean computeFlag(ModuleCategory tag) {
      if (tag == null) {
         return true;
      } else {
         return switch (tag) {
            case BATTLE -> this.getShowCombat();
            case MOTION -> this.getShowMovement();
            case VISUALS -> this.getShowRender();
            case ACTOR -> this.getShowPlayer();
            case PVE -> this.getShowOther();
            case MICS -> this.getShowOther();
            case HUD -> true;
         };
      }
   }

   public boolean getCardMode() {
      return this.cardMode != null && this.cardMode;
   }

   public void setCardMode(boolean cardMode) {
      this.cardMode = cardMode;
   }

   public boolean getGraphMode() {
      return this.graphMode == null || this.graphMode;
   }

   public void setGraphMode(boolean graphMode) {
      this.graphMode = graphMode;
   }

   public boolean getAttachInfoHUD() {
      return this.attachInfoHUD == null || this.attachInfoHUD;
   }

   public boolean getMergeCards() {
      return this.mergeCards != null && this.mergeCards;
   }

   public AutoHelper(Module feature, String name, float initialXVal, float initialYVal) {
      this.feature = feature;
      this.name = name;
      this.x = this.computeRound(initialXVal);
      this.y = this.computeRound(initialYVal);
      this.initialXVal = initialXVal;
      this.initialYVal = initialYVal;
      FramebufferResizeEvent.getInstance().subscribe(new EventListener<>(-1, this::lambda$new$0));
   }

   public final void runRound() {
      if (this.dragging) {
         float var1 = (float)this.getMc() - this.startX;
         float var2 = (float)this.getInt() - this.startY;
         float var3 = (float)mc.getWindow().getScaledWidth();
         float var4 = (float)mc.getWindow().getScaledHeight();
         this.lastScreenWidth = var3;
         this.lastScreenHeight = var4;
         float var5 = (float)Math.round(var3 / 2.0F);
         float var6 = (float)Math.round(var4 / 2.0F);
         float var7 = Math.abs(var1 + this.getScale2() / 2.0F - var5);
         if (var7 < 7.0F) {
            var1 = var5 - this.getScale2() / 2.0F;
         }

         if (KeyNames.computeGlfw(342)) {
            float var12 = 20.0F;
            float var13 = (float)Math.round((var1 - var5) / var12) * var12 + var5;
            float var14 = Math.abs(var13 - var1);
            float var8;
            float var10;
            float var11;
            float var15 = Math.min(
               var14,
               Math.min(
                  var11 = Math.abs(
                     (var10 = (float)Math.round((var1 + this.getScale2() / 2.0F - var5) / var12) * var12 + var5) - (var1 + this.getScale2() / 2.0F)
                  ),
                  Math.abs((var8 = (float)Math.round((var1 + this.getScale2() - var5) / var12) * var12 + var5) - (var1 + this.getScale2()))
               )
            );
            var1 = var15 == var14 ? var13 : (var15 == var11 ? var10 - this.getScale2() / 2.0F : var8 - this.getScale2());
            float var16 = (float)Math.round((var2 - var6) / var12) * var12 + var6;
            float var17 = Math.abs(var16 - var2);
            float var18 = (float)Math.round((var2 + this.getScale3() / 2.0F - var6) / var12) * var12 + var6;
            float var19 = Math.abs(var18 - (var2 + this.getScale3() / 2.0F));
            float var20 = (float)Math.round((var2 + this.getScale3() - var6) / var12) * var12 + var6;
            float var21 = Math.abs(var20 - (var2 + this.getScale3()));
            float var22 = Math.min(var17, Math.min(var19, var21));
            var2 = var22 == var17 ? var16 : (var22 == var19 ? var18 - this.getScale3() / 2.0F : var20 - this.getScale3());
         }

         this.x = this.computeRound(EntityHelper.computeFloat(this.x, var1, 0.45F));
         this.y = this.computeRound(EntityHelper.computeFloat(this.y, var2, 0.45F));
         this.handleMax(var3, var4);
      }
   }

   public final void handleNext(int button) {
      if (button == 0 && this.checkVisual()) {
         if (this.dragFilter != null && !this.dragFilter.test((float)this.getMc(), (float)this.getInt())) {
            if (this.clickCallback != null) {
               this.clickCallback.accept((float)this.getMc(), (float)this.getInt());
            }

            return;
         }

         boolean var2 = false;

         for (AutoHelper var4 : DraggableManagerSaveHelper.getInstance().getDraggables().values()) {
            if (var4.isDragging()) {
               var2 = true;
               break;
            }
         }

         if (!var2) {
            this.dragging = true;
            this.startX = (float)((int)((float)this.getMc() - this.x));
            this.startY = (float)((int)((float)this.getInt() - (this.y + this.visualYOffset)));
            if (mc != null && mc.getWindow() != null) {
               this.lastScreenWidth = (float)mc.getWindow().getScaledWidth();
               this.lastScreenHeight = (float)mc.getWindow().getScaledHeight();
            }
         }
      }
   }

   public final void saveDragging(int button) {
      if (button == 0 && this.dragging) {
         this.dragging = false;
         if (mc != null && mc.getWindow() != null) {
            this.lastScreenWidth = (float)mc.getWindow().getScaledWidth();
            this.lastScreenHeight = (float)mc.getWindow().getScaledHeight();
         }

         DraggableManagerSaveHelper.getInstance().save();
         if (this.onReleaseCallback != null) {
            this.onReleaseCallback.run();
         }
      }
   }

   public boolean checkVisual() {
      float var1 = this.y + this.visualYOffset;
      float var2 = (float)this.getMc();
      float var3 = (float)this.getInt();
      float var4 = this.getScale2();
      float var5 = this.getScale3();
      return var2 >= this.x && var2 <= this.x + var4 && var3 >= var1 && var3 <= var1 + var5;
   }

   public int getMc() {
      return(intVal)(mc.mouse.getX() / mc.getWindow().getScaleFactor());
   }

   public int getInt() {
      return(intVal)(mc.mouse.getY() / mc.getWindow().getScaleFactor());
   }

   public float computeRound(float value) {
      return(floatVal)Math.round(value * 2.0F) / 2.0F;
   }

   public void handleLast(float newWidth, float newHeight) {
      if (this.lastScreenWidth > 0.0F && this.lastScreenHeight > 0.0F && newWidth > 0.0F && newHeight > 0.0F) {
         float var3 = this.lastScreenWidth;
         float var4 = this.lastScreenHeight;
         if (Math.abs(var3 - newWidth) > 0.5F || Math.abs(var4 - newHeight) > 0.5F) {
            float var5 = this.getScale2();
            float var6 = this.getScale3();
            if (var5 > 0.0F && var6 > 0.0F) {
               float var7 = (this.x + var5 * 0.5F) / var3;
               if (var7 < 0.35F) {
                  float var8 = this.x;
                  this.x = var8;
               } else if (var7 > 0.65F) {
                  float var10 = var3 - (this.x + var5);
                  this.x = newWidth - var5 - var10;
               } else {
                  float var11 = this.x + var5 * 0.5F - var3 * 0.5F;
                  this.x = newWidth * 0.5F + var11 - var5 * 0.5F;
               }

               float var12 = (this.y + var6 * 0.5F) / var4;
               if (var12 < 0.35F) {
                  float var9 = this.y;
                  this.y = var9;
               } else if (var12 > 0.65F) {
                  float var13 = var4 - (this.y + var6);
                  this.y = newHeight - var6 - var13;
               } else {
                  float var14 = this.y + var6 * 0.5F - var4 * 0.5F;
                  this.y = newHeight * 0.5F + var14 - var6 * 0.5F;
               }
            } else {
               this.x = this.x / var3 * newWidth;
               this.y = this.y / var4 * newHeight;
            }
         }
      }

      this.lastScreenWidth = newWidth;
      this.lastScreenHeight = newHeight;
      this.handleMax(newWidth, newHeight);
   }

   public void runLast() {
      if (mc != null && mc.getWindow() != null) {
         float var1 = (float)mc.getWindow().getScaledWidth();
         float var2 = (float)mc.getWindow().getScaledHeight();
         if (!(var1 <= 0.0F) && !(var2 <= 0.0F)) {
            if (!(this.x < 0.0F) && !(this.y < 0.0F)) {
               if (this.lastScreenWidth > 0.0F && this.lastScreenHeight > 0.0F) {
                  if (Math.abs(this.lastScreenWidth - var1) > 0.5F || Math.abs(this.lastScreenHeight - var2) > 0.5F) {
                     this.handleLast(var1, var2);
                     return;
                  }
               } else {
                  this.lastScreenWidth = var1;
                  this.lastScreenHeight = var2;
               }

               this.handleMax(var1, var2);
            }
         }
      }
   }

   public void handleMax(float screenWidth, float screenHeight) {
      float var3 = 3.0F;
      float var4 = this.getScale2();
      float var5 = this.getScale3();
      if (this.x < var3) {
         this.x = var3;
      }

      if (this.y < var3) {
         this.y = var3;
      }

      if (var4 > 0.0F && this.x + var4 > screenWidth - var3) {
         this.x = Math.max(var3, screenWidth - var4 - var3);
      }

      if (var5 > 0.0F && this.y + var5 > screenHeight - var3) {
         this.y = Math.max(var3, screenHeight - var5 - var3);
      }
   }

   public float getScale2() {
      return this.width * this.scale;
   }

   public float getScale3() {
      return this.height * this.scale;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public boolean isBlur() {
      return this.blur;
   }

   public float getScale() {
      return this.scale;
   }

   public float getInitialXVal() {
      return this.initialXVal;
   }

   public float getInitialYVal() {
      return this.initialYVal;
   }

   public float getStartX() {
      return this.startX;
   }

   public float getStartY() {
      return this.startY;
   }

   public boolean isDragging() {
      return this.dragging;
   }

   public String getName() {
      return this.name;
   }

   public Module getFeature() {
      return this.feature;
   }

   public Runnable getOnReleaseCallback() {
      return this.onReleaseCallback;
   }

   public BiPredicate<Float, Float> getDragFilter() {
      return this.dragFilter;
   }

   public BiConsumer<Float, Float> getClickCallback() {
      return this.clickCallback;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public void setBlur(boolean blur) {
      this.blur = blur;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public void setAttachInfoHUD(Boolean attachInfoHUD) {
      this.attachInfoHUD = attachInfoHUD;
   }

   public void setMergeCards(Boolean mergeCards) {
      this.mergeCards = mergeCards;
   }

   public void setInitialXVal(float initialXVal) {
      this.initialXVal = initialXVal;
   }

   public void setInitialYVal(float initialYVal) {
      this.initialYVal = initialYVal;
   }

   public void setStartX(float startX) {
      this.startX = startX;
   }

   public void setStartY(float startY) {
      this.startY = startY;
   }

   public void setDragging(boolean dragging) {
      this.dragging = dragging;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public void setOnReleaseCallback(Runnable onReleaseCallback) {
      this.onReleaseCallback = onReleaseCallback;
   }

   public void setDragFilter(BiPredicate<Float, Float> dragFilter) {
      this.dragFilter = dragFilter;
   }

   public void setClickCallback(BiConsumer<Float, Float> clickCallback) {
      this.clickCallback = clickCallback;
   }

   public void lambda$new$0(FramebufferResizeEvent event) {
      this.runLast();
   }
}
