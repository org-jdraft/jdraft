package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import org.jdraft.*;
import org.jdraft._java._domain;
import org.jdraft.text.Template;
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
public class $nullExpr implements $bot.$node<NullLiteralExpr, _nullExpr, $nullExpr>,
        $selector.$node<_nullExpr, $nullExpr>,
        $expr<NullLiteralExpr, _nullExpr, $nullExpr> {

    public static $nullExpr of() {
        return new $nullExpr();
    }

    public static $nullExpr of(_nullExpr _i) {
        return new $nullExpr(_i);
    }

    public static $nullExpr of(NullLiteralExpr ile) {
        return new $nullExpr(_nullExpr.of(ile));
    }

    public static $nullExpr of(String... code) {
        return of(_nullExpr.of(code));
    }

    public Predicate<_nullExpr> getPredicate(){
        return this.predicate;
    }

    public $nullExpr setPredicate(Predicate<_nullExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    @Override
    public $nullExpr $hardcode(Translator translator, Tokens kvs) {
        return this;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $nullExpr copy(){
        $nullExpr $n = of().$and( this.predicate.and(t->true) );
        return $n;
    }

    public $nullExpr $and(Predicate<_nullExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $nullExpr $not( $nullExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $nullExpr $not(Predicate<_nullExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_nullExpr> select(String code) {
        try {
            return select(_nullExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_nullExpr> select(String... code) {
        try {
            return select(_nullExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_nullExpr> select(Node n) {
        if (n instanceof NullLiteralExpr) {
            return select(_nullExpr.of((NullLiteralExpr) n));
        }
        return null;
    }

    public Select<_nullExpr> select(Expression e) {
        if (e instanceof NullLiteralExpr) {
            return select(_nullExpr.of((NullLiteralExpr) e));
        }
        return null;
    }

    public Select<_nullExpr> select(_domain _n) {
        if (_n instanceof _nullExpr) {
            return select((_nullExpr) _n);
        }
        return null;
    }

    public Select<_nullExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _nullExpr) {
            return select((_nullExpr) _e);
        }
        return null;
    }

    public Select<_nullExpr> select(_nullExpr _i) {
        if (predicate.test(_i)) {
           // if (stencil == null) {
           //     return new Selected(_i, new Tokens());
            //}
            //Tokens ts = stencil.parse(_i.toString());
            //if (ts != null) {
            return new Select<>(_i, new Tokens());
            //}
            //return null;
        }
        return null;
    }

    public boolean matches(int i) {
        return select(_intExpr.of(i)) != null;
    }

    public _nullExpr instance(String... str) {
        return _nullExpr.of(str);
    }

    @Override
    public _nullExpr draft(Translator translator, Map<String, Object> keyValues) {
        return _nullExpr.of();
    }

    @Override
    public $nullExpr $(String target, String $Name) {
        //if (this.stencil != null) {
        //    this.stencil = this.stencil.$(target, $Name);
        //}
        return this;
    }

    @Override
    public $nullExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    @Override
    public List<String> $list() {
        //if (this.stencil != null) {
        //    return this.stencil.$list();
        //}
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        //if (this.stencil != null) {
        //    return this.stencil.$listNormalized();
        //}
        return new ArrayList<>();
    }

    public boolean isMatchAny() {
        //if (this.stencil == null || this.stencil.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        //}
        //return false;
    }

    public String toString() {
        return "$null{" + this.predicate + "}";
    }

    public Predicate<_nullExpr> predicate = d -> true;

    public $nullExpr() {
    }

    public $nullExpr(_nullExpr _i) {
        //this.stencil = Stencil.of(_i.toString());
    }
}
