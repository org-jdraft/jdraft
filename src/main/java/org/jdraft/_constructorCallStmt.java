package org.jdraft;

import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;

import java.util.*;

/**
 * A call to "super" or "this" in a constructor or initializer.
 * <br/><code>class X { X() { super(15); } }</code>
 * <br/><code>class X { X() { this(1, 2); } }</code>
 *
 * @see _superExpr
 * @see _thisExpr
 */
public final class _constructorCallStmt
        implements _stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _java._node<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _typeArgs._withTypeArguments<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _args._withArgs<ExplicitConstructorInvocationStmt, _constructorCallStmt> {

    public static _constructorCallStmt of(){
        return new _constructorCallStmt( new ExplicitConstructorInvocationStmt( ));
    }

    public static _constructorCallStmt of(ExplicitConstructorInvocationStmt ecs){
        return new _constructorCallStmt( ecs);
    }

    public static _constructorCallStmt of(String...code){
        return new _constructorCallStmt(Stmts.constructorCallStmt( code));
    }

    private ExplicitConstructorInvocationStmt astStmt;

    public _constructorCallStmt(ExplicitConstructorInvocationStmt astStmt){
        this.astStmt = astStmt;
    }

    @Override
    public _constructorCallStmt copy() {
        return new _constructorCallStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.constructorCallStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ExplicitConstructorInvocationStmt ast(){
        return astStmt;
    }

    /**
     * is a constructor call with this(...);
     * @return
     */
    public boolean isThis(){
        return this.astStmt.isThis();
    }

    /**
     * is a constructor call with super(...)
     * @return
     */
    public boolean isSuper(){
        return !this.astStmt.isThis();
    }

    public boolean isExpression( _expr _e){
        if( this.astStmt.getExpression().isPresent() ){
            return Objects.equals( this.astStmt.getExpression().get(), _e.ast());
        }
        return _e == null;
    }

    public _expr getExpression(){
        if( this.astStmt.getExpression().isPresent()){
            return _expr.of(this.astStmt.getExpression().get());
        }
        return null;
    }

    public _constructorCallStmt setExpression(_expr _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        if(astStmt.getExpression().isPresent()) {
            comps.put(_java.Feature.EXPRESSION, astStmt.getExpression().get());
        }

        comps.put(_java.Feature.IS_THIS_CALL, astStmt.isThis());
        comps.put(_java.Feature.IS_SUPER_CALL, !astStmt.isThis());
        comps.put(_java.Feature.ARGS_EXPRS, astStmt.getArguments());
        if( astStmt.getTypeArguments().isPresent()){
            comps.put(_java.Feature.TYPE_ARGS, astStmt.getTypeArguments().get());
        }
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _constructorCallStmt){
            return Objects.equals( ((_constructorCallStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
