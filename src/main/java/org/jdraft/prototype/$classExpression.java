package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ClassExpr;
import org.jdraft.Ex;
import org.jdraft._classExpression;
import org.jdraft._java._domain;
import org.jdraft._typeRef;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class $classExpression implements $prototype.$node<ClassExpr, _classExpression, $classExpression>,
        $selector.$node<_classExpression, $classExpression>,
        $expr<ClassExpr, _classExpression, $classExpression>, Template<_classExpression> {

    public static $classExpression of() {
        return new $classExpression();
    }

    public static $classExpression of(ClassExpr aae) {
        return new $classExpression(aae);
    }

    public static $classExpression of(_classExpression _aa) {
        return new $classExpression(_aa);
    }

    public static $classExpression of(String... code) {
        return new $classExpression(_classExpression.of(code));
    }

    public static $classExpression of(Predicate<_classExpression> matchFn) {
        return new $classExpression(matchFn);
    }

    public Predicate<_classExpression> getPredicate(){
        return this.predicate;
    }

    public boolean isMatchAny() {
        if (this.type.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public _classExpression instance(String... s) {
        return _classExpression.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_domain _j) {
        return _j instanceof _classExpression && matches((_classExpression) _j);
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
        if (n instanceof ClassExpr) {
            return select(_classExpression.of((ClassExpr) n));
        }
        return null;
    }

    public Selected select(_classExpression _aa) {
        if (this.predicate.test(_aa)) {
            $typeRef.Selected s = this.type.select(_aa.getType());
            if (s == null) {
                return null;
            }
            return new Selected(_aa, s.tokens);
        }
        return null;
    }

    public $classExpression $and(Predicate<_classExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $classExpression $not(Predicate<_classExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $classExpression $(String target, String $name) {
        this.type.$(target, $name);
        return this;
    }

    public List<String> list$() {
        return this.type.list$();
    }

    public List<String> list$Normalized() {
        return this.type.list$Normalized();
    }

    public $classExpression $type($typeRef $t) {
        this.type = $t;
        return this;
    }

    public $classExpression $type(_typeRef _e) {
        this.type = $typeRef.of(_e);
        return this;
    }

    public $classExpression $type(String... astE) {
        this.type = $typeRef.of(Text.combine(astE));
        return this;
    }

    @Override
    public _classExpression draft(Translator translator, Map<String, Object> keyValues) {
        _classExpression _e = _classExpression.of();
        _e.setType(this.type.draft(translator, keyValues));
        return _e;
    }

    public _classExpression instance(ClassExpr astNode) {
        return _classExpression.of(astNode);
    }

    public String toString() {
        return "$classExpr{" + System.lineSeparator() + "    " + this.type.toString() + System.lineSeparator() + "}";
    }

    Predicate<_classExpression> predicate = (a) -> true;

    $typeRef type = $typeRef.of();

    public $classExpression() {
    }

    public $classExpression(ClassExpr e) {
        this.type = $typeRef.of(e.getType());
    }

    public $classExpression(_classExpression _e) {
        this.type = $typeRef.of(_e.getType());
    }

    public $classExpression(Predicate<_classExpression> predicate) {
        super();
        $and(predicate);
    }

    public static class Selected extends Select<_classExpression> {

        public Selected(_classExpression _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}