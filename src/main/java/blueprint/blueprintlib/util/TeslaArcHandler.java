package blueprint.blueprintlib.util;

import blueprint.blueprintlib.effects.TeslaArcEffect;
import blueprint.blueprintlib.util.packets.VisualEffectPacket;
import blueprint.blueprintlib.util.packets.VisualEffectType;
import blueprint.blueprintlib.util.packets.network.VisualEffectNetwork;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeslaArcHandler {
    private static final List<TeslaArcEffect> effects = new CopyOnWriteArrayList<>();

    public static void tick() {
        effects.removeIf(TeslaArcEffect::isDead);
        effects.forEach(TeslaArcEffect::tick);
    }

    public static void clientTick() {
        effects.forEach(TeslaArcEffect::tick);
    }

    public static void tick(MinecraftServer server) {
        effects.removeIf(TeslaArcEffect::isDead);
        effects.forEach(TeslaArcEffect::tick);

        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.isInvulnerable()) continue;

                Vec3d playerPos = player.getPos();

                for (TeslaArcEffect arc : effects) {
                    List<List<Vec3d>> strands = arc.getStrands();
                    if (strands == null) continue;

                    outer:
                    for (List<Vec3d> strand : strands) {
                        if (strand.size() < 2) continue;

                        for (int i = 0; i < strand.size() - 1; i++) {
                            Vec3d p1 = strand.get(i);
                            Vec3d p2 = strand.get(i + 1);

                            if (isPlayerNearSegment(player, p1, p2, 0.6)) {
                                player.damage(world.getDamageSources().lightningBolt(), 2.0f);

                                VisualEffectPacket shakePacket = new VisualEffectPacket(
                                        VisualEffectType.SCREEN_SHAKE,
                                        player.getX(), player.getY(), player.getZ(),
                                        0.3f, 20, 0, 0f
                                );
                                VisualEffectNetwork.sendEffectToNearby(world, player.getPos(), 16, shakePacket);

                                Vec3d closest = closestPointOnLine(p1, p2, player.getPos());
                                Vec3d direction = player.getPos().subtract(closest);
                                if (direction.lengthSquared() > 0.0001) {
                                    direction = direction.normalize();
                                } else {
                                    direction = new Vec3d(0, 0, 0);
                                }

                                double knockbackStrength = 0.20;
                                double verticalBoost = 0.04;

                                Vec3d knockback = new Vec3d(
                                        direction.x * knockbackStrength,
                                        verticalBoost,
                                        direction.z * knockbackStrength
                                );

                                player.addVelocity(knockback.x, knockback.y, knockback.z);
                                player.velocityModified = true;

                                world.spawnParticles(
                                        ParticleTypes.ELECTRIC_SPARK,
                                        player.getX(), player.getY() + 1, player.getZ(),
                                        10, 0.3, 0.4, 0.3, 0.02
                                );
                                break outer;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isPlayerNearSegment(ServerPlayerEntity player, Vec3d p1, Vec3d p2, double maxDistance) {
        Vec3d closest = closestPointOnLine(p1, p2, player.getPos());
        return closest.distanceTo(player.getPos()) < maxDistance;
    }

    private static Vec3d closestPointOnLine(Vec3d a, Vec3d b, Vec3d point) {
        Vec3d ab = b.subtract(a);
        double t = point.subtract(a).dotProduct(ab) / ab.lengthSquared();
        t = Math.max(0, Math.min(1, t));
        return a.add(ab.multiply(t));
    }

    public static List<TeslaArcEffect> getEffects() {
        return effects;
    }

    public static void clear() {
        effects.clear();
    }

    public static void add(TeslaArcEffect effect) {
        effects.add(effect);
    }
}
