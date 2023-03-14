package renderer;

import model.Part;
import model.Vertex;
import rasterizers.Linerasterizer;
import rasterizers.TriangleRasterizer;
import solids.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import utils.Lerp;

import java.util.List;

public class Renderer {
    private final TriangleRasterizer triangleRasterizer;
    private final Linerasterizer linerasterizer;
    private final Lerp<Vertex> lerp;
    private Mat4 view;
    private Mat4 proj;
    private Mat4 finalMat;

    public Renderer(TriangleRasterizer triangleRasterizer,Linerasterizer linerasterizer,Mat4 proj,Mat4 view) {
        this.triangleRasterizer = triangleRasterizer;
        this.linerasterizer = linerasterizer;
        this.proj = proj;
        this.view = view;
        lerp = new Lerp<>();
    }

    public void renderSolid (Solid solid){
        finalMat = solid.getMat().mul(view).mul(proj);
        int start;
        for (Part part : solid.getPartBuffer()) {
            switch(part.getType()) {
                case TRIANGLE:
                    start = part.getIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start)).transform(finalMat).dehomog();
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start+1)).transform(finalMat).dehomog();
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start+2)).transform(finalMat).dehomog();
                        start += 3;
                        renderTriangle(a,b,c);
                    }
                    break;
                case LINE:
                    start = part.getIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start)).transform(finalMat);
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start+1)).transform(finalMat);
                        start += 2;
                        renderLine(a,b);
                    }
                    break;
            }
        }
    }
    public void render (List<Solid> scene){
        for (Solid solid :
                scene) {
            render(solid);
        }
    }
    public void render(Solid... solids){
        for (Solid solid: solids){
            renderSolid(solid);
        }
    }
    private void renderTriangle(Vertex a, Vertex b, Vertex c) {

        // fast-clip
        if (fastClip(a.getPosition()) || fastClip(b.getPosition()) || fastClip(c.getPosition()))
            return;
        // srovnani
        if (a.getZ() > b.getZ()) {
            Vertex help = a;
            a = b;
            b = help;
        }
        if (b.getZ() > c.getZ()) {
            Vertex help = b;
            b = c;
            c = help;
        }
        if (a.getZ() > b.getZ()) {
            Vertex help = a;
            a = b;
            b = help;
        }
        double zMin = 0;
        if (a.getZ() < zMin)
            return;
        if (b.getZ() < zMin) {
            double tVAB = (zMin - a.getZ()) / (b.getZ() - a.getZ());
            Vertex vab = lerp.lerp(a, b, tVAB);

            double tVBC = (zMin - b.getZ()) / (c.getZ() - b.getZ());
            Vertex vbc = lerp.lerp(b, c, tVBC);

            triangleRasterizer.rasterizer(a, vab, vbc);
        }
        if (c.getZ() < zMin) {
            double tVAC = (zMin - a.getZ()) / (c.getZ() - a.getZ());
            Vertex vac = lerp.lerp(a, c, tVAC);

            double tVBC = (zMin - b.getZ()) / (c.getZ() - b.getZ());
            Vertex vbc = lerp.lerp(b, c, tVBC);

            triangleRasterizer.rasterizer(a, b, vac);
            triangleRasterizer.rasterizer(vac, vbc, b);
        }
        triangleRasterizer.rasterizer(a, b, c);
    }
    private boolean fastClip(Point3D p){
        return (p.getX() < -p.getW() && p.getX() > p.getW() ||
                p.getY() < -p.getW() && p.getY() > p.getW() ||
                p.getZ() < 0         && p.getZ() > p.getW()
        );
    }
    private void renderLine(Vertex a , Vertex b){

        //fast-clip
        if(fastClip(a.getPosition()) || fastClip(b.getPosition()))
            return;
        double zMin = 0;
        //ořezani
        if (a.getZ() > zMin) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < zMin)
            return;

        if (a.getZ() < zMin) {
            double t = (zMin - a.getZ()) / (b.getZ() - a.getZ());
            a = a.mul(1 - t).add(b.mul(t));
        }
        linerasterizer.rasterizer(a.dehomog(),b.dehomog());
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
