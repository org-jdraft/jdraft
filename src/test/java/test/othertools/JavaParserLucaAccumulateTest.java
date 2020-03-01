package test.othertools;

import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft.pattern.$typeRef;

public class JavaParserLucaAccumulateTest extends TestCase {

    public void testAcc() {
        assertTrue( generateAccumulate(int.class).getParameter(0).isTypeRef(int[].class) );
        assertTrue( generateAccumulate(float.class).getParameter(0).isTypeRef(float[].class) );
        assertTrue( generateAccumulate(double.class).getParameter(0).isTypeRef(double[].class) );
        assertTrue( generateAccumulate(String.class).getParameter(0).isTypeRef(String[].class) );
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
