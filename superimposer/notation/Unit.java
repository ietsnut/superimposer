package superimposer.notation;

import javax.swing.*;
import java.io.*;

public class Unit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    public final ImageIcon icon;

    public Unit(ImageIcon icon) {
        this.icon = icon;
    }

    // RELATION
    public Unit[] contain;
    public Unit[] extend;
    public Unit[] vary;

    // POSITION
    public Cardinality position = Cardinality.STATIC;
    public Unit relative;

    // INTERACTION
    public boolean collide = false;
    public boolean collect = false;
    public boolean drag = false;

    // DIRECTION
    public int up = 0;
    public int down = 0;
    public int left = 0;
    public int right = 0;

    // TRANSITION
    public boolean wander = false;
    public boolean hover = false;
    public boolean sway = false;
    public boolean rotate = false;
    public boolean spin = false;
    public boolean decay = false;
    public Unit morph;
    public Unit[] animate;

    //

    public Unit contain(Unit... contain) {
        this.contain = contain;
        return this;
    }

    public Unit extend(Unit... extend) {
        this.extend = extend;
        return this;
    }

    public Unit vary(Unit... vary) {
        this.vary = vary;
        return this;
    }

    public Unit position(Cardinality position) {
        this.position = position;
        return this;
    }

    public Unit relative(Unit relative) {
        this.position = Cardinality.RELATIVE;
        this.relative = relative;
        return this;
    }

    public Unit collide() {
        this.collide = !this.collide;
        return this;
    }

    public Unit collect() {
        this.collect = !this.collect;
        return this;
    }

    public Unit drag() {
        this.drag = !this.drag;
        return this;
    }

    public Unit up() {
        this.up++;
        return this;
    }

    public Unit down() {
        this.down++;
        return this;
    }

    public Unit left() {
        this.left++;
        return this;
    }

    public Unit right() {
        this.right++;
        return this;
    }

    public Unit wander() {

        return this;
    }



}