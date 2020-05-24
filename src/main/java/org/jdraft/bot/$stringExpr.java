package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.jdraft.*;
import org.jdraft._java._domain;
import org.jdraft.io._batch;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * {@link $expr} $bot for selecting, inspecting, drafting & mutating {@link _stringExpr}s / {@link StringLiteralExpr}s
 */
public class $stringExpr implements $bot.$node<StringLiteralExpr, _stringExpr, $stringExpr>,
        $selector.$node<_stringExpr, $stringExpr>,
        $expr<StringLiteralExpr, _stringExpr, $stringExpr> {

    public static $stringExpr of() {
        return new $stringExpr();
    }

    public static $stringExpr of(_stringExpr _i) {
        return new $stringExpr(_i);
    }

    public static $stringExpr of(StringLiteralExpr ile) {
        return new $stringExpr(_stringExpr.of(ile));
    }

    public static $stringExpr of(Stencil stencil) {
        return new $stringExpr(stencil);
    }

    public static $stringExpr of(String... code) {
        return of(_stringExpr.of(code));
    }

    public static $stringExpr of(String i) {
        return new $stringExpr(_stringExpr.of(i)).$and(_i -> _i.getText() == i);
    }

    public static $stringExpr startsWith(String prefix){
        return of( ).$and( s-> s.startsWith(prefix));
    }

    public static $stringExpr endsWith(String postfix){
        return of( ).$and( s-> s.endsWith(postfix));
    }

    public static $stringExpr contains(String contains){
        return of( ).$and( s-> s.contains(contains));
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $stringExpr copy(){
        $stringExpr $s = of( ).$and(this.predicate.and(t->true) );
        if( this.stencil != null ) {
            $s.stencil = this.stencil.copy();
        }
        return $s;
    }

    @Override
    public $stringExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_stringExpr> getPredicate(){
        return this.predicate;
    }

    public $stringExpr setPredicate(Predicate<_stringExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $stringExpr $and(Predicate<_stringExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $stringExpr $not(Predicate<_stringExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_stringExpr> select(String code) {
        try {
            return select(_stringExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_stringExpr> select(String... code) {
        try {
            return select(_stringExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_stringExpr> select(Node n) {
        if (n instanceof StringLiteralExpr) {
            return select(_stringExpr.of((StringLiteralExpr) n));
        }
        return null;
    }

    public Select<_stringExpr> select(Expression e) {
        if (e instanceof StringLiteralExpr) {
            return select(_stringExpr.of((StringLiteralExpr) e));
        }
        return null;
    }

    public Select<_stringExpr> select(_domain _n) {
        if (_n instanceof _stringExpr) {
            return select((_stringExpr) _n);
        }
        return null;
    }

    public Select<_stringExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _stringExpr) {
            return select((_stringExpr) _e);
        }
        return null;
    }

    public Select<_stringExpr> select(_stringExpr _i) {
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
        return select(_stringExpr.of(i)) != null;
    }

    public _stringExpr instance(String... str) {
        return _stringExpr.of(str);
    }

    @Override
    public _stringExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _stringExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _stringExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $stringExpr $(String target, String $Name) {
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

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _batches
     * @return
     */
    public _project matchReplaceIn(Stencil matchStencil, Stencil replaceStencil, _batch..._batches){
        _project _cus = _project.of(_batches);
        matchReplaceIn(matchStencil,replaceStencil, _cus);
        return _cus;
    }

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _batches
     * @return
     */
    public _project matchReplaceIn(String matchStencil, String replaceStencil, _batch..._batches){
        _project _cus = _project.of(_batches);
        matchReplaceIn(matchStencil,replaceStencil, _cus);
        return _cus;
    }

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _cuss
     * @return
     */
    public List<_stringExpr> matchReplaceIn(Stencil matchStencil, Stencil replaceStencil, _project..._cuss){
        List<_stringExpr> replaced = new ArrayList<>();
        Arrays.stream( _cuss).forEach(_cus -> _cus.forEach(_cu -> replaced.addAll( matchReplaceIn( (_java._node)_cu, matchStencil, replaceStencil) ) ));
        return replaced;
    }

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _cuss
     * @return
     */
    public List<_stringExpr> matchReplaceIn(String matchStencil, String replaceStencil, _project..._cuss){
        List<_stringExpr> replaced = new ArrayList<>();
        Arrays.stream( _cuss).forEach( _cus -> _cus.forEach( _cu -> replaced.addAll( matchReplaceIn( (_java._node)_cu, matchStencil, replaceStencil) ) ));
        return replaced;
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(Class clazz, Stencil matchStencil, Stencil replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(Class clazz, String matchStencil, String replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(_java._node _node, Stencil matchStencil, Stencil replaceStencil ){
        if( _node instanceof _codeUnit && ((_codeUnit) _node).isTopLevel()){
            return matchReplaceIn(((_codeUnit) _node).astCompilationUnit(), matchStencil, replaceStencil);
        }
        return matchReplaceIn(_node.ast(), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(_java._node _node, String matchStencil, String replaceStencil ){
        if( _node instanceof _codeUnit && ((_codeUnit) _node).isTopLevel()){
            return matchReplaceIn(((_codeUnit) _node).astCompilationUnit(), matchStencil, replaceStencil);
        }
        return matchReplaceIn(_node.ast(), matchStencil, replaceStencil);
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(Node astNode, String matchStencil, String replaceStencil ){
        return matchReplaceIn(astNode, Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_stringExpr> matchReplaceIn(Node astNode, Stencil matchStencil, Stencil replaceStencil ){
        List<_stringExpr> strings = new ArrayList<>();

        forSelectedIn(astNode, sel-> {
            strings.add(sel.select.matchReplace(matchStencil, replaceStencil));
        });
        return strings;
    }

    public String toString() {
        if (this.stencil != null) {
            return "$string{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$string{" + this.predicate + "}";
    }

    public Predicate<_stringExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $stringExpr() {
    }

    public $stringExpr(_stringExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $stringExpr(Stencil stencil) {
        this.stencil = stencil;
    }

}
