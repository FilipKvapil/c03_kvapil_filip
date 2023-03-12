package solids.Axis;


import solids.Solid;
import transforms.Point3D;

import java.awt.*;

public class AxisY extends Solid {

    public AxisY(){
        setColor(Color.GREEN);
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D( 0,1,0));
        addIndices(0,1);
    }
}
