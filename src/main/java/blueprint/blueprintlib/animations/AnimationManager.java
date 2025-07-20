package blueprint.blueprintlib.animations;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

public class AnimationManager {
    private static SimpleAnimator animator = null;
    private static int originalCameraMode = -1;
    private static int ticksRemaining = 0;

    public static void tick(MinecraftClient client) {
        if (animator == null) return;

        animator.tick();
        ticksRemaining--;

        if (ticksRemaining <= 0) {
            animator = null;
            if (originalCameraMode != -1) {
                client.options.setPerspective(Perspective.values()[originalCameraMode]);
                originalCameraMode = -1;
            }
        }
    }

    public static ArmPose getCurrentPose() {
        return animator != null ? animator.getCurrentPose() : null;
    }

    public static boolean isAnimating() {
        return animator != null;
    }


    public static void startAnimation(MinecraftClient client, ArmAnimation animation) {
        animator = new SimpleAnimator(animation);

        // Save current perspective
        originalCameraMode = client.options.getPerspective().ordinal();

        // Swtich to third person
        client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);

        ticksRemaining = animator.getTotalDuration() + 10;
    }


    // Old code for a test animation that didn't properly work to test a wave

//    public static void startWaveAnimation(MinecraftClient client) {
//        if (animator != null) return;
//
//        originalCameraMode = client.options.getPerspective().ordinal();
//
//        client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
//
//        animator = createWaveAnimator();
//        ticksRemaining = animator.getTotalDuration() + 10;
//    }

//    private static SimpleAnimator createWaveAnimator() {
//        List<ArmPose> poses = List.of(
//                new ArmPose(0, 0, 0, -5, 2, 0,
//                        0, 0, 0, 5, 2, 0),
//                new ArmPose(
//                        (float)Math.toRadians(-120), 0, (float)Math.toRadians(20), -5, 2, 0,
//                        0, 0, 0, 5, 2, 0
//                ),
//                new ArmPose(
//                        0, 0,0, -5, 2, 0,
//                        0, 0, 0, 5, 2,0
//                )
//        );
//        return new SimpleAnimator(new ArmAnimation(poses, 10));
//    }
}
