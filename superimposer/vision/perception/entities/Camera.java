package superimposer.vision.perception.entities;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import superimposer.vision.perception.terrain.Terrain;
import superimposer.vision.perception.toolbox.Vector3i;


public class Camera
{
    private final static float PI_OVER_180 = 0.0174532925f;

    public Vector3f position = new Vector3f();
    public Vector3f direction = new Vector3f();
    public Matrix4f viewMatrix = new Matrix4f();

    private float pitchAngle;
    private float bearingAngle;
    private float rollAngle;
    private final Quaternion pitch;
    private final Quaternion bearing;
    private final Quaternion roll;
    private final Quaternion rotation;

    private float sway = 0;

    public Camera()
    {
        pitch = new Quaternion();
        bearing = new Quaternion();
        roll = new Quaternion();
        rotation = new Quaternion();
        bearingAngle = 0;
        pitchAngle = 0;
        rollAngle = 0;
    }

    public Camera(float initialBearing, float initialPitch, float initialRoll)
    {
        pitch = new Quaternion();
        bearing = new Quaternion();
        roll = new Quaternion();
        rotation = new Quaternion();
        bearingAngle = initialBearing;
        pitchAngle = initialPitch;
        rollAngle = initialRoll;
    }

    public void reorient()
    {
        Quaternion.mul(roll, pitch, rotation);
        Quaternion.mul(rotation, bearing, rotation);
        Matrix4f pitchMatrix = convertQuaternionToMatrix4f(pitch);
        Quaternion temp = Quaternion.mul(bearing, pitch, null);
        Matrix4f matrix = convertQuaternionToMatrix4f(temp);
        direction.x = matrix.m20;
        direction.y = pitchMatrix.m21;
        direction.z = matrix.m22;
    }

    public void bearing(float bearingDelta)
    {
        bearingAngle += bearingDelta;
        bearing.setFromAxisAngle(new Vector4f(0f, 1f, 0f, bearingAngle * PI_OVER_180));
    }

    public void pitch(float pitchDelta)
    {
        pitchAngle += pitchDelta;
        pitch.setFromAxisAngle(new Vector4f(1f, 0f, 0f, pitchAngle * PI_OVER_180));
    }

    public void roll(float rollDelta)
    {
        rollAngle += rollDelta;
        roll.setFromAxisAngle(new Vector4f(0f, 0f, 1f, rollAngle * PI_OVER_180));
    }

    public void move(Terrain terrain) {
        bearing(Mouse.getDX() / 5f);
        pitch(Mouse.getDY() / -5f);
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            roll(0.5f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            roll(-0.5f);
        }
        if (Math.signum(rollAngle) == 0) {
            rollAngle = 0;
            roll.setFromAxisAngle(new Vector4f(0f, 0f, 1f, rollAngle * PI_OVER_180));
        } if (rollAngle < 0) {
            roll(0.25f);
        } else if (rollAngle > 0) {
            roll(-0.25f);
        }
        if (rollAngle > 3) {
            rollAngle = 3;
            roll.setFromAxisAngle(new Vector4f(0f, 0f, 1f, rollAngle * PI_OVER_180));
        } else if (rollAngle < -3) {
            rollAngle = -3;
            roll.setFromAxisAngle(new Vector4f(0f, 0f, 1f, rollAngle * PI_OVER_180));
        }
        reorient();
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= direction.z / 4;
            position.x += direction.x / 4;
            //position.y = (float) (Math.sin(System.currentTimeMillis() / 1000.0) * 0.1f);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z -= -direction.z / 4;
            position.x -= direction.x / 4;
            //position.y = (float) (Math.sin(System.currentTimeMillis() / 1000.0) * 0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            Vector3f cross = Vector3f.cross(direction, new Vector3f(0,1,0), null);
            position.z += cross.z / 4;
            position.x -= cross.x / 4;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            Vector3f cross = Vector3f.cross(direction, new Vector3f(0,1,0), null);
            position.z -= cross.z / 4;
            position.x += cross.x / 4;
        }
        position.y = terrain.getHeightOfTerrain(position.x, position.z) + 1.8f;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
        viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f rotMatrix = convertQuaternionToMatrix4f(rotation);
        Matrix4f.mul(rotMatrix, viewMatrix, viewMatrix);
        //Vector3i testPosition = new Vector3i((int) -position.x, (int) -position.y, (int) -position.z);
        //viewMatrix.translate(new Vector3f(testPosition.x, testPosition.y, testPosition.z));
        viewMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
    }

    private static Matrix4f convertQuaternionToMatrix4f(Quaternion q) {
        Matrix4f matrix = new Matrix4f();
        matrix.m00 = 1.0f - 2.0f * ( q.getY() * q.getY() + q.getZ() * q.getZ() );
        matrix.m01 = 2.0f * (q.getX() * q.getY() + q.getZ() * q.getW());
        matrix.m02 = 2.0f * (q.getX() * q.getZ() - q.getY() * q.getW());
        matrix.m03 = 0.0f;
        matrix.m10 = 2.0f * ( q.getX() * q.getY() - q.getZ() * q.getW() );
        matrix.m11 = 1.0f - 2.0f * ( q.getX() * q.getX() + q.getZ() * q.getZ() );
        matrix.m12 = 2.0f * (q.getZ() * q.getY() + q.getX() * q.getW() );
        matrix.m13 = 0.0f;
        matrix.m20 = 2.0f * ( q.getX() * q.getZ() + q.getY() * q.getW() );
        matrix.m21 = 2.0f * ( q.getY() * q.getZ() - q.getX() * q.getW() );
        matrix.m22 = 1.0f - 2.0f * ( q.getX() * q.getX() + q.getY() * q.getY() );
        matrix.m23 = 0.0f;
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        matrix.m33 = 1.0f;
        return matrix;
    }

}