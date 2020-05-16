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
 * models the Ast code/use for boolean literals
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

    public static $booleanExpr of(String... code) {
        return of(_booleanExpr.of(code));
    }

    public static $booleanExpr of(boolean i) {
        return new $booleanExpr(_booleanExpr.of(i));
    }

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
        $booleanExpr $copy = of( ).$and(this.predicate.and(t->true) );
        $copy.bool = this.bool.copy();
        return $copy;
    }

    @Override
    public $booleanExpr $hardcode(Translator translator, Tokens kvs) {
        return this;
    }

    public $booleanExpr setPredicate(Predicate<_booleanExpr> predicate){
        this.predicate = predicate;
        return this;
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

    public Select<_booleanExpr> select(_expr _e) {
        if (_e instanceof _booleanExpr) {
            return select((_booleanExpr) _e);
        }
        return null;
    }

    public Select<_booleanExpr> select(_booleanExpr _i) {
        if (predicate.test(_i)) {
            Tokens ts = bool.apply(_i);
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

    @Override
    public _booleanExpr draft(Translator translator, Map<String, Object> keyValues) {
        if (this.bool.isMatchAny() ) {
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
            _booleanExpr _i = _booleanExpr.of(drafted); //instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        Boolean drafted = this.bool.draft(translator, keyValues);
        if (drafted != null) {
            _booleanExpr instance = _booleanExpr.of(drafted);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $booleanExpr $(String target, String $Name) {
        return this;
    }

    @Override
    public List<String> $list() {
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        return new ArrayList<>();
    }

    public boolean isMatchAny() {
        if (this.bool.isMatchAny() ) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        if( this.isMatchAny() ){
            return "$booleanExpr{ ANY }";
        }
        if( this.bool.getExpected() == null ){
            return "$booleanExpr{ "+this.predicate+" }";
        }
        return "$booleanExpr{ "+this.bool.getExpected()+" }";
    }

    public Predicate<_booleanExpr> predicate = d -> true;

    public Select.$BooleanSelect<_booleanExpr> bool = new Select.$BooleanSelect<>
            (_booleanExpr.class, "value", b-> b.getValue());

    public $booleanExpr() {
    }

    public $booleanExpr(_booleanExpr _i) {
        this.bool.setExpected(_i.getValue());
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
            $copy.bool = this.bool.copy();
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
