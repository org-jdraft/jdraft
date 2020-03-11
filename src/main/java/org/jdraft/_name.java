package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import org.jdraft.text.Text;

public class _name implements _java._uniPart<Node, _name> {

    public static _name of( Node n){
        return new _name(n);
    }

    public static _name of( Name name){
        return new _name(name);
    }
    public static _name of( SimpleName simpleName){
        return new _name(simpleName);
    }

    public static _name of(String...name){
        String str = Text.combine(name);
        if( !str.contains(".")){
            //MUST be a name
            return of( new SimpleName(str) );
        } else{
            return of( new Name(str) );
        }
        //return new _name( new SimpleName( Text.combine(name)) );
    }

    public _name(String name){
        this.name = new SimpleName(name);
    }

    public _name(Node sn){
        this.name = sn;
    }

    /** the underlying name */
    public Node name;

    @Override
    public _name copy() {
        return _name.of( name.clone());
    }

    @Override
    public Node ast() {
        return name;
    }

    @Override
    public boolean is(String... stringRep) {
        return of( Text.combine(stringRep) ).equals(this);
    }

    public String toString(){
        return this.name.toString();
    }

    public int hashCode(){
        return 31 * this.name.hashCode();
    }

    public boolean equals( Object o){
        if( o instanceof _name ){
            return this.name.toString().equals( o.toString());
        }
        return false;
    }
}
