package blueprint.blueprintlib;

import eu.midnightdust.lib.config.MidnightConfig;

public class ModConfig extends MidnightConfig {

    @Entry
    public static boolean disableNametags = true; // Default value


    @Entry(min = 0, max = 20)
    public static int bandageHealAmount = 4;
    @Entry(min = 0, max = 40)
    public static int medkitHealAmount = 8;


    @Entry
    public static boolean armPosingEnabled = false;

    // Right Arm Rotation
    @Entry(min = -180, max = 180)
    public static int rightArmPitch = 0;
    @Entry(min = -180, max = 180)
    public static int rightArmYaw = 0;
    @Entry(min = -180, max = 180)
    public static int rightArmRoll = 0;

    // Right Arm Position
    @Entry(min = -20, max = 20)
    public static double rightArmOffsetX = 0.0f; // Default Minecraft: -5.0
    @Entry(min = -20, max = 20)
    public static double rightArmOffsetY = 0.0f; // Default Minecraft: 2.0
    @Entry(min = -20, max = 20)
    public static double rightArmOffsetZ = 0.0f;


    // Left Arm Rotation
    @Entry(min = -180, max = 180)
    public static int leftArmPitch = 0;
    @Entry(min = -180, max = 180)
    public static int leftArmYaw = 0;
    @Entry(min = -180, max = 180)
    public static int leftArmRoll = 0;

    // Left Arm Position
    @Entry(min = -20, max = 20)
    public static double leftArmOffsetX = 0.0f; // Default Minecraft: 5.0
    @Entry(min = -20, max = 20)
    public static double leftArmOffsetY = 0.0f; // Default Minecraft: 2.0
    @Entry(min = -20, max = 20)
    public static double leftArmOffsetZ = 0.0f;


}
