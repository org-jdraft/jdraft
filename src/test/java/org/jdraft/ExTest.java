package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.UnknownType;
import junit.framework.TestCase;

import java.util.*;

/**
 *
 * @author Eric
 */
//@Ast.cache
public class ExTest extends TestCase {

    public void testSwitchExpr(){
        String[] sy ={
                "switch (mode) {",
                "    case \"a\", \"b\":",
                "        yield 1;",
                "    case \"c\":",
                "        yield 2;",
                "    case \"d\", \"e\", \"f\":",
                "        yield 3;",
                "    default:",
                "        yield -1;",
                "}"};

        SwitchExpr se = Expr.switchExpr(sy);
        assertEquals( 4, se.getEntries().size());
        assertEquals( 2, se.getEntry(0).getLabels().size());
        assertEquals( 1, se.getEntry(1).getLabels().size());
        assertEquals( 3, se.getEntry(2).getLabels().size());
    }
    /**
     * Test shortcuts for building Array initializer Expressions
     * of Arrays with varargs
     */
    public void testArrayEx(){

        assertEquals(Expr.of(new int[]{1,2,3,4,5}), Expr.of(1,2,3,4,5));
        assertEquals(Expr.of(new boolean[]{true, false, true}), Expr.of(true,false,true));
        assertEquals(Expr.of(new float[]{1.0f, 2.0f}), Expr.of(1.0f,2.0f));
        assertEquals(Expr.of(new char[]{'a','b','c'}), Expr.of('a','b','c'));
        assertEquals(Expr.of(new double[]{1.0d, 2.0d, 3.0d}), Expr.of(1.0d,2.0d,3.0d));
        assertEquals(Expr.of("{\"A\", \"B\", \"C\"}"), Expr.stringArrayInitExpr("A","B","C") );
    }

    public void testExprThousandsSeparatorHexBinary(){
        StaticJavaParser.parseExpression("1_000");
        StaticJavaParser.parseExpression("0xDEADBEEF");
        StaticJavaParser.parseExpression("0b110100111");

        assertTrue( Expr.JAVAPARSER.parseExpression("1_000").isSuccessful());
        assertTrue( Expr.JAVAPARSER.parseExpression("0xDEADBEEF").isSuccessful());
        assertTrue( Expr.JAVAPARSER.parseExpression("0b110100111").isSuccessful());
        Ast.expression("1_000");
        Ast.expression("0xDEADBEEF");
        Ast.expression("0b110100111");
        
        Expr.intExpr("1_000");
        Expr.intExpr("0xDEADBEEF");
        Expr.intExpr("0b110100111");

        LongLiteralExpr l = Expr.longExpr("1_000_000L");
        LongLiteralExpr ll = Expr.longExpr("1000000L");
        
        //this is a direct syntax comparison
        assertFalse( l.equals(11));
        
        //this is a "semantic" equality comparison
        assertTrue( Expr.equal(l, ll) );
        
        //this is interesting, because syntactically they ARE equal
        // but semantically they are not
        //assertEquals( l, ll);
        
        
    }
    public void testByteShort(){
        StaticJavaParser.parseExpression("(byte)b");
        StaticJavaParser.parseExpression("(short)s");
    }
    
    public void testUnary(){
        UnaryExpr ue = Expr.unaryExpr( ()->!true );
        assertEquals( Expr.of("!true"), ue);
        
        Expr.instanceOfExpr( ($any$)-> $any$ instanceof String );
    }
    
    
    public void testExp(){
        class Outer{
            class $prefix{
                class G{
                    public int i=0;
                }
            }
        }
        
        _class _c = _class.of(Outer.class);
        _c.ast().walk(s -> System.out.println( s) );
        
        Name n = StaticJavaParser.parseName("Outer.$prefix.G");
        
        
                
        System.out.println( n );
    }
    
    public void testObjectCreation(){
        ObjectCreationExpr oce = Expr.newExpr( ()-> new HashMap() );
        assertEquals( oce, Expr.newExpr("new HashMap()"));
        
        assertEquals( Expr.of("List.of(1,2)"), Expr.methodCallExpr("List.of(1,2);"));
    }
    
    public void testArrayExpr(){
        Expr.of(new int[]{1,2,3});
        Expr.of(new float[]{1.0f,2.0f,3.0f});
        Expr.of(new double[]{1.0,2.0,3.0});
        Expr.of(new boolean[]{true});
        Expr.of(new char[]{'a'});
    }
    
    public void testRuntimeAnonymousClass(){
        ObjectCreationExpr oce = Expr.newExpr(new Object(){
            int x,y,z;
        });
        assertTrue( oce.getAnonymousClassBody().get().get(0) instanceof FieldDeclaration );

        oce = Expr.newExpr(
                /** INTENTIONALLY BLANK */
                /** INTENTIONALLY BLANK */
                /** INTENTIONALLY BLANK */
                /** INTENTIONALLY BLANK */
                /** INTENTIONALLY BLANK */
                /** INTENTIONALLY BLANK */
                new Object(){
            int x,y,z;
        });
        assertTrue( oce.getAnonymousClassBody().get().get(0) instanceof FieldDeclaration );
    }


    public void testRuntimeLambda(){
        //System.out.println( _io.describe() );
        LambdaExpr le = Expr.lambdaExpr( ()-> System.out.println(1) );


        /** I need to get rid of the whole space requirement */
        le = Expr
                .lambdaExpr(
                //intentionally
                //intentionally
                //intentionally
                //blank
                ()-> System.out.println(1) );


    }

    /*
    public void testRuntimeLambdaFailure(){
        _io.clearThreadLocalInResolver();

        _io.setThreadLocalInResolver( new _in._resolver() {
            public _in resolve(String sourceId) {
                return null;
            }
            public _in resolve(Class clazz) {
                return null;
            }
            public String describe() {
                return "Null Resolver";
            }
        });

        try{
            LambdaExpr le = Expr.lambda( ()-> System.out.println(1) );
            fail("expected failure ");
        }catch(DraftException de){
            System.out.println( de );
        }
        finally {
            _io.clearThreadLocalInResolver();
        }
    }
    */

    public @interface MyAnn{

    }

    public void testLambda(){
        //no args
        LambdaExpr le = Expr.of( ()-> {assert true;} );
        assertTrue( le.getParameters().isEmpty() );
        assertEquals( Stmt.of( ()->{assert true;}), le.getBody().asBlockStmt().getStatement(0));

        //unknown TYPE args
        le = Expr.lambdaExpr( "(x) ->System.out.println(x)" );
        assertTrue( le.getParameters().get(0).getType() instanceof UnknownType);
        assertEquals( "x", le.getParameters().get(0).getNameAsString());

        le = Expr.lambdaExpr( (Integer x) ->System.out.println(x) );

        //typed args
        le = Expr.of( (String s)->System.out.println(s) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());

        //multiple args
        //le = Expr.of( (Integer i, String s, Boolean b)->System.out.println(s + " "+ i +" "+ b) );
        le = Expr.of( (Integer i, String s, Boolean b)->System.out.println(s + " "+ i +" "+ b) );

        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "i", le.getParameters().get(0).getNameAsString());
        assertEquals( "Integer", le.getParameters().get(0).getTypeAsString());

        assertTrue( le.getParameters().get(1).getType().isClassOrInterfaceType() );
        assertEquals( "s", le.getParameters().get(1).getNameAsString());
        assertEquals( "String", le.getParameters().get(1).getTypeAsString());


        assertTrue( le.getParameters().get(2).getType().isClassOrInterfaceType() );
        assertEquals( "b", le.getParameters().get(2).getNameAsString());
        assertEquals( "Boolean", le.getParameters().get(2).getTypeAsString());

        //multiple statements (block Stmt)
        //le = Expr.of( (Integer i)->{
        le = Expr.of( (Integer i)->{
            assert i > 1;
            System.out.println(i);
        } );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "i", le.getParameters().get(0).getNameAsString());
        assertEquals( "Integer", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getBody().isBlockStmt() );
        assertEquals( Stmt.of((Integer i)->{assert i > 1;}), le.getBody().asBlockStmt().getStatement(0));
        assertEquals( Stmt.of((Integer i)->{System.out.println(i);}), le.getBody().asBlockStmt().getStatement(1));

        //varargs
        le = Expr.of( (String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //ANNOTATIONS
        le = Expr.of( (@MyAnn String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertTrue( le.getParameters().get(0).getAnnotationByClass(MyAnn.class).isPresent());
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //final
        le = Expr.of( (@MyAnn final String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertTrue( le.getParameters().get(0).isFinal());
        assertTrue( le.getParameters().get(0).getAnnotationByClass(MyAnn.class).isPresent());
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //BODY comments

        le = Expr.of( ()->{
            /** JAVADOC comment */
            System.out.println(1);
            /* comment */
            System.out.println(2);
            // line comment
            System.out.println(3);
        });

        List<Comment> cs = le.getAllContainedComments();

        assertEquals( Stmt.of( () ->/** JAVADOC comment */ System.out.println(1) ), le.getBody().asBlockStmt().getStatement(0 ));
        assertEquals( Stmt.of( () ->/* comment */ System.out.println(2) ), le.getBody().asBlockStmt().getStatement(1 ));
        assertEquals( Stmt.of( () ->// line comment
                System.out.println(3) ), le.getBody().asBlockStmt().getStatement(2 ));

        assertEquals( 3, cs.size() );
        assertEquals( new JavadocComment(" JAVADOC comment "), cs.get(0) );
        assertEquals( new BlockComment(" comment "), cs.get(1));
        assertEquals( new LineComment(" line comment"), cs.get(2));
    }

    //well... I want to
    public void testLiterals(){
        //binary, hex
        assertEquals( Expr.of("0b0011"), Expr.of("0b0011"));
        assertEquals( Expr.of("0x0011"), Expr.of("0x0011"));

        assertEquals( Expr.of(0b0011), Expr.of(0b0011));

        //long
        assertEquals( Expr.of(123456789l), Expr.of(123456789L));
        //LongLiteralExpr ll1 = Expr.longLiteral("123L" );
        //LongLiteralExpr ll2 = Expr.longLiteral("123l" );
        //assertEquals( ll1.asLong(), ll2.asLong());
        //assertEquals( ll1, ll2);
        //assertEquals( Expr.of("123456789l"), Expr.of("123456789L"));

        //float
        assertEquals( Expr.of(3.14f), Expr.of(3.14F));
        assertEquals( Expr.of("3.14f"), Expr.of("3.14f"));
        //assertEquals( Expr.of("3.14f"), Expr.of("3.14F"));

        //double
        assertEquals( Expr.of("3.14"), Expr.of("3.14"));
        //assertEquals( Expr.of("3.14d"), Expr.of("3.14D"));
        //assertEquals( Expr.of(3.14f), Expr.of("3.14f"));
    }
}

