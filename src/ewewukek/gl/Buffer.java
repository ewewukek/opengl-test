package ewewukek.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import ewewukek.common.IDisposable;
import ewewukek.io.FileUtils;

public class Buffer implements IDisposable {

    private int glBuffer;

    private int type;
    private int elementType;
    private int elementSize;
    private int elementCount;

    private java.nio.Buffer buffer;

    protected Buffer() {}

    public Buffer(int type, String path) {
        this.type = type;
        elementType = GLTypes.elementType(type);
        elementSize = GLTypes.elementSize(type);
        if (elementType == 0 || elementSize == 0)
            throw new UnsupportedOperationException(GLTypes.toString(type)+" not supported yet");

        switch(elementType) {
            case GL_FLOAT:
                buffer = FileUtils.readFloatBuffer(path);
            break;
            case GL_UNSIGNED_INT:
                buffer = FileUtils.readIntBuffer(path);
            break;
            default:
                throw new UnsupportedOperationException(GLTypes.toString(type)+" not supported yet");
        }

        if (buffer == null)
            throw new NullPointerException(path+" is empty or doesn't exist");
        if (buffer.capacity() == 0)
            throw new IllegalStateException(path+" is empty");
        if (buffer.capacity() % elementSize != 0)
            throw new IllegalStateException("wrong buffer size for "+GLTypes.toString(type)+" in "+path);

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