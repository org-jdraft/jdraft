package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.io._batch;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;

import java.util.Optional;
import java.util.function.*;

/**
 * interface for all $statement bots (bots that can inspect or manipulate {@link Statement} or {@link _stmt}
 * implementations) Useful when you want to represent Statements in an "unspecific" way
 * (otherwise use a specific bot, (i.e. $returnStmt.of(), $whileStmt.of())
 * @param <S>
 * @param <_S>
 * @param <$S>
 */
public interface $stmt<S extends Statement, _S extends _stmt, $S extends $bot.$node<S, _S, $S>>
    extends $bot.$node<S, _S, $S>, $selector.$node<_S, $S>, Template<_S> {

    static $stmt of() {
        return new $s();
    }

    static $stmt of(Stencil stencil) {
        return new $s(stencil);
    }

    static $stmt of(String code) {
        return of( Stmts.of(code));
    }

    static $stmt of(String... code) {
        return of( Stmts.of(code));
    }

    static $stmt of(_stmt<?, ?> _e) {
        return of( _e.ast() );
    }

    /**
     * Matches ANY expression that is an instance of any of the expression classes
     */
    static $stmt of(Class<? extends _stmt>... expressionClasses) {
        return new $s().$and(expressionClasses);
    }

    static $stmt of(Exprs.Command lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Supplier<? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Consumer<? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(BiConsumer<? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Exprs.TriConsumer<? extends Object, ? extends Object,? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Exprs.QuadConsumer<? extends Object, ? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Function<? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(BiFunction<? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Exprs.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt of(Exprs.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Stmts.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $stmt from(LambdaExpr le ){
        Optional<AssertStmt> ows = le.getBody().findFirst(AssertStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    static<$S extends $stmt> $S of(Statement st) {
        if( st.isAssertStmt() ){
            return ($S)$assertStmt.of( (AssertStmt)st);
        }
        if( st.isReturnStmt()){
            return ($S)$returnStmt.of( (ReturnStmt)st);
        }
        if( st.isExpressionStmt()){
            return ($S)$expressionStmt.of( (ExpressionStmt)st);
        }
        return ($S)$s.of( st.toString() );
    }

    /**
     *
     * @param s
     * @return
     */
    default boolean matches(Statement s) {
        return select(s) != null;
    }

    /**
     *
     * @param _s
     * @return
     */
    default boolean matches(_stmt _s) {
        if( _s == null ){
            return isMatchAny();
        }
        return select(_s.ast()) != null;
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(String refactoringPattern){
        return $refactor.of(this, $stmt.of(refactoringPattern));
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo($stmt refactoringPattern){
        return $refactor.of(this, refactoringPattern);
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactorAction the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(Consumer<Select<? extends _stmt>> refactorAction){
        return $refactor.of(this, refactorAction);
    }

    /**
     * build and return a $statement.$refactor for converting one statement to another
     * @param targetPattern
     * @param refactoringPattern
     * @return
     */
    static $refactor refactor(String targetPattern, String refactoringPattern){
        return $refactor.of(targetPattern, refactoringPattern);
    }

    /**
     * build and return a $statement.$refactor for converting one statement to another
     * @param $target
     * @param $refactoring
     * @return
     */
    static $refactor refactor($stmt $target, $stmt $refactoring){
        return $refactor.of($target, $refactoring);
    }


    /**
     * build and return a $statement.$refactor for converting one statement to another
     * @param $target
     * @param refactorAction
     * @return
     */
    static $refactor refactor($stmt $target, Consumer<Select<? extends _stmt>> refactorAction){
        return $refactor.of($target, refactorAction);
    }

    /**
     * Refactoring from one {@link _stmt} to another {@link _stmt}
     * @param <_T> the "target" {@link _stmt} type
     */
    class $refactor<_T extends _stmt> implements $refactorBot {

        /** Selects the instances to refactor from the target */
        $stmt target$Bot;

        /** with each selection do the "Refactor" */
        Consumer<Select<_T>> refactorAction;

        public static $refactor of(String $target, String $change){
            return of($stmt.of($target), $stmt.of($change));
        }

        public static $refactor of(_stmt _target, _stmt _change){
            return of( $stmt.of(_target), $stmt.of(_change));
        }

        public static $refactor of($stmt $target, $stmt $change){
            return of($target, (s)->{
                _stmt _drafted = (_stmt)$change.draft( ((Select)s).tokens);
                _stmt _target = (_stmt) ((Select)s).selection;
                _target.replace(_drafted);
            });
        }

        public static $refactor of($stmt $target, Consumer<Select<? extends _stmt>> refactorAction){
            $refactor $refact = new $refactor();
            $refact.target$Bot = $target;
            $refact.refactorAction = refactorAction;
            return $refact;
        }

        //private constructor, use of() builders
        private $refactor(){ }

        public _type in(Class  clazz){
            return this.target$Bot.forSelectedIn(clazz, refactorAction);
        }

        public _project in(Class... clazz){
            _project _cus = _project.of(clazz);
            this.target$Bot.forSelectedIn(_cus, refactorAction);
            return _cus;
        }

        public <N extends Node> N in(N astNode){
            return (N) this.target$Bot.forSelectedIn(astNode, refactorAction);
        }

        public <_N extends _java._node> _N in(_N _n){
            return (_N) this.target$Bot.forSelectedIn(_n, refactorAction);
        }

        public _project in(_batch... _batches){
            _project _cus = _project.of(_batches);
            this.target$Bot.forSelectedIn(_cus, refactorAction);
            return _cus;
        }

        public _project in(_project... _p){
            _project _cuss = _project.of(_p);
            this.target$Bot.forSelectedIn(_cuss, refactorAction);
            return _cuss;
        }
    }
}
