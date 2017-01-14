package ewewukek.gl;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;


import ewewukek.common.IDisposable;
import static ewewukek.common.Utils.*;

public class Mesh implements IDisposable {

    Map<String, Buffer> attributeStreams = new HashMap<>();
    List<Buffer> primitiveStreams = new ArrayList<>();
    List<Integer> primitiveTypes = new ArrayList<>();

    protected Mesh() {}

    public Mesh(String path) {
        System.out.println("loading "+path);
        for (File file: new File(path).listFiles()) {
            String fileName = file.getName();
            String[] parts = splitStringByChar(fileName, '.');
            if (parts.length < 2 || parts.length > 3 || !parts[parts.length-1].equals("txt"))
                throw new RuntimeException("wrong file name: "+fileName);
            if (parts.length == 2) {
                int primitiveType = 0;
                if (parts[0].equals("points")) {
                    primitiveType = GL_POINTS;
                } else if (parts[0].equals("line_strip")) {
                    primitiveType = GL_LINE_STRIP;
                } else if (parts[0].equals("line_loop")) {
                    primitiveType = GL_LINE_LOOP;
                } else if (parts[0].equals("lines")) {
                    primitiveType = GL_LINES;
                } else if (parts[0].equals("triangle_strip")) {
                    primitiveType = GL_TRIANGLE_STRIP;
                } else if (parts[0].equals("triangle_fan")) {
                    primitiveType = GL_TRIANGLE_FAN;
                } else if (parts[0].equals("triangles")) {
                    primitiveType = GL_TRIANGLES;
                } else {
                    throw new UnsupportedOperationException("primitive type not supported: "+parts[0]);
                }
                primitiveStreams.add(new Buffer(GL_UNSIGNED_INT, file.toString()));
                primitiveTypes.add(primitiveType);
                System.out.println("loaded "+parts[0]);
            } else {
                int type = GLTypes.toInt(parts[0]);
                if (type == 0)
                    throw new UnsupportedOperationException("unsupported buffer type: "+parts[0]);
                Buffer buf = new Buffer(type, file.toString());
                attributeStreams.put(parts[1], buf);
                System.out.println("loaded "+parts[0]+" "+parts[1]+" "+buf.getElementCount());
            }
        }
    }

    public void bindAttributes(Shader shader) {
        int count = shader.getAttributeCount();
        for (int i = 0; i != count; ++i) {
            Shader.Variable att = shader.getAttribute(i);
            if (att.getSize() != 1)
                throw new UnsupportedOperationException("attribute arrays not supported");
            Buffer buf = attributeStreams.get(att.getName());
            if (buf == null) {
                glDisableVertexAttribArray(att.getLocation());
                System.out.println("disabled attrib "+att.getLocation());
            } else {
                if (buf.getType() != att.getType())
                    throw new IllegalStateException("attribute type ("+GLTypes.toString(att.getType())
                        +") doesn't match buffer's ("+GLTypes.toString(buf.getType())+")");
                glEnableVertexAttribArray(att.getLocation());
                buf.bind(GL_ARRAY_BUFFER);
                buf.attribPointer(att.getLocation());
                System.out.println("bound buffer to attrib "+att.toString());
            }
        }
    }

    public void draw() {
        for (int i = 0; i != primitiveStreams.size(); ++i) {
            Buffer buf = primitiveStreams.get(i);
            int type = primitiveTypes.get(i);
            buf.bind(GL_ELEMENT_ARRAY_BUFFER);
            glDrawElements(type, buf.getElementCount(), buf.getElementType(), 0);
        }
    }

    @Override
    public void dispose() {
        // TODO: put some code
    }
}