package org.jdraft;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Objects;
import java.util.function.Predicate;

public final class _returnStmt implements _statement._controlFlow._signal<ReturnStmt, _returnStmt>,
        _java._withExpression<ReturnStmt, _returnStmt>,
        _java._uniPart<ReturnStmt, _returnStmt> {

    public static _returnStmt of(){
        return new _returnStmt( new ReturnStmt( ));
    }

    public static _returnStmt of(ReturnStmt rs){
        return new _returnStmt( rs);
    }

    public static _returnStmt of(_expression _e){
        return of().setExpression(_e.ast());
    }

    public static _returnStmt of(Expression e){
        return of().setExpression(e);
    }

    public static _returnStmt of(Enum e ){
        return of().setExpression(_fieldAccess.of(e.getClass().getCanonicalName()+"."+e.name()).ast());
    }
    public static _returnStmt of(int literal){
        return new _returnStmt( new ReturnStmt( new IntegerLiteralExpr(literal)));
    }

    public static _returnStmt of(long literal){
        return new _returnStmt( new ReturnStmt( _long.of(literal).ast()));
    }

    public static _returnStmt of(char literal){
        return new _returnStmt( new ReturnStmt( new CharLiteralExpr(literal)));
    }

    public static _returnStmt of(boolean literal){
        return new _returnStmt( new ReturnStmt( new BooleanLiteralExpr(literal)));
    }

    public static _returnStmt of(float literal){
        return new _returnStmt( new ReturnStmt( new DoubleLiteralExpr(literal+"F")));
    }

    public static _returnStmt of(double literal){
        return new _returnStmt( new ReturnStmt( new DoubleLiteralExpr(literal+"D")));
    }

    public static _returnStmt ofString(String literal){
        return new _returnStmt( new ReturnStmt( new StringLiteralExpr(literal)));
    }

    public static _returnStmt ofName(String name){
        return new _returnStmt( new ReturnStmt( new NameExpr(name)));
    }

    public static _returnStmt of( String...code){
        return new _returnStmt(Statements.returnStmt( code));
    }

    private ReturnStmt rs;

    public _returnStmt(ReturnStmt rs){
        this.rs = rs;
    }

    @Override
    public _returnStmt copy() {
        return new _returnStmt( this.rs.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Statements.returnStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    /*
    @Override
    public boolean is(ReturnStmt astNode) {
        return this.rs.equals( astNode);
    }
     */

    public ReturnStmt ast(){
        return rs;
    }

    public _returnStmt removeExpression(){
        this.rs.removeExpression();
        return this;
    }

    public boolean isExpression(String...expression){
        if( !this.hasExpression() ){
            return false;
        }
        try{
            return isExpression(Expressions.of(expression));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isExpression(_expression _ex){
        return Expressions.equal( this.getExpression().ast(), _ex.ast());
    }

    public boolean isExpression(Expression ex){
        return Expressions.equal( this.getExpression().ast(), ex);
    }

    public boolean isExpression(Predicate<_expression> matchFn){
        return matchFn.test(getExpression());
    }

    public boolean isExpression( int i){
        return isExpression( Expressions.of(i) );
    }

    public boolean isExpression( boolean b){
        return isExpression( Expressions.of(b) );
    }

    public boolean isExpression( float f){
        return isExpression( Expressions.of(f) );
    }

    public boolean isExpression( long l){
        return isExpression( Expressions.of(l) );
    }

    public boolean isExpression( double d){
        return isExpression( Expressions.of(d) );
    }

    public boolean isExpression( char c){
        return isExpression( Expressions.of(c) );
    }

    public _returnStmt setExpression(String...expression){
        return setExpression(Expressions.of(expression));
    }

    public _returnStmt setExpression(_expression e){
        return setExpression(e.ast());
    }

    public _returnStmt setExpression(Expression e){
        this.rs.setExpression(e);
        return this;
    }

    public _expression getExpression(){
        if( this.hasExpression() ){
            return _expression.of(this.rs.getExpression().get());
        }
        return null;
    }

    /*
    public _returnStmt setExpression(_expression _e){
        return setExpression(_e.ast());
    }

    public _returnStmt setExpression(Expression e){
        this.rs.setExpression(e);
        return this;
    }
    public Expression getExpression(){
        if(this.rs.getExpression().isPresent()){
            return rs.getExpression().get();
        }
        return null;
    }
    */

    public boolean hasExpression(){
        return this.rs.getExpression().isPresent();
    }

    public String toString(){
        return this.rs.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _returnStmt ){
            return Objects.equals( ((_returnStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
