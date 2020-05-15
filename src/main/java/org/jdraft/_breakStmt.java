package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BreakStmt;

import java.util.Objects;


public final class _breakStmt implements _stmt._controlFlow._signal<BreakStmt, _breakStmt>, _java._uniPart<BreakStmt, _breakStmt> {

    public static _breakStmt of(){
        return new _breakStmt( new BreakStmt( ).removeLabel());
    }
    public static _breakStmt of(BreakStmt bs){
        return new _breakStmt(bs);
    }
    public static _breakStmt of(String...code){
        return new _breakStmt(Stmts.breakStmt( code));
    }

    private BreakStmt astStmt;

    public _breakStmt(BreakStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _breakStmt copy() {
        return new _breakStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.breakStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public String getLabel(){
        if(this.astStmt.getLabel().isPresent()){
            return this.astStmt.getLabel().get().asString();
        }
        return null;
    }

    public boolean hasLabel(){
        return this.astStmt.getLabel().isPresent();
    }

    public boolean isLabel(String label){
        if( this.astStmt.getLabel().isPresent() ) {
            return Objects.equals(this.astStmt.getLabel().get().asString(), label);
        }
        return label == null;
    }

    public _breakStmt setLabel(SimpleName sn){
        this.astStmt.setLabel( sn );
        return this;
    }

    public _breakStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _breakStmt removeLabel(){
        this.astStmt.removeLabel();
        return this;
    }

    @Override
    public boolean is(BreakStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public BreakStmt ast(){
        return astStmt;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _breakStmt ){
            return Objects.equals( ((_breakStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
