package dev.daamky.client.module.combat;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "HitSounds",
   category = ModuleCategory.BATTLE,
   description = "Проигрывает звук при попадании по врагу"
)
public class HitSounds extends Module {
   public static final Map<String, String> map = new LinkedHashMap<String, String>() {
      {
         this.put("Bell", "bell");
         this.put("Metallic", "metallic");
         this.put("Bonk", "bonk");
         this.put("Bubble", "bubble");
         this.put("Cod", "cod");
         this.put("Colocol", "colocol");
         this.put("Crime", "crime");
         this.put("Fatality", "fatality");
         this.put("Neverlose", "neverlose");
         this.put("Primordial", "primordial");
         this.put("Sonic Coin", "soniccoin");
         this.put("Squeak", "squeak");
         this.put("UwU", "uwu");
         this.put("Warn", "warn");
         this.put("Ahh", "ahh");
         this.put("Beep", "beep");
         this.put("Boom", "boom");
         this.put("Bruh", "bruh");
         this.put("Crack", "crack");
         this.put("Discord", "ds");
         this.put("ErrorXP", "error_xp");
         this.put("Faaah", "faaah");
         this.put("Gay?????", "gayyyyy");
         this.put("Pay", "pay");
         this.put("Punch", "punch");
         this.put("Rizz", "rizz");
         this.put("Sad Bell", "sad_bell");
         this.put("Wow", "wow");
      }
   };
   public static final HitSounds hitSounds = new HitSounds();
   public final ModeSetting bell = new ModeSetting("Sound").value("Bell").values(map.keySet().toArray(new String[0]));
   public final FloatSetting volume = new FloatSetting("Volume").value(50.0F).range(5.0F, 100.0F).step(5.0F);
   public final BooleanSetting stereoBalance = new BooleanSetting("Stereo Balance").value(true);
   public final Map<String, Clip> map2 = new ConcurrentHashMap<>();
   public ExecutorService executorService;

   public HitSounds() {
      this.addSettings(new Setting[]{this.bell, this.volume, this.stereoBalance});
   }

   @Override
   public void onEnable() {
      this.executorService = Executors.newCachedThreadPool();
   }

   @Override
   public void onDisable() {
      if (this.executorService != null && !this.executorService.isShutdown()) {
         this.executorService.shutdownNow();
         this.executorService = null;
      }
   }

   @Override
   public void onEvent() {
      Subscription var1 = AttackEvent.getInstance().subscribe(new EventListener<>(this::handleEntity));
      this.addEventListeners(new Subscription[]{var1});
   }

   public void handleEntity(AttackEvent.AttackEventData event) {
      if (mc.player != null && mc.world != null) {
         Entity var2 = event.entity();
         if (var2 != null) {
            this.playSound(var2);
         }
      }
   }

   public void playSound(Entity entity) {
      if (this.executorService != null && !this.executorService.isShutdown()) {
         this.executorService.submit(this::lambda$playSound$0);
      }
   }

   public void playSoundInternal(Entity entity) {
      String var2 = this.bell.getValue();
      String var3 = map.get(var2);
      if (var3 == null) {
         System.err.println("[HitSounds] Unknown sound: " + var2);
      } else {
         try {
            Clip var4 = this.map2.computeIfAbsent(var3, HitSounds::lambda$playSoundInternal$1);
            if (var4 == null) {
               return;
            }

            var4.stop();
            var4.setFramePosition(0);
            if (var4.isControlSupported(Type.MASTER_GAIN)) {
               FloatControl var5 = (FloatControl)var4.getControl(Type.MASTER_GAIN);
               float var6 = this.volume.getValue() / 100.0F;
               float var7 = (float)(Math.log10(Math.max((double)var6, 1.0E-4)) * 20.0);
               var7 = Math.max(var7, var5.getMinimum());
               var7 = Math.min(var7, var5.getMaximum());
               var5.setValue(var7);
            }

            if (this.stereoBalance.getValue() && entity != null && mc.player != null && var4.isControlSupported(Type.BALANCE)) {
               Vec3d var12 = entity.getPos().subtract(mc.player.getPos());
               double var13 = MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var12.z, var12.x)) - 90.0);
               double var8 = MathHelper.wrapDegrees(var13 - (double)mc.player.getYaw());
               if (Math.abs(var8) > 180.0) {
                  var8 -= Math.signum(var8) * 360.0;
               }

               FloatControl var10 = (FloatControl)var4.getControl(Type.BALANCE);
               var10.setValue((float)Math.max(-1.0, Math.min(1.0, var8 / 180.0)));
            }

            var4.start();
            var4.start();
         } catch (Exception var11) {
            System.err.println("[HitSounds] Error playing sound '" + var2 + "': " + var11.getMessage());
         }
      }
   }

   public static HitSounds getInstance() {
      return hitSounds;
   }

   public static Clip lambda$playSoundInternal$1(String name) {
      try {
         byte[] var1 = InputHelper.closeClose(name + ".wav");
         if (var1 == null) {
            System.err.println("[HitSounds] Sound file not found: " + name + ".wav");
            return null;
         } else {
            AudioInputStream var2 = AudioSystem.getAudioInputStream(new ByteArrayInputStream(var1));
            AudioFormat var3 = var2.getFormat();
            AudioFormat var4 = new AudioFormat(
               Encoding.PCM_SIGNED, var3.getSampleRate(), 16, var3.getChannels(), var3.getChannels() * 2, var3.getSampleRate(), false
            );
            AudioInputStream var5 = AudioSystem.getAudioInputStream(var4, var2);
            Clip var6 = AudioSystem.getClip();
            var6.open(var5);
            return var6;
         }
      } catch (Exception var7) {
         System.err.println("[HitSounds] Failed to load sound: " + name);
         return null;
      }
   }

   public void lambda$playSound$0(Entity entity) {
      this.playSoundInternal(entity);
   }
}
