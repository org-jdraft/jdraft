package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ContinueStmt;
import org.jdraft.text.Stencil;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static _feature._features<_continueStmt> FEATURES = _feature._features.of(_continueStmt.class,  PARSER, LABEL);

    private ContinueStmt node;

    public _feature._features<_continueStmt> features(){
        return FEATURES;
    }

    public _continueStmt(ContinueStmt rs){
        this.node = rs;
    }

    @Override
    public _continueStmt copy() {
        return new _continueStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _continueStmt replace(ContinueStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public String getLabel(){
        if(this.node.getLabel().isPresent()){
            return this.node.getLabel().get().asString();
        }
        return null;
    }

    public boolean hasLabel(){
        return this.node.getLabel().isPresent();
    }

    public boolean isLabel(Predicate<String> labelMatchFn){
        if( hasLabel()){
            return labelMatchFn.test( getLabel() );
        }
        return false;
    }

    public boolean isLabel(SimpleName label){
        if( this.node.getLabel().isPresent() ) {
            return Objects.equals(this.node.getLabel().get(), label);
        }
        return label == null;
    }

    public boolean isLabel(String label){
        if( this.node.getLabel().isPresent() ) {
            return Objects.equals(this.node.getLabelAsString().get(), label);
        }
        return label == null;
    }

    public boolean isLabel(Stencil labelStencil){
        if( this.node.getLabel().isPresent() ) {
            return labelStencil.parse(this.node.getLabelAsString().get()) != null;
        }
        return false;
    }

    public _continueStmt setLabel(SimpleName label){
        this.node.setLabel( label);
        return this;
    }

    public _continueStmt setLabel(String label){
        this.node.setLabel( new SimpleName(label));
        return this;
    }

    public _continueStmt removeLabel(){
        this.node.removeLabel();
        return this;
    }

    public ContinueStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _continueStmt ){
            return Objects.equals( ((_continueStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
