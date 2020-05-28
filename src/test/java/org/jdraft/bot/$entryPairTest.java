package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Print;

public class $entryPairTest extends TestCase {

    @interface I{
        int value() default 12;
    }

    public void testE(){
        $entryPair $ep = $entryPair.of();
        @I
        class Zero{ }
        assertEquals(0, $ep.countIn(Zero.class));

        @I(1)
        class OneValue{}
        //I need to
        //assertEquals(1, $ep.countIn(OneValue.class));
        Print.tree(Ast.of(OneValue.class));

        @I(value=2)
        class OneKV{ }
        assertEquals(1, $ep.countIn(OneKV.class));
    }

}