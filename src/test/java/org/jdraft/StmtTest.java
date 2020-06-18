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
public class StmtTest extends TestCase {

    public void testPreviousStmtNextStmt(){

        class C{
            void m(){
                assert(1==1);
                assert(2==2);
                assert(3==3);
            }
        }
        _class _c = _class.of(C.class);
        _method _m = _c.firstMethodNamed("m");

        assertEquals( null, Stmt.previous( _m.getAstStatement(0)));
        assertEquals( Stmt.of("assert(1==1);"), Stmt.previous( _m.getAstStatement(1)));
        assertEquals( Stmt.of("assert(2==2);"), Stmt.previous( _m.getAstStatement(2)));

        assertEquals( Stmt.of("assert(2==2);"), Stmt.next( _m.getAstStatement(0)));
        assertEquals( Stmt.of("assert(3==3);"), Stmt.next( _m.getAstStatement(1)));
        assertEquals( null, Stmt.next( _m.getAstStatement(2)));

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

        LabeledStmt first = (LabeledStmt) ((_labeledStmt) ($.labeledStmt().firstIn(_c))).node();

        Stmt.addStatementsBefore(first, Stmt.of( ()->System.out.println(1) ) );
        Stmt.addStatementsAfter(first, Stmt.of( ()->System.out.println(2)));
        assertEquals( Stmt.of(()->System.out.println(1)), _c.firstMethodNamed("m").getAstStatement(0));
        assertEquals( Stmt.of("firstStmt:{}"), _c.firstMethodNamed("m").getAstStatement(1));
        assertEquals( Stmt.of(()->System.out.println(2)), _c.firstMethodNamed("m").getAstStatement(2));

        LabeledStmt second = (LabeledStmt) ((_labeledStmt)$.labeledStmt("secondStmt:{}").firstIn(_c)).node();
        Stmt.addStatementsBefore(second, Stmt.of( ()->System.out.println(1) ) );
        Stmt.addStatementsAfter(second, Stmt.of( ()->System.out.println(2)));

        assertEquals( Stmt.of("firstStmt:{}"), _c.firstMethodNamed("m2").getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(1)), _c.firstMethodNamed("m2").getAstStatement(1));
        assertEquals( Stmt.of("secondStmt:{}"), _c.firstMethodNamed("m2").getAstStatement(2));
        assertEquals( Stmt.of(()->System.out.println(2)), _c.firstMethodNamed("m2").getAstStatement(3));
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

        LabeledStmt first = ((_labeledStmt)$.labeledStmt().firstIn(_c)).node();

        //NOTE: we cant create/initialize more than one Statement *via Lambda) on the same line because of how stack traces work
        Stmt.addStatementsBefore(first, Stmt.of( ()->System.out.println(1) ),
                Stmt.of( ()->System.out.println(2) ) );
        Stmt.addStatementsAfter(first, Stmt.of( ()->System.out.println(3)),
                Stmt.of( ()->System.out.println(4)) );

        //System.out.println( _c );

        assertEquals( Stmt.of(()->System.out.println(1)), _c.firstMethodNamed("m").getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(2)), _c.firstMethodNamed("m").getAstStatement(1));
        assertEquals( Stmt.of("firstStmt:{}"), _c.firstMethodNamed("m").getAstStatement(2));
        assertEquals( Stmt.of(()->System.out.println(3)), _c.firstMethodNamed("m").getAstStatement(3));
        assertEquals( Stmt.of(()->System.out.println(4)), _c.firstMethodNamed("m").getAstStatement(4));

        LabeledStmt second = (LabeledStmt) ((_labeledStmt)$.labeledStmt("secondStmt:{}").firstIn(_c)).node();
        Stmt.addStatementsBefore(second, Stmt.of( ()->System.out.println(1) ),
                Stmt.of( ()->System.out.println(2)));

        Stmt.addStatementsAfter(second, Stmt.of( ()->System.out.println(3)),
                Stmt.of( ()->System.out.println(4)));

        assertEquals( Stmt.of("firstStmt:{}"), _c.firstMethodNamed("m2").getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(1)), _c.firstMethodNamed("m2").getAstStatement(1));
        assertEquals( Stmt.of(()->System.out.println(2)), _c.firstMethodNamed("m2").getAstStatement(2));
        assertEquals( Stmt.of("secondStmt:{}"), _c.firstMethodNamed("m2").getAstStatement(3));
        assertEquals( Stmt.of(()->System.out.println(3)), _c.firstMethodNamed("m2").getAstStatement(4));
        assertEquals( Stmt.of(()->System.out.println(4)), _c.firstMethodNamed("m2").getAstStatement(5));
    }

    public void testReturnStmtLambda(){
        assertEquals( Stmt.of("return 1;") , Stmt.returnStmt(()-> 1));
        assertEquals( Stmt.of("return \"String\";"), Stmt.returnStmt(()-> "String"));
        assertEquals( Stmt.of("return $name$;"), Stmt.returnStmt((String $name$)-> {
            return $name$;
        }));
    }

    public void testDoStmtLambda(){
        DoStmt ds = Stmt.doStmt( ()->{
            do{
                System.out.println(1);
            }while(true);
        });

        assertEquals( Stmt.of( ()->System.out.println(1)), ds.getBody().asBlockStmt().getStatement(0));
        assertTrue( ds.getCondition().asBooleanLiteralExpr().getValue());

        ds = Stmt.doStmt( (Integer a) ->{
            do{
                System.out.println(a);
            } while( a != null );
        });

        assertEquals( Stmt.of( (a)->System.out.println(a)), ds.getBody().asBlockStmt().getStatement(0));
        assertEquals(BinaryExpr.Operator.NOT_EQUALS, ds.getCondition().asBinaryExpr().getOperator());
    }
    //Predicate<_field>, $snip

    public void testBlock(){
        //single stmt
        BlockStmt bs = Stmt.blockStmt( ()-> System.out.println(1));

        assertEquals(1, bs.getStatements().size());
        bs = Stmt.blockStmt( ()-> {
            System.out.println(1);
            System.out.println(2);
        });
        assertEquals(2, bs.getStatements().size());

        //ananoymous TYPE var
        bs = Stmt.blockStmt( (o)->System.out.println(o));

        //typed var
        bs = Stmt.blockStmt( (Integer i)->System.out.println(i));
        assertEquals(Stmt.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );

        //typed vars
        bs = Stmt.blockStmt( (Integer i, String s)->{
            System.out.println(i);
            System.out.println(s);
        });
        assertEquals(Stmt.of( (Integer i)->System.out.println(i)), bs.getStatement(0) );
        assertEquals(Stmt.of( (Integer s)->System.out.println(s)), bs.getStatement(1) );

        //untyped vars
        bs = Stmt.blockStmt( (a, b, c, d)->{
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
        } );
        assertEquals(Stmt.of( (a)->System.out.println(a)), bs.getStatement(0) );
        assertEquals(Stmt.of( (b)->System.out.println(b)), bs.getStatement(1) );
        assertEquals(Stmt.of( (c)->System.out.println(c)), bs.getStatement(2) );
        assertEquals(Stmt.of( (d)->System.out.println(d)), bs.getStatement(3) );

        //what about a list of
        Predicate<_field> pf = (f)-> f.isPublic();
        Predicate<_field> ff = (f)-> f.isType(int.class);
    }

    public void testAllStmtsLambdaWithComments() {
        //make sure we can create statements with comments
        Statement st = Stmt.of(() -> { /** Comment */assert true;
        });
        assertEquals(new JavadocComment(" Comment "), st.getComment().get());
        assertEquals(Stmt.of("assert true;"), st.removeComment());

        st = Stmt.of(() -> { /* Comment */
            assert true;
        });
        assertEquals(new BlockComment(" Comment "), st.getComment().get());
        assertEquals(Stmt.of("assert true;"), st.removeComment());

        //System.out.println(st);
        st = Stmt.of(() -> { // Comment
            assert true;
        });
        assertEquals(new LineComment(" Comment"), st.getComment().get());
        assertEquals(Stmt.of("assert true;"), st.removeComment());
        //System.out.println(st);
    }

    public void testSuperCtorBody(){
        BlockStmt bs = Stmt.blockStmt("{ super(); }");
        //System.out.println( bs );

        //StaticJavaParser.parseStatement( "{ super(); }" );
    }
    public void testBody(){
        Statement st = Stmt.of("this.name = name;");
        System.out.println( st );
    }
    public void testStmtTypesWithLambda(){
        AssertStmt as = Stmt.assertStmt("assert 1==1;");
        as = (AssertStmt) Stmt.of(() -> {
            assert (1 == 1);
        });

        BlockStmt bs = Stmt.blockStmt("{ block(); }");
        bs = (BlockStmt) Stmt.of(() -> {
            System.out.println(1);
            System.out.println(2);
        });

        Statement st = Stmt.of(() -> System.out.println(1));
        assertTrue(st.isExpressionStmt());
        assertEquals(Stmt.of("System.out.println(1);"), st);

        BreakStmt bss = Stmt.breakStmt("break;");
        //bss = Stmt.of( ()-> { break; }  );
        //bss = Stmt.of( ()-> { continue; }  );

        Stmt.breakStmt("break outer;");
        Stmt.continueStmt("continue;");
        Stmt.continueStmt("continue outer;");

        Stmt.constructorCallStmt("this(1);");
        st = Stmt.of((Integer n) -> {
            do {
                assert (1 == 1);
            }
            while (n > 2);
        });

        assertEquals(st, Stmt.doStmt("do{ assert(1==1); }while(n>2);"));

        assertEquals(Stmt.of((Integer a) -> a = 2 + 45), Stmt.expressionStmt("a = 2 + 45;"));


        //Stmt.expressionStmt("a = 2 + 45;");
        assertEquals(Stmt.of((Integer[] aas) -> {
                    for (int a : aas) {
                        System.out.println(a);
                    }
                }),
                Stmt.forEachStmt("for(int a : aas){ System.out.println(a); }"));

        assertEquals(Stmt.of(() -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(i);
                    }
                }),

                Stmt.forStmt("for(int i=0;i<100;i++){ System.out.println(i);}"));

        assertEquals(
                Stmt.of((Boolean isReady) -> {
                    if (isReady) {
                        System.out.println("Its ready");
                    }
                }),
                Stmt.ifStmt("if( isReady ){",
                        "System.out.println(\"Its ready\");",
                        "}"));


        assertEquals(
                Stmt.of(() -> {
                    class F {
                        int i;
                    }
                }),
                Stmt.localClassDeclarationStmt("class F{ int i; }"));

        assertEquals(Stmt.of(() -> {
                    label:
                    System.out.println(1);
                }),
                Stmt.labeledStmt("label: System.out.println(1);"));

        Stmt.returnStmt("return 1;");

        //Stmt.switchCaseStmt("case 1: System.out.println(1); break;");

        assertEquals(Stmt.switchStmt("switch(x){",
                "default: System.out.println(1);",
                "break;",
                "}"), Stmt.of((Integer x) -> {
            switch (x) {
                default:
                    System.out.println(1);
                    break;
            }
        }));

        assertEquals(
                Stmt.synchronizedStmt("synchronized(s){ s++; }"),
                Stmt.of((Integer s) -> {
                    synchronized (s) {
                        s++;
                    }
                }));

        assertEquals(
                Stmt.tryStmt("try{ f.writeExternal(oos); } catch(IOException ioe){}"),
                Stmt.of((Externalizable f, ObjectOutputStream oos) -> {
                    try {
                        f.writeExternal(oos);
                    } catch (IOException ioe) {

                    }
                }));

        assertEquals(
                Stmt.throwStmt("throw new RuntimeException();"),
                Stmt.of(() -> {
                    throw new RuntimeException();
                }));

        assertEquals(Stmt.whileStmt("while(b){",
                "System.out.println(1);",
                "}"), Stmt.of((Boolean b) -> {
            while (b) {
                System.out.println(1);
            }
        }));

        as = Stmt.assertStmt("/**",
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
        Statement st = Stmt.of("// Line Comment", "assert (true);");
        assertEquals( "// Line Comment"+System.lineSeparator()+"assert (true);", st.toString());

        //System.out.println( st );
        st = Stmt.of("/* Block Comment */", "assert (true);");
        assertEquals( "/* Block Comment */"+System.lineSeparator()+"assert (true);", st.toString());
        //System.out.println( st );

        st = Stmt.of("/**",
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
