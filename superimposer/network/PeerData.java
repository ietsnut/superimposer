package superimposer.network;

import superimposer.notation.*;
import java.io.*;

public class PeerData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private int id;
    private final float x;
    private final float y;
    private final Unit unit;
    private final Unit[] units;

    public PeerData(float x, float y, Unit unit, Unit... units) {
        this.x = x;
        this.y = y;
        this.unit = unit;
        this.units = units;
    }

    public PeerData setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public Unit[] getUnits() {
        return units;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Unit getUnit() {
        return unit;
    }
}
