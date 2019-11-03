package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Graph Traversal Algorithms for traversing nodes of a Java AST
 * for selecting / visiting the nodes, providing a simple API for operations
 * on Nodes within a Java source file (by manipulating the AST).
 *
 * The use is best illustrated by example:
 * <PRE>
 * _class _c = _class.of("aaaa.bbbb.C")
 *     .fields("int x=1;", "int y=2;", "String NAME;");
 *
 * //intercept & print all of the {@link _field}s within the _class _c
 * _walk.in(_c, _field.class, f-> System.out.println(f));
 * // prints:
 * //    int x=1;
 * //    int y=2;
 * //    String NAME;
 *
 * //intercept & print all {@link _field}s within _class _c that have initial values
 * _walk.in(_c, _field.class, f-> f.hasInit(), f-> System.out.println(f));
 * // prints:
 * //    int x=1;
 * //    int y=2;
 *
 * // we are not limited to traversing based on either _model or AST classes
 * //... we can traverse the node graph looking for entities that are Node implementations
 *
 * // to find all Integer literals within the code:
 * _walk.in(_c, {@link Ex#INT_LITERAL}, i-> System.out.println(i));
 * // prints:
 * //    1
 * //    2
 *
 * // to find all types within the code:
 * _walk.in(_c, {@link Ex#TYPE}, i-> System.out.println(i));
 *  // prints:
 *  //    "int"
 *  //    "int"
 *  //    "String"
 * </PRE>
 *
 */
public enum _walk {
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
    public static <_J extends _draft> List<Node> list(_J _j, Predicate<Node> nodeMatchFn ) {
        List<Node> found = new ArrayList<>();
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), Node.class, nodeMatchFn, f -> found.add(f));
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
        of(PRE_ORDER, ((_node) _j).ast(), Node.class, nodeMatchFn, f -> found.add(f) );
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
    public static <T, _J extends _draft> List<T> list(_J _j, Class<T> targetClass ) {
        List<T> found = new ArrayList<>();
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), targetClass, t->true, f -> found.add(f));
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
        of(PRE_ORDER, ((_node) _j).ast(), targetClass, t->true, f -> found.add(f) );
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
    public static <T, _J extends _draft> List<T> list(
            _J _j, Class<T> targetClass, Predicate<T> matchFn ) {

        List<T> found = new ArrayList<>();
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), targetClass, matchFn, f -> found.add( (T)f));
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
        of(PRE_ORDER, ((_node) _j).ast(), targetClass, matchFn, f -> found.add(f) );
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
    public static <T, _C extends _code> List<T> list(
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
    public static <T, _C extends _code> List<T> list(
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
    public static <T, _C extends _code> List<T> list(
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
        _walk.in(astRootNode, targetNodeClass, t -> true, nodeActionFn);
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
    public static <T, _J extends _draft> _J in(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _C extends _code> void in( Collection<_C> _sourceCode, Class<T> targetClass, Consumer<T> action ) {
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
    public static <T, _J extends _draft> _J in(_J _j, Class<T> targetClass, Consumer<T> action ) {
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
    public static <T, _J extends _draft> _J in(Node.TreeTraversal tt, _J _j, Class<T> targetClass, Consumer<T> action ) {
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
    public static <T, _J extends _draft> _J in(Node.TreeTraversal tt, _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(tt, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(tt, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <_J extends _draft> _J in(_J _j, Consumer<Node> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), Node.class, t->true, action );
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
    public static <_J extends _draft> _J in(_J _j, Predicate<Node> nodeMatchFn, Consumer<Node> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), Node.class, nodeMatchFn, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), Node.class, nodeMatchFn, action );
        return _j;
    }

    /**
     * Walks the Ast using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy provided
     * {@link _walk#PRE_ORDER}
     * {@link _walk#POST_ORDER}
     * {@link _walk#BREADTH_FIRST}
     * {@link _walk#PARENTS}
     * {@link _walk#DIRECT_CHILDREN} starting from the astRootNode, searching
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
        /*
        astRootNode.stream(traversal).filter(n -> targetNodeClass.isAssignableFrom(n.getClass()) && nodeMatchFn.test((N) n))
                .forEach(n->nodeActionFn.accept( (N)n ) );
        return astRootNode;
        */
        return in( traversal, Integer.MAX_VALUE, astRootNode, targetNodeClass, nodeMatchFn, nodeActionFn);
    }

    public static <R extends Node, N extends Node> R in(
            Node.TreeTraversal traversal,
            int levels,
            R astRootNode,
            Class<N> targetNodeClass,
            Predicate<N> nodeMatchFn,
            Consumer<N> nodeActionFn) {

        astRootNode.stream(traversal).limit(levels).filter(n -> targetNodeClass.isAssignableFrom(n.getClass()) && nodeMatchFn.test((N) n))
                .forEach(n->nodeActionFn.accept( (N)n ) );
        return astRootNode;
    }

    /**
     * Walks the Asts of all of the _sourceCode using the
     * {@link com.github.javaparser.ast.Node.TreeTraversal} strategy provided
     * {@link _walk#PRE_ORDER}
     * {@link _walk#POST_ORDER}
     * {@link _walk#BREADTH_FIRST}
     * {@link _walk#PARENTS}
     * {@link _walk#DIRECT_CHILDREN} starting from the astRootNode, searching
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
    public static <_C extends _code, N extends Node> void in(
            Node.TreeTraversal traversal, Collection<_C> _sourceCode, Class<N> targetNodeClass, Predicate<N> nodeMatchFn, Consumer<N> nodeActionFn){
        _sourceCode.forEach(_c-> in(traversal, _c.astCompilationUnit(), targetNodeClass, nodeMatchFn, nodeActionFn));
    }

    /**
     * Walks the Asts of all of the _sourceCode using the
     * {@link _walk#PRE_ORDER} strategy, searching for matching targetNodeClass
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
    public static <_C extends _code, N extends Node> void in(
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
    public static <T, _J extends _draft> _J parents(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PARENTS, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(PARENTS, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _J extends _draft> _J parents(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PARENTS, ((_code) _j).astCompilationUnit(), targetClass, t->true, action);
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
        of(PARENTS, ((_node) _j).ast(), targetClass, t->true, action );
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
    public static <_J extends _draft> _J parents(_J _j, Consumer<Node> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PARENTS, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(PARENTS, ((_node) _j).ast(), Node.class, t->true, action );
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
    public static <T, _J extends _draft> _J directChildren(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(DIRECT_CHILDREN, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _J extends _draft> _J directChildren(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_code) _j).astCompilationUnit(), targetClass, t->true, action);
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
        of(DIRECT_CHILDREN, ((_node) _j).ast(), targetClass, t->true, action );
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
    public static <_J extends _draft> _J directChildren(_J _j, Consumer<Node> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(DIRECT_CHILDREN, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(DIRECT_CHILDREN, ((_node) _j).ast(), Node.class, t->true, action );
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
    public static <T, _J extends _draft> _J breadthFirst(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(BREADTH_FIRST, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _J extends _draft> _J breadthFirst(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_code) _j).astCompilationUnit(), targetClass, t->true, action);
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
        of(BREADTH_FIRST, ((_node) _j).ast(), targetClass, t->true, action );
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
    public static <_J extends _draft> _J breadthFirst(_J _j, Consumer<Node> action ) {
        
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(BREADTH_FIRST, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(BREADTH_FIRST, ((_node) _j).ast(), Node.class, t->true, action );
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
    public static <T, _J extends _draft> _J postOrder(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(POST_ORDER, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(POST_ORDER, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _J extends _draft> _J postOrder(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(POST_ORDER, ((_code) _j).astCompilationUnit(), targetClass, t->true, action);
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
        of(POST_ORDER, ((_node) _j).ast(), targetClass, t->true, action );
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
    public static <_J extends _draft> _J postOrder(_J _j, Consumer<Node> action ) {
                if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(POST_ORDER, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(POST_ORDER, ((_node) _j).ast(), Node.class, t->true, action );
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
    public static <T, _J extends _draft> _J preOrder(
            _J _j, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), targetClass, matchFn, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), targetClass, matchFn, action );
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
    public static <T, _J extends _draft> _J preOrder(
            _J _j, Class<T> targetClass, Consumer<T> action ) {

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), targetClass, t->true, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), targetClass, t->true, action );
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
    public static <_J extends _draft> _J preOrder(_J _j, Consumer<Node> action ) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                of(PRE_ORDER, ((_code) _j).astCompilationUnit(), Node.class, t->true, action);
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
        of(PRE_ORDER, ((_node) _j).ast(), Node.class, t->true, action );
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
     * ({@link Comment}, {@link JavadocComment}, {@link LineComment}, {@link BlockComment}...)
     *
     * <LI>Logical classes
     * ({@link _field}, {@link _method}, {@link _enum._constant}...)
     *
     * <LI>Logical interfaces
     * ({@link _javadoc._hasJavadoc}, {@link _method._hasMethods}, {@link _anno._hasAnnos}, ...)
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
    public static <T, N extends Node, _J extends _draft, RN extends Node> RN of(
        Node.TreeTraversal tt, RN astRootNode, Class<T> targetClass, Predicate<T> matchFn, Consumer<T> action ) {

        if( Node.class.isAssignableFrom( targetClass ) //Stmts and Expressions
                || targetClass.getPackage().getName().equals( NodeWithAnnotations.class.getPackage().getName() ) // (NodeWithAnnotations, NodeWithArguments, NodeWithBlockStmt, ...
                || targetClass.getPackage().getName().equals( NodeWithAbstractModifier.class.getPackage().getName() )) { //NodeWithAbstractModifier, NodeWithStaticModifier, ...

            if( Comment.class.isAssignableFrom( targetClass ) ) { //we have to _walk Comments Differently
                //because comments can be Orphaned and never touched when we use the normal Ast walk
                if( targetClass == Comment.class ) {
                    Ast.forComments( astRootNode, (Predicate<Comment>)matchFn, (Consumer<Comment>)action );
                }
                else if( targetClass == JavadocComment.class ) {
                    Ast.forComments( astRootNode, JavadocComment.class, (Predicate<JavadocComment>)matchFn, (Consumer<JavadocComment>)action );
                }
                else if( targetClass == BlockComment.class ) {
                    Ast.forComments( astRootNode, BlockComment.class, (Predicate<BlockComment>)matchFn, (Consumer<BlockComment>)action );
                }
                else {
                    Ast.forComments(astRootNode, LineComment.class, (Predicate<LineComment>) matchFn, (Consumer<LineComment>) action);
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
     * {@link _java}, {@link _method._hasMethods}
     * {@link _parameter._hasParameters} etc.
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
    public static <_J extends _draft, N extends Node> N in_java(
            Node.TreeTraversal tt, N astRootNode, Class<_J> _javaClass, Predicate<_J> _javaMatchFn, Consumer<_J> _javaAction ) {
        return in_java(tt, Integer.MAX_VALUE, astRootNode, _javaClass, _javaMatchFn, _javaAction);
    }


    /**
     * A _walk that resolves {@link  _draft} classes (as apposed to AST {@link Node}
     * implementation
     * this requires "special work" building temporary ad-hoc models
     * (i.e. _field, _class, _parameter) to test against predicates
     *
     * NOTE: this ALSO works with {@link _java} interfaces like
     * {@link _java}, {@link _method._hasMethods}
     * {@link _parameter._hasParameters} etc.
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
    public static <_J extends _draft, N extends Node> N in_java(
            Node.TreeTraversal tt, int levels, N astRootNode, Class<_J> _javaClass, Predicate<_J> _javaMatchFn, Consumer<_J> _javaAction ) {

        if( _javaClass == _code.class ){
            in(tt,
                levels,
                astRootNode,
                    Node.class,
                    n ->(n instanceof CompilationUnit || n instanceof TypeDeclaration),
                    a -> {
                        if( a instanceof CompilationUnit ){
                            _code _c = _java.code( (CompilationUnit)a );
                            if( _javaMatchFn.test((_J)_c) ){
                                _javaAction.accept( (_J)_c );
                            }
                        } else if( a instanceof TypeDeclaration && !((TypeDeclaration)a).isTopLevelType()){
                            /** 
                             * NOTE: WE SKIP TOP LEVEL TYPES B/C THEIR COMPILATIONUNITS 
                             * WILL ALREADY HAVE BEEN BE CALLED IN THE WALK
                             */
                            _code _c = _java.type( (TypeDeclaration)a);
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
                        _code _c = _java.code( (CompilationUnit)a );
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
                        _code _c = _java.code( (CompilationUnit)a );
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
        else if( _java.Model._JAVA_TO_AST_NODE_CLASSES.containsKey( _javaClass ) ) {
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
                    _java.Model._JAVA_TO_AST_NODE_CLASSES.get( _javaClass ),
                    t ->true,
                    a -> {
                        _J logical = (_J)_java.of( a );
                        if( _javaMatchFn.test( logical ) ) {
                            _javaAction.accept( logical );
                        }
                    } );
            return astRootNode;
        }
        else if( _javaClass == _declared.class ) {
            in(tt, levels,
                    astRootNode,
                    BodyDeclaration.class,
                    t-> !t.isInitializerDeclaration(), //static Blocks are not members
                    n-> {
                        _declared _n = (_declared)_java.of(n);

                        if( ((Predicate<_declared>)_javaMatchFn).test( _n) ){
                            ((Consumer<_declared>)_javaAction).accept( _n);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _node.class ) {
            in(tt, levels,
                    astRootNode,
                    BodyDeclaration.class,
                    t-> true,
                    n-> {
                        _node _n = (_node)_java.of(n);

                        if( ((Predicate<_node>)_javaMatchFn).test( _n) ){
                            ((Consumer<_node>)_javaAction).accept( _n);
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
        else if( _javaClass == _anno._hasAnnos.class ) {
            in(tt, levels,
                    astRootNode,
                    Node.class,
                    //NOTE: we DONT use NodeWithAnnotations because Annotations CAN be applied in interesting ways
                    // within the code BODY (i.e. on casts) but logically not... to operate on Cast ANNOTATIONS
                    // do so with the AST BODY
                    n -> n instanceof BodyDeclaration || n instanceof Parameter || n instanceof ReceiverParameter,
                    n ->{
                        _anno._hasAnnos ha = (_anno._hasAnnos)_java.of( n );
                        if( ((Predicate<_anno._hasAnnos>)_javaMatchFn).test( ha ) ){
                            ((Consumer<_anno._hasAnnos>)_javaAction).accept(ha);
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _constructor._hasConstructors.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof EnumDeclaration || (n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)n).isInterface()),
                    n-> {
                        _constructor._hasConstructors hc = (_constructor._hasConstructors)_java.of( n );

                        if( ((Predicate<_constructor._hasConstructors>)_javaMatchFn).test( hc) ){
                            ((Consumer<_constructor._hasConstructors>)_javaAction).accept( hc );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _field._hasFields.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof TypeDeclaration || n instanceof EnumConstantDeclaration,
                    n-> {
                        _field._hasFields hf = (_field._hasFields)_java.of( n );

                        if( ((Predicate<_field._hasFields>)_javaMatchFn).test( hf) ){
                            ((Consumer<_field._hasFields>)_javaAction).accept( hf );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _javadoc._hasJavadoc.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithJavadoc,
                    n-> {
                        _javadoc._hasJavadoc hf = (_javadoc._hasJavadoc)_java.of( n );

                        if( ((Predicate<_javadoc._hasJavadoc>)_javaMatchFn).test( hf) ){
                            ((Consumer<_javadoc._hasJavadoc>)_javaAction).accept( hf );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _method._hasMethods.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof EnumConstantDeclaration || n instanceof ClassOrInterfaceDeclaration || n instanceof EnumDeclaration,
                    n-> {
                        _method._hasMethods hm = (_method._hasMethods)_java.of( n );

                        if( ((Predicate<_method._hasMethods>)_javaMatchFn).test( hm) ){
                            ((Consumer<_method._hasMethods>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _modifiers._hasModifiers.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithModifiers,
                    n-> {
                        _modifiers._hasModifiers  hm = (_modifiers._hasModifiers)_java.of( n );

                        if( ((Predicate<_modifiers._hasModifiers>)_javaMatchFn).test( hm) ){
                            ((Consumer<_modifiers._hasModifiers>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _parameter._hasParameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithParameters,
                    n-> {
                        _parameter._hasParameters  hm = (_parameter._hasParameters)_java.of( n );

                        if( ((Predicate<_parameter._hasParameters>)_javaMatchFn).test( hm) ){
                            ((Consumer<_parameter._hasParameters>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _parameter._parameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithParameters,
                    n-> {
                        //need a lambda...
                        _parameter._hasParameters hp = (_parameter._hasParameters)_java.of( n );
                            
                        if( ((Predicate<_parameter._parameters>)_javaMatchFn).test( hp.getParameters() ) ){
                            ((Consumer<_parameter._parameters>)_javaAction).accept( hp.getParameters() );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _receiverParameter._hasReceiverParameter.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof MethodDeclaration || n instanceof ConstructorDeclaration,
                    n-> {
                        _receiverParameter._hasReceiverParameter hm = (_receiverParameter._hasReceiverParameter)_java.of( n );

                        if( ((Predicate<_receiverParameter._hasReceiverParameter>)_javaMatchFn).test( hm) ){
                            ((Consumer<_receiverParameter._hasReceiverParameter>)_javaAction).accept( hm );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _initBlock._hasInitBlocks.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof ClassOrInterfaceDeclaration || n instanceof EnumDeclaration,
                    n-> {
                        _initBlock._hasInitBlocks hsb = null;
                        if( n instanceof ClassOrInterfaceDeclaration){
                            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)n;
                            if( !coid.isInterface() ) {
                                hsb = _class.of((ClassOrInterfaceDeclaration) n);
                            }
                        } else {
                            hsb = _enum.of( (EnumDeclaration)n);
                        }
                        if( hsb != null && ((Predicate<_initBlock._hasInitBlocks>)_javaMatchFn).test( hsb) ){
                            ((Consumer<_initBlock._hasInitBlocks>)_javaAction).accept( hsb );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _throws._hasThrows.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithThrownExceptions,
                    n-> {
                        _throws._hasThrows ht = (_throws._hasThrows)_java.of(n);

                        if( ((Predicate<_throws._hasThrows>)_javaMatchFn).test( ht) ){
                            ((Consumer<_throws._hasThrows>)_javaAction).accept( ht );
                        }
                    });
            return astRootNode;
        }
        else if( _javaClass == _typeParameter._hasTypeParameters.class ) {
            in( tt, levels,
                    astRootNode,
                    Node.class,
                    n-> n instanceof NodeWithTypeParameters,
                    n-> {
                        _typeParameter._hasTypeParameters ht = (_typeParameter._hasTypeParameters)_java.of(n);

                        if( ((Predicate<_typeParameter._hasTypeParameters>)_javaMatchFn).test( ht) ){
                            ((Consumer<_typeParameter._hasTypeParameters>)_javaAction).accept( ht );
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
        else if( _javaClass == _typeParameter._typeParameters.class ) {
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
        return _walk.first(PARENTS, astNode, Node.class, nodeMatchFn);
    }

    public static Node firstParent(_draft _j, Predicate<Node> nodeMatchFn){
        return _walk.first(PARENTS, _j, Node.class, nodeMatchFn);
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
        return _walk.first(PARENTS, astRootNode, nodeTargetClass, n -> true);
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
        return _walk.first(PARENTS, astRootNode, nodeTargetClass, nodeMatchFn);
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
        return _walk.first(PRE_ORDER, astRootNode, nodeTargetClass, n -> true);
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
    public static <_J extends _draft, T> T first(_J _j, Class<T> nodeTargetClass) {
        return _walk.first(PRE_ORDER, _j, nodeTargetClass, n -> true);
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
        return _walk.first(PRE_ORDER, astRootNode, Node.class, nodeMatchFn);
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
    public static <_J extends _draft> Node first(_J _j, Predicate<Node> nodeMatchFn) {
        return _walk.first(PRE_ORDER, _j, Node.class, nodeMatchFn);
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
        return _walk.first(PRE_ORDER, astRootNode, nodeTargetClass, nodeMatchFn);
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
    public static <T, _J extends _draft> T first(_J _j, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        return _walk.first(PRE_ORDER, _j, nodeTargetClass, nodeMatchFn);
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
            Node.TreeTraversal tt, _draft _j, Class<T> nodeTargetClass, Predicate<T> nodeMatchFn) {
        //System.out.println( "FIRST IN " + _j.getClass() );
        //first order of business, determine the ast start node
        if(_j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                //System.out.println( " IS TOP LEVEL "+ ((_code) _j).astCompilationUnit() );
                //System.out.println( " IS TOP LEVEL "+ nodeTargetClass );
                return _walk.first( tt, ((_code) _j).astCompilationUnit(), nodeTargetClass, nodeMatchFn);
            }
            return _walk.first( tt, ((_type) _j).ast(), nodeTargetClass, nodeMatchFn);
        }
        if(_j instanceof _body ){
            return _walk.first( tt, ((_body)_j).ast(), nodeTargetClass, nodeMatchFn);
        }
        return _walk.first( tt, ((_node)_j).ast(), nodeTargetClass, nodeMatchFn);
    }

    public static <T> T first(Node.TreeTraversal tt, _draft _j, Class<T> nodeTargetClass ) {
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
    public static <T, _C extends _code> T first( Collection<_C> jj, Class<T> nodeTargetClass, Predicate<T>nodeMatchFn ){
        return first( _walk.PRE_ORDER, jj, nodeTargetClass, nodeMatchFn );
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
    public static <T, _C extends _code> T first( Node.TreeTraversal tt, Collection<_C> jj, Class<T> nodeTargetClass, Predicate<T>nodeMatchFn ){
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
        if( _draft.class.isAssignableFrom(nodeTargetClass) ){
            //here I'm looking for a _field, _method, etc.
            Optional<Node> on = astStartNode.stream(tt).filter(n -> {
                    if( Objects.equals( _java.Model.AST_NODE_TO_JAVA_CLASSES.get(n.getClass()), nodeTargetClass ) ){
                        return nodeMatchFn.test((T) _java.of(n) );
                    }                    
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
}
