package ewewukek.gl;

import ewewukek.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import org.joml.Matrix4f;

public class Shader implements IDisposable {
    static final FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    int vertexShader;
    int fragmentShader;
    int shaderProgram;

    int uProjectionMatrix;
    int uViewMatrix;

    protected Shader() {}

    public Shader(String path) {
        path = "res/shaders/"+path;
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, FileUtils.readFile(path+".vert.glsl"));
        glCompileShader(vertexShader);
        checkShaderCompileStatus(vertexShader, path+".vert.glsl");

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, FileUtils.readFile(path+".frag.glsl"));
        glCompileShader(fragmentShader);
        checkShaderCompileStatus(fragmentShader, path+".frag.glsl");

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);

        glBindAttribLocation(shaderProgram, Attribute.position, "in_Position");
        glBindAttribLocation(shaderProgram, Attribute.texture, "in_TexCoord");
        // glBindAttribLocation(shaderProgram, Attribute.normal, "in_Normal");
        // glBindAttribLocation(shaderProgram, Attribute.tangent, "in_Tangent");

        glLinkProgram(shaderProgram);
        checkProgramLinkStatus(shaderProgram, path);

        uProjectionMatrix = glGetUniformLocation(shaderProgram, "projectionMatrix");
        uViewMatrix = glGetUniformLocation(shaderProgram, "viewMatrix");

        int uDiffuse = glGetUniformLocation(shaderProgram, "diffuseMap");

        glUseProgram(shaderProgram);

        glUniform1i(uDiffuse, 0);

        glUseProgram(0);
    }

    public void use() {
        glUseProgram(shaderProgram);
    }

    public void setProjectionMatrix(Matrix4f proj) {
        proj.get(fb);
        glUniformMatrix4fv(uProjectionMatrix, false, fb);
    }

    public void setViewMatrix(Matrix4f view) {
        view.get(fb);
        glUniformMatrix4fv(uViewMatrix, false, fb);
    }

    @Override
    public void dispose() {
        if (shaderProgram != 0) { glDeleteProgram(shaderProgram); shaderProgram = 0; }
        if (vertexShader != 0) { glDeleteShader(vertexShader); vertexShader = 0; }
        if (fragmentShader != 0) { glDeleteShader(fragmentShader); fragmentShader = 0; }
    }

    static void checkShaderCompileStatus(int shader, String path) {
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException(path+": "+glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
        }
    }

    static void checkProgramLinkStatus(int shaderProgram, String path) {
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
            throw new RuntimeException(path+": "+glGetProgramInfoLog(shaderProgram, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
        }
    }
}