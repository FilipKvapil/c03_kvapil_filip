package shaders;

import model.Vertex;

public class ShaderFullColor implements Shader{
    @Override
    public Col shade(Vertex V) {
        return new Col(0xff0000);
    }
}
