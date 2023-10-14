package superimposer.notation;

import superimposer.Util;

import java.util.*;

public class Notation {

    HashMap<Character, Preposition> map = new HashMap<>();

    public Notation() {
        map.put('•', new Preposition(Util.image("in")));

        map.put('◯', new Preposition(Util.image("out")));

        map.put('@', new Preposition(Util.image("at")));

        map.put('O', new Preposition(Util.image("on")));

        map.put('O', new Preposition(Util.image("of")));

        map.put('O', new Preposition(Util.image("with")));

        map.put('O', new Preposition(Util.image("by")));

        map.put('<', new Preposition(Util.image("from")));

        map.put('>', new Preposition(Util.image("to")));
    }

}
