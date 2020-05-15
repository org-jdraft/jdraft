package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Argument Lists used in:
 * {@link _methodCallExpr}
 * {@link _constant}
 * {@link _constructorCallStmt}
 * {@link _newExpr}
 * order matters
 *
 * @see org.jdraft.bot.$arguments for a prototype version
 */
public final class _arguments
        implements _java._list<Expression, _expr, _arguments> {

    public static _arguments of(){
        return of( Exprs.methodCallEx("empty()"));
    }

    public static _arguments of(Expression... exs){
        return of().add(exs);
    }

    public static _arguments of(_expr... _exs){
        return of().add(_exs);
    }

    public static <A extends Object> _arguments of( Supplier<A> supplier ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arguments of( Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arguments of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arguments of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arguments from( LambdaExpr le){
        Optional<ArrayCreationExpr> ows = le.getBody().findFirst(ArrayCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get().getInitializer().get().getValues().toArray(new Expression[0]));
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    public static _arguments of(String...args){
        if( args.length == 1){ //they might have already added the ()'s
            String code = args[0].trim();
            if(code.startsWith("(") && code.endsWith(")")){
                code = code.substring(1, code.length() - 1).trim();
                if( code.length() ==0 ){
                    return of();
                }
                return of( Exprs.methodCallEx("empty("+ code+")"));
            }
        }
        StringBuilder sb = new StringBuilder();
        if( !args[0].startsWith("(")) {
            sb.append("(");
        }
        for(int i=0;i<args.length;i++){
            if( i > 0){
                sb.append(", ");
            }
            sb.append(args[i]);
        }
        sb.append(")");
        return of( Exprs.methodCallEx("empty"+ sb.toString()));
    }



    public static _arguments of(NodeWithArguments nwa){
         return new _arguments(nwa);
    }

    public NodeWithArguments nwa;

    public _arguments(NodeWithArguments nwa){
        this.nwa = nwa;
    }

    public _arguments add(int i){
        return add( _intExpr.of(i) );
    }

    public _arguments add(char c){
        return add( _charExpr.of(c) );
    }

    public _arguments add(boolean b){
        return add( _booleanExpr.of(b) );
    }

    public _arguments add(long l){
        return add( _longExpr.of(l) );
    }

    public _arguments add(float f){
        return add( _doubleExpr.of(f) );
    }

    public _arguments add(double d){
        return add( _doubleExpr.of(d) );
    }

    public _arguments add(String...args){
        return add(Arrays.stream(args).map(a -> _expr.of(a) ).collect(Collectors.toList()).toArray(new _expr[0]));
    }

    public boolean isAt(int index, int i){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _intExpr.of(i) );
    }

    public boolean isAt(int index, char c){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _charExpr.of(c) );
    }

    public boolean isAt(int index, boolean b){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _booleanExpr.of(b) );
    }

    public boolean isAt(int index, long l){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _longExpr.of(l) );
    }

    public boolean isAt(int index, float f){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _doubleExpr.of(f) );
    }

    public boolean isAt(int index, double d){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, _doubleExpr.of(d) );
    }

    public boolean isAt(int index, String expression){
        if( index >= this.size()){
            return false;
        }
        return isAt(index, Exprs.of(expression));
    }

    public boolean isAt(int index, Expression e){
        if( index >= this.size()){
            return false;
        }
        return Exprs.equal( getAt(index).ast(), e);
    }

    public _arguments setAt(int index, int i){
        return setAt(index, _intExpr.of(i) );
    }

    public _arguments setAt(int index, char c){
        return setAt(index, _charExpr.of(c) );
    }

    public _arguments setAt(int index, boolean b){
        return setAt(index, _booleanExpr.of(b) );
    }

    public _arguments setAt(int index, long l){
        return setAt(index, _longExpr.of(l) );
    }

    public _arguments setAt(int index, float f){
        return setAt(index, _doubleExpr.of(f) );
    }

    public _arguments setAt(int index, double d){
        return setAt(index, _doubleExpr.of(d) );
    }

    @Override
    public _arguments copy() {
        Node n = (Node)nwa;
        return new _arguments( (NodeWithArguments) (n.clone()) );
    }

    @Override
    public List<_expr> list() {
        return listAstElements().stream().map(e-> _expr.of(e) ).collect(Collectors.toList());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.nwa.getArguments();
    }

    public int hashCode(){
        return 31 * this.nwa.getArguments().hashCode();
    }

    public boolean equals( Object o){
        if( o instanceof _arguments){
            _arguments _as = (_arguments) o;
            if( this.nwa.getArguments().size() == _as.size()){
                for(int i=0;i<this.nwa.getArguments().size(); i++){
                    Expression e = this.nwa.getArgument(i);
                    if( !Exprs.equal(e, _as.nwa.getArgument(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public String toString(){
        //return this.nwa.getArguments().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i=0;i<this.nwa.getArguments().size();i++){

            if( i > 0){
                sb.append(", ");
            }
            sb.append(nwa.getArgument(i));
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Argument Lists used in:
     * {@link _methodCallExpr}
     * {@link _constant}
     * {@link _constructorCallStmt}
     * {@link _newExpr}
     * (order matters list of {@link _expr} )
     *
     */
    public interface _withArguments<N extends Node, _WA extends _java._node> extends _java._node<N, _WA> {

        /**
         * Creates and returns an {@link _arguments} to model the whole arguments list
         * @return an _args modelling 0...n arguments in the arguments list)
         */
        default _arguments getArguments(){
            return of( (NodeWithArguments)ast());
        }

        /**
         * Check if all individual arg ({@link _expr}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allArguments( Predicate<_expr> matchFn){
            return listArguments().stream().allMatch(matchFn);
        }

        default _expr getArgument(int index){
            return _expr.of( ((NodeWithArguments)ast()).getArgument(index) );
        }

        default _WA removeArgument(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_WA)this;
        }

        default _WA setArgument(int index, _expr _e){
            ((NodeWithArguments)ast()).getArguments().set(index, _e.ast());
            return (_WA)this;
        }

        default _WA setArgument(int index, Expression e){
            ((NodeWithArguments)ast()).getArguments().set(index, e);
            return (_WA)this;
        }

        default _WA setArgument(int index, boolean b){
            return setArgument(index, Exprs.of(b));
        }

        default _WA setArgument(int index, int i){
            return setArgument(index, Exprs.of(i));
        }

        default _WA setArgument(int index, char c){
            return setArgument(index, Exprs.of(c));
        }

        default _WA setArgument(int index, float f){
            return setArgument(index, Exprs.of(f));
        }

        default _WA setArgument(int index, long l){
            return setArgument(index, Exprs.of(l));
        }

        default _WA setArgument(int index, double d){
            return setArgument(index, Exprs.of(d));
        }

        default _WA setArguments(_arguments _as){
            ((NodeWithArguments)ast()).setArguments(_as.listAstElements());
            return (_WA)this;
        }

        default _WA setArguments(_expr... _es){
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

        default int countArguments(Predicate<_expr> matchFn){
            return listArguments(matchFn).size();
        }

        default List<_expr> listArguments(){
            List<_expr> args = new ArrayList<>();
            ((NodeWithArguments)ast()).getArguments().forEach(a -> args.add(_expr.of( (Expression)a)));
            return args;
        }

        default List<_expr> listArguments(Predicate<_expr> matchFn){
            return listArguments().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean isArguments(String... es){
            _expr[] _es = new _expr[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expr.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(Expression... es){
            _expr[] _es = new _expr[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expr.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(_expr... _es){
            List<_expr> _tes = listArguments();
            if(_es.length == _tes.size()){
                for(int i=0;i<_es.length;i++){
                    if( ! Exprs.equal(  _es[i].ast(), _tes.get(i).ast() ) ){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isArguments(Predicate<List<_expr>> matchFn){
            return matchFn.test( listArguments() );
        }


        default boolean isArgument(int index, boolean b){
            return isArgument(index, Exprs.of(b));
        }



        default boolean isArgument(int index, int i){
            return isArgument(index, Exprs.of(i));
        }

        default boolean isArgument(int index, char c){
            return isArgument(index, Exprs.of(c));
        }

        default boolean isArgument(int index, float f){
            return isArgument(index, Exprs.of(f));
        }

        default boolean isArgument(int index, long l){
            return isArgument(index, Exprs.of(l));
        }

        default boolean isArgument(int index, double d){
            return isArgument(index, Exprs.of(d));
        }

        default boolean isArgument(int index, String exprString){
            try {
                return Exprs.equal( getArgument(index).ast(), Exprs.of(exprString));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArgument(int index, Expression e){
            try {
                return Exprs.equal( getArgument(index).ast(), e);
            }catch(Exception ex){
                return false;
            }
        }

        /**
         * Verify the argument at this position is an instanceof one of the provided expressionClasses
         * @param index
         * @param expressionClasses
         * @return
         */
        default boolean isArgument(int index, Class<? extends _expr>...expressionClasses ){
            try{
                return Arrays.stream(expressionClasses).anyMatch(ec-> ec.isAssignableFrom(getArgument(index).getClass()));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArgument(int index, _expr _e){
            try {
                return Exprs.equal( getArgument(index).ast(), _e.ast());
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArgument(int index, Predicate<_expr> pe){
            return pe.test( getArgument(index) );
        }

        default _WA addArgument(int i){
            return addArgument( Exprs.of(i) );
        }

        default _WA addArgument(boolean b){
            return addArgument( Exprs.of(b) );
        }

        default _WA addArgument(float f){
            return addArgument( Exprs.of(f) );
        }

        default _WA addArgument(long l){
            return addArgument( Exprs.of(l) );
        }

        default _WA addArgument(double d){
            return addArgument( Exprs.of(d) );
        }

        default _WA addArgument(char c){
            return addArgument( Exprs.of(c) );
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

        default _WA addArguments(_expr... _es){
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
        default _WA removeArguments(Predicate<_expr> matchFn){
            ((NodeWithArguments)ast()).getArguments().removeIf(matchFn);
            return (_WA)this;
        }

        default _WA removeArguments(_expr... es){
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

        default _WA forArguments(Consumer<_expr> argFn){
            ((NodeWithArguments)ast()).getArguments().stream().map( a-> _expr.of( (Expression)a))
                    .forEach(e->  argFn.accept( (_expr)e) );
            return (_WA)this;
        }

        default _WA forArguments(Predicate<_expr> expressionMatchFn, Consumer<_expr> argFn){
            ((NodeWithArguments)ast()).getArguments().stream()
                    .map( a-> _expr.of( (Expression)a))
                    .filter(expressionMatchFn).forEach(e->  argFn.accept( (_expr)e) );
            return (_WA)this;
        }
    }
}
