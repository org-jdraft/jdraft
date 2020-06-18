package org.jdraft;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _localClassStmt implements _stmt<LocalClassDeclarationStmt, _localClassStmt> {

    public static final Function<String, _localClassStmt> PARSER = s-> _localClassStmt.of(s);

    public static _localClassStmt of(){
        return new _localClassStmt( new LocalClassDeclarationStmt( ));
    }

    public static _localClassStmt of(LocalClassDeclarationStmt lc){
        return new _localClassStmt( lc);
    }

    public static _localClassStmt of(String...code){
        return new _localClassStmt(Stmt.localClassDeclarationStmt( code));
    }

    public static <A extends Object> _localClassStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _localClassStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _localClassStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _localClassStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _localClassStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _localClassStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
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

    public static _feature._features<_localClassStmt> FEATURES = _feature._features.of(_localClassStmt.class,  PARSER, CLASS );

    private LocalClassDeclarationStmt node;

    public _localClassStmt(LocalClassDeclarationStmt rs){
        this.node = rs;
    }

    public _feature._features<_localClassStmt> features(){
        return FEATURES;
    }

    @Override
    public _localClassStmt copy() {
        return new _localClassStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _localClassStmt replace(LocalClassDeclarationStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _class get_class(){
        return _class.of( this.node.getClassDeclaration());
    }

    public _localClassStmt set_class(_class _c){
        this.node.setClassDeclaration(_c.node());
        return this;
    }

    public _localClassStmt set_class(ClassOrInterfaceDeclaration cd){
        this.node.setClassDeclaration(cd);
        return this;
    }

    public LocalClassDeclarationStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _localClassStmt ){
            return Objects.equals( ((_localClassStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
