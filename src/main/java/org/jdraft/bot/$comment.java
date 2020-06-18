package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.io._batch;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class $comment implements $bot.$node<Comment, _comment, $comment> {

    /* Static Build Methods */
    public static $comment line(){
        return of().$and(c-> c instanceof _lineComment);
    }

    public static $comment line(String...comment ){
        return of(_lineComment.of(comment)).$and(c-> c instanceof _lineComment);
    }

    public static $comment line(Predicate<_lineComment> predicate){
        return of().$and(c-> c instanceof _lineComment && predicate.test((_lineComment)c ));
    }

    public static $comment block(){
        return of().$and(c-> c instanceof _blockComment);
    }

    public static $comment block(String...comment ){
        return of(comment).$and(c-> c instanceof _blockComment);
    }

    public static $comment block(Predicate<_blockComment> predicate){
        return of().$and(c-> c instanceof _blockComment && predicate.test((_blockComment)c ));
    }

    public static $comment javadoc(){
        return of().$and(c-> c instanceof _javadocComment);
    }

    public static $comment javadoc(String...comment ){
        return of(_javadocComment.of(comment)).$and(c-> c instanceof _javadocComment);
    }

    public static $comment javadoc(Predicate<_javadocComment> predicate){
        return of().$and(c-> c instanceof _javadocComment && predicate.test((_javadocComment)c ));
    }

    public static $comment of(){
        return new $comment();
    }

    public static $comment of( Comment comment ){
        return of( (_comment)_comment.of(comment));
    }

    public static $comment of( String... comment ){
        return new $comment( Stencil.of(comment) );
    }

    public static <_C extends _comment> $comment of( _C _com){
        if( _com instanceof _blockComment ){
            return new $comment( Stencil.of(_com.getText()), c-> c instanceof _blockComment);
        }
        if( _com instanceof _lineComment){
            return new $comment( Stencil.of(_com.getText()), c-> c instanceof _lineComment);
        }
        return new $comment( Stencil.of(_com.getText()), c-> c instanceof _javadocComment);
    }

    public static $comment contains( String contains ){
        return of().$and(c-> c.contains(contains));
    }

    public Predicate<_comment> predicate = t->true;

    /** Defines the entire CONTENTS of the comment (i.e. excluding // or other comment designator characters */
    public Stencil stencil = null;

    public $comment(){
        this.stencil = null;
        this.predicate = t->true;
    }

    public $comment(Stencil stencil){
        this.stencil = stencil;
    }

    public $comment(Stencil stencil, Predicate<_comment> predicate){
        this.stencil = stencil;
        this.predicate = predicate;
    }

    public $comment $not( $comment... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $comment $isAttributed( ){
        return $isAttributed(true);
    }

    /**
     * Matches only comments that have attribution to specific lines or members of code
     * (i.e. NOT orphaned comments)
     *
     * @param isAttributed
     * @return
     */
    public $comment $isAttributed( boolean isAttributed ){
        return $and( c-> c.isAttributed() ==isAttributed );
    }

    public <N extends Node> N forEachIn(N astNode, Predicate<_comment> matchFn, Consumer<_comment> actionFn){
        astNode.getAllContainedComments().stream().forEach(n ->{
            Select<_comment> sel = select(n);
            if( sel != null && matchFn.test(sel.select)) {
                actionFn.accept(sel.select);
            }
        });
        return astNode;
    }

    public List<Select<_comment>> listSelectedIn(Node astNode, Predicate<Select<_comment>> _selectMatchFn) {
        List<Select<_comment>> list = new ArrayList<>();
        astNode.getAllContainedComments().forEach( e -> {
            Select<_comment> s = select(e);
            if (s != null && _selectMatchFn.test(s)) {
                list.add(s);
            }
        });
        return list;
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
    public List<_comment> matchReplaceIn( Stencil matchStencil, Stencil replaceStencil, _project..._cuss){
        List<_comment> replaced = new ArrayList<>();
        Arrays.stream( _cuss).forEach( _cus -> _cus.forEach( _cu -> replaced.addAll( matchReplaceIn( (_tree._node)_cu, matchStencil, replaceStencil) ) ));
        return replaced;
    }

    /**
     *
     * @param matchStencil
     * @param replaceStencil
     * @param _cuss
     * @return
     */
    public List<_comment> matchReplaceIn( String matchStencil, String replaceStencil, _project..._cuss){
        List<_comment> replaced = new ArrayList<>();
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
    public List<_comment> matchReplaceIn( Class clazz, Stencil matchStencil, Stencil replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param clazz
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_comment> matchReplaceIn( Class clazz, String matchStencil, String replaceStencil ){
        return matchReplaceIn( Ast.of(clazz), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_comment> matchReplaceIn(_tree._node _node, Stencil matchStencil, Stencil replaceStencil ){
        if( _node instanceof _codeUnit && ((_codeUnit) _node).isTopLevel()){
            return matchReplaceIn(((_codeUnit) _node).astCompilationUnit(), matchStencil, replaceStencil);
        }
        return matchReplaceIn(_node.node(), matchStencil, replaceStencil);
    }

    /**
     *
     * @param _node
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_comment> matchReplaceIn(_tree._node _node, String matchStencil, String replaceStencil ){
        if( _node instanceof _codeUnit && ((_codeUnit) _node).isTopLevel()){
            return matchReplaceIn(((_codeUnit) _node).astCompilationUnit(), matchStencil, replaceStencil);
        }
        return matchReplaceIn(_node.node(), matchStencil, replaceStencil);
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_comment> matchReplaceIn( Node astNode, String matchStencil, String replaceStencil ){
        return matchReplaceIn(astNode, Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Looks through the contents
     * @param astNode
     * @param matchStencil
     * @param replaceStencil
     * @return
     */
    public List<_comment> matchReplaceIn( Node astNode, Stencil matchStencil, Stencil replaceStencil ){
        List<_comment> comments = new ArrayList<>();

        forSelectedIn(astNode, sel-> {
            comments.add(sel.select.matchReplace(matchStencil, replaceStencil));
        });
        return comments;
    }


    /**
     *
     * @param astNode
     * @param matchFn
     * @param selectActionFn
     * @param <N>
     * @return
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_comment>> matchFn, Consumer<Select<_comment>> selectActionFn){
        astNode.getAllContainedComments().forEach(n ->{
            Select<_comment> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }

    @Override
    public $comment copy() {
        return new $comment ( stencil.copy(), predicate.and(t->true));
    }

    @Override
    public $comment $hardcode(Translator translator, Tokens kvs) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$hardcode(translator, kvs);
        }
        return this;
    }

    @Override
    public Select<_comment> select(Node n) {
        if( n instanceof Comment){
            return select( (_comment)_comment.of((Comment)n));
        }
        return null;
    }

    @Override
    public Predicate<_comment> getPredicate() {
        return predicate;
    }

    @Override
    public $comment setPredicate(Predicate<_comment> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public Select<_comment> select(_comment candidate) {
        if( this.predicate.test(candidate)){
            if( this.stencil == null ){
                return new Select<>( candidate, new Tokens());
            }
            Tokens ts = this.stencil.parse(candidate.getText());
            if( ts != null){
                return new Select<>(candidate, ts);
            }
        }
        return null;
    }

    @Override
    public boolean matches(String candidate) {
        try {
            return matches((_comment) _comment.of(candidate));
        }catch(Exception e){
            return false; //bad comment
        }
    }

    @Override
    public boolean isMatchAny() {
        try {
            return this.stencil == null && this.predicate.test(null);
        }catch(Exception e){
            return false;
        }
    }

    public <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, Node replaceNode) {
        return forEachIn(clazz, p-> Comments.replace((Comment) p.node(), (Statement)replaceNode) );
    }

    public _comment draft(Translator translator, Map<String, Object> keyValues) {
        String built = null;
        if( this.stencil == null ){
            Object com = keyValues.get("$comment");
            if( com instanceof Stencil ){
                built = ((Stencil)com).draft(translator, keyValues);
            } else if( com == null ){
                if( isMatchAny() ) {
                    return null;
                }
                throw new _jdraftException("could not build comment, no stencil and no $comment value set");
            } else{
                built = Stencil.of(com.toString()).draft(translator, keyValues);
            }
        } else {
            built = this.stencil.draft(translator, keyValues);
        }
        try{
            _comment _c = _comment.of(built);
            if( _c != null ){
                if( this.predicate.test(_c)) {
                    return _c;
                }
                return null;
            }
            return null;
        } catch(Exception e){
            throw new _jdraftException("unable to draft comment ", e);
        }
    }

    @Override
    public $comment $(String target, String $Name) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public Template<_comment> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
    }

    @Override
    public List<String> $list() {
        if( this.stencil != null ){
            return this.stencil.$list();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> $listNormalized() {
        if( this.stencil != null ){
            return this.stencil.$listNormalized();
        }
        return Collections.emptyList();
    }

}
