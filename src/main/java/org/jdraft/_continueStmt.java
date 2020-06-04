package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ContinueStmt;

import java.util.Objects;
import java.util.function.Function;

public final class _continueStmt implements _stmt._goto<ContinueStmt, _continueStmt> {

    public static final Function<String, _continueStmt> PARSER = s-> _continueStmt.of(s);

    public static _continueStmt of(){
        return new _continueStmt( new ContinueStmt( ));
    }
    public static _continueStmt of(ContinueStmt cs){
        return new _continueStmt( cs);
    }
    public static _continueStmt of(String...code){
        return new _continueStmt(Stmt.continueStmt( code));
    }

    public static _continueStmt to(String label){
        return new _continueStmt(new ContinueStmt(label));
    }

    public static _feature._one<_continueStmt, String> LABEL = new _feature._one<>(_continueStmt.class, String.class,
            _feature._id.LABEL,
            a -> a.getLabel(),
            (_continueStmt a, String s) -> a.setLabel(s), PARSER);

    public static _feature._features<_continueStmt> FEATURES = _feature._features.of(_continueStmt.class, LABEL);

    private ContinueStmt astStmt;

    public _continueStmt(ContinueStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _continueStmt copy() {
        return new _continueStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.continueStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    /*
    @Override
    public boolean is(ContinueStmt astNode) {
        return this.toString(Print.PRINT_NO_COMMENTS).equals(astNode.toString(Print.PRINT_NO_COMMENTS));
    }
     */

    public String getLabel(){
        if(this.astStmt.getLabel().isPresent()){
            return this.astStmt.getLabel().get().asString();
        }
        return null;
    }

    public boolean hasLabel(){
        return this.astStmt.getLabel().isPresent();
    }

    public boolean isLabel(SimpleName label){
        if( this.astStmt.getLabel().isPresent() ) {
            return Objects.equals(this.astStmt.getLabel().get(), label);
        }
        return label == null;
    }

    public boolean isLabel(String label){
        if( this.astStmt.getLabel().isPresent() ) {
            return Objects.equals(this.astStmt.getLabelAsString().get(), label);
        }
        return label == null;
    }

    public _continueStmt setLabel(SimpleName label){
        this.astStmt.setLabel( label);
        return this;
    }

    public _continueStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _continueStmt removeLabel(){
        this.astStmt.removeLabel();
        return this;
    }

    public ContinueStmt ast(){
        return astStmt;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _continueStmt ){
            return Objects.equals( ((_continueStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
