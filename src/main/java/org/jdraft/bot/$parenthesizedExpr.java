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
import java.util.stream.Stream;

public class $parenthesizedExpr implements $bot.$node<EnclosedExpr, _parenthesizedExpr, $parenthesizedExpr>,
        $selector.$node<_parenthesizedExpr, $parenthesizedExpr>,
        $expr<EnclosedExpr, _parenthesizedExpr, $parenthesizedExpr>, Template<_parenthesizedExpr> {

    public static $parenthesizedExpr of() {
        return new $parenthesizedExpr();
    }

    public static $parenthesizedExpr of(EnclosedExpr aae) {
        return new $parenthesizedExpr(aae);
    }

    public static $parenthesizedExpr of(_parenthesizedExpr _aa) {
        return new $parenthesizedExpr(_aa);
    }

    public static $parenthesizedExpr of(String... code) {
        return new $parenthesizedExpr(_parenthesizedExpr.of(code));
    }

    public Predicate<_parenthesizedExpr> getPredicate(){
        return this.predicate;
    }

    public $parenthesizedExpr setPredicate(Predicate<_parenthesizedExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    @Override
    public $parenthesizedExpr $hardcode(Translator translator, Tokens kvs) {
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

    public _parenthesizedExpr instance(String... s) {
        return _parenthesizedExpr.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _parenthesizedExpr && matches((_parenthesizedExpr) _j);
    }

    public Select<_parenthesizedExpr> select(String... str) {
        return select(Text.combine(str));
    }

    public Select<_parenthesizedExpr> select(String str) {
        try {
            return select(Exprs.classExpr(str));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_parenthesizedExpr> select(Node n) {
        if (n instanceof EnclosedExpr) {
            return select(_parenthesizedExpr.of((EnclosedExpr) n));
        }
        return null;
    }

    public Select<_parenthesizedExpr> select(_parenthesizedExpr _aa) {
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
    public $parenthesizedExpr copy(){
        $parenthesizedExpr $e = of().$and(this.predicate.and(t->true) );
        $e.expression = ($expr)this.expression.copy();
        return $e;
    }

    public $parenthesizedExpr $and(Predicate<_parenthesizedExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $parenthesizedExpr $not( $parenthesizedExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $parenthesizedExpr $not(Predicate<_parenthesizedExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $parenthesizedExpr $(String target, String $name) {
        this.expression.$(target, $name);
        return this;
    }

    public List<String> $list() {
        return this.expression.$list();
    }

    public List<String> $listNormalized() {
        return this.expression.$listNormalized();
    }

    public $parenthesizedExpr $expression($expr $t) {
        this.expression = $t;
        return this;
    }

    public $parenthesizedExpr $expression(_expr _e) {
        this.expression = $expr.of(_e);
        return this;
    }

    public $parenthesizedExpr $expression(String... astE) {
        this.expression = $expr.of(Text.combine(astE));
        return this;
    }

    @Override
    public _parenthesizedExpr draft(Translator translator, Map<String, Object> keyValues) {
        _parenthesizedExpr _e = _parenthesizedExpr.of();
        _e.setExpression((_expr)this.expression.draft(translator, keyValues));
        return _e;
    }

    public _parenthesizedExpr instance(EnclosedExpr astNode) {
        return _parenthesizedExpr.of(astNode);
    }

    public String toString() {
        return "$enclosedExpr{" + System.lineSeparator() + "    " + this.expression.toString() + System.lineSeparator() + "}";
    }

    Predicate<_parenthesizedExpr> predicate = (a) -> true;

    $expr expression = $expr.of();

    public $parenthesizedExpr() {
    }

    public $parenthesizedExpr(EnclosedExpr e) {
        this.expression = $expr.of(e.getInner());
    }

    public $parenthesizedExpr(_parenthesizedExpr _e) {
        this.expression = $expr.of(_e.getExpression());
    }

}
