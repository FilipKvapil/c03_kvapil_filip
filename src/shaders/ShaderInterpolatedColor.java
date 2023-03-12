package shaders;

import model.Vertex;

public class ShaderInterpolatedColor implements Shader {
    @Override
    public Col shade(Vertex v) {
        return v.getColor().mul(1 / v.getOne());
    }
}
