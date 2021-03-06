package org.jdraft.bot;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import junit.framework.TestCase;

import org.jdraft.*;
import org.jdraft.macro._packageName;

import java.util.concurrent.atomic.AtomicInteger;

public class $nameTest extends TestCase {


    //test if I directly use a reference
    public void testDirectUse(){
        //direct use

        //package name
        assertEquals(1, $name.of(JavaParser.class.getPackage().getName())
                .countIn(_package.of(JavaParser.class.getPackage().getName()) ));

        //explicitly omit package name
        assertEquals(0, $name.of(JavaParser.class.getPackage().getName()).$matchPackageNames(false)
                .countIn(_package.of(JavaParser.class.getPackage().getName()) ));

        //in import
        //assertEquals(1, $name.of(JavaParser.class).countIn(_import.of(JavaParser.class) ));

        //Print.tree(_import.of(JavaParser.class));
        assertTrue($name.of(_name.Use.IMPORT_NAME).isIn(_import.of(JavaParser.class).node().getName()));
        //assertTrue($name.of(isImportName(_import.of(JavaParser.class).ast()));

        //explicitly omit imports
        //assertEquals(0, $name.of(JavaParser.class).$matchImports(false).countIn(_import.of(JavaParser.class) ));

        Print.tree(_field.of(JavaParser.class, "i"));

        //type name
        //assertEquals(1, $name.of(JavaParser.class)
        //        .countIn(_field.of(JavaParser.class, "i") ));

        // in array
        assertEquals(0, $name.of("int")
                .countIn(_field.of("int[] i;") ));

        Print.tree(_field.of("Optional<Thingy> i;").node());
        // in generic
        assertEquals(0, $name.of("Thingy")
                .countIn(_field.of("Optional<Thingy> i;") ));

        assertEquals(0, $name.of("org.myproj.Thingy")
                .countIn(_field.of("Optional<org.myproj.Thingy> i;") ));

        // in type Parameter
        assertEquals(0, $name.of("Thingy")
                .countIn(_typeParam.of("<E extends Thingy>") ));

        assertEquals(0, $name.of("Thingy")
                .countIn(_method.of("<A, E extends Thingy> void m(){}") ));

        assertEquals(0, $name.of("org.myproj.Thingy")
                .countIn(_typeParam.of("<E extends org.myproj.Thingy>") ));


        //explicitly omit type name
        //assertEquals(0, $name.of(JavaParser.class).$matchTypeRefNames(false)
        //        .countIn(_field.of(JavaParser.class, "i") ));


        // in array
        //assertEquals(0, $name.of(int.class).$matchTypeRefNames(false)
        //        .countIn(_field.of("int[] i;") ));

        // in generic
        //assertEquals(0, $name.of("Thingy").$matchTypeRefNames(false)
        //        .countIn(_field.of("Optional<Thingy> i;") ));
        //assertEquals(0, $name.of("org.myproj.Thingy").$matchTypeRefNames(false)
        //        .countIn(_field.of("Optional<org.myproj.Thingy> i;") ));

        // in type Parameter
        //assertEquals(0, $name.of("Thingy").$matchTypeRefNames(false)
        //        .countIn(_typeParameter.of("<E extends Thingy>") ));

        //assertEquals(0, $name.of("Thingy").$matchTypeRefNames(false)
        //        .countIn(_method.of("<A, E extends Thingy> void m(){}") ));

        //assertEquals(0, $name.of("org.myproj.Thingy").$matchTypeRefNames(false)
        //        .countIn(_typeParameter.of("<E extends org.myproj.Thingy>") ));


        //variable name
        assertEquals(1, $name.of("varName")
                .countIn(_field.of(JavaParser.class, "varName") ));

        //explicitly omit variable names
        assertEquals( 0, $name.of("varName").$matchVariableNames(false)
                .countIn(_field.of(JavaParser.class, "varName") ));

        Print.tree(_methodRefExpr.of("System.out::println").node());
        /*
        //method referenced
        assertEquals( 1, $name.of("System.out")
                .countIn(_methodRef.of("System.out::println")));

        // explicitly not method reference
        assertEquals( 0, $name.of("System.out").$matchMethodReferences(false)
                .countIn(_methodRef.of("System.out::println")));
        */

        //parameter name
        assertEquals( 1, $name.of("pName")
                .countIn(_param.of("int pName")));

        assertEquals( 0, $name.of("pName").$matchParameterNames(false)
                .countIn(_param.of("int pName")));

        /**Type
        assertEquals( 1, $name.of("pType").$matchParameterNames(false)
                .countIn(_parameter.of("pType pName")));
        */

        assertEquals(1, $name.of("mName")
                .countIn(_method.of("void mName(){}")));

        assertEquals(0, $name.of("mName").$matchMethodNames(false)
                .countIn(_method.of("void mName(){}")));

        assertEquals(1, $name.of("cName")
                .countIn(_constructor.of("cName(){}")));

        assertEquals(0, $name.of("cName").$matchConstructorNames(false)
                .countIn(_constructor.of("cName(){}")));
    }

    public void testPrePostConstains() {
        assertEquals(1, $name.startsWith("pre").countIn(_import.of("pre")));
        assertEquals(0, $name.startsWith("pre").$exclude(_name.Use.IMPORT_NAME, _name.Use.ENUM_DECLARATION_NAME).countIn(_import.of("pre")));
        assertEquals(0, $name.startsWith("pre").$exclude(_name.Use.IMPORT_NAME).countIn(_import.of("pre")));
    }

    public void testStartsWith(){
        assertFalse($name.startsWith( "pre").matches("pr"));

        assertTrue($name.startsWith( "pre").matches("pre"));
        assertFalse($name.startsWith( "pre").matches("Pre")); //case sensistive
        assertTrue($name.startsWith( "pre").matches("preOrder"));
        assertFalse($name.startsWith( "pre").matches("PreOrder"));
    }

    public void testEndsWith(){
        assertFalse($name.endsWith( "Post").matches("pos"));
        assertFalse($name.endsWith( "Post").matches("ost"));

        assertTrue($name.endsWith( "Post").matches("Post")); //exact

        assertFalse($name.endsWith( "Post").matches("post")); //case sensistive
        assertTrue($name.endsWith( "Post").matches("isPost"));
        assertFalse($name.endsWith( "Post").matches("Poster"));
    }

    public void testContains(){
        assertTrue( $name.contains("contain").matches("contain"));
        assertTrue( $name.contains("contain").matches("b4contain"));
        assertTrue( $name.contains("contain").matches("containAfter"));
        assertTrue( $name.contains("contain").matches("b4containAfter"));

        assertFalse( $name.contains("contain").matches("Contain"));
        assertFalse( $name.contains("contain").matches("CONTAIN"));
        assertFalse( $name.contains("contain").matches("contMIDDLEain"));

    }
    /* move this use case to _qualifiedName for the time being
    public void testFullyQualified(){
        class C{
            java.lang.String s = "";
            java.util.Map<java.lang.Integer, java.lang.Float> map = new java.util.HashMap<>();
        }
        assertTrue($name.of("java.lang.$any$").matches(Ast.name("java.lang.String")));
        //SimpleName nm = .getName();

        ClassOrInterfaceType coit = Ast.typeRef("java.lang.String").asClassOrInterfaceType();
        System.out.println( coit );
        Ast.describe( coit );

        assertEquals(1, $name.of("java.lang.$any$").listIn(coit).size());

        assertEquals(5, $name.of("java.lang.$any$").countIn(C.class));
    }

     */

    /*
    interface II <A extends Serializable>{ }
    public void testGenerics(){

        class B<I extends Serializable>{ }
        //Print.tree(B.class);
        assertEquals(0, $name.of("B").$exclude(_name.Use.CLASS_NAME).countIn(B.class));
        assertEquals(1, $name.of("B").countIn(B.class));


        assertEquals(1, $name.of("I").countIn(B.class));
        assertEquals(1, $name.of("Serializable").countIn(B.class));


        class C extends B<String> implements II<String>{
            <A extends B> void  t() throws IOException {
                List<Integer> al = new ArrayList<>();
                try{
                    throw new IOException();
                }catch( RuntimeException | IOException e){ }
            }
        }
        assertEquals(1, $name.of("C").countIn(C.class));
        assertEquals(2, $name.of("B").countIn(C.class));
        assertEquals(1, $name.of("II").countIn(C.class));
        assertEquals(2, $name.of("String").countIn(C.class));
        assertEquals(1, $name.of("Integer").countIn(C.class));
        assertEquals(1, $name.of("List").countIn(C.class));
        assertEquals(1, $name.of("ArrayList").countIn(C.class));

        assertEquals(1, $name.of("A").countIn(C.class));
        assertEquals(1, $name.of("t").countIn(C.class));
        assertEquals(3, $name.of("IOException").countIn(C.class));
        assertEquals(1, $name.of("RuntimeException").countIn(C.class));
        assertEquals(1, $name.of("e").countIn(C.class));
    }
    */
    /*
    public void testByPattern(){

        //     1
        class C1 {
            //2   3               4             5       6
            Map<String, ? extends Serializable> m = new HashMap<>();
        }

        //{Map, String, Serializable, HashMap}
        assertEquals( 4, $name.of().$and(n-> n.isTypeName()).countIn(C1.class));

        assertEquals( 6, $name.of().countIn(C1.class));
        assertEquals( 1, $name.of("C1").countIn(C1.class));
        assertEquals( 1, $name.of("Map").countIn(C1.class));
        assertEquals( 1, $name.of("String").countIn(C1.class));
        assertEquals( 1, $name.of("Serializable").countIn(C1.class));
        assertEquals( 1, $name.of("m").countIn(C1.class));
        assertEquals( 1, $name.of("HashMap").countIn(C1.class));
    }

     */

    public void testTT(){
        @_packageName("ffff.lang.dddd")
        class G{
            //java.lang.String s;
        }
        Print.tree(_class.of(G.class).astCompilationUnit());

        _class _c = _class.of(G.class);
        assertTrue( _name.of(_c.getNameNode()).isTypeDeclarationName() );

        assertEquals(0, $name.of("lang").$matchPackageNames(false).countIn(G.class));

        //$name.of("lang").forEachIn(G.class, )
        assertEquals(0, $name.of("lang")
                .$matchAnnoMemberValueNames(false)
                .$matchAnnoNames(false)
                .$matchConstructorNames(false)
                .$matchImports(false)
                .$matchMethodNames(false)
                .$matchPackageNames(false)
                .$matchVariableNames(false)
                .$matchMethodReferences(false)
                .$matchTypeDeclarationNames(false)
                .$matchTypeDeclarationNames(false)
                .countIn(G.class));

    }

    /*
    public void testMiddleName(){

        _import _i = $name.of("bbbb").forEachIn(_import.of("import aaaa.bbbb.C"),
                new $name.$nameConsumer().onName(n -> n.setId("HEY")));
        System.out.println( _i );
        Print.tree( _i.ast() );
        assertEquals(_import.of("aaaa.HEY.c"), _import.of("aaaa.HEY.c"));

        assertTrue( _i.equals(_import.of("aaaa.HEY.C")));
        assertTrue( _i.is("aaaa.HEY.C;"));
        assertTrue( _i.is("import aaaa.HEY.C;"));



        @_packageName("base.sub.end")
        class V{
            java.lang.String s = new java.lang.String();
        }

        Print.tree(V.class);
        assertEquals(1, $name.of("base$any$")//.$matchInnerPackages()
                .countIn(V.class));

        _class _c = _class.of(V.class);
        System.out.println( _c );

        assertEquals(2, $name.of("lang")//.$matchQualifierPackages()
                .countIn(_c));

        assertEquals(2, $name.of("java")//.$matchInnerPackages()
                .countIn(_c));

        //"parts" of an import

        assertEquals(1, $name.of("$before$sub$after$")//.$matchInnerPackages()
                .countIn(_c));
        assertEquals(1, $name.of("$before$end")//.$matchInnerPackages()
                .countIn(_c));
    }
*/

    /*
    public void testSelectInImport(){
        assertEquals(1, $name.of("print").countIn(_import.of("import aaaa.bbbb.print;").ast()));
        _class _c = _class.of("G").addImports("aaa.bbb.print");
        //System.out.println( _c );

        assertEquals(1, $name.of("print").countIn(_c));
        $name.of("print").forEachIn(_c, n-> {
            if(n.name instanceof SimpleName){
                n.name.replace(new SimpleName("replaced"));
            }
            if(n.name instanceof Name){
                Name nm = (Name)n.name;
                nm.setId("replaced");
            }
            if(n.name instanceof MethodReferenceExpr){
                MethodReferenceExpr mre = (MethodReferenceExpr) n.name;
                mre.setId("replaced");
            }
        });

        assertEquals( 1, $name.of("bbbb").countIn(_import.of("import aaaa.bbbb.C")));
        _import _i = $name.of("bbbb").forEachIn(_import.of("import aaaa.bbbb.C"),
                new $name.$nameConsumer().onName(n -> n.setId("HEY")));

        System.out.println( _i );
    }
*/

    /*
    public void testSelectMethodReference(){
        MethodReferenceExpr mre = Expressions.methodReferenceEx("A::B");

        System.out.println( mre );
        Print.tree(mre);
        assertEquals( 1, $name.of("A").countIn( mre ));
        assertEquals( 1, $name.of("B").countIn( mre ));
    }
     */

    public void testName(){
        $name $n = $name.of("C");
        assertTrue($n.matches( new Name("C")));
        assertFalse($n.isQualified());

        $name $qual = $name.of("aaaa.C");
        assertTrue($qual.isQualified());


        assertTrue($n.matches( new Name( new Name("aaaa"), "C")));

        Name n = Ast.name("aaa.C");

        assertTrue(n.getQualifier().isPresent());
    }

    public void testResolve(){

        // 8// import to be added
        class X{
            int[] print = {0}; // 1) a variable name

            //2) a method name
            void print(){
                //3) a qualified method name
                System.out.print(1);
            }

            //4) a class name
            class print{
                //5) a constructor name
                print(){ }

                //6) method declaration name
                print g(){

                    //7 method call name
                    return new print();
                }
            }
        }

        _class _c = _class.of(X.class);
        //lets verify that I update the imports, so add an import
        _c.addImports("aaaa.bbbb.print;"); //0) an import


        System.out.println( _c );
        //Ast.describe(_import.of("aaaa.bbbb.print;").ast());

        //Ast.describe( _c.astCompilationUnit() );
        /*
        AtomicInteger ai = new AtomicInteger();
        $name.of("print").forEachIn(_c, c-> {
            if( c.name instanceof SimpleName ){
                c.name.replace( new SimpleName("print"+ ai.incrementAndGet()));
            }
            if( c.name instanceof Name){
                c.name.replace( new Name("print"+ ai.incrementAndGet()));
            }
            //System.out.println(">>>>"+c.ast()+" |||  "+c.ast().getParentNode().get()+" "+c.ast().getClass() )
        });
        */
        //System.out.println( _c );

        //$name.of("print").forEachIn(_c, c-> System.out.println(">>>>"+c.ast()+" |||  "+c.ast().getParentNode().get()+" "+c.ast().getClass() ));

        //assertEquals( 8, $name.of("print").countIn(_c));

        $name.of().forEachIn(_c.astCompilationUnit(), n->{
            if( n.node() instanceof Name){
                Name nm = (Name)n.node();
                System.out.println("NAME ****ID " + nm.getId() );

                //n.ast().replace( new Name("changed") );
            }
            if( n.node() instanceof SimpleName){
                System.out.println("SIMPLENAME **** " + n );

            }
        });

         $name.of("print").forEachIn( _c.astCompilationUnit(), n-> {
            if( n.node() instanceof Name){
                Name nm = (Name)n.node();
                System.out.println("NAME **** " + nm.getId() );

                n.node().replace( new Name("changed") );
            }
            if( n.node() instanceof SimpleName){
                n.node().replace( new SimpleName("changed") );
            }
        });

        //System.out.println( _c );

        //verify we changed them all
        //assertEquals( 8 , $name.of("changed").countIn(_c));

        //$import.of("import $any$.print;").removeIn(_c);
        //_c.addImplement("hey");
        System.out.println( _c );
    }

    public void testS(){
        $name $n = $name.of("x");
        assertTrue($n.matches("x"));
        assertTrue($n.matches(new Name("x")));
        assertTrue($n.matches(new SimpleName("x")));

        assertFalse($n.matches("y"));
        assertFalse($n.matches(new Name("y")));
        assertFalse($n.matches(new SimpleName("y")));

        assertNotNull($n.select("x"));
        assertNotNull($n.select(new Name("x")));
        assertNotNull($n.select(new SimpleName("x")));

        assertNull($n.select("y"));
        assertNull($n.select(new Name("y")));
        assertNull($n.select(new SimpleName("y")));


    }

    public void testsE(){
        class C{ //1
            C(){} //2
            int C = 3; //3
            public C getC(){ //4
                return new C(); //5
            }
        }
        //$name.of("C").forEachIn(C.class, )
        AtomicInteger ai = new AtomicInteger();

        $name.of("C").forEachIn(C.class, c -> System.out.println( ai.incrementAndGet()+ ">>" + c.node().getParentNode().get() ));

        $name.of("C").forEachIn(C.class, c -> {
            System.out.println( c.node.getClass());
            if( c.node instanceof Name ){

            }
            if( c.node instanceof SimpleName ){

            }
        });

        //assertEquals(5, $name.of("C").countIn(C.class));
    }

    public void testNameAsExpression(){
        class C{
            int[] a = {1,2,3,4};
            int b = a[0];
            void p(){
                System.out.println(1);
            }
        }

        Print.tree(Ast.of(C.class));
       //System.out.println( _c );
        assertEquals(1, $name.of("println").countIn(C.class));
        assertEquals(2, $name.of("a").countIn(C.class));

        _class _c = $name.of("a").forEachIn(C.class, n->{
            if( n.node() instanceof SimpleName ){
                n.node().replace(new SimpleName("c"));
            }
            //if( n.ast() instanceof Expression){
            //    n.ast().replace(new NameExpr("c"));
            //}
            if( n.node() instanceof Name){
                n.node().replace(new Name("c"));
            }
        });

    }
}
