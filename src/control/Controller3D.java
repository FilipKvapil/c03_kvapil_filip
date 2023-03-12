package control;

import solids.Axis.AxisX;
import solids.Axis.AxisY;
import solids.Axis.AxisZ;
import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import renderer.WireRenderer;
import transforms.*;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {

    private final Panel panel;

    private WireRenderer wireRenderer;

    //Objekty
    private final Cube cube = new Cube();
    private final Hexahonal hexahonal = new Hexahonal();
    private final Pyramid pyramid = new Pyramid();
    //Křivky
    private final CurveFerg curveFerg = new CurveFerg();
    private final CurveBezi curveBezi = new CurveBezi();
    private final CurveCoon curveCoon = new CurveCoon();
    //Axis
    private final AxisX axisX = new AxisX();
    private final AxisY axisY = new AxisY();
    private final AxisZ axisZ = new AxisZ();

    private int mouseX;
    private int mouseY;
    private int mouseButton;

    private int object;
    private int krivka;
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
        //Listener na tlačítka a boxy
        ActionListener listener = e -> {
            Object source = e.getSource();
            if (source == panel.getCBKrivka()) {
                krivka = panel.getCBKrivka().getSelectedIndex();
            } else if (source == panel.getCBKamera()) {
                if (panel.getCBKamera().getSelectedIndex() == 0) {
                    wireRenderer.setProj(projPers);
                    position = new Vec3D(-3, 1, 1);
                } else if (panel.getCBKamera().getSelectedIndex() == 1) {
                    wireRenderer.setProj(projOrtho);
                    position = new Vec3D(-.1, -.2, 0);
                }
                cameraSetPosition();
            } else if (source == panel.getCBTrans()) {
                trans = panel.getCBTrans().getSelectedIndex();
            } else if (source == panel.getCBObjekt()) {
                object = panel.getCBObjekt().getSelectedIndex();
            } else if (source == panel.getJBReset()) {
                position = new Vec3D(-3, 1, 1);
                azimuth = Math.toRadians(0);
                zenith = Math.toRadians(0);
                cameraSetPosition();
            }
            displey();
            panel.grabFocus();
        };

        panel.getCBKamera().addActionListener(listener);
        panel.getCBKrivka().addActionListener(listener);
        panel.getCBObjekt().addActionListener(listener);
        panel.getCBTrans().addActionListener(listener);
        panel.getJBReset().addActionListener(listener);
        initObjects(panel.getRaster());
        initListeners(panel);
        displey();
    }

    //inicializace objektů
    public void initObjects(Raster raster) {
        krivka = panel.getCBKrivka().getSelectedIndex();
        object = panel.getCBObjekt().getSelectedIndex();
        trans = panel.getCBTrans().getSelectedIndex();

        LineRasterizerGraphics lineRasterizer = new LineRasterizerGraphics(raster);
        projOrtho = new Mat4OrthoRH(5, 5, 0.1f, 200f);
        projPers = new Mat4PerspRH(Math.toRadians(60), raster.getHeight() / (float) raster.getWidth(), 0.1f, 200f);
        wireRenderer = new WireRenderer(raster, lineRasterizer, projPers, camera.getViewMatrix());
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
                    if (e.getX() > mouseX && azimuth > -Math.toRadians(90)) {
                        azimuth -= Math.toRadians(.5);
                        cameraSetPosition();
                    } else if (e.getX() < mouseX && azimuth < Math.toRadians(90)) {
                        azimuth += Math.toRadians(.5);
                        cameraSetPosition();
                    }
                    if (e.getY() > mouseY && zenith > -Math.toRadians(90)) {
                        zenith -= Math.toRadians(.5);
                        cameraSetPosition();
                    } else if (e.getY() < mouseY && zenith < Math.toRadians(90)) {
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
        switch (object) {
            case 0 -> {
                cube.setModel(model);
                wireRenderer.render(cube);
            }
            case 1 -> {
                pyramid.setModel(model);
                wireRenderer.render(pyramid);
            }
            case 2 -> {
                hexahonal.setModel(model);
                wireRenderer.render(hexahonal);
            }
        }
        switch (krivka) {
            case 0:
                curveFerg.setModel(model);
                wireRenderer.render(curveFerg);
                break;
            case 1:
                curveBezi.setModel(model);
                wireRenderer.render(curveBezi);
                break;
            case 2:
                curveCoon.setModel(model);
                wireRenderer.render(curveCoon);
                break;
        }


        wireRenderer.render(axisX, axisY, axisZ);
        panel.repaint();
    }

    private void cameraSetPosition() {
        camera = new Camera(camera, position);
        camera = new Camera(camera, azimuth, zenith);
        wireRenderer.setView(camera.getViewMatrix());
        displey();
    }

}
