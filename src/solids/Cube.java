package solids;

import model.Part;
import model.TolopogyType;
import model.Vertex;
import transforms.Col;

import java.awt.*;

public class Cube extends Solid{
    public Cube(){
        //Vertex buffer
        getVertexBuffer().add(new Vertex(1,0,0,new Col(Color.BLUE.getRGB()))); //v0
        getVertexBuffer().add(new Vertex(1,1.,0,new Col(Color.MAGENTA.getRGB()))); //v1
        getVertexBuffer().add(new Vertex(0,1.,0,new Col(Color.RED.getRGB()))); //v2
        getVertexBuffer().add(new Vertex(0,0,0,new Col(Color.BLACK.getRGB()))); //v3

        getVertexBuffer().add(new Vertex(1.,0,1.,new Col(Color.CYAN.getRGB()))); //v4
        getVertexBuffer().add(new Vertex(1.,1.,1.,new Col(Color.WHITE.getRGB()))); //v5
        getVertexBuffer().add(new Vertex(0,1.,1.,new Col(Color.YELLOW.getRGB()))); //v6
        getVertexBuffer().add(new Vertex(0,0,1.,new Col(Color.GREEN.getRGB()))); //v7
        //Index buffer

        // TRIANGLE
        Integer[] indexes = new Integer[] { 4,5,6,4,7,6,0,1,2,0,3,2,0,1,5,0,4,5,3,2,6,3,7,6,1,2,6,1,5,6,0,3,7,0,4,7};

        for (Integer index : indexes
             ) {
            getIndexBuffer().add(index);
        }

        getPartBuffer().add(new Part(TolopogyType.TRIANGLE , 0,12));
    }
}
