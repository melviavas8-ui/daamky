package dev.daamky.client.render.shader;

import dev.daamky.client.module.render.Ambience;
import java.awt.Color;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class WorldShaderUniforms extends ManagedShader {
   public GlUniform class_284;
   public GlUniform class_2842;
   public GlUniform class_2843;

   public WorldShaderUniforms(Identifier id) {
      super(id, VertexFormats.POSITION_TEXTURE_COLOR);
   }

   public void handleEquals(float time, Color accent) {
      if (this.class_284 != null) {
         this.class_284.set(time);
      }

      if (this.class_2842 != null && accent != null) {
         this.class_2842.set((float)accent.getRed() / 255.0F, (float)accent.getGreen() / 255.0F, (float)accent.getBlue() / 255.0F);
      }

      if (this.class_2843 != null) {
         String var3 = Ambience.getInstance().pink.getValue();
         float var4 = 0.0F;
         if ("Red".equals(var3)) {
            var4 = 1.0F;
         } else if ("Blue".equals(var3)) {
            var4 = 2.0F;
         } else if ("Pink".equals(var3)) {
            var4 = 3.0F;
         }

         this.class_2843.set(var4);
      }
   }

   @Override
   public void runAccent() {
      this.class_284 = this.computeName("Time");
      this.class_2842 = this.computeName("Accent");
      this.class_2843 = this.computeName("Mode");
   }
}
