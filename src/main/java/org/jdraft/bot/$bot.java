package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * B is the underlying syntactic representation
 * _B is the _java.domain wrapper of the syntatic thing
 * $B the bot type
 *
 * @param <B> the underlying syntax type
 * @param <_B> the _java.domain type
 * @param <$B> the $bot type
 */
public interface $bot<B, _B, $B>
        extends $selector<_B, $B>, Template<_B> {

    /**
     * Build a new bot instance that is another mutable copy of this
     * @return
     */
    $B copy();

    Select<_B> select(Node n);

    /** */
    default _B firstIn(Class<?> clazz) {
        return firstIn(clazz, p->true);
    }

    /** */
    default _B firstIn(Class<?> clazz, Predicate<_B> matchFn) {
        return firstIn( Ast.of(clazz), matchFn);
    }

    /**
     *
     * @param astNode
     * @return
     */
    default _B firstIn(Node astNode) {
        return firstIn(astNode, p->true);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param matchFn
     * @return  the first Expression that matches (or null if none found)
     */
    default _B firstIn(Node astNode, Predicate<_B> matchFn) {
        Select<_B> sel = selectFirstIn(astNode, (s)-> matchFn.test(s.get()));
        if( sel != null ) {
            return sel.selection;
        }
        return null;
    }

    default Select<_B> selectFirstIn(_java._domain _j) {
        return selectFirstIn(_j, t->true);
    }

    default Select<_B> selectFirstIn(_java._domain _j, Predicate<Select<_B>> matchFn) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                selectFirstIn(_j, matchFn);
            }
        }
        return selectFirstIn(_j, matchFn);
    }

    /** */
    default Select<_B> selectFirstIn(Class<?> clazz) {
        return selectFirstIn(clazz, p->true);
    }

    default Select<_B> selectFirstIn(Class<?> clazz, Predicate<Select<_B>> matchFn) {
        return selectFirstIn( Ast.of(clazz), matchFn);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Expression that matches (or null if none found)
     */
    default Select<_B> selectFirstIn(Node astNode) {
        return selectFirstIn(astNode, t-> true);
    }

    Select<_B> selectFirstIn(Node astNode, Predicate<Select<_B>>predicate);


    default int countIn(Class<?> clazz) {
        _type _t = _java.type(clazz);
        return countIn(_t);
    }

    default int countIn(Class<?> clazz, Predicate<_B> _matchFn) {
        return countIn((_type)_java.type(clazz), _matchFn);
    }

    default int countIn(_java._node<?, ?> _j) {
        if( _j instanceof _type){
            _type _t = (_type)_j;
            if( _t.isTopLevel() ){
                return countIn(_t.astCompilationUnit());
            }
        }
        return countIn(_j.ast());
    }

    default int countIn(_java._node<?, ?> _j, Predicate<_B> _matchFn) {
        if( _j instanceof _type){
            _type _t = (_type)_j;
            if( _t.isTopLevel() ){
                return countIn(_t.astCompilationUnit(), _matchFn);
            }
        }
        return countIn(_j.ast(), _matchFn);
    }

    default int countIn(Node n) {
        return countIn(n, p->true);
    }

    default int countIn(Node n, Predicate<_B> _matchFn) {
        AtomicInteger ai = new AtomicInteger();
        forEachIn(n, _matchFn, e-> ai.incrementAndGet() );
        return ai.get();
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Consumer<_B> actionFn){
        return forEachIn(clazz, p->true, actionFn);
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Predicate<_B> matchFn, Consumer<_B> actionFn){
        _CT _t = _java.type(clazz);
        forEachIn( _t, matchFn, actionFn);
        return _t;
    }

    default <_J extends _java._node<?,?>> _J forEachIn(_J _j, Consumer<_B> actionFn){
        if( _j instanceof _type && ((_type)_j).isTopLevel()){
            forEachIn(((_type) _j).astCompilationUnit(), t->true, actionFn);
            return _j;
        }
        forEachIn(_j.ast(), t->true, actionFn);
        return _j;
    }

    default <_J extends _java._node<?,?>> _J forEachIn(_J _j, Predicate<_B> matchFn, Consumer<_B> actionFn){
        if( _j instanceof _type && ((_type)_j).isTopLevel()){
            forEachIn(((_type) _j).astCompilationUnit(), matchFn, actionFn);
            return _j;
        }
        forEachIn(_j.ast(), matchFn, actionFn);
        return _j;
    }

    default <N extends Node> N forEachIn(N astNode, Consumer<_B> actionFn){
        return forEachIn(astNode, t->true, actionFn);
    }

    default <N extends Node> N forEachIn(N astNode, Predicate<_B> matchFn, Consumer<_B> actionFn){
        astNode.stream().forEach(n ->{
            Select<_B> sel = select(n);
            if( sel != null && matchFn.test(sel.selection)) {
                actionFn.accept(sel.selection);
            }
        });
        return astNode;
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Consumer<Select<_B>> selectActionFn){
        return forSelectedIn(clazz, p->true, selectActionFn);
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        _CT _t = _java.type(clazz);
        forSelectedIn( _t, matchFn, selectActionFn);
        return _t;
    }

    default <_J extends _java._node<?,?>> _J forSelectedIn(_J _j, Consumer<Select<_B>> selectActionFn){
        return forSelectedIn(_j, t->true, selectActionFn);
    }

    default <_J extends _java._node<?,?>> _J forSelectedIn(_J _j, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), matchFn, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), matchFn, selectActionFn); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        forSelectedIn(((_java._node) _j).ast(), matchFn, selectActionFn);
        return _j;
    }

    /**
     *
     * @param <N>
     * @param astNode
     * @param selectActionFn
     * @return
     */
    default <N extends Node> N forSelectedIn(N astNode, Consumer<Select<_B>> selectActionFn) {
        return forSelectedIn(astNode, t->true, selectActionFn);
    }

    default <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        astNode.stream().forEach(n ->{
            Select<_B> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }

    default List<_B> listIn(Class<?> clazz){
        return listIn(Ast.of( clazz ));
    }

    default List<_B> listIn(Class<?> clazz, Predicate<_B> _matchFn){
        return listIn(Ast.of( clazz ), _matchFn);
    }

    default List<_B> listIn(_java._node<?, ?> _j){
        return listIn(_j, t->true);
    }

    default List<_B> listIn(_java._node<?, ?> _j, Predicate<_B> _matchFn){
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit(), _matchFn);
            }
            _type _t = (_type) _j; //only possible
            return listIn (_t.ast(), _matchFn);
        }
        return listIn(_j.ast(), _matchFn);
    }

    default List<_B> listIn(Node astNode){
        return listIn(astNode, p->true);
    }

    default List<_B> listIn(Node astNode, Predicate<_B> _matchFn) {
        List<_B> list = new ArrayList<>();
        forEachIn(astNode, _matchFn, c-> list.add(c));
        return list;
    }

    default List<Select<_B>> listSelectedIn(Class<?> clazz) {
        return listSelectedIn(Ast.of(clazz));
    }

    default List<Select<_B>> listSelectedIn(Class<?> clazz, Predicate<Select<_B>> _selectMatchFn) {
        return listSelectedIn(Ast.of(clazz), _selectMatchFn);
    }

    default List<Select<_B>> listSelectedIn(Node astNode) {
        return listSelectedIn(astNode, p->true);
    }

    default List<Select<_B>> listSelectedIn(Node astNode, Predicate<Select<_B>> _selectMatchFn) {
        List<Select<_B>> list = new ArrayList<>();
        astNode.walk(Node.class, e -> {
            Select<_B> s = select(e);
            if (s != null && _selectMatchFn.test(s)) {
                list.add(s);
            }
        });
        return list;
    }

    default void printIn(Class<?> clazz) {
        forEachIn(Ast.of(clazz), e-> System.out.println(e));
    }

    default void printIn(Node astNode) {
        forEachIn(astNode, e-> System.out.println(e));
    }

    default Stream<_B> streamIn(_java._node<?, ?> _j){
        return listIn(_j).stream();
    }

    default Stream<_B> streamIn(_java._node<?, ?> _j, Predicate<_B> matchFn){
        return listIn(_j, matchFn).stream();
    }

    default Stream<_B> streamIn(Class<?> clazz){
        return listIn(clazz).stream();
    }

    default Stream<_B> streamIn(Class<?> clazz, Predicate<_B> matchFn){
        return listIn(clazz, matchFn).stream();
    }

    default Stream<_B> streamIn(Node astNode){
        return listIn(astNode).stream();
    }

    default Stream<_B> streamIn(Node astNode, Predicate<_B> matchFn){
        return listIn(astNode, matchFn).stream();
    }

    /**
     * a bot that can interact with a composite syntax with other (nested) bots for it's individual parts
     * for instance, a $methodCall is a bot that represents a method call, it has (4) internal part-bots
     * <UL>
     *     <LI>{@link $methodCall#scope}</LI>
     *     <LI>{@link $methodCall#typeArguments}</LI>
     *     <LI>{@link $methodCall#name}</LI>
     *     <LI>{@link $methodCall#arguments}</LI>
     * </UL>
     *  when we test to see if any given {@link _methodCall} is a match, we ask each part-Bot to inspect each part
     *  and see if they match, if they DO, then we know the {@link _methodCall} matches
     * (NanoMachines Son) sorry had to
     */
    interface $multiBot<MB, _MB, $MB extends Template<_MB>> extends $bot<MB, _MB, $MB>, $selector<_MB, $MB>, Template<_MB>{

        /**
         * lists all the embedded bots that can help inspect or mutate the direct parts
         * of the main (parent) bot
         * the $ prefix is because we are not commanding the bot to do anything
         * but rather operating on the bot itslef
         */
        List<$bot> $listBots();

        default boolean isMatchAny(){
            if($listBots().stream().allMatch($b-> $b.isMatchAny()) ){
                try{
                    return getPredicate().test(null);
                }catch(Exception e){ }
            }
            return false;
        }

        default $MB $(String target, String $name){
            $listBots().forEach(b -> b.$(target, $name));
            return ($MB)this;
        }

        default List<String> $list(){
            List<String> strs = new ArrayList<>();
            $listBots().forEach(b -> strs.addAll( b.$list() ));
            return strs;
        }

        default List<String> $listNormalized(){
            List<String> strs = new ArrayList<>();
            $listBots().forEach(b -> strs.addAll( b.$list() ));
            return strs.stream().distinct().collect(Collectors.toList());
        }
    }

    /**
     * a bot that operates on a {@link Node} implementation within the AST
     * can remove and replace nodes in the (overall) AST in addition to selecting nodes within the AST
     *
     * @param <P>
     * @param <_P>
     * @param <$P>
     */
    interface $node<P extends Node, _P extends _java._node, $P extends $node<P,_P,$P>>
            extends $bot<P, _P, $P> {

        /** does this individual Ast node match the prototype? */
        Select<_P> select(Node n);

        /**
         * ==============================================================================================
         * The following methods are "ACTIONS" or will perform WALKING queries/manipulations performed
         * IN/ON syntax structures (AST or _java._astNode's)
         * THESE actions assume the prototype "Target" is mapped to a single Ast Node
         * (so it can be removed or replaced with another Node
         * ==============================================================================================
         */
        default <_CT extends _type<?,?>> _CT removeIn(Class<?> clazz) {
            _CT _ct = _java.type(clazz);
            removeIn(_ct );
            return _ct;
        }

        default <_J extends _java._node<?,?>> _J removeIn(_J _j) {
            if( _j instanceof _codeUnit){
                if( ((_codeUnit) _j).isTopLevel()){
                    forEachIn(((_codeUnit) _j).astCompilationUnit(), p->true, n-> n.ast().removeForced());
                    return _j;
                }
            }
            forEachIn(_j.ast(), p->true, n-> n.ast().removeForced());
            return _j;
        }

        default <_CT extends _type<?,?>> _CT removeIn(Class<?> clazz, Predicate<_P> _matchFn) {
            _CT _ct = _java.type(clazz);
            removeIn(_ct, _matchFn);
            return _ct;
        }

        default <_J extends _java._node<?,?>> _J removeIn(_J _j, Predicate<_P> _matchFn) {
            if( _j instanceof _codeUnit){
                _codeUnit _c = (_codeUnit) _j;
                if( _c.isTopLevel() ){
                    forEachIn(_c.astCompilationUnit(), _matchFn, n-> n.ast().removeForced());
                    return _j;
                }
                _type _t = (_type) _j; //only possible
                forEachIn(_t.ast(), _matchFn, n-> n.ast().removeForced()); //return the TypeDeclaration, not the CompilationUnit
                return _j;
            }
            forEachIn(((_java._node) _j).ast(), _matchFn,  n-> n.ast().removeForced());
            return _j;

        }


        default <N extends Node> N removeIn(N astNode) {
            return forEachIn(astNode, p->true, n-> n.ast().removeForced());
        }

        default <N extends Node> N removeIn(N astNode, Predicate<_P> matchFn) {
            forEachIn(astNode, matchFn, n-> n.ast().removeForced());
            return astNode;
        }

        default <_CT extends _type<?,?>, _N extends _java._node<?, ?>> _CT replaceSelectedIn(Class<?> clazz, Template<_N> replaceNode) {
            _CT _ct = _java.type(clazz);
            replaceSelectedIn(_ct.astCompilationUnit(), t->true, replaceNode);
            return _ct;
        }

        default <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, Node replaceNode) {
            return forEachIn(clazz, p-> p.ast().replace(replaceNode.clone()));
        }

        default <_J extends _java._node,  _N extends _java._node<?, ?>> _J replaceIn(_J _j, Template<_N> _t) {
            if( _j instanceof _codeUnit){
                if( ((_codeUnit) _j).isTopLevel() ) {
                    replaceSelectedIn(((_codeUnit) _j).astCompilationUnit(), t -> true, _t);
                    return _j;
                }
            }
            replaceSelectedIn(_j.ast(), t->true, _t);
            return _j;
        }

        default <_CT extends _type<?,?>, _N extends _java._node<?, ?>> _CT replaceIn(Class<?> clazz, Template<_N> _t) {
            _CT _ct = _java.type(clazz);
            replaceSelectedIn(_ct.astCompilationUnit(), t->true, _t);
            return _ct;
        }

        default <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, _java._node<?, ?> _replace) {
            return forEachIn(clazz, p-> p.ast().replace(_replace.ast().clone()));
        }

        default <N extends Node, _N extends _java._node> N replaceIn(N node, Template<_N> template) {
            return replaceSelectedIn(node, template);
        }

        default <N extends Node> N replaceIn(N node, P replaceNode) {
            return forEachIn(node, p-> p.ast().replace(replaceNode.clone()));
        }

        default <N extends Node, _CT extends _type<?,?>> N replaceIn(N node, _P _replace) {
            return forEachIn(node, p-> p.ast().replace(_replace.ast().clone()));
        }

        default <_CT extends _type<?,?>> _CT replaceSelectedIn(Class<?> clazz, Function<Select<_P>, Node> replaceDeriver) {
            _CT _ct = _java.type(clazz);
            return replaceSelectedIn(_ct, replaceDeriver);
        }

        /**
         * @param astNode
         * @return
         */
        default Select<_P> selectFirstIn(Node astNode, Predicate<Select<_P>> matchFn) {
            Optional<Node> node = astNode.stream().filter(s -> {
                Select<_P> sel = select(s);
                return sel != null && matchFn.test(sel);
            }).findFirst();

            if (node.isPresent()) { //double checking (i.e. perf hit could I remove this?)
                return select(node.get());
            }
            return null;
        }

        default <N extends Node, _N extends _java._node<?, ?>> N replaceSelectedIn(N astNode, Predicate<Select<_P>> selectMatchFn, Template<_N> nodeTemplate) {
            forSelectedIn(astNode, selectMatchFn, s->{
                s.selection.ast().replace( nodeTemplate.draft(s.tokens).ast() );
            });
            return astNode;
        }

        default <N extends Node> N replaceSelectedIn(N astNode, Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver) {
            forSelectedIn(astNode, selectMatchFn, s->{
                s.selection.ast().replace( replaceDeriver.apply(s ) );
            });
            return astNode;
        }

        default void describeIn(Node astNode) {
            forEachIn(astNode, e-> Walk.describe(e));
        }

        default void describeIn(Class<?> clazz) {
            forEachIn(Ast.of(clazz), e-> Walk.describe(e));
        }

        default <_J extends _java._node<?,?>> _J replaceSelectedIn(_J _j, Function<Select<_P>, Node> replaceDeriver) {
            if(_j instanceof _codeUnit){
                if( ((_codeUnit)_j).isTopLevel() ){
                    replaceSelectedIn(((_codeUnit) _j).astCompilationUnit(), p->true, replaceDeriver);
                    return _j;
                }
            }
            replaceSelectedIn(_j.ast(), p->true, replaceDeriver);
            return _j;
        }

        default <_J extends _java._node<?,?>> _J replaceSelectedIn(_J _j, Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver) {
            if(_j instanceof _codeUnit){
                if( ((_codeUnit)_j).isTopLevel() ){
                    replaceSelectedIn(((_codeUnit) _j).astCompilationUnit(), selectMatchFn, replaceDeriver);
                    return _j;
                }
            }
            replaceSelectedIn(_j.ast(), selectMatchFn, replaceDeriver);
            return _j;

            //replaceSelectedIn(_j.ast(), selectMatchFn, replaceDeriver);
            //return _j;
        }

        default <N extends Node> N replaceSelectedIn(N astNode, Function<Select<_P>, Node> replaceDeriver) {
            return replaceSelectedIn(astNode, p->true, replaceDeriver);
        }

        default <N extends Node, _N extends _java._node<?, ?>> N replaceSelectedIn(N astNode, Template<_N> nodeTemplate) {
            return replaceSelectedIn(astNode, t->true, nodeTemplate);
        }
    }
}