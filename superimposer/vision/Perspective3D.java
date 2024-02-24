package superimposer.vision;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import superimposer.vision.perception.Loader;
import superimposer.vision.perception.renderer.MasterRenderer;
import superimposer.vision.perception.OBJLoader;
import superimposer.vision.perception.entities.Camera;
import superimposer.vision.perception.entities.Entity;
import superimposer.vision.perception.entities.Light;
import superimposer.vision.perception.models.RawModel;
import superimposer.vision.perception.models.TexturedModel;
import superimposer.vision.perception.terrain.Terrain;
import superimposer.vision.perception.textures.ModelTexture;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Perspective3D extends Perspective {

    public Perspective3D() {
        super(1000, 800);
        Canvas3D canvas3D = new Canvas3D();
        //add(canvas3D);
        new Thread(canvas3D).start();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, W, H);
    }

    @Override
    public void update() {

    }

    private class Canvas3D extends Canvas implements Runnable {
        public Canvas3D() {

        }
        @Override
        public void run() {
            ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
            try {
                Display.setDisplayMode(new DisplayMode(W, H));
                //Display.setParent(this);
                Display.create(new PixelFormat(), attribs);
                Display.setVSyncEnabled(true);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
            GL11.glViewport(0, 0, W, H);
            Loader loader = new Loader();
            ///
            RawModel model = OBJLoader.loadOBJ(loader, "library_of_lament");
            TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("texture")));
            staticModel.getTexture().setUseFakeLightning(false);


            //staticModel.getTexture().setReflectivity(1f);
            //staticModel.getTexture().setShineDamper(1f);
            ArrayList<Entity> entities = new ArrayList<Entity>();
            Random random = new Random();
            for(int i=0;i<50;i++) {
                Entity grass = new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3);
                grass.setScale(superimposer.library.Random.integer(10, 25));
                grass.setRotY(superimposer.library.Random.integer(0, 360));
                entities.add(grass);
            }
            Light light = new Light(new Vector3f(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE), new Vector3f(1,1,1));
            Terrain terrain = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("path")));
            Terrain terrain2 = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("path")));
            Camera camera = new Camera();
            camera.position.y = 10;
            MasterRenderer renderer = new MasterRenderer();
            Mouse.setGrabbed(true);
            int fps = 0;
            long lastFrameTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
            while (!Display.isCloseRequested()) {
                camera.move();
                renderer.processTerrain(terrain);
                renderer.processTerrain(terrain2);
                for(Entity entity:entities){
                    renderer.processEntity(entity);
                }
                renderer.render(light, camera);
                //Display.sync(FPS);
                if ((Sys.getTime() * 1000) / Sys.getTimerResolution() - lastFrameTime > 1000) { // One second passed
                    Display.setTitle("FPS: " + fps);
                    fps = 0; // Reset the FPS counter
                    lastFrameTime += 1000; // Move to the next second
                }
                fps++; // Increment the FPS counter
                Display.update();
            }
            renderer.cleanUp();
            loader.cleanUp();
            Display.destroy();
        }
    }

}
