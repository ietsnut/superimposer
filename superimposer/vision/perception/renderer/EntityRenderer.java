package superimposer.vision.perception.renderer;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import superimposer.vision.perception.entities.Entity;
import superimposer.vision.perception.models.RawModel;
import superimposer.vision.perception.models.TexturedModel;
import superimposer.vision.perception.shaders.StaticShader;
import superimposer.vision.perception.textures.ModelTexture;
import superimposer.vision.perception.toolbox.Maths;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_REPEAT);
        ModelTexture texture = model.getTexture();
        shader.loadFakeLightingVariable(texture.isUseFakeLightning());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_TEXTURE_3D);
        GL11.glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        matrix.translate(entity.getPosition());
        matrix.rotate((float) Math.toRadians(entity.getRotX()), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(entity.getRotY()), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(entity.getRotZ()), new Vector3f(0, 0, 1));
        matrix.scale(new Vector3f(entity.getScaleX(), entity.getScaleY(), entity.getScaleZ()));
        shader.loadTransformationMatrix(matrix);
    }

}