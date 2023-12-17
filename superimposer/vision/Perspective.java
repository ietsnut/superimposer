package superimposer.vision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import superimposer.library.Image;

public class Perspective extends JFrame implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    private Canvas canvas;
    private Thread thread;
    protected int x, y;
    protected final int w, h;
    public boolean up, down, left, right;
    double FPS = 60;

    public Perspective(int w, int h, int b) {
        this.w = w;
        this.h = h;
        EventQueue.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("sun.java2d.uiScale", "1");
            setUndecorated(true);
            this.canvas = new Canvas(this, new Image("f"+b+".png").scale(w, h, RenderingHints.VALUE_INTERPOLATION_BILINEAR).image());
            this.thread = new Thread(this.canvas);
            this.thread.start();
            setBackground(new Color(0, 0, 0, 0));
            setContentPane(canvas);
            setup();
            pack();
            setLocationRelativeTo(null);
            addMouseListener(this);
            addMouseMotionListener(this);
            addKeyListener(this);
            addMouseWheelListener(this);
            toFront();
            setFocusable(true);
            setFocusableWindowState(true);
            setVisible(true);
        });
    }

    public void setup() {

    }

    private class Canvas extends JPanel implements Runnable {
        private final BufferedImage b;
        private final Perspective instance;

        private Canvas(Perspective instance, BufferedImage b) {
            super(true);
            this.instance = instance;
            this.b = b;
            setOpaque(false);
        }

        @Override
        public void run() {
            double drawInterval = 1e9/instance.FPS;
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;
            while(instance.thread != null) {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;
                if(delta >= 1){
                    instance.update();
                    repaint();
                    delta--;
                }
            }
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            instance.draw(g2d);
            g2d.drawImage(b, 0, 0, getWidth(), getHeight(), this);
        }

        @Override
        public Dimension getPreferredSize() {
            return b == null ? new Dimension(w, h) : new Dimension(b.getWidth(), b.getHeight());
        }
    }

    public void update() {

    }

    public void draw(Graphics2D graphics) {

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> this.up = true;
            case KeyEvent.VK_S -> this.down = true;
            case KeyEvent.VK_A -> this.left = true;
            case KeyEvent.VK_D -> this.right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> this.up = false;
            case KeyEvent.VK_S -> this.down = false;
            case KeyEvent.VK_A -> this.left = false;
            case KeyEvent.VK_D -> this.right = false;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
