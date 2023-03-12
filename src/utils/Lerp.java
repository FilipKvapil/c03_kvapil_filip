package utils;

import model.Vectorizable;

// řeší interpolaci
    public class Lerp<V extends Vectorizable<V>> {
    public V lerp(V v1, V v2, double t){
        return v1.mul(1-t) . add(v2.mul(t));
    }
}