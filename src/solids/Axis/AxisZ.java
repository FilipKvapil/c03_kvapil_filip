package solids.Axis;

import model.Part;
import model.TolopogyType;
import model.Vertex;
import solids.Solid;
import transforms.Col;
import transforms.Point3D;

import java.awt.*;

public class AxisZ extends Solid {

    public AxisZ(){
        getVertexBuffer().add(new Vertex(0,0,0,new Col(Color.BLUE.getRGB()))); //v0
        getVertexBuffer().add(new Vertex(0,0,1,new Col(Color.BLUE.getRGB()))); //v1

        //Index buffer
        // LINE
        getIndexBuffer().add(0);
        getIndexBuffer().add(1);

        getPartBuffer().add(new Part(TolopogyType.LINE, 0 , 1));
    }
}
