package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BreakStmt;
import org.jdraft.text.Stencil;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <PRE>
 * a simple break statement or break statement with a label:
 * for(int i=0;i<100;i++){
 *     if( call(i) == expected){
 *        break outer;
 *     }
 * }
 * outer: System.out.println( "found");
 * </PRE>
 */
public final class _breakStmt
        implements _stmt._goto<BreakStmt, _breakStmt> {

    public static final Function<String, _breakStmt> PARSER = s-> _breakStmt.of(s);

    public static _breakStmt of(){
        return new _breakStmt( new BreakStmt( ).removeLabel());
    }
    public static _breakStmt of(BreakStmt bs){
        return new _breakStmt(bs);
    }
    public static _breakStmt of(String...code){
        return new _breakStmt(Stmt.breakStmt( code));
    }

    public static _feature._one<_breakStmt, String> LABEL = new _feature._one<>(_breakStmt.class, String.class,
            _feature._id.LABEL,
            a -> a.getLabel(),
            (_breakStmt a, String s) -> a.setLabel(s), PARSER);

    public static _feature._features<_breakStmt> FEATURES = _feature._features.of(_breakStmt.class,  PARSER, LABEL);

    public _feature._features<_breakStmt> features(){
        return FEATURES;
    }

    private BreakStmt node;

    public _breakStmt(BreakStmt rs){
        this.node = rs;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _breakStmt replace(BreakStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public _breakStmt copy() {
        return new _breakStmt( this.node.clone());
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
        if( this.node.getLabel().isPresent()){
            return labelMatchFn.test( this.node.getLabel().get().asString() );
        }
        return false;
    }

    public boolean isLabel(String label){
        if( this.node.getLabel().isPresent() ) {
            return Objects.equals(this.node.getLabel().get().asString(), label);
        }
        return label == null;
    }

    public boolean isLabel( Stencil st){
        if( this.node.getLabel().isPresent() ) {
            return st.parse(this.node.getLabel().get().asString()) != null;
        }
        return false;
    }

    public _breakStmt setLabel(SimpleName sn){
        this.node.setLabel( sn );
        return this;
    }

    public _breakStmt setLabel(String label){
        this.node.setLabel( new SimpleName(label));
        return this;
    }

    public _breakStmt removeLabel(){
        this.node.removeLabel();
        return this;
    }

    public BreakStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _breakStmt ){
            return Objects.equals( ((_breakStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
