package superimposer.environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.*;

public class DevelopmentEnvironment extends Environment implements KeyListener {

    StringBuilder code = new StringBuilder();

    Environment keyboard;
    Environment console;

    public DevelopmentEnvironment(int w, int h) {
        super(w, h);
        addKeyListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, w, h, 50, 50));
            }
        });
        run();
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("MONOSPACED", Font.PLAIN, getWidth()/10));
        graphics.drawString(code.toString(), getWidth()/50, (int) (getHeight()/1.25));
    }

    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar == KeyEvent.VK_BACK_SPACE) {
            if (code.length() > 0) {
                code.deleteCharAt(code.length() - 1);
            }
        } else if (code.length() < 16) {
            code.append(keyChar);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
