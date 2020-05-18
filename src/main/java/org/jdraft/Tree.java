package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.ASCIITreePrinter;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Tree Node Traversal Algorithms for traversing nodes of a Java AST / {@link _java._domain}
 * domain objects for selecting / visiting the nodes, providing a simple API for operations
 * on Nodes within a Java source file (by manipulating the AST).
 *
 * The use is best illustrated by example:
 * <PRE>
 * _class _c = _class.of("aaaa.bbbb.C")
 *     .fields("int x=1;", "int y=2;", "String NAME;");
 *
 * //intercept & print all of the {@link _field}s within the _class _c
 * Walk.in(_c, _field.class, f-> System.out.println(f));
 * // prints:
 * //    int x=1;
 * //    int y=2;
 * //    String NAME;
 *
 * //intercept & print all {@link _field}s within _class _c that have initial values
 * Walk.in(_c, _field.class, f-> f.hasInit(), f-> System.out.println(f));
 * // prints:
 * //    int x=1;
 * //    int y=2;
 *
 * // we are not limited to traversing based on either _model or AST classes
 * //... we can traverse the node graph looking for entities that are Node implementations
 *
 * // to find all Integer literals within the code:
 * Walk.in(_c, {@link Exprs#INT_LITERAL}, i-> System.out.println(i));
 * // prints:
 * //    1
 * //    2
 *
 * // to find all types within the code:
 * Walk.in(_c, {@link Exprs#TYPE}, i-> System.out.println(i));
 *  // prints:
 *  //    "int"
 *  //    "int"
 *  //    "String"
 * </PRE>
 * @author Eric
 */
public enum Tree {
    ;

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     */
    public static final Node.TreeTraversal PRE_ORDER = Node.TreeTraversal.PREORDER;

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * PostOrder ("leaves first", or children, then parents) from (A) : D,E,B,C,A
     * </PRE>
     */
    public static final Node.TreeTraversal POST_ORDER = Node.TreeTraversal.POSTORDER;

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     */
    public static final Node.TreeTraversal PARENTS = Node.TreeTraversal.PARENTS;

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Breadth-First (or Level Order) from (A): A,B,C,D,E
     * </PRE>
     */
    public static final Node.TreeTraversal BREADTH_FIRST = Node.TreeTraversal.BREADTHFIRST;

    /**
     * <PRE>
     *            (A)
     *           /  \
     *         (B)   C
     *         / \
     *        D   E
     * Direct Children
     *     From A: B, C
     *     From B: D, E
     * </PRE>
     */
    public static final Node.TreeTraversal DIRECT_CHILDREN = Node.TreeTraversal.DIRECT_CHILDREN;

    /**
     *
     * @param tt
     * @param node
     * @param targetClass
     * @param matchFn
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> Stream<T> stream(
            Node.TreeTraversal tt, N node, Class<T> targetClass, Predicate<T> matchFn ) {
        return list(tt, node, targetClass, matchFn).stream();
    }

    /**
     *
     * @param tt
     * @param _node
     * @param targetClass
     * @param matchFn
     * @param <T>
     * @param <_N>
     * @return
     */
    public static <T, _N extends _java._node> Stream<T> stream(
            Node.TreeTraversal tt, _N _node, Class<T> targetClass, Predicate<T> matchFn ) {
        return list(tt, _node, targetClass, matchFn).stream();
    }

    /**
     * _walk the nodes within & collect all nodes that match all the predicate and return them in order
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     * @param _j
     * @param nodeMatchFn
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> List<Node> list(_J _j, Predicate<Node> nodeMatchFn ) {
        List<Node> found = new ArrayList<>();
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), Node.class, nodeMatchFn, f -> found.add(f));
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), Node.class, nodeMatchFn, f -> found.add(f));
            }
            return found;
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), Node.class, nodeMatchFn, f -> found.add( f) );
            return found;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), Node.class, nodeMatchFn, f -> found.add(f) );
        return found;
    }

    /**
     * Perform a preorder walk through the _model _m, returning a listing all instances of
     * the targetClass found
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param _j the _model entity to search
     * @param targetClass the target Class to search for (can be a Node class, a _model class)
     * @param <T> the target TYPE
     * @param <_J> the model entity (i.e. _class, _method, _constructor, _staticBlock)
     * @return
     */
    public static <T, _J extends _java._domain> List<T> list(_J _j, Class<T> targetClass ) {
        List<T> found = new ArrayList<>();
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, f -> found.add(f));
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), targetClass, t->true, f -> found.add(f));
            }
            return (List<T>)found;
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), targetClass, t->true, f -> found.add( (T)f) );
            return found;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), targetClass, t->true, f -> found.add(f) );
        return (List<T>)found;
    }

    /**
     * Perform a preOrder walk through the astRootNode, returning a listing all instances of
     * the targetClass found
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode the _model entity to search
     * @param targetClass the target Class to search for (can be a Node class, a _model class)
     * @param <T> the target TYPE
     * @param <N> the Ast Node (i.e. EnumDeclaration, MethodDeclaration, ConstructorDeclaration)
     * @return the list of
     */
    public static <T, N extends Node> List<T> list(N astRootNode, Class<T> targetClass ) {
        List<T> found = new ArrayList<>();
        of( PRE_ORDER, astRootNode, targetClass, t->true, f -> found.add(f) );
        return found;
    }

    /**
     * Perform a preOrder walk through the astRootNode, returning a listing all instances of
     * the targetClass found
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from A : A,B,D,E,C
     * </PRE>
     * @param _j the _model entity to search
     * @param targetClass the target Class to search for (can be a Node class, a _model class)
     * @param <T> the target TYPE
     * @param <_J> the model entity (i.e. _class, _method, _constructor, _staticBlock)
     * @param matchFn predicate for selecting nodes to collect
     * @return the list of
     */
    public static <T, _J extends _java._domain> List<T> list(
            _J _j, Class<T> targetClass, Predicate<T> matchFn ) {

        List<T> found = new ArrayList<>();
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, f -> found.add( (T)f));
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), targetClass, matchFn, f -> found.add( (T)f));
            }
            return (List<T>)found;
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), targetClass, matchFn, f -> found.add( (T)f) );
            return found;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), targetClass, matchFn, f -> found.add(f) );
        return (List<T>)found;
    }

    /**
     * Perform a PreOrder walk through the astRootNode, returning a listing all instances of
     * the targetClass found that match the matchFn
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     * @param astRootNode the Ast node to start walking
     * @param targetClass the target Class to search for (can be a Node class, a _model class)
     * @param matchFn the lambda for matching specific instances of the targetClass
     * @param <T> the target TYPE
     * @param <N> the Ast Root Node Type (i.e. EnumDeclaration, MethodDeclaration, ConstructorDeclaration)
     * @return the list of
     */
    public static <T, N extends Node> List<T> list(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn ) {

        List<T> found = new ArrayList<>();
        of( PRE_ORDER, astRootNode, targetClass, matchFn, f -> found.add(f) );
        return found;
    }

    /**
     * Starting at astRootNode walk the nodes using the walk traversal strategy provided
     * @see #POST_ORDER
     * @see #PRE_ORDER
     * @see #BREADTH_FIRST
     * @see #PARENTS
     * @see #DIRECT_CHILDREN
     *
     * intercepting Nodes that implement the targetClass and collecting nodes that
     * pass the matchFn in a List and returning the list.
     *
     * @param tt the walk traversal strategy for walking nodes in the AST
     * @param astRootNode the starting node to start the walk
     * @param targetClass the target classes to intercept
     * @param matchFn function for selecting which nodes to accept and add to the list
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> List<T> list(
            Node.TreeTraversal tt, N astRootNode, Class<T> targetClass, Predicate<T> matchFn ) {

        List<T> found = new ArrayList<>();
        of( tt, astRootNode, targetClass, matchFn, f -> found.add(f) );
        return found;
    }



    public static <T, _N extends _java._node> List<T> list(
            Node.TreeTraversal tt, _N _node, Class<T> targetClass, Predicate<T> matchFn ) {
        if( _node instanceof _codeUnit && ((_codeUnit) _node).isTopLevel()){
            return list( tt, ((_codeUnit) _node).astCompilationUnit(), targetClass, matchFn);
        }
        return list( tt, _node.ast(), targetClass, matchFn);
    }

    /**
     * List all nodes that are of a particualr class
     * @param tt the traversal strategy
     * @param astStartNode
     * @param targetClass
     * @param <T> the target type
     * @param <N> the start node type
     * @return a list of matching nodes
     */
    public static <T, N extends Node> List<T> list(
            Node.TreeTraversal tt, N astStartNode, Class<T> targetClass) {
        return list( tt, astStartNode, targetClass, t-> true);
    }

    /**
     * List parents
     * @param tt
     * @param astStartNode
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> List<Node> list(
            Node.TreeTraversal tt, N astStartNode ) {
        return list( tt, astStartNode, Node.class, t-> true);
    }

    /**
     *
     * @param _sourceCode
     * @param targetClass
     * @param <T>
     * @param <_C>
     * @return
     */
    public static <T, _C extends _codeUnit> List<T> list(
            Collection<_C> _sourceCode, Class<T> targetClass ) {
        return list(_sourceCode, targetClass, t->true);
    }

    /**
     * Go through a collection of _code looking for all instances of the targetClass that matches the matchFn
     *
     * @param _sourceCode
     * @param targetClass
     * @param matchFn
     * @param <T>
     * @return
     */
    public static <T, _C extends _codeUnit> List<T> list(
            Collection<_C> _sourceCode, Class<T> targetClass, Predicate<T> matchFn ) {
        return list(PRE_ORDER, _sourceCode, targetClass, matchFn);
    }

    /**
     * Go through a collection of _code traversing using the tree traversal strategy provided
     * looking for all instances of the targetClass that matches the matchFn
     * @param tt
     * @param _sourceCode
     * @param targetClass
     * @param matchFn
     * @param <T>
     * @return a list of all matching T within all the source code
     */
    public static <T, _C extends _codeUnit> List<T> list(
            Node.TreeTraversal tt, Collection<_C> _sourceCode, Class<T> targetClass, Predicate<T> matchFn ) {
        List<T> found = new ArrayList<>();
        _sourceCode.stream().forEach( _sc -> of( tt, _sc.astCompilationUnit(), targetClass, matchFn, f-> found.add(f)));
        return found;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N in(N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( PRE_ORDER, astRootNode, targetClass, matchFn, action );
    }

    /**
     *
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode the root node to start the walk
     * @param targetNodeClass the target class
     * @param nodeActionFn what to do with matching nodfes
     * @param <RN> the root node type
     * @param <N> the target node type
     * @return the modified root astNode
     */
    public static <RN extends Node, N extends Node> RN in( RN astRootNode, Class<N> targetNodeClass, Consumer<N> nodeActionFn) {
        Tree.in(astRootNode, targetNodeClass, t -> true, nodeActionFn);
        return astRootNode;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J in(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     *
     * @param _sourceCode
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_C>
     */
    public static <T, _C extends _codeUnit> void in(Collection<_C> _sourceCode, Class<T> targetClass, Consumer<T> action ) {
        _sourceCode.forEach( _c-> in( _c, targetClass, action));
    }

    /**
     * A preorder walk within the _model _m for all entities that are of targetClass
     * and calls the action on them
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from A : A,B,D,E,C
     * </PRE>
     * @param _j
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_J> the supplied model node
     * @return
     */
    public static <T, _J extends _java._domain> _J in(_J _j, Class<T> targetClass, Consumer<T> action ) {
        return in( PRE_ORDER, _j, targetClass, action);
    }

    /**
     * walk within the _java model _j based on the for all entities that are of targetClass
     * and calls the action on them

     * @param tt the tree traversal strategy
     * @param _j the "root" _java entity
     * @param targetClass the target class to intercept
     * @param action the action to perform on the target entities
     * @param <T> the target type
     * @param <_J> the supplied _java model node
     * @return
     */
    public static <T, _J extends _java._domain> _J in(Node.TreeTraversal tt, _J _j, Class<T> targetClass, Consumer<T> action ) {
        return in( tt, _j, targetClass, t->true, action);
    }

    /**
     *
     * @param tt
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J in(Node.TreeTraversal tt, _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(tt, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(tt, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( tt, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(tt, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * A preorder walk within the _model node _m
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     * @param _j
     * @param action
     * @param <_J> the supplied model node
     * @return
     */
    public static <_J extends _java._domain> _J in(_J _j, Consumer<Node> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * A preorder walk within the _model node _m
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     * @param _j the model
     * @param nodeMatchFn node matchFn
     * @param action the action to take on the predicate
     * @param <_J>
     * @return the (modified)
     */
    public static <_J extends _java._domain> _J in(_J _j, Predicate<Node> nodeMatchFn, Consumer<Node> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), Node.class, nodeMatchFn, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), Node.class, nodeMatchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), Node.class, nodeMatchFn, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), Node.class, nodeMatchFn, action );
        return _j;
    }

    /**
     * Walks the Ast using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy provided
     * {@link Tree#PRE_ORDER}
     * {@link Tree#POST_ORDER}
     * {@link Tree#BREADTH_FIRST}
     * {@link Tree#PARENTS}
     * {@link Tree#DIRECT_CHILDREN} starting from the astRootNode, searching
     * for matching targetNodeClass and selecting those who pass the
     * nodeMatchFn, to call the nodeActionFn
     *
     * @param traversal the nodeTraversal strategy
     * @param astRootNode the starting node to start the walk
     * @param targetNodeClass a particular node class (or interface) to
     * intercept when on the walk
     * @param nodeMatchFn a predicate for matching particular nodes of the
     * nodeClass when on the walk
     * @param nodeActionFn the action to take on the selected nodes
     * @param <N> the target node type (i.e.
     * {@link com.github.javaparser.ast.expr.Expression},{@link TypeDeclaration}, {@link NodeWithOptionalBlockStmt}
     * @param <R> the root node type
     * @return the modified root AST node
     */
    public static <R extends Node, N extends Node> R in(
            Node.TreeTraversal traversal,
            R astRootNode,
            Class<N> targetNodeClass,
            Predicate<N> nodeMatchFn,
            Consumer<N> nodeActionFn) {
        return in( traversal, Integer.MAX_VALUE, astRootNode, targetNodeClass, nodeMatchFn, nodeActionFn);
    }

    public static <R extends Node, N extends Node> R in(
            Node.TreeTraversal traversal,
            int levels,
            R astRootNode,
            Class<N> targetNodeClass,
            Predicate<N> nodeMatchFn,
            Consumer<N> nodeActionFn) {

        astRootNode.stream(traversal).limit(levels).filter(n -> targetNodeClass.isAssignableFrom(n.getClass())
                && nodeMatchFn.test((N) n))
                .forEach(n->nodeActionFn.accept( (N)n ) );
        return astRootNode;
    }

    /**
     * Walks the Asts of all of the _sourceCode using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy provided
     * {@link Tree#PRE_ORDER}
     * {@link Tree#POST_ORDER}
     * {@link Tree#BREADTH_FIRST}
     * {@link Tree#PARENTS}
     * {@link Tree#DIRECT_CHILDREN} starting from the astRootNode, searching
     * for matching targetNodeClass and selecting those who pass the
     * nodeMatchFn, to call the nodeActionFn
     *
     * @param traversal the nodeTraversal strategy
     * @param _sourceCode a Collection of source code to walk each of the ASTs
     * @param targetNodeClass a particular node class (or interface) to
     * intercept when on the walk
     * @param nodeMatchFn a predicate for matching particular nodes of the
     * nodeClass when on the walk
     * @param nodeActionFn the action to take on the selected nodes
     * @param <N> the target node type (i.e.
     * {@link com.github.javaparser.ast.expr.Expression},{@link TypeDeclaration}, {@link NodeWithOptionalBlockStmt}
     * @param <_C> the _code type
     * @return the modified root AST node
     */
    public static <_C extends _codeUnit, N extends Node> void in(
            Node.TreeTraversal traversal, Collection<_C> _sourceCode, Class<N> targetNodeClass, Predicate<N> nodeMatchFn, Consumer<N> nodeActionFn){
        _sourceCode.forEach(_c-> in(traversal, _c.astCompilationUnit(), targetNodeClass, nodeMatchFn, nodeActionFn));
    }

    /**
     * Walks the Asts of all of the _sourceCode using the
     * {@link Tree#PRE_ORDER} strategy, searching for matching targetNodeClass
     * and selecting those who pass the nodeMatchFn, to call the nodeActionFn
     *
     * @param _sourceCode a Collection of source code to walk each of the ASTs
     * @param targetNodeClass a particular node class (or interface) to
     * intercept when on the walk
     * @param nodeMatchFn a predicate for matching particular nodes of the
     * nodeClass when on the walk
     * @param nodeActionFn the action to take on the selected nodes
     * @param <N> the target node type (i.e.
     * {@link com.github.javaparser.ast.expr.Expression},{@link TypeDeclaration}, {@link NodeWithOptionalBlockStmt}
     * @param <_C> the _code type
     * @return the modified root AST node
     */
    public static <_C extends _codeUnit, N extends Node> void in(
            Collection<_C> _sourceCode, Class<N> targetNodeClass, Predicate<N> nodeMatchFn, Consumer<N> nodeActionFn){
        in( PRE_ORDER, _sourceCode, targetNodeClass,nodeMatchFn,nodeActionFn);
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N parents(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( PARENTS, astRootNode, targetClass, matchFn, action );
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J parents(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PARENTS, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(PARENTS, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PARENTS, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(PARENTS, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     * @param _j
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J parents(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PARENTS, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, action);
                return _j;
            }
            else{
                of(PARENTS, ((_type) _j).ast(), targetClass, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PARENTS, _b.ast(), targetClass, t->true, action );
            return _j;
        }
        of(PARENTS, ((_java._node) _j).ast(), targetClass, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     * @param _j
     * @param action
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> _J parents(_J _j, Consumer<Node> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PARENTS, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(PARENTS, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PARENTS, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(PARENTS, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Direct Children
     *     From A: B, C
     *     From B: D, E
     * </PRE>
     *
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N directChildren(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( DIRECT_CHILDREN, astRootNode, targetClass, matchFn, action );
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *         (B)   C
     *         / \
     *        D   E
     * Direct Children
     *     From (A): B, C
     *     From (B): D, E
     * </PRE>
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J directChildren(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(DIRECT_CHILDREN, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }if( _j instanceof _body){
            _body _b = (_body)_j;
            of( DIRECT_CHILDREN, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(DIRECT_CHILDREN, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Direct Children
     *     From A: B, C
     *     From B: D, E
     * </PRE>
     *
     * @param _j
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J directChildren(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, action);
                return _j;
            }
            else{
                of(DIRECT_CHILDREN, ((_type) _j).ast(), targetClass, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( DIRECT_CHILDREN, _b.ast(), targetClass, t->true, action );
            return _j;
        }
        of(DIRECT_CHILDREN, ((_java._node) _j).ast(), targetClass, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *         (B)   C
     *         / \
     *        D   E
     * Direct Children
     *     From (A): B, C
     *     From (B): D, E
     * </PRE>
     *
     * @param _j
     * @param action
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> _J directChildren(_J _j, Consumer<Node> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(DIRECT_CHILDREN, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( DIRECT_CHILDREN, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(DIRECT_CHILDREN, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Breadth-First (or Level Order) from A: A,B,C,D,E
     * </PRE>
     *
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N breadthFirst(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( BREADTH_FIRST, astRootNode, targetClass, matchFn, action );
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Breadth-First (or Level Order) from (A): A,B,C,D,E
     * </PRE>
     *
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J breadthFirst(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(BREADTH_FIRST, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( BREADTH_FIRST, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(BREADTH_FIRST, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Breadth-First (or Level Order) from A: A,B,C,D,E
     * </PRE>
     * @param _j
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J breadthFirst(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, action);
                return _j;
            }
            else{
                of(BREADTH_FIRST, ((_type) _j).ast(), targetClass, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( BREADTH_FIRST, _b.ast(), targetClass, t->true, action );
            return _j;
        }
        of(BREADTH_FIRST, ((_java._node) _j).ast(), targetClass, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Breadth-First (or Level Order) from (A): A,B,C,D,E
     * </PRE>
     * @param _j
     * @param action
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> _J breadthFirst(_J _j, Consumer<Node> action ) {
        
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(BREADTH_FIRST, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( BREADTH_FIRST, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(BREADTH_FIRST, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * PostOrder ("leaves first", or children, then parents) from A: D,E,B,C,A
     * </PRE>
     *
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N postOrder(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( POST_ORDER, astRootNode, targetClass, matchFn, action );
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * PostOrder ("leaves first", or children, then parents) from (A) : D,E,B,C,A
     * </PRE>
     *
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J postOrder(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(POST_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(POST_ORDER, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( POST_ORDER, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(POST_ORDER, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * PostOrder ("leaves first", or children, then parents) from (A) : D,E,B,C,A
     * </PRE>
     * @param _j
     * @param targetClass
     * @param action
     * @param <T> target type
     * @param <_J> node type
     * @return
     */
    public static <T, _J extends _java._domain> _J postOrder(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(POST_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, action);
                return _j;
            }
            else{
                of(POST_ORDER, ((_type) _j).ast(), targetClass, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( POST_ORDER, _b.ast(), targetClass, t->true, action );
            return _j;
        }
        of(POST_ORDER, ((_java._node) _j).ast(), targetClass, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * PostOrder ("leaves first", or children, then parents) from (A) : D,E,B,C,A
     * </PRE>
     * @param _j
     * @param action
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> _J postOrder(_J _j, Consumer<Node> action ) {
                if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(POST_ORDER, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(POST_ORDER, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( POST_ORDER, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(POST_ORDER, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from A: A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <N>
     * @return
     */
    public static <T, N extends Node> N preOrder(
            N astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        return of( PRE_ORDER, astRootNode, targetClass, matchFn, action );
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A) : A,B,D,E,C
     * </PRE>
     *
     * @param _j
     * @param targetClass
     * @param matchFn
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J preOrder(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, matchFn, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), targetClass, matchFn, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), targetClass, matchFn, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), targetClass, matchFn, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from A: A,B,D,E,C
     * </PRE>
     * @param _j
     * @param targetClass
     * @param action
     * @param <T>
     * @param <_J>
     * @return
     */
    public static <T, _J extends _java._domain> _J preOrder(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), targetClass, t->true, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), targetClass, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), targetClass, t->true, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), targetClass, t->true, action );
        return _j;
    }

    /**
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     * @param _j
     * @param action
     * @param <_J>
     * @return
     */
    public static <_J extends _java._domain> _J preOrder(_J _j, Consumer<Node> action ) {
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                of(PRE_ORDER, ((_codeUnit) _j).astCompilationUnit(), Node.class, t->true, action);
                return _j;
            }
            else{
                of(PRE_ORDER, ((_type) _j).ast(), Node.class, t->true, action);
                return _j;
            }
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            of( PRE_ORDER, _b.ast(), Node.class, t->true, action );
            return _j;
        }
        of(PRE_ORDER, ((_java._node) _j).ast(), Node.class, t->true, action );
        return _j;
    }

    /**
     * Given an AST root node n, and a Tree Traversal Strategy (default is PreOrder),
     * walk the AST tree and match for EVERY occurrence of the target class
     * that matches the matchFn and perform an action on it.
     * NOTE: this can query for targetClasses:
     * <UL>
     * <LI>AST Node classes
     * ({@link com.github.javaparser.ast.expr.Expression}, {@link com.github.javaparser.ast.stmt.Statement}, {@link MethodDeclaration}, {@link EnumDeclaration}...)
     *
     * <LI>AST Node TYPE interfaces(com.github.javaparser.ast.nodeTypes.*)
     * {@link NodeWithAnnotations}, {@link NodeWithCondition}, ...)
     *
     * <LI>Comment types
     * ({@link com.github.javaparser.ast.comments.Comment}, {@link JavadocComment}, {@link LineComment}, {@link BlockComment}...)
     *
     * <LI>Logical classes
     * ({@link _field}, {@link _method}, {@link _constant}...)
     *
     * <LI>Logical interfaces
     * ({@link _javadocComment._withJavadoc}, {@link _method._withMethods}, {@link _annoExprs._withAnnoExprs}, ...)
     * </UL>
     *
     * @param <T> the target Class TYPE ..we need this BECAUSE Node classes/interfaces dont have a common ancestor
     *           (Node is the base class for Ast Node entities, but NodeWithAnnotations, interfaces do not etc.
     * @param <N> the Node class (if the target class is a Node class)
     * @param <RN> the root Node type
     * @param <_J> the logical class (if the target class is a _model class)
     * @param tt the Tree Traversal Strategy ({@link Node.TreeTraversal#POSTORDER}, {@link Node.TreeTraversal#PREORDER}, {@link Node.TreeTraversal#PARENTS}, {@link Node.TreeTraversal#BREADTHFIRST}}
     * @param astRootNode the root AST node to search
     * @param targetClass the target Class (or interface) to intercept
     * @param matchFn the predicate for testing the intercepted Nodes/logical entities
     * @param action the action to take on nodes that match the matchFn
     */
    public static <T, N extends Node, _J extends _java._domain, RN extends Node> RN of(
        Node.TreeTraversal tt, RN astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( Node.class.isAssignableFrom( targetClass ) //Stmts and Expressions
                || targetClass.getPackage().getName().equals( NodeWithAnnotations.class.getPackage().getName() ) // (NodeWithAnnotations, NodeWithArguments, NodeWithBlockStmt, ...
                || targetClass.getPackage().getName().equals( NodeWithAbstractModifier.class.getPackage().getName() )) { //NodeWithAbstractModifier, NodeWithStaticModifier, ...

            if( com.github.javaparser.ast.comments.Comment.class.isAssignableFrom( targetClass ) ) { //we have to _walk Comments Differently
                //because comments can be Orphaned and never touched when we use the normal Ast walk
                if( targetClass == com.github.javaparser.ast.comments.Comment.class ) {
                    Comments.forEachIn( astRootNode, (Predicate<com.github.javaparser.ast.comments.Comment>)matchFn, (Consumer<com.github.javaparser.ast.comments.Comment>)action );
                }
                else if( targetClass == JavadocComment.class ) {
                    Comments.forEachIn( astRootNode, JavadocComment.class, (Predicate<JavadocComment>)matchFn, (Consumer<JavadocComment>)action );
                }
                else if( targetClass == BlockComment.class ) {
                    Comments.forEachIn( astRootNode, BlockComment.class, (Predicate<BlockComment>)matchFn, (Consumer<BlockComment>)action );
                }
                else {
                    Comments.forEachIn(astRootNode, LineComment.class, (Predicate<LineComment>) matchFn, (Consumer<LineComment>) action);
                }
                return astRootNode;
            }else {
                in(tt,
                    astRootNode,
                    (Class<N>) targetClass,
                    e -> targetClass.isAssignableFrom(e.getClass())
                        && matchFn.test((T) e),
                    (Consumer<N>) action);
                return astRootNode;
            }
        }//maybe
        else{
            return in_java(tt, astRootNode, (Class<_J>)targetClass, (Predicate<_J>)matchFn, (Consumer<_J>)action);
        }
    }

    /**
     * A _walk that resolves {@link _java} classes (as apposed to AST {@link Node}
     * implementation
     * this requires "special work" building temporary ad-hoc models 
     * (i.e. _field, _class, _parameter) to test against predicates
     *
     * NOTE: this ALSO works with {@link _java} interfaces like
     * {@link _java}, {@link _method._withMethods}
     * {@link _params._withParams} etc.
     *
     * @param tt the traversal TYPE
     * @param astRootNode the root AST node
     * @param _javaClass the class of the model
     * @param _javaMatchFn
     * @param _javaAction
     * @param <_J>
     * @param <N> the Root node type
     * @return
     */
    public static <_J extends _java._domain, N extends Node> N in_java(
            Node.TreeTraversal tt, N astRootNode, Class<_J> _javaClass, Predicate<_J> _javaMatchFn, Consumer<_J> _javaAction ) {
        return in_java(tt, Integer.MAX_VALUE, astRootNode, _javaClass, _javaMatchFn, _javaAction);
    }

    /**
     * A _walk that resolves {@link  _java._domain} classes (as apposed to AST {@link Node}
     * implementation
     * this requires "special work" building temporary ad-hoc models
     * (i.e. _field, _class, _parameter) to test against predicates
     *
     * NOTE: this ALSO works with {@link _java} interfaces like
     * {@link _java}, {@link _method._withMethods}
     * {@link _params._withParams} etc.
     *
     * @param tt the traversal TYPE
     * @param levels the number of levels (walking up (through parents) or down (through children)) to limit the walk
     * @param astRootNode the root AST node
     * @param _javaClass the class of the model
     * @param _javaMatchFn
     * @param _javaAction
     * @param <_J>
     * @param <N> the Root node type
     * @return
     */
    public static <_J extends _java._domain, N extends Node> N in_java(
            Node.TreeTraversal tt, int levels, N astRootNode, Class<_J> _javaClass, Predicate<_J> _javaMatchFn, Consumer<_J> _javaAction ) {

        //System.out.println( "+IN JAVA");
        //if( true ) {

            Set<_codeUnit> encounteredTypes = new HashSet<>();

            if( _javaClass == _field.class ){
                //_fields are tricky
                in( tt, levels,
                        astRootNode,
                        VariableDeclarator.class,
                        v-> v.getParentNode().isPresent() && (v.getParentNode().get() instanceof FieldDeclaration),
                        v-> {
                            _field _f = _field.of(v);
                            if( _javaMatchFn.test((_J)_f) ){
                                _javaAction.accept( (_J) _f);
                            }
                        });
                return astRootNode;
            }
            in(tt, levels, astRootNode, Node.class, n->true, n->{
                if(n instanceof FieldDeclaration){
                    if( _javaClass.isAssignableFrom(_field.class)) {
                        FieldDeclaration fd = ((FieldDeclaration) n).asFieldDeclaration();
                        fd.getVariables().forEach(v -> {
                            _field _f = _field.of(v);
                            if (_javaMatchFn.test((_J) _f)) {
                                _javaAction.accept((_J) _f);
                            }
                        });
                    }
                } else if( n instanceof VariableDeclarator && n.getParentNode().isPresent()
                        && n.getParentNode().get() instanceof FieldDeclaration) {
                    //System.out.println( "Skipping Field Vars");
                } else{
                    //System.out.println(" >>>> testing of " + _javaClass + "  " + n.getClass());
                    //_java._node _n = (_java._node) _java.of(n);
                    _java._domain _n = (_java._domain) _java.of(n);
                    if( n instanceof CompilationUnit){
                        if( _javaClass.isAssignableFrom(_codeUnit.class) && !encounteredTypes.contains(_n)) {
                            _codeUnit _c = _codeUnit.of((CompilationUnit) n);
                            if (_javaMatchFn.test((_J) _c)) {
                                _javaAction.accept((_J) _c);
                                encounteredTypes.add(_c);
                            }
                        }
                    } else if( n instanceof TypeDeclaration && ((TypeDeclaration)n).isTopLevelType()){
                        /**
                         * NOTE: WE SKIP TOP LEVEL TYPES B/C THEIR COMPILATIONUNITS
                         * WILL ALREADY HAVE BEEN BE CALLED IN THE WALK
                         */
                        _codeUnit _c = _type.of( (TypeDeclaration)n);
                        if( _javaClass.isAssignableFrom(_c.getClass()) && ! encounteredTypes.contains(_c) &&_javaMatchFn.test((_J)_c) ){
                            _javaAction.accept( (_J)_c );
                            encounteredTypes.add(_c);
                        }
                    } else {
                        //System.out.println("testing " + _n.getClass() + " of " + _javaClass);
                        if (_javaClass.isAssignableFrom(_n.getClass())) {

                            if ((_javaMatchFn).test((_J) _n)) {
                                (_javaAction).accept((_J) _n);
                            }
                        }
                    }
                }
            });
            return astRootNode;
        //}

        /** *OLD STUFF  THIS
        if( _statement.class.isAssignableFrom(_javaClass)){
            in( tt, levels,
                    astRootNode,
                    Statement.class,
                    n -> true,
                    n -> {
                        _statement _s = _statement.of( (Statement)n);

                        if( _javaClass.isAssignableFrom(_s.getClass())) {
                            if (((Predicate<_statement>) _javaMatchFn).test(_s)) {
                                ((Consumer<_statement>) _javaAction).accept(_s);
                            }
                        }
                    });
            return astRootNode;
        }
        if( _expression.class.isAssignableFrom(_javaClass) && !(_javaClass == _annoRef.class) ){
            in( tt, levels,
                    astRootNode,
                    Expression.class,
                    n -> true,
                    n -> {
                        _expression _e = _expression.of( (Expression)n);
                        //System.out.println( "Got Expression "+ _e+" "+_e.getClass()+ " " +_javaClass);
                        if(_javaClass.isAssignableFrom(_e.getClass())) {
                            //System.out.println( ">>> Testing "+ _e+" "+_e.getClass()+" to "+_javaClass);
                            if (((Predicate<_expression>) _javaMatchFn).test(_e)) {
                                ((Consumer<_expression>) _javaAction).accept(_e);
                            }
                        }
                    });
            return astRootNode;
        }
        if( _javaClass == _codeUnit.class ){
            in(tt,
                levels,
                astRootNode,
                    Node.class,
                    n ->(n instanceof CompilationUnit || n instanceof TypeDeclaration),
                    a -> {
                        if( a instanceof CompilationUnit ){
                            _codeUnit _c = _codeUnit.of( (CompilationUnit)a );
                            if( _javaMatchFn.test((_J)_c) ){
                                _javaAction.accept( (_J)_c );
                            }
                        } else if( a instanceof TypeDeclaration && !((TypeDeclaration)a).isTopLevelType()){

                             //NOTE: WE SKIP TOP LEVEL TYPES B/C THEIR COMPILATIONUNITS
                             //WILL ALREADY HAVE BEEN BE CALLED IN THE WALK

                            _codeUnit _c = _type.of( (TypeDeclaration)a);
                            if( _javaMatchFn.test((_J)_c) ){
                                _javaAction.accept( (_J)_c );
                            }
                        }                        
                    } );
            return astRootNode;
        } else if (_javaClass == _packageInfo.class){
            in(tt,
                levels,
                astRootNode,
                    Node.class,
                    n ->n instanceof CompilationUnit,
                    a -> {
                        _codeUnit _c = _codeUnit.of( (CompilationUnit)a );
                            if( _c instanceof _packageInfo && _javaMatchFn.test((_J)_c) ){
                                _javaAction.accept( (_J)_c );
                            }                                                
                    } );
            return astRootNode;
        } else if(_javaClass == _moduleInfo.class ){
            in(tt,
                levels,
                astRootNode,
                    Node.class,
                    n ->n instanceof CompilationUnit,
                    a -> {
                        _codeUnit _c = _codeUnit.of( (CompilationUnit)a );
                            if( _c instanceof _moduleInfo && _javaMatchFn.test((_J)_c) ){
                                _javaAction.accept( (_J)_c );
                            }                                                
                    } );
            return astRootNode;
        }
        else if( _javaClass == _field.class ){
            //fields are tricky because a single field declaration can be multiple variable declarations
            //i.e. int x,y,z; is a single FieldDeclaration but (3) VarDeclarators (3) _fields
            //however we have local fields that are defined as VariableDeclarators (so we dont want to count those)
            in( tt, levels,
                    astRootNode,
                    VariableDeclarator.class,
                    v-> v.getParentNode().isPresent() && (v.getParentNode().get() instanceof FieldDeclaration),
                    v-> {
                        _field _f = _field.of(v);
                        if( _javaMatchFn.test((_J)_f) ){
                            _javaAction.accept( (_J) _f);
                        }
                    });
            return astRootNode;
        }
        else if( NodeClassMap.is_node(_javaClass) ){
        //else if( _java.Model._JAVA_TO_AST_NODE_CLASSES.containsKey( _javaClass ) ) {
            //System.out.println("Node Classes ");
            // _anno.class, AnnotationExpr.class
            // _annotation._element.class, AnnotationMemberDeclaration.class
            // _enum._constant.class, EnumConstantDeclaration.class
            // _constructor.class, ConstructorDeclaration.class
            // _field.class, FieldDeclaration.class
            // _method.class, MethodDeclaration.class
            // _parameter.class, Parameter.class
            // _receiverParameter.class, ReceiverParameter.class
            // _staticBlock.class, InitializerDeclaration.class
            // _typeParameter.class, TypeParameter.class
            // _typeRef.class, Type.class
            // _type, TypeDeclaration.class
            // _class, ClassOrInterfaceDeclaration.class
            // _enum EnumDeclaration.class
            // _interface ClassOrInterfaceDeclaration.class
            // _annotation AnnotationDeclaration.class
            //class switch would be nice here
            in(tt, levels,
                astRootNode,
                    NodeClassMap.node(_javaClass),
                    //_java.Model._JAVA_TO_AST_NODE_CLASSES.get( _javaClass ),
                    t ->true,
                    a -> {
                        _J logical = (_J)_java.of( a );
                        if( _javaMatchFn.test( logical ) ) {
                            _javaAction.accept( logical );
                        }
                    } );
            return astRootNode;
        }
        else if( _javaClass == _java._declared.class ) {
            in(tt, levels,
                    astRootNode,
                    BodyDeclaration.class,
                    t-> !t.isInitializerDeclaration(), //static Blocks are not members
                    n-> {
                        _java._declared _n = (_java._declared)_java.of(n);

                        if( ((Predicate<_java._declared>)_javaMatchFn).test( _n) ){
                            ((Consumer<_java._declared>)_javaAction).accept( _n);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _java._node.class ) {
            in(tt, levels,
                    astRootNode,
                    BodyDeclaration.class,
                    t-> true,
                    n-> {
                        _java._node _n = (_java._node)_java.of(n);

                        if( ((Predicate<_java._node>)_javaMatchFn).test( _n) ){
                            ((Consumer<_java._node>)_javaAction).accept( _n);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _body.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithBlockStmt || n instanceof NodeWithOptionalBlockStmt,
                    n-> {
                        _body _b = null;
                        if( n instanceof NodeWithBlockStmt ){
                            _b = _body.of( (NodeWithBlockStmt)n );
                        }else{
                            _b = _body.of( (NodeWithOptionalBlockStmt)n );
                        }
                        if( ((Predicate<_body>)_javaMatchFn).test( _b) ){
                            ((Consumer<_body>)_javaAction).accept( _b);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _body._hasBody.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithBlockStmt || n instanceof NodeWithOptionalBlockStmt,
                    n-> {
                        _body._hasBody _hb = (_body._hasBody)_java.of(n);

                        if( ((Predicate<_body._hasBody>)_javaMatchFn).test( _hb) ){
                            ((Consumer<_body._hasBody>)_javaAction).accept( _hb);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _annoRefs._withAnnoRefs.class ) {
            in(tt, levels,
                    astRootNode,
                    Node.class,
                    //NOTE: we DONT use NodeWithAnnotations because Annotations CAN be applied in interesting ways
                    // within the code BODY (i.e. on casts) but logically not... to operate on Cast ANNOTATIONS
                    // do so with the AST BODY
                    n -> n instanceof BodyDeclaration || n instanceof Parameter || n instanceof ReceiverParameter,
                    n ->{
                        _annoRefs._withAnnoRefs ha = (_annoRefs._withAnnoRefs)_java.of( n );
                        if( ((Predicate<_annoRefs._withAnnoRefs>)_javaMatchFn).test( ha ) ){
                            ((Consumer<_annoRefs._withAnnoRefs>)_javaAction).accept(ha);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _constructor._withConstructors.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof EnumDeclaration || (n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)n).isInterface()),
                    n-> {
                        _constructor._withConstructors hc = (_constructor._withConstructors)_java.of( n );

                        if( ((Predicate<_constructor._withConstructors>)_javaMatchFn).test( hc) ){
                            ((Consumer<_constructor._withConstructors>)_javaAction).accept( hc );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _field._withFields.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof TypeDeclaration || n instanceof EnumConstantDeclaration,
                    n-> {
                        _field._withFields hf = (_field._withFields)_java.of( n );

                        if( ((Predicate<_field._withFields>)_javaMatchFn).test( hf) ){
                            ((Consumer<_field._withFields>)_javaAction).accept( hf );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _javadocComment._withJavadoc.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithJavadoc,
                    n-> {
                        _javadocComment._withJavadoc hf = (_javadocComment._withJavadoc)_java.of( n );

                        if( ((Predicate<_javadocComment._withJavadoc>)_javaMatchFn).test( hf) ){
                            ((Consumer<_javadocComment._withJavadoc>)_javaAction).accept( hf );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _method._withMethods.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof EnumConstantDeclaration || n instanceof ClassOrInterfaceDeclaration || n instanceof EnumDeclaration,
                    n-> {
                        _method._withMethods hm = (_method._withMethods)_java.of( n );

                        if( ((Predicate<_method._withMethods>)_javaMatchFn).test( hm) ){
                            ((Consumer<_method._withMethods>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _modifiers._withModifiers.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithModifiers,
                    n-> {
                        _modifiers._withModifiers hm = (_modifiers._withModifiers)_java.of( n );

                        if( ((Predicate<_modifiers._withModifiers>)_javaMatchFn).test( hm) ){
                            ((Consumer<_modifiers._withModifiers>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _parameters._withParameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithParameters,
                    n-> {
                        _parameters._withParameters hm = (_parameters._withParameters)_java.of( n );

                        if( ((Predicate<_parameters._withParameters>)_javaMatchFn).test( hm) ){
                            ((Consumer<_parameters._withParameters>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _parameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithParameters,
                    n-> {
                        //need a lambda...
                        _parameters._withParameters hp = (_parameters._withParameters)_java.of( n );
                            
                        if( ((Predicate<_parameters>)_javaMatchFn).test( hp.getParameters() ) ){
                            ((Consumer<_parameters>)_javaAction).accept( hp.getParameters() );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _receiverParameter._withReceiverParameter.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof MethodDeclaration || n instanceof ConstructorDeclaration,
                    n-> {
                        _receiverParameter._withReceiverParameter hm = (_receiverParameter._withReceiverParameter)_java.of( n );

                        if( ((Predicate<_receiverParameter._withReceiverParameter>)_javaMatchFn).test( hm) ){
                            ((Consumer<_receiverParameter._withReceiverParameter>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _initBlock._withInitBlocks.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof ClassOrInterfaceDeclaration || n instanceof EnumDeclaration,
                    n-> {
                        _initBlock._withInitBlocks hsb = null;
                        if( n instanceof ClassOrInterfaceDeclaration){
                            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)n;
                            if( !coid.isInterface() ) {
                                hsb = _class.of((ClassOrInterfaceDeclaration) n);
                            }
                        } else {
                            hsb = _enum.of( (EnumDeclaration)n);
                        }
                        if( hsb != null && ((Predicate<_initBlock._withInitBlocks>)_javaMatchFn).test( hsb) ){
                            ((Consumer<_initBlock._withInitBlocks>)_javaAction).accept( hsb );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _throws._withThrows.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithThrownExceptions,
                    n-> {
                        _throws._withThrows ht = (_throws._withThrows)_java.of(n);

                        if( ((Predicate<_throws._withThrows>)_javaMatchFn).test( ht) ){
                            ((Consumer<_throws._withThrows>)_javaAction).accept( ht );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _typeParameters._withTypeParameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithTypeParameters,
                    n-> {
                        _typeParameters._withTypeParameters ht = (_typeParameters._withTypeParameters)_java.of(n);

                        if( ((Predicate<_typeParameters._withTypeParameters>)_javaMatchFn).test( ht) ){
                            ((Consumer<_typeParameters._withTypeParameters>)_javaAction).accept( ht );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _throws.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n -> n instanceof MethodDeclaration || n instanceof ConstructorDeclaration,
                    n -> {
                        _throws _t = _throws.of( (NodeWithThrownExceptions)n);

                        if( ((Predicate<_throws>)_javaMatchFn).test( _t) ){
                            ((Consumer<_throws>)_javaAction).accept( _t );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _typeParameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n -> n instanceof MethodDeclaration || n instanceof ConstructorDeclaration,
                    n -> {
                        _throws _t = _throws.of( (NodeWithThrownExceptions)n);

                        if( ((Predicate<_throws>)_javaMatchFn).test( _t) ){
                            ((Consumer<_throws>)_javaAction).accept( _t );
                        }
                    });
            return astRootNode;
        }
        throw new _jdraftException( "Could not convert Node of Class " + _javaClass + " to _java type" );
        */
    }

    /**
     * Traverse up the AST to find the first Node that matches the nodeMatchFn
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     *
     * @param astNode the root AST node to find the first node instance matching
     * the criteria
     * @param nodeMatchFn function for matching a particular node
     * @return the first node that matches the criteria, else null
     */
    public static Node firstParent(Node astNode, Predicate<Node> nodeMatchFn) {
        return Tree.first(PARENTS, astNode, Node.class, nodeMatchFn);
    }

    public static Node firstParent(_java._domain _j, Predicate<Node> nodeMatchFn){
        return Tree.first(PARENTS, _j, Node.class, nodeMatchFn);
    }

    /**
     * Traverse up the AST to find the first Node that is a nodeTargetClass (or
     * returns null)
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     *
     * @param astRootNode the root AST node to find the first node instance
     * matching the criteria
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param <T> the target node type
     * @return the first node that matches the criteria, else null
     */
    public static <T> T firstParent(Node astRootNode, Class<T> nodeTargetClass) {
        return Tree.first(PARENTS, astRootNode, nodeTargetClass, n -> true);
    }

    /**
     * _walk through the parent nodes from the astRootNode and finds the first
     * Node of nodeTargetClass and nodeMatchFn (or returns null)
     * <PRE>
     *             A
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Parents:
     *    from D: B, A
     *    from E: B, A
     *    from C: A
     *    from B: A
     * </PRE>
     *
     * @param astRootNode the root AST node to find the first node instance
     * matching the criteria
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param nodeMatchFn function for matching a particular node
     * @param <T> the target node type
     * @return the first node that matches the criteria, else null
     */
    public static <T> T firstParent(Node astRootNode, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        return Tree.first(PARENTS, astRootNode, nodeTargetClass, nodeMatchFn);
    }

    /**
     * _walk the nodes in PreOrder fashion starting at astRootNode finding the
     * first instance of the nodeTargetClass and returning it (or return null if
     * not found)
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode
     * @param nodeTargetClass
     * @param <T> the target node type
     * @return
     */
    public static <T> T first(Node astRootNode, Class<T> nodeTargetClass) {
        return Tree.first(PRE_ORDER, astRootNode, nodeTargetClass, n -> true);
    }
    
    /**
     * _walk the nodes in PreOrder fashion starting at astRootNode finding the
     * first instance of the nodeTargetClass and returning it (or return null if
     * not found)
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     * @param <_J> the _java entity type
     * @param _j the _java entity to search through
     * @param nodeTargetClass
     * @param <T> the target node type
     * @return
     */
    public static <_J extends _java._domain, T> T first(_J _j, Class<T> nodeTargetClass) {
        return Tree.first(PRE_ORDER, _j, nodeTargetClass, n -> true);
    }

    /**
     * Traverse the tree in PreOrder fashion looking for the first instance
     * matching the nodeMatchFn
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode
     * @param nodeMatchFn
     * @return
     */
    public static Node first(Node astRootNode, Predicate<Node> nodeMatchFn) {
        return Tree.first(PRE_ORDER, astRootNode, Node.class, nodeMatchFn);
    }

    /**
     * Traverse the tree in PreOrder fashion looking for the first instance
     * matching the nodeMatchFn
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     *
     * @param <_J> the _java entity type
     * @param _j the _java entity to search through
     * @param nodeMatchFn
     * @return
     */
    public static <_J extends _java._domain> Node first(_J _j, Predicate<Node> nodeMatchFn) {
        return Tree.first(PRE_ORDER, _j, Node.class, nodeMatchFn);
    }
    
    /**
     * Traverse the tree in preorder fashion looking for the first instance of
     * nodeTargetClass, and matching the nodeMatchFn
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     *
     * @param astRootNode the root AST node to find the first node instance
     * matching the criteria
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param nodeMatchFn function for matching a particular node
     * @param <T> the target type
     * @return the first node that matches the criteria, else null
     */
    public static <T> T first(Node astRootNode, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        return Tree.first(PRE_ORDER, astRootNode, nodeTargetClass, nodeMatchFn);
    }

    /**
     * Traverse the tree in preorder fashion looking for the first instance of
     * nodeTargetClass, and matching the nodeMatchFn inside any _java entity
     * <PRE>
     *            (A)
     *           /  \
     *          B    C
     *         / \
     *        D   E
     * Preorder (Parent, then children) from (A): A,B,D,E,C
     * </PRE>
     *
     * @param <_J> the _java entity node
     * @param <T> the target type
     * @param _j the _java entity to search through
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param nodeMatchFn function for matching a particular node     
     * @return the first node that matches the criteria, else null
     */
    public static <T, _J extends _java._domain> T first(_J _j, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        return Tree.first(PRE_ORDER, _j, nodeTargetClass, nodeMatchFn);
    }
        
    /**
     * Walks the Ast using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy      
     * {@link #PRE_ORDER}
     * {@link #POST_ORDER}
     * {@link #BREADTH_FIRST}
     * {@link #PARENTS}
     * {@link #DIRECT_CHILDREN} starting at the astStartNode walk the
     * Nodes intercepting all nodes that are of the nodeTargetClass and satisfy
     * the nodeMatchFn and returning the first instance found (or null if none
     * found)
     *
     * @param <T> the target node type 
     * @param tt the walk traversal strategy
     * @param _j the starting AST node to start the walk from
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param nodeMatchFn function for matching a particular node
     * @return the first node that matches the criteria, else null
     */
    public static <T> T first(
            Node.TreeTraversal tt, _java._domain _j, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        //System.out.println( "FIRST IN " + _j.getClass() );
        //first order of business, determine the ast start node
        if(_j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                //System.out.println( " IS TOP LEVEL "+ ((_code) _j).astCompilationUnit() );
                //System.out.println( " IS TOP LEVEL "+ nodeTargetClass );
                return Tree.first( tt, ((_codeUnit) _j).astCompilationUnit(), nodeTargetClass, nodeMatchFn);
            }
            return Tree.first( tt, ((_type) _j).ast(), nodeTargetClass, nodeMatchFn);
        }
        if(_j instanceof _body ){
            return Tree.first( tt, ((_body)_j).ast(), nodeTargetClass, nodeMatchFn);
        }
        return Tree.first( tt, ((_java._node)_j).ast(), nodeTargetClass, nodeMatchFn);
    }

    public static <T> T first(Node.TreeTraversal tt, _java._domain _j, Class<T> nodeTargetClass ) {
        return first(tt, _j, nodeTargetClass, t->true);
    }

    public static <T> T first( Node.TreeTraversal tt, Node n, Class<T> nodeTargetClass ) {
        return first(tt, n, nodeTargetClass, t->true);
    }

    /**
     *
     * @param jj
     * @param nodeTargetClass
     * @param nodeMatchFn
     * @param <T>
     * @return
     */
    public static <T, _C extends _codeUnit> T first(Collection<_C> jj, Class<T> nodeTargetClass, Predicate<T>nodeMatchFn ){
        return first( Tree.PRE_ORDER, jj, nodeTargetClass, nodeMatchFn );
    }

    /**
     *
     * @param tt
     * @param jj
     * @param nodeTargetClass
     * @param nodeMatchFn
     * @param <T>
     * @return
     */
    public static <T, _C extends _codeUnit> T first(Node.TreeTraversal tt, Collection<_C> jj, Class<T> nodeTargetClass, Predicate<T>nodeMatchFn ){
        List<T> found = new ArrayList<T>();
        jj.stream().filter( j -> {
            T t = first(tt, j, nodeTargetClass, nodeMatchFn);
            if( t != null ){
                found.add(t);
                return true;
            }
            return false;
        });
        if( found.isEmpty()){
            return null;
        }
        return found.get(0);
    }
    
    /**
     * Walks the Ast using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy      
     * {@link #PRE_ORDER}
     * {@link #POST_ORDER}
     * {@link #BREADTH_FIRST}
     * {@link #PARENTS}
     * {@link #DIRECT_CHILDREN} starting at the astStartNode walk the
     * Nodes intercepting all nodes that are of the nodeTargetClass and satisfy
     * the nodeMatchFn and returning the first instance found (or null if none
     * found)
     *
     * @param <T> the type being looked for
     * @param tt the walk traversal strategy
     * @param astStartNode the starting AST node to start the walk from
     * @param nodeTargetClass the target node class (could be a interface
     * (Ast#NNodeWithAnnotations)
     * @param nodeMatchFn function for matching a particular node
     * @return the first node that matches the criteria, else null
     */
    public static <T> T first(
            Node.TreeTraversal tt, Node astStartNode, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        
        //if T is a node class... thisll work
        //... if not I need to
        //  1) find the matching AST class for the _node class
        //  2) search through those, creating a new instance for each node & testing 
        if( _java._domain.class.isAssignableFrom(nodeTargetClass) ){

            //here I'm looking for a _field, _method, etc.
            Optional<Node> on = astStartNode.stream(tt).filter(n -> {
                    //System.out.println( "looking at "+ n.getClass()+" "+ _java.of(n).getClass()+" to "+nodeTargetClass );
                if(n instanceof FieldDeclaration){
                    if( nodeTargetClass.isAssignableFrom(_field.class) ){
                        FieldDeclaration fd = ((FieldDeclaration) n).asFieldDeclaration();
                        for(int i=0;i<fd.getVariables().size(); i++) {
                            _field _f = _field.of(fd.getVariable(i));
                            return nodeMatchFn.test( (T)_f);
                        }
                    }
                    //no need if its not matching _field
                } else {
                    _java._node _n = (_java._node) _java.of(n);
                    if (nodeTargetClass.isAssignableFrom(_n.getClass())) {
                        //System.out.println("testing " + _n.getClass() + " of " + _javaClass);
                        return nodeMatchFn.test((T)_n);
                        //if ((_javaMatchFn).test((_J) _n)) {
                        //    (_javaAction).accept((_J) _n);
                        //}
                    }
                }
                /*
                    _java._node _n = (_java._node)_java.of(n);
                    if( nodeTargetClass.isAssignableFrom(_n.getClass())){
                       //System.out.println( "testing "+ n.getClass()+" "+ _java.of(n).getClass() );
                        return nodeMatchFn.test((T)_n );
                    }

                 */
                    /*
                    if( Objects.equals( NodeClassMap._node(n.getClass()), nodeTargetClass ) ){
                        System.out.println( "testing "+ n.getClass()+" "+ _java.of(n).getClass() );
                        return nodeMatchFn.test((T) _java.of(n) );
                    }
                    */
                    /*
                    if( Objects.equals( _java.Model.AST_NODE_TO_JAVA_CLASSES.get(n.getClass()), nodeTargetClass ) ){
                        return nodeMatchFn.test((T) _java.of(n) );
                    }

                     */
                    return false;
                }).findFirst();
            
            if (on.isPresent()) {
                return (T) _java.of( on.get() );
            }
            return null;
        }
        
        Optional<Node> on = astStartNode.stream(tt).filter(n -> {
                if (nodeTargetClass.isAssignableFrom(n.getClass())) {
                    return nodeMatchFn.test((T) n);
                }
                return false;}
            ).findFirst();
        if (on.isPresent()) {
            return (T) on.get();
        }
        return null;
    }

    /**
     * Get the parent "member" (not JUST the parent) for some Node
     * we need this because sometimes nodes are "deeply nested" inside code, i.e.:
     * <PRE>
     *     void m(){
     *         if(true){
     *             here: int i = 0;
     *         }
     *     }
     * </PRE>
     * ...the "parent" of the labeled statement "here: int i=0;" is the ifStatement;
     * what we want to return is rather the method m()
     *
     * @param n the node to look for parent member
     * @param <M> the (expected) member type
     * @return the first Parent member of the node or null
     */
    public static <M extends BodyDeclaration> M parentMemberOf(Node n) {

        Optional<Node> bd =
                n.stream(Node.TreeTraversal.PARENTS).filter(p -> p instanceof BodyDeclaration ).findFirst();
        if( bd.isPresent() ){
            return (M)bd.get();
        }
        return null;
    }

    /**
     * Looks for LabeledStmts within code and removes the labels while retaining the code within the labels
     * fore example take the code with (2) labeledStmts with the label "lablel":
     * <PRE>
     *      class C {
     *             public void m() {
     *                 label: System.out.println( 1 );
     *                 if(System.getProperty("A") != null){
     *                     label: {
     *                     System.out.println(2);
     *                     System.out.println(3);
     *                     }
     *                 }
     *             }
     *         }
     * </PRE>
     *     If we use the code above and call flatten the label, "label":
     *     <PRE>
     *      _class _c = _class.of( C.class);
     *      Ast.flattenLabel(_c.astCompilationUnit(), "label");
     *      System.out.println( _c );
     *     </PRE>
     * ...produces:
     * <PRE>
     *     public class C {
     *         public void m() {
     *             System.out.println(1);
     *             if (System.getProperty("A") != null) {
     *                 System.out.println(2);
     *                 System.out.println(3);
     *             }
     *         }
     *     }
     * </PRE>
     * @param node
     * @param labelName
     * @param <N>
     * @return
     */
    public static <N extends Node> N flattenLabel(N node, String labelName) {
        //if( !isImplemented() ){
        //    throw new _jDraftException("No label : "+labelName+" in non-implemented body");
        //}
        Optional<LabeledStmt> ols
                = node.findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
        while (ols.isPresent()) {
            LabeledStmt ls = ols.get();
            if (ls.getStatement().isBlockStmt()) {
                BlockStmt bs = ls.getStatement().asBlockStmt();
                NodeList<Statement> stmts = bs.getStatements();
                if (stmts.isEmpty()) {
                    ls.remove(); //we have label:{}... just remove it entirely
                    //(ls.getParentNode().get()).remove(); //replace(ls, new EmptyStmt());
                    //(ls.getParentNode().get()).replace(ls, new EmptyStmt());
                } else if (stmts.size() == 1) {
                    if( stmts.get(0).isEmptyStmt()){ //we have label:{;}, remove it entirely
                        ls.remove();
                    } else {
                        ls.getParentNode().get().replace(ls, stmts.get(0));
                    }
                } else {
                    Node parent = ls.getParentNode().get();
                    NodeWithStatements parentNode = (NodeWithStatements) parent;
                    int stmtIndex = parentNode.getStatements().indexOf(ls);
                    for (int i = 0; i < stmts.size(); i++) {
                        parentNode.addStatement(stmtIndex + i, stmts.get(i));
                    }
                    parent.remove(ls);
                }
            } else {
                ls.getParentNode().get().replace(ls, ls.getStatement().clone());
            }
            //check if there is another to be flattened (NOTE: can't be at the same scope
            //but in a smaller scope or another scope (i.e. in a different constructr/method)
            //if we pass in a TypeDeclaration/CompilationUnit
            ols = node.findFirst(LabeledStmt.class, lbs -> lbs.getLabel().toString().equals(labelName));
        }
        return node;
    }

    /**
     * Gets the direct parent (in _node form)
     * @param _j
     * @return
     */
    public _java._node getParent( _java._node _j){
        Optional<Node> on = _j.ast().getParentNode();
        if( on.isPresent() ){
            return (_java._node)_java.of(on.get());
        }
        return null;
    }

    /**
     * Shortcut for checking if an ast has a parent of a particular class that complies with a particular Predicate
     * @param _j the _java entity
     * @param parentNodeClass the node class expected of the parent node
     * @param parentMatchFn predicate for matching the parent
     * @param <_J> the expected _java node type
     * @return true if the parent node exists, is of a particular type and complies with the predicate
     */
    public static <_J extends _java._domain> boolean isParent(_java._domain _j, Class<_J> parentNodeClass, Predicate<_J> parentMatchFn){
        if( _j instanceof _java._node){
            AtomicBoolean ans = new AtomicBoolean(false);
            in_java(Node.TreeTraversal.PARENTS, 1, ((_java._node)_j).ast(), parentNodeClass, parentMatchFn, (t)-> ans.set(true) );
            return ans.get();
        }
        //need to handle _typeParameters, _parameters, _annos
        if( _j instanceof _typeParams){
            _typeParams _tps = (_typeParams)_j;
            _java._node _n = (_java._node)_java.of( (Node)_tps.astHolder());
            return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
        }
        if( _j instanceof _body){
            _body _tps = (_body)_j;
            Object par = _tps.astParentNode();
            if( par != null ){
                _java._node _n = (_java._node)_java.of( (Node)par );
                return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
            }
        }
        if( _j instanceof _params){
            _params _tps = (_params)_j;
            _java._node _n = (_java._node)_java.of( (Node)_tps.astHolder());
            return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
        }
        return false;
    }

    /**
     * Check to see if this java entity has an Ancestor(parents, grandparents...) that
     * matches the type and matchFn
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <A> the match type
     * @return true if
     */
    public static <A> boolean hasAncestor(_java._domain _j, Class<A> type, Predicate<A> matchFn){
        return first(Node.TreeTraversal.PARENTS, _j, type, matchFn) != null;
    }

    /**
     * Check to see if this java entity has an Descendant(child, grandchild...) that
     * matches the type and matchFn
     *
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <D> the match type
     * @return
     */
    public static <D> boolean hasDescendant(_java._domain _j, Class<D> type, Predicate<D> matchFn) {
        return first(Node.TreeTraversal.POSTORDER,_j, type, matchFn) != null;
    }

    /**
     * Check to see if this java entity has a Child that matches the type and matchFn
     *
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <C> the match type
     * @return
     */
    public static <C> boolean hasChild(_java._domain _j, Class<C> type, Predicate<C> matchFn) {
        return first(Node.TreeTraversal.DIRECT_CHILDREN,_j, type, matchFn) != null;
    }

    /**
     * Determine if the Node os one of the expected types
     * @param astNode
     * @param classTypes
     * @return true if the node is one of the types
     */
    public static boolean isNodeOfType( Node astNode, Class...classTypes ){
        boolean jpnodeTypes = Arrays.stream(classTypes).anyMatch(
                nt -> nt.isAssignableFrom(astNode.getClass()));
        if( jpnodeTypes ){
            return true;
        }
        if( astNode instanceof FieldDeclaration ){
            //FieldDeclaration fd = (FieldDeclaration)astNode;
            //_field _f = _field.of(fd.getVariable(0));
            return Arrays.stream(classTypes).anyMatch(
                    nt -> nt.isAssignableFrom(_field.class) || nt.isAssignableFrom(_variable.class));
        }else {
            _java._domain _d = _java.of(astNode);
            return Arrays.stream(classTypes).anyMatch(
                    nt -> nt.isAssignableFrom(_d.getClass()));
        }
    }

    /**
     * Shortcut for checking if the (direct) parent exists and matches the predicate
     * @param node
     * @param parentMatchFn
     * @return
     */
    public static boolean isParent( Node node, Predicate<Node> parentMatchFn){
        if( node.getParentNode().isPresent()){
            return parentMatchFn.test( node.getParentNode().get() );
        }
        return false;
    }

    /**
     * Shortcut for checking if the parent is one of the potential node types
     * @param node the ast node to check
     * @param parentNodeTypes array of node
     * @return
     */
    public static <N extends Node> boolean isParent( Node node, Class<N>... parentNodeTypes){
        if( node.getParentNode().isPresent()){
            return isNodeOfType( node.getParentNode().get(), parentNodeTypes);
        }
        return false;
    }

    /**
     * Shortcut for checking if an ast has a parent of a particular class that complies with a particular Predicate
     * @param node the ast node starting point
     * @param parentNodeClass the node class expected of the parent node
     * @param parentMatchFn predicate for matching the parent
     * @param <N> the expected parent node type
     * @return true if the parent node exists, is of a particular type and complies with the predicate
     */
    public static <N extends Node> boolean isParent( Node node, Class<N> parentNodeClass, Predicate<N> parentMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( parentNodeClass.isAssignableFrom(parent.getClass()) ) {
                return parentMatchFn.test( (N)node.getParentNode().get());
            }
        }
        return false;
    }

    /**
     * Finds the Parent "member" {@link BodyDeclaration} (the member "containing" the $pattern match)
     * and test that it matches the memberMatchFn
     * <PRE>
     * i.e.
     * class FF{
     *     @Deprecated
     *     public int getF(){
     *         int i = 0;
     *         int j = 1;
     *         return 2;
     *     }
     * }
     * //...if we start at the int literal 0:
     * Node oneLiteral = $.of(0).firstIn(FF.class);
     * //we might want to know something about it's containing member, (the method getF())
     *
     * // here we test that the literal is not contained in a parent member/BodyDeclaration
     * // that has the @Deprecated annotation
     * assertTrue( Ast.isParentMember(oneLiteral, bd-> bd.getAnnotation(Deprecated.class) == null) )
     *
     * </PRE>
     *
     * @param node
     * @param parentMemberMatchFn
     * @return
     */
    public static boolean isParentMember( Node node, Predicate<BodyDeclaration> parentMemberMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( parent instanceof BodyDeclaration ){
                return parentMemberMatchFn.test( (BodyDeclaration)parent);
            }
            //recurse to next parent until we find a BodyDeclaration to test of no parent
            return isParentMember(parent, parentMemberMatchFn);
        }
        return false;
    }

    /**
     * List all ancestors of the node
     * @param node
     * @return
     */
    public static List<Node> listAncestors( Node node ){
        List<Node>ancestors = new ArrayList<>();
        node.walk(Node.TreeTraversal.PARENTS, a -> ancestors.add(a));
        return ancestors;
    }

    /**
     * find and return the First Common Ancestor of the left and right nodes or null if there is no common ancestor
     * @param left
     * @param right
     * @return
     */
    public static Node commonAncestor( Node left, Node right ){
        //1)collect all ancestors for right in a set
        Set<Node> rightAncestors = new HashSet<>();
        right.walk(Node.TreeTraversal.PARENTS, p -> rightAncestors.add(p));

        //2) walk ancestors of left until I find an ancestor that exists in the rightAncestorSet
        Node leftAncestor = left;
        while (leftAncestor != null ) {
            if (rightAncestors.contains(leftAncestor)) {
                return leftAncestor;
            }
            if( leftAncestor.getParentNode().isPresent()){
                leftAncestor = leftAncestor.getParentNode().get();
            } else{
                leftAncestor = null;
            }
        }
        return null; //no common ancestor
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorMatchFn the node class expected of the parent node
     * @return true if the ancestor node exists, is of a particular type and complies with the predicate
     */
    public static boolean hasAncestor( Node node, Predicate<Node> ancestorMatchFn){
        return hasAncestor( node, Node.class, ancestorMatchFn);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorNodeClass the node class expected of the parent node
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasAncestor( Node node, Class<N> ancestorNodeClass){
        return hasAncestor( node, ancestorNodeClass, t->true);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class that complies with a particular Predicate
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorNodeClass the node class expected of the parent node
     * @param ancestorMatchFn predicate for matching the parent
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasAncestor( Node node, Class<N> ancestorNodeClass, Predicate<N> ancestorMatchFn){
        return ancestor( node, ancestorNodeClass, ancestorMatchFn ) != null;
    }

    /**
     * Find and return the first ancestor of the node that matches the class and ancestor matchFn (or return null)
     * @param node the start node
     * @param ancestorNodeClass
     * @param ancestorMatchFn
     * @param <N>
     * @return
     *
     * @see #hasAncestor(Node, Class, Predicate)
     */
    public static <N extends Node> N ancestor( Node node, Class<N> ancestorNodeClass, Predicate<N> ancestorMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( ancestorNodeClass.isAssignableFrom(parent.getClass()) ) {
                N nn = (N)node.getParentNode().get();
                boolean match = ancestorMatchFn.test(nn);
                if( match ){
                    return nn;
                }
                return ancestor(parent, ancestorNodeClass, ancestorMatchFn);
            }
        }
        return null;
    }

    public static boolean hasChild( Node n, Predicate<Node> nodeMatchFn){
        return n.getChildNodes().stream().anyMatch(nodeMatchFn);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param DescendantMatchFn the node class expected of the parent node
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static boolean hasDescendant( Node node, Predicate<Node> DescendantMatchFn){
        return hasDescendant( node, Node.class, DescendantMatchFn);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param DescendantNodeClass the node class expected of the parent node
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasDescendant( Node node, Class<N> DescendantNodeClass){
        return hasDescendant( node, DescendantNodeClass, t->true);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class that complies with a particular Predicate
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param descendantNodeClass the node class expected of the parent node
     * @param descendantMatchFn predicate for matching the parent
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particular type and complies with the predicate
     */
    public static <N extends Node> boolean hasDescendant( Node node, Class<N> descendantNodeClass, Predicate<N> descendantMatchFn){
        //return node.stream(Node.TreeTraversal.PREORDER).anyMatch(a -> descendantNodeClass.isAssignableFrom(a.getClass()) && descendantMatchFn.test( (N)a ));
        //return hasDescendant(node, Class)
        return matchDescendant(node, descendantNodeClass,Integer.MAX_VALUE -100, descendantMatchFn );
    }

    public static <N extends Node> boolean hasDescendant( Node node, int depth, Class<N> descendantNodeClass, Predicate<N> descendantMatchFn){
        return matchDescendant(node, descendantNodeClass,depth, descendantMatchFn );
    }

    /**
     * Recursive descent-level search that returns if the node or any of it's children
     * up to (depth) levels deep
     *
     * @param node
     * @param depth
     * @param nodeMatchFn
     * @return
     */
    public static boolean matchDescendant(Node node, int depth, Predicate<Node> nodeMatchFn ) {
        return matchDescendant(node, Node.class, depth, nodeMatchFn);
    }

    public static <N extends Node> boolean  matchDescendant(Node node, Class<N>nodeClass, int depth, Predicate<N>nodeMatchFn){
        if( depth < 0 ){
            return false;
        }
        if( nodeClass.isAssignableFrom(node.getClass()) && nodeMatchFn.test((N)node) ){
            //System.out.println( "FOUND "+node+" with "+ depth );
            return true;
        }
        List<Node> children = node.getChildNodes();
        //final int lvl = levels-1;
        for(int i=0;i<children.size();i++){
            if(matchDescendant(children.get(i),nodeClass, depth-1, nodeMatchFn)){
                return true;
            }
        }
        return false;
    }

    public static String describe( Node n){
        ASCIITreePrinter atp = new ASCIITreePrinter();
        return atp.output(n);
    }

    public static String describe( Node n, Function<Node, String> nodeFormat){
        ASCIITreePrinter atp = new ASCIITreePrinter();
        return atp.output(n, nodeFormat);
    }

    public static String describe( _java._node n){
        ASCIITreePrinter atp = new ASCIITreePrinter(ASCIITreePrinter._NODE_SUMMARY_CLASS_RANGE_FORMAT);
        return atp.output(n);
    }

    public static String describe( _java._node _n, Function<_java._node, String> _nodeFormat){
        ASCIITreePrinter atp = new ASCIITreePrinter( n-> _nodeFormat.apply( (_java._node)_java.of(n) ) );
        return atp.output(_n);
    }
}
