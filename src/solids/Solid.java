package solids;

import model.Part;
import model.Vertex;
import transforms.Col;

import java.awt.*;
import java.util.ArrayList;

public abstract class Solid {
    private ArrayList<Integer> intexBuffer = new ArrayList<>();
    private ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    private ArrayList<Part> partBuffer = new ArrayList<>();
    private Col color = new Col(Color.BLACK.getRGB());

    public ArrayList<Integer> getIndexBuffer() {
        return intexBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public Col getColor() {
        return color;
    }
}
