package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * Model of a forEach iteration Statement
 * <PRE>
 * for(Object o: objects) { ... }
 * </PRE>
 *
 */
public final class _forEachStmt implements
        _stmt._loop<ForEachStmt, _forEachStmt>,
        _body._withBody<_forEachStmt> {

    public static final Function<String, _forEachStmt> PARSER = s-> _forEachStmt.of(s);

    public static _forEachStmt of(){
        return new _forEachStmt( new ForEachStmt( ));
    }
    public static _forEachStmt of(ForEachStmt fe){
        return new _forEachStmt(fe);
    }
    public static _forEachStmt of(String...code){
        return new _forEachStmt(Stmt.forEachStmt( code));
    }

    public static <A extends Object> _forEachStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _forEachStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _forEachStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forEachStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forEachStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _forEachStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forEachStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forEachStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _forEachStmt from( LambdaExpr le){
        Optional<ForEachStmt> ows = le.getBody().findFirst(ForEachStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No for-each statement found in lambda");
    }

    public static _feature._one<_forEachStmt, _body> BODY = new _feature._one<>(_forEachStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_forEachStmt a, _body b) -> a.setBody(b), PARSER);

    public static _feature._one<_forEachStmt, _expr> ITERABLE = new _feature._one<>(_forEachStmt.class, _expr.class,
            _feature._id.ITERABLE,
            a -> a.getIterable(),
            (_forEachStmt a, _expr _e) -> a.setIterable(_e), PARSER);

    public static _feature._one<_forEachStmt, _variablesExpr> VARIABLES = new _feature._one<>(_forEachStmt.class, _variablesExpr.class,
            _feature._id.VARIABLES,
            a -> a.getVariable(),
            (_forEachStmt a, _variablesExpr _e) -> a.setVariable(_e), PARSER);

    public static _feature._features<_forEachStmt> FEATURES = _feature._features.of(_forEachStmt.class, PARSER,  VARIABLES, ITERABLE, BODY);

    private ForEachStmt node;

    public _forEachStmt(ForEachStmt rs){
        this.node = rs;
    }

    public _feature._features<_forEachStmt> features(){
        return FEATURES;
    }

    @Override
    public _forEachStmt copy() {
        return new _forEachStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _forEachStmt replace(ForEachStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean isVariable(String...expression){
        try {
            //NOTE: we convert to _variabelsExpr because this allows out-of-order testing "int a,b" === "int b,a"
            return Objects.equals(_variablesExpr.of(this.node.getVariable()), _variablesExpr.of(Ast.variableDeclarationExpr(expression)));
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean hasVariable(String variableDeclaration){
        return this.getVariable().has(variableDeclaration);
    }

    public boolean hasVariable(_variable _v){
        return this.getVariable().has(_v);
    }

    public boolean hasVariable(Predicate<_variable> _vMatchFn){
        return this.getVariable().has(_vMatchFn);
    }

    public boolean isVariable(VariableDeclarationExpr ve){
        return Objects.equals( this.node.getVariable(), ve);
    }

    public boolean isVariable(_variablesExpr _v){
        return Objects.equals( this.node.getVariable(), _v.node());
    }

    public _variablesExpr getVariable(){
        return new _variablesExpr(this.node.getVariable());
    }

    public _forEachStmt setVariable(String... var){
        this.node.setVariable(Ast.variableDeclarationExpr(var));
        return this;
    }

    public _forEachStmt setVariable( _variablesExpr _v){
        this.node.setVariable(_v.node);
        return this;
    }

    public _forEachStmt setVariable(VariableDeclarationExpr v){
        this.node.setVariable(v);
        return this;
    }

    public boolean isIterable(String...expression){
        try {
            return Objects.equals(this.node.getIterable(), Expr.of(expression));
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean isIterable(Expression e){
        return Objects.equals( this.node.getIterable(), e);
    }

    public boolean isIterable(_expr _e){
        return Objects.equals( this.node.getIterable(), _e.node());
    }

    public _expr getIterable(){
        return _expr.of(this.node.getIterable());
    }

    public _forEachStmt setIterable(String...str){
        this.node.setIterable(Expr.of(str));
        return this;
    }

    public _forEachStmt setIterable(Expression e){
        this.node.setIterable(e);
        return this;
    }

    public _forEachStmt setIterable(_expr e){
        this.node.setIterable(e.node());
        return this;
    }

    public _body getBody(){
        return _body.of( this.node.getBody() );
    }

    @Override
    public _forEachStmt setBody(BlockStmt body) {
        this.node.setBody(body);
        return this;
    }

    public _forEachStmt setBody(_stmt _st){
        this.node.setBody(_st.node());
        return this;
    }

    public _forEachStmt setBody(_body _bd){
        this.node.setBody(_bd.ast());
        return this;
    }

    public _forEachStmt clearBody(){
        this.node.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _forEachStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.node.getBody();
        if( bd instanceof BlockStmt){
            for(int i=0;i<statements.length; i++) {
                bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(bd);
        for(int i=0;i<statements.length; i++) {
            bd.asBlockStmt().addStatement(1+startStatementIndex, statements[i]);
        }
        return this;
    }

    /*
    @Override
    public boolean is(ForEachStmt astNode) {
        return this.astStmt.equals( astNode);
    }
     */

    public ForEachStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _forEachStmt ){
            _forEachStmt _o = (_forEachStmt)other;
            if( !Objects.equals( getVariable(), _o.getVariable() ) ){
                return false;
            }
            if( !Objects.equals(getIterable(), _o.getIterable())){
                return false;
            }
            boolean bodyEquals = Objects.equals( getBody(), _o.getBody());
            return bodyEquals;
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
