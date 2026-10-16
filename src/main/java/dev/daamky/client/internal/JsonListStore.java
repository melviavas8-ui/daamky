package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonListStore {
   public List<String> data = new ArrayList<>();
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

   public abstract String getFriends();

   public Path getPath() {
      return Path.of(ClientPaths.string5, this.getFriends() + ".json");
   }

   public void closeClose() {
      try {
         Files.createDirectories(Path.of(ClientPaths.string5));

         try (FileWriter var1 = new FileWriter(this.getPath().toFile())) {
            GSON.toJson(this.data, var1);
         }
      } catch (IOException var6) {
         throw new RuntimeException("Failed to save: " + this.getPath(), var6);
      }
   }

   public void initInit() {
      try {
         Files.createDirectories(Path.of(ClientPaths.string5));
         File var1 = this.getPath().toFile();
         if (!var1.exists()) {
            var1.createNewFile();
            this.closeClose();
            return;
         }

         try (FileReader var2 = new FileReader(var1)) {
            Type var3 = (new TypeToken<ArrayList<String>>() {
            }).getType();
            this.data = (List<String>)GSON.fromJson(var2, var3);
            if (this.data == null) {
               this.data = new ArrayList<>();
            }
         }
      } catch (IOException var7) {
         this.data = new ArrayList<>();
      }
   }

   public void handleData(String value) {
      if (value != null && !value.trim().isEmpty() && !this.data.contains(value)) {
         this.data.add(value);
         this.closeClose();
      }
   }

   public boolean computeRemove(String value) {
      boolean var2 = this.data.remove(value);
      if (var2) {
         this.closeClose();
      }

      return var2;
   }

   public List<String> getData() {
      return new ArrayList<>(this.data);
   }

   public boolean computeContains(String value) {
      return this.data.contains(value);
   }

}
