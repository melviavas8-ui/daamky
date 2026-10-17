package dev.daamky.client.legacy.module;

import dev.daamky.client.internal.Animation;
import dev.daamky.client.internal.Component;
import dev.daamky.client.internal.Direction2;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.internal.RenderAccess;
import dev.daamky.client.internal.ValueToHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

public abstract class Module implements RenderAccess {
   public final List<Component<?>> settings = new ObjectArrayList();
   public ModuleInfo moduleInfo;
   public String name;
   public String bigName;
   public String desc;
   public ModuleCategory category;
   public boolean enabled;
   public boolean autoEnabled;
   public boolean allowDisable;
   public boolean hidden;
   public int key;
   public final ValueToHelper animation_new = new ValueToHelper();
   public final ValueToHelper animation_new_1 = new ValueToHelper();
   public final ValueToHelper toggleAnimation = new ValueToHelper();
   public final ValueToHelper hoverModule = new ValueToHelper();
   public final ValueToHelper expandedAnimation = new ValueToHelper();
   public final ValueToHelper animtoP7 = new ValueToHelper();
   public final ValueToHelper animtoP8 = new ValueToHelper();
   public Animation animation = new Direction2(300, 1.0);
   public Animation animation1 = new Direction2(300, 1.0);
   public Animation animation2 = new Direction2(300, 1.0);
   public Animation animation3 = new Direction2(300, 1.0);
   public Animation animation12 = new Direction2(300, 1.0);
   public Animation animation14 = new Direction2(300, 1.0);
   public Animation animation15 = new Direction2(300, 1.0);
   public Animation animation16 = new Direction2(300, 1.0);
   public boolean expanded;

   public Module() {
      this(true);
   }

   public Module(boolean requireAnnotation) {
      if (requireAnnotation) {
         Class var2 = this.getClass();
         ModuleInfo var3 = var2.getAnnotation(ModuleInfo.class);
         if (var3 == null) {
            throw new NotImplementedException("@ModuleInfo annotation not found on " + var2.getSimpleName());
         } else {
            this.moduleInfo = var3;
            this.name = var3.name().trim().replaceAll(" ", "");
            this.bigName = var3.name();
            this.desc = var3.desc();
            this.category = var3.category();
            this.autoEnabled = var3.autoEnabled();
            this.allowDisable = var3.allowDisable();
            this.hidden = var3.hidden();
            this.key = var3.key();
            this.run4();
         }
      }
   }

   public void runEnabled() {
      this.handleFlag(!this.enabled);
   }

   public void handleFlag(boolean enabled) {
      this.handleEnabled(enabled, true);
   }

   public void handleEnabled(boolean enabled, boolean notification) {
      if (this.enabled != enabled && (this.allowDisable || enabled)) {
         this.enabled = enabled;
         if (enabled) {
            this.getMc();
         } else {
            this.run3();
         }
      }
   }

   public void run() {
      FactoryHelper.getMudocs().handleContains(this);
   }

   public void run2() {
      FactoryHelper.getMudocs().handleListener(this);
   }

   public void getMc() {
      if (mc.player != null) {
         this.run5();
      }

      this.run();
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
   }

   public void run3() {
      if (mc.player != null) {
         this.run6();
      }

      this.run2();
   }

   public void run4() {
   }

   public void run5() {
   }

   public void run6() {
   }

      public List<Component<?>> getSettings() {
      return this.settings;
   }

      public ModuleInfo getModuleInfo() {
      return this.moduleInfo;
   }

      public String getName() {
      return this.name;
   }

      public String getBigName() {
      return this.bigName;
   }

      public String getDesc() {
      return this.desc;
   }

      public ModuleCategory getCategory() {
      return this.category;
   }

      public boolean isEnabled() {
      return this.enabled;
   }

      public boolean isAutoEnabled() {
      return this.autoEnabled;
   }

      public boolean isAllowDisable() {
      return this.allowDisable;
   }

      public boolean isHidden() {
      return this.hidden;
   }

      public int getKey() {
      return this.key;
   }

      public ValueToHelper getAnimation_new() {
      return this.animation_new;
   }

      public ValueToHelper getAnimation_new_1() {
      return this.animation_new_1;
   }

      public ValueToHelper getToggleAnimation() {
      return this.toggleAnimation;
   }

      public ValueToHelper getHoverModule() {
      return this.hoverModule;
   }

      public ValueToHelper getExpandedAnimation() {
      return this.expandedAnimation;
   }

      public ValueToHelper getAnimtoP7() {
      return this.animtoP7;
   }

      public ValueToHelper getAnimtoP8() {
      return this.animtoP8;
   }

      public Animation getAnimation() {
      return this.animation;
   }

      public Animation getAnimation1() {
      return this.animation1;
   }

      public Animation getAnimation2() {
      return this.animation2;
   }

      public Animation getAnimation3() {
      return this.animation3;
   }

      public Animation getAnimation12() {
      return this.animation12;
   }

      public Animation getAnimation14() {
      return this.animation14;
   }

      public Animation getAnimation15() {
      return this.animation15;
   }

      public Animation getAnimation16() {
      return this.animation16;
   }

      public void setModuleInfo(ModuleInfo moduleInfo) {
      this.moduleInfo = moduleInfo;
   }

      public void setName(String name) {
      this.name = name;
   }

      public void setBigName(String bigName) {
      this.bigName = bigName;
   }

      public void setDesc(String desc) {
      this.desc = desc;
   }

      public void setCategory(ModuleCategory category) {
      this.category = category;
   }

      public void setAutoEnabled(boolean autoEnabled) {
      this.autoEnabled = autoEnabled;
   }

      public void setAllowDisable(boolean allowDisable) {
      this.allowDisable = allowDisable;
   }

      public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

      public void setKey(int key) {
      this.key = key;
   }

      public void setAnimation(Animation animation) {
      this.animation = animation;
   }

      public void setAnimation1(Animation animation1) {
      this.animation1 = animation1;
   }

      public void setAnimation2(Animation animation2) {
      this.animation2 = animation2;
   }

      public void setAnimation3(Animation animation3) {
      this.animation3 = animation3;
   }

      public void setAnimation12(Animation animation12) {
      this.animation12 = animation12;
   }

      public void setAnimation14(Animation animation14) {
      this.animation14 = animation14;
   }

      public void setAnimation15(Animation animation15) {
      this.animation15 = animation15;
   }

      public void setAnimation16(Animation animation16) {
      this.animation16 = animation16;
   }

      public boolean computeAnimation(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Module var2)) {
         return false;
      } else if (!var2.computeFlag(this)) {
         return false;
      } else if (this.isEnabled() != var2.isEnabled()) {
         return false;
      } else if (this.isAutoEnabled() != var2.isAutoEnabled()) {
         return false;
      } else if (this.isAllowDisable() != var2.isAllowDisable()) {
         return false;
      } else if (this.isHidden() != var2.isHidden()) {
         return false;
      } else if (this.getKey() != var2.getKey()) {
         return false;
      } else if (this.isExpanded() != var2.isExpanded()) {
         return false;
      } else {
         List var3 = this.getSettings();
         List var4 = var2.getSettings();
         if (var3 == null ? var4 == null : var3.equals(var4)) {
            ModuleInfo var5 = this.getModuleInfo();
            ModuleInfo var6 = var2.getModuleInfo();
            if (var5 == null ? var6 == null : var5.equals(var6)) {
               String var7 = this.getName();
               String var8 = var2.getName();
               if (var7 == null ? var8 == null : var7.equals(var8)) {
                  String var9 = this.getBigName();
                  String var10 = var2.getBigName();
                  if (var9 == null ? var10 == null : var9.equals(var10)) {
                     String var11 = this.getDesc();
                     String var12 = var2.getDesc();
                     if (var11 == null ? var12 == null : var11.equals(var12)) {
                        ModuleCategory var13 = this.getCategory();
                        ModuleCategory var14 = var2.getCategory();
                        if (var13 == null ? var14 == null : var13.equals(var14)) {
                           ValueToHelper var15 = this.getAnimation_new();
                           ValueToHelper var16 = var2.getAnimation_new();
                           if (var15 == null ? var16 == null : var15.equals(var16)) {
                              ValueToHelper var17 = this.getAnimation_new_1();
                              ValueToHelper var18 = var2.getAnimation_new_1();
                              if (var17 == null ? var18 == null : var17.equals(var18)) {
                                 ValueToHelper var19 = this.getToggleAnimation();
                                 ValueToHelper var20 = var2.getToggleAnimation();
                                 if (var19 == null ? var20 == null : var19.equals(var20)) {
                                    ValueToHelper var21 = this.getHoverModule();
                                    ValueToHelper var22 = var2.getHoverModule();
                                    if (var21 == null ? var22 == null : var21.equals(var22)) {
                                       ValueToHelper var23 = this.getExpandedAnimation();
                                       ValueToHelper var24 = var2.getExpandedAnimation();
                                       if (var23 == null ? var24 == null : var23.equals(var24)) {
                                          ValueToHelper var25 = this.getAnimtoP7();
                                          ValueToHelper var26 = var2.getAnimtoP7();
                                          if (var25 == null ? var26 == null : var25.equals(var26)) {
                                             ValueToHelper var27 = this.getAnimtoP8();
                                             ValueToHelper var28 = var2.getAnimtoP8();
                                             if (var27 == null ? var28 == null : var27.equals(var28)) {
                                                Animation var29 = this.getAnimation();
                                                Animation var30 = var2.getAnimation();
                                                if (var29 == null ? var30 == null : var29.equals(var30)) {
                                                   Animation var31 = this.getAnimation1();
                                                   Animation var32 = var2.getAnimation1();
                                                   if (var31 == null ? var32 == null : var31.equals(var32)) {
                                                      Animation var33 = this.getAnimation2();
                                                      Animation var34 = var2.getAnimation2();
                                                      if (var33 == null ? var34 == null : var33.equals(var34)) {
                                                         Animation var35 = this.getAnimation3();
                                                         Animation var36 = var2.getAnimation3();
                                                         if (var35 == null ? var36 == null : var35.equals(var36)) {
                                                            Animation var37 = this.getAnimation12();
                                                            Animation var38 = var2.getAnimation12();
                                                            if (var37 == null ? var38 == null : var37.equals(var38)) {
                                                               Animation var39 = this.getAnimation14();
                                                               Animation var40 = var2.getAnimation14();
                                                               if (var39 == null ? var40 == null : var39.equals(var40)) {
                                                                  Animation var41 = this.getAnimation15();
                                                                  Animation var42 = var2.getAnimation15();
                                                                  if (var41 == null ? var42 == null : var41.equals(var42)) {
                                                                     Animation var43 = this.getAnimation16();
                                                                     Animation var44 = var2.getAnimation16();
                                                                     return var43 == null ? var44 == null : var43.equals(var44);
                                                                  } else {
                                                                     return false;
                                                                  }
                                                               } else {
                                                                  return false;
                                                               }
                                                            } else {
                                                               return false;
                                                            }
                                                         } else {
                                                            return false;
                                                         }
                                                      } else {
                                                         return false;
                                                      }
                                                   } else {
                                                      return false;
                                                   }
                                                } else {
                                                   return false;
                                                }
                                             } else {
                                                return false;
                                             }
                                          } else {
                                             return false;
                                          }
                                       } else {
                                          return false;
                                       }
                                    } else {
                                       return false;
                                    }
                                 } else {
                                    return false;
                                 }
                              } else {
                                 return false;
                              }
                           } else {
                              return false;
                           }
                        } else {
                           return false;
                        }
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

      public boolean computeFlag(Object other) {
      return other instanceof Module;
   }

      public int getCode() {
      byte var1 = 59;
      int var2 = 1;
      var2 = var2 * 59 + (this.isEnabled() ? 79 : 97);
      var2 = var2 * 59 + (this.isAutoEnabled() ? 79 : 97);
      var2 = var2 * 59 + (this.isAllowDisable() ? 79 : 97);
      var2 = var2 * 59 + (this.isHidden() ? 79 : 97);
      var2 = var2 * 59 + this.getKey();
      var2 = var2 * 59 + (this.isExpanded() ? 79 : 97);
      List var3 = this.getSettings();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      ModuleInfo var4 = this.getModuleInfo();
      var2 = var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getName();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getBigName();
      var2 = var2 * 59 + (var6 == null ? 43 : var6.hashCode());
      String var7 = this.getDesc();
      var2 = var2 * 59 + (var7 == null ? 43 : var7.hashCode());
      ModuleCategory var8 = this.getCategory();
      var2 = var2 * 59 + (var8 == null ? 43 : var8.hashCode());
      ValueToHelper var9 = this.getAnimation_new();
      var2 = var2 * 59 + (var9 == null ? 43 : var9.hashCode());
      ValueToHelper var10 = this.getAnimation_new_1();
      var2 = var2 * 59 + (var10 == null ? 43 : var10.hashCode());
      ValueToHelper var11 = this.getToggleAnimation();
      var2 = var2 * 59 + (var11 == null ? 43 : var11.hashCode());
      ValueToHelper var12 = this.getHoverModule();
      var2 = var2 * 59 + (var12 == null ? 43 : var12.hashCode());
      ValueToHelper var13 = this.getExpandedAnimation();
      var2 = var2 * 59 + (var13 == null ? 43 : var13.hashCode());
      ValueToHelper var14 = this.getAnimtoP7();
      var2 = var2 * 59 + (var14 == null ? 43 : var14.hashCode());
      ValueToHelper var15 = this.getAnimtoP8();
      var2 = var2 * 59 + (var15 == null ? 43 : var15.hashCode());
      Animation var16 = this.getAnimation();
      var2 = var2 * 59 + (var16 == null ? 43 : var16.hashCode());
      Animation var17 = this.getAnimation1();
      var2 = var2 * 59 + (var17 == null ? 43 : var17.hashCode());
      Animation var18 = this.getAnimation2();
      var2 = var2 * 59 + (var18 == null ? 43 : var18.hashCode());
      Animation var19 = this.getAnimation3();
      var2 = var2 * 59 + (var19 == null ? 43 : var19.hashCode());
      Animation var20 = this.getAnimation12();
      var2 = var2 * 59 + (var20 == null ? 43 : var20.hashCode());
      Animation var21 = this.getAnimation14();
      var2 = var2 * 59 + (var21 == null ? 43 : var21.hashCode());
      Animation var22 = this.getAnimation15();
      var2 = var2 * 59 + (var22 == null ? 43 : var22.hashCode());
      Animation var23 = this.getAnimation16();
      return var2 * 59 + (var23 == null ? 43 : var23.hashCode());
   }

}
