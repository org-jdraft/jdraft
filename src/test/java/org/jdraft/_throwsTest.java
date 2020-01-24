package org.jdraft;

import com.github.javaparser.ast.type.ReferenceType;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Eric
 */
public class _throwsTest
    extends TestCase {

    public void testBuildFromScratch(){
        _throws _ts = _throws.of();
        System.out.println( _ts );
        _ts.add( IOException.class);
        System.out.println( _ts );
    }

    @Target(ElementType.TYPE_USE)
    @interface Test{}
    
    public void testThrowsWithAnnotation(){
        class C{
            void m() throws @Test IOException, @Test RuntimeException{}
        }
        
        _class _c = _class.of(C.class);
        System.out.println( _c );
        _throws _ts = _c.getMethod("m").getThrows();
        System.out.println(_ts.toString());
        assertEquals( _anno.of(Test.class).ast(), _ts.get(0).getAnnotation(0));
        assertEquals( _anno.of(Test.class).ast(), _ts.get(1).getAnnotation(0));
        
        System.out.println( _ts.get(0) );
    }

    /** Verify that fully qualified throws == unqualified throws */
    public void testThrowsEquality(){
        _throws _t1 = _throws.of( RuntimeException.class );
        _throws _t2 = _throws.of( "java.lang.RuntimeException" );
        assertTrue( Ast.typesEqual( (ReferenceType)Ast.typeRef(RuntimeException.class),
            (ReferenceType)Ast.typeRef("java.lang.RuntimeException" ) ));

        assertTrue( Ast.typesEqual( (ReferenceType)Ast.typeRef("RuntimeException"),
            (ReferenceType)Ast.typeRef("java.lang.RuntimeException" ) ));

        //assertTrue( _t1.has((ReferenceType)Ast.typeRef("java.lang.RuntimeException" )));
        assertTrue( _t1.has((ReferenceType)Ast.typeRef("RuntimeException" )));
        assertEquals( _t1, _t2 );

        assertEquals( _t1.hashCode(), _t2.hashCode());
    }

    public void testThrowsIs(){

        _throws _tt = _throws.of( RuntimeException.class );
        assertTrue( _tt.is( new Class[]{RuntimeException.class }) );
    }

    public void testT(){
        _throws _tt = _throws.of( RuntimeException.class );

        assertEquals( "throws RuntimeException", _tt.toString() );

        _tt = _throws.of( RuntimeException.class).add( "MyException" );

        assertEquals("throws RuntimeException, MyException", _tt.toString() );

    }

    //throws should be the same no matter the order
    public void testVerifyOrderDoesntMatter(){
        _throws _tt = new _throws( );
        _tt.add( RuntimeException.class );
        _tt.add( IOException.class );

        _throws _ot = _throws.of( IOException.class, RuntimeException.class );
        assertEquals( _tt, _ot );
    }
}
