package org.jdraft.pattern;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Tokens;

import java.util.HashMap;
import java.util.Map;
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
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> $doStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $doStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> $doStmt of( Exprs.QuadFunction<A,B,C,D,E> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
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
                    && this.stmtStencil.isMatchAny()
                    && this.partMap().values().stream().allMatch( p -> p.isMatchAny());
        }catch(Exception e){
            return false;
        }
    }

    public Select<DoStmt, _doStmt> select(_stmt _s){
        System.out.println( "IN SELECT "+ _s);
        if( _s == null ){
            return null;
        }
        if( !statementClass.isAssignableFrom(_s.ast().getClass())){
            return null;
        }
        DoStmt s = (DoStmt)_s.ast();
        if( ! astMatch.test((_doStmt) _stmt.of(s))){
            return null;
        }
        if( this.stmtStencil != null ) {
            Tokens st = this.stmtStencil.parse(_s.ast().toString(NO_COMMENTS));
            if (st == null) {
                return null;
            }
            $ex.Select sel = this.condition.select( ((_doStmt)_s).getCondition());
            if( sel == null ){
                return null;
            }
            st.putAll(sel.tokens);

            $stmt.Select ss = this.body.select( ((_doStmt)_s).getBody().ast());
            if( ss == null ){
                return null;
            }
            st.putAll(ss.tokens);

            return new Select( _s.ast(), $tokens.of(st) );
        }

        Tokens st = this.stmtStencil.parse(_s.ast().toString(NO_COMMENTS));
        return new Select( _s.ast(), $tokens.of(st) );
    }

    /**
     *
     * @param astStmt
     * @return
     */
    public Select<DoStmt, _doStmt> select(Statement astStmt ){
        if( astStmt == null ){
            return null;
        }
        if( !statementClass.isAssignableFrom(astStmt.getClass())){
            return null;
        }
        DoStmt s = (DoStmt)astStmt;
        if( ! astMatch.test((_doStmt) _stmt.of(s))){
            return null;
        }
        Tokens st = this.stmtStencil.parse(astStmt.toString(NO_COMMENTS));
        if( st == null ){
            return null;
        }
        return new Select( astStmt, $tokens.of(st) );
    }


    public Map<String, $pattern> partMap(){
        Map<String, $pattern> partMap = new HashMap<>();
        partMap.put("condition", condition);
        partMap.put("body", body);
        return partMap;
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

    public $doStmt $condition(Predicate<_expr> $condition){
        this.condition.constraint = this.condition.constraint.and($condition);
        return this;
    }

    public $doStmt $condition($ex $condition){
        this.condition = $condition;
        return this;
    }

    public $doStmt $condition(_expr _e){
        this.condition = $ex.of(_e.ast());
        return this;
    }

    public $doStmt $body( $stmt $st ){
        this.body = $st;
        return this;
    }

    public $doStmt $body( Predicate<_stmt> statementMatchFn ){
        this.body.$and(statementMatchFn);
        return this;
    }

}
