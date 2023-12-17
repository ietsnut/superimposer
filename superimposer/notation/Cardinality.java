package superimposer.notation;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class Cardinality implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    public final ImageIcon icon;

    public Cardinality(ImageIcon icon) {
        this.icon = icon;
    }

}
