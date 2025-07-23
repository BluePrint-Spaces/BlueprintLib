package blueprint.blueprintlib.effects.renderer;

import blueprint.blueprintlib.effects.TeslaArcEffect;
import blueprint.blueprintlib.util.TeslaArcHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;

public class TeslaArcRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = camera.getPos();

            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(
                    GlStateManager.SrcFactor.SRC_ALPHA,
                    GlStateManager.DstFactor.ONE
            );
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);

            for (TeslaArcEffect arc : TeslaArcHandler.getEffects()) {
                List<List<Vec3d>> strands = arc.getStrands();
                float baseR = arc.color.getRed() / 255f;
                float baseG = arc.color.getGreen() / 255f;
                float baseB = arc.color.getBlue() / 255f;

                Vec3d direction = arc.direction.normalize();
                Vec3d up = new Vec3d(0, 1, 0);
                Vec3d side = direction.crossProduct(up).normalize().multiply(arc.strandSpacing);

                for (int s = 0; s < strands.size(); s++) {
                    Vec3d offset = side.multiply(((s / (float) Math.max(strands.size() - 1, 1)) - 0.5));
                    List<Vec3d> points = strands.get(s);

                    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                    buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

                    for (int i = 0; i < points.size() - 1; i++) {
                        Vec3d p1 = points.get(i).add(offset).subtract(camPos);
                        Vec3d p2 = points.get(i + 1).add(offset).subtract(camPos);

                        Vec3d segmentDir = p2.subtract(p1).normalize();
                        Vec3d normal = segmentDir.crossProduct(up).normalize().multiply(arc.lineThickness / 2f);

                        float flicker = 0.85f + (float)(Math.random() * 0.15f);
                        float r = baseR * flicker;
                        float g = baseG * flicker;
                        float b = baseB * flicker;

                        float alpha1 = 1f * (1f - ((float)i / points.size()));
                        float alpha2 = 1f * (1f - ((float)(i + 1) / points.size()));

                        Matrix4f matrix = matrices.peek().getPositionMatrix();

                        buffer.vertex(matrix, (float)(p1.x + normal.x), (float)(p1.y + normal.y), (float)(p1.z + normal.z)).color(r, g, b, alpha1).next();
                        buffer.vertex(matrix, (float)(p1.x - normal.x), (float)(p1.y - normal.y), (float)(p1.z - normal.z)).color(r, g, b, alpha1).next();
                        buffer.vertex(matrix, (float)(p2.x - normal.x), (float)(p2.y - normal.y), (float)(p2.z - normal.z)).color(r, g, b, alpha2).next();
                        buffer.vertex(matrix, (float)(p2.x + normal.x), (float)(p2.y + normal.y), (float)(p2.z + normal.z)).color(r, g, b, alpha2).next();
                    }

                    BufferRenderer.drawWithGlobalProgram(buffer.end());
                }
            }

            RenderSystem.defaultBlendFunc(); // Restore normal blend
            RenderSystem.disableBlend();
        });
    }
}
