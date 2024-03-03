package superimposer.vision.perception.toolbox;

public class Vector3i {

    public int x, y, z;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i add(Vector3i other) {
        return new Vector3i(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3i subtract(Vector3i other) {
        return new Vector3i(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3i scale(int scalar) {
        return new Vector3i(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3i mul(Vector3i other) {
        return new Vector3i(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public double len() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public int dot(Vector3i other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3i cross(Vector3i other) {
        return new Vector3i(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public Vector3i slerp(Vector3i target, double alpha) {
        double dot = this.dot(target);
        dot = Math.max(-1.0, Math.min(1.0, dot));

        double theta = Math.acos(dot) * alpha;
        Vector3i relative = target.subtract(this.scale((int) dot)).normalize();
        return this.scale((int) Math.cos(theta)).add(relative.scale((int) Math.sin(theta)));
    }

    public Vector3i normalize() {
        double length = len();
        if (length != 0) {
            double invLength = 1.0 / length;
            return new Vector3i((int) (x * invLength), (int) (y * invLength), (int) (z * invLength));
        } else {
            return new Vector3i(0, 0, 0);
        }
    }
}
