package dev.daamky.client.internal;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.FloatControl.Type;

public class EncodingHelper {
   public static final CopyOnWriteArrayList<Clip> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

   public static void handlePlay(String location, float volume) {
      playSound_wav(location, volume, 1.0F);
   }

   public static void playSound_wav(String location, float volume, float pitch) {
      CompletableFuture.runAsync(EncodingHelper::lambda$playSound_wav$1);
   }

   public static void openOpen(Clip clip, AudioInputStream stream, float pitch) throws Exception {
      float var3 = Math.max(0.5F, Math.min(2.0F, pitch));
      AudioInputStream var4 = stream;
      AudioFormat var5 = stream.getFormat();
      if (var5.getEncoding() != Encoding.PCM_SIGNED) {
         var4 = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, stream);
         var5 = var4.getFormat();
      }

      byte[] var6 = var4.readAllBytes();
      if (var4 != stream) {
         var4.close();
      }

      int var7 = var5.getFrameSize();
      if (var7 <= 0) {
         clip.open(var5, var6, 0, var6.length);
      } else {
         int var8 = var6.length / var7;
         int var9 = (int)((float)var8 / var3);
         if (var9 <= 0) {
            clip.open(var5, var6, 0, var6.length);
         } else {
            byte[] var10 = new byte[var9 * var7];

            for (int var11 = 0; var11 < var9; var11++) {
               int var12 = Math.min(var8 - 1, (int)((float)var11 * var3));
               System.arraycopy(var6, var12 * var7, var10, var11 * var7, var7);
            }

            clip.open(var5, var10, 0, var10.length);
         }
      }
   }

   public static void cleanUpClips() {
      copyOnWriteArrayList.removeIf(EncodingHelper::lambda$cleanUpClips$2);
   }

   public static boolean lambda$cleanUpClips$2(Clip clip) {
      return !clip.isOpen();
   }

   public static void lambda$playSound_wav$1(String location, float pitch, float volume) {
      try {
         cleanUpClips();
         String var3 = "/assets/client/sound/" + location + ".wav";
         InputStream var4 = EncodingHelper.class.getResourceAsStream(var3);
         if (var4 == null) {
            return;
         }

         try (AudioInputStream var5 = AudioSystem.getAudioInputStream(new BufferedInputStream(var4))) {
            Clip var6 = AudioSystem.getClip();
            if (Math.abs(pitch - 1.0F) < 0.001F) {
               var6.open(var5);
            } else {
               openOpen(var6, var5, pitch);
            }

            if (var6.isControlSupported(Type.MASTER_GAIN)) {
               FloatControl var7 = (FloatControl)var6.getControl(Type.MASTER_GAIN);
               float var8 = Math.max(0.0F, Math.min(1.0F, volume));
               float var9 = (float)(Math.log10(var8 <= 0.0F ? 1.0E-4 : (double)var8) * 20.0);
               var7.setValue(var9);
            }

            var6.addLineListener(EncodingHelper::lambda$playSound_wav$0);
            copyOnWriteArrayList.add(var6);
            var6.start();
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   public static void lambda$playSound_wav$0(Clip clip, LineEvent event) {
      if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
         clip.close();
         copyOnWriteArrayList.remove(clip);
      }
   }
}
