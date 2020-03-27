package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.macro._equals;

public class FlattenTest extends TestCase {


    public void testFlatten(){
        @_equals
        class f{
            int x;
            void m(){
                $body:{
                  System.out.println( 1 );
                  System.out.println( 2 );
                }
            }
        }

        _class _c = _class.of(f.class);
        Tree.flattenLabel(_c.astCompilationUnit(), "$body");


        System.out.println( _c );
    }

}
