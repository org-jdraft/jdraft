package org.parseout.binary;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
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
    static BinAddress32 of(int mask, int shift ){
        return new BinAddress32(mask, shift);
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
    class BinAddress32 implements Bin32 {

        public static BinAddress32 of(int shiftedMask){
            return new BinAddress32(shiftedMask);
        }

        public final int mask;
        public final int shift;

        /**
         * Simplified constructor, generally good if we use 0b (binary notation) to show the bits of a BitAddress32
         * especially for explaining the
         * <PRE>
         * BinAddress32 firstByte  = new BinAddress32(0b11111111000000000000000000000000);
         * BinAddress32 secondByte = new BinAddress32(0b00000000111111110000000000000000);
         * BinAddress32 thirdByte  = new BinAddress32(0b00000000000000001111111100000000);
         * BinAddress32 fourthByte = new BinAddress32(0b00000000000000000000000011111111);
         * </PRE>
         * @param shiftedMask
         */
        public BinAddress32(int shiftedMask){
            this ( shiftedMask >> Integer.numberOfTrailingZeros(shiftedMask), Integer.numberOfTrailingZeros(shiftedMask));
        }

        public BinAddress32(int mask, int shift){
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
    }

    /**
     * Converts from an int to a value or a value form an int
     * for example here is a simple Bijection
     * 1  <-> "A"
     * 2  <-> "B"
     * 3  <-> "C"
     * 4  <-> "D"
     * @param <T>
     */
    interface Bijection<T> extends IntFunction<T>, Function<T, Integer> {
    }

    Bijection<Boolean> BOOL = new Bijection<Boolean>(){

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
     * Bijection of a code table
     */
    static class CodeTable implements Bijection<String>{

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

        @Override
        public Integer apply(String s) {
            return codeToBin.get(s);
        }

        @Override
        public String apply(int value) {
            return codes[value];
        }
    }

    class BinStore32<T> {
        public final String name;
        public final BinAddress32 address;
        public final Bijection<T> bijection;

        public BinStore32(int mask, String name, Bijection<T> bijection){
            this.address = BinAddress32.of(mask);
            this.name = name;
            this.bijection = bijection;
        }

        public T load(int word){
            return bijection.apply(address.read(word));
        }

        public int update( T value, int word){
            return address.update(word, bijection.apply(value) );
        }
    }

    /**
     * Packs / stores (2 distinct values within a 32-bit word)
     * @param <A> the type of the first value being stored
     * @param <B> the type of the second value being stored
     */
    class Field2<A,B> {

        public static <A, B> Field2<A,B> of(int addressA, String nameA, Bijection<A>bijectionA,
                                            int addressB, String nameB, Bijection<B>bijectionB){
            return new Field2<>(
                    new BinStore32<>(addressA, nameA, bijectionA),
                    new BinStore32<>(addressB, nameB, bijectionB));
        }

        public BinStore32<A> binStoreA;
        public BinStore32<B> binStoreB;

        public Field2(BinStore32<A>binStoreA, BinStore32<B> binStoreB){
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