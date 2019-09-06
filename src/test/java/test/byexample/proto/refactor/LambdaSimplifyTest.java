package test.byexample.proto.refactor;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.proto.$$;
import org.jdraft.proto.$ex;

import java.util.ArrayList;
import java.util.List;

/**
 * I noticed when looking at recommendations from IntelliJ it will try to recommend
 * simplifying the lambda pattern
 * <PRE>stream().filter(a -> a.isEmpty()).findFirst().isPresent()</PRE>
 * with
 * <PRE>stream().anyMatch(a -> a.isEmpty())</PRE>
 * ...so I thought it'd be nice to figure out how to do this in jdraft
 * (I think this took <about> 3 mins to write and test)
 */
public class LambdaSimplifyTest extends TestCase {

    public void testMatch(){

        $ex $filterFindFirstIsPresent = $$.expr("$any$.stream().filter($match$).findFirst().isPresent()");
        $ex $anyMatch = $$.expr("$any$.stream().anyMatch($match$)");

        class EX{
            String aName;
            public boolean testGG(){
                List<String> ls = new ArrayList<>();

                //example code that uses the filter().findFirst().isPresent() pattern
                return ls.stream().filter(
                        c -> c.equals( aName )).findFirst().isPresent();
            }
        }
        _class _c = _class.of( EX.class);
        //verify I can find the pattern in the code
        assertEquals(1, $filterFindFirstIsPresent.count(_c));

        //refactor using replace with the $anyMatch pattern
        _c = (_class)$filterFindFirstIsPresent.replaceIn(_c, $anyMatch);

        //after refactoring, make sure the pattern doesn't occur
        assertEquals( 0,  $filterFindFirstIsPresent.count(_c));

        //make sure the new $anyMatch pattern occurs in the refactored code
        assertEquals( 1,  $anyMatch.count(_c));
    }
}
