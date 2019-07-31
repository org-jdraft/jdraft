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
     * 
     * @param clazz
     * @return 
     */
    default P firstIn(Class clazz){
        return firstIn( _java.type(clazz) );
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
     * @param _m the the _model node
     * @return  the first matching instance or null if none is found
     */
    default P firstIn(_java _m){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_m;
            return firstIn(_t.ast());
        }
        return firstIn( ((_node)_m).ast());
    }
    
    /**
     * 
     * @param _m
     * @param nodeMatchFn
     * @return 
     */
    default P firstIn(_java _m, Predicate<P> nodeMatchFn){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_m; //only possible 
            return firstIn(_t.ast(), nodeMatchFn);
        }
        return firstIn(((_node)_m).ast(), nodeMatchFn);        
    }
    
    /**
     * 
     * @param astRootNode
     * @return the first matching instance or null
     */
    default P firstIn(Node astRootNode){
        return firstIn(astRootNode, t->true);
    }
    
    /**
     * 
     * @param astRootNode
     * @param nodeMatchFn
     * @return 
     */
    P firstIn(Node astRootNode, Predicate<P> nodeMatchFn);
    
    /**
     * 
     * @param <S>
     * @param clazz
     * @return 
     */
    default <S extends selected<P>> S selectFirstIn( Class clazz ){
        return selectFirstIn(_java.type(clazz));
    }
    
    /**
     * 
     * @param <S>
     * @param _m
     * @return 
     */
    default <S extends selected<P>> S selectFirstIn( _java _m ){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_m; //only possible 
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_node)_m).ast() );       
    }
    
    /**
     * Selects the first instance
     * @param <S>
     * @param n
     * @return 
     */
    <S extends selected<P>> S selectFirstIn( Node n );
        
    /**
     * Find and return a List of all matching the prototype within clazz
     *
     * @param clazz the runtime class (MUST HAVE JAVA SOURCE AVAILABLE IN CLASSPATH)
     * @return a List of Q that match the query
     */
    default List<P> listIn(Class clazz){
        return listIn(_java.type(clazz));
    }
    
    /**
     * 
     * @param clazz
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Class clazz, Predicate<P> nodeMatchFn){
        return listIn(_java.type(clazz), nodeMatchFn);
    }
    
    /**
     * Find and return a List of all matching node types within _n
     *
     * @param _m the root _java model node to start the search (i.e. _class,
     * _method, _packageInfo)
     * @return a List of Q that match the query
     */
    default List<P> listIn(_java _m) {
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_m; //only possible 
            return listIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listIn( ((_node)_m).ast() );               
    }
    
    /**
     * 
     * @param _m the _java model
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(_java _m, Predicate<P>nodeMatchFn){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_m; //only possible 
            return listIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
        }
        return listIn(((_node)_m).ast(),nodeMatchFn);              
    }
    
    /**
     *
     * @param astRootNode the root AST node to start the search
     * @return a List of Q matching the query
     */
    default List<P> listIn(Node astRootNode){
        return listIn( astRootNode, t->true);
    }
    
    /**
     * 
     * @param astRootNode
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Node astRootNode, Predicate<P>nodeMatchFn){        
        List<P> found = new ArrayList<>();
        forEachIn(astRootNode, nodeMatchFn, b-> found.add(b));
        return found;    
    }

    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param clazz runtime class (MUST HAVE .java source code in CLASSPATH)
     * @return the selected
     */
    default List<? extends selected> listSelectedIn(Class clazz){
        return listSelectedIn(_java.type(clazz));
    }
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the _j
     *
     * @param _m the java entity (_type, _method, etc.) where to start the
     * search
     * @return a list of the selected
     */
    default List<? extends selected> listSelectedIn(_java _m){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_m; //only possible 
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        return listSelectedIn( ((_node)_m).ast()); 
        //return listSelectedIn(_n.ast());
    }
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param astRootNode the node to start the search (TypeDeclaration,
     * MethodDeclaration)
     * @return the selected
     */
    List<? extends selected> listSelectedIn(Node astRootNode);
    
    /**
     * 
     * @param clazz the runtime Class (.java source must be on the classpath)
     * @param nodeActionFn what to do with each entity matching the prototype
     * @return the (potentially modified) _type 
     */
    default _type forEachIn(Class clazz, Consumer<P>nodeActionFn ){
        
        return forEachIn(_java.type(clazz), nodeActionFn);
    }
    
    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <M>
     * @param _m the java node to start the walk
     * @param nodeActionFn the function to run on all matching entities
     * @return the modified _java node
     */
    default <M extends _java> M forEachIn(M _m, Consumer<P> nodeActionFn){
        return forEachIn(_m, t->true, nodeActionFn);       
    }
    
    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the _nodeMatchFn within the _node _n
     *
     * @param <M>
     * @param _m the node to search through (_type, _method, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <M extends _java> M forEachIn(M _m, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                forEachIn(_c.astCompilationUnit(), nodeMatchFn, nodeActionFn);
                return _m;
            }
            _type _t = (_type)_m; //only possible 
            forEachIn(_t.ast(), nodeMatchFn, nodeActionFn); //return the TypeDeclaration, not the CompilationUnit
            return _m;
        }
        forEachIn(((_node)_m).ast(), nodeMatchFn, nodeActionFn);
        return _m;
    }
    
    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <N>
     * @param astRootNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param _nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <N extends Node> N forEachIn(N astRootNode, Consumer<P> _nodeActionFn){
        return forEachIn(astRootNode, t->true, _nodeActionFn);
    }

    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the nodeMatchFn within the Node astRootNode
     *
     * @param <N>
     * @param astRootNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    <N extends Node> N forEachIn(N astRootNode, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn);
    
    /**
     * 
     * @param <N>
     * @param clazz
     * @return 
     */
    default <N extends Node> int count( Class clazz ){
        return count( _java.type(clazz));
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
     * @param <M>
     * @param _m
     * @return 
     */
    default <M extends _java> int count(M _m ){
        AtomicInteger ai = new AtomicInteger(0);
        forEachIn(_m, e -> ai.incrementAndGet() );
        return ai.get();
    }
    
    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default _type removeIn(Class clazz){
        return removeIn(_java.type(clazz));
    } 
    
    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @param nodeMatchFn 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default _type removeIn(Class clazz, Predicate<P> nodeMatchFn){
        return removeIn(_java.type(clazz), nodeMatchFn);
    } 
    
    /**
     *
     * @param _m the root java node to start from (_type, _method, etc.)
     * @param <M> the TYPE of model node
     * @return the modified model node
     */
    default <M extends _java> M removeIn(M _m){
        removeIn(_m, t->true);
        return _m;
    }
    
    /**
     * 
     * @param <M> the TYPE of model node
     * @param _m the root _java node to start from (_type, _method, etc.)     
     * @param nodeMatchFn
     * @return the modified model node
     */
    default <M extends _java> M removeIn(M _m, Predicate<P> nodeMatchFn){
        if( _m instanceof _code ){
            _code _c = (_code)_m;
            if( _c.isTopLevel() ){
                removeIn(_c.astCompilationUnit(), nodeMatchFn);
                return _m;
            }
            _type _t = (_type)_m; //only possible 
            removeIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
            return _m;
        }
        removeIn(((_node)_m).ast(), nodeMatchFn); 
        return _m;
    }
    
    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     *
     * @param astRootNode the root node to start search
     * @param <N> the input node TYPE
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astRootNode){
        return removeIn(astRootNode, t->true);
    }

    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     * @param <N> the input node TYPE
     * @param astRootNode the root node to start search     
     * @param nodeMatchFn function to match nodes to remove
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astRootNode, Predicate<P> nodeMatchFn){
        return forEachIn(astRootNode, s-> {
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
    public static class $args implements Map<String, Object> {

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
                throw new _jDraftException("Expected an even number of key values, got (" + $nvs.length + ")");
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
     * @param <P>
     */
    interface selected<P> {

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
     * @param <M> the Draft _model representation
     */
    interface selected_model<M extends _java> {

        /**
         * @return the selected node as a _model (i.e. _method for a MethodDeclaration)
         */
        M model();
    }
}
