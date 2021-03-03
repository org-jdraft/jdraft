package org.jdraft;

import junit.framework.TestCase;

public class _annoMemberTest extends TestCase {

    public void testA(){
        _annoMember _am = _annoMember.of("int value()");
        assertTrue(_am.isType(int.class));
        assertTrue(_am.isType(t-> t.isPrimitiveType()));

        assertTrue(_am.isNamed("value"));
        assertTrue(_am.isNamed("val$any$"));
    }

    @interface A{
        int value();
    }

    @interface B{
        char c();
        int i();
    }

    @interface C{
        @A(1) @B(c='a', i=1) String[] values() default {"S"};
    }

    public void testFull(){
        _annoMember _am = _annoMember.of("@Ayy @Bee(1) @Cee(k=1) @Dee(a=1,b='c') String[] values() default {};");
        assertTrue(_am.hasAnnoNamed("Ayy"));
        assertTrue(_am.hasAnnoNamed("Bee"));
        assertTrue(_am.hasAnnoNamed("Cee"));
        assertTrue(_am.hasAnnoNamed("A$any$"));

        assertTrue( _am.hasAnno("@Ayy") );
        assertTrue( _am.hasAnno("@Ayy($any$)") );
        assertTrue( _am.hasAnno("@Bee($any$)") );
        assertTrue( _am.hasAnno("@Bee(1)") );

        assertTrue( _am.hasAnno("@Cee($any$)") );
        assertTrue( _am.hasAnno("@Cee(k=1)") );

        assertTrue( _am.hasAnno("@Dee(a=1,b='c')") );
        assertTrue( _am.hasAnno("@Dee($any$)") );
        assertTrue( _am.hasAnno("@Dee(a=1)") );

        //find
        assertTrue( _am.hasAnno("@Dee(a=$any$)") );
        assertTrue( _am.hasAnno("@Dee(a = $any$)") );


        assertTrue(_am.isType(String[].class));
    }

    public void testKey(){
        _annoMember _am = _annoMember.of("@Ayy @Bee(1) @Cee(k=1) @Dee(a=1,b='c') String[] values() default {};");

        assertTrue( _am.hasAnno("@Dee($any$=1)") );
        /*
        assertTrue(_am.getAnnos().has("@Dee(b='c')"));

        assertTrue(_am.getAnnos().has("@Dee($any$='c')"));
        */
    }
    public void testPartialOutOfOrder(){
        _annoMember _am = _annoMember.of("@Ayy @Bee(1) @Cee(k=1) @Dee(a=1,b='c') String[] values() default {};");
        assertTrue( _am.hasAnno("@Dee(a=1)") );
    }

    public void testLeftDeepFeatureCompare(){
        _stringExpr _x = _stringExpr.of("x");
        _stringExpr _y = _stringExpr.of("y");
        _tree._node.leftDeepFeatureCompare(_x, _y);
    }

}