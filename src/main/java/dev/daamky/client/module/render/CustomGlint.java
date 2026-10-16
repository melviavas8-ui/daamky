package dev.daamky.client.module.render;

import dev.daamky.client.internal.InputHelper;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.ColorSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.VertexFormats;

@ModuleInfo(
   name = "Custom Glint",
   category = ModuleCategory.VISUALS,
   description = "Кастомизация эффекта зачарования предметов"
)
public class CustomGlint extends Module {
   public static final CustomGlint customGlint = new CustomGlint();
   public static final ShaderProgramKey class_10156 = new ShaderProgramKey(InputHelper.getShader("glint/custom_glint"), VertexFormats.POSITION_TEXTURE, Defines.EMPTY);
   public static boolean flag = false;
   public final ModeSetting rainbow = new ModeSetting("Mode").value("Glass").values("Nebula", "Liquid", "Glass", "Rainbow");
   public final BooleanSetting onlyItems = new BooleanSetting("Only Items").value(true);
   public final BooleanSetting armorGlint = new BooleanSetting("Armor Glint").value(false);
   public final BooleanSetting solidGlint = new BooleanSetting("Solid Glint").value(false);
   public final ColorSetting color1 = new ColorSetting("Color 1").computeColorSetting(new Color(130, 60, 255));
   public final ColorSetting color2 = new ColorSetting("Color 2").computeColorSetting(new Color(255, 60, 180));
   public final FloatSetting flowSpeed = new FloatSetting("Flow Speed").value(1.0F).range(0.2F, 3.0F).step(0.1F);
   public final FloatSetting pulse = new FloatSetting("Pulse").value(0.4F).range(0.0F, 1.0F).step(0.05F);
   public final FloatSetting intensity = new FloatSetting("Intensity").value(1.0F).range(0.1F, 2.5F).step(0.05F);
   public long longVal = System.currentTimeMillis();

   public CustomGlint() {
      this.addSettings(
         new Setting[]{this.rainbow, this.onlyItems, this.armorGlint, this.solidGlint, this.color1, this.color2, this.flowSpeed, this.pulse, this.intensity}
      );
   }

   @Override
   public void onEnable() {
      this.longVal = System.currentTimeMillis();
   }

   @Override
   public void onEvent() {
   }

   public static ShaderProgramKey getClass_10156() {
      return class_10156;
   }

   public static boolean computeContains(ShaderProgramKey key) {
      if (customGlint != null && customGlint.isEnabled() && key != null && key.configId() != null) {
         String var1 = key.configId().getPath();
         if (!var1.contains("glint")) {
            return false;
         } else {
            if (!customGlint.armorGlint.getValue()) {
               if (flag || var1.contains("armor") || var1.contains("armor_entity_glint")) {
                  return false;
               }

               if (customGlint.onlyItems.getValue() && var1.contains("entity_glint") && !var1.contains("direct")) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void handleClass_5944(ShaderProgram sh) {
      if (sh != null) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var3.getWindow() != null) {
            float var4 = (float)var3.getWindow().getWidth();
            float var5 = (float)var3.getWindow().getHeight();
            if (sh.getUniform("uScreenSize") != null) {
               sh.getUniform("uScreenSize").set(var4, var5);
            }

            if (sh.getUniform("uTime") != null) {
               sh.getUniform("uTime").set((float)(System.currentTimeMillis() - this.longVal) / 1000.0F);
            }

            String var6 = this.rainbow.getValue();

            byte var2 = switch (var6) {
               case "Liquid" -> 1;
               case "Glass" -> 2;
               case "Rainbow" -> 3;
               default -> 0;
            };
            if (sh.getUniform("uGlintMode") != null) {
               sh.getUniform("uGlintMode").set(var2);
            }

            if (sh.getUniform("uSolidGlint") != null) {
               sh.getUniform("uSolidGlint").set(this.solidGlint.getValue() ? 1 : 0);
            }

            Color var8 = this.color1.getValue();
            Color var9 = this.color2.getValue();
            if (sh.getUniform("uColor1") != null) {
               sh.getUniform("uColor1").set((float)var8.getRed() / 255.0F, (float)var8.getGreen() / 255.0F, (float)var8.getBlue() / 255.0F);
            }

            if (sh.getUniform("uColor2") != null) {
               sh.getUniform("uColor2").set((float)var9.getRed() / 255.0F, (float)var9.getGreen() / 255.0F, (float)var9.getBlue() / 255.0F);
            }

            if (sh.getUniform("uFlowSpeed") != null) {
               sh.getUniform("uFlowSpeed").set(this.flowSpeed.getValue());
            }

            if (sh.getUniform("uPulse") != null) {
               sh.getUniform("uPulse").set(this.pulse.getValue());
            }

            if (sh.getUniform("uIntensity") != null) {
               sh.getUniform("uIntensity").set(this.intensity.getValue());
            }
         }
      }
   }

   public static CustomGlint getInstance() {
      return customGlint;
   }

   public ModeSetting getRainbow() {
      return this.rainbow;
   }

   public BooleanSetting getOnlyItems() {
      return this.onlyItems;
   }

   public BooleanSetting getArmorGlint() {
      return this.armorGlint;
   }

   public BooleanSetting getSolidGlint() {
      return this.solidGlint;
   }

   public ColorSetting getColor1() {
      return this.color1;
   }

   public ColorSetting getColor2() {
      return this.color2;
   }

   public FloatSetting getFlowSpeed() {
      return this.flowSpeed;
   }

   public FloatSetting getPulse() {
      return this.pulse;
   }

   public FloatSetting getIntensity() {
      return this.intensity;
   }
}
