package blueprint.blueprintlib.util.packets.network;

import blueprint.blueprintlib.util.packets.VisualEffectPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class VisualEffectNetwork {
    public static void sendEffectToNearby(ServerWorld world, Vec3d pos, double radius, VisualEffectPacket effectPacket) {
        PacketByteBuf buf = PacketByteBufs.create();
        effectPacket.write(buf);

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.getPos().squaredDistanceTo(pos) <= radius * radius) {
                ServerPlayNetworking.send(player, VisualEffectPacket.ID, buf);
            }
        }
    }
}
