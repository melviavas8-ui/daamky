package dev.daamky.client.internal;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public class FriendArgument implements ArgumentType<String> {
   public static FriendArgument create() {
      return new FriendArgument();
   }

   public String parse(StringReader reader) {
      String var2 = null;

      try {
         var2 = reader.readString();
      } catch (CommandSyntaxException var5) {
         throw new RuntimeException(var5);
      }

      if (!FriendsStore.getInstance().computeContains(var2)) {
         try {
            throw new DynamicCommandExceptionType(FriendArgument::lambda$parse$0).create(var2);
         } catch (CommandSyntaxException var4) {
            throw new RuntimeException(var4);
         }
      } else {
         return var2;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(FriendsStore.getInstance().getData(), builder);
   }

   public Collection<String> getExamples() {
      List var1 = FriendsStore.getInstance().getData().stream().limit(5L).collect(Collectors.toList());
      return var1.isEmpty() ? List.of() : var1;
   }

   public static Message lambda$parse$0(Object name) {
      return Text.literal("Друга с именем " + name + " не найдено");
   }
}
