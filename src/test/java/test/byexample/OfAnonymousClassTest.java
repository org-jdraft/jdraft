package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._abstract;
import org.jdraft.macro._default;
import org.jdraft.macro._static;
import org.jdraft.macro._toInit;

import java.io.Serializable;

public class OfAnonymousClassTest extends TestCase {

    public void testBuiltOfAnonymousClass(){
        // types can be build with the help of an anonymous class
        //
        _class _c = _class.of( "aaaa.bbbb.C", new Serializable(){
            int i=0;
            public int m(){
                return i + 10;
            }

            @_toInit
            void h(){
                System.out.println("Instance init block");
            }
        });
        System.out.println( _c );

        _interface _i = _interface.of("aaaa.I", new Object(){
            int i = 0;
            @_abstract int m(){ return 3; /*this body is removed*/ }
            @_static String g(){
                return "static method on interface";
            }
            @_default
            void d(){
                System.out.println( "do something in default method" );
            }
        });

        System.out.println( _i );

        _enum _e = _enum.of( "aaaa.E", new Serializable(){
            _constant A,B,C;
            _constant D = new _constant(1, "D");

            @_static void mm(String s){
                System.out.println(s);
            }
        });
        //public enum E {
        //    A, B, C, D(1, "D");
        //    static void mm(String s) {
        //        System.out.println(s);
        //    }
        //}

        System.out.println( _e );

        _method _m = _method.of(new Object(){
            public String getName(){
                return this.name;
            }
            String name;
        });

        _constructor _ct = _constructor.of( new Object(){
            void C( int num, String...values){
                this.num = num;
                this.values = values;
            }
            String[] values;
            int num;
        });

        System.out.println( _c );
    }

}
