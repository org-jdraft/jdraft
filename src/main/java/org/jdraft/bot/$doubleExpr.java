package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._doubleExpr;
import org.jdraft._expr;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * syntax prototype for the model of the double types
 *
 * @author Eric
 */
public class $doubleExpr implements $bot.$node<DoubleLiteralExpr, _doubleExpr, $doubleExpr>,
        $selector.$node<_doubleExpr, $doubleExpr>,
        $expr<DoubleLiteralExpr, _doubleExpr, $doubleExpr> {

    public static $doubleExpr of() {
        return new $doubleExpr();
    }

    public static $doubleExpr of(_doubleExpr _i) {
        return new $doubleExpr(_i);
    }

    public static $doubleExpr of(DoubleLiteralExpr ile) {
        return new $doubleExpr(_doubleExpr.of(ile));
    }

    public static $doubleExpr of(Stencil stencil) {
        return new $doubleExpr(stencil);
    }

    public static $doubleExpr of(String... code) {
        return of(_doubleExpr.of(code));
    }

    public static $doubleExpr of(double i) {
        return new $doubleExpr(_doubleExpr.of(i)).$and(_i -> _i.getValue() == i);
    }

    public Predicate<_doubleExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $doubleExpr copy(){
        $doubleExpr $d = of().$and(this.predicate.and(t->true) );
        $d.stencil = this.stencil.copy();
        return $d;
    }

    @Override
    public $doubleExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $doubleExpr setPredicate(Predicate<_doubleExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $doubleExpr $and(Predicate<_doubleExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $doubleExpr $not( $doubleExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $doubleExpr $not(Predicate<_doubleExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_doubleExpr> select(String code) {
        try {
            return select(_doubleExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_doubleExpr> select(String... code) {
        try {
            return select(_doubleExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_doubleExpr> select(Node n) {
        if (n instanceof DoubleLiteralExpr) {
            return select(_doubleExpr.of((DoubleLiteralExpr) n));
        }
        return null;
    }

    public Select<_doubleExpr> select(Expression e) {
        if (e instanceof DoubleLiteralExpr) {
            return select(_doubleExpr.of((DoubleLiteralExpr) e));
        }
        return null;
    }

    public Select<_doubleExpr> select(_domain _n) {
        if (_n instanceof _doubleExpr) {
            return select((_doubleExpr) _n);
        }
        return null;
    }

    public Select<_doubleExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _doubleExpr) {
            return select((_doubleExpr) _e);
        }
        return null;
    }

    public Select<_doubleExpr> select(_doubleExpr _i) {
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

    public boolean matches(double i) {
        return select(_doubleExpr.of(i)) != null;
    }

    public _doubleExpr instance(String... str) {
        return _doubleExpr.of(str);
    }

    @Override
    public _doubleExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _doubleExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _doubleExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $doubleExpr $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public Template<_doubleExpr> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
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
            return "$double{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$double{" + this.predicate + "}";
    }

    public Predicate<_doubleExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $doubleExpr() {
    }

    public $doubleExpr(_doubleExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $doubleExpr(Stencil stencil) {
        this.stencil = stencil;
    }
}
