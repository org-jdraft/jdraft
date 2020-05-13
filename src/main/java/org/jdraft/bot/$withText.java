package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import org.jdraft.*;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Unifies entities which are representations of Strings or contain Strings
 *
 * i.e. if you want to replace all of the String references to "OldMap" to "NewMap"
 * _project _p = $strings.replace("OldMap", "NewMap");
 *
 * you would replace the Strings in:
 * <UL>
 *     <LI>{@link org.jdraft._string} literals</LI>
 *     <LI>{@link org.jdraft._textBlock} literals</LI>
 *     <LI>{@link org.jdraft._comment} types:</LI>
 *     <LI>    {@link org.jdraft._lineComment} </LI>
 *     <LI>    {@link org.jdraft._blockComment} </LI>
 *     <LI>    {@link org.jdraft._javadocComment} </LI>
 * </UL>
 */
public class $withText implements $bot.$node<Node, _java._node, $withText> {

    public static $withText of(String contains){
         return new $withText( contains, $string.contains(contains), $textBlock.contains(contains), $comment.contains(contains) );
    }

    public Predicate<_java._node>predicate = t->true;

    public String contains;
    public $string string;
    public $textBlock textBlock;
    public $comment comment;

    private $withText(String contains, $string string, $textBlock textBlock, $comment comment){
        this.contains = contains;
        this.string = string;
        this.textBlock = textBlock;
        this.comment = comment;
    }

    @Override
    public Select<_java._node> select(Node n) {
        if( n instanceof Comment){
            return select( (_java._node)_comment.of( (Comment)n) );
        }else if( n instanceof StringLiteralExpr ){
            return select( (_java._node)_string.of( (StringLiteralExpr) n) );
        }else if( n instanceof TextBlockLiteralExpr){
            return select( (_java._node)_textBlock.of( (TextBlockLiteralExpr) n) );
        }
        return null;
    }

    @Override
    public Select<_java._node> select(_java._node candidate) {
        if( !this.predicate.test(candidate) ){
            return null;
        }
        if( candidate instanceof _comment ){
            Select s = this.comment.select((_comment)candidate);
            if( s != null ){
                return new Select<>(candidate, s.tokens);
            }
            return null;
        }
        if( candidate instanceof _string){
            Select s = this.string.select((_string)candidate);
            if( s != null ){
                return new Select<>(candidate, s.tokens);
            }
            return null;
        }
        if( candidate instanceof _textBlock){
            Select s = this.textBlock.select((_textBlock) candidate);
            if( s != null ){
                return new Select<>(candidate, s.tokens);
            }
            return null;
        }
        return null;
    }

    @Override
    public $withText copy() {
        return new $withText( this.contains+"", this.string.copy(), this.textBlock.copy(), this.comment.copy() )
                .$and(this.predicate.and(t->true));
    }

    @Override
    public $withText $hardcode(Translator translator, Tokens kvs) {
        this.comment.$hardcode(translator, kvs);
        this.string.$hardcode(translator, kvs);
        this.textBlock.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public Select<_java._node> selectFirstIn(Node astNode, Predicate<Select<_java._node>> predicate) {
        Optional<Node> on = astNode.stream().filter(n-> {
            Select<_java._node> sn = select(n);
            if( sn != null ){
                return predicate.test(sn);
            }
            return false;
        }).findFirst();
        if( on.isPresent() ){
            return select( on.get());
        }
        return null;
    }

    @Override
    public Predicate<_java._node> getPredicate() {
        return this.predicate;
    }

    @Override
    public $withText setPredicate(Predicate<_java._node> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean matches(String candidate) {
        return select( _string.of(candidate) ) != null ||
               select( (_comment)_comment.of(candidate) ) != null ||
               select(_textBlock.of(candidate) ) != null;
    }

    @Override
    public boolean isMatchAny() {
        return false;
    }

    @Override
    public _java._node draft(Translator translator, Map<String, Object> keyValues) {
        throw new _jdraftException("Cannot draft a $strings");
    }

    @Override
    public Template<_java._node> $(String target, String $Name) {
        this.comment.$(target, $Name);
        this.string.$(target, $Name);
        this.textBlock.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll(this.comment.$list());
        all.addAll(this.string.$list());
        all.addAll(this.textBlock.$list());
        return all;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> all = new ArrayList<>();
        all.addAll(this.comment.$listNormalized());
        all.addAll(this.string.$listNormalized());
        all.addAll(this.textBlock.$listNormalized());
        return all.stream().distinct().collect(Collectors.toList());
    }

    /**
     *
     * @param astNode
     * @param matchFn
     * @param selectActionFn
     * @param <N>
     * @return
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_java._node>> matchFn, Consumer<Select<_java._node>> selectActionFn){

        List<Select<_java._node>> found = new ArrayList<>();

        astNode.getAllContainedComments().forEach(n -> {
            Select<_java._node> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                found.add( sel );
            }
        });

        astNode.stream().forEach(n ->{
            if( !( n instanceof Comment )) { //we need to NOT do orphaned comments twice
                Select<_java._node> sel = select(n);
                if (sel != null && matchFn.test(sel)) {
                    selectActionFn.accept(sel);
                }
            }
        });
        found.stream().forEachOrdered(selectActionFn);
        return astNode;
    }

    /**
     *
     * @param astNode
     * @param matchFn
     * @param actionFn
     * @param <N>
     * @return
     */
    public <N extends Node> N forEachIn(N astNode, Predicate<_java._node> matchFn, Consumer<_java._node> actionFn){
        List<_java._node> found = new ArrayList<>();

        astNode.getAllContainedComments().forEach(n -> {
            Select<_java._node> sel = select(n);
            if( sel != null && matchFn.test(sel.selection)) {
                found.add( sel.selection);
            }
        });

        astNode.stream().forEach(n ->{
            if( !( n instanceof Comment )) { //we need to NOT do orphaned comments twice
                Select<_java._node> sel = select(n);
                if (sel != null && matchFn.test(sel.selection)) {
                    actionFn.accept(sel.selection);
                }
            }
        });
        _java.sort(found).stream().forEachOrdered(actionFn);
        return astNode;
    }

    public <_J extends _java._node> _J replaceIn(_J _j, String...replacement) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ) {
                forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
                    ((_java._withText)s.selection).replace(this.contains, Text.combine(replacement));
                });
                return _j;
            }
        }
        forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
            ((_java._withText)s.selection).replace(this.contains, Text.combine(replacement));
        });
        return _j;
    }

    public  <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, String replacementString) {
        return forEachIn(clazz, p->{
            _java._withText wt = (_java._withText)p;
            wt.replace(this.contains, replacementString);
        });
    }
}
