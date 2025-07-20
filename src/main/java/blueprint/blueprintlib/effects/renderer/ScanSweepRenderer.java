package blueprint.blueprintlib.effects.renderer;

import blueprint.blueprintlib.effects.ScanSweepEffect;
import blueprint.blueprintlib.util.ScanSweepHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class ScanSweepRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = camera.getPos();

            for (ScanSweepEffect effect : ScanSweepHandler.getEffects()) {
                Vec3d pos = effect.position.subtract(camPos);
                float radius = effect.getRadius();
                float alpha = effect.getAlpha();

                float r = effect.color.getRed() / 255f;
                float g = effect.color.getGreen() / 255f;
                float b = effect.color.getBlue() / 255f;

                matrices.push();
                matrices.translate(pos.x, pos.y + 0.01, pos.z); // Slightly above the ground
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90)); // Flat on XZ plane

                RenderSystem.enableBlend();
                RenderSystem.disableCull();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);

                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

                Matrix4f matrix = matrices.peek().getPositionMatrix();

                if (!effect.ringOnly) {
                    // Triangle fan rotating sweep
                    buffer.vertex(matrix, 0, 0, 0).color(r, g, b, alpha).next();

                    int segments = 60;
                    float sweepAngle = 30f; // Width of the sweep in degrees
                    float startAngle = effect.getAngle() - (sweepAngle / 2f); // Rotate over time

                    for (int i = 0; i <= segments; i++) {
                        float t = i / (float) segments;
                        double angle = Math.toRadians(startAngle + (sweepAngle * t));
                        float x = (float) (Math.cos(angle) * radius);
                        float z = (float) (Math.sin(angle) * radius);
                        buffer.vertex(matrix, x, 0, z).color(r, g, b, 0f).next();
                    }
                } else {
                    // Static ring (unchanged)
                    int segments = 60;
                    float ringWidth = radius * 0.05f;
                    float inner = radius - ringWidth;
                    for (int i = 0; i <= segments; i++) {
                        double angle = 2 * Math.PI * i / segments;
                        float xOuter = (float) (Math.cos(angle) * radius);
                        float zOuter = (float) (Math.sin(angle) * radius);
                        float xInner = (float) (Math.cos(angle) * inner);
                        float zInner = (float) (Math.sin(angle) * inner);
                        buffer.vertex(matrix, xInner, 0, zInner).color(r, g, b, 0).next();
                        buffer.vertex(matrix, xOuter, 0, zOuter).color(r, g, b, alpha).next();
                    }
                }

                BufferRenderer.drawWithGlobalProgram(buffer.end());
                RenderSystem.disableBlend();
                matrices.pop();
            }
        });
    }
}