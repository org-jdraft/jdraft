package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

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
public final class _arrayDimension implements _tree._node<ArrayCreationLevel, _arrayDimension>,
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
        return new _arrayDimension(new ArrayCreationLevel().setDimension( _e.node() ));
    }

    public static _feature._one<_arrayDimension, _expr> EXPRESSION = new _feature._one<>(_arrayDimension.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_arrayDimension a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_arrayDimension> FEATURES = _feature._features.of(_arrayDimension.class,  PARSER, EXPRESSION );

    public ArrayCreationLevel node;

    public _arrayDimension(ArrayCreationLevel acl){
        this.node = acl;
    }

    public _feature._features<_arrayDimension> features(){
        return FEATURES;
    }

    @Override
    public _arrayDimension copy() {
        return _arrayDimension.of(this.node);
    }

    public _arrayDimension replace(ArrayCreationLevel ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
    }

    public _expr getExpression(){
        if( this.node.getDimension() != null ) {
            if( this.node.getDimension().isPresent() ) {
                return _expr.of(this.node.getDimension().get());
            }
            return null;
        }
        return null;
    }
    public _arrayDimension setExpression(String... dimension){
        this.node.setDimension(Expr.of(dimension));
        return this;
    }

    public _arrayDimension setExpression(Expression dimension){
        this.node.setDimension(dimension);
        return this;
    }

    public _arrayDimension setExpression(_expr dimension){
        this.node.setDimension(dimension.node());
        return this;
    }

    @Override
    public boolean is(ArrayCreationLevel astNode) {
        return Objects.equals(this.node, astNode);
    }

    public <_IE extends _expr> boolean is(Class<_IE> implClass ){
        _expr _e = getExpression();
        if( _e != null ) {
            return implClass.isAssignableFrom(_e.getClass());
        }
        return false;
    }

    public <_IE extends _expr> boolean is(Class<_IE> implClass, Predicate<_IE> _matchFn){

        _expr _e = getExpression();
        if( _e != null ) {
            return implClass.isAssignableFrom(_e.getClass()) && _matchFn.test( (_IE)_e);
        }
        return false;
    }

    @Override
    public ArrayCreationLevel node() {
        return this.node;
    }

    public String toString(){
        return this.node.toString();
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public boolean equals( Object o ){
        if( o instanceof _arrayDimension ){
            _arrayDimension _a = (_arrayDimension)o;
            return Objects.equals( _a.node, this.node);
        }
        return false;
    }
}
