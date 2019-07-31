package test;

import java.io.Serializable;
import test.subpkg.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * This represents a complex class with complex relationships
 * 
 * trying to exercise all different kinds of things:
 * extends a base Class with Type Parameter
 * implements an interface with a Type Parameter
 * overrides a default method
 * accepts a default method on an interface
 * marker ANNOTATIONS
 * ANNOTATIONS with key values
 * contains a static initializer block
 * contains a local class
 * contains a nested enum
 */
@ann
@ann2(k = 1, v = 'y')
public class ComplexClass<T extends Base>
    extends Base<String>
    implements MarkerInterface<Integer>, WithDefaultMethods<Map<String,Integer>> {

    static {
        System.out.println( 34 );
    }

    /**
     * field JAVADOC
     */
    @ann
    @ann2(k = 2, v = 'g')
    public static final List<String> l = new ArrayList<>();

    /**
     * ctor JAVADOC
     */
    @ann
    @ann2(k = 3, v = 'i')
    protected <e extends Base> ComplexClass( @ann @ann2(k = 5) final String s,
        int... varArgs3 )
        throws AnotherException, DumbException {
        
        System.out.println( 12 );
    }

    /**
     * method JAVADOC
     */
    @ann
    @ann2(k = 3, v = 'i')
    public static <e extends Base> void doIt( @ann @ann2(k = 5) final String xx,
        int... varArgs )
        throws DumbException, AnotherException {
        System.out.println( 15 );
    }

    @Override
    public String abstractMustOverride( String s ) {
        /** A Local Class */
        @ann @ann2(k=362)
        class L extends Base implements Serializable{
            
            @ann @ann2(k=9)
            public int a;
            

            @Override
            public Object abstractMustOverride( String s ) {
                throw new UnsupportedOperationException( "Not supported yet." );
            }
        }
        return (String)new L().abstractMustOverride("34");
    }
    
    @ann @ann2(k='4')
    enum E{
        
        @ann @ann2(k='d')A,        
        B(1,"A"),        
        @ann @ann2(k='b')F(3,"G"){
            
            int withField = 345;
            
            @ann2(k='5')
            public String withBodyMethod(){
                return "Custom toString";
            }
        };
        
        
        private final String name;
        private final int a;
        
        private E(){
            name = "FF";
            a = -1;
        }
        
        private E(int i, String s){
            name = s;
            a = i;
        }
    }
}
