package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.*;
import junit.framework.TestCase;
import org.jdraft.pattern.$;

import java.io.*;
import java.util.function.Predicate;


@Ast.cache
public class StmtsTest extends TestCase {

    public void testPreviousStmtNextStmt(){

        class C{
            void m(){
                assert(1==1);
                assert(2==2);
                assert(3==3);
            }
        }
        _class _c = _class.of(C.class);
        _method _m = _c.getMethod("m");

        assertEquals( null, Stmts.previous( _m.getStatement(0)));
        assertEquals( Stmts.of("assert(1==1);"), Stmts.previous( _m.getStatement(1)));
        assertEquals( Stmts.of("assert(2==2);"), Stmts.previous( _m.getStatement(2)));

        assertEquals( Stmts.of("assert(2==2);"), Stmts.next( _m.getStatement(0)));
        assertEquals( Stmts.of("assert(3==3);"), Stmts.next( _m.getStatement(1)));
        assertEquals( null, Stmts.next( _m.getStatement(2)));

    }

    public void testAddBeforeAfter(){
        class C{
            void m(){
                firstStmt: {}
            }

            void m2(){
                firstStmt:{}
                secondStmt:{}
            }
        }
        _class _c =_class.of(C.class);

        LabeledStmt first = (LabeledStmt) ((_labeledStmt) ($.labeledStmt().firstIn(_c))).ast();

        Stmts.addStatementsBefore(first, Stmts.of( ()->System.out.println(1) ) );
        Stmts.addStatementsAfter(first, Stmts.of( ()->System.out.println(2)));
        assertEquals( Stmts.of(()->System.out.println(1)), _c.getMethod("m").getStatement(0));
        assertEquals( Stmts.of("firstStmt:{}"), _c.getMethod("m").getStatement(1));
        assertEquals( Stmts.of(()->System.out.println(2)), _c.getMethod("m").getStatement(2));

        LabeledStmt second = (LabeledStmt) ((_labeledStmt)$.labeledStmt("secondStmt:{}").firstIn(_c)).ast();
        Stmts.addStatementsBefore(second, Stmts.of( ()->System.out.println(1) ) );
        Stmts.addStatementsAfter(second, Stmts.of( ()->System.out.println(2)));

        assertEquals( Stmts.of("firstStmt:{}"), _c.getMethod("m2").getStatement(0));
        assertEquals( Stmts.of(()->System.out.println(1)), _c.getMethod("m2").getStatement(1));
        assertEquals( Stmts.of("secondStmt:{}"), _c.getMethod("m2").getStatement(2));
        assertEquals( Stmts.of(()->System.out.println(2)), _c.getMethod("m2").getStatement(3));
    }

    public void testAddBeforeAfterMulti(){
        class C{
            void m(){
                firstStmt: {}
            }

            void m2(){
                firstStmt:{}
                secondStmt:{}
            }
        }
        _class _c =_class.of(C.class);

        LabeledStmt first = ((_labeledStmt)$.labeledStmt().firstIn(_c)).ast();

        //NOTE: we cant create/initialize more than one Statement *via Lambda) on the same line because of how stack traces work
        Stmts.addStatementsBefore(first, Stmts.of( ()->System.out.println(1) ),
                Stmts.of( ()->System.out.println(2) ) );
        Stmts.addStatementsAfter(first, Stmts.of( ()->System.out.println(3)),
                Stmts.of( ()->System.out.println(4)) );

        //System.out.println( _c );

        assertEquals( Stmts.of(()->System.out.println(1)), _c.getMethod("m").getStatement(0));
        assertEquals( Stmts.of(()->System.out.println(2)), _c.getMethod("m").getStatement(1));
        assertEquals( Stmts.of("firstStmt:{}"), _c.getMethod("m").getStatement(2));
        assertEquals( Stmts.of(()->System.out.println(3)), _c.getMethod("m").getStatement(3));
        assertEquals( Stmts.of(()->System.out.println(4)), _c.getMethod("m").getStatement(4));

        LabeledStmt second = (LabeledStmt) ((_labeledStmt)$.labeledStmt("secondStmt:{}").firstIn(_c)).ast();
        Stmts.addStatementsBefore(second, Stmts.of( ()->System.out.println(1) ),
                Stmts.of( ()->System.out.println(2)));

        Stmts.addStatementsAfter(second, Stmts.of( ()->System.out.println(3)),
                Stmts.of( ()->System.out.println(4)));

        assertEquals( Stmts.of("firstStmt:{}"), _c.getMethod("m2").getStatement(0));
        assertEquals( Stmts.of(()->System.out.println(1)), _c.getMethod("m2").getStatement(1));
        assertEquals( Stmts.of(()->System.out.println(2)), _c.getMethod("m2").getStatement(2));
        assertEquals( Stmts.of("secondStmt:{}"), _c.getMethod("m2").getStatement(3));
        assertEquals( Stmts.of(()->System.out.println(3)), _c.getMethod("m2").getStatement(4));
        assertEquals( Stmts.of(()->System.out.println(4)), _c.getMethod("m2").getStatement(5));
    }

    public void testReturnStmtLambda(){
        assertEquals( Stmts.of("return 1;") , Stmts.returnStmt(()-> 1));
        assertEquals( Stmts.of("return \"String\";"), Stmts.returnStmt(()-> "String"));
        assertEquals( Stmts.of("return $name$;"), Stmts.returnStmt((String $name$)-> {
            return $name$;
        }));
    }

    public void testDoStmtLambda(){
        DoStmt ds = Stmts.doStmt( ()->{
            do{
                System.out.println(1);
            }while(true);
        });

        assertEquals( Stmts.of( ()->System.out.println(1)), ds.getBody().asBlockStmt().getStatement(0));
        assertTrue( ds.getCondition().asBooleanLiteralExpr().getValue());

        ds = Stmts.doStmt( (Integer a) ->{
            do{
                System.out.println(a);
            } while( a != null );
        });

        assertEquals( Stmts.of( (a)->System.out.println(a)), ds.getBody().asBlockStmt().getStatement(0));
        assertEquals(BinaryExpr.Operator.NOT_EQUALS, ds.getCondition().asBinaryExpr().getOperator());
    }
    //Predicate<_field>, $snip

    public void testBlock(){
        //single stmt
        BlockStmt bs = Stmts.blockStmt( ()-> System.out.println(1));

        assertEquals(1, bs.getStatements().size());
        bs = Stmts.blockStmt( ()-> {
            System.out.println(1);
            System.out.println(2);
        });
        assertEquals(2, bs.getStatements().size());

        //ananoymous TYPE var
        bs = Stmts.blockStmt( (o)->System.out.println(o));

        //typed var
        bs = Stmts.blockStmt( (Integer i)->System.out.println(i));
        assertEquals(Stmts.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );

        //typed vars
        bs = Stmts.blockStmt( (Integer i, String s)->{
            System.out.println(i);
            System.out.println(s);
        });
        assertEquals(Stmts.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );
        assertEquals(Stmts.of( (Integer s)->System.out.println(s)), bs.getStatement(1) );

        //untyped vars
        bs = Stmts.blockStmt( (a, b, c, d)->{
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
        } );
        assertEquals(Stmts.of( (a)->System.out.println(a)), bs.getStatement(0) );
        assertEquals(Stmts.of( (b)->System.out.println(b)), bs.getStatement(1) );
        assertEquals(Stmts.of( (c)->System.out.println(c)), bs.getStatement(2) );
        assertEquals(Stmts.of( (d)->System.out.println(d)), bs.getStatement(3) );

        //what about a list of
        Predicate<_field> pf = (f)-> f.isPublic();
        Predicate<_field> ff = (f)-> f.isTypeRef(int.class);
    }

    public void testAllStmtsLambdaWithComments() {
        //make sure we can create statements with comments
        Statement st = Stmts.of(() -> { /** Comment */assert true;
        });
        assertEquals(new JavadocComment(" Comment "), st.getComment().get());
        assertEquals(Stmts.of("assert true;"), st.removeComment());

        st = Stmts.of(() -> { /* Comment */
            assert true;
        });
        assertEquals(new BlockComment(" Comment "), st.getComment().get());
        assertEquals(Stmts.of("assert true;"), st.removeComment());

        //System.out.println(st);
        st = Stmts.of(() -> { // Comment
            assert true;
        });
        assertEquals(new LineComment(" Comment"), st.getComment().get());
        assertEquals(Stmts.of("assert true;"), st.removeComment());
        //System.out.println(st);
    }

    public void testSuperCtorBody(){
        BlockStmt bs = Stmts.blockStmt("{ super(); }");
        //System.out.println( bs );

        //StaticJavaParser.parseStatement( "{ super(); }" );
    }
    public void testBody(){
        Statement st = Stmts.of("this.name = name;");
        System.out.println( st );
    }
    public void testStmtTypesWithLambda(){
        AssertStmt as = Stmts.assertStmt("assert 1==1;");
        as = (AssertStmt) Stmts.of(() -> {
            assert (1 == 1);
        });

        BlockStmt bs = Stmts.blockStmt("{ block(); }");
        bs = (BlockStmt) Stmts.of(() -> {
            System.out.println(1);
            System.out.println(2);
        });

        Statement st = Stmts.of(() -> System.out.println(1));
        assertTrue(st.isExpressionStmt());
        assertEquals(Stmts.of("System.out.println(1);"), st);

        BreakStmt bss = Stmts.breakStmt("break;");
        //bss = Stmt.of( ()-> { break; }  );
        //bss = Stmt.of( ()-> { continue; }  );

        Stmts.breakStmt("break outer;");
        Stmts.continueStmt("continue;");
        Stmts.continueStmt("continue outer;");

        Stmts.constructorCallStmt("this(1);");
        st = Stmts.of((Integer n) -> {
            do {
                assert (1 == 1);
            }
            while (n > 2);
        });

        assertEquals(st, Stmts.doStmt("do{ assert(1==1); }while(n>2);"));

        assertEquals(Stmts.of((Integer a) -> a = 2 + 45), Stmts.exprStmt("a = 2 + 45;"));


        //Stmt.expressionStmt("a = 2 + 45;");
        assertEquals(Stmts.of((Integer[] aas) -> {
                    for (int a : aas) {
                        System.out.println(a);
                    }
                }),
                Stmts.forEachStmt("for(int a : aas){ System.out.println(a); }"));

        assertEquals(Stmts.of(() -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(i);
                    }
                }),

                Stmts.forStmt("for(int i=0;i<100;i++){ System.out.println(i);}"));

        assertEquals(
                Stmts.of((Boolean isReady) -> {
                    if (isReady) {
                        System.out.println("Its ready");
                    }
                }),
                Stmts.ifStmt("if( isReady ){",
                        "System.out.println(\"Its ready\");",
                        "}"));


        assertEquals(
                Stmts.of(() -> {
                    class F {
                        int i;
                    }
                }),
                Stmts.localClassStmt("class F{ int i; }"));

        assertEquals(Stmts.of(() -> {
                    label:
                    System.out.println(1);
                }),
                Stmts.labeledStmt("label: System.out.println(1);"));

        Stmts.returnStmt("return 1;");

        //Stmt.switchCaseStmt("case 1: System.out.println(1); break;");

        assertEquals(Stmts.switchStmt("switch(x){",
                "default: System.out.println(1);",
                "break;",
                "}"), Stmts.of((Integer x) -> {
            switch (x) {
                default:
                    System.out.println(1);
                    break;
            }
        }));

        assertEquals(
                Stmts.synchronizedStmt("synchronized(s){ s++; }"),
                Stmts.of((Integer s) -> {
                    synchronized (s) {
                        s++;
                    }
                }));

        assertEquals(
                Stmts.tryStmt("try{ f.writeExternal(oos); } catch(IOException ioe){}"),
                Stmts.of((Externalizable f, ObjectOutputStream oos) -> {
                    try {
                        f.writeExternal(oos);
                    } catch (IOException ioe) {

                    }
                }));

        assertEquals(
                Stmts.throwStmt("throw new RuntimeException();"),
                Stmts.of(() -> {
                    throw new RuntimeException();
                }));

        assertEquals(Stmts.whileStmt("while(b){",
                "System.out.println(1);",
                "}"), Stmts.of((Boolean b) -> {
            while (b) {
                System.out.println(1);
            }
        }));

        as = Stmts.assertStmt("/**",
                " * Javadoc Comment",
                " */",
                "assert (true);");
        assertTrue(as.getComment().isPresent());
        assertEquals("/**" + System.lineSeparator() +
                " * Javadoc Comment" + System.lineSeparator() +
                " */" + System.lineSeparator() +
                "assert (true);", as.toString());
    }


        //BlockStmt bss = JavaParser.parseBlock( "/**"+System.lineSeparator()+
        //        " * Javadoc Comment"+System.lineSeparator()+
        //        " */"+System.lineSeparator()+
        //        "{ block(a); }");

        //assertTrue(bss.getComment().isPresent());
        //System.out.println( bss );

        //BlockStmt bs = Stmt.block("{ block(a);}" );

        //bs = Stmt.block("/**",
        //        " * Javadoc Comment",
        //        " */",
        //        "{ block(a); }");

        /** ok this is a known problem
        assertEquals( "/**"+System.lineSeparator()+
                " * Javadoc Comment"+System.lineSeparator()+
                //" * /" +System.lineSeparator()+
                //"{"+System.lineSeparator()+
                //"    block(a);" +System.lineSeparator()+
                //"}", bs.toString());
        */
        //BreakStmt bo = Stmt.breakStmt("/* break outer */break outer;");
        //assertTrue(bo.getComment().isPresent());

    //}

    /**
     * If we try to pass a Commented Statement to JavaParser.parseStatement(...),
     * we have issues (it places the comment WITHIN the Statement between expressions)
     *
     * So we modified Stmt.of(...) to parse Statements within a Block and then extract it
     * which correctly puts the Comment BEFORE the statement
     */
    public void testWithComments(){
        Statement st = Stmts.of("// Line Comment", "assert (true);");
        assertEquals( "// Line Comment"+System.lineSeparator()+"assert (true);", st.toString());

        //System.out.println( st );
        st = Stmts.of("/* Block Comment */", "assert (true);");
        assertEquals( "/* Block Comment */"+System.lineSeparator()+"assert (true);", st.toString());
        //System.out.println( st );

        st = Stmts.of("/**",
                " * Javadoc Comment",
                " */",
                "assert (true);");
        assertEquals( "/**"+System.lineSeparator()+
                " * Javadoc Comment"+System.lineSeparator()+
                " */"+System.lineSeparator()+
                "assert (true);", st.toString());

        //System.out.println( st );
    }

}
