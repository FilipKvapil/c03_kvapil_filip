package solids.Axis;

import solids.Solid;
import transforms.Point3D;

import java.awt.*;

public class AxisZ extends Solid {

    public AxisZ(){
        setColor(Color.BLUE);
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D( 0,0,1));
        addIndices(0,1);
    }
}
