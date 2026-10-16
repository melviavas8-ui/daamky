package dev.daamky.client.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;

public class MacroArgument implements ArgumentType<String> {
   public static MacroArgument create() {
      return new MacroArgument();
   }

   public String parse(StringReader r) {
      int var2 = r.getCursor();

      while (r.canRead() && !Character.isWhitespace(r.peek())) {
         r.skip();
      }

      return r.getString().substring(var2, r.getCursor());
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return builder.buildFuture();
   }

}
