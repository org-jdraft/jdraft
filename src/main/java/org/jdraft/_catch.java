package org.jdraft;

import com.github.javaparser.ast.stmt.CatchClause;

import java.util.HashMap;
import java.util.Map;

public class _catch implements _node<CatchClause, _catch> {

    public CatchClause cc;

    @Override
    public _catch copy() {
        return new _catch( this.cc.clone() );
    }
    public _catch(CatchClause cc){
        this.cc = cc;
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ast.catchClause(stringRep));
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is(CatchClause astNode) {
        return this.cc.equals( astNode );
    }

    @Override
    public CatchClause ast() {
        return this.cc;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put( _java.Component.BODY, this.cc.getBody());
        comps.put( _java.Component.PARAMETER, this.cc.getParameter());
        return comps;
    }
}
