package dev.daamky.client.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.FloatBuffer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class ShaderSource {
   public static int intVal = -1;
   public static int int2 = -1;
   public static int int3 = -1;
   public static int int4 = -1;
   public static int int5 = -1;
   public static int int6 = -1;
   public static int int7 = -1;
   public static int int8 = -1;
   public static int mdoib = -1;
   public static boolean flag = false;
   public static boolean flag2 = false;
   public static final long longVal = System.currentTimeMillis();
   public static float floatVal = 0.5F;
   public static float float2 = 0.5F;
   public static float float3 = 0.5F;
   public static float float4 = 0.5F;
   public static float float5 = 0.0F;
   public static float float6 = 0.0F;
   public static float float7 = 0.0F;
   public static boolean flag3 = false;
   public static long long2 = System.currentTimeMillis();
   public static final String string = "#version 150\nin vec2 aPos;\nout vec2 vUv;\nvoid main() {\n    vUv = (aPos + 1.0) * 0.5;\n    gl_Position = vec4(aPos, 0.0, 1.0);\n}\n";
   public static final String string2 = "#version 150\nin vec2 vUv;\nout vec4 FragColor;\nuniform vec2 uResolution;\nuniform float uTime;\nuniform float uAlpha;\nuniform vec2 uMouse;\nuniform vec2 uMouseVel;\nuniform float uMouseSpeed;\n\n// Seamless smooth hash & noise for volumetric drifting clouds\nvec2 hash2(vec2 p) {\n    p = vec2(dot(p, vec2(127.1, 311.7)), dot(p, vec2(269.5, 183.3)));\n    return -1.0 + 2.0 * fract(sin(p) * 43758.5453123);\n}\n\nfloat gnoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    vec2 u = f * f * (3.0 - 2.0 * f);\n    return mix(mix(dot(hash2(i + vec2(0.0,0.0)), f - vec2(0.0,0.0)),\n                   dot(hash2(i + vec2(1.0,0.0)), f - vec2(1.0,0.0)), u.x),\n               mix(dot(hash2(i + vec2(0.0,1.0)), f - vec2(0.0,1.0)),\n                   dot(hash2(i + vec2(1.0,1.0)), f - vec2(1.0,1.0)), u.x), u.y);\n}\n\n// Multi-octave continuous cloud density\nfloat cloudFbm(vec2 p) {\n    float val = 0.0;\n    float amp = 0.52;\n    for (int i = 0; i < 5; i++) {\n        val += amp * (gnoise(p) * 0.5 + 0.5);\n        p = p * 2.03 + vec2(1.6, 2.3);\n        amp *= 0.49;\n    }\n    return val;\n}\n\nvoid main() {\n    vec2 res = (uResolution.x > 1.0 && uResolution.y > 1.0) ? uResolution : vec2(1920.0, 1080.0);\n    vec2 uv = gl_FragCoord.xy / res.xy;\n    \n    // Aspect-ratio correction for natural proportions\n    float aspect = res.x / res.y;\n    vec2 p = vec2(uv.x * aspect, uv.y);\n    \n    // Ultra-gentle slow breathing for the sky\n    float canvasZoom = 1.0 + sin(uTime * 0.10) * 0.012;\n    p = (p - vec2(aspect * 0.5, 0.5)) * canvasZoom + vec2(aspect * 0.5, 0.5);\n    \n    // Very subtle, gentle parallax drift towards cursor\n    vec2 mouseOffset = (uMouse - vec2(0.5));\n    vec2 inertia = uMouseVel * 0.004;\n    \n    float t = uTime * 0.04;\n    vec2 drift1 = vec2(t * 0.40, t * 0.10);\n    vec2 drift2 = vec2(t * 0.60 + 3.1, -t * 0.12 + 6.7);\n    vec2 drift3 = vec2(t * 0.80 + 8.4, t * 0.20 + 1.2);\n    \n    // Barely noticeable, calm multi-layered depth\n    vec2 p1 = p + mouseOffset * 0.012 + inertia * 0.3;\n    vec2 p2 = p + mouseOffset * 0.022 + inertia * 0.4;\n    vec2 p3 = p + mouseOffset * 0.034 + inertia * 0.5;\n    \n    float c1 = cloudFbm(p1 * 1.3 - drift1);\n    float c2 = cloudFbm(p2 * 2.4 - drift2);\n    float c3 = cloudFbm(p3 * 4.2 - drift3);\n    \n    // Cloud density blending\n    float density = c1 * 0.55 + c2 * 0.30 + c3 * 0.15;\n    \n    // Smooth cloud puff thresholds\n    float clouds = smoothstep(0.38, 0.76, density);\n    float cloudHighlight = pow(smoothstep(0.48, 0.82, density), 1.8) * 0.50;\n    \n    // Deep atmospheric sky + glowing soft clouds\n    vec3 skyColor = vec3(0.05, 0.07, 0.12);\n    vec3 cloudBase = vec3(0.55, 0.65, 0.80);\n    vec3 cloudLit  = vec3(0.92, 0.95, 1.00);\n    \n    vec3 cloudResult = mix(cloudBase, cloudLit, cloudHighlight);\n    vec3 finalColor = mix(skyColor, cloudResult, clouds * 0.75);\n    \n    // Soft subtle vignette\n    vec2 vP = uv - 0.5;\n    float vignette = clamp(1.0 - dot(vP, vP) * 0.85, 0.0, 1.0);\n    finalColor *= (0.50 + 0.50 * vignette);\n    \n    FragColor = vec4(finalColor, uAlpha);\n}\n";

   public static void runGl() {
      if (!flag && !flag2) {
         try {
            int var0 = GL20.glCreateShader(35633);
            GL20.glShaderSource(
               var0, "#version 150\nin vec2 aPos;\nout vec2 vUv;\nvoid main() {\n    vUv = (aPos + 1.0) * 0.5;\n    gl_Position = vec4(aPos, 0.0, 1.0);\n}\n"
            );
            GL20.glCompileShader(var0);
            if (GL20.glGetShaderi(var0, 35713) == 0) {
               System.err.println("[MainMenuShader] Vertex shader compilation failed: " + GL20.glGetShaderInfoLog(var0));
               flag2 = true;
               return;
            }

            int var1 = GL20.glCreateShader(35632);
            GL20.glShaderSource(
               var1,
               "#version 150\nin vec2 vUv;\nout vec4 FragColor;\nuniform vec2 uResolution;\nuniform float uTime;\nuniform float uAlpha;\nuniform vec2 uMouse;\nuniform vec2 uMouseVel;\nuniform float uMouseSpeed;\n\n// Seamless smooth hash & noise for volumetric drifting clouds\nvec2 hash2(vec2 p) {\n    p = vec2(dot(p, vec2(127.1, 311.7)), dot(p, vec2(269.5, 183.3)));\n    return -1.0 + 2.0 * fract(sin(p) * 43758.5453123);\n}\n\nfloat gnoise(vec2 p) {\n    vec2 i = floor(p);\n    vec2 f = fract(p);\n    vec2 u = f * f * (3.0 - 2.0 * f);\n    return mix(mix(dot(hash2(i + vec2(0.0,0.0)), f - vec2(0.0,0.0)),\n                   dot(hash2(i + vec2(1.0,0.0)), f - vec2(1.0,0.0)), u.x),\n               mix(dot(hash2(i + vec2(0.0,1.0)), f - vec2(0.0,1.0)),\n                   dot(hash2(i + vec2(1.0,1.0)), f - vec2(1.0,1.0)), u.x), u.y);\n}\n\n// Multi-octave continuous cloud density\nfloat cloudFbm(vec2 p) {\n    float val = 0.0;\n    float amp = 0.52;\n    for (int i = 0; i < 5; i++) {\n        val += amp * (gnoise(p) * 0.5 + 0.5);\n        p = p * 2.03 + vec2(1.6, 2.3);\n        amp *= 0.49;\n    }\n    return val;\n}\n\nvoid main() {\n    vec2 res = (uResolution.x > 1.0 && uResolution.y > 1.0) ? uResolution : vec2(1920.0, 1080.0);\n    vec2 uv = gl_FragCoord.xy / res.xy;\n    \n    // Aspect-ratio correction for natural proportions\n    float aspect = res.x / res.y;\n    vec2 p = vec2(uv.x * aspect, uv.y);\n    \n    // Ultra-gentle slow breathing for the sky\n    float canvasZoom = 1.0 + sin(uTime * 0.10) * 0.012;\n    p = (p - vec2(aspect * 0.5, 0.5)) * canvasZoom + vec2(aspect * 0.5, 0.5);\n    \n    // Very subtle, gentle parallax drift towards cursor\n    vec2 mouseOffset = (uMouse - vec2(0.5));\n    vec2 inertia = uMouseVel * 0.004;\n    \n    float t = uTime * 0.04;\n    vec2 drift1 = vec2(t * 0.40, t * 0.10);\n    vec2 drift2 = vec2(t * 0.60 + 3.1, -t * 0.12 + 6.7);\n    vec2 drift3 = vec2(t * 0.80 + 8.4, t * 0.20 + 1.2);\n    \n    // Barely noticeable, calm multi-layered depth\n    vec2 p1 = p + mouseOffset * 0.012 + inertia * 0.3;\n    vec2 p2 = p + mouseOffset * 0.022 + inertia * 0.4;\n    vec2 p3 = p + mouseOffset * 0.034 + inertia * 0.5;\n    \n    float c1 = cloudFbm(p1 * 1.3 - drift1);\n    float c2 = cloudFbm(p2 * 2.4 - drift2);\n    float c3 = cloudFbm(p3 * 4.2 - drift3);\n    \n    // Cloud density blending\n    float density = c1 * 0.55 + c2 * 0.30 + c3 * 0.15;\n    \n    // Smooth cloud puff thresholds\n    float clouds = smoothstep(0.38, 0.76, density);\n    float cloudHighlight = pow(smoothstep(0.48, 0.82, density), 1.8) * 0.50;\n    \n    // Deep atmospheric sky + glowing soft clouds\n    vec3 skyColor = vec3(0.05, 0.07, 0.12);\n    vec3 cloudBase = vec3(0.55, 0.65, 0.80);\n    vec3 cloudLit  = vec3(0.92, 0.95, 1.00);\n    \n    vec3 cloudResult = mix(cloudBase, cloudLit, cloudHighlight);\n    vec3 finalColor = mix(skyColor, cloudResult, clouds * 0.75);\n    \n    // Soft subtle vignette\n    vec2 vP = uv - 0.5;\n    float vignette = clamp(1.0 - dot(vP, vP) * 0.85, 0.0, 1.0);\n    finalColor *= (0.50 + 0.50 * vignette);\n    \n    FragColor = vec4(finalColor, uAlpha);\n}\n"
            );
            GL20.glCompileShader(var1);
            if (GL20.glGetShaderi(var1, 35713) == 0) {
               System.err.println("[MainMenuShader] Fragment shader compilation failed: " + GL20.glGetShaderInfoLog(var1));
               flag2 = true;
               return;
            }

            intVal = GL20.glCreateProgram();
            GL20.glAttachShader(intVal, var0);
            GL20.glAttachShader(intVal, var1);
            GL20.glBindAttribLocation(intVal, 0, "aPos");
            GL20.glLinkProgram(intVal);
            if (GL20.glGetProgrami(intVal, 35714) == 0) {
               System.err.println("[MainMenuShader] Program link failed: " + GL20.glGetProgramInfoLog(intVal));
               flag2 = true;
               return;
            }

            GL20.glDeleteShader(var0);
            GL20.glDeleteShader(var1);
            int4 = GL20.glGetUniformLocation(intVal, "uResolution");
            int5 = GL20.glGetUniformLocation(intVal, "uTime");
            int6 = GL20.glGetUniformLocation(intVal, "uAlpha");
            int7 = GL20.glGetUniformLocation(intVal, "uMouse");
            int8 = GL20.glGetUniformLocation(intVal, "uMouseVel");
            mdoib = GL20.glGetUniformLocation(intVal, "uMouseSpeed");
            float[] var2 = new float[]{-1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F};
            int2 = GL30.glGenVertexArrays();
            int3 = GL15.glGenBuffers();
            GL30.glBindVertexArray(int2);
            GL15.glBindBuffer(34962, int3);
            FloatBuffer var3 = MemoryUtil.memAllocFloat(var2.length);
            var3.put(var2).flip();
            GL15.glBufferData(34962, var3, 35044);
            MemoryUtil.memFree(var3);
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(0, 2, 5126, false, 8, 0L);
            GL15.glBindBuffer(34962, 0);
            GL30.glBindVertexArray(0);
            flag = true;
         } catch (Throwable var4) {
            var4.printStackTrace();
            flag2 = true;
         }
      }
   }

   public static void handleFloat(float alpha) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && var1.mouse != null && var1.getWindow() != null && var1.getWindow().getScaleFactor() > 0.0) {
         double var2 = var1.getWindow().getScaleFactor();
         float var4 = (float)(var1.mouse.getX() / var2);
         float var5 = (float)(var1.mouse.getY() / var2);
         handleGl(alpha, var4, var5);
      } else {
         handleGl(alpha, -1000.0F, -1000.0F);
      }
   }

   public static void handleGl(float alpha, float mouseX, float mouseY) {
      if (!flag2) {
         if (!flag) {
            runGl();
            if (flag2) {
               return;
            }
         }

         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var3.getWindow() != null) {
            float var4 = (float)var3.getWindow().getFramebufferWidth();
            float var5 = (float)var3.getWindow().getFramebufferHeight();
            float var6 = (float)(System.currentTimeMillis() - longVal) / 1000.0F;
            long var7 = System.currentTimeMillis();
            float var9 = Math.min(0.1F, Math.max(0.001F, (float)(var7 - long2) / 1000.0F));
            long2 = var7;
            if (var3.getWindow().getScaledWidth() > 0 && var3.getWindow().getScaledHeight() > 0 && mouseX >= 0.0F && mouseY >= 0.0F) {
               float var10 = mouseX / (float)var3.getWindow().getScaledWidth();
               float var11 = 1.0F - mouseY / (float)var3.getWindow().getScaledHeight();
               floatVal = Math.max(0.0F, Math.min(1.0F, var10));
               float2 = Math.max(0.0F, Math.min(1.0F, var11));
               if (!flag3) {
                  float3 = floatVal;
                  float4 = float2;
                  flag3 = true;
               }
            }

            float var16 = float3;
            float var17 = float4;
            float var12 = Math.min(1.0F, var9 * 14.0F);
            float3 = float3 + (floatVal - float3) * var12;
            float4 = float4 + (float2 - float4) * var12;
            float var13 = (float3 - var16) / var9;
            float var14 = (float4 - var17) / var9;
            float5 = float5 + (var13 - float5) * Math.min(1.0F, var9 * 10.0F);
            float6 = float6 + (var14 - float6) * Math.min(1.0F, var9 * 10.0F);
            float var15 = (float)Math.sqrt((double)(float5 * float5 + float6 * float6));
            float7 = float7 + (var15 - float7) * Math.min(1.0F, var9 * 6.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            GL20.glUseProgram(intVal);
            if (int4 != -1) {
               GL20.glUniform2f(int4, var4, var5);
            }

            if (int5 != -1) {
               GL20.glUniform1f(int5, var6);
            }

            if (int6 != -1) {
               GL20.glUniform1f(int6, alpha);
            }

            if (int7 != -1) {
               GL20.glUniform2f(int7, float3, float4);
            }

            if (int8 != -1) {
               GL20.glUniform2f(int8, float5, float6);
            }

            if (mdoib != -1) {
               GL20.glUniform1f(mdoib, float7);
            }

            GL30.glBindVertexArray(int2);
            GL11.glDrawArrays(4, 0, 6);
            GL30.glBindVertexArray(0);
            GL20.glUseProgram(0);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
         }
      }
   }
}
