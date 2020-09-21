package org.parseout.binary;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * (sub word addressing) i.e. storing and retrieving values from within a 32-bit word
 *
 * bit-wise address for a series of bits within a 32-bit word (int)
 *
 * <PRE>
 * --------------------------------  32-bit word (int)
 * 10987654321098765432109876543210  | bit address [0...31]
 * 3322222222221111111111            |
 *
 * ---------------------________---  8-bit Bin32 (address [3...10])
 *                      09876543     | Bin address [3...10]
 *                      1            |
 *
 * ---------------------________---  8-bit Bin (address [3...10] within the 32-bit word)
 * 00000000000000000000011111111000  | shiftedMask (1's at address range [3...10])
 *
 *
 * ...For example: if we want to extract the Bin bit pattern from a 32-bit word:
 *
 * 01001010100111101010110010010100  32-bit int
 * 00000000000000000000011111111000  shiftedMask (1's at address range [3...10])
 * &_______________________________  AND (32-bit int & shiftedMask)
 * 00000000000000000000010010010000  32-bit Bin Pattern
 * >>3_____________________________  shifted >> 3
 * 00000000000000000000000010010010  32-bit Bin Pattern (normalized)
 *                         10010010  8-bit Bin Pattern (relevant bits)
 * </PRE>
 */
public interface Bin32 {

    /** Returns a BitAddress32 implementation of the Bin32 based on the mask and shift*/
    static Address of(int mask, int shift ){
        return new Address(mask, shift);
    }

    /** Mask for the sequence of bits from within the word */
    int getMask();

    /** the left shift applied to the mask to align with the bin (where within the word the bin is) */
    int getShift();

    /** number of bits in the address (i.e. 1 to 32 for 32 bit ints) */
    default int getBitCount() {
        return Integer.bitCount( getMask());
    }

    /** Shifted mask used for identifying the bits within address of the word*/
    default int getShiftedMask(){
        return getMask() << getShift();
    }

    /** Extracts & returns the bit pattern from within 32-bit at the Bin address */
    default int read(int word){
        return (word & getShiftedMask()) >>> getShift();
    }

    /** return a 32-bit word with the the bits at the Bin32 address cleared (to 0's)*/
    default int clear(int word){
        return (word & (~getShiftedMask()));
    }

    /** returns a new 32-bit word where the address bits within the word are updated with the new bit pattern*/
    default int update(int word, int bitPattern){
        return (word & (~getShiftedMask())) | ( (bitPattern << getShift()) & getShiftedMask());
    }

    /** returns the bitPattern that exists within the Bin address */
    default int getBitPattern(int word){
        return (word & getShiftedMask()) >>> getShift();
    }

    /** does this word have ANY 1 bits within the bin set? */
    default boolean hit(int word){
        return (word & getShiftedMask()) != 0;
    }

    /**
     * Does the bitpattern within the Bin32 address match the bitPattern provided?
     * @param word the 32-bit word
     * @param bitPattern
     * @return
     */
    default boolean matches(int word, int bitPattern){
        return (word & (getShiftedMask())) == ( (bitPattern << getShift()));
    }

    static String DASHES = "--------------------------------";

    default String describeBin(){
        String s = DASHES.substring( 32 - getShift());
        s = Integer.toBinaryString(getMask()).replace("1", "_") + s;
        s = DASHES.substring(s.length()) + s;
        return s;
    }

    default String describeBinShift(){
        String s = describeBin();
        if( getBitCount() == 1 ){
            return s + "  ["+getShift()+"]";
        }
        return s + "  ["+getShift()+"..."+(getShift()+getBitCount()-1)+"]";
    }

    default String describe(){
        //String s = DASHES.substring( 32 - getShift());
        //s = Integer.toBinaryString(getMask()).replace("1", "_") + s;
        //s = DASHES.substring(s.length()) + s;
        String s = describeBin();
        if( getBitCount() == 1 ){
            return s + "  ["+getShift()+"] 1-bit";
        }
        return s + "  ["+getShift()+"..."+(getShift()+getBitCount()-1)+"] "+getBitCount()+"-bits";
    }

    /** Simple Bin32 implementation of an address of consecutive bits within a 32-bit word */
    class Address implements Bin32 {

        public static Address of(int shiftedMask){
            return new Address(shiftedMask);
        }

        public final int mask;
        public final int shift;

        /**
         * Simplified constructor, generally good if we use 0b (binary notation) to show the bits of a BitAddress32
         * especially for explaining the
         * <PRE>
         * Bin32.Address firstByte  = new Bin32.Address(0b11111111000000000000000000000000);
         * Bin32.Address secondByte = new Bin32.Address(0b00000000111111110000000000000000);
         * Bin32.Address thirdByte  = new Bin32.Address(0b00000000000000001111111100000000);
         * Bin32.Address fourthByte = new Bin32.Address(0b00000000000000000000000011111111);
         * </PRE>
         * @param shiftedMask
         */
        public Address(int shiftedMask){
            this ( shiftedMask >>> Integer.numberOfTrailingZeros(shiftedMask), Integer.numberOfTrailingZeros(shiftedMask));
        }

        public Address(int mask, int shift){
            this.mask = mask;
            this.shift = shift;

            if( shift < 0 || shift > 31){
                throw new RuntimeException("Invalid shift, must be >=0 and <=31");
            }
            int bitCount = Integer.bitCount( mask );
            if( bitCount == 0 ){
                throw new RuntimeException("Mask 0b"+ Bitwise.bits(mask)+" ...must have at least one 1 bits");
            }
            int runCount = maxConsecutiveOnes(mask);
            if( bitCount != runCount ){
                throw new RuntimeException("Mask 0b"+ Bitwise.bits(mask)+" ...must have CONSECUTIVE 1 bits");
            }

            if( Integer.bitCount( this.mask << this.shift) != bitCount ){
                throw new RuntimeException("Mask 0b"+ Bitwise.bits(mask)+" shifted << "+shift+" is "+ Bitwise.bits(mask << shift)+" loss of mask bits detected" );
            }
        }

        @Override
        public int getMask() {
            return mask;
        }

        @Override
        public int getShift() {
            return shift;
        }

        /**
         * used to verify that the mask is composed of an un-breaking sequence of bits
         * https://www.geeksforgeeks.org/length-longest-consecutive-1s-binary-representation/
         * @param bitPattern
         * @return
         */
        private static int maxConsecutiveOnes(int bitPattern) {
            // Initialize result
            int count = 0;

            // Count the number of iterations to
            // reach x = 0.
            while (bitPattern!=0) {
                // This operation reduces length
                // of every sequence of 1s by one.
                bitPattern = (bitPattern & (bitPattern << 1));

                count++;
            }
            return count;
        }

        public String toString(){
            return Bitwise.bits(this.getShiftedMask());
        }
    }

    /**
     * A bi directional function used for translating from some finite set of
     * target data types to a binary number that can be packed with other values
     * within a 32-bit word
     *
     * Converts from an int to a value or a value form an int
     * for example here is a simple Bijection
     * 0001  <-> "A" (value "A" maps to the 4-bit pattern 0001)
     * 0010  <-> "B"
     * 0011  <-> "C"
     * 0100  <-> "D"
     * 0101  <-> "E"
     * 0110  <-> "F"
     * 0111  <-> "G"
     * 1000  <-> "H"
     * @param <T> the Type of the data to be stored
     */
    interface Bijection<T> extends IntFunction<T>, Function<T, Integer> {
        int maxBin();
    }

    /**
     * Simple Boolean 1-bit bijection (1 = true, 0 = false)
     */
    Bijection<Boolean> BOOL = new Bijection<Boolean>(){

        public int maxBin(){ return 1;}

        @Override
        public Boolean apply(int value) {
            return value == 1;
        }

        @Override
        public Integer apply(Boolean aBoolean) {
            return aBoolean ? 1 : 0;
        }
    };

    /**
     * Takes a given bijection and introduces "nullability", in this case
     * will always make the 0 bit pattern equal null" (and will modify the
     * other entries in the bijection by 1)
     *
     * for example, given the BOOL
     *
     * //here are the (2) values that are stored in the single BOOL bijection
     *
     * //from bin to value
     * assertFalse(Bin32.BOOL.apply(0b0));
     * assertTrue(Bin32.BOOL.apply(0b1));
     * //from value to bin
     * assertEquals( 0, Bin32.BOOL.apply(false));
     * assertEquals( 1, Bin32.BOOL.apply(true));
     *
     * //here we can have the values {null, false, true} we added the mapping
     * //null <-> 0
     * //from bin to value
     * assertNull(Nullable.of(Bin32.BOOL).apply(0b00));
     * assertFalse(Nullable.of(Bin32.BOOL).apply(0b01));
     * assertNull(Nullable.of(Bin32.BOOL).apply(0b10));
     *
     * assertEquals( 0, Nullable.of(Bin32.BOOL).apply(null) );
     * assertEquals( 1, Nullable.of(Bin32.BOOL).apply(false) );
     * assertEquals( 2, Nullable.of(Bin32.BOOL).apply(true) );
     * @param <T>
     */
    class Nullable<T> implements Bijection<T>{

        public static <T> Nullable<T> of( Bijection<T> bijection ){
            return new Nullable(bijection);
        }

        Bijection<T> bijection;

        public int maxBin(){
            return bijection.maxBin() + 1; //0 now means null, so I have to bump by one
        }

        public Nullable(Bijection<T> bijection){
            this.bijection = bijection;
        }

        @Override
        public Integer apply(T t) {
            if( t == null ){
                return 0;
            }
            return bijection.apply(t) + 1;
        }

        @Override
        public T apply(int bin) {
            if( bin == 0){
                return null;
            }
            return bijection.apply( bin - 1);
        }
    }

    class EnumCodeTable<E> implements Bijection<E>{

        public static EnumCodeTable of(Class<? extends Enum> enumClass){
            return new EnumCodeTable(enumClass, false);
        }

        public static EnumCodeTable of(Class<? extends Enum> enumClass, boolean nullable){
            return new EnumCodeTable(enumClass, nullable);
        }

        final int nullOffset;
        final E[] enumConstants;
        final Class<? extends Enum> enumClass;


        public int maxBin(){
            return enumConstants.length -1; //0 is first value
        }

        public EnumCodeTable( Class<? extends Enum> enumClass, boolean nullable){
            if( nullable ){
                nullOffset = 1; //reserve 0 as the null value
            } else{
                nullOffset = 0;
            }
            this.enumClass = enumClass;
            enumConstants = (E[]) enumClass.getEnumConstants();
        }

        @Override
        public Integer apply(E e) {
            return ((Enum)e).ordinal() + nullOffset;
        }

        @Override
        public E apply(int bin) {
            if( bin == 0 && nullOffset == 1){
                return null;
            }
            return (E)enumClass.getEnumConstants()[bin-nullOffset];
        }
    }

    /**
     * a 0...n integer number count
     * NOTE: (no real range checking)
     */
    class Count implements Bijection<Integer>{

        public static Count ofBits(int bitCount){
            int num = (1 << bitCount) -1;
            return of(num);
        }

        public static Count of(int maxBin){
            return new Count(maxBin);
        }
        public final int maxBin;

        public Count(int maxBin){
            this.maxBin = maxBin;
        }

        @Override
        public int maxBin() {
            return maxBin;
        }

        @Override
        public Integer apply(Integer value) {
            return value;
        }

        @Override
        public Integer apply(int bin) {
            return bin;
        }
    }

    /**
     * Bijection of a code table (codified String lookup table)
     */
    class CodeTable implements Bijection<String>{

        public static CodeTable of( String...codes ){
            return new CodeTable(codes);
        }

        Map<String, Integer> codeToBin = new HashMap<>();
        String[] codes;

        public CodeTable(String...codes){
            this.codes = codes;
            for(int i=0;i<codes.length;i++){
                codeToBin.put(codes[i], i );
            }
        }

        public int maxBin(){
            return codes.length - 1;
        }

        @Override
        public Integer apply(String s) {
            return codeToBin.get(s);
        }

        @Override
        public String apply(int value) {
            return codes[value];
        }
    }

    /**
     * Stores a fixed width binary value into a Bin32.Address (consecutive bits within a 32-bit word)
     * that represents a value of type <T>
     * @param <T> the type of the value that is being stored
     */
    class BinStore<T> {
        public final String name;
        public final Address address;
        public final Bijection<T> bijection;

        public BinStore(int mask, String name, Bijection<T> bijection){
            this.address = Address.of(mask);
            this.name = name;
            this.bijection = bijection;
        }

        public T load(int word){
            return bijection.apply(address.read(word));
        }

        public int set( T value){
            return update( value, 0);
        }

        public int update( T value, int word){
            return address.update(word, bijection.apply(value) );
        }

        public String toString(){
            return address.toString()+ " "+name+ " [0..."+this.bijection.maxBin()+"] -> ["+this.bijection.apply(0)+"..."+this.bijection.apply(this.bijection.maxBin())+"]";
        }
    }

    /**
     * Packs / stores (2 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     */
    class Field2<A,B> {

        public static <A,B> Field2<A,B> of(String nameA, Bijection<A> bijectionA,
                                           String nameB, Bijection<B> bijectionB){
            int shift = 0;
            int leading0s = Integer.numberOfLeadingZeros(bijectionA.maxBin() );
            int addressA = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionB.maxBin() );
            int addressB = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            return of( addressA, nameA, bijectionA,
                    addressB, nameB, bijectionB);
        }

        public static <A, B> Field2<A,B> of(int addressA, String nameA, Bijection<A>bijectionA,
                                            int addressB, String nameB, Bijection<B>bijectionB){
            return new Field2<>(
                    new BinStore<>(addressA, nameA, bijectionA),
                    new BinStore<>(addressB, nameB, bijectionB));
        }

        public BinStore<A> binStoreA;
        public BinStore<B> binStoreB;

        public Field2(BinStore<A> binStoreA, BinStore<B> binStoreB){
            this.binStoreA = binStoreA;
            this.binStoreB = binStoreB;
        }

        public int store(A aValue, B bValue){
            return binStoreA.update(aValue, 0) | binStoreB.update(bValue, 0);
        }

        public Object[] load(int word){
            return new Object[]{ binStoreA.load(word), binStoreB.load(word)};
        }

        public A loadA(int word){
            return binStoreA.load(word);
        }

        public B loadB(int word){
            return binStoreB.load(word);
        }

        public int updateA(int word, A aValue){
            return binStoreA.update(aValue, word);
        }

        public int updateB(int word, B bValue){
            return binStoreB.update(bValue, word);
        }

        public String toString(){
            return binStoreA.toString()+System.lineSeparator()
                    +binStoreB.toString();
        }
    }


    /**
     * Packs / stores (3 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     * @param <C> the type of the 3rd value being stored
     */
    class Field3<A,B,C> {

        public static <A,B,C> Field3<A,B,C> of(String nameA, Bijection<A> bijectionA,
                                               String nameB, Bijection<B> bijectionB,
                                               String nameC, Bijection<C> bijectionC){
            int shift = 0;

            int leading0s = Integer.numberOfLeadingZeros(bijectionA.maxBin() );
            int addressA = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionB.maxBin() );
            int addressB = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressC = (-1 >>> leading0s) << shift;
            return of( addressA, nameA, bijectionA,
                    addressB, nameB, bijectionB,
                    addressC, nameC, bijectionC);
        }

        public static <A, B,C> Field3<A,B,C> of(int addressA, String nameA, Bijection<A>bijectionA,
                                                int addressB, String nameB, Bijection<B>bijectionB,
                                                int addressC, String nameC, Bijection<C>bijectionC){
            return new Field3<>(
                    new BinStore<>(addressA, nameA, bijectionA),
                    new BinStore<>(addressB, nameB, bijectionB),
                    new BinStore<>(addressC, nameC, bijectionC)
                    );
        }

        public final BinStore<A> binStoreA;
        public final BinStore<B> binStoreB;
        public final BinStore<C> binStoreC;

        public Field3(BinStore<A> binStoreA, BinStore<B> binStoreB, BinStore<C> binStoreC){
            //I need to verify that the binAddresses do NOT OVERLAP
            this.binStoreA = binStoreA;
            this.binStoreB = binStoreB;
            this.binStoreC = binStoreC;

            //they dont have to be contiguous, but I have to verify that they dont overlap
            int allFieldBits = binStoreA.address.getShiftedMask() | binStoreB.address.getShiftedMask() | binStoreC.address.getShiftedMask();
            int totalBits = Integer.bitCount( binStoreA.address.mask) + Integer.bitCount(binStoreB.address.mask) + Integer.bitCount(binStoreC.address.mask);
            if( totalBits != Integer.bitCount(allFieldBits) ){
                throw new RuntimeException("Invalid Field3, Addresses overlap");
            }
        }

        public int store(A aValue, B bValue, C cValue){
            return binStoreA.update(aValue, 0) | binStoreB.update(bValue, 0) | binStoreC.update(cValue, 0);
        }

        public Object[] load(int word){
            return new Object[]{ binStoreA.load(word), binStoreB.load(word), binStoreC.load(word)};
        }

        public A loadA(int word){
            return binStoreA.load(word);
        }

        public B loadB(int word){
            return binStoreB.load(word);
        }

        public C loadC(int word){
            return binStoreC.load(word);
        }

        public int updateA(int word, A aValue){
            return binStoreA.update(aValue, word);
        }

        public int updateB(int word, B bValue){
            return binStoreB.update(bValue, word);
        }

        public int updateC(int word, C cValue){
            return binStoreC.update(cValue, word);
        }

        public String toString(){
            return binStoreA.toString()+System.lineSeparator()
                    +binStoreB.toString()+System.lineSeparator()
                    +binStoreC.toString();
        }

    }

    /**
     * Packs / stores (4 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     * @param <C> the type of the 3rd value being stored
     * @param <D> the type of the 4th value being stored
     */
    class Field4<A,B,C,D> {

        public static <A,B,C,D> Field4<A,B,C,D> of(String nameA, Bijection<A> bijectionA,
                                               String nameB, Bijection<B> bijectionB,
                                               String nameC, Bijection<C> bijectionC,
                                               String nameD, Bijection<D> bijectionD){
            int shift = 0;

            int leading0s = Integer.numberOfLeadingZeros(bijectionA.maxBin() );
            int addressA = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionB.maxBin() );
            int addressB = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressC = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressD = (-1 >>> leading0s) << shift;

            return of( addressA, nameA, bijectionA,
                    addressB, nameB, bijectionB,
                    addressC, nameC, bijectionC,
                    addressD, nameD, bijectionD);
        }

        public static <A,B,C,D> Field4<A,B,C,D> of(int addressA, String nameA, Bijection<A>bijectionA,
                                                int addressB, String nameB, Bijection<B>bijectionB,
                                                int addressC, String nameC, Bijection<C>bijectionC,
                                                int addressD, String nameD, Bijection<D>bijectionD){
            return new Field4<>(
                    new BinStore<>(addressA, nameA, bijectionA),
                    new BinStore<>(addressB, nameB, bijectionB),
                    new BinStore<>(addressC, nameC, bijectionC),
                    new BinStore<>(addressD, nameD, bijectionD)
            );
        }

        public final BinStore<A> binStoreA;
        public final BinStore<B> binStoreB;
        public final BinStore<C> binStoreC;
        public final BinStore<D> binStoreD;

        public Field4(BinStore<A> binStoreA, BinStore<B> binStoreB, BinStore<C> binStoreC, BinStore<D> binStoreD){
            this.binStoreA = binStoreA;
            this.binStoreB = binStoreB;
            this.binStoreC = binStoreC;
            this.binStoreD = binStoreD;
        }

        public int store(A aValue, B bValue, C cValue, D dValue){
            return binStoreA.update(aValue, 0) | binStoreB.update(bValue, 0) | binStoreC.update(cValue, 0) | binStoreD.update(dValue, 0);
        }

        public Object[] load(int word){
            return new Object[]{ binStoreA.load(word), binStoreB.load(word), binStoreC.load(word), binStoreD.load(word)};
        }

        public A loadA(int word){
            return binStoreA.load(word);
        }

        public B loadB(int word){
            return binStoreB.load(word);
        }

        public C loadC(int word){
            return binStoreC.load(word);
        }

        public D loadD(int word){
            return binStoreD.load(word);
        }

        public int setA(A aValue){ return binStoreA.set(aValue); }
        public int setB(B bValue){ return binStoreB.set(bValue); }
        public int setC(C cValue){ return binStoreC.set(cValue); }
        public int setD(D dValue){ return binStoreD.set(dValue); }

        public int updateA(int word, A aValue){
            return binStoreA.update(aValue, word);
        }

        public int updateB(int word, B bValue){
            return binStoreB.update(bValue, word);
        }

        public int updateC(int word, C cValue){
            return binStoreC.update(cValue, word);
        }

        public int updateD(int word, D dValue){
            return binStoreD.update(dValue, word);
        }

        public String toString(){
            return binStoreA.toString()+System.lineSeparator()
                    +binStoreB.toString()+System.lineSeparator()
                    +binStoreC.toString()+System.lineSeparator()
                    +binStoreD.toString();
        }
    }

    /**
     * Packs / stores (5 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     * @param <C> the type of the 3rd value being stored
     * @param <D> the type of the 4th value being stored
     * @param <E> the type of the 5th value being stored
     */
    class Field5<A,B,C,D,E> {

        public static <A,B,C,D,E> Field5<A,B,C,D,E> of(String nameA, Bijection<A> bijectionA,
                                                       String nameB, Bijection<B> bijectionB,
                                                       String nameC, Bijection<C> bijectionC,
                                                       String nameD, Bijection<D> bijectionD,
                                                       String nameE, Bijection<E> bijectionE){
            int shift = 0;

            int leading0s = Integer.numberOfLeadingZeros(bijectionA.maxBin() );
            int addressA = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionB.maxBin() );
            int addressB = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressC = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressD = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressE = (-1 >>> leading0s) << shift;

            return of( addressA, nameA, bijectionA,
                    addressB, nameB, bijectionB,
                    addressC, nameC, bijectionC,
                    addressD, nameD, bijectionD,
                    addressE, nameE, bijectionE);
        }

        public static <A,B,C,D,E> Field5<A,B,C,D,E> of(int addressA, String nameA, Bijection<A>bijectionA,
                                                           int addressB, String nameB, Bijection<B>bijectionB,
                                                           int addressC, String nameC, Bijection<C>bijectionC,
                                                           int addressD, String nameD, Bijection<D>bijectionD,
                                                           int addressE, String nameE, Bijection<E>bijectionE){
            return new Field5<>(
                    new BinStore<>(addressA, nameA, bijectionA),
                    new BinStore<>(addressB, nameB, bijectionB),
                    new BinStore<>(addressC, nameC, bijectionC),
                    new BinStore<>(addressD, nameD, bijectionD),
                    new BinStore<>(addressE, nameE, bijectionE)
            );
        }

        public final BinStore<A> binStoreA;
        public final BinStore<B> binStoreB;
        public final BinStore<C> binStoreC;
        public final BinStore<D> binStoreD;
        public final BinStore<E> binStoreE;

        public Field5(BinStore<A> binStoreA, BinStore<B> binStoreB, BinStore<C> binStoreC, BinStore<D> binStoreD, BinStore<E> binStoreE){
            this.binStoreA = binStoreA;
            this.binStoreB = binStoreB;
            this.binStoreC = binStoreC;
            this.binStoreD = binStoreD;
            this.binStoreE = binStoreE;
        }

        public int store(A aValue, B bValue, C cValue, D dValue, E eValue){
            return binStoreA.update(aValue, 0)
                    | binStoreB.update(bValue, 0)
                    | binStoreC.update(cValue, 0)
                    | binStoreD.update(dValue, 0)
                    | binStoreE.update(eValue, 0);
        }

        public Object[] load(int word){
            return new Object[]{ binStoreA.load(word), binStoreB.load(word), binStoreC.load(word), binStoreD.load(word), binStoreE.load(word)};
        }

        public A loadA(int word){
            return binStoreA.load(word);
        }

        public B loadB(int word){
            return binStoreB.load(word);
        }

        public C loadC(int word){
            return binStoreC.load(word);
        }

        public D loadD(int word){
            return binStoreD.load(word);
        }
        public E loadE(int word){
            return binStoreE.load(word);
        }


        public int setA(A aValue){ return binStoreA.set(aValue); }
        public int setB(B bValue){ return binStoreB.set(bValue); }
        public int setC(C cValue){ return binStoreC.set(cValue); }
        public int setD(D dValue){ return binStoreD.set(dValue); }
        public int setE(E eValue){ return binStoreE.set(eValue); }

        public int updateA(int word, A aValue){
            return binStoreA.update(aValue, word);
        }

        public int updateB(int word, B bValue){
            return binStoreB.update(bValue, word);
        }

        public int updateC(int word, C cValue){
            return binStoreC.update(cValue, word);
        }

        public int updateD(int word, D dValue){
            return binStoreD.update(dValue, word);
        }

        public int updateE(int word, E eValue){
            return binStoreE.update(eValue, word);
        }

        public String toString(){
            return binStoreA.toString()+System.lineSeparator()
                    +binStoreB.toString()+System.lineSeparator()
                    +binStoreC.toString()+System.lineSeparator()
                    +binStoreD.toString()+System.lineSeparator()
                    +binStoreE.toString();
        }
    }

    /**
     * Packs / stores (6 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     * @param <C> the type of the 3rd value being stored
     * @param <D> the type of the 4th value being stored
     * @param <E> the type of the 5th value being stored
     * @param <F> the type of the 6th value being stored
     */
    class Field6<A,B,C,D,E,F> {

        public static <A,B,C,D,E,F> Field6<A,B,C,D,E,F> of(String nameA, Bijection<A> bijectionA,
                                                   String nameB, Bijection<B> bijectionB,
                                                   String nameC, Bijection<C> bijectionC,
                                                   String nameD, Bijection<D> bijectionD,
                                                   String nameE, Bijection<E> bijectionE,
                                                   String nameF, Bijection<F> bijectionF){
            int shift = 0;

            int leading0s = Integer.numberOfLeadingZeros(bijectionA.maxBin() );
            int addressA = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionB.maxBin() );
            int addressB = (-1 >>> leading0s) << shift;
            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressC = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressD = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressE = (-1 >>> leading0s) << shift;

            shift+= (32 - leading0s);
            //System.out.println( "SHIFT "+shift);
            leading0s = Integer.numberOfLeadingZeros(bijectionC.maxBin() );
            int addressF = (-1 >>> leading0s) << shift;

            return of( addressA, nameA, bijectionA,
                    addressB, nameB, bijectionB,
                    addressC, nameC, bijectionC,
                    addressD, nameD, bijectionD,
                    addressE, nameE, bijectionE,
                    addressF, nameF, bijectionF);
        }

        public static <A,B,C,D,E,F> Field6<A,B,C,D,E,F> of(int addressA, String nameA, Bijection<A>bijectionA,
                                                   int addressB, String nameB, Bijection<B>bijectionB,
                                                   int addressC, String nameC, Bijection<C>bijectionC,
                                                   int addressD, String nameD, Bijection<D>bijectionD,
                                                   int addressE, String nameE, Bijection<E>bijectionE,
                                                   int addressF, String nameF, Bijection<F>bijectionF){
            return new Field6<>(
                    new BinStore<>(addressA, nameA, bijectionA),
                    new BinStore<>(addressB, nameB, bijectionB),
                    new BinStore<>(addressC, nameC, bijectionC),
                    new BinStore<>(addressD, nameD, bijectionD),
                    new BinStore<>(addressE, nameE, bijectionE),
                    new BinStore<>(addressF, nameF, bijectionF)
            );
        }

        public final BinStore<A> binStoreA;
        public final BinStore<B> binStoreB;
        public final BinStore<C> binStoreC;
        public final BinStore<D> binStoreD;
        public final BinStore<E> binStoreE;
        public final BinStore<F> binStoreF;

        public Field6(BinStore<A> binStoreA, BinStore<B> binStoreB, BinStore<C> binStoreC, BinStore<D> binStoreD, BinStore<E> binStoreE, BinStore<F> binStoreF){
            this.binStoreA = binStoreA;
            this.binStoreB = binStoreB;
            this.binStoreC = binStoreC;
            this.binStoreD = binStoreD;
            this.binStoreE = binStoreE;
            this.binStoreF = binStoreF;
        }

        public int store(A aValue, B bValue, C cValue, D dValue, E eValue, F fValue){
            return binStoreA.update(aValue, 0)
                    | binStoreB.update(bValue, 0)
                    | binStoreC.update(cValue, 0)
                    | binStoreD.update(dValue, 0)
                    | binStoreE.update(eValue, 0)
                    | binStoreF.update(fValue, 0);
        }

        public Object[] load(int word){
            return new Object[]{ binStoreA.load(word), binStoreB.load(word), binStoreC.load(word), binStoreD.load(word), binStoreE.load(word), binStoreF.load(word)};
        }

        public A loadA(int word){
            return binStoreA.load(word);
        }

        public B loadB(int word){
            return binStoreB.load(word);
        }

        public C loadC(int word){
            return binStoreC.load(word);
        }

        public D loadD(int word){
            return binStoreD.load(word);
        }
        public E loadE(int word){
            return binStoreE.load(word);
        }
        public F loadF(int word){
            return binStoreF.load(word);
        }


        public int setA(A aValue){ return binStoreA.set(aValue); }
        public int setB(B bValue){ return binStoreB.set(bValue); }
        public int setC(C cValue){ return binStoreC.set(cValue); }
        public int setD(D dValue){ return binStoreD.set(dValue); }
        public int setE(E eValue){ return binStoreE.set(eValue); }
        public int setF(F fValue){ return binStoreF.set(fValue); }

        public int updateA(int word, A aValue){
            return binStoreA.update(aValue, word);
        }

        public int updateB(int word, B bValue){
            return binStoreB.update(bValue, word);
        }

        public int updateC(int word, C cValue){
            return binStoreC.update(cValue, word);
        }

        public int updateD(int word, D dValue){
            return binStoreD.update(dValue, word);
        }

        public int updateE(int word, E eValue){
            return binStoreE.update(eValue, word);
        }

        public int updateF(int word, F fValue){
            return binStoreF.update(fValue, word);
        }

        public String toString(){
            return binStoreA.toString()+System.lineSeparator()
                    +binStoreB.toString()+System.lineSeparator()
                    +binStoreC.toString()+System.lineSeparator()
                    +binStoreD.toString()+System.lineSeparator()
                    +binStoreE.toString()+System.lineSeparator()
                    +binStoreF.toString();
        }
    }

    /**
     * Contiguous Bit pattern within a 32-bit word that represents a specific value
     * (contains a shift (a number between 0 and 31) to represent where the series of bits are within the 32-bit word
     *
     * @see Bin32 a series of bits within a 32-bit word (an "address" of bits within the word bits at [0...31])
     */
    interface BitPattern<V> {

        /** gets the value associated with the BitPattern */
        V getValue();

        /** gets the Bin32 representing the bits reserved within a 32-bit word for the pattern */
        Bin32 getBin32();

        /**
         * The bit pattern for representing a value (0-based)
         * @see #getShiftedBitPattern() for the bin shifted into the frame
         */
        int getBitPattern();

        /**
         * The shifted bit pattern that is shifted to represent the value within a 32-bit word
         * @return
         */
        default int getShiftedBitPattern(){
            return getBitPattern() << getShift();
        }

        /**
         * the number of bit positions (from the low order bits) to skip to get to the first bit in the Bin bit pattern
         * (<<)
         * @return
         */
        default int getShift(){
            return getBin32().getShift();
        }

        /**
         * a 32-bit bitmask to represent the bits that are in the values Bin bitPattern
         * @return
         */
        default int getMask(){
            return getBin32().getMask();
        }

        /**
         * The mask shifted to represent the relevant bits in the BitPattern
         * @return
         */
        default int getShiftedMask(){
            return getMask() << getShift();
        }

        /**
         * # of bits used to represent the bitPattern
         * @return
         */
        default int getBitCount(){
            return Integer.bitCount( getMask() );
        }

        /**
         *
         * @param encoded
         * @return
         */
        default boolean matches( int encoded ){
            return ( encoded & getShiftedMask() ) == getShiftedBitPattern();
        }

        default String describe(){
            return Bitwise.bits(getShiftedBitPattern());
        }
    }
}