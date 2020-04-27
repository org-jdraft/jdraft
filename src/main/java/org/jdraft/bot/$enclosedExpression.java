package org.jdraft.bot;

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

public class $enclosedExpression implements $bot.$node<EnclosedExpr, _enclosedExpression, $enclosedExpression>,
        $selector.$node<_enclosedExpression, $enclosedExpression>,
        $expression<EnclosedExpr, _enclosedExpression, $enclosedExpression>, Template<_enclosedExpression> {

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

    public Predicate<_enclosedExpression> getPredicate(){
        return this.predicate;
    }

    public $enclosedExpression setPredicate( Predicate<_enclosedExpression> predicate){
        this.predicate = predicate;
        return this;
    }

    @Override
    public $enclosedExpression $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
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

    public Select<_enclosedExpression> select(String... str) {
        return select(Text.combine(str));
    }

    public Select<_enclosedExpression> select(String str) {
        try {
            return select(Expressions.classEx(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_enclosedExpression> select(Node n) {
        if (n instanceof EnclosedExpr) {
            return select(_enclosedExpression.of((EnclosedExpr) n));
        }
        return null;
    }

    public Select<_enclosedExpression> select(_enclosedExpression _aa) {
        if (this.predicate.test(_aa)) {
            Select s = this.expression.select(_aa.getExpression());
            if (s == null) {
                return null;
            }
            return new Select<>(_aa, s.tokens);
        }
        return null;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $enclosedExpression copy(){
        $enclosedExpression $e = of().$and(this.predicate.and(t->true) );
        $e.expression = ($expression)this.expression.copy();
        return $e;
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

    public List<String> $list() {
        return this.expression.$list();
    }

    public List<String> $listNormalized() {
        return this.expression.$listNormalized();
    }

    public $enclosedExpression $expression($expression $t) {
        this.expression = $t;
        return this;
    }

    public $enclosedExpression $expression(_expression _e) {
        this.expression = $expression.of(_e);
        return this;
    }

    public $enclosedExpression $expression(String... astE) {
        this.expression = $expression.of(Text.combine(astE));
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

    $expression expression = $expression.of();

    public $enclosedExpression() {
    }

    public $enclosedExpression(EnclosedExpr e) {
        this.expression = $expression.of(e.getInner());
    }

    public $enclosedExpression(_enclosedExpression _e) {
        this.expression = $expression.of(_e.getExpression());
    }

}
