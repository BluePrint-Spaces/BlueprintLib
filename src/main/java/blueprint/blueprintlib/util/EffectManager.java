package blueprint.blueprintlib.util;

public class EffectManager {
    public static void clearAllEffects() {
        TeslaArcHandler.clear();
        ScanSweepHandler.clear();
        CircleEffectHandler.clear();
    }
}
