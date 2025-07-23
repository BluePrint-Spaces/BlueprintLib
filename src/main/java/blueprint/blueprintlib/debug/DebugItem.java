package blueprint.blueprintlib.debug;
import blueprint.blueprintlib.effects.CircleEffect;
import blueprint.blueprintlib.effects.ScanSweepEffect;
import blueprint.blueprintlib.effects.TeslaArcEffect;
import blueprint.blueprintlib.unused.BeamHandler;
import blueprint.blueprintlib.util.*;
import blueprint.blueprintlib.util.packets.VisualEffectPacket;
import blueprint.blueprintlib.util.packets.VisualEffectType;
import blueprint.blueprintlib.util.packets.network.VisualEffectNetwork;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class DebugItem extends Item {
    // Track the current mode using NBT
    private static final String MODE_KEY = "DebugMode";

    public DebugItem(Settings settings) {
        super(settings);
    }

    private DebugMode getMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return DebugMode.values()[nbt.getInt(MODE_KEY) % DebugMode.values().length];
    }

    private void setMode(ItemStack stack, DebugMode mode) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(MODE_KEY, mode.ordinal());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.isSneaking()) {
            // Shift right-click: cycle mode
            if (!world.isClient) {
                DebugMode newMode = getMode(stack).next();
                setMode(stack, newMode);
                player.sendMessage(Text.literal("§6Switched to debug mode: §e" + newMode.name()), true);
            }
            return TypedActionResult.success(stack, world.isClient());
        } else {
            // Regular right-click: Perform debug action
            if (!world.isClient) {
                performDebugAction(world, player, stack);
            }
            TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.pass(stack);
    }

    private void performDebugAction(World world, PlayerEntity player, ItemStack stack) {
        DebugMode mode = getMode(stack);

        switch (mode) {
            case TINT_TEST -> {
                VisualEffectHandler.triggerTint(0x00FFFF, 0.5f, 40);
                player.sendMessage(Text.literal("§aShowing cyan tint!"), true);
            }
            case SCREEN_SHAKE -> {
                // Trigger a screen shake with moderate intensity
                ScreenShakeHandler.triggerShake(1.0);
                player.sendMessage(Text.literal("§bShaking Screen"), true);
            }
            case SERVER_TEST_1 -> {
                ServerWorld serverWorld = (ServerWorld) player.getWorld();
                Vec3d pos = player.getPos();

                VisualEffectPacket packet = new VisualEffectPacket(
                        VisualEffectType.SCREEN_SHAKE,
                        pos.x, pos.y, pos.z,
                        0.3f, // intensity
                        20,   // duration (ticks) - ignored by shake but keep consistent
                        0,    // color - unused for shake
                        0f);    // alpha - unused for shake

                VisualEffectNetwork.sendEffectToNearby(serverWorld, pos, 20, packet);
                player.sendMessage(Text.literal("§cServer Test!"), true);
            }
            case PULSE_TEST -> {
                CircleEffectHandler.spawn(new CircleEffect(
                        player.getPos(),
                        0f,
                        10f,
                        100,
                        new Color(0, 255, 255),
                        true,
                        true,
                        10f
                ));
                player.sendMessage(Text.literal("§dSpawned Pulse"), true);
            }
            case SCAN_TEST -> {
                ScanSweepHandler.add(new ScanSweepEffect(
                        MinecraftClient.getInstance().player.getPos(),
                        4.0f,
                        new Color(0, 255, 255) // cyan
                ));
                player.sendMessage(Text.literal("§eScanner Test!"), true);
            }
            case TESLA_TEST -> {
                TeslaArcHandler.add(new TeslaArcEffect(
                        new Vec3d(player.getX(), player.getY() + 1.5, player.getZ()),
                        player.getRotationVec(1.0f),
                        3.5f,          // length
                        0.6f,          // max width jitter
                        Color.CYAN,
                        12,            // segments
                        5,             // update interval
                        true,          // loop
                        40,            // lifetime if not looping
                        10,            // strand count
                        1,             // spacing
                        0.05f              // line thickness
                ));
            }
        }
    }

    public enum DebugMode {
        TINT_TEST,
        SCREEN_SHAKE,
        SERVER_TEST_1,
        PULSE_TEST,
        SCAN_TEST,
        TESLA_TEST;

        // Cycle to next
        public DebugMode next() {
            DebugMode[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }
}
