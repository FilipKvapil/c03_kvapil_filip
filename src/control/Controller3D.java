package control;

import raster.ImageBuffer;
import raster.ZBuffer;
import rasterizers.Linerasterizer;
import rasterizers.TriangleRasterizer;
import renderer.Renderer;
import solids.Arrow;
import solids.Axis.AxisX;
import solids.Axis.AxisY;
import solids.Axis.AxisZ;
import solids.Cube;
import solids.Pyramid;
import transforms.*;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {

    private final Panel panel;
    private final ZBuffer zBuffer;
    private final TriangleRasterizer triangleRasterizer;
    private final Linerasterizer linerasterizer;
    private Renderer renderer;

    //Axis
    private final AxisX axisX = new AxisX();
    private final AxisY axisY = new AxisY();
    private final AxisZ axisZ = new AxisZ();

    private final Pyramid pyramid = new Pyramid();
    private final Cube cube = new Cube();

    private int mouseX;
    private int mouseY;
    private int mouseButton;

    private boolean blCube = false;
    private boolean blPyramid = false;
    private int trans;

    //model = jednotková matice
    Mat4 model = new Mat4Identity();

    //Otočení
    Mat4 mat4RotXUp = new Mat4RotX(Math.PI / 20);
    Mat4 mat4RotYUp = new Mat4RotY(Math.PI / 20);
    Mat4 mat4RotZUP = new Mat4RotZ(Math.PI / 20);

    Mat4 mat4RotXDown = new Mat4RotX(-(Math.PI / 20));
    Mat4 mat4RotYDown = new Mat4RotY(-(Math.PI / 20));
    Mat4 mat4RotZDown = new Mat4RotZ(-(Math.PI / 20));


    //Posunutí
    Mat4 mat4Transl = new Mat4Identity();

    //Měřítko
    Mat4 mat4Scale;

    //Kamera
    Mat4 projPers;
    Mat4 projOrtho;

    //Pozice kamery
    Vec3D position = new Vec3D(-3, 1, 1);
    double azimuth = Math.toRadians(0);
    double zenith = Math.toRadians(0);
    Camera camera = new Camera(
            position,
            //azimuth
            azimuth,
            //zenith
            zenith,
            1, true
    );

    //Konstruktor
    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.linerasterizer = new Linerasterizer(zBuffer);

        //Listener na tlačítka a boxy
        ActionListener listener = e -> {
            Object source = e.getSource();

            if (source == panel.getCBKrychle()) {
                blCube = !blCube;
            }else if(source == panel.getCBJehlan()){
                blPyramid = !blPyramid;
            } else if (source == panel.getCBKamera()) {
                if (panel.getCBKamera().getSelectedIndex() == 0) {
                    renderer.setProj(projPers);
                    position = new Vec3D(-3, 1, 1);
                } else if (panel.getCBKamera().getSelectedIndex() == 1) {
                    renderer.setProj(projOrtho);
                    position = new Vec3D(-.1, -.2, 0);
                }
                cameraSetPosition();
            } else if (source == panel.getCBTrans()) {
                trans = panel.getCBTrans().getSelectedIndex();
            } else if (source == panel.getJBReset()) {
                position = new Vec3D(-3, 1, 1);
                azimuth = Math.toRadians(0);
                zenith = Math.toRadians(0);
                cameraSetPosition();
            }
            displey();
            panel.grabFocus();
        };

        panel.getCBKrychle().addActionListener(listener);
        panel.getCBJehlan().addActionListener(listener);
        panel.getCBKamera().addActionListener(listener);
        panel.getCBTrans().addActionListener(listener);
        panel.getJBReset().addActionListener(listener);
        initObjects(panel.getRaster());
        initListeners(panel);
        displey();
    }

    //inicializace objektů
    public void initObjects(ImageBuffer raster) {
        trans = panel.getCBTrans().getSelectedIndex();


        projOrtho = new Mat4OrthoRH(5, 5, 0.1f, 200f);
        projPers = new Mat4PerspRH(Math.toRadians(60), raster.getHeight() / (float) raster.getWidth(), 0.1f, 200f);
        renderer = new Renderer(triangleRasterizer, linerasterizer, projPers, camera.getViewMatrix());
    }


    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            //Stisknutí tlačítka myši
            @Override
            public void mousePressed(MouseEvent e) {
                mouseButton = e.getButton();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {

            //Stisknutí tlačítka myši a přetažení
            public void mouseDragged(MouseEvent e) {

                if (panel.isKameraPohyb() && mouseButton == MouseEvent.BUTTON1) {
                    if (e.getX() > mouseX) {
                        azimuth -= Math.toRadians(.5);
                        cameraSetPosition();
                    } else if (e.getX() < mouseX ) {
                        azimuth += Math.toRadians(.5);
                        cameraSetPosition();
                    }
                    if (e.getY() > mouseY) {
                        zenith -= Math.toRadians(.5);
                        cameraSetPosition();
                    } else if (e.getY() < mouseY) {
                        zenith += Math.toRadians(.5);
                        cameraSetPosition();
                    }
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (panel.isChuze()) {
                    if (keyCode == KeyEvent.VK_W) {
                        position = new Vec3D(position.getX() + .1, position.getY(), position.getZ());
                        cameraSetPosition();
                    } else if (keyCode == KeyEvent.VK_S) {
                        position = new Vec3D(position.getX() - .1, position.getY(), position.getZ());
                        cameraSetPosition();
                    } else if (keyCode == KeyEvent.VK_A) {
                        position = new Vec3D(position.getX(), position.getY() + .1, position.getZ());
                        cameraSetPosition();
                    } else if (keyCode == KeyEvent.VK_D) {
                        position = new Vec3D(position.getX(), position.getY() - .1, position.getZ());
                        cameraSetPosition();
                    }
                }
                if (keyCode == KeyEvent.VK_R) {
                    position = new Vec3D(-3, 1, 1);
                    azimuth = Math.toRadians(0);
                    zenith = Math.toRadians(0);
                } else if (keyCode == KeyEvent.VK_NUMPAD4) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(.1, 0, 0);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotXUp);
                        case 2 -> {
                            mat4Scale = new Mat4Scale(1.1, 1.1, 1.1);
                            model = model.mul(mat4Scale);
                        }
                    }
                } else if (keyCode == KeyEvent.VK_NUMPAD1) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(-.1, 0, 0);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotXDown);
                        case 2 -> {
                            mat4Scale = new Mat4Scale(.9, .9, .9);
                            model = model.mul(mat4Scale);
                        }
                    }
                } else if (keyCode == KeyEvent.VK_NUMPAD5) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(0, .1, 0);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotYUp);
                    }
                } else if (keyCode == KeyEvent.VK_NUMPAD2) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(0, -.1, 0);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotYDown);
                    }
                } else if (keyCode == KeyEvent.VK_NUMPAD6) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(0, 0, -.1);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotZUP);
                    }
                } else if (keyCode == KeyEvent.VK_NUMPAD3) {
                    switch (trans) {
                        case 0 -> {
                            mat4Transl = new Mat4Transl(0, 0, .1);
                            model = model.mul(mat4Transl);
                        }
                        case 1 -> model = model.mul(mat4RotZDown);
                    }
                }
                displey();
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void displey() {
        panel.clear();

        if(blPyramid && blCube){
            renderer.render(cube,pyramid);
        }else if(blCube){
            renderer.render(cube);
        }else if(blPyramid){
            renderer.render(pyramid);
        }
        renderer.render(axisX, axisY, axisZ);
        panel.repaint();
    }

    private void cameraSetPosition() {
        camera = new Camera(camera, position);
        camera = new Camera(camera, azimuth, zenith);

        renderer.setView(camera.getViewMatrix());
        displey();
    }
}
