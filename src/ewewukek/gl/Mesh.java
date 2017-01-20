package ewewukek.gl;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.Vector2f;
import org.joml.Vector3f;

import ewewukek.common.IDisposable;
import static ewewukek.common.Utils.*;

public class Mesh implements IDisposable {
    public static final int CALCULATE_TBN = 1;

    private int vertexCount = -1;
    private Map<String, Buffer> attributeStreams = new HashMap<>();
    private List<Buffer> primitiveStreams = new ArrayList<>();
    private List<Integer> primitiveTypes = new ArrayList<>();

    protected Mesh() {}

    public Mesh(String path) {
        this(path, 0);
    }

    public Mesh(String path, int flags) {
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
                if ((flags & CALCULATE_TBN) != 0) {
                    if (parts[0].equals("normal")
                    || parts[0].equals("tangent")
                    || parts[0].equals("bitangent")) {
                        System.out.println("skipping "+parts[0]+", will use calculated instead");
                        continue;
                    }
                }
                int type = GLTypes.toInt(parts[0]);
                if (type == 0)
                    throw new UnsupportedOperationException("unsupported buffer type: "+parts[0]);
                Buffer buf = new Buffer(type, file.toString());
                if (vertexCount == -1) {
                    vertexCount = buf.getElementCount();
                } else {
                    if (vertexCount != buf.getElementCount())
                        throw new IllegalStateException("buffers' vertex counts does not match");
                }
                attributeStreams.put(parts[1], buf);
                System.out.println("loaded "+parts[0]+" "+parts[1]+" "+buf.getElementCount());
            }
        }
        if ((flags & CALCULATE_TBN) != 0)
            calculateTBN();
    }

    public void calculateTBN() {
        Buffer position = attributeStreams.get("position");
        Buffer texcoord = attributeStreams.get("texcoord");
        Buffer triangles = null;
        for (int i = 0; i != primitiveStreams.size(); ++i) {
            int type = primitiveTypes.get(i);
            if (type == GL_TRIANGLES) {
                triangles = primitiveStreams.get(i);
            } else {
                System.err.println("calculateTBN: unsupported primitive type "+GLTypes.primitiveTypeToString(type));
            }
        }
        if (triangles == null) {
            System.err.println("calculateTBN: mesh has no triangles");
            return;
        }
        int triangleCount = triangles.getElementCount() / 3;

        float[] normal = new float[vertexCount * 3];
        float[] tangent = new float[vertexCount * 3];
        float[] bitangent = new float[vertexCount * 3];
        float[] areaSum = new float[vertexCount];

        Vector3f P0 = new Vector3f();
        Vector3f Q1 = new Vector3f();
        Vector3f Q2 = new Vector3f();
        Vector3f N = new Vector3f();
        Vector2f tc0 = new Vector2f();
        Vector2f tc1 = new Vector2f();
        Vector2f tc2 = new Vector2f();
        Vector3f T = new Vector3f();
        Vector3f B = new Vector3f();
        Vector3f tmp1 = new Vector3f();
        Vector3f tmp2 = new Vector3f();
        for (int t = 0; t != triangleCount; ++t) {
            int i0 = triangles.getUInt(t*3);
            int i1 = triangles.getUInt(t*3+1);
            int i2 = triangles.getUInt(t*3+2);

            position.getVector3f(P0, i0);
            position.getVector3f(Q1, i1).sub(P0);
            position.getVector3f(Q2, i2).sub(P0);

            N.set(Q1).cross(Q2);
            float nl = N.length();
            N.normalize();

            normal[i0*3] += N.x;
            normal[i0*3+1] += N.y;
            normal[i0*3+2] += N.z;

            normal[i1*3] += N.x;
            normal[i1*3+1] += N.y;
            normal[i1*3+2] += N.z;

            normal[i2*3] += N.x;
            normal[i2*3+1] += N.y;
            normal[i2*3+2] += N.z;

            areaSum[i0] += nl;
            areaSum[i1] += nl;
            areaSum[i2] += nl;

            texcoord.getVector2f(tc0, i0);
            texcoord.getVector2f(tc1, i1);
            texcoord.getVector2f(tc2, i2);

            float s1 = tc1.x - tc0.x;
            float t1 = tc1.y - tc0.y;
            float s2 = tc2.x - tc0.x;
            float t2 = tc2.y - tc0.y;
            float d = (s1 * t2) - (s2 * t1);

            tmp1.set(Q1).mul(t2);
            tmp2.set(Q2).mul(-t1);
            T.set(tmp1).add(tmp2).div(d);

            tmp1.set(Q1).mul(-s2);
            tmp2.set(Q2).mul(s1);
            B.set(tmp1).add(tmp2).div(d);

            tangent[i0*3] += T.x;
            tangent[i0*3+1] += T.y;
            tangent[i0*3+2] += T.z;

            tangent[i1*3] += T.x;
            tangent[i1*3+1] += T.y;
            tangent[i1*3+2] += T.z;

            tangent[i2*3] += T.x;
            tangent[i2*3+1] += T.y;
            tangent[i2*3+2] += T.z;

            bitangent[i0*3] += B.x;
            bitangent[i0*3+1] += B.y;
            bitangent[i0*3+2] += B.z;

            bitangent[i1*3] += B.x;
            bitangent[i1*3+1] += B.y;
            bitangent[i1*3+2] += B.z;

            bitangent[i2*3] += B.x;
            bitangent[i2*3+1] += B.y;
            bitangent[i2*3+2] += B.z;
        }

        FloatBuffer fb_normal = BufferUtils.createFloatBuffer(vertexCount * 3);
        FloatBuffer fb_tangent = BufferUtils.createFloatBuffer(vertexCount * 3);
        FloatBuffer fb_bitangent = BufferUtils.createFloatBuffer(vertexCount * 3);

        for (int i = 0; i != vertexCount; ++i) {
            fb_normal.put(normal[i*3] / areaSum[i]);
            fb_normal.put(normal[i*3+1] / areaSum[i]);
            fb_normal.put(normal[i*3+2] / areaSum[i]);
            fb_tangent.put(tangent[i*3] / areaSum[i]);
            fb_tangent.put(tangent[i*3+1] / areaSum[i]);
            fb_tangent.put(tangent[i*3+2] / areaSum[i]);
            fb_bitangent.put(bitangent[i*3] / areaSum[i]);
            fb_bitangent.put(bitangent[i*3+1] / areaSum[i]);
            fb_bitangent.put(bitangent[i*3+2] / areaSum[i]);
        }

        attributeStreams.put("normal", new Buffer(GL_FLOAT_VEC3, fb_normal));
        attributeStreams.put("tangent", new Buffer(GL_FLOAT_VEC3, fb_tangent));
        attributeStreams.put("bitangent", new Buffer(GL_FLOAT_VEC3, fb_bitangent));
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
    }
}