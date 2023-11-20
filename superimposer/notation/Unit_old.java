package superimposer.notation;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class Unit_old implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private final ImageIcon icon;
    private final Wave t;
    private final Wave x;
    private final Wave y;
    private final Wave z;
    private final Wave s;
    private final Wave r;

    public Unit_old(ImageIcon icon, Wave t, Wave x, Wave y, Wave z, Wave s, Wave r) {
        this.icon = icon;
        this.t = t;
        this.x = x;
        this.y = y;
        this.z = z;
        this.s = s;
        this.r = r;
    }

    public ImageIcon icon() {
        return this.icon;
    }

    private float value(Wave wave) {
        return wave.getValue(this.t());
    }

    public float t() {
        return this.t.getValue(System.currentTimeMillis());
    }

    public float x() {
        return this.value(this.x);
    }

    public float y() {
        return this.value(this.y);
    }

    public float z() {
        return this.value(this.z);
    }

    public float s() {
        return this.value(this.s);
    }

    public float r() {
        return this.value(this.r);
    }

}

