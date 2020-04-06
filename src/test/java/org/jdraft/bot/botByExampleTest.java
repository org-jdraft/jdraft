package org.jdraft.bot;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.pattern.$body;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * How to build a bot:
 *    individual bot (of)
 *    nested bot
 *
 * What bots do:
 *    matches
 *    select
 *
 *    draft(*)
 *
 *    bot working with another bot
 *
 * Updating bots
 *    $
 *    $hardcode
 *    bot copy
 *
 */
public class botByExampleTest extends TestCase {

    /**
     * Each bot instance has the ability to match against the many forms
     * (String, Node, _java._domain, Class, etc.) representing its specific type:
     *
     * $method bots work with {@link _method}, {@link MethodDeclaration} & Strings representing methods
     * $int bots work with {@link _int}, {@link IntegerLiteralExpr}, int, & Strings representing ints
     * (individual bots are specific, and configured to ONLY work with a specific type or category of types
     * of $expression, $statement node or nodes)
     *
     * This leads to the bots having a VERY SPECIFIC API (which is important!) for what it can do
     * (inspect, build & transform)
     */
    public void testMatch() {

        //$typeRef $hashSet = $typeRef.of(HashSet.class);
        $typeRef $hashSet = $typeRef.of(HashSet.class);

        //matches the String form of "HashSet"
        assertTrue($hashSet.matches("HashSet"));

        //matches the String (fully qualified) "HashSet"
        assertTrue($hashSet.matches("java.util.HashSet"));
        assertFalse($hashSet.matches("java.util.LinkedHashSet")); //not a HashSet

        //matches "HashSet" that is in the (JavaParser) Type form
        Type astType = Types.of(HashSet.class);
        assertTrue($hashSet.matches(astType));

        //matches "HashSet" that is in the (jdraft) _typeRef form
        _typeRef _t = _typeRef.of(HashSet.class);
        assertTrue($hashSet.matches(_t));

        assertFalse($hashSet.matches("LinkedHashSet")); //not a HashSet

        // bots can match specific entities
        assertTrue($hashSet.matches(HashSet.class));
        assertFalse($hashSet.matches(LinkedHashSet.class)); //not a HashSet
    }

    /**
     * the select() method is a matching function which also extracts $tokens$
     * from the bots internal stencil (a pattern). select either returns a Select<?>
     * object or null if the
     *
     * the select() method and "Selects" specific Tokens form matching AST structures
     */
    public void testSelect(){

        /* $bot matches any System.out.println(...) method call, where "any" is the name of the parameter*/

        $statement $printAny = $statement.of("System.out.println($any$);");

        assertTrue($printAny.matches("System.out.println();")); //$any$ matches empty token
        assertTrue($printAny.matches("System.out.println(1);")); //$any$ matches a single token
        assertTrue($printAny.matches("System.out.println(a +'c'+ 11);")); //$any$ matches multiple things

        //the select() can match & "extract" tokens in a single call to .select() returning a Select
        assertNotNull( $printAny.select("System.out.println(1);") ); //if the
        assertNull( $printAny.select("notMatched();") ); //select returns null if the pattern doesn't match

        //sel returns the selected entity (a _statement) and the extracted values for $tokens$
        Select<_statement> sel = $printAny.select("System.out.println(1);");

        //sel contains the _statement that was selected
        assertEquals( sel.get(), _statement.of("System.out.println(1);"));

        //sel has a Tokens (map) we can extract the "any" parameter value with .get(...)
        assertEquals("1", sel.get("any"));

        //we can ask if the selected tokens contains the expected value with .is(...)
        assertTrue(sel.is("any", "1"));

        //the stencil/pattern can have multiple parameters(name, val) & reuse parameters (name)
        $statement $s = $statement.of( (String $name$, String $val$)->System.out.println("$name$ "+$name$+" is "+$val$));

        _statement _s = _statement.of((String a, String v)->System.out.println("a "+a+" is "+v));

        //we can match against this statement
        assertTrue( $s.matches(_s));

        //select & verify the parameter values selected (name = a, val = v)
        assertTrue( $s.select(_s).is("name", "a", "val", "v") );
    }

    /**
     * bots are mutable after initialization
     */
    public void testBotMutate(){

    }

    /**
     * more complex $bots can be nested within other $bots
     * this is how we build
     *
     * also $bots are mutable
     */
    public void testEmbedded$Bots(){
        //matches empty arguments list
        $arguments $noArgs = $arguments.empty();
        assertTrue( $noArgs.matches("()"));

        //the $mc matches method calls that have no arguments
        $methodCall $mc = $methodCall.of( $noArgs );
        class C{
            void m(){
                System.out.println(); //no arguments call
                System.out.println("Arg");
                System.currentTimeMillis(); //no arguments call
                UUID.randomUUID(); //no arguments call
                m();//no arguments call
            }
        }
        //there are (4) no-arg method calls
        assertEquals(4, $mc.countIn(C.class));

        //here we access the nested $bot ($emptyArgs) and test
        assertTrue($mc.get$arguments().matches("()"));

        //the $mc $bot can be thought of as a parent $bot which uses nested $bots
        //you can get the underlying $bots with get$XXX()
        //by default, the nested $bots are "matchAny()" meaning they will match any candidate (or null)
        assertTrue( $mc.get$name().isMatchAny() );
        assertTrue( $mc.get$scope().isMatchAny() );
        assertTrue( $mc.get$typeArguments().isMatchAny() );

        //even though we didnt specify the $name bot on $mc it exists (matches any name)
        assertTrue($mc.get$name().matches("any"));

        //create a new bot that matches the name "m"
        $name $n = $name.of("m");

        //verify we can find the "m" name for the method "void m()" methodCall "m()"
        assertEquals(2, $n.countIn(C.class));

        //update the nested $name $bot on the $mc $bot
        $mc.$name($n);

        //now the $mc $bot only matches 1 (method call with name "m" & no arguments)
        assertEquals(1, $mc.countIn(C.class));

        //A real world example:
        //a $bot matching any method with a body containing a methodCall named "fromXML"
        //$method.of($body.of($methodCall.of("fromXML")));
    }

    /**
     * $bot.draft() is like calling a "Factory method" to build a new jdraft instance of an entity
     *
     */
    public void testDraftBot(){
        //$bot representing a very specific method call
        $methodCall $println = $methodCall.of("System.out.println(1)");

        //$bot will draft (build and return a new instance) of this methodCall
        _methodCall _mc = $println.draft();
        assertEquals( _methodCall.of( ()->System.out.println(1) ), _mc);

        //$println = $methodCall.of( (Object $any$)-> System.out.println($any$) );

    }

    public void testAsBot(){
        //exact matching
    }
    public void testOrBot(){

    }

    public void testBotCopy(){
        $name $startsA = $name.of().$and(n-> n.toString().startsWith("a") );
        assertTrue($startsA.matches("a"));
        assertTrue($startsA.matches("abbbbz"));
        assertTrue($startsA.matches("abbbt"));

        //build a new $name
        //based on $startsA ..... with.copy()
        $name $startsAEndsZ = $startsA.copy();
        //here modiftying $startsAEndsZ doesnt update $startsA
        $startsAEndsZ.$and(n-> n.toString().endsWith("z"));

        assertFalse($startsAEndsZ.matches("a")); //doesnt match (end with z)
        assertTrue($startsAEndsZ.matches("az")); //matches
        assertTrue($startsAEndsZ.matches("abbbbz")); //matches

        assertTrue($startsA.matches("a"));      // $startsA still matches "a"
    }

    public void testBotsWorkingTogether(){
        $typeRef $hashSet = $typeRef.  of(HashSet.class);

        // bots are good at pattern matching... given a String, determine if the String matches
        /* $bot specifically for references to LinkedHashSet */
        $typeRef $linkedHashSet = $typeRef.of(LinkedHashSet.class);

        /* $bot specifically for method calls to Collectors.toSet() */
        $methodCall $collectToSet = $methodCall.of("Collectors.toSet()");

        /* $bot specifically for method calls to Collectors.(...) creating a new LinkedHashSet */
        $methodCall $collectToLHSet = $methodCall.of("Collectors.toCollection(LinkedHashSet::new)");


        //modifying the search

        // each $bot can act independently,
        $hashSet.$isParent(_import.class);
        // the bot can knows how to individually walking any AST model and "match" for relevant patterns
    }
}
