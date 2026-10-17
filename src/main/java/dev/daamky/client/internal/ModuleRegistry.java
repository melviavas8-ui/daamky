package dev.daamky.client.internal;

import dev.daamky.client.module.Module;
import dev.daamky.client.module.combat.AttackAura;
import dev.daamky.client.module.combat.AutoBuff;
import dev.daamky.client.module.combat.AutoExplosion;
import dev.daamky.client.module.combat.AutoGApple;
import dev.daamky.client.module.combat.AutoSwap;
import dev.daamky.client.module.combat.AutoTotem;
import dev.daamky.client.module.combat.Criticals;
import dev.daamky.client.module.combat.ElytraTarget;
import dev.daamky.client.module.combat.HitSounds;
import dev.daamky.client.module.combat.MaceAttacker;
import dev.daamky.client.module.combat.NoEntityTrace;
import dev.daamky.client.module.combat.NoFriendHurt;
import dev.daamky.client.module.combat.TriggerBot;
import dev.daamky.client.module.misc.Anonymous;
import dev.daamky.client.module.misc.AutoRespawn;
import dev.daamky.client.module.misc.Configs;
import dev.daamky.client.module.misc.FakePlayer;
import dev.daamky.client.module.misc.HealthResolver;
import dev.daamky.client.module.misc.MinecraftTweaks;
import dev.daamky.client.module.misc.NoAnnoying;
import dev.daamky.client.module.misc.NoServerPack;
import dev.daamky.client.module.misc.Optimization;
import dev.daamky.client.module.misc.SpecDetect;
import dev.daamky.client.module.misc.TapeMouse;
import dev.daamky.client.module.misc.ToggleSounds;
import dev.daamky.client.module.misc.TotemInfo;
import dev.daamky.client.module.movement.AirStuck;
import dev.daamky.client.module.movement.ElytraStrafe;
import dev.daamky.client.module.movement.Flight;
import dev.daamky.client.module.movement.FreeCam;
import dev.daamky.client.module.movement.InventoryMove;
import dev.daamky.client.module.movement.NoJumpDelay;
import dev.daamky.client.module.movement.NoWeb;
import dev.daamky.client.module.movement.Speed;
import dev.daamky.client.module.movement.Sprint;
import dev.daamky.client.module.movement.Strafe;
import dev.daamky.client.module.movement.WindHop;
import dev.daamky.client.module.player.Alerts;
import dev.daamky.client.module.player.AntiAFK;
import dev.daamky.client.module.player.Assistant;
import dev.daamky.client.module.player.AutoLeave;
import dev.daamky.client.module.player.AutoTool;
import dev.daamky.client.module.player.ClickPearl;
import dev.daamky.client.module.player.ElytraSwap;
import dev.daamky.client.module.player.FakeLag;
import dev.daamky.client.module.player.LockSlot;
import dev.daamky.client.module.player.MacroSystem;
import dev.daamky.client.module.player.NoRotate;
import dev.daamky.client.module.pve.AncientXray;
import dev.daamky.client.module.pve.AuctionHelper;
import dev.daamky.client.module.pve.AutoBuy;
import dev.daamky.client.module.pve.AutoEnd;
import dev.daamky.client.module.pve.AutoFish;
import dev.daamky.client.module.pve.AutoMiner;
import dev.daamky.client.module.pve.AutoWarden;
import dev.daamky.client.module.pve.BaseBuilder;
import dev.daamky.client.module.pve.DeathTravel;
import dev.daamky.client.module.pve.ElytraTravel;
import dev.daamky.client.module.pve.MineHelper;
import dev.daamky.client.module.pve.Nuker;
import dev.daamky.client.module.pve.SpawnerESP;
import dev.daamky.client.module.pve.StashFinder;
import dev.daamky.client.module.pve.StorageESP;
import dev.daamky.client.module.render.Ambience;
import dev.daamky.client.module.render.BlockTweaks;
import dev.daamky.client.module.render.Boxes;
import dev.daamky.client.module.render.CameraTweaks;
import dev.daamky.client.module.render.Cape;
import dev.daamky.client.module.render.Chams;
import dev.daamky.client.module.render.ChinaHat;
import dev.daamky.client.module.render.ClickGUI;
import dev.daamky.client.module.render.Comets;
import dev.daamky.client.module.render.CustomCrosshair;
import dev.daamky.client.module.render.CustomGlint;
import dev.daamky.client.module.render.ESP;
import dev.daamky.client.module.render.Gamma;
import dev.daamky.client.module.render.HandShader;
import dev.daamky.client.module.render.HitEffect;
import dev.daamky.client.module.render.HitTweaks;
import dev.daamky.client.module.render.Interface;
import dev.daamky.client.module.render.ItemPhysic;
import dev.daamky.client.module.render.JumpCircle;
import dev.daamky.client.module.render.MotionBlur;
import dev.daamky.client.module.render.NameTags;
import dev.daamky.client.module.render.NightVision;
import dev.daamky.client.module.render.Particles;
import dev.daamky.client.module.render.Pointers;
import dev.daamky.client.module.render.Removals;
import dev.daamky.client.module.render.SeeInvisibles;
import dev.daamky.client.module.render.ShulkerPreview;
import dev.daamky.client.module.render.SkeletonESP;
import dev.daamky.client.module.render.SmoothCamera;
import dev.daamky.client.module.render.Svetych;
import dev.daamky.client.module.render.SwingAnimation;
import dev.daamky.client.module.render.TargetESP;
import dev.daamky.client.module.render.TotemEffect;
import dev.daamky.client.module.render.Trails;
import dev.daamky.client.module.render.Trajectories;
import dev.daamky.client.module.render.ViewModel;
import dev.daamky.client.module.render.Zoom;
import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {
   public static final ModuleRegistry moduleRegistry = new ModuleRegistry();
   public final List<Module> list = new ArrayList<>();

   public void load() {
      this.handleAdd(
         Sprint.getInstance(),
         ClickGUI.getInstance(),
         ESP.getInstance(),
         Ambience.getInstance(),
         Strafe.getInstance(),
         WindHop.getInstance(),
         ElytraStrafe.getInstance(),
         AirStuck.getInstance(),
         NoJumpDelay.getInstance(),
         AttackAura.getInstance(),
         TriggerBot.getInstance(),
         MaceAttacker.getInstance(),
         AutoRespawn.getInstance(),
         Interface.getInstance(),
         AutoLeave.getInstance(),
         NameTags.getInstance(),
         AutoTool.getInstance(),
         AutoTotem.getInstance(),
         AutoGApple.getInstance(),
         InventoryMove.getInstance(),
         AutoSwap.getInstance(),
         Removals.getInstance(),
         ClickPearl.getInstance(),
         ElytraSwap.getInstance(),
         SwingAnimation.getInstance(),
         ViewModel.getInstance(),
         Speed.getInstance(),
         AutoBuff.getInstance(),
         CameraTweaks.getInstance(),
         Pointers.getInstance(),
         dev.daamky.client.module.player.Timer.getInstance(),
         Assistant.getInstance(),
         ElytraTarget.getInstance(),
         AutoExplosion.getInstance(),
         NoWeb.getInstance(),
         Trajectories.getInstance(),
         NoFriendHurt.getInstance(),
         NoEntityTrace.getInstance(),
         Particles.getInstance(),
         TapeMouse.getInstance(),
         SeeInvisibles.getInstance(),
         HealthResolver.getInstance(),
         Anonymous.getInstance(),
         JumpCircle.getInstance(),
         ToggleSounds.getInstance(),
         MotionBlur.getInstance(),
         FakeLag.getInstance(),
         NightVision.getInstance(),
         Gamma.getInstance(),
         NoServerPack.getInstance(),
         AuctionHelper.getInstance(),
         Trails.getInstance(),
         TargetESP.getInstance(),
         Flight.getInstance(),
         Svetych.getInstance(),
         FreeCam.getInstance(),
         Optimization.getInstance(),
         Zoom.getInstance(),
         ChinaHat.getInstance(),
         ItemPhysic.getInstance(),
         Chams.getInstance(),
         Comets.getInstance(),
         Cape.getInstance(),
         AntiAFK.getInstance(),
         NoRotate.getInstance(),
         AutoMiner.getInstance(),
         BaseBuilder.getInstance(),
         AutoFish.getInstance(),
         DeathTravel.getInstance(),
         ElytraTravel.getInstance(),
         Nuker.getInstance(),
         CustomCrosshair.getInstance(),
         HitSounds.getInstance(),
         Criticals.getInstance(),
         SkeletonESP.getInstance(),
         SpecDetect.getInstance(),
         BlockTweaks.getInstance(),
         Boxes.getInstance(),
         MacroSystem.getInstance(),
         LockSlot.getInstance(),
         dev.daamky.client.module.misc.InventoryHelper.getInstance(),
         TotemInfo.getInstance(),
         HitTweaks.getInstance(),
         AncientXray.getInstance(),
         MineHelper.getInstance(),
         SpawnerESP.getInstance(),
         StashFinder.getInstance(),
         StorageESP.getInstance(),
         AutoBuy.getInstance(),
         Alerts.getInstance(),
         MinecraftTweaks.getInstance(),
         CustomGlint.getInstance(),
         ShulkerPreview.getInstance(),
         HandShader.getInstance(),
         HitEffect.getInstance(),
         Configs.getInstance(),
         FakePlayer.getInstance(),
         TotemEffect.getInstance(),
         NoAnnoying.getInstance(),
         AutoEnd.getInstance(),
         AutoWarden.getInstance(),
         SmoothCamera.getInstance()
      );
      this.list.sort(ModuleRegistry::lambda$load$0);
      Alerts.getInstance().setEnabledInternal(true, true);
   }

   public void handleAdd(Module... features) {
      this.list.addAll(List.of(features));
   }

   public List<Module> getList() {
      return this.list;
   }

   public <T extends Module> T computeNext(Class<T> clazz) {
      for (Module var3 : this.list) {
         if (clazz.isInstance(var3)) {
            return (T)clazz.cast(var3);
         }
      }

      return null;
   }

   public static ModuleRegistry getInstance() {
      return moduleRegistry;
   }

   public static int lambda$load$0(Module a, Module b) {
      return a.getName().compareToIgnoreCase(b.getName());
   }
}
