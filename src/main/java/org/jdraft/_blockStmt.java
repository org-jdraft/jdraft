package org.jdraft;

import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class _blockStmt implements _statement<BlockStmt, _blockStmt> {

    public static _blockStmt of(){
        return new _blockStmt( new BlockStmt( ));
    }
    public static _blockStmt of(BlockStmt bs){
        return new _blockStmt(bs);
    }
    public static _blockStmt of(String...code){
        return new _blockStmt(Stmt.blockStmt( code));
    }

    private BlockStmt astStmt;

    public _blockStmt(BlockStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _blockStmt copy() {
        return new _blockStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.blockStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public List<_statement> listStatements(){
        List<_statement> _sts  = new ArrayList<>();
        this.astStmt.getStatements().forEach(s -> _sts.add( _statement.of(s)));
        return _sts;
    }

    @Override
    public boolean is(BlockStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public BlockStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.STATEMENTS, astStmt.getStatements() );
        return comps;
    }
}
