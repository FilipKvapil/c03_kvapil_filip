package solids;

import model.Vertex;
import transforms.Cubic;
import transforms.Point3D;

public class CurveWire  extends Solid{

    public CurveWire() {
        Point3D[] points = new Point3D[4];
        points[0] = new Point3D(-1,-1,-1);
        points[1] = new Point3D(1,1,-1);
        points[2] = new Point3D(1,-1,1);
        points[3] = new Point3D(-1,1,1);


        Cubic cubic = new Cubic(Cubic.FERGUSON, points);

       /* Vertex v = new Vertex(cubic.compute(0));
        for (int i = 0; i < 100; i++) {
            double t = 1 / 100.;
            v = new Vertex(cubic.compute(t));

            getVertexBuffer().add(v);

            getIndexBuffer().add(getVertexBuffer().size() -2);
            getIndexBuffer().add(getVertexBuffer().size() -1);

        }*/

        //TODO: part buffer
    }
}
