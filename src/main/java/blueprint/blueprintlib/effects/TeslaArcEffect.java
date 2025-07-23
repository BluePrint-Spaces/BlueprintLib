package blueprint.blueprintlib.effects;

import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeslaArcEffect {
    public final Vec3d origin;
    public final Vec3d direction;
    public final float maxLength;
    public final float width;
    public final Color color;
    public final int segmentCount;
    public final int updateInterval;
    public final boolean loop;
    public final int lifetime;
    public final int strandCount;
    public final float strandSpacing;
    public final float lineThickness;

    private final List<List<Vec3d>> arcStrands = new ArrayList<>();
    private int age = 0;


    public TeslaArcEffect(Vec3d origin, Vec3d direction, float maxLength, float width, Color color,
                          int segmentCount, int updateInterval, boolean loop, int lifetime,
                          int strandCount, float strandSpacing, float lineThickness) {
        this.origin = origin;
        this.direction = direction;
        this.maxLength = maxLength;
        this.width = width;
        this.color = color;
        this.segmentCount = segmentCount;
        this.updateInterval = updateInterval;
        this.loop = loop;
        this.lifetime = lifetime;
        this.strandCount = strandCount;
        this.strandSpacing = strandSpacing;
        this.lineThickness = lineThickness;

        regenerateArc();
    }

    public void tick() {
        age++;
        if (loop && age % updateInterval == 0) {
            regenerateArc();
        }
    }

    public boolean isDead() {
        return !loop && age >= lifetime;
    }

    public List<List<Vec3d>> getStrands() {
        return arcStrands;
    }

    private void regenerateArc() {
        arcStrands.clear();
        for (int strand = 0; strand < strandCount; strand++) {
            List<Vec3d> points = new ArrayList<>();
            points.add(origin);
            for (int i = 1; i < segmentCount; i++) {
                float progress = i / (float) segmentCount;
                Vec3d base = origin.add(direction.multiply(progress * maxLength));
                double jitterX = (Math.random() - 0.5) * width;
                double jitterY = (Math.random() - 0.5) * width;
                points.add(base.add(jitterX, jitterY, 0));
            }
            points.add(origin.add(direction.multiply(maxLength)));
            arcStrands.add(points);
        }
    }
}
