package superimposer.notation;

public enum Cardinality {

    OF('/'),
    OUT('\\'),
    LIKE('~'),
    FROM('<'),
    TO('>'),
    AS('H'),
    AROUND('@'),
    BY('X'),
    AT('8');

    private char c;

    Cardinality(char c) {
        this.c = c;
    }

}
