package superimposer.windows;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Keyboard extends Window {

    ArrayList<Button> buttons = new ArrayList<>();

    public Keyboard() {
        super(500, 500, new RoundRectangle2D.Double(0, 0, 500, 500, 100, 100));
        for (int x = 50; x <= getWidth() - 100; x += 100) {
            for (int y = 50; y <= getHeight() - 100; y += 100) {
                buttons.add(new Button(x, y));
            }
        }
    }

    @Override
    public void draw() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.WHITE);
        graphics.drawRoundRect(25, 25, getWidth() - 50, getHeight() - 50, 50, 50);
        for(Button button : buttons) {
            graphics.setStroke(new BasicStroke(1));
            graphics.setColor(Color.WHITE);
            graphics.fillRoundRect(button.x, button.y, 100, 100, 50, 50);
            graphics.setColor(Color.BLACK);
            graphics.drawRoundRect(button.x, button.y, 100, 80, 50, 50);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    class Button {
        int x, y;
        public Button(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

}
