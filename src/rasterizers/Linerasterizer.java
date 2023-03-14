package rasterizers;

import model.Vertex;
import raster.ZBuffer;
import transforms.Vec3D;

import java.util.Arrays;
import java.util.List;

public class Linerasterizer implements Rasterizer{
    private final ZBuffer zBuffer;
    private int color;

    public Linerasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    @Override
    public void rasterizer(Vertex... vertex) {
        List<Vertex> vertexBuffer = Arrays.asList(vertex);
        if (Arrays.asList(vertex).size()!=2)
            return;

        Vertex va = vertexBuffer.get(0);
        Vertex vb = vertexBuffer.get(1);
        //ořezání
        if (va == null || vb == null) {
            return;
        }
        if (Math.min(va.getX(), vb.getX()) > 1.0 || Math.max(va.getX(), vb.getX()) < -1.0
                || Math.min(va.getY(), vb.getY()) > 1.0 || Math.max(va.getY(), vb.getY()) < -1.0
                || Math.min(va.getZ(), vb.getZ()) > 1.0 || Math.max(va.getZ(), vb.getZ()) < -1.0) {
            return;
        }
        //Transformace do okna
        Vertex v1 = va.transformToScreen(zBuffer.getImageBuffer().getWidth(), zBuffer.getImageBuffer().getHeight());
        Vertex v2 = vb.transformToScreen(zBuffer.getImageBuffer().getWidth(), zBuffer.getImageBuffer().getHeight());

        Vec3D a = new Vec3D(v1.getPosition());
        Vec3D b = new Vec3D(v2.getPosition());

        int x1 = (int) a.getX();
        int y1 = (int) a.getY();
        double z1 = a.getZ();

        int x2 = (int) b.getX();
        int y2 = (int) b.getY();
        double z2 = b.getZ();

        // vykreslení

        if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
            //Výměna koncových bodů
            if (x2 < x1) {
                int pomocna = x1;
                x1 = x2;
                x2 = pomocna;

                pomocna = y1;
                y1 = y2;
                y2 = pomocna;

                double pomocZ = z2;
                z2 = z1;
                z1 = pomocZ;
            }

            double k = (double)(y2 - y1) / (x2 - x1);

            for (int x = x1; x <= x2; x++) {
                // vykreslení
                    double t3 = ((double) x - x1) / (x2 - x1);
                    double z3 = z1 * (1.0 - t3) + z2 * t3;

                zBuffer.drawWithTest(x,y1,z3,v1.getColor());
                y1 += k;
            }

        }else{
            //Výměna koncových bodů
            if (y2 < y1) {
                int pomoc = x2;
                x2 = x1;
                x1 = pomoc;

                pomoc = y2;
                y2 = y1;
                y1 = pomoc;
            }
            double k =  (double)(x2 - x1)/(y2 - y1);

            for (int y = y1; y <= y2; y++) {
                if (y > 0 && y < zBuffer.getImageBuffer().getWidth() - 1 && y < zBuffer.getImageBuffer().getHeight() - 1) {
                    // vykreslení
                    double t3 = ((double) y - y1) / (y2 - y1);
                    double z3 = z2 * (1.0 - t3) + z1 * t3;
                    zBuffer.drawWithTest(x1, y, z3, v1.getColor());
                }
                x1 += k;
            }
        }

    }
}
