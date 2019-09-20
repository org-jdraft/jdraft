package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Stencil;
import org.jdraft.Stmt;
import org.jdraft._class;
import org.jdraft.proto.$;
import org.jdraft.proto.$$;
import org.jdraft.proto.$class;
import org.jdraft.proto.$stmt;

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

    public static final $stmt $printOne = $$.stmt("System.out.print(1);");

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
        assertEquals(Stencil.of("A$nm$"),$cl.name.idStencil);

        //AND it is also modifyable (hardcode "class" for "nm" parameter
        $cl.hardcode$("nm", "class");

        assertEquals(Stencil.of("Aclass"),$cl.name.idStencil);

    }
}
