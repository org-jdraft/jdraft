package test.othertools;

import junit.framework.TestCase;

/**
 * Emulate what is going on in AutoValue with jdraft
 * https://github.com/google/auto/blob/master/value/userguide/index.md#in-your-value-class
 */
public class GoogleAutoValueTest extends TestCase {

    @interface AutoValue{
        public static class To{

        }
    }

    @AutoValue
    static abstract class Animal {
        static Animal create(String name, int numberOfLegs) {
            return (Animal)null;
        }

        abstract String name();
        abstract int numberOfLegs();
    }

    public void testAutoValue(){

    }
}
