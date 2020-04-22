package org.jdraft.bot;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._addImports;
import org.jdraft.macro._packageName;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SrefTest extends TestCase {

    public @interface ANN{ }

    public void testNotMatch(){
        //System.out.println( "MI" + $ref.of(JavaParser.class).$matchImports(false).matchImports);

        //;
        //assertEquals(0, $ref.of(JavaParser.class).$matchImports(false).countIn(_import.of(JavaParser.class) ));
    }

    //test if I directly use a reference
    public void testDirectUse(){
        //direct use

        //package name
        assertEquals(1, $ref.of(JavaParser.class.getPackage().getName())
                .countIn(_package.of(JavaParser.class.getPackage().getName()) ));

        //explicitly omit package name
        assertEquals(0, $ref.of(JavaParser.class.getPackage().getName()).$matchPackageNames(false)
                .countIn(_package.of(JavaParser.class.getPackage().getName()) ));

        //in import
        assertEquals(1, $ref.of(JavaParser.class).countIn(_import.of(JavaParser.class) ));

        //Print.tree(_import.of(JavaParser.class));
        assertTrue($ref.isImportName(_import.of(JavaParser.class).ast().getName()));
        assertTrue($ref.isImportName(_import.of(JavaParser.class).ast()));

        //explicitly omit imports
        assertEquals(0, $ref.of(JavaParser.class).$matchImports(false).countIn(_import.of(JavaParser.class) ));

        //type name
        assertEquals(1, $ref.of(JavaParser.class)
                .countIn(_field.of(JavaParser.class, "i") ));

        // in array
        assertEquals(1, $ref.of(int.class)
                .countIn(_field.of("int[] i;") ));

        // in generic
        assertEquals(1, $ref.of("Thingy")
                .countIn(_field.of("Optional<Thingy> i;") ));
        assertEquals(1, $ref.of("org.myproj.Thingy")
                .countIn(_field.of("Optional<org.myproj.Thingy> i;") ));

        // in type Parameter
        assertEquals(1, $ref.of("Thingy")
                .countIn(_typeParameter.of("<E extends Thingy>") ));

        assertEquals(1, $ref.of("Thingy")
                .countIn(_method.of("<A, E extends Thingy> void m(){}") ));

        assertEquals(1, $ref.of("org.myproj.Thingy")
                .countIn(_typeParameter.of("<E extends org.myproj.Thingy>") ));


        //explicitly omit type name
        assertEquals(0, $ref.of(JavaParser.class).$matchTypeRefNames(false)
                .countIn(_field.of(JavaParser.class, "i") ));


        // in array
        assertEquals(0, $ref.of(int.class).$matchTypeRefNames(false)
                .countIn(_field.of("int[] i;") ));

        // in generic
        assertEquals(0, $ref.of("Thingy").$matchTypeRefNames(false)
                .countIn(_field.of("Optional<Thingy> i;") ));
        assertEquals(0, $ref.of("org.myproj.Thingy").$matchTypeRefNames(false)
                .countIn(_field.of("Optional<org.myproj.Thingy> i;") ));

        // in type Parameter
        assertEquals(0, $ref.of("Thingy").$matchTypeRefNames(false)
                .countIn(_typeParameter.of("<E extends Thingy>") ));

        assertEquals(0, $ref.of("Thingy").$matchTypeRefNames(false)
                .countIn(_method.of("<A, E extends Thingy> void m(){}") ));

        assertEquals(0, $ref.of("org.myproj.Thingy").$matchTypeRefNames(false)
                .countIn(_typeParameter.of("<E extends org.myproj.Thingy>") ));


        //variable name
        assertEquals(1, $ref.of("varName")
                .countIn(_field.of(JavaParser.class, "varName") ));

        //explicitly omit variable names
        assertEquals( 0, $ref.of("varName").$matchVariableNames(false)
                .countIn(_field.of(JavaParser.class, "varName") ));

        //method referenced
        assertEquals( 1, $ref.of("System.out")
                .countIn(_methodRef.of("System.out::println")));

        // explicitly not method reference
        assertEquals( 0, $ref.of("System.out").$matchMethodReferences(false)
                .countIn(_methodRef.of("System.out::println")));

        //parameter name
        assertEquals( 1, $ref.of("pName")
                .countIn(_parameter.of("int pName")));

        assertEquals( 0, $ref.of("pName").$matchParameterNames(false)
                .countIn(_parameter.of("int pName")));

        assertEquals( 1, $ref.of("pType").$matchParameterNames(false)
                .countIn(_parameter.of("pType pName")));

        assertEquals(1, $ref.of("mName")
            .countIn(_method.of("void mName(){}")));

        assertEquals(0, $ref.of("mName").$matchMethodNames(false)
                .countIn(_method.of("void mName(){}")));

        assertEquals(1, $ref.of("cName")
                .countIn(_constructor.of("cName(){}")));

        assertEquals(0, $ref.of("cName").$matchConstructorNames(false)
                .countIn(_constructor.of("cName(){}")));
    }
    public void testPackage(){

        Print.tree(_package.of(NodeWithStatements.class.getPackage().getName()));


        //wildcard (i.e. starts with)
        $ref $javaParserRoot = $ref.of("com.github.javaparser$any$");
        assertEquals(1, $javaParserRoot.countIn(_package.of(JavaParser.class.getPackage().getName()))); //main directory $any$ = ""

        assertEquals(1, $javaParserRoot.countIn(_package.of(Expression.class.getPackage().getName()))); //
        assertEquals(1, $javaParserRoot.countIn(_package.of(NodeWithStatements.class.getPackage().getName())));

        _class _c = _class.of("com.github.javaparser.ast.CompilationUnit");
        assertEquals(1, $javaParserRoot.countIn(_c));
    }

    public void testImports(){
        //regular import
        assertTrue( $ref.of(FieldDeclaration.class).matches(_import.of(FieldDeclaration.class)) );

        $ref $javaParserRoot = $ref.of("com.github.javaparser$any$");

        assertEquals(1, $javaParserRoot.countIn(_import.of(JavaParser.class)));//main directory
        assertEquals(1, $javaParserRoot.countIn(_import.of(Expression.class))); //ast subdirectory
        assertEquals(1, $javaParserRoot.countIn(_import.of(NodeWithStatements.class))); //ast.nodetypes subdirectory

        //static import
        assertTrue( $ref.of(FieldDeclaration.class).matches(_import.of(FieldDeclaration.class).setStatic()) );

        _class _c = _class.of("C").addImports(FieldDeclaration.class);
        assertEquals(1, $javaParserRoot.countIn(_c));
        _c.addImportStatic( com.github.javaparser.StaticJavaParser.class);
        assertEquals(2, $javaParserRoot.countIn(_c));
    }

    public void testVariableName(){
        assertTrue( $ref.of("i").matches(_variable.of("int i").getNameNode()) );
        assertTrue( $ref.of("big$any$").matches(_variable.of("int bigTruck").getNameNode()) );
        assertTrue( $ref.of("big$any$").matches(_variable.of("int bigForest").getNameNode()) );
        assertFalse( $ref.of("big$any$").matches(_variable.of("int Truckbig").getNameNode()) );
    }

    public void testAnnotationName(){
        //simple reference
        assertEquals(1, $ref.of("annName").countIn(_anno.of("annName")));
        assertEquals(1, $ref.of("annName").countIn(_anno.of("annName(1)")));
        assertEquals(1, $ref.of("annName").countIn(_anno.of("annName(key=2)")));

        //qualified reference
        assertEquals( 1, $ref.of("fully.qualified.AnnName").countIn(_anno.of("fully.qualified.AnnName")));
        assertEquals( 1, $ref.of("fully.qualified.AnnName").countIn(_anno.of("fully.qualified.AnnName(1)")));
        assertEquals( 1, $ref.of("fully.qualified.AnnName").countIn(_anno.of("fully.qualified.AnnName(key=2)")));
    }

    public void testMethodReference(){

    }

    public void testType(){
        assertTrue( $ref.of("SomeType").matches(_typeRef.of("SomeType") ) );
        assertEquals(1,  $ref.of("SomeType").countIn(_typeRef.of("Optional<SomeType>") ) );
    }

    public void testUp(){
        @_packageName("com.github.javaparser")
        @_addImports({JavaParser.class, ExpressionStmt.class, NodeWithStatements.class})
        @org.jdraft.bot.SrefTest.ANN
        class C{
            Optional<com.github.javaparser.ast.CompilationUnit> op;
            //void m( com.github.javaparser.JavaParser jp){
            void m(com.github.javaparser.javadoc.Javadoc jd) throws com.github.javaparser.ParseProblemException {
                com.github.javaparser.printer.DotPrinter dp;
                com.github.javaparser.resolution.MethodAmbiguityException mae;
                com.github.javaparser.ast.CompilationUnit cu;
                Optional<com.github.javaparser.ast.CompilationUnit> op;
                java.util.Map<com.github.javaparser.ast.expr.Expression, com.github.javaparser.printer.PrettyPrinter> map;
                List l = new ArrayList();
                l.stream().forEach(java.lang.System.out::println);
            }
        }
        _class _c = _class.of(C.class);

        assertEquals(1, $ref.of("org.jdraft$any$").countIn(_c));//find the fully qualified annotation
        //Print.tree(_c.astCompilationUnit() );

        $ref.of("java.lang$any$").printEachTreeIn(_c );
        assertEquals(1, $ref.of("java.lang$any$").countIn(_c));//find the qualified MethdoReference
        //System.out.println( _c );

        //$ref.of("com.github.javaparser$any$").forEachIn()
        assertEquals(13, $ref.of("com.github.javaparser$any$").countIn(_c));
    }
}
