package solids;

import model.Part;
import model.TolopogyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

import java.awt.*;

public class Pyramid extends Solid{
    public Pyramid() {
        //TODO:Dodělat index buffer
        //addIndices(0,1,1,2,2,3,3,0,0,4,1,4,2,4,3,4);

        getVertexBuffer().add(new Vertex(.75, .75,0,new Col(Color.cyan.getRGB()))); //v0
        getVertexBuffer().add(new Vertex(.25,.75,0,new Col(Color.cyan.getRGB()))); //v1
        getVertexBuffer().add(new Vertex(.25,.25,0,new Col(Color.cyan.getRGB()))); //v2
        getVertexBuffer().add(new Vertex(.75, .25,0,new Col(Color.cyan.getRGB()))); //v3
        getVertexBuffer().add(new Vertex(.5,.5,.5,new Col(Color.cyan.getRGB()))); //v4

        //Index buffer

        // TRIANGLE
        getIndexBuffer().add(0);
        getIndexBuffer().add(1);
        getIndexBuffer().add(2);

        getPartBuffer().add(new Part(TolopogyType.TRIANGLE , 0,6));
    }
}
