package org.jdraft.pattern;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import org.jdraft.*;

import java.util.Optional;
import java.util.function.*;

public class $doStmt extends $stmt<DoStmt, _doStmt> {

    public static $doStmt of(){
        return new $doStmt();
    }

    public static $doStmt of(String...code){
        return of( _doStmt.of(code));
    }

    public static $doStmt of( DoStmt ds){
        return of( _doStmt.of(ds));
    }

    public static $doStmt of(_doStmt _ds){
        $doStmt $ds = of();
        $ds.condition = $ex.of( _ds.ast().getCondition() );
        $ds.body = $stmt.of(_ds.ast().getBody() );
        return $ds;
    }

    public static $doStmt of(Predicate<_doStmt> doStmtPredicate){
        return new $doStmt(doStmtPredicate);
    }

    /*
    public static <A extends Object> $doStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> $doStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> $doStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> $doStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $doStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }
     */

    public static <A extends Object, B extends Object> $doStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> $doStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $doStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> $doStmt of( Ex.QuadFunction<A,B,C,D,E> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }


    private static $doStmt from( LambdaExpr le){
        Optional<DoStmt> ows = le.getBody().findFirst(DoStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No do statement found in lambda");
    }


    public boolean isMatchAny(){
        try{
            return this.astMatch.test(null)
                    && this.statementClass == DoStmt.class
                    && this.stmtStencil.isMatchAny();
        }catch(Exception e){
            return false;
        }
    }

    public $ex condition = $ex.any();

    public $stmt body = $stmt.of();

    public $doStmt() {
        super(DoStmt.class, "$doStmt$");
    }

    public $doStmt(Predicate<_doStmt> dsp){
        super(DoStmt.class);
        $and(dsp);
    }

    public $doStmt $condition(Predicate<_expression> $condition){
        this.condition.constraint = this.condition.constraint.and($condition);
        return this;
    }
    public $doStmt $condition($ex $condition){
        this.condition = $condition;
        return this;
    }
    public $doStmt $condition(_expression _e){
        this.condition = $ex.of(_e.ast());
        return this;
    }

    public $doStmt $body( $stmt $st ){
        this.body = $st;
        return this;
    }

    public $doStmt $body( Predicate<_statement> statementMatchFn ){
        this.body.$and(statementMatchFn);
        return this;
    }

}
