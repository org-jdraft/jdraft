package org.jdraft;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.jdraft.text.Stencil;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * a reference to specific array dimension
 * Where Array brackets [] being used to get a value from an array.
 * In <br/><code>getNames()[15*15]</code> the expression is "getNames()" and the index is "15*15".
 *
 * @see _arrayDimension
 */
public final class _arrayAccessExpr
        implements _expr<ArrayAccessExpr, _arrayAccessExpr>,
        _tree._node<ArrayAccessExpr, _arrayAccessExpr> {

    public static final Function<String, _arrayAccessExpr> PARSER = s-> _arrayAccessExpr.of(s);

    public static _arrayAccessExpr of(){
        return new _arrayAccessExpr(new ArrayAccessExpr());
    }

    public static _arrayAccessExpr of(ArrayAccessExpr ae ){
        return new _arrayAccessExpr(ae);
    }

    public static _arrayAccessExpr of(String code){
        return new _arrayAccessExpr(Expr.arrayAccessExpr( code));
    }

    public static _arrayAccessExpr of(String...code){
        return new _arrayAccessExpr(Expr.arrayAccessExpr( code));
    }

    //a simple literal name[3]
    public static _arrayAccessExpr of(String name, int index){
        return of( Expr.nameExpr(name), index);
    }

    /**
     *
     * @param name
     * @param indexes
     * @return
     */
    public static _arrayAccessExpr of(String name, int... indexes){
        return of( Expr.nameExpr(name), indexes);
    }

    /**
     * Here we have to build recursive expressions to handle multi-dimensional arrays
     * where the name could be an _arrayAccess and the index can be
     * @param e
     * @param indexes
     * @return
     */
    public static _arrayAccessExpr of(Expression e, int...indexes){

        if( indexes.length < 1) {
            throw new _jdraftException("must provide at least (1) index");
        }
        ArrayAccessExpr ae = new ArrayAccessExpr();
        ae.setName(e);
        ae.setIndex(new IntegerLiteralExpr(indexes[0]));
        if( indexes.length == 1) {
            return of(ae);
        }
        int[] left = new int[indexes.length-1];
        System.arraycopy(indexes, 1, left, 0, left.length);
        return of(ae, left);
    }

    public static _feature._one<_arrayAccessExpr, _expr> EXPRESSION = new _feature._one<>(_arrayAccessExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_arrayAccessExpr a, _expr _e) -> a.setName(_e), PARSER);

    public static _feature._one<_arrayAccessExpr, _expr> INDEX = new _feature._one<>(_arrayAccessExpr.class, _expr.class,
            _feature._id.INDEX,
            a -> a.getIndex(),
            (_arrayAccessExpr a, _expr _e) -> a.setIndex(_e), PARSER);

    public static _feature._features<_arrayAccessExpr> FEATURES = _feature._features.of(_arrayAccessExpr.class,  PARSER, EXPRESSION, INDEX);

    public ArrayAccessExpr node;

    public _arrayAccessExpr(ArrayAccessExpr node){
        this.node = node;
    }

    @Override
    public _arrayAccessExpr copy() {
        return new _arrayAccessExpr(this.node.clone());
    }

    public ArrayAccessExpr node(){
        return node;
    }

    public _arrayAccessExpr replace(ArrayAccessExpr ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
    }

    public _feature._features<_arrayAccessExpr> features(){
        return FEATURES;
    }

    public boolean isExpression(String expression){
        Stencil st = Stencil.of(expression);
        if( st.isFixedText() ) {
            try {
                return Objects.equals(_expr.of(expression), this.getExpression());
            } catch (Exception e) {
                return false;
            }
        } else{
            st = Stencil.of(_expr.of(expression).toString(Print.PRINT_NO_COMMENTS));
            return st.matches( getExpression().toString(Print.PRINT_NO_COMMENTS));
        }
    }

    public <_IE extends _expr> boolean isExpression(Class<_IE> implClass){
        return implClass.isAssignableFrom( getExpression().getClass() );
    }

    public <_IE extends _expr> boolean isExpression(Class<_IE>implClass, Predicate<_IE>_matchFn ){
        _expr _ee = getExpression();
        if( implClass.isAssignableFrom(_ee.getClass())){
            return _matchFn.test( (_IE)_ee);
        }
        return false;
    }

    public boolean isExpression(Predicate<_expr> exprMatchFn){
        return exprMatchFn.test(this.getExpression());
    }

    public boolean isExpression(Expression e){
        return Objects.equals( _expr.of(e), this.getExpression());
    }

    public boolean isExpression(_expr _e){
        return Objects.equals( _e, this.getExpression());
    }

    public boolean isIndex(String indexExpression){
        if( indexExpression.startsWith("[") && indexExpression.endsWith("]")){
            indexExpression = indexExpression.substring(1, indexExpression.length() -1);
        }
        if( indexExpression.trim().length() == 0 ){
            return this.getIndex().toString().equals("[]");
        }
        Stencil st = Stencil.of(indexExpression);
        if( st.isFixedText() ){
            try{
                return Objects.equals( _expr.of(indexExpression), this.getIndex());
            }catch(Exception e){
                return false;
            }
        }
        return st.matches( getIndex().toString(Print.PRINT_NO_COMMENTS));
    }

    public boolean isIndex(Predicate<_expr> indexPredicate){
        return indexPredicate.test(this.getIndex());
    }

    public <_IE extends _expr> boolean isIndex(Class<_IE> implClass){
        return implClass.isAssignableFrom(getIndex().getClass());
    }

    public <_IE extends _expr> boolean isIndex(Class<_IE>implClass, Predicate<_IE> indexPredicate){
        return implClass.isAssignableFrom(getIndex().getClass()) && indexPredicate.test( (_IE)this.getIndex());
    }

    public boolean isIndex(int i){
        return Objects.equals( _intExpr.of(i), this.getExpression());
    }

    public boolean isIndex(Expression e){
        return Objects.equals( _expr.of(e), this.getExpression());
    }

    public boolean isIndex(_expr _e){
        return Objects.equals( _e, this.getExpression());
    }

    public _arrayAccessExpr setIndex(String index){
        return setIndex( Expr.of(index));
    }

    public _arrayAccessExpr setIndex(Expression e){
        this.node.setIndex(e);
        return this;
    }

    public _arrayAccessExpr setIndex(_expr _e){
        this.node.setIndex(_e.node());
        return this;
    }

    public _arrayAccessExpr setName(String name){
        return setName( Expr.of(name));
    }

    public _arrayAccessExpr setName(Expression e){
        this.node.setName(e);
        return this;
    }

    public _arrayAccessExpr setName(_expr _e){
        this.node.setName(_e.node());
        return this;
    }

    public Expression getExpressionNode() { return this.node.getName(); }

    public _expr getExpression(){
        return _expr.of(this.node.getName());
    }

    public _expr getIndex(){
        return _expr.of(this.node.getIndex());
    }

    public boolean equals(Object other){
        if( other instanceof _arrayAccessExpr){
            return ((_arrayAccessExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}
