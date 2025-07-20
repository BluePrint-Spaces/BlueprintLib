package blueprint.blueprintlib.effects;

import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class ScanSweepEffect {
    public Vec3d position;
    public float radius;
    public float alpha = 1.0f;
    public boolean ringOnly = false;
    public Color color;

    private float angle = 0f; // current angle of the sweep in degrees
    public float speed = 2f;  // degrees per tick
    public boolean permanent = false;
    private int age = 0;

    public ScanSweepEffect(Vec3d position, float radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }

    public void tick() {
        angle = (angle + speed) % 360f;
        age++;
    }

    public float getAngle() {
        return angle;
    }

    public float getRadius() {
        return radius;
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isDead() {
        return !permanent && age > 100; // Example timeout
    }
}