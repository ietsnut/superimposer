package superimposer.notation;

public enum Cardinality {

    CONTAIN('✦', Type.RELATION),
    EXTEND('>', Type.RELATION),
    VARY('~', Type.RELATION),

    STATIC('⊡', Type.POSITION),
    FIXED('⊠', Type.POSITION),
    RELATIVE('⊞', Type.POSITION),
    ABSOLUTE('▤', Type.POSITION),
    PARALLAX('∰', Type.POSITION),

    COLLIDE('☄', Type.INTERACTION),
    COLLECT('☰', Type.INTERACTION),
    DRAG('✍', Type.INTERACTION),

    UP('↑', Type.DIRECTION),
    DOWN('↓', Type.DIRECTION),
    LEFT('←', Type.DIRECTION),
    RIGHT('→', Type.DIRECTION),

    WANDER('X', Type.TRANSITION),
    FLOAT('≋', Type.TRANSITION),
    SWAY('∿', Type.TRANSITION),
    ROTATE('↻', Type.TRANSITION),
    AROUND('∞', Type.TRANSITION),
    DECAY('⟟', Type.TRANSITION),
    MORPH('⌖', Type.TRANSITION),
    ANIMATE('⚙', Type.TRANSITION),

    ARRAY('H', Type.LOCATION),
    PERLIN('ℙ', Type.LOCATION),
    VORONOI('▣', Type.LOCATION),
    SIMPLEX('⨋', Type.LOCATION),
    PATTERN('♾', Type.LOCATION),

    PLANE('_', Type.PERSPECTIVE),
    MAZE('⬰', Type.PERSPECTIVE),
    DUNGEON('⌶', Type.PERSPECTIVE),

    NEW('*', Type.NEW),
    DELETE('.', Type.DELETE);

    private final char character;
    private final Type type;

    Cardinality(char character, Type type) {
        this.character = character;
        this.type = type;
    }

    public char getCharacter() {
        return character;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        RELATION(),
        POSITION(),
        INTERACTION(),
        DIRECTION(),
        TRANSITION(),
        LOCATION(),
        PERSPECTIVE(),
        NEW(),
        DELETE();
    }

}
