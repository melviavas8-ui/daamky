package dev.daamky.client.internal;

import java.util.UUID;

public class EqualsHelper {
   public final String username;
   public final UUID uuid;
   public final String token;
   public final String type;

   public String getUsername() {
      return this.username;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getToken() {
      return this.token;
   }

   public String getType() {
      return this.type;
   }

   public boolean computeEquals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EqualsHelper var2)) {
         return false;
      } else if (!var2.computeFlag(this)) {
         return false;
      } else {
         String var3 = this.getUsername();
         String var4 = var2.getUsername();
         if (var3 == null ? var4 == null : var3.equals(var4)) {
            UUID var5 = this.getUuid();
            UUID var6 = var2.getUuid();
            if (var5 == null ? var6 == null : var5.equals(var6)) {
               String var7 = this.getToken();
               String var8 = var2.getToken();
               if (var7 == null ? var8 == null : var7.equals(var8)) {
                  String var9 = this.getType();
                  String var10 = var2.getType();
                  return var9 != null ? var9.equals(var10) : var10 == null;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean computeFlag(Object other) {
      return other instanceof EqualsHelper;
   }

   public int getCode() {
      byte var1 = 59;
      int var2 = 1;
      String var3 = this.getUsername();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      UUID var4 = this.getUuid();
      var2 = var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getToken();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getType();
      return var2 * 59 + (var6 == null ? 43 : var6.hashCode());
   }

   public String getToken2() {
      return "Session(username=" + this.getUsername() + ", uuid=" + this.getUuid() + ", token=" + this.getToken() + ", type=" + this.getType() + ")";
   }

   public EqualsHelper(String username, UUID uuid, String token, String type) {
      this.username = username;
      this.uuid = uuid;
      this.token = token;
      this.type = type;
   }
}
