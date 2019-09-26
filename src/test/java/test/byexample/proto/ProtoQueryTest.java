package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Stencil;
import org.jdraft.Stmt;
import org.jdraft._class;
import org.jdraft.proto.*;

/**
 * Go over the main methods for querying into source code
 * using the proto
 *
 */
public class ProtoQueryTest extends TestCase {
    @interface Ann{
        int value();
        String name() default "Eric";
    }

    class TheExampleClass{
        @Ann(1)
        public int i=0;
    }

    public static final $stmt $printOne = $.stmt("System.out.print(1);");

    public void testMatches(){
        assertTrue( $printOne.matches("System.out.print(1);") );
        assertTrue($printOne.matches( Stmt.of(()->System.out.print(1)) ));

        assertFalse($printOne.matches("System.out.print(2);") );
    }

    public void testQueryForm_v_DDL(){
        class AClass{

        }

        //the WHERE (CRITERIA) is FIRST and stored
        // AND it is reusable... and mutable)

        //FROM is a parameter

        //WHAT ou are SELECTING

        //WHERE              //FROM               //SELECT & DO THIS WITH EACH RESULT
        $class.of($.PUBLIC).streamIn(AClass.class).map(_class::getFullName).forEach(System.out::println);

        //prototype "criteria" (part QBE, part composite lambda, part jquery sizzle)
        $class $cl = $class.of($.PUBLIC, $.name("A$nm$"));

        //we can use/reuse it (i.e. an ad hoc query verses a "stored procedure"

        //verify the name stencil is
        assertEquals(Stencil.of("A$nm$"),$cl.name.nameStencil);

        //AND it is also modifyable (hardcode "class" for "nm" parameter
        $cl.hardcode$("nm", "class");

        assertEquals(Stencil.of("Aclass"),$cl.name.nameStencil);

    }

    public void test$protoRelativeMatch(){

        // relative constraints
        $.of().$hasChild($.literal());
        $.of().$hasParent($.lambda());
        $.of().$hasAncestor($.localClassStmt());
        $.of().$hasDescendant($enum.of());

        $.of().$hasNoChild($enum.of());
        $.of().$hasNoParent($class.of($.ABSTRACT), $interface.of(), $annotation.of());
        $.of().$hasNoDescendant($enum.of());
        $.of().$hasNoAncestor($class.of($.ABSTRACT));//not the member of an abstract class
    }

    public void test$protoLocationMatch(){
        //$isBefore( $expr );
        //$isBefore( $stmt );

        //$isAfter( $expr );
        //$isAfter( $stmt );
    }

    //I need a way of scanning from the bottom up...
    //i.e. I might be at a particular point in the code
    // like when debugging you narrow things down to a particular line/statement/expression
    ///...then you "look at the preceding code"

    //expand scope
    //i.e. if
}
