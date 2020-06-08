package org.jdraft;

import com.github.javaparser.ast.stmt.EmptyStmt;

import java.util.Objects;
import java.util.function.Function;

/**
 * models the absence of a statement (specifically a placeholder for statements in loops)
 *
 * i.e. ";" (i.e. "for(int i=0;;)")
 */
public final class _emptyStmt implements _stmt<EmptyStmt, _emptyStmt> {

    public static final Function<String, _emptyStmt> PARSER = s-> _emptyStmt.of();

    public static _emptyStmt of(){
        return new _emptyStmt( new EmptyStmt( ));
    }
    public static _emptyStmt of(EmptyStmt es){
        return new _emptyStmt( es);
    }
    public static _emptyStmt of(String...code){
        return new _emptyStmt(Stmt.emptyStmt( code));
    }

    public static _feature._features<_emptyStmt> FEATURES = _feature._features.of(_emptyStmt.class, PARSER);

    private EmptyStmt astStmt;

    public _feature._features<_emptyStmt> features(){
        return FEATURES;
    }

    public _emptyStmt(EmptyStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _emptyStmt copy() {
        return new _emptyStmt( this.astStmt.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.emptyStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    @Override
    public boolean is(EmptyStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public EmptyStmt ast(){
        return astStmt;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _emptyStmt ){
            return Objects.equals( ((_emptyStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
