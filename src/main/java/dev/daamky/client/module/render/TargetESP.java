package dev.daamky.client.module.render;

import dev.daamky.client.event.AttackEvent;
import dev.daamky.client.event.EventListener;
import dev.daamky.client.event.Subscription;
import dev.daamky.client.event.UpdateEvent;
import dev.daamky.client.internal.Render3DEvent;
import dev.daamky.client.internal.Render3DEvent2;
import dev.daamky.client.internal.Render3DEvent3;
import dev.daamky.client.internal.Render3DEvent4;
import dev.daamky.client.internal.TargetRenderer;
import dev.daamky.client.internal.ThickSquare;
import dev.daamky.client.module.Module;
import dev.daamky.client.module.ModuleCategory;
import dev.daamky.client.module.ModuleInfo;
import dev.daamky.client.setting.BooleanSetting;
import dev.daamky.client.setting.FloatSetting;
import dev.daamky.client.setting.ModeSetting;
import dev.daamky.client.setting.Setting;
import net.minecraft.entity.LivingEntity;

@ModuleInfo(
   name = "Target Esp",
   category = ModuleCategory.VISUALS,
   description = "Красиво выделяет вашу текущую цель при атаке"
)
public class TargetESP extends Module {
   public static final TargetESP targetESP = new TargetESP();
   public final Render3DEvent3 render3DEvent3 = new Render3DEvent3();
   public final Render3DEvent render3DEvent = new Render3DEvent();
   public final ThickSquare mofjo = new ThickSquare();
   public final Render3DEvent2 render3DEvent2 = new Render3DEvent2();
   public final Render3DEvent4 render3DEvent4 = new Render3DEvent4();
   public TargetRenderer targetRenderer = this.mofjo;
   public final ModeSetting crystal2 = new ModeSetting("Mode")
      .value("Marker")
      .values("Marker", "Comets", "Cubes", "Ring", "Crystal")
      .onAction(this::lambda$new$0);
   public final ModeSetting triangle2 = new ModeSetting("Texture")
      .value("Thick Square")
      .values("Thick Square", "Circle", "Thin Square", "Nurik", "Triangle")
      .visibleWhen(this::lambda$new$1);
   public final ModeSetting modeSetting = new ModeSetting("Animation").value("In").values("In", "Out", "None");
   public final FloatSetting duration = new FloatSetting("Duration").value(3.0F).range(1.0F, 20.0F).step(1.0F);
   public final FloatSetting size = new FloatSetting("Size").value(1.0F).range(0.1F, 2.0F).step(0.1F);
   public final FloatSetting inSize = new FloatSetting("In size").value(0.0F).range(0.0F, 1.0F).step(0.1F).visibleWhen(this::lambda$new$2);
   public final FloatSetting outSize = new FloatSetting("Out size").value(2.0F).range(1.0F, 2.0F).step(0.1F).visibleWhen(this::lambda$new$3);
   public final BooleanSetting lastPosition = new BooleanSetting("Last position").value(true);
   public final ModeSetting modeSetting2 = new ModeSetting("Ghost mode").value("1").values("1", "2").visibleWhen(this::lambda$new$4);
   public final FloatSetting ghostSpeed = new FloatSetting("Ghost speed").value(2.0F).range(0.1F, 15.0F).step(0.1F).visibleWhen(this::lambda$new$5);
   public final FloatSetting ghostCount = new FloatSetting("Ghost count").value(3.0F).range(1.0F, 3.0F).step(1.0F).visibleWhen(this::lambda$new$6);
   public final FloatSetting cubeSize = new FloatSetting("Cube size").value(0.12F).range(0.05F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$7);
   public final FloatSetting cubeCount = new FloatSetting("Cube count").value(28.0F).range(12.0F, 48.0F).step(2.0F).visibleWhen(this::lambda$new$8);
   public final FloatSetting rotationSpeed = new FloatSetting("Rotation speed").value(4.0F).range(0.5F, 15.0F).step(0.5F).visibleWhen(this::lambda$new$9);
   public final FloatSetting orbitSpeed = new FloatSetting("Orbit speed").value(1.0F).range(0.2F, 3.0F).step(0.1F).visibleWhen(this::lambda$new$10);
   public final BooleanSetting cubeGlow = new BooleanSetting("Cube glow").value(true).visibleWhen(this::lambda$new$11);
   public final FloatSetting crystalSize = new FloatSetting("Crystal Size").value(0.16F).range(0.05F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$12);
   public final FloatSetting crystalCount = new FloatSetting("Crystal Count").value(28.0F).range(12.0F, 48.0F).step(2.0F).visibleWhen(this::lambda$new$13);
   public final FloatSetting crystalRotationSpeed = new FloatSetting("Crystal Rotation Speed")
      .value(6.0F)
      .range(1.0F, 20.0F)
      .step(0.5F)
      .visibleWhen(this::lambda$new$14);
   public final FloatSetting crystalOrbitSpeed = new FloatSetting("Crystal Orbit Speed")
      .value(1.2F)
      .range(0.2F, 3.0F)
      .step(0.1F)
      .visibleWhen(this::lambda$new$15);
   public final BooleanSetting crystalGlow = new BooleanSetting("Crystal Glow").value(true).visibleWhen(this::lambda$new$16);
   public final FloatSetting ringSize = new FloatSetting("Ring Size").value(1.0F).range(0.5F, 2.0F).step(0.05F).visibleWhen(this::lambda$new$17);
   public final FloatSetting ringSpeed = new FloatSetting("Ring Speed").value(4.0F).range(0.5F, 12.0F).step(0.5F).visibleWhen(this::lambda$new$18);
   public final BooleanSetting ghostTrail = new BooleanSetting("Ghost trail").value(true).visibleWhen(this::lambda$new$19);
   public final FloatSetting trailLength = new FloatSetting("Trail length").value(16.0F).range(4.0F, 32.0F).step(1.0F).visibleWhen(this::lambda$new$20);
   public final BooleanSetting redOnImpact = new BooleanSetting("Red on Impact").value(true);
   public final FloatSetting impactFadeIn = new FloatSetting("Impact Fade In").value(0.35F).range(0.05F, 1.0F).step(0.01F).visibleWhen(this::lambda$new$21);
   public final FloatSetting impactFadeOut = new FloatSetting("Impact Fade Out").value(0.08F).range(0.01F, 0.5F).step(0.01F).visibleWhen(this::lambda$new$22);
   public final FloatSetting impactIntensity = new FloatSetting("Impact Intensity").value(1.0F).range(0.1F, 1.0F).step(0.05F).visibleWhen(this::lambda$new$23);

   public TargetESP() {
      this.addSettings(
         new Setting[]{
            this.crystal2,
            this.triangle2,
            this.modeSetting,
            this.duration,
            this.size,
            this.inSize,
            this.outSize,
            this.lastPosition,
            this.redOnImpact,
            this.impactFadeIn,
            this.impactFadeOut,
            this.impactIntensity,
            this.modeSetting2,
            this.ghostSpeed,
            this.ghostCount,
            this.cubeSize,
            this.cubeCount,
            this.rotationSpeed,
            this.orbitSpeed,
            this.cubeGlow,
            this.crystalSize,
            this.crystalCount,
            this.crystalRotationSpeed,
            this.crystalOrbitSpeed,
            this.crystalGlow,
            this.ringSize,
            this.ringSpeed,
            this.ghostTrail,
            this.trailLength
         }
      );
   }

   public TargetRenderer getEquals() {
      String var1 = this.crystal2.getValue();
      if ("Comets".equals(var1)) {
         return this.render3DEvent3;
      } else if ("Cubes".equals(var1)) {
         return this.render3DEvent;
      } else if ("Ring".equals(var1)) {
         return this.render3DEvent2;
      } else {
         return (TargetRenderer)("Crystal".equals(var1) ? this.render3DEvent4 : this.mofjo);
      }
   }

   @Override
   public void onEvent() {
      Subscription var1 = dev.daamky.client.event.Render3DEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$24));
      Subscription var2 = UpdateEvent.getInstance().subscribe(new EventListener<>(this::lambda$onEvent$25));
      Subscription var3 = AttackEvent.getInstance().subscribe(new EventListener<>(TargetESP::lambda$onEvent$26));
      this.addEventListeners(new Subscription[]{var1, var2, var3});
   }

   public static TargetESP getInstance() {
      return targetESP;
   }

   public ModeSetting getCrystal() {
      return this.crystal2;
   }

   public ModeSetting getTriangle() {
      return this.triangle2;
   }

   public FloatSetting getDuration() {
      return this.duration;
   }

   public FloatSetting getSize() {
      return this.size;
   }

   public ModeSetting getModeSetting2() {
      return this.modeSetting2;
   }

   public FloatSetting getGhostSpeed() {
      return this.ghostSpeed;
   }

   public FloatSetting getGhostCount() {
      return this.ghostCount;
   }

   public FloatSetting getCubeSize() {
      return this.cubeSize;
   }

   public FloatSetting getCubeCount() {
      return this.cubeCount;
   }

   public FloatSetting getRotationSpeed() {
      return this.rotationSpeed;
   }

   public FloatSetting getOrbitSpeed() {
      return this.orbitSpeed;
   }

   public BooleanSetting getCubeGlow() {
      return this.cubeGlow;
   }

   public FloatSetting getCrystalSize() {
      return this.crystalSize;
   }

   public FloatSetting getCrystalCount() {
      return this.crystalCount;
   }

   public FloatSetting getCrystalRotationSpeed() {
      return this.crystalRotationSpeed;
   }

   public FloatSetting getCrystalOrbitSpeed() {
      return this.crystalOrbitSpeed;
   }

   public BooleanSetting getCrystalGlow() {
      return this.crystalGlow;
   }

   public FloatSetting getRingSize() {
      return this.ringSize;
   }

   public FloatSetting getRingSpeed() {
      return this.ringSpeed;
   }

   public BooleanSetting getGhostTrail() {
      return this.ghostTrail;
   }

   public FloatSetting getTrailLength() {
      return this.trailLength;
   }

   public BooleanSetting getRedOnImpact() {
      return this.redOnImpact;
   }

   public FloatSetting getImpactFadeIn() {
      return this.impactFadeIn;
   }

   public FloatSetting getImpactFadeOut() {
      return this.impactFadeOut;
   }

   public FloatSetting getImpactIntensity() {
      return this.impactIntensity;
   }

   public BooleanSetting getRedOnImpact2() {
      return this.redOnImpact;
   }

   public FloatSetting getImpactFadeIn2() {
      return this.impactFadeIn;
   }

   public FloatSetting getImpactFadeOut2() {
      return this.impactFadeOut;
   }

   public FloatSetting getImpactIntensity2() {
      return this.impactIntensity;
   }

   public static void lambda$onEvent$26(AttackEvent.AttackEventData event) {
      if (event.entity() instanceof LivingEntity var1 && var1.isAlive() && var1 != mc.player) {
         TargetRenderer.handleOut(var1);
      }
   }

   public void lambda$onEvent$25(UpdateEvent event) {
      TargetRenderer var2 = this.getEquals();
      var2.runEnabled();
      long var3 = (long)(this.duration.getValue() * 250.0F);
      var2.handleLong(var3, this.modeSetting.getValue(), this.size.getValue(), this.inSize.getValue(), this.outSize.getValue());
      var2.runOrbit();
   }

   public void lambda$onEvent$24(dev.daamky.client.event.Render3DEvent.Render3DEventData event) {
      TargetRenderer var2 = this.getEquals();
      TargetRenderer.runLast();
      var2.onRender3D(event);
   }

   public Boolean lambda$new$23() {
      return this.redOnImpact.getValue();
   }

   public Boolean lambda$new$22() {
      return this.redOnImpact.getValue();
   }

   public Boolean lambda$new$21() {
      return this.redOnImpact.getValue();
   }

   public Boolean lambda$new$20() {
      return this.crystal2.is("Ring") && this.ghostTrail.getValue();
   }

   public Boolean lambda$new$19() {
      return this.crystal2.is("Ring");
   }

   public Boolean lambda$new$18() {
      return this.crystal2.is("Ring");
   }

   public Boolean lambda$new$17() {
      return this.crystal2.is("Ring");
   }

   public Boolean lambda$new$16() {
      return this.crystal2.is("Crystal");
   }

   public Boolean lambda$new$15() {
      return this.crystal2.is("Crystal");
   }

   public Boolean lambda$new$14() {
      return this.crystal2.is("Crystal");
   }

   public Boolean lambda$new$13() {
      return this.crystal2.is("Crystal");
   }

   public Boolean lambda$new$12() {
      return this.crystal2.is("Crystal");
   }

   public Boolean lambda$new$11() {
      return this.crystal2.is("Cubes");
   }

   public Boolean lambda$new$10() {
      return this.crystal2.is("Cubes");
   }

   public Boolean lambda$new$9() {
      return this.crystal2.is("Cubes");
   }

   public Boolean lambda$new$8() {
      return this.crystal2.is("Cubes");
   }

   public Boolean lambda$new$7() {
      return this.crystal2.is("Cubes");
   }

   public Boolean lambda$new$6() {
      return this.crystal2.is("Comets");
   }

   public Boolean lambda$new$5() {
      return this.crystal2.is("Comets");
   }

   public Boolean lambda$new$4() {
      return this.crystal2.is("Comets");
   }

   public Boolean lambda$new$3() {
      return this.modeSetting.is("Out");
   }

   public Boolean lambda$new$2() {
      return this.modeSetting.is("In");
   }

   public Boolean lambda$new$1() {
      return this.crystal2.is("Marker");
   }

   public void lambda$new$0() {
      String var1 = this.getCrystal().getValue();

      this.targetRenderer = (TargetRenderer)(switch (var1) {
         case "Comets" -> this.render3DEvent3;
         case "Cubes" -> this.render3DEvent;
         case "Ring" -> this.render3DEvent2;
         case "Crystal" -> this.render3DEvent4;
         default -> this.mofjo;
      });
   }
}
