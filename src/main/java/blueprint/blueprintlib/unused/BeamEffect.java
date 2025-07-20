package blueprint.blueprintlib.unused;

import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class BeamEffect {
    public final Vec3d start;
    public final Vec3d end;
    public final Color color;
    public final float width;
    public final int duration;
    public int age = 0;

    public BeamEffect(Vec3d start, Vec3d end, Color color, float width, int duration) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.width = width;
        this.duration = duration;
    }

    public boolean isDead() {
        return age >= duration;
    }

    public void tick() {
        age++;
    }

    public float getAlpha() {
        return 1.0f - (float) age / duration;
    }
}
