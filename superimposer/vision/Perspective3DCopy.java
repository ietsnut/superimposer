package superimposer.vision;

import superimposer.library.Data;
import superimposer.library.Image;
import superimposer.library.Random;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Perspective3DCopy extends Perspective {

    ArrayList<Ball> balls;
    Camera cam;

    private BufferedImage lerpImage, texture0, texture1, texture2;

    class Camera {
        double x = 0;
        double y = 0;
        double z = 50;
        double rotation = 0;
        double pitch = -20;
        double zoom = 2;
        double fov = 0.25;
        double near = 0;
        double far = 100;
        double farnear_comp = 1 / (far - near);
        double m00 = 0;
        double m10 = 0;
        double m01 = 0;
        double m11 = 0;
        double yscale = 1;
        double zscale = 0;
    }

    class Ball {
        int x, y, z, r;
        Color c;
        Ball() {
            this.x = Random.integer(WIDTH);
            this.y = Random.integer(HEIGHT);
            this.z = Random.integer(200);
            this.r = Random.integer(5, 50);
            this.c = Random.color();
        }

        public int getSPZ() {
            Data data = wp_to_sp(x, y, z);
            return (int) data.data[2];
        }
    }

    public Perspective3DCopy() {
        super(1000, 800);
        this.cam = new Camera();
        this.balls = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            balls.add(new Ball());
        }
    }

    private BufferedImage loadTexture(String res) throws IOException {
        InputStream is = getClass().getResourceAsStream(res);
        BufferedImage textureTmp = ImageIO.read(is);
        BufferedImage texture = new BufferedImage(textureTmp.getWidth(), textureTmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = texture.createGraphics();
        g2.drawImage(textureTmp, 0, 0, null);
        g2.dispose();
        return texture;
    }

    @Override
    public void draw(Graphics2D g) {
        balls.sort(Comparator.comparingInt(Ball::getSPZ));
        Collections.reverse(balls);
        for (Ball ball : balls) {
            Data data = wp_to_sp(ball.x, ball.y, 0);
            if (data.data[3] > 0) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect((int) data.data[0], (int) data.data[1], (int) (ball.r * data.data[3]), (int) (ball.r * data.data[3] * cam.yscale));
            }
        }
        for (Ball ball : balls) {
            Data data = wp_to_sp(ball.x, ball.y, ball.z);
            //System.out.println("2D: " + Arrays.toString(data.data));
            if (data.data[3] > 0) {
                g.setColor(ball.c);
                g.fillRect((int) data.data[0], (int) data.data[1], (int) (ball.r * data.data[3]), (int) (ball.r * data.data[3]));
            }

        }
    }

    @Override
    public void update() {
        cam.rotation += ((KEYS.get(KeyEvent.VK_LEFT)) ? 1 : 0) - ((KEYS.get(KeyEvent.VK_RIGHT)) ? 1 : 0) ;
        cam.pitch += ((KEYS.get(KeyEvent.VK_DOWN)) ? 1 : 0) - ((KEYS.get(KeyEvent.VK_UP)) ? 1 : 0);
        // rotation matrix (for faster computation)
        cam.m00 = lengthdir_x(1, cam.rotation);
        cam.m10 = lengthdir_y(-1, cam.rotation);
        cam.m01 = lengthdir_y(1, cam.rotation);
        cam.m11 = lengthdir_x(1, cam.rotation);
        cam.yscale = lengthdir_y(1, cam.pitch);
        cam.zscale = lengthdir_x(1, cam.pitch);
        // moving around
        double raw_xaxis = ((KEYS.get(KeyEvent.VK_D)) ? 1 : 0) - ((KEYS.get(KeyEvent.VK_A)) ? 1 : 0);
        double raw_yaxis = ((KEYS.get(KeyEvent.VK_S)) ? 1 : 0) - ((KEYS.get(KeyEvent.VK_W)) ? 1 : 0);
        // // move along the viewing direction
        double for_x = lengthdir_x(1, -cam.rotation + 90);
        double for_y = lengthdir_y(1, -cam.rotation + 90);
        double side_x = lengthdir_x(1, -cam.rotation);
        double side_y = lengthdir_y(1, -cam.rotation);
        // // change camera location
        cam.x += (raw_yaxis * for_x + raw_xaxis * side_x) * 4;
        cam.y += (raw_yaxis * for_y + raw_xaxis * side_y) * 4;
        // change FOV on the go
        //cam.fov += (keyboard_check(ord("G")) - keyboard_check(ord("F"))) * 0.01;
    }

    double lengthdir_x(double length, double direction) {
        double radians = Math.toRadians(direction);
        return length * Math.cos(radians);
    }

    double lengthdir_y(double length, double direction) {
        double radians = Math.toRadians(direction);
        return length * Math.sin(radians);
    }

    public Data wp_to_sp(int x, int y, int z) {
        double res_screen_x = x;
        double res_screen_y = y;
        double res_screen_z = z;
        res_screen_x -= cam.x + (double) WIDTH / 2;
        res_screen_y -= cam.y + (double) HEIGHT / 2;
        res_screen_z -= cam.z;
        double xp_net = res_screen_x;
        res_screen_x = xp_net * cam.m00 + res_screen_y * cam.m10;
        res_screen_y = xp_net * cam.m01 + res_screen_y * cam.m11;
        // pitch (rotate y z)
        double yp_net = res_screen_y;
        res_screen_y = yp_net * cam.yscale - res_screen_z * cam.zscale;
        res_screen_z = -yp_net * cam.zscale - res_screen_z * cam.yscale;
        // calculate perspective amount

        double res_screen_scale = (res_screen_z - cam.near) * cam.farnear_comp;
        res_screen_scale += (1 - res_screen_scale) * cam.fov;
        res_screen_scale = cam.zoom / res_screen_scale;
        // move far-away objects to the center of the screen, close-by objects away from the center
        res_screen_x *= res_screen_scale;
        res_screen_y *= res_screen_scale;
        // move (translate) back to the center of the screen
        res_screen_x += (double) WIDTH / 2;
        res_screen_y += (double) HEIGHT / 2;
        return new Data(res_screen_x, res_screen_y, res_screen_z, res_screen_scale);
    }

}
