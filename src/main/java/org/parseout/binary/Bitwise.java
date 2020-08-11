package org.parseout.binary;

/**
 * Utilities for visualizing the binary content within 32-bit ints and 64-bit longs
 * (for succinct data structures or bitwise packed data)
 */
public class Bitwise {

    static final String _64BitLeadingZeros = "0000000000000000000000000000000000000000000000000000000000000000";

    static final String _32BitLeadingZeros = "00000000000000000000000000000000";

    /**
     * NORMALLY, when we convert a long from a number to a bitString, it returns the bitstring with
     * the HIGH ORDER BITS FIRST
     * <PRE>
     * 1L = "0000000000000000000000000000000000000000000000000000000000000001"
     * 2L = "0000000000000000000000000000000000000000000000000000000000000010"
     * 4L = "0000000000000000000000000000000000000000000000000000000000000100"
     * //    |                                                              |
     * //    HIGH BIT                                                 LOW BIT
     * </PRE>
     * SO, if we count from the lower order bits by index to the higher order bits, we would be counting RIGHT TO LEFT
     * i.e. the [0]th bit in the 64 bit string is:
     * <PRE>
     * 0000000000000000000000000000000000000000000000000000000000000001
     *                                                                |
     *                                                               [0]
     *
     *  the [1] indexed bit is:
     * 0000000000000000000000000000000000000000000000000000000000000010
     *                                                               |
     *                                                              [1]
     * </PRE>
     * 10         9         8        7       6      5     4    3    2 1 (consecutive bits)
     * 1111111111011111111101111111101111111011111101111101111011101101 (Example Long number)
     * ---------------------------------------------------------------- (64 bit indexes[0]-[63])
     * 6666555555555544444444443333333333222222222211111111119876543210 [index]
     * 321098765432109876543210987654321098765432109876543210
     * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< (indexes [0] - [63] go from RIGHT to LEFT)
     *
     * if, we want to view the indexes count from LEFT TO RIGHT, we have to reverse the order of the bits
     * 1 2  3   4    5     6      7       8        9         10         (consecutive bits)
     * 1011011101111011111011111101111111011111111011111111101111111111 (Long number with bits transposed)
     * ---------------------------------------------------------------- (64 bit indexes[0]-[63])
     * 0123456789111111111122222222223333333333444444444455555555556666
     *           012345678901234567890123456789012345678901234567890123
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> (indexes [0] - [63] go from LEFT to RIGHT)
     * </PRE>
     *
     * @param binary a long number (representing consecutive bits)
     * @return a 64 character String with (lowbits->highBits) left to right (Padded)
     */
    public static String bitsLtoR(long binary){
        String str = Long.toBinaryString( binary );
        StringBuilder sb = new StringBuilder();
        sb = sb.append(_64BitLeadingZeros.substring(str.length())).append(str).reverse();
        return sb.toString();
    }

    public static String bits(int bitCount, int binary){
        String str = Integer.toBinaryString( binary );
        for(int i=str.length(); i< bitCount;i++){
            str = "0"+str;
        }
        return str;
    }

    public static String bits(int binary){
        String str = Integer.toBinaryString( binary );
        return _32BitLeadingZeros.substring(str.length())+str;
    }

    public static String bits(long binary){
        String str = Long.toBinaryString( binary );
        return _64BitLeadingZeros.substring(str.length())+str;
    }

    public static void print(long binary){
        System.out.println(bits(binary));
    }

    public static void print(int binary){
        System.out.println(bits(binary));
    }
    public static void printLtoR(long binary){
        System.out.println(bitsLtoR(binary));
    }

    /**
     * NORMALLY, when we convert a long from a number to a bitString, it returns the bitstring with
     * the HIGH ORDER BITS FIRST
     * <PRE>
     * 1 = "00000000000000000000000000000001"
     * 2 = "00000000000000000000000000000010"
     * 4 = "00000000000000000000000000000100"
     * //   |                              |
     * //   HIGH BIT                       LOW BIT
     * </PRE>
     * SO, if we count from the lower order bits by index to the higher order bits, we would be counting RIGHT TO LEFT
     * i.e. the [0]th bit in the 64 bit string is:
     * <PRE>
     * 00000000000000000000000000000001
     *                                |
     *                               [0]
     *
     *  the [1] indexed bit is:
     * 00000000000000000000000000000010
     *                               |
     *                              [1]
     * </PRE>
     *       6      5     4    3    2 1 (consecutive bits)
     * 00000011111101111101111011101101 (int number)
     * -------------------------------- (32 bit indexes[0] - [31] )
     * 33222222222211111111119876543210 [index]
     * 1098765432109876543210
     * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< (indexes [0] - [31] go from RIGHT to LEFT)
     *
     * if, we want to view the indexes count from LEFT TO RIGHT, we have to reverse the order of the bits
     * 1 2  3   4    5     6
     * 10110111011110111110111111000000 (transposed int number )
     * -------------------------------- (32 bit indexes [0] - [31])
     * 01234567891111111111222222222233
     *           0123456789012345678901
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> (indexes [0] - [31] go from LEFT to RIGHT)
     * </PRE>
     *
     * @param binary a int number (representing consecutive bits)
     * @return a 32 character String with (lowbits->highBits) left to right (padded with 0s, always 32 chars in length)
     */
    public static String bitsLtoR(int binary){
        final String leadingZeros = "00000000000000000000000000000000";
        String str = Integer.toBinaryString( binary );
        StringBuilder sb = new StringBuilder();
        sb = sb.append(leadingZeros.substring(str.length())).append(str).reverse();
        return sb.toString();
    }

    public static String bits7(int binary){
        final String leadingZeros = "0000000";
        String str = Integer.toBinaryString( binary );
        return leadingZeros.substring(str.length()) + str;
    }

    public static String bits6(int binary){
        final String leadingZeros = "000000";
        String str = Integer.toBinaryString( binary );
        return leadingZeros.substring(str.length()) + str;
    }

    public static String bits5(int binary){
        final String leadingZeros = "00000";
        String str = Integer.toBinaryString( binary );
        return leadingZeros.substring(str.length()) + str;
    }
}
