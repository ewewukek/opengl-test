package ewewukek.gl;

import java.util.Map;
import java.util.HashMap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import ewewukek.io.FileUtils;
import ewewukek.common.IDisposable;

public class Shader implements IDisposable {
    static private final FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    int vs;
    int fs;
    int program;

    int attributeCount;
    Map<String, Variable> attributeMap = new HashMap<>();
    Variable[] attributeList;

    int uniformCount;
    Map<String, Variable> uniformMap = new HashMap<>();
    Variable[] uniformList;

    protected Shader() {}

    public Shader(String path) {

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, FileUtils.readFile(path+".vert.glsl"));
        glCompileShader(vs);
        checkShaderCompileStatus(vs, path+".vert.glsl");

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, FileUtils.readFile(path+".frag.glsl"));
        glCompileShader(fs);
        checkShaderCompileStatus(fs, path+".frag.glsl");

        program = glCreateProgram();
        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glLinkProgram(program);
        checkProgramLinkStatus(program, path);

        IntBuffer sizeTmp = BufferUtils.createIntBuffer(1);
        IntBuffer typeTmp = BufferUtils.createIntBuffer(1);

        attributeCount = glGetProgrami(program, GL_ACTIVE_ATTRIBUTES);
        attributeList = new Variable[attributeCount];
        for (int i = 0; i != attributeCount; ++i) {
            String name = glGetActiveAttrib(program, i, 256, sizeTmp, typeTmp);
            Variable att = new Variable(i, sizeTmp.get(), typeTmp.get(), name);
            attributeMap.put(name, att);
            attributeList[i] = att;
            sizeTmp.flip();
            typeTmp.flip();
        }

        uniformCount = glGetProgrami(program, GL_ACTIVE_UNIFORMS);
        uniformList = new Variable[uniformCount];
        for (int i = 0; i != uniformCount; ++i) {
            String name = glGetActiveUniform(program, i, 256, sizeTmp, typeTmp);
            Variable att = new Variable(i, sizeTmp.get(), typeTmp.get(), name);
            uniformMap.put(name, att);
            uniformList[i] = att;
            sizeTmp.flip();
            typeTmp.flip();
        }
    }

    public void use() {
        glUseProgram(program);
    }

    public int getAttributeCount() { return attributeCount; }
    public Variable getAttribute(String name) { return attributeMap.get(name); }
    public Variable getAttribute(int location) { return attributeList[location]; }

    public int getUniformCount() { return uniformCount; }
    public Variable getUniform(String name) { return uniformMap.get(name); }
    public Variable getUniform(int location) { return uniformList[location]; }

    public class Variable {
        private int location;
        private int size;
        private int type;
        private String name;

        private Variable(int l, int s, int t, String n) {
            location = l; size = s; type = t; name = n;
        }

        public int getLocation() { return location; }
        public int getSize() { return size; }
        public int getType() { return type; }
        public String getName() { return name; }

        @Override
        public String toString() { return ""+location+": "+GLTypes.toString(type)+" "+name+(size > 1?"["+size+"]":""); }
    }

    private Variable findUniformWarn(String name) {
        Variable u = uniformMap.get(name);
        if (u == null) System.err.println("uniform "+name+" not found in shader");
        return u;
    }

    public void setUniform(String name, float x) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        if (u.type != GL_FLOAT) throw new IllegalStateException("can't put float into "+GLTypes.toString(u.getType()));
        glUniform1f(u.getLocation(), x);
    }

    public void setUniform(String name, Vector2f v) {
        setUniform(name, v.x, v.y);
    }

    public void setUniform(String name, float x, float y) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        if (u.type != GL_FLOAT_VEC2) throw new IllegalStateException("can't put vec2 into "+GLTypes.toString(u.getType()));
        glUniform2f(u.getLocation(), x, y);
    }

    public void setUniform(String name, Vector3f v) {
        setUniform(name, v.x, v.y, v.z);
    }

    public void setUniform(String name, float x, float y, float z) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        if (u.type != GL_FLOAT_VEC3) throw new IllegalStateException("can't put vec3 into "+GLTypes.toString(u.getType()));
        glUniform3f(u.getLocation(), x, y, z);
    }

    public void setUniform(String name, int i) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        if (u.type != GL_UNSIGNED_INT
            && u.type != GL_SAMPLER_2D) throw new IllegalStateException("can't put uint into "+GLTypes.toString(u.getType()));
        glUniform1i(u.getLocation(), i);
    }

    public void setUniform(String name, Matrix3f mat) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        mat.get(fb);
        if (u.type != GL_FLOAT_MAT3) throw new IllegalStateException("can't put mat3 into "+GLTypes.toString(u.getType()));
        glUniformMatrix3fv(u.getLocation(), false, fb);
    }

    public void setUniform(String name, Matrix4f mat) {
        Variable u = findUniformWarn(name);
        if (u == null) return;
        mat.get(fb);
        if (u.type != GL_FLOAT_MAT4) throw new IllegalStateException("can't put mat4 into "+GLTypes.toString(u.getType()));
        glUniformMatrix4fv(u.getLocation(), false, fb);
    }

    static void checkShaderCompileStatus(int shader, String path) {
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException(path+": "+glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
        }
    }

    static void checkProgramLinkStatus(int program, String path) {
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            throw new RuntimeException(path+": "+glGetShaderInfoLog(program, glGetShaderi(program, GL_INFO_LOG_LENGTH)));
        }
    }

    @Override
    public void dispose() {
        glDeleteShader(program); program = 0;
        glDeleteShader(vs); vs = 0;
        glDeleteShader(fs); fs = 0;
    }
}