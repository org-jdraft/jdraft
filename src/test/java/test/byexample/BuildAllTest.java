package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class BuildAllTest extends TestCase {

    /**
     * Verify I can create instances with no-arg constructors
     */
    public void testBuildOfNoArg(){
        _java._domain[] _instances = {
                _anno.of(),
                _annos.of(),
                _annotation.of(),
                _arguments.of(),
                _arrayAccess.of(),
                _arrayCreate.of(),
                _arrayDimension.of(),
                _arrayInitialize.of(),
                _assertStmt.of(),
                _assign.of(),
                _binaryExpression.of(),
                _blockStmt.of(),
                _blockComment.of(),
                _body.of(),
                _boolean.of(),
                _breakStmt.of(),
                _caseGroup.of(),
                _cast.of(),
                _catch.of(),
                _char.of(), // or "'c'"
                _class.of(),
                _classExpression.of(),
                _conditionalExpression.of(), //also called "ternary"
                _constant.of(), //enum constant declaration
                _constructor.of(),
                _constructorCallStmt.of(),
                _continueStmt.of(),
                _doStmt.of(),
                _double.of(), //this is for double precision doubles
                _emptyStmt.of(), // empty statement placeholders i.e. for(;;){}
                _enclosedExpression.of(),
                _enum.of(),
                _expressionStmt.of(),
                _field.of(),
                _fieldAccess.of(),
                _forEachStmt.of(),
                _forStmt.of(),
                _ifStmt.of(),
                _import.of(),
                _imports.of(),
                _initBlock.of(),
                _instanceOf.of(),
                _int.of(),
                _interface.of(),
                _javadocComment.of(),
                _labeledStmt.of(),
                _lambda.of(),
                _lineComment.of(),
                _localClassStmt.of(),
                _localVariables.of(),
                _long.of(),
                _method.of(),
                _methodCall.of(),
                _methodReference.of(),
                //_modifier.of(),
                _modifiers.of(),
                _moduleInfo.of(),
                _name.of(),
                _nameExpression.of(),
                _new.of(),
                _null.of(),
                _package.of(),
                _packageInfo.of(),
                _parameter.of(),
                _parameters.of(),
                //_qualifiedName.of(),
                _receiverParameter.of(),
                _returnStmt.of(),
                _string.of(),
                _super.of(),
                _switchEntry.of(),
                _switchExpression.of(),
                _switchStmt.of(),
                _synchronizedStmt.of(),
                _textBlock.of(),
                _this.of(),
                _throws.of(),
                _throwStmt.of(),
                _tryStmt.of(),
                _typeArguments.of(),
                _typeExpression.of(), //In <code>World::greet</code> the "World" is a TypeExpr
                _typeParameter.of(),
                _typeParameters.of(),
                //_typeRef.of(),
                _unary.of(),
                _variable.of(),
                _whileStmt.of(),
                _yieldStmt.of()
        };
    }

    public void testBuildOfString() {
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
                 _javadoc.of("/** a javadoc */"),
                 _labeledStmt.of("outer: print(state);"),
                 _lambda.of("o-> System.out.println(1)"),
                 _lineComment.of("// TODO remove this"),
                 _localClassStmt.of("class Local{ int i=100; }"),
                 _localVariables.of("int i, j = 100"),
                 _long.of("-1L"),
                 _method.of("int m(){ return 56; }"),
                 _methodCall.of("call(120)"),
                 _methodReference.of("Context::getState"),
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

     public void testBuildOfLiteral(){
         _expression._literal[] _literals = {
                 _expression.of(1),
                 _expression.of(1.2d),
                 _expression.of(true),
                 _expression.of('c'),

                 _int.of(1),
                 _double.of(1.2d),
                 _double.of(1.5f),
                 _boolean.of(true),
                 _char.of('c'),
                 _null.of(),
                 _string.of("String"),
                 _textBlock.of("multi", "line", "Text", "Block")
         };
     }

    class C{ int i; }
    enum E{ A; }
    interface I{ void m(); }
    @interface A{ int value(); }

    public void testBuildOfClass(){
        class LocalClass{
            int val;
        }
        _java._domain[] ms = {
                _type.of( C.class), // (build a specific _type)

                //specific types
                _class.of(C.class),
                _enum.of(E.class),
                _interface.of(I.class),
                _annotation.of(A.class),

                _class.of(LocalClass.class), //Local (to this method) classes

                _anno.of(Deprecated.class), //the @Deprecated annotation
                _throws.of(RuntimeException.class),
                _import.of(Map.class),
                _imports.of(File.class, IOException.class),
                _typeRef.of(Integer.class),

                _catch.of(IOException.class, URISyntaxException.class)
                        .setBody(()-> {System.out.println("Ohh man, ohh geez");}),
                _new.of( URL.class ),
                _parameter.of(String.class, "name"),
        };
    }

    /** of( ()-> ) */
    public void testBuildOfLambda(){
        System.out.println( _binaryExpression.of( ()-> 1 +7 + 11 ) );

        //create a lambda version of this
        //_expression.of()

        _java._domain[] ol = {

                //expressions
                _assign.of( (Object a, Object b) -> a = b ),
                _arrayCreate.of( ()-> { String[][] s = new String[1][3];} ),
                _arrayInitialize.of( ()-> { int[] o = {1,3,5,7,9}; } ),
                _binaryExpression.of( ()-> 1 + 4 ),
                _cast.of( o-> (String)o ),
                _catch.of( ()-> {
                    try{
                        throw new IOException();
                    }
                    catch(IOException ioe){
                        throw new RuntimeException(ioe);
                    }
                }),

                //generic statements
                _statement.of( (a) -> System.out.println(a)),

                _assertStmt.of( ()-> {assert (1==1);} ),
                _parameters.of( (final Integer a, String... s)->{} ),
                _arguments.of( ()-> new Object[]{1, "Value", System.currentTimeMillis()} )
        };
    }

    public void testBuiltByAnonymousClass(){
        //build a class
        _class _c = _class.of( "aaaa.bbbb.C", new Object(){
            int i=0;
            public int m(){
                return i + 10;
            }
        });

        _method _m = _method.of( new Object(){
            public String getName(){
                return this.name;
            }
            String name;
        });
        _constructor _ct = _constructor.of( new Object(){
            void C( int num, String...values){
                this.num = num;
                this.values = values;
            }
            String[] values;
            int num;
        });
        System.out.println( _c );
    }

}
