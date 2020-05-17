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
public interface $expr<E extends Expression, _E extends _expr, $E extends $bot.$node<E, _E, $E>>
    extends $bot.$node<E, _E, $E>, $selector.$node<_E, $E>, Template<_E> {

    static $expr of() {
        return new $e();
    }

    static $expr of(Stencil stencil ) {
        return new $e(stencil);
    }

    static $expr of(String code) {
        return of( Exprs.of(code));
    }

    static $expr of(String... code) {
        return of( Exprs.of(code));
    }

    static $expr of(_expr<?,?> _e) {
        return of( _e.ast() );
    }

    /**
     * Matches ANY expression that is an instance of any of the expression classes
     */
    static $expr of(Class<? extends _expr>...expressionClasses) {
        return new $e().$and(expressionClasses);
    }

    static<$E extends $expr> $E of(Expression e) {

        if( e instanceof LiteralExpr){
            if( e instanceof IntegerLiteralExpr){
                return ($E) $intExpr.of( (IntegerLiteralExpr) e);
            }
            if( e instanceof LongLiteralExpr){
                return ($E) $longExpr.of( (LongLiteralExpr) e);
            }
            if( e instanceof StringLiteralExpr){
                return ($E) $stringExpr.of( (StringLiteralExpr) e);
            }
            if( e instanceof DoubleLiteralExpr){
                return ($E) $doubleExpr.of( (DoubleLiteralExpr) e);
            }
            if( e instanceof CharLiteralExpr){
                return ($E) $charExpr.of( (CharLiteralExpr) e);
            }
            if( e instanceof TextBlockLiteralExpr){
                return ($E) $textBlockExpr.of( (TextBlockLiteralExpr) e);
            }
            if( e instanceof BooleanLiteralExpr){
                return ($E) $booleanExpr.of( (BooleanLiteralExpr) e);
            }
            if( e instanceof NullLiteralExpr){
                return ($E) $nullExpr.of( (NullLiteralExpr) e);
            }
        }
        if( e instanceof ArrayAccessExpr ){
            return ($E) $arrayAccessExpr.of( (ArrayAccessExpr)e);
        }
        if( e instanceof BinaryExpr){
            return ($E) $binaryExpr.of( (BinaryExpr)e);
        }
        if( e instanceof ClassExpr ){
            return ($E) $classExpr.of( (ClassExpr)e);
        }
        if( e instanceof EnclosedExpr ){
            return ($E) $parenthesizedExpr.of( (EnclosedExpr)e);
        }
        if( e instanceof MethodCallExpr){
            return ($E) $methodCallExpr.of( (MethodCallExpr) e);
        }
        if( e instanceof UnaryExpr){
            return ($E) $unaryExpr.of( (UnaryExpr) e);
        }
        return ($E)$e.of( e.toString() );
    }

    default boolean matches(Expression e) {
        return select(e) != null;
    }

    default boolean matches(_expr e) {
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
        return $refactor.of(this, $expr.of(refactoringPattern));
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactoringPattern the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo($expr refactoringPattern){
        return $refactor.of(this, refactoringPattern);
    }

    /**
     * Build and return a $refactor that can be used/reused against any combination of Asts/Code/_codeUnits/etc.
     * @param refactorAction the _statement pattern used to replace the target statement
     * @return a new $refactor
     */
    default $refactor refactorTo(Consumer<Select<? extends _expr>> refactorAction){
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
    static $refactor refactor($expr $target, $expr $refactoring){
        return $refactor.of($target, $refactoring);
    }

    /**
     * Refactoring from one {@link _expr} to another {@link _expr}
     * @param <_T> the target {@link _expr} type to be refactored
     */
    class $refactor<_T extends _expr> implements $refactorBot {

        /** Selects the instances to refactor from the target */
        $expr target$Bot;

        /** with each selection do the "Refactor" */
        Consumer<Select<_T>> refactorAction;

        public static $refactor of(String $target, String $change){
            return of($expr.of($target), $expr.of($change));
        }

        public static $refactor of(_expr _target, _expr _change){
            return of( $expr.of(_target), $expr.of(_change));
        }

        public static $refactor of($expr $target, $expr $change){
            return of($target, (s)->{
                _expr _drafted = (_expr)$change.draft( ((Select)s).tokens);
                _expr _target = (_expr) ((Select)s).selection;
                _target.replace(_drafted);
            });
        }

        public static $refactor of($expr $target, Consumer<Select<? extends _expr>> refactorAction){
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
