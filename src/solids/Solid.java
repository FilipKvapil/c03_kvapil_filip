package solids;

import model.Part;
import model.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.awt.*;
import java.util.ArrayList;

public abstract class Solid {
    private ArrayList<Integer> intexBuffer = new ArrayList<>();
    private ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    private ArrayList<Part> partBuffer = new ArrayList<>();
    private ArrayList<Col> color = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();

    public ArrayList<Integer> getIndexBuffer() {
        return intexBuffer;
    }

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public Col getColor(int i) {
        if (color.size() > i && i >= 0) {
            return color.get(i);
        }else {
            return new Col(Color.WHITE.getRGB());
        }
    }

    //modelovací matice
    public Mat4 getMat() {
        return model;
    }

    public void setMat(Mat4 model) {
        this.model = model;
    }
}
