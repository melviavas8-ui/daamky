package dev.daamky.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.PlayerArgument;
import dev.daamky.client.internal.StaffArgument;
import dev.daamky.client.internal.StaffStore;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "staffs"
)
public class StaffsCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("clear").executes(this::lambda$execute$0));
      builder.then(literal("list").executes(this::lambda$execute$1));
      builder.then(literal("add").then(argument("actions", PlayerArgument.create()).executes(this::lambda$execute$2)));
      builder.then(literal("remove").then(argument("actions", StaffArgument.create()).executes(this::lambda$execute$3)));
   }

   public int lambda$execute$3(CommandContext context) throws CommandSyntaxException {
      String var2 = (String)context.getArgument("actions", String.class);
      if (!StaffStore.getInstance().computeContains(var2)) {
         this.handleString("Нет такого " + var2 + "!");
      } else {
         StaffStore.getInstance().computeRemove(var2);
         this.handleString(var2 + " удален из списка лохов.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      String var2 = (String)context.getArgument("actions", String.class);
      if (StaffStore.getInstance().computeContains(var2)) {
         this.handleString("Уже есть в списке лохов!");
      } else {
         StaffStore.getInstance().handleData(var2);
         this.handleString(var2 + " добавлен в список лохов.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      if (StaffStore.getInstance().getData().isEmpty()) {
         this.handleString("Список лохов пуст.");
      } else {
         try {
            String var2 = String.join(", ", StaffStore.getInstance().getData());
            this.handleString("Лохи: " + var2);
         } catch (Exception var3) {
            this.handleString("Произошла ошибка при получении списка лохов!");
         }
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      if (!StaffStore.getInstance().getData().isEmpty()) {
         StaffStore.getInstance().getData();
         this.handleString("Список лохов очищен.");
      } else {
         this.handleString("Список лохов пуст.");
      }

      return this.permissionLevel;
   }
}
