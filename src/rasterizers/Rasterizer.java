package rasterizers;

import model.Vertex;
import transforms.Mat4;


public interface Rasterizer {

    void rasterizer(Vertex... vertex);

}
