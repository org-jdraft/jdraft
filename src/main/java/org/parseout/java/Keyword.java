package org.parseout.java;

import org.parseout.binary.Bin32;
import org.parseout.binary.Bitwise;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Keywords mapped to (22 bit bins based on category and subcategory)
 *
 */
public enum Keyword implements Bin32.BitPattern<String> {

    Package("package", Category.CodeUnit, SubCategory.Property, 0b01, 0b0000000000000110000000000000000000000000000000000000000000000000L),
    Import("import", Category.CodeUnit, SubCategory.Property, 0b10,   0b0000000000000101000000000000000000000000000000000000000000000000L),

    Class( "class", Category.Declaration, SubCategory.Type, 0b01, 0b0000000000000100100000000000000000000000000000000000000000000000L),
    Enum( "enum", Category.Declaration, SubCategory.Type, 0b10, 0b0000000000000100010000000000000000000000000000000000000000000000L),
    Interface( "interface", Category.Declaration, SubCategory.Type, 0b11, 0b0000000000000100001000000000000000000000000000000000000000000000L),

    Extends("extends", Category.Declaration, SubCategory.TypeModifier, 0b100, 0b0000000000000100000100000000000000000000000000000000000000000000L), //, 0b101),   //extends, implements
    Implements("implements", Category.Declaration, SubCategory.TypeModifier, 0b101, 0b0000000000000100000010000000000000000000000000000000000000000000L), //, 0b101),   //extends, implements

    Default("default", Category.Declaration, SubCategory.MemberModifier, 0b110, 0b0000000000000100000001000000000000000000000000000000000000000000L), //, 0b101),   //default, throws
    Throws("throws", Category.Declaration, SubCategory.MemberModifier, 0b111, 0b0000000000000100000000100000000000000000000000000000000000000000L), //, 0b101),   //default, throws

    Public("public", Category.Modifier, SubCategory.Access, 0b01, 0b0000000000000100000000010000000000000000000000000000000000000000L),
    Protected("protected", Category.Modifier, SubCategory.Access, 0b10, 0b0000000000000100000000001000000000000000000000000000000000000000L),
    Private("private", Category.Modifier, SubCategory.Access, 0b11, 0b0000000000000100000000000100000000000000000000000000000000000000L),

    Static("static", Category.Modifier, 0b100, 0b0000000000000100000000000010000000000000000000000000000000000000L),
    Final("final", Category.Modifier,  0b101, 0b0000000000000100000000000001000000000000000000000000000000000000L),
    Abstract("abstract", Category.Modifier,0b110, 0b0000000000000100000000000000100000000000000000000000000000000000L),
    Synchronized("synchronized", Category.Modifier,0b111, 0b0000000000000100000000000000010000000000000000000000000000000000L),
    Volatile("volatile", Category.Modifier, 0b1000, 0b0000000000000100000000000000001000000000000000000000000000000000L),
    Transient("transient", Category.Modifier,0b1001, 0b0000000000000100000000000000000100000000000000000000000000000000L),
    Native("native", Category.Modifier,0b1010, 0b0000000000000100000000000000000010000000000000000000000000000000L),
    StrictFp("strictfp", Category.Modifier,0b1011, 0b0000000000000100000000000000000001000000000000000000000000000000L),

    Assert("assert", Category.Statement, SubCategory.Assertion, 0b0001, 0b0000000000000100000000000000000000100000000000000000000000000000L),
    If("if", Category.Statement, SubCategory.Condition, 0b0010, 0b0000000000000100000000000000000000010000000000000000000000000000L),
    Switch("switch", Category.Statement, SubCategory.Condition, 0b0011, 0b0000000000000100000000000000000000001000000000000000000000000000L),

    Case("case", Category.Statement, SubCategory.Condition_Conjunction, 0b0100, 0b0000000000000100000000000000000000000100000000000000000000000000L),
    Else("else", Category.Statement, SubCategory.Condition_Conjunction, 0b0101, 0b0000000000000100000000000000000000000010000000000000000000000000L),

    Do("do", Category.Statement, SubCategory.Loop, 0b0110, 0b0000000000000100000000000000000000000001000000000000000000000000L),
    For("for", Category.Statement, SubCategory.Loop, 0b0111, 0b0000000000000100000000000000000000000000100000000000000000000000L),
    While("while", Category.Statement, SubCategory.Loop, 0b1000, 0b0000000000000100000000000000000000000000010000000000000000000000L),

    Return("return", Category.Statement, SubCategory.Interrupt, 0b1001, 0b0000000000000100000000000000000000000000001000000000000000000000L),
    Break("break", Category.Statement, SubCategory.Interrupt, 0b1010, 0b0000000000000100000000000000000000000000000100000000000000000000L),
    Continue("continue", Category.Statement, SubCategory.Interrupt, 0b1011, 0b0000000000000100000000000000000000000000000010000000000000000000L),

    Try("try", Category.Statement, SubCategory.Exception, 0b1100, 0b0000000000000100000000000000000000000000000001000000000000000000L),
    Throw("throw", Category.Statement, SubCategory.Exception, 0b1101, 0b0000000000000100000000000000000000000000000000100000000000000000L),

    Catch("catch", Category.Statement, SubCategory.Exception_Conjunction, 0b1110, 0b0000000000000100000000000000000000000000000000010000000000000000L),
    Finally("finally", Category.Statement, SubCategory.Exception_Conjunction, 0b1111, 0b0000000000000100000000000000000000000000000000001000000000000000L),

    New("new", Category.Expression, SubCategory.Creation, 0b001, 0b0000000000000100000000000000000000000000000000000100000000000000L),

    InstanceOf( "instanceof", Category.Expression, SubCategory.Polymorphism, 0b010, 0b0000000000000100000000000000000000000000000000000010000000000000L),
    This( "this", Category.Expression, SubCategory.Polymorphism, 0b011, 0b0000000000000100000000000000000000000000000000000001000000000000L),
    Super( "super", Category.Expression, SubCategory.Polymorphism, 0b100, 0b0000000000000100000000000000000000000000000000000000100000000000L),

    Void( "void", Category.Type,0b0001, 0b0000000000000100000000000000000000000000000000000000010000000000L),

    Bool( "boolean", Category.Type, SubCategory.Bit_1, 0b0010, 0b0000000000000100000000000000000000000000000000000000001000000000L),

    Byte( "byte", Category.Type, SubCategory.Bit_8, 0b0011, 0b0000000000000100000000000000000000000000000000000000000100000000L),

    Char( "char", Category.Type, SubCategory.Bit_16, 0b0100, 0b0000000000000100000000000000000000000000000000000000000010000000L),
    Short( "short", Category.Type, SubCategory.Bit_16, 0b0101, 0b0000000000000100000000000000000000000000000000000000000001000000L),

    Int( "int", Category.Type, SubCategory.Bit_32, 0b0110, 0b0000000000000100000000000000000000000000000000000000000000100000L),

    Float( "float", Category.Type, SubCategory.Bit_32, 0b0111, 0b0000000000000100000000000000000000000000000000000000000000010000L),

    Double( "double", Category.Type, SubCategory.Bit_64, 0b1000, 0b0000000000000100000000000000000000000000000000000000000000001000L),

    Long( "long", Category.Type, SubCategory.Bit_64, 0b1001, 0b0000000000000100000000000000000000000000000000000000000000000100L),

    Const( "const", Category.Unused,0b01, 0b0000000000000100000000000000000000000000000000000000000000000010L),
    Goto( "goto", Category.Unused,0b10,   0b0000000000000100000000000000000000000000000000000000000000000001L);

    public final String keyword;
    public final Category category;
    public final SubCategory subCategory;
    public final int bitPattern;
    public final int shiftedBitPattern;
    public final long termBit;

    Keyword(String keyword, Category category, int bitPattern, long termBit){
        this.keyword = keyword;
        this.category = category;
        this.subCategory = null;
        this.bitPattern = bitPattern;
        this.shiftedBitPattern = bitPattern << category.shift;
        this.termBit = termBit;
    }

    Keyword(String keyword, Category category, SubCategory subCategory, int bitPattern, long termBit){
        this.keyword = keyword;
        this.category = category;
        this.subCategory = subCategory;
        this.bitPattern = bitPattern;
        this.shiftedBitPattern = bitPattern << category.shift;
        this.termBit = termBit;
    }

    public static Keyword of(String name){
        switch(name){
            case "package"      : return Package;
            case "import"       : return Import;
            case "class"        : return Class;
            case "enum"         : return Enum;
            case "interface"    : return Interface;
            case "extends"      : return Extends;
            case "implements"   : return Implements;
            case "default"      : return Default;
            case "throws"       : return Throws;
            case "public"       : return Public;
            case "protected"    : return Protected;
            case "private"      : return Private;
            case "static"       : return Static;
            case "final"        : return Final;
            case "abstract"     : return Abstract;
            case "synchronized" : return Synchronized;
            case "volatile"     : return Volatile;
            case "transient"    : return Transient;
            case "native"       : return Native;
            case "strictfp"     : return StrictFp;
            case "assert"       : return Assert;
            case "if"           : return If;
            case "switch"       : return Switch;
            case "case"         : return Case;
            case "else"         : return Else;
            case "do"           : return Do;
            case "for"          : return For;
            case "while"        : return While;
            case "return"       : return Return;
            case "break"        : return Break;
            case "continue"     : return Continue;
            case "try"          : return Try;
            case "throw"        : return Throw;
            case "catch"        : return Catch;
            case "finally"      : return Finally;
            case "new"          : return New;
            case "instanceof"   : return InstanceOf;
            case "this"         : return This;
            case "super"        : return Super;
            case "void"         : return Void;
            case "boolean"      : return Bool;
            case "byte"         : return Byte;
            case "char"         : return Char;
            case "short"        : return Short;
            case "int"          : return Int;
            case "float"        : return Float;
            case "double"       : return Double;
            case "long"         : return Long;
            case "const"        : return Const;
            case "goto"         : return Goto;
            default : return null;
        }
    }

    public static Map<String,Keyword> MAP = new HashMap<>();
    static{
        Stream.of(Keyword.values()).forEach(k -> MAP.put(k.keyword, k));
    }

    /**
     * Describe all of the Keywords
     * @return
     */
    public static String describeAll(){
        StringBuilder sb = new StringBuilder();
        Stream.of(Keyword.values()).forEach(k -> sb.append( k.toString()).append('\n'));
        return sb.toString();
    }

    public Category getBin32(){
        return category;
    }

    @Override
    public int getBitPattern(){
        return bitPattern;
    }

    public String getValue(){
        return this.keyword;
    }

    @Override
    public int getShiftedBitPattern(){
        return shiftedBitPattern;
    }

    @Override
    public int getShift(){
        return this.category.getShift();
    }

    @Override
    public int getMask(){
        return this.category.getMask();
    }

    public String toString(){
        String bitPattern = "";
        for(int i=0;i<getShift();i++){
            bitPattern +="-";
        }
        bitPattern = Bitwise.bits( getBitCount(), getBitPattern() )+ bitPattern;
        int leading = 32 - bitPattern.length();
        for(int i=0;i<leading;i++){
            bitPattern = "-"+bitPattern;
        }
        if( this.subCategory == null ){

            return bitPattern+" "+category.getName()+"."+super.toString()+"["+ this.keyword +"]";
            //return Bitwise.bits( getShiftedBitPattern())+" "+category.getName()+"."+super.toString()+"["+ symbol +"]";
        }
        return bitPattern+" "+category.getName()+"."+subCategory.name()+"."+super.toString()+"["+ keyword +"]";
        //return Bitwise.bits( getShiftedBitPattern())+" "+category.getName()+"."+subCategory.name()+"."+super.toString()+"["+ symbol +"]";
    }


    public enum Category implements Bin32 {
        CodeUnit(   0b11,  20,"CodeUnit"),
        Declaration(0b111, 17,"Declaration"),
        Modifier(   0b1111,13,"Modifier"),
        Statement(  0b1111,9, "Statement"),
        Expression( 0b111, 6, "Expression"),
        Type(       0b1111,2, "Type"),
        Unused(     0b11,  0, "Unused");

        int mask;
        int shift;
        int shiftedMask;

        String name;

        Category(int mask, int shift,String name){
            this.name = name;
            this.mask = mask;
            this.shift = shift;
            this.shiftedMask = mask << shift;
        }

        public String getName(){ return this.name; }

        @Override
        public int getMask() {
            return mask;
        }

        @Override
        public int getShift() {
            return shift;
        }
    }

    public enum SubCategory implements Bin32 {
        Property(           "Property", Category.CodeUnit, 0b01, 0b10),           //package, import

        Type(               "Type",        Category.Declaration, 0b01, 0b11),            //class, enum, interface
        TypeModifier(       "TypeModifier", Category.Declaration, 0b100, 0b101),   //extends, implements
        MemberModifier(     "MemberModifier", Category.Declaration, 0b110, 0b111), //default, throws

        Access(             "Access", Category.Modifier, 0b01, 0b11),               //public protected private
        //Normal(             "Normal", Category.Modifier, 0b0100, 0b1110),          //abstract, final, native, static, strictfp, synchronized, transient, volatile

        Assertion(          "Assertion", Category.Statement, 0b0001),     //assert
        Condition(          "Condition", Category.Statement, 0b0010, 0b0011),     //if, switch
        Condition_Conjunction(    "Condition_Conjunction", Category.Statement, SubCategory.Condition, 0b0100, 0b0101), //case, else
        Loop(                  "Loop", Category.Statement, 0b0110,0b1000),            //do, for, while
        Interrupt("Interrupt", Category.Statement, 0b1001,0b1011),       //break, continue, return
        Exception("Exception", Category.Statement, 0b1100, 0b1101),      //throw try
        Exception_Conjunction("Exception_Conjunction", Category.Statement, SubCategory.Exception, 0b1110, 0b1111), //catch finally

        Creation( "Creation", Category.Expression, 0b001),       //new
        Polymorphism( "Polymorphism", Category.Expression, 0b010, 0b100),   //instanceof, this, super

        //Void("Void", Category.Type, 0b0001),   //void
        Bit_1("1-Bit", Category.Type, 0b0010),  //boolean
        Bit_8("8-Bit", Category.Type, 0b0011), //byte
        Bit_16("16-Bit", Category.Type, 0b0100, 0b0101), //char, short
        Bit_32("32-Bit", Category.Type, 0b0110, 0b0111), //int, float
        Bit_64("64-Bit", Category.Type, 0b1000, 0b1001); //double, long

        //Unused("Unused", Category.Unused, 0b01, 0b10);   //const, goto

        Category category;
        SubCategory sub;
        int minBin;
        int maxBin;

        String name;
        SubCategory(String name, Category cat, SubCategory sub, int minBin, int maxBin){
            this.name = name;
            this.category = cat;
            this.sub = sub;
            this.minBin = minBin;
            this.maxBin = maxBin;
        }

        SubCategory(String name, Category cat, int bin){
            this.name = name;
            this.category = cat;
            this.minBin = bin;
            this.maxBin = bin;
        }

        SubCategory(String name, Category cat, int minBin, int maxBin){
            this.name = name;
            this.category = cat;
            this.minBin = minBin;
            this.maxBin = maxBin;
        }

        public String getName(){ return name; }

        @Override
        public int getMask() {
            return category.getMask();
        }

        @Override
        public int getShift() {
            return category.getShift();
        }

        public boolean hit(int word){
            int bitPattern = this.category.getBitPattern(word);
            return bitPattern >= minBin && bitPattern <= maxBin;
        }
    }
}
