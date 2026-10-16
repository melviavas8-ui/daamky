package dev.daamky.client.internal;

import dev.daamky.client.module.render.NameTags;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.effect.StatusEffectInstance;

public class PotionEntryHelper {
   public final NameTags module;

   public PotionEntryHelper(NameTags module) {
      this.module = modulex;
   }

   public List<PotionEntryHelper.PotionEntry> initInit(Collection<StatusEffectInstance> effects) {
      ArrayList var2 = new ArrayList();

      for (StatusEffectInstance var4 : effects) {
         String var5 = var4.getTranslationKey();
         var5 = var5.substring(var5.lastIndexOf(".") + 1);
         var5 = var5.substring(0, 1).toUpperCase() + var5.substring(1);
         if (var4.getAmplifier() > 0) {
            var5 = var5 + " " + (var4.getAmplifier() + 1);
         }

         var5 = var5.replaceAll("_", " ");
         var2.add(new PotionEntryHelper.PotionEntry(var5, var4.getDuration()));
      }

      return var2;
   }

   public static record PotionEntry(String text, int duration) {
   }
}
