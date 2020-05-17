package org.jdraft;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * a reference to specific array dimension
 * Where Array brackets [] being used to get a value from an array.
 * In <br/><code>getNames()[15*15]</code> the name expression is getNames() and the index expression is 15*15.
 *
 * @see _arrayDimension
 */
public final class _arrayAccessExpr
        implements _expr<ArrayAccessExpr, _arrayAccessExpr>,
        _java._multiPart<ArrayAccessExpr, _arrayAccessExpr> {

    public static _arrayAccessExpr of(){
        return new _arrayAccessExpr(new ArrayAccessExpr());
    }

    public static _arrayAccessExpr of(ArrayAccessExpr ae ){
        return new _arrayAccessExpr(ae);
    }

    public static _arrayAccessExpr of(String code){
        return new _arrayAccessExpr(Exprs.arrayAccessExpr( code));
    }

    public static _arrayAccessExpr of(String...code){
        return new _arrayAccessExpr(Exprs.arrayAccessExpr( code));
    }

    //a simple literal name[3]
    public static _arrayAccessExpr of(String name, int index){
        return of( Exprs.nameExpr(name), index);
    }

    /**
     *
     * @param name
     * @param indexes
     * @return
     */
    public static _arrayAccessExpr of(String name, int... indexes){
        return of( Exprs.nameExpr(name), indexes);
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

    public ArrayAccessExpr aae;

    public _arrayAccessExpr(ArrayAccessExpr aae){
        this.aae = aae;
    }

    @Override
    public _arrayAccessExpr copy() {
        return new _arrayAccessExpr(this.aae.clone());
    }

    public ArrayAccessExpr ast(){
        return aae;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INDEX, aae.getIndex());
        comps.put(_java.Component.ARRAY_NAME, aae.getName());
        return comps;
    }

    public boolean isNamed(String name){
        try{
            return Objects.equals( _expr.of(name), this.getName());
        }catch(Exception e){
            return false;
        }
    }

    public boolean isNamed(Expression e){
        return Objects.equals( _expr.of(e), this.getName());
    }

    public boolean isNamed(_expr _e){
        return Objects.equals( _e, this.getName());
    }

    public boolean isNamed(Predicate<_expr> namePredicate){
        return namePredicate.test(this.getName());
    }

    public boolean isIndex(String indexExpression){
        try{
            return Objects.equals( _expr.of(indexExpression), this.getIndex());
        }catch(Exception e){
            return false;
        }
    }

    public boolean isIndex(Predicate<_expr> indexPredicate){
        return indexPredicate.test(this.getIndex());
    }

    public boolean isIndex(int i){
        return Objects.equals( _intExpr.of(i), this.getName());
    }

    public boolean isIndex(Expression e){
        return Objects.equals( _expr.of(e), this.getName());
    }

    public boolean isIndex(_expr _e){
        return Objects.equals( _e, this.getName());
    }

    public _arrayAccessExpr setIndex(String index){
        return setIndex( Exprs.of(index));
    }

    public _arrayAccessExpr setIndex(Expression e){
        this.aae.setIndex(e);
        return this;
    }

    public _arrayAccessExpr setIndex(_expr _e){
        this.aae.setIndex(_e.ast());
        return this;
    }

    public _arrayAccessExpr setName(String name){
        return setName( Exprs.of(name));
    }

    public _arrayAccessExpr setName(Expression e){
        this.aae.setName(e);
        return this;
    }

    public _arrayAccessExpr setName(_expr _e){
        this.aae.setName(_e.ast());
        return this;
    }

    public Expression getNameNode() { return this.aae.getName(); }

    public _expr getName(){
        return _expr.of(this.aae.getName());
    }

    public _expr getIndex(){
        return _expr.of(this.aae.getIndex());
    }

    public boolean equals(Object other){
        if( other instanceof _arrayAccessExpr){
            return ((_arrayAccessExpr)other).aae.equals( this.aae);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.aae.hashCode();
    }

    public String toString(){
        return this.aae.toString();
    }
}
