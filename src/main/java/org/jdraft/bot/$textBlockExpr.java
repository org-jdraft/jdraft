package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
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
import java.util.stream.Stream;

/**
 * {@link $expr} $bot for selecting, inspecting, drafting & mutating {@link _textBlockExpr}s / {@link TextBlockLiteralExpr}s
 */
public class $textBlockExpr implements $bot.$node<TextBlockLiteralExpr, _textBlockExpr, $textBlockExpr>,
        $selector.$node<_textBlockExpr, $textBlockExpr>,
        $expr<TextBlockLiteralExpr, _textBlockExpr, $textBlockExpr> {

    public static $textBlockExpr of() {
        return new $textBlockExpr();
    }

    public static $textBlockExpr of(_textBlockExpr _i) {
        return new $textBlockExpr(_i);
    }

    public static $textBlockExpr of(TextBlockLiteralExpr ile) {
        return new $textBlockExpr(_textBlockExpr.of(ile));
    }

    public static $textBlockExpr of(Stencil stencil) {
        return new $textBlockExpr(stencil);
    }

    public static $textBlockExpr of(String... code) {
        return of(_textBlockExpr.of(code));
    }

    public static $textBlockExpr of(String i) {
        return new $textBlockExpr(_textBlockExpr.of(i)).$and(_i -> _i.getText() == i);
    }

    public static $textBlockExpr contains(String contains ){
        return of().$and(tb-> tb.contains(contains));
    }

    public Predicate<_textBlockExpr> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $textBlockExpr copy(){
        $textBlockExpr $t = of( ).$and(this.predicate.and(t->true) );
        if( this.stencil != null ) {
            $t.stencil = this.stencil.copy();
        }
        return $t;
    }

    @Override
    public $textBlockExpr $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    public $textBlockExpr setPredicate(Predicate<_textBlockExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public $textBlockExpr $and(Predicate<_textBlockExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $textBlockExpr $not( $textBlockExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $textBlockExpr $not(Predicate<_textBlockExpr> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Select<_textBlockExpr> select(String code) {
        try {
            return select(_textBlockExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_textBlockExpr> select(String... code) {
        try {
            return select(_textBlockExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_textBlockExpr> select(Node n) {
        if (n instanceof TextBlockLiteralExpr) {
            return select(_textBlockExpr.of((TextBlockLiteralExpr) n));
        }
        return null;
    }

    public Select<_textBlockExpr> select(Expression e) {
        if (e instanceof TextBlockLiteralExpr) {
            return select(_textBlockExpr.of((TextBlockLiteralExpr) e));
        }
        return null;
    }

    public Select<_textBlockExpr> select(_domain _n) {
        if (_n instanceof _textBlockExpr) {
            return select((_textBlockExpr) _n);
        }
        return null;
    }

    public Select<_textBlockExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _textBlockExpr) {
            return select((_textBlockExpr) _e);
        }
        return null;
    }

    public Select<_textBlockExpr> select(_textBlockExpr _i) {
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
    public List<_textBlockExpr> matchReplaceIn(Stencil matchStencil, Stencil replaceStencil, _project..._cuss){
        List<_textBlockExpr> replaced = new ArrayList<>();
        Arrays.stream( _cuss).forEach(_cus -> _cus.forEach(_cu -> replaced.addAll( matchReplaceIn( (_tree._node)_cu, matchStencil, replaceStencil) ) ));
        return replaced;
    }

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _cuss
     * @return
     */
    public List<_textBlockExpr> matchReplaceIn(String matchStencil, String replaceStencil, _project..._cuss){
        List<_textBlockExpr> replaced = new ArrayList<>();
        Arrays.stream( _cuss).forEach( _cus -> _cus.forEach( _cu -> replaced.addAll( matchReplaceIn( (_tree._node)_cu, matchStencil, replaceStencil) ) ));
        return replaced;
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_textBlockExpr> matchReplaceIn(Class clazz, Stencil matchStencil, Stencil replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_textBlockExpr> matchReplaceIn(Class clazz, String matchStencil, String replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_textBlockExpr> matchReplaceIn(_tree._node _node, Stencil matchStencil, Stencil replaceStencil ){
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
    public List<_textBlockExpr> matchReplaceIn(_tree._node _node, String matchStencil, String replaceStencil ){
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
    public List<_textBlockExpr> matchReplaceIn(Node astNode, String matchStencil, String replaceStencil ){
        return matchReplaceIn(astNode, Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_textBlockExpr> matchReplaceIn(Node astNode, Stencil matchStencil, Stencil replaceStencil ){
        List<_textBlockExpr> textBlocks = new ArrayList<>();

        forSelectedIn(astNode, sel-> {
            textBlocks.add(sel.select.matchReplace(matchStencil, replaceStencil));
        });
        return textBlocks;
    }

    public boolean matches(String i) {
        return select(_textBlockExpr.of(i)) != null;
    }

    public _textBlockExpr instance(String... str) {
        return _textBlockExpr.of(str);
    }

    @Override
    public _textBlockExpr draft(Translator translator, Map<String, Object> keyValues) {
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
            _textBlockExpr _i = instance(drafted);
            if (this.predicate.test(_i)) {
                return _i;
            }
            return null;
        }
        String draftedCode = stencil.draft(translator, keyValues);
        if (draftedCode != null) {
            _textBlockExpr instance = instance(draftedCode);
            if (predicate.test(instance)) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public $textBlockExpr $(String target, String $Name) {
        if (this.stencil != null) {
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public $textBlockExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
        return $hardcode(translator, Tokens.of(keyValues));
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
            return "$textBlock{" + System.lineSeparator() + "    " + this.stencil.toString() + System.lineSeparator() + "}";
        }
        return "$textBlock{" + this.predicate + "}";
    }

    public Predicate<_textBlockExpr> predicate = d -> true;

    public Stencil stencil = null;

    public $textBlockExpr() {
    }

    public $textBlockExpr(_textBlockExpr _i) {
        this.stencil = Stencil.of(_i.toString());
    }

    private $textBlockExpr(Stencil stencil) {
        this.stencil = stencil;
    }

}
