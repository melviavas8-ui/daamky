package dev.daamky.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.module.misc.FakePlayer;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "fakeplayer"
)
public class FakeplayerCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$execute$0);
      builder.then(literal("add").executes(this::lambda$execute$1));
      builder.then(literal("remove").executes(this::lambda$execute$2));
      builder.then(literal("del").executes(this::lambda$execute$3));
   }

   public void buildBuild(CommandDispatcher<CommandSource> dispatcher) {
      LiteralArgumentBuilder var2 = LiteralArgumentBuilder.literal("fp");
      this.build(var2);
      dispatcher.register(var2);
   }

   public int lambda$execute$3(CommandContext context) throws CommandSyntaxException {
      FakePlayer.getInstance().setEnabledInternal(false, false);
      this.handleString("§c[FakePlayer] §fФейковый игрок удален!");
      return this.permissionLevel;
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      FakePlayer.getInstance().setEnabledInternal(false, false);
      this.handleString("§c[FakePlayer] §fФейковый игрок удален!");
      return this.permissionLevel;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      FakePlayer.getInstance().setEnabledInternal(true, false);
      this.handleString("§a[FakePlayer] §fФейковый игрок добавлен!");
      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      FakePlayer.getInstance().toggle();
      boolean var2 = FakePlayer.getInstance().isEnabled();
      this.handleString(var2 ? "§a[FakePlayer] §fФейковый игрок добавлен!" : "§c[FakePlayer] §fФейковый игрок удален!");
      return this.permissionLevel;
   }
}
