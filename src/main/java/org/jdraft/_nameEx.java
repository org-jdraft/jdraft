package org.jdraft;

import com.github.javaparser.ast.expr.NameExpr;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class _nameEx implements _expression<NameExpr, _nameEx> {

    public NameExpr ile;

    public _nameEx(NameExpr ile){
        this.ile = ile;
    }

    @Override
    public _nameEx copy() {
        return new _nameEx(this.ile.clone());
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
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.NAME, this.ile.getNameAsString());
        return comps;
    }


    public String toString(){
        return this.ile.toString();
    }
}
