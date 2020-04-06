package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft._textBlock;
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
public class $textBlock implements $bot.$node<TextBlockLiteralExpr, _textBlock, $textBlock>,
        $selector.$node<_textBlock, $textBlock>,
        $expression<TextBlockLiteralExpr, _textBlock, $textBlock> {

    public static $textBlock of() {
        return new $textBlock();
    }

    public static $textBlock of(_textBlock _i) {
        return new $textBlock(_i);
    }

    public static $textBlock of(TextBlockLiteralExpr ile) {
        return new $textBlock(_textBlock.of(ile));
    }

    public static $textBlock of(Stencil stencil) {
        return new $textBlock(stencil);
    }

    public static $textBlock of(String... code) {
        return of(_textBlock.of(code));
    }

    public static $textBlock of(String i) {
        return new $textBlock(_textBlock.of(i)).$and(_i -> _i.getValue() == i);
    }

    /*
    public static $textBlock of(Predicate<_textBlock> _matchFn) {
        return new $textBlock().$and(_matchFn);
    }
     */

    public Predicate<_textBlock> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $textBlock copy(){
        $textBlock $t = of( ).$and(this.predicate.and(t->true) );
        if( this.stencil != null ) {
            $t.stencil = this.stencil.copy();
        }
        return $t;
    }

    @Override
    public $textBlock $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $textBlock setPredicate( Predicate<_textBlock> predicate){
        this.predicate = predicate;
        return this;
    }

    public $textBlock $and(Predicate<_textBlock> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $textBlock $not(Predicate<_textBlock> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_textBlock> select(String code) {
        try {
            return select(_textBlock.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_textBlock> select(String... code) {
        try {
            return select(_textBlock.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_textBlock> select(Node n) {
        if (n instanceof TextBlockLiteralExpr) {
            return select(_textBlock.of((TextBlockLiteralExpr) n));
        }
        return null;
    }

    public Select<_textBlock> select(Expression e) {
        if (e instanceof TextBlockLiteralExpr) {
            return select(_textBlock.of((TextBlockLiteralExpr) e));
        }
        return null;
    }

    public Select<_textBlock> select(_domain _n) {
        if (_n instanceof _textBlock) {
            return select((_textBlock) _n);
        }
        return null;
    }

    public Select<_textBlock> select(_expression<?, ?> _e) {
        if (_e instanceof _textBlock) {
            return select((_textBlock) _e);
        }
        return null;
    }

    public Select<_textBlock> select(_textBlock _i) {
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

    public boolean matches(String i) {
        return select(_textBlock.of(i)) != null;
    }

    public _textBlock instance(String... str) {
        return _textBlock.of(str);
    }

    @Override
    public _textBlock draft(Translator translator, Map<String, Object> keyValues) {
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
            _textBlock _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _textBlock instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $textBlock $(String target, String $Name) {
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
            return "$String{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$String{" + this.predicate + "}";
    }

    public Predicate<_textBlock> predicate = d -> true;

    public Stencil stencil = null;

    public $textBlock() {
    }

    public $textBlock(_textBlock _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $textBlock(Stencil stencil) {
        this.stencil = stencil;
    }

}
