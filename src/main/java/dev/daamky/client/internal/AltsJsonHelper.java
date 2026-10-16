package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import dev.daamky.client.mixin.MinecraftClientSessionAccessor;
import dev.daamky.client.model.RecordData;
import dev.daamky.client.util.MinecraftAccess;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.Session.AccountType;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

public class AltsJsonHelper {
   public static volatile AltsJsonHelper altsJsonHelper;
   public final List<AltsJsonHelper.AltAccount> alts = new ArrayList<>();
   public File altFile;
   public final Gson gson = new Gson();

   public static AltsJsonHelper getInstance() {
      if (altsJsonHelper == null) {
         synchronized (AltsJsonHelper.class) {
            if (altsJsonHelper == null) {
               altsJsonHelper = new AltsJsonHelper();
            }
         }
      }

      return altsJsonHelper;
   }

   public AltsJsonHelper() {
      try {
         File var1 = new File(ClientPaths.string5);
         if (!var1.exists()) {
            var1.mkdirs();
         }

         this.altFile = new File(var1, "alts.json");
         this.closeAlts();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public List<AltsJsonHelper.AltAccount> getAlts() {
      return this.alts;
   }

   public void sortAlts() {
      this.alts.sort(AltsJsonHelper::lambda$sortAlts$0);
   }

   public void handlePinned(AltsJsonHelper.AltAccount alt) {
      if (alt != null) {
         alt.pinned = !alt.pinned;
         if (alt.pinned) {
            for (AltsJsonHelper.AltAccount var3 : this.alts) {
               if (var3 != alt) {
                  var3.pinned = false;
               }
            }

            this.initName(alt);
         }

         this.sortAlts();
         this.closeClose();
      }
   }

   public void initName(AltsJsonHelper.AltAccount account) {
      if (account != null && account.name != null && !account.name.isEmpty()) {
         try {
            MinecraftClient var2 = MinecraftClient.getInstance();
            if (var2 == null) {
               return;
            }

            UUID var3 = FromStringHelper.computeName(account.name);
            Session var4 = new Session(account.name, var3, "", Optional.empty(), Optional.empty(), AccountType.MOJANG);
            ((MinecraftClientSessionAccessor)var2).setSession(var4);

            for (AltsJsonHelper.AltAccount var6 : this.alts) {
               var6.active = var6.equals(account);
            }

            this.closeClose();
         } catch (Throwable var7) {
            var7.printStackTrace();
         }
      }
   }

   public void addAlt(AltsJsonHelper.AltAccount alt) {
      if (alt != null && alt.name != null) {
         if (this.alts.stream().noneMatch(AltsJsonHelper::lambda$addAlt$1)) {
            alt.addAnim = new AnimatedValue();
            alt.addAnim.handleStart(0.0);
            this.alts.add(alt);
            this.sortAlts();
            this.loadSkinAsync(alt);
            this.closeClose();
         }
      }
   }

   public void handleRemove(AltsJsonHelper.AltAccount alt) {
      this.alts.remove(alt);
      this.closeClose();
   }

   public void initAnim(AltsJsonHelper.AltAccount alt) {
      if (alt != null) {
         alt.markedForRemoval = true;
         if (alt.removeAnim == null) {
            alt.removeAnim = new AnimatedValue();
         }

         alt.removeAnim.handleStart(0.0);
      }
   }

   public void loadSkinAsync(AltsJsonHelper.AltAccount alt) {
      if (alt != null && !alt.skinLoading && alt.skinTexturesSupplier == null) {
         alt.skinLoading = true;
         CompletableFuture.runAsync(AltsJsonHelper::lambda$loadSkinAsync$2);
      }
   }

   public void loadNext() {
      for (AltsJsonHelper.AltAccount var2 : this.alts) {
         this.loadSkinAsync(var2);
      }
   }

   public void closeClose() {
      if (this.altFile != null) {
         try (FileWriter var1 = new FileWriter(this.altFile)) {
            this.gson.toJson(this.alts, var1);
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      }
   }

   public void closeAlts() {
      if (this.altFile != null && this.altFile.exists()) {
         try (FileReader var1 = new FileReader(this.altFile)) {
            AltsJsonHelper.AltAccount[] var2 = (AltsJsonHelper.AltAccount[])this.gson.fromJson(var1, AltsJsonHelper.AltAccount[].class);
            if (var2 != null) {
               this.alts.clear();
               this.alts.addAll(Arrays.asList(var2));
               this.sortAlts();
            }
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      }
   }

   public static void lambda$loadSkinAsync$2(AltsJsonHelper.AltAccount alt) {
      try {
         UUID var1 = new RecordData().uuidByName(alt.name);
         if (var1 == null) {
            var1 = FromStringHelper.computeName(alt.name);
         }

         ProfileResult var2 = MinecraftAccess.mc.getSessionService().fetchProfile(var1, false);
         GameProfile var3 = var2 != null ? var2.profile() : null;
         if (var3 == null) {
            var3 = new GameProfile(var1, alt.name);
         }

         alt.skinTexturesSupplier = PlayerListEntry.texturesSupplier(var3);
      } catch (Throwable var7) {
         alt.skinTexturesSupplier = null;
      } finally {
         alt.skinLoading = false;
      }
   }

   public static boolean lambda$addAlt$1(AltsJsonHelper.AltAccount alt, AltsJsonHelper.AltAccount a) {
      return a.name.equalsIgnoreCase(alt.name);
   }

   public static int lambda$sortAlts$0(AltsJsonHelper.AltAccount a, AltsJsonHelper.AltAccount b) {
      return Boolean.compare(b.pinned, a.pinned);
   }

   public static class AltAccount {
      public String name;
      public String server;
      public String date;
      public boolean active;
      public boolean pinned;
      public transient AnimatedValue hoverAnim;
      public transient AnimatedValue addAnim;
      public transient AnimatedValue removeAnim;
      public transient AnimatedValue activeAnim;
      public transient AnimatedValue pulseAnim;
      public transient AnimatedValue pinAnim;
      public transient AnimatedValue pinHoverAnim;
      public transient boolean markedForRemoval;
      public transient Supplier<SkinTextures> skinTexturesSupplier;
      public transient boolean skinLoading;
      public transient Identifier cachedSkinIdentifier;

      public AltAccount(String name, String server) {
         this.name = namex;
         this.server = serverx;
         this.active = false;
         this.pinned = false;

         try {
            this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
         } catch (Throwable var4) {
            this.date = "16.08.2026 22:00";
         }
      }
   }
}
