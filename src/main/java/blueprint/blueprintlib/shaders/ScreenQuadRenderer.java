package blueprint.blueprintlib.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import org.lwjgl.opengl.*;

public class ScreenQuadRenderer {
    private final GLShaderProgram shader;
    private float time = 0f;

    private int vao, vbo;

    public ScreenQuadRenderer(String modId,String vertPath, String fragPath) {
        this.shader = new GLShaderProgram(modId, vertPath, fragPath);
        initQuad();
    }

    private void initQuad() {
        float[] vertices = {
                -1f, -1f,
                1f, -1f,
                1f,  1f,
                -1f,  1f
        };

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        GL11.glDisable(GL43.GL_DEBUG_OUTPUT);
    }

    public void render(float tickDelta) {
        time += tickDelta;

        float deltaTime = MinecraftClient.getInstance().getLastFrameDuration();
        double mouseX = MinecraftClient.getInstance().mouse.getX();
        double mouseY = MinecraftClient.getInstance().mouse.getY();

        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // Additive blend

        shader.use();
        shader.setUniform1f("u_Time", time);
        shader.setUniform1f("u_DeltaTime", deltaTime);
        shader.setUniform2f("u_Resolution",
                MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
                MinecraftClient.getInstance().getWindow().getFramebufferHeight());
        shader.setUniform2f("u_Mouse", (float) mouseX, (float) mouseY);


        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 4);
        GL30.glBindVertexArray(0);

        shader.stop();

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void destroy() {
        shader.destroy();
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
    }
}