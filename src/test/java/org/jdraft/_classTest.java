package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.Log;
import org.jdraft.macro.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import static java.util.Collections.emptyList;
import static java.util.Collections.sort;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;
import org.jdraft.pattern.$;
import org.jdraft.runtime._runtime;
import test.ComplexClass;
import test.NativeMethod;

/**
 *
 * @author Eric
 */
public class _classTest extends TestCase {

    /**
     * Found this bug... (specifying a private class to parse)
     * it's barf on parsing
     * : org.jdraft._draftException: ErrorParsing :[(line 1,col 1) 'private' is not allowed here.]
     */
    public void testPrivateClassCtor(){
        _class _c = _class.of("private class PRIVATE{}");
    }

    /*
    @Retention( RetentionPolicy.RUNTIME )
    @Target({ ElementType.TYPE, ElementType.TYPE_USE})
    @interface _post{
        //String[] values; maybe this will be method names to call in order
        class AnnoMacro implements macro<_post, Node>{

            @Override
            public _type apply(_type _t) {
                //_t.listMethods(m -> ((_method)m).isType(int.class) );
                _t.removeMembers( _method.class, m-> ((_method)m).getName().equals("_post") );

                return _t;
            }
        }
    }

     */

    public void setUp(){
        Log.setAdapter( new Log.StandardOutStandardErrorAdapter() );
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }

    //@_postDraft
    //@_postDraft(String...methodNames)
    public void testMultiTypeUseMacroAutoAnonymousImport(){

        @_package("aaaa.bbbb") @_equals @_hashCode @_get @_set @_autoConstructor class C{
            public int x,y;
            @_final URI u;

            public @_static AtomicInteger getAi() throws IOException, URISyntaxException {
                throw new IOException("Bad stuff");
            }
            public String toString() {
                return "("+x+","+y+")";
            }
        }
        _class _c = _class.of(C.class);
        /*
        _class _c = _class.of( "aaaa.bbbb.C",
                new @_equals @_hashCode @_get @_set @_autoConstructor Object(){
            public int x,y;
            @_final URI u;

            public @_static AtomicInteger getAi() throws IOException, URISyntaxException {
                throw new IOException("Bad stuff");
            }
            public String toString() {
                return "("+x+","+y+")";
            }
        });
        */
        //make sure when drafting we auto import the appropriate types from the API
        // field types (i.e. URI)
        // method return types (i.e. AtomicInteger)
        // thrown exception types (i.e. IOException, URISyntaxException)
        assertTrue( _c.hasImports(URI.class, AtomicInteger.class, IOException.class, URISyntaxException.class) );
        //System.out.println( _c.getMethod("getU"));
        assertNotNull(
                $.method("public boolean equals( Object $a$);").firstIn(_c));

        System.out.println("OUTPUT " + _c);

        assertNotNull(
                $.method("public URI getU(){ return u; }").firstIn(_c));

    }
    public void testStaticClass(){
        _class _c = _class.of("public static class G{}");
        assertTrue( _c.isStatic() );
    }
    
    public void testHeader(){
        _class _a = _class.of("/* License */",
                "package aaaa.bbbb;",
                "/**",
                " * JavaDoc",
                " */",
                "public class FFF{ }" );
        assertNotNull( _a.getHeaderComment() );
        assertNotNull( _a.getJavadoc() );
        assertEquals( _a.getJavadoc().toString().trim(), 
            Ast.javadocComment(
                "/**", 
                " * JavaDoc", 
                " */").toString().trim() );
        assertEquals( Ast.blockComment("/* License */").toString(), _a.getHeaderComment().toString() );
        //System.out.println( _a.getHeaderComment() );
    }
    
    public void testReadInCopyrightHeader(){
        _class _c = _class.of(ClassWithCopyright.class);
        assertNotNull(_c.getHeaderComment());
    }
    
    public void testSimpleAndShortcutClasses(){
        _class _a = _class.of(
            "package hello;",
            "public class HelloWorld{",
            "public static void main(String[] args){",
            "    System.out.println(\"Hello World\");",
            "}}");
    
    
        //shortcut class & component way
        // shortcut class assumes you pass in the fully qualified NAME of the class
        // sets the package and class NAME
        _class _b = _class.of("hello.HelloWorld")
            .main("System.out.println(\"Hello World\");");
        
        assertEquals( _a, _b);
    }
    
    class Be{        
    }
    
    public void testIsExtends(){
        _type _t = _class.of("C").addExtend("B");
        assertTrue( _t.isExtends("B"));
        assertTrue( _t.isExtends( (ClassOrInterfaceType)_typeRef.of("B").ast() ));
        
        assertFalse( _t.isExtends("D"));
        assertFalse( _t.isExtends(String.class));
        
        _t = _class.of("C").addExtend(Be.class);
        System.out.println( _t );

        assertTrue( _t.isExtends(Be.class));
        assertTrue( _t.isExtends("Be"));
        assertTrue( _t.isExtends(Be.class.getCanonicalName()));        
    }
    
    public void testIsImplements(){
        _type _t = _class.of("C").addExtend("B").implement(Serializable.class);
        System.out.println( _t);
        assertTrue( _t.isImplements(Serializable.class));
        assertTrue( _t.isImplements(Serializable.class.getCanonicalName()));
        assertTrue( _t.isImplements("Serializable"));
    }



    /**
     * Checking isImports for
     */
    public void testClassExtend(){

        //explicitly extending the fully qualified type
        _class _c = _class.of("C").addExtend(java.util.Map.class.getCanonicalName());
        System.out.println( _c);
        assertTrue( _c.isExtends(Map.class));

        //implementing by simple name, _enum is in the same package as interface
        _class _base = _class.of("aaaa.bbbb.Base");
        Class iClass = _runtime.Class(_base); //create me the base class in same package
        _c = _class.of("aaaa.bbbb.C").addExtend("Base");
        assertTrue( _c.isExtends(iClass));

        //implementing by simple name, importing by fully qualified name
        _c = _class.of("C").addExtend("Base").addImports(iClass);
        assertTrue( _c.isExtends(iClass));

        //implementing by simple name, importing by wildcard
        //Might fail...
        _c = _class.of("E").addExtend("Base").addImports("aaaa.bbbb.*");
        assertTrue( _c.isExtends(iClass));

        //test generic extend
        _c = _class.of("E").addExtend("aaaa.bbbb.G<I>");
        //should match
        assertTrue( _c.isExtends("aaaa.bbbb.G<I>"));
        assertTrue( _c.isExtends("aaaa.bbbb.G"));

        //test generic extend
        _c = _class.of("E").addExtend("aaaa.bbbb.G<String>");
        assertTrue( _c.isExtends("aaaa.bbbb.G<String>")); //fully qualified
        assertTrue( _c.isExtends("G<String>")); //not fully qualified
        //assertTrue( _e.isImplements("aaaa.bbbb.G<java.lang.String>"));
    }

    public class Inner{
        
    }
    
    /** verify that is I set the  */
    public void setPackageToInner(){
        
        _class _c = _class.of(Inner.class).setPackage("aaaa.bbbb.dddd");
        assertTrue( _c.isTopLevel() );
        assertEquals( _c.getPackage(), "aaaa.bbbb.dddd" );
    }
    
   public void testNotImport(){
       _class _c = _class.of("C");
       //System.out.println( _c.imports(String.class) );
       _c.addImports(String[].class);
       assertFalse( _c.hasImports(String.class));
   }
            
   interface MemberI{ }
    
    interface $Member{        
        interface MemberMember{}
    }
    
    public static class $Base{
        public static class Mem{
            
        }
    }
    
    /**
     * We previously had issues with classes that started with $ for 
     * implementing and extending (expecially for MEMBER CLASSES)
     * this will test those scenarios
     * Classes that start with a 
     */
    public void testImplementMemberClass(){
        _class _c = _class.of("C")
                .implement(MemberI.class).implement($Member.class).implement($Member.MemberMember.class);
        _c = _class.of("C").addExtend($Base.class);
        _c = _class.of("C").addExtend($Base.Mem.class);
        //System.out.println( _c );
    }
    
     public interface ChumbaWomba{
        Map m(UUID u) throws IOException;
    }
    
    public @interface A{
        
    }
    
    //Instead of manually having to import each class
    // infer the imports from the API
    public void testImplementInferImports(){
        _class _c = _class.of("C").impl(new ChumbaWomba(){
            URI u = null;
            @Override
            public Map m(UUID u) throws IOException {
                return new HashMap<>();
            }            
        });
        
        // when we pass the anonymous ChuimbaWomba instance 
        // in to the implement method...
        assertTrue( _c.hasImport(Map.class));
        assertTrue( _c.hasImport(URI.class));
        assertTrue( _c.hasImport(IOException.class));
        assertTrue( _c.hasImport(UUID.class));
        assertTrue( _c.hasImport(ChumbaWomba.class));
        
        //System.out.println( _c );
    }
    
    public abstract class BBC{
        public abstract Set theSet(URI uri, UUID uuid) throws IOException;
    } 
    
    // When you extend and provide the implementation, we
    // the imports are inferred
    public void testExtendInferImports(){
        _class _c = _class.of("B").addExtend(new BBC(){
                    
            AtomicBoolean ab;
                    
            public Set theSet(URI uri, UUID uuid) throws IOException {
                return new HashSet();
            }
            
        });
       // System.out.println( _c );
        
        assertTrue( _c.hasImport(BBC.class));
        assertTrue( _c.hasImport(AtomicBoolean.class));
        assertTrue( _c.hasImport(Set.class));
        assertTrue( _c.hasImport(UUID.class));
        assertTrue( _c.hasImport(URI.class));
        assertTrue( _c.hasImport(IOException.class));        
    }
    
    // When you extend and provide the implementation, we
    // the imports are inferred
    public void testBodyInferImports(){
        _class _c = _class.of("B").body( new BBC(){
                    
            AtomicBoolean ab;
                    
            public Set theSet(URI uri, UUID uuid) throws IOException {
                return new HashSet();
            }
            
        });
        //System.out.println( _c );
        
        assertTrue( _c.hasImport(BBC.class));
        assertTrue( _c.hasImport(AtomicBoolean.class));
        assertTrue( _c.hasImport(Set.class));
        assertTrue( _c.hasImport(UUID.class));
        assertTrue( _c.hasImport(URI.class));
        assertTrue( _c.hasImport(IOException.class));        
    }
    
    public void testInferLocalClassImportsBasedOnAPI(){
        class Local{
            Map map;
            public UUID uuid() throws IOException{ return UUID.randomUUID();}
        }
        _class _c = _class.of(Local.class);
        
        //System.out.println( _c);
        assertTrue( _c.hasImport(Map.class));// UUID.class, IOException.class) );
        
        
        //System.out.println( _c );
    }
    
    public void testJavadoc(){
        _class _c = _class.of("C").setJavadoc("Oh, Hello");
        assertEquals( "Oh, Hello", _c.getJavadoc().getContent());
    }

    public void testCopyright(){
        _class _c = _class.of( ClassWithCopyright.class );
        Comment c = _c.getHeaderComment();
        //System.out.println( c.getContent() );

        _c.setHeaderComment("The Header Comment");
        //System.out.println( _c );
        assertEquals( _c.getHeaderComment().getContent(), "The Header Comment" );

        _c.setHeaderComment(new BlockComment("The Header Comment"));
        assertEquals( _c.getHeaderComment().getContent(), "The Header Comment" );

        //for type... I should do something that says...
        // get or set the Header Comments

        //System.out.println(  "CONTAINED " + _c.ast().getAllContainedComments());
        //System.out.println( _c.ast().getOrphanComments() );

        /*
        //System.out.println( _c );
        if( _c.ast().getComment().isPresent() ){
            System.out.println( "CompilationUnitComments"+ _c.ast().getComment().get());
        }
        if( _c.astType().getComment().isPresent() ){
            System.out.println( "TypeComments"+ _c.astType().getComment().get());
        }
        System.out.println( _c.getJavadoc() );
        */
    }

    
    public void test11Draft(){
        _class _c = _class.of("com.example.helloworld.HelloWorld", new Object(){
            List<Hoverboard> beyond() {
                List<Hoverboard> result = new ArrayList<>();
                result.add(createNimbus(2000));
                result.add(createNimbus("2001"));
                result.add(createNimbus(THUNDERBOLT));
                sort(result);
                return result.isEmpty() ? emptyList() : result;
            }

            @_remove class Hoverboard implements Comparable {
                public int compareTo(Object o){ return 1;}
            }
            @_remove public Hoverboard createNimbus( int val ){ return null; }
            @_remove public Hoverboard createNimbus( String val ){ return null; }
            @_remove public Hoverboard createNimbus( Hoverboard board ){ return null; }
            @_remove Hoverboard THUNDERBOLT = new Hoverboard();
        }).addImportStatic(Collections.class.getCanonicalName()+".*",
                "com.mattel.Hoverboard.Boards.*",
                "com.mattel.Hoverboard.createNimbus.*");
        //System.out.println( _c);
    }
    
    
    public void testMethodParameterTypes(){
        class L{
            public void sortByLength(List<String> strings) {
            }
        }
        _method _m = _class.of(L.class).getMethod("sortByLength");

        //System.out.println( );
        //Arrays.stream( L.class.getMethods()[0].getGenericParameterTypes() ).forEach(t -> System.out.println("&&&&&&& " + t));
        //assertTrue(_m.hasParametersOfType(L.class.getMethods()[0].getGenericParameterTypes()));
        assertTrue(_m.hasParametersOf(L.class.getMethods()[0]));
        //assertTrue(_m.hasParametersOfType(L.class.getMethods()[0].getParameterTypes()));
    }

     public void testSimpleAssign(){
        _class _c = _class.of("public abstract class HelloWorld");
        //note... the above SHOULD WORK without {}
    }

    // verify that, when I define a class via the body of an anonymous object
    // the field types,
    // method return types
    // method parameter types
    // thrown exception types
    // are imported automatically into the _class
    public void testAutoImportInferredFromAnonymousObject(){
        _class _c = _class.of("aaa.gg.FF", new Serializable(){
            Map f;

            public List m(Set s, UUID uuid) throws IOException {
                return null;
            }
        });
        
        //System.out.println( _c);
        assertTrue( _c.hasImport(Serializable.class)); //interface implemented
        assertTrue( _c.hasImport(Map.class)); //field type
        assertTrue( _c.hasImport(List.class));
        assertTrue( _c.hasImport(Set.class));
        assertTrue( _c.hasImport(UUID.class));
        assertTrue( _c.hasImport(IOException.class));
    }

    public interface MyI{
        void m();
    }

    public void testImplement(){
        _class _c = _class.of("C").impl(new MyI(){
            public void m(){
                System.out.println(1);
            }
        });
        System.out.println( _c );

        _c.forFields( f-> f.isTypeRef(int.class), f-> System.out.println(f) );
    }
    
    

    public abstract class Base{
        public abstract int getVal();
    }

    public void testExtends(){
        _class _c = _class.of("C").addExtend(new Base(){
            public int getVal(){
                return 123;
            }
        });
        //System.out.println( _c );
    }


    public interface One{
        String id();
    }

    public void testFullyQualifiedTypeParamsEtc(){
        _class a = _class.of("A")
                .typeParameters("<T extends B, C extends List<D>>")
                .addExtend("aaaa.Base")
                .addImplements("rrrrr.T", "iiii.R")
                .addMethod("aaaa.B blah( bbb.C c){}");

        _class b = _class.of("A")
                .typeParameters("<C extends java.util.List<ddd.D>, T extends bbbb.B>")
                .addExtend("Base")
                .addImplements("R", "T")
                .addMethod("B blah( C c){}");

        assertEquals( a, b);
        assertEquals( a.hashCode(), b.hashCode());
    }
    
    

    public void testTypeHashCode(){
        assertEquals(
            Ast.typeHash( Ast.typeRef("String")),
            Ast.typeHash( Ast.typeRef("java.lang.String" )) );
    }

    /**
     * Verify that fully qualified vs out of order Classes evaluate to the same
     * and the hashcode is same
     */
    public void testExtendsEqualsFullyQualified(){

        _class _a = _class.of("A").addExtend("aaaa.bbb.C");
        _class _b = _class.of("A").addExtend("C");

        assertEquals( _a.hashCode(), _b.hashCode() );
        assertEquals( _a, _b);

        _a = _class.of("B").addExtend("aaaa.V").addImplements("bbbb.C", "cccc.D");
        _b = _class.of("B").addExtend("V").addImplements("D", "C");

        assertEquals( _a.hashCode(), _b.hashCode() );
        assertEquals( _a, _b);


        //if fully qualified but NOT equal, verify
        _a = _class.of("B").addExtend("aaaa.V");
        _b = _class.of("B").addExtend("bbbb.V");
        assertNotSame( _a, _b);

    }
    @_package("aaaa.bbbb") @_dto @_public class Local{
        public int a,b,c;
        @_final String name;
        @_static public void main(String[] args){
            System.out.println("Some Print Statement");
        }
    }
     /**
     * Here _1_build a static final variant of a _class (so we dont have to parse every time)
     *
     */
    public static final _class _c = _class.of(Local.class); //_autoDto.Macro.to(
//_class.of("aaaa.bbbb.Local",
//            new @_dto Object(){
//                public int a,b,c;
//                @_final String name;
//                @_static public void main(String[] args){
//                    System.out.println("Some Print Statement");
//                }
//            });

    @interface _annotat{

    }

    public void testMethodLambda(){
        //_class _c = _class.of("aaaa.V").method("myMethod", ()-> System.out.println("Message"));
        _class _c = _class.of("aaaa.V").addMethod(new Object(){ void myMethod(String a){ System.out.println("a : "+a); } } );
        
        //_c = _class.of("aaaa.V").method("myMethod", (String a)-> System.out.println("a : "+a));
        _c = _class.of("aaaa.V").addMethod(new Object(){
            void myMethod(String a, Map<Integer,String>mi){ 
                System.out.println("a : "+a+" "+mi);  
            } 
        } );
        assertTrue( _c.hasImport(Map.class) );
        //_c = _class.of("aaaa.V").method("myMethod", (String a, Map<Integer,String>mi)-> System.out.println("a : "+a+" "+ mi));

    }
    
     @interface annotat{
        
    }
    
    public void testConstructorLambda(){
        //_class _c = _class.of("aaaa.bbbb.C").constructor(()->System.out.println("in constructor"));
        _class _c = _class.of("aaaa.bbbb.C").addConstructor(new Object(){ public void m(){ System.out.println("in constructor");} });
        //System.out.println( _c );

        //_c = _class.of("D").constructor((String s)->System.out.println("in constructor with "+s));
        _c = _class.of("D").addConstructor(new Object(){ public void c(String s){System.out.println("in constructor with "+s);} } );
        //System.out.println( _c );

        //_c = _class.of("D").constructor((final @_annotat String s)->System.out.println("in constructor with "+s));
        //_c = _class.of("D").constructor(new Object(final @_annotat String s)->System.out.println("in constructor with "+s));
        _c = _class.of("D").addConstructor(new Object(){ public void c(final @annotat String s){System.out.println("in constructor with "+s);} } );
        assertTrue( _c.getConstructor(0).getParameter("s").hasAnno(annotat.class));
        //System.out.println( _c );
    }

    public void testStaticClassLambdaAuto(){
        //when we create the model, we make it a top level model (not a Local Class)
        assertTrue( _c.isPublic() );
        assertTrue( _c.isTopLevel() );
        assertTrue( _c.getPackage().equals("aaaa.bbbb") );
        assertTrue( _c.getField("a").isPublic() );
        assertTrue( _c.getField("a").isTypeRef(int.class) );
        assertTrue( _c.getField("b").isPublic() );
        assertTrue( _c.getField("b").isTypeRef(int.class) );

        //verify we have a static main() method
        assertTrue( _c.getMethod("main").isStatic() );
        assertTrue( _c.getMethod("main").isVoid() );
        assertTrue( _c.getMethod("main").getParameter(0).isTypeRef(String[].class) );
        //verify we created the getters
        assertTrue( _c.getMethod("getA" ).isTypeRef(int.class) );
        assertTrue( _c.getMethod("getB" ).isTypeRef(int.class) );
        assertTrue( _c.getMethod("getC" ).isTypeRef(int.class) );
        assertTrue( _c.getMethod("getName" ).isTypeRef(String.class) );

        //verify that we manufactured a constructor, and it accepts a String (the final field NAME) as the constructor
        assertTrue( _c.getConstructor(0).getParameter(0).isTypeRef(String.class) );
        //assertTrue( _c.getConstructor(0).getBody().getStatement(0).equals( Stmt.of("this.name = name;") ) );
         assertTrue( _c.getConstructor(0).getBody().getStatement(0).equals( Stmt.of("this.name = name;") ) );
    }
    
    
    /**
     * Here we pass in a Lambda Expression that contains a Local Class Declaration
     * that is used as a _class
     *
     */
    public void testAnonymousClassWithMacroAnnotations(){
        //_class _c = _class.of( ()-> { class F{} });
        _class _c = _class.of( "F", new Object(){} );
        //System.out.println( _c );
        assertEquals( "F", _c.getName());

        assertTrue( _c.isPublic() ); //we ensure the class is public
        assertTrue( _c.isTopLevel() ); //we Promote it to a top level class

        assertTrue( _c.getPackage() == null );

        _c = _class.of( "aaaa.bbbb.F", new Object(){ @_static int a;} );
        assertTrue( _c.getField("a").isStatic() );
    }

    //TODO _class.of("pav.asda.CC");
    public void testShortcutClass(){
        _class _c = _class.of("C"); //public class C{}
        assertTrue( _c.isPublic());
        assertEquals("C", _c.getName());
        assertEquals(null, _c.getPackage());
        assertEquals("C", _c.getFullName());
        _c = _class.of("aaaa.bbbb.C");
        assertTrue( _c.isPublic());
        assertEquals("C", _c.getName());
        assertEquals( "aaaa.bbbb", _c.getPackage());
        assertEquals( "aaaa.bbbb.C", _c.getFullName());

        _c = _class.of("aaaa.bbbb.C<val>");
        assertTrue( _c.isPublic());
        assertEquals( _typeParameters.of("<val>"), _c.getTypeParameters());
        assertEquals("C", _c.getName());
        assertEquals( "aaaa.bbbb", _c.getPackage());
        assertEquals( "aaaa.bbbb.C", _c.getFullName());
    }

    public void testImportNative(){
        _class _c = _class.of( NativeMethod.class);
        _method _m1 = _method.of("native int getpid();");
        _method _m2 = _method.of("native int getpid();");
        _m1.equals(_m2);
        assertEquals( _m1, _m2);
        
        assertEquals( _c.getMethod( "getpid"), _method.of( "native int getpid();" ));
        assertTrue( _c.getMethod( "getpid").is( "native int getpid();" ));
        
        assertTrue( _c.hasMethods() );
        assertTrue( _c.hasInitBlocks() );
        assertTrue( _c.getInitBlock(0).is( " System.loadLibrary(\"getpid\");"));
        _method _m = _c.getMethod(0);
        //System.out.println( _m );
        
        assertTrue( _c.getMethod( "getpid").isNative());
        assertTrue( _c.getMethod( "getpid").isTypeRef( int.class));
        
    }
    
    
    
    public void testImportFromClass(){
        //read in the class from the Class file
        _class _c = _class.of( ComplexClass.class );
        //System.out.println( _c ); 
    }
    
    public void testC(){
        _class _c = _class.of("class C{}");
        assertEquals("C", _c.getName());
        assertTrue( _c.getModifiers().is("") );
        assertFalse( _c.hasAnnos());
        assertFalse( _c.hasJavadoc());
        assertFalse( _c.hasExtends());
        assertFalse( _c.hasImplements());
        assertFalse( _c.hasFields());
        assertFalse( _c.hasMethods());
        assertFalse( _c.hasTypeParameters());
        assertFalse( _c.isPublic());
        assertFalse( _c.isPrivate());
        assertFalse( _c.isProtected());
        assertTrue( _c.isPackagePrivate());
        assertFalse( _c.isStatic());
        assertFalse( _c.isFinal());
        assertFalse( _c.isAbstract());
        assertFalse( _c.hasInitBlocks());
        assertFalse( _c.isExtends( Serializable.class ) );
        assertFalse( _c.isImplements( Serializable.class ) );
        assertNull( _c.getInitBlock(0) );
        assertTrue( _c.listMethods().isEmpty() );
        assertTrue( _c.listFields().isEmpty() );
        assertTrue( _c.listConstructors().isEmpty() );
        assertNull( _c.getExtends() );
        assertTrue( _c.listImplements().isEmpty() );
        assertTrue( _c.listAnnos( ).isEmpty());                
    }
    
    

    
    public void testMutate(){
        _class _c = _class.of("class C{}");
        _c.setPackage("blah.fromscratch");
        _c.addImports(Map.class,HashMap.class);
        _c.addImports( "aaaa.bbbb.C", "blah.dat.*");
        _c.setJavadoc("class JAVADOC");
        _c.addAnnos( "@ann", "@ann(k=1,v='y')");
        _c.setPublic();
        _c.setName("Cgg");
        _c.typeParameters("<T extends Impl>");
        _c.addExtend( "Base");
        _c.addImplements( "A", "B");
        _c.initBlock("System.out.println(34);");
        _c.addField( "/** field JAVADOC */",
            "@ann2(k=2,v='g')",
            "public static final List<String> l = new ArrayList<>();");
        _c.addConstructor( "/** ctor JAVADOC */",
            "@ann",
            "@ann2(k=3,v='i')",
            "protected <e extends Element> Cgg( @ann @ann2(k=5)final String s, int...varArgs3 ) throws P, Q, D{",
            "     System.out.println(12);",
            "}");
        _c.addMethod( "/** method JAVADOC */",
            "@ann",
            "@ann2(k=8,v='l')",
            "public static <e extends Fuzz> void doIt( @ann @ann2(k=7)final String xx, int...varArgs ) throws G, H, I{",
            "    System.out.println(15);",
            "}" );
        
        //System.out.println( _c );
    }
    
    /**
     * Create a _class and then ask questions to verify the model
     */
    public void testFull(){
        _class _c = _class.of(
            "package blah.fromscratch;",
            "",
            "import blah.dat.*;",
            "import aaaa.bbbb.C;",
            "import java.util.Map;",
            "import java.util.HashMap;",
            "",
            "/** class JAVADOC */",
            "@ann",
            "@ann2(k=1,v='y')",
            "public class Cgg<T extends Impl> ",
            "    extends Base implements A, B {",
            "",
            "    static{",
            "         System.out.println(34);",
            "    }",
            "",
            "    /** field JAVADOC */",
            "    @ann",
            "    @ann2(k=2,v='g')",
            "    public static final List<String> l = new ArrayList<>();",
            "",
            "    /** ctor JAVADOC */",
            "    @ann",
            "    @ann2(k=3,v='i')",
            "    protected <e extends Element> Cgg( @ann @ann2(k=5)final String s, int...varArgs3 ) throws P, Q, D{",
            "         System.out.println(12);",
            "    }",
            "",
            "    /** method JAVADOC */",
            "    @ann",
            "    @ann2(k=3,v='i')",
            "    public static <e extends Fuzz> void doIt( @ann @ann2(k=5)final String xx, int...varArgs ) throws G, H, I{",
            "         System.out.println(15);",
            "    }",
            "}");
        
        assertTrue( _c.getPackage().equals( "blah.fromscratch"));
        
        assertTrue( _c.hasImport(Map.class) );
        assertTrue( _c.hasImport(HashMap.class) );
        assertTrue( _c.hasImport( "aaaa.bbbb.C"));
        assertTrue( _c.hasImport( "blah.dat.Blart"));
        assertTrue(_c.hasMethods());
        assertEquals(1, _c.listMethods().size());
        _method _m = _c.getMethod("doIt");
        assertTrue( _m.hasJavadoc());
        assertTrue( _m.getJavadoc().getContent().contains( "method JAVADOC" ));
        assertTrue( _m.hasAnnos());
        assertTrue( _m.getAnnos().is("@ann","@ann2(k=3,v='i')"));
        assertTrue( _m.getModifiers().is("public static"));
        assertTrue(_m.hasTypeParameters());
        assertTrue(_m.getTypeParameters().is("<e extends Fuzz>"));
        assertTrue( _m.isTypeRef( "void"));
        assertTrue( _m.isVoid());
        assertEquals("doIt", _m.getName());
        assertTrue( _m.hasParameters());
        assertTrue( _m.getParameters().is( "@ann @ann2(k=5)final String xx, int...varArgs"));
        assertTrue( _m.hasThrows());
        assertTrue( _m.hasThrow("I"));
        assertTrue( _m.hasThrow("H"));
        assertTrue( _m.hasThrow("G"));
        assertTrue( _m.getBody().is( "System.out.println(15);"));        
        assertEquals(1, _c.listConstructors().size());
        _constructor _ct = _c.getConstructor(0);
        assertTrue(_ct.hasJavadoc());
        assertTrue(_ct.getJavadoc().getContent().contains("ctor JAVADOC"));
        assertTrue(_ct.hasAnnos());
        assertTrue( _ct.getAnnos().is("@ann","@ann2(k=3,v='i')"));
        assertTrue( _ct.getModifiers().is("protected"));
        assertTrue( _ct.hasTypeParameters());
        assertTrue( _ct.getTypeParameters().is( "<e extends Element>"));
        assertEquals("Cgg", _ct.getName());
        assertTrue( _ct.hasThrows() );
        assertTrue( _ct.getThrows().is( "throws D, P, Q"));
        assertTrue( _ct.getBody().is( "System.out.println(12);"));
        assertTrue( _ct.hasThrow( "P") );
        assertTrue( _ct.hasThrow( "D") );
        assertTrue( _ct.hasThrow( "Q") );
        assertTrue( _ct.hasParameters() );
        assertEquals(2, _ct.listParameters().size() );
        _parameter _p = _ct.getParameter( 0 );
        assertTrue( _p.is( "@ann @ann2(k=5)final String s" ));
        assertTrue( _p.hasAnnos());
        assertEquals( 2, _p.listAnnos().size());
        assertTrue( _p.isFinal());
        assertTrue( _p.isTypeRef( String.class));
        assertEquals("s", _p.getName());
        assertFalse( _p.isVarArg() );
        _p = _ct.getParameter( 1 );
        assertTrue( _p.is( "int...varArgs3" ));
        assertFalse( _p.hasAnnos());
        assertEquals( 0, _p.listAnnos().size());
        assertFalse( _p.isFinal());
        assertTrue( _p.isTypeRef( int.class));
        assertEquals("varArgs3", _p.getName());
        assertTrue( _p.isVarArg() );
        assertTrue( _ct.isVarArg());
        
        _parameters _ps = _ct.getParameters();
        
        assertEquals(1, _c.listFields().size());
        _field _f = _c.getField("l");
        assertTrue( _f.hasJavadoc());
        assertTrue( _f.getJavadoc().getContent().contains("field JAVADOC"));
        assertTrue( _f.hasAnnos() );
        assertTrue( _f.getAnnos().is("@ann","@ann2(k=2,v='g')") );
        assertTrue( _f.getModifiers().is( "public static final"));
        assertTrue( _f.isTypeRef( "List<String>"));
        assertTrue( _f.getName().equals( "l"));
        assertTrue( _f.isInit(Ex.of("new ArrayList<>()")));
        //System.out.println( _c.listFields());
        
        assertTrue(_c.hasJavadoc());
        assertTrue(_c.getJavadoc().getContent().contains( "class JAVADOC") );
        assertTrue(_c.hasAnnos());
        assertEquals(2, _c.listAnnos().size() );
        
        assertTrue( _c.getAnno(0).is( "@ann") );
        assertTrue( _c.getAnno(1).is( "@ann2(k=1,v='y')") );
        assertTrue( _c.getAnnos().is( "@ann","@ann2(k=1,v='y')"));
        assertTrue( _c.getModifiers().is("public"));
        assertEquals("Cgg", _c.getName());
        assertTrue( _c.hasTypeParameters() );
        assertTrue( _c.getTypeParameters().is( "<T extends Impl>"));
        assertTrue( _c.isExtends( "Base") );
        assertTrue( _c.isImplements( "A"));
        assertTrue( _c.isImplements( "B"));        
        assertTrue( _c.hasInitBlocks());
        assertNotNull( _c.getInitBlock(0) ); //todo better static block
        assertTrue( _c.hasFields());        
    }
}
