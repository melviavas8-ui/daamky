package dev.daamky.client.module.misc;

import com.mojang.authlib.GameProfile;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Render3DEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.BoxRenderer;
import dev.daamky.client.internal.ColorMath;
import dev.daamky.client.internal.Render;
import dev.daamky.client.internal.TextShaper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "SpecDetect",
   category = ModuleCategory.MICS,
   description = "Показывает, кто следит за вами в режиме спектатора"
)
public class SpecDetect extends Module {
   public static final SpecDetect specDetect = new SpecDetect();
   public final Set<Integer> mgeywew = ConcurrentHashMap.newKeySet();
   public final Map<UUID, GameProfile> map = new ConcurrentHashMap<>();
   public final Map<Integer, SpecDetect.PotentialEntity> map2 = new ConcurrentHashMap<>();
   public final Map<Integer, SpecDetect.ConfirmedVanished> map3 = new ConcurrentHashMap<>();
   public static final int intVal = 3;
   public static final long muqoda = 8000L;
   public static final long timeStamp = 6000L;
   public static final long long2 = 3000L;
   public long long3 = 0L;
   public final BooleanSetting chatAlerts = new BooleanSetting("Chat Alerts").value(true);
   public final BooleanSetting espRender = new BooleanSetting("ESP Render").value(true);
   public long long4 = 0L;
   public static final long long5 = 3000L;

   public SpecDetect() {
      this.addSettings(new Setting[]{this.chatAlerts, this.espRender});
   }

   public static SpecDetect getInstance() {
      return specDetect;
   }

   @Override
   public void onEvent() {
      Subscription var1 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   public void handleComp(PacketEvent.PacketEventData data) {
      if (data.isReceive()) {
         Packet var2 = data.packet();
         if (var2 instanceof EntitySpawnS2CPacket var3) {
            this.mgeywew.add(var3.getEntityId());
            this.map2.remove(var3.getEntityId());
            this.map3.remove(var3.getEntityId());
            if (var3.getEntityType() == EntityType.PLAYER) {
               UUID var4 = var3.getUuid();
               Iterator var5 = this.map3.values().iterator();

               while (var5.hasNext()) {
                  if (var4.equals(((SpecDetect.ConfirmedVanished)var5.next()).mioeo)) {
                     var5.remove();
                     return;
                  }
               }
            }
         }

         if (var2 instanceof PlayerListS2CPacket var9) {
            for (Action var17 : var9.getActions()) {
               if (var17 == Action.ADD_PLAYER) {
                  for (net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry var7 : var9.getPlayerAdditionEntries()) {
                     GameProfile var8 = var7.profile();
                     this.map.put(var8.getId(), var8);
                  }
               }
            }
         }

         if (var2 instanceof EntityPositionSyncS2CPacket var10) {
            int var14 = var10.id();
            if (this.mgeywew.contains(var14)) {
               return;
            }

            if (var14 == mc.player.getId()) {
               return;
            }

            if (mc.world != null && mc.world.getEntityById(var14) != null) {
               return;
            }

            Vec3d var18 = var10.values().position();
            this.initCurrent(var14, var18, var10.values().yaw(), "position_sync");
         }

         if (var2 instanceof EntityPositionS2CPacket var11) {
            int var15 = var11.entityId();
            if (this.mgeywew.contains(var15)) {
               return;
            }

            if (var15 == mc.player.getId()) {
               return;
            }

            if (mc.world != null && mc.world.getEntityById(var15) != null) {
               return;
            }

            this.initCurrent(var15, null, 0.0F, "rel_move");
         }

         if (var2 instanceof EntityTrackerUpdateS2CPacket var12) {
            int var16 = var12.id();
            if (this.mgeywew.contains(var16)) {
               return;
            }

            if (var16 == mc.player.getId()) {
               return;
            }

            if (mc.world != null && mc.world.getEntityById(var16) != null) {
               return;
            }

            this.initCurrent(var16, null, 0.0F, "tracker");
         }
      }
   }

   public void initCurrent(int id, Vec3d pos, float yaw, String method) {
      SpecDetect.PotentialEntity var5 = this.map2.get(id);
      if (var5 != null) {
         var5.intVal++;
         var5.long2 = System.currentTimeMillis();
         if (pos != null) {
            var5.doubleVal = pos.x;
            var5.double2 = pos.y;
            var5.double3 = pos.z;
            var5.floatVal = yaw;
         }

         if (var5.intVal >= 3 && !var5.flag) {
            var5.flag = true;
            SpecDetect.ConfirmedVanished var9 = new SpecDetect.ConfirmedVanished();
            var9.intVal = id;
            var9.doubleVal = var5.doubleVal;
            var9.double2 = var5.double2;
            var9.maedag = var5.double3;
            var9.floatVal = var5.floatVal;
            var9.long2 = var5.long2;
            var9.longVal = var5.longVal;
            var9.string2 = method;
            var9.mioeo = this.computeNext(id);
            String var7 = this.computeName(id);
            this.map3.put(id, var9);
            String var8 = var7 != null ? var7 : "ID:" + id;
            this.setMessage("§c[SpecDetect] §6" + var8 + " §fневидимый игрок (пакеты: " + var5.intVal + ")");
         }
      } else {
         SpecDetect.PotentialEntity var6 = new SpecDetect.PotentialEntity();
         var6.mfehom = id;
         var6.long2 = var6.longVal = System.currentTimeMillis();
         var6.intVal = 1;
         if (pos != null) {
            var6.doubleVal = pos.x;
            var6.double2 = pos.y;
            var6.double3 = pos.z;
            var6.floatVal = yaw;
         }

         var6.string = method;
         this.map2.put(id, var6);
      }
   }

   public UUID computeNext(int entityId) {
      if (mc.world == null) {
         return null;
      } else {
         for (PlayerEntity var3 : mc.world.getPlayers()) {
            if (var3.getId() == entityId) {
               return var3.getUuid();
            }
         }

         return null;
      }
   }

   public String computeName(int entityId) {
      if (mc.world == null) {
         return null;
      } else {
         return mc.world.getEntityById(entityId) instanceof OtherClientPlayerEntity var3 ? var3.getGameProfile().getName() : null;
      }
   }

   public void cleanup() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.long3 >= 3000L) {
         this.long3 = var1;
         this.map2.entrySet().removeIf(SpecDetect::lambda$cleanup$2);
         this.map3.entrySet().removeIf(SpecDetect::lambda$cleanup$3);
      }
   }

   public void initNext(Render3DEvent.Render3DEventData data) {
      this.cleanup();
      if (this.espRender.getValue() && !this.map3.isEmpty() && mc.world != null && mc.player != null) {
         MatrixStack var2 = data.getMatrixStack();
         Vec3d var3 = mc.gameRenderer.getCamera().getPos();
         long var4 = System.currentTimeMillis();
         Render.renderEffect.handleBlend(var2);
         Render.boxRenderer.initBlend(var2);

         for (SpecDetect.ConfirmedVanished var7 : this.map3.values()) {
            if ((var7.doubleVal != 0.0 || var7.double2 != 0.0 || var7.maedag != 0.0) && var4 - var7.long2 <= 5000L) {
               float var8 = (float)(0.6 + 0.4 * Math.sin((double)var4 * 0.005 + (double)var7.intVal));
               int var9 = (int)(200.0F * var8);
               Color var10 = new Color(255, 40, 40, var9);
               Render.boxRenderer
                  .handleFloat(
                     (float)(var7.doubleVal - 0.3),
                     (float)var7.double2,
                     (float)(var7.maedag - 0.3),
                     (float)(var7.doubleVal + 0.3),
                     (float)(var7.double2 + 1.8),
                     (float)(var7.maedag + 0.3),
                     2.0F,
                     var10,
                     BoxRenderer.Render.OUTLINE,
                     0.0F
                  );
               Render.boxRenderer
                  .handleFloat(
                     (float)(var7.doubleVal - 0.5),
                     (float)(var7.double2 - 0.2),
                     (float)(var7.maedag - 0.5),
                     (float)(var7.doubleVal + 0.5),
                     (float)(var7.double2 + 2.0),
                     (float)(var7.maedag + 0.5),
                     1.0F,
                     ColorMath.initBlue(Color.WHITE, 40),
                     BoxRenderer.Render.FILL,
                     0.0F
                  );
            }
         }

         Render.renderEffect.handleBlend2(var2);
      }
   }

   public void setMessage(String message) {
      if (this.chatAlerts.getValue()) {
         long var2 = System.currentTimeMillis();
         if (var2 - this.long4 >= 3000L) {
            this.long4 = var2;
            TextShaper.handleString(message);
         }
      }
   }

   public void runClear() {
      this.mgeywew.clear();
      this.map2.clear();
      this.map3.clear();
      this.map.clear();
      TextShaper.handleString("§a[SpecDetect] §fТрекинг сброшен");
   }

   public int size() {
      return this.map3.size();
   }

   public static boolean lambda$cleanup$3(long now, Entry e) {
      return now - e.getValue().long2 > 6000L;
   }

   public static boolean lambda$cleanup$2(long now, Entry e) {
      return now - e.getValue().long2 > 8000L;
   }

   public void lambda$onEvent$1(Render3DEvent.Render3DEventData data) {
      if (this.isEnabled()) {
         this.initNext(data);
      }
   }

   public void lambda$onEvent$0(PacketEvent.PacketEventData data) {
      if (this.isEnabled()) {
         this.handleComp(data);
      }
   }

   private static class ConfirmedVanished {
      public int intVal;
      public UUID mioeo;
      public String string;
      public double doubleVal;
      public double double2;
      public double maedag;
      public float floatVal;
      public long longVal;
      public long long2;
      public String string2;

      public ConfirmedVanished() {
      }
   }

   private static class PotentialEntity {
      public int mfehom;
      public double doubleVal;
      public double double2;
      public double double3;
      public float floatVal;
      public long longVal;
      public long long2;
      public int intVal;
      public boolean flag;
      public String string;

      public PotentialEntity() {
      }
   }
}
