package dev.daamky.client.internal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.command.AutobuyCommand;
import dev.daamky.client.command.BotCommand;
import dev.daamky.client.command.Command;
import dev.daamky.client.command.ConfigCommand;
import dev.daamky.client.command.FakeplayerCommand;
import dev.daamky.client.command.FriendCommand;
import dev.daamky.client.command.GpsCommand;
import dev.daamky.client.command.MacroCommand;
import dev.daamky.client.command.NeuroCommand;
import dev.daamky.client.command.SkinCommand;
import dev.daamky.client.command.StaffsCommand;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RegisterHelper {
   public static final RegisterHelper registerHelper = new RegisterHelper();
   public final CommandDispatcher<CommandSource> commandDispatcher;
   public final ClientCommandSource class_637;
   public final List<Command> list = new ArrayList<>();

   public RegisterHelper() {
      this.commandDispatcher = new CommandDispatcher();
      this.class_637 = new ClientCommandSource(null, MinecraftClient.getInstance());
   }

   public void initInit() {
      FakeplayerCommand var1 = new FakeplayerCommand();
      this.registerAdd(
         new ConfigCommand(),
         new FriendCommand(),
         new StaffsCommand(),
         new MacroCommand(),
         new GpsCommand(),
         new NeuroCommand(),
         new AutobuyCommand(),
         new BotCommand(),
         new SkinCommand(),
         var1
      );
      var1.buildBuild(this.commandDispatcher);
   }

   public void registerAdd(Command... commands) {
      for (Command var5 : commands) {
         var5.register(this.commandDispatcher);
         this.list.add(var5);
      }
   }

   public String getString() {
      return "*";
   }

   public void handleCancel(String message, CallbackInfo ci) {
      String var3 = null;
      if (message.startsWith("*")) {
         var3 = "*";
      } else if (message.startsWith(".")) {
         var3 = ".";
      }

      if (var3 != null) {
         try {
            this.getCommandDispatcher().execute(message.substring(var3.length()), (CommandSource)this.getClass_637());
         } catch (CommandSyntaxException var5) {
            TextShaper.handleString("§cНеизвестная команда или синтаксическая ошибка: " + var5.getMessage());
         } catch (Exception var6) {
            TextShaper.handleString("§cОшибка выполнения команды: " + var6.getMessage());
         }

         ci.cancel();
      }
   }

   public CommandDispatcher<CommandSource> getCommandDispatcher() {
      return this.commandDispatcher;
   }

   public ClientCommandSource getClass_637() {
      return this.class_637;
   }

   public List<Command> getList() {
      return this.list;
   }

   public static RegisterHelper getInstance() {
      return registerHelper;
   }
}
