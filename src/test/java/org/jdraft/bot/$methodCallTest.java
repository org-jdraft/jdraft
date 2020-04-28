package org.jdraft.bot;

import com.github.javaparser.printer.ASCIITreePrinter;
import junit.framework.TestCase;
import org.jdraft.*;
import static java.lang.System.out;
import org.jdraft._arguments;
import org.jdraft.macro._addImports;
import org.jdraft.macro._packageName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class $methodCallTest extends TestCase {


    public void testOf(){
        class GG{
            GG(){
                System.out.println( "In the constructor");
            }
            void m(){
                System.out.println( 123);
            }
        }
        assertEquals(2, $methodCall.of( (Object $any$)-> System.out.println($any$) ).countIn(GG.class));

        _class _c = _class.of(GG.class);
        ASCIITreePrinter.print(_c);

        $methodCall.of().printEachTreeIn(_c);

        $methodCall.of().printEachTreeIn(GG.class);

    }

    public void test$hasDescendant(){
        class GHJ{
            void m(){
                System.out.println( 1 );
            }
            GHJ(){
                System.out.println(3);
            }
        }

        //predicate
        assertEquals(2, $methodCall.of().$hasDescendant(n-> n instanceof _expression._literal).countIn(GHJ.class));
        assertEquals(0, $methodCall.of().$hasDescendant(n-> n instanceof _double).countIn(GHJ.class));

        //selector
        assertEquals(2, $methodCall.of().$hasDescendant($int.of()).countIn(GHJ.class));
        assertEquals(0, $methodCall.of().$hasDescendant($double.of()).countIn(GHJ.class));
        assertEquals(2, $methodCall.of().$hasDescendant($double.of(), $int.of()).countIn(GHJ.class));

        //class
        assertEquals(2, $methodCall.of().$hasDescendant(_expression._literal.class).countIn(GHJ.class));
        assertEquals(2, $methodCall.of().$hasDescendant(_int.class).countIn(GHJ.class));
        assertEquals(2, $methodCall.of().$hasDescendant(_double.class, _int.class).countIn(GHJ.class));
        assertEquals(0, $methodCall.of().$hasDescendant(_double.class).countIn(GHJ.class));
    }

    public void test$isParent(){
        class GHJ{
            void m(){
                System.out.println( 1 );
            }
            GHJ(){
                System.out.println(3);
            }
        }
        Print.tree(GHJ.class);
        assertEquals(2, $methodCall.of().$isParent(m-> m instanceof _expressionStmt).countIn(GHJ.class));
        assertEquals(2, $methodCall.of().$isParent(_expressionStmt.class).countIn(GHJ.class));

        assertEquals(0, $methodCall.of().$isParent(_constructor.class).countIn(GHJ.class));
        assertEquals(0, $methodCall.of().$isParent(_class.class).countIn(GHJ.class));
    }

    public void test$hasAncestor(){
        class GHJ{
            void m(){
                System.out.println( 1 );
            }
            GHJ(){
                System.out.println(3);
            }
        }
        assertEquals(1, $methodCall.of().$hasAncestor(m-> m instanceof _method).countIn(GHJ.class));
        assertEquals(1, $methodCall.of().$hasAncestor(_method.class).countIn(GHJ.class));
        assertEquals(1, $methodCall.of().$hasAncestor(_constructor.class).countIn(GHJ.class));
        assertEquals(2, $methodCall.of().$hasAncestor(_class.class).countIn(GHJ.class));
    }

    public void test$isInMember(){
        class HH{
            void m(){
                System.out.println(2);
            }
            HH(){
                System.out.println(3);
            }
        }
        assertEquals(1, $methodCall.of().$isInMember(m-> m instanceof _method).countIn(HH.class));
        assertEquals(1, $methodCall.of().$isInMember(_method.class).countIn(HH.class));
        assertEquals(1, $methodCall.of().$isInMember(_constructor.class).countIn(HH.class));
        assertEquals(0, $methodCall.of().$isInMember(_field.class).countIn(HH.class));

        _class _am = _class.of(HH.class);
        _am.addInitBlock( ()-> {System.out.println(1);}); //add an init block member
        assertEquals(1, $methodCall.of().$isInMember(_initBlock.class).countIn(_am));
    }

    public void test$IsInType(){
        class G{
            void m(){
                System.out.println(1);
            }
        }

        assertEquals(0, $methodCall.of().$isInType(t-> t instanceof _class).countIn(_methodCall.of("print(1);")));

        assertEquals(1, $methodCall.of().$isInType(t-> t instanceof _class).countIn(G.class));
        assertEquals(1, $methodCall.of().$isInType(_class.class).countIn(G.class));
        assertEquals(0, $methodCall.of().$isInType(t-> t instanceof _interface).countIn(G.class));
        assertEquals(0, $methodCall.of().$isInType(_interface.class).countIn(G.class));

        assertEquals(1, $methodCall.of().$isInType(_class.class, _enum.class).countIn(G.class));
    }



    public void testIsPackage(){
        @_packageName("blah")
        @_addImports({UUID.class, Map.class, HashMap.class})
        class XDE{
            void m(){
                m();
            }
        }
        _class _c = _class.of(XDE.class);
        assertTrue(_c.isInPackage("blah"));
        assertEquals("blah", _c.getPackageName());
        //System.out.println( _class.of(XDE.class));

        assertEquals(1, $methodCall.of().$isInPackage(p-> p.is("blah")).countIn(XDE.class));
        assertEquals(1, $methodCall.of().$isImports(_is-> _is.hasImport(Map.class)).countIn(XDE.class));

        @_packageName("aaaa.bbbb.cccc")
        class GG{
            void m(){
                m();
            }
        }
        assertEquals(1, $methodCall.of().$isInPackage("aaaa.bbbb.cccc").countIn(GG.class));
        assertEquals(1, $methodCall.of().$isInPackage("$any$.cccc").countIn(GG.class));
        assertEquals(1, $methodCall.of().$isInPackage("aaaa.$any$").countIn(GG.class));
    }
    public void testMatchAny(){
        assertTrue($methodCall.of().isMatchAny());
        assertNotNull($methodCall.of().get$name());
        assertNotNull($methodCall.of().get$scope());
        assertNotNull($methodCall.of().get$arguments());
        assertNotNull($methodCall.of().get$typeArguments());
    }


    public void testMethodCallOr() {
        //define (2) instances of $methodCall bots
        $methodCall $a = $methodCall.of().$isInCodeUnit(c -> c instanceof _class);
        $methodCall $b = $methodCall.of().$isInCodeUnit(c -> c instanceof _interface);

        // build an $methodCall.Or instance with the (2) $methodCall bots {$a,$b}
        $methodCall.Or $or = $methodCall.or($a, $b);

        //NOTE: the $or instance IS A $methodCall itself, this is done because there may be
        // some instance that expects a $methodCall, and this will satisfy the requirement
        $methodCall $mc = $or;

        //here we modify the "base instance", of the $or, we add a constraint that applies physically to
        //the underlying $or instance (which again IS a $methodCall), so we update it's
        //predicate, but it can be "thought of" LOGICALLY as applying this constraint to BOTH
        //individual bots {$a, $b}
        $or.$isInCodeUnit(c -> c.hasImport(IOException.class));
        //When we try to select, we ALWAYS FIRST check the base "$or" instances select/match function
        //here the match/select returns FALSE, because the base constraint (import IOException)
        //is NOT met, even though one of the individual bots ($a) DOES match all of its constraints
        assertFalse($or.isIn(_class.of("class C{ long t = System.getTimeMillis(); }")));

        //here the match/select DOES work, because the base constrains (imports IOException) are met,
        // AND one of the OR constraints match
        //(here specifically $a, which looks for ANY methodCall that is defined within a _class)

        assertTrue($or.isIn(_class.of("import java.io.IOException;","class C{ long t = System.getTimeMillis(); }")));

        //(here specifically $b, which looks for ANY methodCall that is defined within an _interface)
        assertTrue($or.isIn(_interface.of("import java.io.IOException;","interface I{ long t = System.getTimeMillis(); }")));

        //this WILL NOT match because neither $a, or $b are true,
        //even though the common "$or" criteria are met (imports IOException)
        assertFalse($or.isIn(_enum.of("import java.io.IOException;","enum E{ A; long t = System.getTimeMillis(); }")));
    }

    public void testOrCommonPredicates(){

        //NOTE:
        $methodCall $or = $methodCall.or($methodCall.of("println"), $methodCall.of("print"))
                .$and( _mc-> _mc.isScope("System.out") || _mc.isScope("out") )
                .$and( _mc -> _mc.isArgument(0, e-> e instanceof _int) );
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
