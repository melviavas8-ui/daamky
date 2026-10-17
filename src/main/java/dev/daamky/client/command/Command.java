package dev.daamky.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.daamky.client.util.MinecraftAccess;
import net.minecraft.command.CommandSource;

public abstract class Command implements MinecraftAccess {
   public final String name;
   public int permissionLevel = 1;

   public Command() {
      CommandInfo var1 = this.getClass().getAnnotation(CommandInfo.class);
      this.name = var1.name();
   }

   public abstract void build(LiteralArgumentBuilder<CommandSource> var1);

   public final void register(CommandDispatcher<CommandSource> dispatcher) {
      LiteralArgumentBuilder var2 = LiteralArgumentBuilder.literal(this.name);
      this.build(var2);
      dispatcher.register(var2);
   }

   public static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
      return RequiredArgumentBuilder.argument(name, type);
   }

   public static LiteralArgumentBuilder<CommandSource> literal(String name) {
      return LiteralArgumentBuilder.literal(name);
   }

   public String getName() {
      return this.name;
   }

   public int getPermissionLevel() {
      return this.permissionLevel;
   }
}
