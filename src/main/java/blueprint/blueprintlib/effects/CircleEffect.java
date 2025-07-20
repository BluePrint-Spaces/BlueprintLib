package blueprint.blueprintlib.effects;

import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class CircleEffect {
    public Vec3d position;
    public float radius;
    public float maxRadius;
    public float alpha;
    public int age;
    public int lifespan;
    public Color color;
    public boolean expand;
    public boolean spin;
    public float rotation; // In degrees
    public float rotationSpeed; // Degrees per tick

    public CircleEffect(Vec3d position, float startRadius, float maxRadius, int lifespan, Color color, boolean expand, boolean spin, float rotationSpeed) {
        this.position = position;
        this.radius = startRadius;
        this.maxRadius = maxRadius;
        this.alpha = 1;
        this.age = 0;
        this.lifespan = lifespan;
        this.color = color;
        this.expand = expand;
        this.spin = spin;
        this.rotation = 0;
        this.rotationSpeed = rotationSpeed;
    }

    public void tick() {
        age++;
        if (expand) {
            radius = Math.min(maxRadius, radius + (maxRadius / lifespan));
        }
        alpha = 1.0f - (age / (float) lifespan);
        if (spin) {
            rotation += rotationSpeed;
        }
    }

    public boolean isAlive() {
        return age < lifespan;
    }
}
