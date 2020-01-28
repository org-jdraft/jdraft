package org.jdraft;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.HashMap;
import java.util.Map;


public class _tryStmt implements _statement<TryStmt, _tryStmt> {

    public static _tryStmt of(){
        return new _tryStmt( new TryStmt( ));
    }
    public static _tryStmt of(TryStmt ts){
        return new _tryStmt(ts);
    }
    public static _tryStmt of(String...code){
        return new _tryStmt(Stmt.tryStmt(code));
    }

    private TryStmt astStmt;

    public _tryStmt(TryStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _tryStmt copy() {
        return new _tryStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.tryStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _tryStmt setTryBlock(_blockStmt tryBlock){

        this.astStmt.setTryBlock( tryBlock.ast());
        return this;
    }

    public _tryStmt setFinallyBlock(_blockStmt finallyBlock){
        this.astStmt.setFinallyBlock(finallyBlock.ast());
        return this;
    }


    /*
    astStmt.getResources();
        astStmt.getFinallyBlock();
        astStmt.getTryBlock();
        astStmt.getCatchClauses();

     */

    public _body getTryBlock(){
        return _body.of( astStmt.getTryBlock());
    }
    public _body getFinallyBlock(){
        if( this.astStmt.getFinallyBlock().isPresent()){
            return _body.of( astStmt.getFinallyBlock() );
        }
        return null;
    }

    public _tryStmt setTryBlock(_body _b){
        this.astStmt.setTryBlock(_b.ast());
        return this;
    }

    public _tryStmt setFinallyBlock(_body _b){
        this.astStmt.setFinallyBlock(_b.ast());
        return this;
    }

    @Override
    public boolean is(TryStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public TryStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        //resources
        //tryblock
        //fnially block
        //catchClauses
        //comps.put(_java.Component.EXPRESSION, astStmt.getExpression());
        //comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }
}
