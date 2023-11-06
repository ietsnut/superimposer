package superimposer.windows;

import superimposer.Superimposer;
import superimposer.notation.Preposition;
import superimposer.notation.Unit;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Code extends Window implements KeyListener {

    ArrayList<Unit> units = new ArrayList<>();
    static Image unit = Superimposer.bimage("unit.png").getScaledInstance(300, 150, Image.SCALE_SMOOTH);

    public Code() {
        super(1500, 150, new RoundRectangle2D.Double(0, 0, 1500, 150, 150, 150));
        addKeyListener(this);
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.WHITE);
        int i = 0;
        for (Unit unit : units) {
            if (unit instanceof Preposition) {
                graphics.drawImage(unit.image, i * 300, 0, this);
            } else {
                graphics.drawImage(unit.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH), (i * 300) + 100, 25, 100, 100, this);
                graphics.drawImage(Code.unit, i * 300, 0, this);
            }
            i++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar == KeyEvent.VK_BACK_SPACE) {
            if (!units.isEmpty()) {
                units.remove(units.size() - 1);
                repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocus();
    }


    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
