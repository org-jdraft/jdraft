package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.macro._get;
import org.jdraft.macro._set;
import org.jdraft.macro._static;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

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

    static class CharBitmap{
        BitSet bs;

        static CharBitmap of(String exactString){
            BitSet bs = new BitSet();
            for(int i=0;i<exactString.length();i++){
                bs.set( exactString.charAt(i) );
            }
            return new CharBitmap(bs);
        }

        CharBitmap(BitSet bitSet){
            this.bs = bitSet;
        }

        public int getMin(){
            return bs.nextSetBit(0);
        }

        public boolean intersects( BitSet bm ){
            return this.bs.intersects( bm );
        }

        public String toString(){
            return bs.toString();
        }
    }

    static class ParseWord{

        public static ParseWord of(String keyword){
            return new ParseWord(keyword);
        }

        final String text;
        final CharBitmap required;

        ParseWord(String keyword){
            this.text = keyword;
            this.required = CharBitmap.of(keyword);
        }
    }


    public void testTokenizer(){
        //build a new class
        _class _c = _class.of("aaaa.bbb.C",new Object(){
            @_set @_get int x = 100;
            @_get @_set double y = -12.3456D;
            public @_static final String ID = UUID.randomUUID().toString();

            public int rr(int avar){
                int count = 1 + 2 * ( 5678 ) / avar;
                System.out.println( "THE COUNT IS "+ count);
                return count;
            }
        });

        List<Object> lo = _java._tokenizer.tokenize(_c.toString());
        System.out.println(lo);
        _java._keyWord.replace(lo);
        System.out.println(lo);
    }

    public void testKeywords(){

    }
    public void testCodeCharSet(){

        CharBitmap lexCharacters = CharBitmap.of("`~!@#$%^&*()-_=+\\|{[]};:'\",<.>/?");
        System.out.println( "LEX CHARACTERS "+ lexCharacters );

        CharBitmap starts = CharBitmap.of(
                "truefalsenull@{['(-+/moduleexportsopensusesrequiresprovidesifclassforpackagetrystaticthrowsyieldwhile<import>assertnewbreakthissupercasecatch"+
                "trydowhileenumreturn\"synchronizedswitchyieldwhile");
        System.out.println( starts );
    }

    public void testCharBitMap(){

        //not #
        CharBitmap cb = CharBitmap.of("`~!1@2#3$4%5^6&7*8(9)0-_=+"+
                "qQwWeErRtTyYuUiIoOpP[{]}\\|" +
                "aAsSdDfFgGhHiIjJkKlL;:'\"" +
                "zZxXcCvVbBnNmM,<.>/?");


        System.out.println( cb.bs );
        System.out.println( "VALUE : \""+ new char[]{9}[0] +"\"");
        System.out.println( "VALUE : \""+ new char[]{10}[0] +"\"");


    }
    public void testJavaOf(){
        _anno _a = (_anno) _java.of("@A");
        _annos _as = (_annos)_java.of("@A @B");
        _annotation _an = (_annotation)_java.of("@interface AI{}");
        _args _ars = (_args)_java.of("(1, 'c')");
        _assignExpr _ae = (_assignExpr)_java.of("a += 1");
        _newArrayExpr _nae  =(_newArrayExpr) _java.of("new a[1][2]");
        _arrayDimension _ad = (_arrayDimension)_java.of("[0]");
        _arrayInitExpr _aie = (_arrayInitExpr)_java.of("{1,2,3}");
        _assertStmt _ast = (_assertStmt) _java.of("assert true;");
        _binaryExpr _be = (_binaryExpr)_java.of("a + b");
        _blockStmt _bst = (_blockStmt)_java.of("{ print(120); }");
        _blockComment _bc = (_blockComment)_java.of("/* comment */");
        _booleanExpr _b = (_booleanExpr) _java.of("true");
        _b = (_booleanExpr) _java.of("false");
        _breakStmt _bs = (_breakStmt)_java.of("break;");
        _bs = (_breakStmt)_java.of("break outer;");
        _switchCase _sc = (_switchCase) _java.of( "case 1:");
        _sc = (_switchCase) _java.of( "case 1: return 1;");
        _sc = (_switchCase) _java.of( "default: return 1;");
        _switchCases _scs = (_switchCases) _java.of( "case 1: case 3: case 5: return ODD;");
        _catch _ct = (_catch)_java.of("catch(IOException e){ }");
        _charExpr _c = (_charExpr)_java.of("'c'");
        _constructorCallStmt _ctc = (_constructorCallStmt)_java.of("this(1);");
        _ctc = (_constructorCallStmt)_java.of("super(1);");
        _continueStmt _cons = (_continueStmt)_java.of("continue;");
        _cons = (_continueStmt)_java.of("continue upper;");
        _doStmt _ds = (_doStmt)_java.of("do{ n++; }while(n<100);");
        _emptyStmt _es = (_emptyStmt)_java.of("");
        _fieldAccessExpr _fa = (_fieldAccessExpr)_java.of("aaaa.ID");
        _parenthesizedExpr _pe = (_parenthesizedExpr) _java.of("(1 + 2)");
        _enum _e = (_enum)_java.of("enum E{}");
        _forEachStmt _fes = (_forEachStmt) _java.of("for(Object o: objects) { print(o); }");
        _forStmt _fs = (_forStmt) _java.of("for(int i=0;i<100;i++){ print(i); }");
        _ifStmt _if = (_ifStmt)_java.of("if(a==b){ print(1); }");
        _import _im = (_import)_java.of("import java.util.*;");
        _im = (_import)_java.of("import java.util.Map;");
        _imports _ims = (_imports)_java.of("import java.util.*;", "import java.net.*;");
        _initBlock _sb = (_initBlock)_java.of("static{ a=1000; }");
        _interface _in = (_interface)_java.of("interface A{}");
        _javadocComment _jdc = (_javadocComment)_java.of("/** jdc */");
        _lambdaExpr _le = (_lambdaExpr)_java.of("()->1");
        _lineComment _lc = (_lineComment)_java.of("// linecomment");
        _modifier _m = (_modifier)_java.of("public");
        _moduleInfo _mi = (_moduleInfo)_java.of("module org.jdraft.mod { exports org.jdraft.mod; }");
        _moduleExports _me = (_moduleExports)_java.of("exports underdog;");
        _moduleUses _mu = (_moduleUses)_java.of("uses underdog;");
        _moduleOpens _mo = (_moduleOpens)_java.of("opens another.module;");
        _moduleRequires _mr = (_moduleRequires)_java.of("requires static alpha.beta;");
        _moduleProvides _mp = (_moduleProvides)_java.of("provides monitor.AClass with monitor.AFactoryClass");
        _newExpr _ne = (_newExpr)_java.of("new Blah()");
        _nullExpr _nu = (_nullExpr)_java.of("null");
        _package _p = (_package)_java.of("package org.jdraft;");
        //_param _pa = (_param)_java.of("final int i");
        _params _ps = (_params)_java.of("(@ann int x, String...names)");
        _receiverParam _rp = (_receiverParam)_java.of("@AnnotatedUsage Currency this");
        _returnStmt _rs = (_returnStmt)_java.of("return 12;");
        _textBlockExpr _tb = (_textBlockExpr)_java.of("\"\"\"  text blocks \"\"\"");
        _stringExpr _str = (_stringExpr)_java.of("\"string\"");
        _switchCase _swc = (_switchCase)_java.of("case 1:");
        _swc = (_switchCase)_java.of("case 1: return 1;");
        _switchCases _swcs = (_switchCases)_java.of("case 1: case 2: return cases;");
        _switchExpr _swe = (_switchExpr)_java.of("switch(val){ case 1: yield 'a'; }");
        _switchStmt _ss = (_switchStmt)_java.of("switch(e){ case 1: return 'a'; default: return 'c';}");
        _superExpr _se = (_superExpr)_java.of("super");
        _synchronizedStmt _sss = (_synchronizedStmt)_java.of("synchronized(a){ }");
        _thisExpr _te = (_thisExpr)_java.of("World.this");
        _throwStmt _ts = (_throwStmt)_java.of("throw new RuntimeException();");
        _throws _ths = (_throws)_java.of("throws IOException");
        _tryStmt _trs = (_tryStmt)_java.of("try{ Files.read(fileName); } catch(IOException ioe){ }");
        _unaryExpr _ue = (_unaryExpr)_java.of("!true");
        _unaryExpr _ue2 = (_unaryExpr)_java.of("i++");
        _unaryExpr _ue3 = (_unaryExpr)_java.of( "-a");
        _whileStmt _ws = (_whileStmt)_java.of("while(true){ keepWorking(); }");
        _yieldStmt _ys = (_yieldStmt)_java.of("yield 4;");

        _arrayAccessExpr _aae = (_arrayAccessExpr)_java.of("a[1][call()]");
        _castExpr _cee  = (_castExpr)_java.of("(String)s");
        _class _cl = (_class)_java.of("class C{}");
        _classExpr _cle = (_classExpr)_java.of("String.class");
        _methodCallExpr _mce = (_methodCallExpr)_java.of("call(120)"); //
        //_constant.of("A(100)"), //enum constant declaration
        _lambdaExpr _lam = (_lambdaExpr)_java.of("o-> System.out.println(1)");
        _textBlockExpr _tbe = (_textBlockExpr)_java.of("\"\"\" text block \"\"\"");
        _enum _en = (_enum)_java.of("enum E{}");
        _constructor _ctor = (_constructor)_java.of("C(int a){ this.a = a; }");
        _method _mm = (_method)_java.of("int m(){ return 56; }");
        _intExpr _ii = (_intExpr)_java.of("1");
        _ii = (_intExpr)_java.of("-1");
        _ii = (_intExpr)_java.of("-1_000");
        _ii = (_intExpr)_java.of("-0xDEAD_BEEF");
        _ii = (_intExpr)_java.of("0xDEAD_BEEF");
        _ii = (_intExpr)_java.of("-0b10100101");
        _ii = (_intExpr)_java.of("0b10100101");

        _longExpr _li = (_longExpr)_java.of("1L");
        _li = (_longExpr)_java.of("1_000_000l");
        _li = (_longExpr)_java.of("-1L");
        _li = (_longExpr)_java.of("-1_000L");
        _li = (_longExpr)_java.of("-0xDEAD_BEEFL");
        _li = (_longExpr)_java.of("0xDEAD_BEEFL");
        _li = (_longExpr)_java.of("-0b10100101L");
        _li = (_longExpr)_java.of("0b10100101L");

        _doubleExpr _de = (_doubleExpr)_java.of("1d");
        _de = (_doubleExpr)_java.of("1.0");
        _de = (_doubleExpr)_java.of("1.0d");
        _de = (_doubleExpr)_java.of("1.0D");
        _de = (_doubleExpr)_java.of("-1d");
        _de = (_doubleExpr)_java.of("-1.0d");
        _de = (_doubleExpr)_java.of("0.101001D");
        _de = (_doubleExpr)_java.of(".101001D");

        //scientific notation
        _de = (_doubleExpr)_java.of("-2.147484E9");
        _de = (_doubleExpr)_java.of("-2.14748E9");
        _de = (_doubleExpr)_java.of("3.303e+23");
        _de = (_doubleExpr)_java.of("3.7872E-1");
        _de = (_doubleExpr)_java.of("378720E-6");

        _doubleExpr _fe = (_doubleExpr)_java.of("1F");
        _fe = (_doubleExpr)_java.of("-1f");
        _fe = (_doubleExpr)_java.of("-1.0f");
        _fe = (_doubleExpr)_java.of("0.101001F");
        _fe = (_doubleExpr)_java.of(".101001F");

        //scientific notation
        _fe = (_doubleExpr)_java.of("-2.147484E9F");
        _fe = (_doubleExpr)_java.of("-2.14748E9F");
        _fe = (_doubleExpr)_java.of("3.303e+23F");
        _fe = (_doubleExpr)_java.of("3.7872E-1F");
        _fe = (_doubleExpr)_java.of("378720E-6F");


        //long l = 378720E-6;

        //maybe tokenize IF i havent figured it out yet
        /*
        _assignExpr.of("x = 1"),
        _binaryExpr.of("a && b"), //repeating binaryExprs
        _ternaryExpr.of("b==0?x:y"), //also called "ternary"
        _expr.of( "1+2" ), //_expression will create any expression type

        _field.of("public int i;"),
        _fieldAccessExpr.of("System.out"),
        _instanceOfExpr.of("A instanceof Map"),
        _labeledStmt.of("outer: print(state);"),

        _variablesExpr.of("int i, j = 100"),

        _methodRefExpr.of("Context::getState"),
        _modifiers.of("public static final"),
        _name.of("x"),
        _nameExpr.of("x"),
        _param.of("final int i"),
        _receiverParam.of("@AnnotatedUsage Currency this"),
        _stmt.of("System.out.println(1);"), //_statement creates any statement type
        _typeArgs.of("<T,R>"),
        _typeExpr.of("World"), //In <code>World::greet</code> the "World" is a TypeExpr
        _typeParam.of("K"),
        _typeParams.of("<String,Integer>"),
        _typeRef.of("List<String>"),
        _variable.of("int i;"),
         */
    }
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
