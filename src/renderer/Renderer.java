package renderer;

import model.Part;
import model.Vertex;
import raster.TriangleRasterizer;
import solids.Solid;
import utils.Lerp;

import java.util.List;

//TODO: přidat interface
public class Renderer {
    //rasterizery - linerasterizer , pointraterizer, trianglerasterizer
    private final TriangleRasterizer triangleRasterizer;
    private final Lerp<Vertex> lerp;

    public Renderer(TriangleRasterizer triangleRasterizer) {
        this.triangleRasterizer = triangleRasterizer;
        lerp = new Lerp<>();
    }

    public void render (Solid solid){
        for (Part part :
                solid.getPartBuffer()) {
            switch(part.getType()) {
                case TRIANGLE:
                    int start = part.getIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start+1));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(start+2));

                        renderTriangle(a,b,c);
                    }
                    break;
                case LINE:
                    //TODO: implementovat
                    break;
                case LINE_STRIP:
                    // code block
                    break;
                case POINT:
                    // code block
                    break;
                case TRIANGLE_STRIP:
                    // code block
                    break;
                default:
                    // code block
            }
        }
    }
    public void render (List<Solid> scene){
        for (Solid solid :
                scene) {
            render(solid);
        }
    }
    private void renderTriangle(Vertex a , Vertex b , Vertex c){
        //TODO: fast-clip - slide 99 - vyhození celých objektů který jsou mimo
        if (a.getZ()> b.getZ()){
            Vertex help = a;
            a = b;
            b = help;
        }
        if (b.getZ()> c.getZ()){
            Vertex help = b;
            b = c;
            c = help;
        }
        if (a.getZ()> b.getZ()){
            Vertex help = a;
            a = b;
            b = help;
        }
        double zMin = 0;
        if(a.getZ() < zMin)
            return;

        if(b.getZ()<zMin){
            double tVAB = (zMin-a.getZ())/(b.getZ()-a.getZ());
            Vertex vab = lerp.lerp(a,b,tVAB);

            double tVAC = (zMin-a.getZ())/(c.getZ()-a.getZ());
            Vertex vac = lerp.lerp(a,c,tVAC);

            triangleRasterizer.rasterizer(a,vab,vac);
        }
        if(c.getZ()<zMin){
            double tVAB = (zMin-b.getZ())/(c.getZ()-b.getZ());
            Vertex vab = lerp.lerp(a,b,tVAB);

            double tVAC = (zMin-a.getZ())/(c.getZ()-a.getZ());
            Vertex vac = lerp.lerp(a,c,tVAC);


            triangleRasterizer.rasterizer(a,b,vac);
            triangleRasterizer.rasterizer(a,vab,vac);
        }



        triangleRasterizer.rasterizer(a,b,c);
    }

}
