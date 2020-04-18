package org.jdraft.mr;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.io._path;
import org.jdraft.pattern.*;

import java.io.IOException;
import java.util.List;

public class JavaParserParseString extends TestCase {

    public void printClassNames(){
        $class.of( $.packageDecl("org.jdraft" ) )
                .forEachIn(
                        _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java").load(), e-> System.out.println( e.getFullName() ));
    }
    public void testSs(){
        //_class _c = _class.of("public class C{ int i= 0;}");

        CompilationUnit cu = Ast.of("public class A{}");
        cu.addImport(new ImportDeclaration("java.util", false, true));
        cu.addImport(new ImportDeclaration("java.util.Map", false, false));

        System.out.println( cu );


    }

    public void testToString(){
           //System.out.println("MP"+ $modifiers.PUBLIC );
           // System.out.println("MP"+ $.PUBLIC );

            //System.out.println( $modifiers.of( $.PUBLIC, $.STATIC ) );
            $method $mm = $method.of( $.PUBLIC, $.NOT_STATIC );
            //System.out.println($mm);

            $method $m = $method.of(
                    $.anno(Deprecated.class),
                    $.PUBLIC, $.NOT_STATIC,
                    $name.of("get$Name$"),
                    $typeParameter.of("$t$ extends Serializable"),
                    $typeRef.of(int.class),
                    $parameter.of("int $p$"),
                    $throws.of(IOException.class, Exception.class),
                    $body.of("return this.$name$;") );
            System.out.println( $m );
    }

    public void testReadAndGetAllParseErrors(){
        String c =
                "public class Test {\n" +
                "    private int x = 42 // missing a semi colon here\n" +
                "    public sayHello() { // here the fault is that this person forgets a return type\n" +
                "          return \"\";\n" +
                "    }\n" +
                "}";

        JavaParser jp = new JavaParser( new ParserConfiguration() );
        ParseResult<CompilationUnit> pr = jp.parse(c);
        List<Problem> prbs = pr.getProblems();
        prbs.forEach(p -> System.out.println( p ));
    }

    @interface MyAnnotation{
        String value();
    }
    static class Sil{
        private final static String BASE = "base";
        private final static String EXTENSION = "some text and it's " + BASE;

        @MyAnnotation(value = EXTENSION)
        public void someMethod() {  }
    }

    public void testSil(){
        //$.name().(Sil.class);
        $node.of("EXTENSION").$hasAncestor($anno.of()).forEachIn(Sil.class, e-> System.out.println(e+" "+e.getClass()));
    }
    public void testParse(){
        String ctorBlock = "{\n" +
                "    super(ctx);\n" +
                "}";

        ConstructorDeclaration cd = (ConstructorDeclaration)
                StaticJavaParser.parseBodyDeclaration("C()"+ctorBlock);
        BlockStmt bs = cd.getBody();
        bs.remove();
        System.out.println( bs );
    }

    class GG{

    }

    class A{
        <T>A(){}

        <T extends GG>A(T t){

        }
    }

    class B extends A{
        B(){
            <Integer>super();
        }
    }
    public void testYY(){

    }
}
