package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

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
 *     <LI>{@link _set}s containing semantically unordered monotonic entities within the Java syntax tree</LI>
 *     <LI>{@link _list}s containing semantically ordered monotonic entities within the Java syntax tree</LI>
 * </UL>
 * @param <_N> the entity type
 */
public interface _tree<_N> extends _java._domain {

    /**
     * Returns the features Object which defines the order of the features as well as the parser
     * for the entity
     * @return the features
     */
    _feature._features<_N> features();

    /**
     * Build and return an (independent) copy of this _node entity
     * @return an independent mutable copy
     */
    _N copy();

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
            return new _walk(Tree.PRE_ORDER, this, _node.class);
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
            return new _walk(Tree.POST_ORDER, this, _node.class);
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
            return new _walk(Tree.BREADTH_FIRST, this, _node.class);
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
            return new _walk<_node>(Tree.PARENTS, this, _node.class);
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
            return new _walk<_node>(Tree.DIRECT_CHILDREN, this, _node.class);
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
            return new _walkFeatures(Tree.PRE_ORDER, this, features);
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
            return new _walkFeatures(Tree.POST_ORDER, this, features);
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
            return new _walkFeatures(Tree.BREADTH_FIRST, this, features);
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
            return new _walkFeatures(Tree.PARENTS, this);
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
            return new _walkFeatures<_F>(Tree.DIRECT_CHILDREN, this, features);
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
            return new _walk(Tree.PRE_ORDER, this, targetClass);
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
            return new _walk(Tree.POST_ORDER, this, targetClass);
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
            return new _walk(Tree.BREADTH_FIRST, this, targetClass);
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
            return new _walk(Tree.PARENTS, this, targetClass);
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
            return new _walk(Tree.DIRECT_CHILDREN, this, targetClass);
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

                            } else if( s.isFixedText() ){ //its fixed text, dont match the text, match the impl
                                Object t = tfm.get(_f); //get the corresponding feature from the other one
                                if( !Objects.equals(o, t) ){ //check equality at the feature object level
                                    isSame.set(false);
                                }
                            } else { //its a mix of text and variables, so lets
                                Object t = tfm.get(_f); //get the corresponding feature from the other one
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
     * A grouping of monotonic (same type) entities where the order doesnt matter
     * to the semantics of the set.  While they may may be stored in NodeList<N>
     * this represents the syntax, however in the semantic side, this:
     * <CODE>
     * void m() throws A, B
     * </CODE>
     *
     * ...is semantically equivalent to this:
     * <CODE>
     *  void m() throws B, A
     * </CODE>
     *
     * @see _annoExprs < AnnotationExpr, _annoRef >
     * @see _imports<  ImportDeclaration ,_import>
     * @see _modifiers <com.github.javaparser.ast.Modifier,_modifier>
     * @see _typeParams < TypeParameter, _typeParam >
     * @see _throws<  ReferenceType ,_typeRef>
     *
     * @param <EL>
     * @param <_EL>
     * @param <_S>
     */
    interface _set<EL extends Node, _EL extends _node, _S extends _set> extends _tree<_S> {

        default boolean isEmpty(){
            return size() == 0;
        }

        default int size(){
            return listAstElements().size();
        }

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

        default List<_EL> list(Predicate<_EL> matchFn){
            return list().stream().filter(matchFn).collect(Collectors.toList());
        }

        default List<EL> listAstElements(Predicate<EL> matchFn){
            return listAstElements().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean anyMatch(Predicate<_EL> _matchFn){
            return list().stream().anyMatch(_matchFn);
        }

        default boolean allMatch(Predicate<_EL> _matchFn){
            return list().stream().allMatch(_matchFn);
        }

        /**
         * is the String representation equal to the _node entity
         * (i.e. if we parse the string, does it create an AST entity that
         * is equal to the node?)
         *
         * @param stringRep the string representation of the node
         * (parsed as an AST and compared to this entity to see if equal)
         * @return true if the Parsed String represents the entity

        default boolean is(String... stringRep){
            try{
                _S _s = features().parse(stringRep);
                //TODO fixy fixy
                Stencil st = Stencil.of( _s.toString());
                if( st.isFixedText() ){
                    //System.out.println ("ITS FIXED TEXT" + _n +System.lineSeparator() +this );
                    return equals( _s );
                } else if( st.isMatchAny() ) {
                    return true;
                } else {
                    //System.out.println ("ITS STENCIL TEXT");
                    //TODO FIXY FIXY break down into components
                    return st.matches( this.toString());
                }
            } catch(Exception e){
                //System.out.println( "FAILED YO "+ e);
                return false;
            }
        }
        */

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
         *   the details on how the code will be formatted (for this element and all sub ELEMENTS)
         * @return A String representing the .java code

        default String toString(PrettyPrinterConfiguration codeFormat) {
            return ast ().toString(codeFormat);
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


        default _S set(List<_EL> els){
            listAstElements().clear();
            els.forEach( el -> listAstElements().add((EL)el.ast()));
            return (_S)this;
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

        default boolean has(EL target){
            return !listAstElements( el-> el.equals(target)).isEmpty();
        }

        default _S add(EL... astElements) {
            for( EL el : astElements ) {
                this.listAstElements().add(el);
            }
            return (_S)this;
        }

        default _S add(_EL... elements) {
            for( _EL el : elements ) {
                this.listAstElements().add( (EL)el.ast());
            }
            return (_S)this;
        }

        /**
         * remove all elements from the set and return the modified empty entity
         */
        default _S clear(){
            this.listAstElements().clear();
            return (_S)this;
        }

        default _S remove(_EL... _els) {
            Arrays.stream( _els ).forEach(e -> this.listAstElements().remove( e.ast() ) );
            return (_S)this;
        }

        default _S remove(EL... els) {
            Arrays.stream(els ).forEach( e -> this.listAstElements().remove( e ) );
            return (_S)this;
        }

        default _S remove(Predicate<_EL> _matchFn) {
            this.list(_matchFn).stream().forEach( e-> remove(e) );
            return (_S)this;
        }

        default _S forEach(Predicate<_EL> matchFn, Consumer<_EL> consumer){
            list(matchFn).forEach(consumer);
            return (_S)this;
        }

        default _S forEach(Consumer<_EL> consumer){
            list().forEach(consumer);
            return (_S)this;
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
         *
         * @param instanceType
         * @param _instanceMatchFn
         * @param <_IT>
         * @return
         */
        default <_IT extends _EL> int count(  Class<_IT> instanceType, Predicate<_IT> _instanceMatchFn){
            return list(e-> {
                if( instanceType.isAssignableFrom(e.getClass()) ){
                    return _instanceMatchFn.test( (_IT)e);
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
        default _S removeAt(int index){
            this.listAstElements().remove(index);
            return (_S)this;
        }

        /**
         * Adds an element at the index and returns the modified list
         * @param index the index to add at (0-based)
         * @param element the element to add
         * @return the modified list
         */
        default _S addAt( int index, _EL... element){
            for(int i=0;i<element.length;i++) {
                this.listAstElements().add(index+i, (EL) element[i].ast());
            }
            return (_S)this;
        }

        /**
         * Adds an element at the index and returns the modified list
         * @param index the index to add at (0-based)
         * @param element the element to add
         * @return the modified list
         */
        default _S addAt( int index, EL... element){
            for(int i=0;i<element.length;i++) {
                this.listAstElements().add(index+i, element[i]);
            }
            return (_S)this;
        }

        /**
         *
         * @param index
         * @param element
         * @return
         */
        default _S setAt( int index, EL element){
            this.listAstElements().set(index, element);
            return (_S)this;
        }

        /**
         *
         * @param index
         * @param _element
         * @return
         */
        default _S setAt( int index, _EL _element){
            this.listAstElements().set(index, (EL)_element.ast());
            return (_S)this;
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
        default boolean isAt( int index, Class<? extends _expr>...classes) {
            if( index >= this.size()){
                return false;
            }
            return Arrays.stream(classes).anyMatch( c-> c.isAssignableFrom( getAt(index).getClass() ));
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
     * Sometimes we have groupings of entities that do not
     * map to a specific Ast entity but a grouping of AST entities
     *
     * @see _params (parameters are ordered)
     * @see _newArrayExpr (the dimensions of the array are in an ordered list)
     * @see _arrayInitExpr (the elements located in the array are ordered)
     */
    interface _list<EL extends Node, _EL extends _node, _L extends _list> extends _set<EL, _EL, _L>, _tree<_L> {

    }
}
