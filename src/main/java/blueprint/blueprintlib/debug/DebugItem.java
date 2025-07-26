package blueprint.blueprintlib.debug;

import blueprint.blueprintlib.effects.CircleEffect;
import blueprint.blueprintlib.effects.ScanSweepEffect;
import blueprint.blueprintlib.effects.TeslaArcEffect;
import blueprint.blueprintlib.util.*;
import blueprint.blueprintlib.util.packets.VisualEffectPacket;
import blueprint.blueprintlib.util.packets.VisualEffectType;
import blueprint.blueprintlib.util.packets.network.VisualEffectNetwork;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class DebugItem extends Item {
    private static final String MODE_KEY = "DebugMode";
    private static final String CURRENT_POS1_KEY = "CurrentArcPos1";
    private static final String ARC_PAIRS_KEY = "ArcPairs";

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
            case CLEAR_EFFECTS -> {
                EffectManager.clearAllEffects();
                stack.getOrCreateNbt().remove(CURRENT_POS1_KEY);
                stack.getOrCreateNbt().remove(ARC_PAIRS_KEY);
                player.sendMessage(Text.literal("§cCleared all effects."), true);
            }
            case TESLA_TEST -> {
                NbtCompound tag = stack.getOrCreateNbt();
                NbtList pairList = tag.getList(ARC_PAIRS_KEY, NbtElement.COMPOUND_TYPE);

                if (pairList.isEmpty()) {
                    player.sendMessage(Text.literal("§cNo arc pairs stored! Right-click two blocks to create one."), true);
                    return;
                }

                for (int i = 0; i < pairList.size(); i++) {
                    NbtCompound pair = pairList.getCompound(i);
                    Vec3d pos1 = fromNbt(pair.getCompound("pos1"));
                    Vec3d pos2 = fromNbt(pair.getCompound("pos2"));
                    Vec3d dir = pos2.subtract(pos1).normalize();
                    float length = (float) pos1.distanceTo(pos2);

                    TeslaArcHandler.add(new TeslaArcEffect(
                            pos1, dir, length,
                            0.6f,
                            Color.CYAN,
                            12,
                            5,
                            true,
                            40,
                            10,
                            1,
                            0.05f
                    ));
                }
                player.sendMessage(Text.literal("§aSpawned " + pairList.size() + " Tesla arcs."), true);
            }
        }
    }

    public enum DebugMode {
        TINT_TEST,
        SCREEN_SHAKE,
        SERVER_TEST_1,
        PULSE_TEST,
        SCAN_TEST,
        TESLA_TEST,
        CLEAR_EFFECTS;

        // Cycle to next
        public DebugMode next() {
            DebugMode[] values = values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }



    private NbtCompound toNbt(Vec3d pos) {
        NbtCompound tag = new NbtCompound();
        tag.putDouble("x", pos.x);
        tag.putDouble("y", pos.y);
        tag.putDouble("z", pos.z);
        return tag;
    }

    private Vec3d fromNbt(NbtCompound tag) {
        return new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        Vec3d hitPos = context.getHitPos();
        if (player == null || player.getWorld().isClient) return ActionResult.SUCCESS;

        NbtCompound tag = stack.getOrCreateNbt();

        if (!tag.contains(CURRENT_POS1_KEY)) {
            tag.put(CURRENT_POS1_KEY, toNbt(hitPos));
            player.sendMessage(Text.literal("§aSet §ePos1 §afor new arc at " + formatVec(hitPos)), true);
        } else {
            Vec3d pos1 = fromNbt(tag.getCompound(CURRENT_POS1_KEY));
            Vec3d pos2 = hitPos;

            NbtList pairList = tag.getList(ARC_PAIRS_KEY, NbtElement.COMPOUND_TYPE);
            NbtCompound pairTag = new NbtCompound();
            pairTag.put("pos1", toNbt(pos1));
            pairTag.put("pos2", toNbt(pos2));
            pairList.add(pairTag);
            tag.put(ARC_PAIRS_KEY, pairList);

            tag.remove(CURRENT_POS1_KEY);

            player.sendMessage(Text.literal("§bStored arc from " + formatVec(pos1) + " to " + formatVec(pos2)), true);
        }

        return ActionResult.SUCCESS;
    }

    private String formatVec(Vec3d vec) {
        return String.format("(%.1f, %.1f, %.1f)", vec.x, vec.y, vec.z);
    }
}
