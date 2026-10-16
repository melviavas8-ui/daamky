package dev.daamky.client.internal;

import dev.daamky.client.gui.component.CategoryComponent;
import dev.daamky.client.gui.component.CheckComponent;
import dev.daamky.client.gui.component.ColorPickerComponent;
import dev.daamky.client.gui.component.ComboComponent;
import dev.daamky.client.gui.component.NumberComponent;
import dev.daamky.client.gui.component.SliderComponent;
import dev.daamky.client.gui.component.SwitchComponent;
import dev.daamky.client.gui.component.TextFieldComponent;
import dev.daamky.client.legacy.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ButtonSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import dev.daamky.client.setting.StringSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class LegacyModule extends Module {
   public final dev.daamky.client.module.Module feature;

   public LegacyModule(dev.daamky.client.module.Module feature) {
      super(false);
      this.feature = featurex;
      this.setName(featurex.getName().trim().replaceAll(" ", ""));
      this.setBigName(featurex.getName());
      this.setDesc(featurex.getDescription() != null ? featurex.getDescription() : "");
      this.setCategory(
         (featurex.getSubCategory() == null || !featurex.getSubCategory().equalsIgnoreCase("PvE")) && featurex.getCategory() != ModuleCategory.PVE
            ? computeModuleCategory(featurex.getCategory())
            : dev.daamky.client.legacy.module.ModuleCategory.PVE
      );
      this.setKey(featurex.getBind());
      this.handleEnabled(featurex.isEnabled(), false);

      for (Setting var3 : featurex.getSettings()) {
         if (var3 instanceof BooleanSetting var4) {
            CheckComponent var23 = new CheckComponent(this, var4.getName(), var4.getValue()) {
               @Override
               public CheckComponent withCachedValue(Boolean value) {
                  super.withCachedValue(value);
                  var4.setValue(value);
                  return this;
               }

               @Override
               public Component withValue(Object param1) {
                  return this.withCachedValue((Boolean)var1);
               }
            };
            var23.computeCheckComponent(var4::isVisible);
         } else if (var3 instanceof FloatSetting var5) {
            float var22 = var5.getValue().floatValue();
            float var28 = var5.getMin();
            float var32 = var5.getMax();
            float var33 = var5.getStep();
            SliderComponent var34 = new SliderComponent(this, var5.getName(), var22, var28, var32, var33) {
               @Override
               public SliderComponent withCachedValue(Float value) {
                  super.withCachedValue(value);
                  var5.setValue(value);
                  return this;
               }

               @Override
               public Float getCachedValue() {
                  return var5.getValue().floatValue();
               }

            };
            var34.computeSliderComponent(var5::isVisible);
         } else if (var3 instanceof ModeSetting var6) {
            List var21 = var6.getValues();
            String[] var27 = var21 != null && !var21.isEmpty() ? var21.toArray(new String[0]) : new String[]{"Default"};
            ComboComponent var31 = new ComboComponent(this, var6.getName(), var27) {
               @Override
               public ComboComponent withValue(String value) {
                  super.withValue(value);
                  var6.setValue(value);
                  return this;
               }

            };
            var31.computeComboComponent(var6::isVisible);
         } else if (!(var3 instanceof MultiBooleanSetting var7)) {
            if (var3 instanceof ColorSetting var8) {
               Color var17 = var8.getValue();
               int var24 = var17 == null ? -1 : var17.getRGB();
               ColorPickerComponent var30 = new ColorPickerComponent(this, var8.getName(), var24) {
                  @Override
                  public ColorPickerComponent withCachedValue(Integer value) {
                     super.withCachedValue(value);
                     var8.handleAction(new Color(value, true));
                     return this;
                  }

               };
               var30.computeColorPickerComponent(var8::isVisible);
            } else if (var3 instanceof StringSetting var9) {
               String var18 = var9.getValue();
               TextFieldComponent var25 = new TextFieldComponent(this, var9.getName(), var18 == null ? "" : var18) {
                  @Override
                  public TextFieldComponent withCachedValue(String value) {
                     super.withCachedValue(value);
                     var9.setValue(value);
                     return this;
                  }

               };
               var25.computeTextFieldComponent(var9::isVisible);
            } else if (var3 instanceof IntegerSetting var10) {
               int var19 = var10.getValue().intValue();
               NumberComponent var26 = new NumberComponent(this, var10.getName(), var19) {
                  @Override
                  public Integer getValue() {
                     return var10.getValue().intValue();
                  }

               };
               var26.computeNumberComponent(var10::isVisible);
            } else if (var3 instanceof ButtonSetting var11) {
               SwitchComponent var20 = new SwitchComponent(this, var11.getName(), var11::press) {
                  @Override
                  public String getName() {
                     return var11.getName();
                  }
               };
               var20.computeSwitchComponent(var11::isVisible);
            }
         } else {
            List var12 = var7.getValue();
            ArrayList var13 = new ArrayList();
            if (var12 != null) {
               for (final BooleanSetting var15 : var12) {
                  CheckComponent var16 = new CheckComponent(var15.getName(), var15.getValue()) {
                  };
                  var13.add(var16);
               }
            }

            CategoryComponent var29 = new CategoryComponent(this, var7.getName(), var13.toArray(new CheckComponent[0]));
            var29.computeCategoryComponent(var7::isVisible);
         }
      }
   }

   public static dev.daamky.client.legacy.module.ModuleCategory computeModuleCategory(ModuleCategory tag) {
      if (tag == null) {
         return dev.daamky.client.legacy.module.ModuleCategory.OTHER;
      } else {
         return switch (tag) {
            case BATTLE -> dev.daamky.client.legacy.module.ModuleCategory.COMBAT;
            case MOTION -> dev.daamky.client.legacy.module.ModuleCategory.MOVEMENT;
            case VISUALS -> dev.daamky.client.legacy.module.ModuleCategory.RENDER;
            case ACTOR -> dev.daamky.client.legacy.module.ModuleCategory.PLAYER;
            case PVE -> dev.daamky.client.legacy.module.ModuleCategory.PVE;
            case MICS -> dev.daamky.client.legacy.module.ModuleCategory.OTHER;
            case HUD -> dev.daamky.client.legacy.module.ModuleCategory.RENDER;
         };
      }
   }

   @Override
   public boolean isEnabled() {
      return this.feature.isEnabled();
   }

   @Override
   public void handleEnabled(boolean enabled, boolean notification) {
      super.handleEnabled(enabled, false);
      this.feature.setEnabledInternal(enabled, false);
   }

   @Override
   public void runEnabled() {
      this.feature.toggle();
      super.handleEnabled(this.feature.isEnabled(), false);
   }

   @Override
   public void setKey(int key) {
      super.setKey(key);
      this.feature.setBind(key);
   }

   @Override
   public int getKey() {
      return this.feature.getBind();
   }

   public dev.daamky.client.module.Module getFeature() {
      return this.feature;
   }
}
