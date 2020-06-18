package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Types;
import org.jdraft._class;
import org.jdraft._typeRef;
import org.jdraft.macro._packageName;
import org.jdraft.pattern.$typeParameter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class $typeRefTest extends TestCase {

    /**
     * https://github.com/javaparser/javaparser/issues/2682
     */
    public void testGetTypes(){
        @_packageName("test")
        class Test {
            private void ioexc() throws java.io.IOException {
                System.out.println("Hello World");
            }
            void test() {
                try {
                    ioexc();
                }
                catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }

        _class _c = _class.of(Test.class);

        assertEquals(2, $typeRef.VOID.countIn(_c));
        assertEquals(2, $typeRef.of().$not($typeRef.VOID).countIn(_c));
        assertEquals(2, $typeRef.of("java.io.IOException").countIn(_c));
        assertEquals(2, $typeRef.of("IOException").countIn(_c));
        assertEquals(1, $typeRef.of().$not($typeRef.VOID).streamIn(_c).distinct().count());

        assertEquals(1, $typeRef.of("java.io.IOException").$not($typeRef.VOID).streamIn(_c).distinct().count());
    }

    public void testAny(){
        assertTrue( $typeRef.of().matches(int.class) );
        assertTrue( $typeRef.of().matches(String.class) );
        assertTrue( $typeRef.of().matches(List.class) );
        assertTrue( $typeRef.of().matches("int") );
        assertTrue( $typeRef.of().matches("Map<String,Integer>") );
        assertTrue( $typeRef.of().matches("Map<>") );
    }

    public void testPredicate(){
        _typeRef _t = _typeRef.of("Map<>");
        assertTrue(_t.node().isClassOrInterfaceType());
        assertTrue(_t.node().asClassOrInterfaceType().isUsingDiamondOperator());

        _t = _typeRef.of( Types.of("Map<>"));
        assertTrue(_t.node().isClassOrInterfaceType());
        assertTrue(_t.node().asClassOrInterfaceType().isUsingDiamondOperator());
        assertTrue( $typeRef.of().$and(t-> t.isUsingDiamondOperator()).matches("Map<>") );
    }

    public void testCountFindList(){
        class T<S extends Serializable> {
            int a;
            String s;
            List<String> list;
            float[] primArray;
            Map<String,Integer> map;
        }
        assertEquals(11, $typeRef.of().countIn(T.class)); //float and float[] are separate types
        assertEquals(11, $typeRef.of().listIn(T.class).size()); //float and float[] are separate types

       //$typeRef.of().listIn(T.class).forEach( t-> System.out.println("HHHHH"+ t)); //_java.describe(t));
        //$typeRef.of().forEachIn(T.class, t-> System.out.println( t)); //_java.describe(t));
        $typeRef.of().printEachTreeIn(T.class);

        assertEquals(1, $typeRef.of().$and(t-> t.isArrayType()).countIn(T.class)); //a single array type
        assertEquals(2, $typeRef.of(float.class).countIn(T.class)); //float and float[] are separate types
        assertEquals(1, $typeRef.of(float.class).$not(t-> t.isArrayType()).countIn(T.class)); //float and float[] are separate types
        assertEquals( 3, $typeRef.of(String.class).countIn(T.class));


        $typeParameter.of().printIn(T.class);

        $typeRef.of("Blahde").printEachTreeIn(T.class);
        //$typeRef.of(t->t.isTypeParameter()).describeIn(T.class);
        assertEquals( 1, $typeRef.of().$and(t-> t.isTypeParameter()).countIn(T.class));


    }
}
