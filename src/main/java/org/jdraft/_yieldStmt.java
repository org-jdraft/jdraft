package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.YieldStmt;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _yieldStmt implements
        _stmt._returns<YieldStmt, _yieldStmt>,
        _java._withExpression<YieldStmt, _yieldStmt>{

    public static final Function<String, _yieldStmt> PARSER = s-> _yieldStmt.of(s);

    public static _yieldStmt of(){
        return new _yieldStmt( new YieldStmt( ));
    }
    public static _yieldStmt of(YieldStmt ys){
        return new _yieldStmt(ys);
    }

    public static _yieldStmt of(String...code){
        String str = "switch(e){ case 1: "+Text.combine(code)+" }";
        _switchExpr _se = _switchExpr.of(str);
        YieldStmt ys = (YieldStmt)_se.getSwitchEntry(0).getStatement(0);
        ys.remove(); //decouple from the string
        return of( ys);
    }

    public static _yieldStmt of(Expression e){
        return new _yieldStmt( new YieldStmt(e));
    }

    public static _feature._one<_yieldStmt, _expr> EXPRESSION = new _feature._one<>(_yieldStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_yieldStmt a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_yieldStmt> FEATURES = _feature._features.of(_yieldStmt.class, PARSER, EXPRESSION);

    private YieldStmt node;

    public _yieldStmt(YieldStmt node){
        this.node = node;
    }

    public _feature._features<_yieldStmt> features(){
        return FEATURES;
    }

    @Override
    public _yieldStmt copy() {
        return new _yieldStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _yieldStmt replace(YieldStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public YieldStmt node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _yieldStmt ){
            return Objects.equals( ((_yieldStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
