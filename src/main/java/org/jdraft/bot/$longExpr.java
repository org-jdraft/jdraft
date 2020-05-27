package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import org.jdraft._expr;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft._longExpr;
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
 * syntax prototype for the model of the long types
 *
 * @author Eric
 */
public class $longExpr implements $bot.$node<LongLiteralExpr, _longExpr, $longExpr>,
        $selector.$node<_longExpr, $longExpr>,
        $expr<LongLiteralExpr, _longExpr, $longExpr> {

    public static $longExpr of() {
        return new $longExpr();
    }

    public static $longExpr of(_longExpr _i) {
        return new $longExpr(_i);
    }

    public static $longExpr of(LongLiteralExpr ile) {
        return new $longExpr(_longExpr.of(ile));
    }

    public static $longExpr of(Stencil stencil) {
        return new $longExpr(stencil);
    }

    public static $longExpr of(String... code) {
        return of(_longExpr.of(code));
    }

    public static $longExpr of(long i) {
        return new $longExpr(_longExpr.of(i)).$and(_i -> _i.getValue() == i);
    }

    public Predicate<_longExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this $bot
     * @return
     */
    public $longExpr copy(){
        $longExpr $l = of( ).$and(this.predicate.and(t->true) );
        $l.stencil = this.stencil.copy();
        return $l;
    }

    @Override
    public $longExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $longExpr setPredicate(Predicate<_longExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $longExpr $and(Predicate<_longExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $longExpr $not( $longExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $longExpr $not(Predicate<_longExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_longExpr> select(String code) {
        try {
            return select(_longExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_longExpr> select(String... code) {
        try {
            return select(_longExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_longExpr> select(Node n) {
        if (n instanceof LongLiteralExpr) {
            return select(_longExpr.of((LongLiteralExpr) n));
        }
        return null;
    }

    public Select<_longExpr> select(Expression e) {
        if (e instanceof LongLiteralExpr) {
            return select(_longExpr.of((LongLiteralExpr) e));
        }
        return null;
    }

    public Select<_longExpr> select(_domain _n) {
        if (_n instanceof _longExpr) {
            return select((_longExpr) _n);
        }
        return null;
    }

    public Select<_longExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _longExpr) {
            return select((_longExpr) _e);
        }
        return null;
    }

    public Select<_longExpr> select(_longExpr _i) {
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
        return select(_longExpr.of(i)) != null;
    }

    public _longExpr instance(String... str) {
        return _longExpr.of(str);
    }

    @Override
    public _longExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _longExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _longExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $longExpr $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public $longExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
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
            return "$long{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$long{" + this.predicate + "}";
    }

    public Predicate<_longExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $longExpr() {
    }

    public $longExpr(_longExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $longExpr(Stencil stencil) {
        this.stencil = stencil;
    }
}
