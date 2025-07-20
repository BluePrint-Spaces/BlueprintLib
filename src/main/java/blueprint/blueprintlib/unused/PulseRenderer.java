package blueprint.blueprintlib.unused;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class PulseRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();

            Vec3d camPos = camera.getPos();

            PulseHandler.getEffects().forEach(effect -> {
                Vec3d pos = effect.position.subtract(camPos);
                float radius = effect.getCurrentRadius();
                float alpha = effect.getAlpha();

                matrices.push();
                matrices.translate(pos.x, pos.y + 0.1, pos.z); // Slightly above ground
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(0)); // Face upward

                RenderSystem.enableBlend();
                RenderSystem.disableCull();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

                int segments = 60;
                float r = effect.color.getRed() / 255f;
                float g = effect.color.getGreen() / 255f;
                float b = effect.color.getBlue() / 255f;

                // Center vertex
                buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0)
                        .color(r, g, b, alpha).next();

                // Outer ring vertices
                for (int i = 0; i <= segments; i++) {
                    double angle = 2 * Math.PI * i / segments;
                    float x = (float) (Math.cos(angle) * radius);
                    float z = (float) (Math.sin(angle) * radius);
                    buffer.vertex(matrices.peek().getPositionMatrix(), x, 0, z)
                            .color(r, g, b, 0f).next(); // Fades out
                }

                tessellator.draw();

                RenderSystem.disableBlend();
                matrices.pop();
            });
        });
    }
}