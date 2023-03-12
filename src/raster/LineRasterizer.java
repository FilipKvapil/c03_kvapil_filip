package raster;

import java.awt.*;

public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void setColor(int color) {
        this.color = new Color(color);
    }

    protected void drawLine(int x1, int y1, int x2, int y2) {

    }
}
