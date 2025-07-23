package blueprint.blueprintlib.util;

import blueprint.blueprintlib.effects.CircleEffect;

import java.util.ArrayList;
import java.util.List;

public class CircleEffectHandler {
    private static final List<CircleEffect> effects = new ArrayList<>();

    public static void spawn(CircleEffect effect) {
        effects.add(effect);
    }

    public static List<CircleEffect> getEffects() {
        return effects;
    }

    public static void tick() {
        effects.removeIf(e -> !e.isAlive());
        effects.forEach(CircleEffect::tick);
    }

    public static void clear() {
        effects.clear();
    }
}
