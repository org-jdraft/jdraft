package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.YieldStmt;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Predicate;


public class _yieldStmt implements _statement._controlFlow._signal<YieldStmt, _yieldStmt>,
        _java._uniPart<YieldStmt, _yieldStmt>,
        _java._withExpression<YieldStmt, _yieldStmt>{

    public static _yieldStmt of(){
        return new _yieldStmt( new YieldStmt( ));
    }
    public static _yieldStmt of(YieldStmt ys){
        return new _yieldStmt(ys);
    }

    public static _yieldStmt of(String...code){
        String t = Text.combine(code);
        if( t.startsWith("yield ") ){ //they could pass in the whole yield statement string
            return new _yieldStmt(Stmt.yieldStmt( code));
        }
        //they could just pass in the expression
        return of(Ex.of( code));
    }

    public static _yieldStmt of(Expression e){
        return new _yieldStmt( new YieldStmt(e));
    }

    private YieldStmt yieldStmt;

    public _yieldStmt(YieldStmt yieldStmt){
        this.yieldStmt = yieldStmt;
    }

    @Override
    public _yieldStmt copy() {
        return new _yieldStmt( this.yieldStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.yieldStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public YieldStmt ast(){
        return yieldStmt;
    }

    public boolean equals(Object other){
        if( other instanceof _yieldStmt ){
            return Objects.equals( ((_yieldStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
