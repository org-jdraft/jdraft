package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.jdraft._expression;
import org.jdraft._int;
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
 * syntax prototype for the model of the int types
 *
 * @author Eric
 */
public class $int implements $bot.$node<IntegerLiteralExpr, _int, $int>,
        $selector.$node<_int, $int>,
        $expression<IntegerLiteralExpr, _int, $int> {

    public static $int of() {
        return new $int();
    }

    public static $int of(_int _i) {
        return new $int(_i);
    }

    public static $int of(IntegerLiteralExpr ile) {
        return new $int(_int.of(ile));
    }

    public static $int of(Stencil stencil) {
        return new $int(stencil);
    }

    public static $int of(String... code) {
        return of(_int.of(code));
    }

    public static $int of(int i) {
        return new $int(_int.of(i)).$and(_i -> _i.getValue() == i);
    }

    public static $int of(Predicate<_int> _matchFn) {
        return new $int().$and(_matchFn);
    }

    public Predicate<_int> getPredicate(){
        return this.predicate;
    }

    public $int setPredicate( Predicate<_int> predicate){
        this.predicate = predicate;
        return this;
    }

    public $int $and(Predicate<_int> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $int $not(Predicate<_int> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_int.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_int.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof IntegerLiteralExpr) {
            return select(_int.of((IntegerLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof IntegerLiteralExpr) {
            return select(_int.of((IntegerLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _int) {
            return select((_int) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _int) {
            return select((_int) _e);
        }
        return null;
    }

    public Selected select(_int _i) {
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

    public _int instance(String... str) {
        return _int.of(str);
    }

    @Override
    public _int draft(Translator translator, Map<String, Object> keyValues) {
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
            _int _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _int instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $int $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> $list() {
        if (this.stencil != null) {
            return this.stencil.$list();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        if (this.stencil != null) {
            return this.stencil.$listNormalized();
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
            return "$int{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$int{" + this.predicate + "}";
    }

    public Predicate<_int> predicate = d -> true;

    public Stencil stencil = null;

    public $int() {
    }

    public $int(_int _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $int(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_int> {

        public Selected(_int _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
