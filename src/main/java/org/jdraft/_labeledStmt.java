package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.walk.Walk;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 *
 * <PRE>
 * label: System.out.println(1);
 * label:{
 *     System.out.println("labeled block");
 *     call(doIt());
 * }
 * </PRE>
 */
public final class _labeledStmt implements _stmt<LabeledStmt, _labeledStmt>,
        _tree._node<LabeledStmt, _labeledStmt> {

    public static final Function<String, _labeledStmt> PARSER = s-> _labeledStmt.of(s);

    public static _labeledStmt of(){
        return new _labeledStmt( new LabeledStmt( ));
    }
    public static _labeledStmt of(LabeledStmt ls){
        return new _labeledStmt(ls);
    }
    public static _labeledStmt of(String...code){
        return new _labeledStmt(Stmt.labeledStmt( code));
    }

    public static <A extends Object> _labeledStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _labeledStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _labeledStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _labeledStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _labeledStmt from( LambdaExpr le){
        Optional<LabeledStmt> ows = le.getBody().findFirst(LabeledStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }

    public static _feature._one<_labeledStmt, String> LABEL = new _feature._one<>(_labeledStmt.class, String.class,
            _feature._id.LABEL,
            a -> a.getLabel(),
            (_labeledStmt a, String s) -> a.setLabel(s), PARSER);

    public static _feature._one<_labeledStmt, _stmt> STATEMENT = new _feature._one<>(_labeledStmt.class, _stmt.class,
            _feature._id.STATEMENT,
            a -> a.getStatement(),
            (_labeledStmt a, _stmt _s) -> a.setStatement(_s), PARSER);

    public static _feature._features<_labeledStmt> FEATURES = _feature._features.of(_labeledStmt.class, PARSER,  LABEL, STATEMENT );

    private LabeledStmt node;

    public _labeledStmt(LabeledStmt rs){
        this.node = rs;
    }

    public _feature._features<_labeledStmt> features(){
        return FEATURES;
    }

    @Override
    public _labeledStmt copy() {
        return new _labeledStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _labeledStmt replace(LabeledStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean isLabel(String label){
        return Objects.equals( this.node.getLabel().asString(), label);
    }

    public boolean isLabel( Predicate<String> matchFn){
        return matchFn.test(this.node.getLabel().asString());
    }

    public String getLabel(){
        return this.node.getLabel().asString();
    }

    public _labeledStmt setLabel(SimpleName label){
        this.node.setLabel( label );
        return this;
    }

    public _labeledStmt setLabel(String label){
        this.node.setLabel( new SimpleName(label));
        return this;
    }

    public _stmt getStatement(){
        return _stmt.of(node.getStatement());
    }

    public _labeledStmt setStatement(String...st){
        return setStatement( Stmt.of(st) );
    }

    public _labeledStmt setStatement(_stmt _st){
        this.node.setStatement(_st.node());
        return this;
    }

    public _labeledStmt setStatement(Statement st){
        this.node.setStatement(st);
        return this;
    }

    public boolean isStatement(Predicate<Statement> matchFn ){
        return matchFn.test(this.node.getStatement());
    }

    public boolean isStatement( Statement st){
        return Objects.equals( this.node.getStatement(), st);
    }

    public boolean isStatement( _stmt st){
        return Objects.equals( this.node.getStatement(), st.node());
    }

    public LabeledStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _labeledStmt ){
            _labeledStmt _o = (_labeledStmt)other;
            if( ! Objects.equals( getLabel(),_o.getLabel() )){
                //System.out.println("LABELS NOT EQ");
                return false;
            }
            if( ! Objects.equals( getStatement(),_o.getStatement() )){
                //System.out.println("STATEMENTS NOT EQ "+getStatement().getClass()+" "+ _o.getStatement().getClass());
                return false;
            }
            return true;
            //return Objects.equals( ((_labeledStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }

    /**
     * Find all labeled statements that match the label "labelName"
     * and flatten the label (inlining the code within the label)
     * for example: <PRE>{@code
     * class c{
     *     void m(){
     *         label: System.out.println(1);
     *     }
     * }
     * _class _c = _class.of(c.class)
     * _java.flattenLabel( _c, "label" );
     * }</PRE>
     * //will make c:
     * <PRE>{@code
     * class c{
     *     void m(){
     *         System.out.println(1);
     *     }
     * }
     * }</PRE>
     *
     * <PRE>{@code
     * class c{
     *     void m(){
     *         label: {
     *             System.out.println(1);
     *             System.out.println(2);
     *             }
     *     }
     * }
     * _class _c = _class.of(c.class)
     * _java.flattenLabel( _c, "label" );
     * }</PRE>
     * //will make c:
     * <PRE>{@code
     * class c{
     *     void m(){
     *         System.out.println(1);
     *         System.out.println(2);
     *     }
     * }
     * }</PRE>
     * @param _j
     * @param labelName
     */
    public static void deLabel(_java._domain _j, String labelName){
        if( _j instanceof _tree._node){
            Walk.deLabel( ((_tree._node)_j).node(), labelName);
            return;
        }
        throw new _jdraftException("cannot flatten a label :"+labelName+" from "+ _j.getClass());
    }

}
