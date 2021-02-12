package org.jdraft;

import junit.framework.TestCase;

/**
 * tests number expressions
 * int
 * long
 * double
 * float
 *
 * hex
 * octal
 *
 * exponent eE p
 */
public class _numberExprTest extends TestCase {

    public void testIntLong(){
        _intExpr _ie = _intExpr.of("1");
        _ie = _intExpr.of("0B10101001");//binary
        _ie = _intExpr.of("01234567");//octal
        _ie = _intExpr.of("0xdead");//hexadecimal


        _longExpr _le = _longExpr.of("1L");
        _le = _longExpr.of("0b10101001L");
        _le = _longExpr.of("01234567L");//octal
        _le = _longExpr.of("0xdead_beefL");//hexadecimal

    }

    public void testDoubleFloat(){
        _doubleExpr _de = _doubleExpr.of("1F");
        _de = _doubleExpr.of(".1");
        _de = _doubleExpr.of(".1f");
        _de = _doubleExpr.of(".1d");
        _de = _doubleExpr.of("1f");
        _de = _doubleExpr.of("1.d");
        _de = _doubleExpr.of("1.f");
        //hexidecimal floating point
        double d = 0x1.2p2;
        _de = _doubleExpr.of("0x1.2p2");
        _de = _doubleExpr.of("0x1.2p-2");
        _de = _doubleExpr.of("0x3.14159p+2");



    }
}
