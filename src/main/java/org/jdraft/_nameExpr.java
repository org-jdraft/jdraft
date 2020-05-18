package org.jdraft;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import org.jdraft.text.Text;

import java.util.Objects;

public final class _nameExpr
        implements _expr<NameExpr, _nameExpr>,
        _java._node<NameExpr, _nameExpr>,
        _java._withName<_nameExpr> {

    public static _nameExpr of(){
        return new _nameExpr( new NameExpr( ));
    }
    public static _nameExpr of(NameExpr ne){
        return new _nameExpr( ne);
    }
    public static _nameExpr of(String...code){
        return new _nameExpr(Exprs.nameExpr( code));
    }

    public NameExpr ne;

    public _nameExpr(NameExpr ne){
        this.ne = ne;
    }

    @Override
    public _nameExpr copy() {
        return new _nameExpr(this.ne.clone());
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

    public SimpleName getNameNode(){ return this.ne.getName(); }

    public String getName(){
        return this.ne.getNameAsString();
    }

    public String toString(){
        return this.ne.toString();
    }

    public _nameExpr setName(String name){
        this.ne.setName(name);
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _nameExpr){
            return Objects.equals( ((_nameExpr)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
