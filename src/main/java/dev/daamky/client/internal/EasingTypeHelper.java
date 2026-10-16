package dev.daamky.client.internal;

public final class EasingTypeHelper {
   public static final float floatVal = (float) Math.PI;
   public static final float float2 = 1.70158F;
   public static final float float3 = 2.5949094F;
   public static final float float4 = 2.70158F;
   public static final float float5 = (float) (Math.PI * 2.0 / 3.0);
   public static final float float6 = (float) Math.PI * 4.0F / 9.0F;

   public static float computeFloat(EasingTypeHelper.EasingType type, float x) {
      float var2 = Math.max(0.0F, Math.min(1.0F, x));
      switch (type) {
         case EASE_IN_SINE:
            return 1.0F - (float)Math.cos((double)(var2 * (float) Math.PI / 2.0F));
         case EASE_OUT_SINE:
            return(floatVal)Math.sin((double)(var2 * (float) Math.PI / 2.0F));
         case EASE_IN_OUT_SINE:
            return -((float)Math.cos((double)((float) Math.PI * var2)) - 1.0F) / 2.0F;
         case EASE_IN_QUAD:
            return var2 * var2;
         case EASE_OUT_QUAD:
            return 1.0F - (1.0F - var2) * (1.0F - var2);
         case EASE_IN_OUT_QUAD:
            return var2 < 0.5F ? 2.0F * var2 * var2 : 1.0F - (float)Math.pow((double)(-2.0F * var2 + 2.0F), 2.0) / 2.0F;
         case EASE_IN_CUBIC:
            return var2 * var2 * var2;
         case EASE_OUT_CUBIC:
            return 1.0F - (float)Math.pow((double)(1.0F - var2), 3.0);
         case EASE_IN_OUT_CUBIC:
            return var2 < 0.5F ? 4.0F * var2 * var2 * var2 : 1.0F - (float)Math.pow((double)(-2.0F * var2 + 2.0F), 3.0) / 2.0F;
         case EASE_IN_QUART:
            return var2 * var2 * var2 * var2;
         case EASE_OUT_QUART:
            return 1.0F - (float)Math.pow((double)(1.0F - var2), 4.0);
         case EASE_IN_OUT_QUART:
            return var2 < 0.5F ? 8.0F * var2 * var2 * var2 * var2 : 1.0F - (float)Math.pow((double)(-2.0F * var2 + 2.0F), 4.0) / 2.0F;
         case EASE_IN_QUINT:
            return var2 * var2 * var2 * var2 * var2;
         case EASE_OUT_QUINT:
            return 1.0F - (float)Math.pow((double)(1.0F - var2), 5.0);
         case EASE_IN_OUT_QUINT:
            return var2 < 0.5F ? 16.0F * var2 * var2 * var2 * var2 * var2 : 1.0F - (float)Math.pow((double)(-2.0F * var2 + 2.0F), 5.0) / 2.0F;
         case EASE_IN_EXPO:
            return var2 == 0.0F ? 0.0F : (float)Math.pow(2.0, 10.0 * (double)var2 - 10.0);
         case EASE_OUT_EXPO:
            return var2 == 1.0F ? 1.0F : 1.0F - (float)Math.pow(2.0, -10.0 * (double)var2);
         case EASE_IN_OUT_EXPO:
            if (var2 == 0.0F) {
               return 0.0F;
            } else {
               if (var2 == 1.0F) {
                  return 1.0F;
               }

               return var2 < 0.5F ? (float)Math.pow(2.0, 20.0 * (double)var2 - 10.0) / 2.0F : (2.0F - (float)Math.pow(2.0, -20.0 * (double)var2 + 10.0)) / 2.0F;
            }
         case EASE_IN_CIRC:
            return 1.0F - (float)Math.sqrt(1.0 - Math.pow((double)var2, 2.0));
         case EASE_OUT_CIRC:
            return(floatVal)Math.sqrt(1.0 - Math.pow((double)var2 - 1.0, 2.0));
         case EASE_IN_OUT_CIRC:
            return var2 < 0.5F
               ? (1.0F - (float)Math.sqrt(1.0 - Math.pow(2.0 * (double)var2, 2.0))) / 2.0F
               : ((float)Math.sqrt(1.0 - Math.pow(-2.0 * (double)var2 + 2.0, 2.0)) + 1.0F) / 2.0F;
         case EASE_IN_BACK:
            return 2.70158F * var2 * var2 * var2 - 1.70158F * var2 * var2;
         case EASE_OUT_BACK:
            return 1.0F + 2.70158F * (float)Math.pow((double)var2 - 1.0, 3.0) + 1.70158F * (float)Math.pow((double)var2 - 1.0, 2.0);
         case EASE_IN_OUT_BACK:
            return var2 < 0.5F
               ? (float)Math.pow(2.0 * (double)var2, 2.0) * (7.189819F * var2 - 2.5949094F) / 2.0F
               : ((float)Math.pow(2.0 * (double)var2 - 2.0, 2.0) * (3.5949094F * (var2 * 2.0F - 2.0F) + 2.5949094F) + 2.0F) / 2.0F;
         case EASE_IN_ELASTIC:
            if (var2 == 0.0F) {
               return 0.0F;
            } else {
               if (var2 == 1.0F) {
                  return 1.0F;
               }

               return -((float)Math.pow(2.0, 10.0 * (double)var2 - 10.0)) * (float)Math.sin((double)((var2 * 10.0F - 10.75F) * (float) (Math.PI * 2.0 / 3.0)));
            }
         case EASE_OUT_ELASTIC:
            if (var2 == 0.0F) {
               return 0.0F;
            } else {
               if (var2 == 1.0F) {
                  return 1.0F;
               }

               return(floatVal)Math.pow(2.0, -10.0 * (double)var2) * (float)Math.sin((double)((var2 * 10.0F - 0.75F) * (float) (Math.PI * 2.0 / 3.0))) + 1.0F;
            }
         case EASE_IN_OUT_ELASTIC:
            if (var2 == 0.0F) {
               return 0.0F;
            } else {
               if (var2 == 1.0F) {
                  return 1.0F;
               }

               return var2 < 0.5F
                  ? -((float)Math.pow(2.0, 20.0 * (double)var2 - 10.0) * (float)Math.sin((double)((20.0F * var2 - 11.125F) * ((float) Math.PI * 4.0F / 9.0F))))
                     / 2.0F
                  : (float)Math.pow(2.0, -20.0 * (double)var2 + 10.0)
                        * (float)Math.sin((double)((20.0F * var2 - 11.125F) * ((float) Math.PI * 4.0F / 9.0F)))
                        / 2.0F
                     + 1.0F;
            }
         case EASE_IN_BOUNCE:
            return 1.0F - computeFloat(1.0F - var2);
         case EASE_OUT_BOUNCE:
            return computeFloat(var2);
         case EASE_IN_OUT_BOUNCE:
            return var2 < 0.5F ? (1.0F - computeFloat(1.0F - 2.0F * var2)) / 2.0F : (1.0F + computeFloat(2.0F * var2 - 1.0F)) / 2.0F;
         case PERLIN_NOISE:
            return(floatVal)(0.5 + 0.5 * Math.sin((double)(var2 * 12.566F) + Math.sin((double)(var2 * 25.132F)) * 0.5));
         case BROWNIAN_JITTER:
            return(floatVal)((double)var2 + 0.08 * (Math.sin((double)(var2 * 31.415F)) + Math.cos((double)(var2 * 62.83F)) * 0.5));
         default:
            return var2;
      }
   }

   public static float computeFloat(float x) {
      float var1 = 7.5625F;
      float var2 = 2.75F;
      if (x < 1.0F / var2) {
         return var1 * x * x;
      } else if (x < 2.0F / var2) {
         float var5;
         return var1 * (var5 = x - 1.5F / var2) * var5 + 0.75F;
      } else {
         float var3;
         float var4;
         return x < 2.5F / var2 ? var1 * (var3 = x - 2.25F / var2) * var3 + 0.9375F : var1 * (var4 = x - 2.625F / var2) * var4 + 0.984375F;
      }
   }

   public static enum EasingType {
      LINEAR("linear", "Linear"),
      EASE_IN_SINE("easeInSine", "Sine In"),
      EASE_OUT_SINE("easeOutSine", "Sine Out"),
      EASE_IN_OUT_SINE("easeInOutSine", "Sine In-Out"),
      EASE_IN_QUAD("easeInQuad", "Quad In"),
      EASE_OUT_QUAD("easeOutQuad", "Quad Out"),
      EASE_IN_OUT_QUAD("easeInOutQuad", "Quad In-Out"),
      EASE_IN_CUBIC("easeInCubic", "Cubic In"),
      EASE_OUT_CUBIC("easeOutCubic", "Cubic Out"),
      EASE_IN_OUT_CUBIC("easeInOutCubic", "Cubic In-Out"),
      EASE_IN_QUART("easeInQuart", "Quart In"),
      EASE_OUT_QUART("easeOutQuart", "Quart Out"),
      EASE_IN_OUT_QUART("easeInOutQuart", "Quart In-Out"),
      EASE_IN_QUINT("easeInQuint", "Quint In"),
      EASE_OUT_QUINT("easeOutQuint", "Quint Out"),
      EASE_IN_OUT_QUINT("easeInOutQuint", "Quint In-Out"),
      EASE_IN_EXPO("easeInExpo", "Expo In"),
      EASE_OUT_EXPO("easeOutExpo", "Expo Out"),
      EASE_IN_OUT_EXPO("easeInOutExpo", "Expo In-Out"),
      EASE_IN_CIRC("easeInCirc", "Circ In"),
      EASE_OUT_CIRC("easeOutCirc", "Circ Out"),
      EASE_IN_OUT_CIRC("easeInOutCirc", "Circ In-Out"),
      EASE_IN_BACK("easeInBack", "Back In"),
      EASE_OUT_BACK("easeOutBack", "Back Out"),
      EASE_IN_OUT_BACK("easeInOutBack", "Back In-Out"),
      EASE_IN_ELASTIC("easeInElastic", "Elastic In"),
      EASE_OUT_ELASTIC("easeOutElastic", "Elastic Out"),
      EASE_IN_OUT_ELASTIC("easeInOutElastic", "Elastic In-Out"),
      EASE_IN_BOUNCE("easeInBounce", "Bounce In"),
      EASE_OUT_BOUNCE("easeOutBounce", "Bounce Out"),
      EASE_IN_OUT_BOUNCE("easeInOutBounce", "Bounce In-Out"),
      PERLIN_NOISE("perlinNoise", "Perlin Noise"),
      BROWNIAN_JITTER("brownianJitter", "Brownian Jitter");

      public final String codeName;
      public final String displayName;

      private EasingType(String codeName, String displayName) {
         this.codeName = codeNamex;
         this.displayName = displayNamex;
      }

      public String getCodeName() {
         return this.codeName;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public float evaluate(float x) {
         return EasingTypeHelper.computeFloat(this, x);
      }

      public static EasingTypeHelper.EasingType[] $values() {
         return new EasingTypeHelper.EasingType[]{
            LINEAR,
            EASE_IN_SINE,
            EASE_OUT_SINE,
            EASE_IN_OUT_SINE,
            EASE_IN_QUAD,
            EASE_OUT_QUAD,
            EASE_IN_OUT_QUAD,
            EASE_IN_CUBIC,
            EASE_OUT_CUBIC,
            EASE_IN_OUT_CUBIC,
            EASE_IN_QUART,
            EASE_OUT_QUART,
            EASE_IN_OUT_QUART,
            EASE_IN_QUINT,
            EASE_OUT_QUINT,
            EASE_IN_OUT_QUINT,
            EASE_IN_EXPO,
            EASE_OUT_EXPO,
            EASE_IN_OUT_EXPO,
            EASE_IN_CIRC,
            EASE_OUT_CIRC,
            EASE_IN_OUT_CIRC,
            EASE_IN_BACK,
            EASE_OUT_BACK,
            EASE_IN_OUT_BACK,
            EASE_IN_ELASTIC,
            EASE_OUT_ELASTIC,
            EASE_IN_OUT_ELASTIC,
            EASE_IN_BOUNCE,
            EASE_OUT_BOUNCE,
            EASE_IN_OUT_BOUNCE,
            PERLIN_NOISE,
            BROWNIAN_JITTER
         };
      }
   }
}
