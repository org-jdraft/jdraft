package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;

import java.util.*;
import java.util.function.Function;

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
        _typeArgs._withTypeArgs<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _args._withArgs<ExplicitConstructorInvocationStmt, _constructorCallStmt> {

    public static final Function<String, _constructorCallStmt> PARSER = s-> _constructorCallStmt.of(s);

    public static _constructorCallStmt of(){
        return new _constructorCallStmt( new ExplicitConstructorInvocationStmt( ));
    }

    public static _constructorCallStmt of(ExplicitConstructorInvocationStmt ecs){
        return new _constructorCallStmt( ecs);
    }

    public static _constructorCallStmt of(String...code){
        return new _constructorCallStmt(Stmt.constructorCallStmt( code));
    }

    public static _feature._one<_constructorCallStmt, Boolean> IS_THIS = new _feature._one<>(_constructorCallStmt.class, Boolean.class,
            _feature._id.IS_THIS,
            a -> a.isThis(),
            (_constructorCallStmt a, Boolean b) -> a.setThis(b), PARSER);

    public static _feature._one<_constructorCallStmt, Boolean> IS_SUPER = new _feature._one<>(_constructorCallStmt.class, Boolean.class,
            _feature._id.IS_SUPER,
            a -> a.isSuper(),
            (_constructorCallStmt a, Boolean b) -> a.setSuper(b), PARSER);

    public static _feature._one<_constructorCallStmt, _expr> EXPRESSION = new _feature._one<>(_constructorCallStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_constructorCallStmt a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._one<_constructorCallStmt, _args> ARGS = new _feature._one<>(_constructorCallStmt.class, _args.class,
            _feature._id.ARGS,
            a -> a.getArgs(),
            (_constructorCallStmt a, _args _e) -> a.setArgs(_e), PARSER);

    public static _feature._one<_constructorCallStmt, _typeArgs> TYPE_ARGS = new _feature._one<>(_constructorCallStmt.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_constructorCallStmt a, _typeArgs _e) -> a.setTypeArgs(_e), PARSER);

    public static _feature._features<_constructorCallStmt> FEATURES = _feature._features.of(
            _constructorCallStmt.class, PARSER, TYPE_ARGS, IS_THIS, IS_SUPER, EXPRESSION, ARGS);

    private ExplicitConstructorInvocationStmt node;

    public _constructorCallStmt(ExplicitConstructorInvocationStmt node){
        this.node = node;
    }

    public _feature._features<_constructorCallStmt> features(){
        return FEATURES;
    }

    @Override
    public _constructorCallStmt copy() {
        return new _constructorCallStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _constructorCallStmt replace(ExplicitConstructorInvocationStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ExplicitConstructorInvocationStmt node(){
        return node;
    }

    /**
     * is a constructor call with this(...);
     * @return
     */
    public boolean isThis(){
        return this.node.isThis();
    }

    /**
     * is a constructor call with super(...)
     * @return
     */
    public boolean isSuper(){
        return !this.node.isThis();
    }

    public _constructorCallStmt setThis(Boolean b){
        this.node.setThis(b);
        return this;
    }

    public _constructorCallStmt setSuper(Boolean b){
        this.node.setThis( ! b );
        return this;
    }

    public boolean hasExpression(){
        return this.node().getExpression().isPresent();
    }

    public _expr getExpression(){
        if( this.node().getExpression().isPresent()){
            this.node.getExpression().get();
        }
        return null;
    }

    public boolean isExpression( _expr _e){
        return Objects.equals( this.getExpression(), _e);
    }

    public boolean isExpression( Expression e){
        if( this.node().getExpression().isPresent()){
            return Expr.equal( this.node().getExpression().get(), e);
        }
        return e == null;
    }

    public _constructorCallStmt setExpression(String...expression){
        return setExpression( _expr.of(expression));
    }

    public _constructorCallStmt setExpression(Expression e){
        if( e != null ) {
            this.node.setExpression(e);
        } else {
            this.node.removeExpression();
        }
        return this;
    }

    public _constructorCallStmt setExpression(_expr _e){
        if( _e != null ) {
            this.node.setExpression(_e.node());
        } else {
            this.node.removeExpression();
        }
        return this;
    }

    public _constructorCallStmt removeExpression(){
        this.node.removeExpression();
        return this;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _constructorCallStmt){
            _constructorCallStmt _o = (_constructorCallStmt)other;
            if(!Objects.equals(getArgs(), _o.getArgs())){
                return false;
            }
            if( !Objects.equals(getExpression(), _o.getExpression())){
                return false;
            }
            if( !Objects.equals(getTypeArgs(), _o.getTypeArgs())){
                return false;
            }
            return true;
            //return Objects.equals( ((_constructorCallStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
