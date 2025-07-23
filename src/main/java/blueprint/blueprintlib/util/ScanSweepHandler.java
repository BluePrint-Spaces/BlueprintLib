package blueprint.blueprintlib.util;

import blueprint.blueprintlib.effects.ScanSweepEffect;

import java.util.ArrayList;
import java.util.List;

public class ScanSweepHandler {
    private static final List<ScanSweepEffect> effects = new ArrayList<>();

    public static void add(ScanSweepEffect effect) {
        effects.add(effect);
    }

    public static List<ScanSweepEffect> getEffects() {
        return effects;
    }

    public static void tick(float deltaTime) {
        effects.removeIf(effect -> {
            effect.tick();
            return effect.isDead();
        });
    }

    public static void clear() {
        effects.clear();
    }
}
