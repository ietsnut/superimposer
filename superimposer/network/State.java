package superimposer.network;

import superimposer.notation.*;
import java.io.*;

public class State implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    public int id;
    public Unit[] units;
    public int x;
    public int y;

}
