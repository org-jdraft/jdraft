package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class _synchronizedStmt implements _statement<SynchronizedStmt, _synchronizedStmt> {

    public static _synchronizedStmt of(){
        return new _synchronizedStmt( new SynchronizedStmt( ));
    }
    public static _synchronizedStmt of(SynchronizedStmt ss){
        return new _synchronizedStmt( ss);
    }
    public static _synchronizedStmt of(String...code){
        return new _synchronizedStmt(Stmt.synchronizedStmt(code));
    }

    private SynchronizedStmt astStmt;

    public _synchronizedStmt(SynchronizedStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _synchronizedStmt copy() {
        return new _synchronizedStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.synchronizedStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getExpression(){
        return _expression.of(this.astStmt.getExpression());
    }

    public _synchronizedStmt setExpression(_expression _e){
        this.astStmt.setExpression( _e.ast());
        return this;
    }

    public _body getBody(){
        return _body.of( (NodeWithBlockStmt)astStmt);
    }

    public _synchronizedStmt setBody(_body _b){
        this.astStmt.setBody(_b.ast());
        return this;
    }

    @Override
    public boolean is(SynchronizedStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public SynchronizedStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.EXPRESSION, astStmt.getExpression());
        comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _synchronizedStmt ){
            return Objects.equals( ((_synchronizedStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
