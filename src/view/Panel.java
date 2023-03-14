package view;

import raster.ImageBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel {
    private ImageBuffer raster;

    private JCheckBox CHBKrychle, CHBJehlan;
    private JCheckBox CHBChuze, CHBKameraPohyb;

    private final String[] transformace= {"Posunutí","Otočení","Měřítko"};
    private final String[] kamera = {"Perspektivní","Ortogonální"};
    JComboBox CBTrans, CBKamera;

    private boolean krychle=false;
    private boolean jehlan=false;

    private boolean kameraPohyb=false;
    private boolean chuze=false;

    JButton JBReset;

    public ImageBuffer getRaster() {
        return raster;
    }

    private static final int FPS = 1000 / 20;
    public static final int WIDTH = 800, HEIGHT = 600;

    Panel() {
        CHBKrychle = new JCheckBox("Krychle");
        CHBKrychle.addActionListener(listener);
        this.add(CHBKrychle);

        CHBJehlan = new JCheckBox("Kamera");
        CHBJehlan.addActionListener(listener);
        this.add(CHBJehlan);

        CBTrans = new JComboBox(transformace);
        CBTrans.addActionListener(listener);
        this.add(CBTrans);

        CBKamera = new JComboBox(kamera);
        this.add(CBKamera);

        CHBKameraPohyb = new JCheckBox("Kamera");
        CHBKameraPohyb.addActionListener(listener);
        this.add(CHBKameraPohyb);

        CHBChuze = new JCheckBox("Chůze");
        CHBChuze.addActionListener(listener);
        this.add(CHBChuze);

        JBReset = new JButton("Reset Kamery");
        this.add(JBReset);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        raster = new ImageBuffer(WIDTH, HEIGHT);

        //hardClear();
        setLoop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public void resize(){
        if (this.getWidth()<1 || this.getHeight()<1)
            return;
        if (this.getWidth()<=raster.getWidth() && this.getHeight()<=raster.getHeight()) //no resize if new is smaller
            return;
        ImageBuffer newRaster = new ImageBuffer(this.getWidth(), this.getHeight());

        newRaster.draw(raster);
        raster = newRaster;
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        raster.clear();
    }

    public void hardClear(){

        CBKamera.setSelectedIndex(0);
        CBTrans.setSelectedIndex(0);
        CHBChuze.setSelected(false);
        CHBKameraPohyb.setSelected(false);
        CHBKrychle.setSelected(false);
        CHBJehlan.setSelected(false);

        kameraPohyb = false;
        chuze = false;

        this.clear();
    }
    private ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (CHBChuze.equals(source)) chuze = !chuze; else if (CHBKameraPohyb.equals(source)) kameraPohyb = !kameraPohyb;
            grabFocus();
        }
    };

    public boolean isKameraPohyb() {
        return kameraPohyb;
    }

    public boolean isChuze() {
        return chuze;
    }

    public JComboBox getCBTrans() {
        return CBTrans;
    }

    public JComboBox getCBKamera() {
        return CBKamera;
    }

    public JButton getJBReset(){return JBReset;}

    public JCheckBox getCBKrychle() {
        return CHBKrychle;
    }

    public JCheckBox getCBJehlan() {
        return CHBJehlan;
    }
}