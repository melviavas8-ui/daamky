package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigStore {
   public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public static final Path CONFIG_DIR = Paths.get("config/autobuy");

   public static void initInit(String name) {
      try {
         Files.createDirectories(CONFIG_DIR);
         AutoBuy var1 = AutoBuy.getInstance();
         ConfigStore.Data var2 = new ConfigStore.Data(
            var1.getV6(),
            new HashSet<>(var1.getSet()),
            NameFilter.getFlag(),
            NameFilter.initInit(),
            var1.isV(),
            var1.getInt2(),
            var1.getInt3(),
            var1.getString(),
            var1.check2(),
            var1.getInt5(),
            var1.check3(),
            var1.getInt6(),
            var1.getInt4(),
            var1.check4(),
            var1.getInt7()
         );

         try (FileWriter var3 = new FileWriter(CONFIG_DIR.resolve(name + ".json").toFile())) {
            GSON.toJson(var2, var3);
         }
      } catch (IOException var8) {
         var8.printStackTrace();
      }
   }

   public static void closeAuto(String name) {
      Path var1 = CONFIG_DIR.resolve(name + ".json");
      if (Files.exists(var1)) {
         try {
            try (FileReader var2 = new FileReader(var1.toFile())) {
               ConfigStore.Data var3 = (ConfigStore.Data)GSON.fromJson(var2, (new TypeToken<ConfigStore.Data>() {
               }).getType());
               if (var3 != null) {
                  AutoBuy var4 = AutoBuy.getInstance();
                  if (var3.multiplier != null) {
                     var4.setV(var3.multiplier);
                  }

                  if (var3.disabledItems != null) {
                     var4.getSet().addAll(var3.disabledItems);
                  }

                  if (var3.staffEnabled != null) {
                     NameFilter.setEnabled(var3.staffEnabled);
                  }

                  if (var3.staffNicknames != null) {
                     NameFilter.handleNext(var3.staffNicknames);
                  }

                  if (var3.enableAutoPay != null) {
                     var4.setV2(var3.enableAutoPay);
                  }

                  if (var3.leftSum != null) {
                     var4.setInt2(var3.leftSum);
                  }

                  if (var3.payWhen != null) {
                     var4.setInt3(var3.payWhen);
                  }

                  if (var3.targetName != null) {
                     var4.setString(var3.targetName);
                  }

                  if (var3.enableRejoin != null) {
                     var4.setFlag(var3.enableRejoin);
                  }

                  if (var3.joinDelay != null) {
                     var4.setInt5(var3.joinDelay);
                  }

                  if (var3.randomAnarchy != null) {
                     var4.setFlag2(var3.randomAnarchy);
                  }

                  if (var3.anarchyNumber != null) {
                     var4.setInt6(var3.anarchyNumber);
                  }

                  if (var3.autoSellMoney != null) {
                     var4.setInt4(var3.autoSellMoney);
                  }

                  if (var3.enableAutoParse != null) {
                     var4.setFlag3(var3.enableAutoParse);
                  }

                  if (var3.priceParseDelay != null) {
                     var4.setInt7(var3.priceParseDelay);
                  }

                  return;
               }
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }

   public static void handleDelete(String name) {
      try {
         Files.deleteIfExists(CONFIG_DIR.resolve(name + ".json"));
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public static List<String> list() {
      if (!Files.exists(CONFIG_DIR)) {
         return Collections.emptyList();
      } else {
         try {
            List var1;
            try (Stream var0 = Files.list(CONFIG_DIR)) {
               var1 = var0.filter(ConfigStore::lambda$list$0).map(ConfigStore::lambda$list$1).collect(Collectors.toList());
            }

            return var1;
         } catch (IOException var5) {
            return Collections.emptyList();
         }
      }
   }

   public static String lambda$list$1(Path p) {
      return p.getFileName().toString().replace(".json", "");
   }

   public static boolean lambda$list$0(Path p) {
      return p.toString().endsWith(".json");
   }

   static class Data {
      public Double multiplier;
      public HashSet<ItemActionCatalog> disabledItems;
      public Boolean staffEnabled;
      public HashSet<String> staffNicknames;
      public Boolean enableAutoPay;
      public Integer leftSum;
      public Integer payWhen;
      public String targetName;
      public Boolean enableRejoin;
      public Integer joinDelay;
      public Boolean randomAnarchy;
      public Integer anarchyNumber;
      public Integer autoSellMoney;
      public Boolean enableAutoParse;
      public Integer priceParseDelay;

      public Data(
         Double multiplier,
         HashSet<ItemActionCatalog> disabledItems,
         Boolean staffEnabled,
         HashSet<String> staffNicknames,
         Boolean enableAutoPay,
         Integer leftSum,
         Integer payWhen,
         String targetName,
         Boolean enableRejoin,
         Integer joinDelay,
         Boolean randomAnarchy,
         Integer anarchyNumber,
         Integer autoSellMoney,
         Boolean enableAutoParse,
         Integer priceParseDelay
      ) {
         this.multiplier = multiplierx;
         this.disabledItems = disabledItemsx;
         this.staffEnabled = staffEnabledx;
         this.staffNicknames = staffNicknamesx;
         this.enableAutoPay = enableAutoPayx;
         this.leftSum = leftSumx;
         this.payWhen = payWhenx;
         this.targetName = targetNamex;
         this.enableRejoin = enableRejoinx;
         this.joinDelay = joinDelayx;
         this.randomAnarchy = randomAnarchyx;
         this.anarchyNumber = anarchyNumberx;
         this.autoSellMoney = autoSellMoneyx;
         this.enableAutoParse = enableAutoParsex;
         this.priceParseDelay = priceParseDelayx;
      }
   }
}
