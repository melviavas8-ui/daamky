package dev.daamky.client.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.MacroArgument;
import dev.daamky.client.internal.NeuroRecHelper;
import dev.daamky.client.internal.StrokesHelper;
import java.util.Collection;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "neuro"
)
public class NeuroCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      NeuroRecHelper var2 = NeuroRecHelper.getInstance();
      builder.then(literal("create").then(argument("name", new MacroArgument()).executes(this::lambda$execute$0)));
      builder.then(
         ((LiteralArgumentBuilder)literal("rec").then(argument("name", new MacroArgument()).executes(this::lambda$execute$1))).executes(this::lambda$execute$2)
      );
      builder.then(literal("stoprec").executes(this::lambda$execute$3));
      builder.then(
         ((LiteralArgumentBuilder)literal("train")
               .then(
                  ((RequiredArgumentBuilder)argument("name", new MacroArgument())
                        .then(argument("epochs", IntegerArgumentType.integer(10, 100000)).executes(this::lambda$execute$4)))
                     .executes(this::lambda$execute$5)
               ))
            .executes(this::lambda$execute$6)
      );
      builder.then(literal("select").then(argument("name", new MacroArgument()).executes(this::lambda$execute$7)));
      builder.then(literal("list").executes(this::lambda$execute$8));
      builder.executes(this::lambda$execute$9);
   }

   public int lambda$execute$9(CommandContext context) throws CommandSyntaxException {
      this.handleString("§a=== Neuro Pipeline ===");
      this.handleString("§71. §f*neuro create <имя>          §8— создать новую модель");
      this.handleString("§72. §f*neuro rec <имя>             §8— запустить запись (FakePlayer бот появится)");
      this.handleString("§7   §f*neuro stoprec               §8— остановить запись, сохранить датасет");
      this.handleString("§73. §f*neuro train <имя> [эпохи]   §8— обучить (по умолчанию 2000 эпох)");
      this.handleString("§74. §f*neuro select <имя>          §8— активировать модель в ротации Neuro");
      this.handleString("§75. §f*neuro list                  §8— список всех моделей со статусом");
      return this.permissionLevel;
   }

   public int lambda$execute$8(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      Collection var3 = nm.values();
      if (var3.isEmpty()) {
         this.handleString("§7[Neuro] Нет моделей. Создай: §f*neuro create <имя>");
         return this.permissionLevel;
      } else {
         this.handleString("§a[Neuro] §7Список моделей:");

         for (StrokesHelper var5 : (Iterable<StrokesHelper>)(Iterable)var3) {
            boolean var6 = var5 == nm.getStrokesHelper();
            String var7 = var5.mvijuk < 0.0 ? "§cне обучена" : "§aобучена §7(loss §f" + String.format("%.5f", var5.mvijuk) + "§7)";
            String var8 = var6 ? "§a▶ " : "§7  ";
            this.handleString(var8 + "§f" + var5.name + " §7— §f" + var5.int2 + " §7сэмплов | " + var7);
         }

         return this.permissionLevel;
      }
   }

   public int lambda$execute$7(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      String var3 = StringArgumentType.getString(context, "name");
      if (nm.computeCase(var3)) {
         this.handleString("§a[Neuro] §7Активирована модель §f" + var3 + " §7для ротации Neuro");
      } else {
         this.handleString("§c[Neuro] Модель §f" + var3 + "§c не найдена");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$6(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      nm.startLearn(null, 2000);
      return this.permissionLevel;
   }

   public int lambda$execute$5(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      String var3 = StringArgumentType.getString(context, "name");
      nm.startLearn(var3, 2000);
      return this.permissionLevel;
   }

   public int lambda$execute$4(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      String var3 = StringArgumentType.getString(context, "name");
      int var4 = IntegerArgumentType.getInteger(context, "epochs");
      nm.startLearn(var3, var4);
      return this.permissionLevel;
   }

   public int lambda$execute$3(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      nm.runNeuro();
      return this.permissionLevel;
   }

   public int lambda$execute$2(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      nm.handleName(null);
      return this.permissionLevel;
   }

   public int lambda$execute$1(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      String var3 = StringArgumentType.getString(context, "name");
      nm.handleName(var3);
      return this.permissionLevel;
   }

   public int lambda$execute$0(NeuroRecHelper nm, CommandContext context) throws CommandSyntaxException {
      String var3 = StringArgumentType.getString(context, "name");
      if (nm.setName(var3)) {
         this.handleString("§a[Neuro] §7Модель §f" + var3 + " §7создана. Начни запись: §f*neuro rec " + var3);
      } else {
         this.handleString("§c[Neuro] Модель §f" + var3 + "§c уже существует. Выбери: §f*neuro select " + var3);
      }

      return this.permissionLevel;
   }
}
