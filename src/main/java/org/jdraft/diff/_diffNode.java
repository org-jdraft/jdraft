package org.jdraft.diff;

import java.util.LinkedList;
import java.util.function.Consumer;

import org.jdraft.*;
import name.fraser.neil.plaintext.diff_match_patch;


/**
 * A single difference between the "left" and "right" code and a path to identify
 * the location / components that contain the diff
 *
 * @param <_PN>
 */
public interface _diffNode<_PN extends _java._domain> {

    /**
     * the Entity that is the parent of the diff... for instance
     * <PRE>
     * LEFT            RIGHT
     * int f = 0;      int f = 1;
     * 
     * in the above: 
     * the "left root" is the field "int f = 0;"
     * the "right root" is the field "int f = 1;"
     * the "path" is (...)field[f].init //the init
     * the "left" value is (Expression(0))
     * the "right" value is (Expression(1))
     * * NOTE: we need access to the root, so that we might "patch"
     * the value (i.e. call keepRight() which will set the values:
     * leftRoot.setInit(Expr.of(1));
     * 
     * LEFT            RIGHT
     * int f = 1;      int f = 1;
     * 
     * </PRE>
     * @return the left root entity containing the diff
     */
    _PN leftParent();

    /**
     * the Entity that is the parent of the diff... for instance
     * <PRE>
     * LEFT            RIGHT
     * int f = 0;      int f = 1;
     * 
     * in the above diffNode:
     * the diff type is "change"
     * the "left root" is the field "int f = 0;"
     * the "right root" is the field "int f = 1;"
     * the "path" is (...)field[f].init 
     * the "left" value is (Expression(0))
     * the "right" value is (Expression(1))
     * 
     * NOTE: we need access to the root, so that we might "patch"
     * the value (i.e. call keepRight() which will set the values:
     * leftRoot.setInit(Expr.of(1));
     * 
     * LEFT            RIGHT
     * int f = 1;      int f = 1;
     * 
     * </PRE>
     * @return the right root entity containing the diff
     */
    _PN rightParent();

    /**
     * Patches the root nodes to accept
     * synonymous with UNDO if LEFT = version 1 & right = version 2
     */
    void patchLeftToRight();

    /**
     * 
     */
    void patchRightToLeft();

    /**
     * @return the path that marks the component types (METHOD, FIELD,
     * PARAMETER, NEST, etc.) and identifiers ( "0", "m(String)") that are
     * traversed to reach the diff node
     */
    _nodePath path();

    /**
     * if the type of change an Add (meaning an entity 
     * NOT PRESENT ON LEFT / PRESENT ON RIGHT
     * @return 
     */
    default boolean isRightOnly() {
        return this instanceof _rightOnly;
    }

    /**
     * a "remove" or it was removed from left to the right
     * PRESENT ON LEFT / NOT ON RIGHT
     * 
     * @return 
     */
    default boolean isLeftOnly() {
        return this instanceof _leftOnly;
    }

    /**
     * 
     * SAME ENTITY ON LEFT AND RIGHT WITH CHANGES
     * 
     * @return 
     */
    default boolean isChange() {
        return this instanceof _change;
    }

    /**
     * 
     * @return 
     */
    default boolean isEdit() {
        return this instanceof _edit;
    }

    default _rightOnly asRightOnly() {
        return (_rightOnly) this;
    }

    default _leftOnly asLeftOnly() {
        return (_leftOnly) this;
    }

    default _change asChange() {
        return (_change) this;
    }

    default _edit asEdit() {
        return (_edit) this;
    }

    default boolean on( String id ){
        return path().has(id);
    }
    
    default boolean on( Class<? extends _tree._node> clazz){
        return path().has(clazz);
    }
    
    default boolean on(Class<? extends _tree._node> clazz, String id){
        return path().has(clazz, id);
    }
        
    default boolean on( Feature feature){
        return path().has(feature);
    }
    
    default boolean on(Feature feature, String id){
        /*
        _path p = path();
        for(int i=0;i<p.size(); i++){
            p.is(i, component, id);
        }
        */
        return path().has(feature, id);
    }
    
    /**
     * is the last part of the path at this Component with this id: i.e.
     * examples
     * <PRE>
     * EXTENDS, ""              //the extends (no id)
     * IMPLEMENTS, ""           //implements (no id)
     * PARAMETER, "0"           //the first parameter
     * FIELD, "f"               //the field named f
     * METHOD, "m(String)"      //the method "m" with the String argument
     * CONSTRUCTOR, "c(int,int)"//the constructor named c with (2) int args
     * </PRE>
     *
     * @param feature the component
     * @param id the identifying features of this path
     * @return true if this is the last part of the path
     */
    default boolean at(Feature feature, String id) {
        return path().isLeaf(feature) && path().isLeafId(id);
    }
    
    /**
     * Is this diffnode located AT this class component
     * @param <_N>
     * @param component
     * @param id
     * @return 
     */
    default <_N extends _tree._node> boolean at(Class<_N> component, String id) {
        return path().isLeaf(component) && path().isLeafId(id);
    }

    default <_N extends _tree._node> boolean at(Class<_N> component) {
        return path().isLeaf(component);
    }

    /**
     * Is the underlying leaf level component where the diff occurs this
     * specific component? (i.e. METHOD, PARAMETER, FIELD)
     *
     * @param feature the expected leaf component
     * @return true if this node has a path ending with this component
     */
    default boolean at(Feature feature) {
        return path().isLeaf(feature);
    }

    /**
     * Is the underlying leaf level id where the diff occurs have an id that
     * matches this id?
     *
     * @param id the expected leaf id
     * @return true if this node has a path ending with this component
     */
    default boolean at(String id) {
        return path().isLeafId(id);
    }

    default boolean at(_nodePath path) {
        return path().equals(path);
    }

    /**
     * Entity occurs on the left entity not on the right (usually a _member, like a _field, _method, etc.)
     * @param <T>
     */
    interface _leftOnly<T> {

        /** the full path where this leftOnly occurs */
        _nodePath path();

        /**
         * The entity in the LEFT 
         * @return the entity that only exists on the LEFT entity
         */
        T left();

        /**
         * (FIX the LEFT component to be like the RIGHT)
         * update the value on the LEFT be equal the value on the RIGHT 
         * <PRE>
         * //here the left field has @Deprecated, the right DOES NOT
         * //the _diffNode will be a leftOnly
         * _diff.of(_field.of("int i").annotate(Deprecated.class), _field.of("int i"));
         * </PRE>
         * in the above we'd REMOVE the @Deprecated annotation on the LEFT
         * to make it match the RIGHT
         */
        void patchRightToLeft();

        /**
         * (FIX the RIGHT component to be like the LEFT)
         * update the value on the RIGHT be equal the value on the LEFT 
         * (we have a component on the LEFT that is NOT on the RIGHT:
         * <PRE>
         * //here the left field has @Deprecated, the right DOES NOT
         * //the _diffNode will be a leftOnly
         * _diff.of(_field.of("int i").annotate(Deprecated.class), _field.of("int i"));
         * </PRE>
         * in the above we'd ADD the @Deprecated annotation to the RIGHT
         * to make it match the RIGHT
         */
        void patchLeftToRight();
    }

    /**
     *
     * @param <T>
     */
    interface _rightOnly<T> {

        /** the full path where this rightOnly occurs */
        _nodePath path();

        /**
         * The entity added in the RIGHT, not in LEFT
         * @return entity that exists on the right only
         */
        T right();

        /**
         * keep the value of the right side ( the added value is added to left)
         */
        void patchRightToLeft();

        /**
         * update the value to be null (on LEFT and RIGHT)
         */
        void patchLeftToRight();
    }

    /**
     * A change between the left and right (distinguished from a left only or right only
     * when we are talking about a "property" rather than a member...
     * i.e. _modifiers,
     *
     * @param <T>
     */
    interface _change<T> {

        /** the full path where this change occurs */
        _nodePath path();

        T left();

        T right();

        void patchLeftToRight();

        void patchRightToLeft();
    }

    /**
     *
     * @author Eric
     */
    interface _edit{

        /** @return a list of Diff entities (ADD, REMOVE, EQUAL) from diff_match_patch */
        LinkedList<diff_match_patch.Diff> listDiffs();

        /** @return the path to the edit diff */
        _nodePath path();

        /** */
        _edit forEach(Consumer<diff_match_patch.Diff> diffActionFn);

        /**
         * For all instances where the text on the right needs to be added to
         * the text on the left
         * @param addActionFn
         * @return
         */
        _edit forAdds(Consumer<diff_match_patch.Diff> addActionFn);

        /**
         * For all instances where the text on the left is to be removed to
         * create the text on the right
         * @param removeActionFn
         * @return
         */
        _edit forRemoves(Consumer<diff_match_patch.Diff> removeActionFn);

        /**
         * For all instances where the text is the same between left and right
         * @param retainActionFn
         * @return
         */
        _edit forRetains(Consumer<diff_match_patch.Diff> retainActionFn);

        /**
         * Iterate through all the diffs and push the LEFT text
         * to the LEFT AND RIGHT entities
         *
         * i.e.<PRE>
         * if we have
         *  LEFT          RIGHT
         *  AAAA11111     AAAA222222
         *    ---------->>
         * we set the results to be
         *  LEFT          RIGHT
         *  AAAA11111     AAAA11111
         * </PRE>
         */
        void patchLeftToRight();

        /**
         * Iterate through all the diffs and push the RIGHT text
         * to the LEFT AND RIGHT entities
         *
         * i.e.<PRE>
         * if we have
         *  LEFT          RIGHT
         *  AAAA11111     AAAA222222
         *      <<----------
         * we set the results to be:
         *  LEFT          RIGHT
         *  AAAA222222    AAAA222222
         * </PRE>
         */
        void patchRightToLeft();

        /**
         * Return the element with the body that is being compared (left side)
         * @return
         */
        _body._withBody leftParent();

        /**
         *
         * @return
         */
        _body._withBody rightParent();
    }

}
