package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _localClassStmt implements _statement<LocalClassDeclarationStmt, _localClassStmt> {

    public static _localClassStmt of(){
        return new _localClassStmt( new LocalClassDeclarationStmt( ));
    }
    public static _localClassStmt of(LocalClassDeclarationStmt lc){
        return new _localClassStmt( lc);
    }
    public static _localClassStmt of(String...code){
        return new _localClassStmt(Stmt.localClassStmt( code));
    }

    public static <A extends Object> _localClassStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _localClassStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _localClassStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _localClassStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _localClassStmt from( LambdaExpr le){
        Optional<LocalClassDeclarationStmt> ows = le.getBody().findFirst(LocalClassDeclarationStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No local class Statement found in lambda");
    }

    private LocalClassDeclarationStmt astStmt;

    public _localClassStmt(LocalClassDeclarationStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _localClassStmt copy() {
        return new _localClassStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.localClassStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _class get_class(){
        return _class.of( this.astStmt.getClassDeclaration());
    }

    public _localClassStmt set_class(_class _c){
        this.astStmt.setClassDeclaration(_c.ast());
        return this;
    }

    @Override
    public boolean is(LocalClassDeclarationStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public LocalClassDeclarationStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        astStmt.getClassDeclaration();

        comps.put( _java.Component.CLASS, get_class());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _localClassStmt ){
            return Objects.equals( ((_localClassStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
