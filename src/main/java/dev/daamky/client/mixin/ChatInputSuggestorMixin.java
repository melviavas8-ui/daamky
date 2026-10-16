package dev.daamky.client.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.daamky.client.internal.RegisterHelper;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatInputSuggestor.class})
public abstract class ChatInputSuggestorMixin {
   @Shadow
   @Final
   TextFieldWidget field_21599;
   @Shadow
   private ParseResults<CommandSource> field_21610;
   @Shadow
   private CompletableFuture<Suggestions> field_21611;
   @Shadow
   private boolean field_21614;

   @Shadow
   public abstract void method_23920(boolean var1);

   @Inject(
      method = {"refresh"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRefresh(CallbackInfo ci) {
      String var3 = this.field_21599.getText();
      String var2;
      if (var3.startsWith(var2 = RegisterHelper.getInstance().getString())) {
         if (this.field_21610 != null && !this.field_21610.getReader().getString().equals(var3)) {
            this.field_21610 = null;
         }

         if (!this.field_21614) {
            this.field_21599.setSuggestion(null);
         }

         StringReader var4 = new StringReader(var3);
         var4.setCursor(var2.length());
         int var5 = this.field_21599.getCursor();
         CommandDispatcher var6 = RegisterHelper.getInstance().getCommandDispatcher();
         if (this.field_21610 == null) {
            this.field_21610 = var6.parse(var4, (CommandSource)RegisterHelper.getInstance().getClass_637());
         }

         int var7 = this.field_21614 ? var4.getCursor() : var2.length();
         if (var5 >= var7) {
            this.field_21611 = var6.getCompletionSuggestions(this.field_21610, var5);
            this.field_21611.thenRun(this::lambda$onRefresh$0);
         }

         ci.cancel();
      }
   }

   private void lambda$onRefresh$0() {
      if (this.field_21611.isDone()) {
         this.method_23920(true);
      }
   }
}
