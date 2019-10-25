package org.jdraft.pattern;

import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft._class;

public class SpatternBlockBodyTest extends TestCase {

     public void testBeforeAfterPreviousNextBlock() {
         class C {
             void b4() {
                 System.out.println(0);
             }

             void m() {
                 System.out.println(1);
                 System.out.println(2);
                 {
                     System.out.println(3);
                     System.out.println(4);
                     if (true) {
                         System.out.println(5);
                     }
                     HERE: System.out.println(6); //<--- at this Statement
                     if (1 == 1) {
                         System.out.println(7);
                     }
                     System.out.println(8);
                 }
                 System.out.println(9);
             }

             void after() {
                 System.out.println(10);
             }
         }
         _class _c = _class.of(C.class);
         LabeledStmt ls = $.labeledStmt().firstIn(_c);
         $stmt $println = $stmt.of( (Object $any$)-> System.out.println($any$) );

         assertEquals( 3, $pattern.BlockScope.listAllBefore(ls, $println ).size() );
         assertEquals( Stmt.of(()->System.out.println(5) ), $pattern.BlockScope.findPrevious(ls, $println ) );
         assertEquals( 2, $pattern.BlockScope.listAllAfter(ls, $println ).size() );
         assertEquals( Stmt.of(()->System.out.println(7) ), $pattern.BlockScope.findNext(ls, $println ) );

         //System.out.println( "BEFORE   " + $pattern.BlockScope.listAllBefore(ls, $println ) );
         //System.out.println( "PREVIOUS " + $pattern.BlockScope.findPrevious(ls, $println ) );
         //System.out.println( "AFTER    " + $pattern.BlockScope.listAllAfter(ls, $println ) );
         //System.out.println( "NEXT     " + $pattern.BlockScope.findNext(ls, $println ) );
     }

    public void testBeforeAfterPreviousNextBody() {
        class C {
            void b4() {
                System.out.println(0);
            }

            void m() {
                System.out.println(1);
                System.out.println(2);
                {
                    System.out.println(3);
                    System.out.println(4);
                    if (true) {
                        System.out.println(5);
                    }
                    HERE: System.out.println(6); //<--- at this Statement
                    if (1 == 1) {
                        System.out.println(7);
                    }
                    System.out.println(8);
                }
                System.out.println(9);
            }

            void after() {
                System.out.println(10);
            }
        }
        _class _c = _class.of(C.class);
        LabeledStmt ls = $.labeledStmt().firstIn(_c);
        $stmt $println = $stmt.of( (Object $any$)-> System.out.println($any$) );

        assertEquals( 5, $pattern.BodyScope.listAllBefore(ls, $println ).size() );
        assertEquals( Stmt.of(()->System.out.println(5) ), $pattern.BodyScope.findPrevious(ls, $println ) );

        System.out.println( $pattern.BodyScope.listAllAfter(ls, $println) );

        assertEquals( 3, $pattern.BodyScope.listAllAfter(ls, $println ).size() );
        assertEquals( Stmt.of(()->System.out.println(7) ), $pattern.BodyScope.findNext(ls, $println ) );

        //System.out.println( "BEFORE   " + $pattern.BlockScope.listAllBefore(ls, $println ) );
        //System.out.println( "PREVIOUS " + $pattern.BlockScope.findPrevious(ls, $println ) );
        //System.out.println( "AFTER    " + $pattern.BlockScope.listAllAfter(ls, $println ) );
        //System.out.println( "NEXT     " + $pattern.BlockScope.findNext(ls, $println ) );
    }

    /*

    public void testGetContextContainer(){
         class C{
             public void m(){
                 System.out.println(1);
                 System.out.println(2);
                 System.out.println(3);
                 System.out.println(4);
             }
         }
         _class _c = _class.of(C.class);
         Expression e = $.of(3).firstIn(_c);

         //Node n = getContextContainer( e );
         //System.out.println( n );
         //List<Node> nodesBefore = nodesBefore( e );
         //System.out.println( nodesBefore );
         IntegerLiteralExpr ie = $pattern.BlockScope.findPrevious(e, $.of(1) );
         System.out.println( ie );
         assertNotNull( ie );

         ie = $pattern.BlockScope.findNext(e, $.of(4) );
         System.out.println( ie );
         assertNotNull( ie );

         //find all statements in the same context before e
         List<Node> selectAllBefore = $pattern.BlockScope.listAllBefore(e, $.stmt() );
         System.out.println( selectAllBefore );

        //find all statements in the same context after e
         List<Node> selectAllAfter = $pattern.BlockScope.listAllAfter(e, $.stmt() );
         System.out.println( selectAllAfter );
    }
     */
}
