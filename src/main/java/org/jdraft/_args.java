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
public final class _args
        implements _java._list<Expression, _expr, _args> {

    public static _args of(){
        return of( Exprs.methodCallEx("empty()"));
    }

    public static _args of(Expression... exs){
        return of().add(exs);
    }

    public static _args of(_expr... _exs){
        return of().add(_exs);
    }

    public static <A extends Object> _args of(Supplier<A> supplier ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _args of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _args of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _args of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _args from(LambdaExpr le){
        Optional<ArrayCreationExpr> ows = le.getBody().findFirst(ArrayCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get().getInitializer().get().getValues().toArray(new Expression[0]));
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    public static _args of(String...args){
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



    public static _args of(NodeWithArguments nwa){
         return new _args(nwa);
    }

    public NodeWithArguments nwa;

    public _args(NodeWithArguments nwa){
        this.nwa = nwa;
    }

    public _args add(int i){
        return add( _intExpr.of(i) );
    }

    public _args add(char c){
        return add( _charExpr.of(c) );
    }

    public _args add(boolean b){
        return add( _booleanExpr.of(b) );
    }

    public _args add(long l){
        return add( _longExpr.of(l) );
    }

    public _args add(float f){
        return add( _doubleExpr.of(f) );
    }

    public _args add(double d){
        return add( _doubleExpr.of(d) );
    }

    public _args add(String...args){
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

    public _args setAt(int index, int i){
        return setAt(index, _intExpr.of(i) );
    }

    public _args setAt(int index, char c){
        return setAt(index, _charExpr.of(c) );
    }

    public _args setAt(int index, boolean b){
        return setAt(index, _booleanExpr.of(b) );
    }

    public _args setAt(int index, long l){
        return setAt(index, _longExpr.of(l) );
    }

    public _args setAt(int index, float f){
        return setAt(index, _doubleExpr.of(f) );
    }

    public _args setAt(int index, double d){
        return setAt(index, _doubleExpr.of(d) );
    }

    @Override
    public _args copy() {
        Node n = (Node)nwa;
        return new _args( (NodeWithArguments) (n.clone()) );
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
        if( o instanceof _args){
            _args _as = (_args) o;
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
    public interface _withArgs<N extends Node, _WA extends _java._node> extends _java._node<N, _WA> {

        /**
         * Creates and returns an {@link _args} to model the whole arguments list
         * @return an _args modelling 0...n arguments in the arguments list)
         */
        default _args getArgs(){
            return of( (NodeWithArguments)ast());
        }

        /**
         * Check if all individual arg ({@link _expr}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allArgs(Predicate<_expr> matchFn){
            return listArgs().stream().allMatch(matchFn);
        }

        default _expr getArg(int index){
            return _expr.of( ((NodeWithArguments)ast()).getArgument(index) );
        }

        default _WA removeArg(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_WA)this;
        }

        default _WA setArg(int index, _expr _e){
            ((NodeWithArguments)ast()).getArguments().set(index, _e.ast());
            return (_WA)this;
        }

        default _WA setArg(int index, Expression e){
            ((NodeWithArguments)ast()).getArguments().set(index, e);
            return (_WA)this;
        }

        default _WA setArg(int index, boolean b){
            return setArg(index, Exprs.of(b));
        }

        default _WA setArg(int index, int i){
            return setArg(index, Exprs.of(i));
        }

        default _WA setArg(int index, char c){
            return setArg(index, Exprs.of(c));
        }

        default _WA setArg(int index, float f){
            return setArg(index, Exprs.of(f));
        }

        default _WA setArg(int index, long l){
            return setArg(index, Exprs.of(l));
        }

        default _WA setArg(int index, double d){
            return setArg(index, Exprs.of(d));
        }

        default _WA setArgs(_args _as){
            ((NodeWithArguments)ast()).setArguments(_as.listAstElements());
            return (_WA)this;
        }

        default _WA setArgs(_expr... _es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(_es).forEach(n -> nle.add(n.ast()));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_WA)this;
        }

        default _WA setArgs(Expression... es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(es).forEach(n -> nle.add(n));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_WA)this;
        }

        default boolean hasArgs(){
            return ((NodeWithArguments)ast()).getArguments().size() > 0 ;
        }

        default int countArgs(){
            return ((NodeWithArguments)ast()).getArguments().size();
        }

        default int countArgs(Predicate<_expr> matchFn){
            return listArgs(matchFn).size();
        }

        default List<_expr> listArgs(){
            List<_expr> args = new ArrayList<>();
            ((NodeWithArguments)ast()).getArguments().forEach(a -> args.add(_expr.of( (Expression)a)));
            return args;
        }

        default List<_expr> listArgs(Predicate<_expr> matchFn){
            return listArgs().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean isArgs(String... es){
            _expr[] _es = new _expr[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expr.of(es[i]);
            }
            return isArgs(_es);
        }

        default boolean isArgs(Expression... es){
            _expr[] _es = new _expr[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expr.of(es[i]);
            }
            return isArgs(_es);
        }

        default boolean isArgs(_expr... _es){
            List<_expr> _tes = listArgs();
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

        default boolean isArgs(Predicate<List<_expr>> matchFn){
            return matchFn.test( listArgs() );
        }

        default boolean isArg(int index, boolean b){
            return isArg(index, Exprs.of(b));
        }

        default boolean isArg(int index, int i){
            return isArg(index, Exprs.of(i));
        }

        default boolean isArg(int index, char c){
            return isArg(index, Exprs.of(c));
        }

        default boolean isArg(int index, float f){
            return isArg(index, Exprs.of(f));
        }

        default boolean isArg(int index, long l){
            return isArg(index, Exprs.of(l));
        }

        default boolean isArg(int index, double d){
            return isArg(index, Exprs.of(d));
        }

        default boolean isArg(int index, String exprString){
            try {
                return Exprs.equal( getArg(index).ast(), Exprs.of(exprString));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArg(int index, Expression e){
            try {
                return Exprs.equal( getArg(index).ast(), e);
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
        default boolean isArg(int index, Class<? extends _expr>...expressionClasses ){
            try{
                return Arrays.stream(expressionClasses).anyMatch(ec-> ec.isAssignableFrom(getArg(index).getClass()));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArg(int index, _expr _e){
            try {
                return Exprs.equal( getArg(index).ast(), _e.ast());
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArg(int index, Predicate<_expr> pe){
            return pe.test( getArg(index) );
        }

        default _WA addArg(int i){
            return addArg( Exprs.of(i) );
        }

        default _WA addArg(boolean b){
            return addArg( Exprs.of(b) );
        }

        default _WA addArg(float f){
            return addArg( Exprs.of(f) );
        }

        default _WA addArg(long l){
            return addArg( Exprs.of(l) );
        }

        default _WA addArg(double d){
            return addArg( Exprs.of(d) );
        }

        default _WA addArg(char c){
            return addArg( Exprs.of(c) );
        }

        default _WA addArg(Expression e){
            return addArgs( e );
        }

        default _WA addArgs(String... es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_WA)this;
        }

        default _WA addArgs(Expression... es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_WA)this;
        }

        default _WA addArgs(_expr... _es){
            Arrays.stream(_es).forEach(_e -> ((NodeWithArguments)ast()).addArgument(_e.ast()));
            return (_WA)this;
        }

        default _WA removeArgs(){
            ((NodeWithArguments)ast()).getArguments().removeIf( t->true);
            return (_WA)this;
        }

        default _WA removeArgs(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_WA)this;
        }
        default _WA removeArgs(Predicate<_expr> matchFn){
            ((NodeWithArguments)ast()).getArguments().removeIf(matchFn);
            return (_WA)this;
        }

        default _WA removeArgs(_expr... es){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i].ast());
            }
            return (_WA)this;
        }

        default _WA removeArgs(Expression... es){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i]);
            }
            return (_WA)this;
        }

        default _WA forArgs(Consumer<_expr> argFn){
            ((NodeWithArguments)ast()).getArguments().stream().map( a-> _expr.of( (Expression)a))
                    .forEach(e->  argFn.accept( (_expr)e) );
            return (_WA)this;
        }

        default _WA forArgs(Predicate<_expr> expressionMatchFn, Consumer<_expr> argFn){
            ((NodeWithArguments)ast()).getArguments().stream()
                    .map( a-> _expr.of( (Expression)a))
                    .filter(expressionMatchFn).forEach(e->  argFn.accept( (_expr)e) );
            return (_WA)this;
        }
    }
}
