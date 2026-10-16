package dev.daamky.client.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

public class UserAgentHelper {
   public static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
   public static final Gson GSON = new GsonBuilder().create();

   public static HttpURLConnection closeRequest(String url, String method, String data, Map<String, String> headers, String agent) throws IOException {
      HttpURLConnection var5 = (HttpURLConnection)new URL(url).openConnection();
      var5.setRequestMethod(method);
      var5.setConnectTimeout(2000);
      var5.setReadTimeout(10000);
      var5.setRequestProperty("User-Agent", agent);
      if (headers != null) {
         for (Entry var7 : headers.entrySet()) {
            var5.setRequestProperty((String)var7.getKey(), (String)var7.getValue());
         }
      }

      var5.setInstanceFollowRedirects(true);
      var5.setDoOutput(true);
      if (data != null && !data.isEmpty()) {
         try (DataOutputStream var11 = new DataOutputStream(var5.getOutputStream())) {
            var11.writeBytes(data);
         }
      }

      var5.connect();
      return var5;
   }

   public static UserAgentHelper.HttpResponse initInit(String url, String method, String data, Map<String, String> headers, String agent) throws IOException {
      HttpURLConnection var6 = closeRequest(url, method, data, headers, agent);
      int var7 = var6.getResponseCode();
      InputStream var8 = var7 >= 200 && var7 < 300 ? var6.getInputStream() : var6.getErrorStream();

      String var5;
      try (BufferedReader var9 = new BufferedReader(new InputStreamReader(var8, StandardCharsets.UTF_8))) {
         StringBuilder var11 = new StringBuilder();

         String var10;
         while ((var10 = var9.readLine()) != null) {
            var11.append(var10);
         }

         var5 = var11.toString();
      }

      return new UserAgentHelper.HttpResponse(var7, var5);
   }

   public static UserAgentHelper.HttpResponse computeHttpResponse(String url, Map<String, String> headers) throws IOException {
      return initInit(url, "GET", "", headers, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
   }

   public static UserAgentHelper.HttpResponse computePost(String url, String data, Map<String, String> headers) throws IOException {
      return initInit(
         url, "POST", data, headers, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"
      );
   }

   public static <T> T computeJson(String url, Object data, Class<T> responseType) throws IOException {
      UserAgentHelper.HttpResponse var3 = computePost(url, GSON.toJson(data), Map.of("Content-Type", "application/json"));
      return (T)GSON.fromJson(var3.text, responseType);
   }

   public static <T, E> UserAgentHelper.Pair<T, E> initJson(String url, Object data, Class<T> successType, Class<E> errorType) throws IOException {
      UserAgentHelper.HttpResponse var4 = computePost(url, GSON.toJson(data), Map.of("Content-Type", "application/json"));
      return var4.code == 200
         ? new UserAgentHelper.Pair<>((T)GSON.fromJson(var4.text, successType), null)
         : new UserAgentHelper.Pair<>(null, (E)GSON.fromJson(var4.text, errorType));
   }

   public static record HttpResponse(int code, String text) {
   }

   public static record Pair<T, E>(T first, E second) {
   }
}
