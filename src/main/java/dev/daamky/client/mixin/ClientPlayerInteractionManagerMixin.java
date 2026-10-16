package dev.daamky.client.mixin;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.internal.ClickSlotEventDataHelper;
import dev.daamky.client.module.misc.FakePlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin {
   @Inject(
      method = {"clickSlot"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
      if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null) {
         if (ClickSlotEventDataHelper.initCancelled(actionType, slotId, button, syncId)) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"interactBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void preventInteraction(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
      if (MinecraftClient.getInstance().world != null
         && ClickSlotEventDataHelper.initCancelled(MinecraftClient.getInstance().world.getBlockState(hitResult.getBlockPos()).getBlock())) {
         cir.setReturnValue(ActionResult.PASS);
      }
   }

   @Inject(
      method = {"attackEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
      if (target != null) {
         AttackEvent.getInstance().postAndCheckCancelled(new AttackEvent.AttackEventData(target));
         if (FakePlayer.getInstance().computeFlag(target)) {
            FakePlayer.getInstance().handleEnabled(player, target);
            player.resetLastAttackedTicks();
            ci.cancel();
         }
      }
   }
}
