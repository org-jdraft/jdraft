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

public class $enclosedExpr implements $bot.$node<EnclosedExpr, _enclosedEx, $enclosedExpr>,
        $selector.$node<_enclosedEx, $enclosedExpr>,
        $expr<EnclosedExpr, _enclosedEx, $enclosedExpr>, Template<_enclosedEx> {

    public static $enclosedExpr of() {
        return new $enclosedExpr();
    }

    public static $enclosedExpr of(EnclosedExpr aae) {
        return new $enclosedExpr(aae);
    }

    public static $enclosedExpr of(_enclosedEx _aa) {
        return new $enclosedExpr(_aa);
    }

    public static $enclosedExpr of(String... code) {
        return new $enclosedExpr(_enclosedEx.of(code));
    }

    public Predicate<_enclosedEx> getPredicate(){
        return this.predicate;
    }

    public $enclosedExpr setPredicate(Predicate<_enclosedEx> predicate){
        this.predicate = predicate;
        return this;
    }

    @Override
    public $enclosedExpr $hardcode(Translator translator, Tokens kvs) {
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

    public _enclosedEx instance(String... s) {
        return _enclosedEx.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _enclosedEx && matches((_enclosedEx) _j);
    }

    public Select<_enclosedEx> select(String... str) {
        return select(Text.combine(str));
    }

    public Select<_enclosedEx> select(String str) {
        try {
            return select(Exprs.classEx(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_enclosedEx> select(Node n) {
        if (n instanceof EnclosedExpr) {
            return select(_enclosedEx.of((EnclosedExpr) n));
        }
        return null;
    }

    public Select<_enclosedEx> select(_enclosedEx _aa) {
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
    public $enclosedExpr copy(){
        $enclosedExpr $e = of().$and(this.predicate.and(t->true) );
        $e.expression = ($expr)this.expression.copy();
        return $e;
    }

    public $enclosedExpr $and(Predicate<_enclosedEx> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $enclosedExpr $not(Predicate<_enclosedEx> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $enclosedExpr $(String target, String $name) {
        this.expression.$(target, $name);
        return this;
    }

    public List<String> $list() {
        return this.expression.$list();
    }

    public List<String> $listNormalized() {
        return this.expression.$listNormalized();
    }

    public $enclosedExpr $expression($expr $t) {
        this.expression = $t;
        return this;
    }

    public $enclosedExpr $expression(_expr _e) {
        this.expression = $expr.of(_e);
        return this;
    }

    public $enclosedExpr $expression(String... astE) {
        this.expression = $expr.of(Text.combine(astE));
        return this;
    }

    @Override
    public _enclosedEx draft(Translator translator, Map<String, Object> keyValues) {
        _enclosedEx _e = _enclosedEx.of();
        _e.setExpression((_expr)this.expression.draft(translator, keyValues));
        return _e;
    }

    public _enclosedEx instance(EnclosedExpr astNode) {
        return _enclosedEx.of(astNode);
    }

    public String toString() {
        return "$enclosedExpr{" + System.lineSeparator() + "    " + this.expression.toString() + System.lineSeparator() + "}";
    }

    Predicate<_enclosedEx> predicate = (a) -> true;

    $expr expression = $expr.of();

    public $enclosedExpr() {
    }

    public $enclosedExpr(EnclosedExpr e) {
        this.expression = $expr.of(e.getInner());
    }

    public $enclosedExpr(_enclosedEx _e) {
        this.expression = $expr.of(_e.getExpression());
    }

}
