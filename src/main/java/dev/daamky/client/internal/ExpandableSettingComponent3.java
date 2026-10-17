package dev.daamky.client.internal;

import dev.daamky.client.setting.Setting;

public abstract class ExpandableSettingComponent3 extends Widget {
   public final QuintOutHelper quintOutHelper = new QuintOutHelper() {
   };

   public void run() {
      this.quintOutHelper.runOpen();
   }

   public void run2() {
      this.quintOutHelper.runOpen2();
   }

   public boolean isOpen() {
      return this.quintOutHelper.isOpen();
   }

   @Override
   public float getFloat() {
      return (float)this.quintOutHelper.getAnimatedValue().getValue();
   }

   public boolean check() {
      return (double)this.getFloat() < 0.8;
   }

   public QuintOutHelper getQuintOutHelper() {
      return this.quintOutHelper;
   }

   public abstract static class ExpandableSettingComponent extends SettingRow {
      public final QuintOutHelper quintOutHelper = new QuintOutHelper() {
      };

      public ExpandableSettingComponent(Setting<?> setting) {
         super(setting);
      }

      public void handleOpen(boolean value) {
         this.quintOutHelper.setOpen(value);
      }

      @Override
      public AnimatedValue getAnimatedValue() {
         return this.quintOutHelper.getAnimatedValue();
      }

   }
}
