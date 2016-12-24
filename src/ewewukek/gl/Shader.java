package ewewukek.gl;

import ewewukek.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader implements IDisposable {

    int vertexShader;
    int fragmentShader;
    int shaderProgram;

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
        // glBindAttribLocation(shaderProgram, Attribute.texture, "in_TexCoord");
        // glBindAttribLocation(shaderProgram, Attribute.normal, "in_Normal");
        // glBindAttribLocation(shaderProgram, Attribute.tangent, "in_Tangent");

        glLinkProgram(shaderProgram);
        checkProgramLinkStatus(shaderProgram, path);

        // int uDiffuse = glGetUniformLocation(shaderProgram, "map_Diffuse");

        // glUseProgram(shaderProgram);
        // glUniform1i(uDiffuse, 0);
        // glUseProgram(0);
    }

    public void use() {
        glUseProgram(shaderProgram);
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