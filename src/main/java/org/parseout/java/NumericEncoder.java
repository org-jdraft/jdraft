package org.parseout.java;

public class NumericEncoder {
    public static final int SIGN_MASK =          0b11000000000;
    public static final int PLUS_SIGN =          0b10000000000; // leading +
    public static final int MINUS_SIGN =         0b01000000000;  // leading -

    public static final int HEX_TYPE_PREFIX =    0b00100000000;  // hexidecimal number 0x or 0X
    public static final int OCTAL_TYPE_PREFIX =  0b00010000000;  // octal number 0...
    public static final int BINARY_TYPE_PREFIX = 0b00001000000;  // binary number 0b/0B ...

    public static final int DECIMAL_POINT      = 0b00000100000;  // leading or interior a decimal point
    public static final int UNDERSCORE         = 0b00000010000;  // use of _ 1_000

    public static final int SCIENTIFIC_NOTION  = 0b00000001000;  // 3.14E25

    //trailing nothing 00
    public static final int POSTFIX_TYPE_MASK  = 0b00000000110;
    public static final int POSTFIX_TYPE_F     = 0b00000000010;
    public static final int POSTFIX_TYPE_D     = 0b00000000100;
    public static final int POSTFIX_TYPE_L     = 0b00000000110;

    public static final int DIGITS =             0b00000000001;


    public static int numericEncode(String str){
        return numericEncode(str.toCharArray());
    }

    private static int numericEncodeTail(int encoded, char[] chars, int index){
        while(index < chars.length -1){
            System.out.println( "intermediate char["+index+"] "+chars[index] );
            switch( chars[index] ){
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':{
                    encoded |= DIGITS;
                    break;
                }
                case '8': case '9': {
                    if( isOctalPrefix(encoded) ){
                        //cant use the character '9' if a octal number
                        throw new RuntimeException("cannot use digits '9' or '8' with octal encoded number ... (0 prefix)");
                    }
                    encoded |= DIGITS;
                    break;
                }
                case 'A': case 'a':case 'B':case 'b':case 'C':case 'c':case 'D':case 'd':case 'F':case 'f':{
                    if( ! isHexPrefix(encoded) ){
                        //cant do hex
                        throw new RuntimeException("cannot use hex char '"+chars[index]+"' without (0x prefix)");
                    }
                    encoded |= DIGITS;
                    break;
                }
                case 'E': case 'e':{ //COULD be a
                    if( isHexPrefix(encoded) ){
                        encoded |= DIGITS;
                        break;
                    }
                    if( isScientificNotation(encoded)){
                        throw new RuntimeException("cannot use e/E for scientific notation more than once");
                    }
                    if( isDigits(encoded) ){
                        encoded |= SCIENTIFIC_NOTION;
                        break;
                    }
                    throw new RuntimeException("cannot start numeric with E/e (before digits)");
                }
                case '_' : {
                    encoded |= UNDERSCORE; //no limit the # of underscores, cant be leading or trailing
                    break;
                }
                case '.' : {
                    System.out.println( "(.) MID CHAR DECIMAL POINT");
                    if( hasDecimalPoint(encoded) ){
                        throw new RuntimeException("cannot have multiple .'s (decimal points) in number");
                    }
                    if( isBinaryPrefix(encoded) ){
                        throw new RuntimeException("cannot have mix 0b/0B binary number with .'s (decimal points) in number");
                    }
                    if( isHexPrefix(encoded) ){
                        throw new RuntimeException("cannot have mix 0x hex prefix with .'s (decimal points) in number");
                    }
                    if( isScientificNotation(encoded) ){
                        throw new RuntimeException("cannot have .'s (decimal points) AFTER e/E (scientific notation) in number");
                    }
                    encoded |= DECIMAL_POINT;
                    break;
                }
                case '+': case '-':{ //in the interior, -/+ can ONLY be applied if the previous character is scientific notation (e, E)
                    if( (encoded & SCIENTIFIC_NOTION) > 0 && (chars[index-1] == 'e' || chars[index-1] =='E')){
                        System.out.println("Sign "+chars[index]+" after e (scientific notation)");
                        break; //the + or minus precedes "e" "E" for scientific notation
                    } else{
                        throw new RuntimeException("cannot use sign (+, -) inside except if for scientific notation (i.e. \"2.0e-3\", \"1.9E+4\"");
                    }
                }
            }
            index ++; //move to the last character
        }

        /**  handle last char */
        System.out.println( "last char["+index+"] "+chars[index] );
        switch(chars[index]){
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': {
                System.out.println( "last char digit : "+ chars[index]);
                encoded |= DIGITS;
                break;
            }
            case '8': case '9': {
                if( isOctalPrefix(encoded) ){
                    //cant use the character '8' '9' if a octal number (0 prefix)
                    throw new RuntimeException("cannot use digits '9' or '8' with octal encoded number ... (0 prefix)");
                }
                System.out.println( "last char digit : "+ chars[index]);
                encoded |= DIGITS;
                break;
            }
            case 'l': case 'L':{
                System.out.println( "last char type postfix : "+chars[index]);
                if( hasDecimalPoint(encoded) ){
                    throw new RuntimeException("cannot use \"L\"  \"l\" postfix for decimal number");
                }else{
                    encoded |= POSTFIX_TYPE_L;
                    break;
                }
            }
            case 'f': case 'F':{
                if( isBinaryPrefix(encoded) ) {
                    throw new RuntimeException("cannot use \"f\"  \"F\" postfix for 0b/0B binary number");
                }
                if( isOctalPrefix(encoded) ) {
                    throw new RuntimeException("cannot use \"f\"  \"F\" postfix for 0 octal number");
                }
                if( isHexPrefix(encoded) ) {
                    System.out.println( "last char hex digit : "+ chars[index]);
                    encoded |= DIGITS;
                    //for hex the last char can be a hex F/f
                    break;
                }
                System.out.println( "last char type postfix : "+ chars[index]);
                encoded |= POSTFIX_TYPE_F;
                break;
            }
            case 'd': case 'D':{

                if( isBinaryPrefix(encoded) ){ //if its a binary number
                    throw new RuntimeException("cannot use \"d\"  \"D\" type postfix for binary (0b prefix) number");
                }
                if( isHexPrefix(encoded) ) { //if its a integer/long and NOT a HEX number CANT use d/D
                    System.out.println( "last char hex digit : "+ chars[index]);
                    encoded |= DIGITS;
                    break; //the d/D is a number, not a postfix
                    //throw new RuntimeException("cannot use \"d\"  \"D\" postfix for hex (0x prefix) number");
                }
                if( isOctalPrefix(encoded) ) { //if its a integer/long and NOT a HEX number CANT use d/D
                    throw new RuntimeException("cannot use \"d\"  \"D\" postfix for octal (0 prefix) number");
                }
                encoded |= POSTFIX_TYPE_D; //allow trailing d
                break;
            }
            case '_' :{
                throw new RuntimeException("cannot use '_' as last character of number");
            }
            case '.' : {
                System.out.println( "last char . (decimal point)");
                if( hasDecimalPoint( encoded )){
                    throw new RuntimeException("cannot have multiple .'s (decimal points) in number");
                }
                if( isBinaryPrefix(encoded ) ){
                    throw new RuntimeException("cannot have mix 0b/0B binary number with .'s (decimal points) in number");
                }
                if( isHexPrefix(encoded) ){
                    throw new RuntimeException("cannot have mix 0x hex prefix with .'s (decimal points) in number");
                }
                if( isScientificNotation(encoded ) ){
                    throw new RuntimeException("cannot have .'s (decimal points) after e/E (scientific notation) in number");
                }
                encoded |= DECIMAL_POINT;
                break;
            }
            case 'A': case 'a':case 'B':case 'b':case 'C':case 'c': case 'E': case 'e':{
                if( !isHexPrefix(encoded)){
                    //cant do hex
                    throw new RuntimeException("cannot use hex char '"+chars[index]+"' without (0x prefix)");
                }
                System.out.println( "last char hex digit : "+ chars[index]);
                encoded |= DIGITS;
                break;
            }
        }
        return encoded;
    }

    public static int numericEncode( char[] chars ){
        int encoded = 0;
        int index = 0;

        /** Handle first digits */

        /** Handle leading + or - */
        if( chars[index]=='+' || chars[index]=='-'){
            if( chars[index]=='+' ) {
                System.out.println( "+ prefix");
                encoded |= PLUS_SIGN;
            } else{
                System.out.println( "- prefix");
                encoded |= MINUS_SIGN;
            }
            index++;
            //in this situation ONLY can i have whitespace between the sign and the next
            while( index < chars.length && Character.isWhitespace( chars[index]) ){
                System.out.println( "whitespace after +/- sign");
                index++;
            }
        }

        //System.out.println( "INDEX AFTER SIGN TEST "+ index);
        switch( chars[index] ){
            case '.' : {
                System.out.println( "Leading . (decimal point)");
                encoded |= DECIMAL_POINT;
                index++;
                break;
            }
            case '0' : {
                //System.out.println( "ZERO ");
                if( chars.length > index + 1 ){
                    switch( chars[index +1] ){
                        case 'x': case 'X': {
                            //System.out.println( "X");
                            if( chars.length > index+1) {
                                System.out.println( "0x 0X (Hex Prefix)");
                                encoded |= HEX_TYPE_PREFIX;
                                index+=2;
                                break;
                            } else{
                                throw new RuntimeException("0x... Hex numbers must have at least (1) digit");
                            }
                        }
                        case 'b': case 'B': {
                            if( chars.length > index + 1 ) {
                                System.out.println("0b 0B (Binary prefix)");
                                encoded |= BINARY_TYPE_PREFIX;
                                index+=2;
                                break;
                            } else{
                                throw new RuntimeException("0b...Binary numbers must have at least 1 digit");
                            }
                        }
                        default :
                            if( chars.length > index && Character.isDigit( chars[index+1])) { //could be 0777(octal) 0.1(decimal)
                                // if this is octal 0777 or decimal 00000.00001
                                // based on if the remaining chars (after index) are decimal, then it's decimal, otherwise octal
                                int restEncoded = numericEncodeTail(encoded, chars, index+1);
                                if( hasDecimalPoint( restEncoded ) ){
                                    return restEncoded; //it isnt octal, because octal cant have decimal point
                                } else{
                                    System.out.println( "0 (Octal prefix)");
                                    encoded |= OCTAL_TYPE_PREFIX;
                                }
                            } else { //
                                System.out.println("first char 0");
                                encoded |= DIGITS;
                                break;
                            }
                    }
                } else{
                    encoded |= DIGITS;
                    break;
                }
            }
            case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':{
                System.out.println( "First digit "+ chars[index]);
                encoded |= DIGITS;
                index ++;
                break; //any simple digits will do
            }
            default: throw new RuntimeException("cannot encode numeric value with "+chars[0]+" first char");
        }

        if( index >= chars.length){
            if ( isDigits( encoded)){
                return encoded;
            }
            throw new RuntimeException("Number must have digits");
        }
        /****** handle INTERIOR digits */
        return encoded | numericEncodeTail(encoded, chars, index);
    }

    public static boolean isDigits(int encodedNumber){
        return (encodedNumber & DIGITS) == DIGITS;
    }

    public static boolean isTypePostfix( int encodedNumber){
        return (encodedNumber & POSTFIX_TYPE_MASK) != 0;
    }
    public static boolean isTypePostfixL(int encodedNumber){
        return (encodedNumber & POSTFIX_TYPE_MASK) == POSTFIX_TYPE_L;
    }

    public static boolean isTypePostfixD(int encodedNumber){
        return (encodedNumber & POSTFIX_TYPE_MASK) == POSTFIX_TYPE_D;
    }

    public static boolean isTypePostfixF(int encodedNumber){
        return (encodedNumber & POSTFIX_TYPE_MASK) == POSTFIX_TYPE_F;
    }

    public static boolean isScientificNotation(int encodedNumber){
        return (encodedNumber & SCIENTIFIC_NOTION) == SCIENTIFIC_NOTION;
    }

    public static boolean isBinaryPrefix(int encodedNumber){
        return (encodedNumber & BINARY_TYPE_PREFIX) == BINARY_TYPE_PREFIX;
    }

    public static boolean isOctalPrefix(int encodedNumber){
        return (encodedNumber & OCTAL_TYPE_PREFIX) == OCTAL_TYPE_PREFIX;
    }

    public static boolean isHexPrefix(int encodedNumber){
        return (encodedNumber & HEX_TYPE_PREFIX) == HEX_TYPE_PREFIX;
    }

    public static boolean isPlusPrefix (int encodedNumber){
        return (encodedNumber & PLUS_SIGN) == PLUS_SIGN;
    }

    public static boolean isMinusPrefix (int encodedNumber){
        return (encodedNumber & MINUS_SIGN) == MINUS_SIGN;
    }

    public static boolean hasDecimalPoint(int encodedNumber){
        return (encodedNumber & DECIMAL_POINT) == DECIMAL_POINT;
    }

    public static boolean hasUnderscore(int encodedNumber){
        return (encodedNumber & UNDERSCORE) == UNDERSCORE;
    }
}
