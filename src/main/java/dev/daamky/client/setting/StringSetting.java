package dev.daamky.client.setting;

import java.util.function.Supplier;

public class StringSetting extends Setting<String> {
   public int maxLength = 48;
   public String placeholder = "";

   public StringSetting(String name) {
      super(name);
      this.value = "";
   }

   public StringSetting value(String value) {
      this.setValue(value);
      return this;
   }

   public void setValue(String value) {
      this.value = value == null ? "" : value;
      this.invokeAction();
   }

   public StringSetting maxLength(int maxLength) {
      this.maxLength = maxLength;
      return this;
   }

   public StringSetting placeholder(String placeholder) {
      this.placeholder = placeholder == null ? "" : placeholder;
      return this;
   }

   public StringSetting visibleWhen(Supplier<Boolean> condition) {
      return (StringSetting)super.visibleWhen(condition);
   }

   public StringSetting onAction(Runnable action) {
      return (StringSetting)super.onAction(action);
   }

   public int getMaxLength() {
      return this.maxLength;
   }

   public String getPlaceholder() {
      return this.placeholder;
   }

   @Override
   public Setting withValue(String param1) {
      return this.value(param1);
   }
}
