package ewewukek.gl;

import java.util.Map;
import java.util.HashMap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import ewewukek.io.FileUtils;
import ewewukek.common.IDisposable;

public class Shader implements IDisposable {
    // static final FloatBuffer fb = BufferUtils.createFloatBuffer(16);

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