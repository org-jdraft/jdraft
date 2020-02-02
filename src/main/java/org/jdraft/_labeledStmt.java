package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.LabeledStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class _labeledStmt implements _statement<LabeledStmt, _labeledStmt> {

    public static _labeledStmt of(){
        return new _labeledStmt( new LabeledStmt( ));
    }
    public static _labeledStmt of(LabeledStmt ls){
        return new _labeledStmt(ls);
    }
    public static _labeledStmt of(String...code){
        return new _labeledStmt(Stmt.labeledStmt( code));
    }

    public static <A extends Object> _labeledStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _labeledStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _labeledStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _labeledStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _labeledStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _labeledStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _labeledStmt from( LambdaExpr le){
        Optional<LabeledStmt> ows = le.getBody().findFirst(LabeledStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }


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
            return is( Stmt.labeledStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public String getLabel(){
        return this.astStmt.getLabel().asString();
    }

    public _labeledStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _statement getStatement(){
        return _statement.of(astStmt.getStatement());
    }

    public _labeledStmt setStatement(_statement _st){
        this.astStmt.setStatement(_st.ast());
        return this;
    }

    @Override
    public boolean is(LabeledStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public LabeledStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LABEL, astStmt.getLabel().asString());
        comps.put(_java.Component.STATEMENT, astStmt.getStatement());
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
}
