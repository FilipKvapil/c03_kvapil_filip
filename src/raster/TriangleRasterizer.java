package raster;

import model.Vertex;
import utils.Lerp;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;
    Vertex vab = null;
    Vertex vac = null;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        lerp = new Lerp<>();
    }

    public void rasterizer (Vertex v1, Vertex v2, Vertex v3){

        //Transformace do okna
        //TODO: transformace bude probíhat ve Vertexu

        Vertex a1 = v1.transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());
        Vertex b1 = v2.transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());
        Vertex c1 = v3.transformToScreen (zBuffer.getImageBuffer().getWidth(),zBuffer.getImageBuffer().getHeight());

        Vec3D a = new Vec3D(a1.getPosition());
        Vec3D b = new Vec3D(b1.getPosition());
        Vec3D c = new Vec3D(c1.getPosition());

        //Debug účely
        Graphics graphics = zBuffer.getImageBuffer().getGraphics();
        graphics.setColor(new Color(0xffff00));

        graphics.drawLine((int)a.x,(int)a.y,(int)b.x,(int)b.y);

        graphics.drawLine((int)b.x,(int)b.y,(int)c.x,(int)c.y);

        graphics.drawLine((int)c.x,(int)c.y,(int)a.x,(int)a.y);

        if (a.y>b.y){
            Vec3D help = b;
            b=a;
            a=help;
        }
        if (b.y>c.y){
            Vec3D help = c;
            c=b;
            b=help;
        }
        if (a.y>b.y){
            Vec3D help =b;
            b=a;
            a=help;
        }
        for (int y = (int)a.y; y<= b.y; y++){
            double t1 = (y-a.y)/(b.y-a.y);
            int x1 = (int) ((1-t1)*a.x+t1*b.x);
            int z1 = (int) ((1-t1)*a.z+t1*b.z);

            vab = lerp.lerp(v1,v2,t1);

            double t2 = (y-a.y)/(c.y-a.y);
            int x2 = (int) ((1-t2)*a.x+t2*c.x);
            int z2 = (int) ((1-t2)*a.z+t2*c.z);

            vac = lerp.lerp(v1,v3,t2);

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

        for (int y = (int)b.y; y<= c.y; y++){
            double t1 = (y-b.y)/(c.y-b.y);
            int x1 = (int) ((1-t1)*b.x+t1*c.x);
            int z1 = (int) ((1-t1)*a.z+t1*b.z);
            double t2 = (y-a.y)/(c.y-a.y);
            int x2 = (int) ((1-t2)*a.x+t2*c.x);
            int z2 = (int) ((1-t2)*a.z+t2*c.z);

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
                Vertex v = lerp.lerp(vac,vab,t3);
                zBuffer.drawWithTest(x,y,z3,new Col(0xffff00));
            }
        }
    }

    private Vec3D transformToWindow (Point3D p){
       return p.ignoreW()
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((zBuffer.getImageBuffer().getWidth()-1)/2.,(zBuffer.getImageBuffer().getHeight()-1)/2.,1));

    }

}
