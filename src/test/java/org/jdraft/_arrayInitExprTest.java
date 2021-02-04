package org.jdraft;

import junit.framework.TestCase;

public class _arrayInitExprTest extends TestCase {

    public void testAI(){
        class C{
            int i = 1;
            int[] j = {1}; //1
            int[][] k = { {1,2}, {3,4}}; //2,3,4

            C(){
                m(new int[] {1,2}); //instance (5)
            }
            void m( int[] values){ }
        }
        _class _c = _class.of(C.class);
        assertEquals( 5, _c.walk(_arrayInitExpr.class).count());
    }
    public void testA(){
        _arrayInitExpr _ai = _arrayInitExpr.of("{}");
        assertEquals(0, _ai.getSize());
        _ai.add("1");
        assertEquals(1, _ai.getSize());
        _ai.removeAt(0);
        assertEquals(0, _ai.getSize());
        _ai.add("\"String\"");
        assertTrue( _ai.isAt(0, _expr.of("\"String\"") ));
        _ai.removeAt(0);
        _ai.add("1","2","3");
        assertTrue( _ai.isAt(0, _e-> _e instanceof _intExpr));

        assertTrue( _ai.isAt(0, _expr.of("1") ));
        assertTrue( _ai.isAt(1, _expr.of("2") ));
        assertTrue( _ai.isAt(2, _expr.of("3") ));

        _ai.removeAt(2);
        assertTrue( _ai.isAt(0, _expr.of("1") ));
        assertTrue( _ai.isAt(1, _expr.of("2") ));
        assertEquals(2, _ai.getSize());

        _ai.add("3");
        assertTrue( _ai.isAt(0, _expr.of("1") ));
        assertTrue( _ai.isAt(1, _expr.of("2") ));
        assertTrue( _ai.isAt(2, _expr.of("3") ));

        _ai.removeAt(1);
        assertTrue( _ai.isAt(0, _expr.of("1") ));
        assertTrue( _ai.isAt(1, _expr.of("3") ));
        assertEquals(2, _ai.getSize());

    }


}