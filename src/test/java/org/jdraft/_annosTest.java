package org.jdraft;

import org.jdraft._anno;
import org.jdraft.Ast;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.jdraft._annos;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _annosTest extends TestCase {

    /** verify changes to the copy dont effect the original */
    public void testCopy(){
        //the FieldDeclaration is the "parent"
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");

        _annos _as = _annos.of( fd  );

        _annos _cp = _as.copy();
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
        _annos _as = _annos.of(fd );
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
        _annos _as1 = _annos.of( "@ann(a=1,b=2)", "@dann(c=3,d=4)" );
        _annos _as2 = _annos.of( "@ann(b=2,a=1)", "@dann(d=4,c=3)" );

        assertTrue( _as1.hashCode() == _as2.hashCode() );
        assertTrue( _as1.equals(_as2));
        assertTrue( _as1.is( is));
        assertTrue( _as2.is( is));
    }

    public void testChildParent(){
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");
        _annos _as = new _annos( fd );
        _as.get( 0 ).addAttr( "Key", 1000 );

        assertTrue( _as.get(0).is("@a(Key=1000)"));

        //parent child
        fd = Ast.field( "@a(1) public int i=100;");

        AnnotationExpr ae = fd.getAnnotation(0).clone();
        //System.out.println( ae.getParentNode().isPresent() );
        _anno _aNoParent = new _anno(ae);
        _aNoParent.addAttr( "Key", 9999 );
        assertTrue( _aNoParent.is("@a(Key=9999)") );
        //System.out.println( _aNoParent );
    }

}
