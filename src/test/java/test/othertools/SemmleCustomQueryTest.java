package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.bot.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the original explanation about using a Semmle Query to do this job
 * https://www.youtube.com/watch?v=xg5LLFrRpRY&feature=youtu.be
 *
 * below is the abbreviated explanation for how to do this in jdraft
 */
public class SemmleCustomQueryTest extends TestCase {

    public void testChangeHashSetToLinkedHashSet(){
        /** This is just an example we use for illustration*/
        class HashCollections{
            void uses(){
                Set<String> exampleSet;

                exampleSet = new HashSet<>(); //should be found (1+)

                exampleSet = new LinkedHashSet<>(); //this is our target (not found 1-)

                exampleSet = new ArrayList<String>().stream().collect(Collectors.toSet()); //should be found (2+)

                //a false positive for ".toSet(...)" verify we DONT match this (2-)
                exampleSet = new CoincidentallyNamed().toSet(new ArrayList<String>());
            }
            class CoincidentallyNamed{ //false positive for matching Collectors.toSet()
                public Set<String> toSet(List<String> list){
                    return null;
                }
            }
        }
        _class _c =_class.of(HashCollections.class);

        //1) replace the direct creation of HashSet to LinkedHashSet
        _c = $typeRef.of(HashSet.class).replaceIn(_c, LinkedHashSet.class);
        //2) replace calls of Collectors.toSet() with a custom toCollection( LinkedHashSet ) variant
        _c = $methodCallExpr.of("Collectors.toSet()").replaceIn(_c, "Collectors.toCollection(LinkedHashSet::new)");
        System.out.println( _c );
    }

    /**
     * Here is a more comprehensive (step by step) example of how we might have
     * developed and tested the bots
     */
    public void testStepByStepExplanation(){
        /** This is just an example we use for illustration*/
        class HashCollections{
            void uses(){
                Set<String> exampleSet;

                exampleSet = new HashSet<>(); //should be found (1+)

                exampleSet = new LinkedHashSet<>(); //this is our target (not found 1-)

                exampleSet = new ArrayList<String>().stream().collect(Collectors.toSet()); //should be found (2+)

                //a false positive for ".toSet(...)" verify we DONT match this (2-)
                exampleSet = new CoincidentallyNamed().toSet(new ArrayList<String>());
            }
            class CoincidentallyNamed{ //false positive for matching Collectors.toSet()
                public Set<String> toSet(List<String> list){
                    return null;
                }
            }
        }
        //create a model _c in place from the example code of HashCollections.class
        _class _c = _class.of(HashCollections.class);

        // (1) verify I can find the HashSet type in the code _c
        assertEquals(1, $typeRef.of(HashSet.class).countIn(_c));

        // (1) Replace the "bad" with "good" & return the modified code _c
        _c = $typeRef.of(HashSet.class).replaceIn(_c, LinkedHashSet.class);

        // (1) verify there are NO MORE HashSets in the code _c after we modified it
        assertEquals(0, $typeRef.of(HashSet.class).countIn(_c));

        // (1) just to be sure, verify we have exactly 1 replacement + the original LinkedHashSet in _c
        assertEquals(2, $typeRef.of(LinkedHashSet.class).countIn(_c));

        // (2) verify I can find the Collectors.toSet
        assertEquals( 1, $methodCallExpr.of("Collectors.toSet()").countIn(_c));

        // (2) verify I can replace all instances of Collectors.toSet() to the custom LinkedHashSet replacement
        _c = $methodCallExpr.of("Collectors.toSet()").replaceIn(_c, $methodCallExpr.of("Collectors.toCollection(LinkedHashSet::new)"));

        // (2) we should now have (3) instances of LinkedHashSet
        assertEquals(3, $typeRef.of(LinkedHashSet.class).countIn(_c));

        //print out the code after all modifications are made
        System.out.println( _c );
    }
}
