package dev.daamky.client.render.shader;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class NoiseShader extends ManagedShader {
   public static final NoiseShader noiseShader = new NoiseShader();

   public NoiseShader() {
      super(Identifier.of("daamky", "noise/noise_shader"), VertexFormats.POSITION_COLOR);
   }
}
