package org.parseout.java;

import junit.framework.TestCase;
import org.parseout.binary.Bitwise;

import java.util.stream.Stream;

public class KeywordTest extends TestCase {

    public void testK(){
        Stream.of(Keyword.values()).forEach(k -> System.out.println(Bitwise.bits(k.termBit)+" "+ k.name()));
    }
}
