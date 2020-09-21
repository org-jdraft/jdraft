package org.parseout;

/**
 * Defines the content of a run
 * (i.e. its content: whitespace, digits...
 * or by its context: comment, quoted...)
 */
public class ContentType {

    /**
     * This is a CATEGORY "mask" for Content Type Runs that are composeable
     * (i.e. we can create Composite terms from a
     * combination ... for example, a Java Identifier like "Address_32Bit"
     * is "Composeable" with runs of:
     *                                "Address"  "_" "32" "Bit"
     *                                Alpha
     *
     */
    public static final int COMPOSABLE_CATEGORY  = 0b00000000000000000000111111111111; //12 bits
    // PRE-DEFINED Content Types that are uniform (i.e. but may be composeable)
    // i.e.
    public static final int WHITESPACE           = 0b00000000000000000000000000000001; //
    public static final int ALPHABET_LOWERCASE   = 0b00000000000000000000000000000010; // a-z
    public static final int ALPHABET_UPPERCASE   = 0b00000000000000000000000000000100; // A-Z
    public static final int DIGITS               = 0b00000000000000000000000000001000; // 0-9 can combine with . _ x X ABCDEF abcdef l L e E
    public static final int OPERATOR             = 0b00000000000000000000000000010000; // +-*/=?&^%!~|            (Run Combinable !=, ++)
    public static final int SEPARATOR            = 0b00000000000000000000000000100000; // : . ,                   (Run Repeatable :: ...)
    public static final int PUNCTUATION_OPEN     = 0b00000000000000000000000001000000; // ( { < [                 (Ind, Not Combinable)
    public static final int PUNCTUATION_CLOSE    = 0b00000000000000000000000010000000; // ) } > ] ;               (Ind, Not Combinable)
    public static final int SIGNIFIER            = 0b00000000000000000000000100000000; // @\#` unused (Ind, NOT COMBINABLE)
    public static final int CONTEXTUAL           = 0b00000000000000000000001000000000; // _ $ used in Identifiers (value_begin) and numbers (i.e. 1_000)
    public static final int SUBSET_1             = 0b00000000000000000000010000000000; // i.e. a
    public static final int SUBSET_2             = 0b00000000000000000000100000000000; //



    public static final int[] ALPHABET = {
            ALPHABET_LOWERCASE,
            ALPHABET_UPPERCASE
    };

    public static final int[] GENERIC = {
            (PUNCTUATION_OPEN  | SUBSET_1), // <
            (PUNCTUATION_CLOSE | SUBSET_1), // >
    };
    //
    public static final int[] HEX_ALPHANUMERIC_LOWER = {
            (ALPHABET_LOWERCASE | SUBSET_1),    // abcdefx
            (ALPHABET_UPPERCASE | SUBSET_1),    // ABCDEFX
    };

    public static final int[] NUMERIC_TYPE_POSTFIX = {
            (ALPHABET_LOWERCASE | SUBSET_2),    // ldf i.e. 888l, 1.2d, 1.3f
            (ALPHABET_UPPERCASE | SUBSET_2),    // LDF i.e. 888L, 1.2D, 1.3F
    };

    public static final int DOT = (SEPARATOR | SUBSET_1);         //.
    public static final int SIGN = (OPERATOR |SUBSET_1);          // + -
    public static final int UNDERSCORE = (CONTEXTUAL | SUBSET_1); // _

    public static final int NUMERIC_TYPE_POSTFIX_LOWERCASE = (ALPHABET_LOWERCASE | SUBSET_2); //l
    public static final int NUMERIC_TYPE_POSTFIX_UPPERCASE = (ALPHABET_UPPERCASE | SUBSET_2); //L
    public static final int HEX_ALPHANUMERIC_LOWERCASE = (ALPHABET_LOWERCASE | SUBSET_1);    // abcdefx
    public static final int HEX_ALPHANUMERIC_UPPERCASE = (ALPHABET_UPPERCASE | SUBSET_1);    // ABCDEFX

    public static final int ANY_NUMERIC = SIGN | WHITESPACE | DOT | DIGITS |UNDERSCORE | HEX_ALPHANUMERIC_LOWERCASE | HEX_ALPHANUMERIC_UPPERCASE | NUMERIC_TYPE_POSTFIX_LOWERCASE |NUMERIC_TYPE_POSTFIX_UPPERCASE;

    public static final int[] NUMERIC = {
            SIGN,                          // + -
            WHITESPACE,
            DOT,                           // .
            DIGITS,                        // 0-9
            UNDERSCORE,                    // _
            HEX_ALPHANUMERIC_LOWERCASE,    // abcdefx e for scientifi notation too
            HEX_ALPHANUMERIC_UPPERCASE,    // ABCDEFX E for scientific notation
            NUMERIC_TYPE_POSTFIX_LOWERCASE,// l
            NUMERIC_TYPE_POSTFIX_UPPERCASE // L
    };



    public static final int combined( int[] bits){
        int comb = 0;
        for(int i=0;i<bits.length;i++){
            comb = comb | bits[i];
        }
        return comb;
    }

    public static final int ANY_IDENTIFIER = ALPHABET_LOWERCASE | ALPHABET_UPPERCASE | CONTEXTUAL | DIGITS;

    public static final int[] IDENTIFIER ={
            ALPHABET_LOWERCASE, //a-z
            ALPHABET_UPPERCASE, //A-Z
            CONTEXTUAL,         //_ $
            DIGITS,             //0-9
    };


    public static final int[] QUALIFIED_NAME = {
            ALPHABET_LOWERCASE,    //a-z
            ALPHABET_UPPERCASE,    //A-Z
            CONTEXTUAL,            //_ $
            (SEPARATOR|SUBSET_1),  // .
            DIGITS,                //0-9
    };


    public static final int SIMPLE               = 0b00000000000000001111000000000000; // IMMUTABLE / UNCOMBINABLE
    public static final int QUOTED               = 0b00000000000000000001000000000000; // IMMUTABLE / UNCOMBINABLE
    public static final int DOUBLE_QUOTED        = 0b00000000000000000010000000000000; // IMMUTABLE / UNCOMBINABLE
    public static final int TEXT_BLOCK           = 0b00000000000000000100000000000000; // IMMUTABLE / UNCOMBINABLE
    public static final int COMMENT              = 0b00000000000000001000000000000000; // IMMUTABLE / UNCOMBINABLE



    public final String name;
    public final int[] runBits;

    public ContentType(String name, int... runBits){
        this.name = name;
        this.runBits = runBits;
    }

    public String getName(){
        return name;
    }

    public int[] getRunBits(){
        return runBits;
    }

    public int getCombinedRunBits(){
        int comb = runBits[0];
        for(int i=0;i<runBits.length; i++){
            comb = comb | runBits[i];
        }
        return comb;
    }
}
