package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.FloatBuffer;
import net.minecraft.client.MinecraftClient;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.ARBInstancedArrays;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

public class UProjHelper {
   public static final int intVal = 2000;
   public static final int int2 = 32;
   public static final int mlaomz = 128;
   public static final Vector3f vector3f = new Vector3f();
   public static final Vector3f mguye = new Vector3f();
   public static final Vector3f vector3f2 = new Vector3f();
   public static final Vector3f vector3f3 = new Vector3f();
   public int int3;
   public int mkozr;
   public int int4;
   public int int5;
   public FloatBuffer floatBuffer;
   public final float[] magof = new float[16];
   public int int6 = 0;
   public int type = 0;
   public int textureId = -1;
   public static final String string = "    #version 330 core\n    layout(location = 0) in vec2 aPos;\n    layout(location = 1) in vec2 p1; layout(location = 2) in vec2 p2;\n    layout(location = 3) in vec2 p3; layout(location = 4) in vec2 p4;\n    layout(location = 5) in vec4 iRadii;\n    layout(location = 6) in vec4 iColor1; layout(location = 7) in vec4 iColor2;\n    layout(location = 8) in vec4 iColor3; layout(location = 9) in vec4 iColor4;\n    layout(location = 10) in vec4 iUv;\n\n    uniform mat4 uProj;\n\n    out vec2 vPos; out vec2 vSize; out vec4 vRadii;\n    out vec4 vColor1; out vec4 vColor2; out vec4 vColor3; out vec4 vColor4;\n    out vec4 vUv;\n\n    void main() {\n        vec2 pos = mix(mix(p1, p4, aPos.x), mix(p2, p3, aPos.x), aPos.y);\n        gl_Position = uProj * vec4(pos, 0.0, 1.0);\n        vPos = aPos;\n        vSize = vec2(distance(p1, p4), distance(p1, p2));\n        vRadii = iRadii;\n        vColor1 = iColor1; vColor2 = iColor2; vColor3 = iColor3; vColor4 = iColor4;\n        vUv = iUv;\n    }\n";
   public static final String string2 = "    #version 330 core\n    in vec2 vPos; in vec2 vSize; in vec4 vRadii;\n    in vec4 vColor1; in vec4 vColor2; in vec4 vColor3; in vec4 vColor4;\n    in vec4 vUv;\n\n    out vec4 FragColor;\n\n    uniform sampler2D uSampler;\n    uniform int uType;\n    uniform vec2 uScreenSize;\n\n    float rdist(vec2 pos, vec2 size, vec4 radius) {\n        radius.xy = (pos.x > 0.0) ? radius.xy : radius.zw;\n        radius.x  = (pos.y > 0.0) ? radius.x  : radius.y;\n        vec2 q = abs(pos) - size + radius.x;\n        return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius.x;\n    }\n\n    void main() {\n        vec4 topColor = mix(vColor1, vColor4, vPos.x);\n        vec4 bottomColor = mix(vColor2, vColor3, vPos.x);\n        vec4 color = mix(topColor, bottomColor, vPos.y);\n\n        vec2 center = vSize * 0.5;\n        vec2 fragPos = (vPos * vSize) - center;\n        float dist = rdist(fragPos, center - 1.0, vRadii);\n        float alpha = 1.0 - smoothstep(0.0, 1.5, dist);\n\n        if(alpha < 0.005) discard;\n\n        if (uType == 1) { // Texture\n            vec2 texUv = mix(vUv.xy, vUv.zw, vPos);\n            color *= texture(uSampler, texUv);\n        } else if (uType == 2) { // Blur (Kawase)\n            vec2 screenUv = gl_FragCoord.xy / uScreenSize;\n            vec4 blurTex = texture(uSampler, screenUv);\n\n            float mixFactor = vUv.x;\n            float baseAlpha = vUv.y;\n\n            color = mix(blurTex, color, mixFactor);\n            color.a *= baseAlpha;\n        } else if (uType == 3) { // Glow Wave\n            float progress = vUv.x;\n            float glowAlpha = vUv.z;\n            float linePos = vPos.x + (vPos.y - 0.5) * 0.35;\n            float wavePos = mix(-0.35, 1.35, progress);\n            float distToWave = abs(linePos - wavePos);\n            float sheen = smoothstep(0.14, 0.0, distToWave);\n            color = vec4(1.0, 1.0, 1.0, sheen * 0.65 * glowAlpha);\n        }\n\n        FragColor = vec4(color.rgb, color.a * alpha);\n    }\n";
   public int int7 = -1;
   public int int8 = -1;
   public int int9 = -1;
   public int mfihf = -1;

   public void runGl() {
      int var1 = GL11.glGetInteger(34229);
      int var2 = GL11.glGetInteger(34964);
      this.floatValBuffer = MemoryUtil.memAllocFloat(64000);
      int var3 = this.computeGl(
         35633,
         "    #version 330 core\n    layout(location = 0) in vec2 aPos;\n    layout(location = 1) in vec2 p1; layout(location = 2) in vec2 p2;\n    layout(location = 3) in vec2 p3; layout(location = 4) in vec2 p4;\n    layout(location = 5) in vec4 iRadii;\n    layout(location = 6) in vec4 iColor1; layout(location = 7) in vec4 iColor2;\n    layout(location = 8) in vec4 iColor3; layout(location = 9) in vec4 iColor4;\n    layout(location = 10) in vec4 iUv;\n\n    uniform mat4 uProj;\n\n    out vec2 vPos; out vec2 vSize; out vec4 vRadii;\n    out vec4 vColor1; out vec4 vColor2; out vec4 vColor3; out vec4 vColor4;\n    out vec4 vUv;\n\n    void main() {\n        vec2 pos = mix(mix(p1, p4, aPos.x), mix(p2, p3, aPos.x), aPos.y);\n        gl_Position = uProj * vec4(pos, 0.0, 1.0);\n        vPos = aPos;\n        vSize = vec2(distance(p1, p4), distance(p1, p2));\n        vRadii = iRadii;\n        vColor1 = iColor1; vColor2 = iColor2; vColor3 = iColor3; vColor4 = iColor4;\n        vUv = iUv;\n    }\n"
      );
      int var4 = this.computeGl(
         35632,
         "    #version 330 core\n    in vec2 vPos; in vec2 vSize; in vec4 vRadii;\n    in vec4 vColor1; in vec4 vColor2; in vec4 vColor3; in vec4 vColor4;\n    in vec4 vUv;\n\n    out vec4 FragColor;\n\n    uniform sampler2D uSampler;\n    uniform int uType;\n    uniform vec2 uScreenSize;\n\n    float rdist(vec2 pos, vec2 size, vec4 radius) {\n        radius.xy = (pos.x > 0.0) ? radius.xy : radius.zw;\n        radius.x  = (pos.y > 0.0) ? radius.x  : radius.y;\n        vec2 q = abs(pos) - size + radius.x;\n        return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius.x;\n    }\n\n    void main() {\n        vec4 topColor = mix(vColor1, vColor4, vPos.x);\n        vec4 bottomColor = mix(vColor2, vColor3, vPos.x);\n        vec4 color = mix(topColor, bottomColor, vPos.y);\n\n        vec2 center = vSize * 0.5;\n        vec2 fragPos = (vPos * vSize) - center;\n        float dist = rdist(fragPos, center - 1.0, vRadii);\n        float alpha = 1.0 - smoothstep(0.0, 1.5, dist);\n\n        if(alpha < 0.005) discard;\n\n        if (uType == 1) { // Texture\n            vec2 texUv = mix(vUv.xy, vUv.zw, vPos);\n            color *= texture(uSampler, texUv);\n        } else if (uType == 2) { // Blur (Kawase)\n            vec2 screenUv = gl_FragCoord.xy / uScreenSize;\n            vec4 blurTex = texture(uSampler, screenUv);\n\n            float mixFactor = vUv.x;\n            float baseAlpha = vUv.y;\n\n            color = mix(blurTex, color, mixFactor);\n            color.a *= baseAlpha;\n        } else if (uType == 3) { // Glow Wave\n            float progress = vUv.x;\n            float glowAlpha = vUv.z;\n            float linePos = vPos.x + (vPos.y - 0.5) * 0.35;\n            float wavePos = mix(-0.35, 1.35, progress);\n            float distToWave = abs(linePos - wavePos);\n            float sheen = smoothstep(0.14, 0.0, distToWave);\n            color = vec4(1.0, 1.0, 1.0, sheen * 0.65 * glowAlpha);\n        }\n\n        FragColor = vec4(color.rgb, color.a * alpha);\n    }\n"
      );
      this.intVal5 = GL20.glCreateProgram();
      GL20.glAttachShader(this.intVal5, var3);
      GL20.glAttachShader(this.intVal5, var4);
      GL20.glLinkProgram(this.intVal5);
      this.intVal7 = GL20.glGetUniformLocation(this.intVal5, "uProj");
      this.intVal8 = GL20.glGetUniformLocation(this.intVal5, "uType");
      this.intVal9 = GL20.glGetUniformLocation(this.intVal5, "uSampler");
      this.mfihf = GL20.glGetUniformLocation(this.intVal5, "uScreenSize");
      this.intVal3 = GL30.glGenVertexArrays();
      GL30.glBindVertexArray(this.intVal3);
      this.mkozr = GL15.glGenBuffers();
      GL15.glBindBuffer(34962, this.mkozr);
      GL15.glBufferData(34962, new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F}, 35044);
      GL20.glEnableVertexAttribArray(0);
      GL20.glVertexAttribPointer(0, 2, 5126, false, 0, 0L);
      this.intVal4 = GL15.glGenBuffers();
      GL15.glBindBuffer(34962, this.intVal4);
      GL15.glBufferData(34962, 256000L, 35048);
      this.handleGl(1, 2, 0);
      this.handleGl(2, 2, 2);
      this.handleGl(3, 2, 4);
      this.handleGl(4, 2, 6);
      this.handleGl(5, 4, 8);
      this.handleGl(6, 4, 12);
      this.handleGl(7, 4, 16);
      this.handleGl(8, 4, 20);
      this.handleGl(9, 4, 24);
      this.handleGl(10, 4, 28);
      GL15.glBindBuffer(34962, var2);
      GL30.glBindVertexArray(var1);
   }

   public void handleGl(int index, int size, int floatOffset) {
      GL20.glEnableVertexAttribArray(index);
      GL20.glVertexAttribPointer(index, size, 5126, false, 128, (long)floatOffset * 4L);
      if (GL.getCapabilities().OpenGL33) {
         GL33.glVertexAttribDivisor(index, 1);
      } else if (GL.getCapabilities().GL_ARB_instanced_arrays) {
         ARBInstancedArrays.glVertexAttribDivisorARB(index, 1);
      }
   }

   public void handleRgb(
      Matrix4f matrix, float x, float y, float w, float h, Vector4f radii, Color c1, Color c2, Color c3, Color c4, int type, int textureId, Vector4f uvData
   ) {
      this.handlePut(matrix, x, y, w, h, radii, c1.getRGB(), c2.getRGB(), c3.getRGB(), c4.getRGB(), typex, textureIdx, uvData);
   }

   public void handlePut(
      Matrix4f matrix, float x, float y, float w, float h, Vector4f radii, int c1, int c2, int c3, int c4, int type, int textureId, Vector4f uvData
   ) {
      if (this.floatValBuffer == null) {
         this.runGl();
      }

      if (typex != this.type || (typex == 1 || typex == 2) && textureIdx != this.textureId || this.intVal6 >= 2000) {
         this.runGl2();
         this.type = typex;
         this.textureId = textureIdx;
      }

      Vector3f var14 = vector3f.set(x, y, 0.0F).mulPosition(matrix);
      Vector3f var15 = mguye.set(x, y + h, 0.0F).mulPosition(matrix);
      Vector3f var16 = vector3f2.set(x + w, y + h, 0.0F).mulPosition(matrix);
      Vector3f var17 = vector3f3.set(x + w, y, 0.0F).mulPosition(matrix);
      this.floatValBuffer.put(var14.x).put(var14.y);
      this.floatValBuffer.put(var15.x).put(var15.y);
      this.floatValBuffer.put(var16.x).put(var16.y);
      this.floatValBuffer.put(var17.x).put(var17.y);
      this.floatValBuffer.put(radii.x).put(radii.y).put(radii.z).put(radii.w);
      this.handlePut(c1);
      this.handlePut(c2);
      this.handlePut(c3);
      this.handlePut(c4);
      if (uvData != null) {
         this.floatValBuffer.put(uvData.x).put(uvData.y).put(uvData.z).put(uvData.w);
      } else {
         this.floatValBuffer.put(0.0F).put(0.0F).put(0.0F).put(0.0F);
      }

      this.intVal6++;
   }

   public void handlePut(Color c) {
      this.floatValBuffer.put((float)c.getRed() / 255.0F).put((float)c.getGreen() / 255.0F).put((float)c.getBlue() / 255.0F).put((float)c.getAlpha() / 255.0F);
   }

   public void handlePut(int c) {
      this.floatValBuffer
         .put((float)(c >> 16 & 0xFF) / 255.0F)
         .put((float)(c >> 8 & 0xFF) / 255.0F)
         .put((float)(c & 0xFF) / 255.0F)
         .put((float)(c >> 24 & 0xFF) / 255.0F);
   }

   public void runGl2() {
      if (this.intVal6 != 0) {
         this.floatValBuffer.flip();
         GL15.glBindBuffer(34962, this.intVal4);
         GL15.glBufferSubData(34962, 0L, this.floatValBuffer);
         this.floatValBuffer.clear();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         GL20.glUseProgram(this.intVal5);
         GL30.glBindVertexArray(this.intVal3);
         Matrix4f var1 = RenderSystem.getProjectionMatrix();
         var1.get(this.magof);
         GL20.glUniformMatrix4fv(this.intVal7, false, this.magof);
         GL20.glUniform1i(this.intVal8, this.type);
         if (this.type != 0) {
            if (this.textureId != -1) {
               RenderSystem.setShaderTexture(0, this.textureId);
               GL20.glUniform1i(this.intVal9, 0);
            }

            MinecraftClient var2 = MinecraftClient.getInstance();
            if (var2 != null && var2.getWindow() != null) {
               GL20.glUniform2f(this.mfihf, (float)var2.getWindow().getFramebufferWidth(), (float)var2.getWindow().getFramebufferHeight());
            }
         }

         GL31.glDrawArraysInstanced(6, 0, 4, this.intVal6);
         GL30.glBindVertexArray(0);
         GL15.glBindBuffer(34962, 0);
         GL20.glUseProgram(0);
         RenderSystem.enableCull();
         this.intVal6 = 0;
      }
   }

   public int computeGl(int type, String source) {
      int var3 = GL20.glCreateShader(typex);
      GL20.glShaderSource(var3, source);
      GL20.glCompileShader(var3);
      return var3;
   }
}
