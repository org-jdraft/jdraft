package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.walk.Walk;
import org.jdraft.walk._walk;
import org.jdraft.walk._walkFeatures;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Super interface for Java syntactical tree objects:
 * <UL>
 *     <LI>{@link _node}s containing individual entities within the Java syntax tree</LI>
 *     <LI>{@link _group}s containing semantically unordered monotonic entities within the Java syntax tree</LI>
 *     <LI>{@link _orderedGroup}s containing semantically ordered monotonic entities within the Java syntax tree</LI>
 * </UL>
 * @param <_T> the _tree implementation type
 */
public interface _tree<_T> extends _java._domain {

    /**
     * Returns the features Object which defines the order of the features as well as the parser
     * for the _tree entity
     * @return the {@link _feature._features}
     */
    _feature._features<_T> features();

    /**
     * Build and return an (independent/mutable) copy of this {@link _tree} entity
     * @return an independent mutable copy of the entity
     */
    _T copy();


    /**
     * A tree entity that maps 1-to-1 to an Ast (Syntax) entity in the syntax tree and JavaParser
     *
     * @param <N> the JavaParser Ast Node type
     * @param <_N> the {@link _node} implementation type
     */
    interface _node<N extends Node, _N extends _node> extends _tree<_N> {

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PRE_ORDER fashion)
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
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default _walk<_node> walk(){
            return new _walk(Walk.PRE_ORDER, this, _node.class);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * PostOrder ("leaves first", "bottom-up") from (A) : D,E,B,C,A
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default _walk<_node> walkPostOrder(){
            return new _walk(Walk.POST_ORDER, this, _node.class);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * Breadth-First (or Level Order) from (A): A,B,C,D,E
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default _walk<_node> walkBreadthFirst(){
            return new _walk(Walk.BREADTH_FIRST, this, _node.class);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PARENTS fashion)
         *
         * <PRE>
         *             A
         *           /  \
         *          B    C
         *         / \
         *        D   E
         *
         * "Parents" (or "up to root"):
         *    from D: B, A
         *    from E: B, A
         *    from B: A
         *    from C: A
         * </PRE>
         *
         * @return
         */
        default _walk<_node> walkParents(){
            return new _walk<_node>(Walk.PARENTS, this, _node.class);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in DIRECT_CHILDREN fashion)
         *
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
         * @return
         */
        default _walk<_node> walkDirectChildren(){
            return new _walk<_node>(Walk.DIRECT_CHILDREN, this, _node.class);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PRE_ORDER fashion)
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
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_F> _walkFeatures<_F> walk(_feature<?, _F>...features){
            return new _walkFeatures(Walk.PRE_ORDER, this, features);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * PostOrder ("leaves first", "bottom-up") from (A) : D,E,B,C,A
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_F> _walkFeatures<_F> walkPostOrder(_feature<?, _F>...features){
            return new _walkFeatures(Walk.POST_ORDER, this, features);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * Breadth-First (or Level Order) from (A): A,B,C,D,E
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_F> _walkFeatures<_F> walkBreadthFirst(_feature<?, _F>...features){
            return new _walkFeatures(Walk.BREADTH_FIRST, this, features);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PARENTS fashion)
         *
         * <PRE>
         *             A
         *           /  \
         *          B    C
         *         / \
         *        D   E
         *
         * "Parents" (or "up to root"):
         *    from D: B, A
         *    from E: B, A
         *    from B: A
         *    from C: A
         * </PRE>
         *
         * @return
         */
        default <_F> _walkFeatures<_F> walkParents(_feature<?, _F>...features){
            return new _walkFeatures(Walk.PARENTS, this);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in DIRECT_CHILDREN fashion)
         *
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
         * @return
         */
        default <_F> _walkFeatures<_F> walkDirectChildren(_feature<?, _F>...features){
            return new _walkFeatures<_F>(Walk.DIRECT_CHILDREN, this, features);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PRE_ORDER fashion)
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
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_D extends _java._domain> _walk<_D> walk(Class<_D> targetClass){
            return new _walk(Walk.PRE_ORDER, this, targetClass);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * PostOrder ("leaves first", "bottom-up") from (A) : D,E,B,C,A
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_D extends _java._domain> _walk<_D> walkPostOrder(Class<_D> targetClass){
            return new _walk(Walk.POST_ORDER, this, targetClass);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default POST_ORDER fashion)
         *
         * <PRE>
         *            (A)
         *           /  \
         *          B    C
         *         / \
         *        D   E
         * Breadth-First (or Level Order) from (A): A,B,C,D,E
         * </PRE>
         * @return a _walk that will allow the traversal of the AST starting at the current {@link _node}
         */
        default <_D extends _java._domain> _walk<_D> walkBreadthFirst(Class<_D> targetClass){
            return new _walk(Walk.BREADTH_FIRST, this, targetClass);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in default PARENTS fashion)
         *
         * <PRE>
         *             A
         *           /  \
         *          B    C
         *         / \
         *        D   E
         *
         * "Parents" (or "up to root"):
         *    from D: B, A
         *    from E: B, A
         *    from B: A
         *    from C: A
         * </PRE>
         *
         * @return
         */
        default <_D extends _tree> _walk<_D> walkParents(Class<_D> targetClass){
            return new _walk(Walk.PARENTS, this, targetClass);
        }

        /**
         * Build and return a _walk object that will prepare walking the AST (in DIRECT_CHILDREN fashion)
         *
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
         * @return
         */
        default <_D extends _java._domain> _walk<_D> walkDirectChildren(Class<_D> targetClass){
            return new _walk(Walk.DIRECT_CHILDREN, this, targetClass);
        }

        /**
         * @return the underlying AST Node instance being manipulated
         * NOTE: the AST node contains physical information (i.e. location in
         * the file (line numbers) and syntax related parent/child relationships
         */
        N ast();

        /**
         * Replace this ast node (wherever it resides in the Ast TREE) with the ASt node
         * and return the replacement Ast node instance
         * @param n the ast nod to replace this node in the tree
         * @param <N> the type of Ast node to replace with
         * @return the replacement node
         */
        default <N extends Node> N replace(N n){
            boolean rep = this.ast().replace(n);
            if( rep ) {
                return n;
            }
            throw new _jdraftException("unable to replace "+ this+" with "+ n);
        }

        /**
         * Replace this ast node (wherever it resides in the Ast TREE) with the ASt node
         * and return the replacement Ast node instance
         * @param _n the astNode to replace this node in the tree
         * @param <_N> the type of _astNode node to replace with
         * @return the replacement _astNode implementation
         */
        default <_N extends _node> _N replace(_N _n){
            replace(_n.ast());
            return _n;
        }

        /**
         * Pass in the AST Pretty Printer configuration which will determine how the code
         * is formatted and return the formatted String representing the code.
         *
         * @see com.github.javaparser.printer.PrettyPrintVisitor the original visitor for walking and printing nodes in AST
         * @see Print.PrintNoAnnotations a configurable subclass of PrettyPrintVisitor that will not print ANNOTATIONS
         * @see PrettyPrinterConfiguration the configurations for spaces, Tabs, for printing
         * @see Print#PRINT_NO_ANNOTATIONS_OR_COMMENTS a premade implementation for
         * @see Print#PRINT_NO_COMMENTS
         *
         * @param codeFormat the details on how the code will be formatted (for this element and all sub ELEMENTS)
         * @return A String representing the .java code
         */
        default String toString(PrettyPrinterConfiguration codeFormat) {
            return ast().toString(codeFormat);
        }

        /**
         * Pass a match function to verify based on a lambda predicate
         * @param matchFn a function to match against the entity
         * @return true if the match function is verified, false otherwise
         */
        default boolean is(Predicate<_N> matchFn){
            return matchFn.test((_N)this);
        }

        default boolean is(Stencil stencil){
            return stencil.matches(toString(Print.PRINT_NO_COMMENTS));
        }

        /**
         * is the String representation equal to the _node entity
         * (i.e. if we parse the string, does it create an AST entity that
         * is equal to the node?)
         *
         * @param stringRep the string representation of the node
         * (parsed as an AST and compared to this entity to see if equal)
         * @return true if the Parsed String represents the entity
         */
        default boolean is(String... stringRep){
            String str = Text.combine(stringRep);
            if( str.startsWith("$") && str.endsWith("$")){
                Stencil st = Stencil.of(str);
                if( st.isMatchAny() ){
                    return true;
                }
            }
            try{
                //_node _n = (_node) _java.of(_java.node( getClass(), Text.combine(stringRep)));
                _N n = features().parse(stringRep);
                Stencil st = Stencil.of( n.toString(Print.PRINT_NO_COMMENTS));
                if( st.isFixedText() ){
                    //System.out.println ("ITS FIXED TEXT" + _n +System.lineSeparator() +this );
                    return equals( n );
                } else {
                    //extract all features from this and other map
                    Map<_feature<_N, ?>, Object> ofm = this.features().featureMap(n);
                    Map<_feature<_N, ?>, Object> tfm = this.features().featureMap((_N)this);

                    //for ALL non null features in
                    AtomicBoolean isSame = new AtomicBoolean(true);
                    ofm.forEach( (_f, o)->{
                        if( isSame.get() && o != null ){ //if the feature is NOT null
                            Stencil s = Stencil.of( o.toString() );
                            if( s.isMatchAny() ){ //its a variable thing, so it matches ANYTHING
                                System.out.println("IsMatchAny");
                            } else if( s.isFixedText() ){ //its fixed text, dont match the text, match the impl
                                Object t = tfm.get(_f); //get the corresponding feature from the other one
                                if( !Objects.equals(o, t) ){ //check equality at the feature object level
                                    System.out.println("NOot same");
                                    isSame.set(false);
                                }
                            } else { //its a mix of text and variables, so lets
                                //System.out.println("Mix of text and vars");
                                Object t = tfm.get(_f); //get the corresponding feature from the other one
                                if( t instanceof _node){
                                    _node _t = (_node)t;
                                    if( !_t.is(s)){
                                        //System.out.println( "NOt Same 1");
                                        isSame.set(false);
                                    }
                                }else if( t instanceof _group){
                                    _group _g = (_group)t;
                                }
                                else if( !s.matches(t.toString()) ){
                                    //System.out.println( "NOt Same 2 "+s + t.toString());
                                    if( t instanceof _annoEntryPair){
                                        _annoEntryPair _ep = (_annoEntryPair)t;
                                        if( _ep.isValueOnly() ){

                                        }
                                    } else {
                                        isSame.set(false);
                                    }
                                }
                            }
                        }
                    });
                    return isSame.get();
                    //System.out.println ("ITS STENCIL TEXT");
                    //return st.matches(this.toString(Print.PRINT_NO_COMMENTS));
                }
            } catch(Exception e){
                //System.out.println( "FAILED YO "+ e);
                return false;
            }
        }

        /**
         * Is the AST node representation equal to the underlying entity
         * @param astNode the astNode to compare against
         * @return true if they represent the same _node, false otherwise
         */
        default boolean is(N astNode) {
            return this.toString(Print.PRINT_NO_COMMENTS).equals(astNode.toString(Print.PRINT_NO_COMMENTS));
        }
    }

    /**
     * A grouping of monotonic (same type) entities where the order doesn't matter
     * to the semantics of the group.  While they may may be stored in an ordered NodeList<N>
     * this represents the syntax, however in the semantic side, this:
     * <CODE>
     * void m() throws A, B
     * </CODE>
     * (while not syntactically the same)
     * ...is semantically equivalent to this:
     * <CODE>
     *  void m() throws B, A
     * </CODE>
     *
     * @see _annos < AnnotationExpr, _annoRef >
     * @see _imports<  ImportDeclaration ,_import>
     * @see _modifiers <com.github.javaparser.ast.Modifier,_modifier>
     * @see _moduleExports
     * @see _moduleOpens
     * @see _moduleProvides
     * @see _throws<  ReferenceType ,_typeRef>
     * @see _typeArgs
     * @see _typeParams < TypeParameter, _typeParam >
     * @see _variablesExpr
     *
     * @param <EL> the Ast syntax {@link Node} type of each element in the {@link _group}
     * @param <_EL> the _jdraft {@link _node} type of each element in the {@link _group}
     * @param <_G> the group implementation type
     */
    interface _group<EL extends Node, _EL extends _node, _G extends _group> extends _tree<_G> {

        /**
         * is the group emtpy
         * @return
         */
        default boolean isEmpty(){
            return size() == 0;
        }

        /**
         * the size of the group
         * @return
         */
        default int size(){
            return listAstElements().size();
        }

        /**
         * Returns a list of the _EL elements
         * @return the list of elements in the group in the order they are declared in the syntax
         */
        List<_EL> list();

        /**
         * NOTE this returns a Mutable reference to the Ast elements
         *
         * meaning:
         * IF YOU ADD ELEMENTS TO THE LIST THEY ARE DIRECTLY ADDED TO THE AST
         * (this is NOT true if you add elements to the list returned in list())
         * @return a MUTABLE reference to the Asts List & elements
         */
        NodeList<EL> listAstElements();

        /**
         * Find and return a list of the elements that match the predicate
         * @param _matchFn the matching predicate
         * @return the list of tree elements in the group (in the order they appear in the group) that match the predicate
         */
        default List<_EL> list(Predicate<_EL> _matchFn){
            return list().stream().filter(_matchFn).collect(Collectors.toList());
        }

        /**
         * returns a list of all instances in the group that are implementations of the implClass type
         * @param implClass the implClass type (NOTE: could be an interface)
         * @param <_EI> the element implementation type
         * @return list of group instances of the implClass
         */
        default <_EI extends _EL> List<_EI> list( Class<_EI> implClass){
            return list().stream().filter(e -> implClass.isAssignableFrom(e.getClass()))
                    .map(e-> (_EI)e).collect(Collectors.toList());
        }

        /**
         * List all of the elements of the group that are of the following implementation classes
         * @param implClasses the implementation classes provided
         * @return the list
         */
        default List<_EL> list(Class<? extends _EL>...implClasses){
            return list().stream().filter(e-> Arrays.stream(implClasses).
                    anyMatch(ec -> ec.isAssignableFrom(e.getClass()))).collect(Collectors.toList());
        }

        /**
         * List elements of a particular implementation class that abide by the predicate
         * @param implClass the element implementation class
         * @param _matchFn predicate to match
         * @param <_EI> elemednt implementation type
         * @return a list containing all elements that are of the implClass and match the predicate
         */
        default <_EI extends _EL> List<_EI> list(Class<_EI>implClass, Predicate<_EI> _matchFn){
            return list().stream().filter(el-> {
                if( implClass.isAssignableFrom(el.getClass())){
                    return _matchFn.test( (_EI)el);
                }
                return false;
            }).map(el -> (_EI)el).collect(Collectors.toList());
        }

        /**
         * List the AST elements that match this predicate
         * @param matchFn
         * @return
         */
        default List<EL> listAstElements(Predicate<EL> matchFn){
            return listAstElements().stream().filter(matchFn).collect(Collectors.toList());
        }

        /**
         * Are ANY of the elements of the group implementers of the following implementation classes
         * @param implClasses implementation classes
         * @return
         */
        default boolean anyMatch(Class<? extends _EL>...implClasses){
            return list(implClasses).size() > 0;
        }

        /**
         * Does any element in the group match this predicate?
         * @param _matchFn
         * @return
         */
        default boolean anyMatch(Predicate<_EL> _matchFn){
            return list().stream().anyMatch(_matchFn);
        }

        /**
         * Does any element in the group match this predicate?
         * @param implClass the element implementation type
         * @param _matchFn the predicate based on the element implementation type
         * @param <_EI> the element implementation type
         * @return true if any of the elements in the group match the type and predicate
         */
        default <_EI extends _EL> boolean anyMatch(Class<_EI>implClass, Predicate<_EI> _matchFn){
            return list(implClass, _matchFn).size() > 0;
        }

        /**
         * Do ALL elements in the group match this predicate?
         * @param _matchFn
         * @return
         */
        default boolean allMatch(Predicate<_EL> _matchFn){
            return list().stream().allMatch(_matchFn);
        }

        /**
         * Are all of the elements of the group implementers of the following implementation classes
         * @param implClasses implementation classes
         * @return
         */
        default boolean allMatch(Class<? extends _EL>...implClasses){
            return list(implClasses).size() == size();
        }

        /**
         * are all the elements of the group this implClass type and do they match the predicate?
         * @param implClass
         * @param _matchFn
         * @param <_EI> element implementation type
         * @return
         */
        default <_EI extends _EL> boolean allMatch(Class<_EI>implClass, Predicate<_EI> _matchFn){
            return list(implClass, _matchFn).size() == size();
        }

        String toString(PrettyPrinterConfiguration prettyPrinter);

        boolean is(String code);

        /*
        default boolean is(Stencil stencil){
            if( stencil.isMatchAny()){
                return true;
            }
            //hmm I guess?
            return stencil.matches(toString(Print.PRINT_NO_COMMENTS));
        }
         */

        default boolean is(_EL... _els){
            Set<_EL> _arr = new HashSet<>();
            Arrays.stream(_els).forEach(n -> _arr.add(n));
            return is(_arr);
        }

        default boolean is(List<_EL> _els){
            Set<_EL> hs = new HashSet<>();
            hs.addAll(_els);
            return is(hs);
        }

        default boolean is(Set<_EL> _els){
            if( this.size() != _els.size() ){
                return false;
            }
            Set<_EL> _tels = new HashSet<>();
            _tels.addAll( list() );

            return Objects.equals( _tels, _els);
            /*
            for(int i=0;i<_els.size(); i++){
                if( !Objects.equals(_els.get(i), _tels.get(i))){
                    return false;
                }
            }
            return true;
             */
        }

        default _G set(List<_EL> els){
            listAstElements().clear();
            els.forEach( el -> listAstElements().add((EL)el.ast()));
            return (_G)this;
        }

        default <_EI extends _EL> _EI first(Class<_EI> implClass){
            List<_EI> _els = this.list(implClass);
            if( _els.isEmpty() ){
                return null;
            }
            return _els.get(0);
        }

        default <_EI extends _EL> _EI first(Class<_EI> implClass, Predicate<_EI> _matchFn){
            List<_EI> _els = this.list(implClass, _matchFn);
            if( _els.isEmpty() ){
                return null;
            }
            return _els.get(0);
        }

        default _EL first(Predicate<_EL> matchFn){
            List<_EL> _els = this.list(matchFn);
            if( _els.isEmpty() ){
                return null;
            }
            return _els.get(0);
        }

        default boolean has(_EL target){
            return !list( el-> el.equals(target)).isEmpty();
        }

        default boolean has(Predicate<_EL> matchFn){
            return !list( matchFn).isEmpty();
        }

        /**
         * Does the group have any elements that are of the implClasses
         * @param implClasses the implementation classes
         * @return true if the group contains elements of any of the implClasses, false otherwise
         */
        default boolean has(Class...implClasses ){
            return !list( implClasses).isEmpty();
        }

        /**
         * Does the group have any elements that are of the implClass and match the matchFn
         * @param implClass
         * @param _matchFn
         * @param <_EI>
         * @return
         */
        default <_EI extends _EL> boolean has(Class<_EI>implClass, Predicate<_EI>_matchFn){
            return !list( implClass, _matchFn).isEmpty();
        }

        default boolean has(EL target){
            return !listAstElements( el-> el.equals(target)).isEmpty();
        }

        default _G add(EL... astElements) {
            for( EL el : astElements ) {
                this.listAstElements().add(el);
            }
            return (_G)this;
        }

        default _G add(_EL... elements) {
            for( _EL el : elements ) {
                this.listAstElements().add( (EL)el.ast());
            }
            return (_G)this;
        }

        /**
         * remove all elements from the set and return the modified empty entity
         */
        default _G clear(){
            this.listAstElements().clear();
            return (_G)this;
        }

        default _G remove(_EL... _els) {
            Arrays.stream( _els ).forEach(e -> this.listAstElements().remove( e.ast() ) );
            return (_G)this;
        }

        default _G remove(EL... els) {
            Arrays.stream(els ).forEach( e -> this.listAstElements().remove( e ) );
            return (_G)this;
        }

        default _G remove(Predicate<_EL> _matchFn) {
            this.list(_matchFn).stream().forEach( e-> remove(e) );
            return (_G)this;
        }

        default <_EI extends _EL> _G remove(Class<_EI>implClass, Predicate<_EI> _matchFn){
            this.list(implClass, _matchFn).stream().forEach( e-> remove(e) );
            return (_G)this;
        }

        /**
         * Removes (AND returns) all elements from the group that match the match the predicate
         *
         * @param _matchFn function to determine if an element is to be removed
         * @return a list of removed elements (in the order they were removed)
         * @see #removeIf(Predicate) to remove and return the modified _group instead of returning the removed elements
         */
        default List<_EL> removeIf(Predicate<_EL> _matchFn) {
            List<_EL> l = this.list(_matchFn);
            l.stream().forEach( e-> remove(e) );
            return l;
        }

        /**
         * Removes (AND returns) all elements from the group of the implType that match the match the predicate
         *
         * @param implClass the implementation type
         * @param _matchFn the function to determine if the element is to be removed
         * @param <_EI> the element implementation type
         * @return the removed elements
         */
        default <_EI extends _EL> List<_EI> removeIf(Class<_EI>implClass, Predicate<_EI> _matchFn){
            List<_EI> l = this.list(implClass, _matchFn);
            l.stream().forEach( e-> remove(e) );
            return l;
        }

        default <_EI extends _EL> _G toEach(Class<_EI>implClass, Predicate<_EI> _matchFn, Consumer<_EI> actionFn){
            list(implClass, _matchFn).forEach(actionFn);
            return (_G)this;
        }

        default _G toEach(Predicate<_EL> matchFn, Consumer<_EL> consumer){
            list(matchFn).forEach(consumer);
            return (_G)this;
        }

        default _G toEach(Consumer<_EL> consumer){
            list().forEach(consumer);
            return (_G)this;
        }

        default <_EI extends _EL> List<_EI> forEach(Class<_EI>implClass, Predicate<_EI> _matchFn, Consumer<_EI> actionFn){
            List<_EI> lei = list(implClass, _matchFn);
            lei.forEach(actionFn);
            return lei;
        }

        default List<_EL> forEach(Predicate<_EL> matchFn, Consumer<_EL> consumer){
            List<_EL> l = list(matchFn);
            l.forEach(consumer);
            return l;
        }

        default List<_EL> forEach(Consumer<_EL> consumer){
            List<_EL> le = list();
            le.forEach(consumer);
            return le;
        }

        /**
         * Count the number of elements in the list that are of these implementation classes
         * @param instanceClasses
         * @return
         */
        default int count( Class<? extends _EL>... instanceClasses ){
            return list(e-> Stream.of(instanceClasses).anyMatch(c-> c.isAssignableFrom(e.getClass()))).size();
        }

        /**
         * count the number of elements in the list that abide by the predicate
         * @param matchFn the predicate matching function
         * @return the count of elements in the list that match the predicate
         */
        default int count( Predicate<_EL> matchFn){
            return list(matchFn).size();
        }

        /**
         * Count the number of elements that are of the EI type and match the _instanceMatchFn
         * @param implType
         * @param _implMatchFn
         * @param <_EI>
         * @return
         */
        default <_EI extends _EL> int count(Class<_EI> implType, Predicate<_EI> _implMatchFn){
            return list(e-> {
                if( implType.isAssignableFrom(e.getClass()) ){
                    return _implMatchFn.test( (_EI)e);
                }
                return false;
            }).size();
        }

        /**
         * E
         * @param index
         * @return
         */
        default _EL getAt(int index){
            return this.list().get(index);
        }

        default int indexOf(_EL target){
            return list().indexOf(target);
        }

        default int indexOf(EL target){
            return listAstElements().indexOf(target);
        }

        /** remove the argument at this index */
        default _G removeAt(int index){
            this.listAstElements().remove(index);
            return (_G)this;
        }

        /**
         * Adds an element at the index and returns the modified list
         * @param index the index to add at (0-based)
         * @param element the element to add
         * @return the modified list
         */
        default _G addAt(int index, _EL... element){
            for(int i=0;i<element.length;i++) {
                this.listAstElements().add(index+i, (EL) element[i].ast());
            }
            return (_G)this;
        }

        /**
         * Adds an element at the index and returns the modified list
         * @param index the index to add at (0-based)
         * @param element the element to add
         * @return the modified list
         */
        default _G addAt(int index, EL... element){
            for(int i=0;i<element.length;i++) {
                this.listAstElements().add(index+i, element[i]);
            }
            return (_G)this;
        }

        /**
         *
         * @param index
         * @param element
         * @return
         */
        default _G setAt(int index, EL element){
            this.listAstElements().set(index, element);
            return (_G)this;
        }

        /**
         *
         * @param index
         * @param _element
         * @return
         */
        default _G setAt(int index, _EL _element){
            this.listAstElements().set(index, (EL)_element.ast());
            return (_G)this;
        }

        /**
         *
         * @param index
         * @param _element
         * @return
         */
        default boolean isAt( int index, _EL _element){
            if( index >= this.size()){
                return false;
            }
            return getAt(index).equals(_element);
        }

        /**
         * does the argument at this index match any of the classes provided?
         * @param index the argument index
         * @param classes the _expression classes for matching the argument
         * @return true if the argument matches any of these classes, false otherwise
         */
        default boolean isAt( int index, Class<? extends _EL>...classes) {
            if( index >= this.size()){
                return false;
            }
            return Arrays.stream(classes).anyMatch( c-> c.isAssignableFrom( getAt(index).getClass() ));
        }

        /**
         * Does
         * @param index the index of the element
         * @param implClass the element implementation class
         * @param _matchFn the predicate for matching against the specific element implementation type
         * @param <_EI> element implementation type
         * @return
         */
        default <_EI extends _EL> boolean isAt(int index, Class<_EI>implClass, Predicate<_EI> _matchFn){
            if( index >= this.size()){
                return false;
            }
            return isAt(index, a-> {
                if( implClass.isAssignableFrom(getAt(index).getClass())){
                    return _matchFn.test( (_EI)getAt(index));
                }
                return false;
            } );
        }

        /**
         *
         * @param index
         * @param matchFn
         * @return
         */
        default boolean isAt( int index, Predicate<_EL> matchFn) {
            if( index >= this.size()){
                return false;
            }
            return matchFn.test( getAt(index));
        }
    }

    /**
     * ORDERED/ ORDER MATTERS TO THE SEMANTICS GROUP
     * Sometimes we have groupings of entities where the ordering of the elements in the syntax matters to the
     * semantics of the program
     *
     * @see _args (the order of arguments matter)
     * @see _arrayInitExpr (the elements located in the array are ordered)
     * @see _blockStmt (the order of statements matter)
     * @see _newArrayExpr (the dimensions of the array are in an ordered list)
     * @see _params (parameters are ordered)
     */
    interface _orderedGroup<EL extends Node, _EL extends _node, _OG extends _orderedGroup> extends _group<EL, _EL, _OG>, _tree<_OG> {

        /**
         * Reimplementation of {@link #is(_node[])} since now ORDER MATTERS
         * @param _els
         * @return
         */
        default boolean is(_EL... _els){
            if( this.size() != _els.length ){
                return false;
            }
            for(int i=0;i<_els.length; i++){
                if( !Objects.equals(this.getAt(i), _els[i])){
                    return false;
                }
            }
            return true;
        }

        /**
         * Reimplementation of {@link #is(List)} since now ORDER MATTERS
         * @param _els
         * @return
         */
        default boolean is(List<_EL> _els){
            if( this.size() != _els.size() ){
                return false;
            }
            for(int i=0;i<_els.size(); i++){
                if( !Objects.equals(this.getAt(i), _els.get(i))){
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * a "view" tree node is a way to provide a user model that has no (1-to-1) physical counterpart
     * within the AST syntax. (Usually it represents a "logical" grouping of elements that are stored in the AST as
     * a {@link NodeList}, but (instead of directly accessing the NodeList implementation, we build this virtual "wrapper"
     * to make the API more intuitive.  this is helpful when we want to "unify" the interface to the syntax in a
     * logical way.
     * (similar to a <A HREF="https://www.essentialsql.com/what-is-a-relational-database-view/">Database view</A>)
     *
     * For instance {@link _body} unifies the API behind multiple AST entities that
     * <UL>
     * <LI>MAY contain a body ({@link com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt}
     *     <PRE>
     *         //method with (empty) body
     *         void m(){}
     *     </PRE>
     *     <PRE>
     *         //method with NO BODY (i.e. "NOT IMPLEMENTED")
     *         void m();
     *     </PRE>
     * <LI>Must contain a blockStmt body {@link com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt}
     *     <PRE>
     *         //constructor MUST have a body that is a {@link BlockStmt}, (it can be empty)
     *         C(){}
     *     </PRE>
     * <LI>May have a single statement or BlockStmt body {@link com.github.javaparser.ast.nodeTypes.NodeWithBody}
     *    <PRE>
     *        //if statement with a single ({@link ReturnStmt}) statement body
     *        if(true)
     *           return 1;
     *    </PRE>
     *    <PRE>
     *        //if statement with a {@link BlockStmt} body
     *        if(true){
     *           return 1;
     *        }
     *    </PRE>
     * </UL>
     * this provides a more convenient model for users of the tool to access query or update the syntax
     * in a consistent way.
     * <UL>
     *     <LI>(i.e. MethodDeclaration which implements</LI>
     * </UL>
     * @see _annos is annotations stored in a {@link NodeList}
     * @see _args expressions stored in a {@link NodeList}
     * @see _body statements (or the absense of statements) in {@link com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt},
     * {@link com.github.javaparser.ast.nodeTypes.NodeWithBody}, {@link com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt}
     * @see _cases {@link com.github.javaparser.ast.stmt.SwitchEntry}s stored that have the same statementBlock
     * @see _imports imports stored within a {@link NodeList}
     * @see _modifiers modifiers stored in a {@link NodeList}
     * @see _params parameters stored in a {@link NodeList}
     * @see _typeArgs type stored in a {@link NodeList}
     * @see _typeParams
     *
     * @param <_V> the view implementation
     */
    interface _view<_V extends _view> extends _tree<_V>{

    }
}
