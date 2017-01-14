package ewewukek.gl;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

import ewewukek.common.IDisposable;

public class ModelMesh implements IDisposable {
    private int vao;
    private boolean vao_set;

    private Mesh mesh;
    private Shader shader;
    private List<String> texture_names = new ArrayList<>();
    private List<Texture> textures = new ArrayList<>();

    protected ModelMesh(Mesh mesh) {
        this.mesh = mesh;
        vao = glGenVertexArrays();
    }

    public ModelMesh setTexture(String name, Texture tex) {
        if (name == null)
            throw new NullPointerException("null argument name");
        if (tex == null)
            throw new NullPointerException("null argument texture");
        if (texture_names.contains(name))
            throw new IllegalArgumentException("entity mesh already has texture "+name);
        texture_names.add(name);
        textures.add(tex);
        return this;
    }

    public ModelMesh setShader(Shader shader) {
        this.shader = shader;
        if (vao_set) resetVao();
        return this;
    }

    protected void draw() {
        glBindVertexArray(vao);
        if (!vao_set) {
            vao_set = true;
            mesh.bindAttributes(shader);
        }
        shader.use();
        for (int i = 0; i != textures.size(); ++i) {
            String name = texture_names.get(i);
            Texture tex = textures.get(i);
            tex.bind(i);
            shader.setUniform(name, i);
        }
        mesh.draw();
    }

    protected Shader getShader() {
        return shader;
    }

    private void resetVao() {
        dispose();
        vao = glGenVertexArrays();
        vao_set = false;
    }

    @Override
    public void dispose() {
        // TODO: implement
    }
}