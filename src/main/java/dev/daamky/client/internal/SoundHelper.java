package dev.daamky.client.internal;

import dev.daamky.client.module.misc.ToggleSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class SoundHelper {
   public static final Identifier class_2960 = net.minecraft.util.Identifier.of(toLowerCase() + "smooth_on");
   public static SoundEvent class_3414 = net.minecraft.sound.SoundEvent.of(class_2960);
   public static final Identifier class_29602 = net.minecraft.util.Identifier.of(toLowerCase() + "smooth_off");
   public static SoundEvent class_34142 = net.minecraft.sound.SoundEvent.of(class_29602);
   public static final Identifier mepiik = net.minecraft.util.Identifier.of(toLowerCase() + "celestial_on");
   public static SoundEvent miujs = net.minecraft.sound.SoundEvent.of(mepiik);
   public static final Identifier class_29603 = net.minecraft.util.Identifier.of(toLowerCase() + "celestial_off");
   public static SoundEvent class_34143 = net.minecraft.sound.SoundEvent.of(class_29603);
   public static final Identifier mxivu = net.minecraft.util.Identifier.of(toLowerCase() + "nursultan_on");
   public static SoundEvent class_34144 = net.minecraft.sound.SoundEvent.of(mxivu);
   public static final Identifier class_29604 = net.minecraft.util.Identifier.of(toLowerCase() + "nursultan_off");
   public static SoundEvent class_34145 = net.minecraft.sound.SoundEvent.of(class_29604);
   public static final Identifier class_29605 = net.minecraft.util.Identifier.of(toLowerCase() + "akrien_on");
   public static SoundEvent class_34146 = net.minecraft.sound.SoundEvent.of(class_29605);
   public static final Identifier class_29606 = net.minecraft.util.Identifier.of(toLowerCase() + "akrien_off");
   public static SoundEvent class_34147 = net.minecraft.sound.SoundEvent.of(class_29606);
   public static final Identifier class_29607 = net.minecraft.util.Identifier.of(toLowerCase() + "tech_on");
   public static SoundEvent mpusg = net.minecraft.sound.SoundEvent.of(class_29607);
   public static final Identifier mqudb = net.minecraft.util.Identifier.of(toLowerCase() + "tech_off");
   public static SoundEvent class_34148 = net.minecraft.sound.SoundEvent.of(mqudb);
   public static final Identifier class_29608 = net.minecraft.util.Identifier.of(toLowerCase() + "blop_on");
   public static SoundEvent class_34149 = net.minecraft.sound.SoundEvent.of(class_29608);
   public static final Identifier class_29609 = net.minecraft.util.Identifier.of(toLowerCase() + "blop_off");
   public static SoundEvent class_341410 = net.minecraft.sound.SoundEvent.of(class_29609);

   public static void run() {
      Registry.register(Registries.SOUND_EVENT, class_2960, class_3414);
      Registry.register(Registries.SOUND_EVENT, class_29602, class_34142);
      Registry.register(Registries.SOUND_EVENT, mepiik, miujs);
      Registry.register(Registries.SOUND_EVENT, class_29603, class_34143);
      Registry.register(Registries.SOUND_EVENT, mxivu, class_34144);
      Registry.register(Registries.SOUND_EVENT, class_29604, class_34145);
      Registry.register(Registries.SOUND_EVENT, class_29605, class_34146);
      Registry.register(Registries.SOUND_EVENT, class_29606, class_34147);
      Registry.register(Registries.SOUND_EVENT, class_29607, mpusg);
      Registry.register(Registries.SOUND_EVENT, mqudb, class_34148);
      Registry.register(Registries.SOUND_EVENT, class_29608, class_34149);
      Registry.register(Registries.SOUND_EVENT, class_29609, class_341410);
   }

   public static void handleVolume(SoundEvent sound) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && var1.player != null && var1.world != null && var1.getCameraEntity() != null) {
         var1.world
            .playSound(
               var1.player, var1.getCameraEntity().getBlockPos(), sound, SoundCategory.BLOCKS, ToggleSounds.getInstance().volume.getValue() / 100.0F, 1.0F
            );
      }
   }

   public static String toLowerCase() {
      return "daamky".toLowerCase() + ":";
   }

   public SoundHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
