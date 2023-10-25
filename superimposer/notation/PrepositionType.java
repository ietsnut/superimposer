package superimposer.notation;

public enum PrepositionType {

    // SET OBJECTS[] OF ASSOCIATION
    IN('a'),

    //
    ON('a'),

    AT('a'),

    // SET ASSOCIATION X Y NEXT TO ANOTHER ASSOCIATION
    BY('a'),

    //
    FOR('a'),

    //
    WITH('a'),

    // SET X Y OF ASSOCIATION
    TO('a'),


    OF('a'),

    //
    FROM('a'),

    // TRANSFORM ASSOCIATION INTO ANOTHER ASSOCIATION
    AS('a'),

    //
    INTO('a'),

    // MAKE COPY OF ASSOCIATION
    LIKE('a'),

    THROUGH('a'),
    AFTER('a'),
    OVER('a'),
    UNDER('a'),
    BEFORE('a'),
    DURING('a'),
    BETWEEN('a');

    private char c;

    PrepositionType(char c) {
        this.c = c;
    }

}
