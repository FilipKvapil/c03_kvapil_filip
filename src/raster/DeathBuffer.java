package raster;

public class DeathBuffer implements Raster<Double>{
    private final double[][] buffer;
    private final int width, height;
    private double defaultValue = 1;

    public DeathBuffer(int width , int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[this.width][this.height];
        clear();
    }
    @Override
    public void clear() {
        // nastaví celý buffer na defaultní hodnoty
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer[i][j] = defaultValue;
            }
        }
    }
    @Override
    public void setClearValue(Double value) {
        this.defaultValue = value;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Double getValue(int x, int y) {

        return buffer[x][y] ;
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if(isValid(x,y))
            buffer[x][y] = value;
    }
}
