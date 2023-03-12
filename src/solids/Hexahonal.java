package solids;

import transforms.Point3D;

import java.awt.*;

public class Hexahonal extends Solid{
    public Hexahonal() {
        //testovací
        setColor(Color.YELLOW);

        for (double i = 1; i < 7; i+=.52) {
            vb.add(new Point3D(Math.cos(i), Math.sin(i), .0));
        }

        ib.add(vb.size()-1);
        ib.add(0);
        for (int i = 1; i < vb.size(); i++) {
            ib.add(i-1);
            ib.add(i);
        }

        for (double i = 1; i < 7; i+=.52) {
            vb.add(new Point3D(Math.cos(i)/1.3, Math.sin(i)/1.3, .5));
        }

        ib.add(vb.size()-1);
        ib.add(12);
        for (int i = 13; i < vb.size(); i++) {
            ib.add(i-1);
            ib.add(i);
        }

        for (int j = 0; j < 12; j++) {
            ib.add(j);
            ib.add(j+12);
        }
    }
}
