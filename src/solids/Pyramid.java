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



        getVertexBuffer().add(new Vertex(.5, -0.5,0,new Col(Color.CYAN.getRGB()))); //v0
        getVertexBuffer().add(new Vertex(.5,.5,0,new Col(Color.MAGENTA.getRGB()))); //v1
        getVertexBuffer().add(new Vertex(1.5,.5,0,new Col(Color.YELLOW.getRGB()))); //v2
        getVertexBuffer().add(new Vertex(1.5, -0.5,0,new Col(Color.BLACK.getRGB()))); //v3
        getVertexBuffer().add(new Vertex(1.,0,.5,new Col(Color.BLACK.getRGB()))); //v4

        //Index buffer

        // TRIANGLE
        Integer[] indexes = new Integer[] { 0,2,1,0,3,2,4,3,2,4,2,1,4,1,0,4,0,3 };

        for (Integer index : indexes
        ) {
            getIndexBuffer().add(index);
        }

        getPartBuffer().add(new Part(TolopogyType.TRIANGLE , 0,6));
    }
}
