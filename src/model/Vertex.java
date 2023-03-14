package model;

import transforms.*;

public class Vertex implements Vectorizable<Vertex>{
    private Point3D position;
    private Col color;
    private double one = 1 ;

    public Vertex(){}

    public Vertex(double x, double y, double z, Col color){
        this.position = new Point3D(x,y,z);
        this.color = color;
    }
    public Vertex(Point3D position, Col color){
        this.position = position;
        this.color = color;
    }

    private Vertex(Point3D position, Col color, double one) {
        this.position = position;
        this.color = color;
        this.one = one;
    }
    public double getX(){
        return position.x;
    }
    public double getY(){
        return position.y;
    }
    public double getW(){
        return position.w;
    }
    public double getZ(){
        return position.z;
    }

    public Point3D getPosition(){
        return position ;
    }
    public Col getColor() {
        return color;
    }
    @Override
    public Vertex mul(double k) {
        Col col = color.mul(k);
        return new Vertex(position.mul(k),col);
    }
    @Override
    public Vertex add(Vertex vertex) {
        Col col = color.add(vertex.getColor());
        return new Vertex(vertex.position.add(position),col);
    }
    public double getOne() {
        return one;
    }

    public Vertex resetOne(){
        return new Vertex(position,color);
    }

    public Vertex transform (Mat4 matice){
        return new Vertex(position.mul(matice),color);
    }
    public Vertex dehomog (){
        return new Vertex((position.mul(1/ position.w)),color,one);
    }
    public Vertex transformToScreen (int width,int height){
        return new Vertex(new Point3D(position.ignoreW()
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width-1)/2.,(height-1)/2.,1))),color,one);

    }




}
