package superimposer.vision.perception.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera2 {

    public Vector3f position = new Vector3f(0, 0, 0);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public Matrix4f viewMatrix = new Matrix4f();
    public Matrix4f positionMatrix = new Matrix4f();

    private float translationSpeed = 0.2f;
    private float rotationSpeed = 5f;

    public void move() {
        final float relativeRotation = (float) Math.toRadians(rotation.y);
        rotation.y += Mouse.getDX() / rotationSpeed;
        rotation.x -= Mouse.getDY() / rotationSpeed;
        if (rotation.x >= 90) {
            rotation.x = 90;
        }
        else if (rotation.x <= -90) {
            rotation.x = -90;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z += -(float) Math.cos(relativeRotation) * translationSpeed;
            position.x += (float) Math.sin(relativeRotation) * translationSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z -= -(float) Math.cos(relativeRotation) * translationSpeed;
            position.x -= (float) Math.sin(relativeRotation) * translationSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.z += (float) Math.sin(relativeRotation) * translationSpeed;
            position.x += (float) Math.cos(relativeRotation) * translationSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.z -= (float) Math.sin(relativeRotation) * translationSpeed;
            position.x -= (float) Math.cos(relativeRotation) * translationSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            //rotation.z += -(float) Math.cos(relativeRotation) * translationSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            //rotation.z += (float) Math.sin(relativeRotation) * translationSpeed;
        }
        viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), viewMatrix);
        viewMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
    }
}
