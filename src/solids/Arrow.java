package solids;

import model.Part;
import model.TolopogyType;
import model.Vertex;
import transforms.Col;

public class Arrow extends Solid {
    public Arrow(){
        //Vertex buffer
        getVertexBuffer().add(new Vertex(0,0,0,new Col(255,255,0))); //v0
        getVertexBuffer().add(new Vertex(.8,0,0,new Col(255,255,0))); //v1
        getVertexBuffer().add(new Vertex(1,0,0,new Col(0,0,255))); //v2
        getVertexBuffer().add(new Vertex(0.8,0.2,0,new Col(0,0,255))); //v3
        getVertexBuffer().add(new Vertex(0.8,-0.2,0,new Col(0,0,255))); //v4

        //Index buffer
        // LINE
        getIndexBuffer().add(0);
        getIndexBuffer().add(1);

        // TRIANGLE
        getIndexBuffer().add(2);
        getIndexBuffer().add(3);
        getIndexBuffer().add(4);

        getPartBuffer().add(new Part(TolopogyType.LINE, 0 , 1));
        getPartBuffer().add(new Part(TolopogyType.TRIANGLE , 2,1));

    }
}
