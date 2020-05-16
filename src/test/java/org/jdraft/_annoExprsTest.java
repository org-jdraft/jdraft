package org.jdraft;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class _annoExprsTest extends TestCase {

    public void testNewApiEmpty(){
        _annoExprs _ts = _annoExprs.of();
        assertEquals(_ts, _ts.copy());
        assertEquals(_ts.hashCode(), _ts.copy().hashCode());

        assertTrue(_ts.is(""));
        assertFalse(_ts.has(_annoExpr.of("@_private")));
        assertTrue(_ts.is( new ArrayList<>()));
        assertTrue(_ts.isEmpty());
        assertEquals(0, _ts.size());
        assertTrue(_ts.list().isEmpty());
        assertTrue(_ts.listAstElements().isEmpty());
        _ts.forEach(t-> System.out.println(t));

    }

    public void testBuildFromScratch(){
        _annoExprs _as = _annoExprs.of();
        assertEquals("", _as.toString());
    }

    /** verify changes to the copy dont effect the original */
    public void testCopy(){
        //the FieldDeclaration is the "parent"
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");

        _annoExprs _as = _annoExprs.of( fd  );

        _annoExprs _cp = _as.copy();
        //make sure this DOESNT effect the Fields ANNOTATIONS
        _cp.clear();

        assertTrue( Ast.anno(  "@a(1)").equals( fd.getAnnotation( 0 ) ));
        _cp.add("@b(1)", "@c(2)");
        assertTrue( Ast.anno(  "@a(1)").equals( fd.getAnnotation( 0 ) ));

        _as.add( "@o(4)" );
        //make sure changes to the original are NOT reflected in the copy
        assertTrue( _cp.is("@b(1)", "@c(2)") );

    }
    public void testAddRemove(){
        //the FieldDeclaration is the "parent"
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");

        //we _2_template the ANNOTATIONS through the _annos and _anno interface
        _annoExprs _as = _annoExprs.of(fd );
        _as.add( "@b(1)", "@c(2)" );

        assertEquals( 3, fd.getAnnotations().size());

        _as.remove( a-> a.getName().equals("a") );

        assertEquals( 2, fd.getAnnotations().size());

        assertTrue( _as.is( "@b(1)", "@c(2)" ));

        //System.out.println( _as );
    }

    //verify that
    public void testEqualsHashCodeIs(){
        String[] is = {"@ann(a=1,b=2)", "@dann(c=3,d=4)"};
        _annoExprs _as1 = _annoExprs.of( "@ann(a=1,b=2)", "@dann(c=3,d=4)" );
        _annoExprs _as2 = _annoExprs.of( "@ann(b=2,a=1)", "@dann(d=4,c=3)" );

        assertTrue( _as1.hashCode() == _as2.hashCode() );
        assertTrue( _as1.equals(_as2));
        assertTrue( _as1.is( is));
        assertTrue( _as2.is( is));
    }

    public void testChildParent(){
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");
        _annoExprs _as = new _annoExprs( fd );
        _as.getAt( 0 ).addMemberValue( "Key", 1000 );

        assertTrue( _as.getAt(0).is("@a(Key=1000)"));

        //parent child
        fd = Ast.field( "@a(1) public int i=100;");

        AnnotationExpr ae = fd.getAnnotation(0).clone();
        //System.out.println( ae.getParentNode().isPresent() );
        _annoExpr _aNoParent = new _annoExpr(ae);
        _aNoParent.addMemberValue( "Key", 9999 );
        assertTrue( _aNoParent.is("@a(Key=9999)") );
        //System.out.println( _aNoParent );
    }

}