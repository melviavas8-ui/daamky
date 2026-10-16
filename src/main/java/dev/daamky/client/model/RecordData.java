package dev.daamky.client.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.daamky.client.internal.FromStringHelper;
import dev.daamky.client.internal.UserAgentHelper;
import java.util.Map;
import java.util.UUID;

public record RecordData() {
   public UUID uuidByName(String username) {
      try {
         UserAgentHelper.HttpResponse var2 = UserAgentHelper.computeHttpResponse(
            "https://api.minecraftservices.com/users/profiles/minecraft/" + username, Map.of()
         );
         if (var2.code() != 200) {
            throw new RuntimeException("Fail " + username);
         } else {
            RecordData.ApiProfileResponse var3 = RecordData.JsonUtils.computeJson(var2.text(), RecordData.ApiProfileResponse.class);
            return FromStringHelper.computeUUID(var3.mwbimqr);
         }
      } catch (Exception var4) {
         return null;
      }
   }

   public static class ApiProfileResponse {
      public String mwbimqr;
      public String mqgzyw;
   }

   public static class JsonUtils {
      public static final Gson GSON = new GsonBuilder().create();

      public static <T> T computeJson(String json, Class<T> classOfT) {
         return (T)GSON.fromJson(json, classOfT);
      }
   }
}
