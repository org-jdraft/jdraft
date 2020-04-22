package org.jdraft;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * You probably dont need to know about all nodes and how they work,
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


    //all
    public void testNodesAlphabetical(){
        //_java._domain is the top-level interface for all instance AND interface abstractions
        _java._domain[] _instances = {
                _anno.of("@A"),
                _annos.of("@A @B"),
                _annotation.of("@interface AI{}"),
                _arguments.of("(1, 'c')"),
                _arrayAccess.of("a[1][call()]"),
                _arrayCreate.of("new a[1][2]"),
                _arrayDimension.of("[0]"),
                _arrayInitialize.of("{1,2,3}"),
                _assertStmt.of("assert true;"),
                _assign.of("x = 1"),
                _binaryExpression.of("a && b"),
                _blockStmt.of("{ print(120); }"),
                _blockComment.of("first line", "second line"),
                _body.of("print(3);"),
                _boolean.of("true"),
                _breakStmt.of("break outer;"),
                _caseGroup.of("case 1: case 3: case 5: return ODD;"),
                _cast.of("(String)s"),
                _catch.of("catch(IOException ioe){ throw new RuntimeException(ioe); }"),
                _char.of('a'), // or "'c'"
                _class.of("class C{}"),
                _classExpression.of("String.class"),
                _conditionalExpression.of("b==0?x:y"), //also called "ternary"
                _constant.of("A(100)"), //enum constant declaration
                _constructor.of("C(int a){ this.a = a; }"),
                _constructorCallStmt.of("this(100);"),
                _constructorCallStmt.of("super(100);"),
                _continueStmt.of("continue inner;"),
                _doStmt.of("do{ i++; }while(i < 100);"),
                _double.of(1.2d), //this is for double precision doubles
                _double.of(1.2f), //this is for double precision floats too
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
                _enclosedExpression.of("(1 + 2)"),
                _enum.of("enum E{}"),
                _expressionStmt.of("System.out.println(1);"),
                _expressionStmt.of(_methodCall.of("print(1)")),
                _field.of("public int i;"),
                _fieldAccess.of("System.out"),
                _forEachStmt.of("for(Object o: objects) { print(o); }"),
                _forStmt.of("for(int i=0;i<100;i++){ print(i); }"),
                _ifStmt.of("if(a==b){ print(1); }"),
                _import.of("java.util.*"),
                _import.of(Map.class),
                _imports.of("import java.util.*;", "import java.net.*;"),
                _initBlock.of("static{ a = 1000; }"),
                _initBlock.of("{ print(state); }"),
                _instanceOf.of("A instanceof Map"),
                _int.of("37"),
                _int.of(37),
                _interface.of("interface I{}"),
                _javadocComment.of("/** a javadoc */"),
                _labeledStmt.of("outer: print(state);"),
                _lambda.of("o-> System.out.println(1)"),
                _lineComment.of("// TODO remove this"),
                _localClassStmt.of("class Local{ int i=100; }"),
                _localVariables.of("int i, j = 100"),
                _long.of("-1L"),
                _method.of("int m(){ return 56; }"),
                _methodCall.of("call(120);"),
                _methodRef.of("Context::getState"),
                _modifier.of("public"),
                _modifiers.of("public static final"),
                _moduleInfo.of("module org.jdraft.mod { exports org.jdraft.mod; }"),
                _name.of("x"),
                _nameExpression.of("x"),
                _new.of("new RuntimeException()"),
                _null.of("null"),
                _package.of("package org.jdraft;"),
                _packageInfo.of("/** information about jdraft */package org.jdraft;"),
                _parameter.of("final int i"),
                _parameters.of("(@ann int x, String...names)"),
                _qualifiedName.of("org.jdraft._class"),
                _receiverParameter.of("@AnnotatedUsage Currency this"),
                _returnStmt.of("return 12;"),
                _string.of("A String literal"),
                _super.of("super"),
                _switchEntry.of("case 1: return 3;"),
                _switchExpression.of("switch(val){ case 1: yield 'a'; }"),
                _switchStmt.of("switch(e){ case 1: return 'a'; default: return 'c';}"),
                _synchronizedStmt.of("synchronized(a){ }"),
                _textBlock.of("first line", "second line", "third line"),
                _this.of("World.this"),
                _throws.of("MyException"),
                _throws.of(RuntimeException.class),
                _throwStmt.of("throw new RuntimeException();"),
                _tryStmt.of("try{ Files.read(fileName); } catch(IOException ioe){ }"),
                _typeArguments.of("<T,R>"),
                _typeExpression.of("World"), //In <code>World::greet</code> the "World" is a TypeExpr
                _typeParameter.of("K"),
                _typeParameters.of("<String,Integer>"),
                _typeRef.of("List<String>"),
                _unary.of("!true"),
                _variable.of("int i;"),
                _whileStmt.of("while(a<100){ i+=a; a++; }"),
                _yieldStmt.of("yield 3;")
         };

        //the interfaces also extend _java._domain
        _java._domain[] _interfaces ={
                _expression.of("1 + 3"),
                (_expression._literal) _int.of(23),
                _statement.of("assert true;"),
                _type.of("class C{}"),
                (_codeUnit) _moduleInfo.of(),
                (_expression._literal) _int.of(1)
        };
    }

    public void testStatementInstances(){
        _statement[] _stmts = {
                _assertStmt.of("assert true;"),
                _blockStmt.of("{ print(120); }"),
                _breakStmt.of("break outer;"),
                _constructorCallStmt.of("this(100);"),
                _constructorCallStmt.of("super(100);"),
                _continueStmt.of("continue inner;"),
                _doStmt.of("do{ i++; }while(i < 100);"),
                _emptyStmt.of(),
                _expressionStmt.of("System.out.println(1);"),
                _expressionStmt.of(_methodCall.of("print(1)")),
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

        _statement _stmt = _statement.of( "assert(true);"); //returns an AssertStmt instance
    }

    public void testSetBasedInstances(){
        //semantically, order doesnt matter
        _java._set[] setBased = {
                _annos.of("@A @B"),
                _imports.of( "import java.util.*;", "import java.net.*;"),
                _localVariables.of("int i, j = 100"),
                _modifiers.of("public static final"),
                _throws.of(IOException.class, URISyntaxException.class),
                _typeArguments.of("<T,R>"),
                _typeParameters.of("<String,Integer>")
        };
    }

    public void testListBasedInstances(){
        //semantically order matters
        _java._list[] ls = {
                _arguments.of("(1, 'c')"),
                _parameters.of("(@ann int x, String...names)")
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
        _expression _ex = _expression.of("1"); //will return _int instance

        //each precise _expression type, has an "of()..." static method for building the _expression
        _expression[] _es = {
                _anno.of("@A"),
                _arrayAccess.of("a[1][call()]"),
                _arrayCreate.of("new a[1][2]"),
                _arrayInitialize.of("{1,2,3}"),
                _assign.of("x = 1"),
                _binaryExpression.of("a && b"),
                _boolean.of("true"),
                _cast.of("(String)s"),
                _char.of('a'),
                _classExpression.of("String.class"),
                _conditionalExpression.of("b==0?x:y"), //also called "ternary"
                _double.of(1.2d), //this is for double precision doubles
                _double.of(1.2f), //this is for double precision floats too
                _enclosedExpression.of("(1 + 2)"),
                _fieldAccess.of("System.out"),
                _instanceOf.of("A instanceof Map"),
                _int.of("37"),
                _lambda.of( "o-> System.out.println(1)"),
                _long.of("-1L"),
                _methodCall.of("call(120);"),
                _methodRef.of("Context::getState"),
                _nameExpression.of("x"),
                _new.of("new RuntimeException()"),
                _null.of("null"),
                _string.of("A String literal"),
                _super.of("super"),
                _switchExpression.of("switch(val){ case 1: yield 'a'; }"),
                _textBlock.of("first line", "second line", "third line"),
                _this.of("World.this"),
                _typeExpression.of("World"),
                _unary.of("!true")
        };
    }
}
