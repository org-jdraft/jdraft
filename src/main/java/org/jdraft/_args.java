package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import org.jdraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

    /**
     * Argument Lists used in:
     * {@link _methodCall}
     * {@link _constant}
     * {@link _constructorCallStmt}
     * {@link _new}
     * (order matters list of {@link _expression} )
     *
     */
    public interface _withArguments<N extends Node, _WA extends _java._node> extends _java._node<N, _WA> {

        /**
         * Creates and returns an {@link _args} to model the whole arguments list
         * @return an _args modelling 0...n arguments in the arguments list)
         */
        default _args getArgs(){
            return of( (NodeWithArguments)ast());
        }

        default _expression getArgument(int index){
            return _expression.of( ((NodeWithArguments)ast()).getArgument(index) );
        }

        default _WA removeArgument(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_WA)this;
        }

        default _WA setArgument(int index, _expression _e){
            ((NodeWithArguments)ast()).getArguments().set(index, _e.ast());
            return (_WA)this;
        }

        default _WA setArgument(int index, Expression e){
            ((NodeWithArguments)ast()).getArguments().set(index, e);
            return (_WA)this;
        }

        default _WA setArgument(int index, boolean b){
            return setArgument(index, Ex.of(b));
        }

        default _WA setArgument(int index, int i){
            return setArgument(index, Ex.of(i));
        }

        default _WA setArgument(int index, char c){
            return setArgument(index, Ex.of(c));
        }

        default _WA setArgument(int index, float f){
            return setArgument(index, Ex.of(f));
        }

        default _WA setArgument(int index, long l){
            return setArgument(index, Ex.of(l));
        }

        default _WA setArgument(int index, double d){
            return setArgument(index, Ex.of(d));
        }

        default _WA setArguments(_args _as){
            ((NodeWithArguments)ast()).setArguments(_as.listAstElements());
            return (_WA)this;
        }

        default _WA setArguments(_expression... _es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(_es).forEach(n -> nle.add(n.ast()));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_WA)this;
        }

        default _WA setArguments(Expression... es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(es).forEach(n -> nle.add(n));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_WA)this;
        }

        default boolean hasArguments(){
            return ((NodeWithArguments)ast()).getArguments().size() > 0 ;
        }

        default int countArguments(){
            return ((NodeWithArguments)ast()).getArguments().size();
        }

        default int countArguments(Predicate<_expression> matchFn){
            return listArguments(matchFn).size();
        }

        default List<_expression> listArguments(){
            List<_expression> args = new ArrayList<>();
            ((NodeWithArguments)ast()).getArguments().forEach(a -> args.add(_expression.of( (Expression)a)));
            return args;
        }

        default List<_expression> listArguments(Predicate<_expression> matchFn){
            return listArguments().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean isArguments(String... es){
            _expression[] _es = new _expression[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expression.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(Expression... es){
            _expression[] _es = new _expression[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expression.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(_expression... _es){
            List<_expression> _tes = listArguments();
            if(_es.length == _tes.size()){
                for(int i=0;i<_es.length;i++){
                    if( ! Ex.equivalent(  _es[i].ast(), _tes.get(i).ast() ) ){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isArguments(Predicate<List<_expression>> matchFn){
            return matchFn.test( listArguments() );
        }

        default boolean isArgument(int index, boolean b){
            return isArgument(index, Ex.of(b));
        }

        default boolean isArgument(int index, int i){
            return isArgument(index, Ex.of(i));
        }

        default boolean isArgument(int index, char c){
            return isArgument(index, Ex.of(c));
        }

        default boolean isArgument(int index, float f){
            return isArgument(index, Ex.of(f));
        }

        default boolean isArgument(int index, long l){
            return isArgument(index, Ex.of(l));
        }

        default boolean isArgument(int index, double d){
            return isArgument(index, Ex.of(d));
        }

        default boolean isArgument(int index, String exprString){
            try {
                return Ex.equivalent( getArgument(index).ast(), Ex.of(exprString));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArgument(int index, Expression e){
            try {
                return Ex.equivalent( getArgument(index).ast(), e);
            }catch(Exception ex){
                return false;
            }
        }

        default boolean isArgument(int index, _expression _e){
            try {
                return Ex.equivalent( getArgument(index).ast(), _e.ast());
            }catch(Exception e){
                return false;
            }
        }


        default _WA addArgument(int i){
            return addArgument( Ex.of(i) );
        }

        default _WA addArgument(boolean b){
            return addArgument( Ex.of(b) );
        }

        default _WA addArgument(float f){
            return addArgument( Ex.of(f) );
        }

        default _WA addArgument(long l){
            return addArgument( Ex.of(l) );
        }

        default _WA addArgument(double d){
            return addArgument( Ex.of(d) );
        }

        default _WA addArgument(char c){
            return addArgument( Ex.of(c) );
        }

        default _WA addArgument(Expression e){
            return addArguments( e );
        }

        default _WA addArguments(String... es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_WA)this;
        }

        default _WA addArguments(Expression... es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_WA)this;
        }

        default _WA addArguments(_expression... _es){
            Arrays.stream(_es).forEach(_e -> ((NodeWithArguments)ast()).addArgument(_e.ast()));
            return (_WA)this;
        }

        default _WA removeArguments(){
            ((NodeWithArguments)ast()).getArguments().removeIf( t->true);
            return (_WA)this;
        }

        default _WA removeArguments(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_WA)this;
        }
        default _WA removeArguments(Predicate<_expression> matchFn){
            ((NodeWithArguments)ast()).getArguments().removeIf(matchFn);
            return (_WA)this;
        }

        default _WA removeArguments(_expression... es){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i].ast());
            }
            return (_WA)this;
        }

        default _WA removeArguments(Expression... es){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i]);
            }
            return (_WA)this;
        }

        default _WA forArguments(Consumer<_expression> argFn){
            ((NodeWithArguments)ast()).getArguments().stream().map( a-> _expression.of( (Expression)a))
                    .forEach(e->  argFn.accept( (_expression)e) );
            return (_WA)this;
        }

        default _WA forArguments(Predicate<_expression> expressionMatchFn, Consumer<_expression> argFn){
            ((NodeWithArguments)ast()).getArguments().stream()
                    .map( a-> _expression.of( (Expression)a))
                    .filter(expressionMatchFn).forEach(e->  argFn.accept( (_expression)e) );
            return (_WA)this;
        }
    }
}
