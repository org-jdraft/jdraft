package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

public class OfStringTest extends TestCase {
    /**
     * The of(String) method just means parse the String and return the underlying model
     */
    public void testBuildOfString() {
        _java._domain[] _instances = {
                //building and returning implementations of interfaces:
                _expr.of("1 + 2"),
                _stmt.of("System.out.println(1);"),
                _type.of("class C{", "int i;", "}"), //infers a line separator between each string
                _codeUnit.of("module org.astro{", "exports org.astro;","}"),

                _annoExpr.of("@A"),
                _annoExprs.of("@A @B"),
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
                _caseGroup.of("case 1: case 3: case 5: return ODD;"),
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
                _doubleExpr.of("1.2d"), //this is for double precision doubles
                _doubleExpr.of("1.2f"), //this is for double precision floats too
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
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
                _imports.of("import java.util.*;", "import java.net.*;"),
                _initBlock.of("static{ a = 1000; }"),
                _initBlock.of("{ print(state); }"),
                _instanceOfExpr.of("A instanceof Map"),
                _intExpr.of("37"),
                _interface.of("interface I{}"),
                _javadocComment.of("/** a javadoc */"),
                _labeledStmt.of("outer: print(state);"),
                _lambdaExpr.of("o-> System.out.println(1)"),
                _lineComment.of("// TODO remove this"),
                _localClassStmt.of("class Local{ int i=100; }"),
                _variablesExpr.of("int i, j = 100"),
                _longExpr.of("-1L"),
                _method.of("int m(){ return 56; }"),
                _methodCallExpr.of("call(120)"),
                _methodRefExpr.of("Context::getState"),
                _modifier.of("public"),
                _modifiers.of("public static final"),
                _moduleInfo.of("module org.jdraft.mod { exports org.jdraft.mod; }"),
                _name.of("x"),
                _nameExpr.of("x"),
                _newExpr.of("new RuntimeException()"),
                _nullExpr.of("null"),
                _package.of("package org.jdraft;"),
                _packageInfo.of("/** information about jdraft */package org.jdraft;"),
                _param.of("final int i"),
                _params.of("(@ann int x, String...names)"),
                _qualifiedName.of("org.jdraft._class"),
                _receiverParam.of("@AnnotatedUsage Currency this"),
                _returnStmt.of("return 12;"),
                _stringExpr.of("A String literal"),
                _superExpr.of("super"),
                _switchEntry.of("case 1: return 3;"),
                _switchExpr.of("switch(val){ case 1: yield 'a'; }"),
                _switchStmt.of("switch(e){ case 1: return 'a'; default: return 'c';}"),
                _synchronizedStmt.of("synchronized(a){ }"),
                _textBlockExpr.of("first line", "second line", "third line"),
                _thisExpr.of("World.this"),
                _throws.of("MyException"),
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
    }

    /**
     * entities can be built with an <CODE>.of(String...)</CODE> static method
     */
    public void testBuiltOfString(){

        //building and returning implementations of interfaces:
        _expr _e = _expr.of("1 + 2");
        _stmt _s = _stmt.of("System.out.println(1);");
        _type _t = _type.of("class C{", "int i;", "}"); //infers a line separator between each string
        _codeUnit _cu = _codeUnit.of("module org.astro{", "exports org.astro;","}");

        //building and returning specific things:
        _binaryExpr _be = _binaryExpr.of("1 + 2");
        _exprStmt _es = _exprStmt.of("System.out.println(1);");
        _class _c = _class.of("class C{", "int i;", "}");
        _moduleInfo _mi = _moduleInfo.of("module org.astro{", "exports org.astro;","}");

        //
        _typeRef _tr = _typeRef.of("List<String>");

    }
}
