package org.jdraft;

import junit.framework.TestCase;

import java.io.Serializable;

/**
 *
 * @author Eric
 */
public class AstImpliedModifiersTest extends TestCase {
    
    abstract static @interface a {}
    @interface b {}
    
    public void testAnnotationImplied(){
        assertEquals( a.class.getModifiers(),  
            b.class.getModifiers() );   
        
        _annotation _a  = _annotation.of( a.class );
        _annotation _b  = _annotation.of( b.class).setName("a");
        assertEquals(_a.hashCode(), _b.hashCode());
        assertEquals(_a, _b);
        
        System.out.println( java.lang.reflect.Modifier.toString( a.class.getModifiers() ) );
        System.out.println( java.lang.reflect.Modifier.toString( _a.getModifiersAsBitMask() ) );
        //assertEquals( _a.modifiersAsBitMask() | java.lang.reflect.Modifier.INTERFACE, a.class.getModifiers() );        
    }
    
    static enum EE{ ;}
    enum EE2{}

    public void testEnumImplied(){
        
        assertEquals( EE.class.getModifiers(),  
            EE2.class.getModifiers() );
         
        _enum _a  = _enum.of( EE.class );
        _enum _b  = _enum.of( EE2.class).setName("EE");
        assertEquals(_a.hashCode(), _b.hashCode());
        assertEquals(_a, _b);         
        
        System.out.println( java.lang.reflect.Modifier.toString( EE2.class.getModifiers() ) );
        System.out.println( java.lang.reflect.Modifier.toString( _a.getModifiersAsBitMask() ) );
        
        //assertEquals( _a.modifiersAsBitMask(), EE.class.getModifiers());
    }
    
    /** TEST the implied private on enum constructors */
    enum EC{
        A;
        EC(){}
    }
    
    enum EC2{
        A;
        private EC2(){}
    }
    public void testEnumConstructor(){
        assertEquals( EC.class.getDeclaredConstructors()[0].getModifiers(),  
            EC2.class.getDeclaredConstructors()[0].getModifiers() );
         
        _enum _a  = _enum.of( EC.class );
        _enum _b  = _enum.of( EC2.class).setName("EC");
        _constructor _c1 = _a.getConstructor(0);
        _constructor _c2 = _b.getConstructor(0);
        
        assertTrue( _c1.isPrivate() ); //it is IMPLIED to be private
        assertTrue( _c2.isPrivate() );
        
        assertEquals(_c1.hashCode(), _c2.hashCode());
        assertEquals(_c1, _c2);         
        
        assertEquals( _c1.getModifiersAsBitMask(), EC.class.getDeclaredConstructors()[0].getModifiers() );
    }
    
    interface I{ int f = 2; }
    interface I2{ public static final int f = 2; }
    public void testEffectiveModifiersInterfaceField() throws NoSuchFieldException{        
        _field _f = _interface.of( I.class ).getField("f");
        assertEquals( I.class.getDeclaredField("f").getModifiers(),  
            I2.class.getDeclaredField("f").getModifiers() );   
        assertEquals( I.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
        assertEquals( I2.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
        
        assertTrue( _f.isStatic() );
        assertTrue( _f.isPublic() );
        assertTrue( _f.isFinal() );
        
        assertTrue( _f.is("int f = 2;") );
        
        //this is gonna SEEM silly, but we allow all these definitions
        // to mean the same thing assuming we disreguard the "implied modifiers"
        // so... in practice ANY of these fields (in this context, on an interface)
        // is equivalent
        System.out.println( _f.getEffectiveModifiers() );
        assertEquals( _modifiers.of("public static final "), _f.MODIFIERS.get(_f) );
        assertTrue( _f.is("public static final int f = 2;") );

        assertTrue( _f.is("static final int f = 2;") );
        assertTrue( _f.is("public int f = 2;") );
    }

    public void testTN(){
        System.out.println( new Serializable(){}.getClass().getName() );
    }
    
     /** Verify the Implied/Effective Modifiers are applied to the field of annotation */
    @interface G{ int f=1; }
    @interface H{ public static final int f=1; }
    public void testEffectiveModifiersAnnotationField() throws NoSuchFieldException{                
        _field _f = _annotation.of(G.class).getField("f");
        
        assertTrue( _f.isPublic() );
        assertTrue( _f.isStatic() );
        assertTrue( _f.isFinal() );
        assertEquals( G.class.getDeclaredField("f").getModifiers(), 
                H.class.getDeclaredField("f").getModifiers() );                        
        assertEquals( G.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
        assertEquals( H.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
    }
   
    
    enum E{ ; int f; final int g = 12;}    
    public void testEffectiveModifiersEnumField() throws NoSuchFieldException{                
        _field _f = _enum.of(E.class).getField("f");
        assertEquals( E.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
        System.out.println( _f.getModifiersAsBitMask() );
        
        _field _g = _enum.of(E.class).getField("g");
        assertEquals( E.class.getDeclaredField("g").getModifiers(), _g.getModifiersAsBitMask() );
        System.out.println( _g.getModifiersAsBitMask() );
    }
    
    enum E2{ ; static int f; }    
    public void testEffectiveModifiersEnumField2() throws NoSuchFieldException{                
        _field _f = _enum.of(E2.class).getField("f");
        assertEquals( E2.class.getDeclaredField("f").getModifiers(), _f.getModifiersAsBitMask() );
    }
    
   
    public enum EM{
        ;
        int m(){
            return 3;
        }
    }
    
    public void testEffectiveModifiersEnumMethod() throws NoSuchMethodException{                
        _method _m = _enum.of(EM.class).firstMethodNamed("m");
        assertEquals(                 
                EM.class.getDeclaredMethod("m", new Class[0]).getModifiers(), 
                _m.getModifiersAsBitMask() );
        System.out.println("MODS" + 
                java.lang.reflect.Modifier.toString(EM.class.getDeclaredMethod("m", new Class[0]).getModifiers()) );
    }
    
}
