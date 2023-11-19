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

    private final char character;

    Cardinality(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return this.character;
    }
}
