package org.jdraft;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _localClassStmt implements _stmt<LocalClassDeclarationStmt, _localClassStmt>,
        _java._node<LocalClassDeclarationStmt, _localClassStmt> {

    public static final Function<String, _localClassStmt> PARSER = s-> _localClassStmt.of(s);

    public static _localClassStmt of(){
        return new _localClassStmt( new LocalClassDeclarationStmt( ));
    }
    public static _localClassStmt of(LocalClassDeclarationStmt lc){
        return new _localClassStmt( lc);
    }
    public static _localClassStmt of(String...code){
        return new _localClassStmt(Stmts.localClassStmt( code));
    }

    public static <A extends Object> _localClassStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _localClassStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _localClassStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _localClassStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _localClassStmt from( LambdaExpr le){
        Optional<LocalClassDeclarationStmt> ows = le.getBody().findFirst(LocalClassDeclarationStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No local class Statement found in lambda");
    }

    public static _feature._one<_localClassStmt, _class> CLASS = new _feature._one<>(_localClassStmt.class, _class.class,
            _feature._id.CLASS,
            a -> a.get_class(),
            (_localClassStmt a, _class _c) -> a.set_class(_c), PARSER);

    public static _feature._meta<_localClassStmt> META = _feature._meta.of(_localClassStmt.class, CLASS );

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
            return is( Stmts.localClassStmt(stringRep));
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

    public _localClassStmt set_class(ClassOrInterfaceDeclaration cd){
        this.astStmt.setClassDeclaration(cd);
        return this;
    }

    //HMM do I want to equals on the _class??
    @Override
    public boolean is(LocalClassDeclarationStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public LocalClassDeclarationStmt ast(){
        return astStmt;
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
