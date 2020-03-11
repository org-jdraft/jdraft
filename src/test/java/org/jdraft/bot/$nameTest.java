package org.jdraft.bot;

import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import junit.framework.TestCase;

import org.jdraft.Ast;
import org.jdraft._class;

public class $nameTest extends TestCase {

    public void testS(){
        $name $n = $name.of("x");
        assertTrue($n.matches("x"));
        assertTrue($n.matches(new Name("x")));
        assertTrue($n.matches(new SimpleName("x")));

        assertFalse($n.matches("y"));
        assertFalse($n.matches(new Name("y")));
        assertFalse($n.matches(new SimpleName("y")));

        assertNotNull($n.select("x"));
        assertNotNull($n.select(new Name("x")));
        assertNotNull($n.select(new SimpleName("x")));

        assertNull($n.select("y"));
        assertNull($n.select(new Name("y")));
        assertNull($n.select(new SimpleName("y")));

        class C{
            C(){}
            int C = 3;
            public C getC(){
                return new C();
            }
        }
        assertEquals(5, $name.of("C").countIn(C.class));
    }

    public void testNameAsExpression(){
        class C{
            int[] a = {1,2,3,4};
            int b = a[0];
            void p(){
                System.out.println(1);
            }
        }

        Ast.describe(Ast.of(C.class));
       //System.out.println( _c );
        assertEquals(1, $name.of("println").countIn(C.class));
        assertEquals(2, $name.of("a").countIn(C.class));

        _class _c = $name.of("a").forEachIn(C.class, n->{
            if( n.ast() instanceof SimpleName ){
                n.ast().replace(new SimpleName("c"));
            }
            //if( n.ast() instanceof Expression){
            //    n.ast().replace(new NameExpr("c"));
            //}
            if( n.ast() instanceof Name){
                n.ast().replace(new Name("c"));
            }
        });

    }
}
