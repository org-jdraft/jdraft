package test.byexample;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft.*;

import java.io.IOException;
import java.util.function.Predicate;

public class AutoCompleteTest extends TestCase {

    public void testBuildField(){
        FieldDeclaration fd = new FieldDeclaration().setPublic(true).addVariable(new VariableDeclarator().setName("i").setType(PrimitiveType.intType()).setInitializer(new IntegerLiteralExpr("0")));
        _field _f = _field.of("public int i=0");
    }

    public void testBuildLambda(){

        //target
        // (Integer a)-> System.out.println(a);
        LambdaExpr l = new LambdaExpr(new Parameter(StaticJavaParser.parseType("Integer"), "a"), new BlockStmt().addStatement(StaticJavaParser.parseStatement("System.out.println(a);") ) ).setEnclosingParameters(true);


        //LambdaExpr le = new LambdaExpr();
        //le.setEnclosingParameters(true); //todo
        //le.addParameter(new Parameter(StaticJavaParser.parseType("Integer"), "a"));
        //le.setBody( StaticJavaParser.parseStatement("System.out.println(a);"));

        System.out.println( l );

        //target
        // (Integer a)-> System.out.println(a);
        _lambda _l = _lambda.of( (Integer a)-> { System.out.println(a); });


        assertEquals( l, _l.ast() );

    }

    public void testKeyPressesAndSaccades(){
        //keypresses & saccades
        // public int i = 0;
        //
        FieldDeclaration fd = new FieldDeclaration().setPublic(true).addVariable(new VariableDeclarator().setName("i").setType(PrimitiveType.intType()).setInitializer(new IntegerLiteralExpr("0")));
       //------*^^^^^^^^^----------*^^^^^^^^^^^^^^^^-----*^^^^^-*^^^--^^^^^^^^^^^


       //  7
        _field _f = _field.of("public int i=0");

        //modify and return chain
        _field _f2 = _field.of().setPublic().setTypeRef(int.class).setName("i").setInit(0);

        assertEquals( fd, _f.getFieldDeclaration());
        assertEquals( fd, _f2.getFieldDeclaration());

        assertEquals( fd.toString(), _f.toString());
        assertEquals( fd.toString(), _f2.toString());
      //----------------------------------------

    }
    // Java Asts, the easy way
    //the general idea is:
    // if your JOB (8+ hours a day, 5 days a week) was to build, inspect, manipulate & test Java code via ASTs
    // ... you'd probably build a tool for that to make you more productive/efficient

    // Defining "Better" along these dimensions:
    // Familiarity - Its just Java code, and Objects runs like Java code, "feels" like Java (names should map 1 to 1)
    // Approachability - "Time for a complete novice to build a non-trivial & useful program with it"
    // Brevity- "... is the soul of wit" (Polonius in Hamlet)
    // Consistency - Learn how one model entity behaves & you understand how like model entities behave
    // Readability - "Eliminate the unnecessary so that the necessary may speak" (Hans Hofmann)
    // Maintainability - "How easy would it be for a complete novice to maintain a program you wrote (years ago)?"
    // Testability - "How hard is it to test the code (that I just built/modified)?"

    // Sacrifices
    // Speed / Performance - for optimal performance, you could spin up Bytecodes
    //                       sometimes there is "double work" that is inefficient
    public void testProgressiveDisclosureAutocompleteAPI(){


        // assuming you automatically imported org.jdraft.*;
        // when you type '_' you are given an autocomplete menu
        // that contains the relevant classes for building jdraft models
        // (Progressive disclosure) the prefix _ this already limits "what you want"
        // i.e. how the autocomplete list will fill and help you
        //
        // _
        // _f
        // _f....
        // _field
        // _field _f
        // _field _f = _field.of("int i=0;");
        // what you typed:
        // _fi    _f = _fi       "int i=0;"
           _field _f = _field.of("public int i=0;");


        //sometimes autocomplete (for standard terms like Type) uses the WRONG thing
        // i.e. when I was doing this "Type" autocompleted to "import jdk.internal.org.objectweb.asm.Type;"
        // the same goes for Field, Parameter, etc.... because jdraft uses the '_' prefix it can disambiguate
        // alternatively the constructor doesn't help autocomplete()
        FieldDeclaration fd = new FieldDeclaration()
                //it takes alot of "work"/typing to represent a fairly simplistic AST,
                // and the editor cant help you much
                .addVariable(new VariableDeclarator(PrimitiveType.intType(), "i", new IntegerLiteralExpr("0")) )
                .setPublic(true);

        //doing this, while more brief is fine, but if you are a novice, you probably
        //dont know to call parseBodyDeclaration and also to typeCast to a FieldDeclaration
        FieldDeclaration fd2 = (FieldDeclaration)StaticJavaParser.parseBodyDeclaration("public int i=0;");
        System.out.println( _f );
        System.out.println( fd );
        System.out.println( fd2 );
    }

    /*
    public void testLambda(){
        LambdaExpr le = (LambdaExpr) StaticJavaParser.parseExpression("()->{"+System.lineSeparator()+"}");
        System.out.println( le.getRange().get() );
        System.out.println( "TR" + le.getTokenRange().get() );

        LambdaExpr le2 = new LambdaExpr();
        le2.setBody( StaticJavaParser.parseBlock("{ System.out.println(1); }"));
        System.out.println( le2.getRange().get() );
        System.out.println( "TR" + le2.getTokenRange().get() );
    }
     */

    // Verbosity
    // Approachability - "Time for a complete novice to build a non-trivial & useful program with it"
    // Verbosity - "Brevity is the soul of wit" (Polonius in Hamlet)
    // Readability - "Eliminate the unnecessary so that the necessary may speak" (Hans Hofmann)
    // Maintainability - "How easy would it be for a complete novice to maintain a program you wrote (years ago)?"
    // Testability - "How hard is it to test the code (that I just built/modified)?"
    public void testLambdaApi(){

        // building the code with a lambda body
        // + has all the benefits of code highlighting and autocomplete
        // + is easier to read and change
        _forStmt _fs = _forStmt.of( ()-> {
           for(int i=0; i< 100; i++){ System.out.println( " i = "+ i); }
        });

        //lots of work compared to above
        //this is verbose
        ForStmt fs = new ForStmt();

        // int i=0
        NodeList<Expression> init = new NodeList<>();
        VariableDeclarationExpr vde = new VariableDeclarationExpr();
        vde.addVariable( new VariableDeclarator().setName("i").setType(PrimitiveType.intType()).setInitializer( new IntegerLiteralExpr("0")) );
        init.add( vde ); //what is "int i=0"
        fs.setInitialization( init );

        //i < 100
        fs.setCompare( new BinaryExpr().setLeft(new NameExpr("i")).setOperator(BinaryExpr.Operator.LESS).setRight( new IntegerLiteralExpr("100")) );

        // i++
        NodeList<Expression> upd = new NodeList<>();
        upd.add( new UnaryExpr());
        fs.setUpdate(upd);

        //building a String literal to represent code, is cumbersome and hard to read and debug
        ForStmt fs3 = (ForStmt)StaticJavaParser.parseStatement("for(int i=0;i<100;i++){ System.out.println(\" i = \" + i); }");
    }

    enum E{
        A, B, C, D;
    }

    public void testEnumSwitch(){
         _switchStmt _ss = _switchStmt.of( (E e)-> {
             switch(e){
                 case A: return 99;
                 case B: return 100;
                 case C:
                 case D: return 102;

                 default: return 101;
             }
         });

         _caseGroup _cg = _ss.listCaseGroups().get(0);
         _cg.listCases().forEach(cc -> System.out.println(" "+cc+ " "+cc.getClass()));
         System.out.println( _cg.listCases() );

         _cg = _ss.getCaseGroup( E.C);
         assertTrue( _cg.hasCase(E.C)); //verify C and D are in the same group
         assertTrue(_cg.hasCase(E.D));

         System.out.println( _ss.getCaseGroup(E.C));

         assertNotNull( _ss.getCaseGroup(E.A) );

         System.out.println( _ss.getCaseGroup(E.A));

         _switchEntry _se  = _ss.getCase(E.A); //gets the case based on the

         System.out.println(_se );


         assertNotNull( _se );
    }

    //name addressing and traversing can be difficult
    public void testTestability(){

        _switchStmt _s = _switchStmt.of( (Integer a)->{
            switch(a){
                case 1: System.out.println("ONE");
                default : System.out.println( "Not One");
            }
        });

        _s.is(s -> s.countSwitchEntries() == 2);
        _s.is(s-> _s.getCase(1) != null);


    }
    public void testT(){
        assertTrue(_class.of("C").listAllJavadocComments().isEmpty());
        assertFalse(_char.of('c').is( c->Character.isUpperCase(c.getValue()) ));

        assertTrue(_catch.of("catch(IOException ioe){}").isParameter(p-> p.isTypeRef(IOException.class)));

        assertTrue(_forStmt.of(()-> {
            for(int i=0;i<100; i++){ } })
                .getCompare().is("i < 100")); //isCompare

        assertTrue(_forStmt.of(()-> {
            for(int i=0;i<100; i++){ }
        }).isCompare("i < 100")); //isCompare

        assertTrue(_forStmt.of(()-> {
            for(int i=0;i<100; i++){
                System.out.println("For Statement");
            }
        }).isCompare(c-> c instanceof _binaryExpression )); //isCompare

        assertEquals(1, _forStmt.of(()-> { for(int i=0;i<100; i++){} })
                .listUpdates(_unary.class)
                .size());
    }
}
