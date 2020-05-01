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
                _expression.of("1 + 2"),
                _statement.of("System.out.println(1);"),
                _type.of("class C{", "int i;", "}"), //infers a line separator between each string
                _codeUnit.of("module org.astro{", "exports org.astro;","}"),

                _annoRef.of("@A"),
                _annoRefs.of("@A @B"),
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
                _double.of("1.2d"), //this is for double precision doubles
                _double.of("1.2f"), //this is for double precision floats too
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
                _imports.of("import java.util.*;", "import java.net.*;"),
                _initBlock.of("static{ a = 1000; }"),
                _initBlock.of("{ print(state); }"),
                _instanceOf.of("A instanceof Map"),
                _int.of("37"),
                _interface.of("interface I{}"),
                _javadocComment.of("/** a javadoc */"),
                _labeledStmt.of("outer: print(state);"),
                _lambda.of("o-> System.out.println(1)"),
                _lineComment.of("// TODO remove this"),
                _localClassStmt.of("class Local{ int i=100; }"),
                _localVariables.of("int i, j = 100"),
                _long.of("-1L"),
                _method.of("int m(){ return 56; }"),
                _methodCall.of("call(120)"),
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
    }

    /**
     * entities can be built with an <CODE>.of(String...)</CODE> static method
     */
    public void testBuiltOfString(){

        //building and returning implementations of interfaces:
        _expression _e = _expression.of("1 + 2");
        _statement _s = _statement.of("System.out.println(1);");
        _type _t = _type.of("class C{", "int i;", "}"); //infers a line separator between each string
        _codeUnit _cu = _codeUnit.of("module org.astro{", "exports org.astro;","}");

        //building and returning specific things:
        _binaryExpression _be = _binaryExpression.of("1 + 2");
        _expressionStmt _es = _expressionStmt.of("System.out.println(1);");
        _class _c = _class.of("class C{", "int i;", "}");
        _moduleInfo _mi = _moduleInfo.of("module org.astro{", "exports org.astro;","}");

        //
        _typeRef _tr = _typeRef.of("List<String>");

    }
}
