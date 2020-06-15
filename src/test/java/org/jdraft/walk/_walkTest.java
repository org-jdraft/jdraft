package org.jdraft.walk;

import junit.framework.TestCase;
import org.jdraft.*;


import java.util.function.Predicate;

public class _walkTest extends TestCase {

    public void testInterface(){
        _class _c = _class.of(_walkTest.class);

        // ? FROM _c
        _walk _w = _c.walk();
        // count (all nodes)
        assertTrue( _w.count() > 3);

                  //SELECT ? FROM _c WHERE node.getClass() == _intExpr.class
        _w = _c.walk(_intExpr.class);

        assertTrue( _w.count() > 2);

        System.out.println(
                //SELECT count FROM _c WHERE node.getClass() == _intExpr.class
                _c.walk(_intExpr.class).count());

        System.out.println(
                //SELECT count FROM _c WHERE node.getClass() = _intExpr.class & ((_intExpr)node).getValue() > 0
                _c.walk(_intExpr.class).count(i-> i.getValue()>0));
        //NOTE: _literal is an interface
        _c.walk(_expr._literal.class).print();
        _c.walk(_intExpr.class).print(_i-> _i.getValue() > 1);

        _args _as = _args.of("(1,'c')");
        assertTrue(_as.is("1, 'c'"));
        assertFalse(_as.is("'c', 1"));


        //_c.walk("System.out.println($any$);")
    }

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
        _c.walk(_intExpr.class).print( _i->_i.getValue() < 0);
        _c.walk(_binaryExpr.class).print(_b->_b.isMinus());

        _walk _w = _c.walk();

        assertNotNull(_w.first(n-> n instanceof _binaryExpr));
        assertNotNull(_w.first(n-> n instanceof _lambdaExpr));

        assertNotNull(_c.walk(_lambdaExpr.class).first(_l->_l.getParam("s").getType().isUnknownType()));


        assertEquals( 1, _w.list(n -> n instanceof _lambdaExpr).size());
        _c.walk(_lambdaExpr.class).print();
        _c.walk(_lambdaExpr.class).print(_l->_l.hasParams());
        TestCase.assertEquals( 1, _c.walk(_lambdaExpr.class).list().size());
        TestCase.assertEquals( 1, _c.walk(_lambdaExpr.class).list(_l->_l.hasParams()).size());

        TestCase.assertEquals(1, _w.stream(n-> n instanceof _lambdaExpr).count());
        //assertEquals(1, _w.stream().count());
        TestCase.assertEquals(5, _c.walk(_intExpr.class).stream().count());
        TestCase.assertEquals(5, _c.walk(_expr._literal.class).count());
        TestCase.assertEquals(2, _c.walk(_parenthesizedExpr.class).count());
        TestCase.assertEquals(4, _c.walk(_binaryExpr.class).count());

        TestCase.assertEquals( 3, _c.walk(_field.class).count());

        _c.walk(_lambdaExpr.class).print();
        _c.walk(_binaryExpr.class).printTree();
    }

    public void testWalkTraversal(){

    }
}
