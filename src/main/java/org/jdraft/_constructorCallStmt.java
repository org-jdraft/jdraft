package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.Type;

import java.util.*;

/**
 * A call to "super" or "this" in a constructor or initializer.
 * <br/><code>class X { X() { super(15); } }</code>
 * <br/><code>class X { X() { this(1, 2); } }</code>
 *
 * @author Julio Vilmar Gesser
 * @see _super
 * @see _this
 */
public class _constructorCallStmt
        implements _statement<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _java._nodeList<Expression, _expression, _constructorCallStmt> {

    public static _constructorCallStmt of(){
        return new _constructorCallStmt( new ExplicitConstructorInvocationStmt( ));
    }

    public static _constructorCallStmt of(ExplicitConstructorInvocationStmt ecs){
        return new _constructorCallStmt( ecs);
    }

    public static _constructorCallStmt of(String...code){
        return new _constructorCallStmt(Stmt.constructorCallStmt( code));
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
    public List<_expression> list() {
        List<_expression> _la = new ArrayList<>();
        listAstElements().forEach(a -> _la.add(_expression.of(a)));
        return _la;
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.astStmt.getArguments();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.constructorCallStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ExplicitConstructorInvocationStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ExplicitConstructorInvocationStmt ast(){
        return astStmt;
    }

    public List<_typeRef> listTypeArguments(){
        List<_typeRef> tas = new ArrayList<>();
        if( this.astStmt.getTypeArguments().isPresent()){
            this.astStmt.getTypeArguments().get().forEach(ts -> tas.add( _typeRef.of(ts)));
        }
        return tas;
    }

    public boolean isThis(){
        return this.astStmt.isThis();
    }

    public boolean isSuper(){
        return !this.astStmt.isThis();
    }

    /**
     * Is using the Diamond operator/ Type Arguments <>
     * @return
     */
    public boolean isUsingDiamondOperator(){
        return this.astStmt.isUsingDiamondOperator();
    }

    public _expression getExpression(){
        if( this.astStmt.getExpression().isPresent()){
            return _expression.of(this.astStmt.getExpression().get());
        }
        return null;
    }

    public _constructorCallStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }


    public List<_expression> listArguments(){
        List<_expression> args = new ArrayList<>();
        this.astStmt.getArguments().forEach(a -> args.add( _expression.of(a)));
        return args;
    }

    public _constructorCallStmt setArguments(_expression... _es ){
        NodeList<Expression> ars = new NodeList<>();
        Arrays.stream(_es).forEach(e -> ars.add(e.ast()));
        this.astStmt.setArguments(ars);
        return this;
    }

    public _constructorCallStmt setArgument(int index, _expression _e ){
        this.astStmt.setArgument(index, _e.ast());
        return this;
    }

    public _constructorCallStmt setTypeArgument(int index, _typeRef _t ){
        if( this.astStmt.getTypeArguments().isPresent()){
            this.astStmt.getTypeArguments().get().set(index, _t.ast());
        } else{
            if( index == 0 ){
                NodeList<Type> nle = new NodeList<>();
                nle.add(_t.ast());
                this.astStmt.setTypeArguments();
            } else{
                throw new _jdraftException("invalid typeArgs index: "+index);
            }
        }
        return this;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if(astStmt.getExpression().isPresent()) {
            comps.put(_java.Component.EXPRESSION, astStmt.getExpression().get());
        }
        comps.put(_java.Component.THIS_CALL, astStmt.isThis());
        comps.put(_java.Component.ARGUMENTS, astStmt.getArguments());
        if( astStmt.getTypeArguments().isPresent()){
            comps.put(_java.Component.TYPE_ARGUMENTS, astStmt.getTypeArguments().get());
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
