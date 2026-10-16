package dev.daamky.client.internal;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.daamky.client.util.KeyNames;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class GlfwKeyHelper implements ArgumentType<String> {
   public static GlfwKeyHelper create() {
      return new GlfwKeyHelper();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String var2 = reader.readString();
      if (KeyNames.parseCase(var2) == -1) {
         throw new DynamicCommandExceptionType(GlfwKeyHelper::lambda$parse$0).create(var2);
      } else {
         return var2;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(
         Stream.of(GLFW.class.getDeclaredFields())
            .filter(GlfwKeyHelper::lambda$listSuggestions$1)
            .map(GlfwKeyHelper::lambda$listSuggestions$2)
            .collect(Collectors.toList()),
         builder
      );
   }

   public Collection<String> getExamples() {
      return List.of();
   }

   public static String lambda$listSuggestions$2(Field f) {
      return f.getName().substring("GLFW_KEY_".length());
   }

   public static boolean lambda$listSuggestions$1(Field f) {
      return f.getName().startsWith("GLFW_KEY_");
   }

   public static Message lambda$parse$0(Object name) {
      return Text.literal("Клавиша " + name.toString() + " не найдена");
   }
}
