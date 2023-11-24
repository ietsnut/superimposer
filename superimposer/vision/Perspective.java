package superimposer.vision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

public abstract class Perspective extends JFrame implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    protected BufferStrategy buffer;
    protected Graphics2D graphics;
    protected int x, y;

    public Perspective(int w, int h, Shape shape) {
        System.setProperty("sun.java2d.opengl", "true");
        setShape(new Area(shape));
        setSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        pack();
        setUndecorated(true);
        setLocationRelativeTo(null);
        createBufferStrategy(2);
        this.buffer = getBufferStrategy();
        this.graphics = (Graphics2D) buffer.getDrawGraphics();
        this.graphics.setColor(Color.BLACK);
        this.graphics.fillRect(0, 0, w, h);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        toFront();
        setFocusable(true);
        setFocusableWindowState(true);
        setVisible(true);
        repaint();
    }

    public void draw() {

    }

    @Override
    public void paint(Graphics g) {
        try {
            this.graphics = (Graphics2D) buffer.getDrawGraphics();
            draw();
        } finally {
            this.graphics.dispose();
        }
        buffer.show();
        Toolkit.getDefaultToolkit().sync();
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
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int x2 = e.getX() - x;
            int y2 = e.getY() - y;
            setLocation(getLocation().x + x2, getLocation().y + y2);
        }
    }

}
