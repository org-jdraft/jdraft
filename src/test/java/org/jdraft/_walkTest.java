package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;


import java.util.function.Predicate;

public class _walkTest extends TestCase {

    public void testW(){
        class C{
            Predicate<String> ps = (s)->s.isEmpty();
            float x,y;
            void m(){
                int res = 1 + ((2 * 3) + 4) - 5;
                //
            }
        }
        _java._walk _w = _class.of(C.class).walk();

        assertNotNull(_w.first(n-> n instanceof _binaryExpression));
        assertNotNull(_w.first(_lambda.class));
        assertNotNull(_w.first(_lambda.class, _l->_l.getParameter("s").getTypeRef().isUnknownType()));

        assertEquals( 1, _w.list(n -> n instanceof _lambda).size());
        _w.print(_lambda.class);
        _w.print(_lambda.class, _l->_l.hasParameters());
        assertEquals( 1, _w.list(_lambda.class).size());
        assertEquals( 1, _w.list(_lambda.class, _l->_l.hasParameters()).size());

        assertEquals(1, _w.stream(n-> n instanceof _lambda).count());
        assertEquals(1, _w.stream(_lambda.class).count());
        assertEquals(5, _w.stream(_int.class).count());
        assertEquals(5, _w.stream(_expression._literal.class).count());
        assertEquals(2, _w.stream(_enclosedExpression.class).count());
        assertEquals(4, _w.stream(_binaryExpression.class).count());

        assertEquals( 3, _w.stream(_field.class).count());

        _w.print(_lambda.class);
        _w.printTree(_binaryExpression.class);
    }

    public void testWalkTraversal(){

    }
}
