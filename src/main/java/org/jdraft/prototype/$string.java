package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft._string;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * syntax prototype for the model of the String types
 *
 * @author Eric
 */
public class $string implements $prototype.$node<StringLiteralExpr, _string, $string>,
        $selector.$node<_string, $string>,
        $expr<StringLiteralExpr, _string, $string> {

    public static $string of() {
        return new $string();
    }

    public static $string of(_string _i) {
        return new $string(_i);
    }

    public static $string of(StringLiteralExpr ile) {
        return new $string(_string.of(ile));
    }

    public static $string of(Stencil stencil) {
        return new $string(stencil);
    }

    public static $string of(String... code) {
        return of(_string.of(code));
    }

    public static $string of(String i) {
        return new $string(_string.of(i)).$and(_i -> _i.getValue() == i);
    }

    public static $string of(Predicate<_string> _matchFn) {
        return new $string().$and(_matchFn);
    }

    public Predicate<_string> getPredicate(){
        return this.predicate;
    }

    public $string $and(Predicate<_string> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $string $not(Predicate<_string> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_string.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_string.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof StringLiteralExpr) {
            return select(_string.of((StringLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof StringLiteralExpr) {
            return select(_string.of((StringLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _string) {
            return select((_string) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _string) {
            return select((_string) _e);
        }
        return null;
    }

    public Selected select(_string _i) {
        if (predicate.test(_i)) {
            if (stencil == null) {
                return new Selected(_i, new Tokens());
            }
            Tokens ts = stencil.parse(_i.toString());
            if (ts != null) {
                return new Selected(_i, ts);
            }
            return null;
        }
        return null;
    }

    public boolean matches(String i) {
        return select(_string.of(i)) != null;
    }

    public _string instance(String... str) {
        return _string.of(str);
    }

    @Override
    public _string draft(Translator translator, Map<String, Object> keyValues) {
        if (this.stencil == null) {
            String overrideName = this.getClass().getSimpleName();
            Object override = keyValues.get(overrideName);
            if (override == null) {
                throw new _jdraftException("no stencil specified for " + this + " ...and no override Stencil/String \"" + overrideName + "\" provided");
            }
            Stencil stencil = null;
            if (override instanceof String) {
                stencil = Stencil.of((String) override);
            } else if (override instanceof Stencil) {
                stencil = (Stencil) override;
            } else {
                stencil = Stencil.of(override.toString());
            }
            String drafted = stencil.draft(translator, keyValues);
            _string _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _string instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $string $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        if (this.stencil != null) {
            return this.stencil.list$();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> list$Normalized() {
        if (this.stencil != null) {
            return this.stencil.list$Normalized();
        }
        return new ArrayList<>();
    }

    public boolean isMatchAny() {
        if (this.stencil == null || this.stencil.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        if (this.stencil != null) {
            return "$String{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$String{" + this.predicate + "}";
    }

    public Predicate<_string> predicate = d -> true;

    public Stencil stencil = null;

    public $string() {
    }

    public $string(_string _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $string(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_string> {

        public Selected(_string _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
