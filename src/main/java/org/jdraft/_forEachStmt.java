package org.jdraft;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import java.util.HashMap;
import java.util.Map;

public class _forEachStmt implements _statement<ForEachStmt, _forEachStmt> {

    public static _forEachStmt of(){
        return new _forEachStmt( new ForEachStmt( ));
    }
    public static _forEachStmt of(ForEachStmt fe){
        return new _forEachStmt(fe);
    }
    public static _forEachStmt of(String...code){
        return new _forEachStmt(Stmt.forEachStmt( code));
    }

    private ForEachStmt astStmt;

    public _forEachStmt(ForEachStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _forEachStmt copy() {
        return new _forEachStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.forEachStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getIterable(){
        return _expression.of(this.astStmt.getIterable());
    }

    public _variable getVariable(){
        return new _variable(this.astStmt.getVariable());
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    public _forEachStmt setIterable(_expression e){
        this.astStmt.setIterable(e.ast());
        return this;
    }

    public _forEachStmt setVariable( _variable _v){
        this.astStmt.setVariable(_v.ile);
        return this;
    }

    public _forEachStmt setBody(_statement _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _forEachStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _forEachStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public boolean is(ForEachStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ForEachStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.ITERABLE, astStmt.getIterable());
        comps.put(_java.Component.VARIABLE, astStmt.getVariable());
        comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }
}
