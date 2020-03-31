package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    public static $comment of( _comment _com){
        if( _com instanceof _blockComment ){
            return new $comment( Stencil.of(_com.getContents()), c-> c instanceof _blockComment);
        }
        if( _com instanceof _lineComment){
            return new $comment( Stencil.of(_com.getContents()), c-> c instanceof _lineComment);
        }
        return new $comment( Stencil.of(_com.getContents()), c-> c instanceof _javadocComment);
    }

    public static $comment of( Predicate<_comment> pc){
        return new $comment(pc);
    }

    public Predicate<_comment> predicate = t->true;

    /** Defines the entire CONTENTS of the comment (i.e. excluding // or other comment designator characters */
    public Stencil stencil = null;

    public $comment(){
        this.stencil = null;
        this.predicate = t->true;
    }

    public $comment(Predicate<_comment> predicate){
        this.stencil = null;
        this.predicate = predicate;
    }

    public $comment(Stencil stencil){
        this.stencil = stencil;
    }

    public $comment(Stencil stencil, Predicate<_comment> predicate){
        this.stencil = stencil;
        this.predicate = predicate;
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
            if( sel != null && matchFn.test(sel.selection)) {
                actionFn.accept(sel.selection);
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
            Tokens ts = this.stencil.parse(candidate.getContents());
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
        return forEachIn(clazz, p-> Comments.replace((Comment) p.ast(), (Statement)replaceNode) );
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


    //I need a "parser" abstraction that can read
    //linePatternSelector -> looks line by line at the contents of the
    //contentPatternSelector ->
}
