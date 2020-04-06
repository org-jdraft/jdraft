package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft._long;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * syntax prototype for the model of the long types
 *
 * @author Eric
 */
public class $long implements $bot.$node<LongLiteralExpr, _long, $long>,
        $selector.$node<_long, $long>,
        $expression<LongLiteralExpr, _long, $long> {

    public static $long of() {
        return new $long();
    }

    public static $long of(_long _i) {
        return new $long(_i);
    }

    public static $long of(LongLiteralExpr ile) {
        return new $long(_long.of(ile));
    }

    public static $long of(Stencil stencil) {
        return new $long(stencil);
    }

    public static $long of(String... code) {
        return of(_long.of(code));
    }

    public static $long of(long i) {
        return new $long(_long.of(i)).$and(_i -> _i.getValue() == i);
    }

    /*
    public static $long of(Predicate<_long> _matchFn) {
        return new $long().$and(_matchFn);
    }
     */

    public Predicate<_long> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this $bot
     * @return
     */
    public $long copy(){
        $long $l = of( ).$and(this.predicate.and(t->true) );
        $l.stencil = this.stencil.copy();
        return $l;
    }

    @Override
    public $long $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $long setPredicate( Predicate<_long> predicate){
        this.predicate = predicate;
        return this;
    }

    public $long $and(Predicate<_long> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $long $not(Predicate<_long> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_long> select(String code) {
        try {
            return select(_long.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_long> select(String... code) {
        try {
            return select(_long.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_long> select(Node n) {
        if (n instanceof LongLiteralExpr) {
            return select(_long.of((LongLiteralExpr) n));
        }
        return null;
    }

    public Select<_long> select(Expression e) {
        if (e instanceof LongLiteralExpr) {
            return select(_long.of((LongLiteralExpr) e));
        }
        return null;
    }

    public Select<_long> select(_domain _n) {
        if (_n instanceof _long) {
            return select((_long) _n);
        }
        return null;
    }

    public Select<_long> select(_expression<?, ?> _e) {
        if (_e instanceof _long) {
            return select((_long) _e);
        }
        return null;
    }

    public Select<_long> select(_long _i) {
        if (predicate.test(_i)) {
            if (stencil == null) {
                return new Select<>(_i, new Tokens());
            }
            Tokens ts = stencil.parse(_i.toString());
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public boolean matches(long i) {
        return select(_long.of(i)) != null;
    }

    public _long instance(String... str) {
        return _long.of(str);
    }

    @Override
    public _long draft(Translator translator, Map<String, Object> keyValues) {
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
            _long _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _long instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $long $(String target, String $Name) {
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
            return "$long{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$long{" + this.predicate + "}";
    }

    public Predicate<_long> predicate = d -> true;

    public Stencil stencil = null;

    public $long() {
    }

    public $long(_long _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $long(Stencil stencil) {
        this.stencil = stencil;
    }
}
