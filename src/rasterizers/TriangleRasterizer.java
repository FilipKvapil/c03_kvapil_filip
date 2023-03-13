package rasterizers;

import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.Arrays;
import java.util.List;

public class TriangleRasterizer implements Rasterizer{
    private final ZBuffer zBuffer;
    private Boolean fillOrNot = true;
    private int color;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    @Override
    public void rasterizer (Vertex... vertex){
        List<Vertex> vertexBuffer = Arrays.asList(vertex);
        if (Arrays.asList(vertex).size()!=3)
            return;

        //Transformace do okna
        Vertex at = vertexBuffer.get(0).transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());
        Vertex bt = vertexBuffer.get(1).transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());
        Vertex ct = vertexBuffer.get(2).transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());

        if (at.getY()>bt.getY()){
            Vertex help = bt;
            bt = at;
            at = help;
        }
        if (bt.getY()>ct.getY()){
            Vertex help = ct;
            ct=bt;
            bt=help;
        }
        if (at.getY()>bt.getY()){
            Vertex help =bt;
            bt=at;
            at=help;
        }

        for (int y = (int)at.getY(); y<= bt.getY(); y++){
            double t1 = (y-at.getY())/(bt.getY()-at.getY());
            int x1 = (int) ((1-t1)*at.getX()+t1*bt.getX());
            int z1 = (int) ((1-t1)*at.getZ()+t1*bt.getZ());

            double t2 = (y-at.getY())/(ct.getY()-at.getY());
            int x2 = (int) ((1-t2)*at.getX()+t2*ct.getX());
            int z2 = (int) ((1-t2)*at.getZ()+t2*ct.getZ());
            
            if (x1 > x2) {
                int help = x1;
                x1 = x2;
                x2 = help;
                help = z1;
                z1 = z2;
                z2 = help;
            }

            for (int x = x1; x < x2; x++) {
                double t3 = ((double) x - x1) / (x2 - x1);
                double z3 = z1 * (1.0 - t3) + z2 * t3;
                zBuffer.drawWithTest(x,y,z3,new Col(0xffff00));
            }
        }

        for (int y = (int)bt.getY(); y<= ct.getY(); y++){
            double t1 = (y-bt.getY())/(ct.getY()-bt.getY());
            int x1 = (int) ((1-t1)*bt.getX()+t1*ct.getX());
            int z1 = (int) ((1-t1)*at.getZ()+t1*bt.getZ());

            double t2 = (y-at.getY())/(ct.getY()-at.getY());
            int x2 = (int) ((1-t2)*at.getX()+t2*ct.getX());
            int z2 = (int) ((1-t2)*at.getZ()+t2*ct.getZ());

            if (x1 > x2) {
                int help = x1;
                x1 = x2;
                x2 = help;
                help = z1;
                z1 = z2;
                z2 = help;
            }

            for (int x = x1; x < x2; x++) {
                double t3 = ((double) x - x1) / (x2 - x1);
                double z3 = z1 * (1.0 - t3) + z2 * t3;

                zBuffer.drawWithTest(x,y,z3,new Col(0xffff00));
            }
        }
    }
    public Boolean getFillOrNot() {
        return fillOrNot;
    }

}
