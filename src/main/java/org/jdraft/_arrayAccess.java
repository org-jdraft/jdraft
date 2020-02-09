package org.jdraft;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Array brackets [] being used to get a value from an array.
 * In <br/><code>getNames()[15*15]</code> the name expression is getNames() and the index expression is 15*15.
 *
 * @see _arrayDimension
 */
public class _arrayAccess
        implements _expression<ArrayAccessExpr, _arrayAccess>,
        _java._compound<ArrayAccessExpr, _arrayAccess> {

    public static _arrayAccess of(){
        return new _arrayAccess(new ArrayAccessExpr());
    }
    public static _arrayAccess of( ArrayAccessExpr ae ){
        return new _arrayAccess(ae);
    }

    public static _arrayAccess of( String code){
        return new _arrayAccess(Ex.arrayAccessEx( code));
    }

    public static _arrayAccess of( String...code){
        return new _arrayAccess(Ex.arrayAccessEx( code));
    }

    //a simple literal name[3]
    public static _arrayAccess of( String name, int index){
        return of( Ex.nameEx(name), index);
    }

    /**
     *
     * @param name
     * @param indexes
     * @return
     */
    public static _arrayAccess of( String name, int... indexes){
        return of( Ex.nameEx(name), indexes);
    }

    /**
     * Here we have to build recursive expressions to handle multi-dimensional arrays
     * where the name could be an _arrayAccess and the index can be
     * @param e
     * @param indexes
     * @return
     */
    public static _arrayAccess of(Expression e, int...indexes){

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
    public ArrayAccessExpr astNode;

    public _arrayAccess(ArrayAccessExpr astNode){
        this.astNode = astNode;
    }

    @Override
    public _arrayAccess copy() {
        return new _arrayAccess(this.astNode.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayAccessEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayAccessExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayAccessExpr ast(){
        return astNode;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INDEX, astNode.getIndex());
        comps.put(_java.Component.ARRAY_NAME, astNode.getName());
        return comps;
    }

    public boolean isName(String name){
        try{
            return Objects.equals( _expression.of(name), this.getName());
        }catch(Exception e){
            return false;
        }
    }

    public boolean isName(Expression e){
        return Objects.equals( _expression.of(e), this.getName());
    }

    public boolean isName(_expression _e){
        return Objects.equals( _e, this.getName());
    }

    public boolean isName(Predicate<_expression> namePredicate){
        return namePredicate.test(this.getName());
    }

    public boolean isIndex(String indexExpression){
        try{
            return Objects.equals( _expression.of(indexExpression), this.getIndex());
        }catch(Exception e){
            return false;
        }
    }

    public boolean isIndex(Predicate<_expression> indexPredicate){
        return indexPredicate.test(this.getIndex());
    }

    public boolean isIndex(int i){
        return Objects.equals( _int.of(i), this.getName());
    }

    public boolean isIndex(Expression e){
        return Objects.equals( _expression.of(e), this.getName());
    }

    public boolean isIndex(_expression _e){
        return Objects.equals( _e, this.getName());
    }


    public _expression getName(){
        return _expression.of(this.astNode.getName());
    }

    public _expression getIndex(){
        return _expression.of(this.astNode.getIndex());
    }

    public boolean equals(Object other){
        if( other instanceof _arrayAccess){
            return ((_arrayAccess)other).astNode.equals( this.astNode);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.astNode.hashCode();
    }

    public String toString(){
        return this.astNode.toString();
    }
}
