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
 * interface for all $statement bots (bots that can inspect or manipulate {@link Statement} or {@link _statement}
 * implementations) Useful when you want to represent Statements in an "unspecific" way
 * (otherwise use a specific bot, (i.e. $returnStmt.of(), $whileStmt.of())
 * @param <S>
 * @param <_S>
 * @param <$S>
 */
public interface $statement<S extends Statement, _S extends _statement, $S extends $bot.$node<S, _S, $S>>
    extends $bot.$node<S, _S, $S>, $selector.$node<_S, $S>, Template<_S> {

    static $statement of() {
        return new $s();
    }

    static $statement of(Stencil stencil) {
        return new $s(stencil);
    }

    static $statement of(String code) {
        return of( Statements.of(code));
    }

    static $statement of(String... code) {
        return of( Statements.of(code));
    }

    static $statement of(_statement<?, ?> _e) {
        return of( _e.ast() );
    }

    /**
     * Matches ANY expression that is an instance of any of the expression classes
     */
    static $statement of(Class<? extends _statement>... expressionClasses) {
        return new $s().$and(expressionClasses);
    }

    static $statement of(Expressions.Command lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Supplier<? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Consumer<? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(BiConsumer<? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Expressions.TriConsumer<? extends Object, ? extends Object,? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Expressions.QuadConsumer<? extends Object, ? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Function<? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(BiFunction<? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Expressions.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement of(Expressions.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithStatement){
        Statement st = Statements.from(Thread.currentThread().getStackTrace()[2]);
        return of(st);
    }

    static $statement from( LambdaExpr le ){
        Optional<AssertStmt> ows = le.getBody().findFirst(AssertStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    static<$S extends $statement> $S of(Statement st) {
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
    default boolean matches(_statement _s) {
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
        return $refactor.of(this, $statement.of(refactoringPattern));
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo($statement refactoringPattern){
        return $refactor.of(this, refactoringPattern);
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactorAction the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(Consumer<Select<? extends _statement>> refactorAction){
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
    static $refactor refactor($statement $target, $statement $refactoring){
        return $refactor.of($target, $refactoring);
    }


    /**
     * build and return a $statement.$refactor for converting one statement to another
     * @param $target
     * @param refactorAction
     * @return
     */
    static $refactor refactor($statement $target, Consumer<Select<? extends _statement>> refactorAction){
        return $refactor.of($target, refactorAction);
    }

    /**
     * Refactoring from one {@link _statement} to another {@link _statement}
     * @param <_T> the "target" {@link _statement} type
     */
    class $refactor<_T extends _statement> implements $refactoring{

        /** Selects the instances to refactor from the target */
        $statement target$Bot;

        /** with each selection do the "Refactor" */
        Consumer<Select<_T>> refactorAction;

        public static $refactor of(String $target, String $change){
            return of($statement.of($target), $statement.of($change));
        }

        public static $refactor of(_statement _target, _statement _change){
            return of( $statement.of(_target), $statement.of(_change));
        }

        public static $refactor of($statement $target, $statement $change){
            return of($target, (s)->{
                _statement _drafted = (_statement)$change.draft( ((Select)s).tokens);
                _statement _target = (_statement) ((Select)s).selection;
                _target.replace(_drafted);
            });
        }

        public static $refactor of($statement $target, Consumer<Select<? extends _statement>> refactorAction){
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

        public _codeUnits in(Class... clazz){
            _codeUnits _cus = _codeUnits.of(clazz);
            this.target$Bot.forSelectedIn(_cus, refactorAction);
            return _cus;
        }

        public <N extends Node> N in(N astNode){
            return (N) this.target$Bot.forSelectedIn(astNode, refactorAction);
        }

        public <_N extends _java._node> _N in(_N _n){
            return (_N) this.target$Bot.forSelectedIn(_n, refactorAction);
        }

        public _codeUnits in(_batch... _batches){
            _codeUnits _cus = _codeUnits.of(_batches);
            this.target$Bot.forSelectedIn(_cus, refactorAction);
            return _cus;
        }

        public _codeUnits in(_codeUnits... _cus){
            _codeUnits _cuss = _codeUnits.of(_cus);
            this.target$Bot.forSelectedIn(_cuss, refactorAction);
            return _cuss;
        }
    }
}
