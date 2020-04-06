package org.jdraft.bot;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.*;

public class $returnStmtTest extends TestCase {

    public void testLocalLineRangeSearch(){
        _class _c = _class.of("public class F{" + //line 0
                "    public int t(){",            //line 1
                "        return 5;",              //line 2
                "    }",                          //line 3
                "    public int r(){",            //line 4
                "         return 7;",             //line 5
                "    }",
                "}");
        assertEquals( 2, $returnStmt.of().countIn(_c));
        assertEquals( 1, $returnStmt.of().$atLine(2).countIn(_c));
        assertEquals(1,  $returnStmt.of().$atLine(5).countIn(_c));

        //testing in a range
        assertEquals( 2, $returnStmt.of().$isInRange(0, 100).countIn(_c));
        assertEquals( 2, $returnStmt.of().$isInRange(_c).countIn(_c));
        assertEquals( 2, $returnStmt.of().$isInRange(_c.ast()).countIn(_c));
        assertEquals( 1, $returnStmt.of().$isInRange(_c.getMethod("t")).countIn(_c));
        assertEquals( 1, $returnStmt.of().$isInRange(_c.getMethod("r").ast()).countIn(_c));
    }

    /*
    public void testHasAncestor(){
        //works
        //$int int_return = $int.of().$isParent(_returnStmt.class);
        //$int int_return = $int.of().$isParent(p-> p instanceof ReturnStmt);

        $int int_return = $int.of().$isParent(p-> p instanceof _returnStmt);


        //$int int_return = $int.of().$isParent(_returnStmt.class);
        class C{
            int i = 0; //doesnt match (not a return stmt)
            int m(){
                return 3; //(matches) int return stmt
            }
        }
        assertEquals( 1, int_return.countIn(C.class));
    }

     */

    public void testParent(){

        class C{
            void v(){
                int i=0;
            }

        }
        _class _c = _class.of(C.class);
        $int.of().forEachIn(C.class, i-> System.out.println( i.ile.getParentNode().get().getClass()));


        assertEquals(1, $int.of().$isParent(VariableDeclarator.class).countIn(C.class));
        assertEquals(1, $int.of().$isParent(_variable.class).countIn(C.class));
    }
    public void testCopy(){
        //r-> r.isExpression(e-> e instanceof LiteralExpr)
        $returnStmt $rs = $returnStmt.of( "return $any$;");
        assertTrue( $rs.matches("return 1;"));
        assertTrue( $rs.matches("return call();"));

        $returnStmt $copy = $rs.copy().$and(r-> r.isExpression(1));
        assertTrue( $copy.matches("return 1;"));
        assertFalse( $copy.matches("return call();"));
        assertTrue( $rs.matches("return call();"));

        $copy.$and(r-> r.isExpression(e -> e instanceof _expression._literal));
        assertTrue( $copy.matches("return 1;"));
        assertFalse( $copy.matches("return call();"));
        assertTrue( $rs.matches("return call();"));

    }

    public void testReturnStmt(){
        assertTrue($returnStmt.of().matches( "return;"));
        assertTrue($returnStmt.of().matches( "return 1;"));
        assertTrue($returnStmt.of().matches( "return m();"));
        assertTrue($returnStmt.of().matches( "return new Object();"));
    }

    public void testExpressionRequiredViaLambda(){
        $returnStmt $rs = $returnStmt.of().$and(r-> r.hasExpression() );

        assertFalse( $rs.matches( "return;"));
        assertTrue( $returnStmt.of().matches( "return 1;"));
        assertTrue( $returnStmt.of().matches( "return m();"));
        assertTrue( $returnStmt.of().matches( "return new Object();") );
    }

    public void testExact(){
        $returnStmt $rs = $returnStmt.of("return 1;");
        assertFalse( $rs.matches( "return;"));
        assertTrue( $rs.matches( "return 1;"));
        assertFalse( $rs.matches( "return 2;"));
        assertFalse( $rs.matches( "return 'c';"));
    }

    public void testModifyByEmbeddedBot(){
        $returnStmt $rs = $returnStmt.of();
        assertTrue( $rs.matches("return;"));
        assertTrue( $rs.matches("return 1;"));
        assertTrue( $rs.matches("return 2;"));
        assertTrue( $rs.matches("return 'c';"));
        $rs.$expression("1");
        assertFalse( $rs.matches("return;"));
        assertTrue( $rs.matches("return 1;"));
        assertFalse( $rs.matches("return 2;"));
        assertFalse( $rs.matches("return 'c';"));
        $rs.$expression("'c'");
        assertFalse( $rs.matches("return;"));
        assertFalse( $rs.matches("return 1;"));
        assertFalse( $rs.matches("return 2;"));
        assertTrue( $rs.matches("return 'c';"));
    }

    public void testSelect(){
        class C{
            void m(){ //1 (blockStmt)
                System.out.println( 1 ); //2
                return; //3
            }
            int i(){ //4 (blockStmt)
                System.out.println( 1 ); //5
                return 3; //6
            }
        }
        assertEquals( 6, $statement.of().countIn(C.class));
        assertEquals( 2, $statement.of(_blockStmt.class).countIn(C.class));
        assertEquals( 2, $statement.of(_expressionStmt.class).countIn(C.class));
        assertEquals(2, $returnStmt.of().countIn(C.class));
    }

}
