package blueprint.blueprintlib.animations;

import blueprint.blueprintlib.util.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PoseManager {
    public static final List<ArmPose> savedPoses = new ArrayList<>();

    public static void saveCurrentPose() {
        ArmPose pose = new ArmPose(
                ModConfig.rightArmPitch,
                ModConfig.rightArmYaw,
                ModConfig.rightArmRoll,
                (float) ModConfig.rightArmOffsetX,
                (float) ModConfig.rightArmOffsetY,
                (float) ModConfig.rightArmOffsetZ,

                ModConfig.leftArmPitch,
                ModConfig.leftArmYaw,
                ModConfig.leftArmRoll,
                (float) ModConfig.leftArmOffsetX,
                (float) ModConfig.leftArmOffsetY,
                (float) ModConfig.leftArmOffsetZ
        );
        savedPoses.add(pose);

        MinecraftClient.getInstance().player.sendMessage(Text.literal("Saved pose #" + savedPoses.size()), true);
    }

    public static void playSavedAnimation(MinecraftClient client) {
        if (savedPoses.isEmpty()) {
            client.player.sendMessage(Text.literal("§cNo saved poses to play!"), true);
            return;
        }

        ArmAnimation animation = new ArmAnimation(savedPoses, 10); // Play each pose for 10 ticks
        AnimationManager.startAnimation(client, animation);

        client.player.sendMessage(Text.literal("§bPlaying saved animation (" + savedPoses.size() + " poses)"), true);
    }

    public static void clearSavedPoses() {
        savedPoses.clear();
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Cleared all saved poses"), true);
    }


    public static void saveCurrentAnimationToFile(String name, int ticksPerPose) {
        if (savedPoses.isEmpty()) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("§cNo poses to save!"), true);
            return;
        }

        AnimationIO.saveAnimation(name, savedPoses, ticksPerPose);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("§aSaved animation to file: " + name + ".json"), true);
    }

    public static void loadAnimationFromFile(MinecraftClient client, String name) {
        ArmAnimation animation = AnimationIO.loadAnimation(name);
        if (animation == null) {
            client.player.sendMessage(Text.literal("§cFailed to load animation!"), true);
            return;
        }

        AnimationManager.startAnimation(client, animation);
        client.player.sendMessage(Text.literal("§bLoaded and playing animation: " + name), true);
    }
}
