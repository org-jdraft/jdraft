package org.jdraft;

import com.github.javaparser.ast.expr.NameExpr;
import org.jdraft.text.Text;

import java.util.Objects;

public class _nameExpression
        implements _expression<NameExpr, _nameExpression>, _java._uniPart<NameExpr, _nameExpression> {

    public static _nameExpression of(){
        return new _nameExpression( new NameExpr( ));
    }
    public static _nameExpression of(NameExpr ne){
        return new _nameExpression( ne);
    }
    public static _nameExpression of( String...code){
        return new _nameExpression(Ex.nameEx( code));
    }

    public NameExpr ne;

    public _nameExpression(NameExpr ne){
        this.ne = ne;
    }

    @Override
    public _nameExpression copy() {
        return new _nameExpression(this.ne.clone());
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
        return ne;
    }

    public String getName(){
        return this.ne.getNameAsString();
    }

    public String toString(){
        return this.ne.toString();
    }

    public _nameExpression setName( String name){
        this.ne.setName(name);
        return this;
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
