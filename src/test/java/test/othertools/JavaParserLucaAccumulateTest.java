package test.othertools;

import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft.pattern.$typeRef;

public class JavaParserLucaAccumulateTest extends TestCase {

    public void testAcc() {
        assertTrue( generateAccumulate(int.class).getParam(0).isType(int[].class) );
        assertTrue( generateAccumulate(float.class).getParam(0).isType(float[].class) );
        assertTrue( generateAccumulate(double.class).getParam(0).isType(double[].class) );
        assertTrue( generateAccumulate(String.class).getParam(0).isType(String[].class) );
    }

    public _method generateAccumulate( Class type ){
        _method _m = _method.of(new Object(){
            public int accumulate( int[] nums){
                int acc = 0;
                for( int n : nums ) {
                    acc = acc + n;
                }
                return acc;
            }
        });
        $typeRef.as(int[].class).replaceIn(_m, type.getCanonicalName()+"[]");
        $typeRef.as(int.class).replaceIn(_m, type);
        return _m;
    }
}
