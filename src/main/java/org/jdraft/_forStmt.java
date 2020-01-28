package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;

import java.util.*;

public class _forStmt implements _statement<ForStmt, _forStmt> {

    public static _forStmt of(){
        return new _forStmt( new ForStmt( ));
    }

    public static _forStmt of(String...code){
        return new _forStmt(Stmt.forStmt( code));
    }

    private ForStmt astStmt;

    public _forStmt(ForStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _forStmt copy() {
        return new _forStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.forStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }
    //forInitializations
    //forUpdates
    //addInitialization
    //addUpdate

    //addStatements
    //addStatements Lambda


    public List<_expression> listInitializations(){
        List<_expression>init = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> init.add(_expression.of(i)));
        return init;
    }

    public List<_expression> listUpdates(){
        List<_expression>update = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> update.add(_expression.of(i)));
        return update;
    }

    public _expression getCompare(){
        if( this.astStmt.getCompare().isPresent()) {
            return _expression.of(this.astStmt.getCompare().get());
        }
        return null;
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    public _forStmt setCompare(_expression e){
        this.astStmt.setCompare(e.ast());
        return this;
    }

    public _forStmt removeCompare(){
        this.astStmt.removeCompare();
        return this;
    }

    public _forStmt setInitialization(_expression... es){
        NodeList<Expression> init = new NodeList<>();
        Arrays.stream(es).forEach(e-> init.add(e.ast()));
        this.astStmt.setInitialization(init);
        return this;
    }

    public _forStmt setUpdate(_expression... es){
        NodeList<Expression> upd = new NodeList<>();
        Arrays.stream(es).forEach(e-> upd.add(e.ast()));
        this.astStmt.setInitialization(upd);
        return this;
    }

    public _forStmt setBody(_statement _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _forStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _forStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public boolean is(ForStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ForStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INITIALIZATION, astStmt.getInitialization());
        comps.put(_java.Component.UPDATE, astStmt.getUpdate());
        comps.put(_java.Component.COMPARE, astStmt.getCompare());
        comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }
}
