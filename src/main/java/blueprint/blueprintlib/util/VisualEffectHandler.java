package blueprint.blueprintlib.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;

public class VisualEffectHandler {
    private static int tintColor = 0x000000;
    private static float tintAlpha = 0f;
    private static int tintDuration = 0;
    private static int tintAge = 0;

    public static void triggerTint(int color, float alpha, int durationTicks) {
        tintColor = color;
        tintAlpha = alpha;
        tintDuration = durationTicks;
        tintAge = 0;
    }

    public static void tick() {
        if (tintAge < tintDuration) {
            tintAge++;
        }
    }

    public static void render(DrawContext context, float tickDelta) {
        if (tintAge >= tintDuration || tintAlpha <= 0) return;

        float progress = (float) tintAge / tintDuration;
        float alpha = tintAlpha * (1.0f - progress); // Fade out over time

        int r = ColorHelper.Argb.getRed(tintColor);
        int g = ColorHelper.Argb.getGreen(tintColor);
        int b = ColorHelper.Argb.getBlue(tintColor);
        int a = (int)(alpha * 255);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getWindow() == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        context.fill(0, 0, width, height, ColorHelper.Argb.getArgb(a, r, g, b));
    }

    public static void initClient() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> render(context, tickDelta));
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
    }
}
