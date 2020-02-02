package org.jdraft;

import com.github.javaparser.ast.expr.NameExpr;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _nameExpression implements _expression<NameExpr, _nameExpression> {

    public static _nameExpression of(){
        return new _nameExpression( new NameExpr( ));
    }
    public static _nameExpression of(NameExpr ne){
        return new _nameExpression( ne);
    }
    public static _nameExpression of( String...code){
        return new _nameExpression(Ex.nameEx( code));
    }

    public NameExpr ile;

    public _nameExpression(NameExpr ile){
        this.ile = ile;
    }

    @Override
    public _nameExpression copy() {
        return new _nameExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        return is( new NameExpr(Text.combine(stringRep)));
    }

    @Override
    public boolean is(NameExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public NameExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.NAME, this.ile.getNameAsString());
        return comps;
    }

    public String getName(){
        return this.ile.getNameAsString();
    }

    public String toString(){
        return this.ile.toString();
    }


    public boolean equals(Object other){
        if( other instanceof _nameExpression ){
            return Objects.equals( ((_nameExpression)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
