package dev.daamky.client.module.player;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.DelayHelper;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.ThemeColor;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.Setting;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Fake Lag",
   category = ModuleCategory.ACTOR,
   description = "Искусственно задерживает пакеты, создавая иллюзию лагов"
)
public class FakeLag extends Module {
   public static final FakeLag fakeLag = new FakeLag();
   public final FloatSetting resetTime = new FloatSetting("Reset time").value(100.0F).range(0.0F, 1000.0F).step(10.0F);
   public final BooleanSetting render = new BooleanSetting("Render").value(true);
   public final BooleanSetting resetOnKnockback = new BooleanSetting("Reset on knockback").value(true);
   public Vec3d class_243 = net.minecraft.util.math.Vec3d.ZERO;
   public final Queue<Packet<?>> queue = new ConcurrentLinkedQueue<>();
   public final DelayHelper mipipy = new DelayHelper();
   public boolean flag;

   public FakeLag() {
      this.addSettings(new Setting[]{this.resetTime, this.render, this.resetOnKnockback});
   }

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      Subscription var3 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$2));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public boolean getMc() {
      return mc.isInSingleplayer();
   }

   public void runPoll() {
      if (!this.flag) {
         this.flag = true;

         try {
            while (!this.queue.isEmpty()) {
               this.handleClass_2596(this.queue.poll());
            }

            this.mipipy.runCurrent();
            if (mc.player != null) {
               this.class_243 = mc.player.getPos();
               return;
            }
         } finally {
            this.flag = false;
         }
      }
   }

   @Override
   public void onEnable() {
      this.mipipy.runCurrent();
      if (mc.player != null) {
         this.class_243 = mc.player.getPos();
      }
   }

   @Override
   public void onDisable() {
      this.runPoll();
   }

   public static FakeLag getInstance() {
      return fakeLag;
   }

   public void lambda$onEvent$2(Render3DEvent.Render3DEventData event) {
      if (!this.getMc() && this.class_243 != null) {
         if (this.render.getValue() && mc.player != null) {
            ClientPlayerEntity var2 = mc.player;
            float var3 = var2.getWidth() / 2.0F;
            float var4 = (float)this.class_243.x;
            float var5 = (float)this.class_243.y;
            float var6 = (float)this.class_243.z;
            Render.boxRenderer
               .handleFloat(
                  var4 - var3,
                  var5,
                  var6 - var3,
                  var4 + var3,
                  var5 + var2.getHeight(),
                  var6 + var3,
                  3.0F,
                  ColorMath.initBlue(ThemeColor.computeColor(0), 200),
                  BoxRenderer.Render.STRIPED,
                  var2.getWidth() / 5.5F
               );
         }
      }
   }

   public void lambda$onEvent$1(PacketEvent.PacketEventData event) {
      if (!this.getMc()) {
         if (event.isReceive()) {
            if (event.packet() instanceof ExplosionS2CPacket) {
               this.runPoll();
            }

            EntityVelocityUpdateS2CPacket var2;
            Packet var3;
            if (mc.player != null
               && (var3 = event.packet()) instanceof EntityVelocityUpdateS2CPacket
               && (var2 = (EntityVelocityUpdateS2CPacket)var3).getEntityId() == mc.player.getId()
               && this.resetOnKnockback.getValue()) {
               this.runPoll();
            }
         } else if (event.isSend()) {
            if (this.flag) {
               return;
            }

            Packet var4 = event.packet();
            if (var4 instanceof KeepAliveC2SPacket || var4 instanceof CommonPongC2SPacket || var4 instanceof TeleportConfirmC2SPacket) {
               return;
            }

            if (var4 instanceof PlayerInteractEntityC2SPacket
               || var4 instanceof UpdateSelectedSlotC2SPacket
               || var4 instanceof HandSwingC2SPacket
               || var4 instanceof PlayerInteractBlockC2SPacket
               || var4 instanceof PlayerInteractItemC2SPacket
               || var4 instanceof ClickSlotC2SPacket) {
               this.runPoll();
               return;
            }

            this.queue.add(var4);
            PacketEvent.getInstance().setCancelled(true);
         }
      }
   }

   public void lambda$onEvent$0(TickEvent event) {
      if (!this.getMc() && this.mipipy.computeCurrent(this.resetTime.getValue().longValue())) {
         this.runPoll();
      }
   }
}
