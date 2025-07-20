package blueprint.blueprintlib.unused;

import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class PulseEffect {
    public final Vec3d position;
    public final int duration;
    public final float maxRadius;
    public final Color color;

    private int age = 0;

    public PulseEffect(Vec3d position, float maxRadius, int duration,  Color color) {
        this.position = position;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.color = color;
    }

    public void tick() {
        age++;
    }

    public boolean isAlive() {
        return age < duration;
    }

    public float getProgress() {
        return (float) age / duration;
    }

    public float getCurrentRadius() {
        return maxRadius * getProgress();
    }

    public float getAlpha() {
        return 255.0f - getProgress(); // Fade out
    }
}
