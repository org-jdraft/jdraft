package org.parseout.java;

import junit.framework.TestCase;

public class NumericEncodeTest extends TestCase {

    public void testExpectFail(){
        //testMultipleDecimal
        //double f = 0.1;

        assertFalse( NumericEncoder.isOctalPrefix(NumericEncoder.numericEncode("0.1")));
        assertTrue( NumericEncoder.hasDecimalPoint( NumericEncoder.numericEncode("0.1")));
        try{ NumericEncoder.numericEncode("0.1L"); fail("expected exception decimal with L postfix"); } catch(Exception e){}


        try{ NumericEncoder.numericEncode(".1."); fail("expected exception"); } catch(Exception e){}
        try{ NumericEncoder.numericEncode("1_"); fail("expected exception"); } catch(Exception e){}
        try{ NumericEncoder.numericEncode("_1"); fail("expected exception"); } catch(Exception e){}
        try{ NumericEncoder.numericEncode("0x1.0"); fail("expected exception"); } catch(Exception e){}
        try{ NumericEncoder.numericEncode("0B1.1"); fail("expected exception"); } catch(Exception e){}

    }

    public void testScientificNotation(){
        double i = 1E1;
        //double d = 1E.1;

        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1E1")));
        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1E-1")));
        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1E+1")));

        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1e1")));
        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1e-1")));
        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("1e+1")));
        assertTrue( NumericEncoder.isScientificNotation(NumericEncoder.numericEncode("-   1.0e-20")));

        //this should fail
        try{ NumericEncoder.numericEncode("1E"); fail("Expected exception");} catch(Exception e){}
        try{ NumericEncoder.numericEncode("1E_"); fail("Expected exception");} catch(Exception e){}
        try{ NumericEncoder.numericEncode("1E.1"); fail("Expected exception");} catch(Exception e){}
    }

    public void testSignFirst(){
        assertTrue( NumericEncoder.isPlusPrefix(NumericEncoder.numericEncode("+1")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-1")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-.1")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("- 1")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-      1"))); //with spaces tabs
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-      \n1"))); //with spaces tabs & carriage return
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-      \n.1d"))); //with spaces tabs & carriage return
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-      \n.10000000000000000d"))); //with spaces tabs & carriage return

        //long i = - 0B1111L;
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("- 0x1111")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("- 0B1111L")));
    }

    public void testLeadingZeroOctalOrDecimal(){
        assertFalse( NumericEncoder.isOctalPrefix(NumericEncoder.numericEncode("0000.0")));
        assertTrue( NumericEncoder.isOctalPrefix(NumericEncoder.numericEncode( "01")));
        assertTrue( NumericEncoder.isOctalPrefix(NumericEncoder.numericEncode( "01234567")));
        //non octal digits (8,9) with octal prefix
        try{ NumericEncoder.numericEncode( "0123456789"); fail("Expected exception"); } catch(Exception e){ /*expected*/ }
        //non octal digits (hex digits)
        try{ NumericEncoder.numericEncode( "01234567ABCDEF"); fail("Expected exception"); } catch(Exception e){ /*expected*/ }


        assertTrue( NumericEncoder.hasDecimalPoint(NumericEncoder.numericEncode("-0000.0")));
    }

    public void testTypePostfixL(){
        assertFalse( NumericEncoder.hasDecimalPoint(NumericEncoder.numericEncode("-1_000L")));
        assertTrue( NumericEncoder.isTypePostfixL(NumericEncoder.numericEncode("-1_000L")));
        assertTrue( NumericEncoder.isTypePostfixL(NumericEncoder.numericEncode("-1_000l")));
        assertTrue( NumericEncoder.isTypePostfixL(NumericEncoder.numericEncode("1L")));
        assertTrue( NumericEncoder.isTypePostfixL(NumericEncoder.numericEncode("1l")));
    }

    public void testTypePostfixF(){
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("1F")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("1f")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("1.0F")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("1.0f")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("- 1.0F")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("+  1.0f")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("+  1.0e01f")));
        assertTrue( NumericEncoder.isTypePostfixF(NumericEncoder.numericEncode("+  1.0e12345f")));

    }

    public void testTypePostfixD(){
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("1D")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("1d")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("1.0D")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("1.0d")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("- 1.0D")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("+  1.0d")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("+  1.0e01d")));
        assertTrue( NumericEncoder.isTypePostfixD(NumericEncoder.numericEncode("+  1.0E12345d")));
    }
    public void testGood(){
        assertTrue( NumericEncoder.isDigits(NumericEncoder.numericEncode("1")));
        assertTrue( NumericEncoder.hasDecimalPoint(NumericEncoder.numericEncode("1.")) );
        assertTrue( NumericEncoder.hasDecimalPoint(NumericEncoder.numericEncode(".1")) );


        assertTrue( NumericEncoder.isHexPrefix(NumericEncoder.numericEncode("0x1")) );
        assertTrue( NumericEncoder.isBinaryPrefix(NumericEncoder.numericEncode("0b1")) );
        assertTrue( NumericEncoder.isOctalPrefix(NumericEncoder.numericEncode("01")) );

        assertTrue( NumericEncoder.hasUnderscore(NumericEncoder.numericEncode("1_000")) );
        assertTrue( NumericEncoder.isPlusPrefix(NumericEncoder.numericEncode("+1")));
        assertTrue( NumericEncoder.isMinusPrefix(NumericEncoder.numericEncode("-1")));


        //0 to 9 (single digit)
        for(int i=0;i<9;i++){
            assertEquals(1, NumericEncoder.numericEncode( i+""));
        }

        //test fail on NO digits
        //test fail on No digits after scientificNotation
        //test
        double[] d = {
                .1, //start with .
                .1d,
                .1D,
                1., //end with dot
                1.d,
                1.D,
                -1,  //sign
                -.1
                -1., //end with .
                +1,
                +.1
                +1., //end with .
                -   1, //sign with spaces/tabs
                -   .1,

                +0.1e1,
                - 1.2e2, //sign and whitespace
                -           1.2E-02
        };
        double f = 1.0e-1;


    }

    public static final float F = 0.1_345f;
    public static final int H = 0xDEAD_BEEF;

    //public static final double sci = 3E-4;//this works... (i.e. scientific notation w/o a decimal point)

    //public static final double HexDec = 0b01.111;
    //public static final float BINDEC = 0b.111;

    //trailing underscore is invalid
    //public static final int trailingUnd = 3_;
}
