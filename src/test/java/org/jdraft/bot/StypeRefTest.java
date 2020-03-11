package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._typeRef;
import org.jdraft.pattern.$typeParameter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StypeRefTest extends TestCase {

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
        assertTrue(_t.ast().isClassOrInterfaceType());
        assertTrue(_t.ast().asClassOrInterfaceType().isUsingDiamondOperator());

        _t = _typeRef.of( Ast.typeRef("Map<>"));
        assertTrue(_t.ast().isClassOrInterfaceType());
        assertTrue(_t.ast().asClassOrInterfaceType().isUsingDiamondOperator());
        assertTrue( $typeRef.of(t-> t.isUsingDiamondOperator()).matches("Map<>") );
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
        $typeRef.of().describeIn(T.class);

        assertEquals(1, $typeRef.of(t-> t.isArrayType()).countIn(T.class)); //a single array type
        assertEquals(2, $typeRef.of(float.class).countIn(T.class)); //float and float[] are separate types
        assertEquals(1, $typeRef.of(float.class).$not(t-> t.isArrayType()).countIn(T.class)); //float and float[] are separate types
        assertEquals( 3, $typeRef.of(String.class).countIn(T.class));


        $typeParameter.of().printIn(T.class);

        $typeRef.of("Blahde").describeIn(T.class);
        //$typeRef.of(t->t.isTypeParameter()).describeIn(T.class);
        assertEquals( 1, $typeRef.of(t-> t.isTypeParameter()).countIn(T.class));


    }
}
