package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public final class _labeledStmt implements _stmt<LabeledStmt, _labeledStmt>,
        _java._node<LabeledStmt, _labeledStmt> {

    public static final Function<String, _labeledStmt> PARSER = s-> _labeledStmt.of(s);

    public static _labeledStmt of(){
        return new _labeledStmt( new LabeledStmt( ));
    }
    public static _labeledStmt of(LabeledStmt ls){
        return new _labeledStmt(ls);
    }
    public static _labeledStmt of(String...code){
        return new _labeledStmt(Stmts.labeledStmt( code));
    }

    public static <A extends Object> _labeledStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _labeledStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _labeledStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _labeledStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
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

    public static _feature._meta<_labeledStmt> META = _feature._meta.of(_labeledStmt.class, LABEL, STATEMENT );

    private LabeledStmt astStmt;

    public _labeledStmt(LabeledStmt rs){
        this.astStmt = rs;
    }


    @Override
    public _labeledStmt copy() {
        return new _labeledStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.labeledStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean isLabel(String label){
        return Objects.equals( this.astStmt.getLabel().asString(), label);
    }

    public boolean isLabel( Predicate<String> matchFn){
        return matchFn.test(this.astStmt.getLabel().asString());
    }

    public String getLabel(){
        return this.astStmt.getLabel().asString();
    }

    public _labeledStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _stmt getStatement(){
        return _stmt.of(astStmt.getStatement());
    }


    public _labeledStmt setStatement(String...st){
        return setStatement( Stmts.of(st) );
    }

    public _labeledStmt setStatement(_stmt _st){
        this.astStmt.setStatement(_st.ast());
        return this;
    }

    public _labeledStmt setStatement(Statement st){
        this.astStmt.setStatement(st);
        return this;
    }

    public boolean isStatement(Predicate<Statement> matchFn ){
        return matchFn.test(this.astStmt.getStatement());
    }

    public boolean isStatement( Statement st){
        return Objects.equals( this.astStmt.getStatement(), st);
    }

    public boolean isStatement( _stmt st){
        return Objects.equals( this.astStmt.getStatement(), st.ast());
    }

    @Override
    public boolean is(LabeledStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public LabeledStmt ast(){
        return astStmt;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.LABEL, astStmt.getLabel().asString());
        comps.put(_java.Feature.STATEMENT, astStmt.getStatement());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _labeledStmt ){
            return Objects.equals( ((_labeledStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
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
    public static void flattenLabel(_java._domain _j, String labelName){
        if( _j instanceof _java._node){
            Tree.flattenLabel( ((_java._node)_j).ast(), labelName);
            return;
        }
        throw new _jdraftException("cannot flatten a label :"+labelName+" from "+ _j.getClass());
    }

}
