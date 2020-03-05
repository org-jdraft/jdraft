package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.EnclosedExpr;
import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class $enclosedExpression implements $proto<EnclosedExpr, _enclosedExpression, $enclosedExpression>,
        $selector.$node<_enclosedExpression, $enclosedExpression>,
        $expr<EnclosedExpr, _enclosedExpression, $enclosedExpression>, Template<_enclosedExpression> {

    public static $enclosedExpression of() {
        return new $enclosedExpression();
    }

    public static $enclosedExpression of(EnclosedExpr aae) {
        return new $enclosedExpression(aae);
    }

    public static $enclosedExpression of(_enclosedExpression _aa) {
        return new $enclosedExpression(_aa);
    }

    public static $enclosedExpression of(String... code) {
        return new $enclosedExpression(_enclosedExpression.of(code));
    }

    public static $enclosedExpression of(Predicate<_enclosedExpression> matchFn) {
        return new $enclosedExpression(matchFn);
    }

    public Predicate<_enclosedExpression> getPredicate(){
        return this.predicate;
    }

    public boolean isMatchAny() {
        if (this.expression.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public _enclosedExpression instance(String... s) {
        return _enclosedExpression.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _enclosedExpression && matches((_enclosedExpression) _j);
    }

    public Selected select(String... str) {
        return select(Text.combine(str));
    }

    public Selected select(String str) {
        try {
            return select(Ex.classEx(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof EnclosedExpr) {
            return select(_enclosedExpression.of((EnclosedExpr) n));
        }
        return null;
    }

    public Selected select(_enclosedExpression _aa) {
        if (this.predicate.test(_aa)) {
            Select s = this.expression.select(_aa.getExpression());
            if (s == null) {
                return null;
            }
            return new Selected(_aa, s.tokens);
        }
        return null;
    }

    public $enclosedExpression $and(Predicate<_enclosedExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $enclosedExpression $not(Predicate<_enclosedExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $enclosedExpression $(String target, String $name) {
        this.expression.$(target, $name);
        return this;
    }

    public List<String> list$() {
        return this.expression.list$();
    }

    public List<String> list$Normalized() {
        return this.expression.list$Normalized();
    }

    public $enclosedExpression $type($expr $t) {
        this.expression = $t;
        return this;
    }

    public $enclosedExpression $type(_expression _e) {
        this.expression = $e.of(_e);
        return this;
    }

    public $enclosedExpression $type(String... astE) {
        this.expression = $e.of(Text.combine(astE));
        return this;
    }

    @Override
    public _enclosedExpression draft(Translator translator, Map<String, Object> keyValues) {
        _enclosedExpression _e = _enclosedExpression.of();
        _e.setExpression((_expression)this.expression.draft(translator, keyValues));
        return _e;
    }

    public _enclosedExpression instance(EnclosedExpr astNode) {
        return _enclosedExpression.of(astNode);
    }

    public String toString() {
        return "$enclosedExpr{" + System.lineSeparator() + "    " + this.expression.toString() + System.lineSeparator() + "}";
    }

    Predicate<_enclosedExpression> predicate = (a) -> true;

    $expr expression = $e.of();

    public $enclosedExpression() {
    }

    public $enclosedExpression(EnclosedExpr e) {
        this.expression = $e.of(e.getInner());
    }

    public $enclosedExpression(_enclosedExpression _e) {
        this.expression = $e.of(_e.getExpression());
    }

    public $enclosedExpression(Predicate<_enclosedExpression> predicate) {
        super();
        $and(predicate);
    }

    public static class Selected extends Select<_enclosedExpression> {

        public Selected(_enclosedExpression _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
