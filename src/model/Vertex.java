package model;

import transforms.*;

//TODO: vymyslet nějaký interface(parametrický)
public class Vertex implements Vectorizable<Vertex>{
    private Point3D position;
    private Col color;
    private Vec3D normal;
    private Vec2D texUV;
    private double one;

    public Vertex(){}

    public Vertex(double x, double y, double z, Col color){
        this.position = new Point3D(x,y,z);
        this.color = color;
        this.one = 1;
        normal();
    }
    public Vertex(Point3D position, Col color){
        this.position = position;
        this.color = color;
        this.one = 1;
        normal();
    }
    public Vertex(Point3D position, Col color,Vec3D normal){
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.one = 1;
    }
    public Vertex(Point3D position, Col color,Vec3D normal,Vec2D texUV){
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.texUV = texUV;
        this.one = 1;
    }

    private Vertex(Point3D position, Col color, Vec2D texUV, double one) {
        this.position = position;
        this.color = color;
        this.texUV = texUV;
        this.one = one;
        normal();
    }
    public double getX(){
        return position.x;
    }
    public double getY(){
        return position.y;
    }
    public double getw(){
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
        return new Vertex(position.mul(k),color);
    }
    @Override
    public Vertex add(Vertex vertex) {
        return new Vertex(vertex.position.add(position),color);
    }
    private void normal (){

    }

    public double getOne() {
        return one;
    }

    public Vec3D getNormal() {
        return normal;
    }
    public Vertex resetOne(){
        return new Vertex(position,color,normal,texUV);
    }

    public Vertex transform (Mat4 matice){
        return new Vertex(position.mul(matice),color);
        //TODO: normála - pozor na transformaci normály je potřeba ji transponovat
    }
    public Vertex dehomog (){
        return null;
        //TODO: dehomogenizace - nový vertex
    }
    public Vertex transformToScreen (int width,int height){
        return new Vertex(new Point3D(position.ignoreW()
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width-1)/2.,(height-1)/2.,1))),color,texUV,one);

    }




}
