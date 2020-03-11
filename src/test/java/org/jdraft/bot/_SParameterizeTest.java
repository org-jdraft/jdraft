package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$class;

public class _SParameterizeTest extends TestCase {

    public void testNest(){
        _class _c = _class.of("C", new Object(){
           @_$({"int", "type"})
           public int i;

           @_$({"int", "type"})
           public void setI(int i){
               this.i = i;
           }
        });
        $class $c = $class.of(_c);

        System.out.println( $c.draft("type", String.class.getSimpleName()) );

    }
    public void testP(){
        _method _m  =_method.of(new Object(){
            @_$({"Something", "Name"})
            public int doSomething(){
                return 56;
            }
        });

        System.out.println( _m );

        _method _n = _method.of( _$.Parameterize.toString(_m) );

        System.out.println( _n );
    }
}
