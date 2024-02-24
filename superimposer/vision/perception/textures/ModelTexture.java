package superimposer.vision.perception.textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 1;

    private boolean useFakeLightning = false;

    public float getShineDamper() {
        return shineDamper;
    }

    public boolean isUseFakeLightning() {
        return useFakeLightning;
    }

    public void setUseFakeLightning(boolean useFakeLightning) {
        this.useFakeLightning = useFakeLightning;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public int getID() {
        return textureID;
    }
}
