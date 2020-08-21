package org.parseout.binary;

import junit.framework.TestCase;

public class Bin32Test extends TestCase {

    public void testFieldsB(){
        assertEquals(1, Bin32.BOOL.maxBin());
        Bin32.BinStore<Boolean> bs = new Bin32.BinStore<>(1 << 31, "b", Bin32.BOOL);

        Bin32.Field4<Integer,Integer,Integer, Boolean> PARTS =
                Bin32.Field4.of(
                        "Length",  Bin32.Count.ofBits( 16 ), //16 bits
                        "Kind",    Bin32.Count.ofBits(  8 ), //8 bits
                        "Use",     Bin32.Count.ofBits(  7 ), //7 bits
                        "Context", Bin32.BOOL                //1 bit
                );

        //min
        assertEquals(0, (int)PARTS.loadA(0));
        //max
        assertEquals(0b1111111111111111, (int)PARTS.loadA(0b1111111111111111));
        assertEquals( 0b11111111, (int)PARTS.loadB( ( 0b11111111<<16) ));
        assertEquals( 0b1111111, (int)PARTS.loadC( ( 0b1111111<<(16+8))));
    }

    public void testCount(){
        Bin32.Count bc = Bin32.Count.of(100);
        assertEquals(new Integer(0), bc.apply(0));
        assertEquals(new Integer(0), bc.apply(new Integer(0)));

        assertEquals(new Integer(100), bc.apply(100));
        assertEquals(new Integer(100), bc.apply(new Integer(100)));

        Bin32.Field2<Integer, Integer> ranges = Bin32.Field2.of(
                "0-100", Bin32.Count.of(100),
                "0-20", Bin32.Count.of(20));
        int word = ranges.store(0,0);
        assertEquals(0, word);
        word = ranges.store(1,1);
        assertEquals(1, (int)ranges.loadA(word));
        assertEquals(1, (int)ranges.loadB(word));

        //lets test permutations using Counts
        for(int i=0;i<100;i++){
            for(int j=0;j<20;j++){
                //store and load round trip
                int packedWord = ranges.store(i,j);
                assertEquals( i, (int)ranges.loadA(packedWord) );
                assertEquals( j, (int)ranges.loadB(packedWord) );
            }
        }

        ranges = Bin32.Field2.of(
                "nullable 0-100", Bin32.Nullable.of(Bin32.Count.of(100)),
                "nullable 0-20 ",  Bin32.Nullable.of( Bin32.Count.of(20)));
        assertEquals(0, ranges.store(null, null));
        assertEquals(1, ranges.store(0, null));
        System.out.println( ranges.binStoreA.address );
        System.out.println( ranges.binStoreB.address );

        System.out.println( ranges.binStoreA );
        System.out.println( ranges.binStoreB );

        System.out.println( ranges );
    }

    enum Suit{
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS
    }

    public void testFields(){
        //I need a version that can calculate the bin addresses positions / shift
        Bin32.Field3<Suit,Boolean,String> triStore = Bin32.Field3.of(
                0b00000111, "suit", Bin32.Nullable.of( Bin32.EnumCodeTable.of(Suit.class, false)),
                0b00001000, "facecard", Bin32.BOOL,
                0b11110000, "seatAlpha", Bin32.CodeTable.of("A","B", "C","D","E","F","G","H") );
        Object[] vals = triStore.load(0);
        System.out.println( triStore );
        assertEquals( null, vals[0]);
        assertFalse( (Boolean)vals[1]);
        assertEquals("A", vals[2]);
        assertNull(triStore.loadA(0));

        triStore = Bin32.Field3.of(
                "suit", Bin32.Nullable.of( Bin32.EnumCodeTable.of(Suit.class, false)),
                "facecard", Bin32.BOOL,
                "seatAlpha", Bin32.CodeTable.of("A","B", "C","D","E","F","G","H") );

        System.out.println( triStore );
        vals = triStore.load(0);
        assertEquals( null, vals[0]);
        assertFalse( (Boolean)vals[1]);
        assertEquals("A", vals[2]);
        assertNull(triStore.loadA(0));

        assertEquals( Suit.SPADES, triStore.loadA(1));
        assertEquals( Suit.HEARTS, triStore.loadA(2));
        assertEquals( Suit.DIAMONDS, triStore.loadA(3));
        assertEquals( Suit.CLUBS, triStore.loadA(4));
    }

    public void testField2(){

        Bin32.Field2<Boolean,Boolean> bistore = new Bin32.Field2<>(
                new Bin32.BinStore<>(0b01, "single", Bin32.BOOL),
                new Bin32.BinStore<>(0b10, "dependants", Bin32.BOOL) );

        assertTrue(bistore.loadA(0b11));
        assertTrue(bistore.loadB(0b11));

        assertFalse(bistore.loadA(0b0));
        assertFalse(bistore.loadB(0b0));

        Bin32.Field2<Boolean,String> hybridStore = new Bin32.Field2<>(
                new Bin32.BinStore<>(0b0001, "online", Bin32.BOOL),
                new Bin32.BinStore<>(0b1110, "grade", Bin32.CodeTable.of(null, "A","B","C","D","F")) );

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
            public int maxBin() {
                return Integer.MAX_VALUE;
            }

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
            public int maxBin() {
                return -1;
            }

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
        Bin32.Address b = new Bin32.Address(0b000010000);
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
