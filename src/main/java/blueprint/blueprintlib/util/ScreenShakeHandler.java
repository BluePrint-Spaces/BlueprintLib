package blueprint.blueprintlib.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ScreenShakeHandler {
    private static Vec3d offset = Vec3d.ZERO;
    private static double decay = 0.9;
    private static double intensity = 0.0;

    /*
    Triggers a screen shake with the specified intensity
    @param amount the initial intensity of the shake (recommended: 0.1 to 1.0)
     */

    public static void triggerShake(double amount) {
        intensity = amount;
        applyRandomOffset();
    }

    //Called every tick to gradually reduce the shake effect
    public static void tick() {
        // Gradually reduce the shake
        offset = offset.multiply(decay);
        intensity *= decay;

        // If it's basically gone, reset
        if (intensity < 0.01) {
            offset = Vec3d.ZERO;
            intensity = 0.0;
        } else {
            applyRandomOffset();
        }
    }

    // Returns the current shake offset to apply to the camera
    public static Vec3d getOffset() {
        return offset;
    }

    private static void applyRandomOffset() {
        offset = new Vec3d(
                (Math.random() - 0.5) * intensity,
                (Math.random() - 0.5) * intensity,
                (Math.random() - 0.5) * intensity
        );
    }
}
