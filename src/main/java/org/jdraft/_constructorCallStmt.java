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
 * @see _super
 * @see _this
 */
public final class _constructorCallStmt
        implements _statement<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _java._multiPart<ExplicitConstructorInvocationStmt, _constructorCallStmt>,
        _java._list<Expression, _expression, _constructorCallStmt> {

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

    public boolean hasTypeArguments(){
        return this.astStmt.getTypeArguments().isPresent();
    }

    public boolean isTypeArguments(_typeRef..._typeArguments){
        List<_typeRef> tas = listTypeArguments();
        if( _typeArguments.length != tas.size()){
            return false;
        }
        for(int i=0;i<tas.size();i++){
            if( !tas.get(i).equals( _typeArguments[i])){
                return false;
            }
        }
        return true;
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

    /**
     * Is using the Diamond operator / Type Arguments <>
     * @return
     */
    public boolean isUsingDiamondOperator(){
        return this.astStmt.isUsingDiamondOperator();
    }

    public boolean isExpression( _expression _e){
        if( this.astStmt.getExpression().isPresent() ){
            return Objects.equals( this.astStmt.getExpression().get(), _e.ast());
        }
        return _e == null;
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

    public boolean isArguments( _expression..._exs){
        List<_expression> tes = list();
        if( _exs.length == tes.size()){
            for(int i=0;i<tes.size();i++){
                if( !tes.get(i).equals( _exs[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
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

    public boolean hasArguments(){
        return this.astStmt.getArguments().size() > 0;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if(astStmt.getExpression().isPresent()) {
            comps.put(_java.Component.EXPRESSION, astStmt.getExpression().get());
        }

        comps.put(_java.Component.THIS_CALL, astStmt.isThis());
        comps.put(_java.Component.SUPER_CALL, !astStmt.isThis());
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
