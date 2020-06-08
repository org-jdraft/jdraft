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
        _class _c = _class.of(C.class);
        _walk _w = _c.walk();

        assertNotNull(_w.first(n-> n instanceof _binaryExpr));
        assertNotNull(_w.first(n-> n instanceof _lambdaExpr));

        assertNotNull(_c.walk(_lambdaExpr.class).first(_l->_l.getParam("s").getType().isUnknownType()));


        assertEquals( 1, _w.list(n -> n instanceof _lambdaExpr).size());
        _c.walk(_lambdaExpr.class).print();
        _c.walk(_lambdaExpr.class).print(_l->_l.hasParams());
        assertEquals( 1, _c.walk(_lambdaExpr.class).list().size());
        assertEquals( 1, _c.walk(_lambdaExpr.class).list(_l->_l.hasParams()).size());

        assertEquals(1, _w.stream(n-> n instanceof _lambdaExpr).count());
        //assertEquals(1, _w.stream().count());
        assertEquals(5, _c.walk(_intExpr.class).stream().count());
        assertEquals(5, _c.walk(_expr._literal.class).count());
        assertEquals(2, _c.walk(_parenthesizedExpr.class).count());
        assertEquals(4, _c.walk(_binaryExpr.class).count());

        assertEquals( 3, _c.walk(_field.class).count());

        _c.walk(_lambdaExpr.class).print();
        _c.walk(_binaryExpr.class).printTree();
    }

    public void testWalkTraversal(){

    }
}
