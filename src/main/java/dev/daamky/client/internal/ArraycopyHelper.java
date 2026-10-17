package dev.daamky.client.internal;

public class ArraycopyHelper {
   public static final int ACCENT = 0;
   public static final int BACKGROUND = 1;
   public static final int OUTLINE = 2;
   public static final int TEXT = 3;
   public static final int TEXT2 = 4;
   public static final int ELEMENT = 5;
   public static final int SEPARATOR = 6;
   public static final int LIGHT_BG = 7;
   public static final int COLOR_COUNT = 8;
   public static final String[] NAMES = new String[]{
      "Цвет визуалов", "Цвет фона", "Обводка", "Светлый текст", "Тёмный текст", "Цвет худа", "Цвет разделителей", "Цвет светлого фона"
   };
   public static final int OPACITY = 0;
   public static final int BLUR = 1;
   public static final String[] SLIDER_NAMES = new String[]{"Прозрачность", "Сила размытия"};
   public static final float[] SLIDER_MIN = new float[]{0.1F, 0.0F};
   public static final float[] SLIDER_MAX = new float[]{1.0F, 6.0F};
   public static final float[] SLIDER_DEF = new float[]{1.0F, 0.0F};
   public static final float[] SLIDER_STEP = new float[]{0.0F, 1.0F};
   public static final int SHADOW = 0;
   public static final int DISTORTION = 1;
   public static final String[] BOOL_NAMES = new String[]{"Эффект тени", "Эффект искажения"};
   public static final boolean[] BOOL_DEF = new boolean[]{true, false};
   public String name;
   public final int[] colors = new int[8];
   public final float[] sliders = new float[2];
   public final boolean[] booleans = new boolean[BOOL_NAMES.length];
   public boolean builtin = false;

   public static int computeInt(int r, int g, int b) {
      return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
   }

   public ArraycopyHelper(String name) {
      this.name = name;
      this.colors[0] = -1;
      this.colors[1] = -15856110;
      this.colors[2] = -1;
      this.colors[3] = -1;
      this.colors[4] = -1;
      this.colors[5] = -1;
      this.colors[6] = -1;
      this.colors[7] = -15066592;
      System.arraycopy(SLIDER_DEF, 0, this.sliders, 0, this.sliders.length);
      System.arraycopy(BOOL_DEF, 0, this.booleans, 0, this.booleans.length);
   }

   public int computeColors(int index) {
      return this.colors[index];
   }

   public void handleColors(int index, int argb) {
      this.colors[index] = argb;
   }

   public float computeSliders(int index) {
      return computeSlider(index, this.sliders[index]);
   }

   public void handleSliders(int index, float value) {
      this.sliders[index] = computeSlider(index, value);
   }

   public static float computeSlider(int index, float value) {
      float var2 = SLIDER_STEP[index];
      if (var2 > 0.0F) {
         value = (float)Math.round(value / var2) * var2;
      }

      if (value < SLIDER_MIN[index]) {
         return SLIDER_MIN[index];
      } else {
         return value > SLIDER_MAX[index] ? SLIDER_MAX[index] : value;
      }
   }

   public boolean computeBooleans(int index) {
      return this.booleans[index];
   }

   public void handleBooleans(int index, boolean value) {
      this.booleans[index] = value;
   }

   public ArraycopyHelper initArraycopy(String newName) {
      ArraycopyHelper var2 = new ArraycopyHelper(newName);
      System.arraycopy(this.colors, 0, var2.colors, 0, 8);
      System.arraycopy(this.sliders, 0, var2.sliders, 0, this.sliders.length);
      System.arraycopy(this.booleans, 0, var2.booleans, 0, this.booleans.length);
      return var2;
   }
}
