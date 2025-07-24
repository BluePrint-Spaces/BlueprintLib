package blueprint.blueprintlib;

import blueprint.blueprintlib.effects.TeslaArcEffect;
import blueprint.blueprintlib.effects.renderer.ScanSweepRenderer;
import blueprint.blueprintlib.effects.renderer.TeslaArcRenderer;
import blueprint.blueprintlib.unused.BeamRenderer;
import blueprint.blueprintlib.effects.renderer.CircleRenderer;
import blueprint.blueprintlib.unused.BeamHandler;
import blueprint.blueprintlib.util.*;
import blueprint.blueprintlib.util.packets.network.VisualEffectClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BlueprintLibraryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VisualEffectHandler.initClient();
        VisualEffectClientNetwork.register();

        CircleRenderer.init();
        BeamRenderer.init();
        ScanSweepRenderer.init();
        TeslaArcRenderer.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                ScreenShakeHandler.tick();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                CircleEffectHandler.tick();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                BeamHandler.tick(); // Advance all active beams
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                ScanSweepHandler.tick(client.getTickDelta());
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                TeslaArcHandler.clientTick();
            }
        });
    }
}
