package org.jdraft.bot;

import com.github.javaparser.ast.expr.*;
import org.jdraft.Expressions;
import org.jdraft._expression;

import org.jdraft.text.Stencil;
import org.jdraft.text.Template;

/**
 * base for all expression based bots
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

    //$E $hardcode(Translator t, Tokens ts);

}
