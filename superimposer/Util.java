package superimposer;

import javax.swing.*;
import java.awt.*;

public class Util {
    public static Image image(String path) {
        return new ImageIcon(Util.class.getResource(path)).getImage();
    }

}
