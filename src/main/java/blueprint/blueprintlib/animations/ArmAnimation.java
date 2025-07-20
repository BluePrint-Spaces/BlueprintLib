package blueprint.blueprintlib.animations;

import java.util.ArrayList;
import java.util.List;

public class ArmAnimation {
    public List<ArmPose> poses = new ArrayList<>();
    public int ticksPerPose = 5;

    public ArmAnimation(List<ArmPose> poses, int ticksPerPose) {
        this.poses = poses;
        this.ticksPerPose = ticksPerPose;
    }
}
