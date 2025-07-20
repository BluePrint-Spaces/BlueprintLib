package blueprint.blueprintlib.animations;

public class SimpleAnimator {
    private final ArmAnimation animation;
    private int animationTick = 0;

    public SimpleAnimator(ArmAnimation animation) {
        this.animation = animation;
    }

    public ArmPose getCurrentPose() {
        int totalDuration = (animation.poses.size()) * animation.ticksPerPose;
        if (totalDuration == 0) return animation.poses.get(0);

        int currentPoseIndex = (animationTick / animation.ticksPerPose) % animation.poses.size();
        int nextPoseIndex = (currentPoseIndex + 1) % animation.poses.size();

        ArmPose currentPose = animation.poses.get(currentPoseIndex);
        ArmPose nextPose = animation.poses.get(nextPoseIndex);

        float t = (animationTick % animation.ticksPerPose) / (float) animation.ticksPerPose;

        // Apply smoothing to t
        t = smoothStep(t);

        return ArmPose.lerp(currentPose, nextPose, t);
    }

    public void tick() {
        animationTick++;
    }

    public int getTotalDuration() {
        return (animation.poses.size()) * animation.ticksPerPose;
    }

    private float smoothStep(float t) {
        return t * t * (3 - 2 * t); // Standard smoothstep function
    }
}