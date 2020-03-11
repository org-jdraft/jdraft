package org.jdraft.bot;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.Stmt;
import org.jdraft._expression;
import org.jdraft._statement;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;

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
        return of( Stmt.of(code));
    }

    static $statement of(String... code) {
        return of( Stmt.of(code));
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

    static<$S extends $statement> $S of(Statement ile) {

        /*
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
         */
        return ($S)$s.of( ile.toString() );
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
}
