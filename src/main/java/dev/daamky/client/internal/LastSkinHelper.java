package dev.daamky.client.internal;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import dev.daamky.client.model.RecordData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;

public class LastSkinHelper {
   public static final LastSkinHelper lastSkinHelper = new LastSkinHelper();
   public final Path path = Paths.get(ClientPaths.string5, "last_skin");
   public final DelayHelper delayHelper = new DelayHelper();
   public static Supplier<SkinTextures> supplier = null;
   public static boolean flag = false;

   public void load() {
      try {
         if (!Files.exists(this.path.getParent())) {
            Files.createDirectories(this.path.getParent());
         }

         if (!Files.exists(this.path)) {
            Files.createFile(this.path);
            Files.writeString(this.path, "");
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      RunnableHelper.handleExecute(this::lambda$load$0);
   }

   public void writeTrim(String skinName) {
      try {
         if (skinName != null && !skinName.trim().isEmpty()) {
            Files.writeString(this.path, skinName.trim());
         } else {
            Files.writeString(this.path, "");
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public String readPrint() {
      try {
         if (Files.exists(this.path)) {
            String var1 = Files.readString(this.path);
            boolean var2 = var1.isEmpty();
            if (!var2) {
               flag = true;
            }

            return var2 ? null : var1;
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return null;
   }

   public void run() {
   }

   public static Supplier<SkinTextures> initInit(String username) {
      UUID var2 = new RecordData().uuidByName(username);
      if (var2 == null) {
         var2 = FromStringHelper.computeName(username);
      }

      ProfileResult var1;
      GameProfile var3 = (var1 = MinecraftClient.getInstance().getSessionService().fetchProfile(var2, false)) == null ? null : var1.profile();
      if (var3 == null) {
         var3 = new GameProfile(var2, username);
      }

      return PlayerListEntry.texturesSupplier(var3);
   }

   public static Supplier<SkinTextures> getSupplier() {
      return flag ? supplier : null;
   }

   public static LastSkinHelper getInstance() {
      return lastSkinHelper;
   }

   public void lambda$load$0() {
      String var1 = this.readPrint();
      if (var1 != null && !var1.trim().isEmpty()) {
         while (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().getSkinProvider() == null || MinecraftClient.getInstance().getSessionService() == null) {
            try {
               Thread.sleep(250L);
            } catch (InterruptedException var3) {
               Thread.currentThread().interrupt();
               return;
            }
         }

         supplier = initInit(var1);
      }
   }
}
