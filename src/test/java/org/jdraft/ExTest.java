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

        SwitchExpr se = Expressions.switchEx(sy);
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

        assertEquals(Expressions.of(new int[]{1,2,3,4,5}), Expressions.intArray(1,2,3,4,5));
        assertEquals(Expressions.of(new boolean[]{true, false, true}), Expressions.booleanArray(true,false,true));
        assertEquals(Expressions.of(new float[]{1.0f, 2.0f}), Expressions.floatArray(1.0f,2.0f));
        assertEquals(Expressions.of(new char[]{'a','b','c'}), Expressions.charArray('a','b','c'));
        assertEquals(Expressions.of(new double[]{1.0d, 2.0d, 3.0d}), Expressions.doubleArray(1.0d,2.0d,3.0d));
        assertEquals(Expressions.of("{\"A\", \"B\", \"C\"}"), Expressions.stringArray("A","B","C") );
    }

    public void testExprThousandsSeparatorHexBinary(){
        StaticJavaParser.parseExpression("1_000");
        StaticJavaParser.parseExpression("0xDEADBEEF");
        StaticJavaParser.parseExpression("0b110100111");
        
        Ast.ex("1_000");
        Ast.ex("0xDEADBEEF");
        Ast.ex("0b110100111");
        
        Expressions.intLiteralEx("1_000");
        Expressions.intLiteralEx("0xDEADBEEF");
        Expressions.intLiteralEx("0b110100111");

        LongLiteralExpr l = Expressions.longLiteralEx("1_000_000L");
        LongLiteralExpr ll = Expressions.longLiteralEx("1000000L");
        
        //this is a direct syntax comparison
        assertFalse( l.equals(11));
        
        //this is a "semantic" equality comparison
        assertTrue( Expressions.equivalent(l, ll) );
        
        //this is interesting, because syntactically they ARE equal
        // but semantically they are not
        //assertEquals( l, ll);
        
        
    }
    public void testByteShort(){
        StaticJavaParser.parseExpression("(byte)b");
        StaticJavaParser.parseExpression("(short)s");
    }
    
    public void testUnary(){
        UnaryExpr ue = Expressions.unaryEx( ()->!true );
        assertEquals( Expressions.of("!true"), ue);
        
        Expressions.instanceOfEx( ($any$)-> $any$ instanceof String );
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
        ObjectCreationExpr oce = Expressions.newEx( ()-> new HashMap() );
        assertEquals( oce, Expressions.newEx("new HashMap()"));
        
        assertEquals( Expressions.of("List.of(1,2)"), Expressions.methodCallEx("List.of(1,2);"));
    }
    
    public void testArrayExpr(){
        Expressions.of(new int[]{1,2,3});
        Expressions.of(new float[]{1.0f,2.0f,3.0f});
        Expressions.of(new double[]{1.0,2.0,3.0});
        Expressions.of(new boolean[]{true});
        Expressions.of(new char[]{'a'});
    }
    
    public void testRuntimeAnonymousClass(){
        ObjectCreationExpr oce = Expressions.newEx(new Object(){
            int x,y,z;
        });
        assertTrue( oce.getAnonymousClassBody().get().get(0) instanceof FieldDeclaration );

        oce = Expressions.newEx(
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
        LambdaExpr le = Expressions.lambdaEx( ()-> System.out.println(1) );


        /** I need to get rid of the whole space requirement */
        le = Expressions
                .lambdaEx(
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
        LambdaExpr le = Expressions.of( ()-> {assert true;} );
        assertTrue( le.getParameters().isEmpty() );
        assertEquals( Statements.of( ()->{assert true;}), le.getBody().asBlockStmt().getStatement(0));

        //unknown TYPE args
        le = Expressions.lambdaEx( "(x) ->System.out.println(x)" );
        assertTrue( le.getParameters().get(0).getType() instanceof UnknownType);
        assertEquals( "x", le.getParameters().get(0).getNameAsString());

        le = Expressions.lambdaEx( (Integer x) ->System.out.println(x) );

        //typed args
        le = Expressions.of( (String s)->System.out.println(s) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());

        //multiple args
        //le = Expr.of( (Integer i, String s, Boolean b)->System.out.println(s + " "+ i +" "+ b) );
        le = Expressions.of( (Integer i, String s, Boolean b)->System.out.println(s + " "+ i +" "+ b) );

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
        le = Expressions.of( (Integer i)->{
            assert i > 1;
            System.out.println(i);
        } );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "i", le.getParameters().get(0).getNameAsString());
        assertEquals( "Integer", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getBody().isBlockStmt() );
        assertEquals( Statements.of((Integer i)->{assert i > 1;}), le.getBody().asBlockStmt().getStatement(0));
        assertEquals( Statements.of((Integer i)->{System.out.println(i);}), le.getBody().asBlockStmt().getStatement(1));

        //varargs
        le = Expressions.of( (String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //ANNOTATIONS
        le = Expressions.of( (@MyAnn String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertTrue( le.getParameters().get(0).getAnnotationByClass(MyAnn.class).isPresent());
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //final
        le = Expressions.of( (@MyAnn final String... s)->System.out.println(Arrays.toString(s)) );
        assertTrue( le.getParameters().get(0).getType().isClassOrInterfaceType() );
        assertTrue( le.getParameters().get(0).isFinal());
        assertTrue( le.getParameters().get(0).getAnnotationByClass(MyAnn.class).isPresent());
        assertEquals( "s", le.getParameters().get(0).getNameAsString());
        assertEquals( "String", le.getParameters().get(0).getTypeAsString());
        assertTrue( le.getParameters().get(0).isVarArgs());

        //BODY comments

        le = Expressions.of( ()->{
            /** JAVADOC comment */
            System.out.println(1);
            /* comment */
            System.out.println(2);
            // line comment
            System.out.println(3);
        });

        List<Comment> cs = le.getAllContainedComments();

        assertEquals( Statements.of( () ->/** JAVADOC comment */ System.out.println(1) ), le.getBody().asBlockStmt().getStatement(0 ));
        assertEquals( Statements.of( () ->/* comment */ System.out.println(2) ), le.getBody().asBlockStmt().getStatement(1 ));
        assertEquals( Statements.of( () ->// line comment
                System.out.println(3) ), le.getBody().asBlockStmt().getStatement(2 ));

        assertEquals( 3, cs.size() );
        assertEquals( new JavadocComment(" JAVADOC comment "), cs.get(0) );
        assertEquals( new BlockComment(" comment "), cs.get(1));
        assertEquals( new LineComment(" line comment"), cs.get(2));
    }

    //well... I want to
    public void testLiterals(){
        //binary, hex
        assertEquals( Expressions.of("0b0011"), Expressions.of("0b0011"));
        assertEquals( Expressions.of("0x0011"), Expressions.of("0x0011"));

        assertEquals( Expressions.of(0b0011), Expressions.of(0b0011));

        //long
        assertEquals( Expressions.of(123456789l), Expressions.of(123456789L));
        //LongLiteralExpr ll1 = Expr.longLiteral("123L" );
        //LongLiteralExpr ll2 = Expr.longLiteral("123l" );
        //assertEquals( ll1.asLong(), ll2.asLong());
        //assertEquals( ll1, ll2);
        //assertEquals( Expr.of("123456789l"), Expr.of("123456789L"));

        //float
        assertEquals( Expressions.of(3.14f), Expressions.of(3.14F));
        assertEquals( Expressions.of("3.14f"), Expressions.of("3.14f"));
        //assertEquals( Expr.of("3.14f"), Expr.of("3.14F"));

        //double
        assertEquals( Expressions.of("3.14"), Expressions.of("3.14"));
        //assertEquals( Expr.of("3.14d"), Expr.of("3.14D"));
        //assertEquals( Expr.of(3.14f), Expr.of("3.14f"));
    }
}

