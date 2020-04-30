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

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class SrefTest extends TestCase {

    public void testPrePostConstains() {
        assertEquals(1, $ref.startsWith("pre").countIn(_import.of("pre")));
        assertEquals(0, $ref.startsWith("pre").$exclude(_name.Use.IMPORT_NAME, _name.Use.ENUM_NAME).countIn(_import.of("pre")));
        assertEquals(0, $ref.startsWith("pre").$exclude(_name.Use.IMPORT_NAME).countIn(_import.of("pre")));
    }

    public void testStartsWith(){
        assertFalse($ref.startsWith( "pre").matches("pr"));

        assertTrue($ref.startsWith( "pre").matches("pre"));
        assertFalse($ref.startsWith( "pre").matches("Pre")); //case sensistive
        assertTrue($ref.startsWith( "pre").matches("preOrder"));
        assertFalse($ref.startsWith( "pre").matches("PreOrder"));
    }

    public void testEndsWith(){
        assertFalse($ref.endsWith( "Post").matches("pos"));
        assertFalse($ref.endsWith( "Post").matches("ost"));

        assertTrue($ref.endsWith( "Post").matches("Post")); //exact

        assertFalse($ref.endsWith( "Post").matches("post")); //case sensistive
        assertTrue($ref.endsWith( "Post").matches("isPost"));
        assertFalse($ref.endsWith( "Post").matches("Poster"));
    }

    public void testContains(){
        assertTrue( $ref.contains("contain").matches("contain"));
        assertTrue( $ref.contains("contain").matches("b4contain"));
        assertTrue( $ref.contains("contain").matches("containAfter"));
        assertTrue( $ref.contains("contain").matches("b4containAfter"));

        assertFalse( $ref.contains("contain").matches("Contain"));
        assertFalse( $ref.contains("contain").matches("CONTAIN"));
        assertFalse( $ref.contains("contain").matches("contMIDDLEain"));

    }

    public void testGenericTypeRefName(){
        //assertEquals(2, $ref.of().countIn(_typeRef.of("HashMap<>").ast()));
        //Print.tree(Types.of("HashMap<>"));
        //assertEquals(2, $ref.of($ref.TYPE_REF_NAME).countIn(_typeRef.of("HashMap<>").ast()));
        //Print.tree(_new.of("new HashMap<>()").ast());
        assertEquals(2, $ref.of($ref.TYPE_REF_NAME).countIn(_new.of("new HashMap<>()").ast()));


    }
    public void testByUse(){

        //     1
        class C1 {
          //3   4                 6             7       9
            Map<String, ? extends Serializable> m = new HashMap<>();
          //2----------------------------------         8--------
          //            5----------------------
        }
        // Here are ALL of the $refs:
        // 1) C1
        // 2) Map<String, ? extends Serializable>
        // 3) Map
        // 4) String
        // 5) ? extends Serializable
        // 6) Serializable
        // 7) m
        // 8) HashMap<>
        // 9) HashMap

        //Here is the AST:
        //"public class C1 {...}" CompilationUnit : (1,1)-(6,3)
        //└─"public class C1 {...}" ClassOrInterfaceDeclaration : (2,1)-(6,1)
        //  ├─"public" Modifier : (2,1)-(2,6)
        //  ├─"C1" SimpleName : (2,14)-(2,15)
        //  └─"Map<String, ? extends Serializable> m = new HashMap<>();" FieldDeclaration : (5,5)-(5,60)
        //    └─"m = new HashMap<>()" VariableDeclarator : (5,41)-(5,59)
        //      ├─"Map<String, ? extends Serializable>" ClassOrInterfaceType : (5,5)-(5,39)
        //      │ ├─"Map" SimpleName : (5,5)-(5,7)
        //      │ ├─"String" ClassOrInterfaceType : (5,9)-(5,14)
        //      │ │ └─"String" SimpleName : (5,9)-(5,14)
        //      │ └─"? extends Serializable" WildcardType : (5,17)-(5,38)
        //      │   └─"Serializable" ClassOrInterfaceType : (5,27)-(5,38)
        //      │     └─"Serializable" SimpleName : (5,27)-(5,38)
        //      ├─"m" SimpleName : (5,41)-(5,41)
        //      └─"new HashMap<>()" ObjectCreationExpr : (5,45)-(5,59)
        //        └─"HashMap<>" ClassOrInterfaceType : (5,49)-(5,57)
        //          └─"HashMap" SimpleName : (5,49)-(5,55)

        //Print.tree(C1.class);

        //{Map, String, Serializable, HashMap}
        //$ref.of().$and(_name.Use.CLASS_NAME, _name.Use.TYPE_REF_NAME).printIn(C1.class);
        $ref.of().$and(_name.Use.CLASS_NAME, _name.Use.TYPE_REF_NAME).printIn(C1.class);
        assertEquals( 8, $ref.of().$and(_name.Use.CLASS_NAME, _name.Use.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 8, $ref.of($ref.CLASS_NAME, $ref.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 7, $ref.of($ref.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of($ref.CLASS_NAME).countIn(C1.class));

        //simplified API
        assertEquals( 8, $ref.of($ref.CLASS_NAME, $ref.TYPE_REF_NAME).countIn(C1.class));

        //System.out.println( " FOUND ");
        $ref.of().printIn(C1.class);

        assertEquals( 9, $ref.of().countIn(C1.class));
        assertEquals( 1, $ref.of($ref.VARIABLE_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("C1").countIn(C1.class));
        assertEquals( 1, $ref.of("C1", $ref.CLASS_NAME).countIn(C1.class));
        assertEquals( 0, $ref.of("C1", $ref.IMPORT_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("C1").$matchTypeDeclarationNames(true).countIn(C1.class));
        assertEquals( 1, $ref.of("Map").countIn(C1.class));
        assertEquals( 1, $ref.of("Map", $ref.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("String").countIn(C1.class));
        assertEquals( 1, $ref.of("String", $ref.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("Serializable").countIn(C1.class));
        assertEquals( 1, $ref.of("Serializable", $ref.TYPE_REF_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("m").countIn(C1.class));
        assertEquals( 1, $ref.of("m", $ref.VARIABLE_NAME).countIn(C1.class));
        assertEquals( 1, $ref.of("HashMap").countIn(C1.class));
        assertEquals( 1, $ref.of("HashMap", $ref.TYPE_REF_NAME).countIn(C1.class));
    }


    public void testMiddleName(){

        @_packageName("base.sub.end")
        class V{
            java.lang.String s = new java.lang.String();
        }

        Print.tree(V.class);
        assertEquals(1, $ref.of("base$any$")
                .countIn(V.class));

        _class _c = _class.of(V.class);
        System.out.println( _c );

        assertEquals(0, $ref.of("lang")
                .countIn(_c));

        assertEquals(2, $ref.contains("lang")
                .countIn(_c));

        assertEquals(2, $ref.startsWith("java")//.$matchInnerPackages()
                .countIn(_c));

        //"parts" of an import

        assertEquals(1, $ref.of("$before$sub$after$")//.$matchInnerPackages()
                .countIn(_c));
        assertEquals(1, $ref.of("$before$end")//.$matchInnerPackages()
                .countIn(_c));
    }

    public interface II <A extends Serializable>{ }
    public void testGenerics(){

        class B<I extends Serializable>{ }
        //Print.tree(B.class);
        assertEquals(0, $ref.of("B").$exclude(_name.Use.CLASS_NAME).countIn(B.class));
        assertEquals(1, $ref.of("B").countIn(B.class));


        assertEquals(1, $ref.of("I").countIn(B.class));
        assertEquals(1, $ref.of("Serializable").countIn(B.class));


        class C extends B<String> implements II<String> {
            <A extends B> void  t() throws IOException {
                List<Integer> al = new ArrayList<>();
                try{
                    throw new IOException();
                }catch( RuntimeException | IOException e){ }
            }
        }
        assertEquals(1, $ref.of("C").countIn(C.class));
        assertEquals(2, $ref.of("B").countIn(C.class));
        assertEquals(1, $ref.of("II").countIn(C.class));
        assertEquals(2, $ref.of("String").countIn(C.class));
        assertEquals(1, $ref.of("Integer").countIn(C.class));
        assertEquals(1, $ref.of("List").countIn(C.class));
        assertEquals(1, $ref.of("ArrayList").countIn(C.class));

        assertEquals(1, $ref.of("A").countIn(C.class));
        assertEquals(1, $ref.of("t").countIn(C.class));
        assertEquals(3, $ref.of("IOException").countIn(C.class));
        assertEquals(1, $ref.of("RuntimeException").countIn(C.class));
        assertEquals(1, $ref.of("e").countIn(C.class));
    }

    public @interface ANN{ }

    public void testNotMatch(){
        //System.out.println( "MI" + $ref.of(JavaParser.class).$matchImports(false).matchImports);

        //;
        //assertEquals(0, $ref.of(JavaParser.class).$matchImports(false).countIn(_import.of(JavaParser.class) ));
    }

    public void testPrint(){
        _java._node _jn = $ref.of(JavaParser.class).firstIn(Ast.field("com.github.javaparser.JavaParser i;"));
        System.out.println( _jn.getClass());
        Print.tree($ref.of(JavaParser.class).firstIn(Ast.field("com.github.javaparser.JavaParser i;")));
        assertTrue(_name.Use.TYPE_REF_NAME.is(_jn.ast()));
        System.out.println(_name.Use.of(_jn.ast()));
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
        //assertTrue($ref.isImportName(_import.of(JavaParser.class).ast().getName()));
        //assertTrue($ref.isImportName(_import.of(JavaParser.class).ast()));
        assertTrue(_name.of(_import.of(JavaParser.class).ast().getName()).isImportName());
        //assertTrue($ref.isImportName(_import.of(JavaParser.class).ast()));

        Print.tree( _import.of(JavaParser.class));

        //explicitly omit imports
        assertEquals(0, $ref.of(JavaParser.class).$matchImports(false).countIn(_import.of(JavaParser.class) ));

        Print.tree(Ast.field("com.github.javaparser.JavaParser i;"));

        //type name
        assertEquals(1, $ref.of(JavaParser.class.getCanonicalName())
                .countIn(Ast.field("com.github.javaparser.JavaParser i;") ));



        assertEquals(1, $ref.of(JavaParser.class)
                .countIn(Ast.field("com.github.javaparser.JavaParser i;") ));

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


        Print.tree(_field.of(JavaParser.class, "i").ast() );
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

        Print.tree(_methodRef.of("System.out::println").ast() );
        // explicitly not method reference
        assertEquals( 0, $ref.of("System.out").$matchMethodReferences(false)
                .countIn(_methodRef.of("System.out::println")));

        //parameter name
        assertEquals( 1, $ref.of("pName")
                .countIn(_parameter.of("int pName").ast()));

        assertEquals( 0, $ref.of("pName").$matchParameterNames(false)
                .countIn(_parameter.of("int pName")));

        Print.tree(_parameter.of("pType pName").ast());

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
