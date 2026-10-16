package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.InventoryUtil;
import dev.daamky.client.internal.Pair;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.IntegerSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Assistant",
   category = ModuleCategory.ACTOR,
   description = "Умный помощник для различных задач"
)
public class Assistant extends Module {
   public static final Assistant assistant = new Assistant();
   public final Supplier<Boolean> supplier = this::lambda$new$0;
   public final Supplier<Boolean> mgocq = this::lambda$new$1;
   public final Supplier<Boolean> supplier2 = this::lambda$new$2;
   public Assistant.Mode mode = Assistant.Mode.FUNTIME;
   public final MultiBooleanSetting hotkeys = new MultiBooleanSetting("Functions").value(new BooleanSetting("Hotkeys").value(false));
   public final ModeSetting holyWorld = new ModeSetting("Mode")
      .value("Fun Time")
      .values("Fun Time", "Holy World")
      .visibleWhen(this.supplier)
      .onAction(this::lambda$new$3);
   public final BooleanSetting legit = new BooleanSetting("Legit").value(true).visibleWhen(this.supplier);
   public final Map<InventoryUtil.ItemUsage, Pair<IntegerSetting, Assistant.Mode>> map = new HashMap<>();
   public final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
   public final List<Pair<Long, Vec3d>> list = new ArrayList<>();
   public final Map<Vec3d, String> map2 = new HashMap<>();

   public Assistant() {
      this.map
         .put(new InventoryUtil.ItemUsage(Items.ENDER_EYE, this), new Pair<>(new IntegerSetting("Disorientation").value(-999), Assistant.Mode.FUNTIME));
      this.map.put(new InventoryUtil.ItemUsage(Items.NETHERITE_SCRAP, this), new Pair<>(new IntegerSetting("Trap").value(-999), Assistant.Mode.FUNTIME));
      this.map.put(new InventoryUtil.ItemUsage(Items.SUGAR, this), new Pair<>(new IntegerSetting("Clear dust").value(-999), Assistant.Mode.FUNTIME));
      this.map.put(new InventoryUtil.ItemUsage(Items.FIRE_CHARGE, this), new Pair<>(new IntegerSetting("Fire whirl").value(-999), Assistant.Mode.FUNTIME));
      this.map.put(new InventoryUtil.ItemUsage(Items.DRIED_KELP, this), new Pair<>(new IntegerSetting("Plast").value(-999), Assistant.Mode.FUNTIME));
      this.map.put(new InventoryUtil.ItemUsage(Items.PHANTOM_MEMBRANE, this), new Pair<>(new IntegerSetting("Divine aura").value(-999), Assistant.Mode.FUNTIME));
      this.map
         .put(new InventoryUtil.ItemUsage(Items.PRISMARINE_SHARD, this), new Pair<>(new IntegerSetting("Explosive trap").value(-999), Assistant.Mode.HOLYWORLD));
      this.map
         .put(new InventoryUtil.ItemUsage(Items.POPPED_CHORUS_FRUIT, this), new Pair<>(new IntegerSetting("Default trap").value(-999), Assistant.Mode.HOLYWORLD));
      this.map.put(new InventoryUtil.ItemUsage(Items.NETHER_STAR, this), new Pair<>(new IntegerSetting("Stun").value(-999), Assistant.Mode.HOLYWORLD));
      this.addSettings(new Setting[]{this.hotkeys, this.holyWorld});
      this.map.forEach(this::lambda$new$4);
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$5));
      Subscription var2 = Render2DEvent.getInstance().subscribe(new EventListener<>(this::handleRender2DEventData));
      Subscription var3 = PacketEvent.getInstance().subscribe(new EventListener<>(this::handlePacketEventData));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public void handleRender2DEventData(Render2DEvent.Render2DEventData event) {
   }

   public void handlePacketEventData(PacketEvent.PacketEventData event) {
   }

   public void handleTickEvent() {
      if (this.supplier.get() && mc.currentScreen == null) {
         this.map.forEach(this::lambda$handleTickEvent$6);
      }
   }

   public double computeSqrt(BlockPos pos1, BlockPos pos2) {
      double var3 = (double)(pos1.getX() - pos2.getX());
      double var5 = (double)(pos1.getY() - pos2.getY());
      double var7 = (double)(pos1.getZ() - pos2.getZ());
      return Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
   }

   public static Assistant getInstance() {
      return assistant;
   }

   public MultiBooleanSetting getHotkeys() {
      return this.hotkeys;
   }

   public ModeSetting getHolyWorld() {
      return this.holyWorld;
   }

   public void lambda$handleTickEvent$6(InventoryUtil.ItemUsage key, Pair value) {
      if (value.getRight() == this.mode) {
         key.handleUse(value.getLeft().getValue(), this.legit.getValue());
      }
   }

   public void lambda$onEvent$5(TickEvent event) {
      this.handleTickEvent();
   }

   public void lambda$new$4(InventoryUtil.ItemUsage key, Pair value) {
      if (value.getRight() == Assistant.Mode.HOLYWORLD) {
         value.getLeft().visibleWhen(this.supplier2);
      }

      if (value.getRight() == Assistant.Mode.FUNTIME) {
         value.getLeft().visibleWhen(this.mgocq);
      }

      this.addSettings(new Setting[]{value.getLeft()});
   }

   public void lambda$new$3() {
      String var1 = this.getHolyWorld().getValue();
      byte var2 = -1;
      switch (var1.hashCode()) {
         case 1382753550:
            if (var1.equals("Fun Time")) {
               var2 = 0;
            }
         default:
            this.mode = switch (var2) {
               case 0 -> Assistant.Mode.FUNTIME;
               default -> Assistant.Mode.HOLYWORLD;
            };
      }
   }

   public Boolean lambda$new$2() {
      return this.supplier.get() && this.mode == Assistant.Mode.HOLYWORLD;
   }

   public Boolean lambda$new$1() {
      return this.supplier.get() && this.mode == Assistant.Mode.FUNTIME;
   }

   public Boolean lambda$new$0() {
      return this.isEnabled() && this.hotkeys.isEnabled("Hotkeys");
   }

   public static enum Mode {
      FUNTIME,
      HOLYWORLD;

      public static Assistant.Mode[] $values() {
         return new Assistant.Mode[]{FUNTIME, HOLYWORLD};
      }
   }
}
