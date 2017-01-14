package ewewukek.gl;

import java.util.HashMap;
import java.util.Map;

import ewewukek.common.IDisposable;

public class Model implements IDisposable {
    private Map<String, ModelMesh> meshes = new HashMap<>();

    public Model() {}

    public ModelMesh addMesh(String name, Mesh mesh) {
        if (meshes.containsKey(name))
            throw new IllegalArgumentException("model already has mesh "+name);
        ModelMesh mm = new ModelMesh(mesh);
        meshes.put(name, mm);
        return mm;
    }

    public ModelMesh getMesh(String name) {
        return meshes.get(name);
    }

    public void draw() {
        meshes.forEach( (k, v) -> v.draw() );
    }

    @Override
    public void dispose() {
        meshes.forEach( (k, v) -> v.dispose() );
        meshes = new HashMap<>();
    }
}