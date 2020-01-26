package org.jdraft;

import com.github.javaparser.ast.expr.CastExpr;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * <PRE><CODE>
 * (String)getCharSeq();
 * (List<String>)
 * </CODE></PRE>
 *
 */
public class _cast implements _expression<CastExpr, _cast> {

    public static _cast of(){
        return new _cast( new CastExpr());
    }

    public static _cast of( String...code){
        return new _cast(Ex.castEx( code));
    }

    public CastExpr ile;

    public _cast(CastExpr ile){
        this.ile = ile;
    }

    @Override
    public _cast copy() {
        return new _cast(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.castEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(CastExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public CastExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, ile.getType());
        comps.put(_java.Component.EXPRESSION, ile.getExpression());
        return comps;
    }

    public _typeRef getType(){
        return _typeRef.of(this.ile.getType());
    }
    public _expression getExpression(){
        return _expression.of(this.ile.getExpression());
    }

    public boolean equals(Object other){
        if( other instanceof _cast){
            return ((_cast)other).ile.equals( this.ile );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }
    
    public String toString(){
        return this.ile.toString();
    }
}
