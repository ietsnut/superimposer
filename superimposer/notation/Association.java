package superimposer.notation;

import javax.swing.*;
import java.awt.*;

public class Association extends Unit {

    Unit[] units;

    public Association(ImageIcon icon, Wave t, Wave x, Wave y, Wave z, Wave s, Wave r, Unit[] units) {
        super(icon, t, x, y, z, s, r);
        this.units = units;
    }

    public Unit[] getUnits() {
        return units;
    }
}
