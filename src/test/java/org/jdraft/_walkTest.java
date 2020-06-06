package org.jdraft;

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

        assertNotNull(_w.first(n-> n instanceof _binaryExpr));
        assertNotNull(_w.first(_lambdaExpr.class));
        assertNotNull(_w.first(_lambdaExpr.class, _l->_l.getParam("s").getType().isUnknownType()));

        assertEquals( 1, _w.list(n -> n instanceof _lambdaExpr).size());
        _w.print(_lambdaExpr.class);
        _w.print(_lambdaExpr.class, _l->_l.hasParams());
        assertEquals( 1, _w.list(_lambdaExpr.class).size());
        assertEquals( 1, _w.list(_lambdaExpr.class, _l->_l.hasParams()).size());

        assertEquals(1, _w.stream(n-> n instanceof _lambdaExpr).count());
        assertEquals(1, _w.stream(_lambdaExpr.class).count());
        assertEquals(5, _w.stream(_intExpr.class).count());
        assertEquals(5, _w.stream(_expr._literal.class).count());
        assertEquals(2, _w.stream(_parenthesizedExpr.class).count());
        assertEquals(4, _w.stream(_binaryExpr.class).count());

        assertEquals( 3, _w.stream(_field.class).count());

        _w.print(_lambdaExpr.class);
        _w.printTree(_binaryExpr.class);
    }

    public void testWalkTraversal(){

    }
}
