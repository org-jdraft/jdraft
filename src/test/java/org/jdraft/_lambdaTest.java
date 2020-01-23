package org.jdraft;

import org.jdraft.Ex.Command;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _lambdaTest extends TestCase {

    /**
     * Make sure I can build an empty Lambda from Scratch
     */
    public void testBuildEmptyOne(){
        _lambda _l = _lambda.of()
                .setParameters("a, b")
                .setBody("System.out.println(a.toString() + b.toString());");

        assertEquals("(a, b)", _l.getParameters().toString());
        assertEquals("System.out.println(a.toString() + b.toString());", _l.getBody().toString());
        //System.out.println(_l);

        //BiConsumer bc = (a, b)-> System.out.println(a.toString() + b.toString());
    }

    public void testSetParameters(){
        _lambda _l = _lambda.of("r->{ return 1;}");
        //test setting an Unknown type parameter
        _l.setParameters("key");
        assertEquals("(key)", _l.getParameters().toString());

        _l.setParameters("String key");
        assertEquals("(String key)", _l.getParameters().toString());

        _l.setParameters("String... key");
        assertEquals("(String... key)", _l.getParameters().toString());

        System.out.println(_l);

        _class _c = _class.of("C")
                .field("public int i;");
        _l = _lambda.of();
        _l = _lambda.of("(a)-> System.out.println(1);")
                .setParameters("b");

    }
    public void testOf(){
        Ex.of(()-> System.out.println() );
        Ex.of((Object o)->System.out.println(o) );
        Ex.of(o-> "e" );
        Ex.of((o)-> "e" );
        Ex.of((o)-> {return "e";} );
        
        _lambda.of(()-> System.out.println() );        
        _lambda.of((String o)->System.out.println(o) );
        _lambda.of((Object o)-> System.out.println(o) );
        _lambda.of( o-> "e" );
        _lambda.of((o)-> "e" );
        _lambda.of((o)-> {return "e";} );
        
        
        /*
        Expr.of((Object o)->System.out.println(o) );
        Expr.of( o-> "e" );
        Expr.of((o)-> "e" );
        Expr.of((o)-> {return "e";} );
        */
        
        //Expr.of(()-> {return "e";} );
        
        _lambda _l = _lambda.of( new Object(){
            Predicate p = p-> true;
        });
        
        //_lambda _l = _lambda.of( Boolean.TRUE );
        _l = _lambda.of( new Object() {
            Command c = ()->System.out.println(1); 
        });
        
        _l = _lambda.of(new Object(){
            Consumer c = (o)->System.out.println(o);
        });
    }
}
