package blueprint.blueprintlib.util.packets.network;

import blueprint.blueprintlib.util.ScreenShakeHandler;
import blueprint.blueprintlib.util.VisualEffectHandler;
import blueprint.blueprintlib.util.packets.VisualEffectPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.Vec3d;

public class VisualEffectClientNetwork {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(VisualEffectPacket.ID, (client, handler, buf, responseSender) -> {
            VisualEffectPacket packet = VisualEffectPacket.read(buf);

            client.execute(() -> {
                if (client.player == null) return;

                Vec3d playerPos = client.player.getPos();
                Vec3d effectPos = new Vec3d(packet.x, packet.y, packet.z);
                double distSq = playerPos.squaredDistanceTo(effectPos);
                if (distSq > 64 * 64) return; // Additional client-side check

                switch (packet.type) {
                    case SCREEN_SHAKE -> ScreenShakeHandler.triggerShake(packet.intensity);
                    case SCREEN_TINT -> VisualEffectHandler.triggerTint(packet.color, packet.alpha, packet.duration);
                }
            });
        });
    }
}
