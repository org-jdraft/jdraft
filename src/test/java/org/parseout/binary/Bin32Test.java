package org.parseout.binary;

import junit.framework.TestCase;

public class Bin32Test extends TestCase {

    /**
     * Verify the mask and shift is valid for the binary address
     */
    public void testInvalidBin(){
        try {
            Bin32.of(0b0, 0);
            fail ("expected exception");
        }catch(Exception e){ }

        try {
            Bin32.of(0b101, 0);
            fail("expected exception for non-contiguous mask");
        }catch(Exception e){}

        try {
            Bin32.of(0b1, 65);
            fail("expected exception for invalid shift");
        }catch(Exception e){}

        try {
            Bin32.of(0b1, -1);
            fail("expected exception for invalid shift");
        }catch(Exception e){}

        try {
            Bin32.of(0b11111, 30);
            fail("expected exception for invalid shift/mask");
        }catch(Exception e){}

    }
    public void testRead1BitBin(){

        assertTrue(Bin32.of(0b1,0).hit(1));
        assertEquals(1, Bin32.of(0b1,0).read(1));
        assertEquals(0, Bin32.of(0b1,0).read(0));

        assertEquals(0, Bin32.of(0b1,0).read(-1 << 1));

        assertTrue( Bin32.of(1, 0).matches(1, 1));
    }




}
