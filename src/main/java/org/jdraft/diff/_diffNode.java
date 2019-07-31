package org.jdraft.diff;

import java.util.LinkedList;
import java.util.function.Consumer;

import org.jdraft.*;
import name.fraser.neil.plaintext.diff_match_patch;


/**
 * A single difference between the "left" and "right" code and a path to identify
 * the location / components that contain the diff
 *
 * @param <R>
 */
public interface _diffNode<R extends _java> {

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
    public R leftRoot();

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
    public R rightRoot();

    /**
     * Patches the root nodes to accept
     * synonymous with UNDO if LEFT = version 1 & right = version 2
     */
    public void patchLeftToRight();

    /**
     * 
     */
    public void patchRightToLeft();

    /**
     * @return the path that marks the component types (METHOD, FIELD,
     * PARAMETER, NEST, etc.) and identifiers ( "0", "m(String)") that are
     * traversed to reach the diff node
     */
    public _path path();

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
    
    default boolean on( Class<? extends _node> clazz){
        return path().has(clazz);
    }
    
    default boolean on( Class<? extends _node> clazz, String id){
        return path().has(clazz, id);
    }
        
    default boolean on( _java.Component component){
        return path().has(component);
    }
    
    default boolean on( _java.Component component, String id){
        /*
        _path p = path();
        for(int i=0;i<p.size(); i++){
            p.is(i, component, id);
        }
        */
        return path().has(component, id);
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
     * @param component the component
     * @param id the identifying features of this path
     * @return true if this is the last part of the path
     */
    default boolean at(_java.Component component, String id) {
        return path().isLeaf(component) && path().isLeafId(id);
    }
    
    /**
     * Is this diffnode located AT this class component
     * @param <C>
     * @param component
     * @param id
     * @return 
     */
    default <C extends _node> boolean at(Class<C> component, String id) {
        return path().isLeaf(component) && path().isLeafId(id);
    }
    
    
    default <C extends _node> boolean at(Class<C> component) {
        return path().isLeaf(component);
    }

    
    /**
     * Is the underlying leaf level component where the diff occurs this
     * specific component? (i.e. METHOD, PARAMETER, FIELD)
     *
     * @param component the expected leaf component
     * @return true if this node has a path ending with this component
     */
    default boolean at(_java.Component component) {
        return path().isLeaf(component);
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

    default boolean at(_path path) {
        return path().equals(path);
    }

    
    public interface _leftOnly<T> {

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
    
    public static interface _rightOnly<T> {

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
     * A change between the left and right
     *
     * @param <T>
     */
    public static interface _change<T> {

        T left();

        T right();

        void patchLeftToRight();

        void patchRightToLeft();
    }

    /**
     *
     * @author Eric
     */
    public static interface _edit{

        /** @return a list of Diff entities (ADD, REMOVE, EQUAL) from diff_match_patch */
        LinkedList<diff_match_patch.Diff> listDiffs();

        /** @return the path to the edit diff */
        _path path();

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
         *
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
         *
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
        _body._hasBody leftRoot();

        /**
         *
         * @return
         */
        _body._hasBody rightRoot();
    }

    
}