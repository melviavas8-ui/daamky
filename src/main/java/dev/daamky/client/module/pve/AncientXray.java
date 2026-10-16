package dev.daamky.client.module.pve;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.PostMotionEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.Render;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;

@ModuleInfo(
   name = "AncientXray",
   category = ModuleCategory.PVE,
   subCategory = "PvE",
   description = "Поиск древних обломков в незере"
)
public class AncientXray extends Module {
   public static AncientXray ancientXray;
   public final FloatSetting range = new FloatSetting("Range").value(16.0F).range(8.0F, 32.0F).step(1.0F);
   public final FloatSetting packetsPerTick = new FloatSetting("Packets Per Tick").value(5.0F).range(1.0F, 50.0F).step(1.0F);
   public final ModeSetting both = new ModeSetting("Render Mode").value("Outline").values("Fill", "Outline", "Both");
   public final ColorSetting outlineColor = new ColorSetting("Outline Color")
      .computeColorSetting(new Color(255, 255, 0, 200))
      .computeVisible(this::lambda$new$0);
   public final ColorSetting fillColor = new ColorSetting("Fill Color").computeColorSetting(new Color(255, 255, 0, 50)).computeVisible(this::lambda$new$1);
   public final Set<BlockPos> mmozh = new CopyOnWriteArraySet<>();
   public final Set<BlockPos> miwva = new HashSet<>();
   public int mekqij = 0;
   public int intVal;
   public int mermur;
   public int int2;

   public static AncientXray getInstance() {
      if (ancientXray == null) {
         ancientXray = new AncientXray();
      }

      return ancientXray;
   }

   public AncientXray() {
      this.addSettings(new Setting[]{this.range, this.packetsPerTick, this.both, this.outlineColor, this.fillColor});
      ancientXray = this;
   }

   @Override
   public void onEnable() {
      this.miwva.clear();
      this.mmozh.clear();
      if (mc.player != null) {
         this.intVal = -this.range.getValue().intValue();
         this.mermur = -this.range.getValue().intValue();
         this.intVal2 = -this.range.getValue().intValue();
      }
   }

   @Override
   public void onEvent() {
      this.addEventListeners(
         new Subscription[]{
            PostMotionEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2)),
            Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$3)),
            PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$4))
         }
      );
   }

   @Override
   public void onDisable() {
      this.mmozh.clear();
      this.miwva.clear();
      super.onDisable();
   }

   public void initInit() {
      if (mc.player != null && mc.world != null) {
         int var1 = 0;
         int var2 = this.range.getValue().intValue();
         BlockPos var3 = mc.player.getBlockPos();
         int var4 = var3.getX();
         int var5 = var3.getY();
         int var6 = var3.getZ();
         Mutable var7 = new Mutable();

         while (var1 < this.packetsPerTick.getValue().intValue()) {
            if (this.intVal > var2) {
               this.intVal = -var2;
               this.mermur++;
            }

            if (this.mermur > var2) {
               this.mermur = -var2;
               this.intVal2++;
            }

            if (this.intVal2 > var2) {
               this.intVal2 = -var2;
               this.intVal = -var2;
               this.mermur = -var2;
               break;
            }

            int var9 = var5 + this.mermur;
            var7.set(var4 + this.intVal, var9, var6 + this.intVal2);
            this.intVal++;
            Block var8;
            if (var9 >= -64
               && var9 <= 320
               && !this.miwva.contains(var7)
               && (
                  (var8 = mc.world.getBlockState(var7).getBlock()) == Blocks.NETHERRACK
                     || var8 == Blocks.BASALT
                     || var8 == Blocks.BLACKSTONE
                     || var8 == Blocks.ANCIENT_DEBRIS
               )) {
               BlockPos var10 = var7.toImmutable();
               mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, var10, Direction.UP));
               this.miwva.add(var10);
               var1++;
            }
         }
      }
   }

   public void onPacket(PacketEvent.PacketEventData event) {
      if (mc.world != null) {
         if (event.packetType() == PacketEvent.PacketEventData.PacketType.RECEIVE) {
            if (event.packet() instanceof BlockUpdateS2CPacket var3) {
               this.handleAdd(var3.getPos(), var3.getState().getBlock());
            } else if (event.packet() instanceof ChunkDeltaUpdateS2CPacket var5) {
               var5.visitUpdates(this::lambda$onPacket$5);
            }
         }
      }
   }

   public void handleAdd(BlockPos pos, Block block) {
      if (block == Blocks.ANCIENT_DEBRIS) {
         this.mmozh.add(pos);
      } else {
         this.mmozh.remove(pos);
      }
   }

   public void handleBoth(Render3DEvent.Render3DEventData event) {
      if (this.isEnabled() && mc.player != null && mc.world != null) {
         for (BlockPos var3 : this.mmozh) {
            boolean var4 = this.both.is("Outline") || this.both.is("Both");
            boolean var5 = this.both.is("Fill") || this.both.is("Both");
            float var6 = (float)var3.getX();
            float var7 = (float)var3.getY();
            float var8 = (float)var3.getZ();
            float var9 = var6 + 1.0F;
            float var10 = var7 + 1.0F;
            float var11 = var8 + 1.0F;
            if (var5) {
               Render.boxRenderer.handleFloat(var6, var7, var8, var9, var10, var11, 0.0F, this.fillColor.getValue(), BoxRenderer.Render.FILL, 0.0F);
            }

            if (var4) {
               Render.boxRenderer.handleFloat(var6, var7, var8, var9, var10, var11, 1.5F, this.outlineColor.getValue(), BoxRenderer.Render.OUTLINE, 0.0F);
            }
         }
      }
   }

   public void lambda$onPacket$5(BlockPos pos, BlockState state) {
      this.handleAdd(pos, state.getBlock());
   }

   public void lambda$onEvent$4(PacketEvent.PacketEventData event) {
      this.onPacket(event);
   }

   public void lambda$onEvent$3(Render3DEvent.Render3DEventData event) {
      this.handleBoth(event);
   }

   public void lambda$onEvent$2(PostMotionEvent.EventPostMotionData event) {
      this.initInit();
   }

   public Boolean lambda$new$1() {
      return this.both.is("Fill") || this.both.is("Both");
   }

   public Boolean lambda$new$0() {
      return this.both.is("Outline") || this.both.is("Both");
   }
}
