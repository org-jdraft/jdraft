package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import org.jdraft.*;
import org.jdraft.io._batch;
import org.jdraft.text.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * B is the underlying (JavaParser AST) syntactic representation
 * _B is the _java.domain wrapper for the syntax entity
 * $B the bot type
 *
 * @param <B> the underlying syntax type (most often a JavaParser {@link Node})
 * @param <_B> the _java.domain type the _jdraft domain type
 * @param <$B> the $bot type the bot type (used to return itself or a copy of the same type from default methods)
 */
public interface $bot<B, _B, $B>
        extends $selector<_B, $B>, Template<_B> {

    /**
     * The quintessential bot function. look at an entity and verify that it matches
     * and "extract"/return the underlying features as Tokens based on its expected structure
     * @param n
     * @return
     */
    Select<_B> select(Node n);

    /**
     * Build a and return a new independent immutable copy this bot
     * @return
     */
    $B copy();

    /*** update the underlying structure / template i.e. updating the parameterization ***/

    /**
     * hardcode the parameterized values and return
     * @param map
     * @return
     */
    default $B $hardcode(Map map ){
        return $hardcode( Translator.DEFAULT_TRANSLATOR, Tokens.of(map) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $B $hardcode(Tokens kvs ) {
        return $hardcode( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $B $hardcode(Object... keyValues ) {
        return $hardcode( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $B $hardcode(Translator translator, Object... keyValues ) {
        return $hardcode( translator, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator
     * @param kvs
     * @return
     */
    $B $hardcode(Translator translator, Tokens kvs );

    /**
     * Parameterizes (2) targets and parameters
     *
     * @param target1
     * @param $paramName1
     * @param target2
     * @param $paramName2
     * @return
     */
    default $B $(String target1, String $paramName1, String target2, String $paramName2){
        return ($B)$(target1,$paramName1).$(target2, $paramName2);
    }

    /**
     * Parameterizes (3) targets and parameters
     *
     * @param target1
     * @param $paramName1
     * @param target2
     * @param $paramName2
     * @return
     */
    default $B $(String target1, String $paramName1, String target2, String $paramName2, String target3, String $paramName3){
        return ($B)$(target1,$paramName1).$(target2, $paramName2).$(target3, $paramName3);
    }

    /**
     * Parameterizes (4) targets and parameters
     *
     * @param t1
     * @param $p1
     * @param t2
     * @param $p2
     * @param t3
     * @param $p3
     * @param t4
     * @param $p4
     * @return
     */
    default $B $(String t1, String $p1, String t2, String $p2, String t3, String $p3, String t4, String $p4){
        return ($B)$(t1,$p1).$(t2, $p2).$(t3, $p3).$(t4, $p4);
    }

    /* ---------------------------------------------Queries ---------------------------------------------- */

    default _B firstIn(_batch... _bs) {
        return firstIn(t->true, _bs);
    }

    /**
     *
     * TODO I COULD do some kinda fork join stuff, but for now, just sequential
     * @param _matchFn match function to apply (in addition to the $bot)
     * @param _batches the batches to load code from
     * @return the first instance of the predicate
     */
    default _B firstIn(Predicate<_B> _matchFn, _batch... _batches) {
        for(int i=0;i<_batches.length; i++){
            _codeUnits _cus = _batches[i].load();
            _B _f = firstIn(_cus, _matchFn);
            if( _f != null ){
                return _f;
            }
        }
        return null;
    }

    /**
     * retrieves the first within the codeUnit provider
     * @param _cus
     * @return
     */
    default _B firstIn(_codeUnits _cus) {
        return firstIn(_cus, t->true);
    }

    /**
     *
     * @param _cus
     * @param matchFn
     * @return
     */
    default _B firstIn(_codeUnits _cus, Predicate<_B> matchFn) {
        Optional<_codeUnit> ocu =
                _cus.stream().filter(_cu -> firstIn(_cu.astCompilationUnit(), matchFn) != null).findFirst();
        if( ocu.isPresent() ){ //I foudn the matching CompilationUnit, now return the underlying first
            return (_B) firstIn( ocu.get().astCompilationUnit() );
        }
        return null;
    }

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

    /**
     * loads each of the defined batches in order and returns the first instance of the found bot
     * (or null if not found)
     * NOTE: only loads batches that are necessary (i.e. if found in the first batch, then doesn't load other batches
     * (2, 3, 4...)
     * @param _batches the batches to load code from
     * @return the first instance of the predicate
     */
    default Select<_B> selectFirstIn(_batch... _batches) {
        return selectFirstIn(t->true, _batches);
    }

    /**
     * loads each of the defined batches in order and returns the first instance of the found bot
     * (or null if not found)
     * NOTE: only loads batches that are necessary (i.e. if found in the first batch, then doesn't load other batches
     * (2, 3, 4...)
     * @param _matchFn match function to apply (in addition to the $bot)
     * @param _batches the batches to load code from
     * @return the first instance of the predicate
     */
    default Select<_B> selectFirstIn(Predicate<Select<_B>> _matchFn, _batch... _batches) {
        for(int i=0;i<_batches.length; i++){
            _codeUnits _cus = _batches[i].load();
            Select<_B>  _f = selectFirstIn(_cus, _matchFn);
            if( _f != null ){
                return _f;
            }
        }
        return null;
    }

    default Select<_B> selectFirstIn(_codeUnits _cus){
        return selectFirstIn(_cus, t->true);
    }

    default Select<_B> selectFirstIn(_codeUnits _cus, Predicate<Select<_B>> matchFn) {
        //this should probably (eventually) be first() methods
        Optional<_codeUnit> ocu = _cus.stream().filter(_cu -> selectFirstIn(_cu, matchFn)!= null ).findFirst();
        if( ocu.isPresent() ){
            return selectFirstIn( ocu.get().astCompilationUnit(), matchFn);
        }
        return null;
    }

    default Select<_B> selectFirstIn(_java._domain _j) {
        return selectFirstIn(_j, t->true);
    }

    default Select<_B> selectFirstIn(_java._domain _j, Predicate<Select<_B>> matchFn) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return selectFirstIn( ((_codeUnit) _j).astCompilationUnit(), matchFn);
            }
        }
        return selectFirstIn( ((_java._node)_j).ast(), matchFn);
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
     * @return the first Expression that matches (or null if none found)
     */
    default Select<_B> selectFirstIn(Node astNode) {
        return selectFirstIn(astNode, t-> true);
    }

    /**
     *
     * @param astNode
     * @param predicate
     * @return
     */
    Select<_B> selectFirstIn(Node astNode, Predicate<Select<_B>>predicate);

    /**
     *
     * @param _matchFn
     * @param _batches
     * @return
     */
    default int countIn(Predicate<_B> _matchFn, _batch..._batches) {
        AtomicInteger ai = new AtomicInteger();
        Arrays.stream(_batches).forEach(b-> ai.getAndAdd( countIn(b.load(), _matchFn) ) );
        return ai.get();
    }

    /**
     *
     * @param _cus
     * @return
     */
    default int countIn(_codeUnits _cus) {
        return countIn( _cus, t->true);
    }

    /**
     *
     * @param _cus
     * @param _matchFn
     * @return
     */
    default int countIn(_codeUnits _cus, Predicate<_B> _matchFn) {
        AtomicInteger ai = new AtomicInteger();
        _cus.forEach(_cu -> ai.addAndGet( countIn(_cu.astCompilationUnit(), _matchFn) ) );
        return ai.get();
    }

    /**
     *
     * @param clazz
     * @return
     */
    default int countIn(Class<?> clazz) {
        _type _t = _type.of(clazz);
        return countIn(_t);
    }

    default int countIn(Class<?> clazz, Predicate<_B> _matchFn) {
        return countIn((_type) _type.of(clazz), _matchFn);
    }

    default int countIn(_java._node<?, ?> _j) {
        if( _j instanceof _codeUnit){
            _codeUnit _t = (_codeUnit)_j;
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

    default _codeUnits forEachIn(Consumer<_B> actionFn, _batch..._batches){
        return forEachIn(t->true, actionFn, _batches);
    }

    default _codeUnits forEachIn(Predicate<_B> matchFn, Consumer<_B> actionFn, _batch..._batches){
        _codeUnits _cus = new _codeUnits();
        Arrays.stream(_batches).forEach( (b) -> _cus.add( forEachIn( b.load(), matchFn, actionFn) ) );
        return _cus;
    }


    default _codeUnits forEachIn(_codeUnits _cus, Consumer<_B> actionFn){
        return forEachIn( _cus, t->true, actionFn);
    }

    default _codeUnits forEachIn(_codeUnits _cus, Predicate<_B> matchFn, Consumer<_B> actionFn){
        _cus.forEach( (_codeUnit _cu) -> forEachIn( _cu.astCompilationUnit(), matchFn, actionFn) );
        return _cus;
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Consumer<_B> actionFn){
        return forEachIn(clazz, p->true, actionFn);
    }

    default <_CT extends _type<?,?>> _CT forEachIn(Class<?> clazz, Predicate<_B> matchFn, Consumer<_B> actionFn){
        _CT _t = _type.of(clazz);
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

    /**
     *
     * @param selectActionFn
     * @param _batches
     * @return
     */
    default _codeUnits forSelectedIn(Consumer<Select<_B>> selectActionFn, _batch..._batches){
        return forSelectedIn(t->true, selectActionFn, _batches);
    }

    /**
     *
     * @param matchFn
     * @param selectActionFn
     * @param _batches
     * @return
     */
    default _codeUnits forSelectedIn(Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn, _batch..._batches){
        _codeUnits _cus = new _codeUnits();
        Arrays.stream(_batches).forEach( b-> _cus.add(forSelectedIn(matchFn, selectActionFn)));
        return _cus;
    }

    default _codeUnits forSelectedIn(_codeUnits _cus, Consumer<Select<_B>> selectActionFn){
        return forSelectedIn(_cus, t->true, selectActionFn);
    }

    default _codeUnits forSelectedIn(_codeUnits _cus, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        _codeUnits _ncu = new _codeUnits();
        _cus.forEach(_c-> _ncu.add(forSelectedIn(_c.astCompilationUnit(), matchFn, selectActionFn) ));
        return _ncu;
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Consumer<Select<_B>> selectActionFn){
        return forSelectedIn(clazz, p->true, selectActionFn);
    }

    default <_CT extends _type<?,?>> _CT forSelectedIn(Class<?> clazz, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        _CT _t = _type.of(clazz);
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

    /**
     *
     * @param astNode
     * @param matchFn
     * @param selectActionFn
     * @param <N>
     * @return
     */
    default <N extends Node> N forSelectedIn(N astNode, Predicate<Select<_B>> matchFn, Consumer<Select<_B>> selectActionFn){
        astNode.stream().forEach(n ->{
            Select<_B> sel = select(n);
            if( sel != null && matchFn.test(sel)) {
                selectActionFn.accept(sel);
            }
        });
        return astNode;
    }

    default List<_B> listIn(_batch..._batches){
        return listIn(t->true, _batches);
    }

    default List<_B> listIn(Predicate<_B> _matchFn, _batch..._batches){
        List<_B> list = new ArrayList<>();
        Arrays.stream(_batches).forEach(b -> list.addAll( listIn(b.load() ,_matchFn)));
        return list;
    }

    default List<_B> listIn(_codeUnits _cus){
        return listIn(_cus, t->true);
    }

    default List<_B> listIn(_codeUnits _cus, Predicate<_B> _matchFn){
        List<_B> fullList = new ArrayList<>();
        _cus.forEach(_cu -> fullList.addAll( listIn(_cu.astCompilationUnit(), _matchFn) ));
        return fullList;
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

    default List<Select<_B>> listSelectedIn(_batch..._batches) {
        return listSelectedIn(t->true, _batches);
    }

    default List<Select<_B>> listSelectedIn(Predicate<Select<_B>> _selectMatchFn, _batch..._batches) {
        List<Select<_B>> selected = new ArrayList<>();
        Arrays.stream(_batches).forEach(_b -> selected.addAll(listSelectedIn(_b.load(), _selectMatchFn)));
        return selected;
    }

    default List<Select<_B>> listSelectedIn(_codeUnits _cus) {
        return listSelectedIn(_cus, t->true);
    }

    default List<Select<_B>> listSelectedIn(_codeUnits _cus, Predicate<Select<_B>> _selectMatchFn) {
        List<Select<_B>> lc = new ArrayList<>();
        _cus.forEach(_cu-> lc.addAll( listSelectedIn(_cu.astCompilationUnit(), _selectMatchFn) ) );
        return lc;
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

    default void printIn(_batch... _batches ){
        Arrays.stream(_batches).forEach(_b -> printIn(_b.load()) );
    }

    default void printIn(_codeUnits _cus ){
        _cus.forEach(_cu -> printIn(_cu.astCompilationUnit()) );
    }

    default void printIn(_java._node _jn) {
        if( ( _jn instanceof _codeUnit) && ( ((_codeUnit)_jn).isTopLevel()) ){
            printIn( ((_codeUnit)_jn).astCompilationUnit());
        } else{
            printIn(_jn.ast());
        }
    }

    default void printIn(Class<?> clazz) {
        forEachIn(Ast.of(clazz), e-> System.out.println(e));
    }

    default void printIn(Node astNode) {
        forEachIn(astNode, e-> System.out.println(e));
    }

    default void printEachTreeIn(_codeUnits _cus){
        _cus.forEach(_c -> printEachTreeIn( (_java._node)_c) );
    }

    default void printEachTreeIn(_java._node _n) {
        forEachIn(_n, e-> Print.tree( (_java._node)e));
    }

    default void printEachTreeIn(Node astNode) {
        forEachIn(astNode, e-> Print.tree( (_java._node)e));
    }

    default void printEachTreeIn(Class<?> clazz) {
        forEachIn(Ast.of(clazz), e-> Print.tree( (_java._node)e));
    }

    default Stream<_B> streamIn(_batch..._batches){
        return listIn(_batches).stream();
    }

    default Stream<_B> streamIn( Predicate<_B> matchFn, _batch..._batches){
        return listIn(matchFn,_batches).stream();
    }

    default Stream<_B> streamIn(_codeUnits _cus){
        return streamIn(_cus, t->true);
    }

    default Stream<_B> streamIn(_codeUnits _cus, Predicate<_B> matchFn){
        return listIn(_cus, matchFn).stream();
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


        default void printEachTreeIn(_batch..._batches){
            Arrays.stream(_batches).forEach(_b -> printEachTreeIn(_b.load()));
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
            _CT _ct = _type.of(clazz);
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

        default _codeUnits removeIn(_batch..._batches) {
            return removeIn(t->true, _batches);
        }

        default _codeUnits removeIn(Predicate<_P> _matchFn, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            removeIn(_cus, _matchFn);
            return _cus;
        }

        default _codeUnits removeIn(_codeUnits _cup){
            return removeIn(_cup, t->true);
        }

        /** TODO remove from _codeUnits ?? cleanup */
        default _codeUnits removeIn(_codeUnits _cup, Predicate<_P> _matchFn) {
            _cup.forEach(_cu -> removeIn(_cu.astCompilationUnit(), _matchFn) );

            //TODO If I encounter a _compilationUnit that is not a package_info or a Module_info
            //and it has not internal _type, I should remove it from _cup before returning

            return _cup;
        }

        default <_CT extends _type<?,?>> _CT removeIn(Class<?> clazz, Predicate<_P> _matchFn) {
            _CT _ct = _type.of(clazz);
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

        default _codeUnits replaceIn(P replaceNode, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            replaceIn(_cus, replaceNode);
            return _cus;
        }

        default <_N extends _java._node> _codeUnits replaceIn(Template<_N> _t, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            replaceIn(_cus, _t);
            return _cus;
        }

        default _codeUnits replaceIn(_codeUnits _cus, P replaceNode) {
            _cus.forEach(_cu-> replaceIn(_cu.astCompilationUnit(), replaceNode) );
            return _cus;
        }

        default <_N extends _java._node> _codeUnits replaceIn(_codeUnits _cus, Template<_N> _t) {
            _cus.forEach(_cu -> replaceIn(_cu.astCompilationUnit(), _t) );
            return _cus;
        }

        default <_CT extends _type<?,?>> _CT replaceIn(Class<?> clazz, Node replaceNode) {
            return forEachIn(clazz, p-> p.ast().replace(replaceNode.clone()));
        }

        default <_J extends _java._node> _J replaceIn(_J _j, String...replacement) {
            if( _j instanceof _codeUnit){
                if( ((_codeUnit) _j).isTopLevel() ) {
                    forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
                        String drafted = Stencil.of(replacement).draft( s.tokens );
                        //assume drafted is a node of the same type
                        boolean replaced = false;
                        Node n = null;
                        try {
                            //assume the replacement will be of the same type
                            n = _java.node(s.selection.ast().getClass(), drafted);
                            replaced = s.selection.ast().replace( n );
                        }catch(Exception e){
                            throw new _jdraftException("Could not parse String :"+System.lineSeparator()+ Text.combine(replacement)+" as "+s.selection.ast().getClass());
                        }
                        if( ! replaced ){
                            throw new _jdraftException("could not replace "+s.selection+" with "+ n);
                        }
                    });
                    return _j;
                }
            }
            forSelectedIn(((_codeUnit) _j).astCompilationUnit(), t->true, s->{
                String drafted = Stencil.of(replacement).draft( s.tokens );
                //assume drafted is a node of the same type
                boolean replaced = false;
                Node n = null;
                try {
                    //assume the replacement will be of the same type
                    n = _java.node(s.selection.ast().getClass(), drafted);
                    replaced = s.selection.ast().replace( n );
                }catch(Exception e){
                    throw new _jdraftException("Could not parse String :"+System.lineSeparator()+ Text.combine(replacement)+" as "+s.selection.ast().getClass());
                }
                if( ! replaced ){
                    throw new _jdraftException("could not replace "+s.selection+" with "+ n);
                }
            });
            return _j;
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
            _CT _ct = _type.of(clazz);
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

        default <N extends Node> N replaceIn(N node, _P _replace) {
            return forEachIn(node, p-> p.ast().replace(_replace.ast().clone()));
        }

        default Select<_P> selectFirstIn(_batch..._batches) {
            return selectFirstIn(t->true, _batches);
        }

        default Select<_P> selectFirstIn(Predicate<Select<_P>> matchFn, _batch..._batches) {
            for(int i=0;i<_batches.length;i++){
                Select<_P> sel = selectFirstIn(_batches[i].load(), matchFn);
                if( sel != null ){
                    return sel;
                }
            }
            return null;
        }

        /**
         *
         * @param _cus
         * @return
         */
        default Select<_P> selectFirstIn(_codeUnits _cus) {
            return selectFirstIn(_cus, t->true);
        }

        /**
         * @param _cus
         * @param matchFn
         * @return
         */
        default Select<_P> selectFirstIn(_codeUnits _cus, Predicate<Select<_P>> matchFn) {
            Optional<_codeUnit> ocu =
                    _cus.stream().filter(_cu -> selectFirstIn(_cu.astCompilationUnit(), matchFn) != null).findFirst();
            if( ocu.isPresent() ){ //I found the matching CompilationUnit, now return the underlying first
                return selectFirstIn( ocu.get().astCompilationUnit() );
            }
            return null;
        }

        /**
         *
         * @param astNode the node to look through
         * @return
         */
        default Select<_P> selectFirstIn(Node astNode) {
            return selectFirstIn(astNode, t->true);
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

        default <_N extends _java._node<?, ?>> _codeUnits replaceSelectedIn(Template<_N> replaceNode, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            replaceSelectedIn(_cus, replaceNode);
            return _cus;
        }

        default _codeUnits replaceSelectedIn(Function<Select<_P>, Node> replaceDeriver, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            replaceSelectedIn(_cus, replaceDeriver);
            return _cus;
        }

        default _codeUnits replaceSelectedIn(Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver, _batch..._batches) {
            _codeUnits _cus = _codeUnits.of(_batches);
            replaceSelectedIn(_cus, selectMatchFn, replaceDeriver);
            return _cus;
        }

        default <_N extends _java._node<?, ?>> _codeUnits replaceSelectedIn(_codeUnits _cus, Template<_N> replaceNode) {
            _cus.forEach(_cu-> replaceSelectedIn(_cu.astCompilationUnit(), replaceNode));
            return _cus;
        }

        default _codeUnits replaceSelectedIn(_codeUnits _cus, Function<Select<_P>, Node> replaceDeriver) {
            _cus.forEach(_cu-> replaceSelectedIn(_cu.astCompilationUnit(), replaceDeriver));
            return _cus;
        }

        default _codeUnits replaceSelectedIn(_codeUnits _cus, Predicate<Select<_P>> selectMatchFn, Function<Select<_P>, Node> replaceDeriver) {
            _cus.forEach(_cu-> replaceSelectedIn(_cu.astCompilationUnit(), selectMatchFn, replaceDeriver));
            return _cus;
        }

        default <_CT extends _type<?,?>, _N extends _java._node<?, ?>> _CT replaceSelectedIn(Class<?> clazz, Template<_N> replaceNode) {
            _CT _ct = _type.of(clazz);
            replaceSelectedIn(_ct.astCompilationUnit(), t->true, replaceNode);
            return _ct;
        }

        default <_CT extends _type<?,?>> _CT replaceSelectedIn(Class<?> clazz, Function<Select<_P>, Node> replaceDeriver) {
            _CT _ct = _type.of(clazz);
            return replaceSelectedIn(_ct, replaceDeriver);
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
        }

        default <N extends Node> N replaceSelectedIn(N astNode, Function<Select<_P>, Node> replaceDeriver) {
            return replaceSelectedIn(astNode, p->true, replaceDeriver);
        }

        default <N extends Node, _N extends _java._node<?, ?>> N replaceSelectedIn(N astNode, Template<_N> nodeTemplate) {
            return replaceSelectedIn(astNode, t->true, nodeTemplate);
        }

    }
}
