package rasterizers;

import model.Vertex;
import raster.ZBuffer;
import transforms.Mat4;
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

        Vertex v1 = vertexBuffer.get(0).dehomog().transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());
        Vertex v2 = vertexBuffer.get(1).dehomog().transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());

        Vec3D a = new Vec3D(v1.getPosition());
        Vec3D b = new Vec3D(v2.getPosition());

        int x1 = (int) a.getX();
        int y1 = (int) a.getY();
        double z1 = a.getZ();

        int x2 = (int) b.getX();
        int y2 = (int) b.getY();
        double z2 = b.getZ();

        // vykreslení
        double dx = x2 - x1;
        double dy = y2 - y1;

        //algoritmus line

    }
}
