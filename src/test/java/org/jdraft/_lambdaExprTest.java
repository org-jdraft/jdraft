package org.jdraft;

import org.jdraft.Exprs.Command;

import java.util.function.Consumer;
import java.util.function.Predicate;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _lambdaExprTest extends TestCase {

    /**
     * Make sure I can build an empty Lambda from Scratch
     */
    public void testBuildEmptyOne(){
        _lambdaExpr _l = _lambdaExpr.of()
                .setParameters("a, b")
                .setBody("System.out.println(a.toString() + b.toString());");

        assertEquals("(a, b)", _l.getParameters().toString());
        assertEquals("System.out.println(a.toString() + b.toString());", _l.getBody().toString());
    }

    /**
     * Make sure I can build an empty Lambda from Scratch
     * (Here with vararg)
     */
    public void testBuildVarArg(){
        _lambdaExpr _l = _lambdaExpr.of()
                .setParameters("String... a")
                .setBody("System.out.println(a.length);");

        assertEquals("(String... a)", _l.getParameters().toString());
        assertEquals("System.out.println(a.length);", _l.getBody().toString());
    }

    public void testLambdaAddStatements(){
        //add code before
        _lambdaExpr _l = _lambdaExpr.of()
                .addStatements(0, "System.out.println(1);");
        System.out.println( _l );
        _l.addStatements("System.out.println(2);");
        System.out.println( _l );
    }

    public void testAddWithStatement(){
        _lambdaExpr _l = _lambdaExpr.of("a-> System.out.println(1);")
                .addStatements(0, "System.out.println(0);");
        //System.out.println( _l );
        _l = _lambdaExpr.of("a-> System.out.println(1);")
                .addStatements(1, "System.out.println(2);");
        //System.out.println( _l );
        assertEquals(
                _lambdaExpr.of("a-> { "+
                        "System.out.println(1); " +
                        "System.out.println(2); }"), _l);

        _l = _lambdaExpr.of("a-> {System.out.println(1); System.out.println(2);}")
                .addStatements(0, "System.out.println(0);");
        //System.out.println( _l );

        _l.addStatements("System.out.println(3);");
        //System.out.println( _l );
        assertEquals(
                _lambdaExpr.of("a-> { System.out.println(0); " +
                        "System.out.println(1); " +
                        "System.out.println(2); " +
                        "System.out.println(3); }"), _l);

    }
    public void testSetParameters(){
        _lambdaExpr _l = _lambdaExpr.of("r->{ return 1;}");
        //test setting an Unknown type parameter
        _l.setParameters("key");
        assertEquals("(key)", _l.getParameters().toString());

        _l.setParameters("String key");
        assertEquals("(String key)", _l.getParameters().toString());

        _l.setParameters("String... key");
        assertEquals("(String... key)", _l.getParameters().toString());

        System.out.println(_l);

        _class _c = _class.of("C")
                .addField("public int i;");
        _l = _lambdaExpr.of();
        _l = _lambdaExpr.of("(a)-> System.out.println(1);")
                .setParameters("b");

    }
    public void testOf(){
        Exprs.of(()-> System.out.println() );
        Exprs.of((Object o)->System.out.println(o) );
        Exprs.of(o-> "e" );
        Exprs.of((o)-> "e" );
        Exprs.of((o)-> {return "e";} );
        
        _lambdaExpr.of(()-> System.out.println() );
        _lambdaExpr.of((String o)->System.out.println(o) );
        _lambdaExpr.of((Object o)-> System.out.println(o) );
        _lambdaExpr.of(o-> "e" );
        _lambdaExpr.of((o)-> "e" );
        _lambdaExpr.of((o)-> {return "e";} );
        
        
        /*
        Expr.of((Object o)->System.out.println(o) );
        Expr.of( o-> "e" );
        Expr.of((o)-> "e" );
        Expr.of((o)-> {return "e";} );
        */
        
        //Expr.of(()-> {return "e";} );
        
        _lambdaExpr _l = _lambdaExpr.of(new Object(){
            Predicate p = p-> true;
        });
        
        //_lambda _l = _lambda.of( Boolean.TRUE );
        _l = _lambdaExpr.of(new Object() {
            Command c = ()->System.out.println(1); 
        });
        
        _l = _lambdaExpr.of(new Object(){
            Consumer c = (o)->System.out.println(o);
        });
    }
}
