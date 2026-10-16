package dev.daamky.client.module.render;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Render2DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.EntityFilterHelper;
import dev.daamky.client.internal.ItemsHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.MultiBooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.function.Supplier;

@ModuleInfo(
   name = "Name Tags",
   category = ModuleCategory.VISUALS,
   description = "Показывает крупные никнеймы, броню и здоровье над игроками"
)
public class NameTags extends Module {
   public static final NameTags nameTags = new NameTags();
   public final MultiBooleanSetting items = new MultiBooleanSetting("Targets")
      .value(
         new BooleanSetting("Self").value(true),
         new BooleanSetting("Players").value(true),
         new BooleanSetting("Animals").value(false),
         new BooleanSetting("Mobs").value(false),
         new BooleanSetting("Items").value(true)
      );
   public final FloatSetting scale = new FloatSetting("Scale").value(1.0F).range(0.1F, 2.0F).step(0.1F);
   public final MultiBooleanSetting armor = new MultiBooleanSetting("Information")
      .value(
         new BooleanSetting("Items").value(true),
         new BooleanSetting("Potions").value(true),
         new BooleanSetting("Health").value(true),
         new BooleanSetting("Armor").value(true)
      );
   public final Supplier<Boolean> supplier = this::lambda$new$0;
   public final MultiBooleanSetting onlyHands = new MultiBooleanSetting("Options")
      .value(
         new BooleanSetting("Special items").value(false).visibleWhen(this.supplier),
         new BooleanSetting("Enchants").value(true).visibleWhen(this.supplier),
         new BooleanSetting("Only hands").value(false).visibleWhen(this.supplier)
      );
   public final BooleanSetting blur = new BooleanSetting("Blur").value(true);
   public final FloatSetting glassy = new FloatSetting("Glassy").value(0.5F).range(0.0F, 1.0F).step(0.1F);
   public final ColorSetting textColor = new ColorSetting("Text color").computeColorSetting(new Color(255, 255, 255));
   public final ColorSetting color = new ColorSetting("Color").computeColorSetting(new Color(20, 20, 20));
   public final ColorSetting friendColor = new ColorSetting("Friend color").computeColorSetting(new Color(132, 229, 121)).computeVisible(this::lambda$new$1);
   public final EntityFilterHelper.EntityFilter entityFilter = new EntityFilterHelper.EntityFilter(this.items.getAllNames());
   public final ItemsHelper itemsHelper = new ItemsHelper(this);

   public NameTags() {
      this.addSettings(new Setting[]{this.items, this.scale, this.armor, this.onlyHands, this.blur, this.glassy, this.textColor, this.color, this.friendColor});
   }

   @Override
   public void onEvent() {
      Subscription var1 = Render2DEvent.getInstance().subscribe(new EventListener<>(1, this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1});
   }

   public static NameTags getInstance() {
      return nameTags;
   }

   public void lambda$onEvent$2(Render2DEvent.Render2DEventData event) {
      this.entityFilter.targetSettings = this.items.getAllNames();
      this.entityFilter.mukey = true;
      this.itemsHelper.handleContext(event);
   }

   public Boolean lambda$new$1() {
      return this.items.isEnabled("Players") || this.items.isEnabled("Self");
   }

   public Boolean lambda$new$0() {
      return this.armor.isEnabled("Items");
   }
}
