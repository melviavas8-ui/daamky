package dev.daamky.client.module.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.daamky.client.internal.NamedChoice;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.render.shader.WorldShaderUniforms;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(
   name = "Ambience",
   category = ModuleCategory.VISUALS,
   description = "Позволяет менять время суток и погоду визуально"
)
public class Ambience extends Module {
   public static final Ambience ambience = new Ambience();
   public final ModeSetting time = new ModeSetting("Time").value(Ambience.WorldTime.DAY).enumValues(Ambience.WorldTime.values());
   public final ModeSetting weather = new ModeSetting("Weather").value(Ambience.Weather.SUNNY).enumValues(Ambience.Weather.values());
   public final BooleanSetting customFog = new BooleanSetting("Custom fog").value(false);
   public final ColorSetting fogColor = new ColorSetting("Fog color").computeColorSetting(new Color(200, 200, 200)).computeVisible(this.customFog::getValue);
   public final FloatSetting fogDistance = new FloatSetting("Fog distance").value(-8.0F).range(-8.0F, 25.0F).step(1.0F).visibleWhen(this.customFog::getValue);
   public final FloatSetting fogDensity = new FloatSetting("Fog density").value(100.0F).range(0.0F, 100.0F).step(1.0F).visibleWhen(this.customFog::getValue);
   public final ModeSetting polarshine = new ModeSetting("Sky Shader").value("Off").values("Off", "Nebula", "Thunder", "PolarShine");
   public final ModeSetting pink = new ModeSetting("Polar Color").value("Green").values("Green", "Red", "Blue", "Pink").visibleWhen(this::lambda$new$0);
   public final ModeSetting modeSetting = new ModeSetting("Nebula Color")
      .value("Purple")
      .values("Purple", "Blue", "Red", "Pink", "Gray", "Custom")
      .visibleWhen(this::lambda$new$1);
   public final ColorSetting customNebula = new ColorSetting("Custom Nebula").computeColorSetting(new Color(180, 50, 220)).computeVisible(this::lambda$new$2);
   public WorldShaderUniforms worldShaderUniforms;
   public WorldShaderUniforms worldShaderUniforms2;
   public WorldShaderUniforms moraoly;

   public Ambience() {
      this.addSettings(
         new Setting[]{
            this.time,
            this.weather,
            this.customFog,
            this.fogColor,
            this.fogDistance,
            this.fogDensity,
            this.polarshine,
            this.pink,
            this.modeSetting,
            this.customNebula
         }
      );
      this.worldShaderUniforms = new WorldShaderUniforms(Identifier.of("daamky", "shaders/sky_nebula"));
      this.worldShaderUniforms2 = new WorldShaderUniforms(Identifier.of("daamky", "shaders/sky_thunder"));
      this.moraoly = new WorldShaderUniforms(Identifier.of("daamky", "shaders/sky_polarshine"));
   }

   public WorldShaderUniforms getEquals() {
      String var1 = this.polarshine.getValue();
      if ("Thunder".equals(var1)) {
         return this.worldShaderUniforms2;
      } else {
         return "PolarShine".equals(var1) ? this.moraoly : this.worldShaderUniforms;
      }
   }

   public Color getColor() {
      String var1 = this.polarshine.getValue();
      if ("Nebula".equals(var1)) {
         String var5 = this.modeSetting.getValue();

         return switch (var5) {
            case "Blue" -> new Color(50, 140, 255);
            case "Red" -> new Color(255, 60, 60);
            case "Pink" -> new Color(255, 105, 180);
            case "Gray" -> new Color(170, 170, 185);
            case "Purple" -> new Color(160, 60, 240);
            case "Custom" -> (Color)this.customNebula.getValue();
            default -> new Color(160, 60, 240);
         };
      } else if ("PolarShine".equals(var1)) {
         String var2 = this.pink.getValue();

         return switch (var2) {
            case "Red" -> new Color(255, 60, 60);
            case "Blue" -> new Color(55, 180, 255);
            case "Pink" -> new Color(255, 105, 180);
            default -> new Color(137, 255, 119);
         };
      } else {
         return Color.WHITE;
      }
   }

   public long computeLong(long original) {
      if (mc.world != null && this.isEnabled()) {
         Ambience.WorldTime var3 = NamedChoice.getChoiceByName(this.time.getValue(), Ambience.WorldTime.values());
         if (var3 == null) {
            return original;
         } else {
            return switch (var3) {
               case DAWN -> 23041L;
               case DAY -> 1000L;
               case NOON -> 6000L;
               case DUSK -> 12610L;
               case NIGHT -> 13000L;
               case MID_NIGHT -> 18000L;
               default -> original;
            };
         }
      } else {
         return original;
      }
   }

   public boolean checkColor() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && var1.world != null && this.isEnabled() && this.customFog.getValue()) {
         GlStateManager._clearColor(
            (float)this.fogColor.getValue().getRed() / 255.0F,
            (float)this.fogColor.getValue().getGreen() / 255.0F,
            (float)this.fogColor.getValue().getBlue() / 255.0F,
            (float)this.fogColor.getValue().getAlpha() / 255.0F
         );
         return true;
      } else {
         return false;
      }
   }

   public Fog initFog(Camera camera, float viewDistance, Fog fog) {
      if (this.isEnabled() && this.customFog.getValue()) {
         float var4 = MathHelper.clamp(this.fogDistance.getValue(), -8.0F, viewDistance);
         float var5 = MathHelper.clamp(this.fogDistance.getValue() + this.fogDensity.getValue(), 0.0F, viewDistance);
         FogShape var6 = fog.shape();
         CameraSubmersionType var7 = camera.getSubmersionType();
         if (var7 == CameraSubmersionType.NONE) {
            var6 = FogShape.SPHERE;
         }

         return new Fog(
            var4,
            var5,
            var6,
            (float)this.fogColor.getValue().getRed() / 255.0F,
            (float)this.fogColor.getValue().getGreen() / 255.0F,
            (float)this.fogColor.getValue().getBlue() / 255.0F,
            (float)this.fogColor.getValue().getAlpha() / 255.0F
         );
      } else {
         return fog;
      }
   }

   @Override
   public void onEvent() {
   }

   public static Ambience getInstance() {
      return ambience;
   }

   public Boolean lambda$new$2() {
      return "Nebula".equals(this.polarshine.getValue()) && "Custom".equals(this.modeSetting.getValue());
   }

   public Boolean lambda$new$1() {
      return "Nebula".equals(this.polarshine.getValue());
   }

   public Boolean lambda$new$0() {
      return "PolarShine".equals(this.polarshine.getValue());
   }

   public static enum Weather implements ModeSetting.NamedChoice {
      NO_CHANGE("No change"),
      SUNNY("Sunny"),
      RAINY("Rainy"),
      SNOWY("Snowy"),
      THUNDER("Thunder");

      public final String name;

      @Override
      public String getName() {
         return this.name;
      }

      private Weather(String name) {
         this.name = name;
      }

      public static Ambience.Weather[] $values() {
         return new Ambience.Weather[]{NO_CHANGE, SUNNY, RAINY, SNOWY, THUNDER};
      }
   }

   private static enum WorldTime implements ModeSetting.NamedChoice {
      NO_CHANGE("No change"),
      DAWN("Dawn"),
      DAY("Day"),
      NOON("Noon"),
      DUSK("Dusk"),
      NIGHT("Night"),
      MID_NIGHT("Mid Night");

      public final String name;

      private WorldTime(String name) {
         this.name = name;
      }

      @Override
      public String getName() {
         return this.name;
      }

      public static Ambience.WorldTime[] $values() {
         return new Ambience.WorldTime[]{NO_CHANGE, DAWN, DAY, NOON, DUSK, NIGHT, MID_NIGHT};
      }
   }
}
