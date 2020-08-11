package org.parseout.java;

import junit.framework.TestCase;
import org.parseout.binary.Bitwise;

import java.util.stream.Stream;

public class SymbolicTest extends TestCase {

    public void testD(){
        //Stream.of(Symbolic.values()).forEach(s -> System.out.println( Bitwise.bits( s.getShiftedBitPattern()) +" "+s.symbol));

        Stream.of(Symbolic.values()).forEach(s -> System.out.println( Bitwise.bits( s.symbolBit) +" "+s.symbol));
    }
}
