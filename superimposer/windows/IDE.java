package superimposer.windows;

import superimposer.Superimposer;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class IDE extends Window implements KeyListener {

    StringBuilder code = new StringBuilder();

    public IDE() {
        super(1500, 150, new RoundRectangle2D.Double(0, 0, 1500, 150, 150, 150));
        addKeyListener(this);
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.WHITE);
        graphics.drawRoundRect(25, 25, getWidth() - 50, getHeight() - 50, 100, 100);
        graphics.setFont(new Font("MONOSPACED", Font.PLAIN, getWidth()/10));



        graphics.drawString(code.toString(), getWidth()/50, (int) (getHeight()/1.25));
    }

    @Override
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
