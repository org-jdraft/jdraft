package org.jdraft.prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jdraft.*;
import org.jdraft.text.Template;

import com.github.javaparser.ast.Node;

public interface $proto<P extends Node, _P extends _java._node, $P extends $proto<P,_P,$P>>
        extends $selector<_P,$P>, Template<_P> {

    Select<_P> select(Node n);

    /**
     * ==============================================================================================
     * The following methods are "ACTIONS" or will perform WALKING queries/manipulations performed
     * IN/ON syntax structures (AST or _java._astNode's)
     * ==============================================================================================
     */

    /** */
    default _P firstIn(Class<?> clazz) {
        return firstIn(clazz, p->true);
    }

    /** */
    default _P firstIn(Class<?> clazz, Predicate<_P> matchFn) {
        return firstIn( Ast.of(clazz), matchFn);
    }

    /**
     *
     * @param astNode
     * @return
     */
    default _P firstIn(Node astNode) {
        return firstIn(astNode, p->true);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param matchFn
     * @return  the first Expression that matches (or null if none found)
     */
    default _P firstIn(Node astNode, Predicate<_P> matchFn) {
        Select<_P> sel = selectFirstIn(astNode, (s)-> matchFn.test(s._sel));
        if( sel != null ) {
            return sel._sel;
        }
        return null;
    }

    default Select<_P> selectFirstIn(_java._domain _j ) {
        return selectFirstIn(_j, t->true);
    }

    default Select<_P> selectFirstIn(_java._domain _j, Predicate<Select<_P>> matchFn) {
        return selectFirstIn(_j, matchFn);
    }

    /** */
    default Select<_P> selectFirstIn(Class<?> clazz) {
        return selectFirstIn(clazz, p->true);
    }

    default Select<_P> selectFirstIn(Class<?> clazz, Predicate<Select<_P>> matchFn) {
        return selectFirstIn( Ast.of(clazz), matchFn);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Expression that matches (or null if none found)
     */
    default Select<_P> selectFirstIn(Node astNode) {
        return selectFirstIn(astNode, t-> true);
    }

    /**
     * @param astNode
     * @return
     */
    default Select<_P> selectFirstIn(Node astNode, Predicate<Select<_P>> matchFn) {
        Optional<Node> node = astNode.stream().filter(s->{
            Select<_P> sel = select(s);
            return sel != null && matchFn.test(sel);
        }).findFirst();

        if( node.isPresent() ) { //double checking (i.e. perf hit could I remove this?)
            return select(node.get());
        }
        return null;
    }

    default int countIn(Class<?> clazz) {
        return countIn(Ast.of(clazz));
    }

    default int countIn(Class<?> clazz, Predicate<_P> _matchFn) {
        return countIn(Ast.of(clazz), _matchFn);
    }

    default int countIn(_java._node<?, ?> _j) {
        return countIn(_j.ast());
    }

    default int countIn(_java._node<?, ?> _j, Predicate<_P> _matchFn) {
        return countIn(_j.ast(), _matchFn);
    }

    default int countIn(Node n) {
        return countIn(n, p->true);
    }

    default int countIn(Node n, Predicate<_P> _matchFn) {
        AtomicInteger ai = new AtomicInteger();
        forEachIn(n, _matchFn, e-> ai.incrementAndGet() );
        return ai.get();
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Consumer<_P> actionFn){
        return forEachIn(clazz, p->true, actionFn);
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Predicate<_P> matchFn, Consumer<_P> actionFn){
        _CT _t = _java.type(clazz);
        forEachIn( _t, matchFn, actionFn);
        return _t;
    }

    default <_J extends _java._node<?,?>> _J forEachIn(_J _j, Predicate<_P> matchFn, Consumer<_P> actionFn){
        forEachIn(_j.ast(), matchFn, actionFn);
        return _j;
    }

    default <N extends Node> N forEachIn(N astNode, Consumer<_P> actionFn){
        return forEachIn(astNode, t->true, actionFn);
    }

    default <N extends Node> N forEachIn(N astNode, Predicate<_P> matchFn, Consumer<_P> actionFn){
        astNode.stream().forEach(n ->{
            Select<_P> sel = select(n);
            if( sel != null && matchFn.test(sel._sel)) {
                actionFn.accept(sel._sel);
            }
        });
        return astNode;
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Consumer<Select<_P>> selectActionFn){
        return forSelectedIn(clazz, p->true, selectActionFn);
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Predicate<Select<_P>> matchFn, Consumer<Select<_P>> selectActionFn){
        _CT _t = _java.type(clazz);
        forSelectedIn( _t, matchFn, selectActionFn);
        return _t;
    }

    default <_J extends _java._node<?,?>> _J forSelectedIn(_J _j, Consumer<Select<_P>> selectActionFn){
        return forSelectedIn(_j, t->true, selectActionFn);
    }

    default <_J extends _java._node<?,?>> _J forSelectedIn(_J _j, Predicate<Select<_P>> matchFn, Consumer<Select<_P>> selectActionFn){
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
    default <N extends Node> N forSelectedIn(N astNode, Consumer<Select<_P>> selectActionFn) {
        return forSelectedIn(astNode, t->true, selectActionFn);
    }

    default <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_P>> matchFn, Consumer<Select<_P>> selectActionFn){
        astNode.stream().forEach(n ->{
            Select<_P> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }

    default List<_P> listIn(Class<?> clazz){
        return listIn(Ast.of( clazz ));
    }

    default List<_P> listIn(Class<?> clazz, Predicate<_P> _matchFn){
        return listIn(Ast.of( clazz ), _matchFn);
    }

    default List<_P> listIn(_java._node<?, ?> _j){
        return listIn(_j.ast());
    }

    default List<_P> listIn(_java._node<?, ?> _j, Predicate<_P> _matchFn){
        return listIn(_j.ast(), _matchFn);
    }

    default List<_P> listIn(Node astNode){
        return listIn(astNode, p->true);
    }

    default List<_P> listIn(Node astNode, Predicate<_P> _matchFn) {
        List<_P> list = new ArrayList<>();
        forEachIn(astNode, _matchFn, c-> list.add(c));
        return list;
    }

    default List<Select<_P>> listSelectedIn(Class<?> clazz) {
        return listSelectedIn(Ast.of(clazz));
    }

    default List<Select<_P>> listSelectedIn(Class<?> clazz, Predicate<Select<_P>> _selectMatchFn) {
        return listSelectedIn(Ast.of(clazz), _selectMatchFn);
    }

    default List<Select<_P>> listSelectedIn(Node astNode) {
        return listSelectedIn(astNode, p->true);
    }

    default List<Select<_P>> listSelectedIn(Node astNode, Predicate<Select<_P>> _selectMatchFn) {
        List<Select<_P>> list = new ArrayList<>();
        astNode.walk(Node.class, e -> {
            Select<_P> s = select(e);
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


    default Stream<_P> streamIn(_java._node<?, ?> _j){
        return listIn(_j).stream();
    }

    default Stream<_P> streamIn(_java._node<?, ?> _j, Predicate<_P> matchFn){
        return listIn(_j, matchFn).stream();
    }

    default Stream<_P> streamIn(Class<?> clazz){
        return listIn(clazz).stream();
    }

    default Stream<_P> streamIn(Class<?> clazz, Predicate<_P> matchFn){
        return listIn(clazz, matchFn).stream();
    }

    default Stream<_P> streamIn(Node astNode){
        return listIn(astNode).stream();
    }

    default Stream<_P> streamIn(Node astNode, Predicate<_P> matchFn){
        return listIn(astNode, matchFn).stream();
    }

    default <_CT extends _type<?,?>> _CT removeIn(Class<?> clazz) {
        _CT _ct = _java.type(clazz);
        removeIn(_ct );
        return _ct;
    }

    default <_CT extends _type<?,?>> _CT removeIn(Class<?> clazz, Predicate<_P> _matchFn) {
        _CT _ct = _java.type(clazz);
        removeIn(_ct, _matchFn);
        return _ct;
    }

    default <_J extends _java._node<?,?>> _J removeIn(_J _j) {
        forEachIn(_j.ast(), p->true, n-> n.ast().removeForced());
        return _j;
    }

    default <_J extends _java._node<?,?>> _J removeIn(_J _j, Predicate<_P> _matchFn) {
        forEachIn(_j.ast(), _matchFn, n-> n.ast().removeForced());
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
        replaceSelectedIn(_ct.ast(), t->true, replaceNode);
        return _ct;
    }

    default <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, Node replaceNode) {
        return forEachIn(clazz, p-> p.ast().replace(replaceNode.clone()));
    }

    default <_J extends _java._node,  _N extends _java._node<?, ?>> _J replaceIn(_J _j, Template<_N> _t) {
        replaceSelectedIn(_j.ast(), t->true, _t);
        return _j;
    }

    default <_CT extends _type<?,?>, _N extends _java._node<?, ?>> _CT replaceIn(Class<?> clazz, Template<_N> _t) {
        _CT _ct = _java.type(clazz);
        replaceSelectedIn(_ct.ast(), t->true, _t);
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

    default <_J extends _java._node<?,?>> _J replaceSelectedIn(_J _j, Function<Select<_P>, Node> replaceDeriver) {
        replaceSelectedIn(_j.ast(), p->true, replaceDeriver);
        return _j;
    }

    default <_J extends _java._node<?,?>> _J replaceSelectedIn(_J _j, Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver) {
        replaceSelectedIn(_j.ast(), selectMatchFn, replaceDeriver);
        return _j;
    }

    default <N extends Node> N replaceSelectedIn(N astNode, Function<Select<_P>, Node> replaceDeriver) {
        return replaceSelectedIn(astNode, p->true, replaceDeriver);
    }

    default <N extends Node, _N extends _java._node<?, ?>> N replaceSelectedIn(N astNode, Template<_N> nodeTemplate) {
        return replaceSelectedIn(astNode, t->true, nodeTemplate);
    }

    default <N extends Node, _N extends _java._node<?, ?>> N replaceSelectedIn(N astNode, Predicate<Select<_P>> selectMatchFn, Template<_N> nodeTemplate) {
        forSelectedIn(astNode, selectMatchFn, s->{
            s._sel.ast().replace( nodeTemplate.draft(s.tokens).ast() );
        });
        return astNode;
    }

    default <N extends Node> N replaceSelectedIn(N astNode, Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver) {
        forSelectedIn(astNode, selectMatchFn, s->{
            s._sel.ast().replace( replaceDeriver.apply(s ) );
        });
        return astNode;
    }

    default void describeIn(Node astNode) {
        forEachIn(astNode, e-> Walk.describe(e));
    }

    default void describeIn(Class<?> clazz) {
        forEachIn(Ast.of(clazz), e-> Walk.describe(e));
    }

}

