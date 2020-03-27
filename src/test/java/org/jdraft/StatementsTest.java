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
public class StatementsTest extends TestCase {

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

        assertEquals( null, Statements.previous( _m.getStatement(0)));
        assertEquals( Statements.of("assert(1==1);"), Statements.previous( _m.getStatement(1)));
        assertEquals( Statements.of("assert(2==2);"), Statements.previous( _m.getStatement(2)));

        assertEquals( Statements.of("assert(2==2);"), Statements.next( _m.getStatement(0)));
        assertEquals( Statements.of("assert(3==3);"), Statements.next( _m.getStatement(1)));
        assertEquals( null, Statements.next( _m.getStatement(2)));

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

        LabeledStmt first = $.labeledStmt().firstIn(_c).ast();

        Statements.addStatementsBefore(first, Statements.of( ()->System.out.println(1) ) );
        Statements.addStatementsAfter(first, Statements.of( ()->System.out.println(2)));
        assertEquals( Statements.of(()->System.out.println(1)), _c.getMethod("m").getStatement(0));
        assertEquals( Statements.of("firstStmt:{}"), _c.getMethod("m").getStatement(1));
        assertEquals( Statements.of(()->System.out.println(2)), _c.getMethod("m").getStatement(2));

        LabeledStmt second = $.labeledStmt("secondStmt:{}").firstIn(_c).ast();
        Statements.addStatementsBefore(second, Statements.of( ()->System.out.println(1) ) );
        Statements.addStatementsAfter(second, Statements.of( ()->System.out.println(2)));

        assertEquals( Statements.of("firstStmt:{}"), _c.getMethod("m2").getStatement(0));
        assertEquals( Statements.of(()->System.out.println(1)), _c.getMethod("m2").getStatement(1));
        assertEquals( Statements.of("secondStmt:{}"), _c.getMethod("m2").getStatement(2));
        assertEquals( Statements.of(()->System.out.println(2)), _c.getMethod("m2").getStatement(3));
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

        LabeledStmt first = $.labeledStmt().firstIn(_c).ast();

        //NOTE: we cant create/initialize more than one Statement *via Lambda) on the same line because of how stack traces work
        Statements.addStatementsBefore(first, Statements.of( ()->System.out.println(1) ),
                Statements.of( ()->System.out.println(2) ) );
        Statements.addStatementsAfter(first, Statements.of( ()->System.out.println(3)),
                Statements.of( ()->System.out.println(4)) );

        //System.out.println( _c );

        assertEquals( Statements.of(()->System.out.println(1)), _c.getMethod("m").getStatement(0));
        assertEquals( Statements.of(()->System.out.println(2)), _c.getMethod("m").getStatement(1));
        assertEquals( Statements.of("firstStmt:{}"), _c.getMethod("m").getStatement(2));
        assertEquals( Statements.of(()->System.out.println(3)), _c.getMethod("m").getStatement(3));
        assertEquals( Statements.of(()->System.out.println(4)), _c.getMethod("m").getStatement(4));

        LabeledStmt second = $.labeledStmt("secondStmt:{}").firstIn(_c).ast();
        Statements.addStatementsBefore(second, Statements.of( ()->System.out.println(1) ),
                Statements.of( ()->System.out.println(2)));

        Statements.addStatementsAfter(second, Statements.of( ()->System.out.println(3)),
                Statements.of( ()->System.out.println(4)));

        assertEquals( Statements.of("firstStmt:{}"), _c.getMethod("m2").getStatement(0));
        assertEquals( Statements.of(()->System.out.println(1)), _c.getMethod("m2").getStatement(1));
        assertEquals( Statements.of(()->System.out.println(2)), _c.getMethod("m2").getStatement(2));
        assertEquals( Statements.of("secondStmt:{}"), _c.getMethod("m2").getStatement(3));
        assertEquals( Statements.of(()->System.out.println(3)), _c.getMethod("m2").getStatement(4));
        assertEquals( Statements.of(()->System.out.println(4)), _c.getMethod("m2").getStatement(5));
    }

    public void testReturnStmtLambda(){
        assertEquals( Statements.of("return 1;") , Statements.returnStmt(()-> 1));
        assertEquals( Statements.of("return \"String\";"), Statements.returnStmt(()-> "String"));
        assertEquals( Statements.of("return $name$;"), Statements.returnStmt((String $name$)-> {
            return $name$;
        }));
    }

    public void testDoStmtLambda(){
        DoStmt ds = Statements.doStmt( ()->{
            do{
                System.out.println(1);
            }while(true);
        });

        assertEquals( Statements.of( ()->System.out.println(1)), ds.getBody().asBlockStmt().getStatement(0));
        assertTrue( ds.getCondition().asBooleanLiteralExpr().getValue());

        ds = Statements.doStmt( (Integer a) ->{
            do{
                System.out.println(a);
            } while( a != null );
        });

        assertEquals( Statements.of( (a)->System.out.println(a)), ds.getBody().asBlockStmt().getStatement(0));
        assertEquals(BinaryExpr.Operator.NOT_EQUALS, ds.getCondition().asBinaryExpr().getOperator());
    }
    //Predicate<_field>, $snip

    public void testBlock(){
        //single stmt
        BlockStmt bs = Statements.blockStmt( ()-> System.out.println(1));

        assertEquals(1, bs.getStatements().size());
        bs = Statements.blockStmt( ()-> {
            System.out.println(1);
            System.out.println(2);
        });
        assertEquals(2, bs.getStatements().size());

        //ananoymous TYPE var
        bs = Statements.blockStmt( (o)->System.out.println(o));

        //typed var
        bs = Statements.blockStmt( (Integer i)->System.out.println(i));
        assertEquals(Statements.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );

        //typed vars
        bs = Statements.blockStmt( (Integer i, String s)->{
            System.out.println(i);
            System.out.println(s);
        });
        assertEquals(Statements.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );
        assertEquals(Statements.of( (Integer s)->System.out.println(s)), bs.getStatement(1) );

        //untyped vars
        bs = Statements.blockStmt( (a, b, c, d)->{
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
        } );
        assertEquals(Statements.of( (a)->System.out.println(a)), bs.getStatement(0) );
        assertEquals(Statements.of( (b)->System.out.println(b)), bs.getStatement(1) );
        assertEquals(Statements.of( (c)->System.out.println(c)), bs.getStatement(2) );
        assertEquals(Statements.of( (d)->System.out.println(d)), bs.getStatement(3) );

        //what about a list of
        Predicate<_field> pf = (f)-> f.isPublic();
        Predicate<_field> ff = (f)-> f.isTypeRef(int.class);
    }

    public void testAllStmtsLambdaWithComments() {
        //make sure we can create statements with comments
        Statement st = Statements.of(() -> { /** Comment */assert true;
        });
        assertEquals(new JavadocComment(" Comment "), st.getComment().get());
        assertEquals(Statements.of("assert true;"), st.removeComment());

        st = Statements.of(() -> { /* Comment */
            assert true;
        });
        assertEquals(new BlockComment(" Comment "), st.getComment().get());
        assertEquals(Statements.of("assert true;"), st.removeComment());

        //System.out.println(st);
        st = Statements.of(() -> { // Comment
            assert true;
        });
        assertEquals(new LineComment(" Comment"), st.getComment().get());
        assertEquals(Statements.of("assert true;"), st.removeComment());
        //System.out.println(st);
    }

    public void testSuperCtorBody(){
        BlockStmt bs = Statements.blockStmt("{ super(); }");
        //System.out.println( bs );

        //StaticJavaParser.parseStatement( "{ super(); }" );
    }
    public void testBody(){
        Statement st = Statements.of("this.name = name;");
        System.out.println( st );
    }
    public void testStmtTypesWithLambda(){
        AssertStmt as = Statements.assertStmt("assert 1==1;");
        as = (AssertStmt) Statements.of(() -> {
            assert (1 == 1);
        });

        BlockStmt bs = Statements.blockStmt("{ block(); }");
        bs = (BlockStmt) Statements.of(() -> {
            System.out.println(1);
            System.out.println(2);
        });

        Statement st = Statements.of(() -> System.out.println(1));
        assertTrue(st.isExpressionStmt());
        assertEquals(Statements.of("System.out.println(1);"), st);

        BreakStmt bss = Statements.breakStmt("break;");
        //bss = Stmt.of( ()-> { break; }  );
        //bss = Stmt.of( ()-> { continue; }  );

        Statements.breakStmt("break outer;");
        Statements.continueStmt("continue;");
        Statements.continueStmt("continue outer;");

        Statements.constructorCallStmt("this(1);");
        st = Statements.of((Integer n) -> {
            do {
                assert (1 == 1);
            }
            while (n > 2);
        });

        assertEquals(st, Statements.doStmt("do{ assert(1==1); }while(n>2);"));

        assertEquals(Statements.of((Integer a) -> a = 2 + 45), Statements.expressionStmt("a = 2 + 45;"));


        //Stmt.expressionStmt("a = 2 + 45;");
        assertEquals(Statements.of((Integer[] aas) -> {
                    for (int a : aas) {
                        System.out.println(a);
                    }
                }),
                Statements.forEachStmt("for(int a : aas){ System.out.println(a); }"));

        assertEquals(Statements.of(() -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(i);
                    }
                }),

                Statements.forStmt("for(int i=0;i<100;i++){ System.out.println(i);}"));

        assertEquals(
                Statements.of((Boolean isReady) -> {
                    if (isReady) {
                        System.out.println("Its ready");
                    }
                }),
                Statements.ifStmt("if( isReady ){",
                        "System.out.println(\"Its ready\");",
                        "}"));


        assertEquals(
                Statements.of(() -> {
                    class F {
                        int i;
                    }
                }),
                Statements.localClassStmt("class F{ int i; }"));

        assertEquals(Statements.of(() -> {
                    label:
                    System.out.println(1);
                }),
                Statements.labeledStmt("label: System.out.println(1);"));

        Statements.returnStmt("return 1;");

        //Stmt.switchCaseStmt("case 1: System.out.println(1); break;");

        assertEquals(Statements.switchStmt("switch(x){",
                "default: System.out.println(1);",
                "break;",
                "}"), Statements.of((Integer x) -> {
            switch (x) {
                default:
                    System.out.println(1);
                    break;
            }
        }));

        assertEquals(
                Statements.synchronizedStmt("synchronized(s){ s++; }"),
                Statements.of((Integer s) -> {
                    synchronized (s) {
                        s++;
                    }
                }));

        assertEquals(
                Statements.tryStmt("try{ f.writeExternal(oos); } catch(IOException ioe){}"),
                Statements.of((Externalizable f, ObjectOutputStream oos) -> {
                    try {
                        f.writeExternal(oos);
                    } catch (IOException ioe) {

                    }
                }));

        assertEquals(
                Statements.throwStmt("throw new RuntimeException();"),
                Statements.of(() -> {
                    throw new RuntimeException();
                }));

        assertEquals(Statements.whileStmt("while(b){",
                "System.out.println(1);",
                "}"), Statements.of((Boolean b) -> {
            while (b) {
                System.out.println(1);
            }
        }));

        as = Statements.assertStmt("/**",
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
        Statement st = Statements.of("// Line Comment", "assert (true);");
        assertEquals( "// Line Comment"+System.lineSeparator()+"assert (true);", st.toString());

        //System.out.println( st );
        st = Statements.of("/* Block Comment */", "assert (true);");
        assertEquals( "/* Block Comment */"+System.lineSeparator()+"assert (true);", st.toString());
        //System.out.println( st );

        st = Statements.of("/**",
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
