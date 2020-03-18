package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._char;
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
 * syntax prototype for the model of the char types
 *
 * @author Eric
 */
public class $char implements $bot.$node<CharLiteralExpr, _char, $char>,
        $selector.$node<_char, $char>,
        $expression<CharLiteralExpr, _char, $char> {

    public static $char of() {
        return new $char();
    }

    public static $char of(_char _i) {
        return new $char(_i);
    }

    public static $char of(CharLiteralExpr ile) {
        return new $char(_char.of(ile));
    }

    public static $char of(Stencil stencil) {
        return new $char(stencil);
    }

    public static $char of(String... code) {
        return of(_char.of(code));
    }

    public static $char of(char i) {
        return new $char(_char.of(i)).$and(_i -> _i.getValue() == i);
    }

    public static $char of(Predicate<_char> _matchFn) {
        return new $char().$and(_matchFn);
    }

    public Predicate<_char> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $char copy(){
        $char $c = of( this.predicate.and(t->true) );
        $c.stencil = this.stencil.copy();
        return $c;
    }

    @Override
    public $char $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $char setPredicate( Predicate<_char> predicate){
        this.predicate = predicate;
        return this;
    }

    public $char $and(Predicate<_char> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $char $not(Predicate<_char> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_char.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_char.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof CharLiteralExpr) {
            return select(_char.of((CharLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof CharLiteralExpr) {
            return select(_char.of((CharLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _char) {
            return select((_char) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _char) {
            return select((_char) _e);
        }
        return null;
    }

    public Selected select(_char _i) {
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

    public boolean matches(char i) {
        return select(_char.of(i)) != null;
    }

    public _char instance(String... str) {
        return _char.of(str);
    }

    @Override
    public _char draft(Translator translator, Map<String, Object> keyValues) {
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
            _char _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _char instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $char $(String target, String $Name) {
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
            return "$char{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$char{" + this.predicate + "}";
    }

    public Predicate<_char> predicate = d -> true;

    public Stencil stencil = null;

    public $char() {
    }

    public $char(_char _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $char(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_char> {

        public Selected(_char _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
