package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._double;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * syntax prototype for the model of the double types
 *
 * @author Eric
 */
public class $double implements $prototype.$node<DoubleLiteralExpr, _double, $double>,
        $selector.$node<_double, $double>,
        $expr<DoubleLiteralExpr, _double, $double> {

    public static $double of() {
        return new $double();
    }

    public static $double of(_double _i) {
        return new $double(_i);
    }

    public static $double of(DoubleLiteralExpr ile) {
        return new $double(_double.of(ile));
    }

    public static $double of(Stencil stencil) {
        return new $double(stencil);
    }

    public static $double of(String... code) {
        return of(_double.of(code));
    }

    public static $double of(double i) {
        return new $double(_double.of(i)).$and(_i -> _i.getValue() == i);
    }

    public static $double of(Predicate<_double> _matchFn) {
        return new $double().$and(_matchFn);
    }

    public Predicate<_double> getPredicate(){
        return this.predicate;
    }

    public $double $and(Predicate<_double> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $double $not(Predicate<_double> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_double.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_double.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof DoubleLiteralExpr) {
            return select(_double.of((DoubleLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof DoubleLiteralExpr) {
            return select(_double.of((DoubleLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _double) {
            return select((_double) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _double) {
            return select((_double) _e);
        }
        return null;
    }

    public Selected select(_double _i) {
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

    public boolean matches(double i) {
        return select(_double.of(i)) != null;
    }

    public _double instance(String... str) {
        return _double.of(str);
    }

    @Override
    public _double draft(Translator translator, Map<String, Object> keyValues) {
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
            _double _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _double instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $double $(String target, String $Name) {
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
            return "$double{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$double{" + this.predicate + "}";
    }

    public Predicate<_double> predicate = d -> true;

    public Stencil stencil = null;

    public $double() {
    }

    public $double(_double _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $double(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_double> {

        public Selected(_double _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
