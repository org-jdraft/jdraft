package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.*;
import static java.lang.System.out;
import org.jdraft._arguments;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class $methodCallTest extends TestCase {

    public void testMatchAny(){
        assertTrue($methodCall.of().isMatchAny());
        assertNotNull($methodCall.of().get$name());
        assertNotNull($methodCall.of().get$scope());
        assertNotNull($methodCall.of().get$arguments());
        assertNotNull($methodCall.of().get$typeArguments());
    }

    //$isInPackage(Predicate<_package> )
    //$isImports(Predicate<_imports> )
    //$isInType(Predicate<_type> )
    public void testOrOv(){

        $methodCall $or = $methodCall.or($methodCall.of("println"), $methodCall.of("print"))
                .$and( _mc-> _mc.isScope("System.out") || _mc.isScope("out") )
                .$and( _mc -> _mc.isArgument(0, e-> e instanceof _int));
        class c{
            void m(){
                System.out.print(1);
                System.out.println(1);
                out.print(2);
                out.println(2);
            }
        }
        assertEquals(4, $or.countIn(c.class));

        $methodCall $orCopy = $or.copy();

        assertEquals(4, $orCopy.countIn(c.class));
    }

    public void testOr(){
        //this matches
        $methodCall $print   = $methodCall.of( (Object $any$)-> System.out.print($any$));
        $methodCall $println = $methodCall.of( (Object $any$)-> System.out.println($any$));
        $methodCall $printOrPrintln = $methodCall.or($print, $println); //we want to match either of these
        class FF{
            void m(){
                System.out.println(1);
                System.out.print(2);
            }
        }
        assertEquals(2, $printOrPrintln.countIn(FF.class));
        //verify I can use
        assertTrue($printOrPrintln.listSelectedIn(FF.class).get(0).is("any", "1"));
        assertTrue($printOrPrintln.listSelectedIn(FF.class).get(1).is("any", "2"));

        //what about static imported System or System.out
        //                      here just match the name "print" (not the fully qualified Scope "System.out.println")
        $print = $methodCall.of("print", $arguments.of("$any$").$and(a-> ((_arguments)a).size() == 1));
        $println = $methodCall.of("println", $arguments.of("$any$").$and(a-> ((_arguments)a).size() == 1));

        $printOrPrintln = $methodCall.or($print, $println);
        class FFG{
            void m(){
                System.out.println(1); //regular System. "scope" match
                System.out.print("A"); //regular System. "scope" match
                out.println('c');      //
                out.print(UUID.randomUUID());
            }
        }
        assertEquals(4, $printOrPrintln.countIn(FFG.class));
        assertTrue($printOrPrintln.listSelectedIn(FFG.class).get(0).is("any", "1"));
        assertTrue($printOrPrintln.listSelectedIn(FFG.class).get(1).is("any", "\"A\""));
        assertTrue($printOrPrintln.listSelectedIn(FFG.class).get(2).is("any", "'c'"));
    }

    public static class PrintablePredicate<P extends Object> implements Predicate<P>{

        public static PrintablePredicate of(Predicate pp){
            _lambda _l = _lambda.from(Thread.currentThread().getStackTrace()[2]);
            return new PrintablePredicate(_l.toString(), pp);
        }

        String code;
        Predicate<P> predicate;

        public PrintablePredicate( String code, Predicate<P> predicate){
            this.code = code;
            this.predicate = predicate;
        }

        @Override
        public boolean test(P p) {
            return predicate.test(p);
        }

        public String toString(){
            return code;
        }
    }

    public void testOr$(){
        $methodCall $print = $methodCall.of("print");
        $methodCall $println = $methodCall.of("println");

        //build the $or match function (matches prints or printlns)
        $methodCall $or = $methodCall.or($print, $println);

        // add a predicate to the $or (which applies the constraint to ALL of
        // effectively applies this "constraint" to BOTH $print or $println
        $or.$and(m-> m.countArguments() == 1);

        assertFalse( $or.matches("print()") );
        assertTrue( $or.matches("print(1)") );
        assertFalse( $or.matches("print(1, 3)")); //not 1 argument

        assertFalse( $or.matches("println()") );
        assertTrue( $or.matches("println(1)") );
        assertFalse( $or.matches("println()")); //not 1 argument


    }

    public void testPredicate(){
        assertTrue( $methodCall.of().$and(m-> !m.hasArguments()).matches("m()") );
        assertFalse( $methodCall.of().$and(m-> !m.hasArguments()).matches("m(1)") );

        assertTrue( $methodCall.of().$and(m-> !m.hasTypeArguments()).matches("m()") );
        assertFalse( $methodCall.of().$and(m-> !m.hasTypeArguments()).matches("<T> m(1)") );
    }

    public void testStencilAny(){
        assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println()"));
        assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println(1)"));
        assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println(1,2)"));
    }

    public void testName(){

        //IF there is only a single token, and there is no ('s we assume you are presenting the name
        $methodCall $mc = $methodCall.of("split");
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );

        $mc = $methodCall.of("split").$scope($expression.of("\"eric\""));
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );
    }

    public void testArguments(){
        $methodCall $mc = $methodCall.of( $arguments.of().$and(a->((_arguments)a).isEmpty()));
        assertTrue($mc.matches("System.out.println();"));
        assertFalse($mc.matches("System.out.println(1);"));
    }

    public void testTypeArguments(){
        $methodCall $mc = $methodCall.of( $typeArguments.of().$and(a->!((_typeArguments)a).isEmpty()));
        assertTrue( $mc.matches(" Collection.<T>call()") );
        assertFalse( $mc.matches("Collection.call()") );
        assertFalse( $mc.matches("call()") );
    }

    public String mc(){
        return "text";
    }
    public void testScope(){
        $methodCall $mc = $methodCall.of();
        assertTrue($mc.isMatchAny());

        //make sure it matches ANY method call
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );

        //scope that is a specific string
        $mc.$scope("\"eric\"");
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> "deric".split("a"))) );

        //specific scope
        $mc.$scope( _string.of("eric"));
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> "deric".split("a"))) );

        //by class type
        $mc = new $methodCall().$scope(_string.class);
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );

        //by more than one class type
        $mc = new $methodCall().$scope(_string.class, _methodCall.class);
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> mc().split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );

        $mc = new $methodCall().$scope(e-> e instanceof _string);
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );

        $mc = new $methodCall().$scope(e-> e != null);
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Expressions.methodCallEx(()-> System.out.println("a"))) );
        assertFalse( $mc.matches( Expressions.methodCallEx( ()-> mc())));

    }
}
