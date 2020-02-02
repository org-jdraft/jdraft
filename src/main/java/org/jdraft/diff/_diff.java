package org.jdraft.diff;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._anno._hasAnnos;
import org.jdraft._body._hasBody;
import org.jdraft._constructor._hasConstructors;
import org.jdraft._field._hasFields;
import org.jdraft._java.Component;
import org.jdraft._javadoc._hasJavadoc;
import org.jdraft._method._hasMethods;
import org.jdraft._modifiers._hasModifiers;
import org.jdraft._receiverParameter._hasReceiverParameter;
import org.jdraft._throws._hasThrows;
import org.jdraft._typeParameter._hasTypeParameters;
import org.jdraft._initBlock._hasInitBlocks;

import org.jdraft.diff._diffNode._edit;

/**
 * <P>
 * Provides a mechanism to _walk and difference (at each "component") for a draft
 * entities.
 *
 * NOTE: this is MUCH different than testing AST Nodes for syntactical equality
 * because of
 * <UL>
 * <LI>Inferred Modifiers:
 * <PRE>
 * (i.e. a method with no modifiers on an interface is inferred to be public,
 * but not syntactically equal to an interface with an IMPLICIT marking of public)
 *
 *  (not syntactically equal, but equivalent in meaning)
 * interface i {int p;}  =/= interface i{ public int p;}
 * </PRE>
 * </LI>
 * <LI>Ordering: the order of many things (i.e. thrown exceptions,
 * typeParameters) matters from a readability perspective (we dont want to
 * change the syntactic order in which exceptions are thrown in the code). But
 * the underlying semantics are the same regardless if your throws clause has A,
 * then B or B, then A
 *
 * (not syntactically equal, but equivalent in meaning) void m() throws A, B;
 * =/= void m() throws B, A
 * </LI>
 * <LI>Fully Qualified Types vs "Bare" when comparing types if they have the
 * same name and are from an explicitly different packages, they should not be
 * equivalent. But the following ARE equivalent (specifiying the fully qualified
 * type on one hand should
 *
 * java.util.Map<java.net.URL,String> map; =/= Map<URL,String> map;
 *
 * public interface Semantic<A> {
 *
 * Are two entities "semantically" equivalent? Note: we need this level
 * difference between equality/equivalency precisely because we often times
 * cannot look at syntactical equality
 *
 * here are a few examples that are syntactically NOT EQUAL but semantically
 * EQUIVALENT
 *
 * (although not syntactically the same, semantically they MEAN the same thing)
 * interface i{ int x(); } interface i{ public int x(); }
 *
 * <H3>Semantic (verses Syntactic) Diff</H3>
 * <P>
 * This diff avoids a naive "syntactic diff"s and opts for semantic differences
 * where possible.</P>
 *
 * <P>
 * For instance, these two interfaces:
 * <PRE>
 * interface A{
 *     int a = 100;
 * }
 *
 * interface B{
 *     public static final int a=100;
 * }
 * </PRE>
 * <P>
 * ...ARE syntactically different, but semantically the same (the field a on the
 * interface above has "implied" modifiers "public static final")... So if we
 * call _diff on the two Classes above they would be the SAME assertTrue(
 * _diff.fieldsOf(A.class, B.class).isEmpty() );
 *
 *
 * @author Eric
 */
public interface _diff {


    /**
     * Build _type models and diff the two types
     *
     * @param clazz
     * @param clazz2
     * @return
     */
    static _diff of(Class clazz, Class clazz2) {
        return of((_type)_java.type(clazz), _java.type(clazz2));
    }

    static _diff of(_type _left, _type _right) {
        return _typeDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_class _left, _class _right) {
        return _classDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_enum _left, _enum _right) {
        return _enumDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_interface _left, _interface _right) {
        return _interfaceDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_annotation _left, _annotation _right) {
        return _annotationDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_field _left, _field _right) {
        return _fieldDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_method _left, _method _right) {
        return _methodDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_constructor _left, _constructor _right) {
        return _constructorDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_constant _left, _constant _right) {
        return _enumDiff.ENUM_CONSTANT_DIFF.diff(_left, _right);
    }

    static _diff of(_annotation._entry _left, _annotation._entry _right) {
        return _annotationDiff.ANNOTATION_ELEMENT_DIFF.diff(_left, _right);
    }

    static _diff extendsOf(_type left, _type right) {
        return _extendsDiff.INSTANCE.diff(left, right);
    }

    static _diff implementsOf(_type left, _type right) {
        return _implementsDiff.INSTANCE.diff(left, right);
    }

    static _diff importsOf(_type left, _type right) {
        return _importsDiff.INSTANCE.diff(left, right);
    }

    static _diff constantsOf(_enum left, _enum right) {
        return _enumDiff.ENUM_CONSTANTS_DIFF.diff(left.listConstants(), right.listConstants());
    }

    static _diff elementsOf(_annotation left, _annotation right) {
        return _annotationDiff.ANNOTATION_ELEMENTS_DIFF.diff(left.listElements(), right.listElements());
    }

    //so if a _hasConstructor has a no arg construstor with no body, it should be 
    // NOT a diff (I should post-process remove a diff
    static _diff constructorsOf(_hasConstructors left, _hasConstructors right) {
        return _constructorsDiff.INSTANCE.diff(left, right);
    }

    static _diff throwsOf(_hasThrows left, _hasThrows right) {
        return _throwsDiff.INSTANCE.diff(left, right);
    }

    static _diff fieldsOf(_hasFields left, _hasFields right) {
        return _fieldsDiff.INSTANCE.diff(left, right);
    }

    static _diff methodsOf(_hasMethods left, _hasMethods right) {
        return _methodsDiff.INSTANCE.diff(left, right);
    }

    static _diff annosOf(_hasAnnos left, _hasAnnos right) {
        return _annosDiff.INSTANCE.diff(left, right);
    }

    static _diff javadocOf(_hasJavadoc left, _hasJavadoc right) {
        return _javadocDiff.INSTANCE.diff(left, right);
    }

    static _diff bodyOf(_hasBody left, _hasBody right) {
        return _bodyDiff.INSTANCE.diff(left, right);
    }

    static _diff modifiersOf(_hasModifiers left, _hasModifiers right) {
        return _modifiersDiff.INSTANCE.diff(left, right);
    }

    static _diff nestsOf(_type left, _type right) {
        return _nestsDiff.INSTANCE.diff(left, right);
    }

    static _diff packageNameOf(_type left, _type right) {
        return _packageNameDiff.INSTANCE.diff(left, right);
    }

    static _diff typeParametersOf(_hasTypeParameters left, _hasTypeParameters right) {
        return _typeParametersDiff.INSTANCE.diff(left, right);
    }

    static _diff staticBlocksOf(_hasInitBlocks left, _hasInitBlocks right) {
        return _initBlocksDiff.INSTANCE.diff(left, right);
    }

    static _diff receiverParameterOf(_hasReceiverParameter left, _hasReceiverParameter right) {
        return _receiverParameterDiff.INSTANCE.diff(left, right);
    }

    /**
     * Signifies that the collection of Diffs is mutable (we can add diffsNodes
     * to the List)
     */
    interface _build extends _diff {
        _build addDiff(_diffNode d);
    }

    /**
     * @return a list of all diff nodes
     */
    List<_diffNode> list();

    /** gets the left root node of the diff (could be null for localized diffs) */
    _java._node leftRoot();

    /** gets the right root node of the diff (could be null for localized diffs) */
    _java._node rightRoot();

    /**
     * @return number of diffs
     */
    default int size() {
        return list().size();
    }

    /**
     *
     * @return true if there are no known diffs between the (2) entities
     */
    default boolean isEmpty() {
        return list().isEmpty();
    }

    /**
     * List all filePaths that have diffs
     *
     * @return
     */
    default List<_nodePath> paths() {
        return list().stream().map(d -> d.path()).collect(Collectors.toList());
    }

    /**
     * Return the _diffNode at the path, (or null if no diff exists at this
     * path)
     *
     * @param _p the underlying _path
     * @return the _diffNode at this path, or null if not found
     */
    default _diffNode at(_nodePath _p) {
        return first(d -> d.path().equals(_p));
    }
    
    default _diffNode on(Class<? extends _java._node> clazz){
        return first( d-> d.on(clazz));
    }
    
    default _diffNode on(Class<? extends _java._node> clazz, String id){
        return first( d-> d.on(clazz, id));
    }
    
    default _diffNode on(Component component){
        return first( d-> d.on(component));
    }
    
    default _diffNode on(Component component, String id){
        return first( d-> d.on(component, id));
    }
    
    default _diffNode leftOnlyAt(_nodePath _p){
        return first(d -> d.isLeftOnly() && d.path().equals(_p));
    }
    
    default <_N extends _java._node> _diffNode leftOnlyAt(Class<_N> nodeClass){
        return first(d -> d.isLeftOnly() && d.at(Component.of(nodeClass)));
    }
    
    default <_N extends _java._node> _diffNode leftOnlyAt(Class<_N> nodeClass, String id){
        return first(d -> d.isLeftOnly() && d.at(Component.of(nodeClass), id));
    }
    
    default _diffNode leftOnlyAt(String id){
        return first(d -> d.isLeftOnly() && d.at(id));
    }
    
    default _diffNode leftOnlyAt(Component component){
        return first(d -> d.isLeftOnly() && d.at(component));
    }
    
    default _diffNode leftOnlyAt(Component component, String id){
        return first(d -> d.isLeftOnly() && d.at(component, id));
    }
    
    default <_N extends _java._node> _diffNode leftOnlyOn(Class<_N> nodeClass){
        return first( d-> d.isLeftOnly() && d.on( Component.of(nodeClass) ) );
    }
    
    default _diffNode leftOnlyOn(String id){
        return first( d-> d.isLeftOnly() && d.on( id ) );
    }
    
    default <_N extends _java._node>_diffNode leftOnlyOn(Class<_N> nodeClass, String id){
        return first( d-> d.isLeftOnly() && d.on( Component.of(nodeClass), id ) );
    }
    
    default _diffNode leftOnlyOn(Component component){
        return first( d-> d.isLeftOnly() && d.on( component ) );
    }
    
    default _diffNode leftOnlyOn(Component component, String id){
        return first( d-> d.isLeftOnly() && d.on( component, id) );
    }
    
    default _diffNode rightOnlyAt(_nodePath _p){
        return first(d -> d.isRightOnly() && d.path().equals(_p));
    }
    
    default <_N extends _java._node> _diffNode rightOnlyAt(Class<_N> nodeClass){
        return first(d -> d.isRightOnly() && d.at(Component.of(nodeClass)));
    }
    
    default <_N extends _java._node> _diffNode rightOnlyAt(Class<_N> nodeClass, String id){
        return first(d -> d.isRightOnly() && d.at(Component.of(nodeClass), id));
    }
    
    default _diffNode rightOnlyAt(String id){
        return first(d -> d.isRightOnly() && d.at(id));
    }
    
    default _diffNode rightOnlyAt(Component component){
        return first(d -> d.isRightOnly() && d.at(component));
    }
    
    default _diffNode rightOnlyAt(Component component, String id){
        return first(d -> d.isRightOnly() && d.at(component, id));
    }
    
    default <_N extends _java._node> _diffNode rightOnlyOn(Class<_N> nodeClass){
        return first( d-> d.isRightOnly() && d.on( Component.of(nodeClass) ) );
    }

    default _diffNode rightOnlyOn(String id){
        return first( d-> d.isRightOnly() && d.on( id ) );
    }
    
    default <_N extends _java._node>_diffNode rightOnlyOn(Class<_N> nodeClass, String id){
        return first( d-> d.isRightOnly() && d.on( Component.of(nodeClass), id ) );
    }
    
    default _diffNode rightOnlyOn(Component component){
        return first( d-> d.isRightOnly() && d.on( component ) );
    }
    
    default _diffNode rightOnlyOn(Component component, String id){
        return first( d-> d.isRightOnly() && d.on( component, id) );
    }
    
    /** change */
    
    /**
     * return the first change at this exact path (or null if none found)
     * @param _p
     * @return 
     */
    default _diffNode changeAt(_nodePath _p){
        return first(d -> d.isChange() && d.path().equals(_p));
    }
    
    default <_N extends _java._node> _diffNode changeAt(Class<_N> nodeClass){
        return first(d -> d.isChange() && d.at(Component.of(nodeClass)));
    }

    default  _diffNode changeAt(String id){
        return first(d -> d.isChange() && d.at(id));
    }
    
    default <_N extends _java._node> _diffNode changeAt(Class<_N> nodeClass, String id){
        return first(d -> d.isChange() && d.at(Component.of(nodeClass), id));
    }
    
    default _diffNode changeAt(Component component){
        return first(d -> d.isChange() && d.at(component));
    }
    
    default _diffNode changeAt(Component component, String id){
        return first(d -> d.isChange() && d.at(component, id));
    }
    
    default <_N extends _java._node> _diffNode changeOn(Class<_N> nodeClass){
        return first( d-> d.isChange() && d.on( Component.of(nodeClass) ) );
    }
    
    default _diffNode changeOn(String id){
        return first( d-> d.isChange() && d.on( id ) );
    }
    
    default <_N extends _java._node>_diffNode changeOn(Class<_N> nodeClass, String id){
        return first( d-> d.isChange() && d.on( Component.of(nodeClass), id ) );
    }
    
    default _diffNode changeOn(Component component){
        return first( d-> d.isChange() && d.on( component ) );
    }
    
    default _diffNode changeOn(Component component, String id){
        return first( d-> d.isChange() && d.on( component, id) );
    }
    
    /** edit */
        /** change */
    
    /**
     * return the first edit that exists at this exact path (or null if none found)
     * @param _p
     * @return 
     */
    default _diffNode editAt(_nodePath _p){
        return first(d -> d.isEdit() && d.path().equals(_p));
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode editAt(Class<_N> nodeClass){
        return first(d -> d.isEdit() && d.at(Component.of(nodeClass)));
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode editAt(String id){
        return first(d -> d.isEdit() && d.at(id));
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode editAt(Class<_N> nodeClass, String id){
        return first(d -> d.isEdit() && d.at(Component.of(nodeClass), id));
    }

    /**
     *
     * @param component
     * @return
     */
    default _diffNode editAt(Component component){
        return first(d -> d.isEdit() && d.at(component));
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default _diffNode editAt(Component component, String id){
        return first(d -> d.isEdit() && d.at(component, id));
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode editOn(Class<_N> nodeClass){
        return first( d-> d.isEdit() && d.on( Component.of(nodeClass) ) );
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node>_diffNode editOn(Class<_N> nodeClass, String id){
        return first( d-> d.isEdit() && d.on( Component.of(nodeClass), id ) );
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode editOn(String id){
        return first( d-> d.isEdit() && d.on( id ) );
    }

    /**
     *
     * @param component
     * @return
     */
    default _diffNode editOn(Component component){
        return first( d-> d.isEdit() && d.on( component ) );
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default _edit editOn(Component component, String id){
        return (_edit) first( d-> d.isEdit() && d.on( component, id) );
    }
    
    /**
     * returns the DiffNode that can be found at this (Full) path
     *
     * @param pathAsTokens the tokens that make up the path
     * @return the _diffNode found at this path or null if not found
     */
    default _diffNode at(Object... pathAsTokens) {
        return at(_nodePath.of(pathAsTokens));
    }

    /**
     * IS there a Diff Node at the specified Component type?
     *
     * @param component the component type
     * @return true if there is a diff node at this type
     */
    default boolean isAt(_java.Component component) {
        return first(d -> d.at(component)) != null;
    }

    /**
     *
     * @param id
     * @return
     */
    default boolean isAt(String id){
        return first(d -> d.at(id)) != null;
    }
    
    /**
     * 
     * @param <_N>
     * @param c
     * @return 
     */
    default <_N extends _java._node> boolean isAt(Class<_N> c ){
        return isAt( Component.of(c) );
    }    
    
    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param component the component type
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isAt(_java.Component component, String id) {
        return first(d -> d.at(component) && d.at(id)) != null;
    }

    /**
     * Is there a Diff Node at (i.e.the LAST part of the path) the specified
     * Component type
     *
     * @param <_N> the _java node class
     * @param node the node class (i.e. _method.class, _field.class)
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default <_N extends _java._node> boolean isAt(Class<_N>node, String id) {
        return isAt(Component.of(node),id);
    }
    
    /**
     * IS there a Diff Node at the specified Component type?
     *
     * @param component the component type
     * @return true if there is a diff node at this type
     */
    default boolean isOn(_java.Component component) {
        return first(d -> d.on(component)) != null;
    }

    /**
     * 
     * @param <_N>
     * @param c
     * @return 
     */
    default <_N extends _java._node> boolean isOn(Class<_N> c ){
        return isOn( Component.of(c) );
    }    

    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isOn(String id) {
        return first(d -> d.on(id)) != null;
    }
    
    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param component the component type
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isOn(_java.Component component, String id) {
        return first(d -> d.on(component,id)) != null;
    }

    /**
     * Is there a Diff Node at (i.e.the LAST part of the path) the specified
     * Component type
     *
     * @param <_N> the _java node class
     * @param node the node class (i.e. _method.class, _field.class)
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default <_N extends _java._node> boolean isOn(Class<_N>node, String id) {
        return isOn(Component.of(node),id);
    }
    
    /**
     * Is there any Object that is either added, removed, or changed that is
     * equal to this object
     *
     * @param obj
     * @return
     */
    default boolean isDiffOf(Object obj) {
        return first(d -> {
            if (d.isRightOnly()) {
                return d.asRightOnly().right().equals(obj);
            }
            if (d.isLeftOnly()) {
                return d.asLeftOnly().left().equals(obj);
            }
            if (d.isChange()) {
                return d.asChange().left().equals(obj)
                        || d.asChange().right().equals(obj);
            }
            return false;
        }) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasRightOnly() {
        return first(d -> d.isRightOnly()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasChange() {
        return first(d -> d.isChange()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasLeftOnly() {
        return first(d -> d.isLeftOnly()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasEdit() {
        return first(d -> d.isEdit()) != null;
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasRightOnlyAt(_java.Component component) {
        return first(d -> d.at(component) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasRightOnlyAt(Class<_N> memClass) {
        return hasRightOnlyAt( Component.of(memClass) ); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasLeftOnlyAt(_java.Component component) {
        return first(d -> d.at(component) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasLeftOnlyAt(Class<_N> memClass) {
        return hasLeftOnlyAt( Component.of(memClass) ); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasChangeAt(_java.Component component) {
        return first(d -> d.at(component) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasChangeAt(Class<_N> memClass) {
        return hasChangeAt( Component.of(memClass) ); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasEditAt(_java.Component component) {
        return first(d -> d.at(component) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasEditAt(Class<_N> memClass) {
        return hasEditAt( Component.of(memClass) ); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasRightOnlyAt(_java.Component component, String id) {
        return first(d -> d.at(component, id) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasRightOnlyAt(Class<_N> memClass, String id) {
        return hasRightOnlyAt( Component.of(memClass) ,id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasLeftOnlyAt(_java.Component component, String id) {
        return first(d -> d.at(component, id) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasLeftOnlyAt(Class<_N> memClass, String id) {
        return hasLeftOnlyAt( Component.of(memClass) ,id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasChangeAt(_java.Component component, String id) {
        return first(d -> d.at(component, id) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasChangeAt(Class<_N> memClass, String id) {
        return hasChangeAt( Component.of(memClass) ,id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasEditAt(_java.Component component, String id) {
        return first(d -> d.at(component, id) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasEditAt(Class<_N> memClass, String id) {
        return hasRightOnlyAt( Component.of(memClass) ,id); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasRightOnlyOn(_java.Component component) {
        return first(d -> d.on(component) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasRightOnlyOn(Class<_N> memClass) {
        return hasRightOnlyOn( Component.of(memClass)); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasLeftOnlyOn(_java.Component component) {
        return first(d -> d.on(component) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasLeftOnlyOn(Class<_N> memClass) {
        return hasLeftOnlyOn( Component.of(memClass)); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasChangeOn(_java.Component component) {
        return first(d -> d.on(component) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasChangeOn(Class<_N> memClass) {
        return hasChangeOn( Component.of(memClass)); 
    }

    /**
     *
     * @param component
     * @return
     */
    default boolean hasEditOn(_java.Component component) {
        return first(d -> d.on(component) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasEditOn(Class<_N> memClass) {
        return hasEditOn( Component.of(memClass)); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasRightOnlyOn(_java.Component component, String id) {
        return first(d -> d.on(component, id) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasRightOnlyOn(Class<_N> memClass, String id) {
        return hasRightOnlyOn( Component.of(memClass), id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasLeftOnlyOn(_java.Component component, String id) {
        return first(d -> d.on(component, id) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasLeftOnlyOn(Class<_N> memClass, String id) {
        return hasLeftOnlyOn( Component.of(memClass), id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasChangeOn(_java.Component component, String id) {
        return first(d -> d.on(component, id) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasChangeOn(Class<_N> memClass, String id) {
        return hasChangeOn( Component.of(memClass), id); 
    }

    /**
     *
     * @param component
     * @param id
     * @return
     */
    default boolean hasEditOn(_java.Component component, String id) {
        return first(d -> d.on(component, id) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> boolean hasEditOn(Class<_N> memClass, String id) {
        return hasEditOn( Component.of(memClass), id); 
    }

    /**
     * For each of the matching DiffNodes in the Tree, perform some action
     *
     * @param _nodeMatchFn function for matching specific _diffNodes
     * @param _nodeActionFn consumer action function on selected _diffNodes
     * @return the potentially modified) _diffTree
     */
    default _diff forEach(Predicate<_diffNode> _nodeMatchFn, Consumer<_diffNode> _nodeActionFn) {
        list(_nodeMatchFn).forEach(_nodeActionFn);
        return this;
    }

    /**
     * For each diff _node use run this _nodeActionFn
     *
     * @param _nodeActionFn action to take on each node
     * @return the potentially modified _diff
     */
    default _diff forEach(Consumer<_diffNode> _nodeActionFn) {
        list().forEach(_nodeActionFn);
        return this;
    }

    /**
     * For all diffs that are ADD (meaning they are ABSENT in LEFT and PRESENT
     * in RIGHT) check if they match the _addNodeMatchFn and, if so, execute the
     * _addNodeActionFn
     *
     * @param _rightOnlyMatchFn matching function for Add nodes
     * @param _rightOnlyActionFn the action for adds that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forRightOnlys(Predicate<_diffNode._rightOnly> _rightOnlyMatchFn, Consumer<_diffNode._rightOnly> _rightOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._rightOnly && _rightOnlyMatchFn.test((_diffNode._rightOnly) d));
        List<_diffNode._rightOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._rightOnly) n));
        ans.forEach(_rightOnlyActionFn);
        return this;
    }

    /**
     * For all diff _nodes that are ADD (meaning they are ABSENT in LEFT and
     * PRESENT in RIGHT) execute the _addNodeActionFn
     *
     * @param _rightOnlyActionFn the action for adds that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forRightOnlys(Consumer<_diffNode._rightOnly> _rightOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._rightOnly);
        List<_diffNode._rightOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._rightOnly) n));
        ans.forEach(_rightOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are REMOVE (meaning they are PRESENT in LEFT and
     * ABSENT in RIGHT) check if they match the _removeNodeMatchFn and, if so,
     * execute the _removeNodeActionFn
     *
     * @param leftOnlyMatchFn matching function for Remove nodes
     * @param leftOnlyActionFn the action for removes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forLeftOnlys(Predicate<_diffNode._leftOnly> leftOnlyMatchFn, Consumer<_diffNode._leftOnly> leftOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._leftOnly && leftOnlyMatchFn.test((_diffNode._leftOnly) d));
        List<_diffNode._leftOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._leftOnly) n));
        ans.forEach(leftOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are REMOVE (meaning they are PRESENT in LEFT and
     * ABSENT in RIGHT) execute the _removeNodeActionFn
     *
     * @param leftOnlyActionFn the action for removes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forLeftOnlys(Consumer<_diffNode._leftOnly> leftOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._leftOnly);
        List<_diffNode._leftOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._leftOnly) n));
        ans.forEach(leftOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are CHANGE (meaning they are PRESENT in LEFT & RIGHT
     * but different) check if they match the _changeNodeMatchFn and, if so,
     * execute the _changeNodeActionFn
     *
     * @param changeMatchFn matching function for change nodes
     * @param changeActionFn the action for changes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forChanges(Predicate<_diffNode._change> changeMatchFn, Consumer<_diffNode._change> changeActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._change && changeMatchFn.test((_diffNode._change) d));
        List<_diffNode._change> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._change) n));
        ans.forEach(changeActionFn);
        return this;
    }

    /**
     * For all diffs that are CHANGE (meaning they are PRESENT in LEFT & RIGHT
     * but different) check if they match the _changeNodeMatchFn and, if so,
     * execute the _changeNodeActionFn
     *
     * @param changeActionFn the action for changes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forChanges(Consumer<_diffNode._change> changeActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._change);
        List<_diffNode._change> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._change) n));
        ans.forEach(changeActionFn);
        return this;
    }

    /**
     * For all diffs that are EDITs (meaning they are PRESENT in LEFT & RIGHT
     * but different on a textual level... this means the underlying code is put
     * through a diff) execute the _editNodeActionFn
     *
     * @param editActionFn the action for edits that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forEdits(Consumer<_diffNode._edit> editActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._edit);
        List<_diffNode._edit> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._edit) n));
        ans.forEach(editActionFn);
        return this;
    }

    /**
     * For all diffs that are EDITs (meaning they are PRESENT in LEFT & RIGHT
     * but different on a textual level... this means the underlying code is put
     * through a diff) check if they match the _editNodeMatchFn and, if so,
     * execute the _editNodeActionFn
     *
     * @param editMatchFn matching function for edit nodes
     * @param editActionFn the action for edits that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forEdits(Predicate<_diffNode._edit> editMatchFn, Consumer<_diffNode._edit> editActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._edit && editMatchFn.test((_diffNode._edit) d));
        List<_diffNode._edit> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._edit) n));
        ans.forEach(editActionFn);
        return this;
    }

    /**
     * List all add nodes that comply with the _addNodeActionFn
     *
     * @param rightOnlyMatchFn match function for selecting diff nodes
     * @return a list of _addNodes
     */
    default List<_diffNode._rightOnly> listRightOnlys(Predicate<_diffNode._rightOnly> rightOnlyMatchFn) {
        List<_diffNode._rightOnly> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._rightOnly && rightOnlyMatchFn.test((_diffNode._rightOnly) d))
                .forEach(e -> ens.add((_diffNode._rightOnly) e));
        return ens;
    }

    /**
     * Lists the diffs that are add Nodes (not found in the left, but found in
     * the right)
     *
     * @see _diffNode._rightOnly
     * @return a list of _addNode that are FOUND on the RIGHT and NOT FOUND on
     * the LEFT
     */
    default List<_diffNode._rightOnly> listRightOnlys() {
        List<_diffNode._rightOnly> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._rightOnly).forEach(n -> tds.add((_diffNode._rightOnly) n));
        return tds;
    }

    /**
     * List all remove nodes that comply with the _removeNodeActionFn
     *
     * @param leftOnlyMatchFn match function for selecting _removeNode s
     * @return a list of _removeNodes that match the _removeNodeMatchFn
     */
    default List<_diffNode._leftOnly> listLeftOnlys(Predicate<_diffNode._leftOnly> leftOnlyMatchFn) {
        List<_diffNode._leftOnly> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._leftOnly && leftOnlyMatchFn.test((_diffNode._leftOnly) d))
                .forEach(e -> ens.add((_diffNode._leftOnly) e));
        return ens;
    }

    /**
     * Lists the diffs that are _removeNodes (Found on the left, but NOT FOUND
     * on the right)
     *
     * @return a list of _removeNode that have removes (Found on left, not on
     * right)
     */
    default List<_diffNode._leftOnly> listLeftOnlys() {
        List<_diffNode._leftOnly> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._leftOnly).forEach(n -> tds.add((_diffNode._leftOnly) n));
        return tds;
    }

    /**
     * List all change nodes that comply with the _changeNodeActionFn
     *
     * @param _changeNodeMatchFn match function for selecting _changeNode s
     * @return a list of _changeNodes that match the _changeNodeMatchFn
     */
    default List<_diffNode._change> listChanges(Predicate<_diffNode._change> _changeNodeMatchFn) {
        List<_diffNode._change> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._change && _changeNodeMatchFn.test((_diffNode._change) d))
                .forEach(e -> ens.add((_diffNode._change) e));
        return ens;
    }

    /**
     * Lists the diffs that are _changeNodes Objects matched (API-wise) on both
     * LEFT and RIGHT but changed (i.e. Modifiers, Annotations, etc.)
     *
     * @return a list of _changeNode that have changes (Found on left & right
     * but different)
     */
    default List<_diffNode._change> listChanges() {
        List<_diffNode._change> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._change).forEach(n -> tds.add((_diffNode._change) n));
        return tds;
    }

    /**
     * List all edit nodes that comply with the _editNodeActionFn
     *
     * @param _editNodeMatchFn match function for selecting diff nodes
     * @return a list of _editNodes
     */
    default List<_diffNode._edit> listEdits(Predicate<_diffNode._edit> _editNodeMatchFn) {
        List<_diffNode._edit> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._edit && _editNodeMatchFn.test((_diffNode._edit) d))
                .forEach(e -> ens.add((_diffNode._edit) e));
        return ens;
    }

    /**
     * Lists the diffs that are edit "text diffs" (usually diffs for some body
     * of code)
     *
     * these involve the edit distance algorithm (more low level involved for
     * determining edits needed to be applied to the left text to create the
     * right text
     *
     * @return a list of _editNode that have edits (usually in code bodies)
     */
    default List<_diffNode._edit> listEdits() {
        List<_diffNode._edit> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._edit).forEach(n -> tds.add((_diffNode._edit) n));
        return tds;
    }

    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param id the
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.on(id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode firstOn(Class<_N> nodeClass) {
        return firstOn( Component.of(nodeClass));
    }
    
    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param componet the last component in the path
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(_java.Component componet) {
        Optional<_diffNode> first
            = list().stream().filter(d -> d.on(componet)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode firstOn(Class<_N> nodeClass, String id) {
        return firstOn( Component.of(nodeClass), id);
    }
    
    /**
     * Find the first diff that has this part (Component and id) anywahere on
     * the path
     *
     * @param componet the last component in the path
     * @param id
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(_java.Component componet, String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.on(componet, id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode firstAt(Class<_N> nodeClass) {
        return firstAt( Component.of(nodeClass));
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode firstAt(String id) {
        Optional<_diffNode> odn = 
            list().stream().filter(d -> d.at(id)).findFirst();        
        if( odn.isPresent()){
            return odn.get();
        }
        return null;
    }
    
    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param component the last component in the path
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstAt(_java.Component component) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.at(component)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _java._node> _diffNode firstAt(Class<_N> nodeClass, String id) {
        return firstAt( Component.of(nodeClass), id);
    }
    
    /**
     * Find the first diff that is at (the LAST part of the path) the component
     * type with this id
     *
     * @param component the last component in the path
     * @param id the last id in the path to the component
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstAt(_java.Component component, String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.at(component, id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     * find the first node that matches the predicate or return null
     *
     * @param _nodeMatchFn
     * @return the first node that matches the function or null if none found
     */
    default _diffNode first(Predicate<_diffNode> _nodeMatchFn) {
        Optional<_diffNode> first
                = list().stream().filter(_nodeMatchFn).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param id the leaf (last) id in the path (i.e. "0", "m(String)")
     * @return
     */
    default List<_diffNode> listAt(String id) {
        return list(d -> d.at(id));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param component the leaf component to look for (i.e. METHOD, FIELD)
     * @return
     */
    default List<_diffNode> listAt(_java.Component component) {
        return list(d -> d.at(component));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param component the leaf component to look for (i.e. METHOD, FIELD)
     * @param id
     * @return
     */
    default List<_diffNode> listAt(_java.Component component, String id) {
        return list(d -> d.at(component, id));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @return
     */
    default <_N extends _java._node> List<_diffNode> listAt(Class<_N> nodeClass) {
        return listAt( Component.of(nodeClass) );
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @param id
     * @return
     */
    default <_N extends _java._node> List<_diffNode> listAt(Class<_N> nodeClass, String id) {
        return listAt( Component.of(nodeClass), id);
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param id the leaf (last) id in the path (i.e. "0", "m(String)")
     * @return
     */
    default List<_diffNode> listOn(String id) {
        return list(d -> d.on(id));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param component the leaf component to look for (i.e. METHOD, FIELD)
     * @return
     */
    default List<_diffNode> listOn(_java.Component component) {
        return list(d -> d.on(component));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @return
     */
    default <_N extends _java._node> List<_diffNode> listOn(Class<_N> nodeClass) {
        return listOn( Component.of(nodeClass) );
    }

    /**
     * List all _diffNode s that have this component & id part ANYWHERE on the
     * path
     *
     * @param component the component to look for (i.e. METHOD, FIELD)
     * @param id the id to look for ("myMethod", "x")
     * @return a list of _diffNodes that occur on or
     */
    default List<_diffNode> listOn(_java.Component component, String id) {
        return list(d -> d.on(component, id));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @param id
     * @return
     */
    default <_N extends _java._node> List<_diffNode> listOn(Class<_N> nodeClass, String id) {
        return listOn(Component.of(nodeClass), id);
    }

    /**
     * Lists nodes matching a matchFn
     *
     * @param _nodeMatchFn matches diff nodes for retrieval
     * @return list of diff nodes that match the nodeMatchFn
     */
    default List<_diffNode> list(Predicate<_diffNode> _nodeMatchFn) {
        return list().stream().filter(_nodeMatchFn).collect(Collectors.toList());
    }

    /**
     * For all diffs, Accept all changes from the RIGHT object and 
     * apply/patching them in on the LEFT object
     *
     * @return
     */
    default _diff patchRightToLeft() {
        forEach(d -> d.patchRightToLeft());
        return this;
    }

    /**
     * For all diffs, Accept all changes from the LEFT object and 
     * apply/patching them in on the RIGHT object
     *
     * @return
     */
    default _diff patchLeftToRight() {
        forEach(d -> d.patchLeftToRight());
        return this;
    }

    /**
     * collection of diff _nodes that represent the deep differences between two
     * domain objects (two _class es, _methods, _fields)
     */
    class _diffList<_RN extends _java._node>
            implements _diff, _diff._build {

        /**
         * the underlying diffs collected
         */
        public final List<_diffNode> diffNodeList;

        /** the left root node of the diff (may be null for local diffs) */
        public final _RN leftRoot;

        /** the right root node of the diff (may be null for local diffs) */
        public final _RN rightRoot;

        public _diffList(_RN leftRoot, _RN rightRoot) {
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            diffNodeList = new ArrayList<>();
        }

        public _diffList(_RN leftRoot, _RN rightRoot, List<_diffNode> diffs) {
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.diffNodeList = diffs;
        }

        /**
         * The starting/root left node where the diff occurs (could be null for local diffs)
         * @return
         */
        public _RN leftRoot(){
            return this.leftRoot;
        }

        /**
         * The starting/root right node where the diff occurs (could be null for local diffs)
         * @return
         */
        public _RN rightRoot(){
            return this.rightRoot;
        }

        @Override
        public List<_diffNode> list() {
            return diffNodeList;
        }

        @Override
        public String toString() {
            if (list().isEmpty()) {
                return "-- (0) differences --";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(diffNodeList.size()).append(") diffs").append(System.lineSeparator());
            this.diffNodeList.forEach(d -> sb.append(d));
            return sb.toString();
        }

        @Override
        public _build addDiff(_diffNode d) {
            diffNodeList.add(d);
            return this;
        }
    }

    /**
     *
     *
     * @author Eric
     * @param <T> the diff node type
     * @param <_RN> the root node type
     */
    interface _differ<T, _RN extends _java._node> {

        /**
         *
         * @param left
         * @param right
         * @return
         */
        default _diff diff(T left, T right) {
            _nodePath _p = new _nodePath();

            //we dont know if
            _diffList md = null;

            if( left instanceof _java._node && right instanceof _java._node) {
                md = new _diffList((_java._node)left, (_java._node) right);
            } else{
                md = new _diffList(null, null);
            }
            diff(_p, md, null, null, left, right);
            return md;
        }

        default _diff diff(_nodePath path, _RN leftRoot, _RN rightRoot) {
            _diffList md = new _diffList(leftRoot, rightRoot);
            diff(path, md, leftRoot, rightRoot);
            return md;
        }

        //launch a diff using the left and right roots as the main roots to diff
        default _diff diff(_nodePath path, _build dt, _RN leftRoot, _RN rightRoot) {
            return diff(path, dt, leftRoot, rightRoot, (T) leftRoot, (T) rightRoot);
        }

        /**
         * diff the left and right nodes where leftParent and rightParent are the containing nodes
         *
         * @param <_PN> the parent _node type
         * @param path the current path to the entity
         * @param ds the mutable _difflist containing the current _diffNode s
         * @param _leftParent the parent of the left node
         * @param _rightParent the parent of the right node
         * @param left the left node/element/property
         * @param right the right node/element/property
         * @return the diffList with all diffs between the left and right nodes
         */
        <_PN extends _java._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, T left, T right);
    }

}
