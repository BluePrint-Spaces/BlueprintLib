package blueprint.blueprintlib.animations;

import com.google.gson.JsonObject;
import net.minecraft.util.math.MathHelper;

public class ArmPose {
    public float rightArmPitch, rightArmYaw, rightArmRoll;
    public float rightArmX, rightArmY, rightArmZ;
    public float leftArmPitch, leftArmYaw, leftArmRoll;
    public float leftArmX, leftArmY, leftArmZ;

    public ArmPose(float rp, float ry, float rr, float rx, float ry2, float rz,
                   float lp, float ly, float lr, float lx, float ly2, float lz) {
        this.rightArmPitch = rp;
        this.rightArmYaw = ry;
        this.rightArmRoll = rr;
        this.rightArmX = rx;
        this.rightArmY = ry2;
        this.rightArmZ = rz;
        this.leftArmPitch = lp;
        this.leftArmYaw = ly;
        this.leftArmRoll = lr;
        this.leftArmX = lx;
        this.leftArmY = ly2;
        this.leftArmZ = lz;
    }

    public static ArmPose lerp(ArmPose a, ArmPose b, float t) {
        return new ArmPose(
                MathHelper.lerp(t, a.rightArmPitch, b.rightArmPitch),
                MathHelper.lerp(t, a.rightArmYaw, b.rightArmYaw),
                MathHelper.lerp(t, a.rightArmRoll, b.rightArmRoll),
                MathHelper.lerp(t, a.rightArmX, b.rightArmX),
                MathHelper.lerp(t, a.rightArmY, b.rightArmY),
                MathHelper.lerp(t, a.rightArmZ, b.rightArmZ),

                MathHelper.lerp(t, a.leftArmPitch, b.leftArmPitch),
                MathHelper.lerp(t, a.leftArmYaw, b.leftArmYaw),
                MathHelper.lerp(t, a.leftArmRoll, b.leftArmRoll),
                MathHelper.lerp(t, a.leftArmX, b.leftArmX),
                MathHelper.lerp(t, a.leftArmY, b.leftArmY),
                MathHelper.lerp(t, a.leftArmZ, b.leftArmZ)
        );
    }

    public static ArmPose fromJson(JsonObject json) {
        return new ArmPose(
                json.get("rightArmPitch").getAsFloat(),
                json.get("rightArmYaw").getAsFloat(),
                json.get("rightArmRoll").getAsFloat(),
                json.get("rightArmX").getAsFloat(),
                json.get("rightArmY").getAsFloat(),
                json.get("rightArmZ").getAsFloat(),

                json.get("leftArmPitch").getAsFloat(),
                json.get("leftArmYaw").getAsFloat(),
                json.get("leftArmRoll").getAsFloat(),
                json.get("leftArmX").getAsFloat(),
                json.get("leftArmY").getAsFloat(),
                json.get("leftArmZ").getAsFloat()
        );
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("rightArmPitch", rightArmPitch);
        json.addProperty("rightArmYaw", rightArmYaw);
        json.addProperty("rightArmRoll", rightArmRoll);
        json.addProperty("rightArmX", rightArmX);
        json.addProperty("rightArmY", rightArmY);
        json.addProperty("rightArmZ", rightArmZ);

        json.addProperty("leftArmPitch", leftArmPitch);
        json.addProperty("leftArmYaw", leftArmYaw);
        json.addProperty("leftArmRoll", leftArmRoll);
        json.addProperty("leftArmX", leftArmX);
        json.addProperty("leftArmY", leftArmY);
        json.addProperty("leftArmZ", leftArmZ);

        return json;
    }
}