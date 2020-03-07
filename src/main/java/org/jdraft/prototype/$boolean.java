package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import org.jdraft._arguments;
import org.jdraft._boolean;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
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
public class $boolean implements $prototype.$node<BooleanLiteralExpr, _boolean, $boolean>,
        $selector.$node<_boolean, $boolean>,
        $expr<BooleanLiteralExpr, _boolean, $boolean> {

    public static $boolean of() {
        return new $boolean();
    }

    public static $boolean of(_boolean _i) {
        return new $boolean(_i);
    }

    public static $boolean of(BooleanLiteralExpr ile) {
        return new $boolean(_boolean.of(ile));
    }

    public static $boolean of(Stencil stencil) {
        return new $boolean(stencil);
    }

    public static $boolean of(String... code) {
        return of(_boolean.of(code));
    }

    public static $boolean of(boolean i) {
        return new $boolean(_boolean.of(i)).$and(_i -> _i.getValue() == i);
    }

    public static $boolean of(Predicate<_boolean> _matchFn) {
        return new $boolean().$and(_matchFn);
    }

    public static $boolean or($boolean...$bs){
        return new $boolean.Or($bs);
    }
    public Predicate<_boolean> getPredicate(){
        return this.predicate;
    }

    public $boolean $and(Predicate<_boolean> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $boolean $not(Predicate<_boolean> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Selected select(String code) {
        try {
            return select(_boolean.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_boolean.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof BooleanLiteralExpr) {
            return select(_boolean.of((BooleanLiteralExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof BooleanLiteralExpr) {
            return select(_boolean.of((BooleanLiteralExpr) e));
        }
        return null;
    }

    public Selected select(_domain _n) {
        if (_n instanceof _boolean) {
            return select((_boolean) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _boolean) {
            return select((_boolean) _e);
        }
        return null;
    }

    public Selected select(_boolean _i) {
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

    public boolean matches(boolean i) {
        return select(_boolean.of(i)) != null;
    }

    public _boolean instance(String... str) {
        return _boolean.of(str);
    }

    @Override
    public _boolean draft(Translator translator, Map<String, Object> keyValues) {
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
            _boolean _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _boolean instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $boolean $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        if (this.stencil != null) {
            return this.stencil.list$();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> list$Normalized() {
        if (this.stencil != null) {
            return this.stencil.list$Normalized();
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

    public Predicate<_boolean> predicate = d -> true;

    public Stencil stencil = null;

    public $boolean() {
    }

    public $boolean(_boolean _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $boolean(Stencil stencil) {
        this.stencil = stencil;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_boolean> {

        public Selected(_boolean _node, Tokens tokens) {
            super(_node, tokens);
        }
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $boolean {

        public Predicate<_boolean> predicate = p-> true;

        public List<$boolean> $boolean = new ArrayList<>();

        private Or($boolean...nms){
            Arrays.stream(nms).forEach(n-> $boolean.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<_boolean> getPredicate(){
            return this.predicate;
        }

        public boolean matches(String args){
            return select(args) != null;
        }

        public boolean matches(String... args){
            return select(args) != null;
        }

        public boolean matches(_expression _exprs){
            if( _exprs instanceof _boolean ) {
                return select( (_boolean)_exprs) != null;
            }
            return false;
        }

        public boolean matches(Expression exprs){
            return select(exprs) != null;
        }

        public $boolean.Selected select(String args){
            try {
                return select(_boolean.of(args));
            }catch(Exception e){
                return null;
            }
        }

        public  $boolean.Selected select(String...args){
            try {
                return select(_boolean.of(args));
            }catch(Exception e){
                return null;
            }
        }

        public  $boolean.Selected select(_expression _expr){
            if( _expr instanceof _boolean ) {
                return select( (_boolean) _expr);
            }
            return null;
        }

        public  $boolean.Selected select(Expression expr){
            if( expr instanceof BooleanLiteralExpr) {
                return select(_boolean.of( (BooleanLiteralExpr)expr));
            }
            return null;
        }

        public boolean matches(_boolean candidate){
            return select(candidate) != null;
        }

        @Override
        public $boolean.Selected select(_boolean candidate) {
            if( predicate.test(candidate) ) {
                Optional<$boolean> on = $boolean.stream().filter(n -> n.matches(candidate)).findFirst();
                if (on.isPresent()) {
                    return on.get().select(candidate);
                }
            }
            return null;
        }
    }
}
