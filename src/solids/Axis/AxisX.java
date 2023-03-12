package solids.Axis;
import solids.Solid;
import transforms.Point3D;

import java.awt.*;

public class AxisX extends Solid {

    public AxisX(){
        setColor(Color.RED);
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D( 1,0,0));
        addIndices(0,1);
    }
}
