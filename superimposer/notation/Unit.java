package superimposer.notation;

import javax.swing.*;
import java.io.*;

public class Unit extends Cardinality implements Serializable  {

    @Serial
    private static final long serialVersionUID = 1;

    public final Object[][] prepositions;

    public Unit(ImageIcon icon) {
        super(icon);
        this.prepositions = new Object[4][9];
    }

    public Unit set(int cardinality, int preposition) {
        prepositions[cardinality][preposition] = true;
        return this;
    }

    public Unit set(int cardinality, int preposition, Unit... to) {
        prepositions[cardinality][preposition] = to;
        return this;
    }

    public Object get(int cardinality, int preposition) {
        return prepositions[cardinality][preposition];
    }

}