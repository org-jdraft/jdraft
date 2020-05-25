package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.jdraft.Print;
import org.jdraft._expr;
import org.jdraft._intExpr;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * syntax prototype for the model of the int types
 *
 * @author Eric
 */
public class $intExpr implements $bot.$node<IntegerLiteralExpr, _intExpr, $intExpr>,
        $selector.$node<_intExpr, $intExpr>,
        $expr<IntegerLiteralExpr, _intExpr, $intExpr> {

    public static $intExpr of() {
        return new $intExpr();
    }

    public static $intExpr of(_intExpr _i) {
        return new $intExpr(_i);
    }

    public static $intExpr of(IntegerLiteralExpr ile) {
        return new $intExpr(_intExpr.of(ile));
    }

    public static $intExpr of(Stencil stencil) {
        return new $intExpr(stencil);
    }

    public static $intExpr of(String... code) {
        return of(_intExpr.of(code));
    }

    public static $intExpr of(int i) {
        return new $intExpr(_intExpr.of(i)).$and(_i -> _i.getValue() == i);
    }

    @Override
    public $intExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $intExpr copy(){
        $intExpr $i = of().$and(this.predicate.and(t->true) );
        $i.stencil = this.stencil.copy();
        return $i;
    }

    public Predicate<_intExpr> getPredicate(){
        return this.predicate;
    }

    public $intExpr setPredicate(Predicate<_intExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $intExpr $and(Predicate<_intExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $intExpr $not( $intExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $intExpr $not(Predicate<_intExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_intExpr> select(String code) {
        try {
            return select(_intExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_intExpr> select(String... code) {
        try {
            return select(_intExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_intExpr> select(Node n) {
        if (n instanceof IntegerLiteralExpr) {
            return select(_intExpr.of((IntegerLiteralExpr) n));
        }
        return null;
    }

    public Select<_intExpr> select(Expression e) {
        if (e instanceof IntegerLiteralExpr) {
            return select(_intExpr.of((IntegerLiteralExpr) e));
        }
        return null;
    }

    public Select<_intExpr> select(_domain _n) {
        if (_n instanceof _intExpr) {
            return select((_intExpr) _n);
        }
        return null;
    }

    public Select<_intExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _intExpr) {
            return select((_intExpr) _e);
        }
        return null;
    }

    public Select<_intExpr> select(_intExpr _i) {
        if (predicate.test(_i)) {
            if (stencil == null) {
                return new Select<>(_i, new Tokens());
            }
            Tokens ts = null;
            if( _i.ast().getComment().isPresent() ){
                //each bot should have a $comment, so if I require a comment I can check here
                String str = _i.ast().getComment().get().getCommentedNode().get().toString(Print.PRINT_NO_COMMENTS);

                //System.out.println( str );
                //trick the node to get ONLY the uncommented node
                ts = stencil.parse(_i.ast().getComment().get().getCommentedNode().get().toString(Print.PRINT_NO_COMMENTS));
            } else {
                ts = stencil.parse(_i.toString());
            }
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public boolean matches(int i) {
        return select(_intExpr.of(i)) != null;
    }

    public _intExpr instance(String... str) {
        return _intExpr.of(str);
    }

    @Override
    public _intExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _intExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _intExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $intExpr $(String target, String $Name) {
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

    public Predicate<_intExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $intExpr() {
    }

    public $intExpr(_intExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $intExpr(Stencil stencil) {
        this.stencil = stencil;
    }

}
