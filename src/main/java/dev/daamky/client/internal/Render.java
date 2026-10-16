package dev.daamky.client.internal;

import dev.daamky.client.render.shader.ShaderProgram;

public final class Render {
   public static UProjHelper uProjHelper = new UProjHelper();
   public static boolean flag = false;
   public static RectRenderer rectRenderer = new RectRenderer();
   public static ShaderProgram shaderProgram = new ShaderProgram();
   public static USizeHelper uSizeHelper = new USizeHelper();
   public static AvatarPngHelper myazem = new AvatarPngHelper();
   public static ScaleHelper mbacti = new ScaleHelper();
   public static RenderEffect renderEffect = new RenderEffect();
   public static BoxRenderer boxRenderer = new BoxRenderer();
   public static RingRenderer ringRenderer = new RingRenderer();

   public static void run() {
   }

   public Render() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
