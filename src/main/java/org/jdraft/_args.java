package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Argument Lists used in:
 * {@link _methodCall}
 * {@link _constant}
 * {@link _constructorCallStmt}
 * {@link _new}
 * order matters
 *
 */
public class _args
        implements _java._list<Expression, _expression, _args> {

    public static _args of(){
        return of( Ex.methodCallEx("empty()"));
    }

    public static _args of(String...args){
        return of( Ex.methodCallEx("empty("+ Text.combine(args)+")"));
    }

    public static _args of(NodeWithArguments nwa){
         return new _args(nwa);
    }

    public NodeWithArguments nwa;

    public _args(NodeWithArguments nwa){
        this.nwa = nwa;
    }

    public _args add( int i){
        return add( _int.of(i) );
    }

    public _args add( char c){
        return add( _char.of(c) );
    }

    public _args add( boolean b){
        return add( _boolean.of(b) );
    }

    public _args add( long l){
        return add( _long.of(l) );
    }

    public _args add( float f){
        return add( _double.of(f) );
    }

    public _args add( double d){
        return add( _double.of(d) );
    }

    public _args add(String...args){
        return add(Arrays.stream(args).map(a -> _expression.of(a) ).collect(Collectors.toList()).toArray(new _expression[0]));
    }

    public boolean isAt(int index, int i){
        return isAt(index, _int.of(i) );
    }

    public boolean isAt(int index, char c){
        return isAt(index, _char.of(c) );
    }

    public boolean isAt(int index, boolean b){
        return isAt(index,_boolean.of(b) );
    }

    public boolean isAt(int index, long l){
        return isAt(index,_long.of(l) );
    }

    public boolean isAt(int index, float f){
        return isAt(index,_double.of(f) );
    }

    public boolean isAt(int index, double d){
        return isAt(index,_double.of(d) );
    }

    public boolean isAt(int index, String expression){
        return isAt(index, Ex.of(expression));
    }

    public boolean isAt(int index, Expression e){
        return Ex.equivalent( getAt(index).ast(), e);
    }

    public _args setAt(int index, int i){
        return setAt(index, _int.of(i) );
    }

    public _args setAt(int index, char c){
        return setAt(index, _char.of(c) );
    }

    public _args setAt(int index, boolean b){
        return setAt(index,_boolean.of(b) );
    }

    public _args setAt(int index, long l){
        return setAt(index,_long.of(l) );
    }

    public _args setAt(int index, float f){
        return setAt(index,_double.of(f) );
    }

    public _args setAt(int index, double d){
        return setAt(index,_double.of(d) );
    }

    @Override
    public _args copy() {
        Node n = (Node)nwa;
        return new _args( (NodeWithArguments) (n.clone()) );
    }

    @Override
    public List<_expression> list() {
        return listAstElements().stream().map(e-> _expression.of(e) ).collect(Collectors.toList());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.nwa.getArguments();
    }

    public int hashCode(){
        return 31 * this.nwa.getArguments().hashCode();
    }

    public boolean equals( Object o){
        if( o instanceof _args){
            _args _as = (_args) o;
            if( this.nwa.getArguments().size() == _as.size()){
                for(int i=0;i<this.nwa.getArguments().size(); i++){
                    Expression e = this.nwa.getArgument(i);
                    if( !Ex.equivalent(e, _as.nwa.getArgument(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
