package dev.daamky.client.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

public class PlayerArgument implements ArgumentType<String> {
   public static PlayerArgument create() {
      return new PlayerArgument();
   }

   public String parse(StringReader reader) {
      try {
         return reader.readString();
      } catch (CommandSyntaxException var3) {
         throw new RuntimeException(var3);
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      ArrayList var4 = new ArrayList();
      if (var3.getNetworkHandler() != null) {
         var3.getNetworkHandler().getPlayerList().forEach(PlayerArgument::lambda$listSuggestions$0);
      }

      return CommandSource.suggestMatching(var4, builder);
   }

   public Collection<String> getExamples() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      ArrayList var2 = new ArrayList();
      if (var1.getNetworkHandler() != null) {
         var1.getNetworkHandler().getPlayerList().stream().limit(5L).forEach(PlayerArgument::lambda$getExamples$1);
      }

      if (var2.isEmpty()) {
         var2.addAll(List.of());
      }

      return var2;
   }

   public static void lambda$getExamples$1(ArrayList examples, PlayerListEntry entry) {
      examples.add(entry.getProfile().getName());
   }

   public static void lambda$listSuggestions$0(ArrayList playerNames, PlayerListEntry entry) {
      playerNames.add(entry.getProfile().getName());
   }
}
