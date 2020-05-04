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
 * {@link $expression} $bot for selecting, inspecting, drafting & mutating {@link _string}s / {@link StringLiteralExpr}s
 */
public class $string implements $bot.$node<StringLiteralExpr, _string, $string>,
        $selector.$node<_string, $string>,
        $expression<StringLiteralExpr, _string, $string> {

    public static $string of() {
        return new $string();
    }

    public static $string of(_string _i) {
        return new $string(_i);
    }

    public static $string of(StringLiteralExpr ile) {
        return new $string(_string.of(ile));
    }

    public static $string of(Stencil stencil) {
        return new $string(stencil);
    }

    public static $string of(String... code) {
        return of(_string.of(code));
    }

    public static $string of(String i) {
        return new $string(_string.of(i)).$and(_i -> _i.getText() == i);
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $string copy(){
        $string $s = of( ).$and(this.predicate.and(t->true) );
        if( this.stencil != null ) {
            $s.stencil = this.stencil.copy();
        }
        return $s;
    }

    @Override
    public $string $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_string> getPredicate(){
        return this.predicate;
    }

    public $string setPredicate( Predicate<_string> predicate){
        this.predicate = predicate;
        return this;
    }

    public $string $and(Predicate<_string> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $string $not(Predicate<_string> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_string> select(String code) {
        try {
            return select(_string.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_string> select(String... code) {
        try {
            return select(_string.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_string> select(Node n) {
        if (n instanceof StringLiteralExpr) {
            return select(_string.of((StringLiteralExpr) n));
        }
        return null;
    }

    public Select<_string> select(Expression e) {
        if (e instanceof StringLiteralExpr) {
            return select(_string.of((StringLiteralExpr) e));
        }
        return null;
    }

    public Select<_string> select(_domain _n) {
        if (_n instanceof _string) {
            return select((_string) _n);
        }
        return null;
    }

    public Select<_string> select(_expression<?, ?> _e) {
        if (_e instanceof _string) {
            return select((_string) _e);
        }
        return null;
    }

    public Select<_string> select(_string _i) {
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
        return select(_string.of(i)) != null;
    }

    public _string instance(String... str) {
        return _string.of(str);
    }

    @Override
    public _string draft(Translator translator, Map<String, Object> keyValues) {
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
            _string _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _string instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $string $(String target, String $Name) {
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
    public List<_string> matchReplaceIn( Stencil matchStencil, Stencil replaceStencil, _project..._cuss){
        List<_string> replaced = new ArrayList<>();
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
    public List<_string> matchReplaceIn( String matchStencil, String replaceStencil, _project..._cuss){
        List<_string> replaced = new ArrayList<>();
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
    public List<_string> matchReplaceIn( Class clazz, Stencil matchStencil, Stencil replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_string> matchReplaceIn( Class clazz, String matchStencil, String replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_string> matchReplaceIn( _java._node _node, Stencil matchStencil, Stencil replaceStencil ){
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
    public List<_string> matchReplaceIn( _java._node _node, String matchStencil, String replaceStencil ){
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
    public List<_string> matchReplaceIn( Node astNode, String matchStencil, String replaceStencil ){
        return matchReplaceIn(astNode, Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_string> matchReplaceIn( Node astNode, Stencil matchStencil, Stencil replaceStencil ){
        List<_string> strings = new ArrayList<>();

        forSelectedIn(astNode, sel-> {
            strings.add(sel.selection.matchReplace(matchStencil, replaceStencil));
        });
        return strings;
    }

    public String toString() {
        if (this.stencil != null) {
            return "$string{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$string{" + this.predicate + "}";
    }

    public Predicate<_string> predicate = d -> true;

    public Stencil stencil = null;

    public $string() {
    }

    public $string(_string _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $string(Stencil stencil) {
        this.stencil = stencil;
    }

}
