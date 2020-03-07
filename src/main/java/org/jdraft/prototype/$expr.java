package org.jdraft.prototype;

import com.github.javaparser.ast.expr.Expression;
import org.jdraft._expression;

import org.jdraft.text.Template;

public interface $expr<E extends Expression, _E extends _expression, $E extends $prototype.$node<E, _E, $E>>
    extends $prototype.$node<E, _E, $E>, $selector.$node<_E, $E>, Template<_E> {

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
