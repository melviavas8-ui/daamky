package dev.daamky.client.internal;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.daamky.client.config.ConfigManager;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public class ConfigArgument implements ArgumentType<String> {
   public static ConfigArgument create() {
      return new ConfigArgument();
   }

   public String parse(StringReader reader) {
      String var2 = null;

      try {
         var2 = reader.readString();
      } catch (CommandSyntaxException var5) {
         throw new RuntimeException(var5);
      }

      if (!ConfigManager.getInstance().getConfigsNames().contains(var2)) {
         try {
            throw new DynamicCommandExceptionType(ConfigArgument::lambda$parse$0).create(var2);
         } catch (CommandSyntaxException var4) {
            throw new RuntimeException(var4);
         }
      } else {
         return var2;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(ConfigManager.getInstance().getConfigsNames(), builder);
   }

   public Collection<String> getExamples() {
      List var1 = ConfigManager.getInstance().getConfigsNames().stream().limit(5L).collect(Collectors.toList());
      return var1.isEmpty() ? List.of() : var1;
   }

   public static Message lambda$parse$0(Object name) {
      return Text.literal("Конфиг с именем " + name + " не найден");
   }
}
