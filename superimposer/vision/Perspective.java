package superimposer.vision;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

abstract class Perspective extends JFrame implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

    protected int X, Y;
    protected final int W, H;
    protected HashMap<Integer, Boolean> KEYS;
    protected final int FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();

    public Perspective(int W, int H) {
        this.W = W;
        this.H = H;
        this.KEYS = new HashMap<>();
        Field[] fields = KeyEvent.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                try {
                    KEYS.put(field.getInt(null), false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.uiScale", "1");
        setUndecorated(true);
        Canvas canvas = new Canvas(this, new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB));
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(canvas);
        pack();
        setLocationRelativeTo(null);
        setLocationRelativeTo(null);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        toFront();
        setFocusable(true);
        setFocusableWindowState(true);
        setVisible(true);
        new Thread(canvas).start();
    }

    private class Canvas extends JPanel implements Runnable {

        private final Perspective perspective;

        private Canvas(Perspective perspective, BufferedImage b) {
            super(true);
            this.perspective = perspective;
            //this.b = b;
            setOpaque(false);
        }

        @Override
        public void run() {
            long previousTime = System.nanoTime();
            long deltaTime = 0;
            long unprocessedTime = 0;
            long timePerFrame = 1000000000 / FPS;
            while (true) {
                long currentTime = System.nanoTime();
                deltaTime = currentTime - previousTime;
                previousTime = currentTime;
                unprocessedTime += deltaTime;
                while (unprocessedTime > timePerFrame) {
                    unprocessedTime -= timePerFrame;
                    perspective.update();
                }
                repaint();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            perspective.draw(g2d);
            //g2d.drawImage(b, 0, 0, getWidth(), getHeight(), this);
        }

        @Override
        public Dimension getPreferredSize() {
            //return b == null ? new Dimension(w, h) : new Dimension(b.getWidth(), b.getHeight());
            return new Dimension(W, H);
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
            X = e.getX();
            Y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            X = e.getX();
            Y = e.getY();
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
            int x2 = e.getX() - X;
            int y2 = e.getY() - Y;
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
        KEYS.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KEYS.put(e.getKeyCode(), false);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

}
