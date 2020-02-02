package org.jdraft;

import com.github.javaparser.ast.expr.SuperExpr;

import java.util.HashMap;
import java.util.Map;

public class _super implements _expression<SuperExpr, _super> {

    public static _super of(){
        return new _super( new SuperExpr());
    }
    public static _super of(SuperExpr se){
        return new _super(se);
    }
    public static _super of( String...code){
        return new _super(Ex.superEx( code));
    }

    public SuperExpr ile;

    public _super(SuperExpr ile){
        this.ile = ile;
    }

    @Override
    public _super copy() {
        return new _super(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.superEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(SuperExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public SuperExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        if(this.ile.getTypeName().isPresent()){
            comps.put(_java.Component.NAME, ile.getTypeName().get().asString());
        }
        return comps;
    }

    public String getName(){
        if(this.ile.getTypeName().isPresent()){
            return ile.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _super){
            return ((_super)other).ile.equals( this.ile );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }

    public String toString(){
        return this.ile.toString();
    }
}
