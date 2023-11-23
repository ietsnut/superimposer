package superimposer.notation;

import javax.swing.*;
import java.io.*;

public class Association extends Unit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    public Unit[] units;
    public final ImageIcon background;

    public Association(ImageIcon icon, int x, int y, ImageIcon background, Unit... units) {
        super(icon, x, y);
        this.units = units;
        this.background = background;
    }

}
