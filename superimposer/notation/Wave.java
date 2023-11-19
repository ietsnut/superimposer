package superimposer.notation;

import java.io.Serial;
import java.io.Serializable;

public record Wave(float... pf) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    public float getValue(float t) {
        float n = 0;
        for(int i = 0; i < pf.length; i += 2) {
            n += (float) Math.sin(2 * Math.PI * pf[i + 1] * t + pf[i]);
        }
        return n;
    }
}
