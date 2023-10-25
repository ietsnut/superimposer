package superimposer.notation;

import superimposer.Superimposer;

import java.awt.image.BufferedImage;
import java.nio.channels.AsynchronousSocketChannel;

public class Association extends Unit {

    // LOCATION
    int x, y, z;

    // SCALE
    int s;

    // ROTATION
    int r;

    // ANIMATION
    int vx, vy, vz, ox, oy, oz, dx, dy, dz;

    // CONTENT
    Unit[] units;

    public Association(BufferedImage image) {
        super(image);
    }

    public void draw() {

    }

    // LOCATION
    public Association at(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public Association by(Association association) {
        this.x = association.x;
        this.y = association.y;
        return this;
    }
    public Association on(int z) {
        this.z = z;
        return this;
    }
    public Association over(Association association) {
        this.z = association.z + 1;
        return this;
    }
    public Association under(Association association) {
        this.z = association.z - 1;
        return this;
    }

    // SCALE
    public Association in(int s) {
        this.s = s;
        return this;
    }

    // ROTATION
    public Association around(int r) {
        this.r = r;
        return this;
    }
    public Association around(Association association) {
        this.r = association.r;
        return this;
    }

    // ANIMATION
    public Association from(int ox, int oy) {
        this.ox = ox;
        this.oy = oy;
        return this;
    }
    public Association to(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }
    public Association between(Association association1, Association association2) {
        this.ox = association1.ox;
        this.oy = association1.oy;
        this.dx = association2.dx;
        this.dy = association2.dy;
        return this;
    }

    // CONTENT
    public Association of(Unit[] units) {
        this.units = units;
        return this;
    }
    public Association of(Association association) {
        this.units = association.units;
        return this;
    }

    // THIS
    public Association like(Association association) {
        this.image = association.image;
        this.x = association.x;
        this.y = association.y;
        this.z = association.z;
        this.vx = association.vx;
        this.vy = association.vy;
        this.vz = association.vz;
        this.s = association.s;
        this.r = association.r;
        this.units = association.units;
        return this;
    }

    // GENERATOR
    public Association among(Association[] associations) {
        // GENERATOR
        return this;
    }

    // GENERATOR
    public Association around(Association[] associations) {
        // GENERATOR
        return this;
    }

    // GENERATOR
    public Association out(Association[] associations) {
        // GENERATOR
        return this;
    }

}
