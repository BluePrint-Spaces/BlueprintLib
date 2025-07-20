package blueprint.blueprintlib.unused;

import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PulseHandler {
    private static final List<PulseEffect> ACTIVE = new ArrayList<>();

    public static void add(Vec3d pos, float radius, int duration, Color color) {
        ACTIVE.add(new PulseEffect(pos, radius, duration, color));
    }

    public static void tick() {
        ACTIVE.removeIf(e -> {
            e.tick();
            return !e.isAlive();
        });
    }

    public static List<PulseEffect> getEffects() {
        return ACTIVE;
    }
}
