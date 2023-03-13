package shaders;

import model.Vertex;
import transforms.Col;

public class ShaderInterpolatedColor implements Shader {
    @Override
    public Col shade(Vertex v) {
        return v.getColor().mul(1 / v.getOne());
    }
}
