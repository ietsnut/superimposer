package superimposer.notation;

import superimposer.Util;

import java.util.*;

public class Notation {

    ArrayList<Preposition> prepositions = new ArrayList<>();

    public Notation() {
        for (PrepositionType type : PrepositionType.values()) {
            prepositions.add(new Preposition(type));
        }
    }

}
