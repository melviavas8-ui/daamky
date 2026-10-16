package dev.daamky.client.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiBooleanSetting extends Setting<List<BooleanSetting>> {
   public MultiBooleanSetting(String name) {
      super(name);
      this.value = new ArrayList<>();
   }

   public MultiBooleanSetting value(List<BooleanSetting> value) {
      this.setValue(value);
      return this;
   }

   public MultiBooleanSetting value(BooleanSetting... value) {
      this.setValue(new ArrayList<>(Arrays.asList(value)));
      return this;
   }

   public void setValue(List<BooleanSetting> value) {
      if (!this.isValue(value)) {
         super.setValue(value);
         if (value != null) {
            for (BooleanSetting var3 : value) {
               if (var3 != null) {
                  Runnable var4 = var3.getAction();
                  var3.onAction(this::lambda$setValue$0);
               }
            }
         }

         this.invokeAction();
      }
   }

   public MultiBooleanSetting onAction(Runnable action) {
      return (MultiBooleanSetting)super.onAction(action);
   }

   public MultiBooleanSetting addValues(BooleanSetting... value) {
      if (value != null) {
         for (BooleanSetting var5 : value) {
            if (var5 != null) {
               Runnable var6 = var5.getAction();
               var5.onAction(this::lambda$addValues$1);
               this.value.add(var5);
            }
         }
      }

      this.invokeAction();
      return this;
   }

   public boolean isEnabled(String name) {
      if (this.value != null && name != null) {
         for (int var2 = 0; var2 < this.value.size(); var2++) {
            BooleanSetting var3 = this.value.get(var2);
            if (var3 != null && name.equalsIgnoreCase(var3.getName())) {
               return var3.getValue() != null && var3.getValue();
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean has(String name) {
      if (this.value != null && name != null) {
         for (int var2 = 0; var2 < this.value.size(); var2++) {
            BooleanSetting var3 = this.value.get(var2);
            if (var3 != null && name.equalsIgnoreCase(var3.getName())) {
               var3.value(!var3.getValue());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public List<String> getValues() {
      return this.value.stream().map(MultiBooleanSetting::lambda$getAllNames$2).collect(Collectors.toList());
   }

   public List<String> getAllNames() {
      return this.value.stream().filter(MultiBooleanSetting::lambda$getList$3).map(MultiBooleanSetting::lambda$getList$4).collect(Collectors.toList());
   }

   public Optional<BooleanSetting> get(String name) {
      if (this.value != null && name != null) {
         for (int var2 = 0; var2 < this.value.size(); var2++) {
            BooleanSetting var3 = this.value.get(var2);
            if (var3 != null && name.equalsIgnoreCase(var3.getName())) {
               return Optional.of(var3);
            }
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public MultiBooleanSetting visibleWhen(Supplier<Boolean> condition) {
      return (MultiBooleanSetting)super.visibleWhen(condition);
   }

   @Override
   public void setValue(Object param1) {
      this.setValue((List<BooleanSetting>)var1);
   }

   @Override
   public Setting withValue(Object param1) {
      return this.value((List<BooleanSetting>)var1);
   }

   public static Object lambda$getList$4(Object s) {
      return s.getName();
   }

   public static boolean lambda$getList$3(Object s) {
      return Boolean.TRUE.equals(s.getValue());
   }

   public static Object lambda$getAllNames$2(Object s) {
      return s.getName();
   }

   public void lambda$addValues$1(Runnable oldAction) {
      if (oldAction != null) {
         oldAction.run();
      }

      this.invokeAction();
   }

   public void lambda$setValue$0(Runnable oldAction) {
      if (oldAction != null) {
         oldAction.run();
      }

      this.invokeAction();
   }
}
