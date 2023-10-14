package superimposer.notation;

public enum PrepositionType {

    IN('a'),
    ON('a'),
    AT('a'),
    BY('a'),
    FOR('a'),
    WITH('a'),
    TO('a'),
    OF('a'),
    FROM('a'),
    AS('a'),
    INTO('a'),
    LIKE('a'),
    THROUGH('a'),
    AFTER('a'),
    OVER('a'),
    BETWEEN('a'),
    UNDER('a'),
    BEFORE('a'),
    DURING('a');

    private char c;

    PrepositionType(char c) {
        this.c = c;
    }

}
