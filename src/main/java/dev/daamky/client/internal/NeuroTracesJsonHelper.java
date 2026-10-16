package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NeuroTracesJsonHelper {
   public static final NeuroTracesJsonHelper INSTANCE = new NeuroTracesJsonHelper();
   public final List<RecordedPath> jumpTraces = new ArrayList<>();
   public final List<RecordedPath> groundTraces = new ArrayList<>();
   public final File saveFile = new File("neuro_traces.json");
   public final Gson gson = new GsonBuilder().setPrettyPrinting().create();

   public static NeuroTracesJsonHelper getInstance() {
      return INSTANCE;
   }

   public List<RecordedPath> getJumpTraces() {
      return this.jumpTraces;
   }

   public List<RecordedPath> getGroundTraces() {
      return this.groundTraces;
   }

   public void handleAdd(RecordedPath trace) {
      if (!trace.points.isEmpty()) {
         if (trace.jump) {
            this.jumpTraces.add(trace);
         } else {
            this.groundTraces.add(trace);
         }
      }
   }

   public RecordedPath computeTraces(boolean isJumping) {
      List var2 = isJumping && !this.jumpTraces.isEmpty() ? this.jumpTraces : this.groundTraces;
      return var2.isEmpty() ? null : (RecordedPath)var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
   }

   public void runClear() {
      this.jumpTraces.clear();
      this.groundTraces.clear();
   }

   public void closeAdd() {
      try {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.jumpTraces);
         var1.addAll(this.groundTraces);

         try (FileWriter var2 = new FileWriter(this.saveFile)) {
            this.gson.toJson(var1, var2);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public void closeClose() {
      if (this.saveFile.exists()) {
         try (FileReader var1 = new FileReader(this.saveFile)) {
            Type var2 = (new TypeToken<ArrayList<RecordedPath>>() {
            }).getType();
            List var3 = (List)this.gson.fromJson(var1, var2);
            this.runClear();
            if (var3 != null) {
               for (RecordedPath var5 : var3) {
                  this.handleAdd(var5);
               }
            }

            System.out.println("Loaded " + (this.jumpTraces.size() + this.groundTraces.size()) + " traces.");
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }
   }

   public boolean checkTraces() {
      return !this.jumpTraces.isEmpty() || !this.groundTraces.isEmpty();
   }
}
