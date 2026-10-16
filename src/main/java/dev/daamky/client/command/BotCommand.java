package dev.daamky.client.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity.RemovalReason;

@CommandInfo(
   name = "bot"
)
public class BotCommand extends Command {
   public static final List<OtherClientPlayerEntity> list = new ArrayList<>();

   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$execute$0);
      builder.then(literal("add").executes(this::lambda$execute$1));
      builder.then(literal("clear").executes(this::lambda$execute$2));
   }

   public int initInit(String name) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.player != null && var2.world != null) {
         GameProfile var3 = new GameProfile(UUID.randomUUID(), name);
         OtherClientPlayerEntity var4 = new OtherClientPlayerEntity(var2.world, var3);
         var4.copyPositionAndRotation(var2.player);
         var4.setHealth(20.0F);
         var2.world.addEntity(var4);
         list.add(var4);
         this.handleString("Фейковый игрок §a" + name + "§f заспавнен! Он стоит на месте, неуязвим и регистрирует хитбоксы/Target ESP.");
         this.handleString("Чтобы удалить всех ботов, напишите §c*bot clear§f.");
         return this.permissionLevel;
      } else {
         this.handleString("Ошибка: вы должны быть в игре!");
         return 0;
      }
   }

   public int initClear() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.world == null) {
         list.clear();
         this.handleString("Список ботов очищен.");
         return this.permissionLevel;
      } else {
         int var2 = 0;

         for (OtherClientPlayerEntity var4 : new ArrayList<>(list)) {
            if (var4 != null) {
               var1.world.removeEntity(var4.getId(), RemovalReason.DISCARDED);
               var2++;
            }
         }

         list.clear();
         this.handleString("Удалено фейковых игроков: §c" + var2 + "§f.");
         return this.permissionLevel;
      }
   }

   public int lambda$execute$2(CommandContext context) throws CommandSyntaxException {
      return this.initClear();
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      return this.initInit("DummyPlayer");
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      return this.initInit("DummyPlayer");
   }
}
