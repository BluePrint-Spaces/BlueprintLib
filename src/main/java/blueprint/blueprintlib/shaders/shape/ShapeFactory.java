package blueprint.blueprintlib.shaders.shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeFactory {
    public static float[] createQuad(float width, float height) {
        float hw = width / 2f;
        float hh = height / 2f;

        return new float[]{
                -hw, -hh, 0f,
                hw, -hh, 0f,
                hw, hh, 0f,
                -hw, -hh, 0f,
                hw, hh, 0f,
                -hw, hh, 0f
        };
    }

    public static float[] createCube(float size) {
        float s = size / 2f;

        return new float[]{
                // Front
                -s, -s, s, s, -s, s, s, s, s,
                -s, -s, s, s, s, s, -s, s, s,

                // Back
                -s, -s, -s, -s, s, -s, s, s, -s,
                -s, -s, -s, s, s, -s, s, -s, -s,

                // Left
                -s, -s, -s, -s, -s, s, -s, s, s,
                -s, -s, -s, -s, s, s, -s, s, -s,

                // Right
                s, -s, -s, s, s, -s, s, s, s,
                s, -s, -s, s, s, s, s, -s, s,

                // Top
                -s, s, -s, -s, s, s, s, s, s,
                -s, s, -s, s, s, s, s, s, -s,

                // Bottom
                -s, -s, -s, s, -s, -s, s, -s, s,
                -s, -s, -s, s, -s, s, -s, -s, s
        };
    }

        // TODO: add createSphere, createCylinder, etc.

    public static float[] createSphere(float radius, int segments, int rings) {
        List<Float> vertices = new ArrayList<>();

        for (int y = 0; y <= rings; y++) {
            float v = (float) y / rings;
            float theta = (float) (v * Math.PI);

            for (int x = 0; x <= segments; x++) {
                float u = (float) x / segments;
                float phi = (float) (u * Math.PI * 2);

                float xPos = (float) (radius * Math.sin(theta) * Math.cos(phi));
                float yPos = (float) (radius * Math.cos(theta));
                float zPos = (float) (radius * Math.sin(theta) * Math.sin(phi));

                vertices.add(xPos);
                vertices.add(yPos);
                vertices.add(zPos);
            }
        }

        List<Float> finalVertices = new ArrayList<>();
        for (int y = 0; y < rings; y++) {
            for (int x = 0; x < segments; x++) {
                int i0 = y * (segments + 1) + x;
                int i1 = i0 + 1;
                int i2 = i0 + segments + 1;
                int i3 = i2 + 1;

                // Triangle 1
                addVertex(finalVertices, vertices, i0);
                addVertex(finalVertices, vertices, i2);
                addVertex(finalVertices, vertices, i1);

                // Triangle 2
                addVertex(finalVertices, vertices, i1);
                addVertex(finalVertices, vertices, i2);
                addVertex(finalVertices, vertices, i3);
            }
        }
        return toFloatArray(finalVertices);
    }


    public static float[] createCylinder(float radius, float height, int segments) {
        List<Float> vertices = new ArrayList<>();

        float halfHeight = height / 2f;

        for (int i = 0; i < segments; i++) {
            float theta0 = (float) (2 * Math.PI * i / segments);
            float theta1 = (float) (2 * Math.PI * (i + 1) / segments);

            float x0 = (float) Math.cos(theta0) * radius;
            float z0 = (float) Math.sin(theta0) * radius;
            float x1 = (float) Math.cos(theta1) * radius;
            float z1 = (float) Math.sin(theta1) * radius;

            // Side quad (2 triangles)
            // Bottom triangle
            vertices.add(x0); vertices.add(-halfHeight); vertices.add(z0);
            vertices.add(x1); vertices.add(-halfHeight); vertices.add(z1);
            vertices.add(x1); vertices.add(halfHeight);  vertices.add(z1);

            // Top triangle
            vertices.add(x0); vertices.add(-halfHeight); vertices.add(z0);
            vertices.add(x1); vertices.add(halfHeight);  vertices.add(z1);
            vertices.add(x0); vertices.add(halfHeight);  vertices.add(z0);
        }

        return toFloatArray(vertices);
    }




//    ======================================================================================================


    private static void addVertex(List<Float> target, List<Float> source, int index) {
        int i = index * 3;
        target.add(source.get(i));
        target.add(source.get(i + 1));
        target.add(source.get(i + 2));
    }

    private static float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
}