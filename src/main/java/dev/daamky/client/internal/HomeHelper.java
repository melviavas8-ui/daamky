package dev.daamky.client.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeHelper implements ArgumentType<String> {
   public String parse(StringReader reader) throws CommandSyntaxException {
      return StringArgumentType.string().parse(reader);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      List var3 = MacroManager.getInstance().getList().stream().map(MacroManager.Macro::getName).toList();
      String var4 = builder.getRemaining().toLowerCase();
      var3.stream().filter(HomeHelper::lambda$listSuggestions$0).forEach(HomeHelper::lambda$listSuggestions$1);
      return builder.buildFuture();
   }

   public Collection<String> getExamples() {
      return List.of("home", "login", "tp");
   }

   public static void lambda$listSuggestions$1(SuggestionsBuilder builder, String arg_0) {
      builder.suggest(arg_0);
   }

   public static boolean lambda$listSuggestions$0(String remaining, String name) {
      return name.toLowerCase().startsWith(remaining);
   }
}
