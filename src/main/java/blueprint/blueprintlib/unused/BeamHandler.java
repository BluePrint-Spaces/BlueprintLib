package blueprint.blueprintlib.unused;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BeamHandler {
    private static final List<BeamEffect> effects = new ArrayList<>();

    public static void spawnBeam(World world, Vec3d start, Vec3d end, Color color, float width, int duration) {
        if (world.isClient()) {
            effects.add(new BeamEffect(start, end, color, width, duration));
        }
    }

    public static List<BeamEffect> getEffects() {
        return effects;
    }

    public static void tick() {
        effects.removeIf(effect -> {
            effect.tick();
            return effect.isDead();
        });
    }
}
