package dev.daamky.client.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.daamky.client.config.ConfigManager;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.command.CommandSource;

public class ConfigValueArgument implements ArgumentType<String> {
   public static ConfigValueArgument create() {
      return new ConfigValueArgument();
   }

   public String parse(StringReader reader) {
      try {
         return reader.readString();
      } catch (CommandSyntaxException var3) {
         throw new RuntimeException(var3);
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(ConfigManager.getInstance().getConfigsNames(), builder);
   }

   public Collection<String> getExamples() {
      List var1 = ConfigManager.getInstance().getConfigsNames().stream().limit(5L).collect(Collectors.toList());
      return var1.isEmpty() ? List.of() : var1;
   }

}
