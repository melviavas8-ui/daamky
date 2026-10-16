package dev.daamky.client.internal;

import dev.daamky.client.legacy.module.Module;
import java.util.function.Supplier;

public class Component<Value> implements Callback3, RenderAccess {
   public Runnable onAction;
   public Runnable onSetVisible;
   public Value value;
   public String name;
   public Supplier<Boolean> visible = Component::lambda$new$0;
   public Module parent;
   public final ValueToHelper animation = new ValueToHelper();
   public final Direction2 animation1 = new Direction2(300, 1.0);

   public String getName() {
      return this.name;
   }

   public Component(String name, Value value) {
      this.name = namex;
      this.value = (Value)valuex;
   }

   public Component(Module parent, String name, Value value) {
      this.parent = parentx;
      this.name = namex;
      this.value = (Value)valuex;
      parentx.getSettings().add(this);
   }

   public Component<?> withValue(Value value) {
      this.value = (Value)valuex;
      if (mc.world != null && mc.player != null && this.onAction != null) {
         this.onAction.run();
      }

      return this;
   }

   @Override
   public Component<?> withValue(Supplier<Boolean> value) {
      this.visible = valuex;
      if (mc.world != null && mc.player != null && this.onSetVisible != null) {
         this.onSetVisible.run();
      }

      return this;
   }

   public Component<?> withOnAction(Runnable action) {
      this.onAction = action;
      return this;
   }

   public Component<?> withOnSetVisible(Runnable action) {
      this.onSetVisible = action;
      return this;
   }

      public Runnable getOnAction() {
      return this.onAction;
   }

      public Runnable getOnSetVisible() {
      return this.onSetVisible;
   }

      public Value getValue() {
      return this.value;
   }

      public Supplier<Boolean> getVisible() {
      return this.visible;
   }

      public Module getParent() {
      return this.parent;
   }

      public ValueToHelper getAnimation() {
      return this.animation;
   }

      public Direction2 getAnimation1() {
      return this.animation1;
   }

   public static Boolean lambda$new$0() {
      return true;
   }
}
