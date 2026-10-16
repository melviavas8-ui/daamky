package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.PriceHelper;
import dev.daamky.client.model.enums.ServerWorld;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;

@ModuleInfo(
   name = "Auction Helper",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Помогает искать выгодные предметы на аукционе"
)
public class AuctionHelper extends Module {
   public static final AuctionHelper auctionHelper = new AuctionHelper();
   public final PriceHelper priceHelper = new PriceHelper();
   public final ModeSetting mode = new ModeSetting("Mode").value(this.priceHelper.serverWorld).enumValues(ServerWorld.values()).onAction(this::lambda$new$0);
   public final FloatSetting parsedSlots = new FloatSetting("Parsed slots").value(100.0F).range(0.0F, 100.0F).step(1.0F);
   public final List<Slot> list = new ArrayList<>();

   public AuctionHelper() {
      this.addSettings(new Setting[]{this.mode, this.parsedSlots});
   }

   public ModeSetting getMode() {
      return this.mode;
   }

   @Override
   public void onEvent() {
      Subscription var1 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void initContains(DrawContext context, Slot slot) {
      if (this.isEnabled() && this.list.contains(slot)) {
         int var3 = slot.x;
         int var4 = slot.y;
         context.fill(var3, var4, var3 + 16, var4 + 16, new Color(0, 255, 0, 100).getRGB());
      }
   }

   public static AuctionHelper getInstance() {
      return auctionHelper;
   }

   public void lambda$onEvent$1(UpdateEvent event) {
      if (mc.player != null) {
         if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var2) {
            this.list.clear();
         }
      }
   }

   public void lambda$new$0() {
      PriceHelper var10000 = this.priceHelper;
      String var1 = this.getMode().getValue();

      var10000.serverWorld = switch (var1) {
         case "Spooky Time" -> ServerWorld.SPOOKY_TIME;
         case "Holy World" -> ServerWorld.HOLY_WORLD;
         case "Really World" -> ServerWorld.REALLY_WORLD;
         case "Fun Time" -> ServerWorld.FUN_TIME;
         default -> ServerWorld.SPOOKY_TIME;
      };
   }
}
