package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.*;
import org.jdraft._java._domain;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;

/**
 * syntax prototype for the model of the boolean types
 *
 * @author Eric
 */
public class $booleanExpr implements $bot.$node<BooleanLiteralExpr, _booleanExpr, $booleanExpr>,
        $selector.$node<_booleanExpr, $booleanExpr>,
        $expr<BooleanLiteralExpr, _booleanExpr, $booleanExpr> {

    public static $booleanExpr of() {
        return new $booleanExpr();
    }

    public static $booleanExpr of(_booleanExpr _i) {
        return new $booleanExpr(_i);
    }

    public static $booleanExpr of(BooleanLiteralExpr ile) {
        return new $booleanExpr(_booleanExpr.of(ile));
    }

    public static $booleanExpr of(Stencil stencil) {
        return new $booleanExpr(stencil);
    }

    public static $booleanExpr of(String... code) {
        return of(_booleanExpr.of(code));
    }

    public static $booleanExpr of(boolean i) {
        return new $booleanExpr(_booleanExpr.of(i)).$and(_i -> _i.getValue() == i);
    }

    //public static $boolean of(Predicate<_boolean> _matchFn) {
    //    return new $boolean().$and(_matchFn);
    //}

    public static $booleanExpr.Or or($booleanExpr...$bs){
        return new $booleanExpr.Or($bs);
    }

    public Predicate<_booleanExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $booleanExpr copy(){
        $booleanExpr $b = of( ).$and(this.predicate.and(t->true) );
        $b.stencil = this.stencil.copy();
        return $b;
    }

    @Override
    public $booleanExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $booleanExpr setPredicate(Predicate<_booleanExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $booleanExpr $and(Predicate<_booleanExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $booleanExpr $not(Predicate<_booleanExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_booleanExpr> select(String code) {
        try {
            return select(_booleanExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_booleanExpr> select(String... code) {
        try {
            return select(_booleanExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_booleanExpr> select(Node n) {
        if (n instanceof BooleanLiteralExpr) {
            return select(_booleanExpr.of((BooleanLiteralExpr) n));
        }
        return null;
    }

    public Select<_booleanExpr> select(Expression e) {
        if (e instanceof BooleanLiteralExpr) {
            return select(_booleanExpr.of((BooleanLiteralExpr) e));
        }
        return null;
    }

    public Select<_booleanExpr> select(_domain _n) {
        if (_n instanceof _booleanExpr) {
            return select((_booleanExpr) _n);
        }
        return null;
    }

    public Select<_booleanExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _booleanExpr) {
            return select((_booleanExpr) _e);
        }
        return null;
    }

    public Select<_booleanExpr> select(_booleanExpr _i) {
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

    public boolean matches(boolean i) {
        return select(_booleanExpr.of(i)) != null;
    }

    public _booleanExpr instance(String... str) {
        return _booleanExpr.of(str);
    }

    @Override
    public _booleanExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _booleanExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _booleanExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $booleanExpr $(String target, String $Name) {
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
            return "$boolean{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$boolean{" + this.predicate + "}";
    }

    public Predicate<_booleanExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $booleanExpr() {
    }

    public $booleanExpr(_booleanExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $booleanExpr(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $booleanExpr {

        public List<$booleanExpr> $booleanExprBots = new ArrayList<>();

        private Or($booleanExpr...bots){
            Arrays.stream(bots).forEach(n-> $booleanExprBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public $booleanExpr.Or copy(){
            $booleanExpr.Or $copy = $booleanExpr.or();
            $copy.$and(this.predicate);
            if( stencil != null ) {
                $copy.stencil = this.stencil.copy();
            }
            this.$booleanExprBots.forEach( ($b) -> $copy.$booleanExprBots.add($b.copy()));
            return $copy;
        }

        @Override
        public Select<_booleanExpr> select(_booleanExpr _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $booleanExpr $whichBot = whichMatch(_candidate);
            if( $whichBot == null ){
                    return null;
            }
            Select whichSelect = $whichBot.select(_candidate);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        /**
         * Return the underlying $arrayAccess that matches the _arrayAccess
         * (or null if none of the $arrayAccess match the candidate _arrayAccess)
         * @param _candidate
         * @return
         */
        public $booleanExpr whichMatch(_booleanExpr _candidate){
            Optional<$booleanExpr> orsel  = this.$booleanExprBots.stream().filter($p-> $p.matches(_candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
