package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import org.jdraft.*;

import org.jdraft.io._batch;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;

import java.util.function.Consumer;

/**
 * base for all expression {@link $bot}s
 *
 * @param <E>
 * @param <_E>
 * @param <$E>
 */
public interface $expression<E extends Expression, _E extends _expression, $E extends $bot.$node<E, _E, $E>>
    extends $bot.$node<E, _E, $E>, $selector.$node<_E, $E>, Template<_E> {

    static $expression of() {
        return new $e();
    }

    static $expression of(Stencil stencil ) {
        return new $e(stencil);
    }

    static $expression of(String code) {
        return of( Expressions.of(code));
    }

    static $expression of(String... code) {
        return of( Expressions.of(code));
    }

    static $expression of(_expression<?,?> _e) {
        return of( _e.ast() );
    }

    /**
     * Matches ANY expression that is an instance of any of the expression classes
     */
    static $expression of(Class<? extends _expression>...expressionClasses) {
        return new $e().$and(expressionClasses);
    }

    static<$E extends $expression> $E of(Expression ile) {

        if( ile instanceof LiteralExpr){
            if( ile instanceof IntegerLiteralExpr){
                return ($E) $int.of( (IntegerLiteralExpr) ile);
            }
            if( ile instanceof LongLiteralExpr){
                return ($E) $long.of( (LongLiteralExpr) ile);
            }
            if( ile instanceof StringLiteralExpr){
                return ($E) $string.of( (StringLiteralExpr) ile);
            }
            if( ile instanceof DoubleLiteralExpr){
                return ($E) $double.of( (DoubleLiteralExpr) ile);
            }
            if( ile instanceof CharLiteralExpr){
                return ($E) $char.of( (CharLiteralExpr) ile);
            }
            if( ile instanceof TextBlockLiteralExpr){
                return ($E) $textBlock.of( (TextBlockLiteralExpr) ile);
            }
            if( ile instanceof BooleanLiteralExpr){
                return ($E) $boolean.of( (BooleanLiteralExpr) ile);
            }
            if( ile instanceof NullLiteralExpr){
                return ($E) $null.of( (NullLiteralExpr) ile);
            }
        }
        if( ile instanceof ArrayAccessExpr ){
            return ($E)$arrayAccess.of( (ArrayAccessExpr)ile);
        }
        if( ile instanceof EnclosedExpr ){
            return ($E)$enclosedExpression.of( (EnclosedExpr)ile);
        }
        return ($E)$e.of( ile.toString() );
    }

    default boolean matches(Expression e) {
        return select(e) != null;
    }

    default boolean matches(_expression e) {
        if( e == null ){
            return isMatchAny();
        }
        return select(e.ast()) != null;
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(String refactoringPattern){
        return $refactor.of(this, $expression.of(refactoringPattern));
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo($expression refactoringPattern){
        return $refactor.of(this, refactoringPattern);
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactorAction the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(Consumer<Select<? extends _expression>> refactorAction){
        return $refactor.of(this, refactorAction);
    }

    /**
     * build and return a $expression.$refactor for converting
     * @param targetPattern
     * @param refactoringPattern
     * @return
     */
    static $refactor refactor(String targetPattern, String refactoringPattern){
        return $refactor.of(targetPattern, refactoringPattern);
    }

    /**
     *
     * @param $target
     * @param $refactoring
     * @return
     */
    static $refactor refactor($expression $target, $expression $refactoring){
        return $refactor.of($target, $refactoring);
    }

    /**
     * Refactoring from one {@link _expression} to another {@link _expression}
     * @param <_T> the target {@link _expression} type to be refactored
     */
    class $refactor<_T extends _expression> implements $refactoring{

        /** Selects the instances to refactor from the target */
        $expression target$Bot;

        /** with each selection do the "Refactor" */
        Consumer<Select<_T>> refactorAction;

        public static $refactor of(String $target, String $change){
            return of($expression.of($target), $expression.of($change));
        }

        public static $refactor of(_expression _target, _expression _change){
            return of( $expression.of(_target), $expression.of(_change));
        }

        public static $refactor of($expression $target, $expression $change){
            return of($target, (s)->{
                _expression _drafted = (_expression)$change.draft( ((Select)s).tokens);
                _expression _target = (_expression) ((Select)s).selection;
                _target.replace(_drafted);
            });
        }

        public static $refactor of($expression $target, Consumer<Select<? extends _expression>> refactorAction){
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

        public _project in(_project... _cus){
            _project _cuss = _project.of(_cus);
            this.target$Bot.forSelectedIn(_cuss, refactorAction);
            return _cuss;
        }
    }
}
