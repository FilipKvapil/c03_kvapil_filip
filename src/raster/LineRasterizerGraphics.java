package raster;


import model.Line;

import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer {


    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    public void drawLine(int x1, int y1, int x2, int y2,Color color) {
        Graphics g = ((RasterBufferedImage)raster).getImg().getGraphics();
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
    public void drawLine(Line line,Color color) {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(),color);
    }


}
