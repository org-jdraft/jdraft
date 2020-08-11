package org.parseout.binary;

/**
 * bit-wise address for a series of bits within a 32-bit word (int)
 *
 * <PRE>
 * ----------------------------------------------------------------  64-bit word (int)
 * 3210987654321098765432109876543210987654321098765432109876543210  | bit address [0...63]
 * 666655555555554444444444333333333322222222221111111111            |
 *
 * -----------------------------------------------------________---  8-bit Bin64 (address [3...10])
 *                                                      09876543     | Bin address [3...10]
 *                                                      1            |
 *
 * -----------------------------------------------------________---  8-bit Bin (address [3...10] within the 32-bit word)
 * 0000000000000000000000000000000000000000000000000000011111111000  | shiftedMask (1's at address range [3...10])
 *
 *
 * ...For example: if we want to extract the Bin bit pattern from a 64-bit word:
 *
 * 0000000000000000000000000000000001001010100111101010110010010100  64-bit int
 * 0000000000000000000000000000000000000000000000000000011111111000  shiftedMask (1's at address range [3...10])
 * &----------------------------------------------------________---  AND (64-bit int & shiftedMask)
 * 0000000000000000000000000000000000000000000000000000010010010000  64-bit Bin Pattern
 * >>3_____________________________________________________________  shifted >> 3
 * 0000000000000000000000000000000000000000000000000000000010010010  64-bit Bin Pattern (normalized)
 *                                                         10010010  8-bit Bin Pattern (relevant bits)
 * </PRE>
 */
public interface Bin64 {

    /** Returns a BitAddress32 implementation of the Bin32 based on the mask and shift*/
    static BitAddress64 of( long mask, int shift ){
        return new BitAddress64(mask, shift);
    }

    /** Mask for the sequence of bits from within the word */
    long getMask();

    /** the left shift applied to the mask to align with the bin (where within the word the bin is) */
    int getShift();

    /** number of bits in the address (i.e. 1 to 32 for 32 bit ints) */
    default int getBitCount() {
        return Long.bitCount( getMask());
    }

    /** Shifted mask used for identifying the bits within address of the word*/
    default long getShiftedMask(){
        return getMask() << getShift();
    }

    /** Extracts & returns the bit pattern from within 32-bit at the Bin address */
    default long read(long word){
        return (word & getShiftedMask()) >>> getShift();
    }

    /** return a 32-bit word with the the bits at the Bin32 address cleared (to 0's)*/
    default long clear(long word){
        return (word & (~getShiftedMask()));
    }

    /** returns a new 32-bit word where the address bits within the word are updated with the new bit pattern*/
    default long update(long word, long bitPattern){
        return (word & (~getShiftedMask())) | ( (bitPattern << getShift()) & getShiftedMask());
    }

    /** returns the bitPattern that exists within the Bin address */
    default long getBitPattern(long word){
        return (word & getShiftedMask()) >>> getShift();
    }

    /** does this word have ANY 1 bits within the bin set? */
    default boolean hit(long word){
        return (word & getShiftedMask()) != 0;
    }

    /**
     * Does the bitpattern within the Bin32 address match the bitPattern provided?
     * @param word the 64-bit word
     * @param bitPattern
     * @return
     */
    default boolean matches(long word, long bitPattern){
        return (word & (getShiftedMask())) == ( (bitPattern << getShift()));
    }

    static String DASHES = "----------------------------------------------------------------";

    default String describe(){
        String s = DASHES.substring( 64 - getShift());
        s = Long.toBinaryString(getMask()).replace("1", "_") + s;
        s = DASHES.substring(s.length()) + s;

        if( getBitCount() == 1 ){
            return s + "  ["+getShift()+"] 1-bit";
        }
        return s + "  ["+getShift()+"..."+(getShift()+getBitCount()-1)+"] "+getBitCount()+"-bits";
    }

    /** Simple Bin64 implementation of an address of consecutive bits within a 64-bit word*/
    class BitAddress64 implements Bin64 {

        public final long mask;
        public final int shift;

        public BitAddress64(long mask, int shift){
            this.mask = mask;
            this.shift = shift;
            if( shift < 0 || shift > 63){
                throw new RuntimeException("Invalid shift, must be >=0 and <=63");
            }
            int bitCount = Long.bitCount( mask );
            int runCount = maxConsecutiveOnes(mask);
            if( bitCount != runCount ){
                throw new RuntimeException("Mask 0b"+ Bitwise.bits(mask)+" ...must have CONSECUTIVE 1 bits");
            }
            if( Long.bitCount( this.mask << this.shift) != bitCount ){
                throw new RuntimeException("Mask 0b"+ Bitwise.bits(mask)+" shifted << "+shift+" is "+ Bitwise.bits(mask << shift)+" loss of mask bits detected" );
            }
        }

        @Override
        public long getMask() {
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
        private static int maxConsecutiveOnes(long bitPattern) {
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
     * Contiguous Bit pattern within a 64-bit word that represents a specific value
     * (contains a shift (a number between 0 and 63) to represent where the series of bits are within the 63-bit word
     *
     * @see Bin64 a series of bits within a 64-bit word (an "address" of bits within the word bits at [0...63])
     */
    interface BitPattern<V> {

        /** gets the value associated with the BitPattern */
        V getValue();

        /** gets the Bin64 representing the bits reserved within a 64-bit word for the pattern */
        Bin64 getBin64();

        /**
         * The bit pattern for representing a value (0-based)
         * @see #getShiftedBitPattern() for the bin shifted into the frame
         */
        long getBitPattern();

        /**
         * The shifted bit pattern that is shifted to represent the value within a 32-bit word
         * @return
         */
        default long getShiftedBitPattern(){
            return getBitPattern() << getShift();
        }

        /**
         * the number of bit positions (from the low order bits) to skip to get to the first bit in the Bin bit pattern
         * (<<)
         * @return
         */
        default int getShift(){
            return getBin64().getShift();
        }

        /**
         * a 32-bit bitmask to represent the bits that are in the values Bin bitPattern
         * @return
         */
        default long getMask(){
            return getBin64().getMask();
        }

        /**
         * The mask shifted to represent the relevant bits in the BitPattern
         * @return
         */
        default long getShiftedMask(){
            return getMask() << getShift();
        }

        /**
         * # of bits used to represent the bitPattern
         * @return
         */
        default int getBitCount(){
            return Long.bitCount( getMask() );
        }

        /**
         *
         * @param encoded
         * @return
         */
        default boolean matches( long encoded ){
            return ( encoded & getShiftedMask() ) == getShiftedBitPattern();
        }

        default String describe(){
            return Bitwise.bits(getShiftedBitPattern());
        }
    }

}