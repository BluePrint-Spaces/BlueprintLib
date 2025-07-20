package blueprint.blueprintlib.unused;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class BeamRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = camera.getPos();

            for (BeamEffect beam : BeamHandler.getEffects()) {
                Vec3d start = beam.start.subtract(camPos);
                Vec3d end = beam.end.subtract(camPos);

                float alpha = beam.getAlpha();
                float width = beam.width;
                Color color = beam.color;

                float r = color.getRed() / 255f;
                float g = color.getGreen() / 255f;
                float b = color.getBlue() / 255f;

                Vec3d dir = end.subtract(start).normalize();
                Vec3d up = new Vec3d(0, 1, 0);
                Vec3d side = dir.crossProduct(up).normalize().multiply(width);

                Matrix4f matrix = matrices.peek().getPositionMatrix();

                RenderSystem.enableBlend();
                RenderSystem.disableCull();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);

                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

                buffer.vertex(matrix, (float)(start.x + side.x), (float)(start.y + side.y), (float)(start.z + side.z)).color(r, g, b, alpha).next();
                buffer.vertex(matrix, (float)(start.x - side.x), (float)(start.y - side.y), (float)(start.z - side.z)).color(r, g, b, alpha).next();
                buffer.vertex(matrix, (float)(end.x - side.x), (float)(end.y - side.y), (float)(end.z - side.z)).color(r, g, b, 0).next();
                buffer.vertex(matrix, (float)(end.x + side.x), (float)(end.y + side.y), (float)(end.z + side.z)).color(r, g, b, 0).next();

                BufferRenderer.drawWithGlobalProgram(buffer.end());

                RenderSystem.disableBlend();
            }
        });
    }
}