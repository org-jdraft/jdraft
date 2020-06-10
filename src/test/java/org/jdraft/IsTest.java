package org.jdraft;

import junit.framework.TestCase;

import java.util.Map;

public class IsTest extends TestCase {

    //test that all instances can
    //is Map<_feature, Object>
    public void testIsAny(){
        //_java._domain is the top-level interface for all instance AND interface abstractions

        assertTrue(_annoExpr.of("@A").is("$any$"));
        assertTrue(_annoExprs.of("@A @B").is("$any$"));
        assertTrue(_annotation.of("@interface AI{}").is("$any$"));
        assertTrue(_args.of("(1, 'c')").is("$any$"));
        assertTrue(_arrayAccessExpr.of("a[1][call()]").is("$any$"));
        assertTrue(_newArrayExpr.of("new a[1][2]").is("$any$"));
        assertTrue(_arrayDimension.of("[0]").is("$any$"));
        assertTrue(_arrayInitExpr.of("{1,2,3}").is("$any$"));
        assertTrue(_assertStmt.of("assert true;").is("$any$"));
        assertTrue(_assignExpr.of("x = 1").is("$any$"));
        assertTrue(_binaryExpr.of("a && b").is("$any$"));
        assertTrue(_blockStmt.of("{ print(120); }").is("$any$"));
        assertTrue(_blockComment.of("first line", "second line").is("$any$"));
        assertTrue(_body.of("print(3);").is("$any$"));
        assertTrue(_booleanExpr.of("true").is("$any$"));
        assertTrue(_breakStmt.of("break outer;").is("$any$"));
        assertTrue(_cases.of("case 1: case 3: case 5: return ODD;").is("$any$"));
        assertTrue(_castExpr.of("(String)s").is("$any$"));
        assertTrue(_catch.of("catch(IOException ioe){ throw new RuntimeException(ioe); }").is("$any$"));
        assertTrue(_charExpr.of('a').is("$any$"));
        assertTrue(_class.of("class C{}").is("$any$"));
        assertTrue(_classExpr.of("String.class").is("$any$"));
        assertTrue(_ternaryExpr.of("b==0?x:y").is("$any$"));
        assertTrue(_constant.of("A(100)").is("$any$"));
        assertTrue(_constructor.of("C(int a){ this.a = a; }").is("$any$"));
        assertTrue(_constructorCallStmt.of("this(100);").is("$any$"));
        assertTrue(_constructorCallStmt.of("super(100);").is("$any$"));
        assertTrue(_continueStmt.of("continue inner;").is("$any$"));
        assertTrue(_doStmt.of("do{ i++; }while(i < 100);").is("$any$"));
        assertTrue(_doubleExpr.of(1.2d).is("$any$"));
        assertTrue(_doubleExpr.of(1.2f).is("$any$"));
        assertTrue(_emptyStmt.of().is("$any$"));
        assertTrue(_expr.of( "1+2" ).is("$any$"));
        assertTrue(_parenthesizedExpr.of("(1 + 2)").is("$any$"));
        assertTrue(_enum.of("enum E{}").is("$any$"));
        assertTrue(_exprStmt.of("System.out.println(1);").is("$any$"));
        assertTrue(_exprStmt.of(_methodCallExpr.of("print(1)")).is("$any$"));
        assertTrue(_field.of("public int i;").is("$any$"));
        assertTrue(_fieldAccessExpr.of("System.out").is("$any$"));
        assertTrue(_forEachStmt.of("for(Object o: objects) { print(o); }").is("$any$"));
        assertTrue(_forStmt.of("for(int i=0;i<100;i++){ print(i); }").is("$any$"));
        assertTrue(_ifStmt.of("if(a==b){ print(1); }").is("$any$"));
        assertTrue(_import.of("java.util.*").is("$any$"));
        assertTrue(_import.of(Map.class).is("$any$"));
        assertTrue(_imports.of("import java.util.*;", "import java.net.*;").is("$any$"));
        assertTrue(_initBlock.of("static{ a = 1000; }").is("$any$"));
        assertTrue(_initBlock.of("{ print(state); }").is("$any$"));
        assertTrue(_instanceOfExpr.of("A instanceof Map").is("$any$"));
        assertTrue(_intExpr.of("37").is("$any$"));
        assertTrue(_intExpr.of(37).is("$any$"));
        assertTrue(_interface.of("interface I{}").is("$any$"));
        assertTrue(_javadocComment.of("/** a javadoc */").is("$any$"));
        assertTrue(_labeledStmt.of("outer: print(state);").is("$any$"));
        assertTrue(_lambdaExpr.of("o-> System.out.println(1)").is("$any$"));
        assertTrue(_lineComment.of("// TODO remove this").is("$any$"));
        assertTrue(_localClassStmt.of("class Local{ int i=100; }").is("$any$"));
        assertTrue(_variablesExpr.of("int i, j = 100").is("$any$"));
        assertTrue(_longExpr.of("-1L").is("$any$"));
        assertTrue(_method.of("int m(){ return 56; }").is("$any$"));
        assertTrue(_methodCallExpr.of("call(120);").is("$any$"));
        assertTrue(_methodRefExpr.of("Context::getState").is("$any$"));
        assertTrue(_modifier.of("public").is("$any$"));
        assertTrue(_modifiers.of("public static final").is("$any$"));

        assertTrue(_moduleInfo.of("module org.jdraft.mod { exports org.jdraft.mod; }").is("$any$"));
        assertTrue(_moduleDirective.of("opens shiny;").is("$any$"));
        assertTrue(_moduleExports.of("exports underdog;").is("$any$"));
        assertTrue(_moduleUses.of("uses underdog;").is("$any$"));
        assertTrue(_moduleOpens.of("opens another.module;").is("$any$"));
        assertTrue(_moduleRequires.of("requires static alpha.beta;").is("$any$"));
        assertTrue(_moduleProvides.of("provides monitor.AClass with monitor.AFactoryClass").is("$any$"));

        assertTrue(_name.of("x").is("$any$"));
        assertTrue(_nameExpr.of("x").is("$any$"));
        assertTrue(_newExpr.of("new RuntimeException()").is("$any$"));
        assertTrue(_nullExpr.of().is("$any$"));
        assertTrue(_package.of("package org.jdraft;").is("$any$"));
        assertTrue(_packageInfo.of("/** information about jdraft */package org.jdraft;").is("$any$"));
        assertTrue(_param.of("final int i").is("$any$"));
        assertTrue(_params.of("(@ann int x, String...names)").is("$any$"));
        assertTrue(_qualifiedName.of("org.jdraft._class").is("$any$"));
        assertTrue(_receiverParam.of("@AnnotatedUsage Currency this").is("$any$"));
        assertTrue(_returnStmt.of("return 12;").is("$any$"));
        assertTrue(_stmt.of("System.out.println(1);").is("$any$"));
        assertTrue(_stringExpr.of("A String literal").is("$any$"));
        assertTrue(_superExpr.of("super").is("$any$"));
        assertTrue(_switchEntry.of("case 1: return 3;").is("$any$"));
        assertTrue(_switchExpr.of("switch(val){ case 1: yield 'a'; }").is("$any$"));
        assertTrue(_switchStmt.of("switch(e){ case 1: return 'a'; default: return 'c';}").is("$any$"));
        assertTrue(_synchronizedStmt.of("synchronized(a){ }").is("$any$"));
        assertTrue(_textBlockExpr.of("first line", "second line", "third line").is("$any$"));
        assertTrue(_thisExpr.of("World.this").is("$any$"));
        assertTrue(_throws.of("MyException").is("$any$"));
        assertTrue(_throws.of(RuntimeException.class).is("$any$"));
        assertTrue(_throwStmt.of("throw new RuntimeException();").is("$any$"));
        assertTrue(_tryStmt.of("try{ Files.read(fileName); } catch(IOException ioe){ }").is("$any$"));
        assertTrue(_typeArgs.of("<T,R>").is("$any$"));
        assertTrue(_typeExpr.of("World").is("$any$"));
        assertTrue(_typeParam.of("K").is("$any$"));
        assertTrue(_typeParams.of("<String,Integer>").is("$any$"));
        assertTrue(_typeRef.of("List<String>").is("$any$"));
        assertTrue(_unaryExpr.of("!true").is("$any$"));
        assertTrue(_variable.of("int i;").is("$any$"));
        assertTrue(_whileStmt.of("while(a<100){ i+=a; a++; }").is("$any$"));
        assertTrue(_yieldStmt.of("yield 3;").is("$any$"));
    }
}
