package org.jdraft.runtime;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import org.jdraft.*;
import org.jdraft.macro.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.BodyDeclaration;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import junit.framework.TestCase;
import org.jdraft.proto.$;
import org.jdraft.proto.$$;
import org.junit.Assert;

/**
 * @author Eric
 */
public class _runtimeTest extends TestCase {

    public static class GreetingInterceptor {
        public Object greet(Object argument) {
            return "Hello from " + argument;
        }
    }

    /**
     * https://github.com/raphw/byte-buddy#a-more-complex-example
     */
    public void testRuntimeClass() throws Exception {
        Class<? extends Function> f = (Class<? extends Function>)_runtime.Class(
                _class.of("ByteBuddy").imports(GreetingInterceptor.class)
                .implement( new Function(){
                    @Override
                    public Object apply(Object o) {
                        return new GreetingInterceptor().greet(o);
                    }
            }));
        String s = (String)f.newInstance().apply("ByteBuddy");
        assertEquals( "Hello from ByteBuddy", s);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface _timed{

        public static final Act A = new Act();

        class Act extends macro<_timed, Node>{
            public Act(){
                super(_timed.class);
            }

            public void expand(Node n){
                if( n instanceof NodeWithBody ){
                    MacroAnno.to( (_body._hasBody) _java.of(n) );
                }
                //if( n instanceof NodeWithOptionalBlockStmt)
            }
        }
         class MacroAnno implements _macro<_member> {

             @Override
             public _member apply( _member _m ){
                if( _m instanceof _body._hasBody ){
                    return (_member)to( (_body._hasBody)_m);
                }
                if( _m instanceof _type ){
                    System.out.println( "TYPED " );
                }
                return _m;
             }

             public static <_HB extends _body._hasBody> _HB to(_HB _m) {
                 //add this as the first statement of the method
                 _m.add(0, ()-> {long start = System.currentTimeMillis();} );

                 //if the method is "empty": dont time nothing
                 if(!_m.hasBody() ){
                     return _m;
                 }
                 //go through each return statement and preface it
                 if( $$.returnStmt().count(_m) > 0 ) {
                     $$.returnStmt().forSelectedIn(_m, sel-> {
                           //replace it with a labeled statement... then flatten
                           LabeledStmt ls = Stmt.labeledStmt(
                           "$add$ : { System.out.println(\" " + ((_named) _m).getName() + " took \" + (System.currentTimeMillis() - start)); " + sel.astStatement.toString() + " }");

                            sel.astStatement.replace(ls);
                            _java.flattenLabel(_m, "$add$");
                            //Ast.flattenLabel(((_node) _m).ast(), "$add$");
                      });
                 } else{
                     //might be a (not return)
                     _m.add(_m.listStatements().size(),
                             Stmt.of((Integer start) -> System.out.println(" took " + (System.currentTimeMillis() - start))));
                 }
                 return _m;
             }
         }

    }

    /**
     *
     */
    public void testChangingExistingCode(){
        _timed.MacroAnno macro = new _timed.MacroAnno();
        _class _c = _class.of("C", new Object(){
            public void m(){
                System.out.println( "Run here ");
            }

            public int m2(int i){
                if( i==1 ){
                    return 1;
                }
                return 2;
            }
        });
        _method _mm = macro.to(_c.getMethod("m"));
        System.out.println( _mm );

        _mm = macro.to(_c.getMethod("m2"));
        System.out.println( _mm );
    }


    /**
     * This is a potpourri of features in draft-java & draft-java-adhoc
     * here we: 
     * 1) build a _class _c by reading the source of an anonymous class
     * 2) process macro annotation (@_final) to modify a field "a"
     * 3) post process another macro _autoDto to build get/set/equals/hashcode
     * 4) use _adhoc to compile, load, create a new instance and return a Proxy
     * 5) call get to retrieve the field value of "i"
     * 6) build another proxy instance p2 from passing in 100
     * 7) verify equals, hashcode, and toString are same for the (p, p2)
     * 8) change a field "name" on p2
     * 9) verify equals, hashcode and toString no longer ==
     * 10) call static "twoX" method
     * 11) call instance "threeX" method on p
     * 12) retrieve the _cd (the source code model) instance from the proxy
     * 13) verify the instance of _cd equals _c (the model that created the class)
     */
    public void testMacrosAndProxy(){
        _class _c = _class.of("A", 
            new @_dto Serializable(){ //_dto adds constructor, equals, hashcode, toString)
                @_final int a; //final field will need int in (generated) constructor
                //public String name = "Fido";
                List bb;
                //String nn = "Fido";
                
                public @_static int twoX(int val){
                    return val*2;
                }
            
                public int threeX(int val){
                    return val * 3;
                }
            });

        System.out.println( _c );
        //compile, load, and create a new instance of _c with 100 arg
        _proxy<Serializable, _code> p = _runtime.of(_c).proxy(_c, 100);
        assertEquals(100, p.get("a"));
        
        
        _proxy<Serializable, _code> p2 = p.of(100); //build me another proxy/instance w 100 
        assertEquals(p, p2); //verify equals works
        assertEquals( p.hashCode(), p2.hashCode()); //verify hashCode works
        assertEquals( p.toString(), p2.toString()); //verify toString works
        
        //change p2 to make sure the equals and hashcode are different
        p2.set("bb", new ArrayList<>());
        
        assertEquals(new ArrayList<>(), p2.get("bb"));
        
        Assert.assertNotEquals(p, p2); //verify equals diff
        Assert.assertNotEquals( p.hashCode(), p2.hashCode()); //verify hashCodes diff
        Assert.assertNotEquals( p.toString(), p2.toString()); //verify toStrings diff
        
        assertEquals(200, p.call("twoX", 100)); //call static method
        assertEquals(300, p.call("threeX", 100)); //call instance method
        
        //we can get the codeModel
        _code _cd = p.get_class();
        assertEquals( _c, _cd); //make sure the models are the same             
    }
    
    public void testCompileAllDifferentTypes(){
        _class _c = _class.of("C");
        _interface _i = _interface.of("I");
        _annotation _a = _annotation.of("A");
        _enum _e = _enum.of("E");
        
        List<_classFile> ff = _runtime.compile(_c, _i, _a, _e);
        ff = _runtime.compile(_c, _i, _a, _e);
    }
    
    public void testFileName(){
        _javaFile _cf = new _javaFile(_class.of("C"));
        assertEquals( "/C.java", _cf.getName());
        
        @_dto class C {
            public @_static int ID = 100;
            public final int i = 1;                        
        }
        _cf = new _javaFile(_class.of(C.class));
        assertEquals( "/C.java", _cf.getName());
        
    }
    
    public void testLoadClassesWithMacroAnnotations(){
        
    }
    
    // Verify that we can create compile and load with annotation macros
     
    public void testLoadMacroType(){
        @_dto class C {
            public @_static int ID = 100;
            public @_final int i;                        
            
            public @_static int twoX(int val){
                return val*2;
            }
            
            public int threeX(int val){
                return val * 3;
            }
        }
        _class _c = _class.of(C.class);
        System.out.println( _c );
        _runtime ah = _runtime.of(_c);
        
        _proxy p = ah.proxy(_c, 200); //final arg i = 200
        assertEquals( 200, p.get("i")); //verify
        assertEquals( 100, p.get("ID")); //get static method
        assertEquals( p, ah.proxy(_c, 200)); //verify equals method
        assertEquals( p.hashCode(), ah.proxy(_c, 200).hashCode()); //verify hashcode
        
        assertEquals(20, p.call("twoX", 10)); //call static method
        assertEquals(30, p.call("threeX", 10)); //call instance method                
    }
    
    public void testCompileModuleInfoPackageInfoAndClass(){
        CompilationUnit modInfo = StaticJavaParser.parse("");
        modInfo.setModule("aaaa");
        
        CompilationUnit AAClass = StaticJavaParser.parse(
            "package aaaa;"+ System.lineSeparator()+"public class AA{}");
        
        CompilationUnit pkgInfo = StaticJavaParser.parse( 
            "package aaaa;"+System.lineSeparator()+"/** Some comments */");
        
        //_draftAdhoc astCompiler = new _draftAdhoc();
        List<_classFile> byteCodeMap = _runtime.compile(AAClass, modInfo, pkgInfo);
        assertEquals( 2, byteCodeMap.size());
    }
    
    /** This is performing load the Manual way... you should use _adhoc instead*/
    public void testLoadManual(){
        CompilationUnit ast = StaticJavaParser.parse("public class GGGG{ public static final int i=1000; }");        
        // here you can manually build an AstFile from the compilationUnit
        _runtime adhoc = _runtime.of( ast );
        Class clazz = adhoc.getClass("GGGG");
        assertNotNull( clazz );        
        
        // the compilationUnit is still mutable, if we change the name of the type
        // the name of the file / class changes
        ast.getType(0).setName("HHHH");
        adhoc = _runtime.of( ast );
        clazz = adhoc.getClass("HHHH");
        assertNotNull( clazz );
        
        // change the package and the file/class changes
        // i.e. we can manipulate the underlying AST and not have 
        // to parse/manipulate Strings (we get all the conveniece of
        // having a mutable model AST and we can 
        ast.setPackageDeclaration("aaaa.bbbb");
        adhoc = _runtime.of( ast );
        clazz = adhoc.getClass("aaaa.bbbb.HHHH");
        assertNotNull( clazz );        
    }
    
    public void testLoadAdhocAst(){
        CompilationUnit ast = StaticJavaParser.parse("public class GGGG{ public static final int i=1000; }");
        _runtime adhoc = _runtime.of(ast);//compile & load
        assertNotNull( adhoc.getClass("GGGG") );
        
        assertEquals(1000, adhoc.getFieldValue("GGGG", "i")); //get field value
        
        ast.getType(0).setName("HHHH");
        adhoc = _runtime.of(ast);//compile & load
        assertNotNull( adhoc.getClass("HHHH") );
        
        ast.setPackageDeclaration("aaaa.bbbb");
        adhoc = _runtime.of(ast); //compile & load
        assertNotNull( adhoc.getClass("aaaa.bbbb.HHHH") );
        
        
        //here add a public static field        
        // public static int ID = 100;
        ast.getType(0).addField(int.class, "ID", Keyword.PUBLIC, Keyword.STATIC)
            .getVariable(0).setInitializer("100");
        
        adhoc = _runtime.of(ast); //compile & load
        
        //get static field value from the compiled/loaded 
        assertEquals(100, adhoc.getFieldValue("aaaa.bbbb.HHHH", "ID")); 
                
        //add a public static no-arg method
        BodyDeclaration<?> bd = StaticJavaParser.parseBodyDeclaration("public static int val() { return 12345; }");        
        ast.getType(0).addMember(bd);
        
        adhoc = _runtime.of(ast); //compile & load
        assertEquals(12345, adhoc.call("aaaa.bbbb.HHHH", "val")); //call method        
    }

    public void testCallMain(){        
        _runtime adhoc = _runtime.of(
            "public class A{",
            "    public static void main(String[] args){ ", 
            "        System.setProperty(\"aaaa\", \"bbbb\");",
            "    }",
            "}");
        
        adhoc.main( "A" );//run the main method on A
        assertEquals( System.getProperty("aaaa"), "bbbb"); //verify main method was called
        
        System.clearProperty("aaaa");
    }
    
    public void testLoadAndNewInstanceFromString(){
        _runtime adhoc = _runtime.of(
                "public class G{",  
                "    final int i;",
                "    public G(int i){",
                "        this.i = i;",
                "    }",
                "}");        
        assertNotNull( adhoc.instance("G", 100) );        
    }
    
    public void testLoadAndNewInstanceNoArgs(){
        
        //test no args
        CompilationUnit cu = StaticJavaParser.parse("public class G{ }");
        _runtime adhoc = _runtime.of(cu);
        Class clazz = adhoc.getClass("G");
        Object inst = adhoc.instance("G");
        assertNotNull( inst );
        inst = adhoc.instance(clazz);        
        assertNotNull( inst );
        assertNotNull( clazz );        
    }
    
    public void testLoadAndNewInstanceWithArg(){
        
        //test no args
        CompilationUnit cu = StaticJavaParser.parse("public class G{ public G(int i) {} }");
        _runtime adhoc = _runtime.of(cu);
        Class clazz = adhoc.getClass("G");
        Object inst = adhoc.instance("G", 100);
        assertNotNull( inst );
        inst = adhoc.instance(clazz, 100);        
        assertNotNull( inst );
        assertNotNull( clazz );        
    }
    
    /**
     * We might want the flexibility of "passing around" an AdHoc, and extracting 
     * the AST
     */
    public void testLoadAndGetCompilationUnitOut(){
        _runtime adhoc = _runtime.of(
            "package aaaa;", 
            "public class FF{",
            "    public int gg = 100;",
            "    public FF(int gg){",
            "        this.gg = gg;",
            "    }",
            "}");        
        
        System.out.println( adhoc );
        System.out.println( adhoc.getFileManager().classLoader.classNameTo_javaFile);
        //the long way to do it:
        _javaFile cmf = 
            adhoc.getFileManager().classLoader.classNameTo_javaFile.get("aaaa.FF");
        assertNotNull(cmf);
        //CompilationUnit cu = cmf.getCodeModel();
        _code _c = cmf.codeModel;
        assertNotNull(_c);
        
        //this is how you can get it
        //cmf = adhoc.getCodeModelFile("aaaa.FF");
        _c = adhoc.get_code("aaaa.FF");
        
        assertNotNull(cmf);
        assertNotNull(_c);
        assertNotNull( adhoc.get_code("aaaa.FF"));        
    }
    
    /*** Compile tests */
    
    public void testSingleAstCompilationUnitTypeNoPackage(){
        _javaFile astFo = 
            new _javaFile( StaticJavaParser.parse("public class AAA{}"));   
        
        List<_classFile> bytecode = _runtime.compile(astFo);
        assertTrue( bytecode.size() == 1);
        assertEquals( "AAA", bytecode.get(0).getFullyQualifiedClassName());
    }
    
    public void testSingleAstCompilationUnitTypeInPackage(){
        _javaFile astFo = 
            new _javaFile( StaticJavaParser.parse("package aaaa; "+System.lineSeparator()+"public class AAA{}"));   
        
        List<_classFile> bytecode = _runtime.compile(astFo);
        assertEquals( "aaaa.AAA", bytecode.get(0).getFullyQualifiedClassName());        
        assertTrue( bytecode.size() == 1);
    }
    
    /**
     * make sure the name of the file for a package private class  (ALONE) is
     * the name of the file
     */
    public void testSinglePackagePrivateClass(){
        _javaFile astPPC = 
            new _javaFile( StaticJavaParser.parse("class AAA{}"));   
        
        List<_classFile> bytecode = _runtime.compile(astPPC);
        
        assertEquals("AAA", bytecode.get(0).getFullyQualifiedClassName());
        assertTrue( bytecode.size() == 1);
    }
    
    /**
     * Make sure a compilationUnit with a package private class and a public class gets the name of the public class
     */
    public void testCompileCompanionClass(){
        _javaFile astPPC = 
            new _javaFile( StaticJavaParser.parse("class AAA{}"+ System.lineSeparator()+"public class BBB{}") );   
        
        List<_classFile> bytecode = _runtime.compile(astPPC);
        
        
        bytecode.forEach(b -> System.out.println( b.getFullyQualifiedClassName() ));
        
        assertEquals("AAA", bytecode.get(0).getFullyQualifiedClassName());
        assertEquals("BBB", bytecode.get(1).getFullyQualifiedClassName());
        assertTrue( bytecode.size() == 2);
    }
    
    /**
     * Make sure if we compile a package-info class (alone) 
     * 
     */
    public void testCompileOnlyPackageInfo(){
        _javaFile astPI = 
            new _javaFile( StaticJavaParser.parse("/** a package info*/"+System.lineSeparator()+"package aaaa;"));   
        List<_classFile> bytecode = _runtime.compile(astPI);
        //package info files are not parsed
        assertTrue( bytecode.isEmpty() );        
    }
    
    /**
     * Pass a module-info into the 
     */
    public void testCompileOnlyModuleInfo(){
        CompilationUnit cu = StaticJavaParser.parse("");
        cu.setModule("aaaa");
        _javaFile astMI = new _javaFile( cu );   
        List<_classFile> bytecode = _runtime.compile(astMI);
        
        //passing a module-info file will get compiled
        assertNotNull( "module-info", bytecode.get(0).getFullyQualifiedClassName() );                
    }    
    
    
}
