package org.parseout.binary;

import junit.framework.TestCase;

public class Bin32Test extends TestCase {

    public void testBiWordStore(){

        Bin32.Field2<Boolean,Boolean> bistore = new Bin32.Field2<>(
                new Bin32.BinStore32<>(0b01, "single", Bin32.BOOL),
                new Bin32.BinStore32<>(0b10, "dependants", Bin32.BOOL) );

        assertTrue(bistore.loadA(0b11));
        assertTrue(bistore.loadB(0b11));

        assertFalse(bistore.loadA(0b0));
        assertFalse(bistore.loadB(0b0));

        Bin32.Field2<Boolean,String> hybridStore = new Bin32.Field2<>(
                new Bin32.BinStore32<>(0b0001, "online", Bin32.BOOL),
                new Bin32.BinStore32<>(0b1110, "grade", Bin32.CodeTable.of(null, "A","B","C","D","F")) );

        assertEquals(0b0011, hybridStore.store(true, "A"));
        assertEquals(0b0101, hybridStore.store(true, "B"));
        assertEquals(0b0111, hybridStore.store(true, "C"));
        assertEquals(0b1001, hybridStore.store(true, "D"));
        assertEquals(0b1011, hybridStore.store(true, "F"));

        hybridStore = Bin32.Field2.of(
                0b0001, "online", Bin32.BOOL,
                0b1110, "grade", Bin32.CodeTable.of(null, "A","B","C","D","F"));

        assertEquals(0b0011, hybridStore.store(true, "A"));
        assertEquals(0b0101, hybridStore.store(true, "B"));
        assertEquals(0b0111, hybridStore.store(true, "C"));
        assertEquals(0b1001, hybridStore.store(true, "D"));
        assertEquals(0b1011, hybridStore.store(true, "F"));

        assertEquals(0b0010, hybridStore.store(false, "A"));
        assertEquals(0b0100, hybridStore.store(false, "B"));
        assertEquals(0b0110, hybridStore.store(false, "C"));
        assertEquals(0b1000, hybridStore.store(false, "D"));
        assertEquals(0b1010, hybridStore.store(false, "F"));

        //load individual values
        Object[] loaded = hybridStore.load(0b000);
        assertEquals( loaded[0], false );
        assertNull( loaded[1]);

        assertFalse( hybridStore.loadA(0b00));
        assertNull( hybridStore.loadB(0b0));


        assertTrue( hybridStore.loadA(0b01));
        assertEquals("A", hybridStore.loadB(0b0011));
        assertEquals("B", hybridStore.loadB(0b0101));
        assertEquals("C", hybridStore.loadB(0b0111));
        assertEquals("D", hybridStore.loadB(0b1001));
        assertEquals("F", hybridStore.loadB(0b1011));

        //update
        assertEquals( 0b0010, hybridStore.updateB(0, "A"));
        assertEquals( 0b0100, hybridStore.updateB(0, "B"));
        assertEquals( 0b0110, hybridStore.updateB(0, "C"));
        assertEquals( 0b1000, hybridStore.updateB(0, "D"));
        assertEquals( 0b1010, hybridStore.updateB(0, "F"));

        assertEquals( 0b0011, hybridStore.updateB(1, "A"));
        assertEquals( 0b0101, hybridStore.updateB(1, "B"));
        assertEquals( 0b0111, hybridStore.updateB(1, "C"));
        assertEquals( 0b1001, hybridStore.updateB(1, "D"));
        assertEquals( 0b1011, hybridStore.updateB(1, "F"));

        assertEquals( 0b0010, hybridStore.updateA(0b0011, false));

    }
    public void testBijection(){
        Bin32.Bijection<String> Bistring = new Bin32.Bijection<String>(){

            @Override
            public String apply(int value) {
                return ""+value;
            }

            @Override
            public Integer apply(String s) {
                return Integer.parseInt(s);
            }
        };

        assertEquals(new Integer(1), Bistring.apply("1"));
        assertEquals("1", Bistring.apply(1));

        Bin32.Bijection<Integer> Bi = new Bin32.Bijection<Integer>(){

            @Override
            public Integer apply(int value) {
                return value;
            }

            @Override
            public Integer apply(Integer integer) {
                return integer;
            }
        };

        assertEquals(new Integer(1), Bi.apply(1));
    }

    public void testBitAddressConstructor(){
        Bin32.BinAddress32 b = new Bin32.BinAddress32(0b000010000);
        assertEquals(4, b.getShift());

        assertEquals(0b000010000, b.getShiftedMask());
        assertEquals(1, b.getMask());
    }

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
