package org.jdraft.adhoc;

import org.jdraft.macro._final;
import org.jdraft.macro._static;
import org.jdraft.macro._dto;
import org.jdraft._class;
import org.jdraft._interface;
import org.jdraft._annotation;
import org.jdraft._enum;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.BodyDeclaration;
import org.jdraft._code;
import java.io.Serializable;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author Eric
 */
public class _adhocTest extends TestCase {
 
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
            new Serializable(){
                @_final int a; //final field will need int in (generated) constructor
                String name = "Fido";
                
                public @_static int twoX(int val){
                    return val*2;
                }
            
                public int threeX(int val){
                    return val * 3;
                }
            }).apply(_dto.$);//_autoDto adds constructor, equals, hashcode, toString)        
        
        //compile, load, and create a new instance of _c with 100 arg
        _proxy<Serializable, _code> p = _adhoc.of(_c).proxy(_c, 100);
        assertEquals(100, p.get("a"));
        
        
        _proxy<Serializable, _code> p2 = p.of(100); //build me another proxy/instance w 100 
        assertEquals(p, p2); //verify equals works
        assertEquals( p.hashCode(), p2.hashCode()); //verify hashCode works
        assertEquals( p.toString(), p2.toString()); //verify toString works
        
        //change p2 to make sure the equals and hashcode are different
        p2.set("name", "Rex");
        
        assertEquals("Rex", p2.get("name"));
        
        Assert.assertNotEquals(p, p2); //verify equals diff
        Assert.assertNotEquals( p.hashCode(), p2.hashCode()); //verify hashCodes diff
        Assert.assertNotEquals( p.toString(), p2.toString()); //verify toStrings diff
        
        assertEquals(200, p.call("twoX", 100)); //call static method
        assertEquals(300, p.call("threeX", 100)); //call instance method
        
        //we can get the codeModel
        _code _cd = p.getCodeModel();        
        assertEquals( _c, _cd); //make sure the models are the same             
    }
    
    public void testCompileAllDifferentTypes(){
        _class _c = _class.of("C");
        _interface _i = _interface.of("I");
        _annotation _a = _annotation.of("A");
        _enum _e = _enum.of("E");
        
        List<_bytecodeFile> ff = _adhoc.compile(_c, _i, _a, _e);
        ff = _adhoc.compile(_c, _i, _a, _e);
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
        _adhoc ah = _adhoc.of(_c);
        
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
        List<_bytecodeFile> byteCodeMap = _adhoc.compile(AAClass, modInfo, pkgInfo);
        assertEquals( 2, byteCodeMap.size());
    }
    
    /** This is performing load the Manual way... you should use _adhoc instead*/
    public void testLoadManual(){
        CompilationUnit ast = StaticJavaParser.parse("public class GGGG{ public static final int i=1000; }");        
        // here you can manually build an AstFile from the compilationUnit
        _adhoc adhoc = _adhoc.of( ast );
        Class clazz = adhoc.getClass("GGGG");
        assertNotNull( clazz );        
        
        // the compilationUnit is still mutable, if we change the name of the type
        // the name of the file / class changes
        ast.getType(0).setName("HHHH");
        adhoc = _adhoc.of( ast );
        clazz = adhoc.getClass("HHHH");
        assertNotNull( clazz );
        
        // change the package and the file/class changes
        // i.e. we can manipulate the underlying AST and not have 
        // to parse/manipulate Strings (we get all the conveniece of
        // having a mutable model AST and we can 
        ast.setPackageDeclaration("aaaa.bbbb");
        adhoc = _adhoc.of( ast );
        clazz = adhoc.getClass("aaaa.bbbb.HHHH");
        assertNotNull( clazz );        
    }
    
    public void testLoadAdhocAst(){
        CompilationUnit ast = StaticJavaParser.parse("public class GGGG{ public static final int i=1000; }");
        _adhoc adhoc = _adhoc.of(ast);//compile & load
        assertNotNull( adhoc.getClass("GGGG") );
        
        assertEquals(1000, adhoc.getFieldValue("GGGG", "i")); //get field value
        
        ast.getType(0).setName("HHHH");
        adhoc = _adhoc.of(ast);//compile & load
        assertNotNull( adhoc.getClass("HHHH") );
        
        ast.setPackageDeclaration("aaaa.bbbb");
        adhoc = _adhoc.of(ast); //compile & load
        assertNotNull( adhoc.getClass("aaaa.bbbb.HHHH") );
        
        
        //here add a public static field        
        // public static int ID = 100;
        ast.getType(0).addField(int.class, "ID", Keyword.PUBLIC, Keyword.STATIC)
            .getVariable(0).setInitializer("100");
        
        adhoc = _adhoc.of(ast); //compile & load        
        
        //get static field value from the compiled/loaded 
        assertEquals(100, adhoc.getFieldValue("aaaa.bbbb.HHHH", "ID")); 
                
        //add a public static no-arg method
        BodyDeclaration<?> bd = StaticJavaParser.parseBodyDeclaration("public static int val() { return 12345; }");        
        ast.getType(0).addMember(bd);
        
        adhoc = _adhoc.of(ast); //compile & load
        assertEquals(12345, adhoc.call("aaaa.bbbb.HHHH", "val")); //call method        
    }

    public void testCallMain(){        
        _adhoc adhoc = _adhoc.of(
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
        _adhoc adhoc = _adhoc.of(
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
        _adhoc adhoc = _adhoc.of(cu);        
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
        _adhoc adhoc = _adhoc.of(cu);        
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
        _adhoc adhoc = _adhoc.of(
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
        
        List<_bytecodeFile> bytecode = _adhoc.compile(astFo);        
        assertTrue( bytecode.size() == 1);
        assertEquals( "AAA", bytecode.get(0).getFullyQualifiedClassName());
    }
    
    public void testSingleAstCompilationUnitTypeInPackage(){
        _javaFile astFo = 
            new _javaFile( StaticJavaParser.parse("package aaaa; "+System.lineSeparator()+"public class AAA{}"));   
        
        List<_bytecodeFile> bytecode = _adhoc.compile(astFo);        
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
        
        List<_bytecodeFile> bytecode = _adhoc.compile(astPPC);
        
        assertEquals("AAA", bytecode.get(0).getFullyQualifiedClassName());
        assertTrue( bytecode.size() == 1);
    }
    
    /**
     * Make sure a compilationUnit with a package private class and a public class gets the name of the public class
     */
    public void testCompileCompanionClass(){
        _javaFile astPPC = 
            new _javaFile( StaticJavaParser.parse("class AAA{}"+ System.lineSeparator()+"public class BBB{}") );   
        
        List<_bytecodeFile> bytecode = _adhoc.compile(astPPC);
        
        
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
        List<_bytecodeFile> bytecode = _adhoc.compile(astPI);
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
        List<_bytecodeFile> bytecode = _adhoc.compile(astMI);
        
        //passing a module-info file will get compiled
        assertNotNull( "module-info", bytecode.get(0).getFullyQualifiedClassName() );                
    }    
    
    
}
