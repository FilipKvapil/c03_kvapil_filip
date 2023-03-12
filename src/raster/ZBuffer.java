package raster;

public class ZBuffer {
    private final ImageBuffer imageBuffer;
    private final DeathBuffer deathBuffer;

    public ZBuffer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.deathBuffer = new DeathBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }
    public void drawWithTest (int x,int y,double z,Col color){
        if (deathBuffer.getValue(x,y) > z){
            imageBuffer.setValue(x,y,color);
            deathBuffer.setValue(x,y,z);
        }
    }
    public ImageBuffer getImageBuffer(){
        return imageBuffer;
    }
}
