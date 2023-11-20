package superimposer.notation;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class Unit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    public final ImageIcon icon;
    public int x;
    public int y;

    public Unit(ImageIcon icon, int x, int y) {
        this.icon = icon;
        this.x = x;
        this.y = y;
    }

}