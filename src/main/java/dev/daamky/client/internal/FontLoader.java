package dev.daamky.client.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class FontLoader {
   public String name;
   public Identifier dataIdentifier;
   public Identifier atlasIdentifier;

   public FontLoader computeCase(String fontName) {
      this.name = fontName;
      this.dataIdentifier = Identifier.of("daamky".toLowerCase(), "fonts/" + fontName + ".json");
      this.atlasIdentifier = Identifier.of("daamky".toLowerCase(), "fonts/" + fontName + ".png");
      return this;
   }

   public ClientFont load() {
      AtlasDataHelper var1 = InputHelper.computeJson(this.dataIdentifier, AtlasDataHelper.class);
      if (var1 == null) {
         throw new RuntimeException(
            "Failed to read font data file: "
               + this.dataIdentifier.toString()
               + "; Are you sure this is json file? Try to check the correctness of its syntax."
         );
      } else {
         InputStream var2 = null;

         try {
            if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getResourceManager() != null) {
               var2 = MinecraftClient.getInstance().getResourceManager().open(this.atlasIdentifier);
            }
         } catch (Throwable var10) {
         }

         if (var2 == null) {
            var2 = InputHelper.computeCase(this.atlasIdentifier.getPath());
         }

         if (var2 == null) {
            throw new RuntimeException("Failed to load texture for font: " + this.name);
         } else {
            try {
               InputStream var3 = var2;

               try {
                  NativeImage var4 = NativeImage.read(var3);
                  NativeImageBackedTexture var5 = new NativeImageBackedTexture(var4);
                  MinecraftClient.getInstance().getTextureManager().registerTexture(this.atlasIdentifier, var5);
                  var5.bindTexture();
               } catch (Throwable var11) {
                  if (var2 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }
                  }

                  throw var11;
               }

               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var12) {
               throw new RuntimeException("Failed to load texture for font: " + this.name, var12);
            }

            AbstractTexture var13 = MinecraftClient.getInstance().getTextureManager().getTexture(this.atlasIdentifier);
            RenderSystem.recordRenderCall(FontLoader::lambda$load$0);
            float var14 = var1.getAtlas().getWidth();
            float var15 = var1.getAtlas().getHeight();
            String var6 = var1.getAtlas().getYOrigin();
            Map var7 = var1.getGlyphs().stream().collect(Collectors.toMap(AtlasDataHelper.GlyphData::getUnicode, FontLoader::lambda$load$1));
            HashMap var8 = new HashMap();
            if (var1.getKernings() != null) {
               var1.getKernings().forEach(FontLoader::lambda$load$2);
            }

            return new ClientFont(this.name, var13, var1.getAtlas(), var1.getMetrics(), var7, var8);
         }
      }
   }

   public static void lambda$load$2(HashMap kernings, AtlasDataHelper.KerningData kerning) {
      HashMap var2 = (HashMap)kernings.get(kerning.getLeftChar());
      if (var2 == null) {
         var2 = new HashMap();
         kernings.put(kerning.getLeftChar(), var2);
      }

      var2.put(kerning.getRightChar(), kerning.getAdvance());
   }

   public static BottomHelper lambda$load$1(float aWidth, float aHeight, String yOrigin, AtlasDataHelper.GlyphData glyphData) {
      return new BottomHelper(glyphData, aWidth, aHeight, yOrigin);
   }

   public static void lambda$load$0(AbstractTexture texture) {
      texture.setFilter(true, false);
   }
}
