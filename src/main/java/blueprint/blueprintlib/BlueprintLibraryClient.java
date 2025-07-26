package blueprint.blueprintlib;

import blueprint.blueprintlib.effects.renderer.ScanSweepRenderer;
import blueprint.blueprintlib.effects.renderer.TeslaArcRenderer;
import blueprint.blueprintlib.shaders.ScreenQuadRenderer;
import blueprint.blueprintlib.shaders.WorldQuadRenderer;
import blueprint.blueprintlib.shaders.shape.ShapeFactory;
import blueprint.blueprintlib.unused.BeamRenderer;
import blueprint.blueprintlib.effects.renderer.CircleRenderer;
import blueprint.blueprintlib.unused.BeamHandler;
import blueprint.blueprintlib.util.*;
import blueprint.blueprintlib.util.packets.network.VisualEffectClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlueprintLibraryClient implements ClientModInitializer {
//    private static ScreenQuadRenderer testRenderer;
//    private static WorldQuadRenderer worldTestRenderer;

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

//        String vertexPath = "shaders/core/example.vert";
//        String fragmentPath = "shaders/core/example.frag";
//
//
//            WorldRenderEvents.AFTER_ENTITIES.register(ctx -> {
//                if (testRenderer == null) {
//                    testRenderer = new ScreenQuadRenderer("blueprintlib",vertexPath, fragmentPath);
//                }
//                testRenderer.render(ctx.tickDelta());

//                HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
//                    testRenderer.render(tickDelta); // purely screen overlay, does NOT affect world
//                });
//        });


//        WorldRenderEvents.AFTER_TRANSLUCENT.register(ctx -> {
//            float[] portalQuad = ShapeFactory.createSphere(1.2f, 32, 16);
//            if (test2 == null) {
//                test2 = new WorldQuadRenderer("blueprintlib", "shaders/post/example.vert", "shaders/post/example.frag", portalQuad, 3);
//            }
//
//            Vec3d renderPos = new Vec3d(392, 65, 519);
//            test2.render(ctx.matrixStack(), renderPos, ctx.tickDelta());
//        });
    }
}