package blueprint.blueprintlib.util;

import blueprint.blueprintlib.effects.TeslaArcEffect;

import java.util.ArrayList;
import java.util.List;

public class TeslaArcHandler {
    private static final List<TeslaArcEffect> effects = new ArrayList<>();

    public static void add(TeslaArcEffect effect) {
        effects.add(effect);
    }

    public static List<TeslaArcEffect> getEffects() {
        return effects;
    }

    public static void tick() {
        effects.removeIf(TeslaArcEffect::isDead);
        effects.forEach(TeslaArcEffect::tick);
    }

    public static void clear() {
        effects.clear();
    }
}
