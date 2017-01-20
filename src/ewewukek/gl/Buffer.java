package ewewukek.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import ewewukek.common.IDisposable;
import ewewukek.io.FileUtils;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Buffer implements IDisposable {

    private int glBuffer;

    private int type;
    private int elementType;
    private int elementSize;
    private int elementCount;

    private java.nio.Buffer buffer;

    protected Buffer() {}

    public Buffer(int type, String path) {
        java.nio.Buffer buffer;

        switch(GLTypes.elementType(type)) {
            case GL_FLOAT:
                buffer = FileUtils.readFloatBuffer(path);
            break;
            case GL_UNSIGNED_INT:
                buffer = FileUtils.readIntBuffer(path);
            break;
            default:
                throw new UnsupportedOperationException(GLTypes.toString(type)+" not supported yet");
        }

        try {
            setType(type);
            setBuffer(buffer);
        } catch(Exception e) {
            throw new IllegalStateException("could not load "+path, e);
        }
    }

    public Buffer(int type, java.nio.Buffer buffer) {
        setType(type);
        setBuffer(buffer);
    }

    private void setType(int type) {
        this.type = type;
        elementType = GLTypes.elementType(type);
        elementSize = GLTypes.elementSize(type);
        if (elementType == 0 || elementSize == 0)
            throw new UnsupportedOperationException(GLTypes.toString(type)+" not supported yet");
    }

    private void setBuffer(java.nio.Buffer buffer) {
        if (buffer == null)
            throw new NullPointerException("null buffer provided");
        if (buffer.capacity() == 0)
            throw new IllegalStateException("buffer is empty");
        if (buffer.capacity() % elementSize != 0)
            throw new IllegalStateException("wrong buffer size for "+GLTypes.toString(type));

        this.buffer = buffer;

        elementCount = buffer.capacity() / elementSize;

        glBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
        buffer.flip();
        switch(elementType) {
            case GL_FLOAT:
                glBufferData(GL_ARRAY_BUFFER, (FloatBuffer)buffer, GL_STATIC_DRAW);
            break;
            case GL_UNSIGNED_INT:
                glBufferData(GL_ARRAY_BUFFER, (IntBuffer)buffer, GL_STATIC_DRAW);
            break;
            default:
                throw new RuntimeException("BUG");
        }
    }

    public int getType() { return type; }
    public int getElementType() { return elementType; }
    public int getElementCount() { return elementCount; }

    private void testTypeAndIndex(int type, int index) {
        if (this.type != type)
            throw new IllegalArgumentException("can't convert "+GLTypes.toString(type)+" to "+GLTypes.toString(type));
        if (index < 0 || index >= elementCount)
            throw new IndexOutOfBoundsException("index out of bounds :"+index);
    }

    public int getUInt(int i) {
        testTypeAndIndex(GL_UNSIGNED_INT, i);
        return ((IntBuffer)buffer).get(i);
    }

    public float getFloat(int i) {
        testTypeAndIndex(GL_FLOAT, i);
        return ((FloatBuffer)buffer).get(i);
    }

    public Vector2f getVector2f(Vector2f v, int i) {
        testTypeAndIndex(GL_FLOAT_VEC2, i);
        buffer.position(i*2);
        return v.set((FloatBuffer)buffer);
    }

    public Vector3f getVector3f(Vector3f v, int i) {
        testTypeAndIndex(GL_FLOAT_VEC3, i);
        buffer.position(i*3);
        return v.set((FloatBuffer)buffer);
    }

    public void bind(int target) {
        if (glBuffer == 0)
            throw new IllegalStateException("trying to bind null buffer");
        glBindBuffer(target, glBuffer);
    }

    public void attribPointer(int location) {
        glVertexAttribPointer(location, elementSize, elementType, false, 0, 0);
    }

    @Override
    public void dispose() {
        glDeleteBuffers(glBuffer);
        glBuffer = 0;
    }
}