package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft._booleanExpr;
import org.jdraft._doStmt;

public class $doStmtTest extends TestCase {

    public void testPredicate(){
        //a doStmt with
        $doStmt $ds = $doStmt.of( (_doStmt d)-> $stmt.of("System.out.println($any$);").isIn( d.getBody() ));
        class C{
            public void ff(){
                do{
                    assert( true);
                    System.out.println(1);
                } while( true);
            }
        }

        assertTrue( $ds.isIn(C.class));

        $ds = $doStmt.of().$body( $stmt.of("System.out.println($any$);") );
        assertTrue( $ds.isIn(C.class));

        /* This fails for now
        $ds = $doStmt.of().$body( $stmt.of("System.out.println(2);") );
        assertFalse( $ds.isIn(C.class));

         */
    }
    public void testMatch(){
        assertTrue($.doStmt().isMatchAny());

        //match against ANY boolean expr (i.e. do... while(true) or do...while(false)
        assertTrue($.doStmt().$condition($boolean.of()).match(Stmt.doStmt("do{ System.out.println(1); } while(false);")));
        assertTrue($.doStmt().$condition(_booleanExpr.of(true)).match(Stmt.doStmt("do{ System.out.println(1); } while(true);")));


        assertTrue($.doStmt().$and( d-> d.getBody().isImplemented()).matches( Stmt.doStmt("do{ System.out.println(1); } while(true);")));
        assertTrue($doStmt.of().match(Stmt.doStmt("do{ System.out.println(1); } while(true);")));

        assertTrue($.doStmt().$body(s-> s.ast().isBlockStmt()).matches(Stmt.doStmt("do{ System.out.println(1); } while(true);")));
    }

    public void testDoAnyMatchAll(){
        class C{ }
        assertEquals(0, $doStmt.of().countIn(C.class));

        class D{
            void m(){
                do{
                    System.out.println(1);
                }while(true);
            }
        }
        assertEquals(1, $doStmt.of().countIn(D.class));

        class E{
            void m(){
                do{}while(true);
            }
            void f(){
                do{}while(true);
            }
        }
        assertEquals(2, $doStmt.of().countIn(E.class));

    }
    public void testdo(){
        _doStmt _ds = _doStmt.of( (Object $any$)-> {
                    int $var$=0;
                    do{
                        System.out.println($any$);
                    }
                    while( $var$ > 0);
                });
        $doStmt $ds = $doStmt.of(_ds);

        class C{
            void m(){
                int i = 100;
                do{
                    System.out.println(1);
                } while(i>0);
            }
            void nomatch(){

            }

        }
        assertEquals(1, $ds.countIn(C.class));
    }
}
