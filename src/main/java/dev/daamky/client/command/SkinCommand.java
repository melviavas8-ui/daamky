package dev.daamky.client.command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.FromStringHelper;
import dev.daamky.client.internal.LastSkinHelper;
import dev.daamky.client.model.RecordData;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.command.CommandSource;

@CommandInfo(
   name = "skin"
)
public class SkinCommand extends Command {
   public static Supplier<SkinTextures> supplier = null;
   public static boolean flag = false;

   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("off").executes(this::lambda$execute$0));
      builder.then(literal("set").then(argument("name", StringArgumentType.string()).executes(this::lambda$execute$1)));
   }

   public static Supplier<SkinTextures> initInit(String username) {
      UUID var3 = new RecordData().uuidByName(username);
      if (var3 == null) {
         var3 = FromStringHelper.computeName(username);
      }

      ProfileResult var1;
      GameProfile var2 = (var1 = mc.getSessionService().fetchProfile(var3, false)) == null ? null : var1.profile();
      if (var2 == null) {
         var2 = new GameProfile(var3, username);
      }

      return PlayerListEntry.texturesSupplier(var2);
   }

   public static Supplier<SkinTextures> getSupplier() {
      return flag ? supplier : null;
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      String var2 = StringArgumentType.getString(context, "name");

      try {
         supplier = initInit(var2);
         flag = true;
         LastSkinHelper.getInstance().writeTrim(var2);
         this.handleString("Установлен скин: " + var2);
      } catch (Exception var4) {
         this.handleString("Не удалось установить скин :c");
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      if (!flag) {
         this.handleString("Скин уже сброшен!");
      } else {
         supplier = null;
         flag = false;
         LastSkinHelper.getInstance().writeTrim(null);
         this.handleString("Скин успешно сброшен!");
      }

      return this.permissionLevel;
   }
}
