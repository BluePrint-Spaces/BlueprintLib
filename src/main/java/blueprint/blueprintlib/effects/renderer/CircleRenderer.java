package blueprint.blueprintlib.effects.renderer;

import blueprint.blueprintlib.util.CircleEffectHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class CircleRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = camera.getPos();

            CircleEffectHandler.getEffects().forEach(effect -> {
                Vec3d relativePos = effect.position.subtract(camPos);
                float radius = effect.radius;
                float alpha = effect.alpha;
                Color color = effect.color;

                matrices.push();
                matrices.translate(relativePos.x, relativePos.y + 0.05, relativePos.z);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(effect.rotation));

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);

                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

                float r = color.getRed() / 255f;
                float g = color.getGreen() / 255f;
                float b = color.getBlue() / 255f;

                buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0,0).color(r, g, b, alpha).next();

                int segments = 64;
                for (int i = 0; i <= segments; i++) {
                    double angle = 2 * Math.PI * i / segments;
                    float x = (float) Math.cos(angle) * radius;
                    float z = (float) Math.sin(angle) * radius;
                    buffer.vertex(matrices.peek().getPositionMatrix(), x, 0, z).color(r, g, b, 0).next();
                }

                BufferRenderer.drawWithGlobalProgram(buffer.end());

                RenderSystem.disableBlend();
                matrices.pop();
            });
        });
    }
}
