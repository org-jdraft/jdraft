package org.jdraft;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

/**
 * You probably don't need to know about all nodes and how they work,
 * but you likely have a job to do with some of the nodes, so this
 * should familiarize yourself with the names and parts of code they
 * refer to.
 *
 * What are these things
 * How are they organized & fit together
 *
 * Which Abstraction
 * This associating an abstraction with examples
 *
 * (Lexicon)
 */
public class _allNodeUseTest extends TestCase {

    public void testNodesAlphabetical(){
        //_java._domain is the top-level interface for all instance AND interface abstractions
        _java._domain[] _instances = {
                _anno.of("@A"),
                _annos.of("@A @B"),
                _annotation.of("@interface AI{}"),
                _args.of("(1, 'c')"),
                _arrayAccessExpr.of("a[1][call()]"),
                _newArrayExpr.of("new a[1][2]"),
                _arrayDimension.of("[0]"),
                _arrayInitExpr.of("{1,2,3}"),
                _assertStmt.of("assert true;"),
                _assignExpr.of("x = 1"),
                _binaryExpr.of("a && b"),
                _blockStmt.of("{ print(120); }"),
                _blockComment.of("first line", "second line"),
                _body.of("print(3);"),
                _booleanExpr.of("true"),
                _breakStmt.of("break outer;"),
                _switchCases.of("case 1: case 3: case 5: return ODD;"),
                _castExpr.of("(String)s"),
                _catch.of("catch(IOException ioe){ throw new RuntimeException(ioe); }"),
                _charExpr.of('a'), // or "'c'"
                _class.of("class C{}"),
                _classExpr.of("String.class"),
                _ternaryExpr.of("b==0?x:y"), //also called "ternary"
                _constant.of("A(100)"), //enum constant declaration
                _constructor.of("C(int a){ this.a = a; }"),
                _constructorCallStmt.of("this(100);"),
                _constructorCallStmt.of("super(100);"),
                _continueStmt.of("continue inner;"),
                _doStmt.of("do{ i++; }while(i < 100);"),
                _doubleExpr.of(1.2d), //this is for double precision doubles
                _doubleExpr.of(1.2f), //this is for double precision floats too
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
                _expr.of( "1+2" ), //_expression will create any expression type
                _parenthesizedExpr.of("(1 + 2)"),
                _enum.of("enum E{}"),
                _exprStmt.of("System.out.println(1);"),
                _exprStmt.of(_methodCallExpr.of("print(1)")),
                _field.of("public int i;"),
                _fieldAccessExpr.of("System.out"),
                _forEachStmt.of("for(Object o: objects) { print(o); }"),
                _forStmt.of("for(int i=0;i<100;i++){ print(i); }"),
                _ifStmt.of("if(a==b){ print(1); }"),
                _import.of("java.util.*"),
                _import.of(Map.class),
                _imports.of("import java.util.*;", "import java.net.*;"),
                _initBlock.of("static{ a = 1000; }"),
                _initBlock.of("{ print(state); }"),
                _instanceOfExpr.of("A instanceof Map"),
                _intExpr.of("37"),
                _intExpr.of(37),
                _interface.of("interface I{}"),
                _javadocComment.of("/** a javadoc */"),
                _labeledStmt.of("outer: print(state);"),
                _lambdaExpr.of("o-> System.out.println(1)"),
                _lineComment.of("// TODO remove this"),
                _localClassStmt.of("class Local{ int i=100; }"),
                _variablesExpr.of("int i, j = 100"),
                _longExpr.of("-1L"),
                _method.of("int m(){ return 56; }"),
                _methodCallExpr.of("call(120);"),
                _methodRefExpr.of("Context::getState"),
                _modifier.of("public"),
                _modifiers.of("public static final"),

                _moduleInfo.of("module org.jdraft.mod { exports org.jdraft.mod; }"),
                _moduleDirective.of("opens shiny;"),
                _moduleExports.of("exports underdog;"),
                _moduleUses.of("uses underdog;"),
                _moduleOpens.of("opens another.module;"),
                _moduleRequires.of("requires static alpha.beta;"),
                _moduleProvides.of("provides monitor.AClass with monitor.AFactoryClass"),

                _name.of("x"),
                _nameExpr.of("x"),
                _newExpr.of("new RuntimeException()"),
                _nullExpr.of(), //the null literal
                _package.of("package org.jdraft;"),
                _packageInfo.of("/** information about jdraft */package org.jdraft;"),
                _param.of("final int i"),
                _params.of("(@ann int x, String...names)"),
                _receiverParam.of("@AnnotatedUsage Currency this"),
                _returnStmt.of("return 12;"),
                _stmt.of("System.out.println(1);"), //_statement creates any statement type
                _stringExpr.of("A String literal"),
                _superExpr.of("super"),
                _switchCase.of("case 1: return 3;"),
                _switchExpr.of("switch(val){ case 1: yield 'a'; }"),
                _switchStmt.of("switch(e){ case 1: return 'a'; default: return 'c';}"),
                _synchronizedStmt.of("synchronized(a){ }"),
                _textBlockExpr.of("first line", "second line", "third line"),
                _thisExpr.of("World.this"),
                _throws.of("MyException"),
                _throws.of(RuntimeException.class),
                _throwStmt.of("throw new RuntimeException();"),
                _tryStmt.of("try{ Files.read(fileName); } catch(IOException ioe){ }"),
                _typeArgs.of("<T,R>"),
                _typeExpr.of("World"), //In <code>World::greet</code> the "World" is a TypeExpr
                _typeParam.of("K"),
                _typeParams.of("<String,Integer>"),
                _typeRef.of("List<String>"),
                _unaryExpr.of("!true"),
                _variable.of("int i;"),
                _whileStmt.of("while(a<100){ i+=a; a++; }"),
                _yieldStmt.of("yield 3;")
         };

        //the interfaces also extend _java._domain
        _java._domain[] _interfaces ={
                _expr.of("1 + 3"),
                (_expr._literal) _intExpr.of(23),
                _stmt.of("assert true;"),
                _type.of("class C{}"),
                (_codeUnit) _moduleInfo.of(),
                (_expr._literal) _intExpr.of(1)
        };
        Arrays.stream(_instances).forEach(i-> {
            if( i instanceof _tree._node) {
                Print.tree((_tree._node) i);
            }
        } );
    }

    public void testPrintTree(){

    }

    public void testStatementInstances(){
        _stmt[] _stmts = {
                _assertStmt.of("assert true;"),
                _blockStmt.of("{ print(120); }"),
                _breakStmt.of("break outer;"),
                _constructorCallStmt.of("this(100);"),
                _constructorCallStmt.of("super(100);"),
                _continueStmt.of("continue inner;"),
                _doStmt.of("do{ i++; }while(i < 100);"),
                _emptyStmt.of(),
                _exprStmt.of("System.out.println(1);"),
                _exprStmt.of(_methodCallExpr.of("print(1)")),
                _forEachStmt.of("for(Object o: objects) { print(o); }"),
                _forStmt.of("for(int i=0;i<100;i++){ print(i); }"),
                _ifStmt.of("if(a==b){ print(1); }"),
                _labeledStmt.of("outer: print(state);"),
                _localClassStmt.of("class Local{ int i=100; }"),
                _returnStmt.of("return 12;"),
                _switchStmt.of("switch(e){ case 1: return 'a'; default: return 'c';}"),
                _synchronizedStmt.of("synchronized(a){ }"),
                _throwStmt.of("throw new RuntimeException();"),
                _tryStmt.of("try{ Files.read(fileName); } catch(IOException ioe){ }"),
                _whileStmt.of("while(a<100){ i+=a; a++; }"),
                _yieldStmt.of("yield 3;")};

        _stmt _stmt = org.jdraft._stmt.of( "assert(true);"); //returns an AssertStmt instance
    }

    public void testSetBasedInstances(){
        //semantically, order doesnt matter
        _tree._group[] setBased = {
                _annos.of("@A @B"),
                _imports.of( "import java.util.*;", "import java.net.*;"),
                _variablesExpr.of("int i, j = 100"),
                _modifiers.of("public static final"),
                _throws.of(IOException.class, URISyntaxException.class),
                _typeArgs.of("<T,R>"),
                _typeParams.of("<String,Integer>")
        };
    }

    public void testListBasedInstances(){
        //semantically order matters
        _tree._orderedGroup[] ls = {
                _args.of("(1, 'c')"),
                _params.of("(@ann int x, String...names)")
        };
    }

    public void testTypes(){
        _type[] _ts = {
                _class.of("class C{}"),
                _interface.of("interface I{}"),
                _enum.of("enum E{}"),
                _annotation.of("@interface A{}")
        };
    }

    public void testCodeUnits(){
        _codeUnit[] _cus = {
                _class.of("class C{}"),
                _interface.of("interface I{}"),
                _enum.of("enum E{}"),
                _annotation.of("@interface A{}"),
                _packageInfo.of("package aaaa.bbbb.C;"),
                _moduleInfo.of("module org.mod { }")
        };
    }

    public void testExpressionTypes(){
        //if you want to construct any _expression type, you can pass in a String
        _expr _ex = _expr.of("1"); //will return _int instance

        //each precise _expression type, has an "of()..." static method for building the _expression
        _expr[] _es = {
                _anno.of("@A"),
                _arrayAccessExpr.of("a[1][call()]"),
                _newArrayExpr.of("new a[1][2]"),
                _arrayInitExpr.of("{1,2,3}"),
                _assignExpr.of("x = 1"),
                _binaryExpr.of("a && b"),
                _booleanExpr.of("true"),
                _castExpr.of("(String)s"),
                _charExpr.of('a'),
                _classExpr.of("String.class"),
                _ternaryExpr.of("b==0?x:y"), //also called "ternary"
                _doubleExpr.of(1.2d), //this is for double precision doubles
                _doubleExpr.of(1.2f), //this is for double precision floats too
                _parenthesizedExpr.of("(1 + 2)"),
                _fieldAccessExpr.of("System.out"),
                _instanceOfExpr.of("A instanceof Map"),
                _intExpr.of("37"),
                _lambdaExpr.of( "o-> System.out.println(1)"),
                _longExpr.of("-1L"),
                _methodCallExpr.of("call(120);"),
                _methodRefExpr.of("Context::getState"),
                _nameExpr.of("x"),
                _newExpr.of("new RuntimeException()"),
                _nullExpr.of("null"),
                _stringExpr.of("A String literal"),
                _superExpr.of("super"),
                _switchExpr.of("switch(val){ case 1: yield 'a'; }"),
                _textBlockExpr.of("first line", "second line", "third line"),
                _thisExpr.of("World.this"),
                _typeExpr.of("World"),
                _unaryExpr.of("!true")
        };
    }
}
