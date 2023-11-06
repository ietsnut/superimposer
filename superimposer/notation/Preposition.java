package superimposer.notation;

import java.awt.*;

public class Preposition extends Unit {
    Cardinality cardinality;
    public Preposition(Image image, Cardinality cardinality) {
        super(image);
        this.cardinality = cardinality;
    }
}
