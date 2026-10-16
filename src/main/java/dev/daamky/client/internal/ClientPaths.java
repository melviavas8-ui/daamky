package dev.daamky.client.internal;

import java.io.File;

public final class ClientPaths {
   public static final String string = "daamky";
   public static final String string2 = "alpha-1";
   public static final String string3 = new File(System.getProperty("user.dir")).getAbsolutePath();
   public static final String string4 = new File(System.getProperty("user.dir"), "daamky/ai_models").getAbsolutePath();
   public static final String string5 = new File(System.getProperty("user.dir"), "daamky/runtime").getAbsolutePath();
   public static final String string6 = new File(System.getProperty("user.dir"), "daamky/themes").getAbsolutePath();
   public static final String string7 = new File(System.getProperty("user.dir"), "daamky/main").getAbsolutePath();

   public ClientPaths() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
