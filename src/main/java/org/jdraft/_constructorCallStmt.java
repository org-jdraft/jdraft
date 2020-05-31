package org.jdraft;

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
        _java._node<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
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

    public static _feature._one<_constructorCallStmt, _args> ARGS = new _feature._one<>(_constructorCallStmt.class, _args.class,
            _feature._id.ARGS,
            a -> a.getArgs(),
            (_constructorCallStmt a, _args _e) -> a.setArgs(_e), PARSER);

    public static _feature._one<_constructorCallStmt, _typeArgs> TYPE_ARGS = new _feature._one<>(_constructorCallStmt.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_constructorCallStmt a, _typeArgs _e) -> a.setTypeArgs(_e), PARSER);


    public static _feature._meta<_constructorCallStmt> META = _feature._meta.of(_constructorCallStmt.class, TYPE_ARGS, IS_THIS, IS_SUPER, ARGS);

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
            return is( Stmt.constructorCallStmt(stringRep));
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

    public _constructorCallStmt setThis(Boolean b){
        this.astStmt.setThis(b);
        return this;
    }

    public _constructorCallStmt setSuper(Boolean b){
        this.astStmt.setThis( ! b );
        return this;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

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
