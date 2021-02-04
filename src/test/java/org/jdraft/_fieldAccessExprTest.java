package org.jdraft;

import junit.framework.TestCase;

import java.io.Serializable;

public class _fieldAccessExprTest extends TestCase {
    static class A<B extends Serializable>{
        public B ser;
    }

    public static final int I = 100;
    public static final Serializable S = "String";
    public static final A<String> astr = new A<String>();

    public void testThis(){
        _fieldAccessExpr _fae = _fieldAccessExpr.of("this.BEE");
        System.out.println( _fae.getScope() );
        assertEquals( _thisExpr.of(), _fae.getScope());
        System.out.println( _fae.getScope().getClass() );

        _fae = _fieldAccessExpr.of("World.this.BEE");
        System.out.println( _fae.getScope() );
        assertEquals( _thisExpr.of("World.this"), _fae.getScope());
        assertEquals( _expr.of("World.this"), _fae.getScope());
        System.out.println( _fae.getScope().getClass() );

        _fae = (_fieldAccessExpr) _expr.of("World.this.BEE");
        assertEquals( _thisExpr.of("World.this"), _fae.getScope());
        assertEquals( _expr.of("World.this"), _fae.getScope());


        _fae = (_fieldAccessExpr) _expr.of("a.b.c");

    }
    public void testF(){
        _fieldAccessExpr _fae = _fieldAccessExpr.of("a.BEE");
        _fae.setTypeArgs(_typeArgs.of("<A>"));

         _fae.node.getTypeArguments();

        System.out.println( _fae );
        System.out.println( _fae.getTypeArg(0) );
    }
}
