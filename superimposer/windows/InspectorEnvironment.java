package superimposer.windows;

import superimposer.Superimposer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class InspectorEnvironment extends JFrame implements MouseListener, MouseMotionListener {

    protected BufferedImage canvas;
    protected Graphics2D graphics;
    protected int x, y, ox, oy, w, h;

    public InspectorEnvironment(int w, int h) {
        super();
        this.w = w;
        this.h = h;
        this.canvas = new BufferedImage(Superimposer.w, Superimposer.h, BufferedImage.TYPE_INT_RGB);
        InspectorEnvironment instance = this;
        setContentPane(new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                graphics = canvas.createGraphics();
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, Superimposer.w, Superimposer.h);
                draw();
                g.drawImage(canvas, -instance.getLocation().x, -instance.getLocation().y, this);
                graphics.dispose();
                repaint();
            }
        });
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void run() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(w, h);
        setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void draw() {

    }

    public void image(Image img, int x, int y) {
        graphics.drawImage(img, x, y, this);
    }

    public void image(Image img, int x, int y, int w, int h) {
        graphics.drawImage(img, x, y, w, h, this);
    }

    public void rect(int x, int y, int w, int h) {
        graphics.fillRect(x, y, w, h);
    }

    public void font(int size) {
        graphics.setFont(new Font("MONOSPACED", Font.PLAIN, size));
    }

    public void text(String text, int x, int y) {
        graphics.drawString(text, x, y);
    }

    public void color(Color color) {
        graphics.setColor(color);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            ox = e.getX();
            oy = e.getY();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            ox = e.getX();
            oy = e.getY();
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
            x = e.getX() - ox;
            y = e.getY() - oy;
            setLocation(getLocation().x + x, getLocation().y + y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
