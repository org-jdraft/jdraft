package test;

import java.io.Serializable;
import test.subpkg.MarkerInterface;
import test.subpkg.WithDefaultMethods;
import test.subpkg.ann2;

/**
 * javadocs
 * @author Eric
 * @param <Y>
 * @param <Z>
 */
@ann
@ann2(k='d')
public interface ComplexInterface <Y, Z extends Base> 
    extends MarkerInterface<String>, WithDefaultMethods<Serializable> {
 
    /** field javadoc */
    @ann @ann2(k='2',v=3)
    static final int VALUE = 120;
    
    @ann @ann2(k='F',v=12345)
    static int getValue(){
        return 12345;
    }
    
    /**
     * the genMethod
     * @param <E>
     * @param s
     * @param vals
     * @return 
     */    
    <E extends Base> E genMethod(@ann @ann2(k='8',v=12) String s,final int...vals );
    
    /** 
     * javadoc
     * @return something
     */
    @ann
    default Z doIt(){
        System.out.println( 3 );
        return null;
    }     
    
    /**
     * Javadoc 
     * @param <T> 
     */
    public static class C<T extends Base>{
        int f;
        
        static{
            System.out.println("Static Block");
        }
    }
    
    /**
     * Chaos
     */
    public enum E{
        A,
        B;
    }
    
    /**
     * Total Chaos
     */
    public @interface A{
        String value() default "SOMEDEFAULT";
    }
}