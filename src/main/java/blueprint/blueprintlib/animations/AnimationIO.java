package blueprint.blueprintlib.animations;

import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AnimationIO {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path ANIMATION_DIR = Paths.get("config", "extraction", "animations");

    public static void saveAnimation(String name, List<ArmPose> poses, int ticksPerPose) {
        try {
            if (!Files.exists(ANIMATION_DIR)) {
                Files.createDirectories(ANIMATION_DIR);
            }
            JsonObject root = new JsonObject();
            root.addProperty("ticksPerPose", ticksPerPose);

            JsonArray poseArray = new JsonArray();
            for (ArmPose pose : poses) {
                poseArray.add(pose.toJson());
            }
            root.add("poses", poseArray);

            Path file = ANIMATION_DIR.resolve(name + ".json");
            try (FileWriter writer = new FileWriter(file.toFile())) {
                gson.toJson(root, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArmAnimation loadAnimation(String name) {
        Path file = ANIMATION_DIR.resolve(name + ".json");
        if (!Files.exists(file)) {
            System.out.println("Animation file does not exist " + file);
            return null;
        }

        try (FileReader reader = new FileReader(file.toFile())) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            int ticksPerPose = root.get("ticksPerPose").getAsInt();
            JsonArray poseArray = root.getAsJsonArray("poses");

            List<ArmPose> poses = new ArrayList<>();
            for (JsonElement element : poseArray) {
                poses.add(ArmPose.fromJson(element.getAsJsonObject()));
            }

            return new ArmAnimation(poses, ticksPerPose);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
