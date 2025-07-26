package blueprint.blueprintlib.shaders;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class GLShaderProgram {
    private final int programId;
    private final String modId;

    public GLShaderProgram(String modId,String vertexPath, String fragmentPath) {
        this.modId = modId;

        int vertexShader = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
        int fragmentShader = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader);
        GL20.glAttachShader(programId, fragmentShader);
        GL20.glLinkProgram(programId);

        int linked = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        if (linked == 0) {
            String log = GL20.glGetProgramInfoLog(programId);
            throw new RuntimeException("Shader link failed:/n" + log);
        }

        // We no longer need these once linked
        GL20.glDetachShader(programId, vertexShader);
        GL20.glDetachShader(programId, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }

    private int loadShader(String path, int type) {
        try {
            String source = loadShaderSource(path);
            int shader = GL20.glCreateShader(type);
            GL20.glShaderSource(shader, source);
            GL20.glCompileShader(shader);

            int compiled = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
            if (compiled == 0) {
                String log = GL20.glGetShaderInfoLog(shader);
                throw new RuntimeException("Shader compile failed:/n" + log);
            }

            return shader;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader file: " + path, e);
        }
    }

    private String loadShaderSource(String path) {
        MinecraftClient client = MinecraftClient.getInstance();
        ResourceManager resourceManager = client.getResourceManager();

        Identifier shaderId = new Identifier(modId, path); // path = "shaders/core/test.vert"

        try (InputStream input = resourceManager.getResource(shaderId).get().getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader source: " + shaderId, e);
        }
    }

    public void use() {
        GL20.glUseProgram(programId);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void setUniform1f(String name, float value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0)
            GL20.glUniform1f(location, value);
    }

    public void setUniform2f(String name, float v0, float v1) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) GL20.glUniform2f(location, v0, v1);
    }

    public void setUniform3f(String name, float x, float y, float z) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) GL20.glUniform3f(location, x, y, z);
    }

    public void setUniform1i(String name, int value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) GL20.glUniform1i(location, value);
    }

    public void destroy() {
        GL20.glDeleteProgram(programId);
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location >= 0) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buffer = stack.mallocFloat(16);
                matrix.get(buffer);
                GL20.glUniformMatrix4fv(location, false, buffer);
            }
        }
    }
}
