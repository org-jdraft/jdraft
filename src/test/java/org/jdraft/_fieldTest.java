package org.jdraft;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft._anno.*;

import java.util.List;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _fieldTest extends TestCase {

    public void testFieldWithAnnotationOrder(){
        _field _f1 = _field.of("@A @B int i= 0;");
        _field _f2 = _field.of("@B @A int i= 0;");
        
        assertEquals( _f1, _f2);
        assertEquals( _f1.hashCode(), _f2.hashCode());
    }
    
    public void testFieldInitWithLambda(){
        _field _f = _field.of("private final String s;")
                .init( ()-> "Lollipop v." + 5.0d );
        System.out.println(_f );
    }

    interface I{
        int x=1;
    }

    interface I2{
        public static final int x = 1;
    }

    public void testFieldImpliedModifiers(){
        _field _f1 = _interface.of(I.class).getField("x");
        _field _f2 = _interface.of(I2.class).getField("x");

        /**
         * these fields _f3 and _f4 are "disconnected", because they don't have
         * a parent _type (they are disconnected from a higher AST parent node)
         * (they may be added to a _type later), until then,
         */
        _field _f3 = _field.of("public int x=1;");
        _field _f4 = _field.of("public static final int x=1;");

        //The AST for the FieldDeclarations is not syntactically the same
        // but it is semantically the same becuase of implied modifiers
        assertNotSame( _f1.getFieldDeclaration(), _f2.getFieldDeclaration() );

        //both connected
        assertEquals( _f1, _f2);


        //connected to disconnected
        assertEquals( _f1, _f3);
        assertEquals( _f1, _f4);

        assertEquals( _f2, _f3);
        assertEquals( _f2, _f4);

        //disconnected to connected
        assertEquals( _f3, _f1);
        assertEquals( _f4, _f1);
        assertEquals( _f3, _f2);
        assertEquals( _f4, _f2);

        //disconnected to disconnected
        assertNotSame(_f3, _f4);

    }

    public void testFieldAnonymousObject(){
        _field _f = _field.of( new Object(){ int x; } );
        assertEquals( _field.of("int x;"), _f );
    }

    public void testAst(){
        _field _f = _field.of("int x;");
        VariableDeclarator vd = _f.ast();
        assertEquals("x", vd.getNameAsString());
        assertTrue( vd.getType().isPrimitiveType() );

        FieldDeclaration fd = _f.getFieldDeclaration();
        assertEquals(1, fd.getVariables().size());

        List<_field> _fs = _field.of( Ast.field("int x,y;"));
        assertEquals( 2, _fs.size());
        assertEquals( _fs.get(0).getFieldDeclaration(), _fs.get(1).getFieldDeclaration());
        assertNotSame(_fs.get(0), _fs.get(1));
    }

    //** makes sure that, when 
    public void testWJavadoc(){
        _field _f = _field.of("int a;");        
        _f.getJavadoc().setContent( "Some", "Multiline", "Content");     
        System.out.println( _f );
        System.out.println( _f.getJavadoc().getContent() );
        assertTrue( _f.getJavadoc().getContent().contains( "Multiline"));
        assertEquals( Text.combine( "Some", "Multiline", "Content"), _f.getJavadoc().getContent() );
    }
    
    public void testSimple(){
        _field _f = _field.of("int i;");
        assertEquals( "i", _f.getName());
        assertEquals( _typeRef.of( "int"), _f.getType());
        assertFalse( _f.isFinal() );
        assertFalse( _f.isPrivate() );
        assertFalse( _f.isProtected() );
        assertFalse( _f.isPublic() );
        assertTrue( _f.isDefaultAccess() );
        assertFalse( _f.isStatic() );
        assertFalse( _f.isTransient() );
        assertFalse( _f.isVolatile() );
        assertTrue( _f.isType( "int") );
        assertTrue( _f.isType( int.class) );
        assertFalse( _f.hasInit());
        assertFalse( _f.hasAnnos());
        
        assertTrue( _f.isPrimitive() );
        assertFalse( _f.isArray() );
    }
    
    public void testEqsHashCode(){
        
        //these FIELDS have some things out of order (MODIFIERS, JAVADOC, ANNOTATIONS)
        // but are SEMANTICALLY the same... therefore they have the same typesEqual and hashcode
        _field _f = _field.of("/** JAVADOC */", "@ann2(k=13,v=2)", "@ann(v=2,k=1)", "public static final int W = 103 + 45;");
        _field _f2 = _field.of("@ann( k = 1 , v = 2 )", "@ann2( v = 2 , k = 13 )", "/** JAVADOC */", "public final static int W=103+45;");
    
        assertEquals( _f, _f2);
        System.out.println( _f.getEffectiveModifiers() );
        System.out.println( _f2.getEffectiveModifiers() );
        assertEquals( _f.hashCode(), _f2.hashCode());
    }
    
    public void testOf(){
        _field _f = _field.of("/** JAVADOC */", "@ann(1)", "@ann2(3)", "public static final int W = 103 + 45;");
        assertNotNull( _f.getJavadoc() );
        assertTrue( _f.getJavadoc().getContent().contains( "JAVADOC"));
        _annos _as = _f.getAnnos();
        assertEquals( 2, _as.size() );
        assertTrue( _as.is("@ann(1)", "@ann2(3)") );
        
        assertEquals( _modifiers.of( "public", "static", "final"), _f.getModifiers() );
        assertTrue( _f.isType( "int"));
        assertEquals("W", _f.getName() );
        assertEquals( Ex.of("103 + 45"), _f.getInit() );
    }
    
}
