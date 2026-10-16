package dev.daamky.client.setting;

import java.util.function.Supplier;

public abstract class Setting<T> {
   public String name;
   public T value;
   public Supplier<Boolean> visibilityCondition = Setting::lambda$new$0;
   public Runnable action;

   public Setting(String name) {
      this.name = namex;
   }

   public Setting<T> visibleWhen(Supplier<Boolean> condition) {
      this.visibilityCondition = condition;
      return this;
   }

   public void invokeAction() {
      if (this.action != null) {
         this.action.run();
      }
   }

   public boolean isValue(T newValue) {
      return this.value != null && newValue != null ? this.value == newValue : false;
   }

   public Setting<T> onAction(Runnable action) {
      this.action = actionx;
      return this;
   }

   public boolean isVisible() {
      return this.visibilityCondition.get();
   }

   public abstract Setting<T> withValue(T var1);

   public String getName() {
      return this.name;
   }

   public T getValue() {
      return this.value;
   }

   public Supplier<Boolean> getVisibilityCondition() {
      return this.visibilityCondition;
   }

   public Runnable getAction() {
      return this.action;
   }

   public void setName(String name) {
      this.name = namex;
   }

   public void setValue(T value) {
      this.value = (T)valuex;
   }

   public void setVisibilityCondition(Supplier<Boolean> visibilityCondition) {
      this.visibilityCondition = visibilityConditionx;
   }

   public void setAction(Runnable action) {
      this.action = actionx;
   }

   public static Boolean lambda$new$0() {
      return true;
   }
}
