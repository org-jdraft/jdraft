package org.jdraft;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import org.jdraft.text.Text;

import java.util.Objects;

public final class _nullExpr implements _expr._literal<NullLiteralExpr, _nullExpr> {

    public static _nullExpr of(){
        return new _nullExpr(new NullLiteralExpr());
    }

    public static _nullExpr of(NullLiteralExpr nl){
        return new _nullExpr(nl);
    }
    public static _nullExpr of(String...code){
        if( Text.combine(code).equals("null")){
            return new _nullExpr( new NullLiteralExpr());
        }
        throw new _jdraftException("invalid code for null literal "+System.lineSeparator()+ Text.combine(code));
    }

    public static _feature._meta<_nullExpr> META = _feature._meta.of(_nullExpr.class);

    public NullLiteralExpr nle;

    public _nullExpr(NullLiteralExpr nle){
        this.nle = nle;
    }

    @Override
    public _nullExpr copy() {
        return new _nullExpr(this.nle.clone());
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
        return nle;
    }

    public String toString(){
        return this.nle.toString();
    }

    public String getValue(){
        return "null";
    }

    public String valueAsString(){
        return "null";
    }

    public boolean equals(Object other){
        if( other instanceof _nullExpr){
            return Objects.equals( ((_nullExpr)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
