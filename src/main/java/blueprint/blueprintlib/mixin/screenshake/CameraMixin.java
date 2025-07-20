package blueprint.blueprintlib.mixin.screenshake;

import blueprint.blueprintlib.util.ScreenShakeHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Vec3d pos;

    @Inject(method = "setPos(DDD)V", at = @At("TAIL"))
    private void blueprintlib$applyScreenShake(double x, double y, double z, CallbackInfo ci) {
        Vec3d shake = ScreenShakeHandler.getOffset();
        this.pos = new Vec3d(x + shake.x, y + shake.y, z + shake.z);
    }
}