package org.jdraft;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _null implements _expression<NullLiteralExpr, _null> {

    public static _null of(){
        return new _null(new NullLiteralExpr());
    }
    public static _null of(NullLiteralExpr nl){
        return new _null(nl);
    }
    public static _null of( String...code){
        if( Text.combine(code).equals("null")){
            return new _null( new NullLiteralExpr());
        }
        throw new _jdraftException("invalid code for null literal "+System.lineSeparator()+ Text.combine(code));
    }

    public NullLiteralExpr ile;

    public _null(NullLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _null copy() {
        return new _null(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        return Text.combine(stringRep).equals("null");
    }

    @Override
    public boolean is(NullLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public NullLiteralExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        return comps;
    }

    public String toString(){
        return this.ile.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _null ){
            return Objects.equals( ((_null)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
