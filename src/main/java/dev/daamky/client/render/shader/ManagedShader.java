package dev.daamky.client.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.daamky.client.mixin.ShaderProgramAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
// import Object /*class_5942*/ protected
import org.jetbrains.annotations.ApiStatus.Internal;

public class ManagedShader {
   public static final List<Runnable> list = new ArrayList<>();
   public ShaderProgram class_5944;
   public ShaderProgramKey class_10156;

   public ManagedShader(Identifier id, VertexFormat vertexFormat) {
      this(id, vertexFormat, DrawMode.QUADS, 0);
   }

   public ManagedShader(Identifier id, VertexFormat vertexFormat, DrawMode drawMode, int uniformBlockSize) {
      this.class_10156 = new ShaderProgramKey(id.withPrefixedPath("core/"), vertexFormat, Defines.EMPTY);
      list.add(() -> this.lambda$new$0(id));
   }

   public void runAccent() {
   }

   public GlUniform computeName(String name) {
      return this.class_5944 == null ? null : ((ShaderProgramAccessor)this.class_5944).getUniformsByName().get(name);
   }

   public GlUniform computeClass_284(String name) {
      return this.computeName(name);
   }

   public void handleVal(String name, float... values) {
      GlUniform var3 = this.computeClass_284(name);
      if (var3 != null) {
         if (var3.getFloatData() == null) {
            if (values.length == 1) {
               var3.set((int)values[0]);
            } else if (values.length == 2) {
               var3.set((int)values[0], (int)values[1]);
            } else if (values.length == 3) {
               var3.set((int)values[0], (int)values[1], (int)values[2]);
            } else if (values.length == 4) {
               var3.set((int)values[0], (int)values[1], (int)values[2], (int)values[3]);
            }
         } else if (values.length == 1) {
            var3.set(values[0]);
         } else if (values.length == 2) {
            var3.set(values[0], values[1]);
         } else if (values.length == 3) {
            var3.set(values[0], values[1], values[2]);
         } else if (values.length == 4) {
            var3.set(values[0], values[1], values[2], values[3]);
         }
      }
   }

   public void handleVal(String name, int value) {
      GlUniform var3 = this.computeClass_284(name);
      if (var3 != null) {
         if (var3.getFloatData() != null) {
            var3.set((float)value);
         } else {
            var3.set(value);
         }
      }
   }

   public void runShader() {
      RenderSystem.setShader(this.class_10156);
   }

   public Object /*class_5942*/ initInit() {
      return new Object /*class_5942*/(this.class_10156);
   }

   public static void handleCustomUniforms(ManagedShader program, BuiltBuffer builtBuffer, ManagedShader.CustomUniforms uniforms) {
      handleShader(program, builtBuffer, uniforms, null);
   }

   public static void handleShader(ManagedShader program, BuiltBuffer builtBuffer, ManagedShader.CustomUniforms uniforms, Object textureView) {
      if (textureView instanceof AbstractTexture) {
         RenderSystem.setShaderTexture(0, ((AbstractTexture)textureView).getGlId());
      } else if (textureView instanceof Identifier) {
         RenderSystem.setShaderTexture(0, (Identifier)textureView);
      } else if (textureView instanceof Integer) {
         RenderSystem.setShaderTexture(0, (Integer)textureView);
      }

      program.runShader();
      if (uniforms != null) {
         uniforms.handleNext(program);
      }

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      BufferRenderer.drawWithGlobalProgram(builtBuffer);
      RenderSystem.setShaderTexture(0, 0);
   }

   @Internal
   public static void run() {
      list.forEach(Runnable::run);
   }

   public void lambda$new$0(Identifier id) {
      try {
         if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getShaderLoader() != null) {
            this.class_5944 = MinecraftClient.getInstance().getShaderLoader().getProgramToLoad(this.class_10156);
            this.runAccent();
         }
      } catch (Throwable var3) {
         System.err.println("[GlProgram] Warning: could not load " + id + ": " + var3.getMessage());
      }
   }

   public static class CustomUniforms {
      public final List<ManagedShader.CustomUniforms.UniformEntry> list = new ArrayList<>();

      public CustomUniforms(int sizeBytes) {
      }

      public void initAdd(int offset, float val) {
         this.list.add(new ManagedShader.CustomUniforms.FloatEntry(offset, val));
      }

      public void initAdd(int offset, float x, float y) {
         this.list.add(new ManagedShader.CustomUniforms.Vec2Entry(offset, x, y));
      }

      public void handleNext(ManagedShader program) {
         if (program.class_5944 != null) {
            for (ManagedShader.CustomUniforms.UniformEntry var3 : this.list) {
               var3.handleVal(program);
            }
         }
      }

      public static String computeContains(ManagedShader program, int offset) {
         if (program.class_5944 == null) {
            return null;
         } else {
            Set var2 = ((ShaderProgramAccessor)program.class_5944).getUniformsByName().keySet();
            if (offset == 0 && var2.contains("uRange")) {
               return "uRange";
            } else if (offset == 4 && var2.contains("uThickness")) {
               return "uThickness";
            } else if (offset == 8 && var2.contains("uSmoothness")) {
               return "uSmoothness";
            } else if (offset == 12 && var2.contains("uOutline")) {
               return "uOutline";
            } else if (offset == 16 && var2.contains("uOutlineThickness")) {
               return "uOutlineThickness";
            } else {
               if (offset == 32) {
                  if (var2.contains("uAlpha")) {
                     return "uAlpha";
                  }

                  if (var2.contains("uSmoothness")) {
                     return "uSmoothness";
                  }
               }

               if (offset == 36 && var2.contains("uMix")) {
                  return "uMix";
               } else {
                  return offset == 40 && var2.contains("uSmoothness") ? "uSmoothness" : null;
               }
            }
         }
      }

      public static String computeContains2(ManagedShader program, int offset) {
         if (program.class_5944 == null) {
            return null;
         } else {
            Set var2 = ((ShaderProgramAccessor)program.class_5944).getUniformsByName().keySet();
            if (offset == 0 && var2.contains("uSize")) {
               return "uSize";
            } else if (offset == 16 && var2.contains("uRadius")) {
               return "uRadius";
            } else {
               return offset == 32 && var2.contains("uOutlineColor") ? "uOutlineColor" : null;
            }
         }
      }

      private static class FloatEntry implements ManagedShader.CustomUniforms.UniformEntry {
         public final int offset;
         public final float val;

         public FloatEntry(int offset, float val) {
            this.offset = offset;
            this.val = val;
         }

         @Override
         public void handleVal(ManagedShader program) {
            String var2 = ManagedShader.CustomUniforms.computeContains(program, this.offset);
            if (var2 != null) {
               program.handleVal(var2, this.val);
            }
         }
      }

      private interface UniformEntry {
         void handleVal(ManagedShader var1);
      }

      private static class Vec2Entry implements ManagedShader.CustomUniforms.UniformEntry {
         public final int offset;
         public final float x;
         public final float y;

         public Vec2Entry(int offset, float x, float y) {
            this.offset = offset;
            this.x = x;
            this.y = y;
         }

         @Override
         public void handleVal(ManagedShader shader) {
         } // STUB (body lost by decompiler)

      }

      private static class Vec4Entry implements ManagedShader.CustomUniforms.UniformEntry {
         public final int offset;
         public final float x;
         public final float y;
         public final float z;
         public final float w;

         public Vec4Entry(int offset, float x, float y, float z, float w) {
            this.offset = offset;
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
         }

         @Override
         public void handleVal(ManagedShader shader) {
         } // STUB (body lost by decompiler)

      }
   }
}
