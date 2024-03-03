package superimposer.vision;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import superimposer.vision.Perspective2D;
import superimposer.vision.perception.Loader;
import superimposer.vision.perception.OBJLoader;
import superimposer.vision.perception.entities.Camera;
import superimposer.vision.perception.entities.Entity;
import superimposer.vision.perception.entities.Light;
import superimposer.vision.perception.models.ModelData;
import superimposer.vision.perception.models.RawModel;
import superimposer.vision.perception.models.TexturedModel;
import superimposer.vision.perception.renderer.MasterRenderer;
import superimposer.vision.perception.terrain.Terrain;
import superimposer.vision.perception.textures.ModelTexture;
import superimposer.vision.perception.textures.TerrainTexture;
import superimposer.vision.perception.textures.TerrainTexturePack;
import superimposer.vision.perception.toolbox.MousePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Perspective3D extends Perspective2D {

    protected final Canvas3D canvas3D;

    public Perspective3D(int W, int H) {
        super(W, H);
        this.canvas3D = new Canvas3D();
        setAlwaysOnTop(true);
        SwingUtilities.invokeLater(canvas3D);
        transferFocus();
        toFront();
    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(10));
        graphics.drawOval(50, 50, W - 100, H - 100);
    }

    private class Canvas3D extends JFrame implements Runnable {

        private Canvas canvas;
        private boolean setup = true;

        private Canvas3D() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(W, H));
            setUndecorated(true);
            //setBackground(new Color(0, 0, 0, 0));
            //setShape(createOctagon(W, H));
            canvas = new Canvas();
            add(canvas);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
            transferFocus();
            toFront();
        }

        Loader loader;
        Camera camera;
        MasterRenderer renderer;
        Terrain terrain, terrain2;
        ArrayList<Entity> entities;
        ArrayList<Light> lights;
        int fps;
        long lastFrameTime;
        int currentmodel = 0;
        Entity en0, en1, en2;
        long lastSec = 0;
        boolean up = true;
        MousePicker picker;

        @Override
        public void run() {
            if (setup) {
                ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
                try {
                    Display.setDisplayMode(new DisplayMode(W, H));
                    Display.setParent(canvas);

                    Display.create(new PixelFormat(), attribs);
                    Display.setVSyncEnabled(true);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                GL11.glViewport(0, 0, W, H);
                loader = new Loader();

                ModelData data = OBJLoader.loadOBJ("1_0");
                //data.tile(4);
                TexturedModel model0 = new TexturedModel(loader.loadToVAO(data), new ModelTexture(loader.loadTexture("1")));
                TexturedModel model1 = new TexturedModel(loader.loadToVAO(data), new ModelTexture(loader.loadTexture("1")));
                TexturedModel model2 = new TexturedModel(loader.loadToVAO(data), new ModelTexture(loader.loadTexture("1")));
                model0.getTexture().setUseFakeLightning(true);
                model1.getTexture().setUseFakeLightning(true);
                model2.getTexture().setUseFakeLightning(true);
                en0 = new Entity(model0, new Vector3f(0, 0, 0), 0, 0, 0, 10, 4);
                en1 = new Entity(model1, new Vector3f(-100, 10, -100), 0, 0, 0, 10, 4);
                en2 = new Entity(model2, new Vector3f(-200, 0, -500), 0, 0, 0, 10, 4);


                //staticModel.getTexture().setUseFakeLightning(false);


                //staticModel.getTexture().setReflectivity(1f);
                //staticModel.getTexture().setShineDamper(1f);
                /*
                entities = new ArrayList<Entity>();
                Random random = new Random();

                for(int i=0;i<50;i++) {
                    Entity grass = new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,superimposer.library.Random.integer(10, 25));
                    grass.setScaleY(grass.getScaleY() * ((float) 2 /superimposer.library.Random.integer(2, 4)));
                    grass.setRotY(superimposer.library.Random.integer(0, 360));
                    entities.add(grass);
                }*/

                lights = new ArrayList<>();
                lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.002f)));
                //lights.add(new Light (new Vector3f (0, 1000, -7000), new Vector3f (0.4f, 0.4f, 0.4f)));
                lights.add(new Light (new Vector3f (-185, 10, -293), new Vector3f (1, 1, 1), new Vector3f (1, 0.01f, 0.002f)));
                lights.add(new Light (new Vector3f (0, 1000, 0), new Vector3f (1, 1, 1)));
                lights.add(new Light (new Vector3f (-293, 7, -305), new Vector3f (2, 2, 0), new Vector3f (1, 0.01f, 0.002f)));

                TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
                TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
                TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
                TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

                TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
                TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

                terrain = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
                //terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");

                camera = new Camera();
                renderer = new MasterRenderer(loader);

                picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain, Arrays.asList(en0, en1, en2));

                Mouse.setGrabbed(true);
                fps = 0;
                lastFrameTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
                this.setup = false;
            }
            if (!Display.isCloseRequested()) {
                this.setLocation(Perspective3D.this.getLocation().x, Perspective3D.this.getLocation().y);
                camera.move(terrain);
                picker.update();
                lights.get(1).setPosition(camera.position);
                /*
                Vector3f terrainPoint = picker.getCurrentTerrainPoint();
                if (terrainPoint != null) {

                    en0.setPosition(terrainPoint);
                    en1.setPosition(terrainPoint);
                    en2.setPosition(terrainPoint);
                    lights.get(0).setPosition(terrainPoint.translate(0, 10, 0));
                }

                System.out.println(terrainPoint);*/

                Entity entity = picker.raycastEntities();
                if (entity != null) {
                    Vector3f pos = new Vector3f(entity.getPosition());
                    lights.get(0).setPosition(pos.translate(0, 10, 0));
                } else {
                    lights.get(0).setPosition(new Vector3f(-1000000, 0, -10000));
                }

                //Vector3f lantern = new Vector3f(camera.position);
                //lights.get(0).setPosition(lantern.translate(-5, -1, -5));
                renderer.processTerrain(terrain);
                //renderer.processTerrain(terrain2);
                /*
                for(Entity entity:entities){
                    renderer.processEntity(entity);
                }*/


                //Display.sync(FPS);


                renderer.processEntity(en0);
                renderer.processEntity(en1);
                renderer.processEntity(en2);

                renderer.render(lights, camera);
                if ((Sys.getTime() * 1000) / Sys.getTimerResolution() - lastFrameTime > 1000) { // One second passed

                    setTitle("FPS: " + fps);
                    fps = 0; // Reset the FPS counter
                    lastFrameTime += 1000; // Move to the next second

                }

                fps++; // Increment the FPS counter
                Display.update();
                SwingUtilities.invokeLater(this);
            } else {
                renderer.cleanUp();
                loader.cleanUp();
                Display.destroy();
            }
        }
    }
}