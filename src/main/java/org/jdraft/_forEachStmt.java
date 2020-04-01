package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * for(Object o: objects) { ... }
 */
public final class _forEachStmt implements _statement._controlFlow._branching<ForEachStmt, _forEachStmt>,
        _statement._controlFlow._loop<ForEachStmt, _forEachStmt>,
        _java._multiPart<ForEachStmt, _forEachStmt>,
        _body._hasBody<_forEachStmt>{

    public static _forEachStmt of(){
        return new _forEachStmt( new ForEachStmt( ));
    }
    public static _forEachStmt of(ForEachStmt fe){
        return new _forEachStmt(fe);
    }
    public static _forEachStmt of(String...code){
        return new _forEachStmt(Statements.forEachStmt( code));
    }

    public static <A extends Object> _forEachStmt of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _forEachStmt of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _forEachStmt of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forEachStmt of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forEachStmt of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _forEachStmt of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forEachStmt of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forEachStmt of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _forEachStmt from( LambdaExpr le){
        Optional<ForEachStmt> ows = le.getBody().findFirst(ForEachStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No for-each statement found in lambda");
    }

    private ForEachStmt astStmt;

    public _forEachStmt(ForEachStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _forEachStmt copy() {
        return new _forEachStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Statements.forEachStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean isIterable(String...expression){
        try {
            return Objects.equals(this.astStmt.getIterable(), Expressions.of(expression));
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean isIterable(Expression e){
        return Objects.equals( this.astStmt.getIterable(), e);
    }

    public boolean isIterable(_expression _e){
        return Objects.equals( this.astStmt.getIterable(), _e.ast());
    }

    public _expression getIterable(){
        return _expression.of(this.astStmt.getIterable());
    }


    public boolean isVariable(String...expression){
        try {
            return Objects.equals(this.astStmt.getVariable(), Ast.varLocalEx(expression));
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean isVariable(VariableDeclarationExpr ve){
        return Objects.equals( this.astStmt.getVariable(), ve);
    }

    public boolean isVariable(_localVariables _v){
        return Objects.equals( this.astStmt.getVariable(), _v.ast());
    }

    public _localVariables getVariable(){
        return new _localVariables(this.astStmt.getVariable());
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    @Override
    public _forEachStmt setBody(BlockStmt body) {
        this.astStmt.setBody(body);
        return this;
    }

    public _forEachStmt setIterable(String...str){
        this.astStmt.setIterable(Expressions.of(str));
        return this;
    }

    public _forEachStmt setIterable(Expression e){
        this.astStmt.setIterable(e);
        return this;
    }

    public _forEachStmt setIterable(_expression e){
        this.astStmt.setIterable(e.ast());
        return this;
    }

    public _forEachStmt setVariable(String... var){
        this.astStmt.setVariable(Ast.varLocalEx(var));
        return this;
    }

    public _forEachStmt setVariable( _localVariables _v){
        this.astStmt.setVariable(_v.varDeclEx);
        return this;
    }

    public _forEachStmt setVariable(VariableDeclarationExpr v){
        this.astStmt.setVariable(v);
        return this;
    }

    public _forEachStmt setBody(_statement _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _forEachStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _forEachStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _forEachStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getBody();
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

    @Override
    public boolean is(ForEachStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ForEachStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.ITERABLE, astStmt.getIterable());
        comps.put(_java.Component.VARIABLE, astStmt.getVariable());
        comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _forEachStmt ){
            return Objects.equals( ((_forEachStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
