package superimposer.windows;

import superimposer.notation.Association;

import java.awt.*;

public class AssociationWindow extends Window {

    Association association;
    Image background;
    Image border;
    Shape shape;

    public AssociationWindow(int w, int h, int screen, Shape shape) {
        super(w, h, screen, shape);
    }
}
