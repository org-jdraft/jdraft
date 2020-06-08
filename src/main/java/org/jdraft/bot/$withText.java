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
import java.util.stream.Stream;

/**
 * Unifies entities which are representations of Strings or contain Strings
 *
 * i.e. if you want to replace all of the String references to "OldMap" to "NewMap"
 * _project _p = $strings.replace("OldMap", "NewMap");
 *
 * you would replace the Strings in:
 * <UL>
 *     <LI>{@link _stringExpr} literals</LI>
 *     <LI>{@link _textBlockExpr} literals</LI>
 *     <LI>{@link org.jdraft._comment} types:</LI>
 *     <LI>    {@link org.jdraft._lineComment} </LI>
 *     <LI>    {@link org.jdraft._blockComment} </LI>
 *     <LI>    {@link org.jdraft._javadocComment} </LI>
 * </UL>
 */
public class $withText implements $bot.$node<Node, _tree._node, $withText> {

    public static $withText of(String contains){
         return new $withText( contains, $stringExpr.contains(contains), $textBlockExpr.contains(contains), $comment.contains(contains) );
    }

    public Predicate<_tree._node>predicate = t->true;

    public String contains;
    private $stringExpr string;
    private $textBlockExpr textBlock;
    private $comment comment;

    private $withText(String contains, $stringExpr string, $textBlockExpr textBlock, $comment comment){
        this.contains = contains;
        this.string = string;
        this.textBlock = textBlock;
        this.comment = comment;
    }

    @Override
    public Select<_tree._node> select(Node n) {
        if( n instanceof Comment){
            return select( (_tree._node)_comment.of( (Comment)n) );
        }else if( n instanceof StringLiteralExpr ){
            return select( (_tree._node) _stringExpr.of( (StringLiteralExpr) n) );
        }else if( n instanceof TextBlockLiteralExpr){
            return select( (_tree._node) _textBlockExpr.of( (TextBlockLiteralExpr) n) );
        }
        return null;
    }

    @Override
    public Select<_tree._node> select(_tree._node candidate) {
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
        if( candidate instanceof _stringExpr){
            Select s = this.string.select((_stringExpr)candidate);
            if( s != null ){
                return new Select<>(candidate, s.tokens);
            }
            return null;
        }
        if( candidate instanceof _textBlockExpr){
            Select s = this.textBlock.select((_textBlockExpr) candidate);
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
    public Select<_tree._node> selectFirstIn(Node astNode, Predicate<Select<_tree._node>> predicate) {
        Optional<Node> on = astNode.stream().filter(n-> {
            Select<_tree._node> sn = select(n);
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
    public Predicate<_tree._node> getPredicate() {
        return this.predicate;
    }

    @Override
    public $withText setPredicate(Predicate<_tree._node> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean matches(String candidate) {
        return select( _stringExpr.of(candidate) ) != null ||
               select( (_comment)_comment.of(candidate) ) != null ||
               select(_textBlockExpr.of(candidate) ) != null;
    }

    @Override
    public boolean isMatchAny() {
        return false;
    }

    @Override
    public _tree._node draft(Translator translator, Map<String, Object> keyValues) {
        throw new _jdraftException("Cannot draft a $strings");
    }

    @Override
    public $withText $(String target, String $Name) {
        this.comment.$(target, $Name);
        this.string.$(target, $Name);
        this.textBlock.$(target, $Name);
        return this;
    }

    @Override
    public $withText $hardcode(Translator translator, Map<String, Object> keyValues) {
        return $hardcode(translator, Tokens.of(keyValues));
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll(this.comment.$list());
        all.addAll(this.string.$list());
        all.addAll(this.textBlock.$list());
        return all;
    }

    public $withText $not( $withText... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
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
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_tree._node>> matchFn, Consumer<Select<_tree._node>> selectActionFn){

        List<Select<_tree._node>> found = new ArrayList<>();

        astNode.getAllContainedComments().forEach(n -> {
            Select<_tree._node> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                found.add( sel );
            }
        });

        astNode.stream().forEach(n ->{
            if( !( n instanceof Comment )) { //we need to NOT do orphaned comments twice
                Select<_tree._node> sel = select(n);
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_tree._node> matchFn, Consumer<_tree._node> actionFn){
        List<_tree._node> found = new ArrayList<>();

        astNode.getAllContainedComments().forEach(n -> {
            Select<_tree._node> sel = select(n);
            if( sel != null && matchFn.test(sel.select)) {
                found.add( sel.select);
            }
        });

        astNode.stream().forEach(n ->{
            if( !( n instanceof Comment )) { //we need to NOT do orphaned comments twice
                Select<_tree._node> sel = select(n);
                if (sel != null && matchFn.test(sel.select)) {
                    actionFn.accept(sel.select);
                }
            }
        });
        _java.sort(found).stream().forEachOrdered(actionFn);
        return astNode;
    }

    public <_J extends _tree._node> _J replaceIn(_J _j, String...replacement) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ) {
                forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
                    ((_java._withText)s.select).replace(this.contains, Text.combine(replacement));
                });
                return _j;
            }
        }
        forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
            ((_java._withText)s.select).replace(this.contains, Text.combine(replacement));
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
