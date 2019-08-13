package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft.Expr;
import org.jdraft.Ast;
import org.jdraft.Stmt;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft._node;
import org.jdraft._typeRef;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Model of a query-by-prototype, (a buildable/mutable query object that has the 
 * structure of the AST entity being queried and contains a hierarchial structure)
 * 
 * $proto objects define a mechanism to walk the AST and query/modify Java code
 * matching against grammar entries via the _node model 
 *
 * @param <P> the TYPE of the node being queried for (likely a 
 * {@link com.github.javaparser.ast.Node} or 
 * {@link org.jdraft._node})
 */
public interface $proto<P> {

    /**
     * does this prototype match this ast node?
     * @param candidate an ast candidate node
     * @return
     */
    boolean match( Node candidate );

    /**
     * 
     * @param clazz
     * @return 
     */
    default P firstIn(Class clazz){
        return firstIn( _java.type(clazz) );
    }

    /**
     *
     * @param clazzes
     * @return
     */
    default P firstIn(Class... clazzes){
        for( int i=0; i< clazzes.length; i++){
            P p = firstIn( _java.type(clazzes[i]) );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param _js
     * @return
     */
    default P firstIn(_java... _js){
        for( int i=0; i< _js.length; i++){
            P p = firstIn( _js[i] );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param astNodes
     * @return
     */
    default P firstIn(Node... astNodes){
        for( int i=0; i< astNodes.length; i++){
            P p = firstIn( astNodes[i] );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     * Find the first in the collection
     * @param codeCollection a collection of code
     * @param <_J> the _code type ( _type, _class, _enum, etc.)
     * @return the first instance found within the code collection
     */
    default <_J extends _java> P firstIn(Collection<_J> codeCollection){
        List<_J> lc = codeCollection.stream().collect(Collectors.toList());
        for( int i=0; i< lc.size(); i++){
            P p = firstIn( lc.get(i) );
            if( p != null ){
                return p;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param nodeMatchFn
     * @return 
     */
    default P firstIn( Class clazz, Predicate<P> nodeMatchFn){
        return firstIn(_java.type(clazz).astCompilationUnit(), nodeMatchFn);
    }
    
    /**
     * Find the first instance matching the prototype instance within the node
     * @param _j the the _model node
     * @return  the first matching instance or null if none is found
     */
    default P firstIn(_java _j){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j;
            return firstIn(_t.ast());
        }
        return firstIn( ((_node)_j).ast());
    }
    
    /**
     * 
     * @param _j
     * @param nodeMatchFn
     * @return 
     */
    default P firstIn(_java _j, Predicate<P> nodeMatchFn){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_j; //only possible
            return firstIn(_t.ast(), nodeMatchFn);
        }
        return firstIn(((_node)_j).ast(), nodeMatchFn);
    }
    
    /**
     * 
     * @param astStartNode
     * @return the first matching instance or null
     */
    default P firstIn(Node astStartNode){
        return firstIn(astStartNode, t->true);
    }
    
    /**
     * 
     * @param astStartNode
     * @param nodeMatchFn
     * @return 
     */
    P firstIn(Node astStartNode, Predicate<P> nodeMatchFn);

    /**
     * 
     * @param <S>
     * @param clazz
     * @return 
     */
    default <S extends selected> S selectFirstIn( Class clazz ){
        return selectFirstIn( _java.type(clazz));
    }

    /**
     *
     * @param <S>
     * @param classes
     * @return
     */
    default <S extends selected> S selectFirstIn( Class... classes ){
        for(int i=0;i<classes.length; i++){
            S s = selectFirstIn( _java.type(classes[i]) );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param <S>
     * @param _js
     * @return
     */
    default <S extends selected> S selectFirstIn( _java... _js ){
        for(int i=0;i<_js.length; i++){
            S s = selectFirstIn( _js[i] );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param <S>
     * @param _jc
     * @return
     */
    default <S extends selected, _J extends _java> S selectFirstIn(Collection<_J> _jc ){
        List<_J> l = _jc.stream().collect(Collectors.toList());
        for(int i=0;i<l.size(); i++){
            S s = selectFirstIn( l.get(i) );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     * 
     * @param <S>
     * @param _j
     * @return 
     */
    default <S extends selected> S selectFirstIn( _java _j ){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_node)_j).ast() );
    }

    /**
     *
     * @param astStartNodes
     * @param <S>
     * @return
     */
    default <S extends selected> S selectFirstIn( Node... astStartNodes ){
        for(int i=0;i<astStartNodes.length; i++){
            S s = selectFirstIn( astStartNodes[i] );
            if( s != null){
                return s;
            }
        }
        return null;
    }

    /**
     * Selects the first instance
     * @param <S>
     * @param astNode
     * @return 
     */
    <S extends selected> S selectFirstIn( Node astNode );
        
    /**
     * Find and return a List of all matching the prototype within clazz
     *
     * @param clazz the runtime class (MUST HAVE JAVA SOURCE AVAILABLE IN CLASSPATH)
     * @return a List of P that match the query
     */
    default List<P> listIn(Class clazz){
        return listIn( _java.type(clazz));
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param classes all of the runtime classes (MUST HAVE SOURCE AVAILABLE ON CLASSPATH)
     * @return a List of P that match the query
     */
    default List<P> listIn( Class...classes ){
        List<P> found = new ArrayList<>();
        Arrays.stream(classes).forEach(c -> found.addAll( listIn(c) ));
        return found;
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _js any collection of _code entities( _class, _enum, ...etc)
     * @param <_J> the underlying _code type (_code, _type, _packageInfo, etc.)
     * @return list of matching P for the query
     */
    default <_J extends _java> List<P> listIn(Collection<_J> _js){
        List<P> found = new ArrayList<>();
        _js.forEach(c -> found.addAll( listIn(c) ));
        return found;
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _js any collection of _code entities( _class, _enum, ...etc)
     * @param nodeMatchFn additional function predicate for matching
     * @param <_J> the underlying _code type (_code, _type, _packageInfo, etc.)
     * @return list of matching P for the query
     */
    default <_J extends _java> List<P> listIn(Collection<_J> _js, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        _js.forEach(c -> found.addAll( listIn(c, nodeMatchFn) ));
        return found;
    }

    /**
     * 
     * @param clazz
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Class clazz, Predicate<P> nodeMatchFn){
        return listIn( _java.type(clazz), nodeMatchFn);
    }

    /**
     *
     * @param _js
     * @return
     */
    default List<P> listIn(_java..._js){
        List<P> found = new ArrayList<>();
        Arrays.stream(_js).forEach( j -> found.addAll( listIn(j)) );
        return found;
    }

    /**
     * Find and return a List of all matching node types within _n
     *
     * @param _j the root _java model node to start the search (i.e. _class,
     * _method, _packageInfo)
     * @return a List of Q that match the query
     */
    default List<P> listIn(_java _j) {
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return listIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listIn( ((_node)_j).ast() );
    }
    
    /**
     * 
     * @param _j the _java model
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(_java _j, Predicate<P>nodeMatchFn){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_j; //only possible
            return listIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
        }
        return listIn(((_node)_j).ast(),nodeMatchFn);
    }

    /**
     *
     * @param astNodes
     * @return
     */
    default List<P> listIn( Node...astNodes){
        ArrayList<P> ap = new ArrayList<>();
        Arrays.stream(astNodes).forEach( n -> ap.addAll( listIn(n) ));
        return ap;
    }

    /**
     *
     * @param astNode the root AST node to start the search
     * @return a List of Q matching the query
     */
    default List<P> listIn(Node astNode){
        return listIn( astNode, t->true);
    }
    
    /**
     * 
     * @param astStartNode
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Node astStartNode, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        forEachIn(astStartNode, nodeMatchFn, b-> found.add(b));
        return found;    
    }

    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param clazz runtime class (MUST HAVE .java source code in CLASSPATH)
     * @return the selected
     */
    default <S extends selected> List<S> listSelectedIn(Class clazz){
        return listSelectedIn( _java.type(clazz));
    }

    /**
     *
     * @param _js
     * @param <S>
     * @param <_J>
     * @return
     */
    default <S extends selected, _J extends _java> List<S> listSelectedIn(Collection<_J> _js){
        List<S> sel = new ArrayList<>();
        _js.forEach(_j -> sel.addAll( listSelectedIn( _j )) );
        return sel;
    }

    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param clazz runtime class (MUST HAVE .java source code in CLASSPATH)
     * @return the selected

    default List<? extends selected> listSelectedIn(Class... clazz){
        List<? extends selected> sel = new ArrayList<>();
        for(int i=0;i<clazz.length; i++){
            _java _j = (_type)_java.type(clazz[i]);
            List<?> ls = listSelectedIn(_j);
            sel.addAll(ls);
        }
        //Arrays.stream(clazz).forEach( c-> sel.addAll( listSelectedIn( _java.type(c)) ) );
        return sel;
    }
    */
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the _j
     *
     * @param _j the java entity (_type, _method, etc.) where to start the
     * search
     * @return a list of the selected
     */
    default <S extends selected> List<S> listSelectedIn(_java _j){
        if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_node)_j).ast());
    }
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param astNode the node to start the search (TypeDeclaration,
     * MethodDeclaration)
     * @return the selected
     */
    <S extends selected> List<S> listSelectedIn(Node astNode);
    
    /**
     * 
     * @param clazz the runtime Class (.java source must be on the classpath)
     * @param nodeActionFn what to do with each entity matching the prototype
     * @return the (potentially modified) _type 
     */
    default <_T extends _type> _T forEachIn(Class clazz, Consumer<P>nodeActionFn ){
        
        return forEachIn( (_T)_java.type(clazz), nodeActionFn);
    }

    /**
     *
     * @param _js
     * @param nodeActionFn
     * @return
     */
    default <_J extends _java> List<_J> forEachIn(Collection<_J> _js, Consumer<P>nodeActionFn ){
        List<_J> ts = new ArrayList<>();
        _js.stream().forEach( j-> ts.add( forEachIn( j, nodeActionFn) ) );
        return ts;
    }

    /**
     *
     * @param _js
     * @param nodeMatchFn
     * @param nodeActionFn
     * @return
     */
    default <_J extends _java> List<_J> forEachIn(Collection<_J> _js, Predicate<P> nodeMatchFn, Consumer<P>nodeActionFn ){
        List<_J> ts = new ArrayList<>();
        _js.stream().forEach( j-> ts.add( forEachIn( j, nodeMatchFn, nodeActionFn) ) );
        return ts;
    }

    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <_J>
     * @param _j the java node to start the walk
     * @param nodeActionFn the function to run on all matching entities
     * @return the modified _java node
     */
    default <_J extends _java> _J forEachIn(_J _j, Consumer<P> nodeActionFn){
        return forEachIn(_j, t->true, nodeActionFn);
    }
    
    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the _nodeMatchFn within the _node _n
     *
     * @param <_J>
     * @param _j the node to search through (_type, _method, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <_J extends _java> _J forEachIn(_J _j, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forEachIn(_c.astCompilationUnit(), nodeMatchFn, nodeActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forEachIn(_t.ast(), nodeMatchFn, nodeActionFn); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        forEachIn(((_node) _j).ast(), nodeMatchFn, nodeActionFn);
        return _j;
    }
    
    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <N>
     * @param astNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param _nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <N extends Node> N forEachIn(N astNode, Consumer<P> _nodeActionFn){
        return forEachIn(astNode, t->true, _nodeActionFn);
    }

    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the nodeMatchFn within the Node astRootNode
     *
     * @param <N>
     * @param astNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    <N extends Node> N forEachIn(N astNode, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn);
    
    /**
     *
     * @param clazz
     * @return 
     */
    default int count( Class clazz ){
        return count( _java.type(clazz));
    }

    /**
     * Count the number of occurrences within the collection of code
     * @param cs the collection to search through
     * @param <_C>
     * @return
     */
    default <_C extends _code> int count( Collection<_C> cs){
        AtomicInteger ai = new AtomicInteger();
        cs.forEach( c -> ai.addAndGet( count(c)));
        return ai.get();
    }

    /**
     *
     * @param clazzes
     * @return
     */
    default int count( Class... clazzes ){
        int count = 0;
        for(int i=0;i<clazzes.length;i++) {
            count +=count((_type) _java.type(clazzes[i]));
        }
        return count;
    }

    /**
     * Determines the count found in all of the Ast nodes
     * @param astNodes AST nodes (TypeDeclaration, MethodDeclaration, CompilationUnit)
     * @param <N> the underlying Node type
     * @return the count of instances found
     */
    default <N extends Node> int count( N... astNodes ){
        int count = 0;
        for(int i=0;i<astNodes.length;i++) {
            count +=count(astNodes[i]);
        }
        return count;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @return 
     */
    default <N extends Node> int count( N astNode ){
        AtomicInteger ai = new AtomicInteger(0);
        forEachIn( astNode, e -> ai.incrementAndGet() );
        return ai.get();
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @return 
     */
    default <_J extends _java> int count(_J _j ){
        AtomicInteger ai = new AtomicInteger(0);
        forEachIn(_j, e -> ai.incrementAndGet() );
        return ai.get();
    }

    /**
     *
     * @param _js
     * @param <_J>
     * @return
     */
    default <_J extends _java> Collection<_J> removeIn(Collection<_J> _js ){
        _js.forEach( _j -> removeIn(_j) );
        return _js;
    }

    /**
     *
     * @param _js
     * @param <_J>
     * @return
     */
    default <_J extends _java> Collection<_J> removeIn(Collection<_J> _js, Predicate<P> nodeMatchFn){
        _js.forEach( _j -> removeIn(_j, nodeMatchFn) );
        return _js;
    }

    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default _type removeIn(Class clazz){
        return removeIn( _java.type(clazz));
    } 
    
    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @param nodeMatchFn 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default _type removeIn(Class clazz, Predicate<P> nodeMatchFn){
        return removeIn( _java.type(clazz), nodeMatchFn);
    } 
    
    /**
     *
     * @param _j the root java node to start from (_type, _method, etc.)
     * @param <_J> the TYPE of model node
     * @return the modified model node
     */
    default <_J extends _java> _J removeIn(_J _j){
        removeIn(_j, t->true);
        return _j;
    }
    
    /**
     * 
     * @param <_J> the TYPE of model node
     * @param _j the root _java node to start from (_type, _method, etc.)
     * @param nodeMatchFn
     * @return the modified model node
     */
    default <_J extends _java> _J removeIn(_J _j, Predicate<P> nodeMatchFn){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                removeIn(_c.astCompilationUnit(), nodeMatchFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            removeIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        removeIn(((_node) _j).ast(), nodeMatchFn);
        return _j;
    }
    
    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     *
     * @param astNode the root node to start search
     * @param <N> the input node TYPE
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astNode){
        return removeIn(astNode, t->true);
    }

    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     * @param <N> the input node TYPE
     * @param astNode the root node to start search
     * @param nodeMatchFn function to match nodes to remove
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astNode, Predicate<P> nodeMatchFn){
        return forEachIn(astNode, s-> {
            if( nodeMatchFn.test( s ) ){
                if( s instanceof Node ){
                    ((Node) s).remove();
                } else{
                    ((_node)s).ast().remove();
                }
            }            
        });
    }
    
    /**
     * An extra layer on top of a Tokens that is specifically
     * for holding value data that COULD be Expressions, Statements and the 
     * like
     */
    class $args implements Map<String, Object> {

        /**
         *
         */
        private Tokens tokens;

        public static $args of(){
            return new $args( Tokens.of() ); 
        }
        
        public static $args of(Tokens ts) {
            if (ts == null) {
                return null;
            }
            return new $args(ts);
        }

        public $args(Tokens ts) {
            this.tokens = ts;
        }

        public Object get(String $name) {
            return tokens.get($name);
        }

        public Tokens asTokens() {
            return tokens;
        }

        public Expression expr(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return Expr.of(obj.toString());
        }

        public Statement stmt(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return Stmt.of(obj.toString());
        }

        public _typeRef type(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return _typeRef.of(obj.toString());
        }

        /**
         * Reads the data from the $nameValues and parse & return the data as
         * a List of Statements
         * @param $name
         * @return 
         */
        public List<Statement> stmts(String $name) {
            Object obj = get($name);
            if (obj == null) {
                return null;
            }
            if (obj.toString().trim().length() == 0) {
                return Collections.EMPTY_LIST;
            }
            return Stmt.block(obj.toString()).getStatements();
        }

        /**
         * 
         * @param tokens
         * @return 
         */
        public boolean isConsistent(Tokens tokens ){
            return this.tokens.isConsistent(tokens);
        }
        
        /**
         * is the clause with the key equal to the Type?
         *
         * @param $name
         * @param astType
         * @return true if
         */
        public boolean is(String $name, Type astType) {
            return is($name, _typeRef.of(astType));
        }

        /**
         * 
         * @param $name
         * @param _t
         * @return 
         */
        public boolean is(String $name, _typeRef _t) {
            return type($name).equals(_t);
        }

        /**
         * is the clause with the key equal to the expression?
         *
         * @param $name
         * @param exp
         * @return true if
         */
        public boolean is(String $name, Expression exp) {
            Expression ex = expr($name);
            return exp.equals(ex);
        }

        /**
         * is the clause with the key equal to the expression?
         *
         * @param $name
         * @param st
         * @return true if
         */
        public boolean is(String $name, Statement st) {
            Statement stmt = stmt($name);
            return stmt.toString(Ast.PRINT_NO_COMMENTS).equals(st.toString(Ast.PRINT_NO_COMMENTS));
        }

        public boolean is($args $nvs) {
            return this.equals($nvs);
        }

        public boolean is(Tokens tks) {
            return this.equals($args.of(tks));
        }

        public boolean is(String $name, Class clazz ){
            Object o = get($name);
            if( o != null ){
                return _typeRef.of( o.toString() ).equals(_typeRef.of(clazz));
            }
            return false;            
        }
        
        
        /**
         * 
         * @param $name
         * @param expectedValue
         * @return 
         */
        public boolean is(String $name, Object expectedValue) {
            //this matches nullExpr or simply not there
            Object o = get($name);

            if (expectedValue == null || expectedValue instanceof NullLiteralExpr) {
                if (!(o == null || o instanceof NullLiteralExpr || o.equals("null"))) {
                    return false;
                }
                return true;
            }
            if (expectedValue instanceof String && o instanceof String) {
                String v = (String) expectedValue;
                String s = (String) o;

                if (s.startsWith("\"") && s.endsWith("\"")) {
                    s = s.substring(1, s.length() - 1);
                }
                if (v.startsWith("\"") && v.endsWith("\"")) {
                    v = v.substring(1, v.length() - 1);
                }
                return s.equals(v);
            }
            if (expectedValue instanceof Expression) {
                return Expr.equivalent((Expression) expectedValue, get($name));
            } else if (expectedValue instanceof String) {
                try {
                    return Expr.equivalent(Expr.of((String) expectedValue), o);
                } catch (Exception e) {

                }
                return Objects.equals(expectedValue, get($name));
            } else if (o.getClass().equals(expectedValue.getClass())) {
                return o.equals(expectedValue);
            }
            return expectedValue.toString().equals(o);
        }

        /**
         *
         * @param $nvs the name values
         * @return
         */
        public boolean is(Object... $nvs) {
            if ($nvs.length % 2 == 1) {
                throw new _draftException("Expected an even number of key values, got (" + $nvs.length + ")");
            }
            for (int i = 0; i < $nvs.length; i += 2) {
                String key = $nvs[i].toString();
                if ( !is(key, get(key) ) ) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !o.getClass().equals($args.class)) {
                return false;
            }
            $args co = ($args) o;
            return Objects.equals(co.tokens, tokens);
        }

        @Override
        public int hashCode() {
            return this.tokens.hashCode();
        }

        @Override
        public String toString() {
            return this.tokens.toString();
        }

        @Override
        public int size() {
            return tokens.size();
        }

        @Override
        public boolean isEmpty() {
            return tokens.isEmpty();
        }

        @Override
        public boolean containsKey(Object $name) {
            return tokens.containsKey($name);
        }

        @Override
        public boolean containsValue(Object value) {
            return tokens.containsValue(value);
        }

        @Override
        public Object get(Object $name) {
            return tokens.get($name);
        }

        @Override
        public Object put(String $name, Object value) {
            return tokens.put($name, value);
        }

        @Override
        public Object remove(Object $name) {
            return tokens.remove($name);
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> $nvs) {
            tokens.putAll($nvs);
        }

        @Override
        public void clear() {
            tokens.clear();
        }

        @Override
        public Set<String> keySet() {
            return tokens.keySet();
        }

        @Override
        public Collection<Object> values() {
            return tokens.values();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return tokens.entrySet();
        }
    }

    /**
     * a selected entity from a prototype query
     *
     */
    interface selected {

        $args args();

        /** Get the value of this $param$ via the name */
        default Object get(String $name ){
            return args().get($name);
        }
        
        default boolean is(Object... $nameValues) {
            return args().is($nameValues);
        }

        default boolean is(String $name, String value) {
            return args().is($name, value);
        }

        default boolean is(String $name, Expression value) {
            return args().is($name, value);
        }

        default boolean is(String $name, Statement value) {
            return args().is($name, value);
        }

        default boolean is(String $name, Type value) {
            return args().is($name, value);
        }                
    }

    /**
     * The entity that is Selected is/has an AST node Representation
     *
     * @param <N> the specific type of AST Node that is selected
     */
    interface selectedAstNode<N extends Node> {

        /**
         * @return the selected AST Node (i.e. Expression, Statement,
         * VariableDeclarator)
         */
        N ast();
    }

    /**
     * The entity that is selected is/has a draft _node Representation (which
     * wraps the underlying Ast Node/Nodes)
     *
     * @param <_J> the jDraft _java representation
     */
    interface selected_model<_J extends _java> {

        /**
         * @return the selected node as a _model (i.e. _method for a MethodDeclaration)
         */
        _J _node();
    }
}
