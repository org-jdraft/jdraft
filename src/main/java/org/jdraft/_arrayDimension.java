package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

/**
 * An expression representing a single Array dimension
 * i.e. for the following (3) dimensional array
 * int[10][arrs.length][getY()] vals;
 * we have (3) distinct _arrayDimension instances:
 * <UL>
 *  <LI>[10] an intLiteral expression denoted dimension
 *  <LI>[arrs.length] a fieldAccessExpression denoted dimension
 *  <LI>[getY()] a methodCallExpr denoted dimension
 *</UL>
 * @see _newArrayExpr where we use these _arrayDimensions to declare arrays
 */
public final class _arrayDimension implements _java._node<ArrayCreationLevel, _arrayDimension>,
        _java._withExpression<ArrayCreationLevel, _arrayDimension> {

    public static final Function<String, _arrayDimension> PARSER = s-> _arrayDimension.of(s);

    public static _arrayDimension of(ArrayCreationLevel acl){
        return new _arrayDimension(acl);
    }

    public static _arrayDimension of(int index){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( Expr.of(index) ));
    }

    public static _arrayDimension of(){
        return of( new ArrayCreationLevel());
    }

    public static _arrayDimension of(String str){
        return of(new String[]{str});
    }

    public static _arrayDimension of(String...str){
        String s = Text.combine(str);
        if( s.startsWith("[") ){
            s = s.substring(1, s.length() - 1);
        }
        return of( Expr.of(s));
    }

    public static _arrayDimension of(Expression e){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( e ));
    }

    public static _arrayDimension of(_expr _e){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( _e.ast() ));
    }

    public static _feature._one<_arrayDimension, _expr> EXPRESSION = new _feature._one<>(_arrayDimension.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_arrayDimension a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_arrayDimension> FEATURES = _feature._features.of(_arrayDimension.class, EXPRESSION );

    public final ArrayCreationLevel astNode;

    public _arrayDimension(ArrayCreationLevel acl){
        this.astNode = acl;
    }

    public _feature._features<_arrayDimension> features(){
        return FEATURES;
    }

    @Override
    public _arrayDimension copy() {
        return _arrayDimension.of(this.astNode);
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.arrayCreationLevel(stringRep));
        } catch(Exception e){
            return false;
        }
    }
     */

    public _expr getExpression(){
        if( this.astNode.getDimension() != null ) {
            return _expr.of(this.astNode.getDimension().get());
        }
        return null;
    }
    public _arrayDimension setExpression(String... dimension){
        this.astNode.setDimension(Expr.of(dimension));
        return this;
    }

    public _arrayDimension setExpression(Expression dimension){
        this.astNode.setDimension(dimension);
        return this;
    }

    public _arrayDimension setExpression(_expr dimension){
        this.astNode.setDimension(dimension.ast());
        return this;
    }

    @Override
    public boolean is(ArrayCreationLevel astNode) {
        return Objects.equals(this.astNode, astNode);
    }

    @Override
    public ArrayCreationLevel ast() {
        return this.astNode;
    }

    public String toString(){
        return this.astNode.toString();
    }

    public int hashCode(){
        return 31 * this.astNode.hashCode();
    }

    public boolean equals( Object o ){
        if( o instanceof _arrayDimension ){
            _arrayDimension _a = (_arrayDimension)o;
            return Objects.equals( _a.astNode, this.astNode);
        }
        return false;
    }
}
