package superimposer.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame implements MouseListener, MouseMotionListener {

    BufferedImage canvas;
    Graphics2D graphics;
    int x, y;

    public Window(int w, int h, Shape shape) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setTitle(this.getClass().getSimpleName());
        setShape(shape);
        setSize(w, h);
        setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
        canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        graphics = canvas.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, w, h);
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (!Thread.currentThread().isInterrupted()) {
                    repaint();
                }
            }
        }).start();
    }

    public void draw() {

    }

    @Override
    public void paint(Graphics g) {
        draw();
        g.drawImage(canvas, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int x2 = e.getX() - x;
            int y2 = e.getY() - y;
            setLocation(getLocation().x + x2, getLocation().y + y2);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
