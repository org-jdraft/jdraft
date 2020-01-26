package lut;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class LutBuilderTest extends TestCase {

    /**
     * This is an example Lut interface,
     * MUST have the "lookup" method and accept a single "key" parameter and return a value
     * (the types for key and value are: String, int, long, float, double, & wrappers (Integer, ...))
     */
    public interface Lut{
        int lookup(String key);
    }

    /** this will be created on static init, we could also populate this dynamically, whatevs */
    public static final Lut LUT;

    static{
        Map<String,Integer> nameToAge = new HashMap<>();
        nameToAge.put("Able", 1);
        nameToAge.put("Ben", 2);
        nameToAge.put("Charles", 3);
        nameToAge.put("Dawn", 4);

        LUT = (Lut)LutBuilder.runtimeLut("NameToAge", Lut.class, nameToAge);
    }

    public void testN(){
        assertEquals(1, LUT.lookup("Able"));
    }
}
