package dev.daamky.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.daamky.client.gui.AutoBuyScreen;
import dev.daamky.client.internal.BuyCheckHelper;
import dev.daamky.client.internal.ConfigStore;
import dev.daamky.client.internal.DebugStartHelper;
import dev.daamky.client.internal.NameFilter;
import dev.daamky.client.internal.SellCmdHelper;
import dev.daamky.client.internal.SortResetHelper;
import dev.daamky.client.internal.TextShaper;
import dev.daamky.client.item.ItemActionCatalog;
import dev.daamky.client.module.pve.AutoBuy;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

@CommandInfo(
   name = "autobuy"
)
public class AutobuyCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$execute$0);
      builder.then(literal("buy").executes(this::lambda$execute$1));
      builder.then(literal("sell").executes(this::lambda$execute$2));
      builder.then(literal("gui").executes(this::lambda$execute$3));
      builder.then(
         ((LiteralArgumentBuilder)literal("prices").then(literal("get").executes(this::lambda$execute$4)))
            .then(literal("list").executes(this::lambda$execute$5))
      );
      builder.then(
         literal("toggle").then(argument("item", StringArgumentType.word()).suggests(AutobuyCommand::lambda$execute$6).executes(this::lambda$execute$7))
      );
      builder.then(
         ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)literal("staff")
                     .then(literal("add").then(argument("nick", StringArgumentType.word()).executes(this::lambda$execute$8))))
                  .then(literal("remove").then(argument("nick", StringArgumentType.word()).executes(this::lambda$execute$9))))
               .then(literal("list").executes(this::lambda$execute$11)))
            .then(
               literal("autoleave")
                  .then(argument("state", StringArgumentType.word()).suggests(AutobuyCommand::lambda$execute$12).executes(this::lambda$execute$13))
            )
      );
      builder.then(
         ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)literal("config")
                     .then(literal("save").then(argument("name", StringArgumentType.word()).executes(this::lambda$execute$14))))
                  .then(
                     literal("load")
                        .then(argument("name", StringArgumentType.word()).suggests(AutobuyCommand::lambda$execute$16).executes(this::lambda$execute$17))
                  ))
               .then(
                  literal("delete")
                     .then(argument("name", StringArgumentType.word()).suggests(AutobuyCommand::lambda$execute$19).executes(this::lambda$execute$20))
               ))
            .then(literal("list").executes(this::lambda$execute$22))
      );
      builder.then(
         ((LiteralArgumentBuilder)((LiteralArgumentBuilder)literal("debug").then(literal("start").executes(this::lambda$execute$23)))
               .then(literal("stop").executes(this::lambda$execute$24)))
            .then(literal("status").executes(this::lambda$execute$25))
      );
   }

   public int lambda$execute$25(CommandContext ctx) throws CommandSyntaxException {
      boolean var2 = DebugStartHelper.getInstance().isRecording();
      TextShaper.handleString("§a[Debug] §fСтатус: " + (var2 ? "§aзапись" : "§cостановлен"));
      return this.permissionLevel;
   }

   public int lambda$execute$24(CommandContext ctx) throws CommandSyntaxException {
      DebugStartHelper.getInstance().runSize();
      return this.permissionLevel;
   }

   public int lambda$execute$23(CommandContext ctx) throws CommandSyntaxException {
      DebugStartHelper.getInstance().runClear();
      return this.permissionLevel;
   }

   public int lambda$execute$22(CommandContext ctx) throws CommandSyntaxException {
      List var2 = ConfigStore.list();
      if (var2.isEmpty()) {
         TextShaper.handleString("§a[Config] §fНет конфигов");
      } else {
         var2.forEach(AutobuyCommand::lambda$execute$21);
      }

      return this.permissionLevel;
   }

   public static void lambda$execute$21(String c) {
      mc.player.sendMessage(Text.literal("§7- §f" + c), false);
   }

   public int lambda$execute$20(CommandContext ctx) throws CommandSyntaxException {
      ConfigStore.handleDelete(StringArgumentType.getString(ctx, "name"));
      TextShaper.handleString("§a[Config] §fУдалено");
      return this.permissionLevel;
   }

   public static CompletableFuture lambda$execute$19(CommandContext ctx, SuggestionsBuilder b) throws CommandSyntaxException {
      ConfigStore.list().forEach((p0) -> AutobuyCommand.lambda$execute$18(b, p0));
      return b.buildFuture();
   }

   public static void lambda$execute$18(SuggestionsBuilder b, String arg_0) {
      b.suggest(arg_0);
   }

   public int lambda$execute$17(CommandContext ctx) throws CommandSyntaxException {
      ConfigStore.closeAuto(StringArgumentType.getString(ctx, "name"));
      TextShaper.handleString("§a[Config] §fЗагружено");
      return this.permissionLevel;
   }

   public static CompletableFuture lambda$execute$16(CommandContext ctx, SuggestionsBuilder b) throws CommandSyntaxException {
      ConfigStore.list().forEach((p0) -> AutobuyCommand.lambda$execute$15(b, p0));
      return b.buildFuture();
   }

   public static void lambda$execute$15(SuggestionsBuilder b, String arg_0) {
      b.suggest(arg_0);
   }

   public int lambda$execute$14(CommandContext ctx) throws CommandSyntaxException {
      ConfigStore.initInit(StringArgumentType.getString(ctx, "name"));
      TextShaper.handleString("§a[Config] §fСохранено");
      return this.permissionLevel;
   }

   public int lambda$execute$13(CommandContext ctx) throws CommandSyntaxException {
      boolean var2 = StringArgumentType.getString(ctx, "state").equalsIgnoreCase("on");
      NameFilter.setEnabled(var2);
      TextShaper.handleString("§a[Staff] §fАвтолив " + (var2 ? "§aвключен" : "§cвыключен"));
      return this.permissionLevel;
   }

   public static CompletableFuture lambda$execute$12(CommandContext ctx, SuggestionsBuilder b) throws CommandSyntaxException {
      b.suggest("on");
      b.suggest("off");
      return b.buildFuture();
   }

   public int lambda$execute$11(CommandContext ctx) throws CommandSyntaxException {
      HashSet var2 = NameFilter.initInit();
      if (var2.isEmpty()) {
         TextShaper.handleString("§a[Staff] §fСписок пуст");
      } else {
         var2.forEach(AutobuyCommand::lambda$execute$10);
      }

      return this.permissionLevel;
   }

   public static void lambda$execute$10(String n) {
      mc.player.sendMessage(Text.literal("§7- §f" + n), false);
   }

   public int lambda$execute$9(CommandContext ctx) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(ctx, "nick");
      if (NameFilter.computeCase(var2)) {
         TextShaper.handleString("§a[Staff] §e" + var2 + "§f удален");
      } else {
         TextShaper.handleString("§c[Staff] §fНика нет в списке");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$8(CommandContext ctx) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(ctx, "nick");
      if (NameFilter.computeAdd(var2)) {
         TextShaper.handleString("§a[Staff] §e" + var2 + "§f добавлен");
      } else {
         TextShaper.handleString("§c[Staff] §fНекорректный ник или уже есть");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$7(CommandContext ctx) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(ctx, "item");

      try {
         ItemActionCatalog var3 = ItemActionCatalog.valueOf(var2.toUpperCase());
         AutoBuy var4 = AutoBuy.getInstance();
         if (var4.getSet().contains(var3)) {
            var4.getSet().remove(var3);
            TextShaper.handleString("§a[AutoBuy] §f" + var2 + " §aвключен");
         } else {
            var4.getSet().add(var3);
            TextShaper.handleString("§a[AutoBuy] §f" + var2 + " §cвыключен");
         }
      } catch (Exception var5) {
         TextShaper.handleString("§c[AutoBuy] §fНеизвестный предмет: " + var2);
      }

      return this.permissionLevel;
   }

   public static CompletableFuture lambda$execute$6(CommandContext ctx, SuggestionsBuilder b) throws CommandSyntaxException {
      for (ItemActionCatalog var5 : ItemActionCatalog.values()) {
         b.suggest(var5.name().toLowerCase());
      }

      return b.buildFuture();
   }

   public int lambda$execute$5(CommandContext ctx) throws CommandSyntaxException {
      AutoBuy var2 = AutoBuy.getInstance();
      if (var2.getMap().isEmpty()) {
         TextShaper.handleString("§a[AutoBuy] §fЦены не получены");
      } else {
         for (Entry var4 : var2.getMap().entrySet()) {
            mc.player.sendMessage(Text.literal("§a[" + var4.getKey() + "] §fстоит " + var4.getValue()), false);
         }
      }

      return this.permissionLevel;
   }

   public int lambda$execute$4(CommandContext ctx) throws CommandSyntaxException {
      SortResetHelper.run();
      TextShaper.handleString("§a[AutoBuy] §fПолучаю цены...");
      return this.permissionLevel;
   }

   public int lambda$execute$3(CommandContext ctx) throws CommandSyntaxException {
      mc.setScreen(AutoBuyScreen.getInstance());
      return this.permissionLevel;
   }

   public int lambda$execute$2(CommandContext ctx) throws CommandSyntaxException {
      if (AutoBuy.getInstance().getMap().isEmpty()) {
         TextShaper.handleString("§a[AutoBuy] §fСначала получите цены");
      } else {
         SellCmdHelper.runEnabled();
         TextShaper.handleString("§a[AutoBuy] §fПродажа запущена");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext ctx) throws CommandSyntaxException {
      AutoBuy var2 = AutoBuy.getInstance();
      if (var2.getMap().isEmpty()) {
         TextShaper.handleString("§a[AutoBuy] §fСначала получите цены: \\autobuy prices get");
      } else {
         BuyCheckHelper.run();
         mc.player.networkHandler.sendChatMessage("/ah");
         var2.setV("Покупка");
         TextShaper.handleString("§a[AutoBuy] §fАвтобай запущен");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext ctx) throws CommandSyntaxException {
      mc.setScreen(AutoBuyScreen.getInstance());
      return this.permissionLevel;
   }
}
