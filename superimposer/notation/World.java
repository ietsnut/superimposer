package superimposer.notation;

import superimposer.windows.Window;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class World extends Window {

    Unit parent;
    Unit[] children;

    public World(int w, int h, int screen, Shape shape, Unit parent, Unit... children) {
        super(w, h, screen, shape);
        this.parent = parent;
        this.children = children;
    }

    @Override
    public void draw() {
        for (Unit child : children) {
            AffineTransform transform = new AffineTransform();
            transform.translate(child.x(), child.y());
            transform.rotate(child.r());
            transform.scale(child.s(), child.s());
            graphics.drawImage(child.icon().getImage(), transform, this);
        }
    }

}
