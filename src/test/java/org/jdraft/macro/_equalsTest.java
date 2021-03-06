package org.jdraft.macro;

import org.jdraft.*;
import org.jdraft.pattern.$method;
import org.jdraft.runtime.*;

import junit.framework.TestCase;

import java.util.Map;
import java.util.UUID;

public class _equalsTest extends TestCase {

    public void testAutoEquals(){
        @_promote("fffff")
        @_equals
        class V{
            public int a,b,c;
            public int[] x;
            public float z;
            public double d;
            public Map<String,Integer> map;
            public String[] names;
            public UUID blah = UUID.randomUUID();
        }

        System.out.println( _class.of(V.class) );
    }


    public void testSuperEquals(){
        /** NOTE: we made this a String (not a lambda, etc.) to improve startup perf */
        $method $equals = $method.of(
                "public boolean equals(Object o){",
                "if(o == null) {",
                "   return false;",
                "}",
                "if(this == o) {",
                "    return true;",
                "}",
                "if(getClass() != o.getClass()){",
                "    return false;",
                "}",
                "$className$ test = ($className$)o;",
                "boolean eq = true;",
                "$callSuperEquals: {eq = super.equals( test );}",
                "$BODY:{}",
                "return eq;",
                "}");

        System.out.println( $equals.body.toString() );
    }

    /**
     * Verify that if _2_template a class with _autoEquals
     * AND it is a derived class, it will call super.typesEqual()
     */
    public void testSubClassCallsSuperEqualsAutoEquals(){

        @_equals
        class A{
            public int a = 100;
        }

        @_equals
        class B extends A{
            public boolean b = true;
        }

        _class _a = _class.of(A.class);

        //_class _a = _java._class( A.class);
        System.out.println(_a );
        _class _b = _class.of(B.class);
        //_class _b = _java._class( B.class);
        System.out.println(_b );

        //compile the classes
        _runtime _ab = _runtime.of(_a, _b);
        //create (2) new instances
        _proxy _a1 = _ab.proxy(_b);
        _proxy _a2 = _ab.proxy(_b);

        assertTrue( _a1.equals(_a2)); //make sure they are equal

        _a1.set("a", 200); //change the field in the instances base class

        assertFalse( _a1.equals(_a2) ); //verify that it is no longer equal
        //i.e. verify the B class calls super.typesEqual()
    }

}
