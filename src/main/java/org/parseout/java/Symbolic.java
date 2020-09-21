package org.parseout.java;

import org.parseout.binary.Bin32;
import org.parseout.binary.Bitwise;

import java.util.stream.Stream;

/*
 * Section Delimiter (8) (4-bits)
 *  { ( < [ (OPEN)
 *  } ( > ] (CLOSE)
 *
 * Separator (8) (4-bits)
 *     , : .
 *     MethodReference
 *     ::
 *     VarArg
 *     ...
 *     Lambda
 *     ->
 *     Annotation
 *     @
 *     GenericType
 *     | Union
 *     & Intersection type
 *
 *     Import Wildcard
 *     *
 *
 * Binary (36) (6-bits)
 *    Conditional (2)
 *       &&,||
 *    Assignment  (12)
 *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
 *    Relational (4)
 *       == != >= <=
 *    Arithmetic (5)
 *       / * + - %
 *    Bitwise (13)
 *       >>, <<, >>>, ~, &, ^, |>>, <<, >>>, ~, &, ^, |
 * Unary (7) (3-bits)
 *    Prefix (5)
 *       !, +, -, ++, --
 *    Postfix (2)
 *       ++, --
 * Escape Character (1-bit)
 *    "\\"
 */
public enum Symbolic implements Bin32.BitPattern<String> {

    /**  Separator (8) (4-bits) , : ; . :: ... -> @ */
    Comma(    ",",      Category.Separator,0b0001,0b100000000000000000000000000000000000000000000000000000000000001L), //1
    Colon(    ":",      Category.Separator,0b0010,0b010000000000000000000000000000000000000000000000000000000000001L), //2
    Dot(      ".",      Category.Separator,0b0011,0b001000000000000000000000000000000000000000000000000000000000001L), //3
    Dbl_Colon("::",     Category.Separator,0b0100,0b000100000000000000000000000000000000000000000000000000000000001L), //4

    VarArg(   "...",    Category.Separator,0b0101,0b000010000000000000000000000000000000000000000000000000000000001L), //5
    Lambda(   "->",     Category.Separator,0b0110,0b000001000000000000000000000000000000000000000000000000000000001L), //6

    At(       "@",      Category.Separator,0b0111,0b000000100000000000000000000000000000000000000000000000000000001L), //7

    //OPERATOR
    /* Type & | ? */
    Intersect("&",      Category.Separator,0b1000,0b000000010000000000000000000000000000000000000000000000000000001L), //8
    Union(    "|",      Category.Separator,0b1001,0b000000001000000000000000000000000000000000000000000000000000001L), //9
    Type_Wildcard("?",  Category.Separator,0b1010,0b000000000100000000000000000000000000000000000000000000000000001L), //10
    Import_Wildcard("*",Category.Separator,0b1011,0b000000000010000000000000000000000000000000000000000000000000001L), //11

    /**
     * Binary (36) (5-bits)
     *    Conditional (2)
     *       &&,||
     *    Assignment  (12)
     *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
     *    Relational (4)
     *       == != >= <=
     *    Arithmetic (5)
     *       / * + - %
     *    Bitwise (6)
     *       >>, <<, >>>, &, ^, |
     */

    /** Operator Conditional (2)
     *  &&,||
     */
    And(     "&&", Category.BinaryOperator, SubCategory.Conditional, 0b00001,0b000000000001000000000000000000000000000000000000000000000000001L), //1
    Or(      "||", Category.BinaryOperator, SubCategory.Conditional, 0b00010,0b000000000000100000000000000000000000000000000000000000000000001L), //2

    /** Operator Assignment  (12)
     *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
     */
    Eq(      "=",   Category.BinaryOperator, SubCategory.Assignment, 0b00011,0b000000000000010000000000000000000000000000000000000000000000001L), //3
    PlusEq(  "+=",  Category.BinaryOperator, SubCategory.Assignment, 0b00100,0b000000000000001000000000000000000000000000000000000000000000001L), //4
    MinusEq( "-=",  Category.BinaryOperator, SubCategory.Assignment, 0b00101,0b000000000000000100000000000000000000000000000000000000000000001L), //5
    MultEq(  "*=",  Category.BinaryOperator, SubCategory.Assignment, 0b00110,0b000000000000000010000000000000000000000000000000000000000000001L), //6
    DivEq(   "/=",  Category.BinaryOperator, SubCategory.Assignment, 0b00111,0b000000000000000001000000000000000000000000000000000000000000001L), //7
    AndEq(   "&=",  Category.BinaryOperator, SubCategory.Assignment, 0b01000,0b000000000000000000100000000000000000000000000000000000000000001L), //8
    OrEq("|=",      Category.BinaryOperator, SubCategory.Assignment, 0b01001,0b000000000000000000010000000000000000000000000000000000000000001L),     //9
    XorEq("^=",     Category.BinaryOperator, SubCategory.Assignment, 0b01010,0b000000000000000000001000000000000000000000000000000000000000001L),    //10
    ModEq("%=",     Category.BinaryOperator, SubCategory.Assignment, 0b01011,0b000000000000000000000100000000000000000000000000000000000000001L),    //11
    ShftLEq("<<=",  Category.BinaryOperator, SubCategory.Assignment, 0b01100,0b000000000000000000000010000000000000000000000000000000000000001L), //12
    ShftREq(">>=",  Category.BinaryOperator, SubCategory.Assignment, 0b01101,0b000000000000000000000001000000000000000000000000000000000000001L), //13
    ShftRUEq(">>>=",Category.BinaryOperator, SubCategory.Assignment, 0b01110,0b000000000000000000000000100000000000000000000000000000000000001L),//14

    /** Operator Relational (4)
     * == != >= <= < >
     */
     IsEq("==",   Category.BinaryOperator, SubCategory.Relational,   0b01111,0b000000000000000000000000010000000000000000000000000000000000001L), //15
     IsNotEq("!=",Category.BinaryOperator, SubCategory.Relational,   0b10000,0b000000000000000000000000001000000000000000000000000000000000001L), //16
     IsGtEq(">=", Category.BinaryOperator, SubCategory.Relational,   0b10001,0b000000000000000000000000000100000000000000000000000000000000001L), //17
     IsLtEq(">=", Category.BinaryOperator, SubCategory.Relational,   0b10010,0b000000000000000000000000000010000000000000000000000000000000001L), //18
     IsLt( "<",   Category.BinaryOperator, SubCategory.Relational,   0b10011,0b000000000000000000000000000001000000000000000000000000000000001L), //19
     IsGt( ">",   Category.BinaryOperator, SubCategory.Relational,   0b10100,0b000000000000000000000000000000100000000000000000000000000000001L), //20

     /** Operator Arithmetic (5)
      *  / * + - %
      */
     Div("/",   Category.BinaryOperator, SubCategory.Arithmetic, 0b10101,    0b000000000000000000000000000000010000000000000000000000000000001L), //0b10011), //21
     Mul("*",   Category.BinaryOperator, SubCategory.Arithmetic, 0b10110,    0b000000000000000000000000000000001000000000000000000000000000001L), //0b10100), //22
     Plus("+",  Category.BinaryOperator, SubCategory.Arithmetic, 0b10111,    0b000000000000000000000000000000000100000000000000000000000000001L), //0b10101), //23
     Minus("-", Category.BinaryOperator, SubCategory.Arithmetic, 0b11000,    0b000000000000000000000000000000000010000000000000000000000000001L), //0b10110), //24
     Mod("%",   Category.BinaryOperator, SubCategory.Arithmetic, 0b11001,    0b000000000000000000000000000000000001000000000000000000000000001L), //0b10111), //25


     /** Operator Bitwise (6)
      *   >>, <<, >>>, &, ^, |
      */
     BShftR(">>",  Category.BinaryOperator, SubCategory.BinaryBitwise,0b11010,0b000000000000000000000000000000000000100000000000000000000000001L),//  0b11000), //26
     BShftL("<<",  Category.BinaryOperator, SubCategory.BinaryBitwise,0b11011,0b000000000000000000000000000000000000010000000000000000000000001L), //0b11001), //27
     BShftRU(">>>",Category.BinaryOperator, SubCategory.BinaryBitwise,0b11100,0b000000000000000000000000000000000000001000000000000000000000001L), //0b11010), //28
     BAnd("&",     Category.BinaryOperator, SubCategory.BinaryBitwise,0b11101,0b000000000000000000000000000000000000000100000000000000000000001L), //0b11011), //29
     BXor("^",     Category.BinaryOperator, SubCategory.BinaryBitwise,0b11110,0b000000000000000000000000000000000000000010000000000000000000001L), //0b11100), //30
     BOr("|",      Category.BinaryOperator, SubCategory.BinaryBitwise,0b11111,0b000000000000000000000000000000000000000001000000000000000000001L), //0b11101), //31

     /** Operator Unary (8) (3-bits)
      *    Prefix (5)
      *       ~, !, +, -, ++, --
      */
     BNot("~",       Category.UnaryOperator, SubCategory.UnaryPrefix,       0b001,0b000000000000000000000000000000000000000000100000000000000000001L), //1
     Bang("!",       Category.UnaryOperator, SubCategory.UnaryPrefix,       0b010,0b000000000000000000000000000000000000000000010000000000000000001L), //2
     Pos("+",        Category.UnaryOperator, SubCategory.UnaryPrefix,       0b011,0b000000000000000000000000000000000000000000001000000000000000001L), //3
     Neg("-",        Category.UnaryOperator, SubCategory.UnaryPrefix,       0b100,0b000000000000000000000000000000000000000000000100000000000000001L), //4
     PlusPlus("++",  Category.UnaryOperator, SubCategory.UnaryPrefixPostfix,0b101,0b000000000000000000000000000000000000000000000010000000000000001L), //5
     MinusMinus("--",Category.UnaryOperator, SubCategory.UnaryPrefixPostfix,0b110,0b000000000000000000000000000000000000000000000001000000000000001L), //6

     /** Punctuation Escape Character (1-bit) *    "\\" */
     Escape("\\",    Category.EscapeCharacter,                             0b1,  0b000000000000000000000000000000000000000000000000100000000000001L) //1
     ;

    @Override
    public String getValue() {
        return symbol;
    }

    @Override
    public Bin32 getBin32() {
        return category;
    }

    @Override
    public int getBitPattern() {
        return bitPattern;
    }

    /**
     * Describe all of the Keywords
     * @return
     */
    public static String describeAll(){
        StringBuilder sb = new StringBuilder();
        Stream.of(Symbolic.values()).forEach(k -> sb.append( k.toString()).append('\n'));
        return sb.toString();
    }

    public String describe(){
        String bitPattern = "";
        for(int i=0;i<getShift();i++){
            bitPattern += "-";
        }
        bitPattern = Bitwise.bits( getBitCount(), getBitPattern() )+ bitPattern;
        int leading = 32 - bitPattern.length();
        for(int i=0;i<leading;i++){
            bitPattern = "-" + bitPattern;
        }
        if( this.subCategory == null ){
            return bitPattern+" "+category.getName()+"."+super.toString()+"["+ symbol +"]";
        }
        return bitPattern+" "+category.getName()+"."+subCategory.name()+"."+super.toString()+"["+ symbol +"]";
    }

    public String toString(){
        return describe();
    }

    public final String symbol;
    public final Category category;
    public final SubCategory subCategory;
    public final int bitPattern;
    public final int shiftedBitPattern;

    public final long symbolBit;

    Symbolic(String symbol, Category category, int bitPattern, long symbolBit){
        this.symbol = symbol;
        this.category = category;
        this.bitPattern = bitPattern;
        this.subCategory = null;
        this.shiftedBitPattern = bitPattern << category.getShift();
        this.symbolBit = symbolBit;
    }

    Symbolic(String symbol, Category category, SubCategory subCategory, int bitPattern, long symbolBit){
        this.symbol = symbol;
        this.category = category;
        this.subCategory = subCategory;
        this.bitPattern = bitPattern;
        this.shiftedBitPattern = bitPattern << category.getShift();
        this.symbolBit = symbolBit;
    }

    /**
     * 23-bits
     *
     * Separator (8) (4-bits)
     * , : . :: ... -> @ | &
     *
     * Binary (36) (6-bits)
     *    Conditional (2)
     *       &&,||
     *    Assignment  (12)
     *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
     *    Relational (4)
     *       == != >= <=
     *    Arithmetic (5)
     *       / * + - %
     *    Bitwise (6)
     *       >>, <<, >>>, &, ^, |
     * Unary (7) (3-bits)
     *    Bitwise (1)
     *       ~
     *    Prefix (3)
     *       !, +, -,
     *    PrefixPostfix (2)
     *       ++, --
     * Escape Character (1-bit)
     *    "\\"
     */
    public enum Category implements Bin32 {
        Separator(       0b1111,  9,  "Separator"),
        BinaryOperator(  0b11111, 4,  "BinaryOperator"),
        UnaryOperator(   0b111,   1,  "UnaryOperator"),
        EscapeCharacter( 0b1,     0,  "EscapeCharacter");

        public final Address bin;
        public final String name;

        Category(int mask, int shift, String name){
            this.bin = Bin32.of(mask, shift);
            this.name = name;
        }

        public String getName(){
            return name;
        }

        @Override
        public int getMask() {
            return bin.mask;
        }

        @Override
        public int getShift() {
            return bin.shift;
        }

        public String describe(){
            return bin.describe()+" ["+name+"]";
        }
    }

    public enum SubCategory implements Bin32 {

        /**
         * Binary (36) (5-bits)
         *    Conditional (2)
         *       &&,||
         *    Assignment  (12)
         *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
         *    Relational (4)
         *       == != >= <=
         *    Arithmetic (5)
         *       / * + - %
         *    Bitwise (6)
         *       >>, <<, >>>, &, ^, |
         */

        /**
         *    Conditional (2)
         *       &&,||
         */
        Conditional(Category.BinaryOperator, 0b00001, 0b00010, "Conditional"), //conjunction?

        /**
         *    Assignment  (12) 0000
         *       = += -=, *=, /=, &=, |=, ^=, %=, <<=, >>=, >>>=
         */
        Assignment(Category.BinaryOperator, 0b00011, 0b01111, "Assignment"),

        /**
         *    Relational (4)
         *       ==  0b10000
         *       !=  0b10001
         *       >=  0b10010
         *       <=  0b10011
         *       <   0b10100
         *       >   0b10101
         */
        //Relational(Category.BinaryOperator, 0b10000, 0b10011, "Relational"),
        Relational(Category.BinaryOperator, 0b10000, 0b10101, "Relational"),

        /**
         *    Arithmetic (5)
         *       /  0b10100
         *       *  0b10101
         *       +  0b10110
         *       -  0b10111
         *       %  0b11000
         */
        Arithmetic(Category.BinaryOperator, 0b10100,0b11000, "Arithmetic" ),

        /**
         *    Bitwise (6)
         *       >>, 0b11001
         *       <<, 0b11010
         *       >>>,0b11011
         *       &,  0b11100
         *       ^,  0b11101
         *       |   0b11110
         */
        BinaryBitwise(Category.BinaryOperator, 0b11001, 0b11110, "BinaryBitwise"),

        /**
         * Unary (6) (3-bits)
         *    Bitwise (1)
         *       ~ 0b001
         *    Prefix (3)
         *       !, 0b010
         *       +, 0b011
         *       -, 0b100
         *    PrefixPostfix (2)
         *       ++, 0b101
         *       --  0b110
         */
        UnaryBitwise(Category.UnaryOperator, 0b001, "UnaryBitwise"),
        UnaryPrefix(Category.UnaryOperator, 0b010, 0b100, "UnaryPrefix"),
        UnaryPrefixPostfix(Category.UnaryOperator, 0b101, 0b110, "UnaryPrefixPostfix");

        public final Category category;

        public final String name;
        public final int minBin;
        public final int maxBin;

        SubCategory(Category category, int minBin, int maxBin, String name){
            this.category = category;
            this.minBin = minBin;
            this.maxBin = maxBin;
            this.name = name;
        }

        SubCategory(Category category, int bin,  String name){
            this.category = category;
            this.minBin = bin;
            this.maxBin = bin;
            this.name = name;
        }

        @Override
        public int getMask() {
            return category.bin.mask;
        }

        @Override
        public int getShift() {
            return category.bin.shift;
        }

        public String describe(){
            if( this.minBin == this.maxBin ){
                return this.category.bin.describeBin() + " ["+Bitwise.bits(this.getBitCount(), this.minBin)+"] ["+category.getName()+"."+this.name+"]";
            }
            return this.category.bin.describeBin() + " ["+Bitwise.bits(this.getBitCount(), this.minBin)+"..."+Bitwise.bits(this.getBitCount(), this.maxBin)+"] ["+category.getName()+"."+this.name+"]";
        }
    }


    public static boolean isBinaryOperator(int word){
        return Category.BinaryOperator.hit(word);
    }
    public static boolean isSeparator(int word){
        return Category.Separator.hit(word);
    }
    public static boolean isUnaryOperator(int word){
        return Category.UnaryOperator.hit(word);
    }
    public static boolean isEscapeCharacter(int word){
        return Category.EscapeCharacter.hit(word);
    }
}
