package blueprint.blueprintlib.mixin;

import blueprint.blueprintlib.ModConfig;
import blueprint.blueprintlib.animations.AnimationManager;
import blueprint.blueprintlib.animations.ArmPose;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class ArmPoseMixin<T extends LivingEntity> {

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    public ArmPoseMixin() {
    }

    @Inject(method = "setAngles", at = @At("TAIL"))
    public void applyCustomArmPose(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch, CallbackInfo ci) {
        ArmPose pose = AnimationManager.getCurrentPose();

        // Requires armPosingEnabled to be false for the animations to play, will fix later

        if (pose != null) {
            rightArm.pitch = pose.rightArmPitch;
            rightArm.yaw = pose.rightArmYaw;
            rightArm.roll = pose.rightArmRoll;
            rightArm.pivotX = pose.rightArmX;
            rightArm.pivotY = pose.rightArmY;
            rightArm.pivotZ = pose.rightArmZ;

            leftArm.pitch = pose.leftArmPitch;
            leftArm.yaw = pose.leftArmYaw;
            leftArm.roll = pose.leftArmRoll;
            leftArm.pivotX = pose.leftArmX;
            leftArm.pivotY = pose.leftArmY;
            leftArm.pivotZ = pose.leftArmZ;

            return;
        }

        if (!ModConfig.armPosingEnabled) return;

        // Right Arm
        this.rightArm.pitch = (float) Math.toRadians(ModConfig.rightArmPitch);
        this.rightArm.yaw = (float) Math.toRadians(ModConfig.rightArmYaw);
        this.rightArm.roll = (float) Math.toRadians(ModConfig.rightArmRoll);

        this.rightArm.pivotX = (float) ModConfig.rightArmOffsetX;
        this.rightArm.pivotY = (float) ModConfig.rightArmOffsetY;
        this.rightArm.pivotZ = (float) ModConfig.rightArmOffsetZ;

        // Left Arm
        this.leftArm.pitch = (float) Math.toRadians(ModConfig.leftArmPitch);
        this.leftArm.yaw = (float) Math.toRadians(ModConfig.leftArmYaw);
        this.leftArm.roll = (float) Math.toRadians(ModConfig.leftArmRoll);

        this.leftArm.pivotX = (float) ModConfig.leftArmOffsetX;
        this.leftArm.pivotY = (float) ModConfig.leftArmOffsetY;
        this.leftArm.pivotZ = (float) ModConfig.leftArmOffsetZ;
    }
}
