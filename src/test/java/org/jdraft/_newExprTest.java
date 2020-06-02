package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class _newExprTest extends TestCase {

    public void testConstruct(){
        _newExpr _n = _newExpr.of();
        System.out.println(_n);
        _n = _newExpr.of(String.class);
        _newExpr.of("MyClass");
        System.out.println(_n);
        try{
            _n = _newExpr.of(int.class);
            fail("Expected exception trying to new a primitive class");
        } catch(Exception e){
            //expected
        }
        System.out.println(_n);
        _n.setTypeRef("Thingy");
        System.out.println(_n);
        _n.setArgs(_intExpr.of(1) );
        System.out.println( _n );

        _n.addTypeArgs(Types.of("A"));
        System.out.println( _n );
        _n.removeTypeArgs(Types.of("A"));
        System.out.println( _n );

        _n.addArgs(_intExpr.of(3));
        assertEquals(2, _n.countArgs());
        _newExpr.of(Integer.class);

        _n = _newExpr.of("new List()");

        _n.setUseDiamondOperator();
        //System.out.println("DIAMOND" +  _n );
        assertTrue( _n.isUsingDiamondOperator());

        _n = _newExpr.of("new <> List()");
        assertTrue( _n.isUsingDiamondOperator() );
        _n.addTypeArgs(Integer.class);
        //System.out.println( _n );

    }

    public void testAnon(){
        _newExpr _n = _newExpr.of( ()-> new ArrayList() );
        assertTrue(_n.isTypeRef(ArrayList.class));

        _n = _newExpr.of( ()-> new ArrayList<String>() );
        //System.out.println( _n.getType() );
        assertTrue(_n.isTypeRef("ArrayList<String>"));

        _n = _newExpr.of( ()-> new Serializable(){
           public int getY(){
               return 102;
           }
        });

        assertTrue( _n.isTypeRef(Serializable.class) );
        assertEquals(1, _n.listAnonymousBodyMembers().size());
        _n.addTypeArgs(Types.of("T") );
        _n.getTypeArg(0);
    }

    public void testTypeArgs(){
        ObjectCreationExpr oce = new ObjectCreationExpr();
        oce.setType("A<String>");
        oce.setTypeArguments( Types.of("TP1"), Types.of("TP2"));
        oce.setArguments(new NodeList<Expression>());
        oce.addArgument(_intExpr.of(2).ast());
        System.out.println( oce);
    }

    public void testArgs(){
        class A{
            A(int...vals){ }
        }

        _newExpr _n = _newExpr.of( ()-> new A(1,2,3,4,5) );
        assertTrue(_n.hasArgs());
        assertEquals( 5, _n.countArgs() );
        assertTrue(_n.isArg(0, _intExpr.of(1)));
        assertTrue(_n.isArg(1, _intExpr.of(2)));
        assertTrue(_n.isArg(2, _intExpr.of(3)));
        assertTrue(_n.isArg(3, _intExpr.of(4)));
        assertTrue(_n.isArg(4, _intExpr.of(5)));

        assertEquals( 5, _n.listArgs().size());
        assertEquals( _intExpr.of(1), _n.getArg(0));
        assertEquals( _intExpr.of(2), _n.getArg(1));
        assertEquals( _intExpr.of(3), _n.getArg(2));
        assertEquals( _intExpr.of(4), _n.getArg(3));
        assertEquals( _intExpr.of(5), _n.getArg(4));
        AtomicInteger ai = new AtomicInteger(0);
        _n.forArgs(a -> ai.incrementAndGet());
        assertEquals(5, ai.get());
        ai.set(0);
        _n.forArgs(e-> e instanceof _intExpr && ((_intExpr)e).getValue() < 3, e-> ai.incrementAndGet());
        assertEquals(2, ai.get());

        _n.addArgs(_intExpr.of(6));
        assertEquals(6, _n.countArgs());
        assertEquals( _intExpr.of(6), _n.getArg(5));
    }

    public void testFull(){
        _newExpr _n = _newExpr.of( ()-> {
            new HashMap.Entry<String, Long>() {
                public String getKey() {return null;}

                @Override
                public Long getValue() {
                    return null;
                }

                @Override
                public Long setValue(Long value) {
                    return null;
                }
            };
        });
        System.out.println( _n.getScope() );
        System.out.println( _n.getTypeRef() );
        System.out.println( _n.listArgs() );
        //assertEquals(2, _n.listTypeArguments().size() );
    }
}
