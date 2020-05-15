package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;

import java.util.List;

/**
 * Test calling {@link At#nodeAt(Node, int)}  and {@link At#memberAt(Node, int)} variants
 *
 */
public class AtTest extends TestCase {

    public void testGetAt(){
/*<--*/ Statement st = At.nodeAt(AtTest.class, 23); //I want to get this line of code
        assertNotNull(st);
        String s =
/*<--*/                 "A String on a line by itself";
        StringLiteralExpr sle = At.nodeAt(AtTest.class, 26);

/*<--*/ int i = 100;
        assertEquals( Types.of(int.class),At.nodeAt(AtTest.class, 29,9));
        assertEquals( "i",At.nodeAt(AtTest.class, 29,13).toString());
        assertEquals( Exprs.of(100),At.nodeAt(AtTest.class, 29,18) );
    }
    //NOTE: this is sensititve to lines in file method should start at
    //IF YOU REFACTOR THIS CODE YOU NEED TO UPDATE THE VALUE OF THIS_LINE_NUMBER
    public void testAt(){
        /*<--*/ int THIS_LINE_NUMBER = 37; //<-- THIS VALUE IS SENSITIVE TO THE LINE NUMBER
        Node n = At.nodeAt(AtTest.class, THIS_LINE_NUMBER, 9);
        assertNotNull(n);
        assertNotNull( At.nodeAt(AtTest.class, THIS_LINE_NUMBER, 9) );
        MethodDeclaration m = At.memberAt(AtTest.class, THIS_LINE_NUMBER, 9);
        assertTrue( m.getNameAsString().equals("testAt"));
        MethodDeclaration md = At.memberAt(n, 40);
        assertEquals("testAt", md.getNameAsString());

        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        md = At.memberAt(Ast.of(AtTest.class), 39);
        Log.setAdapter(new Log.SilentAdapter());
        assertEquals("testAt", md.getNameAsString());
    }

    public void testAtLineOnly(){
/*<--*/ int THIS_LINE_NUMBER = 53; //<-- THIS VALUE IS SENSITIVE TO THE LINE NUMBER
        CompilationUnit cu = Ast.of(AtTest.class);
        assertEquals( 1, cu.getType(0).getMethodsByName("testAtLineOnly").size());
        MethodDeclaration md = At.memberAt(cu, THIS_LINE_NUMBER);
        assertEquals("testAtLineOnly",md.getNameAsString());
        Statement st = At.nodeAt(cu, 53);
        assertNotNull( st );
        System.out.println( st );
    }

    public void testMemberAt(){
        _class _c = _class.of("public class C{",        //line 1
                "    int f = 100;",                     //line 2
                "    public C(int f){ this.f = f; }",   //line 3
                "    public int getF(){ return f;}",    //line 4
                "    ",                                 //line 5
                "    { System.out.println(1); }",       //line 6
                "}");                                   //line 7

        _class _cc = At._memberAt(_c, 1);
        assertEquals( _c, _cc);
        _field _f = At._memberAt(_c, 2);
        assertTrue(_f.is("int f = 100;"));
        _constructor _ct = At._memberAt(_c, 3);
        assertTrue(_ct.is("public C(int f){ this.f = f; }"));
        _method _m = At._memberAt(_c, 4);
        assertTrue(_m.is("public int getF(){ return f; }"));

        _cc = At._memberAt(_c, 5);
        assertEquals( _c, _cc);

        _initBlock _ib = At._memberAt(_c, 6);
        assertEquals( _initBlock.of("{ System.out.println(1); }"), _ib);

        _cc = At._memberAt(_c, 7);
        assertEquals( _c, _cc);

        assertNull( At._memberAt(_c, 8));
    }


    public void testBlockAt3(){
        class C {
            void m() {
                assert(0 == 0);
                assert(1 == 1);
                assert(2==2);
                /*comment*/
            }
        }
        _class _c = _class.of(C.class);
        //System.out.println( _c.ast().getRange().get() );
        List<Comment> cc = _c.getMethod("m").ast().getAllContainedComments();
        //System.out.println( cc.get(0).getRange() );
        Comment c = cc.get(0);
        System.out.println( "COMMENT RANGE" + c.getRange().get() );
        BlockStmt bs = At.blockAt( _c.ast(), c.getRange().get().begin);
        assertEquals(3, At.getStatementIndex(bs, c.getRange().get().begin));


        bs.addStatement(3, Stmts.blockStmt("{/*<code></code>*/}"));
        System.out.println( bs );
        assertNotNull( bs );
    }

    public void testBlockAt2(){
        class C {
            void m() {
                assert(0 == 0);
                assert(1 == 1);
                /*comment*/
                assert(2==2);
            }
        }
        _class _c = _class.of(C.class);
        //System.out.println( _c.ast().getRange().get() );
        List<Comment> cc = _c.getMethod("m").ast().getAllContainedComments();
        //System.out.println( cc.get(0).getRange() );
        Comment c = cc.get(0);
        System.out.println( "COMMENT RANGE" + c.getRange().get() );
        BlockStmt bs = At.blockAt( _c.ast(), c.getRange().get().begin);
        assertEquals(2, At.getStatementIndex(bs, c.getRange().get().begin));


        bs.addStatement(2, Stmts.labeledStmt("HERE:{}"));
        System.out.println( bs );
        assertNotNull( bs );
    }

    public void testBlockAt1(){
        class C {
            void m() {
                assert (0 == 0);
                /*comment*/
            }
        }
        _class _c = _class.of(C.class);
        //System.out.println( _c.ast().getRange().get() );
        List<Comment> cc = _c.getMethod("m").ast().getAllContainedComments();
        //System.out.println( cc.get(0).getRange() );
        Comment c = cc.get(0);
        //System.out.println( "COMMENT RANGE" + c.getRange().get() );
        BlockStmt bs = At.blockAt( _c.ast(), c.getRange().get().begin);
        assertEquals(1, At.getStatementIndex(bs, c.getRange().get().begin));
        assertNotNull( bs );
    }

    public void testBlockAt(){
        class C {
            int f;
            void m(){
                /*comment*/
            }
        }
        _class _c = _class.of(C.class);
        //System.out.println( _c.ast().getRange().get() );
        List<Comment> cc = _c.getMethod("m").ast().getAllContainedComments();
        //System.out.println( cc.get(0).getRange() );
        Comment c = cc.get(0);
        //System.out.println( "COMMENT RANGE" + c.getRange().get() );
        BlockStmt bs = At.blockAt( _c.ast(), c.getRange().get().begin);
        assertEquals(0, At.getStatementIndex(bs, c.getRange().get().begin));
        assertNotNull( bs );

        //System.out.println( bs );
    }
}
