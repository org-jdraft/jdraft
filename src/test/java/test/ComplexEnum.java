package test;

import java.io.Serializable;
import test.subpkg.MarkerInterface;
import test.subpkg.ann2;

/**
 *
 * @author Eric
 */
public enum ComplexEnum implements Serializable, MarkerInterface<String> {
    /**JAVADOC c*/A,
    
    /** JAVADOC constant*/ @ann @ann2(k='o') B(1, "String"){
        
        /** JAVADOC field */ @ann @ann2(k='5') public static final int num = 12233;
        
        /**
         * Method Javadoc
         * @return something
         */
        public final int getNum(){
            return 12345;
        }
    },
    C(2, "Blah");
    
    static{
        System.out.println(12231);
    }
    private ComplexEnum(){}
    
    @ann @ann2(k='y')
    private ComplexEnum(@ann @ann2(k='l',v=6) int i, String... s){
        
    }
    
    public static void AMethod(String...vals){
        System.out.println( 23123 );
    }
    
}
