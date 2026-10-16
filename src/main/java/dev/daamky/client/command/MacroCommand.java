package dev.daamky.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.GlfwKeyHelper;
import dev.daamky.client.internal.HomeHelper;
import dev.daamky.client.internal.MacroArgument;
import dev.daamky.client.internal.MacroManager;
import dev.daamky.client.util.KeyNames;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

@CommandInfo(
   name = "macro"
)
public class MacroCommand extends Command {
   public final MacroManager macroManager = MacroManager.getInstance();

   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         literal("add")
            .then(
               argument("name", new MacroArgument())
                  .then(argument("key", new GlfwKeyHelper()).then(argument("message", StringArgumentType.greedyString()).executes(this::lambda$execute$0)))
            )
      );
      builder.then(literal("remove").then(argument("name", new HomeHelper()).executes(this::lambda$execute$1)));
      builder.then(literal("clear").executes(this::lambda$execute$2));
      builder.then(literal("list").executes(this::lambda$execute$4));
   }

   public int lambda$execute$4(CommandContext context) throws CommandSyntaxException {
      if (this.macroManager.getList().isEmpty()) {
         this.handleString("Список пустой");
         return 0;
      } else {
         this.macroManager.getList().forEach(this::lambda$execute$3);
         return this.permissionLevel;
      }
   }

   public void lambda$execute$3(MacroManager.Macro macro) {
      this.handleString(
         "Название: "
            + Formatting.GRAY
            + macro.getName()
            + Formatting.RESET
            + ", Команда: "
            + Formatting.GRAY
            + macro.getMessage()
            + Formatting.RESET
            + ", Кнопка: "
            + Formatting.GRAY
            + KeyNames.computeNone(macro.getKey())
      );
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      this.macroManager.getList().clear();
      this.handleString("Все макросы были удалены.");
      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "name");
      if (!this.macroManager.has(var2)) {
         this.handleString("Макрос с таким именем не найден!");
         return 0;
      } else {
         this.macroManager.remove(var2);
         this.handleString("Макрос " + var2 + " был успешно удален!");
         return this.permissionLevel;
      }
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "name");
      String var3 = StringArgumentType.getString(context, "key");
      String var4 = StringArgumentType.getString(context, "message");
      int var5 = KeyNames.parseCase(var3);
      if (var5 == -1) {
         this.handleString("Клавиша " + var3 + " не найдена!");
         return 0;
      } else if (this.macroManager.has(var2)) {
         this.handleString("Макрос с таким именем уже существует!");
         return 0;
      } else {
         this.macroManager.initAdd(var2, var4, var5);
         this.handleString("Добавлен макрос с названием " + var2 + " с кнопкой " + var3 + " с командой " + var4);
         return this.permissionLevel;
      }
   }
}
