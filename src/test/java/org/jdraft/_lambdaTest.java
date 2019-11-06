package org.jdraft;

import org.jdraft.Ex.Command;
import java.util.function.Consumer;
import java.util.function.Predicate;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _lambdaTest extends TestCase {
    
    
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
