package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.text.Tokens;

import java.util.List;

public class $annoExprsTest extends TestCase {

    public void testMatchAny(){
        //verify that "anyMatch" matches no annotations
        assertTrue( $annos.of().matches(""));

        //singe annotations
        assertTrue( $annos.of().matches("@A"));
        assertTrue( $annos.of().matches("@A(1)"));
        assertTrue( $annos.of().matches("@A(k=1,v=2)"));

        //multiple annotations
        assertTrue( $annos.of().matches("@A @B"));
        assertTrue( $annos.of().matches("@A(1) @B(1)"));
        assertTrue( $annos.of().matches("@A(k=1,v=2) @C(k=1,v=2)"));
    }

    public void testMatchAsSingle() {
        assertTrue($annos.as("@A").matches("@A"));
        assertFalse($annos.as("@A").matches("@B"));
        assertFalse($annos.as("@A").matches("@A @B"));
        assertFalse($annos.as("@A").matches("@A(1)"));
        assertFalse($annos.as("@A").matches("@A(key=1,value=2)"));

        assertTrue($annos.as("@A(1)").matches("@A(1)"));
        assertFalse($annos.as("@A(1)").matches("@A"));
        assertTrue($annos.as("@A(1)").matches("@A(value=1)"));
        assertFalse($annos.as("@A(1)").matches("@A(value=1,other=2)"));

        assertTrue($annos.as("@A(k=1,v=2)").matches("@A(k=1,v=2)"));
        assertTrue($annos.as("@A(k=1,v=2)").matches("@A(v=2,k=1)")); //order doenst matter for properties

        assertFalse($annos.as("@A(k=1,v=2)").matches("@A(k=2,v=1)"));

        //draft
        assertEquals(_annos.of("@A(k=1,v=2)"), $annos.as("@A(k=1,v=2)").draft());
    }

    public void testMatchMultiple(){
        assertTrue($annos.of("@A @B").matches("@A @B"));
        assertTrue($annos.of("@A @B").matches("@A(1) @B(2)"));
        assertTrue($annos.of("@A @B").matches("@A(key=1) @B(val=1,v=3)"));
        assertTrue($annos.of("@A @B").matches("@B @A")); //order doesnt matter (for annotations)

        //As
        assertTrue($annos.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
        assertTrue($annos.as("@A(1) @B").matches("@A(1) @B"));
        assertTrue($annos.as("@A(1) @B").matches("@B @A(1)"));
        assertFalse($annos.as("@A(1) @B").matches("@A(1) @B(2)"));

        assertTrue($annos.as("@A @B").matches("@A @B"));
        assertTrue($annos.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
    }

    public void test$List(){
        assertEquals( $annos.of("@my$name$(key=$value$)").$list().get(0), "name");
        assertEquals( $annos.of("@my$name$(key=$value$)").$list().get(1), "value");
    }

    public void test$ListNormalized(){
        assertEquals( $annos.of("@my$name$(key=$value$)").$listNormalized().get(0), "name");
        assertEquals( $annos.of("@my$name$(key=$value$)").$listNormalized().get(1), "value");
    }

    @interface A{}
    @interface B{}
    @interface C{}

    public void testFromClass(){
        _annos _ars = _annos.of("@A");
        assertTrue(_ars.is("@A"));
        $annos $ars = $annos.of(_ars);
        assertTrue( $ars.matches("@A"));

        _ars = _annos.of(B.class);
        assertTrue( _ars.is("@B"));

        assertTrue($annos.of(_anno.of(B.class)).matches("@B"));

        $annos.of(B.class).matches("@B");

        $annos.of(_anno.of(B.class)).matches("@B");
    }

    public void test$Or(){

        $annos $BandC = $annos.of(B.class, C.class);
        assertTrue($BandC.matches("@B @C"));
        assertTrue($BandC.matches("@C @B"));

        //EITHER BOTH A OR B and C
        $annos $ars = $annos.or( $annos.of(A.class), $annos.of(B.class, C.class));

        assertTrue($ars.matches("@A"));
        assertTrue($ars.matches("@B @C"));
        assertTrue($ars.matches("@C @B"));

        assertTrue($ars.matches("@A(1)"));
        assertTrue($ars.matches("@A(1) @B @C"));
        //$annoRefs.of(A.class, B.class, C.class);
    }

    public void testHardCodeInd() {
        $anno $ae = $anno.of("@A($value$)");
        assertTrue($ae.entryPairs.isMatchAll());
        assertTrue($ae.matches("@A(1)"));
        assertTrue($ae.matches("@A(2)"));
        assertTrue($ae.matches("@A('f')"));
        $ae.$hardcode("value", 1);
        assertFalse($ae.entryPairs.isMatchAll());
        System.out.println( $ae.entryPairs.getBot(0) );
        assertTrue( $ae.entryPairs.getBot(0).matches("1"));
        assertTrue($ae.matches("@A(1)"));
        assertFalse($ae.matches("@A('c')"));
        assertFalse($ae.matches("@A(2)"));
    }


    //public class $Print{}
    //public class $PrintTree{}

    public static<_T extends _tree._node, _F, $S> $bot<_T, $S> fromFeature(_feature<_T, _F> _f){
        if(_f.getTargetClass() ==_method.class){
            if( _f.getFeatureId() == _feature._id.NAME ) {
                return ($bot<_T, $S>)$name.of(_name.Use.METHOD_NAME);
                //return ($selector<_T, $S>) $method.of( $name.of());
            }
        }
        return null;
    }

    public static class $List<_T extends _tree._node, _F>{

        public  static<_T extends _tree._node, _F> $List<_T, _F> of(_feature<_T, _F> _f){
            return new $List(_f);
        }

        public _feature<_T, _F> _f;

        public $List(_feature<_T, _F> _f){
            this._f = _f;
        }


        public List<_F> in(Class clazz){
            return null;
            /*
             $selector<_T, _F> sel = fromFeature(_f);
             CompilationUnit cu = Ast.of(clazz);
             List<_F> found = new ArrayList<>();
             cu.walk(n -> {
                 Select s = sel.matches( n );
                 if( s != null ){

                 }
             });
             */
        }
    }

    public void testI$List(){
         List<String> ns = $List.of(_method.NAME).in($annoExprsTest.class);
    }

    public void testExprs(){
        $annos $aes = $annos.of( $anno.of("@A($value$)") );
        assertTrue($aes.annosList.getBot(0).entryPairs.isMatchAll());

        assertTrue($aes.matches("@A(1)"));
        assertTrue($aes.matches("@A(2)"));
        assertTrue($aes.matches("@A('f')"));

        $aes.$hardcode(Tokens.of("value", 1));
        assertNotNull( $aes.annosList.getBot(0).entryPairs.selectFrom(_anno.of("A(1)")) );
        //System.out.println( "LIST BOTS"+ $aes.$annoExprsList );
        //System.out.println( "FIRST BOT"+ $aes.$annoExprsList.get(0) );
        //System.out.println("BOTTTTTT"+( $aes.$annoExprsList.get(0).entryPairs.getBot(0)) );
        assertNull( $aes.annosList.getBot(0).entryPairs.selectFrom(_anno.of("A(2)")) );
        assertFalse($aes.annosList.getBot(0).entryPairs.isMatchAll());
        assertTrue($aes.matches("@A(1)"));
        assertFalse($aes.matches("@A('c')"));
        assertFalse($aes.matches("@A(2)"));

    }
    public void testHardCode(){
        $annos $ars = $annos.or( $annos.of("@A($value$)"), $annos.of("@my$name$($key$=2)") );

        assertTrue( $ars.matches("@A(1)"));
        assertTrue( $ars.matches("@A(2)"));
        $ars.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $ars.matches("@A(1)"));
        assertFalse( $ars.matches("@A(2)"));
    }

}
