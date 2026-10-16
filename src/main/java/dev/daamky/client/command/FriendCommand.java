package dev.daamky.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.FriendArgument;
import dev.daamky.client.internal.FriendsStore;
import dev.daamky.client.internal.PlayerArgument;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "friend"
)
public class FriendCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("clear").executes(this::lambda$execute$0));
      builder.then(literal("list").executes(this::lambda$execute$1));
      builder.then(literal("add").then(argument("actions", PlayerArgument.create()).executes(this::lambda$execute$2)));
      builder.then(literal("remove").then(argument("actions", FriendArgument.create()).executes(this::lambda$execute$3)));
   }

   public int lambda$execute$3(CommandContext context) throws CommandSyntaxException {
      String var2 = (String)context.getArgument("actions", String.class);
      if (!FriendsStore.getInstance().computeContains(var2)) {
         this.handleString("Вы еще не дружили с " + var2 + ".");
      } else {
         FriendsStore.getInstance().computeRemove(var2);
         this.handleString(var2 + " удален из друзей.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      String var2 = (String)context.getArgument("actions", String.class);
      if (FriendsStore.getInstance().computeContains(var2)) {
         this.handleString(var2 + "уже есть в друзьях.");
      } else {
         FriendsStore.getInstance().handleData(var2);
         this.handleString(var2 + " добавлен в друзья.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      if (FriendsStore.getInstance().getData().isEmpty()) {
         this.handleString("У тебя нет друзей.");
      } else {
         try {
            String var2 = String.join(", ", FriendsStore.getInstance().getData());
            this.handleString("Друзья: " + var2);
         } catch (Exception var3) {
            this.handleString("Произошла ошибка при получении списка друзей.");
         }
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      if (!FriendsStore.getInstance().getData().isEmpty()) {
         FriendsStore.getInstance().getData();
         this.handleString("Список друзей очищен.");
      } else {
         this.handleString("У тебя нет друзей.");
      }

      return this.permissionLevel;
   }
}
