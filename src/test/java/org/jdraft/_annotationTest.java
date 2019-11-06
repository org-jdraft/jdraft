package org.jdraft;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.jdraft._annotation._element;
import java.util.List;

import org.jdraft.pattern.$ex;
import java.lang.annotation.ElementType;
import junit.framework.TestCase;
import test.ComplexAnnotationType;
import test.subpkg.ann2;

/**
 *
 * @author Eric
 */
public class _annotationTest extends TestCase  {




    public void testAnnotationObject(){

        _annotation _a = _annotation.of( "A", new Object(){
            /** A Javadoc */
            int value = 100;

            String name= "Eric";
        });

        System.out.println( _a );

        assertTrue( _a.getElement("value").equals(_annotation._element.of("/** A Javadoc */ int value() default 100;")));
        assertTrue( _a.getElement("name").is("String name() default \"Eric\";"));
    }

    public void testHeader(){
        _annotation _a = _annotation.of("/* License */",
                "package aaaa.bbbb;",
                "/**",
                " * JavaDoc",
                " */",
                "public @interface FFF{ }" );
        assertNotNull( _a.getHeaderComment() );
        assertNotNull( _a.getJavadoc() );
        assertEquals( _a.getJavadoc().toString().trim(), 
            Ast.javadocComment(
                "/**", 
                " * JavaDoc", 
                " */").toString().trim() );
        assertEquals( Ast.blockComment("/* License */").toString(), _a.getHeaderComment().toString() );
        System.out.println( _a.getHeaderComment() );
    }
    
    public void testEquality(){
        _field _f1 = _field.of("public static final int f = 123;");
        _field _f2 = _field.of("public static final int f = 123;");
        assertEquals( _f1, _f2);
        
        _annotation _a = _annotation.of("@interface nested { public static final int f = 123; }");
        _annotation _a2 = _annotation.of("@interface nested { public static final int f = 123; }");
        
        assertEquals( _a, _a2);
    }
    
    public void testTargets(){
        _annotation _a = _annotation.of("aaaa.bbbb.A").targets(ElementType.CONSTRUCTOR );
        System.out.println( _a);
        _a = _annotation.of("aaaa.bbbb.A").targets(ElementType.CONSTRUCTOR, ElementType.FIELD);
        System.out.println( _a);
    }
    public void testAnnotatAnonymousBody(){
        _annotation _a = _annotation.of("aaaa.bbbb.Anny", new Object(){
            int a;
            String s;
            float f;

            int[] vs = {1,2,3,4,5};
            String ss = "Some String";
        });
        assertEquals(5, _a.listElements().size());
        assertTrue($ex.of("{1,2,3,4,5}").matches(_a.getElement("vs").getDefaultValue()));
        assertTrue($ex.stringLiteralEx("Some String").matches(_a.getElement("ss").getDefaultValue()));
    }

    public void testImport(){
        _annotation _an = _annotation.of(ComplexAnnotationType.class);
        assertEquals( "test", _an.getPackage() );
        assertTrue(_an.hasImport( ann2.class));
        assertTrue(_an.hasJavadoc());
        assertTrue( _an.hasAnnos());
        assertTrue( _an.getAnnos().is( "@ann", "@ann2(k='5',v=7)"));
        assertTrue( _an.getModifiers().is( "public"));
        assertTrue(_an.isPublic());

        assertTrue( _an.hasFields() );
        assertTrue( _an.getField( "V").getModifiers().is( "public static final") );
        assertTrue( _an.getField("V").isType( int.class));
        assertEquals( Ex.of( 102), _an.getField("V").getInit());

        _element _p = _an.getElement("value");
        assertTrue(_p.getAnnos().is( "@ann", "@ann2(k='3',v=2)"));
        assertTrue(_p.getJavadoc().getContent().contains( "javadoc"));
        assertFalse( _p.hasDefaultValue());
        assertTrue( _p.isType( int.class));

        _p = _an.getElement("s");
        assertFalse( _p.hasJavadoc() );
        assertFalse( _p.hasAnnos() );
        assertTrue( _p.hasDefaultValue());
        assertEquals( Ex.stringLiteralEx( "String"), _p.getDefaultValue());

        _p = _an.getElement("clazz");
        assertFalse( _p.hasJavadoc() );
        assertFalse( _p.hasAnnos() );
        assertTrue( _p.hasDefaultValue());
        assertEquals( _p.getType(), _typeRef.of(Ast.typeRef("Class[]")));

        assertTrue( _p.isType(Class[].class) );
        assertEquals( Ex.arrayInitializerEx( "{}"),_p.getDefaultValue());

        _p = _an.getElement("vval");
        assertFalse( _p.hasJavadoc() );
        assertFalse( _p.hasAnnos() );
        assertTrue( _p.hasDefaultValue());
        assertTrue( _p.isType( int.class) );
        assertEquals( Ex.nameEx("V"),_p.getDefaultValue());
        assertEquals( "ComplexAnnotationType", _an.getName());

        assertEquals( 4, _an.listNests().size());
        assertEquals( 1, _an.listNests(t-> t instanceof _class).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _interface).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _enum).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _annotation).size());


        //verify we can find the field in each nested TYPE
        assertEquals( Ex.intLiteralEx(123), _an.listNests(t-> t instanceof _class).get(0).getField("f").getInit() );
        assertEquals( Ex.intLiteralEx(123), _an.listNests(t-> t instanceof _enum).get(0).getField("f").getInit() );
        assertEquals( Ex.intLiteralEx(123), _an.listNests(t-> t instanceof _interface).get(0).getField("f").getInit() );
        assertEquals( Ex.intLiteralEx(123), _an.listNests(t-> t instanceof _annotation).get(0).getField("f").getInit() );


        //add NESTS
        _an.nest( _class.of("class NC{}") );
        _an.nest( _enum.of("enum NE{;}") );
        _an.nest( _interface.of("interface NI{}") );
        _an.nest( _annotation.of("@interface NA{}") );

        //verify they have been added
        assertEquals( 1, _an.listNests(t-> t instanceof _class && t.getName().equals("NC")).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _interface && t.getName().equals("NI")).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _enum && t.getName().equals("NE")).size());
        assertEquals( 1, _an.listNests(t-> t instanceof _annotation && t.getName().equals("NA")).size());

        System.out.println( _an );

        //find every int literal in the code and return it
        List<IntegerLiteralExpr> ls =
                Walk.list(_an, Ex.INT_LITERAL, i-> i.asInt() > 0 );

        System.out.println( ls );

    }
}
