package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.daamky.client.module.Module;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DraggableManagerSaveHelper {
   public static final DraggableManagerSaveHelper draggableManagerSaveHelper = new DraggableManagerSaveHelper();
   public final LinkedHashMap<String, AutoHelper> draggables = new LinkedHashMap<>();
   public final LinkedHashMap<String, AutoHelper> pendingLoadedDraggables = new LinkedHashMap<>();
   public final File CONFIG_DIR = new File(ClientPaths.string5 + "/drags.json");
   public final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
   public final AtomicBoolean savePending = new AtomicBoolean(false);
   public static final ScheduledExecutorService saveExecutor = Executors.newSingleThreadScheduledExecutor(DraggableManagerSaveHelper::lambda$static$0);

   public AutoHelper initShow(Module feature, String name, float x, float y) {
      AutoHelper var5 = new AutoHelper(feature, name, x, y);
      AutoHelper var6 = this.pendingLoadedDraggables.get(name);
      if (var6 != null) {
         var5.setX(var6.getX());
         var5.setY(var6.getY());
         var5.setBlur(var6.isBlur());
         var5.setAttachInfoHUD(var6.getAttachInfoHUD());
         var5.setMergeCards(var6.getMergeCards());
         float var7 = var6.getScale();
         if (var7 < 0.1F) {
            var7 = 1.0F;
         }

         var5.setScale(var7);
         var5.setShowCombat(var6.getShowCombat());
         var5.setShowMovement(var6.getShowMovement());
         var5.setShowRender(var6.getShowRender());
         var5.setShowPlayer(var6.getShowPlayer());
         var5.setShowOther(var6.getShowOther());
         var5.setShowSuffix(var6.getShowSuffix());
         var5.setAlignSide(var6.getAlignSide());
         var5.setCardMode(var6.getCardMode());
         var5.setGraphMode(var6.getGraphMode());
         if (var6.lastScreenWidth > 0.0F) {
            var5.lastScreenWidth = var6.lastScreenWidth;
            var5.lastScreenHeight = var6.lastScreenHeight;
         }

         this.pendingLoadedDraggables.remove(name);
      }

      this.draggables.put(name, var5);
      return var5;
   }

   public void save() {
      if (this.savePending.compareAndSet(false, true)) {
         saveExecutor.schedule(this::lambda$save$1, 500L, TimeUnit.MILLISECONDS);
      }
   }

   public void initConfig() {
      if (!this.CONFIG_DIR.exists()) {
         this.CONFIG_DIR.getParentFile().mkdirs();
      }

      if (this.CONFIG_DIR.toPath().getFileSystem().isOpen()) {
         try {
            LinkedHashMap var1;
            synchronized (this.draggables) {
               var1 = new LinkedHashMap<>(this.draggables);
            }

            String var6 = this.GSON.toJson(var1);
            Files.writeString(this.CONFIG_DIR.toPath(), var6);
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      } else {
         System.err.println("File runtime closed. Could not save drag data.");
      }
   }

   public void initShow() {
      if (!this.CONFIG_DIR.exists()) {
         this.CONFIG_DIR.getParentFile().mkdirs();
      } else {
         try {
            String var1 = Files.readString(this.CONFIG_DIR.toPath());
            Map var2 = (Map)this.GSON.fromJson(var1, (new TypeToken<Map<String, AutoHelper>>() {
            }).getType());
            if (var2 != null) {
               for (Entry var4 : var2.entrySet()) {
                  String var5 = (String)var4.getKey();
                  AutoHelper var6 = (AutoHelper)var4.getValue();
                  if (var6 != null) {
                     AutoHelper var7 = this.draggables.get(var5);
                     if (var7 != null) {
                        var7.setX(var6.getX());
                        var7.setY(var6.getY());
                        var7.setBlur(var6.isBlur());
                        var7.setAttachInfoHUD(var6.getAttachInfoHUD());
                        var7.setMergeCards(var6.getMergeCards());
                        float var8 = var6.getScale();
                        if (var8 < 0.1F) {
                           var8 = 1.0F;
                        }

                        var7.setScale(var8);
                        var7.setShowCombat(var6.getShowCombat());
                        var7.setShowMovement(var6.getShowMovement());
                        var7.setShowRender(var6.getShowRender());
                        var7.setShowPlayer(var6.getShowPlayer());
                        var7.setShowOther(var6.getShowOther());
                        var7.setShowSuffix(var6.getShowSuffix());
                        var7.setAlignSide(var6.getAlignSide());
                        var7.setCardMode(var6.getCardMode());
                        var7.setGraphMode(var6.getGraphMode());
                        if (var6.lastScreenWidth > 0.0F) {
                           var7.lastScreenWidth = var6.lastScreenWidth;
                           var7.lastScreenHeight = var6.lastScreenHeight;
                        }

                        this.draggables.put(var5, var7);
                     } else {
                        this.pendingLoadedDraggables.put(var5, var6);
                     }
                  }
               }
            }
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }
   }

   public static DraggableManagerSaveHelper getInstance() {
      return draggableManagerSaveHelper;
   }

   public LinkedHashMap<String, AutoHelper> getDraggables() {
      return this.draggables;
   }

   public void lambda$save$1() {
      this.savePending.set(false);
      this.initConfig();
   }

   public static Thread lambda$static$0(Runnable r) {
      Thread var1 = new Thread(r, "DraggableManager-Save");
      var1.setDaemon(true);
      return var1;
   }
}
