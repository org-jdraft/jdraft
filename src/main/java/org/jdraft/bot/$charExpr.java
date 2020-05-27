package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._charExpr;
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
 * syntax prototype for the model of the char types
 *
 * @author Eric
 */
public class $charExpr  implements $bot.$node<CharLiteralExpr, _charExpr, $charExpr>,
        $selector.$node<_charExpr, $charExpr>,
        $expr<CharLiteralExpr, _charExpr, $charExpr> {

    public static $charExpr of() {
        return new $charExpr();
    }

    public static $charExpr of(_charExpr _i) {
        return new $charExpr(_i);
    }

    public static $charExpr of(CharLiteralExpr ile) {
        return new $charExpr(_charExpr.of(ile));
    }

    public static $charExpr of(Stencil stencil) {
        return new $charExpr(stencil);
    }

    public static $charExpr of(String... code) {
        return of(_charExpr.of(code));
    }

    public static $charExpr of(char i) {
        return new $charExpr(_charExpr.of(i)).$and(_i -> _i.getValue() == i);
    }

    public Predicate<_charExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $charExpr copy(){
        $charExpr $c = of().$and(this.predicate.and(t->true) );
        $c.stencil = this.stencil.copy();
        return $c;
    }

    @Override
    public $charExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $charExpr setPredicate(Predicate<_charExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $charExpr $and(Predicate<_charExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $charExpr $not( $charExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $charExpr $not(Predicate<_charExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_charExpr> select(String code) {
        try {
            return select(_charExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_charExpr> select(String... code) {
        try {
            return select(_charExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_charExpr> select(Node n) {
        if (n instanceof CharLiteralExpr) {
            return select(_charExpr.of((CharLiteralExpr) n));
        }
        return null;
    }

    public Select<_charExpr> select(Expression e) {
        if (e instanceof CharLiteralExpr) {
            return select(_charExpr.of((CharLiteralExpr) e));
        }
        return null;
    }

    public Select<_charExpr> select(_domain _n) {
        if (_n instanceof _charExpr) {
            return select((_charExpr) _n);
        }
        return null;
    }

    public Select<_charExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _charExpr) {
            return select((_charExpr) _e);
        }
        return null;
    }

    public Select<_charExpr> select(_charExpr _i) {
        if (predicate.test(_i)) {
            if (stencil == null) {
                return new Select<_charExpr>(_i, new Tokens());
            }
            Tokens ts = stencil.parse(_i.toString());
            if (ts != null) {
                return new Select<_charExpr>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public boolean matches(char i) {
        return select(_charExpr.of(i)) != null;
    }

    public _charExpr instance(String... str) {
        return _charExpr.of(str);
    }

    @Override
    public _charExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _charExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _charExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $charExpr $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public Template<_charExpr> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
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
            return "$char{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$char{" + this.predicate + "}";
    }

    public Predicate<_charExpr> predicate = c -> true;

    public Stencil stencil = null;

    public $charExpr() { }

    public $charExpr(_charExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $charExpr(Stencil stencil) {
        this.stencil = stencil;
    }

}
