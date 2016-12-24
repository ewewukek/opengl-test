package ewewukek.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class Mesh implements IDisposable {
    int vao;

    int vertexCount;

    int vboPosition;
    int vboTexCoord;
    // int vboNormal;
    // int vboTangent;

    int triangleStripElementCount;
    int trianglesElementCount;

    int vboTriangleStrip;
    int vboTriangles;

    protected Mesh() {}

    public Mesh(String path) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vertexCount = 4;
        FloatBuffer fb = BufferUtils.createFloatBuffer(vertexCount * 3);
        fb.put(-1.0F);   fb.put(-1.0F);   fb.put(0.0F);
        fb.put(-1.0F);   fb.put(1.0F);    fb.put(0.0F);
        fb.put(1.0F);    fb.put(-1.0F);   fb.put(0.0F);
        fb.put(1.0F);    fb.put(1.0F);    fb.put(0.0F);
        fb.flip();

        vboPosition = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboPosition);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glEnableVertexAttribArray(Attribute.position);
        glVertexAttribPointer(Attribute.position, 3, GL_FLOAT, false, 0, 0);

        fb = BufferUtils.createFloatBuffer(vertexCount * 2);
        fb.put(0.0F);    fb.put(0.0F);
        fb.put(0.0F);    fb.put(1.0F);
        fb.put(1.0F);    fb.put(0.0F);
        fb.put(1.0F);    fb.put(1.0F);
        fb.flip();

        vboTexCoord = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoord);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glEnableVertexAttribArray(Attribute.texture);
        glVertexAttribPointer(Attribute.texture, 2, GL_FLOAT, false, 0, 0);

        triangleStripElementCount = 3;
        IntBuffer ib = BufferUtils.createIntBuffer(triangleStripElementCount);
        ib.put(0);
        ib.put(1);
        ib.put(2);
        ib.flip();

        vboTriangleStrip = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboTriangleStrip);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        trianglesElementCount = 3;
        ib = BufferUtils.createIntBuffer(trianglesElementCount);
        ib.put(2);
        ib.put(1);
        ib.put(3);
        ib.flip();

        vboTriangles = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboTriangles);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw() {
        glBindVertexArray(vao);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboTriangleStrip);
        glDrawElements(GL_TRIANGLE_STRIP, triangleStripElementCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboTriangles);
        glDrawElements(GL_TRIANGLES, trianglesElementCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        if (vboPosition != 0) { glDeleteBuffers(vboPosition); vboPosition = 0; }
        if (vboTexCoord != 0) { glDeleteBuffers(vboTexCoord); vboTexCoord = 0; }
        // if (vboNormal != 0) {
        // if (vboTangent != 0) {
        if (vboTriangleStrip != 0) { glDeleteBuffers(vboTriangleStrip); vboTriangleStrip = 0; }
        if (vboTriangles != 0) { glDeleteBuffers(vboTriangles); vboTriangles = 0; }
    }
}