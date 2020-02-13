package test.othertools;

import junit.framework.TestCase;
import org.jdraft.pattern.*;

/**
 * https://github.com/google/error-prone/wiki/Writing-a-check#avoiding-return-null-statements
 */
public class GoogleErrorProneTestAvoidReturnNullTest extends TestCase {

    //we just need this for test purposes
    @interface Nullable{}

    public void testFindMethodsWithReturnNull(){

        //this is the pattern to search for
        // specifically methods with return null and NO Nullable annotation
        $method $m = $method.of( $.stmt("return null;") ).$not( Nullable.class);

        //here's a test
        class F{
            Object m(){
                return null;
            }
            @Nullable Object t(){
                return null;
            }
        }

        //verify we find one
        assertEquals(1, $m.countIn(F.class));
        assertTrue( $m.firstIn(F.class).getName().equals("m")); //verify it is matching against method m
    }
}
