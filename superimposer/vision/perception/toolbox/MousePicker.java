package superimposer.vision.perception.toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import superimposer.vision.perception.entities.Camera;
import superimposer.vision.perception.entities.Entity;
import superimposer.vision.perception.terrain.Terrain;
import superimposer.vision.perception.textures.TerrainTexture;

import java.util.List;

public class MousePicker {

    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    private List<Entity> entities;

    public MousePicker(Camera cam, Matrix4f projection, Terrain terrain, List<Entity> entities) {
        this.camera = cam;
        this.projectionMatrix = projection;
        this.viewMatrix = cam.viewMatrix;
        this.terrain = terrain;
        this.entities = entities;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public void update() {
        viewMatrix = camera.viewMatrix;
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay() {
        float mouseX = Display.getWidth() / 2f;
        float mouseY = Display.getHeight() / 2f;
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / Display.getWidth() - 1f;
        float y = (2.0f * mouseY) / Display.getHeight() - 1f;
        return new Vector2f(x, y);
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.position;
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            return endPoint;
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f testPoint) {
        float height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        return testPoint.y < height;
    }

    public Entity raycastEntities() {
        Vector3f rayOrigin = camera.position;
        Vector3f rayDirection = currentRay;
        for (Entity entity : entities) {
            if (rayIntersectsSphere(rayOrigin, rayDirection, entity.getPosition(), entity.getCollisionRadius())) {
                return entity; // Return the first entity that intersects
            }
        }
        return null; // Return null if no intersection with any entity
    }

    private boolean rayIntersectsSphere(Vector3f rayOrigin, Vector3f rayDirection, Vector3f sphereCenter, float radius) {
        // Normalize the ray direction vector
        rayDirection.normalise();

        // Vector from the ray's origin to the center of the sphere
        Vector3f rayToSphere = Vector3f.sub(sphereCenter, rayOrigin, null);

        // Project rayToSphere onto the ray's direction
        float projection = Vector3f.dot(rayToSphere, rayDirection);

        // If projection is negative, the sphere is behind the ray
        if (projection < 0) {
            return false;
        }

        // Calculate the distance from the sphere to the closest point on the ray
        Vector3f closestPoint = Vector3f.add(rayOrigin, (Vector3f) rayDirection.scale(projection), null);
        float distance = Vector3f.sub(closestPoint, sphereCenter, null).length();

        // If the distance is less than or equal to the sphere's radius, intersection occurs
        return distance <= radius;
    }

}
