package superimposer.notation;

import superimposer.Util;

import java.awt.*;

public record Preposition(PrepositionType type, Image icon) {
    public Preposition(PrepositionType type) {
        this(type, Util.image(type.name()));
    }
}
