package dev.daamky.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.config.ConfigManager;
import dev.daamky.client.gui.MainMenuScreen;
import dev.daamky.client.internal.ConfigArgument;
import dev.daamky.client.internal.ConfigValueArgument;
import dev.daamky.client.internal.FactoryHelper;
import dev.daamky.client.util.PlatformUtils;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "config"
)
public class ConfigCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("list").executes(this::lambda$execute$0));
      builder.then(literal("remove").then(argument("config", ConfigArgument.create()).executes(this::lambda$execute$1)));
      builder.then(literal("load").then(argument("config", ConfigArgument.create()).executes(this::lambda$execute$2)));
      builder.then(literal("save").then(argument("config", ConfigValueArgument.create()).executes(this::lambda$execute$3)));
      builder.then(literal("folder").executes(this::lambda$execute$4));
      builder.then(literal("menu").executes(this::lambda$execute$6));
      builder.then(literal("gui").executes(this::lambda$execute$8));
   }

   public int lambda$execute$8(CommandContext context) throws CommandSyntaxException {
      MinecraftClient var2 = MinecraftClient.getInstance();
      var2.execute(ConfigCommand::lambda$execute$7);
      return this.permissionLevel;
   }

   public static void lambda$execute$7(MinecraftClient mc) {
      MainMenuScreen.currentView = MainMenuScreen.ViewMode.CONFIGS;
      mc.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
   }

   public int lambda$execute$6(CommandContext context) throws CommandSyntaxException {
      MinecraftClient var2 = MinecraftClient.getInstance();
      var2.execute(ConfigCommand::lambda$execute$5);
      return this.permissionLevel;
   }

   public static void lambda$execute$5(MinecraftClient mc) {
      MainMenuScreen.currentView = MainMenuScreen.ViewMode.CONFIGS;
      mc.setScreen(FactoryHelper.getInstance().getMainMenuScreen());
   }

   public int lambda$execute$4(CommandContext context) throws CommandSyntaxException {
      if (PlatformUtils.computeContains("https://t.me/secretdevelopmentpizza")) {
         this.handleString("Открываю...");
      } else {
         this.handleString("Не удалось открыть папку конфигов.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$3(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "config");
      ConfigManager.getInstance().setCurrentConfig(var2);
      this.handleString("Сохранён конфиг: " + var2);
      return this.permissionLevel;
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "config");
      ConfigManager.getInstance().loadJson(var2);
      if (ConfigManager.getInstance().computeResolve(var2)) {
         this.handleString("Загружен конфиг: " + var2);
      } else {
         this.handleString("Я не нашла такого конфига T.T");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "config");
      if (ConfigManager.getInstance().computeResolve(var2)) {
         ConfigManager.getInstance().handleConfig(var2);
         this.handleString("Конфиг " + var2 + " удалён.");
      } else {
         this.handleString("Я не нашла такого конфига.");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      List var2 = ConfigManager.getInstance().getConfigsNames();
      if (var2.isEmpty()) {
         this.handleString("Список конфигов пуст.");
      } else {
         this.handleString("Список конфигов: " + String.join(", ", var2));
      }

      return this.permissionLevel;
   }
}
