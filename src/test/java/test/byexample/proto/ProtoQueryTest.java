package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Stmt;
import org.jdraft._class;
import org.jdraft.proto.$;
import org.jdraft.proto.$$;
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
}
