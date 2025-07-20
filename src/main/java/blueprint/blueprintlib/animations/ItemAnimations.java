package blueprint.blueprintlib.animations;

import java.util.List;

public class ItemAnimations {

    public static ArmAnimation createBandageAnimation() {
        List<ArmPose> poses = List.of(
                new ArmPose(
                        0f, 0f, 0f, 0f, 0f, 0f,
                        0f, 0f, 0f, 0f, 0f, 0f
                ),
                new ArmPose(
                        (float) Math.toRadians(-60), (float) Math.toRadians(10), 0f, 0f, 0f, 0f,
                        0f, 0f, 0f, 0f, 0f, 0f
                ),
                new ArmPose(
                        (float) Math.toRadians(-60), (float) Math.toRadians(10), 0f, 0f, 0f, 0f,
                        (float) Math.toRadians(-30), (float) Math.toRadians(-30), (float) Math.toRadians(20), 0f, 0f, 0f
                ),
                new ArmPose(
                        (float) Math.toRadians(-60), (float) Math.toRadians(10), 0f, 0f, 0f, 0f,
                        (float) Math.toRadians(-40), (float) Math.toRadians(-20), (float) Math.toRadians(10), 0f, 0f, 0f
                ),
                new ArmPose(
                        (float) Math.toRadians(-60), (float) Math.toRadians(10), 0f, 0f, 0f, 0f,
                        (float) Math.toRadians(-30), (float) Math.toRadians(-30), (float) Math.toRadians(20), 0f, 0f, 0f
                ),
                new ArmPose(
                        0f, 0f, 0f, 0f, 0f, 0f,
                        0f, 0f, 0f, 0f, 0f, 0f
                )
        );

        return new ArmAnimation(poses, 8);
    }
}
