package dev.daamky.client.module.misc;

import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.PacketEvent;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.TickEvent;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.notification.NotificationManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Language;
import java.util.Map.Entry;

@ModuleInfo(
   name = "Totem Info",
   category = ModuleCategory.MICS,
   description = "Отслеживает взрывы тотемов в радиусе 50 блоков и предупреждает об окончании эффектов"
)
public class TotemInfo extends Module {
   public static final TotemInfo totemInfo = new TotemInfo();
   public final Map<UUID, Long> mvios = new HashMap<>();
   public final Map<UUID, Boolean> map = new HashMap<>();
   public final Set<RegistryEntry<StatusEffect>> set = new HashSet<>();
   public final Map<RegistryEntry<StatusEffect>, String> map2 = new HashMap<>();

   @Override
   public void onEvent() {
      Subscription var1 = TickEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$0));
      Subscription var2 = PacketEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$1));
      this.addEventListeners(new Subscription[]{var1, var2});
   }

   @Override
   public void onDisable() {
      this.set.clear();
      this.map2.clear();
      this.map.clear();
      this.mvios.clear();
   }

   public boolean computeContains(ItemStack stack) {
      if (stack == null || stack.isEmpty()) {
         return false;
      } else if (!stack.isOf(Items.TOTEM_OF_UNDYING)) {
         return false;
      } else {
         String var2 = stack.getName().getString().toLowerCase();
         return var2.contains("чар") || var2.contains("элит") || var2.contains("лучш") || var2.contains("super") || stack.hasGlint();
      }
   }

   public static TotemInfo getInstance() {
      return totemInfo;
   }

   public void lambda$onEvent$1(PacketEvent.PacketEventData event) {
      if (mc.player != null && mc.world != null) {
         if (event.isReceive()) {
            Packet var4 = event.packet();
            Entity var2;
            EntityStatusS2CPacket var3;
            if (var4 instanceof EntityStatusS2CPacket && (var3 = (EntityStatusS2CPacket)var4).getStatus() == 35 && (var2 = var3.getEntity(mc.world)) instanceof PlayerEntity
               )
             {
               PlayerEntity var5 = (PlayerEntity)var2;
               if (var5.distanceTo(mc.player) <= 50.0F) {
                  UUID var6 = var5.getUuid();
                  long var7 = System.currentTimeMillis();
                  if (var7 - this.mvios.getOrDefault(var6, 0L) > 500L) {
                     this.mvios.put(var6, var7);
                     boolean var9 = this.map.getOrDefault(var6, false);
                     String var10 = var9 ? "чар. тотем" : "обычный тотем";
                     String var11 = var5.getName().getString();
                     String var12 = var11 + " потерял " + var10;
                     NotificationManager.initAdd(var12, NotificationManager.Notification.Type.WARNING, 3500L);
                  }
               }
            }
         }
      }
   }

   public void lambda$onEvent$0(TickEvent event) {
      if (mc.player != null && mc.world != null) {
         for (PlayerEntity var3 : mc.world.getPlayers()) {
            if (var3.distanceTo(mc.player) <= 50.0F) {
               boolean var4 = this.computeContains(var3.getMainHandStack()) || this.computeContains(var3.getOffHandStack());
               this.map.put(var3.getUuid(), var4);
            }
         }

         Collection var9 = mc.player.getStatusEffects();
         HashMap var10 = new HashMap();

         for (StatusEffectInstance var5 : var9) {
            var10.put(var5.getEffectType(), var5.getDuration());
         }

         for (StatusEffectInstance var15 : var9) {
            RegistryEntry var6 = var15.getEffectType();
            int var7 = var15.getDuration() / 20;
            if (var7 == 10) {
               if (!this.set.contains(var6)) {
                  String var8 = Language.getInstance().get(((StatusEffect)var6.value()).getTranslationKey());
                  NotificationManager.initAdd("Эффект " + var8 + " закончится через 10 сек.", NotificationManager.Notification.Type.WARNING, 3500L);
                  this.set.add(var6);
               }
            } else if (var7 > 10) {
               this.set.remove(var6);
            }
         }

         for (Entry var16 : this.map2.entrySet()) {
            if (!var10.containsKey(var16.getKey())) {
               NotificationManager.initAdd("Эффект " + (String)var16.getValue() + " закончился", NotificationManager.Notification.Type.ERROR, 3500L);
               this.set.remove(var16.getKey());
            }
         }

         this.map2.clear();

         for (StatusEffectInstance var17 : var9) {
            String var18 = Language.getInstance().get(((StatusEffect)var17.getEffectType().value()).getTranslationKey());
            this.map2.put(var17.getEffectType(), var18);
         }
      }
   }
}
