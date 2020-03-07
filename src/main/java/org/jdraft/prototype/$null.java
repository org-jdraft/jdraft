package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import org.jdraft.*;
import org.jdraft._java._domain;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * syntax prototype for the model of the int types
 *
 * @author Eric
 */
public class $null implements $prototype.$node<NullLiteralExpr, _null, $null>,
        $selector.$node<_null, $null>,
        $expr<NullLiteralExpr, _null, $null> {

    public static $null of() {
        return new $null();
    }

    public static $null of(_null _i) {
        return new $null(_i);
    }

    public static $null of(NullLiteralExpr ile) {
        return new $null(_null.of(ile));
    }

    public static $null of(Stencil stencil) {
        return new $null(stencil);
    }

    public static $null of(String... code) {
        return of(_null.of(code));
    }

    public static $null of(Predicate<_null> _matchFn) {
        return new $null().$and(_matchFn);
    }

    public Predicate<_null> getPredicate(){
        return this.predicate;
    }

    public $null $and(Predicate<_null> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $null $not(Predicate<_null> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_null.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_null.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof NullLiteralExpr) {
            return select(_null.of((NullLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof NullLiteralExpr) {
            return select(_null.of((NullLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _null) {
            return select((_null) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _null) {
            return select((_null) _e);
        }
        return null;
    }

    public Selected select(_null _i) {
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

    public boolean matches(int i) {
        return select(_int.of(i)) != null;
    }

    public _null instance(String... str) {
        return _null.of(str);
    }

    @Override
    public _null draft(Translator translator, Map<String, Object> keyValues) {
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
            _null _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _null instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $null $(String target, String $Name) {
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
            return "$null{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$null{" + this.predicate + "}";
    }

    public Predicate<_null> predicate = d -> true;

    public Stencil stencil = null;

    public $null() {
    }

    public $null(_null _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $null(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_null> {

        public Selected(_null _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
