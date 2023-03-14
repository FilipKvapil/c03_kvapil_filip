package rasterizers;

import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TriangleRasterizer implements Rasterizer{
    private final ZBuffer zBuffer;
    private final Boolean fillOrNot = true;
    private int color;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    @Override
    public void rasterizer (Vertex... vertex){

        List<Vertex> vertexBuffer = Arrays.asList(vertex);
        if (vertexBuffer.size() != 3) {
            return;
        }

        //Transformace do okna
        Vertex at = vertexBuffer.get(0).transformToScreen(zBuffer.getImageBuffer().getWidth(), zBuffer.getImageBuffer().getHeight());
        Vertex bt = vertexBuffer.get(1).transformToScreen(zBuffer.getImageBuffer().getWidth(), zBuffer.getImageBuffer().getHeight());
        Vertex ct = vertexBuffer.get(2).transformToScreen(zBuffer.getImageBuffer().getWidth(), zBuffer.getImageBuffer().getHeight());

        if(fillOrNot) {
            if (at.getY() > bt.getY()) {
                Vertex help = at;
                at = bt;
                bt = help;
            }
            if (bt.getY() > ct.getY()) {
                Vertex help = bt;
                bt = ct;
                ct = help;
            }
            if (at.getY() > bt.getY()) {
                Vertex help = at;
                at = ct;
                ct = help;
            }

            for (int y = Math.max((int) at.getY() + 1, 0); y <= Math.min((int) bt.getY(), zBuffer.getImageBuffer().getHeight() - 1); y++) {
                double t = ((double) y - at.getY()) / (bt.getY() - at.getY());
                double x1 = at.getX() * (1.0 - t) + bt.getX() * t;
                double z1 = at.getZ() * (1.0 - t) + bt.getZ() * t;

                double t2 = ((double) y - at.getY()) / (ct.getY() - at.getY());
                double x2 = at.getX() * (1.0 - t2) + ct.getX() * t2;
                double z2 = at.getZ() * (1.0 - t2) + ct.getZ() * t2;

                if (x1 > x2) {
                    double help = x1;
                    x1 = x2;
                    x2 = help;
                    help = z1;
                    z1 = z2;
                    z2 = help;
                }

                for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, zBuffer.getImageBuffer().getWidth() - 1); x++) {
                    double t3 = ((double) x - x1) / (x2 - x1);
                    double z3 = z1 * (1.0 - t3) + z2 * t3;
                    zBuffer.drawWithTest(x, y, z3, at.getColor());
                }
            }

            for (int y = Math.max((int) bt.getY() + 1, 0); y <= Math.min(ct.getY(), zBuffer.getImageBuffer().getHeight() - 1); y++) {
                double t = ((double) y - bt.getY()) / (ct.getY() - bt.getY());
                double x1 = bt.getX() * (1.0 - t) + ct.getX() * t;
                double z1 = bt.getZ() * (1.0 - t) + ct.getZ() * t;

                double t2 = ((double) y - at.getY()) / (ct.getY() - at.getY());
                double x2 = at.getX() * (1.0 - t2) + ct.getX() * t2;
                double z2 = at.getZ() * (1.0 - t2) + ct.getZ() * t2;

                if (x1 > x2) {
                    double help = x1;
                    x1 = x2;
                    x2 = help;
                    help = z1;
                    z1 = z2;
                    z2 = help;
                }

                for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, zBuffer.getImageBuffer().getWidth() - 1); x++) {
                    double t3 = ((double) x - x1) / (x2 - x1);
                    double z3 = z1 * (1.0 - t3) + z2 * t3;
                    zBuffer.drawWithTest(x, y, z3, at.getColor());
                }
            }
        }else{
            Graphics graphics = zBuffer.getImageBuffer().getGraphics();
            graphics.setColor(new Color(Color.BLACK.getRGB()));

            graphics.drawLine((int)at.getX(),(int)at.getY(),(int)bt.getX(),(int)bt.getY());

            graphics.drawLine((int)bt.getX(),(int)bt.getY(),(int)ct.getX(),(int)ct.getY());

            graphics.drawLine((int)ct.getX(),(int)ct.getY(),(int)at.getX(),(int)at.getY());
        }
    }
    public Boolean getFillOrNot() {
        return fillOrNot;
    }

}
