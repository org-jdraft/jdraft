package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class _newTest extends TestCase {

    public void testConstruct(){
        _new _n = _new.of();
        System.out.println(_n);
        _n = _new.of(String.class);
        _new.of("MyClass");
        System.out.println(_n);
        try{
            _n = _new.of(int.class);
            fail("Expected exception trying to new a primitive class");
        } catch(Exception e){
            //expected
        }
        System.out.println(_n);
        _n.setTypeRef("Thingy");
        System.out.println(_n);
        _n.setArguments(_int.of(1) );
        System.out.println( _n );

        _n.addTypeArguments(Ast.typeRef("A"));
        System.out.println( _n );
        _n.removeTypeArguments(Ast.typeRef("A"));
        System.out.println( _n );

        _n.addArguments(_int.of(3));
        assertEquals(2, _n.countArguments());
        _new.of(Integer.class);

        _n = _new.of("new List()");
        _n.setUseDiamondOperator();
        //System.out.println("DIAMOND" +  _n );
        assertTrue( _n.isUsingDiamondOperator());

        _n = _new.of("new <> List()");
        assertTrue( _n.isUsingDiamondOperator() );
        _n.addTypeArguments(Integer.class);
        //System.out.println( _n );

    }

    public void testAnon(){
        _new _n = _new.of( ()-> new ArrayList() );
        assertTrue(_n.isTypeRef(ArrayList.class));

        _n = _new.of( ()-> new ArrayList<String>() );
        //System.out.println( _n.getType() );
        assertTrue(_n.isTypeRef("ArrayList<String>"));

        _n = _new.of( ()-> new Serializable(){
           public int getY(){
               return 102;
           }
        });

        assertTrue( _n.isTypeRef(Serializable.class) );
        assertEquals(1, _n.listAnonymousDeclarations().size());
        _n.addTypeArguments(Ast.typeRef("T") );
        _n.getTypeArgument(0);
    }

    public void testTypeArgs(){
        ObjectCreationExpr oce = new ObjectCreationExpr();
        oce.setType("A<String>");
        oce.setTypeArguments( Ast.typeRef("TP1"), Ast.typeRef("TP2"));
        oce.setArguments(new NodeList<Expression>());
        oce.addArgument(_int.of(2).ast());
        System.out.println( oce);
    }

    public void testArgs(){
        class A{
            A(int...vals){ }
        }

        _new _n = _new.of( ()-> new A(1,2,3,4,5) );
        assertTrue(_n.hasArguments());
        assertEquals( 5, _n.countArguments() );
        assertTrue(_n.isArgument(0, _int.of(1)));
        assertTrue(_n.isArgument(1, _int.of(2)));
        assertTrue(_n.isArgument(2, _int.of(3)));
        assertTrue(_n.isArgument(3, _int.of(4)));
        assertTrue(_n.isArgument(4, _int.of(5)));

        assertEquals( 5, _n.listArguments().size());
        assertEquals( _int.of(1), _n.getArgument(0));
        assertEquals( _int.of(2), _n.getArgument(1));
        assertEquals( _int.of(3), _n.getArgument(2));
        assertEquals( _int.of(4), _n.getArgument(3));
        assertEquals( _int.of(5), _n.getArgument(4));
        AtomicInteger ai = new AtomicInteger(0);
        _n.forArguments(a -> ai.incrementAndGet());
        assertEquals(5, ai.get());
        ai.set(0);
        _n.forArguments(e-> e instanceof _int && ((_int)e).getValue() < 3, e-> ai.incrementAndGet());
        assertEquals(2, ai.get());

        _n.addArguments(_int.of(6));
        assertEquals(6, _n.countArguments());
        assertEquals( _int.of(6), _n.getArgument(5));
    }

    public void testFull(){
        _new _n = _new.of( ()-> {
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
        System.out.println( _n.listArguments() );
        //assertEquals(2, _n.listTypeArguments().size() );
    }
}
