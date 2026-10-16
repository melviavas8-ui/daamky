package dev.daamky.client.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.daamky.client.internal.GpsRenderer;
import net.minecraft.command.CommandSource;
import org.joml.Vector2i;

@CommandInfo(
   name = "gps"
)
public class GpsCommand extends Command {
   @Override
   public void build(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("off").executes(this::lambda$execute$0));
      builder.then(
         literal("add").then(argument("x", IntegerArgumentType.integer()).then(argument("z", IntegerArgumentType.integer()).executes(this::lambda$execute$1)))
      );
   }

   public int lambda$execute$1(CommandContext context) throws CommandSyntaxException {
      int var2 = IntegerArgumentType.getInteger(context, "x");
      int var3 = IntegerArgumentType.getInteger(context, "z");
      if (var2 == 0 && var3 == 0) {
         this.handleString("Ты не можешь указать маршрут на нулевые координаты.");
      } else {
         Vector2i var4 = new Vector2i(var2, var3);
         if (GpsRenderer.getInstance().getGpsPosition() != null && GpsRenderer.getInstance().getGpsPosition().equals(var4)) {
            this.handleString("Такая точка уже есть в маршруте.");
         } else {
            GpsRenderer.getInstance().setGpsPosition(var4);
            this.handleString("Установлен маршрут: " + var4.x + " " + var4.y);
         }
      }

      return this.permissionLevel;
   }

   public int lambda$execute$0(CommandContext context) throws CommandSyntaxException {
      if (GpsRenderer.getInstance().getGpsPosition() == null) {
         this.handleString("А чё ты выключить собираешься?");
      } else {
         GpsRenderer.getInstance().setGpsPosition(null);
         this.handleString("Маршрут успешно удален.");
      }

      return this.permissionLevel;
   }
}
