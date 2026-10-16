package dev.daamky.client.internal;

import java.util.UUID;

public class FromStringHelper {
   public static UUID computeUUID(String string) {
      try {
         return UUID.fromString(string);
      } catch (IllegalArgumentException var2) {
         return initInit(string);
      }
   }

   public static UUID initInit(String input) {
      if (input.length() != 32) {
         throw new IllegalArgumentException("ХУЙ");
      } else {
         long var1 = Long.parseUnsignedLong(input.substring(0, 16), 16);
         long var3 = Long.parseUnsignedLong(input.substring(16, 32), 16);
         return new UUID(var1, var3);
      }
   }

   public static UUID computeName(String name) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
   }
}
