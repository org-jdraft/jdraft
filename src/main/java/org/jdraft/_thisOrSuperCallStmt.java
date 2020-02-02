package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.Type;

import java.util.*;

//? lambda
public class _thisOrSuperCallStmt implements _statement<ExplicitConstructorInvocationStmt, _thisOrSuperCallStmt> {

    public static _thisOrSuperCallStmt of(){
        return new _thisOrSuperCallStmt( new ExplicitConstructorInvocationStmt( ));
    }

    public static _thisOrSuperCallStmt of(ExplicitConstructorInvocationStmt ecs){
        return new _thisOrSuperCallStmt( ecs);
    }

    public static _thisOrSuperCallStmt of(String...code){
        return new _thisOrSuperCallStmt(Stmt.thisOrSuperCallStmt( code));
    }

    private ExplicitConstructorInvocationStmt astStmt;

    public _thisOrSuperCallStmt(ExplicitConstructorInvocationStmt astStmt){
        this.astStmt = astStmt;
    }

    @Override
    public _thisOrSuperCallStmt copy() {
        return new _thisOrSuperCallStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.thisOrSuperCallStmt(stringRep));
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

    public boolean isUsingDiamondOperator(){
        return this.astStmt.isUsingDiamondOperator();
    }

    public _expression getExpression(){
        if( this.astStmt.getExpression().isPresent()){
            return _expression.of(this.astStmt.getExpression().get());
        }
        return null;
    }

    public _thisOrSuperCallStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }


    public List<_expression> listArguments(){
        List<_expression> args = new ArrayList<>();
        this.astStmt.getArguments().forEach(a -> args.add( _expression.of(a)));
        return args;
    }

    public _thisOrSuperCallStmt setArguments( _expression... _es ){
        NodeList<Expression> ars = new NodeList<>();
        Arrays.stream(_es).forEach(e -> ars.add(e.ast()));
        this.astStmt.setArguments(ars);
        return this;
    }

    public _thisOrSuperCallStmt setArgument( int index, _expression _e ){
        this.astStmt.setArgument(index, _e.ast());
        return this;
    }

    public _thisOrSuperCallStmt setTypeArgument( int index, _typeRef _t ){
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
        if( other instanceof _thisOrSuperCallStmt ){
            return Objects.equals( ((_thisOrSuperCallStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
