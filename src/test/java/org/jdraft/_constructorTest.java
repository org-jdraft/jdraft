package org.jdraft;

import org.jdraft.macro._public;
import junit.framework.TestCase;

import java.io.IOException;

/**
 *
 * @author Eric
 */
public class _constructorTest extends TestCase {

    public void testBuildFromScratch(){
        _constructor _ct = _constructor.of();
        _ct.setName("C")
                .setParameters("int i").setModifiers("public")
                .addThrows(IOException.class);
        System.out.println(_ct);
    }

    public void testConstructorWithAnnoMacros(){
        _constructor _ct = _constructor.of( new Object(){
            @_public void m(){
                System.out.println( " M ");
            }
        });
        assertTrue( _ct.isPublic() );
    }
    
    enum E{
        A;

        E(){
        }
    }

    enum E2{
        ;
        private E2(){
        }
    }

    public void testConstructorModifiers(){
        /** verify that (at runtime) they are semantically equal .. same constructor modifiers */
        assertEquals( E.class.getDeclaredConstructors()[0].getModifiers(),
                E2.class.getDeclaredConstructors()[0].getModifiers());

        /** but they are not Syntactically the same (in the AST)*/
        assertNotSame( _enum.of( E.class ).getConstructor(0).ast(),
                _enum.of( E2.class ).getConstructor(0).setName("E").ast());

        /** but they are equal in the _constructor model */
        assertEquals( _enum.of( E.class ).getConstructor(0),
                _enum.of( E2.class ).getConstructor(0).setName("E") );

        assertEquals( _enum.of( E.class ).getConstructor(0).hashCode(),
                _enum.of( E2.class ).getConstructor(0).setName("E").hashCode() );
    }

    static abstract class C{
        C(){}
    }
    class D{
        D(){}
    }

    public void testCt(){

        System.out.println( C.class.getDeclaredConstructors()[0].getModifiers() );
        System.out.println( D.class.getDeclaredConstructors()[0].getModifiers() );
    }

    public void testConstructorBody() {
        _constructor ct = _constructor.of(new Object() {
            public void name() {
                System.out.println(1);
            }
        });

        _constructor ct2 = _constructor.of(new Object() {
            int a, b;
            String name;

            /**
             * The JAVADOC for the constructor
             *
             * @param a
             * @param b
             * @param name
             */
            public void constructor(int a, int b, String name) {
                System.out.println(1);
                this.a = a;
                this.b = b;
                this.name = name;
            }
        });
    }

    public void testHasConstructor(){
        // here I pass in a Method that will be converted into a constructor
        // it should be
        _class _c = _class.of("C").addFields("int x,y;")
                .addConstructor(new Object(){
                    /** note these "placeholder" FIELDS are ignored */
                    int x,y;

                    /**
                     * This is a constructor
                     * @param x some x VALUE
                     * @param y some y VALUE
                     */
                    @Deprecated
                    public void constructor(int x, int y){
                        this.x = x;
                        this.y = y;
                    }
                });
        _constructor _ct = _c.getConstructor(0);
        assertTrue( _ct.hasAnnoRef(Deprecated.class));
        assertTrue( _ct.getJavadoc().getText().contains("This is a constructor"));
        assertTrue( _ct.getParameter(0).is("int x"));
        assertTrue( _ct.getParameter(1).is("int y"));
        //System.out.println(_c);
        //_javac.of( _c); //make sure it compiles
    }

    public void testC(){
        _constructor _ct = _constructor.of("A(){}"); 
        assertEquals("A",_ct.getName());
        assertTrue( _ct.hasBody() );
        assertFalse( _ct.hasJavadoc());
        assertFalse( _ct.hasParameters());
        assertFalse( _ct.hasAnnoRefs() );
        assertFalse( _ct.hasThrows());
        assertFalse( _ct.hasTypeParameters() ); 
        
        assertFalse( _ct.isPublic() );
        assertFalse( _ct.isPrivate() );
        assertFalse( _ct.isProtected() );
    }
    
    public void testFull(){
        _constructor _ct = _constructor.of(
            "/** ctor JAVADOC */",
            "@ann",
            "@ann2(k=1,v='t')",
            "public <E extends element> C( @ann @ann2(k=1,v='e')final int v, Map<Integer,String>...m )",
            "    throws A, B {",
            "    System.out.println(1);",
            "    this.v = v;",
            "    this.m = m;",
            "}"); 
        assertTrue( _ct.hasJavadoc() );
        assertTrue( _ct.getJavadoc().getText().contains( "ctor JAVADOC"));
        assertTrue( _ct.hasAnnoRefs() );
        assertTrue( _ct.getAnnoRef( 0 ).is( "@ann") );
        assertTrue( _ct.getAnnoRef( 1 ).is( "@ann2(k=1,v='t')") );
        assertTrue( _ct.getModifiers().is( "public"));
        assertTrue( _ct.hasTypeParameters() ); 
        assertTrue( _ct.getTypeParameters().is( "<E extends element>") ); 
        assertEquals("C",_ct.getName());
        assertTrue( _ct.hasParameters());
        
        assertTrue( _ct.getParameters().is( "@ann @ann2(k=1,v='e')final int v, Map<Integer,String>...m" ));
        assertTrue( _ct.getParameter(0).is("@ann @ann2(k=1,v='e')final int v"));
        assertFalse( _ct.getParameter(0).isVarArg());        
        assertTrue( _ct.getParameter(1).is("Map<Integer,String>...m"));
        assertTrue( _ct.getParameter(1).isVarArg());        
        assertTrue( _ct.isVarArg());
        assertTrue( _ct.hasThrows());
        assertTrue( _ct.getThrows().is("A,B"));
        assertTrue( _ct.hasThrow("A"));
        assertTrue( _ct.hasThrow("B"));
        assertTrue( _ct.hasBody() );
        assertTrue( _ct.getBody().is( "System.out.println(1);","this.v = v;","this.m = m;" ));        
    }
    
}
