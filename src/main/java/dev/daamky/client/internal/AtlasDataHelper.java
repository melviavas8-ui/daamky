package dev.daamky.client.internal;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public final class AtlasDataHelper {
   public AtlasDataHelper.AtlasData atlas;
   public AtlasDataHelper.MetricsData metrics;
   public List<AtlasDataHelper.GlyphData> glyphs;
   @SerializedName("kerning")
   public List<AtlasDataHelper.KerningData> kernings;

   public AtlasDataHelper.AtlasData getAtlas() {
      return this.atlas;
   }

   public AtlasDataHelper.MetricsData getMetrics() {
      return this.metrics;
   }

   public List<AtlasDataHelper.GlyphData> getGlyphs() {
      return this.glyphs;
   }

   public List<AtlasDataHelper.KerningData> getKernings() {
      return this.kernings;
   }

   public static final class AtlasData {
      @SerializedName("distanceRange")
      public float range;
      public float width;
      public float height;
      public String yOrigin;

      public float getRange() {
         return this.range;
      }

      public float getWidth() {
         return this.width;
      }

      public float getHeight() {
         return this.height;
      }

      public String getYOrigin() {
         return this.yOrigin;
      }
   }

   public static final class BoundsData {
      public float left;
      public float top;
      public float right;
      public float bottom;

      public float getLeft() {
         return this.left;
      }

      public float getTop() {
         return this.top;
      }

      public float getRight() {
         return this.right;
      }

      public float getBottom() {
         return this.bottom;
      }
   }

   public static final class GlyphData {
      public int unicode;
      public float advance;
      public AtlasDataHelper.BoundsData planeBounds;
      public AtlasDataHelper.BoundsData atlasBounds;

      public int getUnicode() {
         return this.unicode;
      }

      public float getAdvance() {
         return this.advance;
      }

      public AtlasDataHelper.BoundsData getPlaneBounds() {
         return this.planeBounds;
      }

      public AtlasDataHelper.BoundsData getAtlasBounds() {
         return this.atlasBounds;
      }
   }

   public static final class KerningData {
      @SerializedName("unicode1")
      public int leftChar;
      @SerializedName("unicode2")
      public int rightChar;
      public float advance;

      public int getLeftChar() {
         return this.leftChar;
      }

      public int getRightChar() {
         return this.rightChar;
      }

   }

   public static final class MetricsData {
      public float lineHeight;
      public float ascender;
      public float descender;

      public float getLineHeight() {
         return this.lineHeight;
      }

      public float getAscender() {
         return this.ascender;
      }

      public float getDescender() {
         return this.descender;
      }

      public float getDescender2() {
         return this.lineHeight + this.descender;
      }
   }
}
