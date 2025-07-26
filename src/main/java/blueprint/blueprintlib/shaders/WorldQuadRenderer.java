package blueprint.blueprintlib.shaders;

import blueprint.blueprintlib.shaders.shape.ShapeFactory;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class WorldQuadRenderer {
    private final GLShaderProgram shader;
    private final int vao, vbo;
    private float time = 0f;
    private int vertexCount = 0;

    public WorldQuadRenderer(String modId, String vertPath, String fragPath,
                             float[] vertexData, int dimensions) {
        this.shader = new GLShaderProgram(modId, vertPath, fragPath);
        this.vertexCount = vertexData.length / dimensions;

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, dimensions, GL11.GL_FLOAT, false, dimensions * Float.BYTES, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render(MatrixStack matrices, Vec3d pos, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        time += tickDelta;
        Vector3f viewDir = camera.getHorizontalPlane().normalize();
        float deltaTime = MinecraftClient.getInstance().getLastFrameDuration();
        float rotationAngle = (time * 0.5f) % ((float) Math.PI * 2f);
        Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f); // Uniform scale

        Matrix4f model = new Matrix4f()
                .translate((float)(pos.x - cameraPos.x), (float)(pos.y - cameraPos.y), (float)(pos.z - cameraPos.z));

        Matrix4f view = matrices.peek().getPositionMatrix();
        Matrix4f projection = RenderSystem.getProjectionMatrix();

        RenderSystem.disableCull();
//        RenderSystem.disableDepthTest(); // optional, or enable with depthMask tweak
        RenderSystem.enableBlend();

        shader.use();
        shader.setUniform1f("u_Time", time);
        shader.setUniformMatrix4f("u_Model", model);
        shader.setUniformMatrix4f("u_View", view);
        shader.setUniformMatrix4f("u_Projection", projection);
        shader.setUniform2f("u_Resolution",
                MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
                MinecraftClient.getInstance().getWindow().getFramebufferHeight());
        shader.setUniform3f("u_ViewDir", (float) viewDir.x, (float) viewDir.y, (float) viewDir.z);
        shader.setUniform1f("u_DeltaTime", deltaTime);
        shader.setUniform1f("u_Rotation", rotationAngle);
        shader.setUniform3f("u_Scale", scale.x(), scale.y(), scale.z());

        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
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