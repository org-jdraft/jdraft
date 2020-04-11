package org.jdraft.bot;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import org.jdraft.*;
import org.jdraft.pattern.$;
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

    /**
     * Is the range of this Node
     * @param _n
     * @return
     */
    default $B $isInRange(_java._node _n ){
        return $isInRange(_n.ast());
    }

    /**
     * Is the range of this Node
     * @param n
     * @return
     */
    default $B $isInRange(Node n){ ;
        if( n.getRange().isPresent() ){
            return $isInRange(n.getRange().get());
        }
        throw new _jdraftException("Node "+n+" does not have a range");
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param range
     * @return
     */
    default $B $isInRange(final Range range ){
        Predicate<_B> pp = n -> {
            if (n instanceof Node) {
                return ((Node)n).getRange().isPresent() && range.strictlyContains( ((Node) n).getRange().get());
            } else if (n instanceof _java._node) {
                Node node = ((_java._node)n).ast();
                return node.getRange().isPresent() && range.strictlyContains(node.getRange().get());
            }else if (n instanceof _body) {
                Node node = ((_body)n).ast();
                return node.getRange().isPresent() && range.strictlyContains(node.getRange().get());
            }
            return false;
        };
        return $and( pp );
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param beginLine
     * @param beginColumn
     * @param endLine
     * @param endColumn
     * @return
     */
    default $B $isInRange(int beginLine, int beginColumn, int endLine, int endColumn){
        return $isInRange(Range.range(beginLine,beginColumn,endLine, endColumn));
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param beginLine
     * @param endLine
     * @return
     */
    default $B $isInRange(int beginLine, int endLine){
        return $isInRange(Range.range(beginLine,0,endLine, Integer.MAX_VALUE -10000));
    }

    /**
     * Verifies that the ENTIRE matching pattern exists on this specific line in the code
     * @param line the line expected
     * @return the modified $pattern
     */
    default $B $atLine(int line ){
        return $isInRange(Range.range(line,0,line, Integer.MAX_VALUE -10000));
    }

    /**
     * does the candidate
     * @param packageNameStencil a specific name (i.e. "java.util" or a parameterizedName "$any$.daos")
     * @return
     */
    default $B $isInPackage(String packageNameStencil){
        _package _pk = _package.of(packageNameStencil);
        String pkgname = _pk.toString().trim();

        //lets make this a Stencil so we can match via a stencil (if the stencil has no parameters it'll match as well)
        Stencil st = Stencil.of(pkgname);
        return $isInPackage( p-> {
            return st.matches( p.toString().trim());
        } );
    }

    /**
     *
     * @param packageMatchFn
     * @return
     */
    default $B $isInPackage(Predicate<_package> packageMatchFn){
        return $and(n -> {
            //System.out.println( "N CLASS "+ n.getClass() );
            Optional<CompilationUnit> ocu = ((_java._node)n).ast().findCompilationUnit();
            if( ocu.isPresent() && ocu.get().getPackageDeclaration().isPresent() ){
                _package _p = _package.of(ocu.get().getPackageDeclaration().get());
                 return packageMatchFn.test(_p);
            }
            return false;
        });
    }

    /**
     * candidates must have imported the given classes
     * @param classNames
     * @return
     */
    default $B $isImports( String...classNames){
        return $isImports( is-> is.hasImports(classNames));
    }

    /**
     * candidates must have imported the given classes
     * @param clazzes
     * @return
     */
    default $B $isImports( Class...clazzes){
        return $isImports( is-> is.hasImports(clazzes));
    }

    /**
     * Adds a constraint for matching candidates who are defined in compilationUnits
     * that have matching _imports
     *
     * in simple terms: Does this Node/thing exist in a Class file that matches these imports
     *
     * @param _importsMatchFn function to match imports
     * @return the modified $bot instance with the constraint added
     */
    default $B $isImports(Predicate<_imports> _importsMatchFn){
        return $and(n -> {
            Optional<CompilationUnit> ocu = ((_java._node)n).ast().findCompilationUnit();
            if( ocu.isPresent() ){
                _imports _is = _imports.of(ocu.get());
                return _importsMatchFn.test(_is);
            }
            return false;
        });
    }

    //$isImports(Predicate<_imports> )
    //$isInType(Predicate<_type> )

    default $B $isParent(Class... parentClassTypes ){
        return $and(n -> {
            if (n instanceof Node) {
                return Tree.isParent( (Node)n, parentClassTypes);
            } else if (n instanceof _java._node) {
                return Tree.isParent( ((_java._node)n).ast(), parentClassTypes);
            } else if (n instanceof _body) {
                return Tree.isParent( ((_body)n).ast(), parentClassTypes);
            } else if( n instanceof _variable){
                // I NEED _java.isParent()
                return Tree.isParent( ((_variable)n).ast(), parentClassTypes);
            }
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : " + n.getClass());

        });
    }

    /**
     * Adds a constraint to test that the PARENT does NOT MATCH ANY of the bots provided
     * <PRE>
     *     class
     * </PRE>
     *
     * @param $bs
     * @return
     */
    default $B $isParentNot($bot.$node ... $bs){
        for(int i=0;i<$bs.length; i++) {
            $bot $b = $bs[i];
            Predicate<_B> pp = n -> {
                if (n instanceof Node) {
                    return Tree.isParent((Node) n, c -> $b.matches(c));
                } else if (n instanceof _java._node) {
                    return Tree.isParent(((_java._node) n).ast(), c -> $b.matches(c));
                } else if (n instanceof _body) {
                    return Tree.isParent(((_body) n).ast(), c -> $b.matches(c));
                }
                return false;
            };
            $not(pp);
        }
        return ($B)this;
    }

    /**
     * Adds a constraint to test that the parent of the instance Is this node the child of a ?
     * @param $bs the prototypes to match against
     * @return
     */
    default $B $isParent($bot... $bs ){
        return $and(n -> {
            if (n instanceof Node) {
                return Tree.isParent( (Node)n, c-> Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _java._node) {
                return Tree.isParent( ((_java._node)n).ast(), c->Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _body) {
                return Tree.isParent( ((_body)n).ast(), c->Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
            } else {
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : " + n.getClass());
            }
        });
        //return and( (n)-> Ast.isParent( (Node)n, e-> proto.match(e) ) );
    }

    default $B $hasAncestor( int levels, $bot... $bots ){
        return $and(n-> {
            if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($bots).anyMatch($b ->$b.matches(c)));
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($bots).anyMatch($b ->$b.matches(c)));
            } else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
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

    /**
     *
     * @param astNode
     * @param predicate
     * @return
     */
    Select<_B> selectFirstIn(Node astNode, Predicate<Select<_B>>predicate);

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

        default <N extends Node, _CT extends _type<?,?>> N replaceIn(N node, _P _replace) {
            return forEachIn(node, p-> p.ast().replace(_replace.ast().clone()));
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
            forEachIn(astNode, e-> Tree.describe(e));
        }

        default void describeIn(Class<?> clazz) {
            forEachIn(Ast.of(clazz), e-> Tree.describe(e));
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
