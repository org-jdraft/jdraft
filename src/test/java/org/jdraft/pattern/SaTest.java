/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.pattern;

import com.github.javaparser.ast.expr.MemberValuePair;
import org.jdraft.Expressions;
import org.jdraft._annoRef;
import org.jdraft._annoRefs;
import org.jdraft._class;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 *
 * @author Eric
 */
public class SaTest extends TestCase {

    public void test$annosAs(){
        //
        $annoRefs $as = $annoRefs.as();

        assertTrue($as.matches(_annoRefs.of()));
        assertTrue($as.matches(""));
        assertFalse($as.matches("@A"));

        $as = $annoRefs.as("@A");

        assertTrue($as.matches("@A"));
        assertFalse($as.matches(""));
        assertFalse($as.matches("@A(1)"));
        assertFalse($as.matches("@A", "@B"));

    }
    public void goal(){
        //this is a prototype for a $method which matches all methods that
        //have a matching annotation @A(count=1)
        
        // $method.of( $a.of("@A(count=1)"), $typeDecl.of(String.class) );
    }
    
    public void testConstructWith$Components(){
        $.anno($id.of("A")).matches("@A(1)");
        $.anno("A").matches("@A(1)");
        
        
        $annoRef $a = $annoRef.of( $id.of("A"), $annoRef.$memberValue.of(Expressions.of(1)));
        assertTrue( $a.matches("@A(1)") );        
    }
    
    public void testOverrideParameter(){
        
        System.out.println( $annoRef.of("B").draftToString() + "" );
        
        assertEquals(_annoRef.of("B"), $annoRef.of().draft("$anno", "@B"));
        assertEquals(_annoRef.of("B").toString(), $annoRef.of()
                .draftToString("$anno", "@B"));
    }
    
    public void testAnyCompose(){
        try{
            $annoRef.of().draft();
            fail("expected exception for no name");
        }catch(Exception e){
            
        }        
        //override parameter
        assertEquals(_annoRef.of("E"), $annoRef.of().draft("$anno", "@E"));
        
    }
    
    public void testAnyMatch(){
        class T{
            @Deprecated int a;
            
            @Deprecated
            void m(){}
        }
        
        assertEquals( 2, $annoRef.of().countIn(T.class));
    }
    
    public void testOutOfOrderKeyValues(){
        $annoRef a = $annoRef.of("@A(a=1,b=2)");
        assertTrue( a.matches(_annoRef.of("@A(b=2,a=1)")) );
    }
    
    public void testParensOrNoParens(){
        $annoRef a = $annoRef.of("@E");
        
        assertTrue(a.matches(_annoRef.of("@E")));
        //this doesnt work
        assertTrue(a.matches(_annoRef.of("@E()")));
    }
    
    public void testFullyQualified(){
        $annoRef a = $annoRef.of("@A");
        assertTrue( a.matches("@A") );
        assertTrue( a.matches("@A()") );
        assertTrue( a.matches("@fully.qualified.A") );
        assertTrue( a.matches("@fully.qualified.A()") );
        
        assertFalse( a.matches("@B") );
        assertFalse( a.matches("@fully.qualified.A.B") );
        
        
        a = $annoRef.of("@fully.qualified.A");
        assertTrue( a.matches("@fully.qualified.A") );
        assertTrue( a.matches("@fully.qualified.A(1)") );
        assertTrue( a.matches("@fully.qualified.A(a=1)") );
        assertTrue( a.matches("@fully.qualified.A(a=1,b=2)") );
        assertFalse( a.matches("@A") );
        assertFalse( a.matches("@A()") );
        assertFalse( a.matches("@fully.qualified.B") );
        assertFalse( a.matches("@different.packge.A") );
        
    }
    
    public void testSsa(){
        $annoRef a = $annoRef.of("@A(count=1)");
        assertTrue( a.matches("@A(count=1)") );
    }
    
    public void testG(){
        //a marker annotation match
        $annoRef a = $annoRef.of("@A");
        assertTrue( a.matches("@A") ); //matches a marker
        assertTrue( a.matches("@A()") ); //matches a marker with parens
        assertTrue( a.matches("@A(1)") ); //matches a "value" annotation
        assertTrue( a.matches("@A(count=1)") ); //matches a keyvalue
        assertTrue( a.matches("@A(count=1,str=\"eric\")") );//matches a multi key value
        
        
        
        //an annotation with single value
        a = $annoRef.of("@A(\"value\")");
        assertFalse( a.matches("@A") ); //X missing a value
        assertTrue( a.matches("@A(\"value\")") ); //exact match w/ value
        assertTrue( a.matches("@A(key=\"value\")") );// with a key=(value)
        assertFalse( a.matches("@A(key=\"val\")") ); // X not the correct value
        
        //an annotation with key value
        a = $annoRef.of("@A(count=1)");
        assertFalse( a.matches("@A") );
        assertTrue( a.matches("@A(1)") ); //simple value matches
        assertTrue( a.matches("@A(count=1)") ); //exact match
        assertFalse( a.matches("@A(count=2)") ); //X wrong value
        assertTrue( a.matches("@A(count=1,str=\"eric\")") ); 
        assertTrue( a.matches("@A(str=\"eric\", count=1)") ); //in whatever order
        
        //an annotation with multiple keyvalues
        a = $annoRef.of("@A(count=1,str=\"e\")");
        assertFalse( a.matches("@A(1)") ); //simple value matches
        assertFalse( a.matches("@A(count=1)") ); //exact match
        assertFalse( a.matches("@A(count=2)") ); //X wrong value
        assertTrue( a.matches("@A(count=1,str=\"e\")") ); 
        assertTrue( a.matches("@A(str=\"e\",count=1)") ); // different order
        assertTrue( a.matches("@A(str=\"e\", count=1,extra=3.4)") ); //extra args
        
        assertFalse( a.matches("@A(count=1, str=\"d\")") ); //X wrong value
        
    }
    public void testA(){
        $annoRef a = $annoRef.of("@A");
        assertTrue( a.matches("@A"));
    }
    
    public void testS(){
        $annoRef.$memberValue.of().matches(new MemberValuePair());
        $annoRef.$memberValue.of().matches(new MemberValuePair("a", Expressions.of(1)));
        
        //static  membervalues
        $annoRef.$memberValue.of("a", "100").matches(new MemberValuePair("a", Expressions.stringLiteralEx("100")));
        $annoRef.$memberValue.of("a", "100").matches(new MemberValuePair("a", Expressions.of("100")));
        
        //dynamic value
        $annoRef.$memberValue.of("a", "$value$").matches(new MemberValuePair("a", Expressions.of("100")));
        $annoRef.$memberValue.of("a", "$value$").matches(new MemberValuePair("a", Expressions.of("1")));
        $annoRef.$memberValue.of("a", "$value$").matches(new MemberValuePair("a", Expressions.stringLiteralEx("Blah")));
        $annoRef.$memberValue.of("a", "$value$").matches(new MemberValuePair("a", Expressions.of(new int[]{1,2,3,4})));
        
        
    }
    
    public void testSingleValueAnno(){
       MemberValuePair mvp = new MemberValuePair().setValue(Expressions.of(1));
       assertTrue($annoRef.$memberValue.of(Expressions.of(1)).matches(mvp));
    }
 
    
    
    public void testAny(){
        _class _c = _class.of("C");
        assertEquals( 0, $annoRef.of().listIn(_c).size());
        
        //add a top level annotation
        _c.addAnnoRefs(Deprecated.class);
        assertEquals( 1, $annoRef.of().listIn(_c).size());


        $annoRef.of().forEachIn(_c, a-> System.out.println(a.getName()));
        $annoRef.of().forEachIn(_c, a-> !a.hasValues(), a-> System.out.println( a) );


    }
    
    @interface R{ int value() default 10; }
    @interface S{ Class[] clazz() default String.class; }
   
    public void testMatchClass(){
        
        assertTrue($annoRef.of(R.class).matches("R"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R"));
        
        assertTrue($annoRef.of(R.class).matches("R()"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R()"));
        
        assertTrue($annoRef.of(R.class).matches("R(1)"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R(2)"));
        
        assertTrue($annoRef.of(R.class).matches("R(value=1)"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R(value=2)"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R()"));
        
        assertTrue($annoRef.of(R.class).matches("R()"));
        assertTrue($annoRef.of(R.class).matches("@draft.java.proto.SaTest.R()"));
        
        /*
        assertTrue($anno.of("R($any$)").matches("@draft.java.proto.SaTest.R()"));  
        
        assertTrue($anno.of("R($any$)").matches("@draft.java.proto.SaTest.R()"));   
        assertTrue($anno.of("R($any$)").matches("R()"));   
        
        assertTrue($anno.of("R($any$)").matches("@draft.java.proto.SaTest.R(1)"));   
        assertTrue($anno.of("R($any$)").matches("R(2)"));
        
        assertTrue($anno.of("R($any$)").select("@draft.java.proto.SaTest.R(1)").is("any", Expr.of(1) ) );   
        assertTrue($anno.of("R($any$)").select("@draft.java.proto.SaTest.R(1)").is("any", "1" ) );  
       
        assertTrue($anno.of("R($any$)").select("@draft.java.proto.SaTest.R(1)").is("any", Expr.of(1) ) );   
        assertTrue($anno.of("R.class, "($any$)").select("@draft.java.proto.SaTest.R(1)").is("any", "1" ) );  
        */
        
        
        
        assertTrue($annoRef.of(R.class).matches("R(2)"));
        
        assertTrue($annoRef.of(S.class).matches("S()"));
        assertTrue($annoRef.of(S.class).matches("S(Float.class)"));
        assertTrue($annoRef.of(S.class).$and(a-> a.hasValue(v -> v.isClassExpr())).matches("@S(Float.class)"));
        assertFalse($annoRef.of(S.class).$and(a-> a.hasValue(v -> v.isClassExpr())).matches("@S"));
        assertFalse($annoRef.of(S.class).$and(a-> a.hasValue(v -> v.isClassExpr())).matches("@S({Float.class, String.class})"));
        
    }
    
    /**
     * Verify I can match $a with Class based on simple and/or
     * fully qualified annotations
     */
    public void testFullyQual(){        
        // when I do this, I need to change the regex as EITHER
        // 
        $annoRef a = $annoRef.of(R.class);
        
        @SannoRefTest.R
        class C{}        
        _class _c = _class.of(C.class);        
        assertNotNull( a.firstIn(_c) );
        
        @R
        class D{}        
        _class _d = _class.of(D.class);        
        assertNotNull( a.firstIn(_d) );                
    }
    
    @interface name{
        int value() default 1;
        String prefix() default "";
    }

    @interface name2{
        String string();
    }
    
    @interface num{
        int x();
    }
    
    @interface val{
        float value();
    }

    public void testN(){
        @val(3.1f)
        @num(x=2)
        class C{
            
        }
    }
    public void testFirst(){
        @name(2)
        class C{
            @name(3)
            int g =1;
        }
        
        _class _c = _class.of(C.class);
        assertNotNull( $annoRef.of("name").firstIn(_c) );
        
        //verify that we can find 
        assertNotNull( $annoRef.of("name").firstIn(_c,
                //there is an Integer attribute value that is odd
                (a)-> a.hasValue(e -> e.isIntegerLiteralExpr() && e.asIntegerLiteralExpr().asInt() % 2 == 1)) );
        
        assertNotNull( $annoRef.of("name").firstIn(_c, (a) -> a.hasValue(3)) );
        assertNotNull( $annoRef.of("name(3)").firstIn(_c ));
        assertNotNull( $annoRef.of("name(3)").firstIn(_c, _a-> _a.hasValue(3)) );
    }
     
    public void testStatic$a(){
        $annoRef a = $annoRef.of("@name");
        assertEquals( _annoRef.of("@name"), a.draft());
        assertTrue( a.matches(_annoRef.of("@name")));

        @name
        class C{
            @name int x;
            @name void m(){}
        }
        _class _c = _class.of(C.class);
        assertEquals( 3, a.listSelectedIn(_c).size() );
        _c = a.replaceIn(_c, $annoRef.of("@name2"));
        assertEquals( 0, a.listSelectedIn(_c).size() ); //verify they are all changed
        assertEquals( 3, a.of("@name2").listSelectedIn(_c).size() ); //verify they are all changed
    }

    public void testDynamicAnno(){
        //any @NAME annotation with a prefix
        $annoRef a = $annoRef.of("@name(prefix=$any$)");

        assertTrue( a.matches( _annoRef.of("@name(prefix=\"1\")") ));
        
        assertNotNull( a.select( _annoRef.of("@name(prefix=\"1\")") ));

        System.out.println( "GOTTEN " + a.select(_annoRef.of("@name(prefix=\"1\")") ).tokens);
        assertTrue( a.select(_annoRef.of("@name(prefix=\"1\")") ).is("any", Expressions.stringLiteralEx("1")) );

        assertTrue( a.select(_annoRef.of("@name(prefix=\"ABCD\")")).is("any", "ABCD"));
        assertTrue( a.$list().contains("any"));


        @name(prefix="Mr.")
        class C{
            @name int x;
            @name(prefix="Mrs.") void m(){}
        }
        _class _c = _class.of(C.class);
        assertEquals( 2, a.listSelectedIn(_c).size());

        // In this case, it'd be better to just use _walk
        // Here we Transpose the property information from @NAME to the @name2 annotation
        a.replaceIn(_c, $annoRef.of("@name2(string=$any$)") );
        System.out.println(_c );

        _annoRef _a = a.draft("any", "\"Some String\"");
        assertEquals( _annoRef.of("@name(prefix=\"Some String\")"), _a );
    }


     public void testRegex(){
       Pattern p = Pattern.compile("([0-9]+)(?:st|nd|rd|th)");
       assertTrue( p.matcher("1st").matches() );     
       //assertTrue( p.matcher("1").matches() );     
       //assertTrue( p.matcher("11").matches() );     
       assertTrue( p.matcher("2nd").matches() );     
       
       //Pattern p2 = Pattern.compile("(@)(?:draft.java.proto.$aTest.)(R)");
       //Pattern p2 = Pattern.compile("\\@(?:draft.java.proto.$aTest.)?(R)");
       //assertTrue(p2.matcher("@R").matches());
       
       Pattern p3 = Pattern.compile( 
            Pattern.quote("@") + 
            "(?:"+ Pattern.quote("draft.java.proto.$aTest.") + ")?" +
            Pattern.quote("R") );
       
        assertFalse(p3.matcher("@S").matches());
        
        assertTrue(p3.matcher("@R").matches());
        assertTrue(p3.matcher("@draft.java.proto.$aTest.R").matches());       
        assertFalse(p3.matcher("@draft.java.proto.$aTestw.R").matches());       
    }
}
