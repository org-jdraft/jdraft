package org.jdraft;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Objects;


public class _returnStmt implements _statement._controlFlow._signal<ReturnStmt, _returnStmt>,
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
        return new _returnStmt(Stmt.returnStmt( code));
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
            return is( Stmt.returnStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ReturnStmt astNode) {
        return this.rs.equals( astNode);
    }

    public ReturnStmt ast(){
        return rs;
    }

    public _returnStmt removeExpression(){
        this.rs.removeExpression();
        return this;
    }

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
