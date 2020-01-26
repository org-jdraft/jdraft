package org.jdraft;

import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Map;
//TODO FIX THIS
public class _returnStmt implements _statement<ReturnStmt, _returnStmt> {

    private ReturnStmt rs;

    @Override
    public _returnStmt copy() {
        return null;
    }

    @Override
    public boolean is(String... stringRep) {
        return false;
    }

    @Override
    public boolean is(ReturnStmt astNode) {
        return false;
    }

    public ReturnStmt ast(){
        return rs;
    }

    @Override
    public Map<_java.Component, Object> components() {
        return null;
    }
}
