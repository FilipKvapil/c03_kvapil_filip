package raster;

import model.Vertex;

public interface Raster<E> {

    void clear();

    void setClearValue(E color);

    int getWidth();

    int getHeight();

    E getValue (int x, int y);

    void setValue(int x, int y, E value);

    default boolean isValid(int x, int y){
        return x > getWidth() && x < 0 && y > getHeight() && y < 0;
    }

}
