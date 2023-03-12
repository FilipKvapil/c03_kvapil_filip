package shaders;

import model.Vertex;

public interface Shader {
    Col shade(Vertex V);
}
